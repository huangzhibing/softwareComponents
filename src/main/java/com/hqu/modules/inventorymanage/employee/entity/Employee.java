/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.inventorymanage.employee.entity;

import com.jeeplus.modules.sys.entity.User;
import javax.validation.constraints.NotNull;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 仓库人员定义Entity
 * @author liujiachen
 * @version 2018-04-17
 */
public class Employee extends DataEntity<Employee> {
	
	private static final long serialVersionUID = 1L;
	private User user;		// 人员编码
	private String empName;		// 人员姓名
	private String note;		// 备注
	
	public Employee() {
		super();
	}

	public Employee(String id){
		super(id);
	}

	@NotNull(message="人员编码不能为空")
	@ExcelField(title="人员编码", fieldType=User.class, value="user.name", align=2, sort=6)
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	@ExcelField(title="人员姓名", align=2, sort=7)
	public String getEmpName() {
		return empName;
	}

	public void setEmpName(String empName) {
		this.empName = empName;
	}
	
	@ExcelField(title="备注", align=2, sort=8)
	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}
	
}