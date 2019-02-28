/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.inventorymanage.resumecheckout.service;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.hqu.modules.basedata.machinetype.entity.MachineType;
import com.hqu.modules.basedata.machinetype.mapper.MachineTypeMapper;
import com.hqu.modules.basedata.period.entity.Period;
import com.hqu.modules.inventorymanage.resumecheckout.mapper.ResumeCheckoutMapper;

/**
 * 设备类别定义Service
 * @author 何志铭
 * @version 2018-04-05
 */
@Service
public class ResumeCheckoutService {
	@Resource
	private ResumeCheckoutMapper resumeCheckoutMapper;
	
	public List<String> findLastByDate(Date date) {
		return resumeCheckoutMapper.findLastByDate(date);
	}

	public List<String> findNextByDate(Date date) { return resumeCheckoutMapper.findNextByDate(date); }
	
}