/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.inventorymanage.bin.service;

import java.util.List;

import com.hqu.modules.inventorymanage.employee.entity.Employee;
import com.hqu.modules.inventorymanage.employee.entity.EmployeeWareHouse;
import com.hqu.modules.inventorymanage.employee.mapper.EmployeeMapper;
import com.hqu.modules.inventorymanage.employee.mapper.EmployeeWareHouseMapper;
import com.hqu.modules.inventorymanage.warehouse.mapper.WareHouseMapper;
import com.jeeplus.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.hqu.modules.inventorymanage.bin.entity.Bin;
import com.hqu.modules.inventorymanage.bin.mapper.BinMapper;

/**
 * 货区定义Service
 * @author M1ngz
 * @version 2018-04-12
 */
@Service
@Transactional(readOnly = true)
public class BinService extends CrudService<BinMapper, Bin> {
    @Autowired
    WareHouseMapper wareHouseMapper;
    @Autowired
    EmployeeMapper employeeMapper;
    @Autowired
    EmployeeWareHouseMapper employeeWareHouseMapper;

	public Bin get(String id) {
		return super.get(id);
	}
	
	public List<Bin> findList(Bin bin) {
		return super.findList(bin);
	}
	
	public Page<Bin> findPage(Page<Bin> page, Bin bin) {
		return super.findPage(page, bin);
	}
	
	@Transactional(readOnly = false)
	public void save(Bin bin) {
		super.save(bin);
	}
	
	@Transactional(readOnly = false)
	public void delete(Bin bin) {
	    Bin dbBin = mapper.get(bin);
        List<EmployeeWareHouse> employeeWareHouse = employeeWareHouseMapper.getByBin(dbBin);
        for(EmployeeWareHouse employeeWareHouse1 : employeeWareHouse){
            Employee e = employeeWareHouse1.getEmp();
            employeeMapper.deleteByEmpId(e);
            employeeWareHouseMapper.delete(employeeWareHouse1);
        }
		super.delete(bin);
	}

    public String getMaxId(String wareId) {
        String id = mapper.getMaxId(wareId);
        if(id == null){
            return "00";
        } else {
            return String.format("%02d", Integer.parseInt(id.substring(4,6)) + 1);
        }
    }


    public Bin getByBinId(String binId){
        return mapper.getByBinId(binId);
    }

    public List<Bin> findAllByWareId(String wareId){
        Bin bin = new Bin();
        bin.setWareId(wareId);
        return mapper.findList(bin);
    }
}