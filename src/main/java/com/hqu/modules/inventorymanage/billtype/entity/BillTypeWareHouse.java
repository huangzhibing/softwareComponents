/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.inventorymanage.billtype.entity;

import com.hqu.modules.inventorymanage.warehouse.entity.WareHouse;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 单据类型与仓库关系表Entity
 * @author dongqida
 * @version 2018-04-14
 */
public class BillTypeWareHouse extends DataEntity<BillTypeWareHouse> {
	
	private static final long serialVersionUID = 1L;
	private BillType billType;		// 出入库类型 父类
	private WareHouse wareHouse;		// 库房号
	private String wareName;		// 库房名称
	private String adminMode;		// 管理标识
	
	public BillTypeWareHouse() {
		super();
	}

	public BillTypeWareHouse(String id){
		super(id);
	}

	public BillTypeWareHouse(BillType billType){
		this.billType = billType;
	}

	public BillType getBillType() {
		return billType;
	}

	public void setBillType(BillType billType) {
		this.billType = billType;
	}
	
	@ExcelField(title="库房号", fieldType=WareHouse.class, value="wareHouse.wareName", align=2, sort=8)
	public WareHouse getWareHouse() {
		return wareHouse;
	}

	public void setWareHouse(WareHouse wareHouse) {
		this.wareHouse = wareHouse;
	}
	
	@ExcelField(title="库房名称", align=2, sort=9)
	public String getWareName() {
		return wareName;
	}

	public void setWareName(String wareName) {
		this.wareName = wareName;
	}
	
	@ExcelField(title="管理标识", align=2, sort=10)
	public String getAdminMode() {
		return adminMode;
	}

	public void setAdminMode(String adminMode) {
		this.adminMode = adminMode;
	}
	
}