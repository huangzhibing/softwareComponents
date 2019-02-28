/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.workshopmanage.dispatchbill.entity;

import java.util.List;
import com.google.common.collect.Lists;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 车间派工单Entity
 * @author zhangxin
 * @version 2018-05-31
 */
public class DispatchBill extends DataEntity<DispatchBill> {
	
	private static final long serialVersionUID = 1L;
	private String routineBillNo;		// 加工路线单号
	private String routingCode;		// 工艺号
	private String dispatchNo;		// 派工单号
	private String centerCode;		// 计划工作中心
	private String actualcenterCode;		// 实作工作中心
	private String personInCharge;		// 负责人
	private String teamCode;		// 计划班组
	private String actualTeamCode;		// 实作班组
	private String shiftCode;		// 班次
	private String workHour;		// 计划工时
	private String actualWorkHour;		// 实作工时
	private List<DispatchBillPerson> dispatchBillPersonList = Lists.newArrayList();		// 子表列表
	
	public DispatchBill() {
		super();
	}

	public DispatchBill(String id){
		super(id);
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
	
	@ExcelField(title="派工单号", align=2, sort=9)
	public String getDispatchNo() {
		return dispatchNo;
	}

	public void setDispatchNo(String dispatchNo) {
		this.dispatchNo = dispatchNo;
	}
	
	@ExcelField(title="计划工作中心", align=2, sort=10)
	public String getCenterCode() {
		return centerCode;
	}

	public void setCenterCode(String centerCode) {
		this.centerCode = centerCode;
	}
	
	@ExcelField(title="实作工作中心", align=2, sort=11)
	public String getActualcenterCode() {
		return actualcenterCode;
	}

	public void setActualcenterCode(String actualcenterCode) {
		this.actualcenterCode = actualcenterCode;
	}
	
	@ExcelField(title="负责人", align=2, sort=12)
	public String getPersonInCharge() {
		return personInCharge;
	}

	public void setPersonInCharge(String personInCharge) {
		this.personInCharge = personInCharge;
	}
	
	@ExcelField(title="计划班组", align=2, sort=13)
	public String getTeamCode() {
		return teamCode;
	}

	public void setTeamCode(String teamCode) {
		this.teamCode = teamCode;
	}
	
	@ExcelField(title="实作班组", align=2, sort=14)
	public String getActualTeamCode() {
		return actualTeamCode;
	}

	public void setActualTeamCode(String actualTeamCode) {
		this.actualTeamCode = actualTeamCode;
	}
	
	@ExcelField(title="班次", align=2, sort=15)
	public String getShiftCode() {
		return shiftCode;
	}

	public void setShiftCode(String shiftCode) {
		this.shiftCode = shiftCode;
	}
	
	@ExcelField(title="计划工时", align=2, sort=16)
	public String getWorkHour() {
		return workHour;
	}

	public void setWorkHour(String workHour) {
		this.workHour = workHour;
	}
	
	@ExcelField(title="实作工时", align=2, sort=17)
	public String getActualWorkHour() {
		return actualWorkHour;
	}

	public void setActualWorkHour(String actualWorkHour) {
		this.actualWorkHour = actualWorkHour;
	}
	
	public List<DispatchBillPerson> getDispatchBillPersonList() {
		return dispatchBillPersonList;
	}

	public void setDispatchBillPersonList(List<DispatchBillPerson> dispatchBillPersonList) {
		this.dispatchBillPersonList = dispatchBillPersonList;
	}
}