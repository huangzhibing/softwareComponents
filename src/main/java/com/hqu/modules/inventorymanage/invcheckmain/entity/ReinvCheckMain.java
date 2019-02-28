/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.inventorymanage.invcheckmain.entity;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.List;
import com.google.common.collect.Lists;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 超期复检主表Entity
 * @author syc代建
 * @version 2018-05-28
 */
public class ReinvCheckMain extends DataEntity<ReinvCheckMain> {
	
	private static final long serialVersionUID = 1L;
	private String billNum;		// 单据号
	private String periodId;		// 核算期
	private Date makeDate;		// 制单日期
	private String makeEmpId;		// 制单人编码
	private String makeEmpName;		// 制单人名称
	private String billFlag;		// 单据状态
	private String wareId;		// 仓库编码
	private String wareName;		// 仓库名称
	private String wareEmpId;		// 库管员编码
	private String wareEmpName;		// 库管员姓名
	private String checkEmpId;		// 复验员编码
	private String checkEmpName;		// 复验员姓名
	private String qmsFlag;		// 质检标志(1已质检、0未质检、2正在质检)
	private String beginBillNum;		// 开始 单据号
	private String endBillNum;		// 结束 单据号
	private Date beginMakeDate;		// 开始 制单日期
	private Date endMakeDate;		// 结束 制单日期
	private List<ReInvCheckDetail> reInvCheckDetailList = Lists.newArrayList();		// 子表列表
	
	public ReinvCheckMain() {
		super();
	}

	public ReinvCheckMain(String id){
		super(id);
	}

	@ExcelField(title="单据号", align=2, sort=7)
	public String getBillNum() {
		return billNum;
	}

	public void setBillNum(String billNum) {
		this.billNum = billNum;
	}
	
	@ExcelField(title="核算期", align=2, sort=8)
	public String getPeriodId() {
		return periodId;
	}

	public void setPeriodId(String periodId) {
		this.periodId = periodId;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="制单日期", align=2, sort=9)
	public Date getMakeDate() {
		return makeDate;
	}

	public void setMakeDate(Date makeDate) {
		this.makeDate = makeDate;
	}
	
	@ExcelField(title="制单人编码", align=2, sort=10)
	public String getMakeEmpId() {
		return makeEmpId;
	}

	public void setMakeEmpId(String makeEmpId) {
		this.makeEmpId = makeEmpId;
	}
	
	@ExcelField(title="制单人名称", align=2, sort=11)
	public String getMakeEmpName() {
		return makeEmpName;
	}

	public void setMakeEmpName(String makeEmpName) {
		this.makeEmpName = makeEmpName;
	}
	
	@ExcelField(title="单据状态", align=2, sort=12)
	public String getBillFlag() {
		return billFlag;
	}

	public void setBillFlag(String billFlag) {
		this.billFlag = billFlag;
	}
	
	@ExcelField(title="仓库编码", align=2, sort=13)
	public String getWareId() {
		return wareId;
	}

	public void setWareId(String wareId) {
		this.wareId = wareId;
	}
	
	@ExcelField(title="仓库名称", align=2, sort=14)
	public String getWareName() {
		return wareName;
	}

	public void setWareName(String wareName) {
		this.wareName = wareName;
	}
	
	@ExcelField(title="库管员编码", align=2, sort=15)
	public String getWareEmpId() {
		return wareEmpId;
	}

	public void setWareEmpId(String wareEmpId) {
		this.wareEmpId = wareEmpId;
	}
	
	@ExcelField(title="库管员姓名", align=2, sort=16)
	public String getWareEmpName() {
		return wareEmpName;
	}

	public void setWareEmpName(String wareEmpName) {
		this.wareEmpName = wareEmpName;
	}
	
	@ExcelField(title="复验员编码", align=2, sort=17)
	public String getCheckEmpId() {
		return checkEmpId;
	}

	public void setCheckEmpId(String checkEmpId) {
		this.checkEmpId = checkEmpId;
	}
	
	@ExcelField(title="复验员姓名", align=2, sort=18)
	public String getCheckEmpName() {
		return checkEmpName;
	}

	public void setCheckEmpName(String checkEmpName) {
		this.checkEmpName = checkEmpName;
	}
	
	@ExcelField(title="质检标志(1已质检、0未质检、2正在质检)", align=2, sort=19)
	public String getQmsFlag() {
		return qmsFlag;
	}

	public void setQmsFlag(String qmsFlag) {
		this.qmsFlag = qmsFlag;
	}
	
	public String getBeginBillNum() {
		return beginBillNum;
	}

	public void setBeginBillNum(String beginBillNum) {
		this.beginBillNum = beginBillNum;
	}
	
	public String getEndBillNum() {
		return endBillNum;
	}

	public void setEndBillNum(String endBillNum) {
		this.endBillNum = endBillNum;
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
		
	public List<ReInvCheckDetail> getReInvCheckDetailList() {
		return reInvCheckDetailList;
	}

	public void setReInvCheckDetailList(List<ReInvCheckDetail> reInvCheckDetailList) {
		this.reInvCheckDetailList = reInvCheckDetailList;
	}
}