/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.purchasemanage.purinvcheckpunctual.service;

import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.hqu.modules.purchasemanage.purinvcheckpunctual.entity.PurInvCheckPunctual;
import com.hqu.modules.purchasemanage.purinvcheckpunctual.mapper.PurInvCheckPunctualMapper;

/**
 * 供应商到货准时率明细Service
 * @author zz
 * @version 2018-08-16
 */
@Service
@Transactional(readOnly = true)
public class PurInvCheckPunctualService extends CrudService<PurInvCheckPunctualMapper, PurInvCheckPunctual> {
	@Autowired
	private PurInvCheckPunctualMapper purInvCheckPunctualMapper;
	public PurInvCheckPunctual get(String id) {
		return super.get(id);
	}
	
	public List<PurInvCheckPunctual> findList(PurInvCheckPunctual purInvCheckPunctual) {
		return super.findList(purInvCheckPunctual);
	}
	
	public Page<PurInvCheckPunctual> findPage(Page<PurInvCheckPunctual> page, PurInvCheckPunctual purInvCheckPunctual) {
//		List<PurInvCheckPunctual> punlist = purInvCheckPunctualMapper.findList(purInvCheckPunctual);
//		Iterator<PurInvCheckPunctual> it = punlist.iterator();
//		int serialnum = 1;
//		while(it.hasNext()){
//			PurInvCheckPunctual pun = it.next();
//			pun.setSerialNum(String.valueOf(serialnum));
//			serialnum++;
//		}
//		page.setList(punlist);
		return super.findPage(page, purInvCheckPunctual);
	}
	
	@Transactional(readOnly = false)
	public void save(PurInvCheckPunctual purInvCheckPunctual) {
		super.save(purInvCheckPunctual);
	}
	
	@Transactional(readOnly = false)
	public void delete(PurInvCheckPunctual purInvCheckPunctual) {
		super.delete(purInvCheckPunctual);
	}
	
}