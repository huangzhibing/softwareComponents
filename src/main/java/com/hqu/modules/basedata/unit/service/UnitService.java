/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.basedata.unit.service;

import java.util.List;

import com.hqu.modules.basedata.unittype.entity.UnitType;
import com.hqu.modules.basedata.unittype.mapper.UnitTypeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.hqu.modules.basedata.unit.entity.Unit;
import com.hqu.modules.basedata.unit.mapper.UnitMapper;

/**
 * 计量单位定义Service
 * @author 黄志兵
 * @version 2018-04-05
 */
@Service
@Transactional(readOnly = true)
public class UnitService extends CrudService<UnitMapper, Unit> {

	@Autowired
	UnitMapper unitMapper;
	@Autowired
	UnitTypeMapper unitTypeMapper;

	public Unit get(String id) {
		return super.get(id);
	}
	
	public List<Unit> findList(Unit unit) {
		return super.findList(unit);
	}
	
	public Page<Unit> findPage(Page<Unit> page, Unit unit) {
		return super.findPage(page, unit);
	}
	
	@Transactional(readOnly = false)
	public void save(Unit unit) {
		super.save(unit);
	}

	@Transactional(readOnly = false)
	public void saveTry(Unit unit) throws Exception {
		/**
		 * 检测非空性
		 */
		if(unit.getUnitCode()==null || unit.getUnitCode()=="")
			throw new Exception("代码为空");
		if(unit.getUnitName()==null || unit.getUnitName()=="")
			throw new Exception("名称为空");

		/**
		 * 检测唯一性
		 */
		Unit nothing = new Unit();
		List<Unit> checkOnly=this.findList(nothing);
		int count;
		for(count=0;count<checkOnly.size();count++){
			//System.out.println(checkOnly.get(count).getUnitTypeCode()+".+-"+count+unitType.getUnitTypeCode());
			if(unit.getUnitCode().equals(checkOnly.get(count).getUnitCode()))
			{
				throw new Exception("代码已存在");
			}
			if(unit.getUnitName().equals(checkOnly.get(count).getUnitName()))
			{
				throw new Exception("单位已存在");
			}
		}
		UnitType unitType = new UnitType();
		unitType.setUnitTypeName(unit.getUnittype().getUnitTypeName());
		List<UnitType> unitTypes = unitTypeMapper.findList(unitType);
		if(unitTypes == null || unitTypes.size() <= 0){
			throw new Exception("不存在该类型");
		}
		unit.setUnittype(unitType);
		super.save(unit);
	}
	
	@Transactional(readOnly = false)
	public void delete(Unit unit) {
		super.delete(unit);
	}

	public List<Unit> getStandUnitCode(){

		return unitMapper.getStandUnitCode();
	}

	public String getMaxUnitCode(){
		return unitMapper.getMaxUnitCode();
	}


}