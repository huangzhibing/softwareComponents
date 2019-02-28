/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.purchasemanage.contracttype.entity;


import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 合同类型定义Entity
 * @author tyo
 * @version 2018-05-26
 */
public class ContractType extends DataEntity<ContractType> {
	
	private static final long serialVersionUID = 1L;
	private String contypeid;		// 合同类型代码
	private String contypename;		// 合同类型名称
	private String contypenotes;		// 备注
	private String userDeptCode;		// 登陆用户所在部门编码
	
	public ContractType() {
		super();
	}

	public ContractType(String id){
		super(id);
	}

	@ExcelField(title="合同类型代码", align=2, sort=7)
	public String getContypeid() {
		return contypeid;
	}

	public void setContypeid(String contypeid) {
		this.contypeid = contypeid;
	}
	
	@ExcelField(title="合同类型名称", align=2, sort=8)
	public String getContypename() {
		return contypename;
	}

	public void setContypename(String contypename) {
		this.contypename = contypename;
	}
	
	@ExcelField(title="备注", align=2, sort=9)
	public String getContypenotes() {
		return contypenotes;
	}

	public void setContypenotes(String contypenotes) {
		this.contypenotes = contypenotes;
	}
	
	@ExcelField(title="登陆用户所在部门编码", align=2, sort=10)
	public String getUserDeptCode() {
		return userDeptCode;
	}

	public void setUserDeptCode(String userDeptCode) {
		this.userDeptCode = userDeptCode;
	}
	
}