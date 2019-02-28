/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.inventorymanage.newinvcheckmain.entity;

import com.hqu.modules.basedata.item.entity.Item;
import com.hqu.modules.inventorymanage.invaccount.entity.InvAccount;
import javax.validation.constraints.NotNull;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 超期复验子表Entity
 * @author Neil
 * @version 2018-06-16
 */
public class NewinvCheckDetail extends DataEntity<NewinvCheckDetail> {
	
	private static final long serialVersionUID = 1L;
	private NewinvCheckMain billNum;		// 单据号 父类
	private String serialNum;		// 序号
	private Item itemCode;		// 物料编号
	private String itemSpec;		// 物料名称
	private String itemName;		// 物料规格
	private String measUnit;		// 单位
	private Integer itemQty;		// 数量
	private Double itemPrice;		// 实际价格
	private Double itemSum;		// 实际金额
	private String itemBatch;		// 批次
	private String binId;		// 货区代码
	private String binName;		// 货区名称
	private String locId;		// 货位编码
	private String locName;		// 货位名称
	private Integer hgQty;		// 合格数量
	
	public NewinvCheckDetail() {
		super();
	}

	public NewinvCheckDetail(String id){
		super(id);
	}

	public NewinvCheckDetail(NewinvCheckMain billNum){
		this.billNum = billNum;
	}

	public NewinvCheckMain getBillNum() {
		return billNum;
	}

	public void setBillNum(NewinvCheckMain billNum) {
		this.billNum = billNum;
	}
	
	@ExcelField(title="序号", align=2, sort=7)
	public String getSerialNum() {
		return serialNum;
	}

	public void setSerialNum(String serialNum) {
		this.serialNum = serialNum;
	}
	
	@NotNull(message="物料编号不能为空")
	@ExcelField(title="物料编号", fieldType=Item.class, value="itemCode.item", align=2, sort=8)
	public Item getItemCode() {
		return itemCode;
	}

	public void setItemCode(Item itemCode) {
		this.itemCode = itemCode;
	}
	
	@ExcelField(title="物料名称", align=2, sort=9)
	public String getItemSpec() {
		return itemSpec;
	}

	public void setItemSpec(String itemSpec) {
		this.itemSpec = itemSpec;
	}
	
	@ExcelField(title="物料规格", align=2, sort=10)
	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	
	@ExcelField(title="单位", align=2, sort=11)
	public String getMeasUnit() {
		return measUnit;
	}

	public void setMeasUnit(String measUnit) {
		this.measUnit = measUnit;
	}
	
	@NotNull(message="数量不能为空")
	@ExcelField(title="数量", align=2, sort=12)
	public Integer getItemQty() {
		return itemQty;
	}

	public void setItemQty(Integer itemQty) {
		this.itemQty = itemQty;
	}
	
	@ExcelField(title="实际价格", align=2, sort=13)
	public Double getItemPrice() {
		return itemPrice;
	}

	public void setItemPrice(Double itemPrice) {
		this.itemPrice = itemPrice;
	}
	
	@ExcelField(title="实际金额", align=2, sort=14)
	public Double getItemSum() {
		return itemSum;
	}

	public void setItemSum(Double itemSum) {
		this.itemSum = itemSum;
	}
	
	@ExcelField(title="批次", align=2, sort=15)
	public String getItemBatch() {
		return itemBatch;
	}

	public void setItemBatch(String itemBatch) {
		this.itemBatch = itemBatch;
	}
	
	@ExcelField(title="货区代码", align=2, sort=16)
	public String getBinId() {
		return binId;
	}

	public void setBinId(String binId) {
		this.binId = binId;
	}
	
	@ExcelField(title="货区名称", align=2, sort=17)
	public String getBinName() {
		return binName;
	}

	public void setBinName(String binName) {
		this.binName = binName;
	}
	
	@ExcelField(title="货位编码", align=2, sort=18)
	public String getLocId() {
		return locId;
	}

	public void setLocId(String locId) {
		this.locId = locId;
	}
	
	@ExcelField(title="货位名称", align=2, sort=19)
	public String getLocName() {
		return locName;
	}

	public void setLocName(String locName) {
		this.locName = locName;
	}
	
	@NotNull(message="合格数量不能为空")
	@ExcelField(title="合格数量", align=2, sort=20)
	public Integer getHgQty() {
		return hgQty;
	}

	public void setHgQty(Integer hgQty) {
		this.hgQty = hgQty;
	}
	
}