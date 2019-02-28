package com.hqu.modules.inventorymanage.outsourcingoutput.mapper;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.hqu.modules.basedata.period.entity.Period;
import com.hqu.modules.inventorymanage.billmain.entity.BillDetailCode;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
@MyBatisMapper
public interface OutsourcingOutputMapper {
	public Period findPeriodByTime(Map<String, Object> map);
	
	public Boolean changeRealQty(Map<String, Object>map);
	
	public Boolean changeBillFlagById(Map<String, Object> map);
	/**
	 * 找二维码
	 * @param billNum
	 * @return
	 */
	public List<BillDetailCode> findCodeByBillNum(String billNum);
	
	public Boolean updateOflagByBarcode(Map<String, Object> map);
	
	public String findDeptCode(String id);
	
}
