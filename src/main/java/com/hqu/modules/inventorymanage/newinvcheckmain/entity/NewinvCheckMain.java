/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.inventorymanage.newinvcheckmain.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.common.collect.Lists;
import com.hqu.modules.inventorymanage.employee.entity.Employee;
import com.hqu.modules.inventorymanage.warehouse.entity.WareHouse;
import com.jeeplus.common.utils.excel.annotation.ExcelField;
import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.modules.sys.entity.User;

import java.util.Date;
import java.util.List;

/**
 * 超期复验Entity
 * @author Neil
 * @version 2018-06-15
 */
public class NewinvCheckMain extends DataEntity<NewinvCheckMain> {
	
	private static final long serialVersionUID = 1L;
	private String billNum;		// 单据号
	private String periodId;		// 核算期
	private Date makeDate;		// 制单日期
	private User makeEmpid;		// 制单人编码
	private String makeEmpName;		// 制单人姓名
	private String billFlag;		// 单据状态
	private WareHouse wareId;		// 仓库编码
	private String wareName;		// 仓库名称
	private Employee wareEmpId;		// 库管员编码
	private String wareEmpName;		// 库管员姓名
	private User checkEmpId;		// 复验员编码
	private String checkEmpName;		// 复验员姓名
	private String qmsFlag;		// 质检标志
	private Date beginMakeDate;		// 开始 制单日期
	private Date endMakeDate;		// 结束 制单日期
	private List<NewinvCheckDetail> newinvCheckDetailList = Lists.newArrayList();		// 子表列表
	private List<NewinvCheckDetailCode> newinvCheckDetailCodeList = Lists.newArrayList();		// 子表列表
	
	public NewinvCheckMain() {
		super();
	}

	public NewinvCheckMain(String id){
		super(id);
	}

	@ExcelField(title="单据号", align=2, sort=6)
	public String getBillNum() {
		return billNum;
	}

	public void setBillNum(String billNum) {
		this.billNum = billNum;
	}
	
	@ExcelField(title="核算期", align=2, sort=7)
	public String getPeriodId() {
		return periodId;
	}

	public void setPeriodId(String periodId) {
		this.periodId = periodId;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="制单日期", align=2, sort=8)
	public Date getMakeDate() {
		return makeDate;
	}

	public void setMakeDate(Date makeDate) {
		this.makeDate = makeDate;
	}
	
	@ExcelField(title="制单人编码", fieldType=User.class, value="makeEmpid.no", align=2, sort=9)
	public User getMakeEmpid() {
		return makeEmpid;
	}

	public void setMakeEmpid(User makeEmpid) {
		this.makeEmpid = makeEmpid;
	}
	
	@ExcelField(title="制单人姓名", align=2, sort=10)
	public String getMakeEmpName() {
		return makeEmpName;
	}

	public void setMakeEmpName(String makeEmpName) {
		this.makeEmpName = makeEmpName;
	}
	
	@ExcelField(title="单据状态", dictType="billflag", align=2, sort=11)
	public String getBillFlag() {
		return billFlag;
	}

	public void setBillFlag(String billFlag) {
		this.billFlag = billFlag;
	}
	
	@ExcelField(title="仓库编码", fieldType=WareHouse.class, value="wareId.wareID", align=2, sort=12)
	public WareHouse getWareId() {
		return wareId;
	}

	public void setWareId(WareHouse wareId) {
		this.wareId = wareId;
	}
	
	@ExcelField(title="仓库名称", align=2, sort=13)
	public String getWareName() {
		return wareName;
	}

	public void setWareName(String wareName) {
		this.wareName = wareName;
	}
	
	@ExcelField(title="库管员编码", fieldType=Employee.class, value="wareEmpId.empID", align=2, sort=14)
	public Employee getWareEmpId() {
		return wareEmpId;
	}

	public void setWareEmpId(Employee wareEmpId) {
		this.wareEmpId = wareEmpId;
	}
	
	@ExcelField(title="库管员姓名", align=2, sort=15)
	public String getWareEmpName() {
		return wareEmpName;
	}

	public void setWareEmpName(String wareEmpName) {
		this.wareEmpName = wareEmpName;
	}
	
	@ExcelField(title="复验员编码", fieldType=User.class, value="checkEmpId.no", align=2, sort=16)
	public User getCheckEmpId() {
		return checkEmpId;
	}

	public void setCheckEmpId(User checkEmpId) {
		this.checkEmpId = checkEmpId;
	}
	
	@ExcelField(title="复验员姓名", align=2, sort=17)
	public String getCheckEmpName() {
		return checkEmpName;
	}

	public void setCheckEmpName(String checkEmpName) {
		this.checkEmpName = checkEmpName;
	}
	
	@ExcelField(title="质检标志", align=2, sort=18)
	public String getQmsFlag() {
		return qmsFlag;
	}

	public void setQmsFlag(String qmsFlag) {
		this.qmsFlag = qmsFlag;
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
		this.endMakeDate = endMakeDate;
	}
		
	public List<NewinvCheckDetail> getNewinvCheckDetailList() {
		return newinvCheckDetailList;
	}

	public void setNewinvCheckDetailList(List<NewinvCheckDetail> newinvCheckDetailList) {
		this.newinvCheckDetailList = newinvCheckDetailList;
	}

	public List<NewinvCheckDetailCode> getNewinvCheckDetailCodeList() {
		return newinvCheckDetailCodeList;
	}

	public void setNewinvCheckDetailCodeList(List<NewinvCheckDetailCode> newinvCheckDetailCodeList) {
		this.newinvCheckDetailCodeList = newinvCheckDetailCodeList;
	}
}