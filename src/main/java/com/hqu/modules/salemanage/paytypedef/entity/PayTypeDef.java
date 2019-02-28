/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.salemanage.paytypedef.entity;


import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 付款方式定义Entity
 * @author M1ngz
 * @version 2018-05-07
 */
public class PayTypeDef extends DataEntity<PayTypeDef> {
	
	private static final long serialVersionUID = 1L;
	private String typeCode;		// 付款方式编码
	private String typeName;		// 付款方式名称
	private String notes;		// 备注
	
	public PayTypeDef() {
		super();
	}

	public PayTypeDef(String id){
		super(id);
	}

	@ExcelField(title="付款方式编码", align=2, sort=7)
	public String getTypeCode() {
		return typeCode;
	}

	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}
	
	@ExcelField(title="付款方式名称", align=2, sort=8)
	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	
	@ExcelField(title="备注", align=2, sort=9)
	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}
	
}