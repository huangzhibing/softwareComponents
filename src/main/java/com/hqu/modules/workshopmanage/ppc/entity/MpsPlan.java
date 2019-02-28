/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.workshopmanage.ppc.entity;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import javax.validation.constraints.NotNull;

import com.google.common.collect.Lists;
import com.hqu.modules.workshopmanage.process.entity.SfcProcessDetail;
import org.springframework.format.annotation.DateTimeFormat;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * MPSPlanEntity
 * @author XHC
 * @version 2018-06-01
 */
public class MpsPlan extends DataEntity<MpsPlan> {
	
	private static final long serialVersionUID = 1L;
	private String mpsPlanid;		// 主生产计划号
	private String reqID;		// 销售订单号
	private String prodCode;		// 产品编码
	private String prodName;		// 产品名称
	private Integer planQty;		// 计划数量
	private Integer invAllocatedQty;		// 库存占用量
	private Integer finishQty;		// 已完工数量
	private String sourceFlag;		// 来源标志
	private Date planBDate;		// 计划开始日期
	private Date planEDate;		// 计划结束日期
	private String planStatus;		// 计划状态
	private String MRPDealStatus;		// MRP处理状态
	private String SFCDealStatus;		// 车间处理状态
	private Integer assignedQty;		// 已安排生产数量
	private String makePID;		// 计划制定人
	private String makeName;//计划指定人名称
	private String confirmPID;		// 计划提交人
	private String auditPID;		// 计划审批人
	private String deliveryPID;		// 计划下达人
	private String closePID;		// 计划结案人
	
	@DateTimeFormat(pattern="yyyy-mm-dd")
	private Date makeDate;		// 计划制定日期
	private Date confirmDate;		// 计划提交日期
	private Date auditDate;		// 计划审批日期
	private String deliveryDate;		// 计划下达日期
	private Date closeDate;		// 计划结案日期

	private String auditComment; //计划审批意见

	//日期查询 临时变量
	private Date beginPlanBDate;// 计划开始日期 开始查询条件
	private Date endPlanBDate;// 计划开始日期 结束查询条件
	private Date beginPlanEDate;// 计划开始日期  开始查询条件
	private Date endPlanEDate;// 计划开始日期 结束查询条件

	//MRP计划关联列表 临时变量
	private List<MrpPlan> mrpPlanList= Lists.newArrayList();

	//车间作业计划子表 临时变量
	private List<SfcProcessDetail> sfcProcessDetailList = Lists.newArrayList();		// 子表列表


	public List<SfcProcessDetail> getSfcProcessDetailList() {
		return sfcProcessDetailList;
	}

	public void setSfcProcessDetailList(List<SfcProcessDetail> sfcProcessDetailList) {
		this.sfcProcessDetailList = sfcProcessDetailList;
	}

	public List<MrpPlan> getMrpPlanList() {
		return mrpPlanList;
	}

	public void setMrpPlanList(List<MrpPlan> mrpPlanList) {
		this.mrpPlanList = mrpPlanList;
	}

	public Date getBeginPlanBDate() {
		return beginPlanBDate;
	}

	public void setBeginPlanBDate(Date beginPlanBDate) {
		this.beginPlanBDate = beginPlanBDate;
	}

	public Date getEndPlanBDate() {
		return endPlanBDate;
	}

	public void setEndPlanBDate(Date endPlanBDate) {
		this.endPlanBDate = endPlanBDate;
	}

	public Date getBeginPlanEDate() {
		return beginPlanEDate;
	}

	public void setBeginPlanEDate(Date beginPlanEDate) {
		this.beginPlanEDate = beginPlanEDate;
	}

	public Date getEndPlanEDate() {
		return endPlanEDate;
	}

	public void setEndPlanEDate(Date endPlanEDate) {
		this.endPlanEDate = endPlanEDate;
	}

	public String getAuditComment() {
		return auditComment;
	}

	public void setAuditComment(String auditComment) {
		this.auditComment = auditComment;
	}

	public String getMakeName() {
		return makeName;
	}

	public void setMakeName(String makeName) {
		this.makeName = makeName;
	}

	public MpsPlan() {
		super();
	}

	public MpsPlan(String id){
		super(id);
	}

	@ExcelField(title="主生产计划号", align=2, sort=6)
	public String getMpsPlanid() {
		return mpsPlanid;
	}

	public void setMpsPlanid(String mpsPlanid) {
		this.mpsPlanid = mpsPlanid;
	}
	
	@ExcelField(title="销售订单号", align=2, sort=7)
	public String getReqID() {
		return reqID;
	}

	public void setReqID(String reqID) {
		this.reqID = reqID;
	}
	
	@ExcelField(title="产品编码", align=2, sort=8)
	public String getProdCode() {
		return prodCode;
	}

	public void setProdCode(String prodCode) {
		this.prodCode = prodCode;
	}
	
	@ExcelField(title="产品名称", align=2, sort=9)
	public String getProdName() {
		return prodName;
	}

	public void setProdName(String prodName) {
		this.prodName = prodName;
	}
	
	@ExcelField(title="计划数量", align=2, sort=10)
	public Integer getPlanQty() {
		return planQty;
	}

	public void setPlanQty(Integer planQty) {
		this.planQty = planQty;
	}
	
	@ExcelField(title="库存占用量", align=2, sort=11)
	public Integer getInvAllocatedQty() {
		return invAllocatedQty;
	}

	public void setInvAllocatedQty(Integer invAllocatedQty) {
		this.invAllocatedQty = invAllocatedQty;
	}
	
	@ExcelField(title="已完工数量", align=2, sort=12)
	public Integer getFinishQty() {
		return finishQty;
	}

	public void setFinishQty(Integer finishQty) {
		this.finishQty = finishQty;
	}
	
	@ExcelField(title="来源标志", align=2, sort=13)
	public String getSourceFlag() {
		return sourceFlag;
	}

	public void setSourceFlag(String sourceFlag) {
		this.sourceFlag = sourceFlag;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd")
	@NotNull(message="计划开始日期不能为空")
	@ExcelField(title="计划开始日期", align=2, sort=14)
	public Date getPlanBDate() {
		return planBDate;
	}

	public void setPlanBDate(Date planBDate) {
		this.planBDate = planBDate;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd")
	@NotNull(message="计划结束日期不能为空")
	@ExcelField(title="计划结束日期", align=2, sort=15)
	public Date getPlanEDate() {
		return planEDate;
	}

	public void setPlanEDate(Date planEDate) {
		this.planEDate = planEDate;
	}
	
	@ExcelField(title="计划状态", align=2, sort=16)
	public String getPlanStatus() {
		return planStatus;
	}

	public void setPlanStatus(String planStatus) {
		this.planStatus = planStatus;
	}
	
	@ExcelField(title="MRP处理状态", align=2, sort=17)
	public String getMRPDealStatus() {
		return MRPDealStatus;
	}

	public void setMRPDealStatus(String MRPDealStatus) {
		this.MRPDealStatus = MRPDealStatus;
	}
	
	@ExcelField(title="车间处理状态", align=2, sort=18)
	public String getSFCDealStatus() {
		return SFCDealStatus;
	}

	public void setSFCDealStatus(String SFCDealStatus) {
		this.SFCDealStatus = SFCDealStatus;
	}
	
	@ExcelField(title="已安排生产数量", align=2, sort=19)
	public Integer getAssignedQty() {
		return assignedQty;
	}

	public void setAssignedQty(Integer assignedQty) {
		this.assignedQty = assignedQty;
	}
	
	@ExcelField(title="计划制定人", align=2, sort=20)
	public String getMakePID() {
		return makePID;
	}

	public void setMakePID(String makePID) {
		this.makePID = makePID;
	}
	
	@ExcelField(title="计划提交人", align=2, sort=21)
	public String getConfirmPID() {
		return confirmPID;
	}

	public void setConfirmPID(String confirmPID) {
		this.confirmPID = confirmPID;
	}
	
	@ExcelField(title="计划审批人", align=2, sort=22)
	public String getAuditPID() {
		return auditPID;
	}

	public void setAuditPID(String auditPID) {
		this.auditPID = auditPID;
	}
	
	@ExcelField(title="计划下达人", align=2, sort=23)
	public String getDeliveryPID() {
		return deliveryPID;
	}

	public void setDeliveryPID(String deliveryPID) {
		this.deliveryPID = deliveryPID;
	}
	
	@ExcelField(title="计划结案人", align=2, sort=24)
	public String getClosePID() {
		return closePID;
	}

	public void setClosePID(String closePID) {
		this.closePID = closePID;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd")
	@ExcelField(title="计划制定日期", align=2, sort=25)
	public Date getMakeDate() {
		
		return makeDate;
	}

	public void setMakeDate(Date makeDate) {
		this.makeDate = makeDate;
	}
    @JsonFormat(pattern = "yyyy-MM-dd")
	@ExcelField(title="计划提交日期", align=2, sort=26)
	public Date getConfirmDate() {
		return confirmDate;
	}

	public void setConfirmDate(Date confirmDate) {
		this.confirmDate = confirmDate;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="计划审批日期", align=2, sort=27)
	public Date getAuditDate() {
		return auditDate;
	}

	public void setAuditDate(Date auditDate) {
		this.auditDate = auditDate;
	}
	
	@ExcelField(title="计划下达日期", align=2, sort=28)
	public String getDeliveryDate() {
		return deliveryDate;
	}

	public void setDeliveryDate(String deliveryDate) {
		this.deliveryDate = deliveryDate;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="计划结案日期", align=2, sort=29)
	public Date getCloseDate() {
		return closeDate;
	}

	public void setCloseDate(Date closeDate) {
		this.closeDate = closeDate;
	}
	
}