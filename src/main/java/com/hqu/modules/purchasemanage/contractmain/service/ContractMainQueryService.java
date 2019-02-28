/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.purchasemanage.contractmain.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.sys.entity.DictValue;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.utils.UserUtils;
import com.jeeplus.common.utils.StringUtils;
import com.hqu.modules.purchasemanage.contractmain.entity.ContractMain;
import com.hqu.modules.purchasemanage.contracttype.entity.ContractType;
import com.hqu.modules.purchasemanage.contracttype.mapper.ContractTypeMapper;
import com.hqu.modules.purchasemanage.group.entity.GroupBuyer;
import com.hqu.modules.purchasemanage.group.mapper.GroupBuyerMapper;
import com.hqu.modules.purchasemanage.linkman.entity.LinkMan;
import com.hqu.modules.purchasemanage.linkman.entity.Paccount;
import com.hqu.modules.purchasemanage.linkman.mapper.LinkManMapper;
import com.hqu.modules.purchasemanage.paymode.entity.PayMode;
import com.hqu.modules.purchasemanage.paymode.mapper.PayModeMapper;
import com.hqu.modules.purchasemanage.purplan.entity.ItemExtend;
import com.hqu.modules.purchasemanage.purplan.entity.PurPlanDetail;
import com.hqu.modules.purchasemanage.purplan.entity.PurPlanMain;
import com.hqu.modules.purchasemanage.purplan.mapper.ItemExtendMapper;
import com.hqu.modules.purchasemanage.purplan.mapper.PurPlanDetailMapper;
import com.hqu.modules.purchasemanage.purplan.mapper.PurPlanMainMapper;
import com.hqu.modules.purchasemanage.transtype.entity.TranStype;
import com.hqu.modules.purchasemanage.transtype.mapper.TranStypeMapper;
import com.hqu.modules.qualitymanage.purinvcheckmain.entity.InvCheckDetail;
import com.hqu.modules.qualitymanage.purinvcheckmain.entity.InvCheckMain;
import com.hqu.modules.qualitymanage.purinvcheckmain.mapper.InvCheckDetailMapper;
import com.hqu.modules.qualitymanage.purinvcheckmain.mapper.InvCheckMainMapper;
import com.hqu.modules.basedata.item.entity.Item;
import com.hqu.modules.basedata.item.mapper.ItemMapper;
import com.hqu.modules.purchasemanage.applymain.entity.ApplyDetail;
import com.hqu.modules.purchasemanage.applymain.entity.ApplyMain;
import com.hqu.modules.purchasemanage.applymain.mapper.ApplyDetailMapper;
import com.hqu.modules.purchasemanage.applymain.mapper.ApplyMainMapper;
import com.hqu.modules.purchasemanage.contractmain.entity.ConRelations;
import com.hqu.modules.purchasemanage.contractmain.entity.ContractDetail;
import com.hqu.modules.purchasemanage.contractmain.mapper.ConRelationsMapper;
import com.hqu.modules.purchasemanage.contractmain.mapper.ContractDetailCreateMapper;
import com.hqu.modules.purchasemanage.contractmain.mapper.ContractDetailMapper;
import com.hqu.modules.purchasemanage.contractmain.mapper.ContractMainCreateMapper;
import com.hqu.modules.purchasemanage.contractmain.mapper.ContractMainMapper;

/**
 * 采购合同管理Service
 * @author ltq
 * @version 2018-04-28
 */
@Service
@Transactional(readOnly = true)
public class ContractMainQueryService extends CrudService<ContractMainCreateMapper, ContractMain> {
	@Autowired
	private ContractMainCreateMapper contractMainMapper;
	@Autowired
	private ContractDetailCreateMapper contractDetailMapper;
	@Autowired
	private PayModeMapper payModeMapper;
	@Autowired
	private TranStypeMapper tranStypeMapper;
	@Autowired
	private ContractTypeMapper  contractTypeMapper;
	@Autowired
	private PurPlanDetailMapper purPlanDetailMapper;
	@Autowired
	private GroupBuyerMapper groupBuyerMapper;
	@Autowired
	private ItemExtendMapper itemExtendMapper;
	@Autowired
	private PurPlanMainMapper purPlanMainMapper;
	@Autowired
	private ItemMapper itemMapper;
	@Autowired
	private ConRelationsMapper conRelationsMapper;
	@Autowired
	private ApplyMainMapper applyMainMapper;
	@Autowired
	private ApplyDetailMapper applyDetailMapper;
	@Autowired
	private InvCheckDetailMapper invCheckDetailMapper;
	@Autowired
	private InvCheckMainMapper invCheckMainMapper;
	@Autowired
	private LinkManMapper linkManMapper;
	
	public ContractMain get(String id) {
		ContractMain contractMain = super.get(id);
		contractMain.setContractDetailList(contractDetailMapper.findList(new ContractDetail(contractMain)));
		return contractMain;
	}

//	public void writeIsPrint(ContractMain contractMain){
////		ContractMain contractMain =new ContractMain();
////		contractMain.setId(id);
//		contractMainMapper.writeIsPrint(contractMain);
//
//	}

	public List<ContractMain> findList(ContractMain contractMain) {
		return super.findList(contractMain);
	}
	
	public List<PurPlanDetail> findPurPlanDetailList(PurPlanDetail purPlanDetail) {
		return purPlanDetailMapper.findList(purPlanDetail);
	}
	public List<LinkMan> getLinkManList(String id) {
		Paccount paccount =new Paccount();
		paccount.setId(id);
		paccount.setLinkManList(linkManMapper.findList(new LinkMan(paccount)));
		return paccount.getLinkManList();
	}
	
	public Page<ContractMain> findPage(Page<ContractMain> page, ContractMain contractMain) {
		Item item=contractMain.getItem();
		if(item!=null&&!("".equals(item.getId()))){
			page=findPageService(page,contractMain);
		}else if(contractMain.getItemName()!=null&&!("".equals(contractMain.getItemName()))){
			page=findPageService(page,contractMain);
		}else if(contractMain.getItemModel()!=null&&!("".equals(contractMain.getItemModel()))){
			page=findPageService(page,contractMain);
		}else if(contractMain.getPlanBillNum()!=null&&!("".equals(contractMain.getPlanBillNum()))){//计划单号不为空
	 		//page=super.findPage(page, contractMain);
			 contractMain.setOrderBy(page.getOrderBy());
			 List<ContractMain> conList=contractMainMapper.findPageNew(contractMain);
			//添加满足计划单号搜索的查询结果
	         List<String> newrelationStr= getConBillNumByPlan(contractMain.getPlanBillNum()); //存储满足计划单号所对应的合同编号	         
	         List <ContractMain> resultList=new  ArrayList <ContractMain>();
	         for(ContractMain attr:conList){				       
			    	   //计划单号的搜索条件不为空
			    	   if(newrelationStr.size()>0){
			    		    //满足计划单号搜索所筛选的合同编号
			    		   if(newrelationStr.contains(attr.getBillNum())) 
			    			   resultList.add(attr);			    	  
			           }
			 }
	         page.setList(resultList);
	         page.setCount(resultList.size());
		}else{//物料相关条件和计划单号的搜索条件为空
			List<ContractMain> resultList=contractMainMapper.findPageNew(contractMain);
			page.setList(resultList);
	        page.setCount(resultList.size());
		}
		return page;
	}
	//物料和合同主表检索查询
	public Page<ContractMain> findPageService(Page<ContractMain> page, ContractMain contractMain){
		//搜索框中的物料信息不为空
		ContractDetail contractDetail=new ContractDetail();
		contractDetail.setItem(contractMain.getItem());
		contractDetail.setItemName(contractMain.getItemName());
		contractDetail.setItemModel(contractMain.getItemModel());	
		//通过物料信息查询子表的数据，得到子表中物料的模糊查询结果
		List<ContractDetail> contractDetailList=contractDetailMapper.findList(contractDetail);
		Iterator<ContractDetail> it = contractDetailList.iterator();
		//得到所有子表查询的外键（主表字段的billNum）
		List <String> idList = new  ArrayList <String>(); 
		while(it.hasNext()){
			ContractDetail contrDetail=it.next();	
		    String contractMainBillNum=contrDetail.getContractMain().getBillNum();
		    idList.add(contractMainBillNum);
		}
		//主表contractMain id去重
		List<String> newList = new  ArrayList<String>(); 
		Set<String> set = new  HashSet<String>(); 
         set.addAll(idList);
         newList.addAll(set);
        //依据主表的id查询，并
         List <ContractMain> resultList=new  ArrayList <ContractMain>();
         
         //只能查看是制单人本人的合同
 	//	 User user=UserUtils.getUser();
 	// 	 contractMain.setUser(user);
 		 Page<ContractMain> page1=super.findPage(page, contractMain);
         //通过检索框中主表的输入信息模糊检索主表
         List<ContractMain> pageList=page1.getList();
        
         //添加满足计划单号搜索的查询结果
         if(contractMain.getPlanBillNum()!=null&&!("".equals(contractMain.getPlanBillNum()))){//计划单号不为空
        	 List<String> newrelationStr= getConBillNumByPlan(contractMain.getPlanBillNum()); //存储满足计划单号所对应的合同编号
                 if(newrelationStr.size()>0){
                	//添加满足物料、计划单号和主表项的查询结果
                     for(ContractMain attr:pageList){	
            		       if(newList.contains(attr.getBillNum())&&newrelationStr.contains(attr.getBillNum())){
            		    	   resultList.add(attr);
            		       }	        	 
            		 }
                 }
         }else{//计划单号为空
        	//添加满足物料和主表项的查询结果
             for(ContractMain attr:pageList){	
    		       if(newList.contains(attr.getBillNum())){
    		    	   resultList.add(attr);
    		       }	        	 
    		 }
         }
         		
         //将查询结果返回
         page.setList(resultList);
         page.setCount(resultList.size());
         return page;  
	}
	//返回data请求时补全信息
	public Page<ContractMain> addInf(Page<ContractMain> page){
		List<ContractMain> contrList=new ArrayList<ContractMain>();

		Iterator<ContractMain> it = page.getList().iterator();
		while(it.hasNext()){
			   ContractMain contMain=it.next();	
			   if(contMain==null||"".equals(contMain))
				   continue;
			   else{
			   //补全制单人信息			
			    User makerEmp=UserUtils.get(contMain.getUser().getId());
			    contMain.setUser(makerEmp);
			    //补全采购人信息
			  //  String  buyerId=contMain.getGroupBuyer().getUser().getId();
			    GroupBuyer  buyer=contMain.getGroupBuyer();
			    GroupBuyer groupBuyer=groupBuyerMapper.get(buyer);
			//    User  buyer=UserUtils.get(buyerId);
			//    GroupBuyer groupBuyer=new GroupBuyer();

			//	groupBuyer.setUser(buyer);
				contMain.setGroupBuyer(groupBuyer);
				contrList.add(contMain);
				}
		}	
		page.setList(contrList);
		return page;		
	}
	//返回满足模糊搜索的计划单号所生成的所有合同编号
	public List<String> getConBillNumByPlan(String planBillNum){
		//添加满足计划单号搜索的查询结果
        List<String> newrelationStr= new  ArrayList<String>(); //存储满足计划单号所对应的合同编号
        if(planBillNum!=null&&!("".equals(planBillNum))){
       	//计划单号模糊查找找到对应的合同单号
			ConRelations conRelations=new ConRelations();
			//conRelations.setState("A");
			conRelations.setType("PC");
			conRelations.setFrontNum(planBillNum);
			List<ConRelations> conRelationsList=conRelationsMapper.findList(conRelations);
			Iterator<ConRelations> conRelationsIt = conRelationsList.iterator();
			//关系表中得到所有满足搜索条件的计划生成的合同
			List <String> relationStr = new  ArrayList <String>(); 
			while(conRelationsIt.hasNext()){
				ConRelations conRel=conRelationsIt.next();	
			    String contMainBillNum=conRel.getAfterNum();
			    relationStr.add(contMainBillNum);
			}
			//合同单号去重			
			Set<String> setR = new  HashSet<String>(); 
			setR.addAll(relationStr);
			newrelationStr.addAll(setR);
 	     }
        return newrelationStr;
	}
	
//form请求时为ContractMain对象补全信息
public ContractMain addFormContractMainInf(ContractMain contractMain){
		
	String billNum=contractMain.getBillNum();
	//合同录入时生成合同号
	if(billNum==null||"".equals(billNum)){
		Date date = new Date();
		//转换提日期输出格式
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		String strDate=dateFormat.format(date);
	    String sources = "0123456789"; // 加上一些字母，就可以生成pc站的验证码了  
	        Random rand = new Random();  
	        StringBuffer flag = new StringBuffer();  
	        for (int j = 0; j < 4; j++)   
	        {  
	            flag.append(sources.charAt(rand.nextInt(9)) + "");  
	        }  
	     String billN="con"+strDate+flag.toString();   
	     contractMain.setBillNum(billN);
	}
	User user=UserUtils.getUser();
	User maker=contractMain.getUser();
	//合同录入时自动生成制单人
	if(maker==null||"".equals(maker.getId())){
		contractMain.setUser(user);
		contractMain.setMakeEmpname(user.getName());
	}else{
		//修改合同录入时补全制单人信息
	   User makerEmp=UserUtils.get(contractMain.getUser().getId());
	   contractMain.setUser(makerEmp);
	}
	//修改合同录入时补全采购员信息
	if(contractMain.getGroupBuyer()!=null){	
		GroupBuyer buyer=contractMain.getGroupBuyer();
		if(buyer==null||"".equals(buyer.getId())) {							  
		}else{				
		    GroupBuyer groupBuyer=groupBuyerMapper.get(buyer);
			contractMain.setGroupBuyer(groupBuyer);
		}	
	}
	
	    //补全供应商信息
		if(contractMain.getAccount()!=null&&contractMain.getAccount().getId()!=null){
			List<LinkMan> linkMans=getLinkManList(contractMain.getAccount().getId());
		    //查找该供应商的联系人
			for(LinkMan linkMan:linkMans){
				if(linkMan.getState().equals("生效状态")){
					//将供应商联系人添加进合同对象的accountLinkMam属性
					contractMain.setAccountLinkMam(linkMan.getLinkName());
				}
			}
		}
	
	//设置成正在录入和修改
	String billStateFlag=contractMain.getBillStateFlag();	
	if(billStateFlag==null||"".equals(billStateFlag)){
		contractMain.setBillStateFlag("A");
	}
	
	
  List<ContractDetail> contractDetailList=contractMain.getContractDetailList();
  //生成的合同查找计划信息、需求信息
  if("C".equals(contractMain.getContrState())){		
	//合同信息对应的计划信息
	List <PurPlanDetail> purPlanDetailList = new  ArrayList <PurPlanDetail>();	
	if(contractDetailList!=null&&contractDetailList.size()!=0){
		Iterator<ContractDetail> itConDetails = contractDetailList.iterator();
		while(itConDetails.hasNext()){
			ContractDetail conDetail=itConDetails.next();
			//查找合同子表对应的计划单号
			ConRelations conRelations=new ConRelations();
			//conRelations.setState("A");
			conRelations.setType("PC");
			conRelations.setAfterNum(conDetail.getContractMain().getBillNum());
			conRelations.setAfterId(conDetail.getSerialNum());
			conRelations=conRelationsMapper.getConRelations(conRelations);
			//设置合同子表对应的计划单号和计划序号
			//conDetail.setPlanBillNum(conRelations.getFrontNum());
			//conDetail.setPlanSerialNum(conRelations.getFrontId());
			//设置合同签订量
			//conDetail.setFrontItemQty(conDetail.getItemQty());
			if(conRelations!=null){
			    //查找合同对应的计划信息
				PurPlanDetail purPlanDetail=new PurPlanDetail();
				PurPlanMain purPlanMain=new PurPlanMain();
				purPlanMain.setBillNum(conRelations.getFrontNum());
			    purPlanMain=purPlanMainMapper.getPurPlanMain(purPlanMain);
			    //单据类型转换
			    if("M".equals(purPlanMain.getBillType())) 
			    	purPlanMain.setBillType("录入");
			    else 
			    	purPlanMain.setBillType("生成");
				purPlanDetail.setBillNum(purPlanMain);
				purPlanDetail.setSerialNum(conRelations.getFrontId());
				purPlanDetail=purPlanDetailMapper.getPurPlanDetail(purPlanDetail);
				if(purPlanDetail!=null){
			     //填充计划子表的数据			
				 Item  item=itemMapper.get(purPlanDetail.getItemCode());
				 ItemExtend itemExtend=new ItemExtend();
	    		 itemExtend.setCode(item.getCode());
	    		 itemExtend.setId(item.getId());
	    		 //填写物料材质
	    		 purPlanDetail.setItemTexture(item.getTexture());
	           	 //填写物料编号
	    		 purPlanDetail.setItemCode(itemExtend);
	           	 //填入计划单号
	    		 purPlanDetail.setBillNum(purPlanMain);
				 purPlanDetailList.add(purPlanDetail);
				}
			}
		}		
		contractMain.setPurPlanDetailList(purPlanDetailList);
	}
	
 //依据计划信息查询需求信息
	List <ApplyDetail> applyDetailList = new  ArrayList <ApplyDetail>();//计划信息对应的需求信息,录入的计划无需求信息
	if(purPlanDetailList!=null&&purPlanDetailList.size()!=0){
		Iterator<PurPlanDetail> itplanDetails = purPlanDetailList.iterator();
		while(itplanDetails.hasNext()){
			PurPlanDetail planDetail=itplanDetails.next();
			//依据计划子表中存储的主表的id查找主表编号
			//PurPlanMain purPlanMain=new PurPlanMain();
			//purPlanMain.setId(planDetail.getBillNum().getBillNum());
			//purPlanMain=purPlanMainMapper.get(planDetail.getBillNum());
			//System.out.println("purPlanMain++++++++++++++++++++++++++"+purPlanMain);
			//查找合同子表对应的计划单号
			ConRelations conRelations=new ConRelations();
			//conRelations.setState("A");
			conRelations.setType("AP");
			conRelations.setAfterNum(planDetail.getBillNum().getBillNum());
			conRelations.setAfterId(planDetail.getSerialNum());
			conRelations=conRelationsMapper.getConRelations(conRelations);
			if(conRelations!=null){//该计划为生成的的，若录入则没有对应的采购需求
				//依据关系表中采购需求单号和序号，查找采购需求
				ApplyMain applyMain=new ApplyMain();
				applyMain.setBillNum(conRelations.getFrontNum());
                List<ApplyMain> applyMains=applyMainMapper.findList(applyMain);
                if(applyMains.size()>0){
                	
	                applyMain=applyMains.get(0);
	                ApplyDetail applyDetail=new ApplyDetail();
	                applyDetail.setApplyMain(applyMain);
	                applyDetail.setSerialNum(conRelations.getFrontId());
	                List<ApplyDetail> applyDetails=applyDetailMapper.findList(applyDetail);
	                if(applyDetails.size()>0){
	                	
		                applyDetail=applyDetails.get(0);                
		                applyDetail.setApplyMain(applyMain);
		                applyDetailList.add(applyDetail);
	                }
			    }
            }
		}
	}
	contractMain.setApplyDetailList(applyDetailList);
  }	//end if("C".equals(contractMain.getContrState()))
 //依据合同信息查询到货信息
	List <InvCheckDetail> invCheckDetailList = new  ArrayList <InvCheckDetail>();//计划信息对应的需求信息,录入的计划无需求信息
	if(contractDetailList!=null&&contractDetailList.size()!=0){
		Iterator<ContractDetail> itConDetails = contractDetailList.iterator();
		while(itConDetails.hasNext()){
			ContractDetail conDetail=itConDetails.next();
			//关系表中查找由合同生成的到货
			ConRelations conRelations=new ConRelations();
			conRelations.setType("CD");
			conRelations.setFrontNum(conDetail.getContractMain().getBillNum());
			conRelations.setFrontId(conDetail.getSerialNum());
			//conRelations=conRelationsMapper.getConRelations(conRelations);
		    List<ConRelations>	conRelationsList=conRelationsMapper.findList(conRelations);
			if(conRelations!=null&&conRelationsList.size()>0)//该合同子表存在到货信息
				for(ConRelations conRel:conRelationsList){
					//conDetail.setInvCheckBillNum(conRelations.getAfterNum());
				    //}/*else{			
					//查找合同子表生成的对应的到货子表
					InvCheckMain invCheckMain=new InvCheckMain();
					invCheckMain.setBillnum(conRel.getAfterNum());
					List<InvCheckMain> invCheckMains=invCheckMainMapper.findList(invCheckMain);
					if(invCheckMains.size()>0){
						invCheckMain=invCheckMains.get(0);					
						InvCheckDetail invCheckDetail=new InvCheckDetail();
						invCheckDetail.setInvCheckMain(invCheckMain);
						//System.out.println("invCheckMain.getBillnum()+++++++++++++++++++++++++++++"+invCheckMain.getBillnum());
						invCheckDetail.setSerialnum(conRel.getAfterId());
						List<InvCheckDetail> invCheckDetails=invCheckDetailMapper.findList(invCheckDetail);
						if(invCheckDetails.size()>0){
							invCheckDetail=invCheckDetails.get(0);
							//设置到货的合同单号
							String afterNum=conDetail.getContractMain().getBillNum();
							invCheckDetail.setConBillNum(afterNum);
							//设置到货日期
							invCheckDetail.setArriveDate(invCheckMain.getArriveDate());
							//设置到货状态
							invCheckDetail.setBillStateFlag(invCheckMain.getBillStateFlag());
							//查找生成该合同的计划
							ConRelations relations=new ConRelations();
							//conRelations.setState("A");
							relations.setType("PC");
							
							relations.setAfterNum(afterNum);
							relations.setAfterId(conDetail.getSerialNum());
							relations=conRelationsMapper.getConRelations(relations);
							//设置由合同生成到货时，合同所对应的计划单号
							if(relations!=null){ 
								conDetail.setPlanBillNum(relations.getFrontNum());
								invCheckDetail.setPlanBillNum(conDetail.getPlanBillNum());
								
								invCheckDetailList.add(invCheckDetail);
							}
						}
					}
				}						
		}
	}
	    contractMain.setInvCheckDetailList(invCheckDetailList);	
		return contractMain;		
}








//获取支付方式列表
public List<DictValue> getPayList(){
	        //支付方式名称下拉框选项
			List<DictValue> payList=new ArrayList<DictValue>(); 
			//获取所有的支付方式
			PayMode payMode=new PayMode();
			List<PayMode> payModes=payModeMapper.findList(payMode);
			Iterator<PayMode> it = payModes.iterator();
			 while(it.hasNext()){
				 PayMode pay=it.next(); 
				 DictValue dictValue=new DictValue();
				 dictValue.setValue(pay.getId());
				 dictValue.setLabel(pay.getPaymodename());
				 payList.add(dictValue);			
			 }
	return payList;	
}
//获取运输方式列表
public List<DictValue> getTransList(){
	//运输方式名称下拉框选项
			List<DictValue> transList=new ArrayList<DictValue>(); 
			TranStype tranStype=new TranStype();
			List<TranStype> transTypes=tranStypeMapper.findList(tranStype);
			Iterator<TranStype> transIt = transTypes.iterator();
			 while(transIt.hasNext()){
				 TranStype trans=transIt.next(); 
				 DictValue dictValue_Trans=new DictValue();
				 dictValue_Trans.setValue(trans.getId());
				 dictValue_Trans.setLabel(trans.getTranstypename());
				 transList.add(dictValue_Trans);			
			 }
	return transList;	
}

//依据采购员id筛选采购计划
public Page<PurPlanDetail> selectPlan(Page<PurPlanDetail> page, PurPlanDetail purPlanDetail) {
	     List<PurPlanDetail> purPlanDetails= purPlanDetailMapper.findList(purPlanDetail);
		 Iterator<PurPlanDetail> purPlanIt = purPlanDetails.iterator();
        
		 while(purPlanIt.hasNext()){
			 //添加物料数据
        	 PurPlanDetail purPlan=purPlanIt.next();
        	 ItemExtend item= itemExtendMapper.get(purPlan.getItemCode());
        	 //填写物料材质
        	 purPlan.setItemTexture(item.getTexture());
        	 //填写物料编号
        	 purPlan.setItemCode(item);
        	 PurPlanMain planMain= purPlanMainMapper.get(purPlan.getBillNum().getId());
        	 //填入计划单号
        	 purPlan.setBillNum(planMain);
        	 
       	 }
		 page.setList(purPlanDetails);
	  return page;
}


	@Transactional(readOnly = false)
	public void save(ContractMain contractMain) {
		String billNum=contractMain.getBillNum();
		if(contractMain.getPayMode()!=null&&!("".equals(contractMain.getPayMode().getId()))){
			PayMode payMode=payModeMapper.get(contractMain.getPayMode());
			contractMain.setPayMode(payMode);
			contractMain.setPaymodeName(payMode.getPaymodename());
		}
		if(contractMain.getTransType()!=null&&!("".equals(contractMain.getTransType().getId()))){
			TranStype transType=tranStypeMapper.get(contractMain.getTransType());		
			contractMain.setTransType(transType);
			contractMain.setTranmodeName(transType.getTranstypename());
		}
		if(contractMain.getContractType()!=null&&!("".equals(contractMain.getContractType().getId()))){
			ContractType contractType=contractTypeMapper.get(contractMain.getContractType().getId());
			contractMain.setContypeName(contractType.getContypename());
			contractMain.setContractType(contractType);
		}
		
		super.save(contractMain);
		for (ContractDetail contractDetail : contractMain.getContractDetailList()){
			if (contractDetail.getId() == null){
				continue;
			}
			if (ContractDetail.DEL_FLAG_NORMAL.equals(contractDetail.getDelFlag())){
				if (StringUtils.isBlank(contractDetail.getId())){
					contractDetail.setContractMain(contractMain);
					contractDetail.preInsert();
					contractDetailMapper.insert(contractDetail);
				}else{
					contractDetail.preUpdate();
					contractMain.setBillNum(billNum);
					contractDetail.setContractMain(contractMain);
					contractDetailMapper.update(contractDetail);
				}
			}else{
				contractDetailMapper.delete(contractDetail);
			}
		}
	}
	
	@Transactional(readOnly = false)
	public void delete(ContractMain contractMain) {
		super.delete(contractMain);
		contractDetailMapper.delete(new ContractDetail(contractMain));
	}
	
	@Transactional(readOnly = false)
    public Page<ContractDetail> findDetailPageService(Page<ContractDetail> page, ContractMain contractMain){
		//Page<ContractMain> pag= super.findPage(page, contractMain);
		ContractMain con=new ContractMain();
		con.setBillNum(contractMain.getBillNum());
		ContractMain conMain= contractMainMapper.get(con);
		
		List<ContractDetail>  contractDetails= conMain.getContractDetailList();
		
		page.setList(contractDetails);
		page.setCount(contractDetails.size());
		return page;
	}	
}