/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.basedata.itemclass.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import javax.validation.constraints.NotNull;
import java.util.List;
import com.google.common.collect.Lists;

import com.jeeplus.core.persistence.TreeEntity;

/**
 * 物料类定义Entity
 * 
 * @author dongqida
 * @version 2018-04-02
 */
public class ItemClass extends TreeEntity<ItemClass> {

	private static final long serialVersionUID = 1L;

	private String classId;

	private List<ItemClassF> itemClassFList = Lists.newArrayList(); // 子表列表

	public String getClassId() {
		return classId;
	}

	public void setClassId(String classId) {
		this.classId = classId;
	}

	public ItemClass() {
		super();
	}

	public ItemClass(String id) {
		super(id);
	}

	public ItemClass getParent() {
		return parent;
	}

	@Override
	public void setParent(ItemClass parent) {
		this.parent = parent;

	}

	public List<ItemClassF> getItemClassFList() {
		return itemClassFList;
	}

	public void setItemClassFList(List<ItemClassF> itemClassFList) {
		this.itemClassFList = itemClassFList;
	}

	public String getParentId() {
		return parent != null && parent.getId() != null ? parent.getId() : "0";
	}
}