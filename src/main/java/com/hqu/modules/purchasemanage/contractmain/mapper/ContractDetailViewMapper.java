/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.purchasemanage.contractmain.mapper;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;

import java.util.List;

import com.hqu.modules.purchasemanage.contractmain.entity.ContractDetailView;
import com.hqu.modules.purchasemanage.contractmain.entity.ContractDetailWarning;
import com.hqu.modules.purchasemanage.contractmain.entity.ContractMain;

/**
 * 采购订单汇总MAPPER接口
 * @author ltq
 * @version 2018-04-24
 */
@MyBatisMapper
public interface ContractDetailViewMapper extends BaseMapper<ContractDetailView> {
	public List<ContractDetailView> findListNotArriveAll(ContractDetailView contractDetailView);
}