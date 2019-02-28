/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.costmanage.cositem.entity;


import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 科目定义Entity
 * @author zz
 * @version 2018-09-05
 */
public class CosItem extends DataEntity<CosItem> {
	
	private static final long serialVersionUID = 1L;
	private String itemCode;		// 科目编码
	private String itemName;		// 科目名称
	private String itemClass;		// 科目级别
	private String itemFinish;		// 是否为叶子科目
	private CosItemLeft fatherId;		// 上级科目编号 父类
	
	public CosItem() {
		super();
	}

	public CosItem(String id){
		super(id);
	}

	public CosItem(CosItemLeft fatherId){
		this.fatherId = fatherId;
	}

	@ExcelField(title="科目编码", align=2, sort=7)
	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}
	
	@ExcelField(title="科目名称", align=2, sort=8)
	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	
	@ExcelField(title="科目级别", align=2, sort=9)
	public String getItemClass() {
		return itemClass;
	}

	public void setItemClass(String itemClass) {
		this.itemClass = itemClass;
	}
	
	@ExcelField(title="是否为叶子科目", dictType="itemFinish", align=2, sort=10)
	public String getItemFinish() {
		return itemFinish;
	}

	public void setItemFinish(String itemFinish) {
		this.itemFinish = itemFinish;
	}
	
	public CosItemLeft getFatherId() {
		return fatherId;
	}

	public void setFatherId(CosItemLeft fatherId) {
		this.fatherId = fatherId;
	}
	
}