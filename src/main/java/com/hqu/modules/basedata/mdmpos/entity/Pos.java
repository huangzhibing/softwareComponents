/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.basedata.mdmpos.entity;

import com.jeeplus.modules.sys.entity.Office;
import javax.validation.constraints.NotNull;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 岗位维护表Entity
 * @author luyumiao
 * @version 2018-04-11
 */
public class Pos extends DataEntity<Pos> {
	
	private static final long serialVersionUID = 1L;
	private String code;		// 岗位编码
	private String name;		// 岗位名称
	private Office orgzCode;		// 所属部门编号
	private String orgzName;		// 所属部门名称
	private String note;		// 岗位职能说明
	
	public Pos() {
		super();
	}

	public Pos(String id){
		super(id);
	}

	@ExcelField(title="岗位编码", align=2, sort=1)
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	@ExcelField(title="岗位名称", align=2, sort=2)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@NotNull(message="所属部门编号不能为空")
	@ExcelField(title="所属部门编号", fieldType=Office.class, value="orgzCode.code", align=2, sort=3)
	public Office getOrgzCode() {
		return orgzCode;
	}

	public void setOrgzCode(Office orgzCode) {
		this.orgzCode = orgzCode;
	}
	
	@ExcelField(title="所属部门名称", align=2, sort=4)
	public String getOrgzName() {
		return orgzName;
	}

	public void setOrgzName(String orgzName) {
		this.orgzName = orgzName;
	}
	
	@ExcelField(title="岗位职能说明", align=2, sort=5)
	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}
	
}