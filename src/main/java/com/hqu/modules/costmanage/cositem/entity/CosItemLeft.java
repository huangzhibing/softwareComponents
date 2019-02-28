/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.costmanage.cositem.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import javax.validation.constraints.NotNull;
import java.util.List;
import com.google.common.collect.Lists;

import com.jeeplus.core.persistence.TreeEntity;

/**
 * 科目定义Entity
 * @author zz
 * @version 2018-09-05
 */
public class CosItemLeft extends TreeEntity<CosItemLeft> {
	
	private static final long serialVersionUID = 1L;
	private String itemCode;		// 科目编号
	
	private List<CosItem> cosItemList = Lists.newArrayList();		// 子表列表
	
	public CosItemLeft() {
		super();
	}

	public CosItemLeft(String id){
		super(id);
	}

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}
	
	public  CosItemLeft getParent() {
			return parent;
	}
	
	@Override
	public void setParent(CosItemLeft parent) {
		this.parent = parent;
		
	}
	
	public List<CosItem> getCosItemList() {
		return cosItemList;
	}

	public void setCosItemList(List<CosItem> cosItemList) {
		this.cosItemList = cosItemList;
	}
	public String getParentId() {
		return parent != null && parent.getId() != null ? parent.getId() : "0";
	}
}