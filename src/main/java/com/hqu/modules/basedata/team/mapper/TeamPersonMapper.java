/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.basedata.team.mapper;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.hqu.modules.basedata.team.entity.TeamPerson;

/**
 * 班组人员关系表MAPPER接口
 * @author xn
 * @version 2018-04-16
 */
@MyBatisMapper
public interface TeamPersonMapper extends BaseMapper<TeamPerson> {
	
}