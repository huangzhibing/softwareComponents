/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.workshopmanage.materialorder.entity;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import javax.validation.constraints.NotNull;
import java.util.List;
import com.google.common.collect.Lists;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 领料单Entity
 * @author zhangxin
 * @version 2018-05-22
 */
public class SfcMaterialOrder extends DataEntity<SfcMaterialOrder> {
	
	private static final long serialVersionUID = 1L;
	private String materialOrder;		// 单据号
	private String receiveType;		// 领料类型
	private String qualityFlag; //良品标识
	private String ioType;		// 出入库标记
	private String billType;		// 单据类型
	private String outFlag;		// 出库标志
	private String materialType;		// 物料类型
	private String wareId;		// 库房代码
	private String wareName;		// 库房名称
	private String periodId;		// 核算期
	private String itemType;		// 领料类型
	private String shopId;		// 部门代码
	private String shopName;		// 部门名称
	private String responser;		// 领料人代码
	private String respName;		// 领料人姓名
	private String editor;		// 制单人名称
	private Date editDate;		// 制单日期
	private String useId;		// 用途代码
	private String useDesc;		// 用途名称
	private String editorId;		// 制单人编码
	private String billStateFlag;		// 单据状态
	private Date operDate;		// 业务日期
	private String wareEmpid;		// 库管员编码
	private String wareEmpname;		// 库管员名称
	private String operCode;		// 工位编码
	private String operName;		// 工位名称
	private String routineBillNo;		// 加工路线单号
	private String routingCode;		// 工艺号
	private String InvBillNo; //对应出库单号
	private String materialOrderInReturn; //退料对应领料单号

	private String isLack;//是否缺料

	private Date beginOperDate;		// begin 业务日期
	private Date endOperDate;		// end 业务日期
	
	private List<SfcMaterialOrderDetail> sfcMaterialOrderDetailList = Lists.newArrayList();		// 子表列表
	
	public SfcMaterialOrder() {
		super();
	}

	public SfcMaterialOrder(String id){
		super(id);
	}

	@ExcelField(title="单据号", align=2, sort=7)
	public String getMaterialOrder() {
		return materialOrder;
	}

	public void setMaterialOrder(String materialOrder) {
		this.materialOrder = materialOrder;
	}
	
	@ExcelField(title="领料类型", align=2, sort=8)
	public String getReceiveType() {
		return receiveType;
	}

	public void setReceiveType(String receiveType) {
		this.receiveType = receiveType;
	}
	
	@ExcelField(title="出入库标记", align=2, sort=9)
	public String getIoType() {
		return ioType;
	}

	public void setIoType(String ioType) {
		this.ioType = ioType;
	}
	
	@ExcelField(title="单据类型", align=2, sort=10)
	public String getBillType() {
		return billType;
	}

	public void setBillType(String billType) {
		this.billType = billType;
	}
	
	@ExcelField(title="出库标志", align=2, sort=11)
	public String getOutFlag() {
		return outFlag;
	}

	public void setOutFlag(String outFlag) {
		this.outFlag = outFlag;
	}
	
	@ExcelField(title="物料类型", align=2, sort=12)
	public String getMaterialType() {
		return materialType;
	}

	public void setMaterialType(String materialType) {
		this.materialType = materialType;
	}
	
	@ExcelField(title="库房代码", align=2, sort=13)
	public String getWareId() {
		return wareId;
	}

	public void setWareId(String wareId) {
		this.wareId = wareId;
	}
	
	@ExcelField(title="库房名称", align=2, sort=14)
	public String getWareName() {
		return wareName;
	}

	public void setWareName(String wareName) {
		this.wareName = wareName;
	}
	
	@ExcelField(title="核算期", align=2, sort=15)
	public String getPeriodId() {
		return periodId;
	}

	public void setPeriodId(String periodId) {
		this.periodId = periodId;
	}
	
	@ExcelField(title="领料类型", align=2, sort=16)
	public String getItemType() {
		return itemType;
	}

	public void setItemType(String itemType) {
		this.itemType = itemType;
	}
	
	@ExcelField(title="部门代码", align=2, sort=17)
	public String getShopId() {
		return shopId;
	}

	public void setShopId(String shopId) {
		this.shopId = shopId;
	}
	
	@ExcelField(title="部门名称", align=2, sort=18)
	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}
	
	@ExcelField(title="领料人代码", align=2, sort=19)
	public String getResponser() {
		return responser;
	}

	public void setResponser(String responser) {
		this.responser = responser;
	}
	
	@ExcelField(title="领料人姓名", align=2, sort=20)
	public String getRespName() {
		return respName;
	}

	public void setRespName(String respName) {
		this.respName = respName;
	}
	
	@ExcelField(title="制单人名称", align=2, sort=21)
	public String getEditor() {
		return editor;
	}

	public void setEditor(String editor) {
		this.editor = editor;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@NotNull(message="制单日期不能为空")
	@ExcelField(title="制单日期", align=2, sort=22)
	public Date getEditDate() {
		return editDate;
	}

	public void setEditDate(Date editDate) {
		this.editDate = editDate;
	}
	
	@ExcelField(title="用途代码", align=2, sort=23)
	public String getUseId() {
		return useId;
	}

	public void setUseId(String useId) {
		this.useId = useId;
	}
	
	@ExcelField(title="用途名称", align=2, sort=24)
	public String getUseDesc() {
		return useDesc;
	}

	public void setUseDesc(String useDesc) {
		this.useDesc = useDesc;
	}
	
	@ExcelField(title="制单人编码", align=2, sort=25)
	public String getEditorId() {
		return editorId;
	}

	public void setEditorId(String editorId) {
		this.editorId = editorId;
	}
	
	@ExcelField(title="单据状态", align=2, sort=26)
	public String getBillStateFlag() {
		return billStateFlag;
	}

	public void setBillStateFlag(String billStateFlag) {
		this.billStateFlag = billStateFlag;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd")
	@NotNull(message="业务日期不能为空")
	@ExcelField(title="业务日期", align=2, sort=27)
	public Date getOperDate() {
		return operDate;
	}

	public void setOperDate(Date operDate) {
		this.operDate = operDate;
	}
	
	@ExcelField(title="库管员编码", align=2, sort=28)
	public String getWareEmpid() {
		return wareEmpid;
	}

	public void setWareEmpid(String wareEmpid) {
		this.wareEmpid = wareEmpid;
	}
	
	@ExcelField(title="库管员名称", align=2, sort=29)
	public String getWareEmpname() {
		return wareEmpname;
	}

	public void setWareEmpname(String wareEmpname) {
		this.wareEmpname = wareEmpname;
	}
	
	@ExcelField(title="工位编码", align=2, sort=30)
	public String getOperCode() {
		return operCode;
	}

	public void setOperCode(String operCode) {
		this.operCode = operCode;
	}
	
	@ExcelField(title="工位名称", align=2, sort=31)
	public String getOperName() {
		return operName;
	}

	public void setOperName(String operName) {
		this.operName = operName;
	}
	
	@ExcelField(title="加工路线单号", align=2, sort=31)
	public String getroutineBillNo() {
		return routineBillNo;
	}

	public void setroutineBillNo(String routineBillNo) {
		this.routineBillNo = routineBillNo;
	}
	
	@ExcelField(title="工艺号", align=2, sort=31)
	public String getroutingCode() {
		return routingCode;
	}

	public void setroutingCode(String routingCode) {
		this.routingCode = routingCode;
	}
	
	@ExcelField(title="出库单号", align=2, sort=31)
	public String getInvBillNo() {
		return InvBillNo;
	}

	public void setInvBillNo(String InvBillNo) {
		this.InvBillNo = InvBillNo;
	}

	public Date getBeginOperDate() {
		return beginOperDate;
	}

	public void setBeginOperDate(Date beginOperDate) {
		this.beginOperDate = beginOperDate;
	}

	public Date getEndOperDate() {
		return endOperDate;
	}

	public void setEndOperDate(Date endOperDate) {
		this.endOperDate = endOperDate;
	}

	public List<SfcMaterialOrderDetail> getSfcMaterialOrderDetailList() {
		return sfcMaterialOrderDetailList;
	}

	public void setSfcMaterialOrderDetailList(List<SfcMaterialOrderDetail> sfcMaterialOrderDetailList) {
		this.sfcMaterialOrderDetailList = sfcMaterialOrderDetailList;
	}

	@ExcelField(title="退库对应领料单号", align=2, sort=31)
	public String getMaterialOrderInReturn() {
		return materialOrderInReturn;
	}

	public void setMaterialOrderInReturn(String materialOrderInReturn) {
		this.materialOrderInReturn = materialOrderInReturn;
	}

	public String getIsLack() {
		return isLack;
	}

	public void setIsLack(String isLack) {
		this.isLack = isLack;
	}

	public String getQualityFlag() {
		return qualityFlag;
	}

	public void setQualityFlag(String qualityFlag) {
		this.qualityFlag = qualityFlag;
	}
}