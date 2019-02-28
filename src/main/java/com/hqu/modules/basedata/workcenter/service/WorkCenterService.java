/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.basedata.workcenter.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.hqu.modules.basedata.workcenter.entity.WorkCenter;
import com.hqu.modules.basedata.workcenter.mapper.WorkCenterMapper;

/**
 * 工作中心Service
 * @author dongqida
 * @version 2018-04-05
 */
@Service
@Transactional(readOnly = true)
public class WorkCenterService extends CrudService<WorkCenterMapper, WorkCenter> {

	@Autowired
	private WorkCenterMapper workCenterMapper;
	public WorkCenter get(String id) {
		return super.get(id);
	}
	
	public List<WorkCenter> findList(WorkCenter workCenter) {
		return super.findList(workCenter);
	}
	
	public Page<WorkCenter> findPage(Page<WorkCenter> page, WorkCenter workCenter) {
		return super.findPage(page, workCenter);
	}
	
	@Transactional(readOnly = false)
	public void save(WorkCenter workCenter) {
		super.save(workCenter);
	}
	
	@Transactional(readOnly = false)
	public void delete(WorkCenter workCenter) {
		super.delete(workCenter);
	}
	
	public String findMaxCode() {
		return workCenterMapper.findMaxCode();
	}
}