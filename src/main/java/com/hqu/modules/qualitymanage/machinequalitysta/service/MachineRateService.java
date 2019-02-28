/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.qualitymanage.machinequalitysta.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.hqu.modules.qualitymanage.eInfoBoard.entity.eInfoPurStatus;
import com.hqu.modules.qualitymanage.eInfoBoard.mapper.eInfoPurStatusMapper;
import com.hqu.modules.qualitymanage.qmreport.entity.QmReportRSn;
import com.hqu.modules.qualitymanage.qmreport.mapper.QmReportRSnMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.hqu.modules.qualitymanage.machinequalitysta.entity.MachineRate;
import com.hqu.modules.qualitymanage.machinequalitysta.mapper.MachineRateMapper;

/**
 * 整机良率统计（电子看板）Service
 * @author yxb
 * @version 2018-11-17
 */
@Service
@Transactional(readOnly = true)
public class MachineRateService extends CrudService<MachineRateMapper, MachineRate> {

	//统计起始时间的设置，例：早上8点开始每个小时 截止17-18点
	private static final double dayBeginTime=8;
	private static final double dayEndTime=18;

	@Autowired
	private eInfoPurStatusMapper eInfoPurStatusMapper;
	@Autowired
	private QmReportRSnMapper qmReportRSnMapper;

	public MachineRate get(String id) {
		return super.get(id);
	}
	
	public List<MachineRate> findList(MachineRate machineRate) {
		return super.findList(machineRate);
	}
	
	public Page<MachineRate> findPage(Page<MachineRate> page, MachineRate machineRate) {

		return super.findPage(page, machineRate);
	}

	@Transactional(readOnly = false)
	public void machineRateStaByDay(MachineRate machineRate) {
		SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat simpleDateTimeFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date now=new Date();
		if(machineRate.getBeginTimeScope()!=null&&!simpleDateFormat.format(machineRate.getBeginTimeScope()).equals(simpleDateFormat.format(now))){
			//查询历史某天的统计数据
			String day = simpleDateFormat.format(machineRate.getBeginTimeScope());
			for(double time = dayBeginTime;time < dayEndTime;time++){

			}
		}else{
			String day = simpleDateFormat.format(now);//取出当天日期 yyyy-MM-dd
			String deleteTodaySql="DELETE FROM qms_machine_rate WHERE time_type='D' and time_scope LIKE '"+day+"%'";
			super.mapper.execDeleteSql(deleteTodaySql);

			for(double time = dayBeginTime;time < dayEndTime;time++){
				try {
					MachineRate machineRate1 = new MachineRate();
					Date begin=simpleDateTimeFormat.parse(day+" "+(int)time+":00:00");
					Date end=simpleDateTimeFormat.parse(day+" "+(int)(time+1)+":00:00");
					machineRate1.setTimeScope(begin);
					machineRate1.setTimeType("D");
					eInfoPurStatus eInfo = new eInfoPurStatus();
					eInfo.setStartBeginDate(begin);
					eInfo.setStartEndDate(end);

					//统计安规产出量及不合格数量
					eInfo.setCheckprj("安规");
					machineRate1=getMachineRateSta(machineRate1, eInfo);
					//统计功能老化产出量及不合格数量
					eInfo.setCheckprj("功能老化");
					machineRate1=getMachineRateSta(machineRate1, eInfo);
					//统计激活产出量及不合格数量
					eInfo.setCheckprj("激活");
					machineRate1=getMachineRateSta(machineRate1, eInfo);
					//统计包装产出量及不合格数量
					eInfo.setCheckprj("包装");
					machineRate1=getMachineRateSta(machineRate1, eInfo);
					super.save(machineRate1);
				} catch (ParseException e) {
					System.out.print("整机良率统计_______________________________error"+e.getMessage());
					e.printStackTrace();
				}

			}
		}

	}

	//machineRateStaByDay的子方法
	private MachineRate getMachineRateSta(MachineRate machineRate, eInfoPurStatus eInfo) {
		List<eInfoPurStatus> eInfoPurStatusList=eInfoPurStatusMapper.findList(eInfo);
		int failCount=0;
		QmReportRSn qmReportRSn = new QmReportRSn();
		for(eInfoPurStatus infoPurStatus:eInfoPurStatusList){//查找每个整机检验单安规过程有没有对应的质检问题记录，只有记录大于0则判定存在不合格。
			qmReportRSn.setReportId(eInfo.getReportId());
			List<QmReportRSn> qmReportRSns = qmReportRSnMapper.findList(qmReportRSn);
			if(qmReportRSns.size()!=0){
				failCount++;
			}
		}
		if("安规".equals(eInfo.getCheckprj())) {
			if (eInfoPurStatusList.size() != 0) {
				machineRate.setAgNum(eInfoPurStatusList.size());
				double ratio = (eInfoPurStatusList.size() - failCount) / eInfoPurStatusList.size();
				machineRate.setAgRatio(ratio);
			} else {
				machineRate.setAgNum(0);
				machineRate.setAgRatio(0.0);
			}
		}else if("功能老化".equals(eInfo.getCheckprj())) {
			if (eInfoPurStatusList.size() != 0) {
				machineRate.setLhNum(eInfoPurStatusList.size());
				double ratio = (eInfoPurStatusList.size() - failCount) / eInfoPurStatusList.size();
				machineRate.setLhRatio(ratio);
			} else {
				machineRate.setLhNum(0);
				machineRate.setLhRatio(0.0);
			}
		}else if("激活".equals(eInfo.getCheckprj())) {
			if (eInfoPurStatusList.size() != 0) {
				machineRate.setJhNum(eInfoPurStatusList.size());
				double ratio = (eInfoPurStatusList.size() - failCount) / eInfoPurStatusList.size();
				machineRate.setJhRatio(ratio);
			} else {
				machineRate.setJhNum(0);
				machineRate.setJhRatio(0.0);
			}
		}else if("包装".equals(eInfo.getCheckprj())) {
			if (eInfoPurStatusList.size() != 0) {
				machineRate.setBzNum(eInfoPurStatusList.size());
				double ratio = (eInfoPurStatusList.size() - failCount) / eInfoPurStatusList.size();
				machineRate.setBzRatio(ratio);
			} else {
				machineRate.setBzNum(0);
				machineRate.setBzRatio(0.0);
			}
		}
		return machineRate;
	}

	@Transactional(readOnly = false)
	public void save(MachineRate machineRate) {
		super.save(machineRate);
	}
	
	@Transactional(readOnly = false)
	public void delete(MachineRate machineRate) {
		super.delete(machineRate);
	}
	
}