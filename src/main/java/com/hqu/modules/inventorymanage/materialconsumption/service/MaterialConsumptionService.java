/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.inventorymanage.materialconsumption.service;

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
import com.hqu.modules.inventorymanage.materialconsumption.entity.MaterialConsumption;
import com.hqu.modules.inventorymanage.billmain.entity.BillDetail;
import com.hqu.modules.inventorymanage.billmain.mapper.BillDetailMapper;


@Service
@Transactional(readOnly = true)
public class MaterialConsumptionService extends CrudService<BillMainMapper, BillMain> {

	@Autowired
	private BillDetailMapper billDetailMapper;
	
	public Page<BillMain> findPage1(Page<BillMain> page, BillMain billMain,BillDetail billDetail) {
		Page<BillMain> myPage=super.findPage(page, billMain);
		List<BillMain> list=myPage.getList();
		for(int i=0;i<list.size();i++) {
			billDetail.setBillNum(list.get(i));
			list.get(i).setBillDetailList(billDetailMapper.findList(billDetail));
		}
		sumByDepart(myPage);
		return myPage;
	}
	
	public Page<MaterialConsumption> findPage2(Page<BillMain> page, BillMain billMain,BillDetail billDetail) {
		Page<BillMain> myPage=super.findPage(page, billMain);
		List<BillMain> list=myPage.getList();
		for(int i=0;i<list.size();i++) {
			billDetail.setBillNum(list.get(i));
			list.get(i).setBillDetailList(billDetailMapper.findList(billDetail));
		}
		sumByDepartAndItem(myPage);
		return alterPage(myPage);
	}
	/*
	 * 统计部门领料的总数量和金额
	 */
	private void sumByDepart(Page<BillMain> page){
		List<BillMain> billMains=page.getList();
		for(int i=0;i<billMains.size();i++) {
			for(int j=i+1;j<billMains.size();j++) {
				BillMain ib=billMains.get(i);
				List<BillDetail> billDetails=ib.getBillDetailList();
				sumItem(billDetails);
				BillMain jb=billMains.get(j);
				if(ib.getBillDetailList().size()==0||jb.getBillDetailList().size()==0) break;
				if(ib.getDept()!=null&&jb.getDept()!=null) {
					if (ib.getDept().getCode().equals(jb.getDept().getCode())) {
						sumItem(jb.getBillDetailList());
						ib.getBillDetailList().get(0).setItemQty(jb.getBillDetailList().get(0).getItemQty() + ib.getBillDetailList().get(0).getItemQty());
						ib.getBillDetailList().get(0).setItemSum(jb.getBillDetailList().get(0).getItemSum() + ib.getBillDetailList().get(0).getItemSum());
						billMains.remove(j--);
					}
				}
			}
		}
	}
	
	/*
	 * 合并物料相同的数据
	 */
	private void sumItem(List<BillDetail> billDetails) {
		for(int l=1;l<billDetails.size();l++) {
			billDetails.get(0).setItemQty(billDetails.get(l).getItemQty()+billDetails.get(0).getItemQty());
			billDetails.get(0).setItemSum(billDetails.get(l).getItemSum()+billDetails.get(0).getItemSum());
		}
	}
	
	/*
	 * 统计部门领取的各个物料的总数量和金额
	 */
	private void sumByDepartAndItem(Page<BillMain> page){
		List<BillMain> billMains=page.getList();
		for(int i=0;i<billMains.size();i++) {
			for(int j=i+1;j<billMains.size();j++) {
				BillMain ib=billMains.get(i);
				BillMain jb=billMains.get(j);
				if(ib.getDept()!=null&&jb.getDept()!=null&&ib.getDept().getCode()!=null&&jb.getDept().getCode()!=null) {
					if (ib.getDept().getCode().equals(jb.getDept().getCode())) {
						combineByDepart(ib.getBillDetailList(), jb.getBillDetailList());
						billMains.remove(j--);
					}
				}
			}
		}
	}
	
	/*
	 * 合并物料相同的数据
	 */
	private void combineByDepart(List<BillDetail> a,List<BillDetail> b) {
		for(int i=0;i<a.size();i++) {
			for(int j=0;j<b.size();j++) {
				if(a.get(i).getItemName()!=null&&b.get(j).getItemName()!=null) {
					if (a.get(i).getItemName().equals(b.get(j).getItemName())) {
						a.get(i).setItemQty(a.get(i).getItemQty() + b.get(j).getItemQty());
						a.get(i).setItemSum(a.get(i).getItemSum() + b.get(j).getItemSum());
						b.remove(j--);
					}
				}
			}
		}
		a.addAll(b);
	}
	
	/*
	 * 更改page内数据（BillDetail->MaterialConsumption)
	 */
	private Page<MaterialConsumption> alterPage(Page<BillMain> page) {
		List<BillMain> billList=page.getList();
		Page<MaterialConsumption> mate=new Page<MaterialConsumption>();
		for(BillMain b:billList) {
			for(BillDetail detail:b.getBillDetailList()) {
				MaterialConsumption m=new MaterialConsumption();
				if(b.getDept()!=null) {
					m.setDeptCode(b.getDept().getCode());
				}
				m.setDeptName(b.getDeptName());
				if(detail.getItem()!=null) {
					m.setItemCode(detail.getItem().getId());
				}
				m.setItemName(detail.getItemName());
				m.setItemSpec(detail.getItemSpec());
				m.setMeasUnit(detail.getMeasUnit());
				m.setItemQty(detail.getItemQty());
				m.setItemSum(detail.getItemSum());
				mate.getList().add(m);
			}
		}
		return mate;
	}
}