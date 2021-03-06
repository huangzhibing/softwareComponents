/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.workshopmanage.ppc.entity;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * MRP计划Entity
 * @author XHC
 * @version 2018-06-28
 */
public class MrpPlan extends DataEntity<MrpPlan> {
	
	private static final long serialVersionUID = 1L;
	private String MRPPlanID;		// MRP计划号
	private String itemCode;		// 物料编码
	private String itemName;		// 物料名称
	private Integer planQty;		// 计划数量
	private Integer invAllocatedQty;		// 库存占用量
	private Integer finishQty;		// 已完工数量
	private String MPSplanid;		// MPS计划号
	private Date planBdate;		// 计划开始日期
	private Date planEDate;		// 计划结束日期
	private String planStatus;		// 计划状态
	private String purDealStatus;		// 采购处理状态
	private String sfcDealStatus;		// 车间处理状态
	private String makePID;		// 计划制定人
	private String confirmPID;		// 计划提交人
	private String auditPID;		// 计划审批人
	private String deliveryPID;		// 计划下达人
	private String closePID;		// 计划结案人
	private Date makeDate;		// 计划制定日期
	private Date confirmDate;		// 计划提交日期
	private Date auditDate;		// 计划审批日期
	private Date deliveryDate;		// 计划下达日期
	private Date closeDate;		// 计划结案日期
	private String auditComment ;		// 审批意见
	private String isLeaf;		// 是否叶节点
	
	public MrpPlan() {
		super();
	}

	public MrpPlan(String id){
		super(id);
	}

	@ExcelField(title="MRP计划号", align=2, sort=6)
	public String getMRPPlanID() {
		return MRPPlanID;
	}

	public void setMRPPlanID(String MRPPlanID) {
		this.MRPPlanID = MRPPlanID;
	}
	
	@ExcelField(title="物料编码", align=2, sort=7)
	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}
	
	@ExcelField(title="物料名称", align=2, sort=8)
	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	
	@ExcelField(title="计划数量", align=2, sort=9)
	public Integer getPlanQty() {
		return planQty;
	}

	public void setPlanQty(Integer planQty) {
		this.planQty = planQty;
	}
	
	@ExcelField(title="库存占用量", align=2, sort=10)
	public Integer getInvAllocatedQty() {
		return invAllocatedQty;
	}

	public void setInvAllocatedQty(Integer invAllocatedQty) {
		this.invAllocatedQty = invAllocatedQty;
	}
	
	@ExcelField(title="已完工数量", align=2, sort=11)
	public Integer getFinishQty() {
		return finishQty;
	}

	public void setFinishQty(Integer finishQty) {
		this.finishQty = finishQty;
	}
	
	@ExcelField(title="MPS计划号", align=2, sort=12)
	public String getMPSplanid() {
		return MPSplanid;
	}

	public void setMPSplanid(String MPSplanid) {
		this.MPSplanid = MPSplanid;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="计划开始日期", align=2, sort=13)
	public Date getPlanBdate() {
		return planBdate;
	}

	public void setPlanBdate(Date planBdate) {
		this.planBdate = planBdate;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="计划结束日期", align=2, sort=14)
	public Date getPlanEDate() {
		return planEDate;
	}

	public void setPlanEDate(Date planEDate) {
		this.planEDate = planEDate;
	}
	
	@ExcelField(title="计划状态", align=2, sort=15)
	public String getPlanStatus() {
		return planStatus;
	}

	public void setPlanStatus(String planStatus) {
		this.planStatus = planStatus;
	}
	
	@ExcelField(title="采购处理状态", align=2, sort=16)
	public String getPurDealStatus() {
		return purDealStatus;
	}

	public void setPurDealStatus(String purDealStatus) {
		this.purDealStatus = purDealStatus;
	}
	
	@ExcelField(title="车间处理状态", align=2, sort=17)
	public String getSfcDealStatus() {
		return sfcDealStatus;
	}

	public void setSfcDealStatus(String sfcDealStatus) {
		this.sfcDealStatus = sfcDealStatus;
	}
	
	@ExcelField(title="计划制定人", align=2, sort=18)
	public String getMakePID() {
		return makePID;
	}

	public void setMakePID(String makePID) {
		this.makePID = makePID;
	}
	
	@ExcelField(title="计划提交人", align=2, sort=19)
	public String getConfirmPID() {
		return confirmPID;
	}

	public void setConfirmPID(String confirmPID) {
		this.confirmPID = confirmPID;
	}
	
	@ExcelField(title="计划审批人", align=2, sort=20)
	public String getAuditPID() {
		return auditPID;
	}

	public void setAuditPID(String auditPID) {
		this.auditPID = auditPID;
	}
	
	@ExcelField(title="计划下达人", align=2, sort=21)
	public String getDeliveryPID() {
		return deliveryPID;
	}

	public void setDeliveryPID(String deliveryPID) {
		this.deliveryPID = deliveryPID;
	}
	
	@ExcelField(title="计划结案人", align=2, sort=22)
	public String getClosePID() {
		return closePID;
	}

	public void setClosePID(String closePID) {
		this.closePID = closePID;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="计划制定日期", align=2, sort=23)
	public Date getMakeDate() {
		return makeDate;
	}

	public void setMakeDate(Date makeDate) {
		this.makeDate = makeDate;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="计划提交日期", align=2, sort=24)
	public Date getConfirmDate() {
		return confirmDate;
	}

	public void setConfirmDate(Date confirmDate) {
		this.confirmDate = confirmDate;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="计划审批日期", align=2, sort=25)
	public Date getAuditDate() {
		return auditDate;
	}

	public void setAuditDate(Date auditDate) {
		this.auditDate = auditDate;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="计划下达日期", align=2, sort=26)
	public Date getDeliveryDate() {
		return deliveryDate;
	}

	public void setDeliveryDate(Date deliveryDate) {
		this.deliveryDate = deliveryDate;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="计划结案日期", align=2, sort=27)
	public Date getCloseDate() {
		return closeDate;
	}

	public void setCloseDate(Date closeDate) {
		this.closeDate = closeDate;
	}
	
	@ExcelField(title="审批意见", align=2, sort=28)
	public String getAuditComment () {
		return auditComment ;
	}

	public void setAuditComment (String auditComment ) {
		this.auditComment  = auditComment ;
	}
	
	@ExcelField(title="是否叶节点", align=2, sort=30)
	public String getIsLeaf() {
		return isLeaf;
	}

	public void setIsLeaf(String isLeaf) {
		this.isLeaf = isLeaf;
	}
	
}