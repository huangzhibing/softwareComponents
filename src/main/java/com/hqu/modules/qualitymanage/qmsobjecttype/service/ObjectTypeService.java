/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.qualitymanage.qmsobjecttype.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.hqu.modules.qualitymanage.qmsobjecttype.entity.ObjectType;
import com.hqu.modules.qualitymanage.qmsobjecttype.mapper.ObjectTypeMapper;

/**
 * 检验对象类型Service
 * @author tyo
 * @version 2018-04-26
 */
@Service
@Transactional(readOnly = true)
public class ObjectTypeService extends CrudService<ObjectTypeMapper, ObjectType> {

	public ObjectType get(String id) {
		return super.get(id);
	}
	
	public List<ObjectType> findList(ObjectType objectType) {
		return super.findList(objectType);
	}
	
	public Page<ObjectType> findPage(Page<ObjectType> page, ObjectType objectType) {
		String orderBy=page.getOrderBy();
		if(orderBy!=null&&"".equals(orderBy)){
			page.setOrderBy("objtcode");
		}		
		return super.findPage(page, objectType);
	}
	
	@Transactional(readOnly = false)
	public void save(ObjectType objectType) {
		super.save(objectType);
	}
	
	@Transactional(readOnly = false)
	public void delete(ObjectType objectType) {
		super.delete(objectType);
	}
	
}