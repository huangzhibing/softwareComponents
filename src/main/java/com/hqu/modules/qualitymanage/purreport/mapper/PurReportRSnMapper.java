/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.qualitymanage.purreport.mapper;

import java.util.List;

import com.hqu.modules.qualitymanage.purreport.entity.PurReportRSn;
import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;

/**
 * 质量管理检验抽检表MAPPER接口
 * @author 孙映川
 * @version 2018-04-05
 */
@MyBatisMapper
public interface PurReportRSnMapper extends BaseMapper<PurReportRSn> {
	public void myUpdate(PurReportRSn purReportRSn);
	public void myInsert(PurReportRSn purReportRSn);
	
	public void deleteAllByReportId(String id);
	public List<PurReportRSn> getList(String id);
	
}