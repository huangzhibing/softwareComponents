/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.costmanage.realaccount.entity;


import com.hqu.modules.costmanage.cositem.entity.CosItem;
import com.hqu.modules.costmanage.cosprodobject.entity.CosProdObject;
import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 凭证附表Entity
 * @author hzb
 * @version 2018-09-05
 */
public class RealAccountDetail extends DataEntity<RealAccountDetail> {
	
	private static final long serialVersionUID = 1L;
	private RealAccount billNumId;		// 凭证号 父类
	private Integer recNo;		// 序号
	private CosItem itemId;		// 科目代码
	private String itemName;		// 科目名称
	private Double itemSum;		// 科目金额
	private String blflag;		// 借贷方向
	private String abst;		// 摘要
	private CosProdObject cosProdObject;		// 核算对象编号
	private String prodName;		// 核算对象名称
	private String prodUnit;		// 核算对象单位
	private String prodSpec;		// 核算对象规格
	private Double prodQty;		// 核算对象数量
	
	public RealAccountDetail() {
		super();
	}

	public RealAccountDetail(String id){
		super(id);
	}

	public RealAccountDetail(RealAccount billNumId){
		this.billNumId = billNumId;
	}

	public RealAccount getBillNumId() {
		return billNumId;
	}

	public void setBillNumId(RealAccount billNumId) {
		this.billNumId = billNumId;
	}
	
	@ExcelField(title="序号", align=2, sort=8)
	public Integer getRecNo() {
		return recNo;
	}

	public void setRecNo(Integer recNo) {
		this.recNo = recNo;
	}
	
	@ExcelField(title="科目代码", align=2, sort=9)
	public CosItem getItemId() {
		return itemId;
	}

	public void setItemId(CosItem itemId) {
		this.itemId = itemId;
	}
	
	@ExcelField(title="科目名称", align=2, sort=10)
	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	
	@ExcelField(title="科目金额", align=2, sort=11)
	public Double getItemSum() {
		return itemSum;
	}

	public void setItemSum(Double itemSum) {
		this.itemSum = itemSum;
	}
	
	@ExcelField(title="借贷方向", dictType="blflag", align=2, sort=12)
	public String getBlflag() {
		return blflag;
	}

	public void setBlflag(String blflag) {
		this.blflag = blflag;
	}
	
	@ExcelField(title="摘要", align=2, sort=13)
	public String getAbst() {
		return abst;
	}

	public void setAbst(String abst) {
		this.abst = abst;
	}
	
	@ExcelField(title="核算对象编号", align=2, sort=14)
	public CosProdObject getCosProdObject() {
		return cosProdObject;
	}

	public void setCosProdObject(CosProdObject cosProdObject) {
		this.cosProdObject = cosProdObject;
	}
	
	@ExcelField(title="核算对象名称", align=2, sort=15)
	public String getProdName() {
		return prodName;
	}

	public void setProdName(String prodName) {
		this.prodName = prodName;
	}
	
	@ExcelField(title="核算对象单位", align=2, sort=16)
	public String getProdUnit() {
		return prodUnit;
	}

	public void setProdUnit(String prodUnit) {
		this.prodUnit = prodUnit;
	}
	
	@ExcelField(title="核算对象规格", align=2, sort=17)
	public String getProdSpec() {
		return prodSpec;
	}

	public void setProdSpec(String prodSpec) {
		this.prodSpec = prodSpec;
	}
	
	@ExcelField(title="核算对象数量", align=2, sort=18)
	public Double getProdQty() {
		return prodQty;
	}

	public void setProdQty(Double prodQty) {
		this.prodQty = prodQty;
	}

}