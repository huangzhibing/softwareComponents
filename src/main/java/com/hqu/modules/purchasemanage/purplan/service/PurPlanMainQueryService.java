/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.purchasemanage.purplan.service;

import com.hqu.modules.purchasemanage.contractmain.entity.ContractDetail;
import com.hqu.modules.purchasemanage.contractmain.entity.ContractMain;
import com.hqu.modules.purchasemanage.contractmain.mapper.ContractDetailMapper;
import com.hqu.modules.purchasemanage.contractmain.mapper.ContractMainMapper;
import com.hqu.modules.purchasemanage.group.mapper.GroupBuyerMapper;

import com.hqu.modules.purchasemanage.purplan.entity.PlanRelations;
import com.hqu.modules.purchasemanage.purplan.entity.PurPlanDetail;
import com.hqu.modules.purchasemanage.purplan.entity.PurPlanMain;
import com.hqu.modules.purchasemanage.purplan.mapper.ItemExtendMapper;
import com.hqu.modules.purchasemanage.purplan.mapper.PlanRelationsMapper;
import com.hqu.modules.purchasemanage.purplan.mapper.PurPlanDetailMapper;
import com.hqu.modules.purchasemanage.purplan.mapper.PurPlanMainMapper;
import com.hqu.modules.purchasemanage.relations.mapper.RelationsMapper;
import com.hqu.modules.purchasemanage.rollplannewquery.entity.RollPlanNew;
import com.hqu.modules.purchasemanage.rollplannewquery.mapper.RollPlanNewMapper;
import com.hqu.modules.purchasemanage.rollplannewquery.service.RollPlanNewService;

import com.hqu.modules.qualitymanage.purinvcheckmain.entity.InvCheckDetail;
import com.hqu.modules.qualitymanage.purinvcheckmain.entity.InvCheckMain;
import com.hqu.modules.qualitymanage.purinvcheckmain.mapper.InvCheckDetailMapper;
import com.hqu.modules.qualitymanage.purinvcheckmain.mapper.InvCheckMainMapper;
import com.jeeplus.core.service.CrudService;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


/**
 * 采购计划关联查询Service
 * @author yangxianbang
 * @version 2018-5-24
 */
@Service
@Transactional(readOnly = true)
public class PurPlanMainQueryService extends CrudService<PurPlanMainMapper, PurPlanMain> {

	@Autowired
	private PurPlanDetailMapper purPlanDetailMapper;

	@Autowired
	GroupBuyerMapper buyerMapper;


	@Autowired
	PlanRelationsMapper planRelationsMapper;
	@Autowired
	RollPlanNewMapper rollPlanNewMapper;
	@Autowired
	InvCheckMainMapper invCheckMainMapper;
	@Autowired
	InvCheckDetailMapper invCheckDetailMapper;
	@Autowired
	ContractMainMapper contractMainMapper;
	@Autowired
	ContractDetailMapper contractDetailMapper;

	public PurPlanMain get(String id) {
		PurPlanMain purPlanMain = super.get(id);
		List<PurPlanDetail> detaillist = purPlanDetailMapper.findList(new PurPlanDetail(purPlanMain));
		for (PurPlanDetail d : detaillist) {
			if (d.getBuyerId() != null) {
				d.setBuyerId(buyerMapper.get(d.getBuyerId().getId()));
			}

		}

		purPlanMain.setRollPlanNewList(findRollPlans(detaillist));
		purPlanMain.setPurPlanDetailList(detaillist);
		List<ContractDetail> contractDetailList=findContracts(detaillist);
		purPlanMain.setContractDetailList(contractDetailList);
		purPlanMain.setInvCheckDetailList(findInvCheckDetailsByC(contractDetailList));
		if(purPlanMain.getInvCheckDetailList().size()<=0)//由合同找到货子表信息找不到时
		{
			purPlanMain.setInvCheckDetailList(findInvCheckDetailsByP(detaillist));
		}
		return purPlanMain;
	}

	/**
	 * 查找对应的滚动计划信息
	 * @param planDetails
	 * @return
	 */
	private List<RollPlanNew> findRollPlans(List<PurPlanDetail> planDetails){
		List<RollPlanNew> rollPlanList=new ArrayList<>();
		for (PurPlanDetail d : planDetails) {
			if(d.getApplySerialNum()!=null&&d.getApplyBillNum()!=null) {
				RollPlanNew rollPlanNew = new RollPlanNew();
				rollPlanNew.setSerialNum(d.getApplySerialNum());
				rollPlanNew.setBillNum(d.getApplyBillNum());
				List<RollPlanNew> rollPlanNews = rollPlanNewMapper.findList(rollPlanNew);
				if (rollPlanNews.size() > 0) {
					rollPlanList.addAll(rollPlanNews);
				}
			}
		}
		return rollPlanList;
	}
	/**
	 * 查找计划对应的合同子表信息
	 * @param planDetails
	 * @return
	 */
	private List<ContractDetail> findContracts(List<PurPlanDetail> planDetails){
		List<ContractDetail> contractDetailList=new ArrayList<>();
		for(PurPlanDetail d:planDetails){
			for(PlanRelations relations :findRelations(true,d.getBillNum().getBillNum(),d.getSerialNum(),"PC")){
				ContractDetail cd= new ContractDetail();
				cd.setSerialNum(relations.getAfterId());
				if(cd.getContractMain()==null){
					ContractMain cm=new ContractMain();
					cm.setBillNum(relations.getAfterNum());
					cd.setContractMain(cm);
				}else {
					cd.getContractMain().setBillNum(relations.getAfterNum());
				}
				List<ContractDetail> contractDetails=contractDetailMapper.findList(cd);
				if(contractDetails.size()>0){
					for(ContractDetail contractDetail:contractDetails){
						contractMainMapper.get(contractDetail.getContractMain());
						List<ContractMain> contractMains =contractMainMapper.findList(contractDetail.getContractMain());
						if(contractMains.size()>0) {
							contractDetail.setContractMain(contractMains.get(0));   //业务主键唯一，这里的size一般为1
						}
					}
					contractDetailList.addAll(contractDetails);
				}



			}
		}
		return contractDetailList;
	}

	/**
	 * 查找合同对应的到货子表信息
	 * @param contractDetails
	 * @return
	 */
	private List<InvCheckDetail> findInvCheckDetailsByC(List<ContractDetail> contractDetails){
		List<InvCheckDetail> invCheckDetailList=new ArrayList<>();
		for(ContractDetail cd:contractDetails){
			for(PlanRelations relations :findRelations(true,cd.getContractMain().getBillNum(),cd.getSerialNum(),"CD")){
				//查找对应关系的invCheckDetail
				InvCheckDetail invCheckDetail=new InvCheckDetail();
				invCheckDetail.setSerialnum(relations.getAfterId());
				if(invCheckDetail.getInvCheckMain()==null) {
					InvCheckMain invCheckMain=new InvCheckMain();
					invCheckMain.setBillnum(relations.getAfterNum());
					invCheckDetail.setInvCheckMain(invCheckMain);
				}else{
					invCheckDetail.getInvCheckMain().setBillnum(relations.getAfterNum());
				}
				List<InvCheckDetail> invCheckDetails=invCheckDetailMapper.findList(invCheckDetail);
				if(invCheckDetails.size()>0){
					for(InvCheckDetail icd:invCheckDetails){
						List<InvCheckMain> invCheckMains =invCheckMainMapper.findList(icd.getInvCheckMain());
						if(invCheckMains.size()>0) {
							icd.setInvCheckMain(invCheckMains.get(0));//业务主键唯一，这里的size一般为1
						}
						icd.setConBillNum(relations.getFrontNum());
					}
					invCheckDetailList.addAll(invCheckDetails);

				}


			}
		}
		return invCheckDetailList;
	}

	/**
	 * 查找计划对应的到货子表信息
	 * @param purPlanDetails
	 * @return
	 */
	private List<InvCheckDetail> findInvCheckDetailsByP(List<PurPlanDetail> purPlanDetails){
		List<InvCheckDetail> invCheckDetailList=new ArrayList<>();
		for(PurPlanDetail d:purPlanDetails){
			for(PlanRelations relations :findRelations(true,d.getBillNum().getBillNum(),d.getSerialNum(),"PD")){
				//查找对应关系的invCheckDetail
				InvCheckDetail invCheckDetail=new InvCheckDetail();
				invCheckDetail.setSerialnum(relations.getAfterId());
				if(invCheckDetail.getInvCheckMain()==null) {
					InvCheckMain invCheckMain=new InvCheckMain();
					invCheckMain.setBillnum(relations.getAfterNum());
					invCheckDetail.setInvCheckMain(invCheckMain);
				}else{
					invCheckDetail.getInvCheckMain().setBillnum(relations.getAfterNum());
				}
				List<InvCheckDetail> invCheckDetails=invCheckDetailMapper.findList(invCheckDetail);
				if(invCheckDetails.size()>0){
					for(InvCheckDetail icd:invCheckDetails){
						List<InvCheckMain> invCheckMains =invCheckMainMapper.findList(icd.getInvCheckMain());
						if(invCheckMains.size()>0) {
							icd.setInvCheckMain(invCheckMains.get(0));//业务主键唯一，这里的size一般为1
						}
						//icd.setConBillNum(relations.getFrontNum());
						icd.setPlanBillNum(relations.getFrontNum());
					}
					invCheckDetailList.addAll(invCheckDetails);
				}


			}
		}
		return invCheckDetailList;
	}

	/**
	 * 根据前者找后者或者后者找前者 isFront=true/前找后
	 * @param billNum
	 * @param id
	 * @param type
	 * @return
	 */
	private List<PlanRelations> findRelations(Boolean isFront,String billNum, Integer id, String type){
		PlanRelations r=new PlanRelations();
		if(isFront) {
			r.setFrontId(id);
			r.setFrontNum(billNum);
		}else{
			r.setAfterNum(billNum);
			r.setAfterId(id);
		}
		r.setType(type);
		return planRelationsMapper.findList(r);
	}






}