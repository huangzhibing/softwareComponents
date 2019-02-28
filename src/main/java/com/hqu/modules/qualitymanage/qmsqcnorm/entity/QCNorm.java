/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.qualitymanage.qmsqcnorm.entity;
//import com.hqu.modules.qualitymanage.ObjectDef.entity.ObjectDef;
import javax.validation.constraints.NotNull;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.List;
import com.google.common.collect.Lists;
import com.hqu.modules.qualitymanage.objectdef.entity.ObjectDef;
import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 检验标准定义Entity
 * @author luyumiao
 * @version 2018-05-05
 */
public class QCNorm extends DataEntity<QCNorm> {
	
	private static final long serialVersionUID = 1L;
	private String qcnormId;		// 检验标准号
	private String qcnormName;		// 检验标准名称
	private ObjectDef qnobj;		// 检验对象编码
	private String objNameRu;		// 检验对象名称
	private String qualityNorm;		// 执行标准
	private String compilePerson;		// 编制人
	private Date compileDate;		// 编制日期
	private Date beginDate;		// 生效日期
	private Date endDate;		// 失效日期
	private Date beginBeginDate;		// 开始 生效日期
	private Date endBeginDate;		// 结束 生效日期
	private String qualityNormFile;  //执行标准附件
	
	private List<QCNormItem> qCNormItemList = Lists.newArrayList();		// 子表列表
	
	public QCNorm() {
		super();
	}

	public QCNorm(String id){
		super(id);
	}

	public String getQualityNormFile() {
		return qualityNormFile;
	}

	public void setQualityNormFile(String qualityNormFile) {
		this.qualityNormFile = qualityNormFile;
	}
	
	@ExcelField(title="检验标准号", align=2, sort=7)
	public String getQcnormId() {
		return qcnormId;
	}

	public void setQcnormId(String qcnormId) {
		this.qcnormId = qcnormId;
	}
	
	@ExcelField(title="检验标准名称", align=2, sort=8)
	public String getQcnormName() {
		return qcnormName;
	}

	public void setQcnormName(String qcnormName) {
		this.qcnormName = qcnormName;
	}
	
	@NotNull(message="检验对象编码不能为空")
	@ExcelField(title="检验对象编码", fieldType=ObjectDef.class, value="qnobj.objCode", align=2, sort=9)
	public ObjectDef getQnobj() {
		return qnobj;
	}

	public void setQnobj(ObjectDef qnobj) {
		this.qnobj = qnobj;
	}
	
	@ExcelField(title="检验对象名称", align=2, sort=10)
	public String getObjNameRu() {
		return objNameRu;
	}

	public void setObjNameRu(String objNameRu) {
		this.objNameRu = objNameRu;
	}
	
	@ExcelField(title="执行标准", align=2, sort=11)
	public String getQualityNorm() {
		return qualityNorm;
	}

	public void setQualityNorm(String qualityNorm) {
		this.qualityNorm = qualityNorm;
	}
	
	@ExcelField(title="编制人", align=2, sort=12)
	public String getCompilePerson() {
		return compilePerson;
	}

	public void setCompilePerson(String compilePerson) {
		this.compilePerson = compilePerson;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="编制日期", align=2, sort=13)
	public Date getCompileDate() {
		return compileDate;
	}

	public void setCompileDate(Date compileDate) {
		this.compileDate = compileDate;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="生效日期", align=2, sort=14)
	public Date getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="失效日期", align=2, sort=15)
	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
	public Date getBeginBeginDate() {
		return beginBeginDate;
	}

	public void setBeginBeginDate(Date beginBeginDate) {
		this.beginBeginDate = beginBeginDate;
	}
	
	public Date getEndBeginDate() {
		return endBeginDate;
	}

	public void setEndBeginDate(Date endBeginDate) {
		this.endBeginDate = endBeginDate;
	}
		
	public List<QCNormItem> getQCNormItemList() {
		return qCNormItemList;
	}

	public void setQCNormItemList(List<QCNormItem> qCNormItemList) {
		this.qCNormItemList = qCNormItemList;
	}
}