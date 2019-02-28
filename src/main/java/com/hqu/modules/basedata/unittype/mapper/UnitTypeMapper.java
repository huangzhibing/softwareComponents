/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.basedata.unittype.mapper;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.hqu.modules.basedata.unittype.entity.UnitType;

/**
 * 计量单位类别维护MAPPER接口
 * @author yrg
 * @version 2018-04-05
 */
@MyBatisMapper
public interface UnitTypeMapper extends BaseMapper<UnitType> {

	public String selectMax();

}