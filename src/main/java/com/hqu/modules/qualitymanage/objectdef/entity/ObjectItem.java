/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.qualitymanage.objectdef.entity;

import com.hqu.modules.qualitymanage.qualitynorm.entity.QualityNorm;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 检验对象子表Entity
 * @author M1ngz
 * @version 2018-04-07
 */
public class ObjectItem extends DataEntity<ObjectItem> {
	
	private static final long serialVersionUID = 1L;
	private ObjectDef obj;		// 检验对象编码 父类
	private String objName;		// 检验对象名称
	private QualityNorm qcNorm;		// 检验标准号
	private String QCNormName;		// 检验标准名称
	
	public ObjectItem() {
		super();
	}

	public ObjectItem(String id){
		super(id);
	}

	public ObjectItem(ObjectDef obj){
		this.obj = obj;
	}

	public ObjectDef getObj() {
		return obj;
	}

	public void setObj(ObjectDef obj) {
		this.obj = obj;
	}
	
	@ExcelField(title="检验对象名称", align=2, sort=8)
	public String getObjName() {
		return objName;
	}

	public void setObjName(String objName) {
		this.objName = objName;
	}
	
	@ExcelField(title="检验标准号", fieldType=QualityNorm.class, value="qcNorm.qnormname", align=2, sort=9)
	public QualityNorm getQcNorm() {
		return qcNorm;
	}

	public void setQcNorm(QualityNorm qcNorm) {
		this.qcNorm = qcNorm;
	}
	
	@ExcelField(title="检验标准名称", align=2, sort=10)
	public String getQCNormName() {
		return QCNormName;
	}

	public void setQCNormName(String QCNormName) {
		this.QCNormName = QCNormName;
	}
	
}