/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.workshopmanage.ppc.mapper;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.hqu.modules.workshopmanage.ppc.entity.MpsPlan;

import java.util.List;

/**
 * MPSPlanMAPPER接口
 * @author XHC
 * @version 2018-06-01
 */
@MyBatisMapper
public interface MpsPlanMapper extends BaseMapper<MpsPlan> {
    public List<MpsPlan> findMyList(MpsPlan entity);
    public List<MpsPlan> findAuditList(MpsPlan entity);
    public MpsPlan getById(String id);
    public List<MpsPlan> findMRPDealList(MpsPlan entity);
    public List<MpsPlan> findMRPAuditList(MpsPlan entity);
    public List<MpsPlan> findSFCDealList(MpsPlan entity);
    public List<MpsPlan> findSFCDealCList(MpsPlan mpsPlan);
    public List<MpsPlan> findSFCDealAllList(MpsPlan mpsPlan);
}