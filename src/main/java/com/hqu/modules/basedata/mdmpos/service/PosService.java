/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.basedata.mdmpos.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.hqu.modules.basedata.mdmpos.entity.Pos;
import com.hqu.modules.basedata.mdmpos.mapper.PosMapper;

/**
 * 岗位维护表Service
 * @author luyumiao
 * @version 2018-04-11
 */
@Service
@Transactional(readOnly = true)
public class PosService extends CrudService<PosMapper, Pos> {

	public Pos get(String id) {
		return super.get(id);
	}
	
	public List<Pos> findList(Pos pos) {
		return super.findList(pos);
	}
	
	public Page<Pos> findPage(Page<Pos> page, Pos pos) {
		return super.findPage(page, pos);
	}
	
	@Transactional(readOnly = false)
	public void save(Pos pos) {
		super.save(pos);
	}
	
	@Transactional(readOnly = false)
	public void delete(Pos pos) {
		super.delete(pos);
	}
	
}