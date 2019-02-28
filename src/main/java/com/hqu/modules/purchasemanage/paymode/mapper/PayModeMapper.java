/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.purchasemanage.paymode.mapper;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.hqu.modules.purchasemanage.paymode.entity.PayMode;

/**
 * 付款方式定义MAPPER接口
 * @author 石豪迈
 * @version 2018-04-14
 */
@MyBatisMapper
public interface PayModeMapper extends BaseMapper<PayMode> {
	 /**
     * 获得付款方式编码的最大值流水号
     * @return MaxPaymodeid
     */
    
	public String getMaxPaymodeid();
	
}