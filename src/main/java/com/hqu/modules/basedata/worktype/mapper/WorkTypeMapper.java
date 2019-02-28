/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.basedata.worktype.mapper;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.hqu.modules.basedata.worktype.entity.WorkType;

/**
 * 工种定义MAPPER接口
 * @author zb
 * @version 2018-04-07
 */
@MyBatisMapper
public interface WorkTypeMapper extends BaseMapper<WorkType> {
	Integer getCodeNum(String workTypeCode);
}