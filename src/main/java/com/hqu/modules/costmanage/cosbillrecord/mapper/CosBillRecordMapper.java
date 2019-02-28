/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.costmanage.cosbillrecord.mapper;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.hqu.modules.costmanage.cosbillrecord.entity.CosBillRecord;

/**
 * 材料单据稽核MAPPER接口
 * @author zz
 * @version 2018-08-29
 */
@MyBatisMapper
public interface CosBillRecordMapper extends BaseMapper<CosBillRecord> {
	CosBillRecord getBack(String id);
}