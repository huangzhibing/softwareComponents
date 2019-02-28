/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.purchasemanage.rollplannewquery.entity;

import com.hqu.modules.basedata.item.entity.Item;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.hqu.modules.basedata.item.entity.ItemClassNew;
import com.hqu.modules.purchasemanage.applytype.entity.ApplyType;
import com.jeeplus.modules.sys.entity.Office;
import com.jeeplus.modules.sys.entity.User;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 滚动计划查询Entity
 * @author yangxianbang
 * @version 2018-05-08
 */
public class RollPlanNew extends DataEntity<RollPlanNew> {
	
	private static final long serialVersionUID = 1L;
	private String billNum;		// 单据编号
	private String billType;		// 单据类型
	private Integer serialNum;		// 序号
	private Item itemCode;		// 物料编号
	private String itemName;		// 物料名称
	private String itemSpecmodel;		// 物料规格
	private String unitName;		// 计量单位
	private Integer applyQty;		// 需求数量
	private Double planPrice;		// 计划价
	private Double planSum;		// 计划总额
	private Date requestDate;		// 需求日期
	private Date planArrivedate;		// 计划到达日期
	private String notes;		// 说明
	private String massRequire;		// 质量要求
	private String applyQtyNotes;		// 说明
	private Integer batchLt;		// 采购提前期
	private Date purStartDate;		// 采购开始日期
	private Date purArriveDate;		// 计划到货日期
	private Double purQty;		// 采购数量
	private Double invQty;		// 库存数量
	private Double safetyQty;		// 安全库存
	private Double realQty;		// 实际数量
	private Double roadQty;		// 在途量
	private Date makeDate;		// 制单日期
	private Office applyDeptId;		// 需求部门编码
	private String applyDept;		// 需求部门名称
	private User makeEmpid;		// 制单人代码
	private String makeEmpname;		// 制单人名称
	private String sourseFlag;		// 来源
	private String opFlag;		// 操作标志
	private String planNum;		// 计划号
	private Date beginRequestDate;		// 开始 需求日期
	private Date endRequestDate;		// 结束 需求日期
	private Date beginPlanArrivedate;		// 开始 计划到达日期
	private Date endPlanArrivedate;		// 结束 计划到达日期
	private Date beginPurStartDate;		// 开始 采购开始日期
	private Date endPurStartDate;		// 结束 采购开始日期
	private Date beginPurArriveDate;		// 开始 计划到货日期
	private Date endPurArriveDate;		// 结束 计划到货日期
	private Date beginMakeDate;		// 开始 制单日期
	private Date endMakeDate;		// 结束 制单日期

	private ApplyType applyTypeId;//需求类型编码
	private String applyTypeName;//需求类型名称

	private ItemClassNew itemClassCode;//物料类别编码
	private String itemClassName;//物料类别名称

	private String costPrice;//移动价

	private String itemTexture;//物料材质


	public String getItemTexture() {
		return itemTexture;
	}

	public void setItemTexture(String itemTexture) {
		this.itemTexture = itemTexture;
	}

	public String getCostPrice() {
		return costPrice;
	}

	public void setCostPrice(String costPrice) {
		this.costPrice = costPrice;
	}

	public ItemClassNew getItemClassCode() {
		return itemClassCode;
	}

	public void setItemClassCode(ItemClassNew itemClassCode) {
		this.itemClassCode = itemClassCode;
	}

	public String getItemClassName() {
		return itemClassName;
	}

	public void setItemClassName(String itemClassName) {
		this.itemClassName = itemClassName;
	}

	public ApplyType getApplyTypeId() {
		return applyTypeId;
	}

	public void setApplyTypeId(ApplyType applyTypeId) {
		this.applyTypeId = applyTypeId;
	}

	public String getApplyTypeName() {
		return applyTypeName;
	}

	public void setApplyTypeName(String applyTypeName) {
		this.applyTypeName = applyTypeName;
	}

	public RollPlanNew() {
		super();
	}

	public RollPlanNew(String id){
		super(id);
	}

	@ExcelField(title="单据编号", align=2, sort=7)
	public String getBillNum() {
		return billNum;
	}

	public void setBillNum(String billNum) {
		this.billNum = billNum;
	}
	
	@ExcelField(title="单据类型", align=2, sort=8)
	public String getBillType() {
		return billType;
	}

	public void setBillType(String billType) {
		this.billType = billType;
	}
	
	@ExcelField(title="序号", align=2, sort=9)
	public Integer getSerialNum() {
		return serialNum;
	}

	public void setSerialNum(Integer serialNum) {
		this.serialNum = serialNum;
	}
	
	@ExcelField(title="物料编号", fieldType=Item.class, value="itemCode.code", align=2, sort=10)
	public Item getItemCode() {
		return itemCode;
	}

	public void setItemCode(Item itemCode) {
		this.itemCode = itemCode;
	}
	
	@ExcelField(title="物料名称", align=2, sort=11)
	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	
	@ExcelField(title="物料规格", align=2, sort=12)
	public String getItemSpecmodel() {
		return itemSpecmodel;
	}

	public void setItemSpecmodel(String itemSpecmodel) {
		this.itemSpecmodel = itemSpecmodel;
	}
	
	@ExcelField(title="计量单位", align=2, sort=13)
	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}
	
	@ExcelField(title="需求数量", align=2, sort=14)
	public Integer getApplyQty() {
		return applyQty;
	}

	public void setApplyQty(Integer applyQty) {
		this.applyQty = applyQty;
	}
	
	@ExcelField(title="计划价", align=2, sort=15)
	public Double getPlanPrice() {
		return planPrice;
	}

	public void setPlanPrice(Double planPrice) {
		this.planPrice = planPrice;
	}
	
	@ExcelField(title="计划总额", align=2, sort=16)
	public Double getPlanSum() {
		return planSum;
	}

	public void setPlanSum(Double planSum) {
		this.planSum = planSum;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="需求日期", align=2, sort=17)
	public Date getRequestDate() {
		return requestDate;
	}

	public void setRequestDate(Date requestDate) {
		this.requestDate = requestDate;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="计划到达日期", align=2, sort=18)
	public Date getPlanArrivedate() {
		return planArrivedate;
	}

	public void setPlanArrivedate(Date planArrivedate) {
		this.planArrivedate = planArrivedate;
	}
	
	@ExcelField(title="说明", align=2, sort=19)
	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}
	
	@ExcelField(title="质量要求", align=2, sort=20)
	public String getMassRequire() {
		return massRequire;
	}

	public void setMassRequire(String massRequire) {
		this.massRequire = massRequire;
	}
	
	@ExcelField(title="说明", align=2, sort=21)
	public String getApplyQtyNotes() {
		return applyQtyNotes;
	}

	public void setApplyQtyNotes(String applyQtyNotes) {
		this.applyQtyNotes = applyQtyNotes;
	}
	
	@ExcelField(title="采购提前期", align=2, sort=22)
	public Integer getBatchLt() {
		return batchLt;
	}

	public void setBatchLt(Integer batchLt) {
		this.batchLt = batchLt;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="采购开始日期", align=2, sort=23)
	public Date getPurStartDate() {
		return purStartDate;
	}

	public void setPurStartDate(Date purStartDate) {
		this.purStartDate = purStartDate;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="计划到货日期", align=2, sort=24)
	public Date getPurArriveDate() {
		return purArriveDate;
	}

	public void setPurArriveDate(Date purArriveDate) {
		this.purArriveDate = purArriveDate;
	}
	
	@ExcelField(title="采购数量", align=2, sort=25)
	public Double getPurQty() {
		return purQty;
	}

	public void setPurQty(Double purQty) {
		this.purQty = purQty;
	}
	
	@ExcelField(title="库存数量", align=2, sort=26)
	public Double getInvQty() {
		return invQty;
	}

	public void setInvQty(Double invQty) {
		this.invQty = invQty;
	}
	
	@ExcelField(title="安全库存", align=2, sort=27)
	public Double getSafetyQty() {
		return safetyQty;
	}

	public void setSafetyQty(Double safetyQty) {
		this.safetyQty = safetyQty;
	}
	
	@ExcelField(title="实际数量", align=2, sort=28)
	public Double getRealQty() {
		return realQty;
	}

	public void setRealQty(Double realQty) {
		this.realQty = realQty;
	}
	
	@ExcelField(title="在途量", align=2, sort=29)
	public Double getRoadQty() {
		return roadQty;
	}

	public void setRoadQty(Double roadQty) {
		this.roadQty = roadQty;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="制单日期", align=2, sort=30)
	public Date getMakeDate() {
		return makeDate;
	}

	public void setMakeDate(Date makeDate) {
		this.makeDate = makeDate;
	}
	
	@ExcelField(title="需求部门编码", fieldType=Office.class, value="applyDeptId.code", align=2, sort=31)
	public Office getApplyDeptId() {
		return applyDeptId;
	}

	public void setApplyDeptId(Office applyDeptId) {
		this.applyDeptId = applyDeptId;
	}
	
	@ExcelField(title="需求部门名称", align=2, sort=32)
	public String getApplyDept() {
		return applyDept;
	}

	public void setApplyDept(String applyDept) {
		this.applyDept = applyDept;
	}
	
	@ExcelField(title="制单人代码", fieldType=User.class, value="makeEmpid.no", align=2, sort=33)
	public User getMakeEmpid() {
		return makeEmpid;
	}

	public void setMakeEmpid(User makeEmpid) {
		this.makeEmpid = makeEmpid;
	}
	
	@ExcelField(title="制单人名称", align=2, sort=34)
	public String getMakeEmpname() {
		return makeEmpname;
	}

	public void setMakeEmpname(String makeEmpname) {
		this.makeEmpname = makeEmpname;
	}
	
	@ExcelField(title="来源", align=2, sort=35)
	public String getSourseFlag() {
		return sourseFlag;
	}

	public void setSourseFlag(String sourseFlag) {
		this.sourseFlag = sourseFlag;
	}
	
	@ExcelField(title="操作标志", align=2, sort=36)
	public String getOpFlag() {
		return opFlag;
	}

	public void setOpFlag(String opFlag) {
		this.opFlag = opFlag;
	}
	
	@ExcelField(title="计划号", align=2, sort=37)
	public String getPlanNum() {
		return planNum;
	}

	public void setPlanNum(String planNum) {
		this.planNum = planNum;
	}
	
	public Date getBeginRequestDate() {
		return beginRequestDate;
	}

	public void setBeginRequestDate(Date beginRequestDate) {
		this.beginRequestDate = beginRequestDate;
	}
	
	public Date getEndRequestDate() {
		return endRequestDate;
	}

	public void setEndRequestDate(Date endRequestDate) {
		this.endRequestDate = endRequestDate;
	}
		
	public Date getBeginPlanArrivedate() {
		return beginPlanArrivedate;
	}

	public void setBeginPlanArrivedate(Date beginPlanArrivedate) {
		this.beginPlanArrivedate = beginPlanArrivedate;
	}
	
	public Date getEndPlanArrivedate() {
		return endPlanArrivedate;
	}

	public void setEndPlanArrivedate(Date endPlanArrivedate) {
		this.endPlanArrivedate = endPlanArrivedate;
	}
		
	public Date getBeginPurStartDate() {
		return beginPurStartDate;
	}

	public void setBeginPurStartDate(Date beginPurStartDate) {
		this.beginPurStartDate = beginPurStartDate;
	}
	
	public Date getEndPurStartDate() {
		return endPurStartDate;
	}

	public void setEndPurStartDate(Date endPurStartDate) {
		this.endPurStartDate = endPurStartDate;
	}
		
	public Date getBeginPurArriveDate() {
		return beginPurArriveDate;
	}

	public void setBeginPurArriveDate(Date beginPurArriveDate) {
		this.beginPurArriveDate = beginPurArriveDate;
	}
	
	public Date getEndPurArriveDate() {
		return endPurArriveDate;
	}

	public void setEndPurArriveDate(Date endPurArriveDate) {
		this.endPurArriveDate = endPurArriveDate;
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
		
}