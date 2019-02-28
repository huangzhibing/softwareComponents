/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.purchasemanage.pursupbackdetail.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.hqu.modules.purchasemanage.pursupbackdetail.entity.PurSupBackDetail;
import com.hqu.modules.purchasemanage.pursupbackdetail.mapper.PurSupBackDetailMapper;

/**
 * 供应商退货明细Service
 * @author zz
 * @version 2018-08-19
 */
@Service
@Transactional(readOnly = true)
public class PurSupBackDetailService extends CrudService<PurSupBackDetailMapper, PurSupBackDetail> {

	public PurSupBackDetail get(String id) {
		return super.get(id);
	}
	
	public List<PurSupBackDetail> findList(PurSupBackDetail purSupBackDetail) {
		return super.findList(purSupBackDetail);
	}
	
	public Page<PurSupBackDetail> findPage(Page<PurSupBackDetail> page, PurSupBackDetail purSupBackDetail) {
		return super.findPage(page, purSupBackDetail);
	}
	
	@Transactional(readOnly = false)
	public void save(PurSupBackDetail purSupBackDetail) {
		super.save(purSupBackDetail);
	}
	
	@Transactional(readOnly = false)
	public void delete(PurSupBackDetail purSupBackDetail) {
		super.delete(purSupBackDetail);
	}
	
}