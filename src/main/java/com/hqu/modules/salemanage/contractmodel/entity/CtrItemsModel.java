/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.salemanage.contractmodel.entity;


import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 合同模板Entity
 * @author dongqida
 * @version 2018-05-05
 */
public class CtrItemsModel extends DataEntity<CtrItemsModel> {
	
	private static final long serialVersionUID = 1L;
	private String itemOrder;		// 合同模板编码
	private String itemName;		// 合同模板名称
	private String itemContents;		// 合同文本
	
	public CtrItemsModel() {
		super();
	}

	public CtrItemsModel(String id){
		super(id);
	}

	@ExcelField(title="合同模板编码", align=2, sort=7)
	public String getItemOrder() {
		return itemOrder;
	}

	public void setItemOrder(String itemOrder) {
		this.itemOrder = itemOrder;
	}
	
	@ExcelField(title="合同模板名称", align=2, sort=8)
	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	
	@ExcelField(title="合同文本", align=2, sort=9)
	public String getItemContents() {
		return itemContents;
	}

	public void setItemContents(String itemContents) {
		this.itemContents = itemContents;
	}
	
}