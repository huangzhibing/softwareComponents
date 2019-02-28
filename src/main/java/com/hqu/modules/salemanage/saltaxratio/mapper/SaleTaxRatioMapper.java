/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.salemanage.saltaxratio.mapper;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.hqu.modules.salemanage.saltaxratio.entity.SaleTaxRatio;

/**
 * 销售税率定义MAPPER接口
 * @author liujiachen
 * @version 2018-05-09
 */
@MyBatisMapper
public interface SaleTaxRatioMapper extends BaseMapper<SaleTaxRatio> {
    public String getMaxSalTaxRatioID();
}