/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.costmanage.cosbillrecord.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.hqu.modules.costmanage.billingrule.entity.CosBillRule;
import com.hqu.modules.costmanage.billingrule.mapper.CosBillRuleMapper;
import com.hqu.modules.costmanage.cosbillrecord.entity.CosBillRecord;
import com.hqu.modules.costmanage.cosbillrecord.mapper.CosBillRecordMapper;

/**
 * 材料单据稽核Service
 * @author zz
 * @version 2018-08-29
 */
@Service
@Transactional(readOnly = true)
public class CosBillRecordService extends CrudService<CosBillRecordMapper, CosBillRecord> {
	@Autowired
	private CosBillRuleMapper cosBillRuleMapper;
	
	//id与billnum都可以
	public CosBillRecord get(String id) {
		CosBillRecord cosBillRecord = super.get(id);
		return cosBillRecord;
	}
	
	public List<CosBillRecord> findList(CosBillRecord cosBillRecord) {
		return super.findList(cosBillRecord);
	}
	
	public Page<CosBillRecord> findPage(Page<CosBillRecord> page, CosBillRecord cosBillRecord) {
		return super.findPage(page, cosBillRecord);
	}
	
	@Transactional(readOnly = false)
	public void save(CosBillRecord cosBillRecord) {
		super.save(cosBillRecord);
	}
	
	@Transactional(readOnly = false)
	public void delete(CosBillRecord cosBillRecord) {
		super.delete(cosBillRecord);
	}
	
	
	
}