/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.basedata.product.service;

import java.util.List;

import com.hqu.modules.basedata.item.entity.Item;
import com.hqu.modules.basedata.item.mapper.ItemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.hqu.modules.basedata.product.entity.Product;
import com.hqu.modules.basedata.product.mapper.ProductMapper;
import org.springframework.util.StringUtils;

/**
 * 产品维护Service
 * @author M1ngz
 * @version 2018-04-06
 */
@Service
@Transactional(readOnly = true)
public class ProductService extends CrudService<ProductMapper, Product> {
	@Autowired
	ItemMapper itemMapper;

	public Product get(String id) {
		return super.get(id);
	}

	public Product getById(String id) { return mapper.getById(id); }
	
	public List<Product> findList(Product product) {
		return super.findList(product);
	}
	
	public Page<Product> findPage(Page<Product> page, Product product) {
		return super.findPage(page, product);
	}
	
	@Transactional(readOnly = false)
	public void save(Product product) {
		super.save(product);
	}
	
	@Transactional(readOnly = false)
	public void delete(Product product) {
		super.delete(product);
	}


	@Transactional(readOnly = false)
	public void saveTry(Product product) throws Exception {
		/**
		 * 检测非空性
		 */
		if(StringUtils.isEmpty(product.getItem()))
			throw new Exception("代码为空");

		/**
		 * 检测唯一性
		 */
		List<Product> checkOnly = this.findList(product);
		if(checkOnly != null || checkOnly.size() >0){
			throw new Exception("记录已存在");
		}
		Item item = new Item();
		item.setCode(product.getItem().getCode());
		List<Item> items = itemMapper.findList(item);
		if(items == null || items.size() <= 0){
			throw new Exception("不存在该类型");
		}
		product.setItem(item);
		product.setItemNameRu(item.getName());
		product.setItemName(item.getName());
		super.save(product);
	}

	public Integer getCountByCode(String code) {
		return mapper.getCountByCode(code);
	}
}