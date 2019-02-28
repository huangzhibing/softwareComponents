/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.workshopmanage.sfcinvcheckmain.entity;

import javax.validation.constraints.NotNull;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 完工入库通知单子表Entity
 * @author zhangxin
 * @version 2018-06-01
 */
public class SfcInvCheckDetail extends DataEntity<SfcInvCheckDetail> {
	
	private static final long serialVersionUID = 1L;
	private SfcInvCheckMain sfcInvCheckMain;		// 单据编号 父类
	private Integer serialNo;		// 序号
	private String itemCode;		// 物料编号
	private String itemName;		// 物料名称
	private String mSerialNo; //机器序列号
	private String massRequire;		// 质量要求
	private String objSn;		// 检验对象序列号
	private String itemBarcode;		// 物料二维码
	private Double realQty;		// 实际数量
	private Double realPrice;		// 实际单价
	private Double realSum;		// 实际金额
	private Double realPriceTaxed;		// 含税单价
	private Double realSumTaxed;		// 含税金额
	private Double taxSum;		// 税额
	private String checkState;		// 作废标志
	private String notes;		// 备注
	private String balanceNotes;		// 结算说明
	private String balanceFlag;		// 结算标志
	private String corBillnum;		// 对应单号
	private Integer corSerialnum;		// 对应序号
	private String backFlag;		// 退货标志
	private String itemSpecmodel;		// 物料规格
	private String unitCode;		// 计量单位
	private String itemPdn;		// 物料图号
	private String billNoDetail; //实际的单据号，用于追溯
	
	public SfcInvCheckDetail() {
		super();
	}

	public SfcInvCheckDetail(String id){
		super(id);
	}

	public SfcInvCheckDetail(SfcInvCheckMain sfcInvCheckMain){
		this.sfcInvCheckMain = sfcInvCheckMain;
	}

	public SfcInvCheckMain getSfcInvCheckMain() {
		return sfcInvCheckMain;
	}

	public void setSfcInvCheckMain(SfcInvCheckMain sfcInvCheckMain) {
		this.sfcInvCheckMain = sfcInvCheckMain;
	}
	
	@NotNull(message="序号不能为空")
	@ExcelField(title="序号", align=2, sort=8)
	public Integer getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(Integer serialNo) {
		this.serialNo = serialNo;
	}
	
	@ExcelField(title="物料编号", align=2, sort=9)
	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}
	
	@ExcelField(title="物料名称", align=2, sort=10)
	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	@ExcelField(title="机器序列号", align=2, sort=10)
	public String getmSerialNo() {
		return mSerialNo;
	}

	public void setMSerialNo(String mSerialNo) {
		this.mSerialNo = mSerialNo;
	}
	
	@ExcelField(title="质量要求", align=2, sort=11)
	public String getMassRequire() {
		return massRequire;
	}

	public void setMassRequire(String massRequire) {
		this.massRequire = massRequire;
	}
	
	@ExcelField(title="检验对象序列号", align=2, sort=12)
	public String getObjSn() {
		return objSn;
	}

	public void setObjSn(String objSn) {
		this.objSn = objSn;
	}
	
	@ExcelField(title="物料二维码", align=2, sort=13)
	public String getItemBarcode() {
		return itemBarcode;
	}

	public void setItemBarcode(String itemBarcode) {
		this.itemBarcode = itemBarcode;
	}
	
	@ExcelField(title="实际数量", align=2, sort=14)
	public Double getRealQty() {
		return realQty;
	}

	public void setRealQty(Double realQty) {
		this.realQty = realQty;
	}
	
	@ExcelField(title="实际单价", align=2, sort=15)
	public Double getRealPrice() {
		return realPrice;
	}

	public void setRealPrice(Double realPrice) {
		this.realPrice = realPrice;
	}
	
	@ExcelField(title="实际金额", align=2, sort=16)
	public Double getRealSum() {
		return realSum;
	}

	public void setRealSum(Double realSum) {
		this.realSum = realSum;
	}
	
	@ExcelField(title="含税单价", align=2, sort=17)
	public Double getRealPriceTaxed() {
		return realPriceTaxed;
	}

	public void setRealPriceTaxed(Double realPriceTaxed) {
		this.realPriceTaxed = realPriceTaxed;
	}
	
	@ExcelField(title="含税金额", align=2, sort=18)
	public Double getRealSumTaxed() {
		return realSumTaxed;
	}

	public void setRealSumTaxed(Double realSumTaxed) {
		this.realSumTaxed = realSumTaxed;
	}
	
	@ExcelField(title="税额", align=2, sort=19)
	public Double getTaxSum() {
		return taxSum;
	}

	public void setTaxSum(Double taxSum) {
		this.taxSum = taxSum;
	}
	
	@ExcelField(title="作废标志", align=2, sort=20)
	public String getCheckState() {
		return checkState;
	}

	public void setCheckState(String checkState) {
		this.checkState = checkState;
	}
	
	@ExcelField(title="备注", align=2, sort=21)
	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}
	
	@ExcelField(title="结算说明", align=2, sort=22)
	public String getBalanceNotes() {
		return balanceNotes;
	}

	public void setBalanceNotes(String balanceNotes) {
		this.balanceNotes = balanceNotes;
	}
	
	@ExcelField(title="结算标志", align=2, sort=23)
	public String getBalanceFlag() {
		return balanceFlag;
	}

	public void setBalanceFlag(String balanceFlag) {
		this.balanceFlag = balanceFlag;
	}
	
	@ExcelField(title="对应单号", align=2, sort=24)
	public String getCorBillnum() {
		return corBillnum;
	}

	public void setCorBillnum(String corBillnum) {
		this.corBillnum = corBillnum;
	}
	
	@ExcelField(title="对应序号", align=2, sort=25)
	public Integer getCorSerialnum() {
		return corSerialnum;
	}

	public void setCorSerialnum(Integer corSerialnum) {
		this.corSerialnum = corSerialnum;
	}
	
	@ExcelField(title="退货标志", align=2, sort=26)
	public String getBackFlag() {
		return backFlag;
	}

	public void setBackFlag(String backFlag) {
		this.backFlag = backFlag;
	}
	
	@ExcelField(title="物料规格", align=2, sort=27)
	public String getItemSpecmodel() {
		return itemSpecmodel;
	}

	public void setItemSpecmodel(String itemSpecmodel) {
		this.itemSpecmodel = itemSpecmodel;
	}
	
	@ExcelField(title="计量单位", align=2, sort=28)
	public String getUnitCode() {
		return unitCode;
	}

	public void setUnitCode(String unitCode) {
		this.unitCode = unitCode;
	}
	
	@ExcelField(title="物料图号", align=2, sort=29)
	public String getItemPdn() {
		return itemPdn;
	}

	public void setItemPdn(String itemPdn) {
		this.itemPdn = itemPdn;
	}
	
	@ExcelField(title="物料图号", align=2, sort=29)
	public String getBillNoDetail() {
		return billNoDetail;
	}

	public void setBillNoDetail(String billNoDetail) {
		this.billNoDetail = billNoDetail;
	}



}