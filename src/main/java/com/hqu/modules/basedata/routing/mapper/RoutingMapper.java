/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.basedata.routing.mapper;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.hqu.modules.basedata.routing.entity.Routing;

import java.util.List;

/**
 * 工艺路线MAPPER接口
 * @author huang.youcai
 * @version 2018-06-02
 */
@MyBatisMapper
public interface RoutingMapper extends BaseMapper<Routing> {

    public String getMaxRoutingCode();
    public List<Routing> findListByCode(Routing routing);
}