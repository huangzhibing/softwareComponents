/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.basedata.routing.entity;

import com.hqu.modules.basedata.product.entity.Product;
import javax.validation.constraints.NotNull;
import com.hqu.modules.basedata.item.entity.Item;
import com.jeeplus.modules.sys.entity.Office;
import java.util.List;
import com.google.common.collect.Lists;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 工艺路线Entity
 * @author huang.youcai
 * @version 2018-06-02
 */
public class Routing extends DataEntity<Routing> {
	
	private static final long serialVersionUID = 1L;
	private String routingCode;		// 工艺编码
	private String routingName;		// 工艺名称
	private String routingDesc;		// 工艺描述
	private Integer routingSeqNo;    //工艺序号
	private Product product;		// 产品代码
	private String productName;		// 产品名称
	private Item item;		// 零件代码
	private String itemName;		// 零件名称
	private Office office;		// 部门代码
	private String deptName;		// 部门名称
	private Double unitWage;		//单位计件工资
	private String assembleflag;  //总装工艺标识
	private List<RoutingWork> routingWorkList = Lists.newArrayList();		// 子表列表
	private List<RoutingItem> routingItemList = Lists.newArrayList();		// 子表列表
	
	public Routing() {
		super();
	}

	public Routing(String id){
		super(id);
	}

	@ExcelField(title="工艺编码", align=2, sort=7)
	public String getRoutingCode() {
		return routingCode;
	}

	public void setRoutingCode(String routingCode) {
		this.routingCode = routingCode;
	}
	
	@ExcelField(title="工艺名称", align=2, sort=8)
	public String getRoutingName() {
		return routingName;
	}

	public void setRoutingName(String routingName) {
		this.routingName = routingName;
	}
	
	@ExcelField(title="工艺描述", align=2, sort=9)
	public String getRoutingDesc() {
		return routingDesc;
	}

	public void setRoutingDesc(String routingDesc) {
		this.routingDesc = routingDesc;
	}
	
	@NotNull(message="产品代码不能为空")
	@ExcelField(title="产品代码", fieldType=Product.class, value="product.itemName", align=2, sort=10)
	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}
	
	@ExcelField(title="产品名称", align=2, sort=11)
	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	@ExcelField(title="零件代码", fieldType=Item.class, value="item.name", align=2, sort=12)
	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}
	
	@ExcelField(title="零件名称", align=2, sort=13)
	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	
	@NotNull(message="部门代码不能为空")
	@ExcelField(title="部门代码", fieldType=Office.class, value="office.name", align=2, sort=14)
	public Office getOffice() {
		return office;
	}

	public void setOffice(Office office) {
		this.office = office;
	}
	
	@ExcelField(title="部门名称", align=2, sort=15)
	public String getDeptName() {
		return deptName;
	}

	public Double getUnitWage() {
		return unitWage;
	}

	public void setUnitWage(Double unitWage) {
		this.unitWage = unitWage;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	
	public List<RoutingWork> getRoutingWorkList() {
		return routingWorkList;
	}

	public void setRoutingWorkList(List<RoutingWork> routingWorkList) {
		this.routingWorkList = routingWorkList;
	}

    public Integer getRoutingSeqNo() {
        return routingSeqNo;
    }

    public void setRoutingSeqNo(Integer routingSeqNo) {
        this.routingSeqNo = routingSeqNo;
    }

	public List<RoutingItem> getRoutingItemList() {
		return routingItemList;
	}

	public void setRoutingItemList(List<RoutingItem> routingItemList) {
		this.routingItemList = routingItemList;
	}

	public String getAssembleflag() {
		return assembleflag;
	}

	public void setAssembleflag(String assembleflag) {
		this.assembleflag = assembleflag;
	}
}