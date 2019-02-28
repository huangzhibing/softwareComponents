/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.purchasemanage.contractmain.entity;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.jeeplus.modules.sys.entity.User;
import javax.validation.constraints.NotNull;

import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;

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
public class ContractDetailView extends ActEntity<ContractDetailView> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Double serialNum;//序号
	//private String madeDate;  //下单日期
	private String billNum;//订单编号
	private String accountCode;//供应商编码
	private String accountId;//供应商ID
	private String accountName;//供应商名称
	private String itemCode;//物料编码
	private String itemName;//物料名称
	private String itemModel;//物料规格
	private Item item; //物料，用于存储检索表单对象的值
	private Account account;//供应商，用于存储检索表单对象的值
	private String unit;//单位
	//private Double orderTimes;//下单次数
	private Double orderNum;//下单数量
	private Double itemPriceTaxed;//含税单价
	private Double itemSumTaxed;//总额
	private String notes;//备注
	private Double itemPrice;//物料单价
	
	private Double  acceptedItemQty;//已交数量
	private Double  unacceptedItemQty;//未交数量
	private Double  storedItemQty;//已入库数量
	private Double  unstoredItemQty;//未入库数量
	
	private String invoiceNum;		// 发票号
	private String payStateFlag;		// 付款标志
	//private Double  itemQty;//签订数量
	private Date arriveDate;//交货日期

	private Date planArriveDate;//计划到货日期 ------------------------更新

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="计划到货时间", align=2, sort=12)
	public Date getPlanArriveDate() {
		return planArriveDate;
	}

	public void setPlanArriveDate(Date planArriveDate) {
		this.planArriveDate = planArriveDate;
	}

	private String maker;//制单人
	//检索条件
	private Date createBeginDate;  //起始下单日期
	private Date createEndDate;  //终止下单日期
	
	private Date arriveBeginDate;  //起始下单日期
	private Date arriveEndDate;  //终止下单日期
	private String orderBy;//排序字段



	public String getOrderBy() {
		return orderBy;
	}
	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}
	public String getInvoiceNum() {
		return invoiceNum;
	}
	public void setInvoiceNum(String invoiceNum) {
		this.invoiceNum = invoiceNum;
	}
	public String getPayStateFlag() {
		if(this.payStateFlag!=null){
			if((this.payStateFlag).equals("Y"))
				 this.payStateFlag="是";
			if((this.payStateFlag).equals("N"))
				 this.payStateFlag="否";
		}
		return payStateFlag;
	}
	public void setPayStateFlag(String payStateFlag) {
		this.payStateFlag = payStateFlag;
	}
	public Double getAcceptedItemQty() {
		if(this.acceptedItemQty==null)
			this.acceptedItemQty=0.0;
		return acceptedItemQty;
	}
	public void setAcceptedItemQty(Double acceptedItemQty) {
		this.acceptedItemQty = acceptedItemQty;
	}
	public Double getUnacceptedItemQty() {
		if(this.orderNum==null){
			this.orderNum=0.0;
		}
		if(this.acceptedItemQty==null){
			this.acceptedItemQty=0.0;
		}
		   this.unacceptedItemQty=this.orderNum-this.acceptedItemQty;
		return unacceptedItemQty;
	}
	public void setUnacceptedItemQty(Double unacceptedItemQty) {
		this.unacceptedItemQty = unacceptedItemQty;
	}
	public Double getStoredItemQty() {
		if(this.storedItemQty==null)
			this.storedItemQty=0.0;
		return storedItemQty;
	}
	public void setStoredItemQty(Double storedItemQty) {
		this.storedItemQty = storedItemQty;
	}
	public Double getUnstoredItemQty() {
		if(this.acceptedItemQty==null){
			this.acceptedItemQty=0.0;
		}
		if(this.storedItemQty==null){
			this.storedItemQty=0.0;
		}
		   this.unstoredItemQty=this.acceptedItemQty-this.storedItemQty;
		return unstoredItemQty;
	}
	public void setUnstoredItemQty(Double unstoredItemQty) {
		this.unstoredItemQty = unstoredItemQty;
	}
	public Double getItemPrice() {
		return itemPrice;
	}
	public void setItemPrice(Double itemPrice) {
		this.itemPrice = itemPrice;
	}
	public Date getArriveBeginDate() {
		return arriveBeginDate;
	}
	public void setArriveBeginDate(Date arriveBeginDate) {
		this.arriveBeginDate = arriveBeginDate;
	}
	public Date getArriveEndDate() {
		return arriveEndDate;
	}
	public void setArriveEndDate(Date arriveEndDate) {
		this.arriveEndDate = arriveEndDate;
	}
/*	public String getMadeDate() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String beginDate = formatter.format(this.createBeginDate);
		String endDate=formatter.format(this.createEndDate);
		this.madeDate=beginDate+" ~ "+endDate;
		return madeDate;
	}
	
	public void setMadeDate(String madeDate) {
		this.madeDate = madeDate;
	}
	*/
	public String getAccountId() {
		return accountId;
	}
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}
	
	public Item getItem() {
		return item;
	}
	public void setItem(Item item) {
		this.item = item;
	}
	public Account getAccount() {
		return account;
	}
	public void setAccount(Account account) {
		this.account = account;
	}
	public Date getCreateBeginDate() {
		return createBeginDate;
	}
	public void setCreateBeginDate(Date createBeginDate) {
		this.createBeginDate = createBeginDate;
	}
	public Date getCreateEndDate() {
		return createEndDate;
	}
	public void setCreateEndDate(Date createEndDate) {
		//搜索栏的日期是当天日期的00:00:00，要使得检索包含当日需要加一天
		if(createEndDate!=null){
				Calendar c = Calendar.getInstance();
		        c.setTime(createEndDate);
		        c.add(Calendar.DAY_OF_MONTH, 1);// 今天+1天
		        this.createEndDate=c.getTime();
		}else{
			this.createEndDate = createEndDate;
		}
	//	this.createEndDate = createEndDate;
	}
	public String getAccountCode() {
		return accountCode;
	}
	public void setAccountCode(String accountCode) {
		this.accountCode = accountCode;
	}
	public String getItemCode() {
		return itemCode;
	}
	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}
	public Double getSerialNum() {
		return serialNum;
	}
	public void setSerialNum(Double serialNum) {
		this.serialNum = serialNum;
	}
	
    public String getBillNum() {
		return billNum;
	}
	public void setBillNum(String billNum) {
		this.billNum = billNum;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="计划到达时间", align=2, sort=12)
	public Date getArriveDate() {
		return arriveDate;
	}
	public void setArriveDate(Date arriveDate) {
		this.arriveDate = arriveDate;
	}
	public String getMaker() {
		return maker;
	}
	public void setMaker(String maker) {
		this.maker = maker;
	}
	/*	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
*/
	public String getAccountName() {
		return accountName;
	}
	public void setAccountName(String accountName) {
		this.accountName = accountName;
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
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	
	public Double getOrderNum() {
		return orderNum;
	}
	public void setOrderNum(Double orderNum) {
		this.orderNum = orderNum;
	}
	public Double getItemPriceTaxed() {
		//保留小数点后两位
		//this.itemPriceTaxed=(double)Math.round(this.itemPriceTaxed*100)/100;
	/*	if(orderNum==null||orderNum.equals(0)||this.itemSumTaxed==null){
			this.itemPriceTaxed=0.0;
		}else{
			Double d=this.itemSumTaxed/orderNum;
			BigDecimal bg = new BigDecimal(d);
			this.itemPriceTaxed = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		}
	*/
		return itemPriceTaxed;
	}
	public void setItemPriceTaxed(Double itemPriceTaxed) {
		this.itemPriceTaxed = itemPriceTaxed;
	}
	public Double getItemSumTaxed() {
		//保留小数点后两位
		/*if(this.itemSumTaxed==null){
			this.itemSumTaxed=0.0;
		}else{
			BigDecimal bg = new BigDecimal(this.itemSumTaxed);
			this.itemSumTaxed = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		}
		*/
		return itemSumTaxed;
	}
	public void setItemSumTaxed(Double itemSumTaxed) {
		this.itemSumTaxed = itemSumTaxed;
	}
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
	
	
}