/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.purchasemanage.purplanstatis.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.hqu.modules.purchasemanage.purplanstatis.entity.PurPlanDetailStatis;
import com.hqu.modules.purchasemanage.purplanstatis.mapper.PurPlanDetailStatisMapper;

/**
 * 采购计划明细表Service
 * @author yxb
 * @version 2018-08-15
 */
@Service
@Transactional(readOnly = true)
public class PurPlanDetailStatisService extends CrudService<PurPlanDetailStatisMapper, PurPlanDetailStatis> {

	public PurPlanDetailStatis get(String id) {
		return super.get(id);
	}
	
	public List<PurPlanDetailStatis> findList(PurPlanDetailStatis purPlanDetailStatis) {
		return super.findList(purPlanDetailStatis);
	}
	
	public Page<PurPlanDetailStatis> findPage(Page<PurPlanDetailStatis> page, PurPlanDetailStatis purPlanDetailStatis) {
		dataRuleFilter(purPlanDetailStatis);
		purPlanDetailStatis.setPage(page);
		page.setList(mapper.findList(purPlanDetailStatis));
		//设置序号
		for(int serialNum=(page.getPageNo()-1)*page.getPageSize()+1,i = 0;i<page.getList().size();++serialNum,++i){
			page.getList().get(i).setSerialNum(serialNum);
		}
		return page;
	}
	
	@Transactional(readOnly = false)
	public void save(PurPlanDetailStatis purPlanDetailStatis) {
		super.save(purPlanDetailStatis);
	}
	
	@Transactional(readOnly = false)
	public void delete(PurPlanDetailStatis purPlanDetailStatis) {
		super.delete(purPlanDetailStatis);
	}
	
}