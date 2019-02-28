/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.inventorymanage.resumecheckout.mapper;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;

import java.util.Date;
import java.util.List;

import com.hqu.modules.basedata.machinetype.entity.MachineType;
import com.hqu.modules.basedata.period.entity.Period;

/**
 * 设备类别定义MAPPER接口
 * @author 何志铭
 * @version 2018-04-05
 */
@MyBatisMapper
public interface ResumeCheckoutMapper extends BaseMapper<MachineType> {

	public List<String> findLastByDate(Date date);

	public List<String> findNextByDate(Date date);
	
}