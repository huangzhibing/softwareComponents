/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.purchasemanage.purplan.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.hqu.modules.purchasemanage.purplan.entity.PlanRelations;
import com.hqu.modules.purchasemanage.purplan.mapper.PlanRelationsMapper;

/**
 * 关系描述Service
 * @author yxb
 * @version 2018-05-14
 */
@Service
@Transactional(readOnly = true)
public class PlanRelationsService extends CrudService<PlanRelationsMapper, PlanRelations> {

	@Autowired
	public PlanRelationsMapper planRelationsMapper;

	public PlanRelations get(String id) {
		return super.get(id);
	}
	
	public List<PlanRelations> findList(PlanRelations planRelations) {
		return super.findList(planRelations);
	}
	
	public Page<PlanRelations> findPage(Page<PlanRelations> page, PlanRelations planRelations) {
		return super.findPage(page, planRelations);
	}
	
	@Transactional(readOnly = false)
	public void save(PlanRelations planRelations) {
		super.save(planRelations);
	}
	
	@Transactional(readOnly = false)
	public void delete(PlanRelations planRelations) {
		super.delete(planRelations);
	}

	public void deleteByBillNum(PlanRelations planRelations){planRelationsMapper.deleteByBillNum(planRelations); }

	public void deleteByBillNumAndSerialNum(PlanRelations planRelations){planRelationsMapper.deleteByBillNumAndSerialNum(planRelations); }
}