/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.inventorymanage.use.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.hqu.modules.inventorymanage.use.entity.Use;
import com.hqu.modules.inventorymanage.use.mapper.UseMapper;

/**
 * 用途定义Service
 * @author lmy
 * @version 2018-04-14
 */
@Service
@Transactional(readOnly = true)
public class UseService extends CrudService<UseMapper, Use> {

	public Use get(String id) {
		return super.get(id);
	}
	
	public List<Use> findList(Use use) {
		return super.findList(use);
	}
	
	public Page<Use> findPage(Page<Use> page, Use use) {
		return super.findPage(page, use);
	}
	
	@Transactional(readOnly = false)
	public void save(Use use) {
		super.save(use);
	}
	
	@Transactional(readOnly = false)
	public void delete(Use use) {
		super.delete(use);
	}
	
}