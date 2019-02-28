/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.workshopmanage.materialorder.entity;


import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 领料单子表Entity
 * @author zhangxin
 * @version 2018-05-22
 */
public class SfcMaterialOrderDetail extends DataEntity<SfcMaterialOrderDetail> {
	
	private static final long serialVersionUID = 1L;
	private SfcMaterialOrder materialOrder;		// 领料单号 父类
	private Integer sequenceId;		// 序号
	private String materialId;		// 材料代码
	private String materialName;		// 材料名称
	private String numUnit;		// 计量单位
	private String materialSpec;		// 材料规格
	private Double requireNum;		// 申领数量
	private Double receiveNum;		// 实发数量
	private Double receivedNum;		// 已领数量
	private Double unitPrice;		// 计划价格
	private Double sumPrice;		// 计划金额
	private Double realPrice;		// 实际价格
	private Double realSum;		// 实际金额
	private String mpsId;		// MPS号
	private String projId;		// 工程号
	private String orderId;		// 令单号
	private String taskId;		// 任务号
	private String batchId;		// 批号
	private String itemCode;		// 零件代码
	private String itemName;		// 零件名称
	private Double orderNum;		// 计划数量
	private Double sizePerItem;		// 单件下料尺寸
	private String operNo;		// 工序号
	private String operCode;		// 工序代码
	private String itemBatch;		// 批次
	private String binId;		// 货区代码
	private String binName;		// 货区名称
	private String locId;		// 货位代码
	private String locName;		// 货位名称
	private String materialOrderDetail; //实际的领料单号
	private String isLack; //是否缺料Y/N
	private Integer lackQty;//缺料数量

	private Double returnNum;   //退料数量
	
	public SfcMaterialOrderDetail() {
		super();
	}

	public SfcMaterialOrderDetail(String id){
		super(id);
	}

	public SfcMaterialOrderDetail(SfcMaterialOrder materialOrder){
		this.materialOrder = materialOrder;
	}

	public SfcMaterialOrder getMaterialOrder() {
		return materialOrder;
	}

	public void setMaterialOrder(SfcMaterialOrder materialOrder) {
		this.materialOrder = materialOrder;
		this.materialOrderDetail = materialOrder.getMaterialOrder();
	}
	
	@ExcelField(title="序号", align=2, sort=8)
	public Integer getSequenceId() {
		return sequenceId;
	}

	public void setSequenceId(Integer sequenceId) {
		this.sequenceId = sequenceId;
	}
	
	@ExcelField(title="材料代码", align=2, sort=9)
	public String getMaterialId() {
		return materialId;
	}

	public void setMaterialId(String materialId) {
		this.materialId = materialId;
	}
	
	@ExcelField(title="材料名称", align=2, sort=10)
	public String getMaterialName() {
		return materialName;
	}

	public void setMaterialName(String materialName) {
		this.materialName = materialName;
	}
	
	@ExcelField(title="计量单位", align=2, sort=11)
	public String getNumUnit() {
		return numUnit;
	}

	public void setNumUnit(String numUnit) {
		this.numUnit = numUnit;
	}
	
	@ExcelField(title="材料规格", align=2, sort=12)
	public String getMaterialSpec() {
		return materialSpec;
	}

	public void setMaterialSpec(String materialSpec) {
		this.materialSpec = materialSpec;
	}
	
	@ExcelField(title="申领数量", align=2, sort=13)
	public Double getRequireNum() {
		return requireNum;
	}

	public void setRequireNum(Double requireNum) {
		this.requireNum = requireNum;
	}
	
	@ExcelField(title="实发数量", align=2, sort=14)
	public Double getReceiveNum() {
		return receiveNum;
	}

	public void setReceiveNum(Double receiveNum) {
		this.receiveNum = receiveNum;
	}
	
	@ExcelField(title="已领数量", align=2, sort=15)
	public Double getReceivedNum() {
		return receivedNum;
	}

	public void setReceivedNum(Double receivedNum) {
		this.receivedNum = receivedNum;
	}
	
	@ExcelField(title="计划价格", align=2, sort=16)
	public Double getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(Double unitPrice) {
		this.unitPrice = unitPrice;
	}
	
	@ExcelField(title="计划金额", align=2, sort=17)
	public Double getSumPrice() {
		return sumPrice;
	}

	public void setSumPrice(Double sumPrice) {
		this.sumPrice = sumPrice;
	}
	
	@ExcelField(title="实际价格", align=2, sort=18)
	public Double getRealPrice() {
		return realPrice;
	}

	public void setRealPrice(Double realPrice) {
		this.realPrice = realPrice;
	}
	
	@ExcelField(title="实际金额", align=2, sort=19)
	public Double getRealSum() {
		return realSum;
	}

	public void setRealSum(Double realSum) {
		this.realSum = realSum;
	}
	
	@ExcelField(title="MPS号", align=2, sort=20)
	public String getMpsId() {
		return mpsId;
	}

	public void setMpsId(String mpsId) {
		this.mpsId = mpsId;
	}
	
	@ExcelField(title="工程号", align=2, sort=21)
	public String getProjId() {
		return projId;
	}

	public void setProjId(String projId) {
		this.projId = projId;
	}
	
	@ExcelField(title="令单号", align=2, sort=22)
	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	
	@ExcelField(title="任务号", align=2, sort=23)
	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	
	@ExcelField(title="批号", align=2, sort=24)
	public String getBatchId() {
		return batchId;
	}

	public void setBatchId(String batchId) {
		this.batchId = batchId;
	}
	
	@ExcelField(title="零件代码", align=2, sort=25)
	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}
	
	@ExcelField(title="零件名称", align=2, sort=26)
	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	
	@ExcelField(title="计划数量", align=2, sort=27)
	public Double getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(Double orderNum) {
		this.orderNum = orderNum;
	}
	
	@ExcelField(title="单件下料尺寸", align=2, sort=28)
	public Double getSizePerItem() {
		return sizePerItem;
	}

	public void setSizePerItem(Double sizePerItem) {
		this.sizePerItem = sizePerItem;
	}
	
	@ExcelField(title="工序号", align=2, sort=29)
	public String getOperNo() {
		return operNo;
	}

	public void setOperNo(String operNo) {
		this.operNo = operNo;
	}
	
	@ExcelField(title="工序代码", align=2, sort=30)
	public String getOperCode() {
		return operCode;
	}

	public void setOperCode(String operCode) {
		this.operCode = operCode;
	}
	
	@ExcelField(title="批次", align=2, sort=31)
	public String getItemBatch() {
		return itemBatch;
	}

	public void setItemBatch(String itemBatch) {
		this.itemBatch = itemBatch;
	}
	
	@ExcelField(title="货区代码", align=2, sort=32)
	public String getBinId() {
		return binId;
	}

	public void setBinId(String binId) {
		this.binId = binId;
	}
	
	@ExcelField(title="货区名称", align=2, sort=33)
	public String getBinName() {
		return binName;
	}

	public void setBinName(String binName) {
		this.binName = binName;
	}
	
	@ExcelField(title="货位代码", align=2, sort=34)
	public String getLocId() {
		return locId;
	}

	public void setLocId(String locId) {
		this.locId = locId;
	}
	
	@ExcelField(title="货位名称", align=2, sort=35)
	public String getLocName() {
		return locName;
	}

	public void setLocName(String locName) {
		this.locName = locName;
	}
	
	@ExcelField(title="实际物料代码", align=2, sort=9)
	public String getmaterialOrderDetail() {
		return materialOrderDetail;
	}

	public void setmaterialOrderDetail(String materialOrderDetail) {
		this.materialOrderDetail = materialOrderDetail;
	}

	public String getIsLack() {
		return isLack;
	}

	public void setIsLack(String isLack) {
		this.isLack = isLack;
	}

	public Integer getLackQty() {
		return lackQty;
	}

	public void setLackQty(Integer lackQty) {
		this.lackQty = lackQty;
	}

	public Double getReturnNum() {
		return returnNum;
	}

	public void setReturnNum(Double returnNum) {
		this.returnNum = returnNum;
	}


}