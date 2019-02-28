/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.basedata.productcopy.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.hqu.modules.basedata.productcopy.entity.MDMCopy;
import com.hqu.modules.basedata.productcopy.mapper.MDMCopyMapper;

/**
 * 产品结构复制Service
 * @author xn
 * @version 2018-04-13
 */
@Service
@Transactional(readOnly = true)
public class MDMCopyService extends CrudService<MDMCopyMapper, MDMCopy> {

	public MDMCopy get(String id) {
		return super.get(id);
	}
	
	public List<MDMCopy> findList(MDMCopy mDMCopy) {
		return super.findList(mDMCopy);
	}
	
	public Page<MDMCopy> findPage(Page<MDMCopy> page, MDMCopy mDMCopy) {
		return super.findPage(page, mDMCopy);
	}
	
	@Transactional(readOnly = false)
	public void save(MDMCopy mDMCopy) {
		super.save(mDMCopy);
	}
	
	@Transactional(readOnly = false)
	public void delete(MDMCopy mDMCopy) {
		super.delete(mDMCopy);
	}
	
}