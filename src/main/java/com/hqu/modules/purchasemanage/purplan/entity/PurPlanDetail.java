/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.purchasemanage.purplan.entity;

import com.hqu.modules.basedata.item.entity.Item;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.hqu.modules.basedata.account.entity.Account;
import com.hqu.modules.purchasemanage.group.entity.GroupBuyer;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 采购计划子表Entity
 * @author yangxianbang
 * @version 2018-04-23
 */
public class PurPlanDetail extends DataEntity<PurPlanDetail> {
	
	private static final long serialVersionUID = 1L;
	private PurPlanMain billNum;		// 单据编号 父类
	private Integer serialNum;		// 序号
	private ItemExtend itemCode;		// 物料编号
	private String itemName;		// 物料名称
	private String itemSpecmodel;		// 物料规格
	private String itemTexture;		// 物料材质
	private String unitName;		// 计量单位
	private Double planQty;		// 计划数量
	private Double purQty;       //需求数量
	private Double purPrice;		// 采购单价
	private Double purSum;		// 采购总额
	private Double planPrice;		// 计划单价
	private Double planSum;		// 计划总额
	private Double invQty;		// 库存数量
	private Double roadQty;		// 在途数量
	private Date purStartDate;		// 采购开始时间
	private Date purArriveDate;		// 采购到货时间
	private Account supId;		// 供应商编号
	private String supName;		// 供应商名称
	private GroupBuyer buyerId;		// 采购员编码
	private String buyerName;		// 采购员名称
	private Double safetyQtys;		// 安全库存量
	private String batchLt;		// 采购提前期
	private Date requestDate;		// 需求日期
	private Double costPrice;		// 移动价
	private Double conQty;		// 合同量
	private Double checkQty;		// 到货量
	private String notes;		// 备注
	private Double applyQtyAll;		// 数量合计
	private String log;		// 修改日志
	private String itemPdn;		// 物料图号
	private Double applySumAll;		// 金额合计

	private String applyBillNum;//需求编码
	private Integer applySerialNum;//需求序号
	private String applyId;//需求ID（回写计划号用）


	private String billnu;//子表单据编号（UUID）

	public String getBillnu() {
		return billnu;
	}

	public void setBillnu(String billnu) {
		this.billnu = billnu;
	}

	public String getApplyId() {
		return applyId;
	}

	public void setApplyId(String applyId) {
		this.applyId = applyId;
	}

	public String getApplyBillNum() {
		return applyBillNum;
	}

	public void setApplyBillNum(String applyBillNum) {
		this.applyBillNum = applyBillNum;
	}

	public Integer getApplySerialNum() {
		return applySerialNum;
	}

	public void setApplySerialNum(Integer applySerialNum) {
		this.applySerialNum = applySerialNum;
	}

	public PurPlanDetail() {
		super();
	}

	public PurPlanDetail(String id){
		super(id);
	}

	public PurPlanDetail(PurPlanMain billNum){
		this.billNum = billNum;
	}

	public PurPlanMain getBillNum() {
		return billNum;
	}

	public void setBillNum(PurPlanMain billNum) {
		this.billNum = billNum;
	}

	public Double getPurQty() {
		return purQty;
	}

	public void setPurQty(Double purQty) {
		this.purQty = purQty;
	}

	@ExcelField(title="序号", align=2, sort=8)
	public Integer getSerialNum() {
		return serialNum;
	}

	public void setSerialNum(Integer serialNum) {
		this.serialNum = serialNum;
	}
	
	@ExcelField(title="物料编号", fieldType=ItemExtend.class, value="itemCode.code", align=2, sort=9)
	public ItemExtend getItemCode() {
		return itemCode;
	}

	public void setItemCode(ItemExtend itemCode) {
		this.itemCode = itemCode;
	}
	
	@ExcelField(title="物料名称", align=2, sort=10)
	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	
	@ExcelField(title="物料规格", align=2, sort=11)
	public String getItemSpecmodel() {
		return itemSpecmodel;
	}

	public void setItemSpecmodel(String itemSpecmodel) {
		this.itemSpecmodel = itemSpecmodel;
	}
	
	@ExcelField(title="物料材质", align=2, sort=12)
	public String getItemTexture() {
		return itemTexture;
	}

	public void setItemTexture(String itemTexture) {
		this.itemTexture = itemTexture;
	}
	
	@ExcelField(title="计量单位", align=2, sort=13)
	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}
	
	@ExcelField(title="计划数量", align=2, sort=14)
	public Double getPlanQty() {
		return planQty;
	}

	public void setPlanQty(Double planQty) {
		this.planQty = planQty;
	}
	
	@ExcelField(title="采购单价", align=2, sort=15)
	public Double getPurPrice() {
		return purPrice;
	}

	public void setPurPrice(Double purPrice) {
		this.purPrice = purPrice;
	}
	
	@ExcelField(title="采购总额", align=2, sort=16)
	public Double getPurSum() {
		return purSum;
	}

	public void setPurSum(Double purSum) {
		this.purSum = purSum;
	}
	
	@ExcelField(title="计划单价", align=2, sort=17)
	public Double getPlanPrice() {
		return planPrice;
	}

	public void setPlanPrice(Double planPrice) {
		this.planPrice = planPrice;
	}
	
	@ExcelField(title="计划总额", align=2, sort=18)
	public Double getPlanSum() {
		return planSum;
	}

	public void setPlanSum(Double planSum) {
		this.planSum = planSum;
	}
	
	@ExcelField(title="库存数量", align=2, sort=19)
	public Double getInvQty() {
		return invQty;
	}

	public void setInvQty(Double invQty) {
		this.invQty = invQty;
	}
	
	@ExcelField(title="在途数量", align=2, sort=20)
	public Double getRoadQty() {
		return roadQty;
	}

	public void setRoadQty(Double roadQty) {
		this.roadQty = roadQty;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="采购开始时间", align=2, sort=21)
	public Date getPurStartDate() {
		return purStartDate;
	}

	public void setPurStartDate(Date purStartDate) {
		this.purStartDate = purStartDate;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="采购到货时间", align=2, sort=22)
	public Date getPurArriveDate() {
		return purArriveDate;
	}

	public void setPurArriveDate(Date purArriveDate) {
		this.purArriveDate = purArriveDate;
	}
	
	@ExcelField(title="供应商编号", fieldType=Account.class, value="supId.accountCode", align=2, sort=23)
	public Account getSupId() {
		return supId;
	}

	public void setSupId(Account supId) {
		this.supId = supId;
	}
	
	@ExcelField(title="供应商名称", align=2, sort=24)
	public String getSupName() {
		return supName;
	}

	public void setSupName(String supName) {
		this.supName = supName;
	}
	
	@ExcelField(title="采购员编码", fieldType=GroupBuyer.class, value="buyerId.buyername", align=2, sort=25)
	public GroupBuyer getBuyerId() {
		return buyerId;
	}

	public void setBuyerId(GroupBuyer buyerId) {
		this.buyerId = buyerId;
	}
	
	@ExcelField(title="采购员名称", align=2, sort=26)
	public String getBuyerName() {
		return buyerName;
	}

	public void setBuyerName(String buyerName) {
		this.buyerName = buyerName;
	}
	
	@ExcelField(title="安全库存量", align=2, sort=27)
	public Double getSafetyQtys() {
		return safetyQtys;
	}

	public void setSafetyQtys(Double safetyQtys) {
		this.safetyQtys = safetyQtys;
	}
	
	@ExcelField(title="采购提前期", align=2, sort=28)
	public String getBatchLt() {
		return batchLt;
	}

	public void setBatchLt(String batchLt) {
		this.batchLt = batchLt;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="需求日期", align=2, sort=29)
	public Date getRequestDate() {
		return requestDate;
	}

	public void setRequestDate(Date requestDate) {
		this.requestDate = requestDate;
	}
	
	@ExcelField(title="移动价", align=2, sort=30)
	public Double getCostPrice() {
		return costPrice;
	}

	public void setCostPrice(Double costPrice) {
		this.costPrice = costPrice;
	}
	
	@ExcelField(title="合同量", align=2, sort=31)
	public Double getConQty() {
		return conQty;
	}

	public void setConQty(Double conQty) {
		this.conQty = conQty;
	}
	
	@ExcelField(title="到货量", align=2, sort=32)
	public Double getCheckQty() {
		return checkQty;
	}

	public void setCheckQty(Double checkQty) {
		this.checkQty = checkQty;
	}
	
	@ExcelField(title="备注", align=2, sort=33)
	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}
	
	@ExcelField(title="数量合计", align=2, sort=34)
	public Double getApplyQtyAll() {
		return applyQtyAll;
	}

	public void setApplyQtyAll(Double applyQtyAll) {
		this.applyQtyAll = applyQtyAll;
	}
	
	@ExcelField(title="修改日志", align=2, sort=35)
	public String getLog() {
		return log;
	}

	public void setLog(String log) {
		this.log = log;
	}
	
	@ExcelField(title="物料图号", align=2, sort=36)
	public String getItemPdn() {
		return itemPdn;
	}

	public void setItemPdn(String itemPdn) {
		this.itemPdn = itemPdn;
	}
	
	@ExcelField(title="金额合计", align=2, sort=37)
	public Double getApplySumAll() {
		return applySumAll;
	}

	public void setApplySumAll(Double applySumAll) {
		this.applySumAll = applySumAll;
	}
	
}