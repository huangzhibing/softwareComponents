/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.basedata.mdmcalendar.entity;


import com.jeeplus.core.persistence.DataEntity;

import java.util.Date;

import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 企业日历定义Entity
 * @author 何志铭
 * @version 2018-04-02
 */
public class MdmCalendar extends DataEntity<MdmCalendar> {
	
	private static final long serialVersionUID = 1L;
	private String curDate;		// 日期
	private String weekDay;		// 星期
	private String workFlag;		// 工作日标志
	private String dayName;		// 节日名称
	private String workTime;		// 标准工时
	private String beginCurDate;		// 开始 日期(业务主键)
	private String endCurDate;		// 结束 日期(业务主键)
	
	public MdmCalendar() {
		super();
	}

	public MdmCalendar(String id){
		super(id);
	}

	@ExcelField(title="日期", align=2, sort=7)
	public String getCurDate() {
		return curDate;
	}

	public void setCurDate(String curDate) {
		this.curDate = curDate;
	}
	
	@ExcelField(title="星期", align=2, sort=8)
	public String getWeekDay() {
		return weekDay;
	}

	public void setWeekDay(String weekDay) {
		this.weekDay = weekDay;
	}
	
	@ExcelField(title="工作日标志", align=2, sort=9)
	public String getWorkFlag() {
		return workFlag;
	}

	public void setWorkFlag(String workFlag) {
		this.workFlag = workFlag;
	}
	
	@ExcelField(title="节日名称", align=2, sort=10)
	public String getDayName() {
		return dayName;
	}

	public void setDayName(String dayName) {
		this.dayName = dayName;
	}
	
	@ExcelField(title="标准工时", align=2, sort=11)
	public String getWorkTime() {
		return workTime;
	}

	public void setWorkTime(String workTime) {
		this.workTime = workTime;
	}
	
	public String getBeginCurDate() {
		return beginCurDate;
	}

	public void setBeginCurDate(String beginCurDate) {
		this.beginCurDate = beginCurDate;
	}
	
	public String getEndCurDate() {
		return endCurDate;
	}

	public void setEndCurDate(String endCurDate) {
		this.endCurDate = endCurDate;
	}
		
}