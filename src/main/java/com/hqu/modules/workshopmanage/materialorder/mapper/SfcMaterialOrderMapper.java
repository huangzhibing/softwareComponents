/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.workshopmanage.materialorder.mapper;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.hqu.modules.workshopmanage.materialorder.entity.SfcMaterialOrder;

/**
 * 领料单MAPPER接口
 * @author zhangxin
 * @version 2018-05-22
 */
@MyBatisMapper
public interface SfcMaterialOrderMapper extends BaseMapper<SfcMaterialOrder> {
    //删除指定领料日期系统生成的还没审核的领料单
	public void deleteNoPassMaterialOrderWithDateTime(SfcMaterialOrder sfcMaterialOrder);
}