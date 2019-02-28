/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.purchasemanage.pursupbackdetail.entity;


import com.jeeplus.core.persistence.DataEntity;

import java.util.Calendar;
import java.util.Date;

import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 供应商退货明细Entity
 * @author zz
 * @version 2018-08-19
 */
public class PurSupBackDetail extends DataEntity<PurSupBackDetail> {
	
	private static final long serialVersionUID = 1L;
	private Integer serialNum;		// 序号
	private String backDate;		// 退货日期
	private String billNum;		// 退货号码
	private String supId;		// 供应商编码
	private String supName;		// 供应商名称
	private String itemCode;		// 物料代码
	private String itemName;		// 物料名称
	private String itemSpecModel;		// 物料规格
	private String backQty;		// 退货数量
	private String unitCode;		// 单位
	private String realPriceTaxed;		// 单价
	private String realSumTaxed;		// 总金额
	private String conBillNum;		// 采购单号
	private String backReason;		// 退货原因
	private Date beginBackDate;		// 开始 退货日期
	private Date endBackDate;		// 结束 退货日期
	
	public PurSupBackDetail() {
		super();
	}

	public PurSupBackDetail(String id){
		super(id);
	}

	@ExcelField(title="序号", align=2, sort=7)
	public Integer getSerialNum() {
		return serialNum;
	}

	public void setSerialNum(Integer serialNum) {
		this.serialNum = serialNum;
	}
	
	@ExcelField(title="退货日期", align=2, sort=8)
	public String getBackDate() {
		return backDate;
	}

	public void setBackDate(String backDate) {
		this.backDate = backDate;
	}
	
	@ExcelField(title="退货号码", align=2, sort=9)
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
	
	@ExcelField(title="物料代码", align=2, sort=12)
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
	public String getItemSpecModel() {
		return itemSpecModel;
	}

	public void setItemSpecModel(String itemSpecModel) {
		this.itemSpecModel = itemSpecModel;
	}
	
	@ExcelField(title="退货数量", align=2, sort=15)
	public String getBackQty() {
		return backQty;
	}

	public void setBackQty(String backQty) {
		this.backQty = backQty;
	}
	
	@ExcelField(title="单位", align=2, sort=16)
	public String getUnitCode() {
		return unitCode;
	}

	public void setUnitCode(String unitCode) {
		this.unitCode = unitCode;
	}
	
	@ExcelField(title="单价", align=2, sort=17)
	public String getRealPriceTaxed() {
		return realPriceTaxed;
	}

	public void setRealPriceTaxed(String realPriceTaxed) {
		this.realPriceTaxed = realPriceTaxed;
	}
	
	@ExcelField(title="总金额", align=2, sort=18)
	public String getRealSumTaxed() {
		return realSumTaxed;
	}

	public void setRealSumTaxed(String realSumTaxed) {
		this.realSumTaxed = realSumTaxed;
	}
	
	@ExcelField(title="采购单号", align=2, sort=19)
	public String getConBillNum() {
		return conBillNum;
	}

	public void setConBillNum(String conBillNum) {
		this.conBillNum = conBillNum;
	}
	
	@ExcelField(title="退货原因", align=2, sort=20)
	public String getBackReason() {
		return backReason;
	}

	public void setBackReason(String backReason) {
		this.backReason = backReason;
	}
	
	public Date getBeginBackDate() {
		return beginBackDate;
	}

	public void setBeginBackDate(Date beginBackDate) {
		this.beginBackDate = beginBackDate;
	}
	
	public Date getEndBackDate() {
		return endBackDate;
	}

	public void setEndBackDate(Date endBackDate) {
		if(endBackDate != null){
			Calendar c = Calendar.getInstance();
			c.setTime(endBackDate);
			c.add(Calendar.DAY_OF_MONTH, 1);
			this.endBackDate = c.getTime();
		}else{
			this.endBackDate = endBackDate;
		}
	}
		
}