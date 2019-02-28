/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.purchasemanage.group.entity;

import com.hqu.modules.basedata.account.entity.Account;
import com.hqu.modules.purchasemanage.suptype.entity.SupType;
import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 供应商Entity
 * @author 方翠虹,石豪迈,陶瑶
 * @version 2018-04-18
 */
public class GroupSup extends DataEntity<GroupSup> {
	
	private static final long serialVersionUID = 1L;
	private Group Group;		// 采购组代码 父类
	private SupType supid;		// 供应商代码
	Account account;     //供应商代码
	private String supname;		// 供应商名称


	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public GroupSup() {
		super();
		this.setIdType(IDTYPE_AUTO);
	}

	public GroupSup(String id){
		super(id);
	}

	public GroupSup(Group Group){
		this.Group = Group;
	}

	public Group getGroup() {
		return Group;
	}

	public void setGroup(Group Group) {
		this.Group = Group;
	}

	//@NotNull(message="供应商代码不能为空")
	@ExcelField(title="供应商代码", fieldType=SupType.class, value="supid.suptypeId", align=2, sort=8)
	public SupType getSupid() {
		return supid;
	}

	public void setSupid(SupType supid) {
		this.supid = supid;
	}

	@ExcelField(title="供应商名称", align=2, sort=9)
	public String getSupname() {
		return supname;
	}

	public void setSupname(String supname) {
		this.supname = supname;
	}
	
}