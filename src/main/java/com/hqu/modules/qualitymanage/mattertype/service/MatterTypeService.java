/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.qualitymanage.mattertype.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.hqu.modules.qualitymanage.mattertype.entity.MatterType;
import com.hqu.modules.qualitymanage.mattertype.mapper.MatterTypeMapper;

/**
 * MatterTypeService
 * @author 方翠虹
 * @version 2018-08-18
 */
@Service
@Transactional(readOnly = true)
public class MatterTypeService extends CrudService<MatterTypeMapper, MatterType> {

	public MatterType get(String id) {
		return super.get(id);
	}
	
	public List<MatterType> findList(MatterType matterType) {
		return super.findList(matterType);
	}
	
	public Page<MatterType> findPage(Page<MatterType> page, MatterType matterType) {
		return super.findPage(page, matterType);
	}
	
	@Transactional(readOnly = false)
	public void save(MatterType matterType) {
		super.save(matterType);
	}
	
	@Transactional(readOnly = false)
	public void delete(MatterType matterType) {
		super.delete(matterType);
	}
	
}