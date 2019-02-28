/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.salemanage.pickbill.mapper;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.hqu.modules.salemanage.pickbill.entity.PickBill;

/**
 * 销售发货单据MAPPER接口
 * @author M1ngz
 * @version 2018-05-15
 */
@MyBatisMapper
public interface PickBillMapper extends BaseMapper<PickBill> {
    /**
     * 更新流程实例ID
     * @param pickBill
     * @return
     */
    public int updateProcessInstanceId(PickBill pickBill);


    public PickBill getByProInsId(String id);

    /**
     * 更新实际开始结束时间
     * @param pickBill
     * @return
     */
    public int updateRealityTime(PickBill pickBill);

    public String getMaxId();
}