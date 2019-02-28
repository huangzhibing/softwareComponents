/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.purchasemanage.adtdetail.mapper;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.hqu.modules.purchasemanage.adtdetail.entity.AdtDetail;

/**
 * 审核表MAPPER接口
 * @author ckw
 * @version 2018-05-08
 */
@MyBatisMapper
public interface AdtDetailMapper extends BaseMapper<AdtDetail> {
	public Integer findStep(AdtDetail adtDetail);
}