/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.inventorymanage.employee.mapper;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.hqu.modules.inventorymanage.employee.entity.Employee;

/**
 * 仓库人员定义MAPPER接口
 * @author liujiachen
 * @version 2018-04-17
 */
@MyBatisMapper
public interface EmployeeMapper extends BaseMapper<Employee> {
    void deleteByEmpId(Employee employee);
}