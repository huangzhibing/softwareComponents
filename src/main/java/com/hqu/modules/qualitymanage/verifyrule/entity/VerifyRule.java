/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.qualitymanage.verifyrule.entity;


import com.hqu.modules.qualitymanage.qmsqcnorm.entity.QCNorm;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.modules.sys.entity.Role;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 验证规则表Entity
 * @author 石豪迈
 * @version 2018-05-16
 */
public class VerifyRule extends DataEntity<VerifyRule> {
	
	private static final long serialVersionUID = 1L;
	private String ruleId;		// 规则编码
	private String checkprj;		// 测试项目
	private QCNorm qcnorm;		// 检验标准号
	private String qcnormName;		// 检验标准名称
	private Role role;
	//private String roleCode;		// 岗位编码
	private String roleName;		// 岗位名称
	
	public VerifyRule() {
		super();
		this.setIdType(IDTYPE_AUTO);
	}

	public VerifyRule(String id){
		super(id);
	}

	@ExcelField(title="规则编码", align=2, sort=7)
	public String getRuleId() {
		return ruleId;
	}

	public void setRuleId(String ruleId) {
		this.ruleId = ruleId;
	}
	
	@ExcelField(title="测试项目", dictType="CheckPrj", align=2, sort=8)
	public String getCheckprj() {
		return checkprj;
	}

	public void setCheckprj(String checkprj) {
		this.checkprj = checkprj;
	}
	
	@ExcelField(title="检验标准号", fieldType=QCNorm.class, value="qcnorm.qcnormId", align=2, sort=9)
	public QCNorm getQcnorm() {
		return qcnorm;
	}

	public void setQcnorm(QCNorm qcnorm) {
		this.qcnorm = qcnorm;
	}
	
	@ExcelField(title="检验标准名称", align=2, sort=10)
	public String getQcnormName() {
		return qcnormName;
	}

	public void setQcnormName(String qcnormName) {
		this.qcnormName = qcnormName;
	}

	@ExcelField(title="岗位编码", fieldType=Role.class, value="role.id", align=2, sort=11)
	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role =role;
	}
	
	//@ExcelField(title="岗位编码", align=2, sort=11)
	//public String getRoleCode() {
	//	return roleCode;
	//}

	//public void setRoleCode(String roleCode) {
	//	this.roleCode = roleCode;
	//}
	
	@ExcelField(title="岗位名称", align=2, sort=12)
	public String getRoleName() {
		return roleName;
	}
	
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	
	
}