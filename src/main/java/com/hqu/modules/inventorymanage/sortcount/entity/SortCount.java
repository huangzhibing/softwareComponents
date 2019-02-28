package com.hqu.modules.inventorymanage.sortcount.entity;


import com.hqu.modules.basedata.account.entity.Account;
import com.hqu.modules.basedata.item.entity.Item;
import com.hqu.modules.inventorymanage.bin.entity.Bin;
import com.hqu.modules.inventorymanage.invaccount.entity.InvAccount;
import com.hqu.modules.inventorymanage.location.entity.Location;
import com.hqu.modules.inventorymanage.warehouse.entity.WareHouse;
import com.jeeplus.common.utils.excel.annotation.ExcelField;
import com.jeeplus.core.persistence.BaseEntity;
import com.jeeplus.core.persistence.DataEntity;

import javax.validation.constraints.NotNull;

/**
 * 资金及ABC分类统计Entity
 * @author hzb
 * @version 2018-05-21
 */
public class SortCount extends DataEntity<InvAccount>{

    private static final long serialVersionUID = 1L;
    private WareHouse ware;		// 仓库编号
    private Item item;		// 物料编号
    private String year;		// 会计年度
    private String period;		// 会计期间
    private Double beginQty;		// 期初数量
    private Double beginSum;		// 期初金额
    private Double currInQty;		// 本期入库量
    private Double currInSum;		// 入库金额
    private Double currOutQty;		// 本期出库量
    private Double currOutSum;		// 出库金额
    private Double nowQty;		// 现有量
    private Double nowSum;		// 现有金额
    private Double costPrice;		// 单价
    private Double costTax;		// 差异率
    private Double beginSub;		// 期初差异
    private Double currInSub;		// 收入差异
    private Double currOutSub;		// 发出差异
    private Double nowSub;		// 结存差异
    private Double minCost;		// 最小入库单价
    private Double maxCost;		// 最大入库单价
    private String companyCode;		// 公司机构类码
    private Double tinSum;		// 调整入库
    private Double toutSum;		// 调整出库
    private Double dinQty;		// 调拨入库数量
    private Double dinSum;		// 调拨入库金额
    private Double doutQty;		// 调拨出库数量
    private Double doutSum;		// 调拨出库金额
    private Double pinQty;		// 盘点入库数量
    private Double pinSum;		// 盘点入库金额
    private Double poutQty;		// 盘点出库数量
    private Double poutSum;		// 盘点出库金额
    private Double qinQty;		// 其他入库数量
    private Double qinSum;		// 其他入库金额
    private Double qoutQty;		// 其他出库数量
    private Double qoutSum;		// 其他出库金额
    private String checkFlag;		// 盘点标志
    private String toolFlag;		// 工具标志
    private Bin bin;		// 货区编号
    private Location loc;		// 货位编号
    private String itemBatch;		// 批次号
    private String corId;		// 供应商编号
    private String deptId;		// 部门编号
    private String applyCode;		// 需求号
    private String orderCode;		// 订单号
    private String operNo;		// 工序号
    private String sourceFlag;		// 自制外协标记
    private Double salQty;		// 待发货数量
    private Double purQty;		// 待入库数量
    private Double dinTranQty;		// 调拨在途数量
    private Double doutTranQty;		// 调拨待发数量
    private Double sfcQty;		// 计划备料数量
    private Double qmsQty;		// 不合格数量
    private Double realQty;		// 可用数量
    private String ratioQty;     //数量比例
    private String ratioSum;    //金额比例
    private String sortType;    //abc类别

    public SortCount(InvAccount invaccount){    //构造含有仓库物料现有量和现有金额的类
        ware = invaccount.getWare();
        item = invaccount.getItem();
        nowQty = invaccount.getNowQty();
        nowSum = invaccount.getNowSum();
    }

    public String getSortType() {
        return sortType;
    }

    public void setSortType(String sortType) {
        this.sortType = sortType;
    }

    public SortCount() {
        super();
    }

    public SortCount(String id){
        super(id);
    }

    @NotNull(message="仓库编号不能为空")
    @ExcelField(title="仓库编号", fieldType=WareHouse.class, value="ware.wareID", align=2, sort=7)
    public WareHouse getWare() {
        return ware;
    }

    public void setWare(WareHouse ware) {
        this.ware = ware;
    }

    @NotNull(message="物料编号不能为空")
    @ExcelField(title="物料编号", fieldType=Item.class, value="item.code", align=2, sort=8)
    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    @ExcelField(title="会计年度", align=2, sort=9)
    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    @ExcelField(title="会计期间", align=2, sort=10)
    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    @ExcelField(title="期初数量", align=2, sort=11)
    public Double getBeginQty() {
        return beginQty;
    }

    public void setBeginQty(Double beginQty) {
        this.beginQty = beginQty;
    }

    @ExcelField(title="期初金额", align=2, sort=12)
    public Double getBeginSum() {
        return beginSum;
    }

    public void setBeginSum(Double beginSum) {
        this.beginSum = beginSum;
    }

    @ExcelField(title="本期入库量", align=2, sort=13)
    public Double getCurrInQty() {
        return currInQty;
    }

    public void setCurrInQty(Double currInQty) {
        this.currInQty = currInQty;
    }

    @ExcelField(title="入库金额", align=2, sort=14)
    public Double getCurrInSum() {
        return currInSum;
    }

    public void setCurrInSum(Double currInSum) {
        this.currInSum = currInSum;
    }

    @ExcelField(title="本期出库量", align=2, sort=15)
    public Double getCurrOutQty() {
        return currOutQty;
    }

    public void setCurrOutQty(Double currOutQty) {
        this.currOutQty = currOutQty;
    }

    @ExcelField(title="出库金额", align=2, sort=16)
    public Double getCurrOutSum() {
        return currOutSum;
    }

    public void setCurrOutSum(Double currOutSum) {
        this.currOutSum = currOutSum;
    }

    @ExcelField(title="现有量", align=2, sort=17)
    public Double getNowQty() {
        return nowQty;
    }

    public void setNowQty(Double nowQty) {
        this.nowQty = nowQty;
    }

    @ExcelField(title = "数量比例")
    public String getRatioQty() {
        return ratioQty;
    }

    public void setRatioQty(String ratioQty) {
        this.ratioQty = ratioQty;
    }

    @ExcelField(title = "金额比例")
    public String getRatioSum() {
        return ratioSum;
    }

    public void setRatioSum(String ratioSum) {
        this.ratioSum = ratioSum;
    }


    @ExcelField(title="现有金额", align=2, sort=18)
    public Double getNowSum() {
        return nowSum;
    }

    public void setNowSum(Double nowSum) {
        this.nowSum = nowSum;
    }

    @ExcelField(title="单价", align=2, sort=19)
    public Double getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(Double costPrice) {
        this.costPrice = costPrice;
    }

    @ExcelField(title="差异率", align=2, sort=20)
    public Double getCostTax() {
        return costTax;
    }

    public void setCostTax(Double costTax) {
        this.costTax = costTax;
    }

    @ExcelField(title="期初差异", align=2, sort=21)
    public Double getBeginSub() {
        return beginSub;
    }

    public void setBeginSub(Double beginSub) {
        this.beginSub = beginSub;
    }

    @ExcelField(title="收入差异", align=2, sort=22)
    public Double getCurrInSub() {
        return currInSub;
    }

    public void setCurrInSub(Double currInSub) {
        this.currInSub = currInSub;
    }

    @ExcelField(title="发出差异", align=2, sort=23)
    public Double getCurrOutSub() {
        return currOutSub;
    }

    public void setCurrOutSub(Double currOutSub) {
        this.currOutSub = currOutSub;
    }

    @ExcelField(title="结存差异", align=2, sort=24)
    public Double getNowSub() {
        return nowSub;
    }

    public void setNowSub(Double nowSub) {
        this.nowSub = nowSub;
    }

    @ExcelField(title="最小入库单价", align=2, sort=25)
    public Double getMinCost() {
        return minCost;
    }

    public void setMinCost(Double minCost) {
        this.minCost = minCost;
    }

    @ExcelField(title="最大入库单价", align=2, sort=26)
    public Double getMaxCost() {
        return maxCost;
    }

    public void setMaxCost(Double maxCost) {
        this.maxCost = maxCost;
    }

    @ExcelField(title="公司机构类码", align=2, sort=27)
    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    @ExcelField(title="调整入库", align=2, sort=28)
    public Double getTinSum() {
        return tinSum;
    }

    public void setTinSum(Double tinSum) {
        this.tinSum = tinSum;
    }

    @ExcelField(title="调整出库", align=2, sort=29)
    public Double getToutSum() {
        return toutSum;
    }

    public void setToutSum(Double toutSum) {
        this.toutSum = toutSum;
    }

    @ExcelField(title="调拨入库数量", align=2, sort=30)
    public Double getDinQty() {
        return dinQty;
    }

    public void setDinQty(Double dinQty) {
        this.dinQty = dinQty;
    }

    @ExcelField(title="调拨入库金额", align=2, sort=31)
    public Double getDinSum() {
        return dinSum;
    }

    public void setDinSum(Double dinSum) {
        this.dinSum = dinSum;
    }

    @ExcelField(title="调拨出库数量", align=2, sort=32)
    public Double getDoutQty() {
        return doutQty;
    }

    public void setDoutQty(Double doutQty) {
        this.doutQty = doutQty;
    }

    @ExcelField(title="调拨出库金额", align=2, sort=33)
    public Double getDoutSum() {
        return doutSum;
    }

    public void setDoutSum(Double doutSum) {
        this.doutSum = doutSum;
    }

    @ExcelField(title="盘点入库数量", align=2, sort=34)
    public Double getPinQty() {
        return pinQty;
    }

    public void setPinQty(Double pinQty) {
        this.pinQty = pinQty;
    }

    @ExcelField(title="盘点入库金额", align=2, sort=35)
    public Double getPinSum() {
        return pinSum;
    }

    public void setPinSum(Double pinSum) {
        this.pinSum = pinSum;
    }

    @ExcelField(title="盘点出库数量", align=2, sort=36)
    public Double getPoutQty() {
        return poutQty;
    }

    public void setPoutQty(Double poutQty) {
        this.poutQty = poutQty;
    }

    @ExcelField(title="盘点出库金额", align=2, sort=37)
    public Double getPoutSum() {
        return poutSum;
    }

    public void setPoutSum(Double poutSum) {
        this.poutSum = poutSum;
    }

    @ExcelField(title="其他入库数量", align=2, sort=38)
    public Double getQinQty() {
        return qinQty;
    }

    public void setQinQty(Double qinQty) {
        this.qinQty = qinQty;
    }

    @ExcelField(title="其他入库金额", align=2, sort=39)
    public Double getQinSum() {
        return qinSum;
    }

    public void setQinSum(Double qinSum) {
        this.qinSum = qinSum;
    }

    @ExcelField(title="其他出库数量", align=2, sort=40)
    public Double getQoutQty() {
        return qoutQty;
    }

    public void setQoutQty(Double qoutQty) {
        this.qoutQty = qoutQty;
    }

    @ExcelField(title="其他出库金额", align=2, sort=41)
    public Double getQoutSum() {
        return qoutSum;
    }

    public void setQoutSum(Double qoutSum) {
        this.qoutSum = qoutSum;
    }

    @ExcelField(title="盘点标志", dictType="checkFlag", align=2, sort=42)
    public String getCheckFlag() {
        return checkFlag;
    }

    public void setCheckFlag(String checkFlag) {
        this.checkFlag = checkFlag;
    }

    @ExcelField(title="工具标志", dictType="toolFlag", align=2, sort=43)
    public String getToolFlag() {
        return toolFlag;
    }

    public void setToolFlag(String toolFlag) {
        this.toolFlag = toolFlag;
    }

    @ExcelField(title="货区编号", fieldType=Bin.class, value="bin.binId", align=2, sort=44)
    public Bin getBin() {
        return bin;
    }

    public void setBin(Bin bin) {
        this.bin = bin;
    }

    @ExcelField(title="货位编号", fieldType=Location.class, value="loc.locId", align=2, sort=45)
    public Location getLoc() {
        return loc;
    }

    public void setLoc(Location loc) {
        this.loc = loc;
    }

    @ExcelField(title="批次号", align=2, sort=46)
    public String getItemBatch() {
        return itemBatch;
    }

    public void setItemBatch(String itemBatch) {
        this.itemBatch = itemBatch;
    }

    @ExcelField(title="供应商编号", align=2, sort=47)
    public String getCorId() {
        return corId;
    }

    public void setCorId(String corId) {
        this.corId = corId;
    }

    @ExcelField(title="部门编号", align=2, sort=48)
    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    @ExcelField(title="需求号", align=2, sort=49)
    public String getApplyCode() {
        return applyCode;
    }

    public void setApplyCode(String applyCode) {
        this.applyCode = applyCode;
    }

    @ExcelField(title="订单号", align=2, sort=50)
    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    @ExcelField(title="工序号", align=2, sort=51)
    public String getOperNo() {
        return operNo;
    }

    public void setOperNo(String operNo) {
        this.operNo = operNo;
    }

    @ExcelField(title="自制外协标记", dictType="sourceFlag", align=2, sort=52)
    public String getSourceFlag() {
        return sourceFlag;
    }

    public void setSourceFlag(String sourceFlag) {
        this.sourceFlag = sourceFlag;
    }

    @ExcelField(title="待发货数量", align=2, sort=53)
    public Double getSalQty() {
        return salQty;
    }

    public void setSalQty(Double salQty) {
        this.salQty = salQty;
    }

    @ExcelField(title="待入库数量", align=2, sort=54)
    public Double getPurQty() {
        return purQty;
    }

    public void setPurQty(Double purQty) {
        this.purQty = purQty;
    }

    @ExcelField(title="调拨在途数量", align=2, sort=55)
    public Double getDinTranQty() {
        return dinTranQty;
    }

    public void setDinTranQty(Double dinTranQty) {
        this.dinTranQty = dinTranQty;
    }

    @ExcelField(title="调拨待发数量", align=2, sort=56)
    public Double getDoutTranQty() {
        return doutTranQty;
    }

    public void setDoutTranQty(Double doutTranQty) {
        this.doutTranQty = doutTranQty;
    }

    @ExcelField(title="计划备料数量", align=2, sort=57)
    public Double getSfcQty() {
        return sfcQty;
    }

    public void setSfcQty(Double sfcQty) {
        this.sfcQty = sfcQty;
    }

    @ExcelField(title="不合格数量", align=2, sort=58)
    public Double getQmsQty() {
        return qmsQty;
    }

    public void setQmsQty(Double qmsQty) {
        this.qmsQty = qmsQty;
    }

    @ExcelField(title="可用数量", align=2, sort=59)
    public Double getRealQty() {
        return realQty;
    }

    public void setRealQty(Double realQty) {
        this.realQty = realQty;
    }

}
