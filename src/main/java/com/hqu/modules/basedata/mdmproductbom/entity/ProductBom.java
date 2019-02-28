/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.basedata.mdmproductbom.entity;

import com.hqu.modules.basedata.product.entity.Product;
import javax.validation.constraints.NotNull;
import com.hqu.modules.basedata.item.entity.Item;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 产品结构维护Entity
 * @author m1ngz
 * @version 2018-06-04
 */
public class ProductBom extends DataEntity<ProductBom> {
	
	private static final long serialVersionUID = 1L;
	private Product product;		// 产品编码
	private String productItemName;		// 产品名称
	private Item item;		// 零件编码
	private String itemName;		// 零件名称
	private String itemPdn;		// 零件图号
	private String itemSpec;		// 零件规格
	private String itemMeasure;		// 零件单位
	private String leadTimeAssemble;		// 装配提前期
	private String numInFather;		// 在父项中的数量
	private Item fatherItem;		// 父零件编码
	private String fatherItemName;		// 父零件名称
	private String fatherItemPdn;		// 父零件图号
	private String fatherItemSpec;		// 父零件规格
	private String isLeaf;		// 叶节点标识
	
	public ProductBom() {
		super();
	}

	public ProductBom(String id){
		super(id);
	}

	@NotNull(message="产品编码不能为空")
	@ExcelField(title="产品编码", fieldType=Product.class, value="product.itemName", align=2, sort=6)
	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}
	
	@ExcelField(title="产品名称", align=2, sort=7)
	public String getProductItemName() {
		return productItemName;
	}

	public void setProductItemName(String productItemName) {
		this.productItemName = productItemName;
	}
	
	@NotNull(message="零件编码不能为空")
	@ExcelField(title="零件编码", fieldType=Item.class, value="item.name", align=2, sort=8)
	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}
	
	@ExcelField(title="零件名称", align=2, sort=9)
	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	
	@ExcelField(title="零件图号", align=2, sort=10)
	public String getItemPdn() {
		return itemPdn;
	}

	public void setItemPdn(String itemPdn) {
		this.itemPdn = itemPdn;
	}
	
	@ExcelField(title="零件规格", align=2, sort=11)
	public String getItemSpec() {
		return itemSpec;
	}

	public void setItemSpec(String itemSpec) {
		this.itemSpec = itemSpec;
	}
	
	@ExcelField(title="零件单位", align=2, sort=12)
	public String getItemMeasure() {
		return itemMeasure;
	}

	public void setItemMeasure(String itemMeasure) {
		this.itemMeasure = itemMeasure;
	}
	
	@ExcelField(title="装配提前期", align=2, sort=13)
	public String getLeadTimeAssemble() {
		return leadTimeAssemble;
	}

	public void setLeadTimeAssemble(String leadTimeAssemble) {
		this.leadTimeAssemble = leadTimeAssemble;
	}
	
	@ExcelField(title="在父项中的数量", align=2, sort=14)
	public String getNumInFather() {
		return numInFather;
	}

	public void setNumInFather(String numInFather) {
		this.numInFather = numInFather;
	}
	
	@NotNull(message="父零件编码不能为空")
	@ExcelField(title="父零件编码", fieldType=Item.class, value="fatherItem.name", align=2, sort=15)
	public Item getFatherItem() {
		return fatherItem;
	}

	public void setFatherItem(Item fatherItem) {
		this.fatherItem = fatherItem;
	}
	
	@ExcelField(title="父零件名称", align=2, sort=16)
	public String getFatherItemName() {
		return fatherItemName;
	}

	public void setFatherItemName(String fatherItemName) {
		this.fatherItemName = fatherItemName;
	}
	
	@ExcelField(title="父零件图号", align=2, sort=17)
	public String getFatherItemPdn() {
		return fatherItemPdn;
	}

	public void setFatherItemPdn(String fatherItemPdn) {
		this.fatherItemPdn = fatherItemPdn;
	}
	
	@ExcelField(title="父零件规格", align=2, sort=18)
	public String getFatherItemSpec() {
		return fatherItemSpec;
	}

	public void setFatherItemSpec(String fatherItemSpec) {
		this.fatherItemSpec = fatherItemSpec;
	}
	
	@ExcelField(title="叶节点标识", align=2, sort=19)
	public String getIsLeaf() {
		return isLeaf;
	}

	public void setIsLeaf(String isLeaf) {
		this.isLeaf = isLeaf;
	}
	
}