/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.salemanage.accounttype.entity;


import com.jeeplus.core.persistence.DataEntity;

import org.hibernate.validator.constraints.Length;

import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 客户类型定义Entity
 * @author hzm
 * @version 2018-05-05
 */
public class AccountType2 extends DataEntity<AccountType2> {
	
	private static final long serialVersionUID = 1L;
	private String accTypeCode;		// 客户类型编码
	private String accTypeName;		// 客户类型名称
	private String accTypeDesc;		// 客户类型描述
	
	public AccountType2() {
		super();
	}

	public AccountType2(String id){
		super(id);
	}
	
	@Length(min=2, max=2, message="客户类型编码长度必须介于 2 和 2 之间")
	@ExcelField(title="客户类型编码", align=2, sort=1)
	public String getAccTypeCode() {
		return accTypeCode;
	}

	public void setAccTypeCode(String accTypeCode) {
		this.accTypeCode = accTypeCode;
	}
	
	@ExcelField(title="客户类型名称", align=2, sort=2)
	public String getAccTypeName() {
		return accTypeName;
	}

	public void setAccTypeName(String accTypeName) {
		this.accTypeName = accTypeName;
	}
	
	@ExcelField(title="客户类型描述", align=2, sort=3)
	public String getAccTypeDesc() {
		return accTypeDesc;
	}

	public void setAccTypeDesc(String accTypeDesc) {
		this.accTypeDesc = accTypeDesc;
	}
	
}