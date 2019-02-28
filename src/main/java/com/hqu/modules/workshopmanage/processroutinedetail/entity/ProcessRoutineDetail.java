/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.workshopmanage.processroutinedetail.entity;

import com.google.common.collect.Lists;
import com.hqu.modules.basedata.workcenter.entity.WorkCenter;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hqu.modules.workshopmanage.processmaterial.entity.ProcessMaterial;
import com.hqu.modules.workshopmanage.processmaterialdetail.entity.ProcessMaterialDetail;
import com.jeeplus.modules.sys.entity.User;
import javax.validation.constraints.NotNull;
import com.hqu.modules.basedata.team.entity.Team;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 加工路线单明细单Entity
 * @author xhc
 * @version 2018-08-22
 */
public class ProcessRoutineDetail extends DataEntity<ProcessRoutineDetail> {
	
	private static final long serialVersionUID = 1L;
	private String processbillno;		// 车间计划作业号
	private Integer batchno;		// 批次序号
	private String routinebillno;		// 加工路线单号
	private Integer produceno;		// 生产序号
	private String routingcode;		// 工艺编码
	private String workprocedurecode;		// 工序编码
	private WorkCenter centercode;		// 工作中心代码
	private String islastrouting;		// 末道工艺标志
	private String prodcode;		// 产品编码
	private String planqty;		// 计划生产数量
	private Date planbdate;		// 计划生产日期
	private String realqty;		// 实际生产数量
	private Date planedate;		// 实际生产日期
	private String billstate;		// 单据状态
	private String assignedstate;		// 派工状态
	private String invcheckstate; //完工单生成状态
	private String makepid;		// 制定人
	private String actualcentercode;		// 实作工作中心
	private String personincharge;		// 负责人
	private Team teamcode;		// 计划班组
	private String actualteamcode;		// 实作班组
	private String shiftname;		// 班次
	private String workhour;		// 计划工时
	private String actualworkhour;		// 实作工时
	private Date makedate;		// 制定日期
	private String confirmpid;		// 提交人
	private Date confirmdate;		// 提交日期
	private String deliverypid;		// 下达人
	private Date deliverydate;		// 下达日期
	private String prodname;		// 产品名称
	private String routingname;		// 工艺名称
	private Integer seqno;		// 单件序号
	private Date actualBDate;//实际开始时间（安规激活老化）
	private Date beginPlanbdate;		// 开始 计划生产日期
	private Date endPlanbdate;		// 结束 计划生产日期
	private Date beginPlanedate;		// 开始 实际生产日期
	private Date endPlanedate;		// 结束 实际生产日期
	private Date beginMakedate;		// 开始 制定日期
	private Date endMakedate;		// 结束 制定日期
	private Date beginConfirmdate;		// 开始 提交日期
	private Date endConfirmdate;		// 结束 提交日期
	private Date beginDeliverydate;		// 开始 下达日期
	private Date endDeliverydate;		// 结束 下达日期
	private String mserialno;		// 机器序列号
	private String objSn; //机器二维码
	private String cosBillNum;

	private String hasQualityPro;//是否有零件品质异常

	private List<ProcessMaterial> processMaterialList= Lists.newArrayList();//物料安装单列表 临时变量

	private List<ProcessMaterialDetail> processMaterialDetailList=Lists.newArrayList();//物料安装明细列表 临时变量

	public ProcessRoutineDetail() {
		super();
	}

	public ProcessRoutineDetail(String id){
		super(id);
	}


	public List<ProcessMaterial> getProcessMaterialList() {
		return processMaterialList;
	}

	public void setProcessMaterialList(List<ProcessMaterial> processMaterialList) {
		this.processMaterialList = processMaterialList;
	}

	public List<ProcessMaterialDetail> getProcessMaterialDetailList() {
		return processMaterialDetailList;
	}

	public void setProcessMaterialDetailList(List<ProcessMaterialDetail> processMaterialDetailList) {
		this.processMaterialDetailList = processMaterialDetailList;
	}


	@ExcelField(title="车间计划作业号", align=2, sort=7)
	public String getProcessbillno() {
		return processbillno;
	}

	public void setProcessbillno(String processbillno) {
		this.processbillno = processbillno;
	}
	
	@ExcelField(title="批次序号", align=2, sort=8)
	public Integer getBatchno() {
		return batchno;
	}

	public void setBatchno(Integer batchno) {
		this.batchno = batchno;
	}
	
	@ExcelField(title="加工路线单号", align=2, sort=9)
	public String getRoutinebillno() {
		return routinebillno;
	}

	public void setRoutinebillno(String routinebillno) {
		this.routinebillno = routinebillno;
	}
	
	@ExcelField(title="生产序号", align=2, sort=10)
	public Integer getProduceno() {
		return produceno;
	}

	public void setProduceno(Integer produceno) {
		this.produceno = produceno;
	}
	
	@ExcelField(title="工艺编码", align=2, sort=11)
	public String getRoutingcode() {
		return routingcode;
	}

	public void setRoutingcode(String routingcode) {
		this.routingcode = routingcode;
	}
	
	@ExcelField(title="工序编码", align=2, sort=12)
	public String getWorkprocedurecode() {
		return workprocedurecode;
	}

	public void setWorkprocedurecode(String workprocedurecode) {
		this.workprocedurecode = workprocedurecode;
	}
	
	@ExcelField(title="工作中心代码", fieldType=WorkCenter.class, value="centercode.centerCode", align=2, sort=13)
	public WorkCenter getCentercode() {
		return centercode;
	}

	public void setCentercode(WorkCenter centercode) {
		this.centercode = centercode;
	}
	
	@ExcelField(title="末道工艺标志", align=2, sort=14)
	public String getIslastrouting() {
		return islastrouting;
	}

	public void setIslastrouting(String islastrouting) {
		this.islastrouting = islastrouting;
	}
	
	@ExcelField(title="产品编码", align=2, sort=15)
	public String getProdcode() {
		return prodcode;
	}

	public void setProdcode(String prodcode) {
		this.prodcode = prodcode;
	}
	
	@ExcelField(title="计划生产数量", align=2, sort=16)
	public String getPlanqty() {
		return planqty;
	}

	public void setPlanqty(String planqty) {
		this.planqty = planqty;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd")
	@ExcelField(title="计划生产日期", align=2, sort=17)
	public Date getPlanbdate() {
		return planbdate;
	}

	public void setPlanbdate(Date planbdate) {
		this.planbdate = planbdate;
	}
	
	@ExcelField(title="实际生产数量", align=2, sort=18)
	public String getRealqty() {
		return realqty;
	}

	public void setRealqty(String realqty) {
		this.realqty = realqty;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
	@ExcelField(title="实际生产日期", align=2, sort=19)
	public Date getPlanedate() {
		return planedate;
	}

	public void setPlanedate(Date planedate) {
		this.planedate = planedate;
	}
	
	@ExcelField(title="单据状态", align=2, sort=20)
	public String getBillstate() {
		return billstate;
	}

	public void setBillstate(String billstate) {
		this.billstate = billstate;
	}
	
	@ExcelField(title="派工状态", align=2, sort=21)
	public String getAssignedstate() {
		return assignedstate;
	}

	public void setAssignedstate(String assignedstate) {
		this.assignedstate = assignedstate;
	}
	
	@ExcelField(title="制定人", align=2, sort=22)
	public String getMakepid() {
		return makepid;
	}

	public void setMakepid(String makepid) {
		this.makepid = makepid;
	}
	
	@ExcelField(title="实作工作中心", align=2, sort=23)
	public String getActualcentercode() {
		return actualcentercode;
	}

	public void setActualcentercode(String actualcentercode) {
		this.actualcentercode = actualcentercode;
	}
	
	@NotNull(message="负责人不能为空")
	@ExcelField(title="负责人", fieldType=User.class, value="", align=2, sort=24)
	public String getPersonincharge() {
		return personincharge;
	}

	public void setPersonincharge(String personincharge) {
		this.personincharge = personincharge;
	}
	
	@NotNull(message="计划班组不能为空")
	@ExcelField(title="计划班组", fieldType=Team.class, value="teamcode.teamCode", align=2, sort=25)
	public Team getTeamcode() {
		return teamcode;
	}

	public void setTeamcode(Team teamcode) {
		this.teamcode = teamcode;
	}
	
	@ExcelField(title="实作班组", align=2, sort=26)
	public String getActualteamcode() {
		return actualteamcode;
	}

	public void setActualteamcode(String actualteamcode) {
		this.actualteamcode = actualteamcode;
	}
	
	@ExcelField(title="班次", align=2, sort=27)
	public String getShiftname() {
		return shiftname;
	}

	public void setShiftname(String shiftname) {
		this.shiftname = shiftname;
	}
	
	@ExcelField(title="计划工时", align=2, sort=28)
	public String getWorkhour() {
		return workhour;
	}

	public void setWorkhour(String workhour) {
		this.workhour = workhour;
	}
	
	@ExcelField(title="实作工时", align=2, sort=29)
	public String getActualworkhour() {
		return actualworkhour;
	}

	public void setActualworkhour(String actualworkhour) {
		this.actualworkhour = actualworkhour;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="制定日期", align=2, sort=30)
	public Date getMakedate() {
		return makedate;
	}

	public void setMakedate(Date makedate) {
		this.makedate = makedate;
	}
	
	@ExcelField(title="提交人", align=2, sort=31)
	public String getConfirmpid() {
		return confirmpid;
	}

	public void setConfirmpid(String confirmpid) {
		this.confirmpid = confirmpid;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="提交日期", align=2, sort=32)
	public Date getConfirmdate() {
		return confirmdate;
	}

	public void setConfirmdate(Date confirmdate) {
		this.confirmdate = confirmdate;
	}
	
	@ExcelField(title="下达人", align=2, sort=33)
	public String getDeliverypid() {
		return deliverypid;
	}

	public void setDeliverypid(String deliverypid) {
		this.deliverypid = deliverypid;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="下达日期", align=2, sort=34)
	public Date getDeliverydate() {
		return deliverydate;
	}

	public void setDeliverydate(Date deliverydate) {
		this.deliverydate = deliverydate;
	}
	
	@ExcelField(title="产品名称", align=2, sort=35)
	public String getProdname() {
		return prodname;
	}

	public void setProdname(String prodname) {
		this.prodname = prodname;
	}
	
	@ExcelField(title="工艺名称", align=2, sort=36)
	public String getRoutingname() {
		return routingname;
	}

	public void setRoutingname(String routingname) {
		this.routingname = routingname;
	}
	
	@ExcelField(title="单件序号", align=2, sort=37)
	public Integer getSeqno() {
		return seqno;
	}

	public void setSeqno(Integer seqno) {
		this.seqno = seqno;
	}
	
	public Date getBeginPlanbdate() {
		return beginPlanbdate;
	}

	public void setBeginPlanbdate(Date beginPlanbdate) {
		this.beginPlanbdate = beginPlanbdate;
	}
	
	public Date getEndPlanbdate() {
		return endPlanbdate;
	}

	public void setEndPlanbdate(Date endPlanbdate) {
		this.endPlanbdate = endPlanbdate;
	}
		
	public Date getBeginPlanedate() {
		return beginPlanedate;
	}

	public void setBeginPlanedate(Date beginPlanedate) {
		this.beginPlanedate = beginPlanedate;
	}
	
	public Date getEndPlanedate() {
		return endPlanedate;
	}

	public void setEndPlanedate(Date endPlanedate) {
		this.endPlanedate = endPlanedate;
	}
		
	public Date getBeginMakedate() {
		return beginMakedate;
	}

	public void setBeginMakedate(Date beginMakedate) {
		this.beginMakedate = beginMakedate;
	}
	
	public Date getEndMakedate() {
		return endMakedate;
	}

	public void setEndMakedate(Date endMakedate) {
		this.endMakedate = endMakedate;
	}
		
	public Date getBeginConfirmdate() {
		return beginConfirmdate;
	}

	public void setBeginConfirmdate(Date beginConfirmdate) {
		this.beginConfirmdate = beginConfirmdate;
	}
	
	public Date getEndConfirmdate() {
		return endConfirmdate;
	}

	public void setEndConfirmdate(Date endConfirmdate) {
		this.endConfirmdate = endConfirmdate;
	}
		
	public Date getBeginDeliverydate() {
		return beginDeliverydate;
	}

	public void setBeginDeliverydate(Date beginDeliverydate) {
		this.beginDeliverydate = beginDeliverydate;
	}
	
	public Date getEndDeliverydate() {
		return endDeliverydate;
	}

	public void setEndDeliverydate(Date endDeliverydate) {
		this.endDeliverydate = endDeliverydate;
	}

	public Date getActualBDate() {
		return actualBDate;
	}

	public void setActualBDate(Date actualBDate) {
		this.actualBDate = actualBDate;
	}

	public String getInvcheckstate() {
		return invcheckstate;
	}

	public void setInvcheckstate(String invcheckstate) {
		this.invcheckstate = invcheckstate;
	}

	public String getMserialno() {
		return mserialno;
	}

	public void setMserialno(String mserialno) {
		this.mserialno = mserialno;
	}

	public String getObjSn() {
		return objSn;
	}

	public void setObjSn(String objSn) {
		this.objSn = objSn;
	}

	public String getCosBillNum() {
		return cosBillNum;
	}

	public void setCosBillNum(String cosBillNum) {
		this.cosBillNum = cosBillNum;
	}

	public String getHasQualityPro() {
		return hasQualityPro;
	}

	public void setHasQualityPro(String hasQualityPro) {
		this.hasQualityPro = hasQualityPro;
	}
}