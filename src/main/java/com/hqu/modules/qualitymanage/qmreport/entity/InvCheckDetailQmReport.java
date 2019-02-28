/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.qualitymanage.qmreport.entity;

import com.hqu.modules.basedata.item.entity.Item;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 到货子表持久层Entity
 * @author yxb
 * @version 2018-05-22
 */
public class InvCheckDetailQmReport extends DataEntity<InvCheckDetailQmReport> {
	
	private static final long serialVersionUID = 1L;
	private String billnum;		// 单据编号
	private Integer serialnum;		// 序号
	private Item item;		// 物料编号
	private String itemName;		// 物料名称
	private String massRequire;		// 质量要求
	private Integer checkQty;		// 到货数量
	private Integer realQty;		// 实际数量
	private Double realPrice;		// 实际单价
	private Double realSum;		// 实际金额
	private Double realPriceTaxed;		// 含税单价
	private Double realSumTaxed;		// 含税金额
	private Double taxSum;		// 税额
	private Double transSum;		// 不含税运费
	private Double transSumTaxed;		// 含税运费
	private String checkState;		// 作废标志
	private String notes;		// 备注
	private Double taxRatio;		// 税率
	private Double transRatio;		// 运费税率
	private String balanceFlag;		// 结算标志
	private String balanceNotes;		// 结算说明
	private String supEvalFlag;		// 供应商评价标志
	private String supEvalNotes;		// 供应商评价说明
	private Double qtSum;		// 杂费
	private Double xfSum;		// 卸费
	private Integer ycRealqty;		// 已冲实际数量
	private String corBillnum;		// 对应单号
	private Integer corSerialnum;		// 对应序号
	private Integer oldQty;		// 保留数量
	private Double oldPrice;		// 保留单价
	private Double oldSum;		// 保留金额
	private Double oldPriceTaxed;		// 保留含税单价
	private Double oldSumTaxed;		// 保留含税金额
	private Double oldTransSum;		// 保留运费
	private Double oldTransSumTaxed;		// 保留含税运费
	private String backFlag;		// 退货标志
	private String itemSpecmodel;		// 物料规格
	private String unitCode;		// 计量单位
	private String itemPdn;		// 物料图号
	private String itemNotes;		// 说明
	
	public InvCheckDetailQmReport() {
		super();
	}

	public InvCheckDetailQmReport(String id){
		super(id);
	}

	@ExcelField(title="单据编号", align=2, sort=7)
	public String getBillnum() {
		return billnum;
	}

	public void setBillnum(String billnum) {
		this.billnum = billnum;
	}
	
	@ExcelField(title="序号", align=2, sort=8)
	public Integer getSerialnum() {
		return serialnum;
	}

	public void setSerialnum(Integer serialnum) {
		this.serialnum = serialnum;
	}
	
	@ExcelField(title="物料编号", fieldType=Item.class, value="item.code", align=2, sort=9)
	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}
	
	@ExcelField(title="物料名称", align=2, sort=10)
	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	
	@ExcelField(title="质量要求", align=2, sort=11)
	public String getMassRequire() {
		return massRequire;
	}

	public void setMassRequire(String massRequire) {
		this.massRequire = massRequire;
	}
	
	@ExcelField(title="到货数量", align=2, sort=12)
	public Integer getCheckQty() {
		return checkQty;
	}

	public void setCheckQty(Integer checkQty) {
		this.checkQty = checkQty;
	}
	
	@ExcelField(title="实际数量", align=2, sort=13)
	public Integer getRealQty() {
		return realQty;
	}

	public void setRealQty(Integer realQty) {
		this.realQty = realQty;
	}
	
	@ExcelField(title="实际单价", align=2, sort=14)
	public Double getRealPrice() {
		return realPrice;
	}

	public void setRealPrice(Double realPrice) {
		this.realPrice = realPrice;
	}
	
	@ExcelField(title="实际金额", align=2, sort=15)
	public Double getRealSum() {
		return realSum;
	}

	public void setRealSum(Double realSum) {
		this.realSum = realSum;
	}
	
	@ExcelField(title="含税单价", align=2, sort=16)
	public Double getRealPriceTaxed() {
		return realPriceTaxed;
	}

	public void setRealPriceTaxed(Double realPriceTaxed) {
		this.realPriceTaxed = realPriceTaxed;
	}
	
	@ExcelField(title="含税金额", align=2, sort=17)
	public Double getRealSumTaxed() {
		return realSumTaxed;
	}

	public void setRealSumTaxed(Double realSumTaxed) {
		this.realSumTaxed = realSumTaxed;
	}
	
	@ExcelField(title="税额", align=2, sort=18)
	public Double getTaxSum() {
		return taxSum;
	}

	public void setTaxSum(Double taxSum) {
		this.taxSum = taxSum;
	}
	
	@ExcelField(title="不含税运费", align=2, sort=19)
	public Double getTransSum() {
		return transSum;
	}

	public void setTransSum(Double transSum) {
		this.transSum = transSum;
	}
	
	@ExcelField(title="含税运费", align=2, sort=20)
	public Double getTransSumTaxed() {
		return transSumTaxed;
	}

	public void setTransSumTaxed(Double transSumTaxed) {
		this.transSumTaxed = transSumTaxed;
	}
	
	@ExcelField(title="作废标志", align=2, sort=21)
	public String getCheckState() {
		return checkState;
	}

	public void setCheckState(String checkState) {
		this.checkState = checkState;
	}
	
	@ExcelField(title="备注", align=2, sort=22)
	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}
	
	@ExcelField(title="税率", align=2, sort=23)
	public Double getTaxRatio() {
		return taxRatio;
	}

	public void setTaxRatio(Double taxRatio) {
		this.taxRatio = taxRatio;
	}
	
	@ExcelField(title="运费税率", align=2, sort=24)
	public Double getTransRatio() {
		return transRatio;
	}

	public void setTransRatio(Double transRatio) {
		this.transRatio = transRatio;
	}
	
	@ExcelField(title="结算标志", align=2, sort=25)
	public String getBalanceFlag() {
		return balanceFlag;
	}

	public void setBalanceFlag(String balanceFlag) {
		this.balanceFlag = balanceFlag;
	}
	
	@ExcelField(title="结算说明", align=2, sort=26)
	public String getBalanceNotes() {
		return balanceNotes;
	}

	public void setBalanceNotes(String balanceNotes) {
		this.balanceNotes = balanceNotes;
	}
	
	@ExcelField(title="供应商评价标志", align=2, sort=27)
	public String getSupEvalFlag() {
		return supEvalFlag;
	}

	public void setSupEvalFlag(String supEvalFlag) {
		this.supEvalFlag = supEvalFlag;
	}
	
	@ExcelField(title="供应商评价说明", align=2, sort=28)
	public String getSupEvalNotes() {
		return supEvalNotes;
	}

	public void setSupEvalNotes(String supEvalNotes) {
		this.supEvalNotes = supEvalNotes;
	}
	
	@ExcelField(title="杂费", align=2, sort=29)
	public Double getQtSum() {
		return qtSum;
	}

	public void setQtSum(Double qtSum) {
		this.qtSum = qtSum;
	}
	
	@ExcelField(title="卸费", align=2, sort=30)
	public Double getXfSum() {
		return xfSum;
	}

	public void setXfSum(Double xfSum) {
		this.xfSum = xfSum;
	}
	
	@ExcelField(title="已冲实际数量", align=2, sort=31)
	public Integer getYcRealqty() {
		return ycRealqty;
	}

	public void setYcRealqty(Integer ycRealqty) {
		this.ycRealqty = ycRealqty;
	}
	
	@ExcelField(title="对应单号", align=2, sort=32)
	public String getCorBillnum() {
		return corBillnum;
	}

	public void setCorBillnum(String corBillnum) {
		this.corBillnum = corBillnum;
	}
	
	@ExcelField(title="对应序号", align=2, sort=33)
	public Integer getCorSerialnum() {
		return corSerialnum;
	}

	public void setCorSerialnum(Integer corSerialnum) {
		this.corSerialnum = corSerialnum;
	}
	
	@ExcelField(title="保留数量", align=2, sort=34)
	public Integer getOldQty() {
		return oldQty;
	}

	public void setOldQty(Integer oldQty) {
		this.oldQty = oldQty;
	}
	
	@ExcelField(title="保留单价", align=2, sort=35)
	public Double getOldPrice() {
		return oldPrice;
	}

	public void setOldPrice(Double oldPrice) {
		this.oldPrice = oldPrice;
	}
	
	@ExcelField(title="保留金额", align=2, sort=36)
	public Double getOldSum() {
		return oldSum;
	}

	public void setOldSum(Double oldSum) {
		this.oldSum = oldSum;
	}
	
	@ExcelField(title="保留含税单价", align=2, sort=37)
	public Double getOldPriceTaxed() {
		return oldPriceTaxed;
	}

	public void setOldPriceTaxed(Double oldPriceTaxed) {
		this.oldPriceTaxed = oldPriceTaxed;
	}
	
	@ExcelField(title="保留含税金额", align=2, sort=38)
	public Double getOldSumTaxed() {
		return oldSumTaxed;
	}

	public void setOldSumTaxed(Double oldSumTaxed) {
		this.oldSumTaxed = oldSumTaxed;
	}
	
	@ExcelField(title="保留运费", align=2, sort=39)
	public Double getOldTransSum() {
		return oldTransSum;
	}

	public void setOldTransSum(Double oldTransSum) {
		this.oldTransSum = oldTransSum;
	}
	
	@ExcelField(title="保留含税运费", align=2, sort=40)
	public Double getOldTransSumTaxed() {
		return oldTransSumTaxed;
	}

	public void setOldTransSumTaxed(Double oldTransSumTaxed) {
		this.oldTransSumTaxed = oldTransSumTaxed;
	}
	
	@ExcelField(title="退货标志", align=2, sort=41)
	public String getBackFlag() {
		return backFlag;
	}

	public void setBackFlag(String backFlag) {
		this.backFlag = backFlag;
	}
	
	@ExcelField(title="物料规格", align=2, sort=42)
	public String getItemSpecmodel() {
		return itemSpecmodel;
	}

	public void setItemSpecmodel(String itemSpecmodel) {
		this.itemSpecmodel = itemSpecmodel;
	}
	
	@ExcelField(title="计量单位", align=2, sort=43)
	public String getUnitCode() {
		return unitCode;
	}

	public void setUnitCode(String unitCode) {
		this.unitCode = unitCode;
	}
	
	@ExcelField(title="物料图号", align=2, sort=44)
	public String getItemPdn() {
		return itemPdn;
	}

	public void setItemPdn(String itemPdn) {
		this.itemPdn = itemPdn;
	}
	
	@ExcelField(title="说明", align=2, sort=45)
	public String getItemNotes() {
		return itemNotes;
	}

	public void setItemNotes(String itemNotes) {
		this.itemNotes = itemNotes;
	}
	
}