/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.costmanage.cositem.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.service.TreeService;
import com.jeeplus.common.utils.StringUtils;
import com.hqu.modules.costmanage.cositem.entity.CosItemLeft;
import com.hqu.modules.costmanage.cositem.mapper.CosItemLeftMapper;

/**
 * 科目定义Service
 * @author zz
 * @version 2018-09-05
 */
@Service
@Transactional(readOnly = true)
public class CosItemLeftService extends TreeService<CosItemLeftMapper, CosItemLeft> {
	@Autowired
	CosItemLeftMapper cosItemLeftMapper;
	public CosItemLeft get(String id) {
		return super.get(id);
	}
	
	public List<CosItemLeft> findList(CosItemLeft cosItemLeft) {
		if (StringUtils.isNotBlank(cosItemLeft.getParentIds())){
			cosItemLeft.setParentIds(","+cosItemLeft.getParentIds()+",");
		}
		return super.findList(cosItemLeft);
	}
	
	@Transactional(readOnly = false)
	public void save(CosItemLeft cosItemLeft) {
		super.save(cosItemLeft);
	}
	
	@Transactional(readOnly = false)
	public void delete(CosItemLeft cosItemLeft) {
		super.delete(cosItemLeft);
	}
	
	public String getMaximumCode(CosItemLeft cosItemLeft){
		String max = cosItemLeftMapper.getMaximumCode(cosItemLeft);
		int maxint = Integer.parseInt(max) + 1;
		max = String.valueOf(maxint);
		max = "0" + max;

		return max;
	}
	
}