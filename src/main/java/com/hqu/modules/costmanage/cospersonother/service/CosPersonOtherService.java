/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.costmanage.cospersonother.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.hqu.modules.costmanage.cospersonother.entity.CosPersonOther;
import com.hqu.modules.costmanage.cospersonother.mapper.CosPersonOtherMapper;

/**
 * 其它工资单据稽核Service
 * @author hzm
 * @version 2018-09-01
 */
@Service
@Transactional(readOnly = true)
public class CosPersonOtherService extends CrudService<CosPersonOtherMapper, CosPersonOther> {

	public CosPersonOther get(String id) {
		return super.get(id);
	}
	
	public List<CosPersonOther> findList(CosPersonOther cosPersonOther) {
		return super.findList(cosPersonOther);
	}
	
	public Page<CosPersonOther> findPage(Page<CosPersonOther> page, CosPersonOther cosPersonOther) {
		return super.findPage(page, cosPersonOther);
	}
	
	@Transactional(readOnly = false)
	public void save(CosPersonOther cosPersonOther) {
		super.save(cosPersonOther);
	}
	
	@Transactional(readOnly = false)
	public void delete(CosPersonOther cosPersonOther) {
		super.delete(cosPersonOther);
	}

	@Transactional(readOnly = false)
	public int updataReal(String itemSum,String cosBillNum){
		return mapper.updataReal(itemSum,cosBillNum);
	}
}