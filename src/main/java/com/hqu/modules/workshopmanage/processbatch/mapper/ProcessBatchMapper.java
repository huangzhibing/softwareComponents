/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.workshopmanage.processbatch.mapper;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.hqu.modules.workshopmanage.processbatch.entity.ProcessBatch;

import java.util.List;

/**
 * 车间作业计划分批表MAPPER接口
 * @author xhc
 * @version 2018-08-04
 */
@MyBatisMapper
public interface ProcessBatchMapper extends BaseMapper<ProcessBatch> {
    //billState = 'C' 的计划 (已经确认)，且 assignedState = "P"(未处理或者正在计划)
	public List<ProcessBatch> findListForProcessRoutine(ProcessBatch processBatch);
    //billState = 'C' 的计划 (已经确认)，且 assignedState = "P"(未处理或者正在计划) or "C"(已提交)
    public List<ProcessBatch> findListForProcessRoutineQuery(ProcessBatch processBatch);
}