/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.purchasemanage.linkman.entity;

import org.hibernate.validator.constraints.Email;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 联系人档案Entity
 * @author luyumiao
 * @version 2018-04-15
 */
public class LinkMan extends DataEntity<LinkMan> {
	
	private static final long serialVersionUID = 1L;
	private String linkCode;		// 联系人编号
	private String linkName;		// 联系人姓名
	private String linkSex;		// 性别
	private String linkLevel;		// 级别
	private String linkPosition;		// 职位
	private String linkTel;		// 电话
	private String linkPhone;		// 手机
	private String linkMail;		// mail
	private String state;		//状态:生效或失效
	private Paccount mdmAccount;		// 主表ID 父类
	
	public LinkMan() {
		super();
	}

	public LinkMan(String id){
		super(id);
	}

	public LinkMan(Paccount mdmAccount){
		this.mdmAccount = mdmAccount;
	}

	@ExcelField(title="联系人编号", align=2, sort=7)
	public String getLinkCode() {
		return linkCode;
	}

	public void setLinkCode(String linkCode) {
		this.linkCode = linkCode;
	}
	
	@ExcelField(title="联系人姓名", align=2, sort=8)
	public String getLinkName() {
		return linkName;
	}

	public void setLinkName(String linkName) {
		this.linkName = linkName;
	}
	
	@ExcelField(title="性别", dictType="sex", align=2, sort=9)
	public String getLinkSex() {
		return linkSex;
	}

	public void setLinkSex(String linkSex) {
		this.linkSex = linkSex;
	}
	
	@ExcelField(title="级别", align=2, sort=10)
	public String getLinkLevel() {
		return linkLevel;
	}

	public void setLinkLevel(String linkLevel) {
		this.linkLevel = linkLevel;
	}
	
	@ExcelField(title="职位", align=2, sort=11)
	public String getLinkPosition() {
		return linkPosition;
	}

	public void setLinkPosition(String linkPosition) {
		this.linkPosition = linkPosition;
	}
	
	@ExcelField(title="电话", align=2, sort=12)
	public String getLinkTel() {
		return linkTel;
	}

	public void setLinkTel(String linkTel) {
		this.linkTel = linkTel;
	}
	
	@ExcelField(title="手机", align=2, sort=13)
	public String getLinkPhone() {
		return linkPhone;
	}

	public void setLinkPhone(String linkPhone) {
		this.linkPhone = linkPhone;
	}
	
	@Email(message="mail必须为合法邮箱")
	@ExcelField(title="mail", align=2, sort=14)
	public String getLinkMail() {
		return linkMail;
	}

	public void setLinkMail(String linkMail) {
		this.linkMail = linkMail;
	}
	
	@ExcelField(title="状态", align=2, sort=15)
	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
	
	public Paccount getMdmAccount() {
		return mdmAccount;
	}

	public void setMdmAccount(Paccount mdmAccount) {
		this.mdmAccount = mdmAccount;
	}
	
}