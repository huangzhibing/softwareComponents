/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.qualitymanage.purreportquery.entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hqu.modules.qualitymanage.mattertype.entity.MatterType;
import com.hqu.modules.qualitymanage.objectdef.entity.ObjectDef;
import com.hqu.modules.qualitymanage.qmsqcnorm.entity.QCNorm;
import com.jeeplus.common.utils.excel.annotation.ExcelField;
import com.jeeplus.core.persistence.DataEntity;

/**
 * 质量管理检验抽检表Entity
 * @author 孙映川
 * @version 2018-04-17
 */
public class PurReportRSnQuery extends DataEntity<PurReportRSnQuery> {
	
	private static final long serialVersionUID = 1L;
	private PurReportQuery PurReport;		// 检验单编号 父类
	private String objSn;		// 检验对象序列号
	private ObjectDef objectDef;		// 检验对象编码
	private String objName;		// 检验对象名称
	private String checkResult;		// 检验结论
	private String matterCode;		// 问题类型编码
	private MatterType matterType;		// 问题类型名称
	private Date checkDate;		// 检验日期
	private String checkTime;		// 检验时间
	private String cpersionCode;		// 检验人代码
	private String checkPerson;		// 检验人名称
	private String memo;		// 备注
	private QCNorm qCNorm;		// 检验标准名称
	private String isQmatter;		// 质量问题是否已处理
	
	public PurReportRSnQuery() {
		super();
		this.setIdType(IDTYPE_AUTO);
	}

	public PurReportRSnQuery(String id){
		super(id);
	}

	public PurReportRSnQuery(PurReportQuery PurReport){
		this.PurReport = PurReport;
	}

	public PurReportQuery getPurReport() {
		return PurReport;
	}

	public void setPurReport(PurReportQuery PurReport) {
		this.PurReport = PurReport;
	}
	
	@ExcelField(title="检验对象序列号", align=2, sort=8)
	public String getObjSn() {
		return objSn;
	}

	public void setObjSn(String objSn) {
		this.objSn = objSn;
	}
	
	@ExcelField(title="检验对象编码", fieldType=ObjectDef.class, value="objectDef.objCode", align=2, sort=9)
	public ObjectDef getObjectDef() {
		return objectDef;
	}

	public void setObjectDef(ObjectDef objectDef) {
		this.objectDef = objectDef;
	}
	
	@ExcelField(title="检验对象名称", align=2, sort=10)
	public String getObjName() {
		return objName;
	}

	public void setObjName(String objName) {
		this.objName = objName;
	}
	
	@ExcelField(title="检验结论", dictType="isQualified", align=2, sort=11)
	public String getCheckResult() {
		return checkResult;
	}

	public void setCheckResult(String checkResult) {
		this.checkResult = checkResult;
	}
	
	@ExcelField(title="问题类型编码", align=2, sort=12)
	public String getMatterCode() {
		return matterCode;
	}

	public void setMatterCode(String matterCode) {
		this.matterCode = matterCode;
	}
	
	@ExcelField(title="问题类型名称", fieldType=MatterType.class, value="matterType.mattername", align=2, sort=13)
	public MatterType getMatterType() {
		return matterType;
	}

	public void setMatterType(MatterType matterType) {
		this.matterType = matterType;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="检验日期", align=2, sort=14)
	public Date getCheckDate() {
		return checkDate;
	}

	public void setCheckDate(Date checkDate) {
		this.checkDate = checkDate;
	}
	
	@ExcelField(title="检验时间", align=2, sort=15)
	public String getCheckTime() {
		return checkTime;
	}

	public void setCheckTime(String checkTime) {
		this.checkTime = checkTime;
	}
	
	@ExcelField(title="检验人代码", align=2, sort=16)
	public String getCpersionCode() {
		return cpersionCode;
	}

	public void setCpersionCode(String cpersionCode) {
		this.cpersionCode = cpersionCode;
	}
	
	@ExcelField(title="检验人名称", align=2, sort=17)
	public String getCheckPerson() {
		return checkPerson;
	}

	public void setCheckPerson(String checkPerson) {
		this.checkPerson = checkPerson;
	}
	
	@ExcelField(title="备注", align=2, sort=18)
	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}
	
	@ExcelField(title="检验标准名称", fieldType=QCNorm.class, value="qCNorm.qcnormname", align=2, sort=19)
	public QCNorm getQCNorm() {
		return qCNorm;
	}

	public void setQCNorm(QCNorm qCNorm) {
		this.qCNorm = qCNorm;
	}
	
	@ExcelField(title="质量问题是否已处理", align=2, sort=20)
	public String getIsQmatter() {
		return isQmatter;
	}

	public void setIsQmatter(String isQmatter) {
		this.isQmatter = isQmatter;
	}
	
}