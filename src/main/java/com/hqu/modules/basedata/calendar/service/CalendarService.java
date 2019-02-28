/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.basedata.calendar.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.google.common.collect.Maps;
import com.hqu.modules.basedata.calendar.entity.Calendar;
import com.hqu.modules.basedata.calendar.mapper.CalendarMapper;
import com.hqu.modules.basedata.machinetype.entity.MachineType;

/**
 * 企业日历定义Service
 * @author 何志铭
 * @version 2018-04-04
 */
@Service
@Transactional(readOnly = true)
public class CalendarService extends CrudService<CalendarMapper, Calendar> {
	/*
	 * 检查唯一性
	 */
	public Boolean getCodeNum(String tableName,String fieldName,String sfieldValue,String efieldValue) {
        Map<String,Object> condition = Maps.newHashMap();
        condition.put("tableName",tableName);
        condition.put("fieldName",fieldName);
        condition.put("sfieldValue","'"+sfieldValue+"'");
        condition.put("efieldValue","'"+efieldValue+"'");
//        condition.put("fieldValue",fieldValue);
	    return mapper.checkCode(condition)>0;
	}
	/*
	 * 生成企业日历
	 */
	@Transactional(readOnly = false)
	public void saveAll(Calendar calendar,HttpServletRequest request) throws ParseException {
		String workTime=request.getParameter("workTime");
		String restTime=request.getParameter("restTime");
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		Date startDate=sdf.parse(request.getParameter("curDate"));
		Date endDate=sdf.parse(request.getParameter("endDate"));
		java.util.Calendar cal = java.util.Calendar.getInstance();
		cal.setTime(startDate);
		String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
		while(startDate.getTime()<=endDate.getTime()) {
			//保存日期
			calendar.setCurDate(startDate);
			//保存日期对应星期
			int w = cal.get(java.util.Calendar.DAY_OF_WEEK) - 1;
			calendar.setWeekDay(weekDays[w]);
			//保存日期对应工作日标识与工作时间
			if(cal.get(java.util.Calendar.DAY_OF_WEEK)==java.util.Calendar.SATURDAY||cal.get(java.util.Calendar.DAY_OF_WEEK)==java.util.Calendar.SUNDAY){
				calendar.setWorkFlag("N");
				calendar.setWorkTime(restTime);
			}else {
				calendar.setWorkFlag("Y");
				calendar.setWorkTime(workTime);
			}
			super.save(calendar);//保存
			cal.add(java.util.Calendar.DATE,1);
			startDate =cal.getTime();
			calendar=new Calendar();
		}
	}
	
	public Calendar get(String id) {
		return super.get(id);
	}
	
	public List<Calendar> findList(Calendar calendar) {
		return super.findList(calendar);
	}
	
	public Page<Calendar> findPage(Page<Calendar> page, Calendar calendar) {
		return super.findPage(page, calendar);
	}
	
	@Transactional(readOnly = false)
	public void save(Calendar calendar) {
		super.save(calendar);
	}
	
	@Transactional(readOnly = false)
	public void delete(Calendar calendar) {
		super.delete(calendar);
	}
	
	@Transactional(readOnly = false)
	public void saveExc(Calendar calendar) throws Exception {
		/**
		 * 检测非空性
		 */
		if(calendar.getCurDate()==null)
			throw new Exception("日期为空");
		if(calendar.getWeekDay()==null || calendar.getWeekDay()=="")
			throw new Exception("星期为空");
		if(calendar.getWorkFlag()==null || calendar.getWorkFlag()=="")
			throw new Exception("工作日标识为空");
		if(calendar.getWorkTime()==null || calendar.getWorkTime()=="")
			throw new Exception("标准工时为空");
		/**
		 * 检测唯一性
		 */
		Calendar nothing = new Calendar();		
		List<Calendar> checkOnly=this.findList(nothing);
		int count;		
		for(count=0;count<checkOnly.size();count++){
			//System.out.println(checkOnly.get(count).getUnitTypeCode()+".+-"+count+unitType.getUnitTypeCode());
			if(calendar.getCurDate().equals(checkOnly.get(count).getCurDate()))
			{
				throw new Exception("该日期已存在");
			}
		}
			super.save(calendar);
		}
	
}