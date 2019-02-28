/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.purchasemanage.monthreport.mapper;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.hqu.modules.purchasemanage.monthreport.entity.MonthReport;

/**
 * 进料不合格统计表，关联IQC来料检验单MAPPER接口
 * @author ckw
 * @version 2018-08-25
 */
@MyBatisMapper
public interface MonthReportMapper extends BaseMapper<MonthReport> {
	
}