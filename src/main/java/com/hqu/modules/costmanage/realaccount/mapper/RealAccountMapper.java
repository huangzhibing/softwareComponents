/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.costmanage.realaccount.mapper;

import com.hqu.modules.costmanage.personwage.entity.PersonWage;
import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;

import java.util.List;

import com.hqu.modules.costmanage.realaccount.entity.RealAccount;

/**
 * 材料凭证单据管理MAPPER接口
 * @author hzb
 * @version 2018-09-05
 */
@MyBatisMapper
public interface RealAccountMapper extends BaseMapper<RealAccount> {

	String getMaxBillNum();

	List<RealAccount> findAccountingList(RealAccount realAccount);

	String getMaxCLCBBillNum();
	
	String getMaxRGCBBillNum();

}