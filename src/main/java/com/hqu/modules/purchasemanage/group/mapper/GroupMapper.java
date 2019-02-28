/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.purchasemanage.group.mapper;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.hqu.modules.purchasemanage.group.entity.Group;

/**
 * GroupMAPPER接口
 * @author 方翠虹,石豪迈,陶瑶
 * @version 2018-04-18
 */
@MyBatisMapper
public interface GroupMapper extends BaseMapper<Group> {
	/**
     * 获得采购组代码的最大值流水号
     * @return MaxGroupid
     */
    public String getMaxGroupid();
}