/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.qualitymanage.qmreportquery.mapper;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.hqu.modules.qualitymanage.qmreportquery.entity.QmReportQuery;

import java.util.List;

/**
 * 查询质量问题报告MAPPER接口
 * @author yangxianbang
 * @version 2018-04-17
 */
@MyBatisMapper
public interface QmReportQueryMapper extends BaseMapper<QmReportQuery> {
	public List<QmReportQuery> findListWithDetail(QmReportQuery qmReportQuery);
}