/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.inventorymanage.warehouse.entity;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 仓库管理Entity
 * @author M1ngz
 * @version 2018-04-12
 */
public class WareHouse extends DataEntity<WareHouse> {
	
	private static final long serialVersionUID = 1L;
	private String wareID;		// 库房号
	private String wareName;		// 库房名称
	private String wareAddress;		// 库房地址
	private Double planCost;		// 计划资金
	private String adminMode;		// 管理标识
	private String priceMode;		// 价格设置
	private String currPeriod;		// 当前结算期
	private Date lastCarrdate;		// 最近结转日期
	private String note;		// 备注
	private String itemType;		// 货物标志
	private String subFlag;		// 是否允许库存为负标志
	private String taxFlag;		// 是否为含税价入库标志
	private String batchFlag;		// 批次管理标志
	private String fncId;		// 对应财务编码
	private Integer isLeaf;		// 是否末级
	private String autoFlag;  // 自动仓库标志
	

	public WareHouse() {
		super();
	}

	public WareHouse(String id){
		super(id);
	}

	public String getAutoFlag() {
		return autoFlag;
	}

	public void setAutoFlag(String autoFlag) {
		this.autoFlag = autoFlag;
	}

	@ExcelField(title="库房号", align=2, sort=7)
	public String getWareID() {
		return wareID;
	}

	public void setWareID(String wareID) {
		this.wareID = wareID;
	}
	
	@ExcelField(title="库房名称", align=2, sort=8)
	public String getWareName() {
		return wareName;
	}

	public void setWareName(String wareName) {
		this.wareName = wareName;
	}
	
	@ExcelField(title="库房地址", align=2, sort=9)
	public String getWareAddress() {
		return wareAddress;
	}

	public void setWareAddress(String wareAddress) {
		this.wareAddress = wareAddress;
	}
	
	@ExcelField(title="计划资金", align=2, sort=10)
	public Double getPlanCost() {
		return planCost;
	}

	public void setPlanCost(Double planCost) {
		this.planCost = planCost;
	}
	
	@ExcelField(title="管理标识", dictType="adminMode", align=2, sort=11)
	public String getAdminMode() {
		return adminMode;
	}

	public void setAdminMode(String adminMode) {
		this.adminMode = adminMode;
	}
	
	@ExcelField(title="价格设置", dictType="priceMode", align=2, sort=12)
	public String getPriceMode() {
		return priceMode;
	}

	public void setPriceMode(String priceMode) {
		this.priceMode = priceMode;
	}
	
	@ExcelField(title="当前结算期", align=2, sort=13)
	public String getCurrPeriod() {
		return currPeriod;
	}

	public void setCurrPeriod(String currPeriod) {
		this.currPeriod = currPeriod;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="最近结转日期", align=2, sort=14)
	public Date getLastCarrdate() {
		return lastCarrdate;
	}

	public void setLastCarrdate(Date lastCarrdate) {
		this.lastCarrdate = lastCarrdate;
	}
	
	@ExcelField(title="备注", align=2, sort=15)
	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}
	
	@ExcelField(title="货物标志", dictType="itemType", align=2, sort=16)
	public String getItemType() {
		return itemType;
	}

	public void setItemType(String itemType) {
		this.itemType = itemType;
	}
	
	@ExcelField(title="是否允许库存为负标志", dictType="subFlag", align=2, sort=17)
	public String getSubFlag() {
		return subFlag;
	}

	public void setSubFlag(String subFlag) {
		this.subFlag = subFlag;
	}
	
	@ExcelField(title="是否为含税价入库标志", dictType="taxFlag", align=2, sort=18)
	public String getTaxFlag() {
		return taxFlag;
	}

	public void setTaxFlag(String taxFlag) {
		this.taxFlag = taxFlag;
	}
	
	@ExcelField(title="批次管理标志", dictType="batchFlag", align=2, sort=19)
	public String getBatchFlag() {
		return batchFlag;
	}

	public void setBatchFlag(String batchFlag) {
		this.batchFlag = batchFlag;
	}
	
	@ExcelField(title="对应财务编码", align=2, sort=20)
	public String getFncId() {
		return fncId;
	}

	public void setFncId(String fncId) {
		this.fncId = fncId;
	}
	
	@ExcelField(title="是否末级", align=2, sort=21)
	public Integer getIsLeaf() {
		return isLeaf;
	}

	public void setIsLeaf(Integer isLeaf) {
		this.isLeaf = isLeaf;
	}
	
}