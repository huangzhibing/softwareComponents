/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.costmanage.realaccount.entity;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.List;
import com.google.common.collect.Lists;

import com.hqu.modules.costmanage.personwage.entity.PersonWage;
import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 材料凭证单据管理Entity
 * @author hzb
 * @version 2018-09-05
 */
public class RealAccount extends DataEntity<RealAccount> {
	
	private static final long serialVersionUID = 1L;
	private String billNum;		// 凭证号
	private String oriBillId;		// 原始凭证号
	private Date oriBillDate;		// 原始单据日期
	private String periodId;		// 会计期间
	private String makeId;		// 制单人编码
	private Date makeDate;		// 制单日期
	private String makeName;		// 制单人名称
	private String cosBillNumStatus;		// 单据处理状态
	private String billMode;		// 制单方式
	private String checkId;		// 稽核人代码
	private Date checkDate;		// 稽核日期
	private String checkName;		// 稽核人姓名
	private String billType;		// 凭证类型
	private List<RealAccountDetail> realAccountDetailList = Lists.newArrayList();		// 子表列表
	private List<PersonWage> personWageList = Lists.newArrayList();			//人工成本列表

	private String itemName; //设置子表查询的科目名称
	private String prodName; //设置子表查询的核算对象名称
	
	private String mpsPlanId;	//主生产计划号
	private String orderId;		//内部订单号
	
	public RealAccount() {
		super();
	}

	public RealAccount(String id){
		super(id);
	}

	@ExcelField(title="凭证号", align=2, sort=7)
	public String getBillNum() {
		return billNum;
	}

	public void setBillNum(String billNum) {
		this.billNum = billNum;
	}
	
	@ExcelField(title="原始凭证号", align=2, sort=8)
	public String getOriBillId() {
		return oriBillId;
	}

	public void setOriBillId(String oriBillId) {
		this.oriBillId = oriBillId;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="原始单据日期", align=2, sort=9)
	public Date getOriBillDate() {
		return oriBillDate;
	}

	public void setOriBillDate(Date oriBillDate) {
		this.oriBillDate = oriBillDate;
	}
	
	@ExcelField(title="会计期间", align=2, sort=10)
	public String getPeriodId() {
		return periodId;
	}

	public void setPeriodId(String periodId) {
		this.periodId = periodId;
	}
	
	@ExcelField(title="制单人编码", align=2, sort=11)
	public String getMakeId() {
		return makeId;
	}

	public void setMakeId(String makeId) {
		this.makeId = makeId;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="制单日期", align=2, sort=12)
	public Date getMakeDate() {
		return makeDate;
	}

	public void setMakeDate(Date makeDate) {
		this.makeDate = makeDate;
	}
	
	@ExcelField(title="制单人名称", align=2, sort=13)
	public String getMakeName() {
		return makeName;
	}

	public void setMakeName(String makeName) {
		this.makeName = makeName;
	}
	
	@ExcelField(title="单据处理状态", dictType="cosBillNumStatus", align=2, sort=14)
	public String getCosBillNumStatus() {
		return cosBillNumStatus;
	}

	public void setCosBillNumStatus(String cosBillNumStatus) {
		this.cosBillNumStatus = cosBillNumStatus;
	}
	
	@ExcelField(title="制单方式", dictType="billMode", align=2, sort=15)
	public String getBillMode() {
		return billMode;
	}

	public void setBillMode(String billMode) {
		this.billMode = billMode;
	}

	@ExcelField(title="稽核人代码", align=2, sort=16)
	public String getCheckId() {
		return checkId;
	}

	public void setCheckId(String checkId) {
		this.checkId = checkId;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="稽核日期", align=2, sort=17)
	public Date getCheckDate() {
		return checkDate;
	}

	public void setCheckDate(Date checkDate) {
		this.checkDate = checkDate;
	}
	
	@ExcelField(title="稽核人姓名", align=2, sort=18)
	public String getCheckName() {
		return checkName;
	}

	public void setCheckName(String checkName) {
		this.checkName = checkName;
	}
	
	@ExcelField(title="凭证类型", align=2, sort=19)
	public String getBillType() {
		return billType;
	}

	public void setBillType(String billType) {
		this.billType = billType;
	}
	
	public List<RealAccountDetail> getRealAccountDetailList() {
		return realAccountDetailList;
	}

	public void setRealAccountDetailList(List<RealAccountDetail> realAccountDetailList) {
		this.realAccountDetailList = realAccountDetailList;
	}

	public List<PersonWage> getPersonWageList() {
		return personWageList;
	}

	public void setPersonWageList(List<PersonWage> personWageList) {
		this.personWageList = personWageList;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getProdName() {
		return prodName;
	}

	public void setProdName(String prodName) {
		this.prodName = prodName;
	}
	@ExcelField(title="主生产计划号", align=2, sort=20)
	public String getMpsPlanId() {
		return mpsPlanId;
	}

	public void setMpsPlanId(String mpsPlanId) {
		this.mpsPlanId = mpsPlanId;
	}
	@ExcelField(title="内部订单号", align=2, sort=21)
	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

}