/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.purchasemanage.paymode.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.hqu.modules.purchasemanage.paymode.entity.PayMode;
import com.hqu.modules.purchasemanage.paymode.mapper.PayModeMapper;

/**
 * 付款方式定义Service
 * @author 石豪迈
 * @version 2018-04-14
 */
@Service
@Transactional(readOnly = true)
public class PayModeService extends CrudService<PayModeMapper, PayMode> {
	@Autowired
	PayModeMapper payModeMapper;

	public PayMode get(String id) {
		return super.get(id);
	}
	
	public List<PayMode> findList(PayMode payMode) {
		return super.findList(payMode);
	}
	
	public Page<PayMode> findPage(Page<PayMode> page, PayMode payMode) {
		String orderBy=page.getOrderBy();
		if(orderBy!=null&&"".equals(orderBy)){
			page.setOrderBy("paymodeid");
		}		
		return super.findPage(page, payMode);
	}
	
	@Transactional(readOnly = false)
	public void save(PayMode payMode) {
		super.save(payMode);
	}
	
	@Transactional(readOnly = false)
	public void delete(PayMode payMode) {
		super.delete(payMode);
	}
	public String getMaxPaymodeid(){
		return payModeMapper.getMaxPaymodeid();
	}
	
}