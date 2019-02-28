/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.costmanage.billingrule.entity;


import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 制单规则Entity
 * @author ccr
 * @version 2018-09-01
 */
public class CosBillRule extends DataEntity<CosBillRule> {
	
	private static final long serialVersionUID = 1L;
	private String ruleId;		// 规则编号
	private String ruleName;		// 规则名称
	private String ruleDesc;		// 规则描述
	private String billCatalog;		// 单据类别                                                                                                                                                                                                    
	private String blFlag;		// 借贷方向
	private String itemRule;		// 科目计算规则
	private String itemNotes;		// 科目规则内容
	private String prodRule;		// 核算对象计算规则
	private String resNotes;		// 核算对象计算内容
	private String ruleType;		// 是否使用规则制单
	private String corType;		// 对应的原始单据类型
	
	public CosBillRule() {
		super();
	}

	public CosBillRule(String id){
		super(id);
	}

	@ExcelField(title="规则编号", align=2, sort=7)
	public String getRuleId() {
		return ruleId;
	}

	public void setRuleId(String ruleId) {
		this.ruleId = ruleId;
	}
	
	@ExcelField(title="规则名称", align=2, sort=8)
	public String getRuleName() {
		return ruleName;
	}

	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
	}
	
	@ExcelField(title="规则描述", align=2, sort=9)
	public String getRuleDesc() {
		return ruleDesc;
	}

	public void setRuleDesc(String ruleDesc) {
		this.ruleDesc = ruleDesc;
	}
	
	@ExcelField(title="单据类别                                                                                                                                                                                                    ", align=2, sort=10)
	public String getBillCatalog() {
		return billCatalog;
	}

	public void setBillCatalog(String billCatalog) {
		this.billCatalog = billCatalog;
	}
	
	@ExcelField(title="借贷方向", align=2, sort=11)
	public String getBlFlag() {
		return blFlag;
	}

	public void setBlFlag(String blFlag) {
		this.blFlag = blFlag;
	}
	
	@ExcelField(title="科目计算规则", align=2, sort=12)
	public String getItemRule() {
		return itemRule;
	}

	public void setItemRule(String itemRule) {
		this.itemRule = itemRule;
	}
	
	@ExcelField(title="科目规则内容", align=2, sort=13)
	public String getItemNotes() {
		return itemNotes;
	}

	public void setItemNotes(String itemNotes) {
		this.itemNotes = itemNotes;
	}
	
	@ExcelField(title="核算对象计算规则", align=2, sort=14)
	public String getProdRule() {
		return prodRule;
	}

	public void setProdRule(String prodRule) {
		this.prodRule = prodRule;
	}
	
	@ExcelField(title="核算对象计算内容", align=2, sort=15)
	public String getResNotes() {
		return resNotes;
	}

	public void setResNotes(String resNotes) {
		this.resNotes = resNotes;
	}
	
	@ExcelField(title="是否使用规则制单", align=2, sort=16)
	public String getRuleType() {
		return ruleType;
	}

	public void setRuleType(String ruleType) {
		this.ruleType = ruleType;
	}
	
	@ExcelField(title="对应的原始单据类型", align=2, sort=17)
	public String getCorType() {
		return corType;
	}

	public void setCorType(String corType) {
		this.corType = corType;
	}
	
}