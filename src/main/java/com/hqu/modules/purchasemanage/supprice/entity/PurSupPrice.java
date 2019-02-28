/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.purchasemanage.supprice.entity;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.google.common.collect.Lists;
import com.hqu.modules.basedata.account.entity.Account;
import com.hqu.modules.basedata.item.entity.Item;
import com.hqu.modules.purchasemanage.suptype.entity.SupType;
import com.jeeplus.common.utils.excel.annotation.ExcelField;
import com.jeeplus.core.persistence.DataEntity;

/**
 * 供应商价格维护Entity
 * @author syc
 * @version 2018-08-02
 */
public class PurSupPrice extends DataEntity<PurSupPrice> {
	
	private static final long serialVersionUID = 1L;
	private Account account;		// 供应商代码
	private String supName;		// 供应商名称
	private String areaCode;		// 所属地区编码
	private String areaName;		// 所属地区名称
	private SupType supType;		// 供应商类别编码
	private String supTypeName;		// 供应商类别名称
	private String prop;		// 企业性质
	private String address;		// 地址
	private Item item;		// 物料编码
	private String itemName;		// 物料名称
	private Date date;
	private List<PurSupPriceDetail> purSupPriceDetailList = Lists.newArrayList();		// 子表列表
	
	public PurSupPrice() {
		super();
	}

	public PurSupPrice(String id){
		super(id);
	}

	@NotNull(message="供应商代码不能为空")
	@ExcelField(title="供应商代码", fieldType=Account.class, value="account.accountCode", align=2, sort=7)
	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}
	
	@ExcelField(title="供应商名称", align=2, sort=8)
	public String getSupName() {
		return supName;
	}

	public void setSupName(String supName) {
		this.supName = supName;
	}
	
	@ExcelField(title="所属地区编码", align=2, sort=9)
	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}
	
	@ExcelField(title="所属地区名称", align=2, sort=10)
	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}
	
	@NotNull(message="供应商类别编码不能为空")
	@ExcelField(title="供应商类别编码", fieldType=SupType.class, value="supType.suptypeId", align=2, sort=11)
	public SupType getSupType() {
		return supType;
	}

	public void setSupType(SupType supType) {
		this.supType = supType;
	}
	
	@ExcelField(title="供应商类别名称", align=2, sort=12)
	public String getSupTypeName() {
		return supTypeName;
	}

	public void setSupTypeName(String supTypeName) {
		this.supTypeName = supTypeName;
	}
	
	@ExcelField(title="企业性质", align=2, sort=13)
	public String getProp() {
		return prop;
	}

	public void setProp(String prop) {
		this.prop = prop;
	}
	
	@ExcelField(title="地址", align=2, sort=14)
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
	public List<PurSupPriceDetail> getPurSupPriceDetailList() {
		return purSupPriceDetailList;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public void setPurSupPriceDetailList(List<PurSupPriceDetail> purSupPriceDetailList) {
		this.purSupPriceDetailList = purSupPriceDetailList;
	}
}