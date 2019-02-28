/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.basedata.unittype.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.hqu.modules.basedata.unittype.entity.UnitType;
import com.hqu.modules.basedata.unittype.mapper.UnitTypeMapper;

/**
 * 计量单位类别维护Service
 * @author yrg
 * @version 2018-04-05
 */
@Service
@Transactional(readOnly = true)
public class UnitTypeService extends CrudService<UnitTypeMapper, UnitType> {

	@Resource
	private UnitTypeMapper unitTypeMapper;
	
	public String selectMax() {
		return unitTypeMapper.selectMax();
	}
	
	public UnitType get(String id) {
		return super.get(id);
	}
	
	public List<UnitType> findList(UnitType unitType) {
		return super.findList(unitType);
	}
	
	public Page<UnitType> findPage(Page<UnitType> page, UnitType unitType) {
		return super.findPage(page, unitType);
	}
	
	@Transactional(readOnly = false)
	public void saveTry(UnitType unitType) throws Exception {
		/**
		 * 检测非空性
		 */
		if(unitType.getUnitTypeCode()==null || unitType.getUnitTypeCode()=="")
			throw new Exception("代码为空");
		if(unitType.getUnitTypeName()==null || unitType.getUnitTypeName()=="")
			throw new Exception("名称为空");
		
		/**
		 * 检测唯一性
		 */
		UnitType nothing = new UnitType();		
		List<UnitType> checkOnly=this.findList(nothing);
		int count;		
		for(count=0;count<checkOnly.size();count++){
			//System.out.println(checkOnly.get(count).getUnitTypeCode()+".+-"+count+unitType.getUnitTypeCode());
			if(unitType.getUnitTypeCode().equals(checkOnly.get(count).getUnitTypeCode()))
			{
				throw new Exception("代码已存在");
			}
			if(unitType.getUnitTypeName().equals(checkOnly.get(count).getUnitTypeName()))
			{
				throw new Exception("单位已存在");
			}
		}
		super.save(unitType);
	}
	
	@Transactional(readOnly = false)
	public void save(UnitType unitType) {
		super.save(unitType);
	}
	
	@Transactional(readOnly = false)
	public void delete(UnitType unitType) {
		super.delete(unitType);
	}
	
}