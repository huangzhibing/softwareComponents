/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.basedata.worktype.entity;


import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 工种定义Entity
 * @author zb
 * @version 2018-04-07
 */
public class WorkType extends DataEntity<WorkType> {
	
	private static final long serialVersionUID = 1L;
	private String workTypeCode;		// 工种代码
	private String workTypeName;		// 工种名称
	private String workTypeDesc;		// 工种名称说明
	
	public WorkType() {
		super();
	}

	public WorkType(String id){
		super(id);
	}

	@ExcelField(title="工种代码", align=2, sort=7)
	public String getWorkTypeCode() {
		return workTypeCode;
	}

	public void setWorkTypeCode(String workTypeCode) {
		this.workTypeCode = workTypeCode;
	}
	
	@ExcelField(title="工种名称", align=2, sort=8)
	public String getWorkTypeName() {
		return workTypeName;
	}

	public void setWorkTypeName(String workTypeName) {
		this.workTypeName = workTypeName;
	}
	
	@ExcelField(title="工种名称说明", align=2, sort=9)
	public String getWorkTypeDesc() {
		return workTypeDesc;
	}

	public void setWorkTypeDesc(String workTypeDesc) {
		this.workTypeDesc = workTypeDesc;
	}
	
}