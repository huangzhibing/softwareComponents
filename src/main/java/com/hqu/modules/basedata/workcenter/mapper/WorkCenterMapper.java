/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.basedata.workcenter.mapper;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.hqu.modules.basedata.workcenter.entity.WorkCenter;

/**
 * 工作中心MAPPER接口
 * @author dongqida
 * @version 2018-04-05
 */
@MyBatisMapper
public interface WorkCenterMapper extends BaseMapper<WorkCenter> {
	
	public String findMaxCode();
}