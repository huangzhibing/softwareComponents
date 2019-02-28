/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.basedata.item.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.hqu.modules.basedata.item.entity.Item;
import com.hqu.modules.basedata.item.mapper.ItemMapper;

/**
 * 物料定义Service
 * @author xn
 * @version 2018-04-06
 */
@Service
@Transactional(readOnly = true)
public class ItemService extends CrudService<ItemMapper, Item> {
	
	@Autowired
	private ItemMapper itemMapper;
	
	public Item get(String id) {
		return super.get(id);
	}
	
	public List<Item> findList(Item item) {
		return super.findList(item);
	}
	
	public Page<Item> findPage(Page<Item> page, Item item) {
		return super.findPage(page, item);
	}

	public Page<Item> findPage2(Page<Item> page, Item item) {
		dataRuleFilter(item);
		item.setPage(page);
		page.setList(itemMapper.findList2(item));
		return page;
	}
	
	@Transactional(readOnly = false)
	public void save(Item item) {
		super.save(item);
	}
	
	@Transactional(readOnly = false)
	public void delete(Item item) {
		super.delete(item);
	}

	public List<Item> findList1(Item item) {return mapper.findList1(item);}

	public List<Item> findList2(Item item) {return mapper.findList2(item);}

	public Item getByCode(String code) { return mapper.getByCode(code); }
}