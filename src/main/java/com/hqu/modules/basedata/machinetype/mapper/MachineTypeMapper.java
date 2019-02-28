/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.basedata.machinetype.mapper;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.hqu.modules.basedata.machinetype.entity.MachineType;

/**
 * 设备类别定义MAPPER接口
 * @author 何志铭
 * @version 2018-04-05
 */
@MyBatisMapper
public interface MachineTypeMapper extends BaseMapper<MachineType> {
	public String selectMax();
}