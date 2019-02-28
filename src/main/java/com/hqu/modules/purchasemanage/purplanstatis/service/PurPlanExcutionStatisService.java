/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.purchasemanage.purplanstatis.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.hqu.modules.purchasemanage.purplanstatis.entity.PurPlanExcutionStatis;
import com.hqu.modules.purchasemanage.purplanstatis.mapper.PurPlanExcutionStatisMapper;

/**
 * 采购计划执行情况表Service
 * @author yxb
 * @version 2018-08-19
 */
@Service
@Transactional(readOnly = true)
public class PurPlanExcutionStatisService extends CrudService<PurPlanExcutionStatisMapper, PurPlanExcutionStatis> {

	public PurPlanExcutionStatis get(String id) {
		return super.get(id);
	}
	
	public List<PurPlanExcutionStatis> findList(PurPlanExcutionStatis purPlanExcutionStatis) {
		return super.findList(purPlanExcutionStatis);
	}
	
	public Page<PurPlanExcutionStatis> findPage(Page<PurPlanExcutionStatis> page, PurPlanExcutionStatis purPlanExcutionStatis) {
		dataRuleFilter(purPlanExcutionStatis);
		purPlanExcutionStatis.setPage(page);
		page.setList(mapper.findList(purPlanExcutionStatis));
		//设置序号
		for(int serialNum=(page.getPageNo()-1)*page.getPageSize()+1,i = 0;i<page.getList().size();++serialNum,++i){
			page.getList().get(i).setSerialNum(serialNum);
		}
		return page;
	}

	@Transactional(readOnly = false)
	public void save(PurPlanExcutionStatis purPlanExcutionStatis) {
		super.save(purPlanExcutionStatis);
	}
	
	@Transactional(readOnly = false)
	public void delete(PurPlanExcutionStatis purPlanExcutionStatis) {
		super.delete(purPlanExcutionStatis);
	}
	
}