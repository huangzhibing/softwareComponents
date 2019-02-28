/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.purchasemanage.relations.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.hqu.modules.purchasemanage.relations.entity.Relations;
import com.hqu.modules.purchasemanage.relations.mapper.RelationsMapper;

/**
 * 关系描述Service
 * @author syc
 * @version 2018-04-30
 */
@Service
@Transactional(readOnly = true)
public class RelationsService extends CrudService<RelationsMapper, Relations> {

	public Relations get(String id) {
		return super.get(id);
	}
	
	public List<Relations> findList(Relations relations) {
		return super.findList(relations);
	}
	
	public Page<Relations> findPage(Page<Relations> page, Relations relations) {
		return super.findPage(page, relations);
	}
	
	@Transactional(readOnly = false)
	public void save(Relations relations) {
		super.save(relations);
	}
	
	@Transactional(readOnly = false)
	public void delete(Relations relations) {
		super.delete(relations);
	}
	
}