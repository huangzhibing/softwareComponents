/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.qualitymanage.purinvcheckmain.mapper;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.hqu.modules.qualitymanage.purinvcheckmain.entity.ItemforInv;

import java.util.List;

/**
 * 物料定义MAPPER接口
 * @author 张铮
 * @version 2018-06-05
 */
@MyBatisMapper
public interface ItemforInvMapper extends BaseMapper<ItemforInv> {


    public List<ItemforInv> findList1(ItemforInv entity);

}