/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.qualitymanage.verifyqcnorm.service;

import com.hqu.modules.qualitymanage.verifyqcnorm.entity.VerifyQCNorm;
import com.hqu.modules.qualitymanage.verifyqcnorm.mapper.VerifyQCNormMapper;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 检验数据验证Service
 * @author 石豪迈
 * @version 2018-05-18
 */
@Service
@Transactional(readOnly = true)
public class VerifyQCNormService extends CrudService<VerifyQCNormMapper, VerifyQCNorm> {

	public VerifyQCNorm get(String id) {
		return super.get(id);
	}
	
	public List<VerifyQCNorm> findList(VerifyQCNorm verifyQCNorm) {
		return super.findList(verifyQCNorm);
	}
	
	public Page<VerifyQCNorm> findPage(Page<VerifyQCNorm> page, VerifyQCNorm verifyQCNorm) {
		return super.findPage(page, verifyQCNorm);
	}
	
	@Transactional(readOnly = false)
	public void save(VerifyQCNorm verifyQCNorm) {
		super.save(verifyQCNorm);
	}
	
	@Transactional(readOnly = false)
	public void delete(VerifyQCNorm verifyQCNorm) {
		super.delete(verifyQCNorm);
	}
	
}