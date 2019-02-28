/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.purchasemanage.areacode.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.hqu.modules.purchasemanage.areacode.entity.AreaCode;
import com.hqu.modules.purchasemanage.areacode.mapper.AreaCodeMapper;

/**
 * 地区定义Service
 * @author 石豪迈
 * @version 2018-04-25
 */
@Service
@Transactional(readOnly = true)
public class AreaCodeService extends CrudService<AreaCodeMapper, AreaCode> {
	@Autowired
	AreaCodeMapper areaCodeMapper;

	public AreaCode get(String id) {
		return super.get(id);
	}
	
	public List<AreaCode> findList(AreaCode areaCode) {
		return super.findList(areaCode);
	}
	
	public Page<AreaCode> findPage(Page<AreaCode> page, AreaCode areaCode) {
		String orderBy=page.getOrderBy();
		if(orderBy!=null&&"".equals(orderBy)){
			page.setOrderBy("areacode");
		}		
		return super.findPage(page, areaCode);
	}
	
	@Transactional(readOnly = false)
	public void save(AreaCode areaCode) {
		super.save(areaCode);
	}
	
	@Transactional(readOnly = false)
	public void delete(AreaCode areaCode) {
		super.delete(areaCode);
	}
	public String getMaxAreacode(){
		return areaCodeMapper.getMaxAreacode();
	}
	
}