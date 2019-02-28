/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.workshopmanage.process.entity;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import com.google.common.collect.Lists;
import com.hqu.modules.workshopmanage.ppc.entity.MpsPlan;
import com.hqu.modules.workshopmanage.processbatch.entity.ProcessBatch;
import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 车间作业计划子表Entity
 * @author xhc
 * @version 2018-08-02
 */
public class SfcProcessDetail extends DataEntity<SfcProcessDetail> {
	
	private static final long serialVersionUID = 1L;
	private String processBillNo;		// 作业计划编号
	private String mpsPlanId;		// 主生产计划号
	private String billType;		// 单据类型
	private String billState;		// 单据状态
	private String assignedState;		// 派工状态
	private String prodCode;		// 产品编码
	private String prodName;		// 产品名称
	private String planQty;		// 计划数量
	private Date planBdate;		// 计划开始日期
	private Date planEdate;		// 计划结束日期
	private String finishQty;		// 已完成数量
	private String sfcNo;		// 车间号
	private String makePID;		// 制定人
	private Date makeDate;		// 制定日期
	private String confirmPID;		// 提交人
	private Date confirmDate;		// 提交日期
	private String deliveryPID;		// 下达人
	private Date deliveryDate;		// 下达日期
	private Date beginPlanBdate;		// 开始 计划开始日期
	private Date endPlanBdate;		// 结束 计划开始日期
	private Date beginPlanEdate;		// 开始 计划结束日期
	private Date endPlanEdate;		// 结束 计划结束日期
	private Date beginMakeDate;		// 开始 制定日期
	private Date endMakeDate;		// 结束 制定日期
	private Date beginConfirmDate;		// 开始 提交日期
	private Date endConfirmDate;		// 结束 提交日期
	private Date beginDeliveryDate;		// 开始 下达日期
	private Date endDeliveryDate;		// 结束 下达日期

	//车间作业分批列表子表 临时变量
	private List<ProcessBatch> processBatchList= Lists.newArrayList();  //分批表子表列表




	public SfcProcessDetail() {
		super();
	}

	public SfcProcessDetail(String id){
		super(id);
	}

	public SfcProcessDetail(MpsPlan mpsPlan) {
		mpsPlanId=mpsPlan.getMpsPlanid();
	}


	public List<ProcessBatch> getProcessBatchList() {
		return processBatchList;
	}

	public void setProcessBatchList(List<ProcessBatch> processBatchList) {
		this.processBatchList = processBatchList;
	}

	@ExcelField(title="作业计划编号", align=2, sort=7)
	public String getProcessBillNo() {
		return processBillNo;
	}

	public void setProcessBillNo(String processBillNo) {
		this.processBillNo = processBillNo;
	}
	
	@ExcelField(title="主生产计划号", align=2, sort=8)
	public String getMpsPlanId() {
		return mpsPlanId;
	}

	public void setMpsPlanId(String mpsPlanId) {
		this.mpsPlanId = mpsPlanId;
	}
	
	@ExcelField(title="单据类型", align=2, sort=9)
	public String getBillType() {
		return billType;
	}

	public void setBillType(String billType) {
		this.billType = billType;
	}
	
	@ExcelField(title="单据状态", align=2, sort=10)
	public String getBillState() {
		return billState;
	}

	public void setBillState(String billState) {
		this.billState = billState;
	}
	
	@ExcelField(title="派工状态", align=2, sort=11)
	public String getAssignedState() {
		return assignedState;
	}

	public void setAssignedState(String assignedState) {
		this.assignedState = assignedState;
	}
	
	@ExcelField(title="产品编码", align=2, sort=12)
	public String getProdCode() {
		return prodCode;
	}

	public void setProdCode(String prodCode) {
		this.prodCode = prodCode;
	}
	
	@ExcelField(title="产品名称", align=2, sort=13)
	public String getProdName() {
		return prodName;
	}

	public void setProdName(String prodName) {
		this.prodName = prodName;
	}
	
	@ExcelField(title="计划数量", align=2, sort=14)
	public String getPlanQty() {
		return planQty;
	}

	public void setPlanQty(String planQty) {
		this.planQty = planQty;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd")
	@ExcelField(title="计划开始日期", align=2, sort=15)
	public Date getPlanBdate() {
		return planBdate;
	}

	public void setPlanBdate(Date planBdate) {
		this.planBdate = planBdate;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd")
	@ExcelField(title="计划结束日期", align=2, sort=16)
	public Date getPlanEdate() {
		return planEdate;
	}

	public void setPlanEdate(Date planEdate) {
		this.planEdate = planEdate;
	}
	
	@ExcelField(title="已完成数量", align=2, sort=17)
	public String getFinishQty() {
		return finishQty;
	}

	public void setFinishQty(String finishQty) {
		this.finishQty = finishQty;
	}
	
	@ExcelField(title="车间号", align=2, sort=18)
	public String getSfcNo() {
		return sfcNo;
	}

	public void setSfcNo(String sfcNo) {
		this.sfcNo = sfcNo;
	}
	
	@ExcelField(title="制定人", fieldType=String.class, value="", align=2, sort=19)
	public String getMakePID() {
		return makePID;
	}

	public void setMakePID(String makePID) {
		this.makePID = makePID;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="制定日期", align=2, sort=20)
	public Date getMakeDate() {
		return makeDate;
	}

	public void setMakeDate(Date makeDate) {
		this.makeDate = makeDate;
	}
	
	@ExcelField(title="提交人", fieldType=String.class, value="", align=2, sort=21)
	public String getConfirmPID() {
		return confirmPID;
	}

	public void setConfirmPID(String confirmPID) {
		this.confirmPID = confirmPID;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="提交日期", align=2, sort=22)
	public Date getConfirmDate() {
		return confirmDate;
	}

	public void setConfirmDate(Date confirmDate) {
		this.confirmDate = confirmDate;
	}
	
	@ExcelField(title="下达人", fieldType=String.class, value="", align=2, sort=23)
	public String getDeliveryPID() {
		return deliveryPID;
	}

	public void setDeliveryPID(String deliveryPID) {
		this.deliveryPID = deliveryPID;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="下达日期", align=2, sort=24)
	public Date getDeliveryDate() {
		return deliveryDate;
	}

	public void setDeliveryDate(Date deliveryDate) {
		this.deliveryDate = deliveryDate;
	}
	
	public Date getBeginPlanBdate() {
		return beginPlanBdate;
	}

	public void setBeginPlanBdate(Date beginPlanBdate) {
		this.beginPlanBdate = beginPlanBdate;
	}
	
	public Date getEndPlanBdate() {
		return endPlanBdate;
	}

	public void setEndPlanBdate(Date endPlanBdate) {
		this.endPlanBdate = endPlanBdate;
	}
		
	public Date getBeginPlanEdate() {
		return beginPlanEdate;
	}

	public void setBeginPlanEdate(Date beginPlanEdate) {
		this.beginPlanEdate = beginPlanEdate;
	}
	
	public Date getEndPlanEdate() {
		return endPlanEdate;
	}

	public void setEndPlanEdate(Date endPlanEdate) {
		this.endPlanEdate = endPlanEdate;
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
		
	public Date getBeginConfirmDate() {
		return beginConfirmDate;
	}

	public void setBeginConfirmDate(Date beginConfirmDate) {
		this.beginConfirmDate = beginConfirmDate;
	}
	
	public Date getEndConfirmDate() {
		return endConfirmDate;
	}

	public void setEndConfirmDate(Date endConfirmDate) {
		this.endConfirmDate = endConfirmDate;
	}
		
	public Date getBeginDeliveryDate() {
		return beginDeliveryDate;
	}

	public void setBeginDeliveryDate(Date beginDeliveryDate) {
		this.beginDeliveryDate = beginDeliveryDate;
	}
	
	public Date getEndDeliveryDate() {
		return endDeliveryDate;
	}

	public void setEndDeliveryDate(Date endDeliveryDate) {
		this.endDeliveryDate = endDeliveryDate;
	}
		
}