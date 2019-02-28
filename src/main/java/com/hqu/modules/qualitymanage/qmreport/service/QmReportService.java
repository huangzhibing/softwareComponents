/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.qualitymanage.qmreport.service;

import java.util.ArrayList;
import java.util.List;

import com.hqu.modules.basedata.item.entity.Item;
import com.hqu.modules.qualitymanage.qmreport.entity.InvCheckDetailQmReport;
import com.hqu.modules.qualitymanage.qmreport.mapper.InvCheckDetailQmReportMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.common.utils.StringUtils;
import com.hqu.modules.qualitymanage.purreport.service.PurReportQmService;
import com.hqu.modules.qualitymanage.qmreport.entity.QmReport;
import com.hqu.modules.qualitymanage.qmreport.mapper.QmReportMapper;
import com.hqu.modules.qualitymanage.qmreport.entity.QmReportRSn;
import com.hqu.modules.qualitymanage.qmreport.mapper.QmReportRSnMapper;

/**
 * 质量问题报告Service
 * @author yangxianbang
 * @version 2018-04-08
 */
@Service
@Transactional(readOnly = true)
public class QmReportService extends CrudService<QmReportMapper, QmReport> {

	
	@Autowired
	private QmReportRSnMapper qmReportRSnMapper;
	
	@Autowired
	private PurReportQmService purReportService;
	@Autowired
	private InvCheckDetailQmReportMapper invCheckDetailQmReportMapper;
	
	public QmReport get(String id) {
		QmReport qmReport = super.get(id);
		qmReport.setQmReportRSnList(qmReportRSnMapper.findList(new QmReportRSn(qmReport)));
		return qmReport;
	}
	
	public List<QmReport> findList(QmReport qmReport) {
		return super.findList(qmReport);
	}
	
	public Page<QmReport> findPage(Page<QmReport> page, QmReport qmReport) {
		return super.findPage(page, qmReport);
	}


	//往到货通知单子表写退货标志
	private void writeInvCheckFlag(QmReport qmReport){
		if(qmReport.getMhandlingName().getMhandlingname().contains("退货")){
			for (QmReportRSn qmReportRSn : qmReport.getQmReportRSnList()){
				if (qmReportRSn.getId() == null){
					continue;
				}
				if (QmReportRSn.DEL_FLAG_NORMAL.equals(qmReportRSn.getDelFlag())){
					InvCheckDetailQmReport invCheckDetailQmReport=new InvCheckDetailQmReport();
					invCheckDetailQmReport.setBillnum(qmReportRSn.getBillNum());
					Item item=new Item();
					item.setCode(qmReportRSn.getObjCode());
					invCheckDetailQmReport.setItem(item);
					List<InvCheckDetailQmReport> invCheckDetailQmReportList=invCheckDetailQmReportMapper.findList(invCheckDetailQmReport);
					for(InvCheckDetailQmReport icd:invCheckDetailQmReportList){//查找对应检验对象在采购到货通知单中的物料
						if("1".equals(icd.getBackFlag())){  //已经设置过退货标志的跳过 （防止一个到货单有多个相同的物料）
							continue;
						}else {
							icd.setBackFlag("1");
							invCheckDetailQmReportMapper.update(icd);
							break;
						}
					}
				}
			}

		}
	}

	@Transactional(readOnly = false)
	public void save(QmReport qmReport) {
		if("采购".equals(qmReport.getQmType())){
			if("已提交".equals(qmReport.getState())){
				writeInvCheckFlag(qmReport);  //为采购到货 且提交状态，把不合格要退货的检验对象 往到货表写退货标志
			}
		}

		qMatterProcess(qmReport);

		super.save(qmReport);
		for (QmReportRSn qmReportRSn : qmReport.getQmReportRSnList()){
			if("已提交".equals(qmReport.getState())){   //提交状态时把不合格对象的退库状态设为1
				qmReportRSn.setIsRetreat("1");
			}
			if (qmReportRSn.getId() == null){
				continue;
			}
			if (QmReportRSn.DEL_FLAG_NORMAL.equals(qmReportRSn.getDelFlag())){
				if (StringUtils.isBlank(qmReportRSn.getId())){
					qmReportRSn.setQmReportId(qmReport);
					qmReportRSn.preInsert();
					qmReportRSnMapper.insert(qmReportRSn);
				}else{
					qmReportRSn.preUpdate();
					qmReportRSnMapper.update(qmReportRSn);
				}
			}else{
				qmReportRSnMapper.delete(qmReportRSn);
			}
		}
	}

	private void qMatterProcess(QmReport qmReport) {
		int flag=-1;
		if("编辑中".equals(qmReport.getState())){
			flag=1;
		}else if("已提交".equals(qmReport.getState())){
			flag=2;
		}
		if(!qmReport.getId().equals("")) {
		//更改检验对象标志前的归零预处理  @author xianbang
			QmReport history= get(qmReport.getId());
			
			if(history!=null) {
				List<String> purReportRSnIds=new ArrayList<String>();
				for (QmReportRSn qmReportRSn : history.getQmReportRSnList()) {
					purReportRSnIds.add(qmReportRSn.getPurReportRSnId());
				}
				purReportService.preDealQMatter(purReportRSnIds);
			}
		}
		//检验对象标志处理 @author xianbang
		List<String> purReportRSnIds=new ArrayList<String>();
		List<String> purReportRSnDelIds=new ArrayList<String>();
		for (QmReportRSn qmReportRSn : qmReport.getQmReportRSnList()) {
			if(qmReportRSn.getDelFlag().equals("1")) {
				purReportRSnDelIds.add(qmReportRSn.getPurReportRSnId());
			}else {
				purReportRSnIds.add(qmReportRSn.getPurReportRSnId());
			}
		}
		purReportService.dealQMatter(purReportRSnIds,purReportRSnDelIds,flag);
		//
	}
	
	@Transactional(readOnly = false)
	public void delete(QmReport qmReport) {
		super.delete(qmReport);
		//删除时把已处理对象的标志归零 @author xianbang
		List<QmReportRSn> qmRSns=qmReportRSnMapper.findList(new QmReportRSn(qmReport));
		List<String> purReportRSnIds=new ArrayList<String>();
		List<String> purReportRSnDelIds=new ArrayList<String>();
		for(QmReportRSn qmRSn : qmRSns) {
			purReportRSnIds.add(qmRSn.getPurReportRSnId());
		}
		purReportService.preDealQMatter(purReportRSnIds);
		purReportService.dealQMatter(purReportRSnIds,purReportRSnDelIds, 0);
		//
		qmReportRSnMapper.delete(new QmReportRSn(qmReport));
	}
	
}