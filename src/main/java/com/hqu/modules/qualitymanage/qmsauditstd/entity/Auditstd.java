/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.qualitymanage.qmsauditstd.entity;

import com.jeeplus.modules.sys.entity.Office;
import javax.validation.constraints.NotNull;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 稽核标准Entity
 * @author syc1
 * @version 2018-05-25
 */
public class Auditstd extends DataEntity<Auditstd> {
	
	
	private static final long serialVersionUID = 1L;
	private String sn;		// 序号
	private String auditpCode;		// 稽核流程ID
	private String auditpName;		// 稽核流程名称
	private String auditItemCode;		// 稽核标准要素编码
	private String auditItemName;		// 稽核标准要素名称
	private Office office;		// 稽核部门编号
	private String auditDeptName;		// 稽核部门名称
	
	public Auditstd() {
		super();
	}

	public Auditstd(String id){
		super(id);
	}

	@ExcelField(title="序号", align=2, sort=7)
	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}
	
	@ExcelField(title="稽核流程ID", align=2, sort=8)
	public String getAuditpCode() {
		return auditpCode;
	}

	public void setAuditpCode(String auditpCode) {
		this.auditpCode = auditpCode;
	}
	
	@ExcelField(title="稽核流程名称", align=2, sort=9)
	public String getAuditpName() {
		return auditpName;
	}

	public void setAuditpName(String auditpName) {
		this.auditpName = auditpName;
	}
	
	@ExcelField(title="稽核标准要素编码", align=2, sort=10)
	public String getAuditItemCode() {
		return auditItemCode;
	}

	public void setAuditItemCode(String auditItemCode) {
		this.auditItemCode = auditItemCode;
	}
	
	@ExcelField(title="稽核标准要素名称", align=2, sort=11)
	public String getAuditItemName() {
		return auditItemName;
	}

	public void setAuditItemName(String auditItemName) {
		this.auditItemName = auditItemName;
	}
	
	@NotNull(message="稽核部门编号不能为空")
	@ExcelField(title="稽核部门编号", fieldType=Office.class, value="office.code", align=2, sort=12)
	public Office getOffice() {
		return office;
	}

	public void setOffice(Office office) {
		this.office = office;
	}
	
	@ExcelField(title="稽核部门名称", align=2, sort=13)
	public String getAuditDeptName() {
		return auditDeptName;
	}

	public void setAuditDeptName(String auditDeptName) {
		this.auditDeptName = auditDeptName;
	}
	
}