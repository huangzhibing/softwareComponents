/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.costmanage.cosprodobject.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.hqu.modules.costmanage.cosprodobject.entity.CosProdObject;
import com.hqu.modules.costmanage.cosprodobject.mapper.CosProdObjectMapper;

/**
 * 核算对象定义(右表）Service
 * @author zz
 * @version 2018-09-07
 */
@Service
@Transactional(readOnly = true)
public class CosProdObjectService extends CrudService<CosProdObjectMapper, CosProdObject> {

	public CosProdObject get(String id) {
		return super.get(id);
	}
	
	public List<CosProdObject> findList(CosProdObject cosProdObject) {
		return super.findList(cosProdObject);
	}
	
	public Page<CosProdObject> findPage(Page<CosProdObject> page, CosProdObject cosProdObject) {
		return super.findPage(page, cosProdObject);
	}
	
	@Transactional(readOnly = false)
	public void save(CosProdObject cosProdObject) {
		super.save(cosProdObject);
	}
	
	@Transactional(readOnly = false)
	public void delete(CosProdObject cosProdObject) {
		super.delete(cosProdObject);
	}
	
}