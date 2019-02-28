/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.salemanage.contract.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.hqu.modules.salemanage.contract.entity.CtrTypeDef;
import com.hqu.modules.salemanage.contract.mapper.CtrTypeDefMapper;

/**
 * 合同类型Service
 * @author dongqida
 * @version 2018-05-05
 */
@Service
@Transactional(readOnly = true)
public class CtrTypeDefService extends CrudService<CtrTypeDefMapper, CtrTypeDef> {

	public CtrTypeDef get(String id) {
		return super.get(id);
	}
	
	public List<CtrTypeDef> findList(CtrTypeDef ctrTypeDef) {
		return super.findList(ctrTypeDef);
	}
	
	public Page<CtrTypeDef> findPage(Page<CtrTypeDef> page, CtrTypeDef ctrTypeDef) {
		return super.findPage(page, ctrTypeDef);
	}
	
	@Transactional(readOnly = false)
	public void save(CtrTypeDef ctrTypeDef) {
		super.save(ctrTypeDef);
	}
	
	@Transactional(readOnly = false)
	public void delete(CtrTypeDef ctrTypeDef) {
		super.delete(ctrTypeDef);
	}
	
}