/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.purchasemanage.rollplannewquery.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.hqu.modules.purchasemanage.rollplannewquery.entity.RollPlanNew;
import com.hqu.modules.purchasemanage.rollplannewquery.mapper.RollPlanNewMapper;

/**
 * 滚动计划查询Service
 * @author yangxianbang
 * @version 2018-05-08
 */
@Service
@Transactional(readOnly = true)
public class RollPlanNewService extends CrudService<RollPlanNewMapper, RollPlanNew> {

	public RollPlanNew get(String id) {
		return super.get(id);
	}
	
	public List<RollPlanNew> findList(RollPlanNew rollPlanNew) {
		return super.findList(rollPlanNew);
	}
	
	public Page<RollPlanNew> findPage(Page<RollPlanNew> page, RollPlanNew rollPlanNew) {
		return super.findPage(page, rollPlanNew);
	}

	public Page<RollPlanNew> findDataForPlanPage(Page<RollPlanNew> page, RollPlanNew rollPlanNew) {
		dataRuleFilter(rollPlanNew);
		rollPlanNew.setOpFlag("0");
		rollPlanNew.setPage(page);
		page.setList(mapper.findListForPlan(rollPlanNew));
		for(RollPlanNew r:page.getList()){             //把所有数量字段值为空的都置为0
			if(r.getApplyQty()==null){
				r.setApplyQty(0);
			}
			if(r.getInvQty()==null) {
				r.setInvQty(0.0);
			}
			if(r.getCostPrice()==null){
				r.setCostPrice("0");
			}
			if(r.getSafetyQty()==null){
				r.setSafetyQty(0.0);
			}
		}
		return page;
	}
	
	@Transactional(readOnly = false)
	public void save(RollPlanNew rollPlanNew) {
		super.save(rollPlanNew);
	}
	
	@Transactional(readOnly = false)
	public void delete(RollPlanNew rollPlanNew) {
		super.delete(rollPlanNew);
	}


}