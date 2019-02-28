/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.purchasemanage.transtype.entity;


import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 运输方式定义Entity
 * @author 石豪迈
 * @version 2018-04-14
 */
public class TranStype extends DataEntity<TranStype> {
	
	private static final long serialVersionUID = 1L;
	private String transtypeid;		// 运输方式编码
	private String transtypename;		// 运输方式名称
	private String notes;		// 备注
	
	public TranStype() {
		super();
	}

	public TranStype(String id){
		super(id);
	}

	@ExcelField(title="运输方式编码", align=2, sort=7)
	public String getTranstypeid() {
		return transtypeid;
	}

	public void setTranstypeid(String transtypeid) {
		this.transtypeid = transtypeid;
	}
	
	@ExcelField(title="运输方式名称", align=2, sort=8)
	public String getTranstypename() {
		return transtypename;
	}

	public void setTranstypename(String transtypename) {
		this.transtypename = transtypename;
	}
	
	@ExcelField(title="备注", align=2, sort=9)
	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}
	
}