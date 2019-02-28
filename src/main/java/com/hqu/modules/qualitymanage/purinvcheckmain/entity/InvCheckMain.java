/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.qualitymanage.purinvcheckmain.entity;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.common.collect.Lists;
import com.hqu.modules.basedata.item.entity.Item;
import com.hqu.modules.purchasemanage.contracttype.entity.ContractType;
import com.hqu.modules.purchasemanage.group.entity.GroupBuyer;
import com.hqu.modules.purchasemanage.plantype.entity.PlanType;
import com.jeeplus.common.utils.excel.annotation.ExcelField;
import com.jeeplus.core.persistence.DataEntity;

/**
 * 入库通知单Entity
 * @author 张铮
 * @version 2018-04-21
 */
public class InvCheckMain extends DataEntity<InvCheckMain> {
	
	private static final long serialVersionUID = 1L;
	private String billnum;		// 单据编号
	private String billType;		// 单据类型
	private String periodId;		// 核算期
	private Date makeDate;		// 制单日期
	private String makeEmpid;		// 制单人编号
	private String makeEmpname;		// 制单人名称
	private String makeNotes;		// 制单说明
	private String billStateFlag;		// 单据标志
	private String buyerId;		// 采购员编号
	private GroupBuyer groupBuyer;		// 采购员编号
	private String buyerName;		// 采购员名称
	private String wareEmpid;		// 库管员编号
	private String wareEmpname;		// 库管员名称
	private Date arriveDate;		// 到货日期
	private String supId;		// 供应商编号
	private String supName;		// 供应商名称

	private String supCode;     //供应商编码
	
	private String supAddress;		// 供应商地址
	private String invoiceNum;		// 发票号
	private String wareId;		// 仓库编号
	private String wareName;		// 仓库名称
	private String notes;		// 备注
	private String invFlag;		// 仓库处理标志
	private String thFlag;		// 估价标记
	private String carrierId;		// 承运商编号
	private String carrierName;		// 承运商名称
	private String invoiceNumCar;		// 承运发票号
	private Date awayDate;		// 发货日期
	private Double tranpriceSum;		// 运费合计
	private Double priceSum;		// 金额合计
	private Double priceSumTaxed;		// 含税总金额
	private Double priceSumSubtotal;		// 小计金额
	private String payStateFlag;		// 付款标志
	private String payStateNotes;		// 付款说明
	private String inPayaccFlag;		// 预付标志
	private String orderOrContract;		// 单据来源
	private String qmsFlag;		// 质检标志
	private String ioType;		// 入库类型
	private String ioFlag;		// 入库标志
	private String ioDesc;		// 入库类型描述
	private String depId;		// 部门编码
	private String deptName;		// 部门名称
	private Date bdate;		// 业务日期
	private String userDeptCode;		// 登陆人所在部门编码
	private String billNotes;		// 单据说明
	private String billState;		// 单据状态
	private Date beginMakeDate;		// 开始 制单日期
	private Date endMakeDate;		// 结束 制单日期
	private Date beginBdate;		// 开始 业务日期
	private Date endBdate;		// 结束 业务日期
	private Date beginArriveDate;
	private Date endArriveDate;
	
	private Item mainItem;
	private String mainItemName;
	private String mainItemSpecmodel;
	private List<InvCheckDetail> invCheckDetailList = Lists.newArrayList();		// 子表列表
	private List<InvCheckDetailCode> invCheckDetailCodeList = Lists.newArrayList();		// 子表列表
	private String itemSource;		//	物料来源
	private String planId;//计划单编号
	private String planTypeName;   //计划单类型名称	
	private String contractId;   //合同单编号
	private String contractName;		//合同单类型名称
	
	private String itemId;   //物料编号关联查询
	private String itemName;		//物料名称关联查询
	private double taxRatio;		//税率
	private String taxRatioNew;  //提供格式转换
	
	private String backReason; //退货原因

	public InvCheckMain() {
		super();
	}

	public InvCheckMain(String id){
		super(id);
	}

	@ExcelField(title="单据编号", align=2, sort=7)
	public String getBillnum() {
		return billnum;
	}

	public void setBillnum(String billnum) {
		this.billnum = billnum;
	}
	
	@ExcelField(title="单据类型", align=2, sort=8)
	public String getBillType() {
		return billType;
	}

	public void setBillType(String billType) {
		this.billType = billType;
	}
	
	@ExcelField(title="核算期", align=2, sort=9)
	public String getPeriodId() {
		return periodId;
	}

	public void setPeriodId(String periodId) {
		this.periodId = periodId;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="制单日期", align=2, sort=10)
	public Date getMakeDate() {
		return makeDate;
	}

	public void setMakeDate(Date makeDate) {
		this.makeDate = makeDate;
	}
	
	@ExcelField(title="制单人编号", align=2, sort=11)
	public String getMakeEmpid() {
		return makeEmpid;
	}

	public void setMakeEmpid(String makeEmpid) {
		this.makeEmpid = makeEmpid;
	}
	
	@ExcelField(title="制单人名称", align=2, sort=12)
	public String getMakeEmpname() {
		return makeEmpname;
	}

	public void setMakeEmpname(String makeEmpname) {
		this.makeEmpname = makeEmpname;
	}
	
	@ExcelField(title="制单说明", align=2, sort=13)
	public String getMakeNotes() {
		return makeNotes;
	}

	public void setMakeNotes(String makeNotes) {
		this.makeNotes = makeNotes;
	}
	
	@ExcelField(title="单据标志", align=2, sort=14)
	public String getBillStateFlag() {
		return billStateFlag;
	}

	public void setBillStateFlag(String billStateFlag) {
		this.billStateFlag = billStateFlag;
	}
	
//	@ExcelField(title="采购员编号", align=2, sort=15)
//	public String getBuyerId() {
//		return buyerId;
//	}
//
//	public void setBuyerId(String buyerId) {
//		this.buyerId = buyerId;
//	}
	
	@ExcelField(title="采购员名称", align=2, sort=16)
	public String getBuyerName() {
		return buyerName;
	}

	public void setBuyerName(String buyerName) {
		this.buyerName = buyerName;
	}
	
	@ExcelField(title="库管员编号", align=2, sort=17)
	public String getWareEmpid() {
		return wareEmpid;
	}

	public void setWareEmpid(String wareEmpid) {
		this.wareEmpid = wareEmpid;
	}
	
	@ExcelField(title="库管员名称", align=2, sort=18)
	public String getWareEmpname() {
		return wareEmpname;
	}

	public void setWareEmpname(String wareEmpname) {
		this.wareEmpname = wareEmpname;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="到货日期", align=2, sort=19)
	public Date getArriveDate() {
		return arriveDate;
	}

	public void setArriveDate(Date arriveDate) {
		this.arriveDate = arriveDate;
	}
	
	@ExcelField(title="供应商编号", align=2, sort=20)
	public String getSupId() {
		return supId;
	}

	public void setSupId(String supId) {
		this.supId = supId;
	}
	
	@ExcelField(title="供应商名称", align=2, sort=21)
	public String getSupName() {
		return supName;
	}

	public void setSupName(String supName) {
		this.supName = supName;
	}
	
	@ExcelField(title="供应商地址", align=2, sort=22)
	public String getSupAddress() {
		return supAddress;
	}

	public void setSupAddress(String supAddress) {
		this.supAddress = supAddress;
	}
	
	@ExcelField(title="发票号", align=2, sort=23)
	public String getInvoiceNum() {
		return invoiceNum;
	}

	public void setInvoiceNum(String invoiceNum) {
		this.invoiceNum = invoiceNum;
	}
	
	@ExcelField(title="仓库编号", align=2, sort=24)
	public String getWareId() {
		return wareId;
	}

	public void setWareId(String wareId) {
		this.wareId = wareId;
	}
	
	@ExcelField(title="仓库名称", align=2, sort=25)
	public String getWareName() {
		return wareName;
	}

	public void setWareName(String wareName) {
		this.wareName = wareName;
	}
	
	@ExcelField(title="备注", align=2, sort=26)
	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}
	
	@ExcelField(title="仓库处理标志", align=2, sort=27)
	public String getInvFlag() {
		return invFlag;
	}

	public void setInvFlag(String invFlag) {
		this.invFlag = invFlag;
	}
	
	@ExcelField(title="估价标记", align=2, sort=28)
	public String getThFlag() {
		return thFlag;
	}

	public void setThFlag(String thFlag) {
		this.thFlag = thFlag;
	}
	
	@ExcelField(title="承运商编号", align=2, sort=29)
	public String getCarrierId() {
		return carrierId;
	}

	public void setCarrierId(String carrierId) {
		this.carrierId = carrierId;
	}
	
	@ExcelField(title="承运商名称", align=2, sort=30)
	public String getCarrierName() {
		return carrierName;
	}

	public void setCarrierName(String carrierName) {
		this.carrierName = carrierName;
	}
	
	@ExcelField(title="承运发票号", align=2, sort=31)
	public String getInvoiceNumCar() {
		return invoiceNumCar;
	}

	public void setInvoiceNumCar(String invoiceNumCar) {
		this.invoiceNumCar = invoiceNumCar;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="发货日期", align=2, sort=32)
	public Date getAwayDate() {
		return awayDate;
	}

	public void setAwayDate(Date awayDate) {
		this.awayDate = awayDate;
	}
	
	@ExcelField(title="运费合计", align=2, sort=33)
	public Double getTranpriceSum() {
		return tranpriceSum;
	}

	public void setTranpriceSum(Double tranpriceSum) {
		this.tranpriceSum = tranpriceSum;
	}
	
	@ExcelField(title="金额合计", align=2, sort=34)
	public Double getPriceSum() {
		return priceSum;
	}

	public void setPriceSum(Double priceSum) {
		this.priceSum = priceSum;
	}
	
	@ExcelField(title="含税总金额", align=2, sort=35)
	public Double getPriceSumTaxed() {
		return priceSumTaxed;
	}

	public void setPriceSumTaxed(Double priceSumTaxed) {
		this.priceSumTaxed = priceSumTaxed;
	}
	
	@ExcelField(title="小计金额", align=2, sort=36)
	public Double getPriceSumSubtotal() {
		return priceSumSubtotal;
	}

	public void setPriceSumSubtotal(Double priceSumSubtotal) {
		this.priceSumSubtotal = priceSumSubtotal;
	}
	
	@ExcelField(title="付款标志", align=2, sort=37)
	public String getPayStateFlag() {
		return payStateFlag;
	}

	public void setPayStateFlag(String payStateFlag) {
		this.payStateFlag = payStateFlag;
	}
	
	@ExcelField(title="付款说明", align=2, sort=38)
	public String getPayStateNotes() {
		return payStateNotes;
	}

	public void setPayStateNotes(String payStateNotes) {
		this.payStateNotes = payStateNotes;
	}
	
	@ExcelField(title="预付标志", align=2, sort=39)
	public String getInPayaccFlag() {
		return inPayaccFlag;
	}

	public void setInPayaccFlag(String inPayaccFlag) {
		this.inPayaccFlag = inPayaccFlag;
	}
	
	@ExcelField(title="单据来源", align=2, sort=40)
	public String getOrderOrContract() {
		return orderOrContract;
	}

	public void setOrderOrContract(String orderOrContract) {
		this.orderOrContract = orderOrContract;
	}
	
	@ExcelField(title="质检标志", align=2, sort=41)
	public String getQmsFlag() {
		return qmsFlag;
	}

	public void setQmsFlag(String qmsFlag) {
		this.qmsFlag = qmsFlag;
	}
	
	@ExcelField(title="入库类型", align=2, sort=42)
	public String getIoType() {
		return ioType;
	}

	public void setIoType(String ioType) {
		this.ioType = ioType;
	}
	
	@ExcelField(title="入库标志", align=2, sort=43)
	public String getIoFlag() {
		return ioFlag;
	}

	public void setIoFlag(String ioFlag) {
		this.ioFlag = ioFlag;
	}
	
	@ExcelField(title="入库类型描述", align=2, sort=44)
	public String getIoDesc() {
		return ioDesc;
	}

	public void setIoDesc(String ioDesc) {
		this.ioDesc = ioDesc;
	}
	
	@ExcelField(title="部门编码", align=2, sort=45)
	public String getDepId() {
		return depId;
	}

	public void setDepId(String depId) {
		this.depId = depId;
	}
	
	@ExcelField(title="部门名称", align=2, sort=46)
	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="业务日期", align=2, sort=47)
	public Date getBdate() {
		return bdate;
	}

	public void setBdate(Date bdate) {
		this.bdate = bdate;
	}
	
	
	
	@ExcelField(title="单据说明", align=2, sort=49)
	public String getBillNotes() {
		return billNotes;
	}

	public void setBillNotes(String billNotes) {
		this.billNotes = billNotes;
	}
	
	@ExcelField(title="单据状态", align=2, sort=50)
	public String getBillState() {
		return billState;
	}

	public void setBillState(String billState) {
		this.billState = billState;
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
		
	public Date getBeginBdate() {
		return beginBdate;
	}

	public void setBeginBdate(Date beginBdate) {
		this.beginBdate = beginBdate;
	}
	
	public Date getEndBdate() {
		return endBdate;
	}

	public void setEndBdate(Date endBdate) {
		this.endBdate = endBdate;
	}
		
	public List<InvCheckDetail> getInvCheckDetailList() {
		return invCheckDetailList;
	}

	public void setInvCheckDetailList(List<InvCheckDetail> invCheckDetailList) {
		this.invCheckDetailList = invCheckDetailList;
	}
	
	@ExcelField(title="登陆人所在部门编码", align=2, sort=48)
	public String getUserDeptCode() {
		return userDeptCode;
	}

	public void setUserDeptCode(String userDeptCode) {
		this.userDeptCode = userDeptCode;
	}

	public String getSupCode() {
		return supCode;
	}

	public void setSupCode(String supCode) {
		this.supCode = supCode;
	}

	public Item getMainItem() {
		return mainItem;
	}

	public void setMainItem(Item mainItem) {
		this.mainItem = mainItem;
	}

	public String getMainItemName() {
		return mainItemName;
	}

	public void setMainItemName(String mainItemName) {
		this.mainItemName = mainItemName;
	}

	public String getMainItemSpecmodel() {
		return mainItemSpecmodel;
	}

	public void setMainItemSpecmodel(String mainItemSpecmodel) {
		this.mainItemSpecmodel = mainItemSpecmodel;
	}

	public Date getBeginArriveDate() {
		return beginArriveDate;
	}

	public void setBeginArriveDate(Date beginArriveDate) {
		this.beginArriveDate = beginArriveDate;
	}

	public Date getEndArriveDate() {
		return endArriveDate;
	}

	public void setEndArriveDate(Date endArriveDate) {
		this.endArriveDate = endArriveDate;
	}

	public GroupBuyer getGroupBuyer() {
		return groupBuyer;
	}

	public void setGroupBuyer(GroupBuyer groupBuyer) {
		this.groupBuyer = groupBuyer;
	}

	public String getBuyerId() {
		return buyerId;
	}

	public void setBuyerId(String buyerId) {
		this.buyerId = buyerId;
	}


	public String getPlanTypeName() {
		return planTypeName;
	}

	public void setPlanTypeName(String planTypeName) {
		this.planTypeName = planTypeName;
	}

	public String getItemSource() {
		return itemSource;
	}

	public void setItemSource(String itemSource) {
		this.itemSource = itemSource;
	}


	public String getPlanId() {
		return planId;
	}

	public void setPlanId(String planId) {
		this.planId = planId;
	}

	public String getContractId() {
		return contractId;
	}

	public void setContractId(String contractId) {
		this.contractId = contractId;
	}

	public String getContractName() {
		return contractName;
	}

	public void setContractName(String contractName) {
		this.contractName = contractName;
	}

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public List<InvCheckDetailCode> getInvCheckDetailCodeList() {
		return invCheckDetailCodeList;
	}

	public void setInvCheckDetailCodeList(List<InvCheckDetailCode> invCheckDetailCodeList) {
		this.invCheckDetailCodeList = invCheckDetailCodeList;
	}

	public Double getTaxRatio() {
		return taxRatio;
	}

	public void setTaxRatio(Double taxRatio) {
		this.taxRatio = taxRatio;
	}
	
    public String getTaxRatioNew() {
		return taxRatioNew;
	}

	public void setTaxRatioNew(String taxRatioNew) {
		String ratio=taxRatioNew.split("%")[0];
		this.taxRatio = Double.parseDouble(ratio)/100.0;
		this.taxRatioNew = taxRatioNew;
	}

	public String getBackReason() {
		return backReason;
	}

	public void setBackReason(String backReason) {
		this.backReason = backReason;
	}

}