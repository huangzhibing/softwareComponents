/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.purchasemanage.purplan.service;

import java.util.*;


import com.google.common.collect.Maps;
import com.hqu.modules.basedata.account.entity.Account;
import com.hqu.modules.basedata.item.entity.Item;
import com.hqu.modules.purchasemanage.group.mapper.GroupBuyerMapper;
import com.hqu.modules.purchasemanage.purplan.entity.PlanRelations;
import com.hqu.modules.purchasemanage.rollplannewquery.entity.RollPlanNew;
import com.hqu.modules.purchasemanage.rollplannewquery.service.RollPlanNewService;
import com.hqu.modules.purchasemanage.supprice.entity.PurSupPrice;
import com.hqu.modules.purchasemanage.supprice.entity.PurSupPriceDetail;
import com.hqu.modules.purchasemanage.supprice.mapper.PurSupPriceDetailMapper;
import com.jeeplus.modules.act.service.ActTaskService;
import com.jeeplus.modules.act.utils.ActUtils;
import org.activiti.engine.IdentityService;
import org.apache.poi.util.SystemOutLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.common.utils.StringUtils;
import com.hqu.modules.purchasemanage.purplan.entity.PurPlanMain;
import com.hqu.modules.purchasemanage.purplan.mapper.PurPlanMainMapper;

import com.hqu.modules.purchasemanage.purplan.entity.ItemExtend;
import com.hqu.modules.purchasemanage.purplan.entity.PurPlanDetail;
import com.hqu.modules.purchasemanage.purplan.mapper.ItemExtendMapper;
import com.hqu.modules.purchasemanage.purplan.mapper.PurPlanDetailMapper;



/**
 * 采购计划Service
 * @author yangxianbang
 * @version 2018-06-07
 */
@Service
@Transactional(readOnly = true)
public class PurPlanMainService extends CrudService<PurPlanMainMapper, PurPlanMain> {

	@Autowired
	private PurPlanDetailMapper purPlanDetailMapper;
	@Autowired
	private ItemExtendMapper itemExtendMapper;
    @Autowired
	GroupBuyerMapper buyerMapper;


    @Autowired
	PlanRelationsService planRelationsService;
	@Autowired
	RollPlanNewService rollPlanNewService;

	@Autowired
	private PurSupPriceDetailMapper purSupPriceDetailMapper;

	@Autowired
	IdentityService identityService;
	@Autowired
	ActTaskService actTaskService;
	//结构{对应流程定义的表示，业务表名} 采购计划制定模型
	public static final String[] PUR_PLAN_AUDIT = new String[]{"pur_plan", "pur_planmain"};
    //结构{对应流程定义的表示，业务表名} 采购计划录入模型
    public static final String[] PUR_PLAN_AUDIT_INPUT = new String[]{"pur_plan_input", "pur_planmain"};




	public PurPlanMain get(String id) {
		PurPlanMain purPlanMain = super.get(id);
		List<PurPlanDetail> detaillist=purPlanDetailMapper.findList(new PurPlanDetail(purPlanMain));
	/*	for(PurPlanDetail d : detaillist){
			if(d.getBuyerId()!=null) {
				d.setBuyerId(buyerMapper.get(d.getBuyerId().getId()));
			}
		}*/
		purPlanMain.setPurPlanDetailList(detaillist);
		return purPlanMain;
	}
	
	public Page<ItemExtend> findItemPage(Page<ItemExtend> page, ItemExtend itemExtend){
		dataRuleFilter(itemExtend);
		List<ItemExtend> itemList=itemExtendMapper.findList(itemExtend);
		page.setCount(itemList.size());
		page.setList(itemExtendMapper.findList(itemExtend));
		return page;
	}
	
	public List<PurPlanMain> findList(PurPlanMain purPlanMain) {
		return super.findList(purPlanMain);
	}

	/**
	 * 查找所有状态的单据
	 * @param page 分页对象
	 * @param purPlanMain
	 * @return
	 */
	public Page<PurPlanMain> findPage(Page<PurPlanMain> page, PurPlanMain purPlanMain) {
		purPlanMain.setPage(page);
		if(purPlanMain.getItemCode()==null){//从外部直接调用data接口时使用
			return super.findPage(page,purPlanMain);
		}
		else if("".equals(purPlanMain.getItemCode().getId())&&"".equals(purPlanMain.getSpecModel())&&"".equals(purPlanMain.getItemName())) {
			return super.findPage(page, purPlanMain);
		}
		else {
			List plans=findDetailList( purPlanMain);
			plans=fixList(page,plans);
			page.setCount(plans.size());
			page.setList(plans);
			return page;
		}
	}

	/**
	 * 查找状态为录入A和审核不通过B的单据
	 * @param page
	 * @param purPlanMain
	 * @return
	 */
	public Page<PurPlanMain> findABPage(Page<PurPlanMain> page, PurPlanMain purPlanMain) {
		purPlanMain.setPage(page);
		List listA=null;
		if("".equals(purPlanMain.getItemCode().getId())&&"".equals(purPlanMain.getSpecModel())&&"".equals(purPlanMain.getItemName())) {

			if("".equals(purPlanMain.getBillStateFlag())) {    //没有根据单据状态进行筛选时，把状态A B的单据都找出来
				purPlanMain.setBillStateFlag("A");
				listA = findList(purPlanMain);
				purPlanMain.setBillStateFlag("B");
				List listB = findList(purPlanMain);
				listA.addAll(listB);

			}else{
				listA = findList(purPlanMain);
			}
			page.setCount(listA.size());
			listA = fixList(page, listA);
			page.setList(listA);
		}
		else {
			if("".equals(purPlanMain.getBillStateFlag())) {   //没有根据单据状态进行筛选时，把状态A B的单据都找出来
				purPlanMain.setBillStateFlag("A");
				listA = findDetailList(purPlanMain);
				purPlanMain.setBillStateFlag("B");
				List listB = findDetailList(purPlanMain);
				listA.addAll(listB);
			}else {
				listA=findDetailList(purPlanMain);
			}
			page.setCount(listA.size());
			listA = fixList(page, listA);
			page.setList(listA);

		}
		return page;
	}

	/**
	 * 修正不能分页问题
	 * @param page
	 * @param listA
	 * @return
	 */
	private List fixList(Page<PurPlanMain> page, List listA) {
		page.setCount(listA.size());
		page.setList(listA);
		if(!"-1".equals(page.getPageSize())) {
			listA = listA.subList(page.getFirstResult(), (int) (page.getPageNo() * page.getPageSize() <= page.getCount() ? page.getPageNo() * page.getPageSize() : page.getCount()));
		}
		return listA;
	}

	/***
	 * 关联子表字段查询
	 * @param purPlanMain 带有子表查询属性的查询实体
	 * @return 符合查询条件的List<PurPlanMain></>
	 */
	private List<PurPlanMain> findDetailList( PurPlanMain purPlanMain) {
		PurPlanDetail detail=new PurPlanDetail();
		ItemExtend item=new ItemExtend();
		Set<String> planIds=new HashSet<String>();
		if(purPlanMain.getItemCode()!=null) {
			item.setId(purPlanMain.getItemCode().getId());
		}
		item.setName(purPlanMain.getItemName());
		item.setSpecModel(purPlanMain.getSpecModel());
			
		detail.setItemCode(item);
		dataRuleFilter(detail);
		List<PurPlanDetail> details= purPlanDetailMapper.findList(detail);//查出所有符合查询的子项
		for(PurPlanDetail d : details) {
			if(d.getBillNum()!=null) {
				planIds.add(d.getBillNum().getId());
			}
		}
		
		List<PurPlanMain> plans=super.findList(purPlanMain);
		Iterator<PurPlanMain> plansIt=plans.iterator();
		while(plansIt.hasNext()) {
			PurPlanMain plan=plansIt.next();
			if(!planIds.contains(plan.getId())) {
				plansIt.remove();
			}
		}
		

		return plans;
	}


	/**
	 * 物料价格查询
	 * @param itemId
	 * @param itemNum
	 * @param accountId
	 * @return
	 */
	public List<PurSupPriceDetail> getPurSupPriceList(String itemId, Double itemNum, String accountId) {
		List<PurSupPriceDetail> purSupPriceDetails=new ArrayList<PurSupPriceDetail>();
		PurSupPriceDetail purSupPriceDetail=new PurSupPriceDetail();
		Item item=new Item();
		item.setId(itemId);
		purSupPriceDetail.setItem(item);
		purSupPriceDetail.setMinQty(itemNum);
		purSupPriceDetail.setMaxQty(itemNum);
		Date curDate=new Date();
		purSupPriceDetail.setStartDate(curDate);
		purSupPriceDetail.setEndDate(curDate);
		Account account=new Account();
		account.setId(accountId);
		PurSupPrice purSupPrice=new PurSupPrice();
		purSupPrice.setAccount(account);
		purSupPriceDetail.setAccount(purSupPrice);
		purSupPriceDetails=purSupPriceDetailMapper.findPriceList(purSupPriceDetail);
		return purSupPriceDetails;
	}

	
	@Transactional(readOnly = false)
	public void save(PurPlanMain purPlanMain) {
		super.save(purPlanMain);
		//子表序号
		int detailIndex=0;
		for (PurPlanDetail purPlanDetail : purPlanMain.getPurPlanDetailList()){
			if (purPlanDetail.getId() == null){
				continue;
			}
			if (PurPlanDetail.DEL_FLAG_NORMAL.equals(purPlanDetail.getDelFlag())){  //删除标志为0
				if (StringUtils.isBlank(purPlanDetail.getId())){             //主键ID为空 增加
					purPlanDetail.setBillNum(purPlanMain);
					purPlanDetail.setSerialNum(++detailIndex);  //给子表序号赋值
					purPlanDetail.preInsert();
					purPlanDetailMapper.insert(purPlanDetail);

					dealRelationAndPlanNum(purPlanMain, purPlanDetail,true,0);

				}else{                                              //ID不為空 修改
					purPlanDetail.preUpdate();
					dealRelationAndPlanNum(purPlanMain, purPlanDetail,false,++detailIndex);//注意在修改purPlanDetail前调用 （fixed BUG）
					purPlanDetail.setSerialNum(detailIndex); //给子表序号赋值
					purPlanDetailMapper.update(purPlanDetail);
				}

			}else{
				//此处可能存在有关于回写计划号和生成关系的BUG
				//重置对应滚动记录计划号 版本2
				RollPlanNew rollPlanNew=new RollPlanNew();
				rollPlanNew.setSerialNum(purPlanDetail.getApplySerialNum());
				rollPlanNew.setBillNum(purPlanDetail.getApplyBillNum());
				//RollPlanNew rollPlanNew=rollPlanNewService.get(purPlanDetail.getApplyId());//为修改时，由于从数据库读出来时applyID为空，故此处要做填充处理
				List<RollPlanNew> rollPlanNews =rollPlanNewService.findList(rollPlanNew);
				if(rollPlanNews!=null&&rollPlanNews.size()>0)
				{
					rollPlanNew=rollPlanNews.get(0);
					rollPlanNew.setPlanNum("");
					rollPlanNew.setOpFlag("0"); //设置加入采购计划标志 0未加入/1已加入/2作废
					rollPlanNewService.save(rollPlanNew);
				}else {
					System.out.println("================================回写滚动计划的计划号时，没有找到需求单号为："+purPlanDetail.getApplyBillNum()+"，序号为："+purPlanDetail.getApplySerialNum()+"对应的的记录！===========================");
				}
				deleteRelation(purPlanMain.getBillNum(),1,purPlanDetail.getSerialNum());
				purPlanDetailMapper.delete(purPlanDetail);
			}
		}
	}

	/**
	 * 处理生成关系和回写滚动计划  （修改和增加时用）
	 * @param purPlanMain
	 * @param purPlanDetail
	 * @param isAdd true/增加 false/修改
	 * @param detailIndex 修改时此参数不能为空
	 */
	private void dealRelationAndPlanNum(PurPlanMain purPlanMain, PurPlanDetail purPlanDetail,boolean isAdd,int detailIndex) {
		if(purPlanDetail.getApplyBillNum()!=null&&!(purPlanDetail.getApplyBillNum().equals(""))){//仅在子表为新增时才记录关系和回写计划号，此外还要判断单前单据是否为 计划制定

			if(isAdd) {
				//记录生成关系
				PlanRelations planRelations = new PlanRelations();
				planRelations.setAfterNum(purPlanMain.getBillNum());
				planRelations.setFrontNum(purPlanDetail.getApplyBillNum());
				planRelations.setAfterId(purPlanDetail.getSerialNum());
				planRelations.setFrontId(purPlanDetail.getApplySerialNum());
				planRelations.setType("AP");
				planRelationsService.save(planRelations);
				//回写计划号；
				RollPlanNew rollPlanNew=rollPlanNewService.get(purPlanDetail.getApplyId());//增加时applyID不会为空
				rollPlanNew.setPlanNum(purPlanMain.getBillNum());
				rollPlanNew.setOpFlag("1"); //设置加入采购计划标志 0未加入/1已加入/2作废
				rollPlanNewService.save(rollPlanNew);
			}else{               //修改时不处理滚动计划号
				//修改生成关系 的相关序号
				PlanRelations planRelations = new PlanRelations();
				planRelations.setAfterNum(purPlanMain.getBillNum());
				planRelations.setFrontNum(purPlanDetail.getApplyBillNum());
				planRelations.setAfterId(purPlanDetail.getSerialNum());//修改前的生成关系中的序号
				List<PlanRelations> planRelationsList=planRelationsService.findList(planRelations);
				if(planRelationsList!=null&&planRelationsList.size()>0){
					planRelations=planRelationsList.get(0);
					planRelations.setAfterId(detailIndex); //修改后生成关系中的序号
					planRelationsService.save(planRelations);
				}else {
					System.out.println("================================修改生成关系时，没有找到生成关系的需求单号为："+purPlanDetail.getApplyBillNum()+"，序号为："+purPlanDetail.getApplySerialNum()+"对应的的记录！===========================");

				}
				//回写计划号；
			/*	RollPlanNew rollPlanNew=new RollPlanNew();
				rollPlanNew.setSerialNum(purPlanDetail.getApplySerialNum());
				rollPlanNew.setBillNum(purPlanDetail.getApplyBillNum());
				//RollPlanNew rollPlanNew=rollPlanNewService.get(purPlanDetail.getApplyId());//为修改时，由于从数据库读出来时applyID为空，故此处要做填充处理
				List<RollPlanNew> rollPlanNews =rollPlanNewService.findList(rollPlanNew);
				if(rollPlanNews!=null&&rollPlanNews.size()>0)
				{
					rollPlanNew=rollPlanNews.get(0);
					rollPlanNew.setPlanNum(purPlanMain.getBillNum());
					rollPlanNew.setOpFlag("1"); //设置加入采购计划标志 0未加入/1已加入/2作废
					rollPlanNewService.save(rollPlanNew);
				}else {
					System.out.println("================================回写滚动计划的计划号时，没有找到需求单号为："+purPlanDetail.getApplyBillNum()+"，序号为："+purPlanDetail.getApplySerialNum()+"对应的的记录！===========================");
				}*/

			}


		}
	}

	/**
	 * 删除生成关系表关系  type=1时 serialNum不能为空
	 * @param billNum
	 * @param type 删除类型 1/依据生成序号和生成单据号 2/依据生成单据号
	 */
	private void deleteRelation(String billNum,int type,int serialNum) {

			//if (purPlanDetail.getApplyBillNum() != null && !(purPlanDetail.getApplyBillNum().equals(""))) {  //不管是否是计划定制，都把对应的采购计划编号相关的关系表数据清空一下

		if(type==2) {
			PlanRelations planRelations = new PlanRelations();
			planRelations.setAfterNum(billNum);
			planRelations.setType("AP");
			planRelationsService.deleteByBillNum(planRelations);
		}
		if(type==1) {

			PlanRelations planRelations = new PlanRelations();
			planRelations.setAfterNum(billNum);
			planRelations.setAfterId(serialNum);
			planRelations.setType("AP");
			planRelationsService.deleteByBillNumAndSerialNum(planRelations);

		}
			//}
	}

	@Transactional(readOnly = false)
	public void delete(PurPlanMain purPlanMain) {
		//计划号设置为空
		RollPlanNew rollPlanNew=new RollPlanNew();
		rollPlanNew.setPlanNum(purPlanMain.getBillNum());
		List<RollPlanNew> rollPlanNews=rollPlanNewService.findList(rollPlanNew);//查找这个生成关系对应的滚动计划记录
		for(RollPlanNew roll:rollPlanNews){         //把滚动计划中需求记录的计划号置为空
			roll.setPlanNum("");
			roll.setOpFlag("0"); //设置未加入计划
			rollPlanNewService.save(roll);
		}
		//删除生成关系数据
		deleteRelation(purPlanMain.getBillNum(),2,0);
		super.delete(purPlanMain);
		purPlanDetailMapper.delete(new PurPlanDetail(purPlanMain));
	}


	/**
	 * 提交操作 保存并且启动工流
	 * @param purPlanMain
	 */
	@Transactional(readOnly = false)
	public void submitSave(PurPlanMain purPlanMain) {

		/**-------------------------------业务操作----------------------**/
		save(purPlanMain);


		/**-------------工作流启动操作------------------**/

		// 申请发起
		if (StringUtils.isBlank(purPlanMain.getId())||"".equals(purPlanMain.getAct().getTaskId())||purPlanMain.getAct().getTaskId()==null){



			// 用来设置启动流程的人员ID，引擎会自动把用户ID保存到activiti:initiator中
			identityService.setAuthenticatedUserId(purPlanMain.getCurrentUser().getLoginName());
			if("C".equals(purPlanMain.getBillType())) {
                // 启动流程
                actTaskService.startProcess(PUR_PLAN_AUDIT[0], PUR_PLAN_AUDIT[1], purPlanMain.getId(), purPlanMain.getBillNum());
            }else if("M".equals(purPlanMain.getBillType())){
                // 启动流程
                actTaskService.startProcess(PUR_PLAN_AUDIT_INPUT[0], PUR_PLAN_AUDIT_INPUT[1], purPlanMain.getId(), purPlanMain.getBillNum());
            }

		}

		// 重新编辑申请
		else{


			purPlanMain.getAct().setComment(("yes".equals(purPlanMain.getAct().getFlag())?"[重申] ":"[作废] ")+purPlanMain.getAct().getComment());

			// 完成流程任务
			Map<String, Object> vars = Maps.newHashMap();
			vars.put("reApply", "yes".equals(purPlanMain.getAct().getFlag())? true : false);
			actTaskService.complete(purPlanMain.getAct().getTaskId(), purPlanMain.getAct().getProcInsId(), purPlanMain.getAct().getComment(), purPlanMain.getBillNum(), vars);
		}


	}

	/**
	 * 审核时保存业务表数据和审核数据
	 * @param purPlanMain
	 */
	@Transactional(readOnly = false)
	public void auditSave(PurPlanMain purPlanMain) {

		Map<String, Object> vars = Maps.newHashMap();
		// 对不同环节的业务逻辑进行操作
		String taskDefKey = purPlanMain.getAct().getTaskDefKey();

		// 审核环节
		if ("planAudit1".equals(taskDefKey)){
			vars.put("auditPass1", "yes".equals(purPlanMain.getAct().getFlag())? true : false);
			//vars.put("sum",purPlanMain.getPlanPriceSum());
			purPlanMain.getAct().setComment(("yes".equals(purPlanMain.getAct().getFlag())?"[同意] ":"[不同意] ")+purPlanMain.getAct().getComment());
			if("yes".equals(purPlanMain.getAct().getFlag())) {
				/*if(null!=actTaskService.getFinishedProcIns(purPlanMain.getAct().getProcInsId())) {
					purPlanMain.setBillStateFlag("E");
				}*/
			/*	if(purPlanMain.getPlanPriceSum()<10000){
					purPlanMain.setBillStateFlag("E");             //依赖于工作流模型（该工作流模型中只有两层审核）
				}*/
				purPlanMain.setBillStateFlag("C");
			}else{
				purPlanMain.setBillStateFlag("B");
			}
		}
		/*else if("planAudit2".equals(taskDefKey)){
			vars.put("auditPass1", "yes".equals(purPlanMain.getAct().getFlag())? true : false);
			//vars.put("sum",purPlanMain.getPlanPriceSum());
			purPlanMain.getAct().setComment(("yes".equals(purPlanMain.getAct().getFlag())?"[同意] ":"[不同意] ")+purPlanMain.getAct().getComment());
			if("yes".equals(purPlanMain.getAct().getFlag())) {
				//if(null!=actTaskService.getFinishedProcIns(purPlanMain.getAct().getProcInsId())) {  //依赖于工作流模型（该工作流模型中只有两层审核）
					purPlanMain.setBillStateFlag("E");                        //依赖于工作流模型（该工作流模型中只有两层审核）
				//}
			}else{
				purPlanMain.setBillStateFlag("B");
			}
		}*/
		else if ("reInput".equals(taskDefKey)){
			vars.put("reApply", "yes".equals(purPlanMain.getAct().getFlag())? true : false);
            purPlanMain.getAct().setComment(("yes".equals(purPlanMain.getAct().getFlag())?"[重申] ":"[作废] ")+purPlanMain.getAct().getComment());
			if( "yes".equals(purPlanMain.getAct().getFlag())){
				purPlanMain.setBillStateFlag("W");
			}else{
				purPlanMain.setBillStateFlag("V");//作废
			}
		}
		else if("planClose".equals(taskDefKey)){
			vars.put("billFlag",purPlanMain.getBillStateFlag());
		}
		else if("planOrder".equals(taskDefKey)){
			vars.put("billFlag",purPlanMain.getBillStateFlag());
		}
		else if ("end".equals(taskDefKey)){

		}

		// 未知环节，直接返回
		else{
			return;
		}

		// 提交流程任务
		actTaskService.complete(purPlanMain.getAct().getTaskId(), purPlanMain.getAct().getProcInsId(), purPlanMain.getAct().getComment(), vars);
		//保存业务表状态
		save(purPlanMain);
	}
}

				//重置对应滚动记录计划号 版本1
				/*
				PlanRelations planRelations=new PlanRelations();
				planRelations.setAfterId(purPlanDetail.getSerialNum());
				planRelations.setAfterNum(purPlanMain.getBillNum());
				planRelations.setType("AP");
				List<PlanRelations> relationList=planRelationsService.findList(planRelations);
				for(PlanRelations pr:relationList){
					RollPlanNew rollPlan=new RollPlanNew();
					rollPlan.setBillNum(pr.getFrontNum());
					rollPlan.setSerialNum(pr.getFrontId());
					List<RollPlanNew> rollPlanList=rollPlanNewService.findList(rollPlan);
					if(rollPlanList!=null&&rollPlanList.size()>0){
						RollPlanNew rp=rollPlanList.get(0);
						rp.setPlanNum("");
						rp.setOpFlag("0");//设置加入采购计划标志 0未加入/1已加入/2作废
						rollPlanNewService.save(rp);

					}else {
						System.out.println("================================回写滚动计划的计划号时，没有找到需求单号为："+purPlanDetail.getApplyBillNum()+"，序号为："+purPlanDetail.getApplySerialNum()+"对应的的记录！===========================");
					}
				}
				*/