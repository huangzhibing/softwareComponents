/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.costmanage.cositem.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.hqu.modules.costmanage.cositem.entity.CosItem;
import com.hqu.modules.costmanage.cositem.mapper.CosItemMapper;

/**
 * 科目定义Service
 * @author zz
 * @version 2018-09-05
 */
@Service
@Transactional(readOnly = true)
public class CosItemService extends CrudService<CosItemMapper, CosItem> {

	public CosItem get(String id) {
		return super.get(id);
	}
	
	public List<CosItem> findList(CosItem cosItem) {
		return super.findList(cosItem);
	}
	
	public Page<CosItem> findPage(Page<CosItem> page, CosItem cosItem) {
		return super.findPage(page, cosItem);
	}
	
	@Transactional(readOnly = false)
	public void save(CosItem cosItem) {
		super.save(cosItem);
	}
	
	@Transactional(readOnly = false)
	public void delete(CosItem cosItem) {
		super.delete(cosItem);
	}
	
}