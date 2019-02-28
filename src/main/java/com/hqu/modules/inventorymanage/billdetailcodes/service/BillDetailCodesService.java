/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.inventorymanage.billdetailcodes.service;

import java.util.List;

import com.hqu.modules.inventorymanage.billmain.entity.BillMain;
import com.hqu.modules.inventorymanage.billmain.mapper.BillMainMapper;
import com.jeeplus.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.hqu.modules.inventorymanage.billdetailcodes.entity.BillDetailCodes;
import com.hqu.modules.inventorymanage.billdetailcodes.mapper.BillDetailCodesMapper;

/**
 * 二维码扫描表Service
 * @author huang.youcai
 * @version 2018-08-06
 */
@Service
@Transactional(readOnly = true)
public class BillDetailCodesService extends CrudService<BillDetailCodesMapper, BillDetailCodes> {

	@Autowired
	BillMainMapper billMainMapper;

	public BillDetailCodes get(String id) {
		return super.get(id);
	}
	
	public List<BillDetailCodes> findList(BillDetailCodes billDetailCodes) {
		return super.findList(billDetailCodes);
	}
	
	public Page<BillDetailCodes> findPage(Page<BillDetailCodes> page, BillDetailCodes billDetailCodes) {
		return super.findPage(page, billDetailCodes);
	}
	
	@Transactional(readOnly = false)
	public void save(BillDetailCodes billDetailCodes) {
		super.save(billDetailCodes);
	}
	
	@Transactional(readOnly = false)
	public void delete(BillDetailCodes billDetailCodes) {
		super.delete(billDetailCodes);
	}

	@Transactional(readOnly = false)
	public void saveUdpData(String code,String binId,String locId) {
		BillDetailCodes billDetailCodes = new BillDetailCodes();
		billDetailCodes.setBinId(binId);
		billDetailCodes.setLocId(locId);
		billDetailCodes.setItemBarcode(code);
		BillMain billMain = billMainMapper.selectMaxBillMainInfo();
//		if(billMain == null){
//			billDetailCodes.setBillNum(code.substring(0,15));
//		}else{
//			billDetailCodes.setBillNum(billMain.getBillNum());
//		}
		String billnum=billMainMapper.getNumByChk(code.substring(0,15));
		if(StringUtils.isNotBlank(billnum)) {
			billDetailCodes.setBillNum(billMainMapper.getNumByChk(code.substring(0, 15)));
			billDetailCodes.setItemCode(code.substring(15, 27));
			super.save(billDetailCodes);
		}
		else {
			logger.debug("找不到二维码对应的单据号！");
		}
	}
	@Transactional(readOnly = false)
	public void saveUdpData(String code,String billNum) {
		BillDetailCodes billDetailCodes = new BillDetailCodes();
		billDetailCodes.setItemBarcode(code);
		billDetailCodes.setBillNum(billNum);
		billDetailCodes.setItemCode(code.substring(15,27));
		super.save(billDetailCodes);
	}
	@Transactional(readOnly = false)
	public void saveOutTreasuryUdpData(String code,String billNum,String binId,String locId) {
		BillDetailCodes billDetailCodes = new BillDetailCodes();
		billDetailCodes.setItemBarcode(code);
		billDetailCodes.setBillNum(billNum);
		billDetailCodes.setBinId(binId);
		billDetailCodes.setLocId(locId);
		billDetailCodes.setItemCode(code.substring(15,27));
		super.save(billDetailCodes);
	}
	
}