/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.basedata.productcopy.entity;

import com.hqu.modules.basedata.mdmproductbom.entity.MdmProductBom;
import javax.validation.constraints.NotNull;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 产品结构复制Entity
 * @author xn
 * @version 2018-04-13
 */
public class MDMCopy extends DataEntity<MDMCopy> {
	
	private static final long serialVersionUID = 1L;
	private MdmProductBom source;		// 源
	private MdmProductBom target;		// 目标
	
	public MDMCopy() {
		super();
	}

	public MDMCopy(String id){
		super(id);
	}

	@NotNull(message="源不能为空")
	@ExcelField(title="源", fieldType=MdmProductBom.class, value="source.productItemName", align=2, sort=7)
	public MdmProductBom getSource() {
		return source;
	}

	public void setSource(MdmProductBom source) {
		this.source = source;
	}
	
	@NotNull(message="目标不能为空")
	@ExcelField(title="目标", fieldType=MdmProductBom.class, value="target.productItemName", align=2, sort=8)
	public MdmProductBom getTarget() {
		return target;
	}

	public void setTarget(MdmProductBom target) {
		this.target = target;
	}
	
}