/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.purchasemanage.purinvcheckpunctual.entity;


import com.jeeplus.core.persistence.DataEntity;

import java.util.Calendar;
import java.util.Date;

import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 供应商到货准时率明细Entity
 * @author zz
 * @version 2018-08-16
 */
public class PurInvCheckPunctual extends DataEntity<PurInvCheckPunctual> {
	
	private static final long serialVersionUID = 1L;
	private String serialNum;		// 序号
	private String makeDate;		// 下单日期
	private String billNum;		// 订单号码
	private String supId;		// 供应商编码
	private String supName;		// 供应商名称
	private String itemCode;		// 物料编码
	private String itemName;		// 物料名称
	private String itemSpecmodel;		// 物料规格
	private String makeNum;		// 下单数量
	private String arriveDate;		// 交货日期
	private String actualArriveDate;		// 实际交货日期
	private String checkQty;		// 实际交货数量
	private String delayedDays;		// 延期天数
	private String makeEmpid;		// 制单人id
	private String makeEmpname;		// 制单人名称
	private String remark;		// 备注
	private Date beginMakeDate;		// 开始 下单日期
	private Date endMakeDate;		// 结束 下单日期
	
	public PurInvCheckPunctual() {
		super();
	}

	public PurInvCheckPunctual(String id){
		super(id);
	}

	@ExcelField(title="序号", align=2, sort=7)
	public String getSerialNum() {
		return serialNum;
	}

	public void setSerialNum(String serialNum) {
		this.serialNum = serialNum;
	}
	
	@ExcelField(title="下单日期", align=2, sort=8)
	public String getMakeDate() {
		return makeDate;
	}

	public void setMakeDate(String makeDate) {
		this.makeDate = makeDate;
	}
	
	@ExcelField(title="订单号码", align=2, sort=9)
	public String getBillNum() {
		return billNum;
	}

	public void setBillNum(String billNum) {
		this.billNum = billNum;
	}
	
	@ExcelField(title="供应商编码", align=2, sort=10)
	public String getSupId() {
		return supId;
	}

	public void setSupId(String supId) {
		this.supId = supId;
	}
	
	@ExcelField(title="供应商名称", align=2, sort=11)
	public String getSupName() {
		return supName;
	}

	public void setSupName(String supName) {
		this.supName = supName;
	}
	
	@ExcelField(title="物料编码", align=2, sort=12)
	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}
	
	@ExcelField(title="物料名称", align=2, sort=13)
	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	
	@ExcelField(title="物料规格", align=2, sort=14)
	public String getItemSpecmodel() {
		return itemSpecmodel;
	}

	public void setItemSpecmodel(String itemSpecmodel) {
		this.itemSpecmodel = itemSpecmodel;
	}
	
	@ExcelField(title="下单数量", align=2, sort=15)
	public String getMakeNum() {
		return makeNum;
	}

	public void setMakeNum(String makeNum) {
		this.makeNum = makeNum;
	}
	
	@ExcelField(title="交货日期", align=2, sort=16)
	public String getArriveDate() {
		return arriveDate;
	}

	public void setArriveDate(String arriveDate) {
		this.arriveDate = arriveDate;
	}
	
	@ExcelField(title="实际交货日期", align=2, sort=17)
	public String getActualArriveDate() {
		return actualArriveDate;
	}

	public void setActualArriveDate(String actualArriveDate) {
		this.actualArriveDate = actualArriveDate;
	}
	
	@ExcelField(title="实际交货数量", align=2, sort=18)
	public String getCheckQty() {
		return checkQty;
	}

	public void setCheckQty(String checkQty) {
		this.checkQty = checkQty;
	}
	
	@ExcelField(title="延期天数", align=2, sort=19)
	public String getDelayedDays() {
		return delayedDays;
	}

	public void setDelayedDays(String delayedDays) {
		this.delayedDays = delayedDays;
	}
	
	@ExcelField(title="制单人id", align=2, sort=20)
	public String getMakeEmpid() {
		return makeEmpid;
	}

	public void setMakeEmpid(String makeEmpid) {
		this.makeEmpid = makeEmpid;
	}
	
	@ExcelField(title="制单人名称", align=2, sort=21)
	public String getMakeEmpname() {
		return makeEmpname;
	}

	public void setMakeEmpname(String makeEmpname) {
		this.makeEmpname = makeEmpname;
	}
	
	@ExcelField(title="备注", align=2, sort=22)
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	public Date getBeginMakeDate() {
		return beginMakeDate;
	}

	public void setBeginMakeDate(Date beginMakeDate) {
		this.beginMakeDate = beginMakeDate;
	}
	
	public Date getEndMakeDate() {
		return endMakeDate;
	}

	public void setEndMakeDate(Date endMakeDate) {
		if(endMakeDate != null){
			Calendar c = Calendar.getInstance();
			c.setTime(endMakeDate);
			c.add(Calendar.DAY_OF_MONTH, 1);
			this.endMakeDate = c.getTime();
		}else{
			this.endMakeDate = endMakeDate;
		}
	}
		
}