/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.basedata.itemclass.mapper;

import com.jeeplus.core.persistence.TreeMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;

import java.util.Map;

import com.hqu.modules.basedata.itemclass.entity.ItemClass;

/**
 * 物料类定义MAPPER接口
 * 
 * @author dongqida
 * @version 2018-04-02
 */
@MyBatisMapper
public interface ItemClassMapper extends TreeMapper<ItemClass> {

	public ItemClass findByClassId(String classid);

	public ItemClass findByClassCode(String code);

	public void updateClassId(Map<String, Object> map);
}