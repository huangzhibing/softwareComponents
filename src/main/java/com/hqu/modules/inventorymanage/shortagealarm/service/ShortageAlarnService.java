package com.hqu.modules.inventorymanage.shortagealarm.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;
import com.hqu.modules.basedata.period.entity.Period;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.common.utils.StringUtils;
import com.hqu.modules.inventorymanage.billmain.entity.BillMain;
import com.hqu.modules.inventorymanage.billmain.mapper.BillMainMapper;
import com.hqu.modules.inventorymanage.invaccount.entity.InvAccount;
import com.hqu.modules.inventorymanage.invaccount.mapper.InvAccountMapper;
import com.hqu.modules.inventorymanage.shortagealarm.mapper.ShortageAlarmMapper;
import com.hqu.modules.inventorymanage.billmain.entity.BillDetail;
import com.hqu.modules.inventorymanage.billmain.mapper.BillDetailMapper;


@Service
@Transactional(readOnly = true)
public class ShortageAlarnService extends CrudService<InvAccountMapper, InvAccount> {

	public Page<InvAccount> findPage(Page<InvAccount> page, InvAccount invAccount) {
		page=super.findPage(page, invAccount);
		sumByWareAndItem(page.getList());
		return page;
	}
	
	/*
	 * 合并相同仓库相同物料的库存账数据
	 */
	public void sumByWareAndItem(List<InvAccount> invs) {
		for(int i=0;i<invs.size();i++) {
			for(int j=i+1;j<invs.size();j++) {
				if(StringUtils.equals(invs.get(i).getWare().getWareName(),invs.get(j).getWare().getWareName())) {
					if(StringUtils.equals(invs.get(i).getItem().getName(),invs.get(j).getItem().getName())) {
						invs.get(i).setNowQty(invs.get(i).getNowQty()+invs.get(j).getNowQty());
						invs.remove(j--);
					}
				}
			}
		}
	}
	


}