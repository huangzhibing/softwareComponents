/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.qualitymanage.purreporttypesta.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

import java.util.Date;

/**
 * 来料按物料类别统计分析Entity
 * @author yxb
 * @version 2018-08-25
 */
public class PurReportTypeSta extends DataEntity<PurReportTypeSta> {
	
	private static final long serialVersionUID = 1L;
	private String year;		// 年份
	private String month;		// 月份
	private String type;		// 物料类别
	private Integer sum;		// 检验批次
	private Integer qualifiedNum;		// 合格批次
	private Integer failNum;		// 不合格批次
	private Integer specialNum;		// 特采批次
	private Double qualifiedRate;		// 来料合格率

	private Date dateQuery; //前台要查询的月份 临时变量
	
	public PurReportTypeSta() {
		super();
	}

	public PurReportTypeSta(String id){
		super(id);
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
	
	@ExcelField(title="物料类别", align=2, sort=9)
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	@ExcelField(title="检验批次", align=2, sort=10)
	public Integer getSum() {
		return sum;
	}

	public void setSum(Integer sum) {
		this.sum = sum;
	}
	
	@ExcelField(title="合格批次", align=2, sort=11)
	public Integer getQualifiedNum() {
		return qualifiedNum;
	}

	public void setQualifiedNum(Integer qualifiedNum) {
		this.qualifiedNum = qualifiedNum;
	}
	
	@ExcelField(title="不合格批次", align=2, sort=12)
	public Integer getFailNum() {
		return failNum;
	}

	public void setFailNum(Integer failNum) {
		this.failNum = failNum;
	}
	
	@ExcelField(title="特采批次", align=2, sort=13)
	public Integer getSpecialNum() {
		return specialNum;
	}

	public void setSpecialNum(Integer specialNum) {
		this.specialNum = specialNum;
	}
	
	@ExcelField(title="来料合格率", align=2, sort=14)
	public Double getQualifiedRate() {
		return qualifiedRate;
	}

	public void setQualifiedRate(Double qualifiedRate) {
		this.qualifiedRate = qualifiedRate;
	}
	
}