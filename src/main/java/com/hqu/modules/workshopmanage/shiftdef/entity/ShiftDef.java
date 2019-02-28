/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.workshopmanage.shiftdef.entity;


import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 班次定义Entity
 * @author zhangxin
 * @version 2018-05-25
 */
public class ShiftDef extends DataEntity<ShiftDef> {
	
	private static final long serialVersionUID = 1L;
	private String shiftname;		// 班次名称
	private String begintime;		// 班次开始时间
	private String endtime;		// 班次结束时间
	
	public ShiftDef() {
		super();
	}

	public ShiftDef(String id){
		super(id);
	}

	@ExcelField(title="班次名称", align=2, sort=7)
	public String getShiftname() {
		return shiftname;
	}

	public void setShiftname(String shiftname) {
		this.shiftname = shiftname;
	}
	
	@ExcelField(title="班次开始时间", align=2, sort=8)
	public String getBegintime() {
		return begintime;
	}

	public void setBegintime(String begintime) {
		this.begintime = begintime;
	}
	
	@ExcelField(title="班次结束时间", align=2, sort=9)
	public String getEndtime() {
		return endtime;
	}

	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}
	
}