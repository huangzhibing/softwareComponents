/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.purchasemanage.contractmain.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.hqu.modules.purchasemanage.contractmain.entity.ConRelations;
import com.hqu.modules.purchasemanage.contractmain.mapper.ConRelationsMapper;

/**
 * 关系描述Service
 * @author ltq
 * @version 2018-05-17
 */
@Service
@Transactional(readOnly = true)
public class ConRelationsService extends CrudService<ConRelationsMapper, ConRelations> {

	public ConRelations get(String id) {
		return super.get(id);
	}
	
	public List<ConRelations> findList(ConRelations conRelations) {
		return super.findList(conRelations);
	}
	
	public Page<ConRelations> findPage(Page<ConRelations> page, ConRelations conRelations) {
		return super.findPage(page, conRelations);
	}
	
	@Transactional(readOnly = false)
	public void save(ConRelations conRelations) {
		super.save(conRelations);
	}
	
	@Transactional(readOnly = false)
	public void delete(ConRelations conRelations) {
		super.delete(conRelations);
	}
	
}