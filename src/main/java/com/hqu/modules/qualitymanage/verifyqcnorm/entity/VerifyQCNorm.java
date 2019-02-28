/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.qualitymanage.verifyqcnorm.entity;


import com.jeeplus.common.utils.excel.annotation.ExcelField;
import com.jeeplus.core.persistence.DataEntity;

/**
 * 检验数据验证Entity
 * @author 石豪迈
 * @version 2018-05-18
 */
public class VerifyQCNorm extends DataEntity<VerifyQCNorm> {
	
	private static final long serialVersionUID = 1L;
	private String reportId;		// 检验单编号
	private String objSn;		// 检验对象序列号
	private String objCode;		// 检验对象编码
	private String objName;		// 检验对象名称
	private String ruleId;		// 规则ID
	private String checkprj;		// 测试项目
	private String qcnormId;		// 检验标准号
	private String qcnormName;		// 检验标准名称
	private String isfisished;		// 是否检验完成
	private String roleCode;		// 岗位编码
	private String roleName;		// 岗位名称
	private String isQuality;		// 检验是否合格
	private String mserialno;		//机台序列码
	
	public VerifyQCNorm() {
		super();
		this.setIdType(IDTYPE_AUTO);
	}

	public VerifyQCNorm(String id){
		super(id);
	}

	@ExcelField(title="检验单编号", align=2, sort=7)
	public String getReportId() {
		return reportId;
	}

	public void setReportId(String reportId) {
		this.reportId = reportId;
	}
	
	@ExcelField(title="检验对象序列号", align=2, sort=8)
	public String getObjSn() {
		return objSn;
	}

	public void setObjSn(String objSn) {
		this.objSn = objSn;
	}
	
	@ExcelField(title="检验对象编码", align=2, sort=9)
	public String getObjCode() {
		return objCode;
	}

	public void setObjCode(String objCode) {
		this.objCode = objCode;
	}
	
	@ExcelField(title="检验对象名称", align=2, sort=10)
	public String getObjName() {
		return objName;
	}

	public void setObjName(String objName) {
		this.objName = objName;
	}
	
	@ExcelField(title="规则ID", align=2, sort=11)
	public String getRuleId() {
		return ruleId;
	}

	public void setRuleId(String ruleId) {
		this.ruleId = ruleId;
	}
	
	@ExcelField(title="测试项目", align=2, sort=12)
	public String getCheckprj() {
		return checkprj;
	}

	public void setCheckprj(String checkprj) {
		this.checkprj = checkprj;
	}
	
	@ExcelField(title="检验标准号", align=2, sort=13)
	public String getQcnormId() {
		return qcnormId;
	}

	public void setQcnormId(String qcnormId) {
		this.qcnormId = qcnormId;
	}
	
	@ExcelField(title="检验标准名称", align=2, sort=14)
	public String getQcnormName() {
		return qcnormName;
	}

	public void setQcnormName(String qcnormName) {
		this.qcnormName = qcnormName;
	}
	
	@ExcelField(title="是否检验完成", dictType="", align=2, sort=15)
	public String getIsfisished() {
		return isfisished;
	}

	public void setIsfisished(String isfisished) {
		this.isfisished = isfisished;
	}
	
	@ExcelField(title="岗位编码", align=2, sort=16)
	public String getRoleCode() {
		return roleCode;
	}

	public void setRoleCode(String roleCode) {
		this.roleCode = roleCode;
	}
	
	@ExcelField(title="岗位名称", align=2, sort=17)
	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	
	@ExcelField(title="检验是否合格", align=2, sort=18)
	public String getIsQuality() {
		return isQuality;
	}

	public void setIsQuality(String isQuality) {
		this.isQuality = isQuality;
	}

	public String getMserialno() {
		return mserialno;
	}

	public void setMserialno(String mserialno) {
		this.mserialno = mserialno;
	}
	
	
	
}