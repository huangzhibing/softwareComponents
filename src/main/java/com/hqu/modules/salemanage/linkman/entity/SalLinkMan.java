/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.salemanage.linkman.entity;


import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 联系人维护Entity
 * @author hzm
 * @version 2018-05-13
 */
public class SalLinkMan extends DataEntity<SalLinkMan> {
	
	private static final long serialVersionUID = 1L;
	private Salaccount salaccount;		// 客户编码 父类
	private String linkManCode;		// 联系人编码
	private String linkManName;		// 联系人名称
	private String linkManSex;		// 联系人性别
	private String linkManTel;		// 联系人电话
	private String linkManMobile;		// 联系人手机
	private String linkManEmail;		// 联系人电子邮件
	private String linkManFax;		// 联系人传真
	private String remark;		// 备注
	
	public SalLinkMan() {
		super();
	}

	public SalLinkMan(String id){
		super(id);
	}

	public SalLinkMan(Salaccount salaccount){
		this.salaccount = salaccount;
	}

	public Salaccount getAccountCode() {
		return salaccount;
	}

	public void setAccountCode(Salaccount salaccount) {
		this.salaccount = salaccount;
	}
	
	@ExcelField(title="联系人编码", align=2, sort=2)
	public String getLinkManCode() {
		return linkManCode;
	}

	public void setLinkManCode(String linkManCode) {
		this.linkManCode = linkManCode;
	}
	
	@ExcelField(title="联系人名称", align=2, sort=3)
	public String getLinkManName() {
		return linkManName;
	}

	public void setLinkManName(String linkManName) {
		this.linkManName = linkManName;
	}
	
	@ExcelField(title="联系人性别", align=2, sort=4)
	public String getLinkManSex() {
		return linkManSex;
	}

	public void setLinkManSex(String linkManSex) {
		this.linkManSex = linkManSex;
	}
	
	@ExcelField(title="联系人电话", align=2, sort=5)
	public String getLinkManTel() {
		return linkManTel;
	}

	public void setLinkManTel(String linkManTel) {
		this.linkManTel = linkManTel;
	}
	
	@ExcelField(title="联系人手机", align=2, sort=6)
	public String getLinkManMobile() {
		return linkManMobile;
	}

	public void setLinkManMobile(String linkManMobile) {
		this.linkManMobile = linkManMobile;
	}
	
	@ExcelField(title="联系人电子邮件", align=2, sort=7)
	public String getLinkManEmail() {
		return linkManEmail;
	}

	public void setLinkManEmail(String linkManEmail) {
		this.linkManEmail = linkManEmail;
	}
	
	@ExcelField(title="联系人传真", align=2, sort=8)
	public String getLinkManFax() {
		return linkManFax;
	}

	public void setLinkManFax(String linkManFax) {
		this.linkManFax = linkManFax;
	}
	
	@ExcelField(title="备注", align=2, sort=9)
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
}