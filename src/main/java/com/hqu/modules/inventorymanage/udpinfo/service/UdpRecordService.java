/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.inventorymanage.udpinfo.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.hqu.modules.inventorymanage.udpinfo.entity.UdpRecord;
import com.hqu.modules.inventorymanage.udpinfo.mapper.UdpRecordMapper;

/**
 * 记录udp信息Service
 * @author huang.youcai
 * @version 2018-08-06
 */
@Service
@Transactional(readOnly = true)
public class UdpRecordService extends CrudService<UdpRecordMapper, UdpRecord> {

	public UdpRecord get(String id) {
		return super.get(id);
	}
	
	public List<UdpRecord> findList(UdpRecord udpRecord) {
		return super.findList(udpRecord);
	}
	
	public Page<UdpRecord> findPage(Page<UdpRecord> page, UdpRecord udpRecord) {
		return super.findPage(page, udpRecord);
	}
	
	@Transactional(readOnly = false)
	public void save(UdpRecord udpRecord) {
		super.save(udpRecord);
	}
	
	@Transactional(readOnly = false)
	public void delete(UdpRecord udpRecord) {
		super.delete(udpRecord);
	}

	@Transactional(readOnly = false)
	public void saveUdpIno(String udpInfo,String type){
		logger.info("记录udp信息:{}",udpInfo);
		UdpRecord udpRecord = new UdpRecord();
		udpRecord.setUdpInfo(udpInfo);
		udpRecord.setUdpSendUser(type);
		super.save(udpRecord);
	};

	@Transactional(readOnly = false)
	public void saveBWUdpIno(String udpInfo){
		saveUdpIno(udpInfo,"BW");
	};

	@Transactional(readOnly = false)
	public void saveERPUdpIno(String udpInfo){
		saveUdpIno(udpInfo,"ERP");
	};
}