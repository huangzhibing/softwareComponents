/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.costmanage.personwage.mapper;

import com.hqu.modules.basedata.routing.entity.Routing;
import com.hqu.modules.workshopmanage.processroutine.entity.ProcessRoutine;
import com.hqu.modules.workshopmanage.processroutinedetail.entity.ProcessRoutineDetail;
import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.hqu.modules.costmanage.personwage.entity.PersonWage;
import org.h2.value.ValueStringIgnoreCase;

import java.util.List;
import java.util.Map;

/**
 * 人工工资MAPPER接口
 * @author 黄志兵
 * @version 2018-09-01
 */
@MyBatisMapper
public interface PersonWageMapper extends BaseMapper<PersonWage> {
	Double getQty(PersonWage personWage);
	PersonWage getBack(String id);
	PersonWage checkExist(ProcessRoutine processRoutine);
	String findMaxBillNum(String para);
	List<PersonWage> getPersonWageList(String billNum);
	String getOrderId(String processBillNo);
}