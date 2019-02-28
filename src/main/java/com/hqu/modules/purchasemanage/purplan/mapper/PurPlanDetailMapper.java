/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.purchasemanage.purplan.mapper;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.hqu.modules.purchasemanage.purplan.entity.PurPlanDetail;

/**
 * 采购计划子表MAPPER接口
 * @author yangxianbang
 * @version 2018-04-23
 */
@MyBatisMapper
public interface PurPlanDetailMapper extends BaseMapper<PurPlanDetail> {
	PurPlanDetail getPurPlanDetail(PurPlanDetail purPlanDetail);
	public String getUserNo(PurPlanDetail purPlanDetail);
}