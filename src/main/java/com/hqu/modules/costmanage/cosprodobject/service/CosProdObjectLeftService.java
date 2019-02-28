/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.costmanage.cosprodobject.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.service.TreeService;
import com.jeeplus.common.utils.StringUtils;
import com.hqu.modules.costmanage.cosprodobject.entity.CosProdObjectLeft;
import com.hqu.modules.costmanage.cosprodobject.mapper.CosProdObjectLeftMapper;

/**
 * 核算对象定义Service
 * @author zz
 * @version 2018-09-07
 */
@Service
@Transactional(readOnly = true)
public class CosProdObjectLeftService extends TreeService<CosProdObjectLeftMapper, CosProdObjectLeft> {

	public CosProdObjectLeft get(String id) {
		return super.get(id);
	}
	
	public List<CosProdObjectLeft> findList(CosProdObjectLeft cosProdObjectLeft) {
		if (StringUtils.isNotBlank(cosProdObjectLeft.getParentIds())){
			cosProdObjectLeft.setParentIds(","+cosProdObjectLeft.getParentIds()+",");
		}
		return super.findList(cosProdObjectLeft);
	}
	
	@Transactional(readOnly = false)
	public void save(CosProdObjectLeft cosProdObjectLeft) {
		super.save(cosProdObjectLeft);
	}
	
	@Transactional(readOnly = false)
	public void delete(CosProdObjectLeft cosProdObjectLeft) {
		super.delete(cosProdObjectLeft);
	}
	
}