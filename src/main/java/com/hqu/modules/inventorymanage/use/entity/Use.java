/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.inventorymanage.use.entity;

import org.hibernate.validator.constraints.Length;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 用途定义Entity
 * @author lmy
 * @version 2018-04-14
 */
public class Use extends DataEntity<Use> {
	
	private static final long serialVersionUID = 1L;
	private String useId;		// 用途代码
	private String useDesc;		// 用途名称
	private String note;		// 备注
	
	public Use() {
		super();
	}

	public Use(String id){
		super(id);
	}

	@Length(min=4, max=4, message="用途代码长度必须介于 4 和 4 之间")
	@ExcelField(title="用途代码", align=2, sort=7)
	public String getUseId() {
		return useId;
	}

	public void setUseId(String useId) {
		this.useId = useId;
	}
	
	@ExcelField(title="用途名称", align=2, sort=8)
	public String getUseDesc() {
		return useDesc;
	}

	public void setUseDesc(String useDesc) {
		this.useDesc = useDesc;
	}
	
	@ExcelField(title="备注", align=2, sort=9)
	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}
	
}