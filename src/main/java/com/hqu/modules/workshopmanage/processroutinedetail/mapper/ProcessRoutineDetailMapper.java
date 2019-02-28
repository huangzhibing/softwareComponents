/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.workshopmanage.processroutinedetail.mapper;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.hqu.modules.workshopmanage.processroutinedetail.entity.ProcessRoutineDetail;

import java.util.List;

/**
 * 加工路线单明细单MAPPER接口
 * @author xhc
 * @version 2018-08-22
 */
@MyBatisMapper
public interface ProcessRoutineDetailMapper extends BaseMapper<ProcessRoutineDetail> {
    //更新invcheckstate状态
    public void updateInvCheckState(ProcessRoutineDetail processRoutineDetail);

    //物料安装明细录入专用（带有当前负责人权限配置）
    public List<ProcessRoutineDetail> findAssembleList(ProcessRoutineDetail processRoutineDetail);

    //结合车间物料安装明细表查找工艺路线明细单数据（车间物料安装明细查询用，查找是否存在零件品质异常）
    public List<ProcessRoutineDetail> findListWithMaterialDetail(ProcessRoutineDetail processRoutineDetail);

	//查找islastrouting 为 Y且assemble为Y的工艺数据 （最后一道总装工艺，完工入库单生成用）
    public List<ProcessRoutineDetail> findInvCheckList(ProcessRoutineDetail processRoutineDetail);
}