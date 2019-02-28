/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.salemanage.linkman.entity;

import com.hqu.modules.basedata.accounttype.entity.AccountType;
import com.hqu.modules.salemanage.accounttype.entity.AccountType2;
import com.hqu.modules.salemanage.areadef.entity.AreaDef;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.URL;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.List;
import com.google.common.collect.Lists;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 联系人维护Entity
 * @author hzm
 * @version 2018-05-13
 */
public class Salaccount extends DataEntity<Salaccount> {
	
	private static final long serialVersionUID = 1L;
	private String accountCode;		// 企业编码
	private String accountName;		// 企业名称
	private String accountSname;		// 简称
	private AccountType accType;		// 企业类型编码
	private String accTypeNameRu;		// 企业类型名称
	private AreaDef areaCode;		// 地区编码
	private String areaName;		// 地区名称
	private String accountProp;		// 性质
	private String levelCode;		// 级别编码
	private String accountMgr;		// 法人
	private AccountType2 subTypeCode;		// 类别编码
	private String subTypeName;		// 类别名称
	private String netAsset;		// 净资产
	private String supHigherUp;		// 上级部门
	private String accountAddr;		// 联系地址
	private String postCode;		// 邮编
	private String mainProd;		// 主要产品
	private String regCap;		// 注册资本
	private String empDim;		// 员工规模
	private String recentStat;		// 近期经营状况
	private String taxCode;		// 纳税号
	private String telNum;		// 电话
	private String reg;		// 电话挂号
	private String faxNum;		// 传真
	private String email;		// 电子邮件
	private String webAddr;		// Web地址
	private String revPerson;		// 收货人
	private String bankName;		// 开户行
	private String bankAccount;		// 银行账户
	private String accountIndu;		// 所属行业
	private String busiRev;		// 年营业收入
	private Date inputDate;		// 录入日期
	private String makeEmpid;		// 编制人编号
	private String makeEmpname;		// 编制人姓名
	private String supEvalation;		// 综合评估
	private String supReputationSum;		// 信誉额度
	private String supNotes;		// 备注
	private String state;		// 供货商状态
	private String subIntr;		// 供货情况简介
	private String supFlag;		// 标志位（接口用）
	private String data1;		// 专用保留字段
	private String flag;		// 有效标志
	private Double baseScore;		// 基础得分
	private Double tradeScore;		// 交易得分
	private Double totalScore;		// 总分
	private String result;		// 评价结果
	private String fncId;		// 对应财务编码
	private Double supFloat;		// 流动资金
	private String supTax;		// 能否开全额增值税发票
	private String salOrgzCode;		// 销售组织编码
	private List<SalLinkMan> linkManList = Lists.newArrayList();		// 子表列表
	
	public Salaccount() {
		super();
	}

	public Salaccount(String id){
		super(id);
	}

	@ExcelField(title="企业编码", align=2, sort=7)
	public String getAccountCode() {
		return accountCode;
	}

	public void setAccountCode(String accountCode) {
		this.accountCode = accountCode;
	}
	
	@ExcelField(title="企业名称", align=2, sort=8)
	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	
	@ExcelField(title="简称", align=2, sort=9)
	public String getAccountSname() {
		return accountSname;
	}

	public void setAccountSname(String accountSname) {
		this.accountSname = accountSname;
	}
	
	@ExcelField(title="企业类型编码", fieldType=AccountType.class, value="accType.accTypeName", align=2, sort=10)
	public AccountType getAccType() {
		return accType;
	}

	public void setAccType(AccountType accType) {
		this.accType = accType;
	}
	
	@ExcelField(title="企业类型名称", align=2, sort=11)
	public String getAccTypeNameRu() {
		return accTypeNameRu;
	}

	public void setAccTypeNameRu(String accTypeNameRu) {
		this.accTypeNameRu = accTypeNameRu;
	}
	
	@ExcelField(title="地区编码", align=2, sort=12)
	public AreaDef getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(AreaDef areaCode) {
		this.areaCode = areaCode;
	}
	
	@ExcelField(title="地区名称", align=2, sort=13)
	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}
	
	@ExcelField(title="性质", align=2, sort=14)
	public String getAccountProp() {
		return accountProp;
	}

	public void setAccountProp(String accountProp) {
		this.accountProp = accountProp;
	}
	
	@ExcelField(title="级别编码", align=2, sort=15)
	public String getLevelCode() {
		return levelCode;
	}

	public void setLevelCode(String levelCode) {
		this.levelCode = levelCode;
	}
	
	@ExcelField(title="法人", align=2, sort=16)
	public String getAccountMgr() {
		return accountMgr;
	}

	public void setAccountMgr(String accountMgr) {
		this.accountMgr = accountMgr;
	}
	
	@ExcelField(title="类别编码", align=2, sort=17)
	public AccountType2 getSubTypeCode() {
		return subTypeCode;
	}

	public void setSubTypeCode(AccountType2 subTypeCode) {
		this.subTypeCode = subTypeCode;
	}
	
	@ExcelField(title="类别名称", align=2, sort=18)
	public String getSubTypeName() {
		return subTypeName;
	}

	public void setSubTypeName(String subTypeName) {
		this.subTypeName = subTypeName;
	}
	
	@ExcelField(title="净资产", align=2, sort=19)
	public String getNetAsset() {
		return netAsset;
	}

	public void setNetAsset(String netAsset) {
		this.netAsset = netAsset;
	}
	
	@ExcelField(title="上级部门", align=2, sort=20)
	public String getSupHigherUp() {
		return supHigherUp;
	}

	public void setSupHigherUp(String supHigherUp) {
		this.supHigherUp = supHigherUp;
	}
	
	@ExcelField(title="联系地址", align=2, sort=21)
	public String getAccountAddr() {
		return accountAddr;
	}

	public void setAccountAddr(String accountAddr) {
		this.accountAddr = accountAddr;
	}
	
	@ExcelField(title="邮编", align=2, sort=22)
	public String getPostCode() {
		return postCode;
	}

	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}
	
	@ExcelField(title="主要产品", align=2, sort=23)
	public String getMainProd() {
		return mainProd;
	}

	public void setMainProd(String mainProd) {
		this.mainProd = mainProd;
	}
	
	@ExcelField(title="注册资本", align=2, sort=24)
	public String getRegCap() {
		return regCap;
	}

	public void setRegCap(String regCap) {
		this.regCap = regCap;
	}
	
	@ExcelField(title="员工规模", align=2, sort=25)
	public String getEmpDim() {
		return empDim;
	}

	public void setEmpDim(String empDim) {
		this.empDim = empDim;
	}
	
	@ExcelField(title="近期经营状况", align=2, sort=26)
	public String getRecentStat() {
		return recentStat;
	}

	public void setRecentStat(String recentStat) {
		this.recentStat = recentStat;
	}
	
	@ExcelField(title="纳税号", align=2, sort=27)
	public String getTaxCode() {
		return taxCode;
	}

	public void setTaxCode(String taxCode) {
		this.taxCode = taxCode;
	}
	
	@ExcelField(title="电话", align=2, sort=28)
	public String getTelNum() {
		return telNum;
	}

	public void setTelNum(String telNum) {
		this.telNum = telNum;
	}
	
	@ExcelField(title="电话挂号", align=2, sort=29)
	public String getReg() {
		return reg;
	}

	public void setReg(String reg) {
		this.reg = reg;
	}
	
	@ExcelField(title="传真", align=2, sort=30)
	public String getFaxNum() {
		return faxNum;
	}

	public void setFaxNum(String faxNum) {
		this.faxNum = faxNum;
	}
	
	@ExcelField(title="电子邮件", align=2, sort=31)
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	@ExcelField(title="Web地址", align=2, sort=32)
	public String getWebAddr() {
		return webAddr;
	}

	public void setWebAddr(String webAddr) {
		this.webAddr = webAddr;
	}
	
	@ExcelField(title="收货人", align=2, sort=33)
	public String getRevPerson() {
		return revPerson;
	}

	public void setRevPerson(String revPerson) {
		this.revPerson = revPerson;
	}
	
	@ExcelField(title="开户行", align=2, sort=34)
	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	
	@ExcelField(title="银行账户", align=2, sort=35)
	public String getBankAccount() {
		return bankAccount;
	}

	public void setBankAccount(String bankAccount) {
		this.bankAccount = bankAccount;
	}
	
	@ExcelField(title="所属行业", align=2, sort=36)
	public String getAccountIndu() {
		return accountIndu;
	}

	public void setAccountIndu(String accountIndu) {
		this.accountIndu = accountIndu;
	}
	
	@ExcelField(title="年营业收入", align=2, sort=37)
	public String getBusiRev() {
		return busiRev;
	}

	public void setBusiRev(String busiRev) {
		this.busiRev = busiRev;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="录入日期", align=2, sort=38)
	public Date getInputDate() {
		return inputDate;
	}

	public void setInputDate(Date inputDate) {
		this.inputDate = inputDate;
	}
	
	@ExcelField(title="编制人编号", align=2, sort=39)
	public String getMakeEmpid() {
		return makeEmpid;
	}

	public void setMakeEmpid(String makeEmpid) {
		this.makeEmpid = makeEmpid;
	}
	
	@ExcelField(title="编制人姓名", align=2, sort=40)
	public String getMakeEmpname() {
		return makeEmpname;
	}

	public void setMakeEmpname(String makeEmpname) {
		this.makeEmpname = makeEmpname;
	}
	
	@ExcelField(title="综合评估", align=2, sort=41)
	public String getSupEvalation() {
		return supEvalation;
	}

	public void setSupEvalation(String supEvalation) {
		this.supEvalation = supEvalation;
	}
	
	@ExcelField(title="信誉额度", align=2, sort=42)
	public String getSupReputationSum() {
		return supReputationSum;
	}

	public void setSupReputationSum(String supReputationSum) {
		this.supReputationSum = supReputationSum;
	}
	
	@ExcelField(title="备注", align=2, sort=43)
	public String getSupNotes() {
		return supNotes;
	}

	public void setSupNotes(String supNotes) {
		this.supNotes = supNotes;
	}
	
	@ExcelField(title="供货商状态", align=2, sort=44)
	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
	
	@ExcelField(title="供货情况简介", align=2, sort=45)
	public String getSubIntr() {
		return subIntr;
	}

	public void setSubIntr(String subIntr) {
		this.subIntr = subIntr;
	}
	
	@ExcelField(title="标志位（接口用）", align=2, sort=46)
	public String getSupFlag() {
		return supFlag;
	}

	public void setSupFlag(String supFlag) {
		this.supFlag = supFlag;
	}
	
	@ExcelField(title="专用保留字段", align=2, sort=47)
	public String getData1() {
		return data1;
	}

	public void setData1(String data1) {
		this.data1 = data1;
	}
	
	@ExcelField(title="有效标志", align=2, sort=48)
	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}
	
	@ExcelField(title="基础得分", align=2, sort=49)
	public Double getBaseScore() {
		return baseScore;
	}

	public void setBaseScore(Double baseScore) {
		this.baseScore = baseScore;
	}
	
	@ExcelField(title="交易得分", align=2, sort=50)
	public Double getTradeScore() {
		return tradeScore;
	}

	public void setTradeScore(Double tradeScore) {
		this.tradeScore = tradeScore;
	}
	
	@ExcelField(title="总分", align=2, sort=51)
	public Double getTotalScore() {
		return totalScore;
	}

	public void setTotalScore(Double totalScore) {
		this.totalScore = totalScore;
	}
	
	@ExcelField(title="评价结果", align=2, sort=52)
	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}
	
	@ExcelField(title="对应财务编码", align=2, sort=53)
	public String getFncId() {
		return fncId;
	}

	public void setFncId(String fncId) {
		this.fncId = fncId;
	}
	
	@ExcelField(title="流动资金", align=2, sort=54)
	public Double getSupFloat() {
		return supFloat;
	}

	public void setSupFloat(Double supFloat) {
		this.supFloat = supFloat;
	}
	
	@ExcelField(title="能否开全额增值税发票", align=2, sort=55)
	public String getSupTax() {
		return supTax;
	}

	public void setSupTax(String supTax) {
		this.supTax = supTax;
	}
	
	@ExcelField(title="销售组织编码", align=2, sort=56)
	public String getSalOrgzCode() {
		return salOrgzCode;
	}

	public void setSalOrgzCode(String salOrgzCode) {
		this.salOrgzCode = salOrgzCode;
	}
	
	public List<SalLinkMan> getLinkManList() {
		return linkManList;
	}

	public void setLinkManList(List<SalLinkMan> linkManList) {
		this.linkManList = linkManList;
	}
}