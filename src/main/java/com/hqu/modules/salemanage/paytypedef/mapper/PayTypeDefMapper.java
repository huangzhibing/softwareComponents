/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.salemanage.paytypedef.mapper;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.hqu.modules.salemanage.paytypedef.entity.PayTypeDef;

/**
 * 付款方式定义MAPPER接口
 * @author M1ngz
 * @version 2018-05-07
 */
@MyBatisMapper
public interface PayTypeDefMapper extends BaseMapper<PayTypeDef> {
	public String selectMax();
}