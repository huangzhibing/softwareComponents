/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.basedata.product.entity;

import com.hqu.modules.basedata.item.entity.Item;
import javax.validation.constraints.NotNull;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 产品维护Entity
 * @author M1ngz
 * @version 2018-04-06
 */
public class Product extends DataEntity<Product> {
	
	private static final long serialVersionUID = 1L;
	private Item item;		// 产品编码
	private String itemNameRu;		// 产品名称
	private String itemPdn;		// 产品图号
	private String itemSpec;		// 产品规格
	private String itemMeasure;		// 单位
	private String leadTimeAssemble;		// 装配提前期
	private String itemName;		// 产品名称

	private String itemCode;


	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public Product() {
		super();
	}

	public Product(String id){
		super(id);
	}

	@NotNull(message="产品编码不能为空")
	@ExcelField(title="产品编码", fieldType=Item.class, value="item. code", align=2, sort=6)
	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}
	
	@ExcelField(title="产品名称", align=2, sort=7)
	public String getItemNameRu() {
		return itemNameRu;
	}

	public void setItemNameRu(String itemNameRu) {
		this.itemNameRu = itemNameRu;
	}
	
	@ExcelField(title="产品图号", align=2, sort=8)
	public String getItemPdn() {
		return itemPdn;
	}

	public void setItemPdn(String itemPdn) {
		this.itemPdn = itemPdn;
	}
	
	@ExcelField(title="产品规格", align=2, sort=9)
	public String getItemSpec() {
		return itemSpec;
	}

	public void setItemSpec(String itemSpec) {
		this.itemSpec = itemSpec;
	}
	
	@ExcelField(title="单位", align=2, sort=10)
	public String getItemMeasure() {
		return itemMeasure;
	}

	public void setItemMeasure(String itemMeasure) {
		this.itemMeasure = itemMeasure;
	}
	
	@ExcelField(title="装配提前期", align=2, sort=11)
	public String getLeadTimeAssemble() {
		return leadTimeAssemble;
	}

	public void setLeadTimeAssemble(String leadTimeAssemble) {
		this.leadTimeAssemble = leadTimeAssemble;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
}