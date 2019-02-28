/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.inventorymanage.ppcsearch.service;

import java.util.List;

import com.hqu.modules.inventorymanage.ppcsearch.entity.MSerialNoSearch;
import com.hqu.modules.inventorymanage.ppcsearch.mapper.MSerialNoSearchMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.hqu.modules.workshopmanage.ppc.entity.MSerialNo;
import com.hqu.modules.workshopmanage.ppc.mapper.MSerialNoMapper;

/**
 * 编码关联表Service
 * @author yxb
 * @version 2018-10-31
 */
@Service
@Transactional(readOnly = true)
public class MSerialNoSearchService extends CrudService<MSerialNoSearchMapper, MSerialNoSearch> {

	public MSerialNoSearch get(String id) {
		return super.get(id);
	}
	
	public List<MSerialNoSearch> findList(MSerialNoSearch mSerialNo) {
		return super.findList(mSerialNo);
	}
	
	public Page<MSerialNoSearch> findPage(Page<MSerialNoSearch> page, MSerialNoSearch mSerialNo) {
		return super.findPage(page, mSerialNo);
	}
	
	@Transactional(readOnly = false)
	public void save(MSerialNoSearch mSerialNo) {
		super.save(mSerialNo);
	}
	
	@Transactional(readOnly = false)
	public void delete(MSerialNoSearch mSerialNo) {
		super.delete(mSerialNo);
	}
	
}