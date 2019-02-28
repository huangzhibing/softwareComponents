/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.purchasemanage.contractmain.mapper;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;

import java.util.List;

import com.hqu.modules.purchasemanage.contractmain.entity.ContractMain;

/**
 * 采购合同管理MAPPER接口
 * @author ltq
 * @version 2018-04-24
 */
@MyBatisMapper
public interface ContractMainMapper extends BaseMapper<ContractMain> {
	public List<ContractMain> findPageNew(ContractMain contractMain);
	public ContractMain getByProcInsId(String procInsId);
	public int updateInsId(ContractMain contractMain);
}