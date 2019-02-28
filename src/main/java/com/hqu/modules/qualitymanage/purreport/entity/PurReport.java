/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.qualitymanage.purreport.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotNull;

import com.hqu.modules.qualitymanage.verifyqcnorm.entity.VerifyQCNorm;
import com.hqu.modules.workshopmanage.sfcinvcheckmain.entity.SfcInvCheckMain;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.common.collect.Lists;
import com.hqu.modules.inventorymanage.invcheckmain.entity.ReinvCheckMain;
import com.hqu.modules.qualitymanage.purinvcheckmain.entity.InvCheckMain;
import com.jeeplus.common.utils.excel.annotation.ExcelField;
import com.jeeplus.core.persistence.ActEntity;
import com.jeeplus.modules.sys.entity.Office;

/**
 * 检验单(采购/装配/整机检测)Entity
 * @author 孙映川
 * @version 2018-04-05
 */
public class PurReport extends ActEntity<PurReport> {
	
	private static final long serialVersionUID = 1L;

	private String reportId;		// 检验单编号
	private String billNum;		// 单据编号
	private InvCheckMain invCheckMain;		// 采购报检单编号
	private String isAudit;		// 是否审核
	private String checktCode;		// 检验类型编码
	private String checktName;		// 检验类型名称
	private String rndFul;		// 全检/抽检
	private String qualityNorm;		// 执行标准
	private String cdeptCode;		// 检验部门编码
	private Office office;		// 检验部门名称
	private String cpositionCode;		// 检验岗位编码
	private String cpositionName;		// 检验岗位名称
	//测试同步
	private Date checkDate;		// 检验日期
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	private String sendState;
	private String checkTime;		// 检验时间
	private String cpersonCode;		// 检验人代码
	private String checkPerson;		// 检验人名称
	private String checkResult;		// 检验结论
	private String jdeptCode;		// 审核部门编码
	private Office deptOffice;		// 审核部门名称
	private String jpositionCode;		// 审核岗位编码
	private String jpositionName;		// 审核岗位名称
	private Date justifyDate;		// 审核日期
	private String justifyRemark;		// 审核不通过意见 
	private SfcInvCheckMain sfcInvCheckMain;		// 完工入库单编号
	private double percentPass;		// 合格率
	
	private String scanCode;


	private ReinvCheckMain reinvCheckMain;		// 超期复检单编号
	private String jpersonCode;		// 审核人代码
	private String justifyPerson;		// 审核人
	private String justifyResult;		// 审核结论
	private String memo;		// 备注
	private String state;		// 单据状态
	private String isQmatter;		// 质量问题是否已处理
	private List<PurReportRSn> purReportRSnList = Lists.newArrayList();		// 子表列表

	//追溯时安规检验列表
	private List<VerifyQCNorm> aList = new ArrayList<>();
	//追溯时老化检验列表
	private List<VerifyQCNorm> lList = new ArrayList<>();
	//追溯时激活检验列表
	private List<VerifyQCNorm> jList = new ArrayList<>();
	//追溯时包装检验列表
	private List<VerifyQCNorm> bList = new ArrayList<>();
	
	

	//------------activiti
	private String processInstanceId; // 流程实例编号
	//-- 临时属性 --//
	// 流程任务
	private Task task;
	private Map<String, Object> variables;
	// 运行中的流程实例
	private ProcessInstance processInstance;
	// 历史的流程实例
	private HistoricProcessInstance historicProcessInstance;
	// 流程定义
	private ProcessDefinition processDefinition;

	
	
	
	//-----------tq
	private String billnum;		// 采购报检单编号
	private String jdeptName;		// 审核部门名称
	private Date beginCheckDate;		// 开始 检验日期
	private Date endCheckDate;		// 结束 检验日期
	
	//============zz
	private String audit;		// 待检验
	
	private String userDeptCode;
	
	//================xb
	

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public String getBillnum() {
		return billnum;
	}

	public void setBillnum(String billnum) {
		this.billnum = billnum;
	}

	public String getJdeptName() {
		return jdeptName;
	}

	public void setJdeptName(String jdeptName) {
		this.jdeptName = jdeptName;
	}

	public Date getBeginCheckDate() {
		return beginCheckDate;
	}

	public void setBeginCheckDate(Date beginCheckDate) {
		this.beginCheckDate = beginCheckDate;
	}

	public Date getEndCheckDate() {
		return endCheckDate;
	}

	public void setEndCheckDate(Date endCheckDate) {
		this.endCheckDate = endCheckDate;
	}

	public PurReport() {
		super();
		this.setIdType(IDTYPE_AUTO);
	}

	public PurReport(String id){
		super(id);
	}

	@ExcelField(title="检验单编号", align=2, sort=7)
	public String getReportId() {
		return reportId;
	}

	public void setReportId(String reportId) {
		this.reportId = reportId;
	}
	
	@NotNull(message="采购报检单编号不能为空")
	@ExcelField(title="采购报检单编号", fieldType=InvCheckMain.class, value="invCheckMain.billnum", align=2, sort=8)
	public InvCheckMain getInvCheckMain() {
		return invCheckMain;
	}

	public void setInvCheckMain(InvCheckMain invCheckMain) {
		this.invCheckMain = invCheckMain;
	}
	
	@ExcelField(title="检验类型编码", align=2, sort=9)
	public String getChecktCode() {
		return checktCode;
	}

	public void setChecktCode(String checktCode) {
		this.checktCode = checktCode;
	}
	
	@ExcelField(title="检验类型名称", align=2, sort=10)
	public String getChecktName() {
		return checktName;
	}

	public void setChecktName(String checktName) {
		this.checktName = checktName;
	}
	
	@ExcelField(title="全检/抽检", dictType="checkType", align=2, sort=11)
	public String getRndFul() {
		return rndFul;
	}

	public void setRndFul(String rndFul) {
		this.rndFul = rndFul;
	}
	
	@ExcelField(title="执行标准", align=2, sort=12)
	public String getQualityNorm() {
		return qualityNorm;
	}

	public void setQualityNorm(String qualityNorm) {
		this.qualityNorm = qualityNorm;
	}
	
	@ExcelField(title="检验部门编码", align=2, sort=13)
	public String getCdeptCode() {
		return cdeptCode;
	}

	public void setCdeptCode(String cdeptCode) {
		this.cdeptCode = cdeptCode;
	}
	
	@ExcelField(title="检验部门名称", fieldType=Office.class, value="office.name", align=2, sort=14)
	public Office getOffice() {
		return office;
	}

	public void setOffice(Office office) {
		this.office = office;
	}
	
	@ExcelField(title="检验岗位编码", align=2, sort=15)
	public String getCpositionCode() {
		return cpositionCode;
	}

	public void setCpositionCode(String cpositionCode) {
		this.cpositionCode = cpositionCode;
	}
	
	@ExcelField(title="检验岗位名称", align=2, sort=16)
	public String getCpositionName() {
		return cpositionName;
	}

	public void setCpositionName(String cpositionName) {
		this.cpositionName = cpositionName;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="检验日期", align=2, sort=17)
	public Date getCheckDate() {
		return checkDate;
	}

	public void setCheckDate(Date checkDate) {
		this.checkDate = checkDate;
	}
	
	@ExcelField(title="检验时间", align=2, sort=18)
	public String getCheckTime() {
		return checkTime;
	}

	public void setCheckTime(String checkTime) {
		this.checkTime = checkTime;
	}
	
	@ExcelField(title="检验人代码", align=2, sort=19)
	public String getCpersonCode() {
		return cpersonCode;
	}

	public void setCpersonCode(String cpersonCode) {
		this.cpersonCode = cpersonCode;
	}
	
	@ExcelField(title="检验人名称", align=2, sort=20)
	public String getCheckPerson() {
		return checkPerson;
	}

	public void setCheckPerson(String checkPerson) {
		this.checkPerson = checkPerson;
	}
	
	@ExcelField(title="检验结论", align=2, sort=21)
	public String getCheckResult() {
		return checkResult;
	}

	public void setCheckResult(String checkResult) {
		this.checkResult = checkResult;
	}
	
	@ExcelField(title="审核部门编码", align=2, sort=22)
	public String getJdeptCode() {
		return jdeptCode;
	}

	public void setJdeptCode(String jdeptCode) {
		this.jdeptCode = jdeptCode;
	}
	
	@ExcelField(title="审核部门名称", fieldType=Office.class, value="deptOffice.name", align=2, sort=23)
	public Office getDeptOffice() {
		return deptOffice;
	}

	public void setDeptOffice(Office deptOffice) {
		this.deptOffice = deptOffice;
	}
	
	@ExcelField(title="审核岗位编码", align=2, sort=24)
	public String getJpositionCode() {
		return jpositionCode;
	}

	public void setJpositionCode(String jpositionCode) {
		this.jpositionCode = jpositionCode;
	}
	
	@ExcelField(title="审核岗位名称", align=2, sort=25)
	public String getJpositionName() {
		return jpositionName;
	}

	public void setJpositionName(String jpositionName) {
		this.jpositionName = jpositionName;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="审核日期", align=2, sort=26)
	public Date getJustifyDate() {
		return justifyDate;
	}

	public void setJustifyDate(Date justifyDate) {
		this.justifyDate = justifyDate;
	}
	
	@ExcelField(title="审核人代码", align=2, sort=27)
	public String getJpersonCode() {
		return jpersonCode;
	}

	public void setJpersonCode(String jpersonCode) {
		this.jpersonCode = jpersonCode;
	}
	
	@ExcelField(title="审核人", align=2, sort=28)
	public String getJustifyPerson() {
		return justifyPerson;
	}

	public void setJustifyPerson(String justifyPerson) {
		this.justifyPerson = justifyPerson;
	}
	
	@ExcelField(title="审核结论", align=2, sort=29)
	public String getJustifyResult() {
		return justifyResult;
	}

	public void setJustifyResult(String justifyResult) {
		this.justifyResult = justifyResult;
	}
	
	@ExcelField(title="备注", align=2, sort=30)
	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}
	
	@ExcelField(title="单据状态", dictType="state", align=2, sort=31)
	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
	
	public List<PurReportRSn> getPurReportRSnList() {
		return purReportRSnList;
	}

	public void setPurReportRSnList(List<PurReportRSn> purReportRSnList) {
		this.purReportRSnList = purReportRSnList;
	}
	@ExcelField(title="质量问题是否已处理", align=2, sort=33)
	public String getIsQmatter() {
		return isQmatter;
	}

	public void setIsQmatter(String isQmatter) {
		this.isQmatter = isQmatter;
	}
	public String getBillNum() {
		return billNum;
	}

	public void setBillNum(String billNum) {
		this.billNum = billNum;
	}
	public String getJustifyRemark() {
		return justifyRemark;
	}

	public void setJustifyRemark(String justifyRemark) {
		this.justifyRemark = justifyRemark;
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

	@ExcelField(title="完工入库单编号", fieldType=SfcInvCheckMain.class, value="sfcInvCheckMain.billNo", align=2, sort=34)
	public SfcInvCheckMain getSfcInvCheckMain() {
		return sfcInvCheckMain;
	}

	public void setSfcInvCheckMain(SfcInvCheckMain sfcInvCheckMain) {
		this.sfcInvCheckMain = sfcInvCheckMain;
	}

	public String getUserDeptCode() {
		return userDeptCode;
	}

	public void setUserDeptCode(String userDeptCode) {
		this.userDeptCode = userDeptCode;
	}

	public double getPercentPass() {
		return percentPass;
	}

	public void setPercentPass(double percentPass) {
		this.percentPass = percentPass;
	}

	public String getIsAudit() {
		return isAudit;
	}

	public void setIsAudit(String isAudit) {
		this.isAudit = isAudit;
	}

	public String getAudit() {
		return audit;
	}

	public void setAudit(String audit) {
		this.audit = audit;
	}

	public ReinvCheckMain getReinvCheckMain() {
		return reinvCheckMain;
	}

	public void setReinvCheckMain(ReinvCheckMain reinvCheckMain) {
		this.reinvCheckMain = reinvCheckMain;
	}

	public String getSendState() {
		return sendState;
	}

	public void setSendState(String sendState) {
		this.sendState = sendState;
	}

	public String getScanCode() {
		return scanCode;
	}

	public void setScanCode(String scanCode) {
		this.scanCode = scanCode;
	}


	public List<VerifyQCNorm> getaList() {
		return aList;
	}

	public void setaList(List<VerifyQCNorm> aList) {
		this.aList = aList;
	}

	public List<VerifyQCNorm> getlList() {
		return lList;
	}

	public void setlList(List<VerifyQCNorm> lList) {
		this.lList = lList;
	}

	public List<VerifyQCNorm> getjList() {
		return jList;
	}

	public void setjList(List<VerifyQCNorm> jList) {
		this.jList = jList;
	}

	public List<VerifyQCNorm> getbList() {
		return bList;
	}

	public void setbList(List<VerifyQCNorm> bList) {
		this.bList = bList;
	}

	public static long getSerialVersionUID() {

		return serialVersionUID;
	}
}