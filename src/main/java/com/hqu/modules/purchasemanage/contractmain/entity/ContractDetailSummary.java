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
public class ContractDetailSummary extends ActEntity<ContractDetailSummary> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Double serialNum;//序号
	private String madeDate;  //下单日期
	
	private String accountCode;//供应商编码
	private String accountId;//供应商编码
	private String accountName;//供应商名称
	private String itemCode;//物料编码
	private String itemName;//物料名称
	private String itemModel;//物料规格
	private Item item; //物料，用于存储检索表单对象的值
	private Account account;//供应商，用于存储检索表单对象的值
	private String unit;//单位
	private Double orderTimes;//下单次数
	private Double orderNum;//下单数量
	private Double itemPriceTaxed;//含税单价
	private Double itemSumTaxed;//总额
	private String notes;//备注
	private Double itemPrice;//物料单价
	private Double  itemQty;//签订数量
	
	private Date itemBeginDate;  //物料的下单日期段的起始值
	private Date itemEndDate;  //物料的下单日期段的终止值
	
	//检索条件
	private Date createBeginDate;  //起始下单日期
	private Date createEndDate;  //终止下单日期
	private String orderBy;//排序字段
	
	public String getOrderBy() {
		return orderBy;
	}
	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}
	public Date getItemBeginDate() {
		return itemBeginDate;
	}
	public void setItemBeginDate(Date itemBeginDate) {
		this.itemBeginDate = itemBeginDate;
	}
	public Date getItemEndDate() {
		return itemEndDate;
	}
	public void setItemEndDate(Date itemEndDate) {
		this.itemEndDate = itemEndDate;
	}
	public String getMadeDate() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String beginDate = formatter.format(this.itemBeginDate);
		String endDate=formatter.format(this.itemEndDate);
		this.madeDate=beginDate+" ~ "+endDate;
		return madeDate;
	}
	public void setMadeDate(String madeDate) {
		this.madeDate = madeDate;
	}
	public String getAccountId() {
		return accountId;
	}
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}
	public Double getItemPrice() {
		return itemPrice;
	}
	public void setItemPrice(Double itemPrice) {
		this.itemPrice = itemPrice;
	}
	public Double getItemQty() {
		return itemQty;
	}
	public void setItemQty(Double itemQty) {
		this.itemQty = itemQty;
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
	public Double getOrderTimes() {
		return orderTimes;
	}
	public void setOrderTimes(Double orderTimes) {
		this.orderTimes = orderTimes;
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
		if(orderNum.equals(0)){
			this.itemPriceTaxed=0.0;
		}else{
			Double d=this.itemSumTaxed/orderNum;
			BigDecimal bg = new BigDecimal(d);
			this.itemPriceTaxed = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		}
		return itemPriceTaxed;
	}
	public void setItemPriceTaxed(Double itemPriceTaxed) {
		this.itemPriceTaxed = itemPriceTaxed;
	}
	public Double getItemSumTaxed() {
		//保留小数点后两位
		//this.itemSumTaxed=(double)Math.round(this.itemSumTaxed*100)/100;
		BigDecimal bg = new BigDecimal(this.itemSumTaxed);
		this.itemSumTaxed = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
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