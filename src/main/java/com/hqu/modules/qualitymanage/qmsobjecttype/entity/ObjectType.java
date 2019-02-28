/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.qualitymanage.qmsobjecttype.entity;


import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 检验对象类型Entity
 * @author tyo
 * @version 2018-04-26
 */
public class ObjectType extends DataEntity<ObjectType> {
	
	private static final long serialVersionUID = 1L;
	private String objtcode;		// 检验对象类型编码
	private String objtname;		// 检验对象类型名称
	private String objtdes;		// 检验对象类型描述
	
	public ObjectType() {
		super();
	}

	public ObjectType(String id){
		super(id);
	}

	@ExcelField(title="检验对象类型编码", align=2, sort=7)
	public String getObjtcode() {
		return objtcode;
	}

	public void setObjtcode(String objtcode) {
		this.objtcode = objtcode;
	}
	
	@ExcelField(title="检验对象类型名称", align=2, sort=8)
	public String getObjtname() {
		return objtname;
	}

	public void setObjtname(String objtname) {
		this.objtname = objtname;
	}
	
	@ExcelField(title="检验对象类型描述", align=2, sort=9)
	public String getObjtdes() {
		return objtdes;
	}

	public void setObjtdes(String objtdes) {
		this.objtdes = objtdes;
	}
	
}