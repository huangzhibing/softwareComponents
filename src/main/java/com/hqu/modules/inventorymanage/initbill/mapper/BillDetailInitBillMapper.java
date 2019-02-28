/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.inventorymanage.initbill.mapper;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;

import java.util.List;

import com.hqu.modules.inventorymanage.billmain.entity.BillDetail;

/**
 * 出入库单据子表MAPPER接口
 * @author M1ngz
 * @version 2018-04-16
 */
@MyBatisMapper
public interface BillDetailInitBillMapper extends BaseMapper<BillDetail> {
	public List<String> findIdByItemCode(String itemCode);
}