/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.basedata.workprocedure.entity;

import com.jeeplus.modules.sys.entity.Office;
import javax.validation.constraints.NotNull;
import com.jeeplus.modules.sys.entity.User;
import com.hqu.modules.basedata.machinetype.entity.MachineType;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 工序定义Entity
 * @author liujiachen
 * @version 2018-04-24
 */
public class WorkProcedure extends DataEntity<WorkProcedure> {
	
	private static final long serialVersionUID = 1L;
	private String workProcedureId;		// 工序编码
	private String workProcedureName;		// 工序名称
	private String workProcedureDesc;		// 工序描述
	private Office deptCode;		// 所属部门编码
	private String deptName;		// 所属部门名称
	private User user;		// 负责人编码
	private String userNameRu;		// 负责人名称
	private MachineType machineTypeCode;		// 工作中心设备类别编码
	private String machineTypeName;		// 工作中心设备类别名称
	private Integer workTime;		// 定额工时
	private String isConvey;		// 是否转序
	
	public WorkProcedure() {
		super();
	}

	public WorkProcedure(String id){
		super(id);
	}

	@ExcelField(title="工序编码", align=2, sort=6)
	public String getWorkProcedureId() {
		return workProcedureId;
	}

	public void setWorkProcedureId(String workProcedureId) {
		this.workProcedureId = workProcedureId;
	}
	
	@ExcelField(title="工序名称", align=2, sort=7)
	public String getWorkProcedureName() {
		return workProcedureName;
	}

	public void setWorkProcedureName(String workProcedureName) {
		this.workProcedureName = workProcedureName;
	}
	
	@ExcelField(title="工序描述", align=2, sort=8)
	public String getWorkProcedureDesc() {
		return workProcedureDesc;
	}

	public void setWorkProcedureDesc(String workProcedureDesc) {
		this.workProcedureDesc = workProcedureDesc;
	}
	
	@NotNull(message="所属部门编码不能为空")
	@ExcelField(title="所属部门编码", fieldType=Office.class, value="deptCode.id", align=2, sort=9)
	public Office getDeptCode() {
		return deptCode;
	}

	public void setDeptCode(Office deptCode) {
		this.deptCode = deptCode;
	}
	
	@ExcelField(title="所属部门名称", align=2, sort=10)
	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	
	@NotNull(message="负责人编码不能为空")
	@ExcelField(title="负责人编码", fieldType=User.class, value="user.name", align=2, sort=11)
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	@ExcelField(title="负责人名称", align=2, sort=12)
	public String getUserNameRu() {
		return userNameRu;
	}

	public void setUserNameRu(String userNameRu) {
		this.userNameRu = userNameRu;
	}
	
	@NotNull(message="工作中心设备类别编码不能为空")
	@ExcelField(title="工作中心设备类别编码", fieldType=MachineType.class, value="machineTypeCode.machineTypeName", align=2, sort=13)
	public MachineType getMachineTypeCode() {
		return machineTypeCode;
	}

	public void setMachineTypeCode(MachineType machineTypeCode) {
		this.machineTypeCode = machineTypeCode;
	}
	
	@ExcelField(title="工作中心设备类别名称", align=2, sort=14)
	public String getMachineTypeName() {
		return machineTypeName;
	}

	public void setMachineTypeName(String machineTypeName) {
		this.machineTypeName = machineTypeName;
	}
	
	@ExcelField(title="定额工时", align=2, sort=15)
	public Integer getWorkTime() {
		return workTime;
	}

	public void setWorkTime(Integer workTime) {
		this.workTime = workTime;
	}
	
	@ExcelField(title="是否转序", dictType="is_convey", align=2, sort=16)
	public String getIsConvey() {
		return isConvey;
	}

	public void setIsConvey(String isConvey) {
		this.isConvey = isConvey;
	}
	
}