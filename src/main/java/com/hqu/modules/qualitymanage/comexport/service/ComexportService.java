/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.qualitymanage.comexport.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.hqu.modules.qualitymanage.comexport.entity.Comexport;
import com.hqu.modules.qualitymanage.comexport.mapper.ComexportMapper;

/**
 * 功能老化激活安规导出Service
 * @author syc
 * @version 2018-11-09
 */
@Service
@Transactional(readOnly = true)
public class ComexportService extends CrudService<ComexportMapper, Comexport> {

	public Comexport get(String id) {
		return super.get(id);
	}
	
	public List<Comexport> findList(Comexport comexport) {
		return super.findList(comexport);
	}
	
	public Page<Comexport> findPage(Page<Comexport> page, Comexport comexport) {
		return super.findPage(page, comexport);
	}
	
	@Transactional(readOnly = false)
	public void save(Comexport comexport) {
		super.save(comexport);
	}
	
	@Transactional(readOnly = false)
	public void delete(Comexport comexport) {
		super.delete(comexport);
	}
	
}