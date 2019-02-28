/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.purchasemanage.transtype.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.hqu.modules.purchasemanage.transtype.entity.TranStype;
import com.hqu.modules.purchasemanage.transtype.mapper.TranStypeMapper;

/**
 * 运输方式定义Service
 * @author 石豪迈
 * @version 2018-04-14
 */
@Service
@Transactional(readOnly = true)
public class TranStypeService extends CrudService<TranStypeMapper, TranStype> {
	@Autowired
	TranStypeMapper tranStypeMapper;

	public TranStype get(String id) {
		return super.get(id);
	}
	
	public List<TranStype> findList(TranStype tranStype) {
		return super.findList(tranStype);
	}
	
	public Page<TranStype> findPage(Page<TranStype> page, TranStype tranStype) {
		String orderBy=page.getOrderBy();
		if(orderBy!=null&&"".equals(orderBy)){
			page.setOrderBy("transtypeid");
		}		
		return super.findPage(page, tranStype);
	}
	
	@Transactional(readOnly = false)
	public void save(TranStype tranStype) {
		super.save(tranStype);
	}
	
	@Transactional(readOnly = false)
	public void delete(TranStype tranStype) {
		super.delete(tranStype);
	}
	public String getMaxTranstypeid(){
		return tranStypeMapper.getMaxTranstypeid();
	}
	
}