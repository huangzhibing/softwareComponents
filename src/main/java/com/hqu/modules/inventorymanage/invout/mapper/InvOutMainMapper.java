/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.inventorymanage.invout.mapper;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.hqu.modules.inventorymanage.invout.entity.InvOutMain;

/**
 * 放行单MAPPER接口
 * @author M1ngz
 * @version 2018-06-02
 */
@MyBatisMapper
public interface InvOutMainMapper extends BaseMapper<InvOutMain> {
    /**
     * 更新流程实例ID
     * @param pickBill
     * @return
     */
    public int updateProcessInstanceId(InvOutMain invOutMain);


    public InvOutMain getByProInsId(String id);

    /**
     * 更新实际开始结束时间
     * @param pickBill
     * @return
     */
    public int updateRealityTime(InvOutMain invOutMain);

    public String getMaxId();
}