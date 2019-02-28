/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.basedata.itemclass.entity;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 物料附表Entity
 * 
 * @author dongqida
 * @version 2018-04-02
 */
public class ItemClassF extends DataEntity<ItemClassF> {

	private static final long serialVersionUID = 1L;
	private String classCode; // 物料类型编码
	private String className; // 物料类型名称
	private ItemClass fatherClassCode; // 父物料类名称 父类
	private String fatherCLassName; // 父物料名称
	private String isLeaf; // 叶节点标识
	private String systemSign; // 系统标识
	

	public ItemClassF() {
		super();
	}

	public ItemClassF(String id) {
		super(id);
	}

	public ItemClassF(ItemClass fatherClassCode) {
		this.fatherClassCode = fatherClassCode;
	}

	@ExcelField(title = "物料类型编码", align = 2, sort = 7)
	public String getClassCode() {
		return classCode;
	}

	public void setClassCode(String classCode) {
		this.classCode = classCode;
	}

	@ExcelField(title = "物料类型名称", align = 2, sort = 8)
	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public ItemClass getFatherClassCode() {
		return fatherClassCode;
	}

	public void setFatherClassCode(ItemClass fatherClassCode) {
		this.fatherClassCode = fatherClassCode;
	}

	@ExcelField(title = "父物料名称", align = 2, sort = 10)
	public String getFatherCLassName() {
		return fatherCLassName;
	}

	public void setFatherCLassName(String fatherCLassName) {
		this.fatherCLassName = fatherCLassName;
	}

	@ExcelField(title = "叶节点标识", align = 2, sort = 11)
	public String getIsLeaf() {
		return isLeaf;
	}

	public void setIsLeaf(String isLeaf) {
		this.isLeaf = isLeaf;
	}

	@ExcelField(title = "系统标识", dictType = "systemSign", align = 2, sort = 12)
	public String getSystemSign() {
		return systemSign;
	}

	public void setSystemSign(String systemSign) {
		this.systemSign = systemSign;
	}

}