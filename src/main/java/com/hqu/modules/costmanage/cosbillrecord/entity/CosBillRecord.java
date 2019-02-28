/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.costmanage.cosbillrecord.entity;

import com.jeeplus.modules.sys.entity.User;

import com.jeeplus.core.persistence.DataEntity;
import com.hqu.modules.costmanage.billingrule.entity.CosBillRule;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 材料单据稽核Entity
 * @author zz
 * @version 2018-08-29
 */
public class CosBillRecord extends DataEntity<CosBillRecord> {
	
	private static final long serialVersionUID = 1L;
	private String corBillCatalog;		// 对应单据类型
	private String corBillNum;		// 对应的单据号
	private String cosBillNum;		// 成本单据号
	private String cosBillNumStatus;		// 单据处理状态
	private User checkId;		// 稽核人代码
	private String checkDate;		// 稽核日期
	private String checkName;		// 稽核人姓名
	private CosBillRule cosBillRule;  //获取材料单据对应制单规则
	
	public CosBillRecord() {
		super();
	}

	public CosBillRecord(String id){
		super(id);
	}

	@ExcelField(title="对应单据类型", align=2, sort=7)
	public String getCorBillCatalog() {
		return corBillCatalog;
	}

	public void setCorBillCatalog(String corBillCatalog) {
		this.corBillCatalog = corBillCatalog;
	}
	
	@ExcelField(title="对应的单据号", align=2, sort=8)
	public String getCorBillNum() {
		return corBillNum;
	}

	public void setCorBillNum(String corBillNum) {
		this.corBillNum = corBillNum;
	}
	
	@ExcelField(title="成本单据号", align=2, sort=9)
	public String getCosBillNum() {
		return cosBillNum;
	}

	public void setCosBillNum(String cosBillNum) {
		this.cosBillNum = cosBillNum;
	}
	
	@ExcelField(title="单据处理状态", align=2, sort=10)
	public String getCosBillNumStatus() {
		return cosBillNumStatus;
	}

	public void setCosBillNumStatus(String cosBillNumStatus) {
		this.cosBillNumStatus = cosBillNumStatus;
	}
	
	@ExcelField(title="稽核人代码", align=2, sort=11)
	public User getCheckId() {
		return checkId;
	}

	public void setCheckId(User checkId) {
		this.checkId = checkId;
	}
	
	@ExcelField(title="稽核日期", align=2, sort=12)
	public String getCheckDate() {
		return checkDate;
	}

	public void setCheckDate(String checkDate) {
		this.checkDate = checkDate;
	}
	
	@ExcelField(title="稽核人姓名", align=2, sort=13)
	public String getCheckName() {
		return checkName;
	}

	public void setCheckName(String checkName) {
		this.checkName = checkName;
	}

	public CosBillRule getCosBillRule() {
		return cosBillRule;
	}

	public void setCosBillRule(CosBillRule cosBillRule) {
		this.cosBillRule = cosBillRule;
	}
	
}