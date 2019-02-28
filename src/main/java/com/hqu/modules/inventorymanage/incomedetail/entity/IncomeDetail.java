/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.inventorymanage.incomedetail.entity;

import com.hqu.modules.basedata.item.entity.Item;
import com.hqu.modules.inventorymanage.billmain.entity.BillMain;
import com.hqu.modules.inventorymanage.bin.entity.Bin;
import com.hqu.modules.inventorymanage.location.entity.Location;
import com.jeeplus.common.utils.excel.annotation.ExcelField;
import com.jeeplus.core.persistence.DataEntity;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 出入库单据子表Entity
 * @author M1ngz
 * @version 2018-04-16
 */
public class IncomeDetail extends DataEntity<IncomeDetail> {

	private static final long serialVersionUID = 1L;
	private BillMain billNum;		// 单据号 父类
	private Integer serialNum;		// 序号
	private Bin bin;		// 货区号
	private String binName;		// 货区名称
	private Location loc;		// 货位号
	private String locName;		// 货位名称
	private Item item;		// 物料代码
	private String itemName;		// 物料名称
	private String itemSpec;		// 物料规格
	private String itemPdn;		// 物料图号
	private String barCode;		// 物料条码
	private String orderNum;		// 定单号
	private String planNum;		// 计划号
	private String measUnit;		// 计量单位
	private Double itemQty;		// 数量
	private Double itemRqty;		// 请领数量
	private Double itemPrice;		// 实际单价
	private Double itemSum;		// 实际金额
	private String itemBatch;		// 物料批号
	private String fitemCode;		// 父项代码
	private String forderNum;		// 父项订单号
	private String contractNum;		// 工程号
	private String corBillNum;		// 采购入库通知单号
	private String pickFlag;		// 提货标志
	private String estimateFlag;		// 估价标志
	private String note;		// 备注
	private String mrpFlag;		// MRP标志
	private Double planPrice;		// 计划单价
	private Double taxSum;		// 税额
	private String vehicleCode;		// 销售传来的车号
	private String revStation;		// 销售传来的到站
	private String revAccount;		// 销售传来的客户
	private String cosFlag;		// 成本标志位
	private Integer orderCodes;		// 接口序号
	private Double trafficCost;		// 运费
	private String operNo;		// 工序号
	private String sourceFlag;		// 自制外协标记
	private String operName;		// 工序名称
	private Double planSum;		// 计划金额
	private Integer corSerialnum;		// 对应序号
	private Double oldPrice;		// 保留单价
	private Double cgQty;		// 冲估数量
	private Double cgSum;		// 冲估金额
	private Double subSum;		// 成本差额
	private Double vouchQty;		// 开票数量
	private Double vouchSum;		// 开票金额
	private Double vouchSub;		// 开票差额
	private Double realSum;		// 不含税金额
	private Double realSumTaxed;		// 含税金额
	private Double taxRatio;		// 税率
	private String accountId;  //对应的库存账的id
	private Double itemQtyTemp; //存储可用量
	private String wareName;	//存储仓库名称
	private Double beginQty;	//期初结存
	private Date billDate;		// 出入库日期
	private Date beginBillDate;		// 开始 出入库日期
	private Date endBillDate;		// 结束 出入库日期
	private String ioDesc;	//单据类型
	private String period; //核算期
	private String itemCode;
	private String wareId;
	private String flag;

	public String getWareId() {
		return wareId;
	}

	public void setWareId(String wareId) {
		this.wareId = wareId;
	}

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public Date getBeginBillDate() {
		return beginBillDate;
	}

	public void setBeginBillDate(Date beginBillDate) {
		this.beginBillDate = beginBillDate;
	}

	public Date getEndBillDate() {
		return endBillDate;
	}

	public void setEndBillDate(Date endBillDate) {
		this.endBillDate = endBillDate;
	}

	public String getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period = period;
	}

	public String getWareName() {
		return wareName;
	}

	public void setWareName(String wareName) {
		this.wareName = wareName;
	}

	public Double getBeginQty() {
		return beginQty;
	}

	public void setBeginQty(Double beginQty) {
		this.beginQty = beginQty;
	}

	public Date getBillDate() {
		return billDate;
	}

	public void setBillDate(Date billDate) {
		this.billDate = billDate;
	}

	public String getIoDesc() {
		return ioDesc;
	}

	public void setIoDesc(String ioDesc) {
		this.ioDesc = ioDesc;
	}

	public Double getItemQtyTemp() {
		return itemQtyTemp;
	}

	public void setItemQtyTemp(Double itemQtyTemp) {
		this.itemQtyTemp = itemQtyTemp;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public IncomeDetail() {
		super();
	}

	public IncomeDetail(String id){
		super(id);
	}

	public IncomeDetail(BillMain billNum){
		this.billNum = billNum;
	}
	public BillMain getBillNum() {
		return billNum;
	}

	public void setBillNum(BillMain billNum) {
		this.billNum = billNum;
	}
	
	@NotNull(message="序号不能为空")
	@ExcelField(title="序号", align=2, sort=8)
	public Integer getSerialNum() {
		return serialNum;
	}

	public void setSerialNum(Integer serialNum) {
		this.serialNum = serialNum;
	}
	
	@ExcelField(title="货区号", fieldType=Bin.class, value="bin.binId", align=2, sort=9)
	public Bin getBin() {
		return bin;
	}

	public void setBin(Bin bin) {
		this.bin = bin;
	}
	
	@ExcelField(title="货区名称", align=2, sort=10)
	public String getBinName() {
		return binName;
	}

	public void setBinName(String binName) {
		this.binName = binName;
	}
	
	@ExcelField(title="货位号", fieldType=Location.class, value="loc.locId", align=2, sort=11)
	public Location getLoc() {
		return loc;
	}

	public void setLoc(Location loc) {
		this.loc = loc;
	}
	
	@ExcelField(title="货位名称", align=2, sort=12)
	public String getLocName() {
		return locName;
	}

	public void setLocName(String locName) {
		this.locName = locName;
	}
	
	@NotNull(message="物料代码不能为空")
	@ExcelField(title="物料代码", fieldType=Item.class, value="item.code", align=2, sort=13)
	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}
	
	@ExcelField(title="物料名称", align=2, sort=14)
	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	
	@ExcelField(title="物料规格", align=2, sort=15)
	public String getItemSpec() {
		return itemSpec;
	}

	public void setItemSpec(String itemSpec) {
		this.itemSpec = itemSpec;
	}
	
	@ExcelField(title="物料图号", align=2, sort=16)
	public String getItemPdn() {
		return itemPdn;
	}

	public void setItemPdn(String itemPdn) {
		this.itemPdn = itemPdn;
	}
	
	@ExcelField(title="物料条码", align=2, sort=17)
	public String getBarCode() {
		return barCode;
	}

	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}
	
	@ExcelField(title="定单号", align=2, sort=18)
	public String getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}
	
	@ExcelField(title="计划号", align=2, sort=19)
	public String getPlanNum() {
		return planNum;
	}

	public void setPlanNum(String planNum) {
		this.planNum = planNum;
	}
	
	@ExcelField(title="计量单位", align=2, sort=20)
	public String getMeasUnit() {
		return measUnit;
	}

	public void setMeasUnit(String measUnit) {
		this.measUnit = measUnit;
	}
	
	@ExcelField(title="数量", align=2, sort=21)
	public Double getItemQty() {
		return itemQty;
	}

	public void setItemQty(Double itemQty) {
		this.itemQty = itemQty;
	}
	
	@ExcelField(title="请领数量", align=2, sort=22)
	public Double getItemRqty() {
		return itemRqty;
	}

	public void setItemRqty(Double itemRqty) {
		this.itemRqty = itemRqty;
	}
	
	@ExcelField(title="实际单价", align=2, sort=23)
	public Double getItemPrice() {
		return itemPrice;
	}

	public void setItemPrice(Double itemPrice) {
		this.itemPrice = itemPrice;
	}
	
	@ExcelField(title="实际金额", align=2, sort=24)
	public Double getItemSum() {
		return itemSum;
	}

	public void setItemSum(Double itemSum) {
		this.itemSum = itemSum;
	}
	
	@ExcelField(title="物料批号", align=2, sort=25)
	public String getItemBatch() {
		return itemBatch;
	}

	public void setItemBatch(String itemBatch) {
		this.itemBatch = itemBatch;
	}
	
	@ExcelField(title="父项代码", align=2, sort=26)
	public String getFitemCode() {
		return fitemCode;
	}

	public void setFitemCode(String fitemCode) {
		this.fitemCode = fitemCode;
	}
	
	@ExcelField(title="父项订单号", align=2, sort=27)
	public String getForderNum() {
		return forderNum;
	}

	public void setForderNum(String forderNum) {
		this.forderNum = forderNum;
	}
	
	@ExcelField(title="工程号", align=2, sort=28)
	public String getContractNum() {
		return contractNum;
	}

	public void setContractNum(String contractNum) {
		this.contractNum = contractNum;
	}
	
	@ExcelField(title="采购入库通知单号", align=2, sort=29)
	public String getCorBillNum() {
		return corBillNum;
	}

	public void setCorBillNum(String corBillNum) {
		this.corBillNum = corBillNum;
	}
	
	@ExcelField(title="提货标志", dictType="pickFlag", align=2, sort=30)
	public String getPickFlag() {
		return pickFlag;
	}

	public void setPickFlag(String pickFlag) {
		this.pickFlag = pickFlag;
	}
	
	@ExcelField(title="估价标志", dictType="estimateFlag", align=2, sort=31)
	public String getEstimateFlag() {
		return estimateFlag;
	}

	public void setEstimateFlag(String estimateFlag) {
		this.estimateFlag = estimateFlag;
	}
	
	@ExcelField(title="备注", align=2, sort=32)
	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}
	
	@ExcelField(title="MRP标志", align=2, sort=33)
	public String getMrpFlag() {
		return mrpFlag;
	}

	public void setMrpFlag(String mrpFlag) {
		this.mrpFlag = mrpFlag;
	}
	
	@ExcelField(title="计划单价", align=2, sort=34)
	public Double getPlanPrice() {
		return planPrice;
	}

	public void setPlanPrice(Double planPrice) {
		this.planPrice = planPrice;
	}
	
	@ExcelField(title="税额", align=2, sort=35)
	public Double getTaxSum() {
		return taxSum;
	}

	public void setTaxSum(Double taxSum) {
		this.taxSum = taxSum;
	}
	
	@ExcelField(title="销售传来的车号", align=2, sort=36)
	public String getVehicleCode() {
		return vehicleCode;
	}

	public void setVehicleCode(String vehicleCode) {
		this.vehicleCode = vehicleCode;
	}
	
	@ExcelField(title="销售传来的到站", align=2, sort=37)
	public String getRevStation() {
		return revStation;
	}

	public void setRevStation(String revStation) {
		this.revStation = revStation;
	}
	
	@ExcelField(title="销售传来的客户", align=2, sort=38)
	public String getRevAccount() {
		return revAccount;
	}

	public void setRevAccount(String revAccount) {
		this.revAccount = revAccount;
	}
	
	@ExcelField(title="成本标志位", dictType="cosFlag", align=2, sort=39)
	public String getCosFlag() {
		return cosFlag;
	}

	public void setCosFlag(String cosFlag) {
		this.cosFlag = cosFlag;
	}
	
	@ExcelField(title="接口序号", align=2, sort=40)
	public Integer getOrderCodes() {
		return orderCodes;
	}

	public void setOrderCodes(Integer orderCodes) {
		this.orderCodes = orderCodes;
	}
	
	@ExcelField(title="运费", align=2, sort=41)
	public Double getTrafficCost() {
		return trafficCost;
	}

	public void setTrafficCost(Double trafficCost) {
		this.trafficCost = trafficCost;
	}
	
	@ExcelField(title="工序号", align=2, sort=42)
	public String getOperNo() {
		return operNo;
	}

	public void setOperNo(String operNo) {
		this.operNo = operNo;
	}
	
	@ExcelField(title="自制外协标记", dictType="sourceFlag", align=2, sort=43)
	public String getSourceFlag() {
		return sourceFlag;
	}

	public void setSourceFlag(String sourceFlag) {
		this.sourceFlag = sourceFlag;
	}
	
	@ExcelField(title="工序名称", align=2, sort=44)
	public String getOperName() {
		return operName;
	}

	public void setOperName(String operName) {
		this.operName = operName;
	}
	
	@ExcelField(title="计划金额", align=2, sort=45)
	public Double getPlanSum() {
		return planSum;
	}

	public void setPlanSum(Double planSum) {
		this.planSum = planSum;
	}
	
	@ExcelField(title="对应序号", align=2, sort=46)
	public Integer getCorSerialnum() {
		return corSerialnum;
	}

	public void setCorSerialnum(Integer corSerialnum) {
		this.corSerialnum = corSerialnum;
	}
	
	@ExcelField(title="保留单价", align=2, sort=47)
	public Double getOldPrice() {
		return oldPrice;
	}

	public void setOldPrice(Double oldPrice) {
		this.oldPrice = oldPrice;
	}
	
	@ExcelField(title="冲估数量", align=2, sort=48)
	public Double getCgQty() {
		return cgQty;
	}

	public void setCgQty(Double cgQty) {
		this.cgQty = cgQty;
	}
	
	@ExcelField(title="冲估金额", align=2, sort=49)
	public Double getCgSum() {
		return cgSum;
	}

	public void setCgSum(Double cgSum) {
		this.cgSum = cgSum;
	}
	
	@ExcelField(title="成本差额", align=2, sort=50)
	public Double getSubSum() {
		return subSum;
	}

	public void setSubSum(Double subSum) {
		this.subSum = subSum;
	}
	
	@ExcelField(title="开票数量", align=2, sort=51)
	public Double getVouchQty() {
		return vouchQty;
	}

	public void setVouchQty(Double vouchQty) {
		this.vouchQty = vouchQty;
	}
	
	@ExcelField(title="开票金额", align=2, sort=52)
	public Double getVouchSum() {
		return vouchSum;
	}

	public void setVouchSum(Double vouchSum) {
		this.vouchSum = vouchSum;
	}
	
	@ExcelField(title="开票差额", align=2, sort=53)
	public Double getVouchSub() {
		return vouchSub;
	}

	public void setVouchSub(Double vouchSub) {
		this.vouchSub = vouchSub;
	}
	
	@ExcelField(title="不含税金额", align=2, sort=54)
	public Double getRealSum() {
		return realSum;
	}

	public void setRealSum(Double realSum) {
		this.realSum = realSum;
	}
	
	@ExcelField(title="含税金额", align=2, sort=55)
	public Double getRealSumTaxed() {
		return realSumTaxed;
	}

	public void setRealSumTaxed(Double realSumTaxed) {
		this.realSumTaxed = realSumTaxed;
	}
	
	@ExcelField(title="税率", align=2, sort=56)
	public Double getTaxRatio() {
		return taxRatio;
	}

	public void setTaxRatio(Double taxRatio) {
		this.taxRatio = taxRatio;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}
}