/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.purchasemanage.plantype.entity;


import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * PlanTypeEntity
 * @author 方翠虹
 * @version 2018-04-14
 */
public class PlanType extends DataEntity<PlanType> {
	
	private static final long serialVersionUID = 1L;
	private String plantypeId;		// 计划类别编码
	private String plantypename;		// 计划类别名称
	private String plantypenotes;		// 备注
	
	public PlanType() {
		super();
	}

	public PlanType(String id){
		super(id);
	}

	@ExcelField(title="计划类别编码", align=2, sort=7)
	public String getPlantypeId() {
		return plantypeId;
	}

	public void setPlantypeId(String plantypeId) {
		this.plantypeId = plantypeId;
	}
	
	@ExcelField(title="计划类别名称", align=2, sort=8)
	public String getPlantypename() {
		return plantypename;
	}

	public void setPlantypename(String plantypename) {
		this.plantypename = plantypename;
	}
	
	@ExcelField(title="备注", align=2, sort=9)
	public String getPlantypenotes() {
		return plantypenotes;
	}

	public void setPlantypenotes(String plantypenotes) {
		this.plantypenotes = plantypenotes;
	}
	
}