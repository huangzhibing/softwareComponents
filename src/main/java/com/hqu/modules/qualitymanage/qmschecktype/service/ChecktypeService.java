/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.qualitymanage.qmschecktype.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.hqu.modules.qualitymanage.qmschecktype.entity.Checktype;
import com.hqu.modules.qualitymanage.qmschecktype.mapper.ChecktypeMapper;

/**
 * 检验类型Service
 * @author tyo
 * @version 2018-04-14
 */
@Service
@Transactional(readOnly = true)
public class ChecktypeService extends CrudService<ChecktypeMapper, Checktype> {

	public Checktype get(String id) {
		return super.get(id);
	}
	
	public List<Checktype> findList(Checktype checktype) {
		return super.findList(checktype);
	}
	
	public Page<Checktype> findPage(Page<Checktype> page, Checktype checktype) {
		String orderBy=page.getOrderBy();
		if(orderBy!=null&&"".equals(orderBy)){
			page.setOrderBy("checktcode");
		}		
		return super.findPage(page, checktype);
	}
	
	@Transactional(readOnly = false)
	public void save(Checktype checktype) {
		super.save(checktype);
	}
	
	@Transactional(readOnly = false)
	public void delete(Checktype checktype) {
		super.delete(checktype);
	}
	
}