/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.qualitymanage.qualitynorm.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.hqu.modules.qualitymanage.qualitynorm.entity.QualityNorm;
import com.hqu.modules.qualitymanage.qualitynorm.mapper.QualityNormMapper;

/**
 * 检验执行标准Service
 * @author syc
 * @version 2018-04-26
 */
@Service
@Transactional(readOnly = true)
public class QualityNormService extends CrudService<QualityNormMapper, QualityNorm> {

	public QualityNorm get(String id) {
		return super.get(id);
	}
	
	public List<QualityNorm> findList(QualityNorm qualityNorm) {
		return super.findList(qualityNorm);
	}
	
	public Page<QualityNorm> findPage(Page<QualityNorm> page, QualityNorm qualityNorm) {
		String orderBy=page.getOrderBy();
		if(orderBy!=null&&"".equals(orderBy)){
			page.setOrderBy("qnormcode");
		}		
		return super.findPage(page, qualityNorm);
	}
	
	@Transactional(readOnly = false)
	public void save(QualityNorm qualityNorm) {
		super.save(qualityNorm);
	}
	
	@Transactional(readOnly = false)
	public void delete(QualityNorm qualityNorm) {
		super.delete(qualityNorm);
	}

	public List<QualityNorm> findAll() {
		// TODO Auto-generated method stub
		return null;
	}
	public List<QualityNorm> getAll() { return mapper.getAll();}
}