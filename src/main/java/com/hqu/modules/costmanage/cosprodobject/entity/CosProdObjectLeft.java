/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.costmanage.cosprodobject.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import javax.validation.constraints.NotNull;
import java.util.List;
import com.google.common.collect.Lists;

import com.jeeplus.core.persistence.TreeEntity;

/**
 * 核算对象定义Entity
 * @author zz
 * @version 2018-09-07
 */
public class CosProdObjectLeft extends TreeEntity<CosProdObjectLeft> {
	
	private static final long serialVersionUID = 1L;
	private String prodId;		// 核算对象编码
	
	private List<CosProdObject> cosProdObjectList = Lists.newArrayList();		// 子表列表
	
	public CosProdObjectLeft() {
		super();
	}

	public CosProdObjectLeft(String id){
		super(id);
	}

	public String getProdId() {
		return prodId;
	}

	public void setProdId(String prodId) {
		this.prodId = prodId;
	}
	
	public  CosProdObjectLeft getParent() {
			return parent;
	}
	
	@Override
	public void setParent(CosProdObjectLeft parent) {
		this.parent = parent;
		
	}
	
	public List<CosProdObject> getCosProdObjectList() {
		return cosProdObjectList;
	}

	public void setCosProdObjectList(List<CosProdObject> cosProdObjectList) {
		this.cosProdObjectList = cosProdObjectList;
	}
	public String getParentId() {
		return parent != null && parent.getId() != null ? parent.getId() : "0";
	}
}