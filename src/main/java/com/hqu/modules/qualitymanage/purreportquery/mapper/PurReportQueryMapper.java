/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.qualitymanage.purreportquery.mapper;

import java.util.List;

import com.hqu.modules.qualitymanage.purreportquery.entity.PurReportQuery;
import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.jeeplus.modules.sys.entity.Office;
import com.jeeplus.modules.sys.entity.User;

/**
 * 检验单查询(采购/装配/整机检测)MAPPER接口
 * @author 孙映川
 * @version 2018-04-17
 */
@MyBatisMapper
public interface PurReportQueryMapper extends BaseMapper<PurReportQuery> {
	//通过用户id获取所属office列表
	public List<Office> getOfficeListByUserId(String userId);
	
	//由office的Id获取该部门的所有成员
	public List<User> getUserByOfficeId(String officeId);
	
	
	
}