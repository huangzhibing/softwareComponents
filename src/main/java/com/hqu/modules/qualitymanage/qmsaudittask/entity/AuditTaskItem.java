/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.qualitymanage.qmsaudittask.entity;

import com.hqu.modules.qualitymanage.qmsaudittask.entity.Audittask;
import javax.validation.constraints.NotNull;
import com.hqu.modules.qualitymanage.qmsauditstd.entity.Auditstd;
import com.jeeplus.modules.sys.entity.User;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 稽核任务项Entity
 * @author syc
 * @version 2018-05-25
 */
public class AuditTaskItem extends DataEntity<AuditTaskItem> {
	
	private static final long serialVersionUID = 1L;
	private String sn;		// 序号
	private Audittask audittask;		// 稽核任务编号 父类
	private String audittName;		// 稽核任务名称
	private Auditstd auditstd;		// 稽核流程ID
	private String auditpName;		// 稽核流程名称
	private String auditItemCode;		// 稽核标准要素编码
	private String auditItemName;		// 稽核标准要素名称
	private String auditDeptCode;		// 稽核部门编号
	private String auditDeptName;		// 稽核部门名称
	private User auditer;		// 内审员编号
	private String auditGmName;		// 内审员名称
	private Date auditDate;		// 审核完成时间
	private String isMeet;		// 是否符合
	private String remark;		// 不符合项说明
	private User tracker;		// 追踪人编号
	private String trackpName;		// 追踪人名称
	private String isCorrect;		// 不符合项是否已纠正
	private Date beginAuditDate;		// 开始 审核完成时间
	private Date endAuditDate;		// 结束 审核完成时间
	
	public AuditTaskItem() {
		super();
	}

	public AuditTaskItem(String id){
		super(id);
	}

	public AuditTaskItem(Audittask audittask){
		this.audittask = audittask;
	}

	@ExcelField(title="序号", align=2, sort=7)
	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}
	
	@NotNull(message="稽核任务编号不能为空")
	public Audittask getAudittask() {
		return audittask;
	}

	public void setAudittask(Audittask audittask) {
		this.audittask = audittask;
	}
	
	@ExcelField(title="稽核任务名称", align=2, sort=9)
	public String getAudittName() {
		return audittName;
	}

	public void setAudittName(String audittName) {
		this.audittName = audittName;
	}
	
	@NotNull(message="稽核流程ID不能为空")
	@ExcelField(title="稽核流程ID", fieldType=Auditstd.class, value="auditstd.auditpCode", align=2, sort=10)
	public Auditstd getAuditstd() {
		return auditstd;
	}

	public void setAuditstd(Auditstd auditstd) {
		this.auditstd = auditstd;
	}
	
	@ExcelField(title="稽核流程名称", align=2, sort=11)
	public String getAuditpName() {
		return auditpName;
	}

	public void setAuditpName(String auditpName) {
		this.auditpName = auditpName;
	}
	
	@ExcelField(title="稽核标准要素编码", align=2, sort=12)
	public String getAuditItemCode() {
		return auditItemCode;
	}

	public void setAuditItemCode(String auditItemCode) {
		this.auditItemCode = auditItemCode;
	}
	
	@ExcelField(title="稽核标准要素名称", align=2, sort=13)
	public String getAuditItemName() {
		return auditItemName;
	}

	public void setAuditItemName(String auditItemName) {
		this.auditItemName = auditItemName;
	}
	
	@ExcelField(title="稽核部门编号", align=2, sort=14)
	public String getAuditDeptCode() {
		return auditDeptCode;
	}

	public void setAuditDeptCode(String auditDeptCode) {
		this.auditDeptCode = auditDeptCode;
	}
	
	@ExcelField(title="稽核部门名称", align=2, sort=15)
	public String getAuditDeptName() {
		return auditDeptName;
	}

	public void setAuditDeptName(String auditDeptName) {
		this.auditDeptName = auditDeptName;
	}
	
	@NotNull(message="内审员编号不能为空")
	@ExcelField(title="内审员编号", fieldType=User.class, value="auditer.no", align=2, sort=16)
	public User getAuditer() {
		return auditer;
	}

	public void setAuditer(User auditer) {
		this.auditer = auditer;
	}
	
	@ExcelField(title="内审员名称", align=2, sort=17)
	public String getAuditGmName() {
		return auditGmName;
	}

	public void setAuditGmName(String auditGmName) {
		this.auditGmName = auditGmName;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@NotNull(message="审核完成时间不能为空")
	@ExcelField(title="审核完成时间", align=2, sort=18)
	public Date getAuditDate() {
		return auditDate;
	}

	public void setAuditDate(Date auditDate) {
		this.auditDate = auditDate;
	}
	
	@ExcelField(title="是否符合", dictType="auditCriterion", align=2, sort=19)
	public String getIsMeet() {
		return isMeet;
	}

	public void setIsMeet(String isMeet) {
		this.isMeet = isMeet;
	}
	
	@ExcelField(title="不符合项说明", align=2, sort=20)
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	@NotNull(message="追踪人编号不能为空")
	@ExcelField(title="追踪人编号", fieldType=User.class, value="tracker.no", align=2, sort=21)
	public User getTracker() {
		return tracker;
	}

	public void setTracker(User tracker) {
		this.tracker = tracker;
	}
	
	@ExcelField(title="追踪人名称", align=2, sort=22)
	public String getTrackpName() {
		return trackpName;
	}

	public void setTrackpName(String trackpName) {
		this.trackpName = trackpName;
	}
	
	@ExcelField(title="不符合项是否已纠正", align=2, sort=23)
	public String getIsCorrect() {
		return isCorrect;
	}

	public void setIsCorrect(String isCorrect) {
		this.isCorrect = isCorrect;
	}
	
	public Date getBeginAuditDate() {
		return beginAuditDate;
	}

	public void setBeginAuditDate(Date beginAuditDate) {
		this.beginAuditDate = beginAuditDate;
	}
	
	public Date getEndAuditDate() {
		return endAuditDate;
	}

	public void setEndAuditDate(Date endAuditDate) {
		this.endAuditDate = endAuditDate;
	}
		
}