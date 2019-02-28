/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.purchasemanage.taxratio.entity;

import javax.validation.constraints.NotNull;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 采购税率定义Entity
 * @author luyumiao
 * @version 2018-04-25
 */
public class TaxRatio extends DataEntity<TaxRatio> {
	
	private static final long serialVersionUID = 1L;
	private String taxratioId;		// 流水号
	private Double taxRatio;		// 税率
	private String userDeptCode;		// 登录用户所在部门的编码
	
	public TaxRatio() {
		super();
	}

	public TaxRatio(String id){
		super(id);
	}

	@ExcelField(title="流水号", align=2, sort=7)
	public String getTaxratioId() {
		return taxratioId;
	}

	public void setTaxratioId(String taxratioId) {
		this.taxratioId = taxratioId;
	}
	
	@NotNull(message="税率不能为空")
	@ExcelField(title="税率", align=2, sort=8)
	public Double getTaxRatio() {
		return taxRatio;
	}

	public void setTaxRatio(Double taxRatio) {
		this.taxRatio = taxRatio;
	}
	
	@ExcelField(title="登录用户所在部门的编码", align=2, sort=9)
	public String getUserDeptCode() {
		return userDeptCode;
	}

	public void setUserDeptCode(String userDeptCode) {
		this.userDeptCode = userDeptCode;
	}
	
}