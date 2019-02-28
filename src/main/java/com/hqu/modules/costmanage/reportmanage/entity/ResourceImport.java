package com.hqu.modules.costmanage.reportmanage.entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jeeplus.common.utils.excel.annotation.ExcelField;
import com.jeeplus.core.persistence.DataEntity;

public class ResourceImport extends DataEntity<ResourceImport>{
	
	private static final long serialVersionUID = 1L;
	private String corName;	
	private Date billDate;	
	private String billNum;	
	private String ioDesc;	
	private String wareName;	
	private String frontNum	;
	private String itemCode;	
	private String itemName;	
	private String itemSpec;	
	private String measUnit;	
	private Integer itemQty;	
	private Integer itemPrice;	
	private Integer itemSum;	
	private String billPerson;	
	private String wareEmpname;	
	private String billEmpname;
	private String note;
	private Date beginBillDate;		// 开始 出入库日期
	private Date endBillDate;		// 结束 出入库日期
	private String orderNum;
	private String workPosname;
	private String deptName;
	private String contractCode;
	
	
	public ResourceImport() {
		super();
	}
	
	public String getContractCode() {
		return contractCode;
	}

	public void setContractCode(String contractCode) {
		this.contractCode = contractCode;
	}

	public String getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}

	public String getWorkPosname() {
		return workPosname;
	}

	public void setWorkPosname(String workPosname) {
		this.workPosname = workPosname;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public Date getBeginBillDate() {
		return beginBillDate;
	}
	public void setBeginBillDate(Date beginBillDate) {
		this.beginBillDate = beginBillDate;
	}
	public Date getEndBillDate() {
		return endBillDate;
	}
	public void setEndBillDate(Date endBillDate) {
		this.endBillDate = endBillDate;
	}
	@ExcelField(title="供货单位", align=2, sort=7)
	public String getCorName() {
		return corName;
	}
	public void setCorName(String corName) {
		this.corName = corName;
	}
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="入库日期", align=2, sort=8)
	public Date getBillDate() {
		return billDate;
	}
	public void setBillDate(Date billDate) {
		this.billDate = billDate;
	}
	@ExcelField(title="入库单号", align=2, sort=9)
	public String getBillNum() {
		return billNum;
	}
	public void setBillNum(String billNum) {
		this.billNum = billNum;
	}
	@ExcelField(title="入库类别", align=2, sort=10)
	public String getIoDesc() {
		return ioDesc;
	}
	public void setIoDesc(String ioDesc) {
		this.ioDesc = ioDesc;
	}
	@ExcelField(title="所入仓库", align=2, sort=11)
	public String getWareName() {
		return wareName;
	}
	public void setWareName(String wareName) {
		this.wareName = wareName;
	}
	@ExcelField(title="订单号", align=2, sort=12)
	public String getFrontNum() {
		return frontNum;
	}
	public void setFrontNum(String frontNum) {
		this.frontNum = frontNum;
	}
	@ExcelField(title="存货编码", align=2, sort=13)
	public String getItemCode() {
		return itemCode;
	}
	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}
	@ExcelField(title="材料名称", align=2, sort=14)
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	@ExcelField(title="规格型号", align=2, sort=15)
	public String getItemSpec() {
		return itemSpec;
	}
	public void setItemSpec(String itemSpec) {
		this.itemSpec = itemSpec;
	}
	@ExcelField(title="计量单位", align=2, sort=16)
	public String getMeasUnit() {
		return measUnit;
	}
	public void setMeasUnit(String measUnit) {
		this.measUnit = measUnit;
	}
	@ExcelField(title="数量", align=2, sort=17)
	public Integer getItemQty() {
		return itemQty;
	}
	public void setItemQty(Integer itemQty) {
		this.itemQty = itemQty;
	}
	@ExcelField(title="单价", align=2, sort=18)
	public Integer getItemPrice() {
		return itemPrice;
	}
	public void setItemPrice(Integer itemPrice) {
		this.itemPrice = itemPrice;
	}
	@ExcelField(title="金额", align=2, sort=19)
	public Integer getItemSum() {
		return itemSum;
	}
	public void setItemSum(Integer itemSum) {
		this.itemSum = itemSum;
	}
	@ExcelField(title="采购人", align=2, sort=20)
	public String getBillPerson() {
		return billPerson;
	}
	public void setBillPerson(String billPerson) {
		this.billPerson = billPerson;
	}
	@ExcelField(title="仓库员", align=2, sort=21)
	public String getWareEmpname() {
		return wareEmpname;
	}
	public void setWareEmpname(String wareEmpname) {
		this.wareEmpname = wareEmpname;
	}
	@ExcelField(title="制单人", align=2, sort=22)
	public String getBillEmpname() {
		return billEmpname;
	}
	public void setBillEmpname(String billEmpname) {
		this.billEmpname = billEmpname;
	}
	@ExcelField(title="备注", align=2, sort=23)
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	
	

}
