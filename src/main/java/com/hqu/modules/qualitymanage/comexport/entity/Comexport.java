/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.qualitymanage.comexport.entity;


import java.util.Date;

import com.jeeplus.common.utils.excel.annotation.ExcelField;
import com.jeeplus.core.persistence.DataEntity;

/**
 * 功能老化激活安规导出Entity
 * @author syc
 * @version 2018-11-09
 */
public class Comexport extends DataEntity<Comexport> {
	
	private static final long serialVersionUID = 1L;
	private String itemname;		// 测试项目
	private String detail;		// 明细
	private String centercode;		// 机台序列码
	private String checktime;		// 测试时间
	private String checkperson;		// 测试人
	private String goodnum;		// 良品
	private String badnum;		// 不良品
	private String rate;		// 良率
	private String baddetail;		// 不良现象明细
	private Date beginDate; //检索开始时间
	private Date endDate; //检索结束时间
	private String isGood;  //判断是否合格
	
	
	
	public Comexport(String itemname, String detail, String centercode, String checktime, String checkperson,
			String goodnum, String badnum, String rate, String baddetail) {
		super();
		this.itemname = itemname;
		this.detail = detail;
		this.centercode = centercode;
		this.checktime = checktime;
		this.checkperson = checkperson;
		this.goodnum = goodnum;
		this.badnum = badnum;
		this.rate = rate;
		this.baddetail = baddetail;
	}

	public Comexport() {
		super();
	}

	public Comexport(String id){
		super(id);
	}

	@ExcelField(title="测试项目", align=2, sort=7)
	public String getItemname() {
		return itemname;
	}

	public void setItemname(String itemname) {
		this.itemname = itemname;
	}
	
	
	public Date getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	@ExcelField(title="明细", align=2, sort=8)
	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}
	
	@ExcelField(title="机台序列码", align=2, sort=9)
	public String getCentercode() {
		return centercode;
	}

	public void setCentercode(String centercode) {
		this.centercode = centercode;
	}
	
	@ExcelField(title="测试时间", align=2, sort=10)
	public String getChecktime() {
		return checktime;
	}

	public void setChecktime(String checktime) {
		this.checktime = checktime;
	}
	
	@ExcelField(title="测试人", align=2, sort=11)
	public String getCheckperson() {
		return checkperson;
	}

	public void setCheckperson(String checkperson) {
		this.checkperson = checkperson;
	}
	
	@ExcelField(title="良品", align=2, sort=12)
	public String getGoodnum() {
		return goodnum;
	}

	public void setGoodnum(String goodnum) {
		this.goodnum = goodnum;
	}
	
	@ExcelField(title="不良品", align=2, sort=13)
	public String getBadnum() {
		return badnum;
	}

	public void setBadnum(String badnum) {
		this.badnum = badnum;
	}
	
	@ExcelField(title="良率", align=2, sort=14)
	public String getRate() {
		return rate;
	}

	public void setRate(String rate) {
		this.rate = rate;
	}
	
	@ExcelField(title="不良现象明细", align=2, sort=15)
	public String getBaddetail() {
		return baddetail;
	}

	public void setBaddetail(String baddetail) {
		this.baddetail = baddetail;
	}

	public String getIsGood() {
		return isGood;
	}

	public void setIsGood(String isGood) {
		this.isGood = isGood;
	}
	
	
}