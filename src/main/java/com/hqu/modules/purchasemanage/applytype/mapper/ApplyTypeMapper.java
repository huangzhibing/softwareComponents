/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.purchasemanage.applytype.mapper;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.hqu.modules.purchasemanage.applytype.entity.ApplyType;

/**
 * ApplyTypeMAPPER接口
 * @author 方翠虹
 * @version 2018-04-25
 */
@MyBatisMapper
public interface ApplyTypeMapper extends BaseMapper<ApplyType> {
	 /**
     * 获得付款方式编码的最大值流水号
     * @return MaxApplytypeid
     */
    
	public String getMaxApplytypeid();
	

}