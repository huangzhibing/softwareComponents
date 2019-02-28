/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.qualitymanage.verifyrule.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.hqu.modules.qualitymanage.verifyrule.entity.VerifyRule;
import com.hqu.modules.qualitymanage.verifyrule.mapper.VerifyRuleMapper;

/**
 * 验证规则表Service
 * @author 石豪迈
 * @version 2018-05-16
 */
@Service
@Transactional(readOnly = true)
public class VerifyRuleService extends CrudService<VerifyRuleMapper, VerifyRule> {

	public VerifyRule get(String id) {
		return super.get(id);
	}
	
	public List<VerifyRule> findList(VerifyRule verifyRule) {
		return super.findList(verifyRule);
	}
	
	public Page<VerifyRule> findPage(Page<VerifyRule> page, VerifyRule verifyRule) {
		String orderBy=page.getOrderBy();
		if(orderBy!=null&&"".equals(orderBy)){
			page.setOrderBy("ruleId");
		}		
		return super.findPage(page, verifyRule);
	}
	
	@Transactional(readOnly = false)
	public void save(VerifyRule verifyRule) {
		super.save(verifyRule);
	}
	
	@Transactional(readOnly = false)
	public void delete(VerifyRule verifyRule) {
		super.delete(verifyRule);
	}
	
}