/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.inventorymanage.location.mapper;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.hqu.modules.inventorymanage.location.entity.Location;

/**
 * 货位管理MAPPER接口
 * @author M1ngz
 * @version 2018-04-12
 */
@MyBatisMapper
public interface LocationMapper extends BaseMapper<Location> {
	String getMaxId(String binId);
	String getByBinId(String binId);
	Location getByLocId(String locId);
}