package com.hqu.modules.inventorymanage.initbill.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hqu.modules.basedata.period.entity.Period;
import com.hqu.modules.inventorymanage.billmain.entity.BillMain;
import com.hqu.modules.inventorymanage.initbill.mapper.BillDetailInitBillMapper;
import com.hqu.modules.inventorymanage.initbill.mapper.InitBillMapper;

@Service
public class InitBillService {
	@Autowired
	private InitBillMapper outsourcingOutputMapper;
	@Autowired
	private BillDetailInitBillMapper billDetailOutsourcingMapper;
	@Autowired
	private BillMainInitBillService billMainOutsourcingService;
	public Period findPeriodByTime(Map<String, Object> map) {
		return outsourcingOutputMapper.findPeriodByTime(map);
	}
	public List<BillMain> findBillMainByItemCode(String itemCode){
		List<String> ids=billDetailOutsourcingMapper.findIdByItemCode(itemCode);
		if(ids.size()==0) return null;
		List<BillMain> blist=new ArrayList<>();
		for(int i=0;i<ids.size();i++) {
			blist.add(billMainOutsourcingService.get(ids.get(i)));
		}
		return blist;
	}
	
	public Boolean changeRealQty(Map<String, Object>map) {
		return outsourcingOutputMapper.changeRealQty(map);
	}
	public Boolean updateQty(Map<String, Object> map) {
		return outsourcingOutputMapper.updateQty(map);
	}
	public Boolean updateRealQty(Map<String, Object> map) {
		return outsourcingOutputMapper.updateRealQty(map);
	}
}
