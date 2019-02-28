/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.basedata.machinetype.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.hqu.modules.basedata.machinetype.entity.MachineType;
import com.hqu.modules.basedata.machinetype.mapper.MachineTypeMapper;
import com.hqu.modules.basedata.unittype.entity.UnitType;

/**
 * 设备类别定义Service
 * @author 何志铭
 * @version 2018-04-05
 */
@Service
@Transactional(readOnly = true)
public class MachineTypeService extends CrudService<MachineTypeMapper, MachineType> {
	@Resource
	private MachineTypeMapper machineTypeMapper;
	
	public String selectMax() {
		return machineTypeMapper.selectMax();
	}
	
	public MachineType get(String id) {
		return super.get(id);
	}
	
	public List<MachineType> findList(MachineType machineType) {
		return super.findList(machineType);
	}
	
	public Page<MachineType> findPage(Page<MachineType> page, MachineType machineType) {
		return super.findPage(page, machineType);
	}
	
	@Transactional(readOnly = false)
	public void save(MachineType machineType) {
		super.save(machineType);
	}
	
	@Transactional(readOnly = false)
	public void delete(MachineType machineType) {
		super.delete(machineType);
	}

		@Transactional(readOnly = false)
	public void saveExc(MachineType machineType) throws Exception {
		/**
		 * 检测非空性
		 */
		if(machineType.getMachineTypeCode()==null || machineType.getMachineTypeCode()=="")
			throw new Exception("代码为空");
		if(machineType.getMachineTypeName()==null || machineType.getMachineTypeName()=="")
			throw new Exception("名称为空");
		
		/**
		 * 检测唯一性
		 */
		MachineType nothing = new MachineType();		
		List<MachineType> checkOnly=this.findList(nothing);
		int count;		
		for(count=0;count<checkOnly.size();count++){
			//System.out.println(checkOnly.get(count).getUnitTypeCode()+".+-"+count+unitType.getUnitTypeCode());
			if(machineType.getMachineTypeCode().equals(checkOnly.get(count).getMachineTypeCode()))
			{
				throw new Exception("代码已存在");
			}
			if(machineType.getMachineTypeName().equals(checkOnly.get(count).getMachineTypeName()))
			{
				throw new Exception("名称已存在");
			}
		}
			super.save(machineType);
		}
	
}