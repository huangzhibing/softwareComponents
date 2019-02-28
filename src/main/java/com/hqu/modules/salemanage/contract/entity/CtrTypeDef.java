/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.salemanage.contract.entity;


import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 合同类型Entity
 * @author dongqida
 * @version 2018-05-05
 */
public class CtrTypeDef extends DataEntity<CtrTypeDef> {
	
	private static final long serialVersionUID = 1L;
	private String ctrTypeCode;		// 合同类型编码
	private String ctrTypeName;		// 合同类型名称
	private String ctrTypeDesc;		// 合同类型描述
	
	public CtrTypeDef() {
		super();
	}

	public CtrTypeDef(String id){
		super(id);
	}

	@ExcelField(title="合同类型编码", align=2, sort=7)
	public String getCtrTypeCode() {
		return ctrTypeCode;
	}

	public void setCtrTypeCode(String ctrTypeCode) {
		this.ctrTypeCode = ctrTypeCode;
	}
	
	@ExcelField(title="合同类型名称", align=2, sort=8)
	public String getCtrTypeName() {
		return ctrTypeName;
	}

	public void setCtrTypeName(String ctrTypeName) {
		this.ctrTypeName = ctrTypeName;
	}
	
	@ExcelField(title="合同类型描述", align=2, sort=9)
	public String getCtrTypeDesc() {
		return ctrTypeDesc;
	}

	public void setCtrTypeDesc(String ctrTypeDesc) {
		this.ctrTypeDesc = ctrTypeDesc;
	}
	
}