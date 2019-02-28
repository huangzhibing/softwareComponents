package com.hqu.modules.inventorymanage.amountadjustment.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hqu.modules.basedata.period.entity.Period;
import com.hqu.modules.inventorymanage.billmain.entity.BillMain;
import com.hqu.modules.inventorymanage.outsourcingoutput.mapper.BillDetailOutsourcingMapper;
import com.hqu.modules.inventorymanage.outsourcingoutput.mapper.OutsourcingOutputMapper;

@Service
public class AmountAdjustmentService {
	@Autowired
	private OutsourcingOutputMapper outsourcingOutputMapper;
	@Autowired
	private BillDetailOutsourcingMapper billDetailOutsourcingMapper;
	@Autowired
	private BillMainAmountService billMainOutsourcingService;
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
}
