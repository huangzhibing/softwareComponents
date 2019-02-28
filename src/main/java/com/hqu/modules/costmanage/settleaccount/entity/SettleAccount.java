package com.hqu.modules.costmanage.settleaccount.entity;

import com.jeeplus.core.persistence.DataEntity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import com.hqu.modules.basedata.period.entity.Period;
import com.hqu.modules.basedata.period.mapper.PeriodMapper;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 月末结算Entity
 * @author zz
 * @version 2018-09-05
 */
public class SettleAccount extends DataEntity<SettleAccount> {
	
	private static final long serialVersionUID = 1L;
	private Date currentDate;		// 当前日期
	private Period periodId;		// 当前核算期
	
	@Autowired
	private PeriodMapper periodMapper;
	public Date getCurrentDate() {
		Calendar calendar = Calendar.getInstance();
		Date time = calendar.getTime();
		currentDate = time;
		return currentDate;
	}
	
	public void setCurrentDate(Date currentDate) {
		this.currentDate = currentDate;
	}
	
	public Period getPeriodId() {
//		Calendar calendar = Calendar.getInstance();
//		Date time = calendar.getTime();
//		Period per = new Period();
//		per.setCurrentDate(time);
//		per = periodMapper.getCurrentPeriod(per);
//		periodId = per;
		return periodId;
	}
	
	public void setPeriodId(Period periodId) {
		this.periodId = periodId;
	}

	
	
}
