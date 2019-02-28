/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.basedata.personwork.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.hqu.modules.basedata.personwork.entity.PersonWork;
import com.hqu.modules.basedata.personwork.mapper.PersonWorkMapper;

/**
 * 人员工种表Service
 * @author liujiachen
 * @version 2018-06-04
 */
@Service
@Transactional(readOnly = true)
public class PersonWorkService extends CrudService<PersonWorkMapper, PersonWork> {

	public PersonWork get(String id) {
		return super.get(id);
	}
	
	public List<PersonWork> findList(PersonWork personWork) {
		return super.findList(personWork);
	}
	
	public Page<PersonWork> findPage(Page<PersonWork> page, PersonWork personWork) {
		return super.findPage(page, personWork);
	}
	
	@Transactional(readOnly = false)
	public void save(PersonWork personWork) {
		super.save(personWork);
	}
	
	@Transactional(readOnly = false)
	public void delete(PersonWork personWork) {
		super.delete(personWork);
	}
	
}