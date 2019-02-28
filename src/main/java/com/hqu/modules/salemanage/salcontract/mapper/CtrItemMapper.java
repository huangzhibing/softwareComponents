/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.salemanage.salcontract.mapper;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;

import java.util.Set;

import com.hqu.modules.salemanage.salcontract.entity.CtrItem;

/**
 * 销售合同子表MAPPER接口
 * @author dongqida
 * @version 2018-05-12
 */
@MyBatisMapper
public interface CtrItemMapper extends BaseMapper<CtrItem> {
	public Set<String> findByProdCode(String prodCode);
}