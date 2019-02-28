/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.qualitymanage.mattertype.entity;


import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * MatterTypeEntity
 * @author 方翠虹
 * @version 2018-08-18
 */
public class MatterType extends DataEntity<MatterType> {
	
	private static final long serialVersionUID = 1L;
	private String mattercode;		// 问题类型编码
	private String mattername;		// 问题类型名称
	private String matterdes;		// 问题类型描述
	private String type;		// 缺陷描述类型
	
	public MatterType() {
		super();
	}

	public MatterType(String id){
		super(id);
	}

	@ExcelField(title="问题类型编码", align=2, sort=6)
	public String getMattercode() {
		return mattercode;
	}

	public void setMattercode(String mattercode) {
		this.mattercode = mattercode;
	}
	
	@ExcelField(title="问题类型名称", align=2, sort=7)
	public String getMattername() {
		return mattername;
	}

	public void setMattername(String mattername) {
		this.mattername = mattername;
	}
	
	@ExcelField(title="问题类型描述", align=2, sort=8)
	public String getMatterdes() {
		return matterdes;
	}

	public void setMatterdes(String matterdes) {
		this.matterdes = matterdes;
	}
	
	@ExcelField(title="缺陷描述类型", dictType="mtype", align=2, sort=10)
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
}