/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.basedata.calendar.mapper;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;

import java.util.Map;

import com.hqu.modules.basedata.calendar.entity.Calendar;

/**
 * 企业日历定义MAPPER接口
 * @author 何志铭
 * @version 2018-04-04
 */
@MyBatisMapper
public interface CalendarMapper extends BaseMapper<Calendar> {

	Integer checkCode(Map<String,Object> condition);
	
}