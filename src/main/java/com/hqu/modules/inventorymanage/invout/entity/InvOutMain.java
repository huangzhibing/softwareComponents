/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.inventorymanage.invout.entity;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import javax.validation.constraints.NotNull;

import com.jeeplus.core.persistence.ActEntity;
import com.jeeplus.modules.sys.entity.User;
import com.hqu.modules.inventorymanage.warehouse.entity.WareHouse;
import com.jeeplus.modules.sys.entity.Office;
import com.hqu.modules.inventorymanage.billmain.entity.BillMain;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;

/**
 * 放行单Entity
 * @author M1ngz
 * @version 2018-06-02
 */
public class InvOutMain extends ActEntity<InvOutMain> {
	
	private static final long serialVersionUID = 1L;
	private String billNum;		// 单据号
	private Date makeDate;		// 制单日期
	private User makeEmp;		// 制单人编码
	private String makeEmpName;		// 制单人名称
	private String billFlag;		// 单据状态
	private WareHouse ware;		// 仓库编码
	private String wareName;		// 仓库名称
	private User wareEmp;		// 库管员编码
	private String wareEmpName;		// 库管员姓名
	private Office dept;		// 部门编码
	private String deptName;		// 部门名称
	private String carrierName;		// 承运商名称
	private String carNum;		// 承运车号
	private Date awayDate;		// 出厂日期
	private BillMain corBillNum;		// 对应单号
	private String processInstanceId;		// 流程实例ID
	private Date beginMakeDate;		// 开始 制单日期
	private Date endMakeDate;		// 结束 制单日期
	private List<InvOutDetail> invOutDetailList = Lists.newArrayList();		// 子表列表

	// 流程任务
	private Task task;
	private Map<String, Object> variables;
	// 运行中的流程实例
	private ProcessInstance processInstance;
	// 历史的流程实例
	private HistoricProcessInstance historicProcessInstance;
	// 流程定义
	private ProcessDefinition processDefinition;

	private Map temp;

	public Map getTemp() {
		return temp;
	}

	public void setTemp(Map temp) {
		this.temp = temp;
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

	public InvOutMain() {
		super();
	}

	public InvOutMain(String id){
		super(id);
	}

	@ExcelField(title="单据号", align=2, sort=7)
	public String getBillNum() {
		return billNum;
	}

	public void setBillNum(String billNum) {
		this.billNum = billNum;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@NotNull(message="制单日期不能为空")
	@ExcelField(title="制单日期", align=2, sort=8)
	public Date getMakeDate() {
		return makeDate;
	}

	public void setMakeDate(Date makeDate) {
		this.makeDate = makeDate;
	}
	
	@NotNull(message="制单人编码不能为空")
	@ExcelField(title="制单人编码", fieldType=User.class, value="makeEmp.no", align=2, sort=9)
	public User getMakeEmp() {
		return makeEmp;
	}

	public void setMakeEmp(User makeEmp) {
		this.makeEmp = makeEmp;
	}
	
	@ExcelField(title="制单人名称", align=2, sort=10)
	public String getMakeEmpName() {
		return makeEmpName;
	}

	public void setMakeEmpName(String makeEmpName) {
		this.makeEmpName = makeEmpName;
	}
	
	@ExcelField(title="单据状态", dictType="pbillStat", align=2, sort=11)
	public String getBillFlag() {
		return billFlag;
	}

	public void setBillFlag(String billFlag) {
		this.billFlag = billFlag;
	}
	
	@NotNull(message="仓库编码不能为空")
	@ExcelField(title="仓库编码", fieldType=WareHouse.class, value="ware.wareID", align=2, sort=12)
	public WareHouse getWare() {
		return ware;
	}

	public void setWare(WareHouse ware) {
		this.ware = ware;
	}
	
	@ExcelField(title="仓库名称", align=2, sort=13)
	public String getWareName() {
		return wareName;
	}

	public void setWareName(String wareName) {
		this.wareName = wareName;
	}
	
	@NotNull(message="库管员编码不能为空")
	@ExcelField(title="库管员编码", fieldType=User.class, value="wareEmp.no", align=2, sort=14)
	public User getWareEmp() {
		return wareEmp;
	}

	public void setWareEmp(User wareEmp) {
		this.wareEmp = wareEmp;
	}
	
	@ExcelField(title="库管员姓名", align=2, sort=15)
	public String getWareEmpName() {
		return wareEmpName;
	}

	public void setWareEmpName(String wareEmpName) {
		this.wareEmpName = wareEmpName;
	}
	
	@NotNull(message="部门编码不能为空")
	@ExcelField(title="部门编码", fieldType=Office.class, value="dept.code", align=2, sort=16)
	public Office getDept() {
		return dept;
	}

	public void setDept(Office dept) {
		this.dept = dept;
	}
	
	@ExcelField(title="部门名称", align=2, sort=17)
	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	
	@ExcelField(title="承运商名称", align=2, sort=18)
	public String getCarrierName() {
		return carrierName;
	}

	public void setCarrierName(String carrierName) {
		this.carrierName = carrierName;
	}
	
	@ExcelField(title="承运车号", align=2, sort=19)
	public String getCarNum() {
		return carNum;
	}

	public void setCarNum(String carNum) {
		this.carNum = carNum;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@NotNull(message="出厂日期不能为空")
	@ExcelField(title="出厂日期", align=2, sort=20)
	public Date getAwayDate() {
		return awayDate;
	}

	public void setAwayDate(Date awayDate) {
		this.awayDate = awayDate;
	}
	
	@NotNull(message="对应单号不能为空")
	@ExcelField(title="对应单号", align=2, sort=21)
	public BillMain getCorBillNum() {
		return corBillNum;
	}

	public void setCorBillNum(BillMain corBillNum) {
		this.corBillNum = corBillNum;
	}
	
	@ExcelField(title="流程实例ID", align=2, sort=22)
	public String getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}
	
	public Date getBeginMakeDate() {
		return beginMakeDate;
	}

	public void setBeginMakeDate(Date beginMakeDate) {
		this.beginMakeDate = beginMakeDate;
	}
	
	public Date getEndMakeDate() {
		return endMakeDate;
	}

	public void setEndMakeDate(Date endMakeDate) {
		this.endMakeDate = endMakeDate;
	}
		
	public List<InvOutDetail> getInvOutDetailList() {
		return invOutDetailList;
	}

	public void setInvOutDetailList(List<InvOutDetail> invOutDetailList) {
		this.invOutDetailList = invOutDetailList;
	}
}