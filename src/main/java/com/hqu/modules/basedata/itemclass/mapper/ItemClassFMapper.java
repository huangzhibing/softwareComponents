/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.basedata.itemclass.mapper;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;

import java.util.List;
import java.util.Map;

import com.hqu.modules.basedata.itemclass.entity.ItemClassF;

/**
 * 物料附表MAPPER接口
 * 
 * @author dongqida
 * @version 2018-04-02
 */
@MyBatisMapper
public interface ItemClassFMapper extends BaseMapper<ItemClassF> {

	public List<ItemClassF> findByCodeLength(Map<String, Object> map);

	public ItemClassF findByCode(String code);

	public String findCodeById(String id);
	public String findMaxCode();
}