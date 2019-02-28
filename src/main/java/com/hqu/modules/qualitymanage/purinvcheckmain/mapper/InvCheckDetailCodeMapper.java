/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.qualitymanage.purinvcheckmain.mapper;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;

import java.util.List;

import com.hqu.modules.qualitymanage.purinvcheckmain.entity.InvCheckDetailCode;
import com.hqu.modules.qualitymanage.purinvcheckmain.entity.InvCheckMain;

/**
 * 二维码MAPPER接口
 * @author 张铮
 * @version 2018-06-22
 */
@MyBatisMapper
public interface InvCheckDetailCodeMapper extends BaseMapper<InvCheckDetailCode> {
	List<InvCheckDetailCode> findListbyBillNum(InvCheckDetailCode invCheckDetailCode);
}