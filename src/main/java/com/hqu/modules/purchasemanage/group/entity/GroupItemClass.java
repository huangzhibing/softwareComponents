/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.purchasemanage.group.entity;

import com.hqu.modules.basedata.item.entity.Item;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 物料类Entity
 * @author 方翠虹,石豪迈,陶瑶
 * @version 2018-04-18
 */
public class GroupItemClass extends DataEntity<GroupItemClass> {
	
	private static final long serialVersionUID = 1L;
	private Group Group;		// 采购组代码 父类
	private Item item;		// 物料类代码
	private String itemclassname;		// 物料类名称
	
	public GroupItemClass() {
		super();
		this.setIdType(IDTYPE_AUTO);
	}

	public GroupItemClass(String id){
		super(id);
	}

	public GroupItemClass(Group Group){
		this.Group = Group;
	}

	public Group getGroup() {
		return Group;
	}

	public void setGroup(Group Group) {
		this.Group = Group;
	}
	
	@ExcelField(title="物料类代码", fieldType=Item.class, value="item.code", align=2, sort=8)
	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}
	
	@ExcelField(title="物料类名称", align=2, sort=9)
	public String getItemclassname() {
		return itemclassname;
	}

	public void setItemclassname(String itemclassname) {
		this.itemclassname = itemclassname;
	}
	
}