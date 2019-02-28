/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.basedata.mdmcalendar.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.hqu.modules.basedata.mdmcalendar.entity.MdmCalendar;
import com.hqu.modules.basedata.mdmcalendar.mapper.MdmCalendarMapper;

/**
 * 企业日历定义Service
 * @author 何志铭
 * @version 2018-04-02
 */
@Service
@Transactional(readOnly = true)
public class MdmCalendarService extends CrudService<MdmCalendarMapper, MdmCalendar> {

	public MdmCalendar get(String id) {
		return super.get(id);
	}
	
	public List<MdmCalendar> findList(MdmCalendar mdmCalendar) {
		return super.findList(mdmCalendar);
	}
	
	public Page<MdmCalendar> findPage(Page<MdmCalendar> page, MdmCalendar mdmCalendar) {
		return super.findPage(page, mdmCalendar);
	}
	
	@Transactional(readOnly = false)
	public void save(MdmCalendar mdmCalendar) {
		super.save(mdmCalendar);
	}
	
	@Transactional(readOnly = false)
	public void delete(MdmCalendar mdmCalendar) {
		super.delete(mdmCalendar);
	}
	
}