/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.salemanage.salorder.entity;


import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 内部定单会签表Entity
 * @author dongqida
 * @version 2018-05-27
 */
public class SalOrderAudit extends DataEntity<SalOrderAudit> {
	
	private static final long serialVersionUID = 1L;
	private SalOrder orderCode;		// 订单编码 父类
	private String auditCode;		// 会签编码
	private String auditPerson;		// 会签人
	private String auditDate;		// 会签日期
	private String auditNote;		// 会签意见
	private String auditStat;		// 状态
	
	public SalOrderAudit() {
		super();
	}

	public SalOrderAudit(String id){
		super(id);
	}

	public SalOrderAudit(SalOrder orderCode){
		this.orderCode = orderCode;
	}

	public SalOrder getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(SalOrder orderCode) {
		this.orderCode = orderCode;
	}
	
	@ExcelField(title="会签编码", align=2, sort=8)
	public String getAuditCode() {
		return auditCode;
	}

	public void setAuditCode(String auditCode) {
		this.auditCode = auditCode;
	}
	
	@ExcelField(title="会签人", align=2, sort=9)
	public String getAuditPerson() {
		return auditPerson;
	}

	public void setAuditPerson(String auditPerson) {
		this.auditPerson = auditPerson;
	}
	
	@ExcelField(title="会签日期", align=2, sort=10)
	public String getAuditDate() {
		return auditDate;
	}

	public void setAuditDate(String auditDate) {
		this.auditDate = auditDate;
	}
	
	@ExcelField(title="会签意见", align=2, sort=11)
	public String getAuditNote() {
		return auditNote;
	}

	public void setAuditNote(String auditNote) {
		this.auditNote = auditNote;
	}
	
	@ExcelField(title="状态", align=2, sort=12)
	public String getAuditStat() {
		return auditStat;
	}

	public void setAuditStat(String auditStat) {
		this.auditStat = auditStat;
	}
	
}