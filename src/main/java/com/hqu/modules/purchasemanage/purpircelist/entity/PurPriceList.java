/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.purchasemanage.purpircelist.entity;

import java.util.Calendar;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 价格清单表Entity
 * @author zz
 * @version 2018-08-20
 */
public class PurPriceList extends DataEntity<PurPriceList> {
	
	private static final long serialVersionUID = 1L;
	private String serialNum;		// 序号
	private String supId;		// 供应商编码
	private String supName;		// 供应商名称
	private String itemCode;		// 物料编码
	private String itemName;		// 物料名称
	private String unit;		// 单位
	private String priceTaxed;		// 含税价格
	private String priceRange;		// 阶梯价格数量范围
	private String taxRatio;		// 税率
	private Date creatDateItem;		// 创建日期
	private String notes;		// 备注
	private Date beginCreatDateItem;		// 开始 创建日期
	private Date endCreatDateItem;		// 结束 创建日期
	
	public PurPriceList() {
		super();
	}

	public PurPriceList(String id){
		super(id);
	}

	@ExcelField(title="序号", align=2, sort=7)
	public String getSerialNum() {
		return serialNum;
	}

	public void setSerialNum(String serialNum) {
		this.serialNum = serialNum;
	}
	
	@ExcelField(title="供应商编码", align=2, sort=8)
	public String getSupId() {
		return supId;
	}

	public void setSupId(String supId) {
		this.supId = supId;
	}
	
	@ExcelField(title="供应商名称", align=2, sort=9)
	public String getSupName() {
		return supName;
	}

	public void setSupName(String supName) {
		this.supName = supName;
	}
	
	@ExcelField(title="物料编码", align=2, sort=10)
	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}
	
	@ExcelField(title="物料名称", align=2, sort=11)
	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	
	@ExcelField(title="单位", align=2, sort=12)
	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}
	
	@ExcelField(title="含税价格", align=2, sort=13)
	public String getPriceTaxed() {
		return priceTaxed;
	}

	public void setPriceTaxed(String priceTaxed) {
		this.priceTaxed = priceTaxed;
	}
	
	@ExcelField(title="阶梯价格数量范围", align=2, sort=14)
	public String getPriceRange() {
		return priceRange;
	}

	public void setPriceRange(String priceRange) {
		this.priceRange = priceRange;
	}
	
	@ExcelField(title="税率", align=2, sort=15)
	public String getTaxRatio() {
		return taxRatio;
	}

	public void setTaxRatio(String taxRatio) {
		this.taxRatio = taxRatio;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="创建日期", align=2, sort=16)
	public Date getCreatDateItem() {
		return creatDateItem;
	}

	public void setCreatDateItem(Date creatDateItem) {
		this.creatDateItem = creatDateItem;
	}
	
	@ExcelField(title="备注", align=2, sort=17)
	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}
	
	public Date getBeginCreatDateItem() {
		return beginCreatDateItem;
	}

	public void setBeginCreatDateItem(Date beginCreatDateItem) {
		this.beginCreatDateItem = beginCreatDateItem;
	}
	
	public Date getEndCreatDateItem() {
		return endCreatDateItem;
	}

	public void setEndCreatDateItem(Date endCreatDateItem) {
		if(endCreatDateItem != null){
			Calendar c = Calendar.getInstance();
			c.setTime(endCreatDateItem);
			c.add(Calendar.DAY_OF_MONTH, 1);
			this.endCreatDateItem = c.getTime();
		}
		else{
			this.endCreatDateItem = endCreatDateItem;
		}
	}
		
}