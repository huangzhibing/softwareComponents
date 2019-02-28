/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.inventorymanage.location.entity;


import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 货位管理Entity
 * @author M1ngz
 * @version 2018-04-12
 */
public class Location extends DataEntity<Location> {
	
	private static final long serialVersionUID = 1L;
	private String wareId;		// 库房号
	private String binId;		// 货区号
	private String locId;		// 货位号
	private String locDesc;		// 货位名称
	private String note;		// 备注
	
	public Location() {
		super();
	}

	public Location(String id){
		super(id);
	}

	@ExcelField(title="库房号", align=2, sort=7)
	public String getWareId() {
		return wareId;
	}

	public void setWareId(String wareId) {
		this.wareId = wareId;
	}
	
	@ExcelField(title="货区号", align=2, sort=8)
	public String getBinId() {
		return binId;
	}

	public void setBinId(String binId) {
		this.binId = binId;
	}
	
	@ExcelField(title="货位号", align=2, sort=9)
	public String getLocId() {
		return locId;
	}

	public void setLocId(String locId) {
		this.locId = locId;
	}
	
	@ExcelField(title="货位名称", align=2, sort=10)
	public String getLocDesc() {
		return locDesc;
	}

	public void setLocDesc(String locDesc) {
		this.locDesc = locDesc;
	}
	
	@ExcelField(title="备注", align=2, sort=11)
	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}
	
}