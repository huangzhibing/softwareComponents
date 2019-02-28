/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.qualitymanage.purinvcheckmain.mapper;

import java.util.List;

import com.hqu.modules.qualitymanage.purinvcheckmain.entity.InvCheckDetail;
import com.hqu.modules.qualitymanage.purinvcheckmain.entity.InvCheckMain;
import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;

/**
 * 入库通知单子表MAPPER接口
 * @author 张铮
 * @version 2018-04-21
 */
@MyBatisMapper
public interface InvCheckDetailMapper extends BaseMapper<InvCheckDetail> {
	public List<InvCheckDetail> findListbyBillNum(InvCheckDetail invCheckDetail);
}