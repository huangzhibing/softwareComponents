/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.basedata.team.entity;


import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 班组人员关系表Entity
 * @author xn
 * @version 2018-04-16
 */
public class TeamPerson extends DataEntity<TeamPerson> {
	
	private static final long serialVersionUID = 1L;
	private Team teamCode;		// 班组编码 父类
	private String personCode;		// 人员编码
	private String personName;		// 人员名称
	private String workTypeCode;		// 工种编码
	private String workTypeName;		// 工种名称
	
	public TeamPerson() {
		super();
	}

	public TeamPerson(String id){
		super(id);
	}

	public TeamPerson(Team teamCode){
		this.teamCode = teamCode;
	}

	public Team getTeamCode() {
		return teamCode;
	}

	public void setTeamCode(Team teamCode) {
		this.teamCode = teamCode;
	}
	
	@ExcelField(title="人员编码", align=2, sort=8)
	public String getPersonCode() {
		return personCode;
	}

	public void setPersonCode(String personCode) {
		this.personCode = personCode;
	}
	
	@ExcelField(title="人员名称", align=2, sort=9)
	public String getPersonName() {
		return personName;
	}

	public void setPersonName(String personName) {
		this.personName = personName;
	}
	
	@ExcelField(title="工种编码", align=2, sort=10)
	public String getWorkTypeCode() {
		return workTypeCode;
	}

	public void setWorkTypeCode(String workTypeCode) {
		this.workTypeCode = workTypeCode;
	}
	
	@ExcelField(title="工种名称", align=2, sort=11)
	public String getWorkTypeName() {
		return workTypeName;
	}

	public void setWorkTypeName(String workTypeName) {
		this.workTypeName = workTypeName;
	}
	
}