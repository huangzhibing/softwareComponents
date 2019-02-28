/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.purchasemanage.purplan.mapper;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.hqu.modules.purchasemanage.purplan.entity.PlanRelations;

/**
 * 关系描述MAPPER接口
 * @author yxb
 * @version 2018-05-14
 */
@MyBatisMapper
public interface PlanRelationsMapper extends BaseMapper<PlanRelations> {
	public int deleteByBillNum(PlanRelations planRelations);

    public int deleteByBillNumAndSerialNum(PlanRelations planRelations);
}