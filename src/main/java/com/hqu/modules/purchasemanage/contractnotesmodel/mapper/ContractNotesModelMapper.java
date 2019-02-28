/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.purchasemanage.contractnotesmodel.mapper;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.hqu.modules.purchasemanage.contractnotesmodel.entity.ContractNotesModel;

/**
 * 合同模板定义MAPPER接口
 * @author luyumiao
 * @version 2018-04-25
 */
@MyBatisMapper
public interface ContractNotesModelMapper extends BaseMapper<ContractNotesModel> {

    /**
     * 获得合同模板代码的最大值
     * @return MaxAccTypeCode
     */
    public String getMaxContractId();
}