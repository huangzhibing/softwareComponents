/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.basedata.accounttype.service;

import java.util.List;

import com.hqu.modules.basedata.unittype.entity.UnitType;
import com.jeeplus.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.hqu.modules.basedata.accounttype.entity.AccountType;
import com.hqu.modules.basedata.accounttype.mapper.AccountTypeMapper;

/**
 * 企业类型表单Service
 * @author 黄志兵
 * @version 2018-04-05
 */
@Service
@Transactional(readOnly = true)
public class AccountTypeService extends CrudService<AccountTypeMapper, AccountType> {

	@Autowired
	AccountTypeMapper accountTypeMapper;

	public AccountType get(String id) {
		return super.get(id);
	}
	
	public List<AccountType> findList(AccountType accountType) {
		return super.findList(accountType);
	}
	
	public Page<AccountType> findPage(Page<AccountType> page, AccountType accountType) {
		return super.findPage(page, accountType);
	}
	
	@Transactional(readOnly = false)
	public void save(AccountType accountType) {
		super.save(accountType);
	}
	@Transactional(readOnly = false)
	public void saveTry(AccountType accountType)throws Exception{
		/**
		 * 检测非空性
		 */
		if(StringUtils.isEmpty(accountType.getAccTypeCode()))
			throw new Exception("代码为空");
		if(StringUtils.isEmpty(accountType.getAccTypeName()))
			throw new Exception("名称为空");
		/**
		 * 检测唯一性
		 */
		AccountType accountType1 = new AccountType();
		List<AccountType> checkOnly  = this.findList(accountType1);
		int count;
		for(count=0;count<checkOnly.size();count++){
			if(accountType.getAccTypeCode().equals(checkOnly.get(count).getAccTypeCode())){
				throw new Exception("代码已存在");
			}
			if(accountType.getAccTypeName().equals(checkOnly.get(count).getAccTypeName())){
				throw new Exception("单位已存在");
			}
		}
		super.save(accountType);
	}


	
	@Transactional(readOnly = false)
	public void delete(AccountType accountType) {
		super.delete(accountType);
	}

	public String getMaxAccTypeCode(){
		return accountTypeMapper.getMaxAccTypeCode();
	}
}