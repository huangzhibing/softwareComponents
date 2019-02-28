/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.purchasemanage.adtmodel.entity;



import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;
import com.jeeplus.modules.sys.entity.Role;

/**
 * 工作流模型Entity
 * @author ckw
 * @version 2018-05-08
 */
public class AdtModel extends DataEntity<AdtModel> {
	
	private static final long serialVersionUID = 1L;
	private String sn;		// 模型序列号
	private String modelCode;		// 模型编号
	private String jpersonCode;		// 审核人代码
	private String justifyPerson;		// 审核人
	private String jdeptCode;		// 审核部门编码
	private String jdeptName;		// 审核部门名称
	private Role role;		// 审核岗位编码
	private String roleName;		// 审核岗位名称
	private String isFirstperson;		// 是否第一审核人
	
	public AdtModel() {
		super();
	}

	public AdtModel(String id){
		super(id);
	}

	@ExcelField(title="模型序列号", align=2, sort=7)
	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}
	
	@ExcelField(title="模型编号", align=2, sort=8)
	public String getModelCode() {
		return modelCode;
	}

	public void setModelCode(String modelCode) {
		this.modelCode = modelCode;
	}

	@ExcelField(title="审核岗位代码", fieldType=Role.class, value="role.name", align=2, sort=9)
	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}
	@ExcelField(title="审核人岗位名称", align=2, sort=14)
	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	@ExcelField(title="审核人代码", align=2, sort=13)
	public String getJpersonCode() {
		return jpersonCode;
	}

	public void setJpersonCode(String jpersonCode) {
		this.jpersonCode = jpersonCode;
	}

	@ExcelField(title="审核人", align=2, sort=10)
	public String getJustifyPerson() {
		return justifyPerson;
	}

	public void setJustifyPerson(String justifyPerson) {
		this.justifyPerson = justifyPerson;
	}
	
	@ExcelField(title="审核部门编码", align=2, sort=11)
	public String getJdeptCode() {
		return jdeptCode;
	}

	public void setJdeptCode(String jdeptCode) {
		this.jdeptCode = jdeptCode;
	}
	
	@ExcelField(title="审核部门名称", align=2, sort=12)
	public String getJdeptName() {
		return jdeptName;
	}

	public void setJdeptName(String jdeptName) {
		this.jdeptName = jdeptName;
	}
	

	@ExcelField(title="是否第一审核人", align=2, sort=15)
	public String getIsFirstperson() {
		return isFirstperson;
	}

	public void setIsFirstperson(String isFirstperson) {
		this.isFirstperson = isFirstperson;
	}
	
}