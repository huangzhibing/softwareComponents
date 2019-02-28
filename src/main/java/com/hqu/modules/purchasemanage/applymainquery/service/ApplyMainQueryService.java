/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.purchasemanage.applymainquery.service;

import com.google.common.collect.Lists;

import com.hqu.modules.basedata.item.entity.Item;
import com.hqu.modules.basedata.item.mapper.ItemMapper;
import com.hqu.modules.purchasemanage.applymain.entity.ApplyDetail;
import com.hqu.modules.purchasemanage.applymain.entity.ApplyMain;

import com.hqu.modules.basedata.item.entity.Item;
import com.hqu.modules.basedata.item.mapper.ItemMapper;

import com.hqu.modules.purchasemanage.applymainquery.entity.ApplyDetailQuery;
import com.hqu.modules.purchasemanage.applymainquery.entity.ApplyMainQuery;
import com.hqu.modules.purchasemanage.applymainquery.mapper.ApplyDetailQueryMapper;
import com.hqu.modules.purchasemanage.applymainquery.mapper.ApplyMainQueryMapper;
import com.hqu.modules.purchasemanage.contractmain.entity.ContractDetail;
import com.hqu.modules.purchasemanage.contractmain.entity.ContractMain;
import com.hqu.modules.purchasemanage.contractmain.mapper.ContractDetailMapper;
import com.hqu.modules.purchasemanage.contractmain.mapper.ContractMainMapper;
import com.hqu.modules.purchasemanage.purplan.entity.PurPlanDetail;
import com.hqu.modules.purchasemanage.purplan.entity.PurPlanMain;
import com.hqu.modules.purchasemanage.purplan.mapper.PurPlanDetailMapper;
import com.hqu.modules.purchasemanage.purplan.mapper.PurPlanMainMapper;
import com.hqu.modules.purchasemanage.relations.entity.Relations;
import com.hqu.modules.purchasemanage.relations.mapper.RelationsMapper;
import com.hqu.modules.qualitymanage.purinvcheckmain.entity.InvCheckDetail;
import com.hqu.modules.qualitymanage.purinvcheckmain.entity.InvCheckMain;
import com.hqu.modules.qualitymanage.purinvcheckmain.mapper.InvCheckDetailMapper;
import com.hqu.modules.qualitymanage.purinvcheckmain.mapper.InvCheckMainMapper;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 采购需求查询Service
 * @author syc
 * @version 2018-04-26
 */
@Service
@Transactional(readOnly = true)
public class ApplyMainQueryService extends CrudService<ApplyMainQueryMapper, ApplyMainQuery> {

	@Autowired
	private ApplyDetailQueryMapper applyDetailQueryMapper;
	
	@Autowired
	private RelationsMapper relationsMapper;
	@Autowired
	private ItemMapper itemMapper;
	
	
	@Autowired
	private PurPlanMainMapper purPlanMainMapper;
	@Autowired
	private PurPlanDetailMapper purPlanDetailMapper;
	
	@Autowired
	private ApplyMainQueryMapper applyMainQueryMapper;
	@Autowired
	private ContractMainMapper contractMainMapper;
	@Autowired
	private ContractDetailMapper contractDetailMapper;
	
	@Autowired
	private InvCheckMainMapper invCheckMainMapper;
	@Autowired
	private InvCheckDetailMapper invCheckDetailMapper;
	
	
	public ApplyMainQuery get(String id) {
		ApplyMainQuery applyMainQuery = super.get(id);
		applyMainQuery.setApplyDetailQueryList(applyDetailQueryMapper.findList(new ApplyDetailQuery(applyMainQuery)));
		return applyMainQuery;
	}
	
	public List<ApplyMainQuery> findList(ApplyMainQuery applyMainQuery) {
		return super.findList(applyMainQuery);
	}
	
	public ApplyMainQuery addInfo2(ApplyMainQuery applyMainQuery){
		List<ApplyDetailQuery> applyDetailQueryList = applyMainQuery.getApplyDetailQueryList();		// 子表列表
		List<PurPlanDetail> purPlanDetailList = Lists.newArrayList();		//计划信息子表
		List<ContractDetail> contractDetailList = Lists.newArrayList();		//合同信息子表
		List<InvCheckDetail> invCheckDetailList = Lists.newArrayList();		//到货信息子表
		
		
		//需求生成计划
		if(applyDetailQueryList!=null&&applyDetailQueryList.size()!=0){
			Iterator<ApplyDetailQuery> it = applyDetailQueryList.iterator();
			while(it.hasNext()){
				ApplyDetailQuery applyDetailQuery = it.next();
				String billNum = applyMainQuery.getBillNum();
				Integer serialNum = applyDetailQuery.getSerialNum();
				Relations relations = new Relations();
				relations.setFrontNum(billNum);
				relations.setFrontId(serialNum);
				relations.setType("AP");
				List<Relations> findList = relationsMapper.findList(relations);
				if(findList!=null){
					for(Relations temp:findList){
						//计划单号和序号
						String planBillNum = temp.getAfterNum();
						Integer planSer = temp.getAfterId();
						//根据计划单号寻找计划主表
						PurPlanMain purPlanMain = getPlanMainByBillNum(planBillNum);
						//根据计划主表和序号找计划子表
						PurPlanDetail planDatail = getPlanDatail(purPlanMain, planSer);
						purPlanDetailList.add(planDatail);
						
						
						//计划生成合同部分
						Relations relation1 = new Relations();
						relation1.setFrontNum(planBillNum);
						relation1.setFrontId(planSer);
						relation1.setType("PC");
						//查关系表
						List<Relations> relationList = findRelation(relation1);
						if(relationList!=null){
							for(Relations temp1 :relationList){
								if(temp1!=null){
									//合同单号和子表序号
									String conBillNum = temp1.getAfterNum();
									Integer conSer = temp1.getAfterId();
									//获取合同主表
									ContractMain contractMain = getConMainByBillNum(conBillNum);
									//获取合同子表
									ContractDetail contractDetail = getConDetail(contractMain, conSer);
									contractDetailList.add(contractDetail);
									
									
									//合同生成到货
									Relations relation2 = new Relations();
									relation2.setFrontNum(conBillNum);
									relation2.setFrontId(conSer);
									relation2.setType("CD");
									//查关系表
									List<Relations> relationList2 = findRelation(relation2);
									if(relationList2!=null){
										for(Relations temp2:relationList2){
											if(temp2!=null){
												//到货单号和子表序号
												String invBillNum = temp2.getAfterNum();
												Integer invSer = temp2.getAfterId();
												//获取到货主表
												InvCheckMain invCheckMain = getInvCheckMainByBillNum(invBillNum);
												//获取到货子表
												InvCheckDetail invCheckDetail = getInvCheckDetail(invCheckMain, invSer);
												invCheckDetailList.add(invCheckDetail);
											}
										}
									}
								
								}
							}
						}
						
						
						//计划生成到货
						Relations relation3 = new Relations();
						relation3.setFrontNum(planBillNum);
						relation3.setFrontId(planSer);
						relation3.setType("PD");
						//查关系表
						List<Relations> relationList3 = findRelation(relation3);
						if(relationList3!=null){
							for(Relations temp3:relationList3){
								if(temp3!=null){
									//到货单号和子表序号
									String invBillNum = temp3.getAfterNum();
									Integer invSer = temp3.getAfterId();
									//获取到货主表
									InvCheckMain invCheckMain = getInvCheckMainByBillNum(invBillNum);
									//获取到货子表
									InvCheckDetail invCheckDetail = getInvCheckDetail(invCheckMain, invSer);
									invCheckDetailList.add(invCheckDetail);
								}
								
							}
						}
					}
				}
				
			}
		}
		applyMainQuery.setApplyDetailQueryList(applyDetailQueryList);
		applyMainQuery.setPurPlanDetailList(purPlanDetailList);
		applyMainQuery.setContractDetailList(contractDetailList);
		applyMainQuery.setInvCheckDetailList(invCheckDetailList);
		return applyMainQuery;
	}
	
	public InvCheckMain getInvCheckMainByBillNum(String billNum){
		InvCheckMain invCheckMain = new InvCheckMain();
		invCheckMain.setBillnum(billNum);
		List<InvCheckMain> findList = invCheckMainMapper.findList(invCheckMain);
		if(findList!=null&&findList.size()!=0){
			return findList.get(0);
		}
		return null;
	}
	public InvCheckDetail getInvCheckDetail(InvCheckMain invCheckMain,Integer serio){
		InvCheckDetail invCheckDetail = new InvCheckDetail();
		invCheckDetail.setInvCheckMain(invCheckMain);
		invCheckDetail.setSerialnum(serio);
		List<InvCheckDetail> findList = invCheckDetailMapper.findList(invCheckDetail);
		if(findList!=null&&findList.size()!=0){
			InvCheckDetail invCheckDetail2 = findList.get(0);
			invCheckDetail2.setInvCheckMain(invCheckMain);
			return invCheckDetail2;
		}
		return null;
	}
	
	public ContractDetail getConDetail(ContractMain contractMain,Integer serio){
		if(contractMain==null){return null;}
		String id = contractMain.getId();
		ContractDetail contractDetail = new ContractDetail();
		contractDetail.setContractMain(contractMain);
		contractDetail.setSerialNum(serio);
		List<ContractDetail> findList = contractDetailMapper.findList(contractDetail);
		if(findList!=null&&findList.size()!=0){
			ContractDetail contractDetail2 = findList.get(0);
			contractDetail2.setContractMain(contractMain);
			return contractDetail2;
		}
		return null;
	}
	public ContractMain getConMainByBillNum(String billNum){
		ContractMain contractMain = new ContractMain();
		contractMain.setBillNum(billNum);
		List<ContractMain> findList = contractMainMapper.findList(contractMain);
		if(findList!=null&&findList.size()!=0){
			return findList.get(0);
		}
		return null;
	}
	
	public List<Relations> findRelation(Relations relations){
		List<Relations> findList = relationsMapper.findList(relations);
		if(findList!=null&&findList.size()!=0){
			return findList;
		}
		return null;
	}
	
	public PurPlanMain getPlanMainByBillNum(String billNum){
		if(billNum==null||"".equals(billNum)){
			return null;
		}
		PurPlanMain purPlanMain = new PurPlanMain();
		purPlanMain.setBillNum(billNum);
		List<PurPlanMain> findList = purPlanMainMapper.findList(purPlanMain);
		if(findList!=null&&findList.size()!=0){
			return findList.get(0);
		}
		return null;
	}
	
	public PurPlanDetail getPlanDatail(PurPlanMain purPlanMain ,Integer serio){
		if(purPlanMain==null){return null;}
		String id = purPlanMain.getId();
		PurPlanDetail purPlanDetail = new PurPlanDetail();
		purPlanDetail.setBillNum(purPlanMain);
		purPlanDetail.setSerialNum(serio);
		List<PurPlanDetail> findList = purPlanDetailMapper.findList(purPlanDetail);
		if(findList!=null&&findList.size()!=0){
			PurPlanDetail purPlanDetail2 = findList.get(0);
			purPlanDetail2.setBillNum(purPlanMain);
			return purPlanDetail2;
		}
		return null;
		
	}
	
	//三次循环分别得到：计划、合同、到货子表信息
	public ApplyMainQuery addInfo(ApplyMainQuery applyMainQuery){
		//初始化三个List
		List<PurPlanDetail> purPlanDetailList = Lists.newArrayList();
		List<ContractDetail> contractDetailList = Lists.newArrayList();	
		List<InvCheckDetail> invCheckDetailList = Lists.newArrayList();	
		
		List<ApplyDetailQuery> applyDetailQueryList = applyMainQuery.getApplyDetailQueryList();
		Iterator<ApplyDetailQuery> it = applyDetailQueryList.iterator();
		while(it.hasNext()){
			ApplyDetailQuery applyDetailQuery = it.next();
			//获取查询relations的查询条件
			String billNum = applyMainQuery.getBillNum();
			Integer serialNum = applyDetailQuery.getSerialNum();
			//查询relations
			Relations relations = new Relations();
			relations.setFrontNum(billNum);
			relations.setFrontId(serialNum);
			relations.setType("AR");
			List<Relations> findList = relationsMapper.findList(relations);
			Relations relation = listToOne(findList);
			//relation查询
			if(relation!=null){
				//获取单据状态
				String state = relation.getState();
				if(!"V".equals(state)){
					//单据号
					String afterNum = relation.getAfterNum();
					//序号
					Integer afterId = relation.getAfterId();
					//向采购计划表查询
					//获取主表
					PurPlanMain temp = new PurPlanMain();
					temp.setBillNum(afterNum);
					if(purPlanMainMapper.findList(temp)!=null){
						PurPlanMain purPlanMain = purPlanMainMapper.findList(temp).get(0);
						if(purPlanMain!=null){
							//获取子表
							PurPlanDetail purPlanDetail = new PurPlanDetail();
							//按条件筛选子表
							purPlanDetail.setBillNum(purPlanMain);
							purPlanDetail.setSerialNum(afterId);
							List<PurPlanDetail> purPlanDetailList2 = purPlanDetailMapper.findList(purPlanDetail);
							if(purPlanDetailList2!=null){
								//获取子表
								PurPlanDetail purPlanDetail2 = purPlanDetailList2.get(0);
								//将子表中加入主表的信息
								purPlanDetail2.setBillNum(purPlanMain);
								//加入列表
								purPlanDetailList.add(purPlanDetail2);
							}
						}
					}
					
				}
			}
		}
		applyMainQuery.setPurPlanDetailList(purPlanDetailList);
		
		Iterator<PurPlanDetail> itPurPlanDetail = purPlanDetailList.iterator();
		while(itPurPlanDetail.hasNext()){
			PurPlanDetail purPlanDetail = itPurPlanDetail.next();
			PurPlanMain purPlanMain = purPlanDetail.getBillNum();
			purPlanMain = purPlanMainMapper.get(purPlanMain);
			//获取计划编号
			String billNum = purPlanMain.getBillNum();
			//获取序号
			Integer serialNum = purPlanDetail.getSerialNum();
			//查询relations
			Relations relations = new Relations();
			relations.setFrontNum(billNum);
			relations.setFrontId(serialNum);
			relations.setType("PC");
			List<Relations> findList = relationsMapper.findList(relations);
			//因为一个计划可能会对应多个合同  ,故需便利
			Iterator<Relations> itrelations = findList.iterator();
			while(itrelations.hasNext()){
				Relations relation = itrelations.next();
				//获取单据状态
				String state = relation.getState();
				if(!"V".equals(state)){
					//获取合同单据号
					String afterNum = relation.getAfterNum();
					//获取合同生成序号
					Integer afterId = relation.getAfterId();
					//向采购合同表查询
					//获取主表
					ContractMain contractMain = new  ContractMain();
					contractMain.setBillNum(afterNum);
					ContractMain contractMain2 = contractMainMapper.findList(contractMain).get(0);
					if(contractMain2!=null){
						//获取子表
						ContractDetail contractDetail = new ContractDetail();
						//按条件筛选子表
						contractDetail.setContractMain(contractMain2);
						contractDetail.setSerialNum(afterId);
						
						List<ContractDetail> contractDetailFindList2 = contractDetailMapper.findList(contractDetail);
						if(contractDetailFindList2!=null){
							//获取子表
							ContractDetail contractDetai2 = contractDetailFindList2.get(0);
							//将子表中加入主表的信息
							contractDetai2.setContractMain(contractMain2);
							//加入列表
							contractDetailList.add(contractDetai2);
						}
					}
				}
			}
		}
		applyMainQuery.setContractDetailList(contractDetailList);
		
		
		Iterator<ContractDetail> itContractDetail = contractDetailList.iterator();
		while(itContractDetail.hasNext()){
			ContractDetail contractDetail = itContractDetail.next();
			ContractMain contractMain = contractDetail.getContractMain();
			contractMain = contractMainMapper.get(contractMain);
			//获取合同的billnum
			String billNum = contractMain.getBillNum();
			//获取合同序号
			Integer serialNum = contractDetail.getSerialNum();
			//查询relations
			Relations relations = new Relations();
			relations.setFrontNum(billNum);
			relations.setFrontId(new Long(serialNum).intValue());
			relations.setType("CD");
			List<Relations> findList = relationsMapper.findList(relations);
			//因为一个合同可能会对应多个到货  ,故需便利
			Iterator<Relations> itRelations = findList.iterator();
			while(itRelations.hasNext()){
				Relations relation = itRelations.next();
				//获取单据状态
				String state = relation.getState();
				if(!"V".equals(state)){
					//获取到货单据号
					String afterNum = relation.getAfterNum();
					//获取到货生成序号
					Integer afterId = relation.getAfterId();
					//向采购到货表查询
					//获取主表
					InvCheckMain invCheckMain = new InvCheckMain();
					invCheckMain.setBillnum(afterNum);
					InvCheckMain invCheckMain2 = invCheckMainMapper.findList(invCheckMain).get(0);
					if(invCheckMain2!=null){
						//获取子表
						InvCheckDetail invCheckDetail = new InvCheckDetail();
						//按条件筛选子表
						invCheckDetail.setInvCheckMain(invCheckMain2);
						invCheckDetail.setSerialnum(afterId);
						//筛选到货子表
						List<InvCheckDetail> InvCheckDetailFindList = invCheckDetailMapper.findList(invCheckDetail);
						if(InvCheckDetailFindList!=null){
							InvCheckDetail invCheckDetail2 = InvCheckDetailFindList.get(0);
							invCheckDetail2.setInvCheckMain(invCheckMain2);
							invCheckDetailList.add(invCheckDetail2);
						}
					}
				}
			}
		}
		
		//采购计划至到货
		itPurPlanDetail = purPlanDetailList.iterator();
		while(itPurPlanDetail.hasNext()){
			PurPlanDetail purPlanDetail = itPurPlanDetail.next();
			PurPlanMain purPlanMain = purPlanDetail.getBillNum();
			purPlanMain = purPlanMainMapper.get(purPlanMain);
			//获取计划编号
			String billNum = purPlanMain.getBillNum();
			//获取序号
			Integer serialNum = purPlanDetail.getSerialNum();
			//查询relations
			Relations relations = new Relations();
			relations.setFrontNum(billNum);
			relations.setFrontId(serialNum);
			relations.setType("PD");
			List<Relations> findList = relationsMapper.findList(relations);
			//因为一个计划可能会对应多个到货  ,故需便利
			Iterator<Relations> itrelations = findList.iterator();
			while(itrelations.hasNext()){
				Relations relation = itrelations.next();
				//获取单据状态
				String state = relation.getState();
				if(!"V".equals(state)){
					//获取合同单据号
					String afterNum = relation.getAfterNum();
					//获取合同生成序号
					Integer afterId = relation.getAfterId();
					//向采购合同表查询
					//获取主表
					InvCheckMain invCheckMain = new InvCheckMain();
					invCheckMain.setBillnum(afterNum);
					InvCheckMain invCheckMain2 = invCheckMainMapper.findList(invCheckMain).get(0);
					if(invCheckMain2!=null){
						//获取子表
						InvCheckDetail invCheckDetail = new InvCheckDetail();
						//按条件筛选子表
						invCheckDetail.setInvCheckMain(invCheckMain2);
						invCheckDetail.setSerialnum(afterId);
						//筛选到货子表
						List<InvCheckDetail> InvCheckDetailFindList = invCheckDetailMapper.findList(invCheckDetail);
						if(InvCheckDetailFindList!=null){
							InvCheckDetail invCheckDetail2 = InvCheckDetailFindList.get(0);
							invCheckDetail2.setInvCheckMain(invCheckMain2);
							invCheckDetailList.add(invCheckDetail2);
						}
					}
				}
			}
		}
		applyMainQuery.setInvCheckDetailList(invCheckDetailList);
		
		return applyMainQuery;
	}
	
	public Relations listToOne(List<Relations> list){
		if(list!=null){
			if(list.size()>0){
				Iterator<Relations> it = list.iterator();
				while(it.hasNext()){
					Relations relations = it.next();
					return relations;
				}
			}
		}
		return null;
	}
	
	
	
	
	public Page<ApplyMainQuery> findPage(Page<ApplyMainQuery> page, ApplyMainQuery applyMainQuery) {
		dataRuleFilter(applyMainQuery);
		applyMainQuery.setPage(page);
		List<ApplyMainQuery> resultList = new ArrayList<ApplyMainQuery>();
		List<ApplyMainQuery> findList = findChange(applyMainQuery);
		Iterator<ApplyMainQuery> it = findList.iterator();
		while(it.hasNext()){
			ApplyMainQuery next = it.next();
			String billStateFlag = next.getBillStateFlag();
			if("A".equals(billStateFlag)){
				next.setBillStateFlag("正在录入");
			}
			if("W".equals(billStateFlag)){
				next.setBillStateFlag("录入完毕");
			}
			if("E".equals(billStateFlag)){
				next.setBillStateFlag("审批通过");
			}
			if("B".equals(billStateFlag)){
				next.setBillStateFlag("审批未通过");
			}
			if("V".equals(billStateFlag)){
				next.setBillStateFlag("作废单据");
			}
			resultList.add(next);
		}
		page.setList(resultList);
		return page;
	}
	
	
	private List<ApplyMainQuery> findChange(ApplyMainQuery entity) {
		List<ApplyMainQuery> resultList = new ArrayList<ApplyMainQuery>();
		//item 中存放子表的筛选条件
		Item item = entity.getItem();
		Item item1 = itemMapper.get(item.getId());
		if(item1==null){
			item1 = new Item();
		}
		item.setCode(item1.getCode());
		item.setName(entity.getItemName());
		item.setSpecModel(entity.getItemSpecmodel());
		String billStateFlag = entity.getBillStateFlag();
		//遍历主表
		List<ApplyMainQuery> findList = applyMainQueryMapper.findList(entity);
		Iterator<ApplyMainQuery> it = findList.iterator();
		while(it.hasNext()){
			ApplyMainQuery applyMainQuery = it.next();
			//根据筛选条件获取主表下的子表
			ApplyDetailQuery applyDetailQuery = new ApplyDetailQuery(applyMainQuery);
			applyDetailQuery.setItem(item);
			applyDetailQuery.setItemName(item.getName());
			applyDetailQuery.setItemSpecmodel(item.getSpecModel());
			List<ApplyDetailQuery> ApplyDetailQueryList = applyDetailQueryMapper.findList(applyDetailQuery);
			//如果子表bu为空或长度bu为零
			if(ApplyDetailQueryList!=null&&ApplyDetailQueryList.size()!=0){
				//将A改成正在录入
				if(applyMainQuery.getBillStateFlag().equals("A")){
					applyMainQuery.setBillStateFlag("正在录入");
				}
				//将W改成正在录入
				if(applyMainQuery.getBillStateFlag().equals("W")){
					applyMainQuery.setBillStateFlag("录入完毕");
				}
				
				//将B改成正在录入
				if(applyMainQuery.getBillStateFlag().equals("B")){
					applyMainQuery.setBillStateFlag("审批未通过");
				}
				
				resultList.add(applyMainQuery);
			}
		}
		return resultList;
	}
	
	
	
	@Transactional(readOnly = false)
	public void save(ApplyMainQuery applyMainQuery) {
		super.save(applyMainQuery);
		for (ApplyDetailQuery applyDetailQuery : applyMainQuery.getApplyDetailQueryList()){
			if (applyDetailQuery.getId() == null){
				continue;
			}
			if (ApplyDetailQuery.DEL_FLAG_NORMAL.equals(applyDetailQuery.getDelFlag())){
				if (StringUtils.isBlank(applyDetailQuery.getId())){
					applyDetailQuery.setApplyMainQuery(applyMainQuery);
					applyDetailQuery.preInsert();
					applyDetailQueryMapper.insert(applyDetailQuery);
				}else{
					applyDetailQuery.preUpdate();
					applyDetailQueryMapper.update(applyDetailQuery);
				}
			}else{
				applyDetailQueryMapper.delete(applyDetailQuery);
			}
		}
	}
	
	@Transactional(readOnly = false)
	public void delete(ApplyMainQuery applyMainQuery) {
		super.delete(applyMainQuery);
		applyDetailQueryMapper.delete(new ApplyDetailQuery(applyMainQuery));
	}
	
}