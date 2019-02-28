/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.inventorymanage.amountadjustment.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.common.utils.StringUtils;
import com.hqu.modules.inventorymanage.billmain.entity.BillMain;
import com.hqu.modules.inventorymanage.billmain.mapper.BillMainMapper;
import com.hqu.modules.inventorymanage.outsourcingoutput.mapper.BillDetailOutsourcingMapper;
import com.hqu.modules.inventorymanage.outsourcingoutput.mapper.BillMainOutsourcingMapper;
import com.hqu.modules.inventorymanage.amountadjustment.mapper.BillDetailAmountMapper;
import com.hqu.modules.inventorymanage.amountadjustment.mapper.BillMainAmountMapper;
import com.hqu.modules.inventorymanage.billmain.entity.BillDetail;
import com.hqu.modules.inventorymanage.billmain.mapper.BillDetailMapper;

/**
 * 出入库单据Service
 * @author M1ngz
 * @version 2018-04-16
 */
@Service
@Transactional(readOnly = true)
public class BillMainAmountService extends CrudService<BillMainAmountMapper, BillMain> {

	@Autowired
	private BillDetailAmountMapper billDetailMapper;
	
	
	
	public BillMain get(String id) {
		BillMain billMain = super.get(id);
		billMain.setBillDetailList(billDetailMapper.findList(new BillDetail(billMain)));
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
		for (BillDetail billDetail : billMain.getBillDetailList()){
			if (billDetail.getId() == null){
				continue;
			}
			if (BillDetail.DEL_FLAG_NORMAL.equals(billDetail.getDelFlag())){
				if (StringUtils.isBlank(billDetail.getId())){
					billDetail.setBillNum(billMain);
					billDetail.preInsert();
					billDetailMapper.insert(billDetail);
				}else{
					billDetail.preUpdate();
					billDetailMapper.update(billDetail);
				}
			}else{
				billDetailMapper.delete(billDetail);
			}
		}
	}
	
	@Transactional(readOnly = false)
	public void delete(BillMain billMain) {
		super.delete(billMain);
		billDetailMapper.delete(new BillDetail(billMain));
	}


	public 	String getMaxIdByTypeAndDate(String para){
	    return mapper.getMaxIdByTypeAndDate(para);
    }

}