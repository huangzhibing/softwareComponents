/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.qualitymanage.purreportnew.entity;

import com.hqu.modules.qualitymanage.mattertype.entity.MatterType;
import javax.validation.constraints.NotNull;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * IQC来料检验报告功能检查缺陷描述Entity
 * @author syc
 * @version 2018-08-18
 */
public class Purreportfundetail extends DataEntity<Purreportfundetail> {
	
	private static final long serialVersionUID = 1L;
	private PurReportNew reportId;		// 报告单主键 父类
	private MatterType matterType;		// 产品功能缺陷描述
	private Integer crNum;		// 致命缺陷产品数量
	private Integer majNum;		// 严重缺陷产品数量
	private Integer minNum;		// 轻微缺陷产品数量
	private Integer sum;		// 不良品数量
	
	public Purreportfundetail() {
		super();
	}

	public Purreportfundetail(String id){
		super(id);
	}

	public Purreportfundetail(PurReportNew reportId){
		this.reportId = reportId;
	}

	public PurReportNew getReportId() {
		return reportId;
	}

	public void setReportId(PurReportNew reportId) {
		this.reportId = reportId;
	}
	
	@NotNull(message="产品功能缺陷描述不能为空")
	@ExcelField(title="产品功能缺陷描述", fieldType=MatterType.class, value="matterType.mattername", align=2, sort=8)
	public MatterType getMatterType() {
		return matterType;
	}

	public void setMatterType(MatterType matterType) {
		this.matterType = matterType;
	}
	
	@ExcelField(title="致命缺陷产品数量", align=2, sort=9)
	public Integer getCrNum() {
		return crNum;
	}

	public void setCrNum(Integer crNum) {
		this.crNum = crNum;
	}
	
	@ExcelField(title="严重缺陷产品数量", align=2, sort=10)
	public Integer getMajNum() {
		return majNum;
	}

	public void setMajNum(Integer majNum) {
		this.majNum = majNum;
	}
	
	@ExcelField(title="轻微缺陷产品数量", align=2, sort=11)
	public Integer getMinNum() {
		return minNum;
	}

	public void setMinNum(Integer minNum) {
		this.minNum = minNum;
	}
	
	@ExcelField(title="不良品数量", align=2, sort=12)
	public Integer getSum() {
		return sum;
	}

	public void setSum(Integer sum) {
		this.sum = sum;
	}
	
}