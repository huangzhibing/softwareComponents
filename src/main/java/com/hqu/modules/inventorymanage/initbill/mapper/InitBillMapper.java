package com.hqu.modules.inventorymanage.initbill.mapper;

import java.util.Map;

import org.springframework.stereotype.Repository;

import com.hqu.modules.basedata.period.entity.Period;
import com.hqu.modules.inventorymanage.invaccount.entity.InvAccount;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
@MyBatisMapper
public interface InitBillMapper {
	public Period findPeriodByTime(Map<String, Object> map);
	
	public Boolean changeRealQty(Map<String, Object>map);
	
	public Boolean updateQty(Map<String, Object> map);
	
	public Boolean updateRealQty(Map<String, Object> map);
}
