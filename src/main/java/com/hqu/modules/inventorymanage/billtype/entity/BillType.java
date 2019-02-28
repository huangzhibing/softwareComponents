/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.inventorymanage.billtype.entity;

import java.util.List;
import com.google.common.collect.Lists;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 单据类型定义Entity
 * @author dongqida
 * @version 2018-04-14
 */
public class BillType extends DataEntity<BillType> {
	
	private static final long serialVersionUID = 1L;
	private String ioType;		// 出入库类型
	private String ioFlag;		// 出入库标识
	private String ioDesc;		// 描述
	private String currQty;		// 影响现有量
	private String currTotalQty;		// 影响总现有量
	private String beginQty;		// 影响期初量
	private String inQty;		// 影响累计入库
	private String outQty;		// 影响累计出库
	private String wasteQty;		// 影响废品量
	private String waitQty;		// 影响待验量
	private String assignQty;		// 影响已分配量
	private String wshopQty;		// 影响车间任务
	private String wshopUse;		// 影响车间领用
	private String wshopCost;		// 影响生产成本
	private String purAssign;		// 影响采购任务
	private String sellOrder;		// 影响销售订单
	private String costPrice;		// 影响移动平均价
	private String accountId;		// 科目代码
	private String accountName;		// 科目名称
	private String dinQty;		// 影响调拨入库
	private String doutQty;		// 影响调拨出库
	private String pinQty;		// 影响盘盈入库
	private String poutQty;		// 影响盘亏出库
	private String tinQty;		// 影响调整入库
	private String toutQty;		// 影响调整出库
	private String note;		// 备注
	private List<BillTypeWareHouse> billTypeWareHouseList = Lists.newArrayList();		// 子表列表
	
	public BillType() {
		super();
	}

	public BillType(String id){
		super(id);
	}

	@ExcelField(title="出入库类型", align=2, sort=7)
	public String getIoType() {
		return ioType;
	}

	public void setIoType(String ioType) {
		this.ioType = ioType;
	}
	
	@ExcelField(title="出入库标识", dictType="billType", align=2, sort=8)
	public String getIoFlag() {
		return ioFlag;
	}

	public void setIoFlag(String ioFlag) {
		this.ioFlag = ioFlag;
	}
	
	
	
	public String getIoDesc() {
		return ioDesc;
	}

	public void setIoDesc(String ioDesc) {
		this.ioDesc = ioDesc;
	}

	@ExcelField(title="影响现有量", dictType="billInfluence", align=2, sort=10)
	public String getCurrQty() {
		return currQty;
	}

	public void setCurrQty(String currQty) {
		this.currQty = currQty;
	}
	
	@ExcelField(title="影响总现有量", dictType="billInfluence", align=2, sort=11)
	public String getCurrTotalQty() {
		return currTotalQty;
	}

	public void setCurrTotalQty(String currTotalQty) {
		this.currTotalQty = currTotalQty;
	}
	
	@ExcelField(title="影响期初量", dictType="billInfluence", align=2, sort=12)
	public String getBeginQty() {
		return beginQty;
	}

	public void setBeginQty(String beginQty) {
		this.beginQty = beginQty;
	}
	
	@ExcelField(title="影响累计入库", dictType="billInfluence", align=2, sort=13)
	public String getInQty() {
		return inQty;
	}

	public void setInQty(String inQty) {
		this.inQty = inQty;
	}
	
	@ExcelField(title="影响累计出库", dictType="billInfluence", align=2, sort=14)
	public String getOutQty() {
		return outQty;
	}

	public void setOutQty(String outQty) {
		this.outQty = outQty;
	}
	
	@ExcelField(title="影响废品量", dictType="billInfluence", align=2, sort=15)
	public String getWasteQty() {
		return wasteQty;
	}

	public void setWasteQty(String wasteQty) {
		this.wasteQty = wasteQty;
	}
	
	@ExcelField(title="影响待验量", dictType="billInfluence", align=2, sort=16)
	public String getWaitQty() {
		return waitQty;
	}

	public void setWaitQty(String waitQty) {
		this.waitQty = waitQty;
	}
	
	@ExcelField(title="影响已分配量", dictType="billInfluence", align=2, sort=17)
	public String getAssignQty() {
		return assignQty;
	}

	public void setAssignQty(String assignQty) {
		this.assignQty = assignQty;
	}
	
	@ExcelField(title="影响车间任务", dictType="billInfluence", align=2, sort=18)
	public String getWshopQty() {
		return wshopQty;
	}

	public void setWshopQty(String wshopQty) {
		this.wshopQty = wshopQty;
	}
	
	@ExcelField(title="影响车间领用", dictType="billInfluence", align=2, sort=19)
	public String getWshopUse() {
		return wshopUse;
	}

	public void setWshopUse(String wshopUse) {
		this.wshopUse = wshopUse;
	}
	
	@ExcelField(title="影响生产成本", dictType="billInfluence", align=2, sort=20)
	public String getWshopCost() {
		return wshopCost;
	}

	public void setWshopCost(String wshopCost) {
		this.wshopCost = wshopCost;
	}
	
	@ExcelField(title="影响采购任务", dictType="billInfluence", align=2, sort=21)
	public String getPurAssign() {
		return purAssign;
	}

	public void setPurAssign(String purAssign) {
		this.purAssign = purAssign;
	}
	
	@ExcelField(title="影响销售订单", dictType="billInfluence", align=2, sort=22)
	public String getSellOrder() {
		return sellOrder;
	}

	public void setSellOrder(String sellOrder) {
		this.sellOrder = sellOrder;
	}
	
	@ExcelField(title="影响移动平均价", dictType="influenceCostPrice", align=2, sort=23)
	public String getCostPrice() {
		return costPrice;
	}

	public void setCostPrice(String costPrice) {
		this.costPrice = costPrice;
	}
	
	@ExcelField(title="科目代码", align=2, sort=24)
	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}
	
	@ExcelField(title="科目名称", align=2, sort=25)
	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	
	@ExcelField(title="影响调拨入库", dictType="billInfluence", align=2, sort=26)
	public String getDinQty() {
		return dinQty;
	}

	public void setDinQty(String dinQty) {
		this.dinQty = dinQty;
	}
	
	@ExcelField(title="影响调拨出库", dictType="billInfluence", align=2, sort=27)
	public String getDoutQty() {
		return doutQty;
	}

	public void setDoutQty(String doutQty) {
		this.doutQty = doutQty;
	}
	
	@ExcelField(title="影响盘盈入库", dictType="billInfluence", align=2, sort=28)
	public String getPinQty() {
		return pinQty;
	}

	public void setPinQty(String pinQty) {
		this.pinQty = pinQty;
	}
	
	@ExcelField(title="影响盘亏出库", dictType="billInfluence", align=2, sort=29)
	public String getPoutQty() {
		return poutQty;
	}

	public void setPoutQty(String poutQty) {
		this.poutQty = poutQty;
	}
	
	@ExcelField(title="影响调整入库", dictType="billInfluence", align=2, sort=30)
	public String getTinQty() {
		return tinQty;
	}

	public void setTinQty(String tinQty) {
		this.tinQty = tinQty;
	}
	
	@ExcelField(title="影响调整出库", dictType="billInfluence", align=2, sort=31)
	public String getToutQty() {
		return toutQty;
	}

	public void setToutQty(String toutQty) {
		this.toutQty = toutQty;
	}
	
	@ExcelField(title="备注", align=2, sort=32)
	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}
	
	public List<BillTypeWareHouse> getBillTypeWareHouseList() {
		return billTypeWareHouseList;
	}

	public void setBillTypeWareHouseList(List<BillTypeWareHouse> billTypeWareHouseList) {
		this.billTypeWareHouseList = billTypeWareHouseList;
	}
}