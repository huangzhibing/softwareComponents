/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.workshopmanage.shiftdef.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.hqu.modules.workshopmanage.shiftdef.entity.ShiftDef;
import com.hqu.modules.workshopmanage.shiftdef.mapper.ShiftDefMapper;

/**
 * 班次定义Service
 * @author zhangxin
 * @version 2018-05-25
 */
@Service
@Transactional(readOnly = true)
public class ShiftDefService extends CrudService<ShiftDefMapper, ShiftDef> {

	public ShiftDef get(String id) {
		return super.get(id);
	}
	
	public List<ShiftDef> findList(ShiftDef shiftDef) {
		return super.findList(shiftDef);
	}
	
	public Page<ShiftDef> findPage(Page<ShiftDef> page, ShiftDef shiftDef) {
		return super.findPage(page, shiftDef);
	}
	
	@Transactional(readOnly = false)
	public void save(ShiftDef shiftDef) {
		super.save(shiftDef);
	}
	
	@Transactional(readOnly = false)
	public void delete(ShiftDef shiftDef) {
		super.delete(shiftDef);
	}
	
}