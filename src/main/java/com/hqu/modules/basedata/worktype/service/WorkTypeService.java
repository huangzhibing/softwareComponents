/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.basedata.worktype.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.hqu.modules.basedata.worktype.entity.WorkType;
import com.hqu.modules.basedata.worktype.mapper.WorkTypeMapper;

/**
 * 工种定义Service
 * @author zb
 * @version 2018-04-07
 */
@Service
@Transactional(readOnly = true)
public class WorkTypeService extends CrudService<WorkTypeMapper, WorkType> {

	public WorkType get(String id) {
		return super.get(id);
	}

	public List<WorkType> findList(WorkType workType) {
		return super.findList(workType);
	}

	public Page<WorkType> findPage(Page<WorkType> page, WorkType workType) {
		return super.findPage(page, workType);
	}

	@Transactional(readOnly = false)
	public void save(WorkType workType) {
		super.save(workType);
	}

	@Transactional(readOnly = false)
	public void delete(WorkType workType) {
		super.delete(workType);
	}

	public Integer getCodeNum(String workTypeCode) {
		return mapper.getCodeNum(workTypeCode);
	}
}