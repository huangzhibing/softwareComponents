/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.inventorymanage.invaccount.mapper;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.hqu.modules.inventorymanage.invaccount.entity.InvAccount;

import java.util.List;
import java.util.Map;

/**
 * 库存账MAPPER接口
 * @author M1ngz
 * @version 2018-04-22
 */
@MyBatisMapper
public interface InvAccountMapper extends BaseMapper<InvAccount> {
    List<InvAccount> findAllItemByWareCondition(InvAccount invAccount);

    Boolean updateRealQty(Map<String, Object>map);

    InvAccount getByInvAccount(InvAccount invAccount);

}