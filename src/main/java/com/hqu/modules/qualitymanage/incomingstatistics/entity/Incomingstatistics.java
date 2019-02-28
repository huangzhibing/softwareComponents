/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.qualitymanage.incomingstatistics.entity;


import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 主要供应商来料不良批次统计Entity
 * @author syc
 * @version 2018-08-25
 */
public class Incomingstatistics extends DataEntity<Incomingstatistics> implements Comparable{
	
	private static final long serialVersionUID = 1L;
	private String supName;		// 供应商名称
	private Integer sumBatch;		// 来料批次
	private Integer badBatch;		// 不良批次
	private String y;		// 年
	private String m;		// 月
	
	public Incomingstatistics() {
		super();
	}

	public Incomingstatistics(String id){
		super(id);
	}

	@ExcelField(title="供应商名称", align=2, sort=7)
	public String getSupName() {
		return supName;
	}

	public void setSupName(String supName) {
		this.supName = supName;
	}
	
	@ExcelField(title="来料批次", align=2, sort=8)
	public Integer getSumBatch() {
		return sumBatch;
	}

	public void setSumBatch(Integer sumBatch) {
		this.sumBatch = sumBatch;
	}
	
	@ExcelField(title="不良批次", align=2, sort=9)
	public Integer getBadBatch() {
		return badBatch;
	}

	public void setBadBatch(Integer badBatch) {
		this.badBatch = badBatch;
	}
	
	@ExcelField(title="年", align=2, sort=10)
	public String getY() {
		return y;
	}

	public void setY(String y) {
		this.y = y;
	}
	
	@ExcelField(title="月", dictType="mm", align=2, sort=11)
	public String getM() {
		return m;
	}

	public void setM(String m) {
		this.m = m;
	}

	@Override
	public int compareTo(Object o) {
		 if (o instanceof Incomingstatistics) {
			 Incomingstatistics in = (Incomingstatistics)o;
			 if(this.badBatch>in.getBadBatch()){
				 return -1;
			 }
			 if(this.badBatch==in.getBadBatch()){
				 return 0;
			 }
			 if(this.badBatch<in.getBadBatch()){
				 return 1;
			 }
		 }
		return 0;
	}
	
}