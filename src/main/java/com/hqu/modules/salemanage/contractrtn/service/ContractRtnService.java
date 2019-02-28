/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.salemanage.contractrtn.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.hqu.modules.salemanage.contractrtn.entity.ContractRtn;
import com.hqu.modules.salemanage.contractrtn.mapper.ContractRtnMapper;

/**
 * 销售回款管理Service
 * @author liujiachen
 * @version 2018-07-09
 */
@Service
@Transactional(readOnly = true)
public class ContractRtnService extends CrudService<ContractRtnMapper, ContractRtn> {

	public ContractRtn get(String id) {
		return super.get(id);
	}
	
	public List<ContractRtn> findList(ContractRtn contractRtn) {
		return super.findList(contractRtn);
	}
	
	public Page<ContractRtn> findPage(Page<ContractRtn> page, ContractRtn contractRtn) {
		return super.findPage(page, contractRtn);
	}
	
	@Transactional(readOnly = false)
	public void save(ContractRtn contractRtn) {
		super.save(contractRtn);
	}
	
	@Transactional(readOnly = false)
	public void delete(ContractRtn contractRtn) {
		super.delete(contractRtn);
	}
	public 	String getMaxIdByTypeAndDate(String para){
		return mapper.getMaxIdByTypeAndDate(para);
	}
}