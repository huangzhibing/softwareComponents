/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.qualitymanage.samplenorm.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.hqu.modules.qualitymanage.samplenorm.entity.SampleNorm;
import com.hqu.modules.qualitymanage.samplenorm.mapper.SampleNormMapper;

/**
 * 抽样标准维护Service
 * @author ckw
 * @version 2018-09-05
 */
@Service
@Transactional(readOnly = true)
public class SampleNormService extends CrudService<SampleNormMapper, SampleNorm> {

	public SampleNorm get(String id) {
		return super.get(id);
	}
	
	public List<SampleNorm> findList(SampleNorm sampleNorm) {
		return super.findList(sampleNorm);
	}
	
	public Page<SampleNorm> findPage(Page<SampleNorm> page, SampleNorm sampleNorm) {
		return super.findPage(page, sampleNorm);
	}
	
	@Transactional(readOnly = false)
	public void save(SampleNorm sampleNorm) {
		super.save(sampleNorm);
	}
	
	@Transactional(readOnly = false)
	public void delete(SampleNorm sampleNorm) {
		super.delete(sampleNorm);
	}
	
}