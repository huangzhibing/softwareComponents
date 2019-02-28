/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.costmanage.reportmanage.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hqu.modules.basedata.product.entity.Product;
import com.hqu.modules.basedata.workprocedure.entity.WorkProcedure;
import com.jeeplus.common.utils.excel.annotation.ExcelField;
import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.modules.sys.entity.User;

import java.util.Date;

/**
 * 人工工资Entity
 * @author 黄志兵
 * @version 2018-09-01
 */
public class ReportPersonWage extends DataEntity<ReportPersonWage> {
	
	private static final long serialVersionUID = 1L;
	private String periodId;		// 核算期
	private WorkProcedure workProcedure;		// 工序代码
	private User personCode;		// 人员代码
	private Product itemCode;		// 产品代码
	private String itemUnit;		// 单位计件工资
	private String itemQty;		// 工作量
	private String wageUnit;		// 单位分配工资
	private String billStatus;		// 单据状态
	private String billMode;		// 单据类型
	private String checkId;		// 稽核人代码
	private Date checkDate;		// 稽核日期
	private String checkName;		// 稽核人姓名
	private String makeId;		// 录入人代码
	private Date makeDate;		// 录入日期
	private String makeName;		// 录入人姓名
	private String cosBillNum;		// 成本单据号
	private String corBillNum;		// 对应单据号
	private String corSeqNo;		// 对应的序号
	private String officeName;      //车间名称
	private String beginDate;	//开始时间
	private String endDate;  //结束时间
	
	public ReportPersonWage() {
		super();
	}

	public ReportPersonWage(String id){
		super(id);
	}

	public String getOfficeName() {
		return officeName;
	}

	public void setOfficeName(String officeName) {
		this.officeName = officeName;
	}

	@ExcelField(title="核算期", align=2, sort=7)
	public String getPeriodId() {
		return periodId;
	}

	public void setPeriodId(String periodId) {
		this.periodId = periodId;
	}
	
	@ExcelField(title="工序代码", align=2, sort=8)
	public WorkProcedure getWorkCode() {
		return workProcedure;
	}

	public void setWorkCode(WorkProcedure workCode) {
		this.workProcedure = workCode;
	}
	
	@ExcelField(title="人员代码", align=2, sort=9)
	public User getPersonCode() {
		return personCode;
	}

	public void setPersonCode(User personCode) {
		this.personCode = personCode;
	}
	
	@ExcelField(title="产品代码", align=2, sort=10)
	public Product getItemCode() {
		return itemCode;
	}

	public void setItemCode(Product itemCode) {
		this.itemCode = itemCode;
	}
	
	@ExcelField(title="单位计件工资", align=2, sort=11)
	public String getItemUnit() {
		return itemUnit;
	}

	public void setItemUnit(String itemUnit) {
		this.itemUnit = itemUnit;
	}
	
	@ExcelField(title="工作量", align=2, sort=12)
	public String getItemQty() {
		return itemQty;
	}

	public void setItemQty(String itemQty) {
		this.itemQty = itemQty;
	}
	
	@ExcelField(title="单位分配工资", align=2, sort=13)
	public String getWageUnit() {
		return wageUnit;
	}

	public void setWageUnit(String wageUnit) {
		this.wageUnit = wageUnit;
	}
	
	@ExcelField(title="单据状态", dictType="billStatus", align=2, sort=14)
	public String getBillStatus() {
		return billStatus;
	}

	public void setBillStatus(String billStatus) {
		this.billStatus = billStatus;
	}
	
	@ExcelField(title="单据类型", dictType="billMode", align=2, sort=15)
	public String getBillMode() {
		return billMode;
	}

	public void setBillMode(String billMode) {
		this.billMode = billMode;
	}
	
	@ExcelField(title="稽核人代码", align=2, sort=16)
	public String getCheckId() {
		return checkId;
	}

	public void setCheckId(String checkId) {
		this.checkId = checkId;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="稽核日期", align=2, sort=17)
	public Date getCheckDate() {
		return checkDate;
	}

	public void setCheckDate(Date checkDate) {
		this.checkDate = checkDate;
	}
	
	@ExcelField(title="稽核人姓名", align=2, sort=18)
	public String getCheckName() {
		return checkName;
	}

	public void setCheckName(String checkName) {
		this.checkName = checkName;
	}
	
	@ExcelField(title="录入人代码", align=2, sort=19)
	public String getMakeId() {
		return makeId;
	}

	public void setMakeId(String makeId) {
		this.makeId = makeId;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="录入日期", align=2, sort=20)
	public Date getMakeDate() {
		return makeDate;
	}

	public void setMakeDate(Date makeDate) {
		this.makeDate = makeDate;
	}
	
	@ExcelField(title="录入人姓名", align=2, sort=21)
	public String getMakeName() {
		return makeName;
	}

	public void setMakeName(String makeName) {
		this.makeName = makeName;
	}
	
	@ExcelField(title="成本单据号", align=2, sort=22)
	public String getCosBillNum() {
		return cosBillNum;
	}

	public void setCosBillNum(String cosBillNum) {
		this.cosBillNum = cosBillNum;
	}
	
	@ExcelField(title="对应单据号", align=2, sort=23)
	public String getCorBillNum() {
		return corBillNum;
	}

	public void setCorBillNum(String corBillNum) {
		this.corBillNum = corBillNum;
	}
	
	@ExcelField(title="对应的序号", align=2, sort=24)
	public String getCorSeqNo() {
		return corSeqNo;
	}

	public void setCorSeqNo(String corSeqNo) {
		this.corSeqNo = corSeqNo;
	}

	public String getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(String beginDate) {
		this.beginDate = beginDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
}