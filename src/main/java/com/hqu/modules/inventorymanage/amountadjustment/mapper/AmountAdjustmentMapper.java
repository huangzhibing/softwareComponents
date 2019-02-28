package com.hqu.modules.inventorymanage.amountadjustment.mapper;

import java.util.Map;

import org.springframework.stereotype.Repository;

import com.hqu.modules.basedata.period.entity.Period;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
@MyBatisMapper
public interface AmountAdjustmentMapper {
	public Period findPeriodByTime(Map<String, Object> map);
}
