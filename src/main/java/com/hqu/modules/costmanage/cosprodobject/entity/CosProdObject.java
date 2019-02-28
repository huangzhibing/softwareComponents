/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.costmanage.cosprodobject.entity;


import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 核算对象定义(右表）Entity
 * @author zz
 * @version 2018-09-07
 */
public class CosProdObject extends DataEntity<CosProdObject> {
	
	private static final long serialVersionUID = 1L;
	private String prodId;		// 核算对象编码
	private String prodName;		// 核算对象名称
	private String prodCatalog;		// 核算对象类别
	private String prodNum;		// 数量
	private String prodFinish;		// 是否为叶子节点
	private CosProdObjectLeft fatherId;		// 上级核算对象 父类
	private String specModel;		//核算对象规格
	private String unit;		// 核算对象单位
	
	public CosProdObject() {
		super();
	}

	public CosProdObject(String id){
		super(id);
	}

	public CosProdObject(CosProdObjectLeft fatherId){
		this.fatherId = fatherId;
	}

	@ExcelField(title="核算对象编码", align=2, sort=7)
	public String getProdId() {
		return prodId;
	}

	public void setProdId(String prodId) {
		this.prodId = prodId;
	}
	
	@ExcelField(title="核算对象名称", align=2, sort=8)
	public String getProdName() {
		return prodName;
	}

	public void setProdName(String prodName) {
		this.prodName = prodName;
	}
	
	@ExcelField(title="核算对象类别", align=2, sort=9)
	public String getProdCatalog() {
		return prodCatalog;
	}

	public void setProdCatalog(String prodCatalog) {
		this.prodCatalog = prodCatalog;
	}
	
	@ExcelField(title="数量", align=2, sort=10)
	public String getProdNum() {
		return prodNum;
	}

	public void setProdNum(String prodNum) {
		this.prodNum = prodNum;
	}
	
	@ExcelField(title="是否为叶子节点", dictType="itemFinish", align=2, sort=11)
	public String getProdFinish() {
		return prodFinish;
	}

	public void setProdFinish(String prodFinish) {
		this.prodFinish = prodFinish;
	}
	
	public CosProdObjectLeft getFatherId() {
		return fatherId;
	}

	public void setFatherId(CosProdObjectLeft fatherId) {
		this.fatherId = fatherId;
	}

	public String getSpecModel() {
		return specModel;
	}

	public void setSpecModel(String specModel) {
		this.specModel = specModel;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}
	
}