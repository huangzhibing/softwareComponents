/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.costmanage.reportmanage.productCostMapper;

import com.hqu.modules.costmanage.reportmanage.entity.ReportRealAccount;
import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;

import java.util.List;



/**
 * 材料凭证单据管理MAPPER接口
 * @author hzb
 * @version 2018-09-05
 */
@MyBatisMapper
public interface ProductCostMapper extends BaseMapper<ReportRealAccount> {

	String getMaxBillNum();

	List<ReportRealAccount> findAccountingList(ReportRealAccount realAccount);

	String getMaxCLCBBillNum();
	
	String getMaxRGCBBillNum();
}