/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.basedata.itemclass.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.service.TreeService;
import com.jeeplus.common.utils.StringUtils;
import com.hqu.modules.basedata.itemclass.entity.ItemClass;
import com.hqu.modules.basedata.itemclass.mapper.ItemClassMapper;

/**
 * 物料类定义Service
 * 
 * @author dongqida
 * @version 2018-04-02
 */
@Service
@Transactional(readOnly = true)
public class ItemClassService extends TreeService<ItemClassMapper, ItemClass> {

	@Autowired
	private ItemClassMapper itemClassMapper;

	public ItemClass get(String id) {
		return super.get(id);
	}

	public List<ItemClass> findList(ItemClass itemClass) {
		if (StringUtils.isNotBlank(itemClass.getParentIds())) {
			itemClass.setParentIds("," + itemClass.getParentIds() + ",");
		}
		return super.findList(itemClass);
	}

	@Transactional(readOnly = false)
	public void save(ItemClass itemClass) {
		super.save(itemClass);
	}

	@Transactional(readOnly = false)
	public void delete(ItemClass itemClass) {
		super.delete(itemClass);
	}

	public ItemClass findByClassId(String classid) {
		return itemClassMapper.findByClassId(classid);
	}

	public ItemClass findByClassCode(String code) {
		return itemClassMapper.findByClassCode(code);
	}

	public void updateClassId(Map<String, Object> map) {
		itemClassMapper.updateClassId(map);
		;
	}
}