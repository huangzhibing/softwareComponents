/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.qualitymanage.qualitynorm.entity;


import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 检验执行标准Entity
 * @author syc
 * @version 2018-04-26
 */
public class QualityNorm extends DataEntity<QualityNorm> {
	
	private static final long serialVersionUID = 1L;
	private String qnormcode;		// 执行标准编码
	private String qnormname;		// 执行标准名称
	private String qnormdes;		// 执行标准描述
	
	public QualityNorm() {
		super();
	}

	public QualityNorm(String id){
		super(id);
	}

	@ExcelField(title="执行标准编码", align=2, sort=7)
	public String getQnormcode() {
		return qnormcode;
	}

	public void setQnormcode(String qnormcode) {
		this.qnormcode = qnormcode;
	}
	
	@ExcelField(title="执行标准名称", align=2, sort=8)
	public String getQnormname() {
		return qnormname;
	}

	public void setQnormname(String qnormname) {
		this.qnormname = qnormname;
	}
	
	@ExcelField(title="执行标准描述", align=2, sort=9)
	public String getQnormdes() {
		return qnormdes;
	}

	public void setQnormdes(String qnormdes) {
		this.qnormdes = qnormdes;
	}
	
}