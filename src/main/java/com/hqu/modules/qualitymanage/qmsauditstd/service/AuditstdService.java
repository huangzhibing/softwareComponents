/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.qualitymanage.qmsauditstd.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.hqu.modules.qualitymanage.qmsauditstd.entity.Auditstd;
import com.hqu.modules.qualitymanage.qmsauditstd.mapper.AuditstdMapper;

/**
 * 稽核标准Service
 * @author syc
 * @version 2018-05-25
 */
@Service
@Transactional(readOnly = true)
public class AuditstdService extends CrudService<AuditstdMapper, Auditstd> {

	public Auditstd get(String id) {
		return super.get(id);
	}
	
	public List<Auditstd> findList(Auditstd auditstd) {
		return super.findList(auditstd);
	}
	
	public Page<Auditstd> findPage(Page<Auditstd> page, Auditstd auditstd) {
		return super.findPage(page, auditstd);
	}
	
	@Transactional(readOnly = false)
	public void save(Auditstd auditstd) {
		super.save(auditstd);
	}
	
	@Transactional(readOnly = false)
	public void delete(Auditstd auditstd) {
		super.delete(auditstd);
	}
	
}