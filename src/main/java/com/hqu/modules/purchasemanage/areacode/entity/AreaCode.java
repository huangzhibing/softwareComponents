/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.purchasemanage.areacode.entity;


import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 地区定义Entity
 * @author 石豪迈
 * @version 2018-04-25
 */
public class AreaCode extends DataEntity<AreaCode> {
	
	private static final long serialVersionUID = 1L;
	private String areacode;		// 地区编码
	private String areaname;		// 地区名称
	private String notes;		// 备注
	private String userDeptCode;		// 登录用户所在部门编码
	
	public AreaCode() {
		super();
	}

	public AreaCode(String id){
		super(id);
	}

	@ExcelField(title="地区编码", align=2, sort=7)
	public String getAreacode() {
		return areacode;
	}

	public void setAreacode(String areacode) {
		this.areacode = areacode;
	}
	
	@ExcelField(title="地区名称", align=2, sort=8)
	public String getAreaname() {
		return areaname;
	}

	public void setAreaname(String areaname) {
		this.areaname = areaname;
	}
	
	@ExcelField(title="备注", align=2, sort=9)
	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}
	
	@ExcelField(title="登录用户所在部门编码", align=2, sort=10)
	public String getUserDeptCode() {
		return userDeptCode;
	}

	public void setUserDeptCode(String userDeptCode) {
		this.userDeptCode = userDeptCode;
	}
	
}