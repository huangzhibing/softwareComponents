/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.purchasemanage.relations.entity;


import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 关系描述Entity
 * @author syc
 * @version 2018-04-30
 */
public class Relations extends DataEntity<Relations> {
	
	private static final long serialVersionUID = 1L;
	private String frontNum;		// 被生成单据号
	private Integer frontId;		// 被生成序号
	private String afterNum;		// 生成单据号
	private Integer afterId;		// 生成序号
	private String type;		// 关系类型
	private String state;		// state
	
	public Relations() {
		super();
	}

	public Relations(String id){
		super(id);
	}

	@ExcelField(title="被生成单据号", align=2, sort=7)
	public String getFrontNum() {
		return frontNum;
	}

	public void setFrontNum(String frontNum) {
		this.frontNum = frontNum;
	}
	
	@ExcelField(title="被生成序号", align=2, sort=8)
	public Integer getFrontId() {
		return frontId;
	}

	public void setFrontId(Integer frontId) {
		this.frontId = frontId;
	}
	
	@ExcelField(title="生成单据号", align=2, sort=9)
	public String getAfterNum() {
		return afterNum;
	}

	public void setAfterNum(String afterNum) {
		this.afterNum = afterNum;
	}
	
	@ExcelField(title="生成序号", align=2, sort=10)
	public Integer getAfterId() {
		return afterId;
	}

	public void setAfterId(Integer afterId) {
		this.afterId = afterId;
	}
	
	@ExcelField(title="关系类型", align=2, sort=11)
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	@ExcelField(title="state", align=2, sort=12)
	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
	
}