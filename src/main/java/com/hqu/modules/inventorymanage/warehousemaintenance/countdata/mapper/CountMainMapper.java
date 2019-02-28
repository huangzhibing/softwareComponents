/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.inventorymanage.warehousemaintenance.countdata.mapper;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.hqu.modules.inventorymanage.warehousemaintenance.countdata.entity.CountMain;

/**
 * 盘点数据录入MAPPER接口
 * @author zb
 * @version 2018-04-23
 */
@MyBatisMapper
public interface CountMainMapper extends BaseMapper<CountMain> {
    String getMaxIdByTypeAndDate(String para);

}