/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.qualitymanage.purreportnewexport.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.hqu.modules.qualitymanage.purreportnewexport.entity.Purreportnewexport;
import com.hqu.modules.qualitymanage.purreportnewexport.mapper.PurreportnewexportMapper;

/**
 * 采购检验IQC导出Service
 * @author syc
 * @version 2018-11-08
 */
@Service
@Transactional(readOnly = true)
public class PurreportnewexportService extends CrudService<PurreportnewexportMapper, Purreportnewexport> {

	public Purreportnewexport get(String id) {
		return super.get(id);
	}
	
	public List<Purreportnewexport> findList(Purreportnewexport purreportnewexport) {
		return super.findList(purreportnewexport);
	}
	
	public Page<Purreportnewexport> findPage(Page<Purreportnewexport> page, Purreportnewexport purreportnewexport) {
		return super.findPage(page, purreportnewexport);
	}
	
	@Transactional(readOnly = false)
	public void save(Purreportnewexport purreportnewexport) {
		super.save(purreportnewexport);
	}
	
	@Transactional(readOnly = false)
	public void delete(Purreportnewexport purreportnewexport) {
		super.delete(purreportnewexport);
	}
	
}