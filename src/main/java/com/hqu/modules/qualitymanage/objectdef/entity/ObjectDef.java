/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.qualitymanage.objectdef.entity;

import com.hqu.modules.qualitymanage.qmsobjecttype.entity.ObjectType;
import javax.validation.constraints.NotNull;
import java.util.List;
import com.google.common.collect.Lists;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 检验对象定义Entity
 * @author M1ngz
 * @version 2018-04-07
 */
public class ObjectDef extends DataEntity<ObjectDef> {
	
	private static final long serialVersionUID = 1L;
	private String objName;		// 检验对象名称
	private String objId;
	private String objCode;		// 检验对象编码
	private ObjectType objt;		// 检验对象类型编码
	private String objtNameRu;		// 检验对象类型名称
	private String mpFlag;		// 采购/制造
	private String isSale;		// 是否销售
	private String qualityNorm;		// 执行标准
	private String sampNorm;		// 抽样标准
	private String checkMethod;		// 检验方法
	private String sampMethod;		// 采样方法
	private String qmsFlag;			//	质检标志 @zz
	private List<ObjectItem> objectItemList = Lists.newArrayList();		// 子表列表


	public String getObjId() {
		return objId;
	}

	public void setObjId(String objId) {
		this.objId = objId;
	}

	public ObjectDef() {
		super();
	}

	public ObjectDef(String id){
		super(id);
	}

	@ExcelField(title="检验对象名称", align=2, sort=7)
	public String getObjName() {
		return objName;
	}

	public void setObjName(String objName) {
		this.objName = objName;
	}
	
	@ExcelField(title="检验对象编码", align=2, sort=8)
	public String getObjCode() {
		return objCode;
	}

	public void setObjCode(String objCode) {
		this.objCode = objCode;
	}
	
	@NotNull(message="检验对象类型编码不能为空")
	@ExcelField(title="检验对象类型编码", fieldType=ObjectType.class, value="objt.objtcode", align=2, sort=9)
	public ObjectType getObjt() {
		return objt;
	}

	public void setObjt(ObjectType objt) {
		this.objt = objt;
	}
	
	@ExcelField(title="检验对象类型名称", align=2, sort=10)
	public String getObjtNameRu() {
		return objtNameRu;
	}

	public void setObjtNameRu(String objtNameRu) {
		this.objtNameRu = objtNameRu;
	}
	
	@ExcelField(title="采购/制造", dictType="MP_Flag", align=2, sort=11)
	public String getMpFlag() {
		return mpFlag;
	}

	public void setMpFlag(String mpFlag) {
		this.mpFlag = mpFlag;
	}
	
	@ExcelField(title="是否销售", dictType="Is_Sale", align=2, sort=12)
	public String getIsSale() {
		return isSale;
	}

	public void setIsSale(String isSale) {
		this.isSale = isSale;
	}
	
	@ExcelField(title="执行标准", align=2, sort=13)
	public String getQualityNorm() {
		return qualityNorm;
	}

	public void setQualityNorm(String qualityNorm) {
		this.qualityNorm = qualityNorm;
	}
	
	@ExcelField(title="抽样标准", align=2, sort=14)
	public String getSampNorm() {
		return sampNorm;
	}

	public void setSampNorm(String sampNorm) {
		this.sampNorm = sampNorm;
	}
	
	@ExcelField(title="检验方法", align=2, sort=15)
	public String getCheckMethod() {
		return checkMethod;
	}

	public void setCheckMethod(String checkMethod) {
		this.checkMethod = checkMethod;
	}
	
	@ExcelField(title="采样方法", dictType="Samp_Method", align=2, sort=16)
	public String getSampMethod() {
		return sampMethod;
	}

	public void setSampMethod(String sampMethod) {
		this.sampMethod = sampMethod;
	}
	
	public List<ObjectItem> getObjectItemList() {
		return objectItemList;
	}

	public void setObjectItemList(List<ObjectItem> objectItemList) {
		this.objectItemList = objectItemList;
	}

	public String getQmsFlag() {
		return qmsFlag;
	}

	public void setQmsFlag(String qmsFlag) {
		this.qmsFlag = qmsFlag;
	}
}