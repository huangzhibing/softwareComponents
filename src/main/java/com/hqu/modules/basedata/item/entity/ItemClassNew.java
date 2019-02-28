/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.basedata.item.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import javax.validation.constraints.NotNull;
import java.util.List;
import com.google.common.collect.Lists;

import com.jeeplus.core.persistence.TreeEntity;

/**
 * 物料定义Entity
 * @author xn
 * @version 2018-04-06
 */
public class ItemClassNew extends TreeEntity<ItemClassNew> {
	
	private static final long serialVersionUID = 1L;
	private String classId;		// 物料类编码
	
	private List<Item> itemList = Lists.newArrayList();		// 子表列表
	
	public ItemClassNew() {
		super();
	}

	public ItemClassNew(String id){
		super(id);
	}

	public String getClassId() {
		return classId;
	}

	public void setClassId(String classId) {
		this.classId = classId;
	}
	
	public  ItemClassNew getParent() {
			return parent;
	}
	
	@Override
	public void setParent(ItemClassNew parent) {
		this.parent = parent;
		
	}
	
	public List<Item> getItemList() {
		return itemList;
	}

	public void setItemList(List<Item> itemList) {
		this.itemList = itemList;
	}
	public String getParentId() {
		return parent != null && parent.getId() != null ? parent.getId() : "0";
	}
}