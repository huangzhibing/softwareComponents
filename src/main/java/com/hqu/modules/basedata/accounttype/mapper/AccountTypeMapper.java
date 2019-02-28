/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.basedata.accounttype.mapper;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.hqu.modules.basedata.accounttype.entity.AccountType;

/**
 * 企业类型表单MAPPER接口
 * @author 黄志兵
 * @version 2018-04-05
 */
@MyBatisMapper
public interface AccountTypeMapper extends BaseMapper<AccountType> {

    /**
     * 获得关系企业代码的最大值流水号
     * @return MaxAccTypeCode
     */
    public String getMaxAccTypeCode();
}