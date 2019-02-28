/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.salemanage.salcontract.mapper;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;

import java.util.Map;

import com.hqu.modules.salemanage.salcontract.entity.Contract;

/**
 * 销售合同MAPPER接口
 * @author dongqida
 * @version 2018-05-12
 */
@MyBatisMapper
public interface ContractMapper extends BaseMapper<Contract> {
	String getMaxIdByTypeAndDate(String para);
	
	public Boolean updateStat(Map<String, Object>map);
	
	public Boolean updateSuggestions(Map<String, Object>map);

	Contract getByCode(String id);
}