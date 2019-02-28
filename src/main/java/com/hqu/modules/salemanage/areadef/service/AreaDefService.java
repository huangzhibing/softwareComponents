/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.salemanage.areadef.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.hqu.modules.salemanage.accounttype.entity.AccountType2;
import com.hqu.modules.salemanage.accounttype.mapper.AccountType2Mapper;
import com.hqu.modules.salemanage.areadef.entity.AreaDef;
import com.hqu.modules.salemanage.areadef.mapper.AreaDefMapper;

/**
 * 销售地区定义Service
 * @author 黄志兵
 * @version 2018-05-05
 */
@Service
@Transactional(readOnly = true)
public class AreaDefService extends CrudService<AreaDefMapper, AreaDef> {
	@Resource
	private AreaDefMapper areaDefMapper;
	
	public AreaDef get(String id) {
		return super.get(id);
	}
	
	public List<AreaDef> findList(AreaDef areaDef) {
		return super.findList(areaDef);
	}
	
	public Page<AreaDef> findPage(Page<AreaDef> page, AreaDef areaDef) {
		return super.findPage(page, areaDef);
	}
	
	@Transactional(readOnly = false)
	public void save(AreaDef areaDef) {
		super.save(areaDef);
	}
	
	@Transactional(readOnly = false)
	public void delete(AreaDef areaDef) {
		super.delete(areaDef);
	}
	
	public String selectMax() {
		return areaDefMapper.selectMax();
	}
	
	@Transactional(readOnly = false)
	public void saveExc(AreaDef areaDef) throws Exception {
		/**
		 * 检测非空性
		 */
		if(areaDef.getAreaCode()==null ||areaDef.getAreaCode()=="")
			throw new Exception("编码为空");
		if(areaDef.getAreaName()==null || areaDef.getAreaName()=="")
			throw new Exception("名称为空");
		
		/**
		 * 检测唯一性
		 */
		AreaDef nothing = new AreaDef();		
		List<AreaDef> checkOnly=this.findList(nothing);
		int count;		
		for(count=0;count<checkOnly.size();count++){
			//System.out.println(checkOnly.get(count).getUnitTypeCode()+".+-"+count+unitType.getUnitTypeCode());
			if(areaDef.getAreaCode().equals(checkOnly.get(count).getAreaCode()))
			{
				throw new Exception("代码已存在");
			}
			if(areaDef.getAreaName().equals(checkOnly.get(count).getAreaName()))
			{
				throw new Exception("名称已存在");
			}
		}
			super.save(areaDef);
		}

	
}