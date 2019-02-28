/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.purchasemanage.taxratio.mapper;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.hqu.modules.purchasemanage.taxratio.entity.TaxRatio;

/**
 * 采购税率定义MAPPER接口
 * @author luyumiao
 * @version 2018-04-25
 */
@MyBatisMapper
public interface TaxRatioMapper extends BaseMapper<TaxRatio> {

    /**
     * 获得税率定义的最大值流水号
     * @return MaxAccTypeCode
     */
    public String getMaxTaxratioId();
}