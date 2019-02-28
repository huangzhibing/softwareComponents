/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.salemanage.blnctypedef.entity;


import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 结算方式定义Entity
 * @author M1ngz
 * @version 2018-05-07
 */
public class BlncTypeDef extends DataEntity<BlncTypeDef> {
	
	private static final long serialVersionUID = 1L;
	private String blncTypeCode;		// 结算方式编码
	private String blncTypeName;		// 结算方式名称
	private String blncTypeDesc;		// 结算方式描述
	
	public BlncTypeDef() {
		super();
	}

	public BlncTypeDef(String id){
		super(id);
	}

	@ExcelField(title="结算方式编码", align=2, sort=7)
	public String getBlncTypeCode() {
		return blncTypeCode;
	}

	public void setBlncTypeCode(String blncTypeCode) {
		this.blncTypeCode = blncTypeCode;
	}
	
	@ExcelField(title="结算方式名称", align=2, sort=8)
	public String getBlncTypeName() {
		return blncTypeName;
	}

	public void setBlncTypeName(String blncTypeName) {
		this.blncTypeName = blncTypeName;
	}
	
	@ExcelField(title="结算方式描述", align=2, sort=9)
	public String getBlncTypeDesc() {
		return blncTypeDesc;
	}

	public void setBlncTypeDesc(String blncTypeDesc) {
		this.blncTypeDesc = blncTypeDesc;
	}
	
}