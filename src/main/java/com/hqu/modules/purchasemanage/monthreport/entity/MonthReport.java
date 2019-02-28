/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.purchasemanage.monthreport.entity;

import com.hqu.modules.basedata.account.entity.Account;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.hqu.modules.basedata.item.entity.Item;
import com.hqu.modules.qualitymanage.purreportnew.entity.PurReportNew;
import com.jeeplus.modules.sys.entity.User;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 进料不合格统计表，关联IQC来料检验单Entity
 *
 * @author ckw
 * @version 2018-08-25
 */
public class MonthReport extends DataEntity<MonthReport> {

	private static final long serialVersionUID = 1L;
	private Integer seq;        // 序号
	private String qmsCode;        // 报检单编号
//	private Account supId;        // 供应商编号
    private String supCode;// 供应商编号
	private String supName;        // 供应商名称
	private String name;        // 物料名称
	private String specModel;        // 物料规格
	private String className;        // 物料类别
	private Double purQty;        // 来料数量
	private Double chkQty;        // 抽检数量
	private Double failQty;        // 不良数量
	private Double ratio;        // 不良率
	private String chkRes;        // 检验结果
	private Date chkDate;        // 检验日期
	private User makeEmpid;        // 录入人编号
	private User makeEmpName;        // 录入人名称
	private Item item; //物料
   private  String itemNameNew;

	public String getItemNameNew() {
		return itemNameNew;
	}

	public void setItemNameNew(String itemNameNew) {
		this.itemNameNew = itemNameNew;
	}

	private PurReportNew purReportNew; //IQC来料检验单



	public PurReportNew getPurReportNew() {
		return purReportNew;
	}

	public void setPurReportNew(PurReportNew purReportNew) {
		this.purReportNew = purReportNew;
	}


	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public MonthReport() {
		super();
	}

	public MonthReport(String id) {
		super(id);
	}

	@ExcelField(title = "序号", align = 2, sort = 6)
	public Integer getSeq() {
		return seq;
	}

	public void setSeq(Integer seq) {
		this.seq = seq;
	}

	@ExcelField(title = "报检单编号", align = 2, sort = 7)
	public String getQmsCode() {
		return qmsCode;
	}

	public void setQmsCode(String qmsCode) {
		this.qmsCode = qmsCode;
	}

	public String getSupCode() {
		return supCode;
	}

	public void setSupCode(String supCode) {
		this.supCode = supCode;
	}


//	public Account getSupId() {
//		return supId;
//	}
//
//	public void setSupId(Account supId) {
//		this.supId = supId;
//	}

	@ExcelField(title = "供应商名称", align = 2, sort = 9)
	public String getSupName() {
		return supName;
	}

	public void setSupName(String supName) {
		this.supName = supName;
	}

	@ExcelField(title = "物料名称", align = 2, sort = 10)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@ExcelField(title = "物料规格", align = 2, sort = 11)
	public String getSpecModel() {
		return specModel;
	}

	public void setSpecModel(String specModel) {
		this.specModel = specModel;
	}

	@ExcelField(title = "物料类别", align = 2, sort = 12)
	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	@ExcelField(title = "来料数量", align = 2, sort = 13)
	public Double getPurQty() {
		return purQty;
	}

	public void setPurQty(Double purQty) {
		this.purQty = purQty;
	}

	@ExcelField(title = "抽检数量", align = 2, sort = 14)
	public Double getChkQty() {
		return chkQty;
	}

	public void setChkQty(Double chkQty) {
		this.chkQty = chkQty;
	}

	public Double getFailQty() {
		return failQty;
	}

	public void setFailQty(Double failQty) {
		this.failQty = failQty;
	}

	public Double getRatio() {
		return ratio;
	}

	public void setRatio(Double ratio) {
		this.ratio = ratio;
	}

	@ExcelField(title = "检验结果", align = 2, sort = 17)
	public String getChkRes() {
		return chkRes;
	}

	public void setChkRes(String chkRes) {
		this.chkRes = chkRes;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title = "检验日期", align = 2, sort = 18)
	public Date getChkDate() {
		return chkDate;
	}

	public void setChkDate(Date chkDate) {
		this.chkDate = chkDate;
	}

	@ExcelField(title = "录入人编号", fieldType = User.class, value = "", align = 2, sort = 19)
	public User getMakeEmpid() {
		return makeEmpid;
	}

	public void setMakeEmpid(User makeEmpid) {
		this.makeEmpid = makeEmpid;
	}

	@ExcelField(title = "录入人名称", fieldType = User.class, value = "", align = 2, sort = 20)
	public User getMakeEmpName() {
		return makeEmpName;
	}

	public void setMakeEmpName(User makeEmpName) {
		this.makeEmpName = makeEmpName;
	}

}