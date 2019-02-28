/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.salemanage.accounttype.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.hqu.modules.basedata.machinetype.entity.MachineType;
import com.hqu.modules.basedata.machinetype.mapper.MachineTypeMapper;
import com.hqu.modules.salemanage.accounttype.entity.AccountType2;
import com.hqu.modules.salemanage.accounttype.mapper.AccountType2Mapper;

/**
 * 客户类型定义Service
 * @author hzm
 * @version 2018-05-05
 */
@Service
@Transactional(readOnly = true)
public class AccountType2Service extends CrudService<AccountType2Mapper, AccountType2> {
	@Resource
	private AccountType2Mapper accountType2Mapper;
	
	public AccountType2 get(String id) {
		return super.get(id);
	}
	
	public List<AccountType2> findList(AccountType2 accountType2) {
		return super.findList(accountType2);
	}
	
	public Page<AccountType2> findPage(Page<AccountType2> page, AccountType2 accountType2) {
		return super.findPage(page, accountType2);
	}
	
	@Transactional(readOnly = false)
	public void save(AccountType2 accountType2) {
		super.save(accountType2);
	}
	
	@Transactional(readOnly = false)
	public void delete(AccountType2 accountType2) {
		super.delete(accountType2);
	}

	public String selectMax() {
		return accountType2Mapper.selectMax();
	}
	
	@Transactional(readOnly = false)
	public void saveExc(AccountType2 accountType2) throws Exception {
		/**
		 * 检测非空性
		 */
		if(accountType2.getAccTypeCode()==null ||accountType2.getAccTypeCode()=="")
			throw new Exception("编码为空");
		if(accountType2.getAccTypeName()==null || accountType2.getAccTypeName()=="")
			throw new Exception("名称为空");
		
		/**
		 * 检测唯一性
		 */
		AccountType2 nothing = new AccountType2();		
		List<AccountType2> checkOnly=this.findList(nothing);
		int count;		
		for(count=0;count<checkOnly.size();count++){
			//System.out.println(checkOnly.get(count).getUnitTypeCode()+".+-"+count+unitType.getUnitTypeCode());
			if(accountType2.getAccTypeCode().equals(checkOnly.get(count).getAccTypeCode()))
			{
				throw new Exception("代码已存在");
			}
			if(accountType2.getAccTypeName().equals(checkOnly.get(count).getAccTypeName()))
			{
				throw new Exception("名称已存在");
			}
		}
			super.save(accountType2);
		}
	
}