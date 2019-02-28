/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.qualitymanage.purreport.entity;

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
 * @version 2018-04-05
 */
public class PurReportRSn extends DataEntity<PurReportRSn> {
	
	private static final long serialVersionUID = 1L;
	private PurReport PurReport;		// 检验单编号 父类
	private String objSn;		// 检验对象序列号
	private ObjectDef objectDef;		// 检验对象编码
	private String objName;		// 检验对象名称
	private String checkResult;		// 检验结论
	private Date checkDate;		// 检验日期
	private String checkTime;		// 检验时间
	private String cpersionCode;		// 检验人代码
	private String checkPerson;		// 检验人名称
	private String memo;		// 备注
	private String isQmatter;		// 质量问题是否已处理
	private String mserialno;		// 机台序列码
	
	//-------------------------------------------tq
	

	private String objCode;		// 检验对象编码
	
	
	
	//------------------------------------------xb
	
	private String seriaNo;
	private String itemCode;
	private String itemName;
	private String justifyLog;		// 审核历史
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public String getObjCode() {
		return objCode;
	}

	public void setObjCode(String objCode) {
		this.objCode = objCode;
	}

	public QCNorm getqCNorm() {
		return qCNorm;
	}

	public void setqCNorm(QCNorm qCNorm) {
		this.qCNorm = qCNorm;
	}

	public String getMatterCode() {
		return matterCode;
	}

	public void setMatterCode(String matterCode) {
		this.matterCode = matterCode;
	}

	public MatterType getMatterType() {
		return matterType;
	}

	public void setMatterType(MatterType matterType) {
		this.matterType = matterType;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	private QCNorm qCNorm;		// 检验标准名称
	
	private String matterCode;		// 问题类型编码
	private MatterType matterType;		// 问题类型名称
	
	public PurReportRSn() {
		super();
		this.setIdType(IDTYPE_AUTO);
	}

	public PurReportRSn(String id){
		super(id);
	}

	public PurReportRSn(PurReport PurReport){
		this.PurReport = PurReport;
	}

	public PurReport getPurReport() {
		return PurReport;
	}

	public void setPurReport(PurReport PurReport) {
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
	
	
	
	public String getMserialno() {
		return mserialno;
	}

	public void setMserialno(String mserialno) {
		this.mserialno = mserialno;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="检验日期", align=2, sort=12)
	public Date getCheckDate() {
		return checkDate;
	}

	public void setCheckDate(Date checkDate) {
		this.checkDate = checkDate;
	}
	
	@ExcelField(title="检验时间", align=2, sort=13)
	public String getCheckTime() {
		return checkTime;
	}

	public void setCheckTime(String checkTime) {
		this.checkTime = checkTime;
	}
	
	@ExcelField(title="检验人代码", align=2, sort=14)
	public String getCpersionCode() {
		return cpersionCode;
	}

	public void setCpersionCode(String cpersionCode) {
		this.cpersionCode = cpersionCode;
	}
	
	@ExcelField(title="检验人名称", align=2, sort=15)
	public String getCheckPerson() {
		return checkPerson;
	}

	public void setCheckPerson(String checkPerson) {
		this.checkPerson = checkPerson;
	}
	
	@ExcelField(title="备注", align=2, sort=16)
	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}
	
	@ExcelField(title="检验标准名称", fieldType=QCNorm.class, value="qCNorm.qcnormname", align=2, sort=17)
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

	public String getSeriaNo() {
		return seriaNo;
	}

	public void setSeriaNo(String seriaNo) {
		this.seriaNo = seriaNo;
	}

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getJustifyLog() {
		return justifyLog;
	}

	public void setJustifyLog(String justifyLog) {
		this.justifyLog = justifyLog;
	}
	
	
	
}