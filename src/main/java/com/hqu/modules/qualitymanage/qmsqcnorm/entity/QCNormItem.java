/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.qualitymanage.qmsqcnorm.entity;


import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 检验标准子表Entity
 * @author luyumiao
 * @version 2018-05-05
 */
public class QCNormItem extends DataEntity<QCNormItem> {
	
	private static final long serialVersionUID = 1L;
	private QCNorm QCNorm;		// 主表ID 父类
	private String sn;		// 序号
	private String qcnormId;		// 检验标准号
	private String qcnormName;		// 检验标准名称
	private String objCode;		// 检验对象编码
	private String objName;		// 检验对象名称
	private String itemCode;		// 检验指标编码
	private String itemName;		// 检验指标名称
	private String itemUnit;		// 指标单位
	private String dataType;		// 数据类型
	private Integer dataLimit;		// 数据精度
	private String checkMethod;		// 检验方法
	private String checkAppa;		// 检验仪器
	private Integer appaLimit;		// 仪器精度
	private String appaUnit;		// 精度单位
	private Double minUnit;		// 最小值
	private Double normValue1;		// 标准值（数值）
	private String normValue2;		// 标准值（字符）
	private Double maxValue;		// 最大值
	private String formula;		// 公式
	private String sOper;		// 大于等于符号
	private String lOper;		// 小于等于符号
	
	public QCNormItem() {
		super();
	}

	public QCNormItem(String id){
		super(id);
	}

	public QCNormItem(QCNorm QCNorm){
		this.QCNorm = QCNorm;
	}

	public QCNorm getQCNorm() {
		return QCNorm;
	}

	public void setQCNorm(QCNorm QCNorm) {
		this.QCNorm = QCNorm;
	}
	
	@ExcelField(title="序号", align=2, sort=8)
	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}
	
	@ExcelField(title="检验标准号", align=2, sort=9)
	public String getQcnormId() {
		return qcnormId;
	}

	public void setQcnormId(String qcnormId) {
		this.qcnormId = qcnormId;
	}
	
	@ExcelField(title="检验标准名称", align=2, sort=10)
	public String getQcnormName() {
		return qcnormName;
	}

	public void setQcnormName(String qcnormName) {
		this.qcnormName = qcnormName;
	}
	
	@ExcelField(title="检验对象编码", align=2, sort=11)
	public String getObjCode() {
		return objCode;
	}

	public void setObjCode(String objCode) {
		this.objCode = objCode;
	}
	
	@ExcelField(title="检验对象名称", align=2, sort=12)
	public String getObjName() {
		return objName;
	}

	public void setObjName(String objName) {
		this.objName = objName;
	}
	
	@ExcelField(title="检验指标编码", align=2, sort=13)
	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}
	
	@ExcelField(title="检验指标名称", align=2, sort=14)
	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	
	@ExcelField(title="指标单位", align=2, sort=15)
	public String getItemUnit() {
		return itemUnit;
	}

	public void setItemUnit(String itemUnit) {
		this.itemUnit = itemUnit;
	}
	
	@ExcelField(title="数据类型", dictType="dataType", align=2, sort=16)
	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	
	@ExcelField(title="数据精度", align=2, sort=17)
	public Integer getDataLimit() {
		return dataLimit;
	}

	public void setDataLimit(Integer dataLimit) {
		this.dataLimit = dataLimit;
	}
	
	@ExcelField(title="检验方法", align=2, sort=18)
	public String getCheckMethod() {
		return checkMethod;
	}

	public void setCheckMethod(String checkMethod) {
		this.checkMethod = checkMethod;
	}
	
	@ExcelField(title="检验仪器", align=2, sort=19)
	public String getCheckAppa() {
		return checkAppa;
	}

	public void setCheckAppa(String checkAppa) {
		this.checkAppa = checkAppa;
	}
	
	@ExcelField(title="仪器精度", align=2, sort=20)
	public Integer getAppaLimit() {
		return appaLimit;
	}

	public void setAppaLimit(Integer appaLimit) {
		this.appaLimit = appaLimit;
	}
	
	@ExcelField(title="精度单位", align=2, sort=21)
	public String getAppaUnit() {
		return appaUnit;
	}

	public void setAppaUnit(String appaUnit) {
		this.appaUnit = appaUnit;
	}
	
	@ExcelField(title="最小值", align=2, sort=22)
	public Double getMinUnit() {
		return minUnit;
	}

	public void setMinUnit(Double minUnit) {
		this.minUnit = minUnit;
	}
	
	@ExcelField(title="标准值（数值）", align=2, sort=23)
	public Double getNormValue1() {
		return normValue1;
	}

	public void setNormValue1(Double normValue1) {
		this.normValue1 = normValue1;
	}
	
	@ExcelField(title="标准值（字符）", align=2, sort=24)
	public String getNormValue2() {
		return normValue2;
	}

	public void setNormValue2(String normValue2) {
		this.normValue2 = normValue2;
	}
	
	@ExcelField(title="最大值", align=2, sort=25)
	public Double getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(Double maxValue) {
		this.maxValue = maxValue;
	}
	
	@ExcelField(title="公式", align=2, sort=26)
	public String getFormula() {
		return formula;
	}

	public void setFormula(String formula) {
		this.formula = formula;
	}
	
	@ExcelField(title="大于等于符号", align=2, sort=27)
	public String getSOper() {
		return sOper;
	}

	public void setSOper(String sOper) {
		this.sOper = sOper;
	}
	
	@ExcelField(title="小于等于符号", align=2, sort=28)
	public String getLOper() {
		return lOper;
	}

	public void setLOper(String lOper) {
		this.lOper = lOper;
	}
	
}