/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.workshopmanage.mpsplanquery.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.hqu.modules.workshopmanage.mpsplanquery.entity.MpsPlanQuery;
import com.hqu.modules.workshopmanage.mpsplanquery.mapper.MpsPlanQueryMapper;

/**
 * 主生产计划查询Service
 * @author yxb
 * @version 2018-09-04
 */
@Service
@Transactional(readOnly = true)
public class MpsPlanQueryService extends CrudService<MpsPlanQueryMapper, MpsPlanQuery> {

	public MpsPlanQuery get(String id) {
		return super.get(id);
	}
	
	public List<MpsPlanQuery> findList(MpsPlanQuery mpsPlanQuery) {
		return super.findList(mpsPlanQuery);
	}
	
	public Page<MpsPlanQuery> findPage(Page<MpsPlanQuery> page, MpsPlanQuery mpsPlanQuery) {
		return super.findPage(page, mpsPlanQuery);
	}
	
	@Transactional(readOnly = false)
	public void save(MpsPlanQuery mpsPlanQuery) {
		super.save(mpsPlanQuery);
	}
	
	@Transactional(readOnly = false)
	public void delete(MpsPlanQuery mpsPlanQuery) {
		super.delete(mpsPlanQuery);
	}
	
}