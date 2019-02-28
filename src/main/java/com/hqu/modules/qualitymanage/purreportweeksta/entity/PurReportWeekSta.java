/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.qualitymanage.purreportweeksta.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

import java.util.Date;

/**
 * 进料检验统计Entity
 * @author yxb
 * @version 2018-08-25
 */
public class PurReportWeekSta extends DataEntity<PurReportWeekSta> {
	
	private static final long serialVersionUID = 1L;
	private String year;		// 年份
	private String month;		// 月份
	private String week;		// 周次
	private String sum;		// 检验批次
	private String qualifiedNum;		// 合格批次
	private String failNum;		// 不合格批次
	private String otherNum;		// 其他
	private String specialNum;		// 特采批次
	private String specialRate;		// 特采率
	private String qualifiedRate;		// 进料合格率


	private Date dateQuery; //前台要查询的月份 临时变量
	private Date begindate;
	private Date enddate;
	
	public PurReportWeekSta() {
		super();
	}
	public PurReportWeekSta(String a,String b,String c,String d,String e,String f,String g) {
		super();
		sum=a;
		qualifiedNum=b;
		failNum=c;
		specialNum=d;
		otherNum=e;
		qualifiedRate=f;
		specialRate=g;
	}

	public PurReportWeekSta(String id){
		super(id);
	}

	@JsonFormat(pattern = "yyyy-MM-dd")
	public Date getBegindate() {
		return begindate;
	}

	public void setBegindate(Date begindate) {
		this.begindate = begindate;
	}

	@JsonFormat(pattern = "yyyy-MM-dd")
	public Date getEnddate() {
		return enddate;
	}

	public void setEnddate(Date enddate) {
		this.enddate = enddate;
	}

	@JsonFormat(pattern = "yyyy-MM")
	public Date getDateQuery() {
		return dateQuery;
	}

	public void setDateQuery(Date dateQuery) {
		this.dateQuery = dateQuery;
	}

	@ExcelField(title="年份", align=2, sort=7)
	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}
	
	@ExcelField(title="月份", align=2, sort=8)
	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}
	
	@ExcelField(title="周次", align=2, sort=9)
	public String getWeek() {
		return week;
	}

	public void setWeek(String week) {
		this.week = week;
	}
	
	@ExcelField(title="检验批次", align=2, sort=10)
	public String getSum() {
		return sum;
	}

	public void setSum(String sum) {
		this.sum = sum;
	}
	
	@ExcelField(title="合格批次", align=2, sort=11)
	public String getQualifiedNum() {
		return qualifiedNum;
	}

	public void setQualifiedNum(String qualifiedNum) {
		this.qualifiedNum = qualifiedNum;
	}
	
	@ExcelField(title="不合格批次", align=2, sort=12)
	public String getFailNum() {
		return failNum;
	}

	public void setFailNum(String failNum) {
		this.failNum = failNum;
	}
	
	@ExcelField(title="其他", align=2, sort=13)
	public String getOtherNum() {
		return otherNum;
	}

	public void setOtherNum(String otherNum) {
		this.otherNum = otherNum;
	}
	
	@ExcelField(title="特采批次", align=2, sort=14)
	public String getSpecialNum() {
		return specialNum;
	}

	public void setSpecialNum(String specialNum) {
		this.specialNum = specialNum;
	}
	
	@ExcelField(title="特采率", align=2, sort=15)
	public String getSpecialRate() {
		return specialRate;
	}

	public void setSpecialRate(String specialRate) {
		this.specialRate = specialRate;
	}
	
	@ExcelField(title="进料合格率", align=2, sort=16)
	public String getQualifiedRate() {
		return qualifiedRate;
	}

	public void setQualifiedRate(String qualifiedRate) {
		this.qualifiedRate = qualifiedRate;
	}
	
}