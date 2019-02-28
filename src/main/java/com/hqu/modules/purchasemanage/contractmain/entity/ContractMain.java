/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.purchasemanage.contractmain.entity;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.jeeplus.modules.sys.entity.User;
import javax.validation.constraints.NotNull;

import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.format.annotation.NumberFormat;

import com.hqu.modules.basedata.account.entity.Account;
import com.hqu.modules.basedata.item.entity.Item;
import com.hqu.modules.purchasemanage.group.entity.GroupBuyer;
import com.hqu.modules.purchasemanage.linkman.entity.Paccount;
import com.hqu.modules.purchasemanage.transtype.entity.TranStype;
import com.hqu.modules.qualitymanage.purinvcheckmain.entity.InvCheckDetail;
import com.hqu.modules.purchasemanage.paymode.entity.PayMode;
import com.hqu.modules.purchasemanage.purplan.entity.PurPlanDetail;
import com.hqu.modules.purchasemanage.adtdetail.entity.AdtDetail;
import com.hqu.modules.purchasemanage.applymain.entity.ApplyDetail;
import com.hqu.modules.purchasemanage.contractnotesmodel.entity.ContractNotesModel;
import com.hqu.modules.purchasemanage.contracttype.entity.ContractType;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.jeeplus.core.persistence.ActEntity;
import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 采购合同管理Entity
 * @author ltq
 * @version 2018-04-24
 */
public class ContractMain extends ActEntity<ContractMain> {
	
	private static final long serialVersionUID = 1L;
	private String billNum;		// 单据号
	private String billType;		// 单据类型
	private Date makeDate;		// 制单日期
	private User user;		// 制单人编号
	private String makeEmpname;		// 制单人名称
	private String makeNotes;		// 制单说明
	private String billStateFlag;		// 单据状态
	private Account account;		// 供应商编号
	
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
	@NumberFormat(pattern="#,###,###.##")
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
	private Date beginBdate;		// 开始 业务日期
	private Date endBdate;		// 结束 业务日期
	private List<ContractDetail> contractDetailList = Lists.newArrayList();		// 合同子表列表	
	//private List<RollPlanNew> rollPlanNewList = Lists.newArrayList();		// 子表列表
	private List<PurPlanDetail> purPlanDetailList = Lists.newArrayList();		// 计划子表列表
	private List<ApplyDetail> applyDetailList=Lists.newArrayList();            //需求子表列表
	private List<InvCheckDetail> invCheckDetailList=Lists.newArrayList();   //到货子表列表
	private ContractNotesModel contractNotesModel;		// 合同模板
	private String contractModelName;
	private String contractTypeId;//合同类型编码，用于合同审核时form页面	
	private String planBillNum;//计划单号,检索
	private Item item;         //物料单号，查询
	private String itemName;   //物料名称，查询
	private String itemModel;  //物料规格,查询	
	private AdtDetail adtDetail;//定义审核流程
	private String nextUser;  //定义下一个审核人
	private String justifyRemark;//审核不通过意见
	private String name;//上传文件
	private String orderBy;//排序字段
	private String buyerId; //前端页面隐藏值
	private String procInsId;//流程实例Id 
	
	private String taxRatioPrint;//打印界面显示税率
	private String madeDate;// 打印界面显示制单日期
	//private String payModePrint;// 打印界面显示制单日期
	
	private String buyerPhoneNum;   //采购员电话
	private String buyerFaxNum;   //采购员传真
	private String accountLinkMam;    //供应商联系人
	private String accountFaxNum;   //供应商传真
	private String accountTelNum;   //供应商电话
	private String deliveryAddress;   //送货地址
	private String taxRatioNew;		// 税率
	
	private String isPrint; //是否打印标志

	private String goodsSumTaxedStr;//含税总额的字符串形式，用于按逗号分隔显示
	
	public String getGoodsSumTaxedStr() {
		
		return goodsSumTaxedStr;
	}

	public void setGoodsSumTaxedStr(String goodsSumTaxedStr) {
		this.goodsSumTaxedStr = goodsSumTaxedStr;
	}

	public String getIsPrint() {
		return isPrint;
	}

	public void setIsPrint(String isPrint) {
		this.isPrint = isPrint;
	}

	public String getTaxRatioPrint() {
    	
    	if(this.taxRatio!=null) {
    		Double d=this.taxRatio*100;
        	this.taxRatioPrint=d.toString()+"%";
    	}    	
		return taxRatioPrint;
	}

	public void setTaxRatioPrint(String taxRatioPrint) {
		this.taxRatioPrint = taxRatioPrint;
	}
	//@JsonFormat(pattern = "yyyy/MM/dd")
	//@ExcelField(title="制单日期", align=2, sort=9)
	public String getMadeDate() {
		if(this.createDate!=null) {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
		    String dateString = formatter.format(this.createDate);
			this.madeDate=dateString;
		}
		return madeDate;
	}

	public void setMadeDate(String madeDate) {
		this.madeDate = madeDate;
	}


	public String getTaxRatioNew() {
		return taxRatioNew;
	}

	public void setTaxRatioNew(String taxRatioNew) {
		String ratio=taxRatioNew.split("%")[0];
		this.taxRatio = Double.parseDouble(ratio)/100.0;
		this.taxRatioNew = taxRatioNew;
	}

	public String getBuyerPhoneNum() {
		return buyerPhoneNum;
	}

	public void setBuyerPhoneNum(String buyerPhoneNum) {
		this.buyerPhoneNum = buyerPhoneNum;
	}

	public String getBuyerFaxNum() {
		return buyerFaxNum;
	}

	public void setBuyerFaxNum(String buyerFaxNum) {
		this.buyerFaxNum = buyerFaxNum;
	}


	public String getAccountLinkMam() {
		return accountLinkMam;
	}

	public void setAccountLinkMam(String accountLinkMam) {
		this.accountLinkMam = accountLinkMam;
	}

	public String getAccountFaxNum() {
		return accountFaxNum;
	}

	public void setAccountFaxNum(String accountFaxNum) {
		this.accountFaxNum = accountFaxNum;
	}

	public String getAccountTelNum() {
		return accountTelNum;
	}

	public void setAccountTelNum(String accountTelNum) {
		this.accountTelNum = accountTelNum;
	}

	public String getDeliveryAddress() {
		return deliveryAddress;
	}

	public void setDeliveryAddress(String deliveryAddress) {
		this.deliveryAddress = deliveryAddress;
	}

		//-- 临时属性 --//
		// 流程任务
		private Task task;
		private Map<String, Object> variables;
		// 运行中的流程实例
		private ProcessInstance processInstance;
		// 历史的流程实例
		private HistoricProcessInstance historicProcessInstance;
		// 流程定义
		private ProcessDefinition processDefinition;
		
		public Task getTask() {
			return task;
		}

		public void setTask(Task task) {
			this.task = task;
		}

		public Map<String, Object> getVariables() {
			return variables;
		}

		public void setVariables(Map<String, Object> variables) {
			this.variables = variables;
		}

		public ProcessInstance getProcessInstance() {
			return processInstance;
		}

		public void setProcessInstance(ProcessInstance processInstance) {
			this.processInstance = processInstance;
		}

		public HistoricProcessInstance getHistoricProcessInstance() {
			return historicProcessInstance;
		}

		public void setHistoricProcessInstance(HistoricProcessInstance historicProcessInstance) {
			this.historicProcessInstance = historicProcessInstance;
		}

		public ProcessDefinition getProcessDefinition() {
			return processDefinition;
		}

		public void setProcessDefinition(ProcessDefinition processDefinition) {
			this.processDefinition = processDefinition;
		}

		
	
	
	public String getProcInsId() {
		return procInsId;
	}

	public void setProcInsId(String procInsId) {
		this.procInsId = procInsId;
	}

	public List<InvCheckDetail> getInvCheckDetailList() {
		return invCheckDetailList;
	}

	public void setInvCheckDetailList(List<InvCheckDetail> invCheckDetailList) {
		this.invCheckDetailList = invCheckDetailList;
	}
	public List<ApplyDetail> getApplyDetailList() {
		return applyDetailList;
	}

	public void setApplyDetailList(List<ApplyDetail> applyDetailList) {
		this.applyDetailList = applyDetailList;
	}
	public String getBuyerId() {
		return buyerId;
	}

	public void setBuyerId(String buyerId) {
		this.buyerId = buyerId;
	}

	public String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	public String getNextUser() {
		return nextUser;
	}

	public void setNextUser(String nextUser) {
		this.nextUser = nextUser;
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getJustifyRemark() {
		return justifyRemark;
	}

	public void setJustifyRemark(String justifyRemark) {
		this.justifyRemark = justifyRemark;
	}

	public String getContractTypeId() {
		return contractTypeId;
	}

	public void setContractTypeId(String contractTypeId) {
		this.contractTypeId = contractTypeId;
	}
	public AdtDetail getAdtDetail() {
		return adtDetail;
	}

	public void setAdtDetail(AdtDetail adtDetail) {
		this.adtDetail = adtDetail;
	}

	public ContractMain() {
		super();
	}

	public ContractMain(String id){
		super(id);
	}

	public Item getItem() {
		return item;
	}
	public ContractNotesModel getContractNotesModel() {
		return contractNotesModel;
	}
	public String getContractModelName() {
		return contractModelName;
	}

	public void setContractModelName(String contractModelName) {
		this.contractModelName = contractModelName;
	}
	public void setContractNotesModel(ContractNotesModel contractNotesModel) {
		this.contractNotesModel = contractNotesModel;
	}
	public String getPlanBillNum() {
		return planBillNum;
	}

	public void setPlanBillNum(String planBillNum) {
		this.planBillNum = planBillNum;
	}
	public void setItem(Item item) {
		this.item = item;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getItemModel() {
		return itemModel;
	}

	public void setItemModel(String itemModel) {
		this.itemModel = itemModel;
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
	
	@NotNull(message="供应商编号不能为空")
	@ExcelField(title="供应商编号", fieldType=Account.class, value="account.accountCode", align=2, sort=14)
	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}
   /* public Paccount getAccount() {
		return account;
	}

	public void setAccount(Paccount account) {
		this.account = account;
	}
	*/
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
	/*public void setTaxRatio(String taxRatio) {
		String ratio=taxRatio.split("%")[0];
		this.taxRatio = Double.parseDouble(ratio)/100.0;
	}
	*/
	@NotNull(message="采购员编号不能为空")
	@ExcelField(title="采购员编号", fieldType=GroupBuyer.class, value="groupBuyer.user.no", align=2, sort=19)
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
	@NumberFormat(pattern="#,###,###.##")
	@ExcelField(title="含税总额", align=2, sort=30)
	public Double getGoodsSumTaxed() {
		return goodsSumTaxed;
	}

	public void setGoodsSumTaxed(Double goodsSumTaxed) {
		//将按逗号分隔后的数字转字符串 
		java.text.NumberFormat nf = (java.text.NumberFormat) new DecimalFormat("#,###.00");
		String s = ((java.text.NumberFormat) nf).format(goodsSumTaxed);
		this.goodsSumTaxedStr= s;
		
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
		
	public List<PurPlanDetail> getPurPlanDetailList() {
		return purPlanDetailList;
	}

	public List<ContractDetail> getContractDetailList() {
		return contractDetailList;
	}

	public void setContractDetailList(List<ContractDetail> contractDetailList) {
		this.contractDetailList = contractDetailList;
	}

	public void setPurPlanDetailList(List<PurPlanDetail> purPlanDetailList) {
		this.purPlanDetailList = purPlanDetailList;
	}
	
	
}