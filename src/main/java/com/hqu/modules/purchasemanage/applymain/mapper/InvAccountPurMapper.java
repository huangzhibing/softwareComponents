/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.purchasemanage.applymain.mapper;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;

import org.apache.ibatis.annotations.Param;

import com.hqu.modules.purchasemanage.applymain.entity.InvAccountPur;

/**
 * 库存账MAPPER接口
 * @author M1ngz
 * @version 2018-04-22
 */
@MyBatisMapper
public interface InvAccountPurMapper extends BaseMapper<InvAccountPur> {
	//public InvAccountPur getInvAccountByItemCode(@Param("itemCode")String itemCode);

}