package com.hqu.modules.purchasemanage.applymain.entity;

import com.hqu.modules.basedata.item.entity.Item;

public class ItemCustmer extends Item{
	private String nowSum;		// 库存量
	private String costPrice;		// 移动平均价
	
	public String getNowSum() {
		return nowSum;
	}
	public void setNowSum(String nowSum) {
		this.nowSum = nowSum;
	}
	public String getCostPrice() {
		return costPrice;
	}
	public void setCostPrice(String costPrice) {
		this.costPrice = costPrice;
	}
	
	

}
