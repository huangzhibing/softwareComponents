/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.purchasemanage.contractmain.mapper;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.hqu.modules.purchasemanage.contractmain.entity.ContractDetail;

/**
 * 采购合同子表MAPPER接口
 * @author ltq
 * @version 2018-04-24
 */
@MyBatisMapper
public interface ContractDetailMapper extends BaseMapper<ContractDetail> {
	ContractDetail	getContractDetail(ContractDetail contractDetail);
}