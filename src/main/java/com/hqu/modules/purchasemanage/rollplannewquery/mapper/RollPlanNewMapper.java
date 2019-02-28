/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.purchasemanage.rollplannewquery.mapper;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.hqu.modules.purchasemanage.rollplannewquery.entity.RollPlanNew;

import java.util.List;

/**
 * 滚动计划查询MAPPER接口
 * @author yangxianbang
 * @version 2018-05-08
 */
@MyBatisMapper
public interface RollPlanNewMapper extends BaseMapper<RollPlanNew> {
//获取包涵了采购计划已选的滚动计划和未加入计划的滚动计划
    List<RollPlanNew> findListForPlan(RollPlanNew rollPlanNew);
}