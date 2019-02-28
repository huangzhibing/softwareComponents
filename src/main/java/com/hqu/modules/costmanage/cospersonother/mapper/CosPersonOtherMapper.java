/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.costmanage.cospersonother.mapper;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.hqu.modules.costmanage.cospersonother.entity.CosPersonOther;

/**
 * 其它工资单据稽核MAPPER接口
 * @author hzm
 * @version 2018-09-01
 */
@MyBatisMapper
public interface CosPersonOtherMapper extends BaseMapper<CosPersonOther> {
	Double getSum(CosPersonOther cosPersonOther);
	int updataReal(String itemSum,String cosBillNum);
}