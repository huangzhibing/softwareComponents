/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.basedata.accounttype.entity;


import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 企业类型表单Entity
 * @author 黄志兵
 * @version 2018-04-05
 */
public class AccountType extends DataEntity<AccountType> {
	
	private static final long serialVersionUID = 1L;
	private String accTypeCode;		// 企业类型编码
	private String accTypeName;		// 企业类型名称
	
	public AccountType() {
		super();
	}

	public AccountType(String id){
		super(id);
	}

	@ExcelField(title="企业类型编码", align=2, sort=1)
	public String getAccTypeCode() {
		return accTypeCode;
	}

	public void setAccTypeCode(String accTypeCode) {
		this.accTypeCode = accTypeCode;
	}
	
	@ExcelField(title="企业类型名称", align=2, sort=2)
	public String getAccTypeName() {
		return accTypeName;
	}

	public void setAccTypeName(String accTypeName) {
		this.accTypeName = accTypeName;
	}
	
}