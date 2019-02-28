/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.purchasemanage.monthreport.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.hqu.modules.purchasemanage.monthreport.entity.MonthReport;
import com.hqu.modules.purchasemanage.monthreport.mapper.MonthReportMapper;

/**
 * 进料不合格统计表，关联IQC来料检验单Service
 * @author ckw
 * @version 2018-08-25
 */
@Service
@Transactional(readOnly = true)
public class MonthReportService extends CrudService<MonthReportMapper, MonthReport> {

	public MonthReport get(String id) {
		return super.get(id);
	}
	
	public List<MonthReport> findList(MonthReport monthReport) {
		return super.findList(monthReport);
	}
	
	public Page<MonthReport> findPage(Page<MonthReport> page, MonthReport monthReport) {
		return super.findPage(page, monthReport);
	}
	
	@Transactional(readOnly = false)
	public void save(MonthReport monthReport) {
		super.save(monthReport);
	}
	
	@Transactional(readOnly = false)
	public void delete(MonthReport monthReport) {
		super.delete(monthReport);
	}
	
}