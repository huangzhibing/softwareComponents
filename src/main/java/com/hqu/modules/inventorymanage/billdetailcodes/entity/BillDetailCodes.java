/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.inventorymanage.billdetailcodes.entity;


import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 二维码扫描表Entity
 * @author huang.youcai
 * @version 2018-08-06
 */
public class BillDetailCodes extends DataEntity<BillDetailCodes> {

	private static final long serialVersionUID = 1L;
	private String billNum;		// 单据编号
	private String itemCode;		// 物料编号
	private String serialNum;		// 序号
	private String itemBarcode;		// 物料二维码
	private String supBarcode;		// 供应商二维码
	private String binId;		// 货区编号
	private String locId;		// 货位编号

	public BillDetailCodes() {
		super();
	}

	public BillDetailCodes(String id){
		super(id);
	}

	@ExcelField(title="单据编号", align=2, sort=6)
	public String getBillNum() {
		return billNum;
	}

	public void setBillNum(String billNum) {
		this.billNum = billNum;
	}

	@ExcelField(title="物料编号", align=2, sort=7)
	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	@ExcelField(title="序号", align=2, sort=8)
	public String getSerialNum() {
		return serialNum;
	}

	public void setSerialNum(String serialNum) {
		this.serialNum = serialNum;
	}

	@ExcelField(title="物料二维码", align=2, sort=9)
	public String getItemBarcode() {
		return itemBarcode;
	}

	public void setItemBarcode(String itemBarcode) {
		this.itemBarcode = itemBarcode;
	}

	@ExcelField(title="供应商二维码", align=2, sort=10)
	public String getSupBarcode() {
		return supBarcode;
	}

	public void setSupBarcode(String supBarcode) {
		this.supBarcode = supBarcode;
	}

	@ExcelField(title="货区编号", align=2, sort=11)
	public String getBinId() {
		return binId;
	}

	public void setBinId(String binId) {
		this.binId = binId;
	}

	@ExcelField(title="货位编号", align=2, sort=12)
	public String getLocId() {
		return locId;
	}

	public void setLocId(String locId) {
		this.locId = locId;
	}

}