/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.salemanage.contractrtn.entity;

import com.hqu.modules.basedata.account.entity.Account;
import javax.validation.constraints.NotNull;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.jeeplus.modules.sys.entity.User;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 销售回款管理Entity
 * @author liujiachen
 * @version 2018-07-09
 */
public class ContractRtn extends DataEntity<ContractRtn> {
	
	private static final long serialVersionUID = 1L;
	private String rtnBillCode;		// 回款单编码
	private String contractCode;		// 合同编码
	private String rtnSum;		// 回款金额
	private Account account;		// 客户编码
	private String accountName;		// 客户名称
	private Date inputDate;		// 录入日期
	private Date beginInputDate;		// 开始 录入日期
	private Date endInputDate;		// 结束 录入日期
	
	public ContractRtn() {
		super();
	}

	public ContractRtn(String id){
		super(id);
	}

	@ExcelField(title="回款单编码", align=2, sort=6)
	public String getRtnBillCode() {
		return rtnBillCode;
	}

	public void setRtnBillCode(String rtnBillCode) {
		this.rtnBillCode = rtnBillCode;
	}
	
	@ExcelField(title="合同编码", align=2, sort=7)
	public String getContractCode() {
		return contractCode;
	}

	public void setContractCode(String contractCode) {
		this.contractCode = contractCode;
	}
	
	@ExcelField(title="回款金额", align=2, sort=8)
	public String getRtnSum() {
		return rtnSum;
	}

	public void setRtnSum(String rtnSum) {
		this.rtnSum = rtnSum;
	}
	
	@NotNull(message="客户编码不能为空")
	@ExcelField(title="客户编码", align=2, sort=9)
	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}
	
	@ExcelField(title="客户名称", align=2, sort=10)
	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="录入日期", align=2, sort=11)
	public Date getInputDate() {
		return inputDate;
	}

	public void setInputDate(Date inputDate) {
		this.inputDate = inputDate;
	}

	public Date getBeginInputDate() {
		return beginInputDate;
	}

	public void setBeginInputDate(Date beginInputDate) {
		this.beginInputDate = beginInputDate;
	}
	
	public Date getEndInputDate() {
		return endInputDate;
	}

	public void setEndInputDate(Date endInputDate) {
		this.endInputDate = endInputDate;
	}

}