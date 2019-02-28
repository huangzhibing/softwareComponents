/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.purchasemanage.purplan.entity;

import com.hqu.modules.basedata.account.entity.Account;
import com.hqu.modules.basedata.item.entity.Item;
import com.hqu.modules.basedata.period.entity.Period;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.hqu.modules.purchasemanage.contractmain.entity.ContractDetail;
import com.hqu.modules.purchasemanage.rollplannewquery.entity.RollPlanNew;
import com.hqu.modules.qualitymanage.purinvcheckmain.entity.InvCheckDetail;
import com.jeeplus.core.persistence.ActEntity;
import com.jeeplus.modules.sys.entity.User;
import com.hqu.modules.purchasemanage.plantype.entity.PlanType;
import com.jeeplus.modules.sys.entity.Office;
import java.util.List;
import com.google.common.collect.Lists;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 采购计划Entity
 * @author yangxianbang
 * @version 2018-04-23
 */
public class PurPlanMain extends ActEntity<PurPlanMain> {
	
	private static final long serialVersionUID = 1L;
	private String billNum;		// 采购计划编号
	private String billType;		// 单据类型
	private Period periodId;		// 核算期
	private Date planDate;		// 计划日期
	private Date makeDate;		// 制单日期
	private User makeEmpid;		// 制单人编号
	private String makeEmpname;		// 制单人名称
	private String makeNotes;		// 单据说明
	private String billStateFlag;		// 单据状态
	private Double planPriceSum;		// 总计划金额
	private String needType;		// 需求来源
	private Account supId;		// 供应商编号
	private String supName;		// 供应商名称
	private Double taxRatio; //税率
	private PlanType planTypeCode;		// 计划类型编码
	private String planTypeName;		// 计划类型名称
	private String mrpPlanId;		// MRP计划号
	private String planNote;		// 备注
	private String fbrNo;		// 公司编码
	private Date bdate;		// 业务日期
	private Office planDeptId;		// 部门代码
	private String planDeptName;		// 部门名称
	private String planUse;		// 用途
	private String userDeptCode;		// 登录用户所归属的部门编码
	private Date beginPlanDate;		// 开始 计划日期
	private Date endPlanDate;		// 结束 计划日期
	private Date beginMakeDate;		// 开始 制单日期
	private Date endMakeDate;		// 结束 制单日期
	private String specModel;
	private Item itemCode;
	private String itemName;
	private List<PurPlanDetail> purPlanDetailList = Lists.newArrayList();		// 子表列表
	private List<RollPlanNew> rollPlanNewList=Lists.newArrayList();   //关联滚动计划子表
	private List<ContractDetail> contractDetailList=Lists.newArrayList();//关联合同计划子表
	private List<InvCheckDetail> invCheckDetailList=Lists.newArrayList();//关联到货子表

	private String justifyRemark;

	private String taxRatioNew;		// 税率

	public String getTaxRatioNew() {
		return taxRatioNew;
	}

	public void setTaxRatioNew(String taxRatioNew) {
		String ratio=taxRatioNew.split("%")[0];
		this.taxRatio = Double.parseDouble(ratio)/100.0;
		this.taxRatioNew = taxRatioNew;
	}



	public List<RollPlanNew> getRollPlanNewList() {
		return rollPlanNewList;
	}

	public void setRollPlanNewList(List<RollPlanNew> rollPlanNewList) {
		this.rollPlanNewList = rollPlanNewList;
	}

	public List<ContractDetail> getContractDetailList() {
		return contractDetailList;
	}

	public void setContractDetailList(List<ContractDetail> contractDetailList) {
		this.contractDetailList = contractDetailList;
	}

	public List<InvCheckDetail> getInvCheckDetailList() {
		return invCheckDetailList;
	}

	public void setInvCheckDetailList(List<InvCheckDetail> invCheckDetailList) {
		this.invCheckDetailList = invCheckDetailList;
	}

	public String getJustifyRemark() {
		return justifyRemark;
	}

	public void setJustifyRemark(String justifyRemark) {
		this.justifyRemark = justifyRemark;
	}

	public PurPlanMain() {
		super();
	}

	public PurPlanMain(String id){
		super(id);
	}

	public Account getSupId() {
		return supId;
	}

	public void setSupId(Account supId) {
		this.supId = supId;
	}

	public String getSupName() {
		return supName;
	}

	public void setSupName(String supName) {
		this.supName = supName;
	}

	public Double getTaxRatio() {
		return taxRatio;
	}

	public void setTaxRatio(Double taxRatio) {
		this.taxRatio = taxRatio;
	}

	@ExcelField(title="采购计划编号", align=2, sort=7)
	public String getBillNum() {
		return billNum;
	}

	public void setBillNum(String billNum) {
		this.billNum = billNum;
	}
	
	@ExcelField(title="单据类型", dictType="bill_type_purplan", align=2, sort=8)
	public String getBillType() {
		return billType;
	}

	public void setBillType(String billType) {
		this.billType = billType;
	}
	
	@ExcelField(title="核算期", fieldType=Period.class, value="periodId.periodId", align=2, sort=9)
	public Period getPeriodId() {
		return periodId;
	}

	public void setPeriodId(Period periodId) {
		this.periodId = periodId;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="计划日期", align=2, sort=10)
	public Date getPlanDate() {
		return planDate;
	}

	public void setPlanDate(Date planDate) {
		this.planDate = planDate;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="制单日期", align=2, sort=11)
	public Date getMakeDate() {
		return makeDate;
	}

	public void setMakeDate(Date makeDate) {
		this.makeDate = makeDate;
	}
	
	@ExcelField(title="制单人编号", fieldType=User.class, value="makeEmpid.loginName", align=2, sort=12)
	public User getMakeEmpid() {
		return makeEmpid;
	}

	public void setMakeEmpid(User makeEmpid) {
		this.makeEmpid = makeEmpid;
	}
	
	@ExcelField(title="制单人名称", align=2, sort=13)
	public String getMakeEmpname() {
		return makeEmpname;
	}

	public void setMakeEmpname(String makeEmpname) {
		this.makeEmpname = makeEmpname;
	}
	
	@ExcelField(title="单据说明", align=2, sort=14)
	public String getMakeNotes() {
		return makeNotes;
	}

	public void setMakeNotes(String makeNotes) {
		this.makeNotes = makeNotes;
	}
	
	@ExcelField(title="单据状态", dictType="purplan_state_flag", align=2, sort=15)
	public String getBillStateFlag() {
		return billStateFlag;
	}

	public void setBillStateFlag(String billStateFlag) {
		this.billStateFlag = billStateFlag;
	}
	
	@ExcelField(title="总计划金额", align=2, sort=16)
	public Double getPlanPriceSum() {
		return planPriceSum;
	}

	public void setPlanPriceSum(Double planPriceSum) {
		this.planPriceSum = planPriceSum;
	}
	
	@ExcelField(title="需求来源", align=2, sort=17)
	public String getNeedType() {
		return needType;
	}

	public void setNeedType(String needType) {
		this.needType = needType;
	}
	
	@ExcelField(title="计划类型编码", fieldType=PlanType.class, value="planTypeCode.plantypeid", align=2, sort=18)
	public PlanType getPlanTypeCode() {
		return planTypeCode;
	}

	public void setPlanTypeCode(PlanType planTypeCode) {
		this.planTypeCode = planTypeCode;
	}
	
	@ExcelField(title="计划类型名称", align=2, sort=19)
	public String getPlanTypeName() {
		return planTypeName;
	}

	public void setPlanTypeName(String planTypeName) {
		this.planTypeName = planTypeName;
	}
	
	@ExcelField(title="MRP计划号", align=2, sort=20)
	public String getMrpPlanId() {
		return mrpPlanId;
	}

	public void setMrpPlanId(String mrpPlanId) {
		this.mrpPlanId = mrpPlanId;
	}
	
	@ExcelField(title="备注", align=2, sort=21)
	public String getPlanNote() {
		return planNote;
	}

	public void setPlanNote(String planNote) {
		this.planNote = planNote;
	}
	
	@ExcelField(title="公司编码", align=2, sort=22)
	public String getFbrNo() {
		return fbrNo;
	}

	public void setFbrNo(String fbrNo) {
		this.fbrNo = fbrNo;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="业务日期", align=2, sort=23)
	public Date getBdate() {
		return bdate;
	}

	public void setBdate(Date bdate) {
		this.bdate = bdate;
	}
	
	@ExcelField(title="部门代码", fieldType=Office.class, value="planDeptId.name", align=2, sort=24)
	public Office getPlanDeptId() {
		return planDeptId;
	}

	public void setPlanDeptId(Office planDeptId) {
		this.planDeptId = planDeptId;
	}
	
	@ExcelField(title="部门名称", align=2, sort=25)
	public String getPlanDeptName() {
		return planDeptName;
	}

	public void setPlanDeptName(String planDeptName) {
		this.planDeptName = planDeptName;
	}
	
	@ExcelField(title="用途", align=2, sort=26)
	public String getPlanUse() {
		return planUse;
	}

	public void setPlanUse(String planUse) {
		this.planUse = planUse;
	}
	
	@ExcelField(title="登录用户所归属的部门编码", align=2, sort=27)
	public String getUserDeptCode() {
		return userDeptCode;
	}

	public void setUserDeptCode(String userDeptCode) {
		this.userDeptCode = userDeptCode;
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
		
	public Date getBeginMakeDate() {
		return beginMakeDate;
	}

	public void setBeginMakeDate(Date beginMakeDate) {
		this.beginMakeDate = beginMakeDate;
	}
	
	public Date getEndMakeDate() {
		return endMakeDate;
	}

	public void setEndMakeDate(Date endMakeDate) {
		this.endMakeDate = endMakeDate;
	}
		
	public List<PurPlanDetail> getPurPlanDetailList() {
		return purPlanDetailList;
	}

	public void setPurPlanDetailList(List<PurPlanDetail> purPlanDetailList) {
		this.purPlanDetailList = purPlanDetailList;
	}

	public String getSpecModel() {
		return specModel;
	}

	public void setSpecModel(String specModel) {
		this.specModel = specModel;
	}

	public Item getItemCode() {
		return itemCode;
	}

	public void setItemCode(Item itemCode) {
		this.itemCode = itemCode;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	
}