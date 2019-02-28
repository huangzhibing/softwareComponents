/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.purchasemanage.applytype.entity;


import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * ApplyTypeEntity
 * @author 方翠虹
 * @version 2018-04-25
 */
public class ApplyType extends DataEntity<ApplyType> {
	
	private static final long serialVersionUID = 1L;
	private String applytypeid;		// 类别编号
	private String applytypename;		// 类型名称
	private String applytypenote;		// 类型说明
	private String userDeptCode;		// 登陆用户所在部门编码
	
	public ApplyType() {
		super();
	}

	public ApplyType(String id){
		super(id);
	}

	@ExcelField(title="类别编号", align=2, sort=7)
	public String getApplytypeid() {
		return applytypeid;
	}

	public void setApplytypeid(String applytypeid) {
		this.applytypeid = applytypeid;
	}
	
	@ExcelField(title="类型名称", align=2, sort=8)
	public String getApplytypename() {
		return applytypename;
	}

	public void setApplytypename(String applytypename) {
		this.applytypename = applytypename;
	}
	
	@ExcelField(title="类型说明", align=2, sort=9)
	public String getApplytypenote() {
		return applytypenote;
	}

	public void setApplytypenote(String applytypenote) {
		this.applytypenote = applytypenote;
	}
	
	@ExcelField(title="登陆用户所在部门编码", align=2, sort=10)
	public String getUserDeptCode() {
		return userDeptCode;
	}

	public void setUserDeptCode(String userDeptCode) {
		this.userDeptCode = userDeptCode;
	}
	
}