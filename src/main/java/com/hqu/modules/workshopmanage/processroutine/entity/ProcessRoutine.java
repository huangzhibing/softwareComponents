/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.workshopmanage.processroutine.entity;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import com.google.common.collect.Lists;
import com.hqu.modules.workshopmanage.processbatch.entity.ProcessBatch;
import com.hqu.modules.workshopmanage.processroutinedetail.entity.ProcessRoutineDetail;
import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 车间加工路线单表Entity
 * @author xhc
 * @version 2018-08-06
 */
public class ProcessRoutine extends DataEntity<ProcessRoutine> {
	
	private static final long serialVersionUID = 1L;
	private String processBillNo;		// 车间计划作业号
	private Integer batchNo;		// 批次序号
	private String routineBillNo;		// 加工路线单号
	private Integer produceNo;		// 生产序号
	private String routingCode;		// 工艺编码
	private String workprocedureCode;		// 工序编码
	private String centerCode;		// 工作中心代码
	private String isLastRouting;		// 末道工艺标志
	private String prodCode;		// 产品编码
	private String prodName;		// 产品名称
	private Double planQty;		// 计划生产数量
	private Date planBData;		// 计划生产日期
	private Double realQty;		// 实际生产数量
	private Date planEDate;		// 实际生产日期
	private String billState;		// 单据状态
	private String assignedState;		// 派工状态
	private String makePID;		// 制定人
	private String actualCenterCode;		// 实作工作中心
	private String personIncharge;		// 负责人
	private String teamCode;		// 计划班组
	private String actualTeamCode;		// 实作班组
	private String shiftname;		// 班次
	private Double workhour;		// 计划工时
	private Double actualWorkhour;		// 实作工时
	private Date makeDate;		// 制定日期
	private String confirmPID;		// 提交人
	private Date confirmDate;		// 提交日期
	private String deliveryPID;		// 下达人
	private Date deliveryDate;		// 下达日期
	private String routingName;    //工艺名称
	private Integer seqNo;//单件序号
	private Date actualBDate;//实际开始时间（安规激活老化）
	private Date beginPlanBData;		// 开始 计划生产日期
	private Date endPlanBData;		// 结束 计划生产日期
	private Date beginPlanEDate;		// 开始 实际生产日期
	private Date endPlanEDate;		// 结束 实际生产日期
	private Date beginMakeDate;		// 开始 制定日期
	private Date endMakeDate;		// 结束 制定日期
	private Date beginConfirmDate;		// 开始 提交日期
	private Date endConfirmDate;		// 结束 提交日期
	private Date beginDeliveryDate;		// 开始 下达日期
	private Date endDeliveryDate;		// 结束 下达日期
	private String cosBillNum;
	private String periodId;	//存储需要用的核算期

	//private List<ProcessRoutineDetail> processRoutineDetailList = Lists.newArrayList();//加工路线明细列表 临时变量
	
	public ProcessRoutine() {
		super();
	}

	public ProcessRoutine(String id){
		super(id);
	}

    public ProcessRoutine(ProcessBatch processBatch) {
		processBillNo=processBatch.getProcessBillNo();
		batchNo=processBatch.getBatchNo();
    }


/*	public List<ProcessRoutineDetail> getProcessRoutineDetailList() {
		return processRoutineDetailList;
	}

	public void setProcessRoutineDetailList(List<ProcessRoutineDetail> processRoutineDetailList) {
		this.processRoutineDetailList = processRoutineDetailList;
	}*/

	public String getPeriodId() {
		return periodId;
	}

	public void setPeriodId(String periodId) {
		this.periodId = periodId;
	}

	public String getRoutingName() {
		return routingName;
	}

	public void setRoutingName(String routingName) {
		this.routingName = routingName;
	}

	public Integer getSeqNo() {
		return seqNo;
	}

	public void setSeqNo(Integer seqNo) {
		this.seqNo = seqNo;
	}

	@ExcelField(title="车间计划作业号", align=2, sort=7)
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
	
	@ExcelField(title="加工路线单号", align=2, sort=9)
	public String getRoutineBillNo() {
		return routineBillNo;
	}

	public void setRoutineBillNo(String routineBillNo) {
		this.routineBillNo = routineBillNo;
	}
	
	@ExcelField(title="生产序号", align=2, sort=10)
	public Integer getProduceNo() {
		return produceNo;
	}

	public void setProduceNo(Integer produceNo) {
		this.produceNo = produceNo;
	}
	
	@ExcelField(title="工艺编码", align=2, sort=11)
	public String getRoutingCode() {
		return routingCode;
	}

	public void setRoutingCode(String routingCode) {
		this.routingCode = routingCode;
	}
	
	@ExcelField(title="工序编码", align=2, sort=12)
	public String getWorkprocedureCode() {
		return workprocedureCode;
	}

	public void setWorkprocedureCode(String workprocedureCode) {
		this.workprocedureCode = workprocedureCode;
	}
	
	@ExcelField(title="工作中心代码", align=2, sort=13)
	public String getCenterCode() {
		return centerCode;
	}

	public void setCenterCode(String centerCode) {
		this.centerCode = centerCode;
	}
	
	@ExcelField(title="末道工艺标志", align=2, sort=14)
	public String getIsLastRouting() {
		return isLastRouting;
	}

	public void setIsLastRouting(String isLastRouting) {
		this.isLastRouting = isLastRouting;
	}
	
	@ExcelField(title="产品编码", align=2, sort=15)
	public String getProdCode() {
		return prodCode;
	}

	public void setProdCode(String prodCode) {
		this.prodCode = prodCode;
	}
	
	@ExcelField(title="产品名称", align=2, sort=16)
	public String getProdName() {
		return prodName;
	}

	public void setProdName(String prodName) {
		this.prodName = prodName;
	}
	
	@ExcelField(title="计划生产数量", align=2, sort=17)
	public Double getPlanQty() {
		return planQty;
	}

	public void setPlanQty(Double planQty) {
		this.planQty = planQty;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd")
	@ExcelField(title="计划生产日期", align=2, sort=18)
	public Date getPlanBData() {
		return planBData;
	}

	public void setPlanBData(Date planBData) {
		this.planBData = planBData;
	}
	
	@ExcelField(title="实际生产数量", align=2, sort=19)
	public Double getRealQty() {
		return realQty;
	}

	public void setRealQty(Double realQty) {
		this.realQty = realQty;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd")
	@ExcelField(title="实际生产日期", align=2, sort=20)
	public Date getPlanEDate() {
		return planEDate;
	}

	public void setPlanEDate(Date planEDate) {
		this.planEDate = planEDate;
	}
	
	@ExcelField(title="单据状态", align=2, sort=21)
	public String getBillState() {
		return billState;
	}

	public void setBillState(String billState) {
		this.billState = billState;
	}
	
	@ExcelField(title="派工状态", align=2, sort=22)
	public String getAssignedState() {
		return assignedState;
	}

	public void setAssignedState(String assignedState) {
		this.assignedState = assignedState;
	}
	
	@ExcelField(title="制定人", align=2, sort=23)
	public String getMakePID() {
		return makePID;
	}

	public void setMakePID(String makePID) {
		this.makePID = makePID;
	}
	
	@ExcelField(title="实作工作中心", align=2, sort=24)
	public String getActualCenterCode() {
		return actualCenterCode;
	}

	public void setActualCenterCode(String actualCenterCode) {
		this.actualCenterCode = actualCenterCode;
	}
	
	@ExcelField(title="负责人", align=2, sort=25)
	public String getPersonIncharge() {
		return personIncharge;
	}

	public void setPersonIncharge(String personIncharge) {
		this.personIncharge = personIncharge;
	}
	
	@ExcelField(title="计划班组", align=2, sort=26)
	public String getTeamCode() {
		return teamCode;
	}

	public void setTeamCode(String teamCode) {
		this.teamCode = teamCode;
	}
	
	@ExcelField(title="实作班组", align=2, sort=27)
	public String getActualTeamCode() {
		return actualTeamCode;
	}

	public void setActualTeamCode(String actualTeamCode) {
		this.actualTeamCode = actualTeamCode;
	}
	
	@ExcelField(title="班次", align=2, sort=28)
	public String getShiftname() {
		return shiftname;
	}

	public void setShiftname(String shiftName) {
		this.shiftname = shiftName;
	}
	
	@ExcelField(title="计划工时", align=2, sort=29)
	public Double getWorkhour() {
		return workhour;
	}

	public void setWorkhour(Double workhour) {
		this.workhour = workhour;
	}
	
	@ExcelField(title="实作工时", align=2, sort=30)
	public Double getActualWorkhour() {
		return actualWorkhour;
	}

	public void setActualWorkhour(Double actualWorkhour) {
		this.actualWorkhour = actualWorkhour;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="制定日期", align=2, sort=31)
	public Date getMakeDate() {
		return makeDate;
	}

	public void setMakeDate(Date makeDate) {
		this.makeDate = makeDate;
	}
	
	@ExcelField(title="提交人", align=2, sort=32)
	public String getConfirmPID() {
		return confirmPID;
	}

	public void setConfirmPID(String confirmPID) {
		this.confirmPID = confirmPID;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="提交日期", align=2, sort=33)
	public Date getConfirmDate() {
		return confirmDate;
	}

	public void setConfirmDate(Date confirmDate) {
		this.confirmDate = confirmDate;
	}
	
	@ExcelField(title="下达人", align=2, sort=34)
	public String getDeliveryPID() {
		return deliveryPID;
	}

	public void setDeliveryPID(String deliveryPID) {
		this.deliveryPID = deliveryPID;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="下达日期", align=2, sort=35)
	public Date getDeliveryDate() {
		return deliveryDate;
	}

	public void setDeliveryDate(Date deliveryDate) {
		this.deliveryDate = deliveryDate;
	}
	
	public Date getBeginPlanBData() {
		return beginPlanBData;
	}

	public void setBeginPlanBData(Date beginPlanBData) {
		this.beginPlanBData = beginPlanBData;
	}
	
	public Date getEndPlanBData() {
		return endPlanBData;
	}

	public void setEndPlanBData(Date endPlanBData) {
		this.endPlanBData = endPlanBData;
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

	public String getCosBillNum() {
		return cosBillNum;
	}

	public void setCosBillNum(String cosBillNum) {
		this.cosBillNum = cosBillNum;
	}

	public Date getActualBDate() {
		return actualBDate;
	}

	public void setActualBDate(Date actualBDate) {
		this.actualBDate = actualBDate;
	}
}