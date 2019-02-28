/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.qualitymanage.qmreport.entity;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 不合格的检验对象Entity
 * @author yangxianbang
 * @version 2018-04-10
 */
public class QmReportRSn extends DataEntity<QmReportRSn> {
	
	private static final long serialVersionUID = 1L;
	private String sn;		// 序号
	private QmReport qmReportId;		// 问题处理报告单编号 父类
	private String reportId;		// 检验单编号
	private String billNum;		// 采购报检单编号
	private String objSn;		// 检验对象序列号
	private String objCode;		// 检验对象编码
	private String objName;		// 检验对象名称
	private String checkResult;		// 检验结论
	private String checkDate;		// 检验日期
	private String checkTime;		// 检验时间
	private String cPersonCode;		// 检验人代码
	private String checkPerson;		// 检验人名称
	private String matterCode;		// 问题类型编码
	private String matterName;		// 问题类型名称
	private String matterType;      //问题所属阶段
	private String memo;		// 备注

	private String isRetreat;//是否需要退库（装配检验时用）

	private String isDeal;//是否已处理
	private String dealResult;//处理结果


	private String purReportRSnId; //保存purReport检验对象的id

	public String getMatterType() {
		return matterType;
	}

	public void setMatterType(String matterType) {
		this.matterType = matterType;
	}

	public String getIsRetreat() {
		return isRetreat;
	}

	public String getIsDeal() {
		return isDeal;
	}

	public void setIsDeal(String isDeal) {
		this.isDeal = isDeal;
	}

	public String getDealResult() {
		return dealResult;
	}

	public void setDealResult(String dealResult) {
		this.dealResult = dealResult;
	}

	public void setIsRetreat(String isRetreat) {
		this.isRetreat = isRetreat;
	}

	public QmReportRSn() {
		super();
	}

	public QmReportRSn(String id){
		super(id);
	}

	public QmReportRSn(QmReport qmReportId){
		this.qmReportId = qmReportId;
	}

	@ExcelField(title="序号", align=2, sort=6)
	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}
	
	public QmReport getQmReportId() {
		return qmReportId;
	}

	public void setQmReportId(QmReport qmReportId) {
		this.qmReportId = qmReportId;
	}
	
	@ExcelField(title="检验单编号", align=2, sort=8)
	public String getReportId() {
		return reportId;
	}

	public void setReportId(String reportId) {
		this.reportId = reportId;
	}
	
	@ExcelField(title="采购报检单编号", align=2, sort=9)
	public String getBillNum() {
		return billNum;
	}

	public void setBillNum(String billNum) {
		this.billNum = billNum;
	}
	
	@ExcelField(title="检验对象序列号", align=2, sort=10)
	public String getObjSn() {
		return objSn;
	}

	public void setObjSn(String objSn) {
		this.objSn = objSn;
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
	
	@ExcelField(title="检验结论", align=2, sort=13)
	public String getCheckResult() {
		return checkResult;
	}

	public void setCheckResult(String checkResult) {
		this.checkResult = checkResult;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="检验日期", align=2, sort=14)
	public String getCheckDate() {
		return checkDate;
	}

	public void setCheckDate(String checkDate) {
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
	public String getCPersonCode() {
		return cPersonCode;
	}

	public void setCPersonCode(String cPersonCode) {
		this.cPersonCode = cPersonCode;
	}
	
	@ExcelField(title="检验人名称", align=2, sort=17)
	public String getCheckPerson() {
		return checkPerson;
	}

	public void setCheckPerson(String checkPerson) {
		this.checkPerson = checkPerson;
	}
	
	@ExcelField(title="问题类型编码", align=2, sort=18)
	public String getMatterCode() {
		return matterCode;
	}

	public void setMatterCode(String matterCode) {
		this.matterCode = matterCode;
	}
	
	@ExcelField(title="问题类型名称", align=2, sort=19)
	public String getMatterName() {
		return matterName;
	}

	public void setMatterName(String matterName) {
		this.matterName = matterName;
	}
	
	@ExcelField(title="备注", align=2, sort=20)
	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getPurReportRSnId() {
		return purReportRSnId;
	}

	public void setPurReportRSnId(String purReportRSnId) {
		this.purReportRSnId = purReportRSnId;
	}
	
}