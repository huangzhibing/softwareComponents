/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.purchasemanage.suptype.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.hqu.modules.purchasemanage.suptype.entity.SupType;
import com.hqu.modules.purchasemanage.suptype.mapper.SupTypeMapper;

/**
 * 供应商类别定义Service
 * @author tyo
 * @version 2018-04-18
 */
@Service
@Transactional(readOnly = true)
public class SupTypeService extends CrudService<SupTypeMapper, SupType> {

	public SupType get(String id) {
		return super.get(id);
	}
	
	public List<SupType> findList(SupType supType) {
		return super.findList(supType);
	}
	
	public Page<SupType> findPage(Page<SupType> page, SupType supType) {
		String orderBy=page.getOrderBy();
		if(orderBy!=null&&"".equals(orderBy)){
			page.setOrderBy("suptypeId");
		}		
		return super.findPage(page, supType);
	}
	
	@Transactional(readOnly = false)
	public void save(SupType supType) {
		super.save(supType);
	}
	
	@Transactional(readOnly = false)
	public void delete(SupType supType) {
		super.delete(supType);
	}
	
}