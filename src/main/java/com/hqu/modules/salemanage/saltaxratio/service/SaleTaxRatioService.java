/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.salemanage.saltaxratio.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.hqu.modules.salemanage.saltaxratio.entity.SaleTaxRatio;
import com.hqu.modules.salemanage.saltaxratio.mapper.SaleTaxRatioMapper;

import javax.xml.ws.Action;

/**
 * 销售税率定义Service
 * @author liujiachen
 * @version 2018-05-09
 */
@Service
@Transactional(readOnly = true)
public class SaleTaxRatioService extends CrudService<SaleTaxRatioMapper, SaleTaxRatio> {

	@Autowired
	SaleTaxRatioMapper saleTaxRatioMapper;

	public SaleTaxRatio get(String id) {
		return super.get(id);
	}
	
	public List<SaleTaxRatio> findList(SaleTaxRatio saleTaxRatio) {
		return super.findList(saleTaxRatio);
	}
	
	public Page<SaleTaxRatio> findPage(Page<SaleTaxRatio> page, SaleTaxRatio saleTaxRatio) {
		return super.findPage(page, saleTaxRatio);
	}
	
	@Transactional(readOnly = false)
	public void save(SaleTaxRatio saleTaxRatio) {
		super.save(saleTaxRatio);
	}
	
	@Transactional(readOnly = false)
	public void delete(SaleTaxRatio saleTaxRatio) {
		super.delete(saleTaxRatio);
	}

	public String getMaxSalTaxRatioID(){return saleTaxRatioMapper.getMaxSalTaxRatioID();}
}