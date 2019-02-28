/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.purchasemanage.contractmain.entity;

import com.hqu.modules.purchasemanage.contractmain.entity.ContractMain;

import java.util.Date;

import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.NumberFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hqu.modules.basedata.item.entity.Item;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 采购合同子表Entity
 * @author ltq
 * @version 2018-04-24
 */
public class ContractDetail extends DataEntity<ContractDetail> {
	
	private static final long serialVersionUID = 1L;
	private ContractMain contractMain;		// 单据编号 父类
	private Integer serialNum;		// 序号
	private Item item;		// 物料编号
	private String itemName;		// 物料名称
	private Double planQty;		// 计划量
	private Double itemQty;		// 数量
	private Double itemPrice;		// 无税单价
	private Double itemSum;		// 无税总额
	@NumberFormat(pattern="#,###,###.##")
	private Double itemPriceTaxed;		// 含税单价
	@NumberFormat(pattern="#,###,###.##")
	private Double itemSumTaxed;		// 含税金额
	private String massRequire;		// 质量要求
	private String notes;		// 备注
	private String itemModel;		// 物料规格
	private String itemTexture;		// 物料材质
	private String measUnit;		// 单位
	private Double costPrice;		// 移动价
	private Double checkQty;		// 到货量
	private Double transPrice;		// 运费价格
	private Double transSum;		// 运费金额
	private String itemPdn;		// 物料图号
	private String userDeptCode;		// 登入用户所在部门编码
	private String planBillNum;  //计划单号，合同生成时关系表中填写	
	private Integer planSerialNum;//计划序号，合同生成时关系表中填写
	private Double frontItemQty; //当前的合同数量
	private Date arriveDate;//计划到货日期
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@NotNull(message="计划到达时间不能为空")
	@ExcelField(title="计划到达时间", align=2, sort=12)
	public Date getArriveDate() {
		return arriveDate;
	}
	public void setArriveDate(Date arriveDate) {
		this.arriveDate = arriveDate;
	}

	public Double getFrontItemQty() {
		return frontItemQty;
	}

	public void setFrontItemQty(Double frontItemQty) {
		this.frontItemQty = frontItemQty;
	}

	public Integer getPlanSerialNum() {
		return planSerialNum;
	}

	public void setPlanSerialNum(Integer planSerialNum) {
		this.planSerialNum = planSerialNum;
	}

	public String getPlanBillNum() {
		return planBillNum;
	}

	public void setPlanBillNum(String planBillNum) {
		this.planBillNum = planBillNum;
	}

	public ContractDetail() {
		super();
	}

	public ContractDetail(String id){
		super(id);
	}

	public ContractDetail(ContractMain contractMain){
		this.contractMain = contractMain;
	}

	@NotNull(message="单据编号不能为空")
	public ContractMain getContractMain() {
		return contractMain;
	}

	public void setContractMain(ContractMain contractMain) {
		this.contractMain = contractMain;
	}
	
	@ExcelField(title="序号", align=2, sort=8)
	public Integer getSerialNum() {
		return serialNum;
	}

	public void setSerialNum(Integer serialNum) {
		this.serialNum = serialNum;
	}
	
	@NotNull(message="物料编号不能为空")
	@ExcelField(title="物料编号", fieldType=Item.class, value="item.code", align=2, sort=9)
	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}
	
	@ExcelField(title="物料名称", align=2, sort=10)
	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	
	@ExcelField(title="计划量", align=2, sort=11)
	public Double getPlanQty() {
		return planQty;
	}

	public void setPlanQty(Double planQty) {
		this.planQty = planQty;
	}
	
	@NotNull(message="数量不能为空")
	@ExcelField(title="数量", align=2, sort=12)
	public Double getItemQty() {
		return itemQty;
	}

	public void setItemQty(Double itemQty) {
		this.itemQty = itemQty;
	}
	@NumberFormat(pattern="#,###,###.##")
	@NotNull(message="无税单价不能为空")
	@ExcelField(title="无税单价", align=2, sort=13)
	public Double getItemPrice() {
		return itemPrice;
	}

	public void setItemPrice(Double itemPrice) {
		this.itemPrice = itemPrice;
	}
	@NumberFormat(pattern="#,###,###.##")
	@NotNull(message="无税总额不能为空")
	@ExcelField(title="无税总额", align=2, sort=14)
	public Double getItemSum() {
		return itemSum;
	}

	public void setItemSum(Double itemSum) {
		this.itemSum = itemSum;
	}
	@NumberFormat(pattern="#,###,###.##")
	@NotNull(message="含税单价不能为空")
	@ExcelField(title="含税单价", align=2, sort=15)
	public Double getItemPriceTaxed() {
		return itemPriceTaxed;
	}

	public void setItemPriceTaxed(Double itemPriceTaxed) {
		this.itemPriceTaxed = itemPriceTaxed;
	}
	@NumberFormat(pattern="###,###")
	@NotNull(message="含税金额不能为空")
	@ExcelField(title="含税金额", align=2, sort=16)
	public Double getItemSumTaxed() {
		return itemSumTaxed;
	}

	public void setItemSumTaxed(Double itemSumTaxed) {
		this.itemSumTaxed = itemSumTaxed;
	}
	
	@ExcelField(title="质量要求", align=2, sort=17)
	public String getMassRequire() {
		return massRequire;
	}

	public void setMassRequire(String massRequire) {
		this.massRequire = massRequire;
	}
	
	@ExcelField(title="备注", align=2, sort=18)
	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}
	
	@ExcelField(title="物料规格", align=2, sort=19)
	public String getItemModel() {
		return itemModel;
	}

	public void setItemModel(String itemModel) {
		this.itemModel = itemModel;
	}
	
	@ExcelField(title="物料材质", align=2, sort=20)
	public String getItemTexture() {
		return itemTexture;
	}

	public void setItemTexture(String itemTexture) {
		this.itemTexture = itemTexture;
	}
	
	@ExcelField(title="单位", align=2, sort=21)
	public String getMeasUnit() {
		return measUnit;
	}

	public void setMeasUnit(String measUnit) {
		this.measUnit = measUnit;
	}
	
	@ExcelField(title="移动价", align=2, sort=22)
	public Double getCostPrice() {
		return costPrice;
	}

	public void setCostPrice(Double costPrice) {
		this.costPrice = costPrice;
	}
	
	@ExcelField(title="到货量", align=2, sort=23)
	public Double getCheckQty() {
		return checkQty;
	}

	public void setCheckQty(Double checkQty) {
		this.checkQty = checkQty;
	}
	
	@ExcelField(title="运费价格", align=2, sort=24)
	public Double getTransPrice() {
		return transPrice;
	}

	public void setTransPrice(Double transPrice) {
		this.transPrice = transPrice;
	}
	
	@ExcelField(title="运费金额", align=2, sort=25)
	public Double getTransSum() {
		return transSum;
	}

	public void setTransSum(Double transSum) {
		this.transSum = transSum;
	}
	
	@ExcelField(title="物料图号", align=2, sort=26)
	public String getItemPdn() {
		return itemPdn;
	}

	public void setItemPdn(String itemPdn) {
		this.itemPdn = itemPdn;
	}
	
	@ExcelField(title="登入用户所在部门编码", align=2, sort=27)
	public String getUserDeptCode() {
		return userDeptCode;
	}

	public void setUserDeptCode(String userDeptCode) {
		this.userDeptCode = userDeptCode;
	}
	
}