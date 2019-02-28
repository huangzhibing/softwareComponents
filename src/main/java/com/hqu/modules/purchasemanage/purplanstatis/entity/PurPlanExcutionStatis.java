/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.purchasemanage.purplanstatis.entity;

import com.hqu.modules.basedata.item.entity.Item;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.jeeplus.modules.sys.entity.User;
import com.hqu.modules.basedata.account.entity.Account;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 采购计划执行情况表Entity
 * @author yxb
 * @version 2018-08-19
 */
public class PurPlanExcutionStatis extends DataEntity<PurPlanExcutionStatis> {
	
	private static final long serialVersionUID = 1L;
	private String planBillNum;		// 计划单据编号
	private String billType;		// 单据类型
	private Integer serialNum;		// 序号
	private Item itemCode;		// 物料编码
	private String itemName;		// 物料名称
	private String itemSpecmodel;		// 物料规格
	private String unitName;		// 计量单位
	private Double planQty;		// 计划采购数量
	private Double purQty;		// 需求数量
	private Date planDate;		// 计划日期
	private String planPrice;		// 单价
	private String planSum;		// 预算金额
	private User buyerId;		// 采购员编号
	private String buyerName;		// 采购员
	private Account supId;		// 供应商编码
	private String supName;		// 供应商名称
	private Date bdate;		// 下单日期
	private String billNum;		// 订单号
	private Double itemQty;		// 订单数量
	private Date conArriveDate;		// 交货日期
	private Date arriveDate;		// 实际交货日期
	private Double checkQty;		// 实际交货数量
	private String qmsFlag;		// 质量状态
	private Date beginPlanDate;		// 开始 计划日期
	private Date endPlanDate;		// 结束 计划日期
	
	public PurPlanExcutionStatis() {
		super();
	}

	public PurPlanExcutionStatis(String id){
		super(id);
	}

	@ExcelField(title="计划单据编号", align=2, sort=6)
	public String getPlanBillNum() {
		return planBillNum;
	}

	public void setPlanBillNum(String planBillNum) {
		this.planBillNum = planBillNum;
	}
	
	@ExcelField(title="单据类型", align=2, sort=7)
	public String getBillType() {
		return billType;
	}

	public void setBillType(String billType) {
		this.billType = billType;
	}
	
	@ExcelField(title="序号", align=2, sort=8)
	public Integer getSerialNum() {
		return serialNum;
	}

	public void setSerialNum(Integer serialNum) {
		this.serialNum = serialNum;
	}
	
	@ExcelField(title="物料编码", fieldType=Item.class, value="itemCode.code", align=2, sort=9)
	public Item getItemCode() {
		return itemCode;
	}

	public void setItemCode(Item itemCode) {
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
	
	@ExcelField(title="计量单位", align=2, sort=12)
	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}
	
	@ExcelField(title="计划采购数量", align=2, sort=13)
	public Double getPlanQty() {
		return planQty;
	}

	public void setPlanQty(Double planQty) {
		this.planQty = planQty;
	}
	
	@ExcelField(title="需求数量", align=2, sort=14)
	public Double getPurQty() {
		return purQty;
	}

	public void setPurQty(Double purQty) {
		this.purQty = purQty;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd")
	@ExcelField(title="计划日期", align=2, sort=15)
	public Date getPlanDate() {
		return planDate;
	}

	public void setPlanDate(Date planDate) {
		this.planDate = planDate;
	}
	
	@ExcelField(title="单价", align=2, sort=16)
	public String getPlanPrice() {
		return planPrice;
	}

	public void setPlanPrice(String planPrice) {
		this.planPrice = planPrice;
	}
	
	@ExcelField(title="预算金额", align=2, sort=17)
	public String getPlanSum() {
		return planSum;
	}

	public void setPlanSum(String planSum) {
		this.planSum = planSum;
	}
	
	@ExcelField(title="采购员编号", fieldType=User.class, value="buyerId.no", align=2, sort=18)
	public User getBuyerId() {
		return buyerId;
	}

	public void setBuyerId(User buyerId) {
		this.buyerId = buyerId;
	}
	
	@ExcelField(title="采购员", align=2, sort=19)
	public String getBuyerName() {
		return buyerName;
	}

	public void setBuyerName(String buyerName) {
		this.buyerName = buyerName;
	}
	
	@ExcelField(title="供应商编码", fieldType=Account.class, value="supId.accountCode", align=2, sort=20)
	public Account getSupId() {
		return supId;
	}

	public void setSupId(Account supId) {
		this.supId = supId;
	}
	
	@ExcelField(title="供应商名称", align=2, sort=21)
	public String getSupName() {
		return supName;
	}

	public void setSupName(String supName) {
		this.supName = supName;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="下单日期", align=2, sort=22)
	public Date getBdate() {
		return bdate;
	}

	public void setBdate(Date bdate) {
		this.bdate = bdate;
	}
	
	@ExcelField(title="订单号", align=2, sort=23)
	public String getBillNum() {
		return billNum;
	}

	public void setBillNum(String billNum) {
		this.billNum = billNum;
	}
	
	@ExcelField(title="订单数量", align=2, sort=24)
	public Double getItemQty() {
		return itemQty;
	}

	public void setItemQty(Double itemQty) {
		this.itemQty = itemQty;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="交货日期", align=2, sort=25)
	public Date getConArriveDate() {
		return conArriveDate;
	}

	public void setConArriveDate(Date conArriveDate) {
		this.conArriveDate = conArriveDate;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="实际交货日期", align=2, sort=26)
	public Date getArriveDate() {
		return arriveDate;
	}

	public void setArriveDate(Date arriveDate) {
		this.arriveDate = arriveDate;
	}
	
	@ExcelField(title="实际交货数量", align=2, sort=27)
	public Double getCheckQty() {
		return checkQty;
	}

	public void setCheckQty(Double checkQty) {
		this.checkQty = checkQty;
	}
	
	@ExcelField(title="质量状态", dictType="qmsFlagSal", align=2, sort=28)
	public String getQmsFlag() {
		return qmsFlag;
	}

	public void setQmsFlag(String qmsFlag) {
		this.qmsFlag = qmsFlag;
	}
	
	public Date getBeginPlanDate() {
		return beginPlanDate;
	}

	public void setBeginPlanDate(Date beginPlanDate) {
		this.beginPlanDate = beginPlanDate;
	}
	
	public Date getEndPlanDate() {
		return endPlanDate;
	}

	public void setEndPlanDate(Date endPlanDate) {
		this.endPlanDate = endPlanDate;
	}
		
}