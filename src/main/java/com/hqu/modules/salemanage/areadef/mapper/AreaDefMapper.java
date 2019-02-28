/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.salemanage.areadef.mapper;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.hqu.modules.salemanage.areadef.entity.AreaDef;

/**
 * 销售地区定义MAPPER接口
 * @author 黄志兵
 * @version 2018-05-05
 */
@MyBatisMapper
public interface AreaDefMapper extends BaseMapper<AreaDef> {
	public String selectMax();
}