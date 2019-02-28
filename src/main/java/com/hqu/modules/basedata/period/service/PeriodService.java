/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.basedata.period.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.hqu.modules.basedata.period.entity.Period;
import com.hqu.modules.basedata.period.mapper.PeriodMapper;

/**
 * 企业核算期定义Service
 * @author Neil
 * @version 2018-06-02
 */
@Service
@Transactional(readOnly = true)
public class PeriodService extends CrudService<PeriodMapper, Period> {

	public Period get(String id) {
		return super.get(id);
	}
	
	public List<Period> findList(Period period) {
		return super.findList(period);
	}
	
	public Page<Period> findPage(Page<Period> page, Period period) {
		return super.findPage(page, period);
	}
	
	@Transactional(readOnly = false)
	public void save(Period period) {
		super.save(period);
	}
	
	@Transactional(readOnly = false)
	public void delete(Period period) {
		super.delete(period);
	}
	
}