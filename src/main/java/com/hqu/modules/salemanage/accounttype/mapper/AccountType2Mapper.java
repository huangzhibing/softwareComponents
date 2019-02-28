/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.salemanage.accounttype.mapper;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.hqu.modules.salemanage.accounttype.entity.AccountType2;

/**
 * 客户类型定义MAPPER接口
 * @author hzm
 * @version 2018-05-05
 */
@MyBatisMapper
public interface AccountType2Mapper extends BaseMapper<AccountType2> {
	public String selectMax();
}