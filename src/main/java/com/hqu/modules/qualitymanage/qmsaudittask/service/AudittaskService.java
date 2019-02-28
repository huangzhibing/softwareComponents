/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.qualitymanage.qmsaudittask.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.common.utils.StringUtils;
import com.hqu.modules.qualitymanage.qmsaudittask.entity.Audittask;
import com.hqu.modules.qualitymanage.qmsaudittask.mapper.AudittaskMapper;
import com.hqu.modules.qualitymanage.qmsaudittask.entity.AuditTaskItem;
import com.hqu.modules.qualitymanage.qmsaudittask.mapper.AuditTaskItemMapper;

/**
 * 稽核任务Service
 * @author syc
 * @version 2018-05-25
 */
@Service
@Transactional(readOnly = true)
public class AudittaskService extends CrudService<AudittaskMapper, Audittask> {

	@Autowired
	private AuditTaskItemMapper auditTaskItemMapper;
	
	public Audittask get(String id) {
		Audittask audittask = super.get(id);
		audittask.setAuditTaskItemList(auditTaskItemMapper.findList(new AuditTaskItem(audittask)));
		return audittask;
	}
	
	public List<Audittask> findList(Audittask audittask) {
		return super.findList(audittask);
	}
	
	public Page<Audittask> findPage(Page<Audittask> page, Audittask audittask) {
		return super.findPage(page, audittask);
	}
	
	@Transactional(readOnly = false)
	public void save(Audittask audittask) {
		super.save(audittask);
		for (AuditTaskItem auditTaskItem : audittask.getAuditTaskItemList()){
			if (auditTaskItem.getId() == null){
				continue;
			}
			if (AuditTaskItem.DEL_FLAG_NORMAL.equals(auditTaskItem.getDelFlag())){
				if (StringUtils.isBlank(auditTaskItem.getId())){
					auditTaskItem.setAudittask(audittask);
					auditTaskItem.preInsert();
					auditTaskItemMapper.insert(auditTaskItem);
				}else{
					auditTaskItem.preUpdate();
					auditTaskItemMapper.update(auditTaskItem);
				}
			}else{
				auditTaskItemMapper.delete(auditTaskItem);
			}
		}
	}
	
	@Transactional(readOnly = false)
	public void delete(Audittask audittask) {
		super.delete(audittask);
		auditTaskItemMapper.delete(new AuditTaskItem(audittask));
	}
	
}