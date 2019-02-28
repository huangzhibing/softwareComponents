/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.basedata.routing.mapper;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.hqu.modules.basedata.routing.entity.RoutingItem;

/**
 * 工艺路线物料关系表MAPPER接口
 * @author hzm
 * @version 2018-08-26
 */
@MyBatisMapper
public interface RoutingItemMapper extends BaseMapper<RoutingItem> {
	
}