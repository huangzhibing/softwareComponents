/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.workshopmanage.replacelog.entity;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 换件日志Entity
 * @author xhc
 * @version 2019-02-23
 */
public class MaterialReplaceLog extends DataEntity<MaterialReplaceLog> {
	
	private static final long serialVersionUID = 1L;
	private String routinebillno;		// 加工路线单号
	private String seqno;		// 单件序号
	private String routingcode;		// 工艺编码
	private String itemcode;		// 物料编码
	private String itemsn;		// 物料二维码
	private String centercode;		// 工作中心代码
	private String routingname;		// 工艺名称
	private String itemname;		// 物料名称
	private String assembleqty;		// 数量
	private String finishedqty;		// 已安装数量
	private String centername;		// 工作中心名称
	private String assemblepersonid;		// 装配人员ID
	private String assemblepersonname;		// 装配人员名字
	private Date assembletime;		// 装配时间
	private String actualteamcode;		// 实作班组
	private String hasqualitypro;		// 是否有品质异常问题
	private String qualityproblem;		// 异常类型
	private String originalid;		// 原表中的ID
	private Date beginAssembletime;		// 开始 装配时间
	private Date endAssembletime;		// 结束 装配时间

	private String serialno;//机器序列号
	
	public MaterialReplaceLog() {
		super();
	}

	public MaterialReplaceLog(String id){
		super(id);
	}

	@ExcelField(title="加工路线单号", align=2, sort=7)
	public String getRoutinebillno() {
		return routinebillno;
	}

	public void setRoutinebillno(String routinebillno) {
		this.routinebillno = routinebillno;
	}
	
	@ExcelField(title="单件序号", align=2, sort=8)
	public String getSeqno() {
		return seqno;
	}

	public void setSeqno(String seqno) {
		this.seqno = seqno;
	}
	
	@ExcelField(title="工艺编码", align=2, sort=9)
	public String getRoutingcode() {
		return routingcode;
	}

	public void setRoutingcode(String routingcode) {
		this.routingcode = routingcode;
	}
	
	@ExcelField(title="物料编码", align=2, sort=10)
	public String getItemcode() {
		return itemcode;
	}

	public void setItemcode(String itemcode) {
		this.itemcode = itemcode;
	}
	
	@ExcelField(title="物料二维码", align=2, sort=11)
	public String getItemsn() {
		return itemsn;
	}

	public void setItemsn(String itemsn) {
		this.itemsn = itemsn;
	}
	
	@ExcelField(title="工作中心代码", align=2, sort=12)
	public String getCentercode() {
		return centercode;
	}

	public void setCentercode(String centercode) {
		this.centercode = centercode;
	}
	
	@ExcelField(title="工艺名称", align=2, sort=13)
	public String getRoutingname() {
		return routingname;
	}

	public void setRoutingname(String routingname) {
		this.routingname = routingname;
	}
	
	@ExcelField(title="物料名称", align=2, sort=14)
	public String getItemname() {
		return itemname;
	}

	public void setItemname(String itemname) {
		this.itemname = itemname;
	}
	
	@ExcelField(title="数量", align=2, sort=15)
	public String getAssembleqty() {
		return assembleqty;
	}

	public void setAssembleqty(String assembleqty) {
		this.assembleqty = assembleqty;
	}
	
	@ExcelField(title="已安装数量", align=2, sort=16)
	public String getFinishedqty() {
		return finishedqty;
	}

	public void setFinishedqty(String finishedqty) {
		this.finishedqty = finishedqty;
	}
	
	@ExcelField(title="工作中心名称", align=2, sort=17)
	public String getCentername() {
		return centername;
	}

	public void setCentername(String centername) {
		this.centername = centername;
	}
	
	@ExcelField(title="装配人员ID", align=2, sort=18)
	public String getAssemblepersonid() {
		return assemblepersonid;
	}

	public void setAssemblepersonid(String assemblepersonid) {
		this.assemblepersonid = assemblepersonid;
	}
	
	@ExcelField(title="装配人员名字", align=2, sort=19)
	public String getAssemblepersonname() {
		return assemblepersonname;
	}

	public void setAssemblepersonname(String assemblepersonname) {
		this.assemblepersonname = assemblepersonname;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="装配时间", align=2, sort=20)
	public Date getAssembletime() {
		return assembletime;
	}

	public void setAssembletime(Date assembletime) {
		this.assembletime = assembletime;
	}
	
	@ExcelField(title="实作班组", align=2, sort=21)
	public String getActualteamcode() {
		return actualteamcode;
	}

	public void setActualteamcode(String actualteamcode) {
		this.actualteamcode = actualteamcode;
	}
	
	@ExcelField(title="是否有品质异常问题", align=2, sort=22)
	public String getHasqualitypro() {
		return hasqualitypro;
	}

	public void setHasqualitypro(String hasqualitypro) {
		this.hasqualitypro = hasqualitypro;
	}
	
	@ExcelField(title="异常类型", align=2, sort=23)
	public String getQualityproblem() {
		return qualityproblem;
	}

	public void setQualityproblem(String qualityproblem) {
		this.qualityproblem = qualityproblem;
	}
	
	@ExcelField(title="原表中的ID", align=2, sort=24)
	public String getOriginalid() {
		return originalid;
	}

	public void setOriginalid(String originalid) {
		this.originalid = originalid;
	}
	
	public Date getBeginAssembletime() {
		return beginAssembletime;
	}

	public void setBeginAssembletime(Date beginAssembletime) {
		this.beginAssembletime = beginAssembletime;
	}
	
	public Date getEndAssembletime() {
		return endAssembletime;
	}

	public void setEndAssembletime(Date endAssembletime) {
		this.endAssembletime = endAssembletime;
	}

	public String getSerialno() {
		return serialno;
	}

	public void setSerialno(String serialno) {
		this.serialno = serialno;
	}
}