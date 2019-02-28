/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.purchasemanage.contractnotesmodel.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.hqu.modules.basedata.accounttype.mapper.AccountTypeMapper;
import com.hqu.modules.purchasemanage.contractnotesmodel.entity.ContractNotesModel;
import com.hqu.modules.purchasemanage.contractnotesmodel.mapper.ContractNotesModelMapper;

/**
 * 合同模板定义Service
 * @author luyumiao
 * @version 2018-04-25
 */
@Service
@Transactional(readOnly = true)
public class ContractNotesModelService extends CrudService<ContractNotesModelMapper, ContractNotesModel> {

	@Autowired
	ContractNotesModelMapper contractNotesModelMapper;

	
	public ContractNotesModel get(String id) {
		return super.get(id);
	}
	
	public List<ContractNotesModel> findList(ContractNotesModel contractNotesModel) {
		return super.findList(contractNotesModel);
	}
	
	public Page<ContractNotesModel> findPage(Page<ContractNotesModel> page, ContractNotesModel contractNotesModel) {
		return super.findPage(page, contractNotesModel);
	}
	
	@Transactional(readOnly = false)
	public void save(ContractNotesModel contractNotesModel) {
		super.save(contractNotesModel);
	}
	
	@Transactional(readOnly = false)
	public void delete(ContractNotesModel contractNotesModel) {
		super.delete(contractNotesModel);
	}
	
	public String getMaxContractId(){
		return contractNotesModelMapper.getMaxContractId();
	}
	
}