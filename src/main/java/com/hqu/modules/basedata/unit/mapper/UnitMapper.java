/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.basedata.unit.mapper;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.hqu.modules.basedata.unit.entity.Unit;

import java.util.List;

/**
 * 计量单位定义MAPPER接口
 * @author 黄志兵
 * @version 2018-04-05
 */
@MyBatisMapper
public interface UnitMapper extends BaseMapper<Unit> {

    /**
     * 获得符合标准的计量单位编码
     * @return
     */
    public List<Unit> getStandUnitCode();

    /**
     * 获得最大的计量单位编码流水号
     * @return UnitCode
     */
    public String getMaxUnitCode();


}