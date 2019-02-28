/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.salemanage.blnctypedef.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.hqu.modules.salemanage.blnctypedef.entity.BlncTypeDef;
import com.hqu.modules.salemanage.blnctypedef.mapper.BlncTypeDefMapper;

/**
 * 结算方式定义Service
 * @author M1ngz
 * @version 2018-05-07
 */
@Service
@Transactional(readOnly = true)
public class BlncTypeDefService extends CrudService<BlncTypeDefMapper, BlncTypeDef> {

	public BlncTypeDef get(String id) {
		return super.get(id);
	}
	
	public List<BlncTypeDef> findList(BlncTypeDef blncTypeDef) {
		return super.findList(blncTypeDef);
	}
	
	public Page<BlncTypeDef> findPage(Page<BlncTypeDef> page, BlncTypeDef blncTypeDef) {
		return super.findPage(page, blncTypeDef);
	}
	
	@Transactional(readOnly = false)
	public void save(BlncTypeDef blncTypeDef) {
		super.save(blncTypeDef);
	}
	
	@Transactional(readOnly = false)
	public void delete(BlncTypeDef blncTypeDef) {
		super.delete(blncTypeDef);
	}
	
}