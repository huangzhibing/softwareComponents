/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.purchasemanage.group.entity;

import java.util.List;
import com.google.common.collect.Lists;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * GroupEntity
 * @author 方翠虹,石豪迈,陶瑶
 * @version 2018-04-18
 */
public class Group extends DataEntity<Group> {
	
	private static final long serialVersionUID = 1L;
	private String groupid;		// 采购组代码
	private String groupname;		// 采购组名称
	private List<GroupBuyer> groupBuyerList = Lists.newArrayList();		// 子表列表
	private List<GroupItemClass> groupItemClassList = Lists.newArrayList();		// 子表列表
	private List<GroupOrgz> groupOrgzList = Lists.newArrayList();		// 子表列表
	private List<GroupSup> groupSupList = Lists.newArrayList();		// 子表列表
	private List<GroupWare> groupWareList = Lists.newArrayList();		// 子表列表
	
	public Group() {
		super();
	}

	public Group(String id){
		super(id);
	}

	@ExcelField(title="采购组代码", align=2, sort=7)
	public String getGroupid() {
		return groupid;
	}

	public void setGroupid(String groupid) {
		this.groupid = groupid;
	}
	
	@ExcelField(title="采购组名称", align=2, sort=8)
	public String getGroupname() {
		return groupname;
	}

	public void setGroupname(String groupname) {
		this.groupname = groupname;
	}
	
	public List<GroupBuyer> getGroupBuyerList() {
		return groupBuyerList;
	}

	public void setGroupBuyerList(List<GroupBuyer> groupBuyerList) {
		this.groupBuyerList = groupBuyerList;
	}
	public List<GroupItemClass> getGroupItemClassList() {
		return groupItemClassList;
	}

	public void setGroupItemClassList(List<GroupItemClass> groupItemClassList) {
		this.groupItemClassList = groupItemClassList;
	}
	public List<GroupOrgz> getGroupOrgzList() {
		return groupOrgzList;
	}

	public void setGroupOrgzList(List<GroupOrgz> groupOrgzList) {
		this.groupOrgzList = groupOrgzList;
	}
	public List<GroupSup> getGroupSupList() {
		return groupSupList;
	}

	public void setGroupSupList(List<GroupSup> groupSupList) {
		this.groupSupList = groupSupList;
	}
	public List<GroupWare> getGroupWareList() {
		return groupWareList;
	}

	public void setGroupWareList(List<GroupWare> groupWareList) {
		this.groupWareList = groupWareList;
	}
}