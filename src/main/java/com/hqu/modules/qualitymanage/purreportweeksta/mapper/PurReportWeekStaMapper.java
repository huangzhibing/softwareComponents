/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.qualitymanage.purreportweeksta.mapper;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.hqu.modules.qualitymanage.purreportweeksta.entity.PurReportWeekSta;

import java.util.List;

/**
 * 进料检验统计MAPPER接口
 * @author yxb
 * @version 2018-08-25
 */
@MyBatisMapper
public interface PurReportWeekStaMapper extends BaseMapper<PurReportWeekSta> {
    public PurReportWeekSta sumReportNewByDate(PurReportWeekSta purReportWeekSta);
    public void deleteByYearMonth(PurReportWeekSta purReportWeekSta);
}