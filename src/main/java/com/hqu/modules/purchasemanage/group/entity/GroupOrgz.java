/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.purchasemanage.group.entity;

import com.jeeplus.modules.sys.entity.Office;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 部门Entity
 * @author 方翠虹,石豪迈,陶瑶
 * @version 2018-04-18
 */
public class GroupOrgz extends DataEntity<GroupOrgz> {
	
	private static final long serialVersionUID = 1L;
	private Group Group;		// 采购组代码 父类
	private Office office;		// 部门代码
	private String orgzname;		// 部门名称
	
	public GroupOrgz() {
		super();
		this.setIdType(IDTYPE_AUTO);
	}

	public GroupOrgz(String id){
		super(id);
	}

	public GroupOrgz(Group Group){
		this.Group = Group;
	}

	public Group getGroup() {
		return Group;
	}

	public void setGroup(Group Group) {
		this.Group = Group;
	}
	
	@ExcelField(title="部门代码", fieldType=Office.class, value="office.code", align=2, sort=8)
	public Office getOffice() {
		return office;
	}

	public void setOffice(Office office) {
		this.office = office;
	}
	
	@ExcelField(title="部门名称", align=2, sort=9)
	public String getOrgzname() {
		return orgzname;
	}

	public void setOrgzname(String orgzname) {
		this.orgzname = orgzname;
	}
	
}