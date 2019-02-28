/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.basedata.personwork.entity;

import com.jeeplus.modules.sys.entity.User;
import com.hqu.modules.basedata.worktype.entity.WorkType;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 人员工种表Entity
 * @author liujiachen
 * @version 2018-06-04
 */
public class PersonWork extends DataEntity<PersonWork> {
	
	private static final long serialVersionUID = 1L;
	private User user;		// 人员编码
	private String personWorkName;
	private WorkType workTypeCode;		// 工种编码
	private String workTypeName;		// 工种名称
	
	public PersonWork() {
		super();
	}

	public PersonWork(String id){
		super(id);
	}

	@ExcelField(title="人员编码", fieldType=User.class, value="user.name", align=2, sort=7)
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@ExcelField(title="人员名称")
	public String getPersonWorkName(){return personWorkName;}

	public void setPersonWorkName(String personWorkName) {
		this.personWorkName = personWorkName;
	}

	@ExcelField(title="工种编码", fieldType=WorkType.class, value="workType.workTypeName", align=2, sort=8)
	public WorkType getWorkTypeCode() {
		return workTypeCode;
	}

	public void setWorkTypeCode(WorkType workTypeCode) {
		this.workTypeCode = workTypeCode;
	}

	@ExcelField(title="工种名称", align=2, sort=9)
	public String getWorkTypeName() {
		return workTypeName;
	}

	public void setWorkTypeName(String workTypeName) {
		this.workTypeName = workTypeName;
	}

}