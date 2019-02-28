/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.purchasemanage.contracttype.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.hqu.modules.purchasemanage.contracttype.entity.ContractType;
import com.hqu.modules.purchasemanage.contracttype.mapper.ContractTypeMapper;

/**
 * 合同类型定义Service
 * @author tyo
 * @version 2018-05-26
 */
@Service
@Transactional(readOnly = true)
public class ContractTypeService extends CrudService<ContractTypeMapper, ContractType> {

	@Autowired
	ContractTypeMapper contractTypeMapper;

	public ContractType get(String id) {
		return super.get(id);
	}
	
	public List<ContractType> findList(ContractType contractType) {
		return super.findList(contractType);
	}
	
	public Page<ContractType> findPage(Page<ContractType> page, ContractType contractType) {
		String orderBy=page.getOrderBy();
		if(orderBy!=null&&"".equals(orderBy)){
			page.setOrderBy("contypeid");
		}		
		return super.findPage(page, contractType);
	}
	
	@Transactional(readOnly = false)
	public void save(ContractType contractType) {
		super.save(contractType);
	}
	
	@Transactional(readOnly = false)
	public void delete(ContractType contractType) {
		super.delete(contractType);
	}

	public String getMaxContractTypeID(){
		return  contractTypeMapper.getMaxContractTypeID();
	}



	
}