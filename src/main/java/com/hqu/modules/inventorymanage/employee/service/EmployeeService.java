/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.inventorymanage.employee.service;

import java.util.List;

import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.hqu.modules.inventorymanage.employee.entity.Employee;
import com.hqu.modules.inventorymanage.employee.mapper.EmployeeMapper;

/**
 * 仓库人员定义Service
 * @author liujiachen
 * @version 2018-04-17
 */
@Service
@Transactional(readOnly = true)
public class EmployeeService extends CrudService<EmployeeMapper, Employee> {
	@Autowired
	UserMapper userMapper;

	public Employee get(String id) {
		return super.get(id);
	}
	
	public List<Employee> findList(Employee employee) {
		return super.findList(employee);
	}
	
	public Page<Employee> findPage(Page<Employee> page, Employee employee) {
		return super.findPage(page, employee);
	}
	
	@Transactional(readOnly = false)
	public void save(Employee employee) {
		if(employee.getUser().getNo().length() == 32){
			User user = userMapper.get(employee.getUser().getNo());
			employee.setUser(user);
		}

		super.save(employee);
	}
	
	@Transactional(readOnly = false)
	public void delete(Employee employee) {
		super.delete(employee);
	}



}