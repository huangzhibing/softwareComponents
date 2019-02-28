/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.purchasemanage.taxratio.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.hqu.modules.basedata.accounttype.mapper.AccountTypeMapper;
import com.hqu.modules.purchasemanage.taxratio.entity.TaxRatio;
import com.hqu.modules.purchasemanage.taxratio.mapper.TaxRatioMapper;

/**
 * 采购税率定义Service
 * @author luyumiao
 * @version 2018-04-25
 */
@Service
@Transactional(readOnly = true)
public class TaxRatioService extends CrudService<TaxRatioMapper, TaxRatio> {

	@Autowired
	TaxRatioMapper taxRatioMapper;
	
	public TaxRatio get(String id) {
		return super.get(id);
	}
	
	public List<TaxRatio> findList(TaxRatio taxRatio) {
		return super.findList(taxRatio);
	}
	
	public Page<TaxRatio> findPage(Page<TaxRatio> page, TaxRatio taxRatio) {
		return super.findPage(page, taxRatio);
	}
	
	@Transactional(readOnly = false)
	public void save(TaxRatio taxRatio) {
		super.save(taxRatio);
	}
	
	@Transactional(readOnly = false)
	public void delete(TaxRatio taxRatio) {
		super.delete(taxRatio);
	}
	
	public String getMaxTaxratioId(){
		return taxRatioMapper.getMaxTaxratioId();
	}
	
}