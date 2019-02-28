/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.workshopmanage.processmaterial.entity;

import javax.validation.constraints.NotNull;


import com.jeeplus.core.persistence.DataEntity;
import com.hqu.modules.workshopmanage.processroutinedetail.entity.ProcessRoutineDetail;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 车间物料安装单Entity
 * @author chen
 * @version 2018-08-10
 */
public class ProcessMaterial extends DataEntity<ProcessMaterial> {
	
	private static final long serialVersionUID = 1L;
	private String routineBillNo;		// 加工路线单号
	private Integer seqNo;		// 单件序号
	private String routingCode;		// 工艺编码
	private String routingName;		// 工艺编码
	private String itemCode;		// 物料编码
	private String itemName;		// 物料名称
	private Double assembleQty;		// 需安装数量
	private Double finishedQty;		// 已安装数量
	private String centerCode;		// 工作中心代码
	private String centerName;		// 工作中心代码
	private String objSn; //机器二维码
	private String mSerialNo;//机器序列号

	public ProcessMaterial(ProcessRoutineDetail processRoutineDetail) {
		routineBillNo=processRoutineDetail.getRoutinebillno();
		seqNo=processRoutineDetail.getSeqno();
		routingCode=processRoutineDetail.getRoutingcode();
	}

	public ProcessMaterial(){
		super();
	}


	public ProcessMaterial(String id){
		super(id);
	}

	@ExcelField(title="加工路线单号", align=2, sort=7)
	public String getRoutineBillNo() {
		return routineBillNo;
	}

	public void setRoutineBillNo(String routineBillNo) {
		this.routineBillNo = routineBillNo;
	}
	
	@NotNull(message="单件序号不能为空")
	@ExcelField(title="单件序号", align=2, sort=8)
	public Integer getSeqNo() {
		return seqNo;
	}

	public void setSeqNo(Integer seqNo) {
		this.seqNo = seqNo;
	}
	
	@ExcelField(title="工艺编码", align=2, sort=9)
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

	@ExcelField(title="物料名称", align=2, sort=10)
	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	@NotNull(message="需安装数量不能为空")
	@ExcelField(title="需安装数量", align=2, sort=11)
	public Double getAssembleQty() {
		return assembleQty;
	}

	public void setAssembleQty(Double assembleQty) {
		this.assembleQty = assembleQty;
	}
	
	@NotNull(message="已安装数量不能为空")
	@ExcelField(title="已安装数量", align=2, sort=12)
	public Double getFinishedQty() {
		return finishedQty;
	}

	public void setFinishedQty(Double finishedQty) {
		this.finishedQty = finishedQty;
	}
	
	@ExcelField(title="工作中心代码", align=2, sort=13)
	public String getCenterCode() {
		return centerCode;
	}

	public void setCenterCode(String centerCode) {
		this.centerCode = centerCode;
	}

	@ExcelField(title="工作中心名称", align=2, sort=13)
	public String getCenterName() {
		return centerName;
	}

	public void setCenterName(String centerName) {
		this.centerName = centerName;
	}

	public String getObjSn() {
		return objSn;
	}

	public void setObjSn(String objSn) {
		this.objSn = objSn;
	}

	public String getmSerialNo() {
		return mSerialNo;
	}

	public void setmSerialNo(String mSerialNo) {
		this.mSerialNo = mSerialNo;
	}
}