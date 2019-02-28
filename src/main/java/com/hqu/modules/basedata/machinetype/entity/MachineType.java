/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.basedata.machinetype.entity;

import org.hibernate.validator.constraints.Length;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 设备类别定义Entity
 * @author 何志铭
 * @version 2018-04-05
 */
public class MachineType extends DataEntity<MachineType> {
	
	private static final long serialVersionUID = 1L;
	private String machineTypeCode;		// 设备类别代码
	private String machineTypeName;		// 设备类别名称
	
	public MachineType() {
		super();
	}

	public MachineType(String id){
		super(id);
	}

	@Length(min=2, max=2, message="设备类别代码长度必须介于 2 和 2 之间")
	@ExcelField(title="设备类别代码", align=2, sort=1)
	public String getMachineTypeCode() {
		return machineTypeCode;
	}

	public void setMachineTypeCode(String machineTypeCode) {
		this.machineTypeCode = machineTypeCode;
	}
	
	@ExcelField(title="设备类别名称", align=2, sort=2)
	public String getMachineTypeName() {
		return machineTypeName;
	}

	public void setMachineTypeName(String machineTypeName) {
		this.machineTypeName = machineTypeName;
	}
	
}