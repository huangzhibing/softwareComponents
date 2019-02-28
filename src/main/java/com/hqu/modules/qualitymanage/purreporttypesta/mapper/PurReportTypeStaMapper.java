/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.qualitymanage.purreporttypesta.mapper;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.hqu.modules.qualitymanage.purreporttypesta.entity.PurReportTypeSta;

import java.util.List;

/**
 * 来料按物料类别统计分析MAPPER接口
 * @author yxb
 * @version 2018-08-25
 */
@MyBatisMapper
public interface PurReportTypeStaMapper extends BaseMapper<PurReportTypeSta> {
	public List<PurReportTypeSta> sumReportNewByDate(PurReportTypeSta purReportTypeSta);
}