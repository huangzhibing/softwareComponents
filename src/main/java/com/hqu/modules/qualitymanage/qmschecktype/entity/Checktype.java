/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.qualitymanage.qmschecktype.entity;


import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 检验类型Entity
 * @author tyo
 * @version 2018-04-14
 */
public class Checktype extends DataEntity<Checktype> {
	
	private static final long serialVersionUID = 1L;
	private String checktcode;		// 检验类型编码
	private String checktname;		// 检验类型名称
	private String checktdes;		// 检验类型描述
	
	public Checktype() {
		super();
	}

	public Checktype(String id){
		super(id);
	}

	@ExcelField(title="检验类型编码", align=2, sort=7)
	public String getChecktcode() {
		return checktcode;
	}

	public void setChecktcode(String checktcode) {
		this.checktcode = checktcode;
	}
	
	@ExcelField(title="检验类型名称", align=2, sort=8)
	public String getChecktname() {
		return checktname;
	}

	public void setChecktname(String checktname) {
		this.checktname = checktname;
	}
	
	@ExcelField(title="检验类型描述", align=2, sort=9)
	public String getChecktdes() {
		return checktdes;
	}

	public void setChecktdes(String checktdes) {
		this.checktdes = checktdes;
	}
	
}