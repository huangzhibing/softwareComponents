/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.purchasemanage.plantype.mapper;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.hqu.modules.purchasemanage.plantype.entity.PlanType;

/**
 * PlanTypeMAPPER接口
 * @author 方翠虹
 * @version 2018-04-14
 */
@MyBatisMapper
public interface PlanTypeMapper extends BaseMapper<PlanType> {
	 /**
     * 获得计划类型的最大值流水号
     * @return MaxplanTypeId
     */
    public String getMaxPlantypeId();
	
}