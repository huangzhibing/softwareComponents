/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.inventorymanage.employee.service;

import java.util.List;

import com.hqu.modules.inventorymanage.employee.entity.Employee;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.hqu.modules.inventorymanage.employee.entity.EmployeeWareHouse;
import com.hqu.modules.inventorymanage.employee.mapper.EmployeeWareHouseMapper;

/**
 * 仓库人员定义Service
 * @author liujiachen
 * @version 2018-04-18
 */
@Service
@Transactional(readOnly = true)
public class EmployeeWareHouseService extends CrudService<EmployeeWareHouseMapper, EmployeeWareHouse> {
	@Autowired
	UserMapper userMapper;

	public EmployeeWareHouse get(String id) {
		return super.get(id);
	}

	public EmployeeWareHouse getByEmployee(Employee employee) {
		return mapper.getByEmployee(employee);
	}
	
	public List<EmployeeWareHouse> findList(EmployeeWareHouse employeeWareHouse) {
		return super.findList(employeeWareHouse);
	}
	
	public Page<EmployeeWareHouse> findPage(Page<EmployeeWareHouse> page, EmployeeWareHouse employeeWareHouse) {
		return super.findPage(page, employeeWareHouse);
	}
	
	@Transactional(readOnly = false)
	public void save(EmployeeWareHouse employeeWareHouse) {
		super.save(employeeWareHouse);
	}
	
	@Transactional(readOnly = false)
	public void delete(EmployeeWareHouse employeeWareHouse) {

		super.delete(employeeWareHouse);
	}

	public Integer getCount(Employee employee){
		if(employee.getUser().getNo().length() == 32){
			User user = userMapper.get(employee.getUser().getNo());
			employee.setUser(user);
		}
		return mapper.getCount(employee.getUser().getNo());
	}


}