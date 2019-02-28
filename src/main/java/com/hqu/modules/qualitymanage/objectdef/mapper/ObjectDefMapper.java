/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.qualitymanage.objectdef.mapper;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.hqu.modules.qualitymanage.objectdef.entity.ObjectDef;

/**
 * 检验对象定义MAPPER接口
 * @author M1ngz
 * @version 2018-04-07
 */
@MyBatisMapper
public interface ObjectDefMapper extends BaseMapper<ObjectDef> {
	public Integer getCodeNum(String objCode);
	public ObjectDef getByObjCode(ObjectDef objectDef);
}