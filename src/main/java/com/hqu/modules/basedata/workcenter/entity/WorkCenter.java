/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.basedata.workcenter.entity;

import com.jeeplus.modules.sys.entity.Office;
import javax.validation.constraints.NotNull;
import com.hqu.modules.basedata.machinetype.entity.MachineType;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 工作中心Entity
 * @author dongqida
 * @version 2018-04-05
 */
public class WorkCenter extends DataEntity<WorkCenter> {
	
	private static final long serialVersionUID = 1L;
	private String centerCode;		// 工作中心代码
	private String centerName;		// 工作中心名称
	private Office deptCode;		// 部门编码
	private String deptName;		// 部门名称
	private String centerType;		// 核算类别
	private String centerKey;		// 关键工作中心
	private Integer personNum;		// 人员数
	private Integer machineNum;		// 设备数
	private Integer teamNum;		// 班次数
	private Integer centerRate;		// 工作中心日额定能力
	private Integer machineRate;		// 设备日额定能力
	private Integer teamRate;		// 额定班次
	private MachineType machineTypeCode;		// 工作中心设备类别编码
	private String machineTypeName;		// 工作中心设备类别名称
	private String note;		// 备注
	
	public WorkCenter() {
		super();
	}

	public WorkCenter(String id){
		super(id);
	}

	@ExcelField(title="工作中心代码", align=2, sort=7)
	public String getCenterCode() {
		return centerCode;
	}

	public void setCenterCode(String centerCode) {
		this.centerCode = centerCode;
	}
	
	@ExcelField(title="工作中心名称", align=2, sort=8)
	public String getCenterName() {
		return centerName;
	}

	public void setCenterName(String centerName) {
		this.centerName = centerName;
	}
	
	@NotNull(message="部门编码不能为空")
	@ExcelField(title="部门编码", fieldType=Office.class, value="deptCode.name", align=2, sort=9)
	public Office getDeptCode() {
		return deptCode;
	}

	public void setDeptCode(Office deptCode) {
		this.deptCode = deptCode;
	}
	
	@ExcelField(title="部门名称", align=2, sort=10)
	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	
	@ExcelField(title="核算类别", dictType="centerType", align=2, sort=11)
	public String getCenterType() {
		return centerType;
	}

	public void setCenterType(String centerType) {
		this.centerType = centerType;
	}
	
	@ExcelField(title="关键工作中心", dictType="centerKey", align=2, sort=12)
	public String getCenterKey() {
		return centerKey;
	}

	public void setCenterKey(String centerKey) {
		this.centerKey = centerKey;
	}
	
	@ExcelField(title="人员数", align=2, sort=13)
	public Integer getPersonNum() {
		return personNum;
	}

	public void setPersonNum(Integer personNum) {
		this.personNum = personNum;
	}
	
	@ExcelField(title="设备数", align=2, sort=14)
	public Integer getMachineNum() {
		return machineNum;
	}

	public void setMachineNum(Integer machineNum) {
		this.machineNum = machineNum;
	}
	
	@ExcelField(title="班次数", align=2, sort=15)
	public Integer getTeamNum() {
		return teamNum;
	}

	public void setTeamNum(Integer teamNum) {
		this.teamNum = teamNum;
	}
	
	@ExcelField(title="工作中心日额定能力", align=2, sort=16)
	public Integer getCenterRate() {
		return centerRate;
	}

	public void setCenterRate(Integer centerRate) {
		this.centerRate = centerRate;
	}
	
	@ExcelField(title="设备日额定能力", align=2, sort=17)
	public Integer getMachineRate() {
		return machineRate;
	}

	public void setMachineRate(Integer machineRate) {
		this.machineRate = machineRate;
	}
	
	@ExcelField(title="额定班次", align=2, sort=18)
	public Integer getTeamRate() {
		return teamRate;
	}

	public void setTeamRate(Integer teamRate) {
		this.teamRate = teamRate;
	}
	
	@NotNull(message="工作中心设备类别编码不能为空")
	@ExcelField(title="工作中心设备类别编码", fieldType=MachineType.class, value="machineTypeCode.machineTypeName", align=2, sort=19)
	public MachineType getMachineTypeCode() {
		return machineTypeCode;
	}

	public void setMachineTypeCode(MachineType machineTypeCode) {
		this.machineTypeCode = machineTypeCode;
	}
	
	@ExcelField(title="工作中心设备类别名称", align=2, sort=20)
	public String getMachineTypeName() {
		return machineTypeName;
	}

	public void setMachineTypeName(String machineTypeName) {
		this.machineTypeName = machineTypeName;
	}
	
	@ExcelField(title="备注", align=2, sort=21)
	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}
	
}