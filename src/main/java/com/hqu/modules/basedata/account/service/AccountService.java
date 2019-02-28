/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.basedata.account.service;

import java.util.List;

import com.hqu.modules.basedata.accounttype.entity.AccountType;
import com.hqu.modules.basedata.accounttype.mapper.AccountTypeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.hqu.modules.basedata.account.entity.Account;
import com.hqu.modules.basedata.account.mapper.AccountMapper;
import org.springframework.util.StringUtils;

/**
 * 关系企业维护Service
 * @author M1ngz
 * @version 2018-04-05
 */
@Service
@Transactional(readOnly = true)
public class AccountService extends CrudService<AccountMapper, Account> {
    @Autowired
    private AccountMapper mapper;
    @Autowired
    private AccountTypeMapper accountTypeMapper;

	public Account get(String id) {
		return super.get(id);
	}
	
	public List<Account> findList(Account account) {
		return super.findList(account);
	}
	
	public Page<Account> findPage(Page<Account> page, Account account) {
		return super.findPage(page, account);
	}
	
	@Transactional(readOnly = false)
	public void save(Account account) {
		super.save(account);
	}
	
	@Transactional(readOnly = false)
	public void delete(Account account) {
		super.delete(account);
	}

	public Integer getCodeNum(String accCode) { return mapper.getCodeNum(accCode); }

	@Transactional(readOnly = false)
	public void saveTry(Account account) throws Exception {
		/**
		 * 检测非空性
		 */
		if(StringUtils.isEmpty(account.getAccountCode()))
			throw new Exception("代码为空");
		if(StringUtils.isEmpty(account.getAccountName()))
			throw new Exception("名称为空");

		/**
		 * 检测唯一性
		 */
		List<Account> checkOnly = this.findList(new Account());
		int count;
		for(count=0;count<checkOnly.size();count++){
			//System.out.println(checkOnly.get(count).getUnitTypeCode()+".+-"+count+unitType.getUnitTypeCode());
			if(account.getAccountCode().equals(checkOnly.get(count).getAccountCode()))
			{
				throw new Exception("代码已存在");
			}
			if(account.getAccountName().equals(checkOnly.get(count).getAccountName()))
			{
				throw new Exception("单位已存在");
			}
		}
		AccountType accountType = new AccountType();
		account.setAccountCode(account.getAccType().getAccTypeCode());
		List<AccountType> accountTypes = accountTypeMapper.findList(accountType);
		if(accountTypes == null || accountTypes.size() <= 0){
			throw new Exception("不存在该类型");
		}
		account.setAccType(accountTypes.get(0));
		account.setAccTypeNameRu(accountTypes.get(0).getAccTypeName());
		super.save(account);
	}
	
}