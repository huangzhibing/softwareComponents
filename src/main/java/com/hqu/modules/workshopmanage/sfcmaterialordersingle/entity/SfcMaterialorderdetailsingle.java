/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.workshopmanage.sfcmaterialordersingle.entity;


import com.hqu.modules.workshopmanage.materialorder.entity.SfcMaterialOrder;
import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 原单行领料单Entity
 * @author xhc
 * @version 2018-12-09
 */
public class SfcMaterialorderdetailsingle extends DataEntity<SfcMaterialorderdetailsingle> {
	
	private static final long serialVersionUID = 1L;
	private SfcMaterialordermainsingle materialOrder;		// 领料单号 父类
	private Integer sequenceid;		// 序号
	private String materialid;		// 材料代码
	private String materialname;		// 材料名称
	private String numunit;		// 计量单位
	private String materialspec;		// 材料规格
	private Double requirenum;		// 申领数量
	private Double receivenum;		// 实发数量
	private Double receivednum;		// 已领数量
	private Double unitprice;		// 计划价格
	private Double sumprice;		// 计划金额
	private Double realprice;		// 实际价格
	private Double realsum;		// 实际金额
	private String mpsid;		// MPS号
	private String projid;		// 工程号
	private String orderid;		// 令单号
	private String taskid;		// 任务号
	private String batchid;		// 批号
	private String itemcode;		// 零件代码
	private String itemname;		// 零件名称
	private Double ordernum;		// 计划数量
	private Double sizeperitem;		// 单件下料尺寸
	private String operno;		// 工序号
	private String opercode;		// 工序代码
	private String itembatch;		// 批次
	private String binid;		// 货区代码
	private String binname;		// 货区名称
	private String locid;		// 货位代码
	private String locname;		// 货位名称
	private String materialorderdetail;		// 领料单号
	private String islack;		// Y缺料N不缺料
	private Integer lackqty;		// 缺料数量
	
	public SfcMaterialorderdetailsingle() {
		super();
	}

	public SfcMaterialorderdetailsingle(String id){
		super(id);
	}

	//public String getMaterialorder() {
	//	return materialorder;
	//}

	//public void setMaterialorder(String materialorder) {
	//	this.materialorder = materialorder;
	//}


	public SfcMaterialorderdetailsingle(SfcMaterialordermainsingle materialOrder){
		this.materialOrder = materialOrder;
	}

	public SfcMaterialordermainsingle getMaterialOrder() {
		return materialOrder;
	}

	public void setMaterialOrder(SfcMaterialordermainsingle materialOrder) {
		this.materialOrder = materialOrder;
	}

	@ExcelField(title="序号", align=2, sort=8)
	public Integer getSequenceid() {
		return sequenceid;
	}

	public void setSequenceid(Integer sequenceid) {
		this.sequenceid = sequenceid;
	}
	
	@ExcelField(title="材料代码", align=2, sort=9)
	public String getMaterialid() {
		return materialid;
	}

	public void setMaterialid(String materialid) {
		this.materialid = materialid;
	}
	
	@ExcelField(title="材料名称", align=2, sort=10)
	public String getMaterialname() {
		return materialname;
	}

	public void setMaterialname(String materialname) {
		this.materialname = materialname;
	}
	
	@ExcelField(title="计量单位", align=2, sort=11)
	public String getNumunit() {
		return numunit;
	}

	public void setNumunit(String numunit) {
		this.numunit = numunit;
	}
	
	@ExcelField(title="材料规格", align=2, sort=12)
	public String getMaterialspec() {
		return materialspec;
	}

	public void setMaterialspec(String materialspec) {
		this.materialspec = materialspec;
	}
	
	@ExcelField(title="申领数量", align=2, sort=13)
	public Double getRequirenum() {
		return requirenum;
	}

	public void setRequirenum(Double requirenum) {
		this.requirenum = requirenum;
	}
	
	@ExcelField(title="实发数量", align=2, sort=14)
	public Double getReceivenum() {
		return receivenum;
	}

	public void setReceivenum(Double receivenum) {
		this.receivenum = receivenum;
	}
	
	@ExcelField(title="已领数量", align=2, sort=15)
	public Double getReceivednum() {
		return receivednum;
	}

	public void setReceivednum(Double receivednum) {
		this.receivednum = receivednum;
	}
	
	@ExcelField(title="计划价格", align=2, sort=16)
	public Double getUnitprice() {
		return unitprice;
	}

	public void setUnitprice(Double unitprice) {
		this.unitprice = unitprice;
	}
	
	@ExcelField(title="计划金额", align=2, sort=17)
	public Double getSumprice() {
		return sumprice;
	}

	public void setSumprice(Double sumprice) {
		this.sumprice = sumprice;
	}
	
	@ExcelField(title="实际价格", align=2, sort=18)
	public Double getRealprice() {
		return realprice;
	}

	public void setRealprice(Double realprice) {
		this.realprice = realprice;
	}
	
	@ExcelField(title="实际金额", align=2, sort=19)
	public Double getRealsum() {
		return realsum;
	}

	public void setRealsum(Double realsum) {
		this.realsum = realsum;
	}
	
	@ExcelField(title="MPS号", align=2, sort=20)
	public String getMpsid() {
		return mpsid;
	}

	public void setMpsid(String mpsid) {
		this.mpsid = mpsid;
	}
	
	@ExcelField(title="工程号", align=2, sort=21)
	public String getProjid() {
		return projid;
	}

	public void setProjid(String projid) {
		this.projid = projid;
	}
	
	@ExcelField(title="令单号", align=2, sort=22)
	public String getOrderid() {
		return orderid;
	}

	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}
	
	@ExcelField(title="任务号", align=2, sort=23)
	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	
	@ExcelField(title="批号", align=2, sort=24)
	public String getBatchid() {
		return batchid;
	}

	public void setBatchid(String batchid) {
		this.batchid = batchid;
	}
	
	@ExcelField(title="零件代码", align=2, sort=25)
	public String getItemcode() {
		return itemcode;
	}

	public void setItemcode(String itemcode) {
		this.itemcode = itemcode;
	}
	
	@ExcelField(title="零件名称", align=2, sort=26)
	public String getItemname() {
		return itemname;
	}

	public void setItemname(String itemname) {
		this.itemname = itemname;
	}
	
	@ExcelField(title="计划数量", align=2, sort=27)
	public Double getOrdernum() {
		return ordernum;
	}

	public void setOrdernum(Double ordernum) {
		this.ordernum = ordernum;
	}
	
	@ExcelField(title="单件下料尺寸", align=2, sort=28)
	public Double getSizeperitem() {
		return sizeperitem;
	}

	public void setSizeperitem(Double sizeperitem) {
		this.sizeperitem = sizeperitem;
	}
	
	@ExcelField(title="工序号", align=2, sort=29)
	public String getOperno() {
		return operno;
	}

	public void setOperno(String operno) {
		this.operno = operno;
	}
	
	@ExcelField(title="工序代码", align=2, sort=30)
	public String getOpercode() {
		return opercode;
	}

	public void setOpercode(String opercode) {
		this.opercode = opercode;
	}
	
	@ExcelField(title="批次", align=2, sort=31)
	public String getItembatch() {
		return itembatch;
	}

	public void setItembatch(String itembatch) {
		this.itembatch = itembatch;
	}
	
	@ExcelField(title="货区代码", align=2, sort=32)
	public String getBinid() {
		return binid;
	}

	public void setBinid(String binid) {
		this.binid = binid;
	}
	
	@ExcelField(title="货区名称", align=2, sort=33)
	public String getBinname() {
		return binname;
	}

	public void setBinname(String binname) {
		this.binname = binname;
	}
	
	@ExcelField(title="货位代码", align=2, sort=34)
	public String getLocid() {
		return locid;
	}

	public void setLocid(String locid) {
		this.locid = locid;
	}
	
	@ExcelField(title="货位名称", align=2, sort=35)
	public String getLocname() {
		return locname;
	}

	public void setLocname(String locname) {
		this.locname = locname;
	}
	
	@ExcelField(title="领料单号", align=2, sort=36)
	public String getMaterialorderdetail() {
		return materialorderdetail;
	}

	public void setMaterialorderdetail(String materialorderdetail) {
		this.materialorderdetail = materialorderdetail;
	}
	
	@ExcelField(title="Y缺料N不缺料", dictType="sfc_is_lack", align=2, sort=37)
	public String getIslack() {
		return islack;
	}

	public void setIslack(String islack) {
		this.islack = islack;
	}
	
	@ExcelField(title="缺料数量", align=2, sort=38)
	public Integer getLackqty() {
		return lackqty;
	}

	public void setLackqty(Integer lackqty) {
		this.lackqty = lackqty;
	}
	
}