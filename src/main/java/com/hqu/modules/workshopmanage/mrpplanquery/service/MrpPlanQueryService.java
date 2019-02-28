/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.workshopmanage.mrpplanquery.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.hqu.modules.workshopmanage.mrpplanquery.entity.MrpPlanQuery;
import com.hqu.modules.workshopmanage.mrpplanquery.mapper.MrpPlanQueryMapper;

/**
 * 物料需求计划查询Service
 * @author yxb
 * @version 2018-09-04
 */
@Service
@Transactional(readOnly = true)
public class MrpPlanQueryService extends CrudService<MrpPlanQueryMapper, MrpPlanQuery> {

	public MrpPlanQuery get(String id) {
		return super.get(id);
	}
	
	public List<MrpPlanQuery> findList(MrpPlanQuery mrpPlanQuery) {
		return super.findList(mrpPlanQuery);
	}
	
	public Page<MrpPlanQuery> findPage(Page<MrpPlanQuery> page, MrpPlanQuery mrpPlanQuery) {
		return super.findPage(page, mrpPlanQuery);
	}
	
	@Transactional(readOnly = false)
	public void save(MrpPlanQuery mrpPlanQuery) {
		super.save(mrpPlanQuery);
	}
	
	@Transactional(readOnly = false)
	public void delete(MrpPlanQuery mrpPlanQuery) {
		super.delete(mrpPlanQuery);
	}
	
}