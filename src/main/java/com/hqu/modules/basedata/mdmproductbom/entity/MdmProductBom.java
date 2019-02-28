/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.basedata.mdmproductbom.entity;


import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 产品结构维护Entity
 * @author liujiachen
 * @version 2018-04-03
 */
public class MdmProductBom extends DataEntity<MdmProductBom> {
	
	private static final long serialVersionUID = 1L;
	private String productItemCode;		// 产品编码
	private String productItemName;		// 产品名称
	private String itemCode;		// 零件编码(物料)编码
	private String itemName;		// 零件编码(物料)名称
	private String itemPdn;		// 零件编码(物料)图号
	private String itemSpec;		// 零件编码(物料)规格
	private String itemMeasure;		// 零件编码(物料)单位
	private String leadTimeAssemble;		// 装配提前期
	private String numInFather;		// 在父项中的数量
	private String fatherItemCode;		// 父零件(物料)编码
	private String fatherItemName;		// 父零件(物料)名称
	private String fatherItemPdn;		// 父零件(物料)图号
	private String fatherItemSpec;		// 父零件(物料)规格
	private String isLeaf;		// 叶节点标识(是否末级)
	
	public MdmProductBom() {
		super();
	}

	public MdmProductBom(String id){
		super(id);
	}

	@ExcelField(title="产品编码", dictType="", align=2, sort=6)
	public String getProductItemCode() {
		return productItemCode;
	}

	public void setProductItemCode(String productItemCode) {
		this.productItemCode = productItemCode;
	}
	
	@ExcelField(title="产品名称", align=2, sort=7)
	public String getProductItemName() {
		return productItemName;
	}

	public void setProductItemName(String productItemName) {
		this.productItemName = productItemName;
	}
	
	@ExcelField(title="零件编码(物料)编码", dictType="", align=2, sort=8)
	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}
	
	@ExcelField(title="零件编码(物料)名称", align=2, sort=9)
	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	
	@ExcelField(title="零件编码(物料)图号", align=2, sort=10)
	public String getItemPdn() {
		return itemPdn;
	}

	public void setItemPdn(String itemPdn) {
		this.itemPdn = itemPdn;
	}
	
	@ExcelField(title="零件编码(物料)规格", align=2, sort=11)
	public String getItemSpec() {
		return itemSpec;
	}

	public void setItemSpec(String itemSpec) {
		this.itemSpec = itemSpec;
	}
	
	@ExcelField(title="零件编码(物料)单位", align=2, sort=12)
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
	
	@ExcelField(title="父零件(物料)编码", dictType="", align=2, sort=15)
	public String getFatherItemCode() {
		return fatherItemCode;
	}

	public void setFatherItemCode(String fatherItemCode) {
		this.fatherItemCode = fatherItemCode;
	}
	
	@ExcelField(title="父零件(物料)名称", align=2, sort=16)
	public String getFatherItemName() {
		return fatherItemName;
	}

	public void setFatherItemName(String fatherItemName) {
		this.fatherItemName = fatherItemName;
	}
	
	@ExcelField(title="父零件(物料)图号", align=2, sort=17)
	public String getFatherItemPdn() {
		return fatherItemPdn;
	}

	public void setFatherItemPdn(String fatherItemPdn) {
		this.fatherItemPdn = fatherItemPdn;
	}
	
	@ExcelField(title="父零件(物料)规格", align=2, sort=18)
	public String getFatherItemSpec() {
		return fatherItemSpec;
	}

	public void setFatherItemSpec(String fatherItemSpec) {
		this.fatherItemSpec = fatherItemSpec;
	}
	
	@ExcelField(title="叶节点标识(是否末级)", align=2, sort=19)
	public String getIsLeaf() {
		return isLeaf;
	}

	public void setIsLeaf(String isLeaf) {
		this.isLeaf = isLeaf;
	}
	
}