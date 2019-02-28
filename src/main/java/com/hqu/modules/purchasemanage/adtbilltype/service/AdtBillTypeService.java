/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.purchasemanage.adtbilltype.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.hqu.modules.purchasemanage.adtbilltype.entity.AdtBillType;
import com.hqu.modules.purchasemanage.adtbilltype.mapper.AdtBillTypeMapper;

/**
 * 单据类型表Service
 * @author ckw
 * @version 2018-05-08
 */
@Service
@Transactional(readOnly = true)
public class AdtBillTypeService extends CrudService<AdtBillTypeMapper, AdtBillType> {

	public AdtBillType get(String id) {
		return super.get(id);
	}
	
	public List<AdtBillType> findList(AdtBillType adtBillType) {
		return super.findList(adtBillType);
	}
	
	public Page<AdtBillType> findPage(Page<AdtBillType> page, AdtBillType adtBillType) {
		return super.findPage(page, adtBillType);
	}
	
	@Transactional(readOnly = false)
	public void save(AdtBillType adtBillType) {
		super.save(adtBillType);
	}
	
	@Transactional(readOnly = false)
	public void delete(AdtBillType adtBillType) {
		super.delete(adtBillType);
	}
	
}