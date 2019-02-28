/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.qualitymanage.matterhandling.entity;


import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * MatterHandlingEntity
 * @author 方翠虹
 * @version 2018-04-25
 */
public class MatterHandling extends DataEntity<MatterHandling> {
	
	private static final long serialVersionUID = 1L;
	private String mhandlingcode;		// 问题处理编码
	private String mhandlingname;		// 问题处理名称
	private String mhandlingdes;		// 问题处理描述
	
	public MatterHandling() {
		super();
	}

	public MatterHandling(String id){
		super(id);
	}

	@ExcelField(title="问题处理编码", align=2, sort=7)
	public String getMhandlingcode() {
		return mhandlingcode;
	}

	public void setMhandlingcode(String mhandlingcode) {
		this.mhandlingcode = mhandlingcode;
	}
	
	@ExcelField(title="问题处理名称", align=2, sort=8)
	public String getMhandlingname() {
		return mhandlingname;
	}

	public void setMhandlingname(String mhandlingname) {
		this.mhandlingname = mhandlingname;
	}
	
	@ExcelField(title="问题处理描述", align=2, sort=9)
	public String getMhandlingdes() {
		return mhandlingdes;
	}

	public void setMhandlingdes(String mhandlingdes) {
		this.mhandlingdes = mhandlingdes;
	}
	
}