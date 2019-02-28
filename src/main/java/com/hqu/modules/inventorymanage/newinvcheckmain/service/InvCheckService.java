/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.inventorymanage.newinvcheckmain.service;

import com.hqu.modules.inventorymanage.billmain.entity.BillDetail;
import com.hqu.modules.inventorymanage.billmain.entity.BillDetailCode;
import com.hqu.modules.inventorymanage.billmain.entity.BillMain;
import com.hqu.modules.inventorymanage.billmain.mapper.BillMainMapper;
import com.hqu.modules.inventorymanage.outsourcingoutput.mapper.BillDetailOutsourcingMapper;
import com.hqu.modules.inventorymanage.outsourcingoutput.mapper.BillMainOutsourcingMapper;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = false)
public class InvCheckService extends CrudService<BillMainOutsourcingMapper, BillMain> {

	@Autowired
	private BillDetailOutsourcingMapper billDetailMapper;

	@Autowired
	private BillMainMapper billMainMapper;


	public BillMain get(String id) {
		System.out.println(id + "ididididid");
		BillMain billMain = super.get(id);
		System.out.println(billMain + "******");
		billMain.setBillDetailList(billDetailMapper.findList(new BillDetail(billMain)));
		System.out.println("******");
		return billMain;
	}

	public List<BillMain> findList(BillMain billMain) {
		return super.findList(billMain);
	}

	public Page<BillMain> findPage(Page<BillMain> page, BillMain billMain) {
		return super.findPage(page, billMain);
	}

	@Transactional(readOnly = false)
	public void save(BillMain billMain) {
		super.save(billMain);
		for (BillDetail billDetail : billMain.getBillDetailList()) {
			if (StringUtils.isBlank(billDetail.getId())) {
				billDetail.setBillNum(billMain);
				billDetail.preInsert();
				billDetailMapper.insert(billDetail);
			} else {
				billDetail.setBillNum(billMain);
				billDetail.preUpdate();
				billDetailMapper.update(billDetail);
			}
		}
	}

	@Transactional(readOnly = false)
	public void saveBillCode(BillMain billMain, BillDetailCode billDetailCode) {
		if (StringUtils.isBlank(billDetailCode.getId())) {
			billDetailCode.setBillNum(billMain);
			billDetailCode.preInsert();
			billMainMapper.saveBillCode(billDetailCode);
		} else {
			billDetailCode.setBillNum(billMain);
			billDetailCode.preUpdate();
			billMainMapper.updateBillCode(billDetailCode);
		}
	}
}
	
