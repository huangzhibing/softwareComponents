/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.purchasemanage.group.entity;

import com.jeeplus.modules.sys.entity.User;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 采购员Entity
 * @author 方翠虹,石豪迈,陶瑶
 * @version 2018-04-18
 */
public class GroupBuyer extends DataEntity<GroupBuyer> {
	
	private static final long serialVersionUID = 1L;
	private Group Group;		// 采购组代码 父类
	private User user;		// 采购员编码
	private String buyername;		// 采购员名称
	private String buyerlevel;		// 采购员级别
	private String buyerphonenum;   //采购员电话
	private String buyerfaxnum;    //采购员传真
	private String deliveryaddress; //送货地址

	public String getBuyerphonenum() {
		return buyerphonenum;
	}

	public void setBuyerphonenum(String buyerphonenum) {
		this.buyerphonenum = buyerphonenum;
	}

	public String getBuyerfaxnum() {
		return buyerfaxnum;
	}

	public void setBuyerfaxnum(String buyerfaxnum) {
		this.buyerfaxnum = buyerfaxnum;
	}

	public String getDeliveryaddress() {
		return deliveryaddress;
	}

	public void setDeliveryaddress(String deliveryaddress) {
		this.deliveryaddress = deliveryaddress;
	}

	public GroupBuyer() {
		super();
	}

	public GroupBuyer(String id){
		super(id);
	}

	public GroupBuyer(Group Group){
		this.Group = Group;
	}

	public Group getGroup() {
		return Group;
	}

	public void setGroup(Group Group) {
		this.Group = Group;
	}
	
	@ExcelField(title="采购员编码", fieldType=User.class, value="user.name", align=2, sort=8)
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	@ExcelField(title="采购员名称", align=2, sort=9)
	public String getBuyername() {
		return buyername;
	}

	public void setBuyername(String buyername) {
		this.buyername = buyername;
	}
	
	@ExcelField(title="采购员级别", dictType="buyerLevel", align=2, sort=10)
	public String getBuyerlevel() {
		return buyerlevel;
	}

	public void setBuyerlevel(String buyerlevel) {
		this.buyerlevel = buyerlevel;
	}
	
}