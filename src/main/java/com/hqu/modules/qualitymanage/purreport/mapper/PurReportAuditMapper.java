/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.qualitymanage.purreport.mapper;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;

import java.util.List;

import com.hqu.modules.qualitymanage.purreport.entity.PurReport;

/**
 * 检验单(采购/装配/整机检测)MAPPER接口
 * @author 张铮
 * @version 2018-04-03
 */
@MyBatisMapper
public interface PurReportAuditMapper extends BaseMapper<PurReport> {
	//查找条件满足为检验状态‘未审核’并且满足jperson_code是当前登陆用户的记录
	public List<PurReport> findListByJustifyPerson(PurReport purReport);

	public List<PurReport> findListByAuditQuery(PurReport purReport);
}