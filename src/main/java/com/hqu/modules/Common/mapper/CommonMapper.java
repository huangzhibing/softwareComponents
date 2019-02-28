/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.Common.mapper;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;

import java.util.Map;

/**
 * 关系企业维护MAPPER接口
 * @author M1ngz
 * @version 2018-04-05
 */
@MyBatisMapper
public interface CommonMapper {
	Integer checkCode(Map<String,Object> condition);
}