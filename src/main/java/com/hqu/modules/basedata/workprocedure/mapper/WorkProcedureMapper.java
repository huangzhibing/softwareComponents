/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.basedata.workprocedure.mapper;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.hqu.modules.basedata.workprocedure.entity.WorkProcedure;

/**
 * 工序定义MAPPER接口
 * @author liujiachen
 * @version 2018-04-24
 */
@MyBatisMapper
public interface WorkProcedureMapper extends BaseMapper<WorkProcedure> {
    public String getMaxWorkProcedureId();
}