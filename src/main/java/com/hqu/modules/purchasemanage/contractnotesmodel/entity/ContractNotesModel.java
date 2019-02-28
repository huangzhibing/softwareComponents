/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.purchasemanage.contractnotesmodel.entity;


import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 合同模板定义Entity
 * @author luyumiao
 * @version 2018-04-25
 */
public class ContractNotesModel extends DataEntity<ContractNotesModel> {
	
	private static final long serialVersionUID = 1L;
	private String contractId;		// 合同模板编码
	private String contractName;		// 合同模板名称
	private String contractNotes;		// 合同模板内容
	private String userDeptCode;		// 登录用户所在部门的编码
	
	public ContractNotesModel() {
		super();
	}

	public ContractNotesModel(String id){
		super(id);
	}

	@ExcelField(title="合同模板编码", align=2, sort=7)
	public String getContractId() {
		return contractId;
	}

	public void setContractId(String contractId) {
		this.contractId = contractId;
	}
	
	@ExcelField(title="合同模板名称", align=2, sort=8)
	public String getContractName() {
		return contractName;
	}

	public void setContractName(String contractName) {
		this.contractName = contractName;
	}
	
	@ExcelField(title="合同模板内容", align=2, sort=9)
	public String getContractNotes() {
		return contractNotes;
	}

	public void setContractNotes(String contractNotes) {
		this.contractNotes = contractNotes;
	}
	
	@ExcelField(title="登录用户所在部门的编码", align=2, sort=10)
	public String getUserDeptCode() {
		return userDeptCode;
	}

	public void setUserDeptCode(String userDeptCode) {
		this.userDeptCode = userDeptCode;
	}
	
}