/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.basedata.item.mapper;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;

import java.util.List;

import com.hqu.modules.basedata.item.entity.Item;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 物料定义MAPPER接口
 * @author xn
 * @version 2018-04-06
 */
@MyBatisMapper
public interface ItemMapper extends BaseMapper<Item> {
	List<Item> findList2(Item item);
    public List<Item> findList1(Item entity);

    Item getByCode(String code);
}