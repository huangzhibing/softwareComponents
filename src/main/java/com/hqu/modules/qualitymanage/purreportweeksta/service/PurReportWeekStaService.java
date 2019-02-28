/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.qualitymanage.purreportweeksta.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.poi.util.SystemOutLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.hqu.modules.qualitymanage.purreportweeksta.entity.PurReportWeekSta;
import com.hqu.modules.qualitymanage.purreportweeksta.mapper.PurReportWeekStaMapper;

/**
 * 进料检验统计Service
 * @author yxb
 * @version 2018-08-25
 */
@Service
@Transactional(readOnly = true)
public class PurReportWeekStaService extends CrudService<PurReportWeekStaMapper, PurReportWeekSta> {

	@Autowired
	private PurReportWeekStaMapper purReportWeekStaMapper;

	public PurReportWeekSta get(String id) {
		return super.get(id);
	}
	
	public List<PurReportWeekSta> findList(PurReportWeekSta purReportWeekSta) {
		return super.findList(purReportWeekSta);
	}
	@Transactional(readOnly = false)
	public Page<PurReportWeekSta> findPage(Page<PurReportWeekSta> page, PurReportWeekSta purReportWeekSta) {
		Calendar nowDate=Calendar.getInstance();
		nowDate.set(nowDate.get(Calendar.YEAR),nowDate.get(Calendar.MONTH),1);//把当前时间置成当前月的1号
		if(purReportWeekSta.getDateQuery()==null){
			purReportWeekSta.setDateQuery(nowDate.getTime());
		}
		Calendar calendar=Calendar.getInstance();
		calendar.setTime(purReportWeekSta.getDateQuery());

		purReportWeekSta.setYear(String.valueOf(calendar.get(Calendar.YEAR)));
		purReportWeekSta.setMonth(String.valueOf(calendar.get(Calendar.MONTH)+1));

		//删除该月历史统计
		purReportWeekStaMapper.deleteByYearMonth(purReportWeekSta);
		//按周统计并记录在qms_purreport_result_sta统计表中
		SumWeekStaSave(purReportWeekSta, calendar);


		return super.findPage(page, purReportWeekSta);
	}

	/**
	 * 按周统计并记录在qms_purreport_result_sta统计表中
	 * @param purReportWeekSta 用于条件查询
	 * @param calendar
	 */
	private void SumWeekStaSave(PurReportWeekSta purReportWeekSta, Calendar calendar) {
		PurReportWeekSta reportWeekSta;//用于保存到统计表的对象
		//处理第一周
		if(calendar.get(Calendar.DAY_OF_WEEK)==1){//周日
			purReportWeekSta.setBegindate(calendar.getTime());
			purReportWeekSta.setEnddate(calendar.getTime());

		}else{
			purReportWeekSta.setBegindate(calendar.getTime());
			calendar.add(calendar.DATE,8-calendar.get(Calendar.DAY_OF_WEEK));
			purReportWeekSta.setEnddate(calendar.getTime());
		}
		System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx"+purReportWeekSta.getBegindate()+"~"+purReportWeekSta.getEnddate());
		reportWeekSta=purReportWeekStaMapper.sumReportNewByDate(purReportWeekSta);
		if(reportWeekSta==null){
			reportWeekSta=new PurReportWeekSta("0","0","0","0","0","0","0");
		}
		reportWeekSta.setWeek("第1周");
		reportWeekSta.setYear(String.valueOf(calendar.get(Calendar.YEAR)));
		reportWeekSta.setMonth(String.valueOf(calendar.get(Calendar.MONTH)+1));
		save(reportWeekSta);
		//处理第二周
		calendar.add(calendar.DATE,1);
		purReportWeekSta.setBegindate(calendar.getTime());
		calendar.add(calendar.DATE,6);
		purReportWeekSta.setEnddate(calendar.getTime());
		System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx"+purReportWeekSta.getBegindate()+"~"+purReportWeekSta.getEnddate());
		reportWeekSta=purReportWeekStaMapper.sumReportNewByDate(purReportWeekSta);
		if(reportWeekSta==null){
			reportWeekSta=new PurReportWeekSta("0","0","0","0","0","0","0");
		}
		reportWeekSta.setWeek("第2周");
		reportWeekSta.setYear(String.valueOf(calendar.get(Calendar.YEAR)));
		reportWeekSta.setMonth(String.valueOf(calendar.get(Calendar.MONTH)+1));
		save(reportWeekSta);
		//处理第三周
		calendar.add(calendar.DATE,1);
		purReportWeekSta.setBegindate(calendar.getTime());
		calendar.add(calendar.DATE,6);
		purReportWeekSta.setEnddate(calendar.getTime());
		System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx"+purReportWeekSta.getBegindate()+"~"+purReportWeekSta.getEnddate());
		reportWeekSta=purReportWeekStaMapper.sumReportNewByDate(purReportWeekSta);
		if(reportWeekSta==null){
			reportWeekSta=new PurReportWeekSta("0","0","0","0","0","0","0");
		}
		reportWeekSta.setWeek("第3周");
		reportWeekSta.setYear(String.valueOf(calendar.get(Calendar.YEAR)));
		reportWeekSta.setMonth(String.valueOf(calendar.get(Calendar.MONTH)+1));
		save(reportWeekSta);
		//处理第四周
		calendar.add(calendar.DATE,1);
		purReportWeekSta.setBegindate(calendar.getTime());
		calendar.add(calendar.DATE,6);
		purReportWeekSta.setEnddate(calendar.getTime());
		System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx"+purReportWeekSta.getBegindate()+"~"+purReportWeekSta.getEnddate());
		reportWeekSta=purReportWeekStaMapper.sumReportNewByDate(purReportWeekSta);
		if(reportWeekSta==null){
			reportWeekSta=new PurReportWeekSta("0","0","0","0","0","0","0");
		}
		reportWeekSta.setWeek("第4周");
		reportWeekSta.setYear(String.valueOf(calendar.get(Calendar.YEAR)));
		reportWeekSta.setMonth(String.valueOf(calendar.get(Calendar.MONTH)+1));
		save(reportWeekSta);
		//处理第五周
		calendar.add(calendar.DATE,1);
		if(String.valueOf(calendar.get(Calendar.MONTH)+1).equals(purReportWeekSta.getMonth())) {//该月是否有第五周
			purReportWeekSta.setBegindate(calendar.getTime());
			calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));//月末最后一天
			purReportWeekSta.setEnddate(calendar.getTime());
			reportWeekSta = purReportWeekStaMapper.sumReportNewByDate(purReportWeekSta);
		}else {
			reportWeekSta=null;
		}
		System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx"+purReportWeekSta.getBegindate()+"~"+purReportWeekSta.getEnddate());
		if(reportWeekSta==null){
			reportWeekSta=new PurReportWeekSta("0","0","0","0","0","0","0");
		}
		reportWeekSta.setWeek("第5周");
		reportWeekSta.setYear(String.valueOf(calendar.get(Calendar.YEAR)));
		reportWeekSta.setMonth(String.valueOf(calendar.get(Calendar.MONTH)+1));
		save(reportWeekSta);
	}

	@Transactional(readOnly = false)
	public void save(PurReportWeekSta purReportWeekSta) {
		super.save(purReportWeekSta);
	}
	
	@Transactional(readOnly = false)
	public void delete(PurReportWeekSta purReportWeekSta) {
		super.delete(purReportWeekSta);
	}
	
}