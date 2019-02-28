/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.basedata.team.entity;

import com.jeeplus.modules.sys.entity.Office;
import java.util.List;
import com.google.common.collect.Lists;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 班组维护Entity
 * @author xn
 * @version 2018-04-16
 */
public class Team extends DataEntity<Team> {
	
	private static final long serialVersionUID = 1L;
	private String teamCode;		// 班组编码
	private String teamName;		// 班组名称
	private Office deptCode;		// 所属部门编码
	private String deptName;		// 所属部门名称
	private List<TeamPerson> teamPersonList = Lists.newArrayList();		// 子表列表
	
	public Team() {
		super();
	}

	public Team(String id){
		super(id);
	}

	@ExcelField(title="班组编码", align=2, sort=7)
	public String getTeamCode() {
		return teamCode;
	}

	public void setTeamCode(String teamCode) {
		this.teamCode = teamCode;
	}
	
	@ExcelField(title="班组名称", align=2, sort=8)
	public String getTeamName() {
		return teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}
	
	@ExcelField(title="所属部门编码", fieldType=Office.class, value="deptCode.code", align=2, sort=9)
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
	
	public List<TeamPerson> getTeamPersonList() {
		return teamPersonList;
	}

	public void setTeamPersonList(List<TeamPerson> teamPersonList) {
		this.teamPersonList = teamPersonList;
	}
}