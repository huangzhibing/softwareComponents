/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.purchasemanage.applymain.mapper;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.hqu.modules.purchasemanage.applymain.entity.ApplyDetail;

/**
 * 采购需求单子表MAPPER接口
 * @author syc
 * @version 2018-04-21
 */
@MyBatisMapper
public interface ApplyDetailMapper extends BaseMapper<ApplyDetail> {
	public void deleteByBillNum(String id);
}