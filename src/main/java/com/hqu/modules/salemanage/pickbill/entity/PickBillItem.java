/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.salemanage.pickbill.entity;

import com.hqu.modules.basedata.product.entity.Product;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 销售发货单子表Entity
 * @author M1ngz
 * @version 2018-05-15
 */
public class PickBillItem extends DataEntity<PickBillItem> {
	
	private static final long serialVersionUID = 1L;
	private PickBill pbillCode;		// 发货单编码 父类
	private String seqId;		// 序号
	private String itemCode;		// 订单编号
	private Product prodCode;		// 产品编码
	private String prodName;		// 产品名称
	private String prodSpec;		// 产品规格
	private String unitName;		// 单位
	private Double pickQty;		// 发货量
	private Double taxRatio;		// 税率
	private Double prodPrice;		// 无税单价
	private Double prodSum;		// 无税总额
	private Double prodPriceTaxed;		// 含税单价
	private Double prodSumTaxed;		// 含税总额
	private Double transPrice;		// 运费价格
	private Double transSum;		// 运费总额
	
	public PickBillItem() {
		super();
	}

	public PickBillItem(String id){
		super(id);
	}

	public PickBillItem(PickBill pbillCode){
		this.pbillCode = pbillCode;
	}

	public PickBill getPbillCode() {
		return pbillCode;
	}

	public void setPbillCode(PickBill pbillCode) {
		this.pbillCode = pbillCode;
	}
	
	@ExcelField(title="序号", align=2, sort=8)
	public String getSeqId() {
		return seqId;
	}

	public void setSeqId(String seqId) {
		this.seqId = seqId;
	}
	
	@ExcelField(title="订单编号", align=2, sort=9)
	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}
	
	@ExcelField(title="产品编码", fieldType=Product.class, value="prodCode.item.code", align=2, sort=10)
	public Product getProdCode() {
		return prodCode;
	}

	public void setProdCode(Product prodCode) {
		this.prodCode = prodCode;
	}
	
	@ExcelField(title="产品名称", align=2, sort=11)
	public String getProdName() {
		return prodName;
	}

	public void setProdName(String prodName) {
		this.prodName = prodName;
	}
	
	@ExcelField(title="产品规格", align=2, sort=12)
	public String getProdSpec() {
		return prodSpec;
	}

	public void setProdSpec(String prodSpec) {
		this.prodSpec = prodSpec;
	}
	
	@ExcelField(title="单位", align=2, sort=13)
	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}
	
	@ExcelField(title="发货量", align=2, sort=14)
	public Double getPickQty() {
		return pickQty;
	}

	public void setPickQty(Double pickQty) {
		this.pickQty = pickQty;
	}
	
	@ExcelField(title="税率", align=2, sort=15)
	public Double getTaxRatio() {
		return taxRatio;
	}

	public void setTaxRatio(Double taxRatio) {
		this.taxRatio = taxRatio;
	}
	
	@ExcelField(title="无税单价", align=2, sort=16)
	public Double getProdPrice() {
		return prodPrice;
	}

	public void setProdPrice(Double prodPrice) {
		this.prodPrice = prodPrice;
	}
	
	@ExcelField(title="无税总额", align=2, sort=17)
	public Double getProdSum() {
		return prodSum;
	}

	public void setProdSum(Double prodSum) {
		this.prodSum = prodSum;
	}
	
	@ExcelField(title="含税单价", align=2, sort=18)
	public Double getProdPriceTaxed() {
		return prodPriceTaxed;
	}

	public void setProdPriceTaxed(Double prodPriceTaxed) {
		this.prodPriceTaxed = prodPriceTaxed;
	}
	
	@ExcelField(title="含税总额", align=2, sort=19)
	public Double getProdSumTaxed() {
		return prodSumTaxed;
	}

	public void setProdSumTaxed(Double prodSumTaxed) {
		this.prodSumTaxed = prodSumTaxed;
	}
	
	@ExcelField(title="运费价格", align=2, sort=20)
	public Double getTransPrice() {
		return transPrice;
	}

	public void setTransPrice(Double transPrice) {
		this.transPrice = transPrice;
	}
	
	@ExcelField(title="运费总额", align=2, sort=21)
	public Double getTransSum() {
		return transSum;
	}

	public void setTransSum(Double transSum) {
		this.transSum = transSum;
	}
	
}