/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.costmanage.cospersonother.entity;

import com.hqu.modules.basedata.team.entity.Team;
import com.hqu.modules.costmanage.cositem.entity.CosItem;
import com.jeeplus.modules.sys.entity.User;
import javax.validation.constraints.NotNull;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 其它工资单据稽核Entity
 * @author hzm
 * @version 2018-09-01
 */
public class CosPersonOther extends DataEntity<CosPersonOther> {
	
	private static final long serialVersionUID = 1L;
	private String billNum;		// 单据号码
	private String periodID;		// 核算期
	private Team personCode;		// 人员代码
	private String personName;		// 人员名称
	private CosItem itemId;		// 科目编码
	private String itemName;		// 科目名称
	private String itemSum;		// 工资
	private String billStatus;		// 单据状态
	private String billMode;		// 单据类型
	private String checkId;		// 稽核人代码
	private String checkDate;		// 稽核日期
	private String checkName;		// 稽核人姓名
	private String makeId;		// 录入人代码
	private String makeDate;		// 录入日期
	private String makeName;		// 录入人姓名
	
	public CosPersonOther() {
		super();
	}

	public CosPersonOther(String id){
		super(id);
	}

	@ExcelField(title="单据号码", align=2, sort=7)
	public String getBillNum() {
		return billNum;
	}

	public void setBillNum(String billNum) {
		this.billNum = billNum;
	}
	
	@ExcelField(title="核算期", align=2, sort=8)
	public String getPeriodID() {
		return periodID;
	}

	public void setPeriodID(String periodID) {
		this.periodID = periodID;
	}
	
	@NotNull(message="人员代码不能为空")
	@ExcelField(title="人员代码", align=2, sort=9)
	public Team getPersonCode() {
		return personCode;
	}

	public void setPersonCode(Team personCode) {
		this.personCode = personCode;
	}

	@ExcelField(title="人员名称", align=2, sort=10)
	public String getPersonName() {
		return personName;
	}

	public void setPersonName(String personName) {
		this.personName = personName;
	}
	
	@ExcelField(title="科目编码", align=2, sort=11)
	public CosItem getItemId() {
		return itemId;
	}

	public void setItemId(CosItem itemId) {
		this.itemId = itemId;
	}
	
	@ExcelField(title="科目名称", align=2, sort=12)
	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	
	@ExcelField(title="工资", align=2, sort=13)
	public String getItemSum() {
		return itemSum;
	}

	public void setItemSum(String itemSum) {
		this.itemSum = itemSum;
	}
	
	@ExcelField(title="单据状态", align=2, sort=14)
	public String getBillStatus() {
		return billStatus;
	}

	public void setBillStatus(String billStatus) {
		this.billStatus = billStatus;
	}
	
	@ExcelField(title="单据类型", align=2, sort=15)
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
	
	@ExcelField(title="稽核日期", align=2, sort=17)
	public String getCheckDate() {
		return checkDate;
	}

	public void setCheckDate(String checkDate) {
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
	
	@ExcelField(title="录入日期", align=2, sort=20)
	public String getMakeDate() {
		return makeDate;
	}

	public void setMakeDate(String makeDate) {
		this.makeDate = makeDate;
	}
	
	@ExcelField(title="录入人姓名", align=2, sort=21)
	public String getMakeName() {
		return makeName;
	}

	public void setMakeName(String makeName) {
		this.makeName = makeName;
	}

}