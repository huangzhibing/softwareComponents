/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.inventorymanage.invout.entity;

import javax.validation.constraints.NotNull;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 放行单子表Entity
 * @author M1ngz
 * @version 2018-06-02
 */
public class InvOutDetail extends DataEntity<InvOutDetail> {
	
	private static final long serialVersionUID = 1L;
	private InvOutMain billNum;		// 单据号 父类
	private Integer serialNum;		// 序号
	private String itemCode;		// 物料编码
	private String itemSpec;		// 物料名称
	private String itemName;		// 物料规格
	private String measUnit;		// 单位
	private Double itemQty;		// 数量
	
	public InvOutDetail() {
		super();
	}

	public InvOutDetail(String id){
		super(id);
	}

	public InvOutDetail(InvOutMain billNum){
		this.billNum = billNum;
	}

	public InvOutMain getBillNum() {
		return billNum;
	}

	public void setBillNum(InvOutMain billNum) {
		this.billNum = billNum;
	}
	
	@NotNull(message="序号不能为空")
	@ExcelField(title="序号", align=2, sort=8)
	public Integer getSerialNum() {
		return serialNum;
	}

	public void setSerialNum(Integer serialNum) {
		this.serialNum = serialNum;
	}
	
	@ExcelField(title="物料编码", align=2, sort=9)
	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}
	
	@ExcelField(title="物料名称", align=2, sort=10)
	public String getItemSpec() {
		return itemSpec;
	}

	public void setItemSpec(String itemSpec) {
		this.itemSpec = itemSpec;
	}
	
	@ExcelField(title="物料规格", align=2, sort=11)
	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	
	@ExcelField(title="单位", align=2, sort=12)
	public String getMeasUnit() {
		return measUnit;
	}

	public void setMeasUnit(String measUnit) {
		this.measUnit = measUnit;
	}
	
	@NotNull(message="数量不能为空")
	@ExcelField(title="数量", align=2, sort=13)
	public Double getItemQty() {
		return itemQty;
	}

	public void setItemQty(Double itemQty) {
		this.itemQty = itemQty;
	}
	
}