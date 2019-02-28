/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.costmanage.personwage.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.jeeplus.common.utils.StringUtils;
import oracle.sql.DATE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.hqu.modules.costmanage.personwage.entity.PersonWage;
import com.hqu.modules.costmanage.personwage.mapper.PersonWageMapper;

/**
 * 人工工资Service
 * @author 黄志兵
 * @version 2018-09-01
 */
@Service
@Transactional(readOnly = true)
public class PersonWageService extends CrudService<PersonWageMapper, PersonWage> {

	public PersonWage get(String id) {
		return super.get(id);
	}
	
	public List<PersonWage> findList(PersonWage personWage) {
		return super.findList(personWage);
	}
	
	public Page<PersonWage> findPage(Page<PersonWage> page, PersonWage personWage) {
		return super.findPage(page, personWage);
	}
	
	@Transactional(readOnly = false)
	public void save(PersonWage personWage) {
		super.save(personWage);
	}
	
	@Transactional(readOnly = false)
	public void delete(PersonWage personWage) {
		super.delete(personWage);
	}

	//自动生成工资单据号
	public String getBillNum(){
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String billNum = mapper.findMaxBillNum("GZ"+sdf.format(date));
		if(StringUtils.isEmpty(billNum)){
			billNum = "00001";
		}else {
			billNum = String.format("%05d",Integer.parseInt(billNum.substring(10,15))+1);
		}
		return "GZ"+sdf.format(date)+billNum;
	}

}