/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.inventorymanage.warehouse.mapper;

import com.hqu.modules.inventorymanage.bin.entity.Bin;
import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.hqu.modules.inventorymanage.warehouse.entity.WareHouse;

/**
 * 仓库管理MAPPER接口
 * @author M1ngz
 * @version 2018-04-12
 */
@MyBatisMapper
public interface WareHouseMapper extends BaseMapper<WareHouse> {
    String getMaxId();
    WareHouse getByWareId(String wareId);
}