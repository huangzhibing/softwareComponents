/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.inventorymanage.outsourcingoutput.mapper;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.hqu.modules.inventorymanage.billmain.entity.BillMain;

/**
 * 出入库单据MAPPER接口
 * @author M1ngz
 * @version 2018-04-16
 */
@MyBatisMapper
public interface BillMainOutsourcingMapper extends BaseMapper<BillMain> {
	String getMaxIdByTypeAndDate(String para);
}