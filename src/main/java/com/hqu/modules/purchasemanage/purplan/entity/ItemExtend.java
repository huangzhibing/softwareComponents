package com.hqu.modules.purchasemanage.purplan.entity;

import com.hqu.modules.basedata.item.entity.Item;

public class ItemExtend extends Item {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Double costPrice;
	private Double nowQty;
	public Double getCostPrice() {
		return costPrice;
	}
	public void setCostPrice(Double costPrice) {
		this.costPrice = costPrice;
	}
	public Double getNowQty() {
		return nowQty;
	}
	public void setNowQty(Double nowQty) {
		this.nowQty = nowQty;
	}
	
	
}
