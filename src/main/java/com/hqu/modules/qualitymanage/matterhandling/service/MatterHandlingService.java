/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.qualitymanage.matterhandling.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.hqu.modules.qualitymanage.matterhandling.entity.MatterHandling;
import com.hqu.modules.qualitymanage.matterhandling.mapper.MatterHandlingMapper;

/**
 * MatterHandlingService
 * @author 方翠虹
 * @version 2018-04-25
 */
@Service
@Transactional(readOnly = true)
public class MatterHandlingService extends CrudService<MatterHandlingMapper, MatterHandling> {

	public MatterHandling get(String id) {
		return super.get(id);
	}
	
	public List<MatterHandling> findList(MatterHandling matterHandling) {
		return super.findList(matterHandling);
	}
	
	public Page<MatterHandling> findPage(Page<MatterHandling> page, MatterHandling matterHandling) {
		String orderBy=page.getOrderBy();
		if(orderBy!=null&&"".equals(orderBy)){
			page.setOrderBy("mhandlingcode");
		}		
		return super.findPage(page, matterHandling);
	}
	
	@Transactional(readOnly = false)
	public void save(MatterHandling matterHandling) {
		super.save(matterHandling);
	}
	
	@Transactional(readOnly = false)
	public void delete(MatterHandling matterHandling) {
		super.delete(matterHandling);
	}
	
}