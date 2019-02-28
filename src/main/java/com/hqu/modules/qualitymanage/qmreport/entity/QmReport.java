/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.qualitymanage.qmreport.entity;

import com.hqu.modules.qualitymanage.matterhandling.entity.MatterHandling;
import com.hqu.modules.qualitymanage.mattertype.entity.MatterType;
import javax.validation.constraints.NotNull;

import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.jeeplus.core.persistence.ActEntity;
import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 质量问题报告Entity
 * @author yangxianbang
 * @version 2018-05-06
 */
public class QmReport extends ActEntity<QmReport> {
	
	private static final long serialVersionUID = 1L;
	private String sn;		// 序号
	private String qmreportId;		// 问题处理报告单编号
	private String matterCode;		// 问题类型编码
	private MatterType type;		// 问题类型名称
	private User qmPerson;		// 问题处理人
	private Date qmDate;		// 问题处理日期
	private String mhandlingCode;		// 问题处理编码
	private MatterHandling mhandlingName;		// 问题处理意见
	private String state;		// 单据状态
	private Date beginQmDate;		// 开始 问题处理日期
	private Date endQmDate;		// 结束 问题处理日期
	private List<QmReportRSn> qmReportRSnList = Lists.newArrayList();		// 子表列表

	private String rndFul;
	private String qmType;
	private String userDeptCode;
	private Task task;
	private Map<String, Object> variables;
	// 运行中的流程实例
	private ProcessInstance processInstance;
	// 历史的流程实例
	private HistoricProcessInstance historicProcessInstance;
	// 流程定义
	private ProcessDefinition processDefinition;

	private String processInstanceId; // 流程实例编号


	public String getRndFul() {
		return rndFul;
	}

	public void setRndFul(String rndFul) {
		this.rndFul = rndFul;
	}

	public String getQmType() {
		return qmType;
	}

	public void setQmType(String qmType) {
		this.qmType = qmType;
	}

	public String getUserDeptCode() {
		return userDeptCode;
	}

	public void setUserDeptCode(String deptCode) {
		this.userDeptCode = deptCode;
	}

	public String getMatterCode() {
		return matterCode;
	}

	public void setMatterCode(String matterCode) {
		this.matterCode = matterCode;
	}

	public MatterType getType() {
		return type;
	}

	public void setType(MatterType type) {
		this.type = type;
	}

	public Task getTask() {
		return task;
	}

	public void setTask(Task task) {
		this.task = task;
	}

	public Map<String, Object> getVariables() {
		return variables;
	}

	public void setVariables(Map<String, Object> variables) {
		this.variables = variables;
	}

	public ProcessInstance getProcessInstance() {
		return processInstance;
	}

	public void setProcessInstance(ProcessInstance processInstance) {
		this.processInstance = processInstance;
	}

	public HistoricProcessInstance getHistoricProcessInstance() {
		return historicProcessInstance;
	}

	public void setHistoricProcessInstance(HistoricProcessInstance historicProcessInstance) {
		this.historicProcessInstance = historicProcessInstance;
	}

	public ProcessDefinition getProcessDefinition() {
		return processDefinition;
	}

	public void setProcessDefinition(ProcessDefinition processDefinition) {
		this.processDefinition = processDefinition;
	}

	public String getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	public QmReport() {
		super();
	}

	public QmReport(String id){
		super(id);
	}

	@ExcelField(title="序号", align=2, sort=6)
	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}
	
	@ExcelField(title="问题处理报告单编号", align=2, sort=7)
	public String getQmreportId() {
		return qmreportId;
	}

	public void setQmreportId(String qmreportId) {
		this.qmreportId = qmreportId;
	}
	
	@ExcelField(title="问题处理人", align=2, sort=8)
	public User getQmPerson() {
		return qmPerson;
	}

	public void setQmPerson(User qmPerson) {
		this.qmPerson = qmPerson;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@NotNull(message="问题处理日期不能为空")
	@ExcelField(title="问题处理日期", align=2, sort=9)
	public Date getQmDate() {
		return qmDate;
	}

	public void setQmDate(Date qmDate) {
		this.qmDate = qmDate;
	}
	
	@ExcelField(title="问题处理编码", align=2, sort=10)
	public String getMhandlingCode() {
		return mhandlingCode;
	}

	public void setMhandlingCode(String mhandlingCode) {
		this.mhandlingCode = mhandlingCode;
	}
	
	@NotNull(message="问题处理意见不能为空")
	@ExcelField(title="问题处理意见", fieldType=MatterHandling.class, value="mhandlingName.mhandlingname", align=2, sort=11)
	public MatterHandling getMhandlingName() {
		return mhandlingName;
	}

	public void setMhandlingName(MatterHandling mhandlingName) {
		this.mhandlingName = mhandlingName;
	}
	
	@ExcelField(title="单据状态", dictType="done_or_not", align=2, sort=12)
	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
	
	public Date getBeginQmDate() {
		return beginQmDate;
	}

	public void setBeginQmDate(Date beginQmDate) {
		this.beginQmDate = beginQmDate;
	}
	
	public Date getEndQmDate() {
		return endQmDate;
	}

	public void setEndQmDate(Date endQmDate) {
		this.endQmDate = endQmDate;
	}
		
	public List<QmReportRSn> getQmReportRSnList() {
		return qmReportRSnList;
	}

	public void setQmReportRSnList(List<QmReportRSn> qmReportRSnList) {
		this.qmReportRSnList = qmReportRSnList;
	}
}