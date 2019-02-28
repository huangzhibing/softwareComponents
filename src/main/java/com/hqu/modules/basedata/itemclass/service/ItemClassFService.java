/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.basedata.itemclass.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.hqu.modules.basedata.itemclass.entity.ItemClassF;
import com.hqu.modules.basedata.itemclass.mapper.ItemClassFMapper;

/**
 * 物料附表Service
 * 
 * @author dongqida
 * @version 2018-04-02
 */
@Service
@Transactional(readOnly = true)
public class ItemClassFService extends CrudService<ItemClassFMapper, ItemClassF> {

	@Autowired
	private ItemClassFMapper itemClassFMapper;

	public ItemClassF get(String id) {
		return super.get(id);
	}

	public List<ItemClassF> findList(ItemClassF itemClassF) {
		return super.findList(itemClassF);
	}

	public Page<ItemClassF> findPage(Page<ItemClassF> page, ItemClassF itemClassF) {
		return super.findPage(page, itemClassF);
	}

	@Transactional(readOnly = false)
	public void save(ItemClassF itemClassF) {
		super.save(itemClassF);
	}

	@Transactional(readOnly = false)
	public void delete(ItemClassF itemClassF) {
		super.delete(itemClassF);
	}

	public List<ItemClassF> findByCodeLength(Map<String, Object> map) {
		return itemClassFMapper.findByCodeLength(map);
	}

	public ItemClassF findByCode(String code) {
		return itemClassFMapper.findByCode(code);
	}

	public String findCodeById(String id) {
		return itemClassFMapper.findCodeById(id);
	}
	public String findMaxCode() {
		return itemClassFMapper.findMaxCode();
	}
}