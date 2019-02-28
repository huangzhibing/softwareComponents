/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.workshopmanage.sfcproduceratio.mapper;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.hqu.modules.workshopmanage.sfcproduceratio.entity.SfcProduceSta;

import java.util.List;

/**
 * 车间产出量统计MAPPER接口
 * @author yxb
 * @version 2018-11-25
 */
@MyBatisMapper
public interface SfcProduceStaMapper extends BaseMapper<SfcProduceSta> {
    //查找指定时间段内的每个工位组装产品包含的异常
    List<SfcProduceSta> findFailList(SfcProduceSta sfcProduceSta);
}