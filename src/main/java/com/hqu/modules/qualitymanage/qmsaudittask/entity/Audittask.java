/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.qualitymanage.qmsaudittask.entity;

import com.jeeplus.modules.sys.entity.User;
import javax.validation.constraints.NotNull;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.List;
import com.google.common.collect.Lists;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 稽核任务Entity
 * @author syc
 * @version 2018-05-25
 */
public class Audittask extends DataEntity<Audittask> {
	
	private static final long serialVersionUID = 1L;
	private String sn;		// 序号
	private String audittId;		// 稽核任务编号
	private String audittName;		// 稽核任务名称
	private User auditGrouper;		// 审核组长编号
	private String auditGlName;		// 审核组长名称
	private Date auditDate;		// 审核完成时间
	private String auditGoal;		// 审核目的
	private String auditScope;		// 审核范围
	private String auditResult;		// 审核结果综述
	private Date beginAuditDate;		// 开始 审核完成时间
	private Date endAuditDate;		// 结束 审核完成时间
	private List<AuditTaskItem> auditTaskItemList = Lists.newArrayList();		// 子表列表
	
	public Audittask() {
		super();
	}

	public Audittask(String id){
		super(id);
	}

	@ExcelField(title="序号", align=2, sort=7)
	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}
	
	@ExcelField(title="稽核任务编号", align=2, sort=8)
	public String getAudittId() {
		return audittId;
	}

	public void setAudittId(String audittId) {
		this.audittId = audittId;
	}
	
	@ExcelField(title="稽核任务名称", align=2, sort=9)
	public String getAudittName() {
		return audittName;
	}

	public void setAudittName(String audittName) {
		this.audittName = audittName;
	}
	
	@NotNull(message="审核组长编号不能为空")
	@ExcelField(title="审核组长编号", fieldType=User.class, value="auditGrouper.no", align=2, sort=10)
	public User getAuditGrouper() {
		return auditGrouper;
	}

	public void setAuditGrouper(User auditGrouper) {
		this.auditGrouper = auditGrouper;
	}
	
	@ExcelField(title="审核组长名称", align=2, sort=11)
	public String getAuditGlName() {
		return auditGlName;
	}

	public void setAuditGlName(String auditGlName) {
		this.auditGlName = auditGlName;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@NotNull(message="审核完成时间不能为空")
	@ExcelField(title="审核完成时间", align=2, sort=12)
	public Date getAuditDate() {
		return auditDate;
	}

	public void setAuditDate(Date auditDate) {
		this.auditDate = auditDate;
	}
	
	@ExcelField(title="审核目的", align=2, sort=13)
	public String getAuditGoal() {
		return auditGoal;
	}

	public void setAuditGoal(String auditGoal) {
		this.auditGoal = auditGoal;
	}
	
	@ExcelField(title="审核范围", align=2, sort=14)
	public String getAuditScope() {
		return auditScope;
	}

	public void setAuditScope(String auditScope) {
		this.auditScope = auditScope;
	}
	
	@ExcelField(title="审核结果综述", align=2, sort=15)
	public String getAuditResult() {
		return auditResult;
	}

	public void setAuditResult(String auditResult) {
		this.auditResult = auditResult;
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
		
	public List<AuditTaskItem> getAuditTaskItemList() {
		return auditTaskItemList;
	}

	public void setAuditTaskItemList(List<AuditTaskItem> auditTaskItemList) {
		this.auditTaskItemList = auditTaskItemList;
	}
}