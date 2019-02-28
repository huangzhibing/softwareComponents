/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.basedata.calendar.entity;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import javax.validation.constraints.NotNull;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 企业日历定义Entity
 * @author 何志铭
 * @version 2018-04-04
 */
public class Calendar extends DataEntity<Calendar> {
	
	private static final long serialVersionUID = 1L;
	private Date curDate;		// 日期
	private String weekDay;		// 星期
	private String workFlag;		// 工作日标志
	private String dayName;		// 节日名称
	private String workTime;		// 标准工时
	private Date beginCurDate;		// 开始 日期
	private Date endCurDate;		// 结束 日期
	
	public Calendar() {
		super();
	}

	public Calendar(String id){
		super(id);
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@NotNull(message="日期不能为空")
	@ExcelField(title="日期", align=2, sort=1)
	public Date getCurDate() {
		return curDate;
	}

	public void setCurDate(Date curDate) {
		this.curDate = curDate;
	}
	
	@ExcelField(title="星期", align=2, sort=2)
	public String getWeekDay() {
		return weekDay;
	}

	public void setWeekDay(String weekDay) {
		this.weekDay = weekDay;
	}
	
	@ExcelField(title="工作日标志", dictType="", align=2, sort=3)
	public String getWorkFlag() {
		return workFlag;
	}

	public void setWorkFlag(String workFlag) {
		this.workFlag = workFlag;
	}
	
	@ExcelField(title="节日名称", align=2, sort=4)
	public String getDayName() {
		return dayName;
	}

	public void setDayName(String dayName) {
		this.dayName = dayName;
	}
	
	@ExcelField(title="标准工时", align=2, sort=5)
	public String getWorkTime() {
		return workTime;
	}

	public void setWorkTime(String workTime) {
		this.workTime = workTime;
	}
	
	public Date getBeginCurDate() {
		return beginCurDate;
	}

	public void setBeginCurDate(Date beginCurDate) {
		this.beginCurDate = beginCurDate;
	}
	
	public Date getEndCurDate() {
		return endCurDate;
	}

	public void setEndCurDate(Date endCurDate) {
		this.endCurDate = endCurDate;
	}
		
}