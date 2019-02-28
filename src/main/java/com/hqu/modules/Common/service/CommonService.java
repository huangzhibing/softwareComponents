/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.Common.service;

import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.hqu.modules.Common.mapper.CommonMapper;

import java.util.Map;

/**
 * 关系企业维护Service
 * @author M1ngz
 * @version 2018-04-05
 */
@Service
@Transactional(readOnly = true)
public class CommonService {
    @Autowired
    private CommonMapper mapper;

	public Boolean getCodeNum(String tableName,String fieldName,String fieldValue) {
        Map<String,Object> condition = Maps.newHashMap();
        condition.put("tableName",tableName);
        condition.put("fieldName",fieldName);
        condition.put("fieldValue","'"+fieldValue+"'");
//        condition.put("fieldValue",fieldValue);
	    return mapper.checkCode(condition)>0;
	}
	
}