/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.costmanage.reportmanage.reportaccountmapper;

import com.hqu.modules.costmanage.reportmanage.entity.ReportPersonWage;
import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;

/**
 * 人工工资MAPPER接口
 * @author 黄志兵
 * @version 2018-09-01
 */
@MyBatisMapper
public interface ReportAccountMapper extends BaseMapper<ReportPersonWage> {
	Double getQty(ReportPersonWage personWage);
	ReportPersonWage getBack(String id);
}