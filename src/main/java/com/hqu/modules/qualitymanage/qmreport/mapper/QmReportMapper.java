/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.qualitymanage.qmreport.mapper;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.hqu.modules.qualitymanage.qmreport.entity.QmReport;

/**
 * 质量问题报告MAPPER接口
 * @author yangxianbang
 * @version 2018-04-10
 */
@MyBatisMapper
public interface QmReportMapper extends BaseMapper<QmReport> {
	int updateProcessInstanceId(QmReport qmReport);
}