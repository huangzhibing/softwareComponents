/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.costmanage.reportmanage.service;



import com.hqu.modules.costmanage.reportmanage.entity.ReportPersonWage;

import com.hqu.modules.costmanage.reportmanage.reportaccountmapper.ReportAccountMapper;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 人工工资Service
 * @author 黄志兵
 * @version 2018-09-01
 */
@Service
@Transactional(readOnly = true)
public class ReportAccountService extends CrudService<ReportAccountMapper, ReportPersonWage> {

	public ReportPersonWage get(String id) {
		return super.get(id);
	}
	
	public List<ReportPersonWage> findList(ReportPersonWage personWage) {
		return super.findList(personWage);
	}
	
	public Page<ReportPersonWage> findPage(Page<ReportPersonWage> page, ReportPersonWage personWage) {
		return super.findPage(page, personWage);
	}
}