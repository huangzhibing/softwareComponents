/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.qualitymanage.purreportnew.entity;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.common.collect.Lists;
import com.hqu.modules.qualitymanage.objectdef.entity.ObjectDef;
import com.hqu.modules.qualitymanage.purinvcheckmain.entity.InvCheckMain;
import com.hqu.modules.salemanage.accountfile.entity.FileSalaccount;
import com.jeeplus.common.utils.excel.annotation.ExcelField;
import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.modules.sys.entity.User;

/**
 * IQC来料检验报告Entity
 * @author syc
 * @version 2018-08-22
 */
public class PurReportNew extends DataEntity<PurReportNew> {
	
	private static final long serialVersionUID = 1L;
	private String reportId;		// 报告单编号
	private InvCheckMain inv;		// 采购单号
	private ObjectDef obf;		// 料号
	private String itemName;		// 物料名称
	private FileSalaccount filesala;		// 供应商名称
	private double itemCount;		// 来料数量
	private String inspectionNum;		// 送检编号
	private Date itemArriveDate;		// 来料日期
	private Date checkDate;		// 检验日期
	private User user;		// 检验员
	private String rndFul;		// 全检抽检
	private String aql;		// 收货标准
	private Integer crAc;		// 致命缺陷允收数量
	private Integer majAc;		// 严重缺陷允收数量
	private Integer minAc;		// 轻微缺陷允收数量
	private Integer crRe;		// 致命缺陷拒收数量
	private Integer majRe;		// 严重缺陷拒收数量
	private Integer minRe;		// 轻微缺陷拒收数量
	private String checkResult;		// 检验结果
	private String handleResult;		// 检验处理
	private String backBillNum;		// 退货单号
	private Date backDate;		// 退货日期
	private Double backQty;		// 退货数量
	private String unitCode;		// 单位
	private Double realPriceTaxed;		// 单价
	private Double realSumTaxed;		// 总金额
	private String backReason;		// 退货原因
	private String itemSpecmodel;		// 物料规格
	private String supId;		// 供应商编号
	private Date beginItemArriveDate;		// 开始 来料日期
	private Date endItemArriveDate;		// 结束 来料日期
	private Date beginCheckDate;		// 开始 检验日期
	private Date endCheckDate;		// 结束 检验日期
	private String itemCode;	//物料编号
	private String icode;		// 物料编号
	private String supname1;		// 供应商名称
	private List<Purreportfundetail> purreportfundetailList = Lists.newArrayList();		// 子表列表
	private List<Purreportnewsn> purreportnewsnList = Lists.newArrayList();		// 子表列表
	private List<Purreportsizedetail> purreportsizedetailList = Lists.newArrayList();		// 子表列表
	
	public PurReportNew() {
		super();
	}

	public PurReportNew(String id){
		super(id);
	}

	@ExcelField(title="报告单编号", align=2, sort=7)
	public String getReportId() {
		return reportId;
	}

	public void setReportId(String reportId) {
		this.reportId = reportId;
	}
	
	public String getIcode() {
		return icode;
	}

	public void setIcode(String icode) {
		this.icode = icode;
	}

	public String getSupname1() {
		return supname1;
	}

	public void setSupname1(String supname1) {
		this.supname1 = supname1;
	}

	public void setItemCount(double itemCount) {
		this.itemCount = itemCount;
	}

	@NotNull(message="采购单号不能为空")
	@ExcelField(title="采购单号", fieldType=InvCheckMain.class, value="inv.billnum", align=2, sort=8)
	public InvCheckMain getInv() {
		return inv;
	}

	public void setInv(InvCheckMain inv) {
		this.inv = inv;
	}
	
	@NotNull(message="料号不能为空")
	@ExcelField(title="料号", fieldType=ObjectDef.class, value="obf.objCode", align=2, sort=9)
	public ObjectDef getObf() {
		return obf;
	}

	public void setObf(ObjectDef obf) {
		this.obf = obf;
	}
	
	@ExcelField(title="物料名称", align=2, sort=10)
	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	
	@NotNull(message="供应商名称不能为空")
	@ExcelField(title="供应商名称", fieldType=FileSalaccount.class, value="filesala.accountName", align=2, sort=11)
	public FileSalaccount getFilesala() {
		return filesala;
	}

	public void setFilesala(FileSalaccount filesala) {
		this.filesala = filesala;
	}
	
	@NotNull(message="来料数量不能为空")
	@ExcelField(title="来料数量", align=2, sort=12)
	public Double getItemCount() {
		return itemCount;
	}

	public void setItemCount(Double itemCount) {
		this.itemCount = itemCount;
	}
	
	@ExcelField(title="送检编号", align=2, sort=13)
	public String getInspectionNum() {
		return inspectionNum;
	}

	public void setInspectionNum(String inspectionNum) {
		this.inspectionNum = inspectionNum;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="来料日期", align=2, sort=14)
	public Date getItemArriveDate() {
		return itemArriveDate;
	}

	public void setItemArriveDate(Date itemArriveDate) {
		this.itemArriveDate = itemArriveDate;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="检验日期", align=2, sort=15)
	public Date getCheckDate() {
		return checkDate;
	}

	public void setCheckDate(Date checkDate) {
		this.checkDate = checkDate;
	}
	
	@ExcelField(title="检验员", fieldType=User.class, value="user.name", align=2, sort=16)
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	@ExcelField(title="全检抽检", dictType="checkType", align=2, sort=17)
	public String getRndFul() {
		return rndFul;
	}

	public void setRndFul(String rndFul) {
		this.rndFul = rndFul;
	}
	
	@ExcelField(title="收货标准", dictType="AQL", align=2, sort=18)
	public String getAql() {
		return aql;
	}

	public void setAql(String aql) {
		this.aql = aql;
	}
	
	@ExcelField(title="致命缺陷允收数量", align=2, sort=19)
	public Integer getCrAc() {
		return crAc;
	}

	public void setCrAc(Integer crAc) {
		this.crAc = crAc;
	}
	
	@ExcelField(title="严重缺陷允收数量", align=2, sort=20)
	public Integer getMajAc() {
		return majAc;
	}

	public void setMajAc(Integer majAc) {
		this.majAc = majAc;
	}
	
	@ExcelField(title="轻微缺陷允收数量", align=2, sort=21)
	public Integer getMinAc() {
		return minAc;
	}

	public void setMinAc(Integer minAc) {
		this.minAc = minAc;
	}
	
	@ExcelField(title="致命缺陷拒收数量", align=2, sort=22)
	public Integer getCrRe() {
		return crRe;
	}

	public void setCrRe(Integer crRe) {
		this.crRe = crRe;
	}
	
	@ExcelField(title="严重缺陷拒收数量", align=2, sort=23)
	public Integer getMajRe() {
		return majRe;
	}

	public void setMajRe(Integer majRe) {
		this.majRe = majRe;
	}
	
	@ExcelField(title="轻微缺陷拒收数量", align=2, sort=24)
	public Integer getMinRe() {
		return minRe;
	}

	public void setMinRe(Integer minRe) {
		this.minRe = minRe;
	}
	
	@ExcelField(title="检验结果", dictType="checkResultNew", align=2, sort=25)
	public String getCheckResult() {
		return checkResult;
	}

	public void setCheckResult(String checkResult) {
		this.checkResult = checkResult;
	}
	
	@ExcelField(title="检验处理", dictType="handleresult", align=2, sort=26)
	public String getHandleResult() {
		return handleResult;
	}

	public void setHandleResult(String handleResult) {
		this.handleResult = handleResult;
	}
	
	@ExcelField(title="退货单号", align=2, sort=27)
	public String getBackBillNum() {
		return backBillNum;
	}

	public void setBackBillNum(String backBillNum) {
		this.backBillNum = backBillNum;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="退货日期", align=2, sort=28)
	public Date getBackDate() {
		return backDate;
	}

	public void setBackDate(Date backDate) {
		this.backDate = backDate;
	}
	
	@ExcelField(title="退货数量", align=2, sort=29)
	public Double getBackQty() {
		return backQty;
	}

	public void setBackQty(Double backQty) {
		this.backQty = backQty;
	}
	
	@ExcelField(title="单位", align=2, sort=30)
	public String getUnitCode() {
		return unitCode;
	}

	public void setUnitCode(String unitCode) {
		this.unitCode = unitCode;
	}
	
	@ExcelField(title="单价", align=2, sort=31)
	public Double getRealPriceTaxed() {
		return realPriceTaxed;
	}

	public void setRealPriceTaxed(Double realPriceTaxed) {
		this.realPriceTaxed = realPriceTaxed;
	}
	
	@ExcelField(title="总金额", align=2, sort=32)
	public Double getRealSumTaxed() {
		return realSumTaxed;
	}

	public void setRealSumTaxed(Double realSumTaxed) {
		this.realSumTaxed = realSumTaxed;
	}
	
	@ExcelField(title="退货原因", align=2, sort=33)
	public String getBackReason() {
		return backReason;
	}

	public void setBackReason(String backReason) {
		this.backReason = backReason;
	}
	
	@ExcelField(title="物料规格", align=2, sort=34)
	public String getItemSpecmodel() {
		return itemSpecmodel;
	}

	public void setItemSpecmodel(String itemSpecmodel) {
		this.itemSpecmodel = itemSpecmodel;
	}
	
	@ExcelField(title="供应商编号", align=2, sort=35)
	public String getSupId() {
		return supId;
	}

	public void setSupId(String supId) {
		this.supId = supId;
	}
	
	public Date getBeginItemArriveDate() {
		return beginItemArriveDate;
	}

	public void setBeginItemArriveDate(Date beginItemArriveDate) {
		this.beginItemArriveDate = beginItemArriveDate;
	}
	
	public Date getEndItemArriveDate() {
		return endItemArriveDate;
	}

	public void setEndItemArriveDate(Date endItemArriveDate) {
		this.endItemArriveDate = endItemArriveDate;
	}
		
	public Date getBeginCheckDate() {
		return beginCheckDate;
	}

	public void setBeginCheckDate(Date beginCheckDate) {
		this.beginCheckDate = beginCheckDate;
	}
	
	public Date getEndCheckDate() {
		return endCheckDate;
	}

	public void setEndCheckDate(Date endCheckDate) {
		this.endCheckDate = endCheckDate;
	}
		
	public List<Purreportfundetail> getPurreportfundetailList() {
		return purreportfundetailList;
	}

	public void setPurreportfundetailList(List<Purreportfundetail> purreportfundetailList) {
		this.purreportfundetailList = purreportfundetailList;
	}
	public List<Purreportnewsn> getPurreportnewsnList() {
		return purreportnewsnList;
	}

	public void setPurreportnewsnList(List<Purreportnewsn> purreportnewsnList) {
		this.purreportnewsnList = purreportnewsnList;
	}
	public List<Purreportsizedetail> getPurreportsizedetailList() {
		return purreportsizedetailList;
	}

	public void setPurreportsizedetailList(List<Purreportsizedetail> purreportsizedetailList) {
		this.purreportsizedetailList = purreportsizedetailList;
	}

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}
	
}