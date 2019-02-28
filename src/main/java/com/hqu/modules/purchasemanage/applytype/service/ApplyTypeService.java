/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.purchasemanage.applytype.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.hqu.modules.purchasemanage.applytype.entity.ApplyType;
import com.hqu.modules.purchasemanage.applytype.mapper.ApplyTypeMapper;

/**
 * ApplyTypeService
 * @author 方翠虹
 * @version 2018-04-25
 */

@Service
@Transactional(readOnly = true)

public class ApplyTypeService extends CrudService<ApplyTypeMapper, ApplyType> {
	@Autowired
	ApplyTypeMapper applyTypeMapper;

	public ApplyType get(String id) {
		return super.get(id);
	}
	
	public List<ApplyType> findList(ApplyType applyType) {
		return super.findList(applyType);
	}
	
	public Page<ApplyType> findPage(Page<ApplyType> page, ApplyType applyType) {
		String orderBy=page.getOrderBy();
		if(orderBy!=null&&"".equals(orderBy)){
			page.setOrderBy("applytypeid");
		}		
		return super.findPage(page, applyType);
	}
	
	@Transactional(readOnly = false)
	public void save(ApplyType applyType) {
		super.save(applyType);
	}
	
	@Transactional(readOnly = false)
	public void delete(ApplyType applyType) {
		super.delete(applyType);
	}
	public String getMaxApplytypeid(){
		return applyTypeMapper.getMaxApplytypeid();
	}

}