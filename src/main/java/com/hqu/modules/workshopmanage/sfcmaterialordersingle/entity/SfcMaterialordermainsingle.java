/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.workshopmanage.sfcmaterialordersingle.entity;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import com.google.common.collect.Lists;
import com.hqu.modules.workshopmanage.materialorder.entity.SfcMaterialOrderDetail;
import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 原单行领料单Entity
 * @author xhc
 * @version 2018-12-09
 */
public class SfcMaterialordermainsingle extends DataEntity<SfcMaterialordermainsingle> {
	
	private static final long serialVersionUID = 1L;
	private String materialorder;		// 单据号
	private String receivetype;		// 领料类型
	private String qualityflag;//良品标识
	private String iotype;		// 出入库标记
	private String billtype;		// 单据类型
	private String outflag;		// 出库标志
	private String materialtype;		// 物料类型
	private String wareid;		// 库房代码
	private String warename;		// 库房名称
	private String periodid;		// 核算期
	private String itemtype;		// 领料类型
	private String shopid;		// 部门代码
	private String shopname;		// 部门名称
	private String responser;		// 领料人代码
	private String respname;		// 领料人姓名
	private String editor;		// 制单人名称
	private Date editdate;		// 制单日期
	private String useid;		// 用途代码
	private String usedesc;		// 用途名称
	private String editorid;		// 制单人编码
	private String billstateflag;		// 单据状态
	private Date operdate;		// 业务日期
	private String wareempid;		// 库管员编码
	private String wareempname;		// 库管员名称
	private String opercode;		// 工位编码
	private String opername;		// 工位名称
	private String routinebillno;		// 加工路线单号
	private String routingcode;		// 工艺编码
	private String invbillno;		// 出库单号
	private String materialorderinreturn;		// 退料对应领料单号
	private Date beginEditdate;		// 开始 制单日期
	private Date endEditdate;		// 结束 制单日期
	private Date beginOperdate;		// 开始 业务日期
	private Date endOperdate;		// 结束 业务日期

	private List<SfcMaterialorderdetailsingle> sfcMaterialOrderDetailList = Lists.newArrayList();		// 子表列表

	public List<SfcMaterialorderdetailsingle> getSfcMaterialOrderDetailList() {
		return sfcMaterialOrderDetailList;
	}

	public void setSfcMaterialOrderDetailList(List<SfcMaterialorderdetailsingle> sfcMaterialOrderDetailList) {
		this.sfcMaterialOrderDetailList = sfcMaterialOrderDetailList;
	}


	public SfcMaterialordermainsingle() {
		super();
	}

	public SfcMaterialordermainsingle(String id){
		super(id);
	}

	@ExcelField(title="单据号", align=2, sort=7)
	public String getMaterialorder() {
		return materialorder;
	}

	public void setMaterialorder(String materialorder) {
		this.materialorder = materialorder;
	}
	
	@ExcelField(title="领料类型", align=2, sort=8)
	public String getReceivetype() {
		return receivetype;
	}

	public void setReceivetype(String receivetype) {
		this.receivetype = receivetype;
	}
	
	@ExcelField(title="出入库标记", align=2, sort=9)
	public String getIotype() {
		return iotype;
	}

	public void setIotype(String iotype) {
		this.iotype = iotype;
	}
	
	@ExcelField(title="单据类型", dictType="sfc_material_type", align=2, sort=10)
	public String getBilltype() {
		return billtype;
	}

	public void setBilltype(String billtype) {
		this.billtype = billtype;
	}
	
	@ExcelField(title="出库标志", align=2, sort=11)
	public String getOutflag() {
		return outflag;
	}

	public void setOutflag(String outflag) {
		this.outflag = outflag;
	}
	
	@ExcelField(title="物料类型", align=2, sort=12)
	public String getMaterialtype() {
		return materialtype;
	}

	public void setMaterialtype(String materialtype) {
		this.materialtype = materialtype;
	}
	
	@ExcelField(title="库房代码", align=2, sort=13)
	public String getWareid() {
		return wareid;
	}

	public void setWareid(String wareid) {
		this.wareid = wareid;
	}
	
	@ExcelField(title="库房名称", align=2, sort=14)
	public String getWarename() {
		return warename;
	}

	public void setWarename(String warename) {
		this.warename = warename;
	}
	
	@ExcelField(title="核算期", align=2, sort=15)
	public String getPeriodid() {
		return periodid;
	}

	public void setPeriodid(String periodid) {
		this.periodid = periodid;
	}
	
	@ExcelField(title="领料类型", align=2, sort=16)
	public String getItemtype() {
		return itemtype;
	}

	public void setItemtype(String itemtype) {
		this.itemtype = itemtype;
	}
	
	@ExcelField(title="部门代码", align=2, sort=17)
	public String getShopid() {
		return shopid;
	}

	public void setShopid(String shopid) {
		this.shopid = shopid;
	}
	
	@ExcelField(title="部门名称", align=2, sort=18)
	public String getShopname() {
		return shopname;
	}

	public void setShopname(String shopname) {
		this.shopname = shopname;
	}
	
	@ExcelField(title="领料人代码", align=2, sort=19)
	public String getResponser() {
		return responser;
	}

	public void setResponser(String responser) {
		this.responser = responser;
	}
	
	@ExcelField(title="领料人姓名", align=2, sort=20)
	public String getRespname() {
		return respname;
	}

	public void setRespname(String respname) {
		this.respname = respname;
	}
	
	@ExcelField(title="制单人名称", align=2, sort=21)
	public String getEditor() {
		return editor;
	}

	public void setEditor(String editor) {
		this.editor = editor;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="制单日期", align=2, sort=22)
	public Date getEditdate() {
		return editdate;
	}

	public void setEditdate(Date editdate) {
		this.editdate = editdate;
	}
	
	@ExcelField(title="用途代码", align=2, sort=23)
	public String getUseid() {
		return useid;
	}

	public void setUseid(String useid) {
		this.useid = useid;
	}
	
	@ExcelField(title="用途名称", align=2, sort=24)
	public String getUsedesc() {
		return usedesc;
	}

	public void setUsedesc(String usedesc) {
		this.usedesc = usedesc;
	}
	
	@ExcelField(title="制单人编码", align=2, sort=25)
	public String getEditorid() {
		return editorid;
	}

	public void setEditorid(String editorid) {
		this.editorid = editorid;
	}
	
	@ExcelField(title="单据状态", align=2, sort=26)
	public String getBillstateflag() {
		return billstateflag;
	}

	public void setBillstateflag(String billstateflag) {
		this.billstateflag = billstateflag;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="业务日期", align=2, sort=27)
	public Date getOperdate() {
		return operdate;
	}

	public void setOperdate(Date operdate) {
		this.operdate = operdate;
	}
	
	@ExcelField(title="库管员编码", align=2, sort=28)
	public String getWareempid() {
		return wareempid;
	}

	public void setWareempid(String wareempid) {
		this.wareempid = wareempid;
	}
	
	@ExcelField(title="库管员名称", align=2, sort=29)
	public String getWareempname() {
		return wareempname;
	}

	public void setWareempname(String wareempname) {
		this.wareempname = wareempname;
	}
	
	@ExcelField(title="工位编码", align=2, sort=30)
	public String getOpercode() {
		return opercode;
	}

	public void setOpercode(String opercode) {
		this.opercode = opercode;
	}
	
	@ExcelField(title="工位名称", align=2, sort=31)
	public String getOpername() {
		return opername;
	}

	public void setOpername(String opername) {
		this.opername = opername;
	}
	
	@ExcelField(title="加工路线单号", align=2, sort=32)
	public String getRoutinebillno() {
		return routinebillno;
	}

	public void setRoutinebillno(String routinebillno) {
		this.routinebillno = routinebillno;
	}
	
	@ExcelField(title="工艺编码", align=2, sort=33)
	public String getRoutingcode() {
		return routingcode;
	}

	public void setRoutingcode(String routingcode) {
		this.routingcode = routingcode;
	}
	
	@ExcelField(title="出库单号", align=2, sort=34)
	public String getInvbillno() {
		return invbillno;
	}

	public void setInvbillno(String invbillno) {
		this.invbillno = invbillno;
	}
	
	@ExcelField(title="退料对应领料单号", align=2, sort=35)
	public String getMaterialorderinreturn() {
		return materialorderinreturn;
	}

	public void setMaterialorderinreturn(String materialorderinreturn) {
		this.materialorderinreturn = materialorderinreturn;
	}
	
	public Date getBeginEditdate() {
		return beginEditdate;
	}

	public void setBeginEditdate(Date beginEditdate) {
		this.beginEditdate = beginEditdate;
	}
	
	public Date getEndEditdate() {
		return endEditdate;
	}

	public void setEndEditdate(Date endEditdate) {
		this.endEditdate = endEditdate;
	}
		
	public Date getBeginOperdate() {
		return beginOperdate;
	}

	public void setBeginOperdate(Date beginOperdate) {
		this.beginOperdate = beginOperdate;
	}
	
	public Date getEndOperdate() {
		return endOperdate;
	}

	public void setEndOperdate(Date endOperdate) {
		this.endOperdate = endOperdate;
	}

	public String getQualityflag() {
		return qualityflag;
	}

	public void setQualityflag(String qualityflag) {
		this.qualityflag = qualityflag;
	}
}