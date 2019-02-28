/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.purchasemanage.plantype.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.hqu.modules.purchasemanage.plantype.entity.PlanType;
import com.hqu.modules.purchasemanage.plantype.mapper.PlanTypeMapper;

/**
 * PlanTypeService
 * @author 方翠虹
 * @version 2018-04-14
 */
@Service
@Transactional(readOnly = true)
public class PlanTypeService extends CrudService<PlanTypeMapper, PlanType> {
	@Autowired
	PlanTypeMapper planTypeMapper;
	

	public PlanType get(String id) {
		return super.get(id);
	}
	
	public List<PlanType> findList(PlanType planType) {
		return super.findList(planType);
	}
	
	public Page<PlanType> findPage(Page<PlanType> page, PlanType planType) {
		String orderBy=page.getOrderBy();
		if(orderBy!=null&&"".equals(orderBy)){
			page.setOrderBy("plantypeId");
		}		
		return super.findPage(page, planType);
	}
	
	@Transactional(readOnly = false)
	public void save(PlanType planType) {
		super.save(planType);
	}
	
	@Transactional(readOnly = false)
	public void delete(PlanType planType) {
		super.delete(planType);
	}
	public String getMaxPlantypeId(){
		return planTypeMapper.getMaxPlantypeId();
	}
	
	
}