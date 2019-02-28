/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.purchasemanage.paymode.entity;


import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 付款方式定义Entity
 * @author 石豪迈
 * @version 2018-04-14
 */
public class PayMode extends DataEntity<PayMode> {
	
	private static final long serialVersionUID = 1L;
	private String paymodeid;		// 付款方式编码
	private String paymodename;		// 付款方式名称
	private String notes;		// 备注
	
	public PayMode() {
		super();
	}

	public PayMode(String id){
		super(id);
	}

	@ExcelField(title="付款方式编码", align=2, sort=7)
	public String getPaymodeid() {
		return paymodeid;
	}

	public void setPaymodeid(String paymodeid) {
		this.paymodeid = paymodeid;
	}
	
	@ExcelField(title="付款方式名称", align=2, sort=8)
	public String getPaymodename() {
		return paymodename;
	}

	public void setPaymodename(String paymodename) {
		this.paymodename = paymodename;
	}
	
	@ExcelField(title="备注", align=2, sort=9)
	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}
	
}