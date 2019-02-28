/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.costmanage.cosprodobject.mapper;

import com.jeeplus.core.persistence.TreeMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.hqu.modules.costmanage.cosprodobject.entity.CosProdObjectLeft;

/**
 * 核算对象定义MAPPER接口
 * @author zz
 * @version 2018-09-07
 */
@MyBatisMapper
public interface CosProdObjectLeftMapper extends TreeMapper<CosProdObjectLeft> {
	boolean hasChildren(String id);
	boolean hasItem(String id);
}