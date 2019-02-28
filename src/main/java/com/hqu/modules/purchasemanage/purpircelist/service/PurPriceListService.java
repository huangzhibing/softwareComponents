/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.purchasemanage.purpircelist.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.hqu.modules.purchasemanage.purpircelist.entity.PurPriceList;
import com.hqu.modules.purchasemanage.purpircelist.mapper.PurPriceListMapper;

/**
 * 价格清单表Service
 * @author zz
 * @version 2018-08-20
 */
@Service
@Transactional(readOnly = true)
public class PurPriceListService extends CrudService<PurPriceListMapper, PurPriceList> {

	public PurPriceList get(String id) {
		return super.get(id);
	}
	
	public List<PurPriceList> findList(PurPriceList purPriceList) {
		return super.findList(purPriceList);
	}
	
	public Page<PurPriceList> findPage(Page<PurPriceList> page, PurPriceList purPriceList) {
		return super.findPage(page, purPriceList);
	}
	
	@Transactional(readOnly = false)
	public void save(PurPriceList purPriceList) {
		super.save(purPriceList);
	}
	
	@Transactional(readOnly = false)
	public void delete(PurPriceList purPriceList) {
		super.delete(purPriceList);
	}
	
}