/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.inventorymanage.invaccountcode.entity;

import com.hqu.modules.inventorymanage.warehouse.entity.WareHouse;
import com.hqu.modules.basedata.item.entity.Item;
import com.hqu.modules.inventorymanage.bin.entity.Bin;
import com.hqu.modules.inventorymanage.location.entity.Location;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 库存帐扫码Entity
 * @author M1ngz
 * @version 2018-06-03
 */
public class InvAccountCode extends DataEntity<InvAccountCode> {
	
	private static final long serialVersionUID = 1L;
	private WareHouse ware;		// 仓库编号
	private Item item;		// 物料编号
	private String year;		// 会计年度
	private String period;		// 会计期间
	private Bin bin;		// 货区编号
	private Location loc;		// 货位编号
	private String itemBatch;		// 批次号
	private String itemBarcode;		// 物料二维码
	private String supBarcode;		// 供应商二维码
	private String oflag;
	private String machineBarcode;
	private String activationBarcode;
	public InvAccountCode() {
		super();
	}

	public InvAccountCode(String id){
		super(id);
	}

	@ExcelField(title="出库标志", align=2, sort=8)
	public String getOflag() {
		return oflag;
	}


	public void setOflag(String oflag) {
		this.oflag = oflag;
	}

	@ExcelField(title="仓库编号", fieldType=WareHouse.class, value="ware.wareID", align=2, sort=7)
	public WareHouse getWare() {
		return ware;
	}

	public void setWare(WareHouse ware) {
		this.ware = ware;
	}
	
	@ExcelField(title="物料编号", fieldType=Item.class, value="item.code", align=2, sort=8)
	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}
	
	@ExcelField(title="会计年度", align=2, sort=9)
	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}
	
	@ExcelField(title="会计期间", align=2, sort=10)
	public String getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period = period;
	}
	
	@ExcelField(title="货区编号", fieldType=Bin.class, value="bin.binId", align=2, sort=11)
	public Bin getBin() {
		return bin;
	}

	public void setBin(Bin bin) {
		this.bin = bin;
	}
	
	@ExcelField(title="货位编号", fieldType=Location.class, value="loc.locId", align=2, sort=12)
	public Location getLoc() {
		return loc;
	}

	public void setLoc(Location loc) {
		this.loc = loc;
	}
	
	@ExcelField(title="批次号", align=2, sort=13)
	public String getItemBatch() {
		return itemBatch;
	}

	public void setItemBatch(String itemBatch) {
		this.itemBatch = itemBatch;
	}
	
	@ExcelField(title="物料二维码", align=2, sort=14)
	public String getItemBarcode() {
		return itemBarcode;
	}

	public void setItemBarcode(String itemBarcode) {
		this.itemBarcode = itemBarcode;
	}
	
	@ExcelField(title="供应商二维码", align=2, sort=15)
	public String getSupBarcode() {
		return supBarcode;
	}

	public void setSupBarcode(String supBarcode) {
		this.supBarcode = supBarcode;
	}

	public String getMachineBarcode() {
		return machineBarcode;
	}

	public void setMachineBarcode(String machineBarcode) {
		this.machineBarcode = machineBarcode;
	}

	public String getActivationBarcode() {
		return activationBarcode;
	}

	public void setActivationBarcode(String activationBarcode) {
		this.activationBarcode = activationBarcode;
	}
}