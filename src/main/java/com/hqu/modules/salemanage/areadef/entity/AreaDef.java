/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.salemanage.areadef.entity;

import org.hibernate.validator.constraints.Length;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 销售地区定义Entity
 * @author 黄志兵
 * @version 2018-05-05
 */
public class AreaDef extends DataEntity<AreaDef> {
	
	private static final long serialVersionUID = 1L;
	private String areaCode;		// 地区编码
	private String areaName;		// 地区名称
	private String notes;		// 备注
	
	public AreaDef() {
		super();
	}

	public AreaDef(String id){
		super(id);
	}

	@Length(min=2, max=2, message="地区编码长度必须介于 2 和 2 之间")
	@ExcelField(title="地区编码", align=2, sort=1)
	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}
	
	@ExcelField(title="地区名称", align=2, sort=2)
	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}
	
	@ExcelField(title="备注", align=2, sort=3)
	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}
	
}