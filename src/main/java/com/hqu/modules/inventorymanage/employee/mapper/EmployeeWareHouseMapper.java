/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.inventorymanage.employee.mapper;

import com.hqu.modules.inventorymanage.bin.entity.Bin;
import com.hqu.modules.inventorymanage.location.entity.Location;
import com.hqu.modules.inventorymanage.warehouse.entity.WareHouse;
import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;

import java.util.List;

import com.hqu.modules.inventorymanage.employee.entity.Employee;
import com.hqu.modules.inventorymanage.employee.entity.EmployeeWareHouse;

/**
 * 仓库人员定义MAPPER接口
 * @author liujiachen
 * @version 2018-04-18
 */
@MyBatisMapper
public interface EmployeeWareHouseMapper extends BaseMapper<EmployeeWareHouse> {
//	public List<EmployeeWareHouse> findListbyEmp(EmployeeWareHouse employeeWareHouse);
	List<EmployeeWareHouse> getByWareHouse(WareHouse wareHouse);
	List<EmployeeWareHouse> getByBin(Bin bin);
	List<EmployeeWareHouse> getByLoc(Location location);
	EmployeeWareHouse getByEmployee(Employee employee);
	Integer getCount(String empId);
}