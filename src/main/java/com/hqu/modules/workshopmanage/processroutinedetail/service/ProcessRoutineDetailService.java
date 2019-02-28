/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.workshopmanage.processroutinedetail.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.hqu.modules.workshopmanage.processroutinedetail.entity.ProcessRoutineDetail;
import com.hqu.modules.workshopmanage.processroutinedetail.mapper.ProcessRoutineDetailMapper;

/**
 * 加工路线单明细单Service
 * @author xhc
 * @version 2018-08-22
 */
@Service
@Transactional(readOnly = true)
public class ProcessRoutineDetailService extends CrudService<ProcessRoutineDetailMapper, ProcessRoutineDetail> {

	@Autowired
	private ProcessRoutineDetailMapper processRoutineDetailMapper;

	public ProcessRoutineDetail get(String id) {
		return super.get(id);
	}
	
	public List<ProcessRoutineDetail> findList(ProcessRoutineDetail processRoutineDetail) {
		return super.findList(processRoutineDetail);
	}

	public Page<ProcessRoutineDetail> findPage(Page<ProcessRoutineDetail> page, ProcessRoutineDetail processRoutineDetail) {
		return super.findPage(page, processRoutineDetail);
	}
	//查找最后所有产品的末道工艺信息
	public Page<ProcessRoutineDetail> findInvCheckPage(Page<ProcessRoutineDetail> page, ProcessRoutineDetail processRoutineDetail){
		dataRuleFilter(processRoutineDetail);
		processRoutineDetail.setPage(page);
		page.setList(processRoutineDetailMapper.findInvCheckList(processRoutineDetail));
		return page;
	}

	@Transactional(readOnly = false)
	public void save(ProcessRoutineDetail processRoutineDetail) {
		super.save(processRoutineDetail);
	}
	
	@Transactional(readOnly = false)
	public void delete(ProcessRoutineDetail processRoutineDetail) {
		super.delete(processRoutineDetail);
	}
	
}