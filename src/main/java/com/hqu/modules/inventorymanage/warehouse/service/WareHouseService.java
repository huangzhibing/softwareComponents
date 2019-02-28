/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.inventorymanage.warehouse.service;

import java.util.List;

import com.hqu.modules.inventorymanage.employee.entity.Employee;
import com.hqu.modules.inventorymanage.employee.entity.EmployeeWareHouse;
import com.hqu.modules.inventorymanage.employee.mapper.EmployeeMapper;
import com.hqu.modules.inventorymanage.employee.mapper.EmployeeWareHouseMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.hqu.modules.inventorymanage.warehouse.entity.WareHouse;
import com.hqu.modules.inventorymanage.warehouse.mapper.WareHouseMapper;

/**
 * 仓库管理Service
 * @author M1ngz
 * @version 2018-04-12
 */
@Service
@Transactional(readOnly = true)
public class WareHouseService extends CrudService<WareHouseMapper, WareHouse> {
    @Autowired
    WareHouseMapper mapper;
    @Autowired
    EmployeeMapper employeeMapper;
    @Autowired
    EmployeeWareHouseMapper employeeWareHouseMapper;

	public WareHouse get(String id) {
		return super.get(id);
	}
	
	public List<WareHouse> findList(WareHouse wareHouse) {
		return super.findList(wareHouse);
	}
	
	public Page<WareHouse> findPage(Page<WareHouse> page, WareHouse wareHouse) {
		return super.findPage(page, wareHouse);
	}
	
	@Transactional(readOnly = false)
	public void save(WareHouse wareHouse) {
		super.save(wareHouse);
	}
	
	@Transactional(readOnly = false)
	public void delete(WareHouse wareHouse) {
		WareHouse dbWareHouse = mapper.get(wareHouse);
		List<EmployeeWareHouse> employeeWareHouse = employeeWareHouseMapper.getByWareHouse(dbWareHouse);
		for(EmployeeWareHouse employeeWareHouse1 : employeeWareHouse){
			Employee e = employeeWareHouse1.getEmp();
			employeeMapper.deleteByEmpId(e);
			employeeWareHouseMapper.delete(employeeWareHouse1);
		}
		super.delete(wareHouse);
	}

	public String getMaxId() {
	    String id = mapper.getMaxId();
	    if(id == null){
	        return "0000";
        } else {
	        return String.format("%04d", Integer.parseInt(id) + 1);
        }
    }

    public WareHouse getByWareHouseId(String wareId) {
        return mapper.getByWareId(wareId);
    }
}