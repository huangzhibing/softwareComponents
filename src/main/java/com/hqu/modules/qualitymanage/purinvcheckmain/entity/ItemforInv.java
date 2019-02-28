/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.qualitymanage.purinvcheckmain.entity;

import com.hqu.modules.basedata.item.entity.ItemClassNew;
import com.hqu.modules.basedata.unit.entity.Unit;
import javax.validation.constraints.NotNull;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 物料定义Entity
 * @author 张铮
 * @version 2018-06-05
 */
public class ItemforInv extends DataEntity<ItemforInv> {
	
	private static final long serialVersionUID = 1L;
	private String code;		// 物料编码
	private String name;		// 物料名称
	private ItemClassNew classCode;		// 物料类型编码 父类
	private String className;		// 物料类型名称
	private String specModel;		// 规格型号
	private String texture;		// 材质
	private String gb;		// 国标码
	private String drawNO;		// 图号
	private Unit unitCode;		// 计量单位编码
	private String unit;		// 计量单位名称
	private Double safetyQty;		// 安全库存量
	private Integer leadTime;		// 采购提前期
	private Double maxStorage;		// 最大库存
	private Double minStorage;		// 最小库存
	private Double planPrice;		// 计划价格
	private String systemSign;		// 系统标识
	private String isKey;		// 关键件标识
	private Double itemBatch;		// 采购批量
	private String isInMotor;		// 整机标识
	
	public ItemforInv() {
		super();
	}

	public ItemforInv(String id){
		super(id);
	}

	public ItemforInv(ItemClassNew classCode){
		this.classCode = classCode;
	}

	@ExcelField(title="物料编码", align=2, sort=7)
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	@ExcelField(title="物料名称", align=2, sort=8)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public ItemClassNew getClassCode() {
		return classCode;
	}

	public void setClassCode(ItemClassNew classCode) {
		this.classCode = classCode;
	}
	
	@ExcelField(title="物料类型名称", align=2, sort=10)
	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}
	
	@ExcelField(title="规格型号", align=2, sort=11)
	public String getSpecModel() {
		return specModel;
	}

	public void setSpecModel(String specModel) {
		this.specModel = specModel;
	}
	
	@ExcelField(title="材质", align=2, sort=12)
	public String getTexture() {
		return texture;
	}

	public void setTexture(String texture) {
		this.texture = texture;
	}
	
	@ExcelField(title="国标码", align=2, sort=13)
	public String getGb() {
		return gb;
	}

	public void setGb(String gb) {
		this.gb = gb;
	}
	
	@ExcelField(title="图号", align=2, sort=14)
	public String getDrawNO() {
		return drawNO;
	}

	public void setDrawNO(String drawNO) {
		this.drawNO = drawNO;
	}
	
	@NotNull(message="计量单位编码不能为空")
	@ExcelField(title="计量单位编码", fieldType=Unit.class, value="unitCode.unitName", align=2, sort=15)
	public Unit getUnitCode() {
		return unitCode;
	}

	public void setUnitCode(Unit unitCode) {
		this.unitCode = unitCode;
	}
	
	@ExcelField(title="计量单位名称", align=2, sort=16)
	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}
	
	@ExcelField(title="安全库存量", align=2, sort=17)
	public Double getSafetyQty() {
		return safetyQty;
	}

	public void setSafetyQty(Double safetyQty) {
		this.safetyQty = safetyQty;
	}
	
	@ExcelField(title="采购提前期", align=2, sort=18)
	public Integer getLeadTime() {
		return leadTime;
	}

	public void setLeadTime(Integer leadTime) {
		this.leadTime = leadTime;
	}
	
	@ExcelField(title="最大库存", align=2, sort=19)
	public Double getMaxStorage() {
		return maxStorage;
	}

	public void setMaxStorage(Double maxStorage) {
		this.maxStorage = maxStorage;
	}
	
	@ExcelField(title="最小库存", align=2, sort=20)
	public Double getMinStorage() {
		return minStorage;
	}

	public void setMinStorage(Double minStorage) {
		this.minStorage = minStorage;
	}
	
	@ExcelField(title="计划价格", align=2, sort=21)
	public Double getPlanPrice() {
		return planPrice;
	}

	public void setPlanPrice(Double planPrice) {
		this.planPrice = planPrice;
	}
	
	@ExcelField(title="系统标识", dictType="systemSign", align=2, sort=22)
	public String getSystemSign() {
		return systemSign;
	}

	public void setSystemSign(String systemSign) {
		this.systemSign = systemSign;
	}
	
	@ExcelField(title="关键件标识", dictType="is_key", align=2, sort=23)
	public String getIsKey() {
		return isKey;
	}

	public void setIsKey(String isKey) {
		this.isKey = isKey;
	}
	
	@ExcelField(title="采购批量", align=2, sort=24)
	public Double getItemBatch() {
		return itemBatch;
	}

	public void setItemBatch(Double itemBatch) {
		this.itemBatch = itemBatch;
	}
	
	@ExcelField(title="整机标识", dictType="is_in_motor", align=2, sort=25)
	public String getIsInMotor() {
		return isInMotor;
	}

	public void setIsInMotor(String isInMotor) {
		this.isInMotor = isInMotor;
	}
	
}