/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.purchasemanage.group.entity;

import com.hqu.modules.inventorymanage.warehouse.entity.WareHouse;
import javax.validation.constraints.NotNull;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 仓库Entity
 * @author 方翠虹,石豪迈,陶瑶
 * @version 2018-04-18
 */
public class GroupWare extends DataEntity<GroupWare> {
	
	private static final long serialVersionUID = 1L;
	private Group Group;		// 采购组代码 父类
	private String groupid; //采购组id
	private WareHouse warehouse;		// 仓库代码
	private String warename;		// 仓库名称
	
	public GroupWare() {
		super();
		this.setIdType(IDTYPE_AUTO);
	}

	public GroupWare(String id){
		super(id);
	}

	public GroupWare(Group Group){
		this.Group = Group;
	}

	public Group getGroup() {
		return Group;
	}

	public void setGroup(Group Group) {
		this.Group = Group;
	}
	
	@NotNull(message="仓库代码不能为空")
	@ExcelField(title="仓库代码", fieldType=WareHouse.class, value="warehouse.wareId", align=2, sort=8)
	public WareHouse getWarehouse() {
		return warehouse;
	}

	public void setWarehouse(WareHouse warehouse) {
		this.warehouse = warehouse;
	}
	
	@ExcelField(title="仓库名称", align=2, sort=9)
	public String getWarename() {
		return warename;
	}

	public void setWarename(String warename) {
		this.warename = warename;
	}

	public String getGroupid() {
		return groupid;
	}

	public void setGroupid(String groupid) {
		this.groupid = groupid;
	}
	
}