/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.workshopmanage.sfcinvcheckmain.entity;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import javax.validation.constraints.NotNull;
import java.util.List;
import com.google.common.collect.Lists;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 完工入库通知单Entity
 * @author zhangxin
 * @version 2018-06-01
 */
public class SfcInvCheckMain extends DataEntity<SfcInvCheckMain> {
	
	private static final long serialVersionUID = 1L;
	private String billNo;		// 单据编号
	private String billType;		// 单据类型
	private Date makeDate;		// 制单日期
	private String makepId;		// 制单人编号
	private String makepName;		// 制单人名称
	private String makeNotes;		// 制单人说明
	private String billStateFlag;		// 单据标志
	private String workerpId;		// 完工人编号
	private String workerpName;		// 完工人名称
	private Date finishDate;		// 完工日期
	private String shopId;		// 车间编号
	private String shopName;		// 车间名称
	private String warepId;		// 库管员编号
	private String warepName;		// 库管员名称
	private String wareId;		// 仓库编号
	private String wareName;		// 仓库名称
	private String notes;		// 备注
	private String invFlag;		// 库存处理标志
	private String qmsFlag;		// 质检标志
	private String priceSum;		// 金额合计
	private String ioType;		// 入库类型
	private String ioFlag;		// 入库标志
	private String ioDesc;		// 入库类型描述
	private String routineBillNo; //加工路线单号
	private Integer SeqNo; //单件序号
	private String routingCode;//工艺号
	
	private List<SfcInvCheckDetail> sfcInvCheckDetailList = Lists.newArrayList();		// 子表列表
	
	public SfcInvCheckMain() {
		super();
	}

	public SfcInvCheckMain(String id){
		super(id);
	}

	@ExcelField(title="单据编号", align=2, sort=7)
	public String getBillNo() {
		return billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}
	
	@ExcelField(title="单据类型", align=2, sort=8)
	public String getBillType() {
		return billType;
	}

	public void setBillType(String billType) {
		this.billType = billType;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@NotNull(message="制单日期不能为空")
	@ExcelField(title="制单日期", align=2, sort=9)
	public Date getMakeDate() {
		return makeDate;
	}

	public void setMakeDate(Date makeDate) {
		this.makeDate = makeDate;
	}
	
	@ExcelField(title="制单人编号", align=2, sort=10)
	public String getMakepId() {
		return makepId;
	}

	public void setMakepId(String makepId) {
		this.makepId = makepId;
	}
	
	@ExcelField(title="制单人名称", align=2, sort=11)
	public String getMakepName() {
		return makepName;
	}

	public void setMakepName(String makepName) {
		this.makepName = makepName;
	}
	
	@ExcelField(title="制单人说明", align=2, sort=12)
	public String getMakeNotes() {
		return makeNotes;
	}

	public void setMakeNotes(String makeNotes) {
		this.makeNotes = makeNotes;
	}
	
	@ExcelField(title="单据标志", align=2, sort=13)
	public String getBillStateFlag() {
		return billStateFlag;
	}

	public void setBillStateFlag(String billStateFlag) {
		this.billStateFlag = billStateFlag;
	}
	
	@ExcelField(title="完工人编号", align=2, sort=14)
	public String getWorkerpId() {
		return workerpId;
	}

	public void setWorkerpId(String workerpId) {
		this.workerpId = workerpId;
	}
	
	@ExcelField(title="完工人名称", align=2, sort=15)
	public String getWorkerpName() {
		return workerpName;
	}

	public void setWorkerpName(String workerpName) {
		this.workerpName = workerpName;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd")
	@NotNull(message="完工日期不能为空")
	@ExcelField(title="完工日期", align=2, sort=16)
	public Date getFinishDate() {
		return finishDate;
	}

	public void setFinishDate(Date finishDate) {
		this.finishDate = finishDate;
	}
	
	@ExcelField(title="车间编号", align=2, sort=17)
	public String getShopId() {
		return shopId;
	}

	public void setShopId(String shopId) {
		this.shopId = shopId;
	}
	
	@ExcelField(title="车间名称", align=2, sort=18)
	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}
	
	@ExcelField(title="库管员编号", align=2, sort=19)
	public String getWarepId() {
		return warepId;
	}

	public void setWarepId(String warepId) {
		this.warepId = warepId;
	}
	
	@ExcelField(title="库管员名称", align=2, sort=20)
	public String getWarepName() {
		return warepName;
	}

	public void setWarepName(String warepName) {
		this.warepName = warepName;
	}
	
	@ExcelField(title="仓库编号", align=2, sort=21)
	public String getWareId() {
		return wareId;
	}

	public void setWareId(String wareId) {
		this.wareId = wareId;
	}
	
	@ExcelField(title="仓库名称", align=2, sort=22)
	public String getWareName() {
		return wareName;
	}

	public void setWareName(String wareName) {
		this.wareName = wareName;
	}
	
	@ExcelField(title="备注", align=2, sort=23)
	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}
	
	@ExcelField(title="库存处理标志", align=2, sort=24)
	public String getInvFlag() {
		return invFlag;
	}

	public void setInvFlag(String invFlag) {
		this.invFlag = invFlag;
	}
	
	@ExcelField(title="质检标志", align=2, sort=25)
	public String getQmsFlag() {
		return qmsFlag;
	}

	public void setQmsFlag(String qmsFlag) {
		this.qmsFlag = qmsFlag;
	}
	
	@ExcelField(title="金额合计", align=2, sort=26)
	public String getPriceSum() {
		return priceSum;
	}

	public void setPriceSum(String priceSum) {
		this.priceSum = priceSum;
	}
	
	@ExcelField(title="入库类型", align=2, sort=27)
	public String getIoType() {
		return ioType;
	}

	public void setIoType(String ioType) {
		this.ioType = ioType;
	}
	
	@ExcelField(title="入库标志", align=2, sort=28)
	public String getIoFlag() {
		return ioFlag;
	}

	public void setIoFlag(String ioFlag) {
		this.ioFlag = ioFlag;
	}
	
	@ExcelField(title="入库类型描述", align=2, sort=29)
	public String getIoDesc() {
		return ioDesc;
	}

	public void setIoDesc(String ioDesc) {
		this.ioDesc = ioDesc;
	}
	
	@ExcelField(title="加工路线单号", align=2, sort=29)
	public String getRoutineBillNo() {
		return routineBillNo;
	}

	public void setRoutineBillNo(String routineBillNo) {
		this.routineBillNo = routineBillNo;
	}
	
	@ExcelField(title="单件序号", align=2, sort=29)
	public Integer getSeqNo() {
		return SeqNo;
	}

	public void setSeqNo(Integer SeqNo) {
		this.SeqNo = SeqNo;
	}
	
	@ExcelField(title="工艺号", align=2, sort=29)
	public String getRoutingCode() {
		return routingCode;
	}

	public void setRoutingCode(String routingCode) {
		this.routingCode = routingCode;
	}
	

	
	
	public List<SfcInvCheckDetail> getSfcInvCheckDetailList() {
		return sfcInvCheckDetailList;
	}

	public void setSfcInvCheckDetailList(List<SfcInvCheckDetail> sfcInvCheckDetailList) {
		this.sfcInvCheckDetailList = sfcInvCheckDetailList;
	}
}