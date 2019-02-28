/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.workshopmanage.processmaterialdetail.service;

import java.util.List;


import com.hqu.modules.workshopmanage.processroutinedetail.service.ProcessRoutineDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.common.utils.StringUtils;
//import com.hqu.modules.workshopmanage.processmaterialdetail.entity.ProcessRoutineDetail;
//import com.hqu.modules.workshopmanage.processmaterialdetail.mapper.ProcessRoutineDetailMapper;
import com.hqu.modules.workshopmanage.processmaterialdetail.entity.ProcessMaterialDetail;
import com.hqu.modules.workshopmanage.processmaterialdetail.mapper.ProcessMaterialDetailMapper;
import com.hqu.modules.workshopmanage.processroutinedetail.entity.ProcessRoutineDetail;
import com.hqu.modules.workshopmanage.processroutinedetail.mapper.ProcessRoutineDetailMapper;

/**
 * 车间物料安装明细Service
 * @author chen
 * @version 2018-08-10
 */
@Service
@Transactional(readOnly = true)
public class ProcessMaterialDetailService extends CrudService<ProcessRoutineDetailMapper, ProcessRoutineDetail> {

	@Autowired
	private ProcessMaterialDetailMapper processMaterialDetailMapper;
	@Autowired
	private ProcessRoutineDetailMapper processRoutineDetailMapper;
	
	public ProcessRoutineDetail get(String id) {
		ProcessRoutineDetail processRoutineDetail = super.get(id);
		processRoutineDetail.setProcessMaterialDetailList(processMaterialDetailMapper.findList(new ProcessMaterialDetail(processRoutineDetail)));
		return processRoutineDetail;
	}
	
	public List<ProcessRoutineDetail> findList(ProcessRoutineDetail processRoutineDetail) {
		return super.findList(processRoutineDetail);
	}

	//物料安装明细录入页面数据（带有框架配置的仅显示当前负责人工艺权限）
	public Page<ProcessRoutineDetail> findAssemblePage(Page<ProcessRoutineDetail> page, ProcessRoutineDetail processRoutineDetail) {
		dataRuleFilter(processRoutineDetail);
		processRoutineDetail.setPage(page);
		page.setList(mapper.findAssembleList(processRoutineDetail));
		return page;
	}

	//物料安装明细查询
	public Page<ProcessRoutineDetail> findPage(Page<ProcessRoutineDetail> page, ProcessRoutineDetail processRoutineDetail) {
		dataRuleFilter(processRoutineDetail);
		processRoutineDetail.setPage(page);
		page.setList(processRoutineDetailMapper.findListWithMaterialDetail(processRoutineDetail));
		//查找子表中是否存在零件品质异常记录
		for(ProcessRoutineDetail processRoutineDetail1:page.getList()) {
			if("C".equals(processRoutineDetail1.getBillstate())){
				processRoutineDetail1.setHasQualityPro("-");
				continue;
			}
			List<ProcessMaterialDetail> processMaterialDetails=processMaterialDetailMapper.findList(new ProcessMaterialDetail(processRoutineDetail1));
			processRoutineDetail1.setHasQualityPro("N");
			for (ProcessMaterialDetail processMaterialDetail:processMaterialDetails){
				if("Y".equals(processMaterialDetail.getHasQualityPro())){
					processRoutineDetail1.setHasQualityPro("Y");
					break;
				}
			}
		}
		return page;
	}
	
	@Transactional(readOnly = false)
	public void save(ProcessRoutineDetail processRoutineDetail) {
		super.save(processRoutineDetail);
		for (ProcessMaterialDetail processMaterialDetail : processRoutineDetail.getProcessMaterialDetailList()){
			if (processMaterialDetail.getId() == null){
				continue;
			}
			if (ProcessMaterialDetail.DEL_FLAG_NORMAL.equals(processMaterialDetail.getDelFlag())){
				if (StringUtils.isBlank(processMaterialDetail.getId())){
					processMaterialDetail.preInsert();
					processMaterialDetail.setRoutineBillNo(processRoutineDetail.getRoutinebillno());//设置外键值
					processMaterialDetail.setSeqNo(processRoutineDetail.getSeqno());
					processMaterialDetail.setRoutingCode(processRoutineDetail.getRoutingcode());
					processMaterialDetailMapper.insert(processMaterialDetail);
				}else{
					processMaterialDetail.preUpdate();
					processMaterialDetailMapper.update(processMaterialDetail);
				}
			}else{
				processMaterialDetailMapper.delete(processMaterialDetail);
			}
		}
	}
	@Transactional(readOnly = false)
	public void delete(ProcessRoutineDetail processRoutineDetail) {
		super.delete(processRoutineDetail);
		processMaterialDetailMapper.delete(new ProcessMaterialDetail(processRoutineDetail));
	}
	
}