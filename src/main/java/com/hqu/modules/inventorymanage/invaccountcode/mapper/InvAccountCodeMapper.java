/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.inventorymanage.invaccountcode.mapper;

import com.hqu.modules.inventorymanage.billmain.entity.BillDetailCode;
import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.hqu.modules.inventorymanage.invaccountcode.entity.InvAccountCode;

/**
 * 库存帐扫码MAPPER接口
 * @author M1ngz
 * @version 2018-06-03
 */
@MyBatisMapper
public interface InvAccountCodeMapper extends BaseMapper<InvAccountCode> {

    void insertByProduct(BillDetailCode billDetailCode);
}