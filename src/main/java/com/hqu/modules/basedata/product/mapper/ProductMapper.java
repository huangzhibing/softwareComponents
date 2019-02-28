/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.basedata.product.mapper;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.hqu.modules.basedata.product.entity.Product;

/**
 * 产品维护MAPPER接口
 * @author M1ngz
 * @version 2018-04-06
 */
@MyBatisMapper
public interface ProductMapper extends BaseMapper<Product> {
	Product getById(String id);
	Integer getCountByCode(String code);
}