/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.inventorymanage.bin.entity;


import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 货区定义Entity
 * @author M1ngz
 * @version 2018-04-12
 */
public class Bin extends DataEntity<Bin> {
	
	private static final long serialVersionUID = 1L;
	private String wareId;		// 库房号
	private String binId;		// 货区号
	private String binDesc;		// 货区名称
	private String note;		// 备注
	private String isLeaf;		// 是否末级
	
	public Bin() {
		super();
	}

	public Bin(String id){
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
	
	@ExcelField(title="货区名称", align=2, sort=9)
	public String getBinDesc() {
		return binDesc;
	}

	public void setBinDesc(String binDesc) {
		this.binDesc = binDesc;
	}
	
	@ExcelField(title="备注", align=2, sort=10)
	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}
	
	@ExcelField(title="是否末级", align=2, sort=11)
	public String getIsLeaf() {
		return isLeaf;
	}

	public void setIsLeaf(String isLeaf) {
		this.isLeaf = isLeaf;
	}
	
}