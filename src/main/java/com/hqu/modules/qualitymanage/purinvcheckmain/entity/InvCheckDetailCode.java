/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.qualitymanage.purinvcheckmain.entity;


import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 二维码Entity
 * @author 张铮
 * @version 2018-06-22
 */
public class InvCheckDetailCode extends DataEntity<InvCheckDetailCode> {
	
	private static final long serialVersionUID = 1L;
	private InvCheckMain InvCheckMain; //父类
	private String billnum;		// 单据编号
	private String itemCode;		// 物料编号
	private String itemName;        //物料名称
	private String supBarcode;		// 供应商二维码
	private Integer serialnum;		// 序号
	private String itemBarcode;		// 物料二维码
	private String userDeptCode;		// 登陆人所在部门编码
	
	public InvCheckDetailCode() {
		super();
	}

	public InvCheckDetailCode(String id){
		super(id);
	}
	
	public InvCheckDetailCode(InvCheckMain InvCheckMain){
		this.InvCheckMain = InvCheckMain;
	}

	@ExcelField(title="单据编号", align=2, sort=7)
	public String getBillnum() {
		return billnum;
	}

	public void setBillnum(String billnum) {
		this.billnum = billnum;
	}
	
	@ExcelField(title="物料编号", align=2, sort=8)
	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}
	
	@ExcelField(title="供应商二维码", align=2, sort=9)
	public String getSupBarcode() {
		return supBarcode;
	}

	public void setSupBarcode(String supBarcode) {
		this.supBarcode = supBarcode;
	}
	
	@ExcelField(title="序号", align=2, sort=10)
	public Integer getSerialnum() {
		return serialnum;
	}

	public void setSerialnum(Integer serialnum) {
		this.serialnum = serialnum;
	}
	
	@ExcelField(title="物料二维码", align=2, sort=11)
	public String getItemBarcode() {
		return itemBarcode;
	}

	public void setItemBarcode(String itemBarcode) {
		this.itemBarcode = itemBarcode;
	}
	
	@ExcelField(title="登陆人所在部门编码", align=2, sort=12)
	public String getUserDeptCode() {
		return userDeptCode;
	}

	public void setUserDeptCode(String userDeptCode) {
		this.userDeptCode = userDeptCode;
	}

	public InvCheckMain getInvCheckMain() {
		return InvCheckMain;
	}

	public void setInvCheckMain(InvCheckMain invCheckMain) {
		InvCheckMain = invCheckMain;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	
}