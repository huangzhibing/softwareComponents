/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.purchasemanage.applymainquery.entity;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.common.collect.Lists;
import com.hqu.modules.basedata.item.entity.Item;
import com.hqu.modules.purchasemanage.applytype.entity.ApplyType;
import com.hqu.modules.purchasemanage.contractmain.entity.ContractDetail;
import com.hqu.modules.purchasemanage.purplan.entity.PurPlanDetail;
import com.hqu.modules.qualitymanage.purinvcheckmain.entity.InvCheckDetail;
import com.jeeplus.common.utils.excel.annotation.ExcelField;
import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.modules.sys.entity.Office;
import com.jeeplus.modules.sys.entity.User;

/**
 * 采购需求查询Entity
 * @author syc
 * @version 2018-04-26
 */
public class ApplyMainQuery extends DataEntity<ApplyMainQuery> {
	
	private static final long serialVersionUID = 1L;
	private String billNum;		// 单据编号
	private Date makeDate;		// 制定日期
	private Date applyDate;		// 业务日期
	private ApplyType applyType;		// 需求类别编码
	private String applyName;		// 需求类别名称
	private String billStateFlag;		// 单据状态
	private Office office;		// 需求部门编码
	private String applyDept;		// 需求部门名称
	private User user;		// 需求人员编码
	private String makeEmpname;		// 需求人员名称
	private String makeNotes;		// 需求说明
	private String billType;		// 单据类型
	private Date periodId;		// 核算期
	private String sourceFlag;		// 来源标志
	private Integer applyQty;		// 数量合计
	private Integer applySum;		// 金额合计
	private String applyNote;		// 备注
	private String fbrNo;		// 公司编码
	private String userDeptCode;		// 登陆人所在部门
	private Date beginMakeDate;		// 开始 制定日期
	private Date endMakeDate;		// 结束 制定日期
	private Date beginApplyDate;		// 开始 业务日期
	private Date endApplyDate;		// 结束 业务日期
	private List<ApplyDetailQuery> applyDetailQueryList = Lists.newArrayList();		// 子表列表
	private List<PurPlanDetail> purPlanDetailList = Lists.newArrayList();		//计划信息子表
	private List<ContractDetail> contractDetailList = Lists.newArrayList();		//合同信息子表
	private List<InvCheckDetail> invCheckDetailList = Lists.newArrayList();		//到货信息子表
	//查询使用
	private String itemCode;	//物料编号
	private String itemName;	//物料名称
	private String itemSpecmodel;	//物料规格
	private Item item;	//物料
	
	
	
	public ApplyMainQuery() {
		super();
	}

	public ApplyMainQuery(String id){
		super(id);
	}

	@ExcelField(title="单据编号", align=2, sort=7)
	public String getBillNum() {
		return billNum;
	}

	public void setBillNum(String billNum) {
		this.billNum = billNum;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@NotNull(message="制定日期不能为空")
	@ExcelField(title="制定日期", align=2, sort=8)
	public Date getMakeDate() {
		return makeDate;
	}

	public void setMakeDate(Date makeDate) {
		this.makeDate = makeDate;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@NotNull(message="业务日期不能为空")
	@ExcelField(title="业务日期", align=2, sort=9)
	public Date getApplyDate() {
		return applyDate;
	}

	public void setApplyDate(Date applyDate) {
		this.applyDate = applyDate;
	}
	
	@NotNull(message="需求类别编码不能为空")
	@ExcelField(title="需求类别编码", fieldType=ApplyType.class, value="applyType.applytypeid", align=2, sort=10)
	public ApplyType getApplyType() {
		return applyType;
	}

	public void setApplyType(ApplyType applyType) {
		this.applyType = applyType;
	}
	
	@ExcelField(title="需求类别名称", align=2, sort=11)
	public String getApplyName() {
		return applyName;
	}

	public void setApplyName(String applyName) {
		this.applyName = applyName;
	}
	
	@ExcelField(title="单据状态", align=2, sort=12)
	public String getBillStateFlag() {
		return billStateFlag;
	}

	public void setBillStateFlag(String billStateFlag) {
		this.billStateFlag = billStateFlag;
	}
	
	@NotNull(message="需求部门编码不能为空")
	@ExcelField(title="需求部门编码", fieldType=Office.class, value="office.code", align=2, sort=13)
	public Office getOffice() {
		return office;
	}

	public void setOffice(Office office) {
		this.office = office;
	}
	
	@ExcelField(title="需求部门名称", align=2, sort=14)
	public String getApplyDept() {
		return applyDept;
	}

	public void setApplyDept(String applyDept) {
		this.applyDept = applyDept;
	}
	
	@NotNull(message="需求人员编码不能为空")
	@ExcelField(title="需求人员编码", fieldType=User.class, value="user.no", align=2, sort=15)
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	@ExcelField(title="需求人员名称", align=2, sort=16)
	public String getMakeEmpname() {
		return makeEmpname;
	}

	public void setMakeEmpname(String makeEmpname) {
		this.makeEmpname = makeEmpname;
	}
	
	@ExcelField(title="需求说明", align=2, sort=17)
	public String getMakeNotes() {
		return makeNotes;
	}

	public void setMakeNotes(String makeNotes) {
		this.makeNotes = makeNotes;
	}
	
	@ExcelField(title="单据类型", align=2, sort=18)
	public String getBillType() {
		return billType;
	}

	public void setBillType(String billType) {
		this.billType = billType;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="核算期", align=2, sort=19)
	public Date getPeriodId() {
		return periodId;
	}

	public void setPeriodId(Date periodId) {
		this.periodId = periodId;
	}
	
	@ExcelField(title="来源标志", align=2, sort=20)
	public String getSourceFlag() {
		return sourceFlag;
	}

	public void setSourceFlag(String sourceFlag) {
		this.sourceFlag = sourceFlag;
	}
	
	@ExcelField(title="数量合计", align=2, sort=21)
	public Integer getApplyQty() {
		return applyQty;
	}

	public void setApplyQty(Integer applyQty) {
		this.applyQty = applyQty;
	}
	
	@ExcelField(title="金额合计", align=2, sort=22)
	public Integer getApplySum() {
		return applySum;
	}

	public void setApplySum(Integer applySum) {
		this.applySum = applySum;
	}
	
	@ExcelField(title="备注", align=2, sort=23)
	public String getApplyNote() {
		return applyNote;
	}

	public void setApplyNote(String applyNote) {
		this.applyNote = applyNote;
	}
	
	@ExcelField(title="公司编码", align=2, sort=24)
	public String getFbrNo() {
		return fbrNo;
	}

	public void setFbrNo(String fbrNo) {
		this.fbrNo = fbrNo;
	}
	
	@ExcelField(title="登陆人所在部门", align=2, sort=25)
	public String getUserDeptCode() {
		return userDeptCode;
	}

	public void setUserDeptCode(String userDeptCode) {
		this.userDeptCode = userDeptCode;
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
		
	public Date getBeginApplyDate() {
		return beginApplyDate;
	}

	public void setBeginApplyDate(Date beginApplyDate) {
		this.beginApplyDate = beginApplyDate;
	}
	
	public Date getEndApplyDate() {
		return endApplyDate;
	}

	public void setEndApplyDate(Date endApplyDate) {
		this.endApplyDate = endApplyDate;
	}
		
	public List<ApplyDetailQuery> getApplyDetailQueryList() {
		return applyDetailQueryList;
	}

	public void setApplyDetailQueryList(List<ApplyDetailQuery> applyDetailQueryList) {
		this.applyDetailQueryList = applyDetailQueryList;
	}

	public List<PurPlanDetail> getPurPlanDetailList() {
		return purPlanDetailList;
	}

	public void setPurPlanDetailList(List<PurPlanDetail> purPlanDetailList) {
		this.purPlanDetailList = purPlanDetailList;
	}

	public List<ContractDetail> getContractDetailList() {
		return contractDetailList;
	}

	public void setContractDetailList(List<ContractDetail> contractDetailList) {
		this.contractDetailList = contractDetailList;
	}

	public List<InvCheckDetail> getInvCheckDetailList() {
		return invCheckDetailList;
	}

	public void setInvCheckDetailList(List<InvCheckDetail> invCheckDetailList) {
		this.invCheckDetailList = invCheckDetailList;
	}

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getItemSpecmodel() {
		return itemSpecmodel;
	}

	public void setItemSpecmodel(String itemSpecmodel) {
		this.itemSpecmodel = itemSpecmodel;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
	
	
}