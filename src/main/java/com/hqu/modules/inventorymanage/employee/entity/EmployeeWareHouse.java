/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.inventorymanage.employee.entity;

import com.hqu.modules.inventorymanage.employee.entity.Employee;
import javax.validation.constraints.NotNull;
import com.hqu.modules.inventorymanage.warehouse.entity.WareHouse;
import com.hqu.modules.inventorymanage.bin.entity.Bin;
import com.hqu.modules.inventorymanage.location.entity.Location;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 仓库人员定义Entity
 * @author liujiachen
 * @version 2018-04-18
 */
public class EmployeeWareHouse extends DataEntity<EmployeeWareHouse> {
	
	private static final long serialVersionUID = 1L;
	private Employee emp;		// 人员编码
	private WareHouse wareHouse;		// 仓库编码
	private Bin bin;		// 货区编码
	private Location location;		// 货位编码
	private String wareName;//仓库名称 @Ricky
	private String wareID;//仓库编码 @Ricky
	public EmployeeWareHouse() {
		super();
	}

	public EmployeeWareHouse(String id){
		super(id);
	}

	@NotNull(message="人员编码不能为空")
	@ExcelField(title="人员编码", fieldType=Employee.class, value="emp.empName", align=2, sort=7)
	public Employee getEmp() {
		return emp;
	}

	public void setEmp(Employee emp) {
		this.emp = emp;
	}
	
	@ExcelField(title="仓库编码", fieldType=WareHouse.class, value="wareHouse.wareName", align=2, sort=8)
	public WareHouse getWareHouse() {
		return wareHouse;
	}

	public void setWareHouse(WareHouse wareHouse) {
		this.wareHouse = wareHouse;
	}
	
	@ExcelField(title="货区编码", fieldType=Bin.class, value="bin.binName", align=2, sort=9)
	public Bin getBin() {
		return bin;
	}

	public void setBin(Bin bin) {
		this.bin = bin;
	}
	
	@ExcelField(title="货位编码", fieldType=Location.class, value="location.locName", align=2, sort=10)
	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public String getWareName() {
		return wareName;
	}

	public void setWareName(String wareName) {
		this.wareName = wareName;
	}

	public String getWareID() {
		return wareID;
	}

	public void setWareID(String wareID) {
		this.wareID = wareID;
	}
	
}