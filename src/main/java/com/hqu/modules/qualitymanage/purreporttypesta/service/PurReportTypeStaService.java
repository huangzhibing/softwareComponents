/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.qualitymanage.purreporttypesta.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.hqu.modules.qualitymanage.purreportnew.mapper.PurReportNewMapper;
import com.jeeplus.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.hqu.modules.qualitymanage.purreporttypesta.entity.PurReportTypeSta;
import com.hqu.modules.qualitymanage.purreporttypesta.mapper.PurReportTypeStaMapper;

/**
 * 来料按物料类别统计分析Service
 * @author yxb
 * @version 2018-08-25
 */
@Service
@Transactional(readOnly = true)
public class PurReportTypeStaService extends CrudService<PurReportTypeStaMapper, PurReportTypeSta> {

	@Autowired
	PurReportTypeStaMapper purReportTypeStaMapper;


	public PurReportTypeSta get(String id) {
		return super.get(id);
	}
	
	public List<PurReportTypeSta> findList(PurReportTypeSta purReportTypeSta) {
		return super.findList(purReportTypeSta);
	}


	@Transactional(readOnly = false)
	public Page<PurReportTypeSta> findPage(Page<PurReportTypeSta> page, PurReportTypeSta purReportTypeSta) {
		//删除历史的统计数据
		if(purReportTypeSta.getDateQuery()==null){
			purReportTypeSta.setDateQuery(new Date());
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(purReportTypeSta.getDateQuery());
		purReportTypeSta.setYear(String.valueOf(cal.get(Calendar.YEAR)));
		purReportTypeSta.setMonth(String.valueOf(cal.get(Calendar.MONTH)+1));
		delete(purReportTypeSta);
		//重新统对应月份计数据并写入统计表
		for(PurReportTypeSta reportTypeSta:purReportTypeStaMapper.sumReportNewByDate(purReportTypeSta)){
			reportTypeSta.setMonth(purReportTypeSta.getMonth());
			reportTypeSta.setYear(purReportTypeSta.getYear());
			if(StringUtils.isBlank(reportTypeSta.getType())){
				reportTypeSta.setType("其他");
			}
			save(reportTypeSta);
		}
		return super.findPage(page, purReportTypeSta);
	}
	
	@Transactional(readOnly = false)
	public void save(PurReportTypeSta purReportTypeSta) {
		super.save(purReportTypeSta);
	}
	
	@Transactional(readOnly = false)
	public void delete(PurReportTypeSta purReportTypeSta) {
		super.delete(purReportTypeSta);
	}
	
}