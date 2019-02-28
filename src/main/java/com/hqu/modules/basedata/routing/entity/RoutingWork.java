/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.basedata.routing.entity;

import com.hqu.modules.basedata.workprocedure.entity.WorkProcedure;
import javax.validation.constraints.NotNull;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 工艺路线工序Entity
 * @author huang.youcai
 * @version 2018-06-02
 */
public class RoutingWork extends DataEntity<RoutingWork> {
	
	private static final long serialVersionUID = 1L;
	private Routing routingCode;		// 工艺编码 父类
	private WorkProcedure workProcedure;		// 工序编码
	private String workProcedureName;		// 工序名称
	private String workPersonCode;		// 负责人编码
	private String workPersonName;		// 负责人名称
	private String workProcedureDesc;		// 工序职能说明
	private Integer workTime;		// 定额工时
	private Integer workPorcedureSeqNo;    //工序序号

	public RoutingWork() {
		super();
	}

	public RoutingWork(String id){
		super(id);
	}

	public RoutingWork(Routing routingCode){
		this.routingCode = routingCode;
	}

	public Routing getRoutingCode() {
		return routingCode;
	}

	public void setRoutingCode(Routing routingCode) {
		this.routingCode = routingCode;
	}
	
	@NotNull(message="工序编码不能为空")
	@ExcelField(title="工序编码", fieldType=WorkProcedure.class, value="workProcedure.workProcedureName", align=2, sort=8)
	public WorkProcedure getWorkProcedure() {
		return workProcedure;
	}

	public void setWorkProcedure(WorkProcedure workProcedure) {
		this.workProcedure = workProcedure;
	}
	
	@ExcelField(title="工序名称", align=2, sort=9)
	public String getWorkProcedureName() {
		return workProcedureName;
	}

	public void setWorkProcedureName(String workProcedureName) {
		this.workProcedureName = workProcedureName;
	}
	
	@ExcelField(title="负责人编码", align=2, sort=10)
	public String getWorkPersonCode() {
		return workPersonCode;
	}

	public void setWorkPersonCode(String workPersonCode) {
		this.workPersonCode = workPersonCode;
	}
	
	@ExcelField(title="负责人名称", align=2, sort=11)
	public String getWorkPersonName() {
		return workPersonName;
	}

	public void setWorkPersonName(String workPersonName) {
		this.workPersonName = workPersonName;
	}
	
	@ExcelField(title="工序职能说明", align=2, sort=12)
	public String getWorkProcedureDesc() {
		return workProcedureDesc;
	}

	public void setWorkProcedureDesc(String workProcedureDesc) {
		this.workProcedureDesc = workProcedureDesc;
	}
	
	@NotNull(message="定额工时不能为空")
	@ExcelField(title="定额工时", align=2, sort=13)
	public Integer getWorkTime() {
		return workTime;
	}

	public void setWorkTime(Integer workTime) {
		this.workTime = workTime;
	}

	public Integer getWorkPorcedureSeqNo() {
		return workPorcedureSeqNo;
	}

	public void setWorkPorcedureSeqNo(Integer workPorcedureSeqNo) {
		this.workPorcedureSeqNo = workPorcedureSeqNo;
	}
}