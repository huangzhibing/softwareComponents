/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.qualitymanage.purinvcheckmain.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.hqu.modules.qualitymanage.purinvcheckmain.entity.InvCheckRelations;
import com.hqu.modules.qualitymanage.purinvcheckmain.mapper.InvCheckRelationsMapper;

/**
 * 关系描述Service
 * @author ltq
 * @version 2018-05-23
 */
@Service
@Transactional(readOnly = true)
public class InvCheckRelationsService extends CrudService<InvCheckRelationsMapper, InvCheckRelations> {

	public InvCheckRelations get(String id) {
		return super.get(id);
	}
	
	public List<InvCheckRelations> findList(InvCheckRelations invCheckRelations) {
		return super.findList(invCheckRelations);
	}
	
	public Page<InvCheckRelations> findPage(Page<InvCheckRelations> page, InvCheckRelations invCheckRelations) {
		return super.findPage(page, invCheckRelations);
	}
	
	@Transactional(readOnly = false)
	public void save(InvCheckRelations invCheckRelations) {
		super.save(invCheckRelations);
	}
	
	@Transactional(readOnly = false)
	public void delete(InvCheckRelations invCheckRelations) {
		super.delete(invCheckRelations);
	}
	
}