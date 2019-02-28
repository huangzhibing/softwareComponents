/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.inventorymanage.ppcsearch.mapper;

import com.hqu.modules.inventorymanage.ppcsearch.entity.MSerialNoSearch;
import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.hqu.modules.workshopmanage.ppc.entity.MSerialNo;

/**
 * 编码关联表MAPPER接口
 * @author yxb
 * @version 2018-10-31
 */
@MyBatisMapper
public interface MSerialNoSearchMapper extends BaseMapper<MSerialNoSearch> {
	
}