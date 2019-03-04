/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.qualitymanage.purinvcheckmain.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hqu.modules.basedata.item.entity.Item;
import com.jeeplus.common.utils.excel.annotation.ExcelField;
import com.jeeplus.core.persistence.DataEntity;

import java.util.Date;

/**
 * 入库通知单子表Entity
 * @author 张铮
 * @version 2018-04-21
 */
public class InvCheckDetail extends DataEntity<InvCheckDetail> {
	
	private static final long serialVersionUID = 1L;
	private InvCheckMain InvCheckMain;		// 单据编号 父类
	private Integer serialnum;		// 序号
	private Item item;		// 物料编号
	private String itemId;  //物料id @update
	private String itemCode;        //物料编号 @update
	private String itemName;		// 物料名称
	private String massRequire;		// 质量要求
	private Double checkQty;		// 到货数量
	private Double realQty;		// 实际数量
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
	private Double ycRealqty;		// 已冲实际数量
	private String corBillnum;		// 对应单号
	private Double corSerialnum;		// 对应序号
	private Double oldQty;		// 保留数量
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
	private String qmsFlag;			//质检与否
	
	//合同查询属性
	private String conBillNum;  //合同单号
	private String planBillNum; //合同生成到货时，合同所对应的计划单号
	private String billStateFlag;//到货状态
	private Date arriveDate;  //到货日期
	
	private String frontBillNum;     
	private Integer frontSerialNum;
	private Double frontQty;
	private Double maxQty;//可获得的最高到货量，到货量不大于计划或者合同量时使用
	private Integer isPrint;//物料打印标志

	private Item itemInvAccount;		// 库存账物料
	private String itemInvAccountId;		// 库存账物料id
	private String itemInvAccountCode;		// 库存账物料编码
	
	
	
	public String getFrontBillNum() {
		return frontBillNum;
	}

	public void setFrontBillNum(String frontBillNum) {
		this.frontBillNum = frontBillNum;
	}

	public Integer getFrontSerialNum() {
		return frontSerialNum;
	}

	public void setFrontSerialNum(Integer frontSerialNum) {
		this.frontSerialNum = frontSerialNum;
	}

	public Double getFrontQty() {
		return frontQty;
	}

	public void setFrontQty(Double frontQty) {
		this.frontQty = frontQty;
	}
	
	public String getBillStateFlag() {
		return billStateFlag;
	}

	public void setBillStateFlag(String billStateFlag) {
		this.billStateFlag = billStateFlag;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="到货日期", align=2, sort=29)
	public Date getArriveDate() {
		return arriveDate;
	}

	public void setArriveDate(Date arriveDate) {
		this.arriveDate = arriveDate;
	}



	
	public InvCheckDetail() {
		super();
	}

	public InvCheckDetail(String id){
		super(id);
	}

	public InvCheckDetail(InvCheckMain InvCheckMain){
		this.InvCheckMain = InvCheckMain;
	}

	public InvCheckMain getInvCheckMain() {
		return InvCheckMain;
	}

	public void setInvCheckMain(InvCheckMain InvCheckMain) {
		this.InvCheckMain = InvCheckMain;
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
	public Double getCheckQty() {
		return checkQty;
	}

	public void setCheckQty(Double checkQty) {
		this.checkQty = checkQty;
	}
	
	@ExcelField(title="实际数量", align=2, sort=13)
	public Double getRealQty() {
		return realQty;
	}

	public void setRealQty(Double realQty) {
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
	public Double getYcRealqty() {
		return ycRealqty;
	}

	public void setYcRealqty(Double ycRealqty) {
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
	public Double getCorSerialnum() {
		return corSerialnum;
	}

	public void setCorSerialnum(Double corSerialnum) {
		this.corSerialnum = corSerialnum;
	}
	
	@ExcelField(title="保留数量", align=2, sort=34)
	public Double getOldQty() {
		return oldQty;
	}

	public void setOldQty(Double oldQty) {
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

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public String getConBillNum() {
		return conBillNum;
	}

	public void setConBillNum(String conBillNum) {
		this.conBillNum = conBillNum;
	}

	public String getPlanBillNum() {
		return planBillNum;
	}

	public void setPlanBillNum(String planBillNum) {
		this.planBillNum = planBillNum;
	}

	public Double getMaxQty() {
		return maxQty;
	}

	public void setMaxQty(Double maxQty) {
		this.maxQty = maxQty;
	}

	public Integer getIsPrint() {
		return isPrint;
	}

	public void setIsPrint(Integer isPrint) {
		this.isPrint = isPrint;
	}

	public String getQmsFlag() {
		return qmsFlag;
	}

	public void setQmsFlag(String qmsFlag) {
		this.qmsFlag = qmsFlag;
	}

	public Item getItemInvAccount() {
		return itemInvAccount;
	}

	public void setItemInvAccount(Item itemInvAccount) {
		this.itemInvAccount = itemInvAccount;
	}

	public String getItemInvAccountCode() {
		return itemInvAccountCode;
	}

	public void setItemInvAccountCode(String itemInvAccountCode) {
		this.itemInvAccountCode = itemInvAccountCode;
	}

	public String getItemInvAccountId() {
		return itemInvAccountId;
	}

	public void setItemInvAccountId(String itemInvAccountId) {
		this.itemInvAccountId = itemInvAccountId;
	}
}