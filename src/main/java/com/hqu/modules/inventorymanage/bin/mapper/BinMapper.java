/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.inventorymanage.bin.mapper;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.hqu.modules.inventorymanage.bin.entity.Bin;

import java.util.List;

/**
 * 货区定义MAPPER接口
 * @author M1ngz
 * @version 2018-04-12
 */
@MyBatisMapper
public interface BinMapper extends BaseMapper<Bin> {
	String getMaxId(String wareId);
	Bin getByBinId(String binId);
}