/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.purchasemanage.adtdetail.entity;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import javax.validation.constraints.NotNull;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 审核表Entity
 * @author ckw
 * @version 2018-05-08
 */
public class AdtDetail extends DataEntity<AdtDetail> {
	
	private static final long serialVersionUID = 1L;
	private String billNum;		// 单据编号
	private String billTypeCode;		// 单据类型编号
	private String billTypeName;		// 单据类型名称
	private String justifyResult;		// 审核结论
	private String justifyRemark;		// 审核意见
	private String jpersonCode;		// 审核人代码
	private String justifyPerson;		// 审核人
	private String jdeptCode;		// 审核部门编码
	private String jdeptName;		// 审核部门名称
	private String jpositionCode;		// 审核岗位编码
	private String jpositionName;		// 审核岗位名称
	private Date justifyDate;		// 审核日期
	private String modelCode;		// 模型编号
	private String isFinished;		// 当前审核步骤是否完成标志
	private Integer step;		// 审核步骤序号
	private String finishFlag;		// 审核流程结束标志
	
	public AdtDetail() {
		super();
	}

	public AdtDetail(String id){
		super(id);
	}

	@ExcelField(title="单据编号", align=2, sort=7)
	public String getBillNum() {
		return billNum;
	}

	public void setBillNum(String billNum) {
		this.billNum = billNum;
	}
	
	@ExcelField(title="单据类型编号", align=2, sort=8)
	public String getBillTypeCode() {
		return billTypeCode;
	}

	public void setBillTypeCode(String billTypeCode) {
		this.billTypeCode = billTypeCode;
	}
	
	@ExcelField(title="单据类型名称", align=2, sort=9)
	public String getBillTypeName() {
		return billTypeName;
	}

	public void setBillTypeName(String billTypeName) {
		this.billTypeName = billTypeName;
	}
	
	@ExcelField(title="审核结论", dictType="justifyResult", align=2, sort=10)
	public String getJustifyResult() {
		return justifyResult;
	}

	public void setJustifyResult(String justifyResult) {
		this.justifyResult = justifyResult;
	}
	
	@ExcelField(title="审核意见", align=2, sort=11)
	public String getJustifyRemark() {
		return justifyRemark;
	}

	public void setJustifyRemark(String justifyRemark) {
		this.justifyRemark = justifyRemark;
	}
	
	@ExcelField(title="审核人代码", align=2, sort=12)
	public String getJpersonCode() {
		return jpersonCode;
	}

	public void setJpersonCode(String jpersonCode) {
		this.jpersonCode = jpersonCode;
	}
	
	@ExcelField(title="审核人", align=2, sort=13)
	public String getJustifyPerson() {
		return justifyPerson;
	}

	public void setJustifyPerson(String justifyPerson) {
		this.justifyPerson = justifyPerson;
	}
	
	@ExcelField(title="审核部门编码", align=2, sort=14)
	public String getJdeptCode() {
		return jdeptCode;
	}

	public void setJdeptCode(String jdeptCode) {
		this.jdeptCode = jdeptCode;
	}
	
	@ExcelField(title="审核部门名称", align=2, sort=15)
	public String getJdeptName() {
		return jdeptName;
	}

	public void setJdeptName(String jdeptName) {
		this.jdeptName = jdeptName;
	}
	
	@ExcelField(title="审核岗位编码", align=2, sort=16)
	public String getJpositionCode() {
		return jpositionCode;
	}

	public void setJpositionCode(String jpositionCode) {
		this.jpositionCode = jpositionCode;
	}
	
	@ExcelField(title="审核岗位名称", align=2, sort=17)
	public String getJpositionName() {
		return jpositionName;
	}

	public void setJpositionName(String jpositionName) {
		this.jpositionName = jpositionName;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@NotNull(message="审核日期不能为空")
	@ExcelField(title="审核日期", align=2, sort=18)
	public Date getJustifyDate() {
		return justifyDate;
	}

	public void setJustifyDate(Date justifyDate) {
		this.justifyDate = justifyDate;
	}
	
	@ExcelField(title="模型编号", align=2, sort=19)
	public String getModelCode() {
		return modelCode;
	}

	public void setModelCode(String modelCode) {
		this.modelCode = modelCode;
	}
	
	@ExcelField(title="当前审核步骤是否完成标志", dictType="isFinished", align=2, sort=20)
	public String getIsFinished() {
		return isFinished;
	}

	public void setIsFinished(String isFinished) {
		this.isFinished = isFinished;
	}
	
	@ExcelField(title="审核步骤序号", align=2, sort=21)
	public Integer getStep() {
		return step;
	}

	public void setStep(Integer step) {
		this.step = step;
	}
	
	@ExcelField(title="审核流程结束标志", dictType="finishFlag", align=2, sort=22)
	public String getFinishFlag() {
		return finishFlag;
	}

	public void setFinishFlag(String finishFlag) {
		this.finishFlag = finishFlag;
	}
	
}