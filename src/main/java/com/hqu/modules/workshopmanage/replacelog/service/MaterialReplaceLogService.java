/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.workshopmanage.replacelog.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.hqu.modules.workshopmanage.replacelog.entity.MaterialReplaceLog;
import com.hqu.modules.workshopmanage.replacelog.mapper.MaterialReplaceLogMapper;

/**
 * 换件日志Service
 * @author xhc
 * @version 2019-02-23
 */
@Service
@Transactional(readOnly = true)
public class MaterialReplaceLogService extends CrudService<MaterialReplaceLogMapper, MaterialReplaceLog> {

	public MaterialReplaceLog get(String id) {
		return super.get(id);
	}
	
	public List<MaterialReplaceLog> findList(MaterialReplaceLog materialReplaceLog) {
		return super.findList(materialReplaceLog);
	}
	
	public Page<MaterialReplaceLog> findPage(Page<MaterialReplaceLog> page, MaterialReplaceLog materialReplaceLog) {
		return super.findPage(page, materialReplaceLog);
	}
	
	@Transactional(readOnly = false)
	public void save(MaterialReplaceLog materialReplaceLog) {
		super.save(materialReplaceLog);
	}
	
	@Transactional(readOnly = false)
	public void delete(MaterialReplaceLog materialReplaceLog) {
		super.delete(materialReplaceLog);
	}
	
}