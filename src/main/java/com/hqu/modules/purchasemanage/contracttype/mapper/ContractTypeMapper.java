/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.purchasemanage.contracttype.mapper;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.hqu.modules.purchasemanage.contracttype.entity.ContractType;

/**
 * 合同类型定义MAPPER接口
 * @author tyo
 * @version 2018-05-26
 */
@MyBatisMapper
public interface ContractTypeMapper extends BaseMapper<ContractType> {

     String getMaxContractTypeID();
	
}