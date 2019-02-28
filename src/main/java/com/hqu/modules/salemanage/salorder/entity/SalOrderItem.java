/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.salemanage.salorder.entity;

import com.hqu.modules.basedata.product.entity.Product;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 内部订单子表Entity
 * @author dongqida
 * @version 2018-05-27
 */
public class SalOrderItem extends DataEntity<SalOrderItem> {
	
	private static final long serialVersionUID = 1L;
	private SalOrder orderCode;		// 订单编码 父类
	private String seqId;		// 序号
	private Product prodCode;		// 产品编码
	private String prodName;		// 产品名称
	private String prodSpec;		// 规格型号
	private String unitName;		// 单位名称
	private Double prodQty;		// 签订量
	private Double prodPrice;		// 无税单价
	private Double prodSum;		// 无税总额
	private Double prodPriceTaxed;		// 含税单价
	private Double prodSumTaxed;		// 含税金额
	
	public SalOrderItem() {
		super();
	}

	public SalOrderItem(String id){
		super(id);
	}

	public SalOrderItem(SalOrder orderCode){
		this.orderCode = orderCode;
	}

	public SalOrder getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(SalOrder orderCode) {
		this.orderCode = orderCode;
	}
	
	@ExcelField(title="序号", align=2, sort=8)
	public String getSeqId() {
		return seqId;
	}

	public void setSeqId(String seqId) {
		this.seqId = seqId;
	}
	
	@ExcelField(title="产品编码", align=2, sort=9)
	public Product getProdCode() {
		return prodCode;
	}

	public void setProdCode(Product prodCode) {
		this.prodCode = prodCode;
	}
	
	@ExcelField(title="产品名称", align=2, sort=10)
	public String getProdName() {
		return prodName;
	}

	public void setProdName(String prodName) {
		this.prodName = prodName;
	}
	
	@ExcelField(title="规格型号", align=2, sort=11)
	public String getProdSpec() {
		return prodSpec;
	}

	public void setProdSpec(String prodSpec) {
		this.prodSpec = prodSpec;
	}
	
	@ExcelField(title="单位名称", align=2, sort=12)
	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}
	
	@ExcelField(title="签订量", align=2, sort=13)
	public Double getProdQty() {
		return prodQty;
	}

	public void setProdQty(Double prodQty) {
		this.prodQty = prodQty;
	}
	
	@ExcelField(title="无税单价", align=2, sort=14)
	public Double getProdPrice() {
		return prodPrice;
	}

	public void setProdPrice(Double prodPrice) {
		this.prodPrice = prodPrice;
	}
	
	@ExcelField(title="无税总额", align=2, sort=15)
	public Double getProdSum() {
		return prodSum;
	}

	public void setProdSum(Double prodSum) {
		this.prodSum = prodSum;
	}
	
	@ExcelField(title="含税单价", align=2, sort=16)
	public Double getProdPriceTaxed() {
		return prodPriceTaxed;
	}

	public void setProdPriceTaxed(Double prodPriceTaxed) {
		this.prodPriceTaxed = prodPriceTaxed;
	}
	
	@ExcelField(title="含税金额", align=2, sort=17)
	public Double getProdSumTaxed() {
		return prodSumTaxed;
	}

	public void setProdSumTaxed(Double prodSumTaxed) {
		this.prodSumTaxed = prodSumTaxed;
	}
	
}