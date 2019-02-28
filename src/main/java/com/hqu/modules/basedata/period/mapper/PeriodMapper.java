/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.basedata.period.mapper;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.hqu.modules.basedata.period.entity.Period;

import java.util.Date;
import java.util.Map;

/**
 * 企业核算期定义MAPPER接口
 * @author Neil
 * @version 2018-06-02
 */
@MyBatisMapper
public interface PeriodMapper extends BaseMapper<Period> {
	public Period getCurrentPeriod(Period period);
	public String getPeriodByClose();
	public Map<Object,Date> getBeginEndPeriod(String periodId);
}