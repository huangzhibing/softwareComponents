/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.qualitymanage.phenomenontemp.entity;


import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 来料不良主要现象Entity
 * @author syc
 * @version 2018-08-24
 */
public class Phenomenontemp extends DataEntity<Phenomenontemp> {
	
	private static final long serialVersionUID = 1L;
	private String sn;		// 序号
	private String phenomenon;		// 不良现象
	private Integer sum;		// 本月总不良批次
	private Integer y;		// 年份
	private Integer m;		// 月份
	
	public Phenomenontemp() {
		super();
	}

	public Phenomenontemp(String id){
		super(id);
	}

	@ExcelField(title="序号", align=2, sort=7)
	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}
	
	@ExcelField(title="不良现象", align=2, sort=8)
	public String getPhenomenon() {
		return phenomenon;
	}

	public void setPhenomenon(String phenomenon) {
		this.phenomenon = phenomenon;
	}
	
	@ExcelField(title="本月总不良批次", align=2, sort=9)
	public Integer getSum() {
		return sum;
	}

	public void setSum(Integer sum) {
		this.sum = sum;
	}
	
	@ExcelField(title="年份", align=2, sort=10)
	public Integer getY() {
		return y;
	}

	public void setY(Integer y) {
		this.y = y;
	}
	
	@ExcelField(title="月份", dictType="mm", align=2, sort=11)
	public Integer getM() {
		return m;
	}

	public void setM(Integer m) {
		this.m = m;
	}
	
}