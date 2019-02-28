/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.workshopmanage.ppc.service;

import java.util.List;

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
public class MSerialNoService extends CrudService<MSerialNoMapper, MSerialNo> {

	public MSerialNo get(String id) {
		return super.get(id);
	}
	
	public List<MSerialNo> findList(MSerialNo mSerialNo) {
		return super.findList(mSerialNo);
	}
	
	public Page<MSerialNo> findPage(Page<MSerialNo> page, MSerialNo mSerialNo) {
		return super.findPage(page, mSerialNo);
	}
	
	@Transactional(readOnly = false)
	public void save(MSerialNo mSerialNo) {
		super.save(mSerialNo);
	}
	
	@Transactional(readOnly = false)
	public void delete(MSerialNo mSerialNo) {
		super.delete(mSerialNo);
	}
	
}