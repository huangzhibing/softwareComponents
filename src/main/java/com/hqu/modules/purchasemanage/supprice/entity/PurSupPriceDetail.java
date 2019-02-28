/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.purchasemanage.supprice.entity;

import com.hqu.modules.basedata.item.entity.Item;
import javax.validation.constraints.NotNull;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 供应商价格维护明细Entity
 * @author syc
 * @version 2018-08-02
 */
public class PurSupPriceDetail extends DataEntity<PurSupPriceDetail> {
	
	private static final long serialVersionUID = 1L;
	private String serialNum;		// 序号
	private PurSupPrice account;		// 供应商编码 父类
	private Item item;		// 物料编码
	private String itemName;		// 物料名称
	private Date startDate;		// 开始时间
	private Date endDate;		// 结束时间
	private Double minQty;		// 最小数量
	private Double maxQty;		// 最大数量
	private Double supPrice;		// 价格
	private String notes;		// 备注

	private String itemSpecmodel;		// 物料规格
	private String unitName;		// 物料单位
	
	public PurSupPriceDetail() {
		super();
	}

	public PurSupPriceDetail(String id){
		super(id);
	}

	public PurSupPriceDetail(PurSupPrice account){
		this.account = account;
	}

	@ExcelField(title="序号", align=2, sort=7)
	public String getSerialNum() {
		return serialNum;
	}

	public void setSerialNum(String serialNum) {
		this.serialNum = serialNum;
	}
	
	public PurSupPrice getAccount() {
		return account;
	}

	public void setAccount(PurSupPrice account) {
		this.account = account;
	}
	
	@NotNull(message="物料编码不能为空")
	@ExcelField(title="物料编码", fieldType=Item.class, value="item.code", align=2, sort=9)
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
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@NotNull(message="开始时间不能为空")
	@ExcelField(title="开始时间", align=2, sort=11)
	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@NotNull(message="结束时间不能为空")
	@ExcelField(title="结束时间", align=2, sort=12)
	public Date getEndDate() {
		return endDate;
	}

	public String getItemSpecmodel() {
		return itemSpecmodel;
	}

	public void setItemSpecmodel(String itemSpecmodel) {
		this.itemSpecmodel = itemSpecmodel;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
	@NotNull(message="最小数量不能为空")
	@ExcelField(title="最小数量", align=2, sort=13)
	public Double getMinQty() {
		return minQty;
	}

	public void setMinQty(Double minQty) {
		this.minQty = minQty;
	}
	
	@NotNull(message="最大数量不能为空")
	@ExcelField(title="最大数量", align=2, sort=14)
	public Double getMaxQty() {
		return maxQty;
	}

	public void setMaxQty(Double maxQty) {
		this.maxQty = maxQty;
	}
	
	@NotNull(message="价格不能为空")
	@ExcelField(title="价格", align=2, sort=15)
	public Double getSupPrice() {
		return supPrice;
	}

	public void setSupPrice(Double supPrice) {
		this.supPrice = supPrice;
	}
	
	@ExcelField(title="备注", align=2, sort=16)
	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}
	
}