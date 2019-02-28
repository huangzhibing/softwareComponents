/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.workshopmanage.mserialnoprint.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.hqu.modules.workshopmanage.mserialnoprint.entity.MSerialNoPrint;
import com.hqu.modules.workshopmanage.mserialnoprint.mapper.MSerialNoPrintMapper;

/**
 * 机器序列号打印Service
 * @author yxb
 * @version 2019-01-10
 */
@Service
@Transactional(readOnly = true)
public class MSerialNoPrintService extends CrudService<MSerialNoPrintMapper, MSerialNoPrint> {

	public MSerialNoPrint get(String id) {
		return super.get(id);
	}
	
	public List<MSerialNoPrint> findList(MSerialNoPrint mSerialNoPrint) {
		return super.findList(mSerialNoPrint);
	}
	
	public Page<MSerialNoPrint> findPage(Page<MSerialNoPrint> page, MSerialNoPrint mSerialNoPrint) {
		return super.findPage(page, mSerialNoPrint);
	}
	
	@Transactional(readOnly = false)
	public void save(MSerialNoPrint mSerialNoPrint) {
		super.save(mSerialNoPrint);
	}
	
	@Transactional(readOnly = false)
	public void delete(MSerialNoPrint mSerialNoPrint) {
		super.delete(mSerialNoPrint);
	}
	
}