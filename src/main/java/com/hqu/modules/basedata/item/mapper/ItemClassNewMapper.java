/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.basedata.item.mapper;

import com.jeeplus.core.persistence.TreeMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.hqu.modules.basedata.item.entity.ItemClassNew;

/**
 * 物料定义MAPPER接口
 * @author xn
 * @version 2018-04-06
 */
@MyBatisMapper
public interface ItemClassNewMapper extends TreeMapper<ItemClassNew> {
	ItemClassNew getByClassId(String classId);
}