/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.salemanage.accountfile.entity;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.hqu.modules.salemanage.contract.entity.CtrTypeDef;
import com.hqu.modules.basedata.account.entity.Account;
import com.hqu.modules.salemanage.blnctypedef.entity.BlncTypeDef;
import com.jeeplus.modules.sys.entity.User;
import com.hqu.modules.salemanage.paytypedef.entity.PayTypeDef;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 销售合同主表Entity
 * @author hzb
 * @version 2018-05-15
 */
public class Contract extends DataEntity<Contract> {
	
	private static final long serialVersionUID = 1L;
	private String contractCode;		// 合同编码
	private Date signDate;		// 签订日期
	private CtrTypeDef contractType;		// 合同类型编码
	private String contractTypeName;		// 合同类型名称
	private Salaccount account;		// 客户编码 父类
	private String accountName;		// 客户名称
	private BlncTypeDef blncType;		// 结算方式编码
	private String blncTypeName;		// 结算方式名称
	private User person;		// 销售人员编码
	private String personName;		// 销售人员姓名
	private Date inputDate;		// 制单日期
	private User inputPerson;		// 制单人编码
	private String contractStat;		// 合同状态
	private String endReason;		// 结案原因
	private User endPerson;		// 结案人编码
	private String endPsnName;		// 结案人姓名
	private Date endDate;		// 结案日期
	private PayTypeDef payType;		// 付款方式编码
	private String payTypeName;		// 付款方式名称
	private Double taxRatio;		// 税率
	private String tranpricePayer;		// 运费承担方
	private Double transfeeSum;		// 运费总额
	private Double goodsSum;		// 不含税总额
	private Double goodsSumTaxed;		// 含税总额
	private String itemContents;		// 合同文本
	private String paperCtrCode;		// 纸制合同号
	private String praRemark;		// 备注
	private Date beginSignDate;		// 开始 签订日期
	private Date endSignDate;		// 结束 签订日期
	
	public Contract() {
		super();
	}

	public Contract(String id){
		super(id);
	}

	public Contract(Salaccount account){
		this.account = account;
	}

	@ExcelField(title="合同编码", align=2, sort=7)
	public String getContractCode() {
		return contractCode;
	}

	public void setContractCode(String contractCode) {
		this.contractCode = contractCode;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="签订日期", align=2, sort=8)
	public Date getSignDate() {
		return signDate;
	}

	public void setSignDate(Date signDate) {
		this.signDate = signDate;
	}
	
	@ExcelField(title="合同类型编码", fieldType=CtrTypeDef.class, value="", align=2, sort=9)
	public CtrTypeDef getContractType() {
		return contractType;
	}

	public void setContractType(CtrTypeDef contractType) {
		this.contractType = contractType;
	}
	
	@ExcelField(title="合同类型名称", align=2, sort=10)
	public String getContractTypeName() {
		return contractTypeName;
	}

	public void setContractTypeName(String contractTypeName) {
		this.contractTypeName = contractTypeName;
	}
	
	public Salaccount getAccount() {
		return account;
	}

	public void setAccount(Salaccount account) {
		this.account = account;
	}
	
	@ExcelField(title="客户名称", align=2, sort=12)
	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	
	@ExcelField(title="结算方式编码", fieldType=BlncTypeDef.class, value="", align=2, sort=13)
	public BlncTypeDef getBlncType() {
		return blncType;
	}

	public void setBlncType(BlncTypeDef blncType) {
		this.blncType = blncType;
	}
	
	@ExcelField(title="结算方式名称", align=2, sort=14)
	public String getBlncTypeName() {
		return blncTypeName;
	}

	public void setBlncTypeName(String blncTypeName) {
		this.blncTypeName = blncTypeName;
	}
	
	@ExcelField(title="销售人员编码", fieldType=User.class, value="", align=2, sort=15)
	public User getPerson() {
		return person;
	}

	public void setPerson(User person) {
		this.person = person;
	}
	
	@ExcelField(title="销售人员姓名", align=2, sort=16)
	public String getPersonName() {
		return personName;
	}

	public void setPersonName(String personName) {
		this.personName = personName;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="制单日期", align=2, sort=17)
	public Date getInputDate() {
		return inputDate;
	}

	public void setInputDate(Date inputDate) {
		this.inputDate = inputDate;
	}
	
	@ExcelField(title="制单人编码", fieldType=User.class, value="", align=2, sort=18)
	public User getInputPerson() {
		return inputPerson;
	}

	public void setInputPerson(User inputPerson) {
		this.inputPerson = inputPerson;
	}
	
	@ExcelField(title="合同状态", align=2, sort=19)
	public String getContractStat() {
		return contractStat;
	}

	public void setContractStat(String contractStat) {
		this.contractStat = contractStat;
	}
	
	@ExcelField(title="结案原因", align=2, sort=20)
	public String getEndReason() {
		return endReason;
	}

	public void setEndReason(String endReason) {
		this.endReason = endReason;
	}
	
	@ExcelField(title="结案人编码", fieldType=User.class, value="", align=2, sort=21)
	public User getEndPerson() {
		return endPerson;
	}

	public void setEndPerson(User endPerson) {
		this.endPerson = endPerson;
	}
	
	@ExcelField(title="结案人姓名", align=2, sort=22)
	public String getEndPsnName() {
		return endPsnName;
	}

	public void setEndPsnName(String endPsnName) {
		this.endPsnName = endPsnName;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="结案日期", align=2, sort=23)
	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
	@ExcelField(title="付款方式编码", fieldType=PayTypeDef.class, value="", align=2, sort=24)
	public PayTypeDef getPayType() {
		return payType;
	}

	public void setPayType(PayTypeDef payType) {
		this.payType = payType;
	}
	
	@ExcelField(title="付款方式名称", align=2, sort=25)
	public String getPayTypeName() {
		return payTypeName;
	}

	public void setPayTypeName(String payTypeName) {
		this.payTypeName = payTypeName;
	}
	
	@ExcelField(title="税率", align=2, sort=26)
	public Double getTaxRatio() {
		return taxRatio;
	}

	public void setTaxRatio(Double taxRatio) {
		this.taxRatio = taxRatio;
	}
	
	@ExcelField(title="运费承担方", align=2, sort=27)
	public String getTranpricePayer() {
		return tranpricePayer;
	}

	public void setTranpricePayer(String tranpricePayer) {
		this.tranpricePayer = tranpricePayer;
	}
	
	@ExcelField(title="运费总额", align=2, sort=28)
	public Double getTransfeeSum() {
		return transfeeSum;
	}

	public void setTransfeeSum(Double transfeeSum) {
		this.transfeeSum = transfeeSum;
	}
	
	@ExcelField(title="不含税总额", align=2, sort=29)
	public Double getGoodsSum() {
		return goodsSum;
	}

	public void setGoodsSum(Double goodsSum) {
		this.goodsSum = goodsSum;
	}
	
	@ExcelField(title="含税总额", align=2, sort=30)
	public Double getGoodsSumTaxed() {
		return goodsSumTaxed;
	}

	public void setGoodsSumTaxed(Double goodsSumTaxed) {
		this.goodsSumTaxed = goodsSumTaxed;
	}
	
	@ExcelField(title="合同文本", align=2, sort=31)
	public String getItemContents() {
		return itemContents;
	}

	public void setItemContents(String itemContents) {
		this.itemContents = itemContents;
	}
	
	@ExcelField(title="纸制合同号", align=2, sort=32)
	public String getPaperCtrCode() {
		return paperCtrCode;
	}

	public void setPaperCtrCode(String paperCtrCode) {
		this.paperCtrCode = paperCtrCode;
	}
	
	@ExcelField(title="备注", align=2, sort=33)
	public String getPraRemark() {
		return praRemark;
	}

	public void setPraRemark(String praRemark) {
		this.praRemark = praRemark;
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
		
}