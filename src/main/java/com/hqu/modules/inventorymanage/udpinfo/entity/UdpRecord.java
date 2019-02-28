/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.inventorymanage.udpinfo.entity;


import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 记录udp信息Entity
 * @author huang.youcai
 * @version 2018-08-06
 */
public class UdpRecord extends DataEntity<UdpRecord> {
	
	private static final long serialVersionUID = 1L;
	private String udpInfo;		// udp字符串信息
	private String udpSendUser;		// 发送方信息BW/ERP
	
	public UdpRecord() {
		super();
	}

	public UdpRecord(String id){
		super(id);
	}

	@ExcelField(title="udp字符串信息", align=2, sort=7)
	public String getUdpInfo() {
		return udpInfo;
	}

	public void setUdpInfo(String udpInfo) {
		this.udpInfo = udpInfo;
	}
	
	@ExcelField(title="发送方信息BW/ERP", align=2, sort=8)
	public String getUdpSendUser() {
		return udpSendUser;
	}

	public void setUdpSendUser(String udpSendUser) {
		this.udpSendUser = udpSendUser;
	}
	
}