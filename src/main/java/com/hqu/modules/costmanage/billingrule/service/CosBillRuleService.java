/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.costmanage.billingrule.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.hqu.modules.costmanage.billingrule.entity.CosBillRule;
import com.hqu.modules.costmanage.billingrule.mapper.CosBillRuleMapper;

/**
 * 制单规则Service
 * @author ccr
 * @version 2018-09-01
 */
@Service
@Transactional(readOnly = true)
public class CosBillRuleService extends CrudService<CosBillRuleMapper, CosBillRule> {
	@Autowired
	CosBillRuleMapper cosBillRuleMapper;

	public CosBillRule get(String id) {
		return super.get(id);
	}
	
	public List<CosBillRule> findList(CosBillRule cosBillRule) {
		return super.findList(cosBillRule);
	}
	
	public Page<CosBillRule> findPage(Page<CosBillRule> page, CosBillRule cosBillRule) {
		return super.findPage(page, cosBillRule);
	}
	
	@Transactional(readOnly = false)
	public void save(CosBillRule cosBillRule) {
		super.save(cosBillRule);
	}
	
	@Transactional(readOnly = false)
	public void delete(CosBillRule cosBillRule) {
		super.delete(cosBillRule);
	}
	public String getMaxRuleId(){
		return cosBillRuleMapper.getMaxRuleId();
	}
	
}