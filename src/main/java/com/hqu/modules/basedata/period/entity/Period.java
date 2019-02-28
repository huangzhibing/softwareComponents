/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.basedata.period.entity;

import org.hibernate.validator.constraints.Length;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import javax.validation.constraints.NotNull;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 企业核算期定义Entity
 * @author Neil
 * @version 2018-06-02
 */
public class Period extends DataEntity<Period> {
	
	private static final long serialVersionUID = 1L;
	private String periodId;		// 核算期编码
	private Date periodBegin;		// 核算期开始日期
	private Date periodEnd;		// 核算期结束日期
	private String beginHour;		// 起始时间
	private String endHour;		// 截止时间
	private String year;		// 年份
	private Date currentDate;	//当前日期
	
	private String closeFlag;  //结账标志
	
	public Period() {
		super();
	}

	public Period(String id){
		super(id);
	}

	@Length(min=6, max=6, message="核算期编码长度必须介于 6 和 6 之间")
	@ExcelField(title="核算期编码", align=2, sort=7)
	public String getPeriodId() {
		return periodId;
	}

	public void setPeriodId(String periodId) {
		this.periodId = periodId;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd")
	@NotNull(message="核算期开始日期不能为空")
	@ExcelField(title="核算期开始日期", align=2, sort=8)
	public Date getPeriodBegin() {
		return periodBegin;
	}

	public void setPeriodBegin(Date periodBegin) {
		this.periodBegin = periodBegin;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd")
	@NotNull(message="核算期结束日期不能为空")
	@ExcelField(title="核算期结束日期", align=2, sort=9)
	public Date getPeriodEnd() {
		return periodEnd;
	}

	public void setPeriodEnd(Date periodEnd) {
		this.periodEnd = periodEnd;
	}
	
	@ExcelField(title="起始时间", align=2, sort=10)
	public String getBeginHour() {
		return beginHour;
	}

	public void setBeginHour(String beginHour) {
		this.beginHour = beginHour;
	}
	
	@ExcelField(title="截止时间", align=2, sort=11)
	public String getEndHour() {
		return endHour;
	}

	public void setEndHour(String endHour) {
		this.endHour = endHour;
	}
	
	@ExcelField(title="年份", align=2, sort=12)
	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public Date getCurrentDate() {
		return currentDate;
	}

	public void setCurrentDate(Date currentDate) {
		this.currentDate = currentDate;
	}

	public String getCloseFlag() {
		return closeFlag;
	}

	public void setCloseFlag(String closeFlag) {
		this.closeFlag = closeFlag;
	}
	
}