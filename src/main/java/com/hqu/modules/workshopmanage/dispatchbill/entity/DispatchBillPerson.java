/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.workshopmanage.dispatchbill.entity;


import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 车间派工单人员表Entity
 * @author zhangxin
 * @version 2018-05-31
 */
public class DispatchBillPerson extends DataEntity<DispatchBillPerson> {
	
	private static final long serialVersionUID = 1L;
	private String routineBillNo;		// 加工路线单号
	private String routingCode;		// 工艺号
	private DispatchBill dispatchNo;		// 派工单号 父类
	private String workerCode;		// 计划执行工人
	private String actualWorkerCode;		// 实际执行工人
	private String actualWorkHour;		// 实际执行工时
	
	public DispatchBillPerson() {
		super();
	}

	public DispatchBillPerson(String id){
		super(id);
	}

	public DispatchBillPerson(DispatchBill dispatchNo){
		this.dispatchNo = dispatchNo;
	}

	@ExcelField(title="加工路线单号", align=2, sort=7)
	public String getRoutineBillNo() {
		return routineBillNo;
	}

	public void setRoutineBillNo(String routineBillNo) {
		this.routineBillNo = routineBillNo;
	}
	
	@ExcelField(title="工艺号", align=2, sort=8)
	public String getRoutingCode() {
		return routingCode;
	}

	public void setRoutingCode(String routingCode) {
		this.routingCode = routingCode;
	}
	
	public DispatchBill getDispatchNo() {
		return dispatchNo;
	}

	public void setDispatchNo(DispatchBill dispatchNo) {
		this.dispatchNo = dispatchNo;
	}
	
	@ExcelField(title="计划执行工人", align=2, sort=10)
	public String getWorkerCode() {
		return workerCode;
	}

	public void setWorkerCode(String workerCode) {
		this.workerCode = workerCode;
	}
	
	@ExcelField(title="实际执行工人", align=2, sort=11)
	public String getActualWorkerCode() {
		return actualWorkerCode;
	}

	public void setActualWorkerCode(String actualWorkerCode) {
		this.actualWorkerCode = actualWorkerCode;
	}
	
	@ExcelField(title="实际执行工时", align=2, sort=12)
	public String getActualWorkHour() {
		return actualWorkHour;
	}

	public void setActualWorkHour(String actualWorkHour) {
		this.actualWorkHour = actualWorkHour;
	}
	
}