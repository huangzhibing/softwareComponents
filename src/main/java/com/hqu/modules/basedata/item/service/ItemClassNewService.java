/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.basedata.item.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.service.TreeService;
import com.jeeplus.common.utils.StringUtils;
import com.hqu.modules.basedata.account.mapper.AccountMapper;
import com.hqu.modules.basedata.item.entity.ItemClassNew;
import com.hqu.modules.basedata.item.mapper.ItemClassNewMapper;

/**
 * 物料定义Service
 * @author xn
 * @version 2018-04-06
 */
@Service
@Transactional(readOnly = true)
public class ItemClassNewService extends TreeService<ItemClassNewMapper, ItemClassNew> {

	@Autowired
    private ItemClassNewMapper itemClassNewMapper;
	
	public ItemClassNew get(String id) {
		return super.get(id);
	}
	
	public List<ItemClassNew> findList(ItemClassNew itemClassNew) {
		if (StringUtils.isNotBlank(itemClassNew.getParentIds())){
			itemClassNew.setParentIds(","+itemClassNew.getParentIds()+",");
		}
		return super.findList(itemClassNew);
	}
	
	@Transactional(readOnly = false)
	public void save(ItemClassNew itemClassNew) {
		super.save(itemClassNew);
	}
	
	@Transactional(readOnly = false)
	public void delete(ItemClassNew itemClassNew) {
		super.delete(itemClassNew);
	}
	
	public ItemClassNew getByClassId(String classId) {
		return itemClassNewMapper.getByClassId(classId);
	}
}