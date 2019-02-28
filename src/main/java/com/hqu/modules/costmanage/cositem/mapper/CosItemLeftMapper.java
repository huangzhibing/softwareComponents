/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.costmanage.cositem.mapper;

import com.jeeplus.core.persistence.TreeMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.hqu.modules.costmanage.cositem.entity.CosItemLeft;

/**
 * 科目定义MAPPER接口
 * @author zz
 * @version 2018-09-05
 */
@MyBatisMapper
public interface CosItemLeftMapper extends TreeMapper<CosItemLeft> {
	String getMaximumCode(CosItemLeft cosItemLeft);
	boolean hasChildren(String id);
	boolean hasItem(String id);
}