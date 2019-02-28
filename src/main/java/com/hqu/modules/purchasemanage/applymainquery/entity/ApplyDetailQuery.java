/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.purchasemanage.applymainquery.entity;

import com.hqu.modules.basedata.item.entity.Item;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 采购需求单查询子表Entity
 * @author syc
 * @version 2018-04-26
 */
public class ApplyDetailQuery extends DataEntity<ApplyDetailQuery> {
	
	private static final long serialVersionUID = 1L;
	private ApplyMainQuery applyMainQuery;		// 需求单号 父类
	private Integer serialNum;		// 序号
	private Item item;		// 物料编号
	private String itemName;		// 物料名称
	private String itemSpecmodel;		// 规格型号
	private Date requestDate;		// 需求日期
	private Integer applyQty;		// 需求数量
	private String nowSum;		// 库存量
	private String unitName;		// 计量单位
	private String costPrice;		// 移动平均价
	private Integer applySum;		// 金额
	private String log;		// 数量修改历史
	private String itemTexture;		// 物料材质
	private String notes;		// 说明
	private Date planArrivedate;		// 计划到货日期
	private Integer applyPrice;		// 需求价格
	private Integer planPrice;		// 计划价格
	private Integer planSum;		// 计划金额
	private String itemPdn;		// 物料图号
	
	public ApplyDetailQuery() {
		super();
	}

	public ApplyDetailQuery(String id){
		super(id);
	}

	public ApplyDetailQuery(ApplyMainQuery applyMainQuery){
		this.applyMainQuery = applyMainQuery;
	}

	public ApplyMainQuery getApplyMainQuery() {
		return applyMainQuery;
	}

	public void setApplyMainQuery(ApplyMainQuery applyMainQuery) {
		this.applyMainQuery = applyMainQuery;
	}
	
	@ExcelField(title="序号", align=2, sort=8)
	public Integer getSerialNum() {
		return serialNum;
	}

	public void setSerialNum(Integer serialNum) {
		this.serialNum = serialNum;
	}
	
	@ExcelField(title="物料编号", fieldType=Item.class, value="item.code", align=2, sort=9)
	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}
	
	@ExcelField(title="物料名称", align=2, sort=10)
	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	
	@ExcelField(title="规格型号", align=2, sort=11)
	public String getItemSpecmodel() {
		return itemSpecmodel;
	}

	public void setItemSpecmodel(String itemSpecmodel) {
		this.itemSpecmodel = itemSpecmodel;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="需求日期", align=2, sort=12)
	public Date getRequestDate() {
		return requestDate;
	}

	public void setRequestDate(Date requestDate) {
		this.requestDate = requestDate;
	}
	
	@ExcelField(title="需求数量", align=2, sort=13)
	public Integer getApplyQty() {
		return applyQty;
	}

	public void setApplyQty(Integer applyQty) {
		this.applyQty = applyQty;
	}
	
	@ExcelField(title="库存量", align=2, sort=14)
	public String getNowSum() {
		return nowSum;
	}

	public void setNowSum(String nowSum) {
		this.nowSum = nowSum;
	}
	
	@ExcelField(title="计量单位", align=2, sort=15)
	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}
	
	@ExcelField(title="移动平均价", align=2, sort=16)
	public String getCostPrice() {
		return costPrice;
	}

	public void setCostPrice(String costPrice) {
		this.costPrice = costPrice;
	}
	
	@ExcelField(title="金额", align=2, sort=17)
	public Integer getApplySum() {
		return applySum;
	}

	public void setApplySum(Integer applySum) {
		this.applySum = applySum;
	}
	
	@ExcelField(title="数量修改历史", align=2, sort=18)
	public String getLog() {
		return log;
	}

	public void setLog(String log) {
		this.log = log;
	}
	
	@ExcelField(title="物料材质", align=2, sort=19)
	public String getItemTexture() {
		return itemTexture;
	}

	public void setItemTexture(String itemTexture) {
		this.itemTexture = itemTexture;
	}
	
	@ExcelField(title="说明", align=2, sort=20)
	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="计划到货日期", align=2, sort=21)
	public Date getPlanArrivedate() {
		return planArrivedate;
	}

	public void setPlanArrivedate(Date planArrivedate) {
		this.planArrivedate = planArrivedate;
	}
	
	@ExcelField(title="需求价格", align=2, sort=22)
	public Integer getApplyPrice() {
		return applyPrice;
	}

	public void setApplyPrice(Integer applyPrice) {
		this.applyPrice = applyPrice;
	}
	
	@ExcelField(title="计划价格", align=2, sort=23)
	public Integer getPlanPrice() {
		return planPrice;
	}

	public void setPlanPrice(Integer planPrice) {
		this.planPrice = planPrice;
	}
	
	@ExcelField(title="计划金额", align=2, sort=24)
	public Integer getPlanSum() {
		return planSum;
	}

	public void setPlanSum(Integer planSum) {
		this.planSum = planSum;
	}
	
	@ExcelField(title="物料图号", align=2, sort=25)
	public String getItemPdn() {
		return itemPdn;
	}

	public void setItemPdn(String itemPdn) {
		this.itemPdn = itemPdn;
	}
	
}