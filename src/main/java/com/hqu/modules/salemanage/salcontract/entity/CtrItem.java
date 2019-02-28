/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.salemanage.salcontract.entity;


import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 销售合同子表Entity
 * @author dongqida
 * @version 2018-05-12
 */
public class CtrItem extends DataEntity<CtrItem> {
	
	private static final long serialVersionUID = 1L;
	private Contract contract;		// 合同编码 父类
	private String itemCode;		// 订单编号
	private String prodCode;		// 产品编码
	private String prodName;		// 产品名称
	private String prodSpec;		// 规格型号
	private String unitName;		// 单位名称
	private Double prodQty;		// 签订量
	private Double prodPrice;		// 无税单价
	private String prodSum;		// 无税总额
	private Double prodPriceTaxed;		// 含税单价	
	private Double prodSumTaxed;		// 含税金额
	private Double pickQty;		// 发货量
	private Double transPrice;		// 运费价格
	private Double transSum;		// 运费金额
	private String periodId;		// 订单期间
	private String remark;		// 备注
	private String productCode;//产品编码
	
	public CtrItem() {
		super();
	}

	public CtrItem(String id){
		super(id);
	}

	public CtrItem(Contract contract){
		this.contract = contract;
	}


	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public Contract getContract() {
		return contract;
	}

	public void setContract(Contract contract) {
		this.contract = contract;
	}
	
	@ExcelField(title="订单编号", align=2, sort=8)
	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}
	
	@ExcelField(title="产品编码", align=2, sort=9)
	public String getProdCode() {
		return prodCode;
	}

	public void setProdCode(String prodCode) {
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
	public String getProdSum() {
		return prodSum;
	}

	public void setProdSum(String prodSum) {
		this.prodSum = prodSum;
	}
	
	@ExcelField(title="含税单价	", align=2, sort=16)
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
	
	@ExcelField(title="发货量", align=2, sort=18)
	public Double getPickQty() {
		return pickQty;
	}

	public void setPickQty(Double pickQty) {
		this.pickQty = pickQty;
	}
	
	@ExcelField(title="运费价格", align=2, sort=19)
	public Double getTransPrice() {
		return transPrice;
	}

	public void setTransPrice(Double transPrice) {
		this.transPrice = transPrice;
	}
	
	@ExcelField(title="运费金额", align=2, sort=20)
	public Double getTransSum() {
		return transSum;
	}

	public void setTransSum(Double transSum) {
		this.transSum = transSum;
	}
	
	@ExcelField(title="订单期间", align=2, sort=21)
	public String getPeriodId() {
		return periodId;
	}

	public void setPeriodId(String periodId) {
		this.periodId = periodId;
	}
	
	@ExcelField(title="备注", align=2, sort=22)
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
}