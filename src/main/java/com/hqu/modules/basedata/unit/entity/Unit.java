/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.basedata.unit.entity;

import com.hqu.modules.basedata.unittype.entity.UnitType;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 计量单位定义Entity
 * @author 黄志兵
 * @version 2018-04-05
 */
public class Unit extends DataEntity<Unit> {
	
	private static final long serialVersionUID = 1L;
	private String unitCode;		// 计量单位编码
	private String unitName;		// 计量单位名称
	private UnitType unittype;		// 计量单位类型
	private String isStand;		// 是否标准
	private String standUnitCode;		// 标准计量单位编码
	private String standUnitName;		// 标准计量单位名称
	private Double conversion;		// 单位换算关系
	
	public Unit() {
		super();
	}

	public Unit(String id){
		super(id);
	}

	@ExcelField(title="计量单位编码", align=2, sort=7)
	public String getUnitCode() {
		return unitCode;
	}

	public void setUnitCode(String unitCode) {
		this.unitCode = unitCode;
	}
	
	@ExcelField(title="计量单位名称", align=2, sort=8)
	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}
	
	@ExcelField(title="计量单位类型", fieldType=UnitType.class, value="unittype.unitTypeName", align=2, sort=9)
	public UnitType getUnittype() {
		return unittype;
	}

	public void setUnittype(UnitType unittype) {
		this.unittype = unittype;
	}
	
	@ExcelField(title="是否标准", align=2, sort=10)
	public String getIsStand() {
		return isStand;
	}

	public void setIsStand(String isStand) {
		this.isStand = isStand;
	}
	
	@ExcelField(title="标准计量单位编码", align=2, sort=11)
	public String getStandUnitCode() {
		return standUnitCode;
	}

	public void setStandUnitCode(String standUnitCode) {
		this.standUnitCode = standUnitCode;
	}
	
	@ExcelField(title="标准计量单位名称", align=2, sort=12)
	public String getStandUnitName() {
		return standUnitName;
	}

	public void setStandUnitName(String standUnitName) {
		this.standUnitName = standUnitName;
	}
	
	@ExcelField(title="单位换算关系", align=2, sort=13)
	public Double getConversion() {
		return conversion;
	}

	public void setConversion(Double conversion) {
		this.conversion = conversion;
	}
	
}