/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.salemanage.contractrtn.mapper;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.hqu.modules.salemanage.contractrtn.entity.ContractRtn;

/**
 * 销售回款管理MAPPER接口
 * @author liujiachen
 * @version 2018-07-09
 */
@MyBatisMapper
public interface ContractRtnMapper extends BaseMapper<ContractRtn> {
    String getMaxIdByTypeAndDate(String para);
}