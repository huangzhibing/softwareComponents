/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.basedata.routing.entity;

import com.hqu.modules.basedata.item.entity.Item;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 工艺路线物料关系表Entity
 * @author hzm
 * @version 2018-08-26
 */
public class RoutingItem extends DataEntity<RoutingItem> {
	
	private static final long serialVersionUID = 1L;
	private Routing routingCode;		// 工艺编码 父类
	private Item item;		// 物料编码
	private String reqQty;		// 需求数量
	private String remark;		// 备注
	private String productCode; //产品编码

	public RoutingItem() {
		super();
	}

	public RoutingItem(String id){
		super(id);
	}

	public RoutingItem(Routing routingCode){
		this.routingCode = routingCode;
	}

	public Routing getRoutingCode() {
		return routingCode;
	}

	public void setRoutingCode(Routing routingCode) {
		this.routingCode = routingCode;
	}
	
	@ExcelField(title="物料编码", fieldType=Item.class, value="", align=2, sort=8)
	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}
	
	@ExcelField(title="需求数量", align=2, sort=9)
	public String getReqQty() {
		return reqQty;
	}

	public void setReqQty(String reqQty) {
		this.reqQty = reqQty;
	}
	
	@ExcelField(title="备注", align=2, sort=10)
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
}