/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.purchasemanage.contractmain.mapper;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.hqu.modules.purchasemanage.contractmain.entity.ConRelations;

/**
 * 关系描述MAPPER接口
 * @author ltq
 * @version 2018-05-17
 */
@MyBatisMapper
public interface ConRelationsMapper extends BaseMapper<ConRelations> {
	ConRelations getConRelations(ConRelations conRelations);
}