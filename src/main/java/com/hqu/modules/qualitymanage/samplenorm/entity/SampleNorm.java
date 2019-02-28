/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.qualitymanage.samplenorm.entity;


import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 抽样标准维护Entity
 * @author ckw
 * @version 2018-09-05
 */
public class SampleNorm extends DataEntity<SampleNorm> {
	
	private static final long serialVersionUID = 1L;
	private String snormCode;		// 抽样标准编码
	private String snormName;		// 抽样标准名称
	private String snormDes;		// 抽样标准描述
	
	public SampleNorm() {
		super();
	}

	public SampleNorm(String id){
		super(id);
	}

	@ExcelField(title="抽样标准编码", align=2, sort=7)
	public String getSnormCode() {
		return snormCode;
	}

	public void setSnormCode(String snormCode) {
		this.snormCode = snormCode;
	}
	
	@ExcelField(title="抽样标准名称", align=2, sort=8)
	public String getSnormName() {
		return snormName;
	}

	public void setSnormName(String snormName) {
		this.snormName = snormName;
	}
	
	@ExcelField(title="抽样标准描述", align=2, sort=9)
	public String getSnormDes() {
		return snormDes;
	}

	public void setSnormDes(String snormDes) {
		this.snormDes = snormDes;
	}
	
}