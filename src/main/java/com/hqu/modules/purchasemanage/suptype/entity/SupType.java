/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.purchasemanage.suptype.entity;


import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 供应商类别定义Entity
 * @author tyo
 * @version 2018-04-18
 */
public class SupType extends DataEntity<SupType> {
	
	private static final long serialVersionUID = 1L;
	private String suptypeId;		// 供应商类别编码
	private String suptypeName;		// 供应商类别名称
	private String suptypeNotes;		// 备注
	
	public SupType() {
		super();
	}

	public SupType(String id){
		super(id);
	}

	@ExcelField(title="供应商类别编码", align=2, sort=7)
	public String getSuptypeId() {
		return suptypeId;
	}

	public void setSuptypeId(String suptypeId) {
		this.suptypeId = suptypeId;
	}
	
	@ExcelField(title="供应商类别名称", align=2, sort=8)
	public String getSuptypeName() {
		return suptypeName;
	}

	public void setSuptypeName(String suptypeName) {
		this.suptypeName = suptypeName;
	}
	
	@ExcelField(title="备注", align=2, sort=9)
	public String getSuptypeNotes() {
		return suptypeNotes;
	}

	public void setSuptypeNotes(String suptypeNotes) {
		this.suptypeNotes = suptypeNotes;
	}
	
}