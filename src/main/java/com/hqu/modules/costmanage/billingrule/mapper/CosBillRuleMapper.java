/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.costmanage.billingrule.mapper;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.hqu.modules.costmanage.billingrule.entity.CosBillRule;
import com.hqu.modules.costmanage.cosbillrecord.entity.CosBillRecord;

/**
 * 制单规则MAPPER接口
 * @author ccr
 * @version 2018-09-01
 */
@MyBatisMapper
public interface CosBillRuleMapper extends BaseMapper<CosBillRule> {
	public String getMaxRuleId();
}