/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.qualitymanage.purinvcheckmain.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.hqu.modules.qualitymanage.purinvcheckmain.entity.InvCheckDetailCode;
import com.hqu.modules.qualitymanage.purinvcheckmain.mapper.InvCheckDetailCodeMapper;

/**
 * 二维码Service
 * @author 张铮
 * @version 2018-06-22
 */
@Service
@Transactional(readOnly = true)
public class InvCheckDetailCodeService extends CrudService<InvCheckDetailCodeMapper, InvCheckDetailCode> {

	public InvCheckDetailCode get(String id) {
		return super.get(id);
	}
	
	public List<InvCheckDetailCode> findList(InvCheckDetailCode invCheckDetailCode) {
		return super.findList(invCheckDetailCode);
	}
	
	public Page<InvCheckDetailCode> findPage(Page<InvCheckDetailCode> page, InvCheckDetailCode invCheckDetailCode) {
		return super.findPage(page, invCheckDetailCode);
	}
	
	@Transactional(readOnly = false)
	public void save(InvCheckDetailCode invCheckDetailCode) {
		super.save(invCheckDetailCode);
	}
	
	@Transactional(readOnly = false)
	public void delete(InvCheckDetailCode invCheckDetailCode) {
		super.delete(invCheckDetailCode);
	}
	
}