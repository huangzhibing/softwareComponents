/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.qualitymanage.machinequalitysta.entity;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 整机良率统计（电子看板）Entity
 * @author yxb
 * @version 2018-11-17
 */
public class MachineRate extends DataEntity<MachineRate> {
	
	private static final long serialVersionUID = 1L;
	private Date timeScope;		// 统计的时间粒度（依据时间统计类型不同而做不一样的读写处理）
	private String timeType;		// 时间统计类型
	private Integer agNum;		// 安规产出量
	private Double agRatio;		// 安规良率
	private Integer lhNum;		// 功能老化产出量
	private Double lhRatio;		// 功能老化良率
	private Integer jhNum;		// 激活产出量
	private Double jhRatio;		// 激活良率
	private Integer bzNum;		// 包装产出量
	private Double bzRatio;		// 包装良率
	private Date beginTimeScope;		// 开始 统计的时间粒度（依据时间统计类型不同而做不一样的读写处理）
	private Date endTimeScope;		// 结束 统计的时间粒度（依据时间统计类型不同而做不一样的读写处理）
	
	public MachineRate() {
		super();
	}

	public MachineRate(String id){
		super(id);
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="统计的时间粒度（依据时间统计类型不同而做不一样的读写处理）", align=2, sort=7)
	public Date getTimeScope() {
		return timeScope;
	}

	public void setTimeScope(Date timeScope) {
		this.timeScope = timeScope;
	}
	
	@ExcelField(title="时间统计类型", dictType="qms_time_type", align=2, sort=8)
	public String getTimeType() {
		return timeType;
	}

	public void setTimeType(String timeType) {
		this.timeType = timeType;
	}
	
	@ExcelField(title="安规产出量", align=2, sort=9)
	public Integer getAgNum() {
		return agNum;
	}

	public void setAgNum(Integer agNum) {
		this.agNum = agNum;
	}
	
	@ExcelField(title="安规良率", align=2, sort=10)
	public Double getAgRatio() {
		return agRatio;
	}

	public void setAgRatio(Double agRatio) {
		this.agRatio = agRatio;
	}
	
	@ExcelField(title="功能老化产出量", align=2, sort=11)
	public Integer getLhNum() {
		return lhNum;
	}

	public void setLhNum(Integer lhNum) {
		this.lhNum = lhNum;
	}
	
	@ExcelField(title="功能老化良率", align=2, sort=12)
	public Double getLhRatio() {
		return lhRatio;
	}

	public void setLhRatio(Double lhRatio) {
		this.lhRatio = lhRatio;
	}
	
	@ExcelField(title="激活产出量", align=2, sort=13)
	public Integer getJhNum() {
		return jhNum;
	}

	public void setJhNum(Integer jhNum) {
		this.jhNum = jhNum;
	}
	
	@ExcelField(title="激活良率", align=2, sort=14)
	public Double getJhRatio() {
		return jhRatio;
	}

	public void setJhRatio(Double jhRatio) {
		this.jhRatio = jhRatio;
	}
	
	@ExcelField(title="包装产出量", align=2, sort=15)
	public Integer getBzNum() {
		return bzNum;
	}

	public void setBzNum(Integer bzNum) {
		this.bzNum = bzNum;
	}
	
	@ExcelField(title="包装良率", align=2, sort=16)
	public Double getBzRatio() {
		return bzRatio;
	}

	public void setBzRatio(Double bzRatio) {
		this.bzRatio = bzRatio;
	}
	
	public Date getBeginTimeScope() {
		return beginTimeScope;
	}

	public void setBeginTimeScope(Date beginTimeScope) {
		this.beginTimeScope = beginTimeScope;
	}
	
	public Date getEndTimeScope() {
		return endTimeScope;
	}

	public void setEndTimeScope(Date endTimeScope) {
		this.endTimeScope = endTimeScope;
	}
		
}