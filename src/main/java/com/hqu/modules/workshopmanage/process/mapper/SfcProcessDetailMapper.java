/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.workshopmanage.process.mapper;

import com.hqu.modules.workshopmanage.process.entity.SfcProcessDetail;
import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;

import java.util.List;

/**
 * 车间作业计划子表MAPPER接口
 * @author xhc
 * @version 2018-08-02
 */
@MyBatisMapper
public interface SfcProcessDetailMapper extends BaseMapper<SfcProcessDetail> {
    public List<SfcProcessDetail> findAssignedList(SfcProcessDetail sfcProcessDetail);

    public List<SfcProcessDetail> findAssignedAllList(SfcProcessDetail sfcProcessDetail);
}