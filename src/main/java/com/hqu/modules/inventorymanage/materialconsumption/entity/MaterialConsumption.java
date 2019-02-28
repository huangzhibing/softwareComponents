package com.hqu.modules.inventorymanage.materialconsumption.entity;

import com.hqu.modules.basedata.account.entity.Account;
import com.jeeplus.core.persistence.DataEntity;

public class MaterialConsumption  extends DataEntity<Account>{
	private String deptCode; //部门编码
	private String deptName; //部门名称
	private String itemCode; //物料编码
	private String itemName; //物料名称
	private String itemSpec; //规格型号
	private String measUnit; //单位
	private Double itemQty; //物料数量
	private Double itemSum; //物料金额
	public String getDeptCode() {
		return deptCode;
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public String getItemCode() {
		return itemCode;
	}
	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public String getItemSpec() {
		return itemSpec;
	}
	public void setItemSpec(String itemSpec) {
		this.itemSpec = itemSpec;
	}
	public String getMeasUnit() {
		return measUnit;
	}
	public void setMeasUnit(String measUnit) {
		this.measUnit = measUnit;
	}
	public Double getItemQty() {
		return itemQty;
	}
	public void setItemQty(Double itemQty) {
		this.itemQty = itemQty;
	}
	public Double getItemSum() {
		return itemSum;
	}
	public void setItemSum(Double itemSum) {
		this.itemSum = itemSum;
	}
}
