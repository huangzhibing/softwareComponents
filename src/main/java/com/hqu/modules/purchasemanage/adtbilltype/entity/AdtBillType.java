/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.purchasemanage.adtbilltype.entity;


import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 单据类型表Entity
 * @author ckw
 * @version 2018-05-08
 */
public class AdtBillType extends DataEntity<AdtBillType> {
	
	private static final long serialVersionUID = 1L;
	private String billTypeCode;		// 单据类型编号
	private String billTypeName;		// 单据类型名称
	
	public AdtBillType() {
		super();
	}

	public AdtBillType(String id){
		super(id);
	}

	@ExcelField(title="单据类型编号", align=2, sort=7)
	public String getBillTypeCode() {
		return billTypeCode;
	}

	public void setBillTypeCode(String billTypeCode) {
		this.billTypeCode = billTypeCode;
	}
	
	@ExcelField(title="单据类型名称", dictType="billTypeCode", align=2, sort=8)
	public String getBillTypeName() {
		return billTypeName;
	}

	public void setBillTypeName(String billTypeName) {
		this.billTypeName = billTypeName;
	}
	
}