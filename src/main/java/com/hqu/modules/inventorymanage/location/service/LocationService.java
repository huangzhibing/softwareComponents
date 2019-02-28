/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.inventorymanage.location.service;

import java.util.List;

import com.alibaba.fastjson.JSON;
import com.hqu.modules.inventorymanage.employee.entity.Employee;
import com.hqu.modules.inventorymanage.employee.entity.EmployeeWareHouse;
import com.hqu.modules.inventorymanage.employee.mapper.EmployeeMapper;
import com.hqu.modules.inventorymanage.employee.mapper.EmployeeWareHouseMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.hqu.modules.inventorymanage.location.entity.Location;
import com.hqu.modules.inventorymanage.location.mapper.LocationMapper;

/**
 * 货位管理Service
 * @author M1ngz
 * @version 2018-04-12
 */
@Service
@Transactional(readOnly = true)
public class LocationService extends CrudService<LocationMapper, Location> {
    @Autowired
    EmployeeMapper employeeMapper;
    @Autowired
    EmployeeWareHouseMapper employeeWareHouseMapper;

	public Location get(String id) {
		return super.get(id);
	}
	
	public List<Location> findList(Location location) {
		return super.findList(location);
	}
	
	public Page<Location> findPage(Page<Location> page, Location location) {
		return super.findPage(page, location);
	}
	
	@Transactional(readOnly = false)
	public void save(Location location) {
		super.save(location);
	}
	
	@Transactional(readOnly = false)
	public void delete(Location location) {
        Location dbLoc = mapper.get(location);
        List<EmployeeWareHouse> employeeWareHouse = employeeWareHouseMapper.getByLoc(dbLoc);
        for(EmployeeWareHouse employeeWareHouse1 : employeeWareHouse){
            Employee e = employeeWareHouse1.getEmp();
            employeeMapper.deleteByEmpId(e);
            employeeWareHouseMapper.delete(employeeWareHouse1);
        }
	    super.delete(location);
	}

    public String getMaxId(String binId) {
        String id = mapper.getMaxId(binId);
        if(id == null){
            return "00";
        } else {
            return String.format("%02d", Integer.parseInt(id.substring(6,8)) + 1);
        }
    }

    public List<Location> findAllByBinId(String binId){
        Location location = new Location();
        logger.debug("binId:"+binId);
        location.setBinId(binId);
        logger.debug("finLocationList"+ JSON.toJSONString(mapper.findList(location)));
        return mapper.findList(location);
    }

    public Location getByLocId(String locId){
        return mapper.getByLocId(locId);
    }

}