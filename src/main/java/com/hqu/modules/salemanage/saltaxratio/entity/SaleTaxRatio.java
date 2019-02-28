/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.salemanage.saltaxratio.entity;


import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 销售税率定义Entity
 * @author liujiachen
 * @version 2018-05-09
 */
public class SaleTaxRatio extends DataEntity<SaleTaxRatio> {
	
	private static final long serialVersionUID = 1L;
	private String salTaxCode;		// 流水号
	private String salTaxRatio;		// 税率
	
	public SaleTaxRatio() {
		super();
	}

	public SaleTaxRatio(String id){
		super(id);
	}

	@ExcelField(title="流水号", align=2, sort=5)
	public String getSalTaxCode() {
		return salTaxCode;
	}

	public void setSalTaxCode(String salTaxCode) {
		this.salTaxCode = salTaxCode;
	}
	
	@ExcelField(title="税率", align=2, sort=6)
	public String getSalTaxRatio() {
		return salTaxRatio;
	}

	public void setSalTaxRatio(String salTaxRatio) {
		this.salTaxRatio = salTaxRatio;
	}
	
}