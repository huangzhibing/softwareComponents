/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.inventorymanage.warehousemaintenance.countdata.entity;

import com.hqu.modules.basedata.item.entity.Item;
import javax.validation.constraints.NotNull;
import com.hqu.modules.basedata.unit.entity.Unit;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 盘点单子表Entity
 * @author zb
 * @version 2018-04-23
 */
public class CountDetail extends DataEntity<CountDetail> {
	
	private static final long serialVersionUID = 1L;
	private CountMain countMain;		// 盘点单号 父类
	private Item item;		// 物料编码
	private String itemName;		// 物料名称
	private Unit measUnit;		// 计量单位
	private String specModel;		// 物料规格
	private String itemPdn;		// 物料图号
	private String batchId;		// 批号
	private Double planPrice;		// 计划单价
    private Double itemQty;		// 账面数量
    private Double realQty;		// 实际数量
	private String ykFlag;		// 处理标志
	
	public CountDetail() {
		super();
	}

	public CountDetail(String id){
		super(id);
	}

	public CountDetail(CountMain countMain){
		this.countMain = countMain;
	}

	public CountMain getCountMain() {
		return countMain;
	}

	public void setCountMain(CountMain countMain) {
		this.countMain = countMain;
	}
	
	@NotNull(message="物料编码不能为空")
	@ExcelField(title="物料编码", fieldType=Item.class, value="item.code", align=2, sort=8)
	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}
	
	@ExcelField(title="物料名称", align=2, sort=9)
	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	
	@ExcelField(title="计量单位", fieldType=Unit.class, value="measUnit.unitCode", align=2, sort=10)
	public Unit getMeasUnit() {
		return measUnit;
	}

	public void setMeasUnit(Unit measUnit) {
		this.measUnit = measUnit;
	}
	
	@ExcelField(title="物料规格", align=2, sort=11)
	public String getSpecModel() {
		return specModel;
	}

	public void setSpecModel(String specModel) {
		this.specModel = specModel;
	}
	
	@ExcelField(title="物料图号", align=2, sort=12)
	public String getItemPdn() {
		return itemPdn;
	}

	public void setItemPdn(String itemPdn) {
		this.itemPdn = itemPdn;
	}
	
	@ExcelField(title="批号", align=2, sort=13)
	public String getBatchId() {
		return batchId;
	}

	public void setBatchId(String batchId) {
		this.batchId = batchId;
	}
	
	@ExcelField(title="计划单价", align=2, sort=14)
	public Double getPlanPrice() {
		return planPrice;
	}

	public void setPlanPrice(Double planPrice) {
		this.planPrice = planPrice;
	}

    @ExcelField(title="账面数量", align=2, sort=15)
    public Double getItemQty() {
        return itemQty;
    }

    public void setItemQty(Double itemQty) {
        this.itemQty = itemQty;
    }

    @ExcelField(title="实际数量", align=2, sort=16)
    public Double getRealQty() {
        return realQty;
    }

    public void setRealQty(Double realQty) {
        this.realQty = realQty;
    }
	
	@ExcelField(title="处理标志", dictType="procFlag", align=2, sort=17)
	public String getYkFlag() {
		return ykFlag;
	}

	public void setYkFlag(String ykFlag) {
		this.ykFlag = ykFlag;
	}
	
}