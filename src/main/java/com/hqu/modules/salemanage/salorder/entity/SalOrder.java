/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.salemanage.salorder.entity;

import com.hqu.modules.basedata.product.entity.Product;
import com.hqu.modules.salemanage.salcontract.entity.Contract;
import javax.validation.constraints.NotNull;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.jeeplus.modules.sys.entity.User;
import java.util.List;
import com.google.common.collect.Lists;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 内部订单Entity
 * @author dongqida
 * @version 2018-05-27
 */
public class SalOrder extends DataEntity<SalOrder> {
	
	private static final long serialVersionUID = 1L;
	private Contract contract;		// 合同编码
	private String orderCode;		// 订单编码
	private Date signDate;		// 签订日期
	private String accountCode;		// 客户编码
	private String accountName;		// 客户名称
	private Date inputDate;		// 制单日期
	private User inputPerson;		// 制单人编码
	private String inputPsnName;		// 制单人姓名
	private String orderStat;		// 订单状态
	private Date sendDate;		// 交货日期
	private Date beginSignDate;		// 开始 签订日期
	private Date endSignDate;		// 结束 签订日期
	private Date beginSendDate;		// 开始 交货日期
	private Date endSendDate;		// 结束 交货日期
	private List<SalOrderAudit> salOrderAuditList = Lists.newArrayList();		// 子表列表
	private List<SalOrderItem> salOrderItemList = Lists.newArrayList();		// 子表列表
	private Product product;
	
	public SalOrder() {
		super();
	}

	public SalOrder(String id){
		super(id);
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	@NotNull(message="合同编码不能为空")
	@ExcelField(title="合同编码", fieldType=Contract.class, value="contract.contractCode", align=2, sort=7)
	public Contract getContract() {
		return contract;
	}

	public void setContract(Contract contract) {
		this.contract = contract;
	}
	
	@ExcelField(title="订单编码", align=2, sort=8)
	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@NotNull(message="签订日期不能为空")
	@ExcelField(title="签订日期", align=2, sort=9)
	public Date getSignDate() {
		return signDate;
	}

	public void setSignDate(Date signDate) {
		this.signDate = signDate;
	}
	
	@ExcelField(title="客户编码", align=2, sort=10)
	public String getAccountCode() {
		return accountCode;
	}

	public void setAccountCode(String accountCode) {
		this.accountCode = accountCode;
	}
	
	@ExcelField(title="客户名称", align=2, sort=11)
	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@NotNull(message="制单日期不能为空")
	@ExcelField(title="制单日期", align=2, sort=12)
	public Date getInputDate() {
		return inputDate;
	}

	public void setInputDate(Date inputDate) {
		this.inputDate = inputDate;
	}
	
	@NotNull(message="制单人编码不能为空")
	@ExcelField(title="制单人编码", align=2, sort=13)
	public User getInputPerson() {
		return inputPerson;
	}

	public void setInputPerson(User inputPerson) {
		this.inputPerson = inputPerson;
	}
	
	@ExcelField(title="制单人姓名", align=2, sort=14)
	public String getInputPsnName() {
		return inputPsnName;
	}

	public void setInputPsnName(String inputPsnName) {
		this.inputPsnName = inputPsnName;
	}
	
	@ExcelField(title="订单状态", dictType="", align=2, sort=15)
	public String getOrderStat() {
		return orderStat;
	}

	public void setOrderStat(String orderStat) {
		this.orderStat = orderStat;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@NotNull(message="交货日期不能为空")
	@ExcelField(title="交货日期", align=2, sort=16)
	public Date getSendDate() {
		return sendDate;
	}

	public void setSendDate(Date sendDate) {
		this.sendDate = sendDate;
	}
	
	public Date getBeginSignDate() {
		return beginSignDate;
	}

	public void setBeginSignDate(Date beginSignDate) {
		this.beginSignDate = beginSignDate;
	}
	
	public Date getEndSignDate() {
		return endSignDate;
	}

	public void setEndSignDate(Date endSignDate) {
		this.endSignDate = endSignDate;
	}
		
	public Date getBeginSendDate() {
		return beginSendDate;
	}

	public void setBeginSendDate(Date beginSendDate) {
		this.beginSendDate = beginSendDate;
	}
	
	public Date getEndSendDate() {
		return endSendDate;
	}

	public void setEndSendDate(Date endSendDate) {
		this.endSendDate = endSendDate;
	}
		
	public List<SalOrderAudit> getSalOrderAuditList() {
		return salOrderAuditList;
	}

	public void setSalOrderAuditList(List<SalOrderAudit> salOrderAuditList) {
		this.salOrderAuditList = salOrderAuditList;
	}
	public List<SalOrderItem> getSalOrderItemList() {
		return salOrderItemList;
	}

	public void setSalOrderItemList(List<SalOrderItem> salOrderItemList) {
		this.salOrderItemList = salOrderItemList;
	}
}