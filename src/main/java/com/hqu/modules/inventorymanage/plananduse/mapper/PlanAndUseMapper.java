package com.hqu.modules.inventorymanage.plananduse.mapper;

import java.util.List;

import com.hqu.modules.purchasemanage.purplan.entity.PurPlanDetail;
import com.hqu.modules.purchasemanage.purplan.entity.PurPlanMain;
import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
@MyBatisMapper
public interface PlanAndUseMapper extends BaseMapper<PurPlanMain>{
	public List<PurPlanDetail> findListTest(PurPlanDetail entity);
}
