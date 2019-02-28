/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.purchasemanage.archives.entity;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.jeeplus.modules.sys.entity.User;
import javax.validation.constraints.NotNull;
import com.hqu.modules.purchasemanage.group.entity.GroupBuyer;
import com.hqu.modules.purchasemanage.transtype.entity.TranStype;
import com.hqu.modules.purchasemanage.paymode.entity.PayMode;
import com.hqu.modules.purchasemanage.contracttype.entity.ContractType;
import com.hqu.modules.purchasemanage.contractnotesmodel.entity.ContractNotesModel;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 采购合同父表Entity
 * @author luyumiao
 * @version 2018-05-01
 */
public class PurContractMain extends DataEntity<PurContractMain> {
	
	private static final long serialVersionUID = 1L;
	private String billNum;		// 单据号
	private String billType;		// 单据类型
	private Date makeDate;		// 制单日期
	private User user;		// 制单人编号
	private String makeEmpname;		// 制单人名称
	private String makeNotes;		// 制单说明
	private String billStateFlag;		// 单据状态
	private Puraccount Puraccount;		// 供应商编号 父类
	private String supName;		// 供应商名称
	private String supAddress;		// 供应商地址
	private String supManager;		// 供应商法人
	private Double taxRatio;		// 税率
	private GroupBuyer groupBuyer;		// 采购员编号
	private String buyerName;		// 采购员名称
	private String paymentNotes;		// 付款说明
	private Double advancePay;		// 预付额款
	private Date advanceDate;		// 预付日期
	private Date endDate;		// 截止日期
	private TranStype transType;		// 运输方式代码
	private String tranmodeName;		// 运输方式名称
	private String tranpricePayer;		// 运费承担方
	private Double tranprice;		// 运费总额
	private Double goodsSum;		// 不含税总额
	private Double goodsSumTaxed;		// 含税总额
	private String dealNote;		// 合同说明
	private String transContractNum;		// 运输协议号
	private String billNotes;		// 单据说明
	private String contrState;		// 合同状态
	private PayMode payMode;		// 付款方式编码
	private String paymodeName;		// 付款方式名称
	private Date bdate;		// 业务日期
	private String alterFlag;		// 变更标志
	private Double contractNeedSum;		// 合同保证金
	private String userDeptCode;		// 登入用户所在部门编码
	private ContractType contractType;		// 合同类型代码
	private String contypeName;		// 合同类型名称
	private String planMainDetail;		// 计划单号
	private ContractNotesModel contractModel;		// 合同模板编码
	private String contractModelName;		// 合同模板名称
	
	public PurContractMain() {
		super();
	}

	public PurContractMain(String id){
		super(id);
	}

	public PurContractMain(Puraccount Puraccount){
		this.Puraccount = Puraccount;
	}

	@ExcelField(title="单据号", align=2, sort=7)
	public String getBillNum() {
		return billNum;
	}

	public void setBillNum(String billNum) {
		this.billNum = billNum;
	}
	
	@ExcelField(title="单据类型", align=2, sort=8)
	public String getBillType() {
		return billType;
	}

	public void setBillType(String billType) {
		this.billType = billType;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="制单日期", align=2, sort=9)
	public Date getMakeDate() {
		return makeDate;
	}

	public void setMakeDate(Date makeDate) {
		this.makeDate = makeDate;
	}
	
	@NotNull(message="制单人编号不能为空")
	@ExcelField(title="制单人编号", align=2, sort=10)
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	@ExcelField(title="制单人名称", align=2, sort=11)
	public String getMakeEmpname() {
		return makeEmpname;
	}

	public void setMakeEmpname(String makeEmpname) {
		this.makeEmpname = makeEmpname;
	}
	
	@ExcelField(title="制单说明", align=2, sort=12)
	public String getMakeNotes() {
		return makeNotes;
	}

	public void setMakeNotes(String makeNotes) {
		this.makeNotes = makeNotes;
	}
	
	@ExcelField(title="单据状态", align=2, sort=13)
	public String getBillStateFlag() {
		return billStateFlag;
	}

	public void setBillStateFlag(String billStateFlag) {
		this.billStateFlag = billStateFlag;
	}
	
	public Puraccount getPuraccount() {
		return Puraccount;
	}

	public void setPuraccount(Puraccount Puraccount) {
		this.Puraccount = Puraccount;
	}
	
	@ExcelField(title="供应商名称", align=2, sort=15)
	public String getSupName() {
		return supName;
	}

	public void setSupName(String supName) {
		this.supName = supName;
	}
	
	@ExcelField(title="供应商地址", align=2, sort=16)
	public String getSupAddress() {
		return supAddress;
	}

	public void setSupAddress(String supAddress) {
		this.supAddress = supAddress;
	}
	
	@ExcelField(title="供应商法人", align=2, sort=17)
	public String getSupManager() {
		return supManager;
	}

	public void setSupManager(String supManager) {
		this.supManager = supManager;
	}
	
	@NotNull(message="税率不能为空")
	@ExcelField(title="税率", align=2, sort=18)
	public Double getTaxRatio() {
		return taxRatio;
	}

	public void setTaxRatio(Double taxRatio) {
		this.taxRatio = taxRatio;
	}
	
	@NotNull(message="采购员编号不能为空")
	@ExcelField(title="采购员编号", fieldType=GroupBuyer.class, value="groupBuyer.buyername", align=2, sort=19)
	public GroupBuyer getGroupBuyer() {
		return groupBuyer;
	}

	public void setGroupBuyer(GroupBuyer groupBuyer) {
		this.groupBuyer = groupBuyer;
	}
	
	@ExcelField(title="采购员名称", align=2, sort=20)
	public String getBuyerName() {
		return buyerName;
	}

	public void setBuyerName(String buyerName) {
		this.buyerName = buyerName;
	}
	
	@ExcelField(title="付款说明", align=2, sort=21)
	public String getPaymentNotes() {
		return paymentNotes;
	}

	public void setPaymentNotes(String paymentNotes) {
		this.paymentNotes = paymentNotes;
	}
	
	@ExcelField(title="预付额款", align=2, sort=22)
	public Double getAdvancePay() {
		return advancePay;
	}

	public void setAdvancePay(Double advancePay) {
		this.advancePay = advancePay;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="预付日期", align=2, sort=23)
	public Date getAdvanceDate() {
		return advanceDate;
	}

	public void setAdvanceDate(Date advanceDate) {
		this.advanceDate = advanceDate;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="截止日期", align=2, sort=24)
	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
	@ExcelField(title="运输方式代码", dictType="Transtype", align=2, sort=25)
	public TranStype getTransType() {
		return transType;
	}

	public void setTransType(TranStype transType) {
		this.transType = transType;
	}
	
	@ExcelField(title="运输方式名称", dictType="TranstypeName", align=2, sort=26)
	public String getTranmodeName() {
		return tranmodeName;
	}

	public void setTranmodeName(String tranmodeName) {
		this.tranmodeName = tranmodeName;
	}
	
	@ExcelField(title="运费承担方", align=2, sort=27)
	public String getTranpricePayer() {
		return tranpricePayer;
	}

	public void setTranpricePayer(String tranpricePayer) {
		this.tranpricePayer = tranpricePayer;
	}
	
	@ExcelField(title="运费总额", align=2, sort=28)
	public Double getTranprice() {
		return tranprice;
	}

	public void setTranprice(Double tranprice) {
		this.tranprice = tranprice;
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
	
	@ExcelField(title="合同说明", align=2, sort=31)
	public String getDealNote() {
		return dealNote;
	}

	public void setDealNote(String dealNote) {
		this.dealNote = dealNote;
	}
	
	@ExcelField(title="运输协议号", align=2, sort=32)
	public String getTransContractNum() {
		return transContractNum;
	}

	public void setTransContractNum(String transContractNum) {
		this.transContractNum = transContractNum;
	}
	
	@ExcelField(title="单据说明", align=2, sort=33)
	public String getBillNotes() {
		return billNotes;
	}

	public void setBillNotes(String billNotes) {
		this.billNotes = billNotes;
	}
	
	@ExcelField(title="合同状态", align=2, sort=34)
	public String getContrState() {
		return contrState;
	}

	public void setContrState(String contrState) {
		this.contrState = contrState;
	}
	
	@ExcelField(title="付款方式编码", dictType="PayMode", align=2, sort=35)
	public PayMode getPayMode() {
		return payMode;
	}

	public void setPayMode(PayMode payMode) {
		this.payMode = payMode;
	}
	
	@ExcelField(title="付款方式名称", dictType="PayModeName", align=2, sort=36)
	public String getPaymodeName() {
		return paymodeName;
	}

	public void setPaymodeName(String paymodeName) {
		this.paymodeName = paymodeName;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@NotNull(message="业务日期不能为空")
	@ExcelField(title="业务日期", align=2, sort=37)
	public Date getBdate() {
		return bdate;
	}

	public void setBdate(Date bdate) {
		this.bdate = bdate;
	}
	
	@ExcelField(title="变更标志", align=2, sort=38)
	public String getAlterFlag() {
		return alterFlag;
	}

	public void setAlterFlag(String alterFlag) {
		this.alterFlag = alterFlag;
	}
	
	@ExcelField(title="合同保证金", align=2, sort=39)
	public Double getContractNeedSum() {
		return contractNeedSum;
	}

	public void setContractNeedSum(Double contractNeedSum) {
		this.contractNeedSum = contractNeedSum;
	}
	
	@ExcelField(title="登入用户所在部门编码", align=2, sort=40)
	public String getUserDeptCode() {
		return userDeptCode;
	}

	public void setUserDeptCode(String userDeptCode) {
		this.userDeptCode = userDeptCode;
	}
	
	@ExcelField(title="合同类型代码", fieldType=ContractType.class, value="contractType.contypeid", align=2, sort=41)
	public ContractType getContractType() {
		return contractType;
	}

	public void setContractType(ContractType contractType) {
		this.contractType = contractType;
	}
	
	@ExcelField(title="合同类型名称", dictType="ContractTypeName", align=2, sort=42)
	public String getContypeName() {
		return contypeName;
	}

	public void setContypeName(String contypeName) {
		this.contypeName = contypeName;
	}
	
	@ExcelField(title="计划单号", align=2, sort=43)
	public String getPlanMainDetail() {
		return planMainDetail;
	}

	public void setPlanMainDetail(String planMainDetail) {
		this.planMainDetail = planMainDetail;
	}
	
	@ExcelField(title="合同模板编码", fieldType=ContractNotesModel.class, value="contractModel.contractId", align=2, sort=44)
	public ContractNotesModel getContractModel() {
		return contractModel;
	}

	public void setContractModel(ContractNotesModel contractModel) {
		this.contractModel = contractModel;
	}
	
	@ExcelField(title="合同模板名称", align=2, sort=45)
	public String getContractModelName() {
		return contractModelName;
	}

	public void setContractModelName(String contractModelName) {
		this.contractModelName = contractModelName;
	}
	
}