/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.workshopmanage.processbatch.entity;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import com.google.common.collect.Lists;
import com.hqu.modules.workshopmanage.process.entity.SfcProcessDetail;
import com.hqu.modules.workshopmanage.processroutine.entity.ProcessRoutine;
import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 车间作业计划分批表Entity
 * @author xhc
 * @version 2018-08-04
 */
public class ProcessBatch extends DataEntity<ProcessBatch> {
	
	private static final long serialVersionUID = 1L;
	private String processBillNo;		// 车间作业计划号
	private Integer batchNo;		// 批次序号
	private String prodCode;		// 产品编码
	private String prodName;      //产品名称
	private Double planQty;		// 计划生产数量
	private Date planBDate;		// 计划生产日期
	private Double realQty;		// 实际生产数量
	private Date planEDate;		// 实际生产日期
	private String billState;		// 单据状态
	private String assignedState;		// 派工状态
	private String makeID;		// 制定人
	private Date makeDate;		// 制定日期
	private String confirmPid;		// 提交人
	private Date confirmDate;		// 提交日期
	private String deliveryPid;		// 下达人
	private Date deliveryDate;		// 下达日期
	private Date beginPlanBDate;		// 开始 计划生产日期
	private Date endPlanBDate;		// 结束 计划生产日期
	private Date beginPlanEDate;		// 开始 实际生产日期
	private Date endPlanEDate;		// 结束 实际生产日期
	private Date beginMakeDate;		// 开始 制定日期
	private Date endMakeDate;		// 结束 制定日期
	private Date beginConfirmDate;		// 开始 提交日期
	private Date endConfirmDate;		// 结束 提交日期
	private Date beginDeliveryDate;		// 开始 下达日期
	private Date endDeliveryDate;		// 结束 下达日期

	//车间加工路线子表 临时变量
	private List<ProcessRoutine> processRoutineList=Lists.newArrayList();// 加工路线列表（分批表子表）

	
	public ProcessBatch() {
		super();
	}

	public ProcessBatch(String id){
		super(id);
	}

	public ProcessBatch(SfcProcessDetail sfcProcessDetail) {
		processBillNo=sfcProcessDetail.getProcessBillNo();
	}

	public List<ProcessRoutine> getProcessRoutineList() {
		return processRoutineList;
	}

	public void setProcessRoutineList(List<ProcessRoutine> processRoutineList) {
		this.processRoutineList = processRoutineList;
	}

	@ExcelField(title="车间作业计划号", align=2, sort=7)
	public String getProcessBillNo() {
		return processBillNo;
	}

	public void setProcessBillNo(String processBillNo) {
		this.processBillNo = processBillNo;
	}
	
	@ExcelField(title="批次序号", align=2, sort=8)
	public Integer getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(Integer batchNo) {
		this.batchNo = batchNo;
	}
	
	@ExcelField(title="产品编码", align=2, sort=9)
	public String getProdCode() {
		return prodCode;
	}

	public void setProdCode(String prodCode) {
		this.prodCode = prodCode;
	}

	@ExcelField(title="产品名称", align=2, sort=10)
	public String getProdName() {
		return prodName;
	}

	public void setProdName(String prodName) {
		this.prodName = prodName;
	}

	@ExcelField(title="计划生产数量", align=2, sort=22)
	public Double getPlanQty() {
		return planQty;
	}

	public void setPlanQty(Double planQty) {
		this.planQty = planQty;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd")
	@ExcelField(title="计划生产日期", align=2, sort=11)
	public Date getPlanBDate() {
		return planBDate;
	}

	public void setPlanBDate(Date planBDate) {
		this.planBDate = planBDate;
	}
	
	@ExcelField(title="实际生产数量", align=2, sort=12)
	public Double getRealQty() {
		return realQty;
	}

	public void setRealQty(Double realQty) {
		this.realQty = realQty;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd")
	@ExcelField(title="实际生产日期", align=2, sort=13)
	public Date getPlanEDate() {
		return planEDate;
	}

	public void setPlanEDate(Date planEDate) {
		this.planEDate = planEDate;
	}
	
	@ExcelField(title="单据状态", align=2, sort=14)
	public String getBillState() {
		return billState;
	}

	public void setBillState(String billState) {
		this.billState = billState;
	}
	
	@ExcelField(title="派工状态", align=2, sort=15)
	public String getAssignedState() {
		return assignedState;
	}

	public void setAssignedState(String assignedState) {
		this.assignedState = assignedState;
	}
	
	@ExcelField(title="制定人", align=2, sort=16)
	public String getMakeID() {
		return makeID;
	}

	public void setMakeID(String makeID) {
		this.makeID = makeID;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="制定日期", align=2, sort=17)
	public Date getMakeDate() {
		return makeDate;
	}

	public void setMakeDate(Date makeDate) {
		this.makeDate = makeDate;
	}
	
	@ExcelField(title="提交人", align=2, sort=18)
	public String getConfirmPid() {
		return confirmPid;
	}

	public void setConfirmPid(String confirmPid) {
		this.confirmPid = confirmPid;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="提交日期", align=2, sort=19)
	public Date getConfirmDate() {
		return confirmDate;
	}

	public void setConfirmDate(Date confirmDate) {
		this.confirmDate = confirmDate;
	}
	
	@ExcelField(title="下达人", align=2, sort=20)
	public String getDeliveryPid() {
		return deliveryPid;
	}

	public void setDeliveryPid(String deliveryPid) {
		this.deliveryPid = deliveryPid;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="下达日期", align=2, sort=21)
	public Date getDeliveryDate() {
		return deliveryDate;
	}

	public void setDeliveryDate(Date deliveryDate) {
		this.deliveryDate = deliveryDate;
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