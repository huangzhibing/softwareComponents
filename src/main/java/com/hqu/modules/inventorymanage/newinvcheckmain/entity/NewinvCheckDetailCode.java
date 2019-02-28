/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.inventorymanage.newinvcheckmain.entity;

import com.jeeplus.common.utils.excel.annotation.ExcelField;
import com.jeeplus.core.persistence.DataEntity;

/**
 * 超期复验单扫码表Entity
 * @author Neil
 * @version 2018-08-21
 */
public class NewinvCheckDetailCode extends DataEntity<NewinvCheckDetailCode> {
	
	private static final long serialVersionUID = 1L;
	private String serialNum;		// 序号
	private NewinvCheckMain billNum;		// 单据编号 父类
	private String itemCode;		// 物料编号
	private String itemBarcode;		// 物料二维码
	private String supBarcode;		// 供应商二维码
	private String binId;           //货区编号
	private String locId;			//货位编号
	
	public NewinvCheckDetailCode() {
		super();
	}

	public NewinvCheckDetailCode(String id){
		super(id);
	}

	public NewinvCheckDetailCode(NewinvCheckMain billNum){
		this.billNum = billNum;
	}

	@ExcelField(title="序号", align=2, sort=7)
	public String getSerialNum() {
		return serialNum;
	}

	public void setSerialNum(String serialNum) {
		this.serialNum = serialNum;
	}
	
	public NewinvCheckMain getBillNum() {
		return billNum;
	}

	public void setBillNum(NewinvCheckMain billNum) {
		this.billNum = billNum;
	}
	
	@ExcelField(title="物料编号", align=2, sort=9)
	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}
	
	@ExcelField(title="物料二维码", align=2, sort=10)
	public String getItemBarcode() {
		return itemBarcode;
	}

	public void setItemBarcode(String itemBarcode) {
		this.itemBarcode = itemBarcode;
	}
	
	@ExcelField(title="供应商二维码", align=2, sort=11)
	public String getSupBarcode() {
		return supBarcode;
	}

	public void setSupBarcode(String supBarcode) {
		this.supBarcode = supBarcode;
	}

	@ExcelField(title = "货区编号", align = 2, sort = 12)
	public String getBinId() {
		return binId;
	}

	public void setBinId(String binId) {
		this.binId=binId;
	}

	@ExcelField(title = "货位编号", align = 2, sort = 13)
	public String getLocId(){
		return locId;
	}

	public void setLocId(String locId) {
		this.locId=locId;
	}
}