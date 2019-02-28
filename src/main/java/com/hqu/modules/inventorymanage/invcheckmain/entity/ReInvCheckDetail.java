/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.inventorymanage.invcheckmain.entity;


import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 超期复验单子表Entity
 * @author syc代建
 * @version 2018-05-28
 */
public class ReInvCheckDetail extends DataEntity<ReInvCheckDetail> {
	
	private static final long serialVersionUID = 1L;
	private ReinvCheckMain reInvCheckMain;		// 单据号 父类
	private Integer serialNum;		// 序号
	private String itemCode;		// 物料编码
	private String itemSpec;		// 物料规格
	private String itemName;		// 物料名称
	private String measUnit;		// 单位
	private Integer itemQty;		// 数量
	private Integer itemPrice;		// 实际价格
	private Integer itemSum;		// 实际金额
	private String itemBatch;		// 批次
	private String binId;		// 获取代码
	private String binName;		// 获取名称
	private String locId;		// 货位代码
	private String locName;		// 货位名称
	private Integer hgQty;		// 合格数量
	
	public ReInvCheckDetail() {
		super();
	}

	public ReInvCheckDetail(String id){
		super(id);
	}

	public ReInvCheckDetail(ReinvCheckMain reInvCheckMain){
		this.reInvCheckMain = reInvCheckMain;
	}

	public ReinvCheckMain getReInvCheckMain() {
		return reInvCheckMain;
	}

	public void setReInvCheckMain(ReinvCheckMain reInvCheckMain) {
		this.reInvCheckMain = reInvCheckMain;
	}
	
	@ExcelField(title="序号", align=2, sort=8)
	public Integer getSerialNum() {
		return serialNum;
	}

	public void setSerialNum(Integer serialNum) {
		this.serialNum = serialNum;
	}
	
	@ExcelField(title="物料编码", align=2, sort=9)
	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}
	
	@ExcelField(title="物料规格", align=2, sort=10)
	public String getItemSpec() {
		return itemSpec;
	}

	public void setItemSpec(String itemSpec) {
		this.itemSpec = itemSpec;
	}
	
	@ExcelField(title="物料名称", align=2, sort=11)
	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	
	@ExcelField(title="单位", align=2, sort=12)
	public String getMeasUnit() {
		return measUnit;
	}

	public void setMeasUnit(String measUnit) {
		this.measUnit = measUnit;
	}
	
	@ExcelField(title="数量", align=2, sort=13)
	public Integer getItemQty() {
		return itemQty;
	}

	public void setItemQty(Integer itemQty) {
		this.itemQty = itemQty;
	}
	
	@ExcelField(title="实际价格", align=2, sort=14)
	public Integer getItemPrice() {
		return itemPrice;
	}

	public void setItemPrice(Integer itemPrice) {
		this.itemPrice = itemPrice;
	}
	
	@ExcelField(title="实际金额", align=2, sort=15)
	public Integer getItemSum() {
		return itemSum;
	}

	public void setItemSum(Integer itemSum) {
		this.itemSum = itemSum;
	}
	
	@ExcelField(title="批次", align=2, sort=16)
	public String getItemBatch() {
		return itemBatch;
	}

	public void setItemBatch(String itemBatch) {
		this.itemBatch = itemBatch;
	}
	
	@ExcelField(title="获取代码", align=2, sort=17)
	public String getBinId() {
		return binId;
	}

	public void setBinId(String binId) {
		this.binId = binId;
	}
	
	@ExcelField(title="获取名称", align=2, sort=18)
	public String getBinName() {
		return binName;
	}

	public void setBinName(String binName) {
		this.binName = binName;
	}
	
	@ExcelField(title="货位代码", align=2, sort=19)
	public String getLocId() {
		return locId;
	}

	public void setLocId(String locId) {
		this.locId = locId;
	}
	
	@ExcelField(title="货位名称", align=2, sort=20)
	public String getLocName() {
		return locName;
	}

	public void setLocName(String locName) {
		this.locName = locName;
	}
	
	@ExcelField(title="合格数量", align=2, sort=21)
	public Integer getHgQty() {
		return hgQty;
	}

	public void setHgQty(Integer hgQty) {
		this.hgQty = hgQty;
	}
	
}