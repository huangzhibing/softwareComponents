/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.salemanage.paytypedef.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.hqu.modules.salemanage.paytypedef.entity.PayTypeDef;
import com.hqu.modules.salemanage.paytypedef.mapper.PayTypeDefMapper;

import javax.annotation.Resource;

/**
 * 付款方式定义Service
 * @author M1ngz
 * @version 2018-05-07
 */
@Service
@Transactional(readOnly = true)
public class PayTypeDefService extends CrudService<PayTypeDefMapper, PayTypeDef> {

	@Resource
	private PayTypeDefMapper payTypeDefMapper;

	public PayTypeDef get(String id) {
		return super.get(id);
	}
	
	public List<PayTypeDef> findList(PayTypeDef payTypeDef) {
		return super.findList(payTypeDef);
	}
	
	public Page<PayTypeDef> findPage(Page<PayTypeDef> page, PayTypeDef payTypeDef) {
		return super.findPage(page, payTypeDef);
	}

	public String selectMax() {
		return payTypeDefMapper.selectMax();
	}

	@Transactional(readOnly = false)
	public void save(PayTypeDef payTypeDef) {
		super.save(payTypeDef);
	}
	
	@Transactional(readOnly = false)
	public void delete(PayTypeDef payTypeDef) {
		super.delete(payTypeDef);
	}
	
}