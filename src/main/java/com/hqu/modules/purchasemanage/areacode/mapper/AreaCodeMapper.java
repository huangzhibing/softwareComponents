/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.purchasemanage.areacode.mapper;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.hqu.modules.purchasemanage.areacode.entity.AreaCode;

/**
 * 地区定义MAPPER接口
 * @author 石豪迈
 * @version 2018-04-25
 */
@MyBatisMapper
public interface AreaCodeMapper extends BaseMapper<AreaCode> {
	 /**
     * 获得地区编码的最大值流水号
     * @return MaxAreacode
     */
    public String getMaxAreacode();
	
}