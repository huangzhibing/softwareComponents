/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.purchasemanage.applymainaudit.mapper;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.hqu.modules.purchasemanage.applymainaudit.entity.ApplyMainAudit;

/**
 * 采购需求审批MAPPER接口
 * @author syc
 * @version 2018-05-08
 */
@MyBatisMapper
public interface ApplyMainAuditMapper extends BaseMapper<ApplyMainAudit> {
	public ApplyMainAudit getByProInId(String pid);
}