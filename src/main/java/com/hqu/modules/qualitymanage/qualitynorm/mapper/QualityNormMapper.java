/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.qualitymanage.qualitynorm.mapper;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.hqu.modules.qualitymanage.qualitynorm.entity.QualityNorm;

import java.util.List;

/**
 * 检验执行标准MAPPER接口
 * @author syc
 * @version 2018-04-26
 */
@MyBatisMapper
public interface QualityNormMapper extends BaseMapper<QualityNorm> {
	List<QualityNorm> getAll();
}