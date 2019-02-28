/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.workshopmanage.processmaterialdetail.entity;


import com.jeeplus.core.persistence.DataEntity;
import com.hqu.modules.workshopmanage.processroutinedetail.entity.ProcessRoutineDetail;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

import java.util.Date;

/**
 * 车间物料安装明细Entity
 * @author chen
 * @version 2018-08-10
 */
public class ProcessMaterialDetail extends DataEntity<ProcessMaterialDetail> {
	
	private static final long serialVersionUID = 1L;
	private String routineBillNo;		// 加工路线单号
	private Integer seqNo;		// 单件序号
	private String routingCode;		// 工艺编码
	private String routingName;		// 工艺名称
	private String itemCode;		// 物料编码
	private String itemName;		// 物料名称
	private String itemSN;		// 物料编号
	private String centerCode;		// 工作中心代码
	private Integer assembleQty; //数量
	private Integer finishedQty; //已安装数量

	private String assemblePersonID;//装配人员
	private String assemblePersonName;

	private Date assembleTime;//装配时间
	private String hasQualityPro;//是否有品质异常
	private String qualityProblem;//异常类型

	private Date assembleTimeBegin;//时间范围查询条件
	private Date assembleTimeEnd;//


	public ProcessMaterialDetail(ProcessRoutineDetail processRoutineDetail) {
		routineBillNo=processRoutineDetail.getRoutinebillno();
		seqNo=processRoutineDetail.getSeqno();
		routingCode=processRoutineDetail.getRoutingcode();
	}
	public ProcessMaterialDetail(){
		super();
	}
	public ProcessMaterialDetail(String id){
		super(id);
	}

	@ExcelField(title="加工路线单号", align=2, sort=6)
	public String getRoutineBillNo() {
		return routineBillNo;
	}

	public void setRoutineBillNo(String routineBillNo) {
		this.routineBillNo = routineBillNo;
	}
	
	@ExcelField(title="单件序号", align=2, sort=7)
	public Integer getSeqNo() {
		return seqNo;
	}

	public void setSeqNo(Integer seqNo) {
		this.seqNo = seqNo;
	}
	
	@ExcelField(title="工艺编码", align=2, sort=8)
	public String getRoutingCode() {
		return routingCode;
	}

	public void setRoutingCode(String routingCode) {
		this.routingCode = routingCode;
	}
	
	@ExcelField(title="工艺名称", align=2, sort=9)
	public String getRoutingName() {
		return routingName;
	}

	public void setRoutingName(String routingName) {
		this.routingName = routingName;
	}
	
	@ExcelField(title="物料编码", align=2, sort=10)
	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}
	
	@ExcelField(title="物料名称", align=2, sort=11)
	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	
	@ExcelField(title="物料编号", align=2, sort=12)
	public String getItemSN() {
		return itemSN;
	}

	public void setItemSN(String itemSN) {
		this.itemSN = itemSN;
	}
	
	@ExcelField(title="工作中心代码", align=2, sort=13)
	public String getCenterCode() {
		return centerCode;
	}

	public void setCenterCode(String centerCode) {
		this.centerCode = centerCode;
	}

	public Integer getAssembleQty() {
		return assembleQty;
	}

	public void setAssembleQty(Integer assembleQty) {
		this.assembleQty = assembleQty;
	}

	public Integer getFinishedQty() {
		return finishedQty;
	}

	public void setFinishedQty(Integer finishedQty) {
		this.finishedQty = finishedQty;
	}

	public String getHasQualityPro() {
		return hasQualityPro;
	}

	public void setHasQualityPro(String hasQualityPro) {
		this.hasQualityPro = hasQualityPro;
	}

	public String getQualityProblem() {
		return qualityProblem;
	}

	public void setQualityProblem(String qualityProblem) {
		this.qualityProblem = qualityProblem;
	}

	public Date getAssembleTime() {
		return assembleTime;
	}

	public void setAssembleTime(Date assembleTime) {
		this.assembleTime = assembleTime;
	}

	public Date getAssembleTimeBegin() {
		return assembleTimeBegin;
	}

	public void setAssembleTimeBegin(Date assembleTimeBegin) {
		this.assembleTimeBegin = assembleTimeBegin;
	}

	public Date getAssembleTimeEnd() {
		return assembleTimeEnd;
	}

	public void setAssembleTimeEnd(Date assembleTimeEnd) {
		this.assembleTimeEnd = assembleTimeEnd;
	}

	public String getAssemblePersonID() {
		return assemblePersonID;
	}

	public void setAssemblePersonID(String assemblePersonID) {
		this.assemblePersonID = assemblePersonID;
	}

	public String getAssemblePersonName() {
		return assemblePersonName;
	}

	public void setAssemblePersonName(String assemblePersonName) {
		this.assemblePersonName = assemblePersonName;
	}
}