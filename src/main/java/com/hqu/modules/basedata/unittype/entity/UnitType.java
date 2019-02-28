/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.basedata.unittype.entity;


import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 计量单位类别维护Entity
 * @author yrg
 * @version 2018-04-05
 */
public class UnitType extends DataEntity<UnitType> {
	
	private static final long serialVersionUID = 1L;
	private String unitTypeCode;		// 单位类别编码
	private String unitTypeName;		// 单位类别名称
	
	public UnitType() {
		super();
	}

	public UnitType(String id){
		super(id);
	}

	@ExcelField(title="单位类别编码", align=2, sort=7)
	public String getUnitTypeCode() {
		return unitTypeCode;
	}

	public void setUnitTypeCode(String unitTypeCode) {
		this.unitTypeCode = unitTypeCode;
	}
	
	@ExcelField(title="单位类别名称", align=2, sort=8)
	public String getUnitTypeName() {
		return unitTypeName;
	}

	public void setUnitTypeName(String unitTypeName) {
		this.unitTypeName = unitTypeName;
	}
	
}