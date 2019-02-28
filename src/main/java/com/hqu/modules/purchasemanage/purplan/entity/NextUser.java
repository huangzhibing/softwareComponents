package com.hqu.modules.purchasemanage.purplan.entity;

import java.io.Serializable;

/*
 * 临时测试用，绑定数据到表单元素
 * 用于审核时设定【下一个审核人】对应的select列表
 */
public class NextUser implements Serializable {

	//审核角色名称
	private String name;
	//审核角色id
	private String deptId;
    //待审核单据id
	private String billId;
	//保存审核不通过意见
	private String note;

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}


	public String getBillId() {
		return billId;
	}

	public void setBillId(String billId) {
		this.billId = billId;
	}

	public NextUser(String name, String deptId) {
		super();
		this.name = name;
		this.deptId = deptId;
	}

	public NextUser() {
		super();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDeptId() {
		return deptId;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

}
