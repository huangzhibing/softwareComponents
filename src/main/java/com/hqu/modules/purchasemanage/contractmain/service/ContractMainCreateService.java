/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.purchasemanage.contractmain.service;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.act.service.ActTaskService;
import com.jeeplus.modules.act.utils.ActUtils;
import com.jeeplus.modules.oa.entity.Leave;
import com.jeeplus.modules.oa.entity.TestAudit;
import com.jeeplus.modules.sys.entity.DictValue;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.utils.UserUtils;
import com.jeeplus.common.utils.Collections3;
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
import com.hqu.modules.purchasemanage.supprice.entity.PurSupPrice;
import com.hqu.modules.purchasemanage.supprice.entity.PurSupPriceDetail;
import com.hqu.modules.purchasemanage.supprice.mapper.PurSupPriceDetailMapper;
import com.hqu.modules.purchasemanage.transtype.entity.TranStype;
import com.hqu.modules.purchasemanage.transtype.mapper.TranStypeMapper;
import com.google.common.collect.Maps;
import com.hqu.modules.basedata.account.entity.Account;
import com.hqu.modules.basedata.item.entity.Item;
import com.hqu.modules.basedata.item.mapper.ItemMapper;
import com.hqu.modules.purchasemanage.contractmain.entity.ConRelations;
import com.hqu.modules.purchasemanage.contractmain.entity.ContractDetail;
import com.hqu.modules.purchasemanage.contractmain.mapper.ConRelationsMapper;
import com.hqu.modules.purchasemanage.contractmain.mapper.ContractDetailCreateMapper;
import com.hqu.modules.purchasemanage.contractmain.mapper.ContractMainCreateMapper;

/**
 * 采购合同管理Service
 * @author ltq
 * @version 2018-04-28
 */
@Service
@Transactional(readOnly = true)
public class ContractMainCreateService extends CrudService<ContractMainCreateMapper, ContractMain> {
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
	private LinkManMapper linkManMapper;
	@Autowired
	private PurSupPriceDetailMapper purSupPriceDetailMapper;
	
	@Autowired
	private RuntimeService runtimeService;
	@Autowired
	protected TaskService taskService;
	@Autowired
	protected HistoryService historyService;
	@Autowired
	protected RepositoryService repositoryService;
	@Autowired
	private IdentityService identityService;
	@Autowired
	private ActTaskService actTaskService;
	
	/**
	 * 获取流程详细及工作流参数
	 * @param id
	 */
	@SuppressWarnings("unchecked")
	public ContractMain get(String id) {
		ContractMain contractMain = super.get(id);
			Map<String,Object> variables=null;
			HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(contractMain.getProcInsId()).singleResult();
			if(historicProcessInstance!=null) {
				variables = Collections3.extractToMap(historyService.createHistoricVariableInstanceQuery().processInstanceId(historicProcessInstance.getId()).list(), "variableName", "value");
			} else {
				variables = runtimeService.getVariables(runtimeService.createProcessInstanceQuery().processInstanceId(contractMain.getProcInsId()).active().singleResult().getId());
			}
			contractMain.setVariables(variables);
		
		contractMain.setContractDetailList(contractDetailMapper.findList(new ContractDetail(contractMain)));
		return contractMain;
	}
	
	
	public ContractMain getByProcInsId(String procInsId) {
		return contractMainMapper.getByProcInsId(procInsId);
		
	}
	
	public List<LinkMan> getLinkManList(String id) {
		Paccount paccount =new Paccount();
		paccount.setId(id);
		paccount.setLinkManList(linkManMapper.findList(new LinkMan(paccount)));
		return paccount.getLinkManList();
	}
	public List<PurSupPriceDetail> getPurSupPriceList(String itemId,String condate,Double itemNum,String accountId) {
		List<PurSupPriceDetail> purSupPriceDetails=new ArrayList<PurSupPriceDetail>();
		PurSupPriceDetail purSupPriceDetail=new PurSupPriceDetail();
		Item item= new Item();
		item.setId(itemId);
		purSupPriceDetail.setItem(item);
		//获取0至itemNum数量的所有价格区间
		purSupPriceDetail.setMinQty(itemNum);
	//	purSupPriceDetail.setMaxQty(itemNum);
		Date curDate=new Date();
		try {
			curDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(condate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
	
//	@Autowired
//	private ConRelationsMapper  conRelationsMapper;
	
	public List<ContractMain> findList(ContractMain contractMain) {
		return super.findList(contractMain);
	}
/*
	public ContractMain get(String id) {
		ContractMain contractMain = super.get(id);
		contractMain.setContractDetailList(contractDetailMapper.findList(new ContractDetail(contractMain)));
		return contractMain;
	}
*/
	public List<PurPlanDetail> findPurPlanDetailList(PurPlanDetail purPlanDetail) {
		return purPlanDetailMapper.findList(purPlanDetail);
	}

	public Page<ContractMain> findPage(Page<ContractMain> page, ContractMain contractMain) {
		
		/**
		 * 工作流
		 */
		dataRuleFilter(contractMain);
        
		
		
		
		
		Item item=contractMain.getItem();
		if(item!=null&&!("".equals(item.getId()))){
			page=findPageService(page,contractMain);
		}else if(contractMain.getItemName()!=null&&!("".equals(contractMain.getItemName()))){
			page=findPageService(page,contractMain);
		}else if(contractMain.getItemModel()!=null&&!("".equals(contractMain.getItemModel()))){
			page=findPageService(page,contractMain);
		}else{  //按单据状态搜索
			if(contractMain.getBillStateFlag()!=null&&!("".equals(contractMain.getBillStateFlag()))){
				//只能查看是制单人本人的合同
		 		 User user=UserUtils.getUser();
		 	 	 contractMain.setUser(user);
				//生成的合同
		 		contractMain.setContrState("C");
		 		contractMain.setOrderBy(page.getOrderBy());
			    List<ContractMain> pageList=contractMainMapper.findPageNew(contractMain);
			    //按计划号搜索
			    if(contractMain.getPlanBillNum()!=null&&!("".equals(contractMain.getPlanBillNum()))){
			    	//添加满足计划单号搜索的查询结果
			         List<String> newrelationStr= getConBillNumByPlan(contractMain.getPlanBillNum()); //存储满足计划单号所对应的合同编号	         
			         List <ContractMain> resultList=new  ArrayList <ContractMain>();
			         for(ContractMain attr:pageList){				       
					    	   //计划单号的搜索条件不为空
					    	   if(newrelationStr.size()>0){
					    		    //满足计划单号搜索所筛选的合同编号
					    		   if(newrelationStr.contains(attr.getBillNum())) 
					    			   resultList.add(attr);			    	  
					           }
					 } 
			        page.setList(resultList);
				 	page.setCount(resultList.size());	
			    }else{
			    	page.setList(pageList);
			 		page.setCount(pageList.size());	
			    }
			    
			}else{
			//只能查看是制单人本人的合同
	 		 User user=UserUtils.getUser();
	 	 	 contractMain.setUser(user);
			//生成的合同
	 		contractMain.setContrState("C");
	 		 //正在录入/修改的合同
	 		contractMain.setBillStateFlag("A");
	 		contractMain.setOrderBy(page.getOrderBy());
		    List<ContractMain> pageList=contractMainMapper.findPageNew(contractMain);		  
		    contractMain.setBillStateFlag("B");
		    List<ContractMain> pageListDisPass=contractMainMapper.findPageNew(contractMain);
	 		List <ContractMain> idList = new  ArrayList <ContractMain>(); 
	 		 idList.addAll(pageList);
	 		 idList.addAll(pageListDisPass);
	 		//按计划号搜索
	 		if(contractMain.getPlanBillNum()!=null&&!("".equals(contractMain.getPlanBillNum()))){
		    	//添加满足计划单号搜索的查询结果
		         List<String> newrelationStr= getConBillNumByPlan(contractMain.getPlanBillNum()); //存储满足计划单号所对应的合同编号	         
		         List <ContractMain> resultList=new  ArrayList <ContractMain>();
		         for(ContractMain attr:idList){				       
				    	   //计划单号的搜索条件不为空
				    	   if(newrelationStr.size()>0){
				    		    //满足计划单号搜索所筛选的合同编号
				    		   if(newrelationStr.contains(attr.getBillNum())) 
				    			   resultList.add(attr);			    	  
				           }
				 } 
		        page.setList(resultList);
			 	page.setCount(resultList.size());	
		    }else{
		    	page.setList(idList);
		 		page.setCount(idList.size());	
		    }
		  }
		}	
		
		
		/**
		 * 工作流
		 */
		for(ContractMain con : page.getList()) {
			String processInstanceId = con.getProcInsId();
			Task task = taskService.createTaskQuery().processInstanceId(processInstanceId).active().singleResult();
			con.setTask(task);
			HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
			if(historicProcessInstance!=null) {
				con.setHistoricProcessInstance(historicProcessInstance);
				con.setProcessDefinition(repositoryService.createProcessDefinitionQuery().processDefinitionId(historicProcessInstance.getProcessDefinitionId()).singleResult());
			} else {
				ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).active().singleResult();
				if (processInstance != null){
					con.setProcessInstance(processInstance);
					con.setProcessDefinition(repositoryService.createProcessDefinitionQuery().processDefinitionId(processInstance.getProcessDefinitionId()).singleResult());
				}
			}
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
 		 User user=UserUtils.getUser();
 	 	 contractMain.setUser(user);
 	 	 //生成的合同
 		 contractMain.setContrState("C");
 		 contractMain.setOrderBy(page.getOrderBy());
         List <ContractMain> pageList=new  ArrayList <ContractMain>();
 		 //按单据状态查询
		 if(contractMain.getBillStateFlag()!=null&&!("".equals(contractMain.getBillStateFlag()))){
			//正在录入/修改的合同
	 		 contractMain.setBillStateFlag("A");
		     pageList=contractMainMapper.findPageNew(contractMain);			
	         //审批未通过的合同
	         contractMain.setBillStateFlag("B");
		     List<ContractMain> pageListDisPass=contractMainMapper.findPageNew(contractMain);	
	         //正在录入/修改的合同和审批未通过的合同
	         pageList.addAll(pageListDisPass);
		 }else{
		     pageList=contractMainMapper.findPageNew(contractMain);	
		 }
         //page.setPageSize(page1.getPageSize());     
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
	        ContractMain contractBill=new ContractMain();
	        contractBill.setBillNum(billN);
	        //判断合同编码是否重复
	      while(contractMainMapper.get(contractBill)!=null){
	    	for (int j = 0; j < 4; j++)   
	        {  
	            flag.append(sources.charAt(rand.nextInt(9)) + "");  
	        } 
	    	 billN="con"+strDate+flag.toString();   
	         contractBill=new ContractMain();
	         contractBill.setBillNum(billN);
	      }
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
	if(contractMain.getGroupBuyer()==null){
		
	}else{
		//String  buyerId=contractMain.getGroupBuyer().getUser().getId();
		GroupBuyer buyer=contractMain.getGroupBuyer();
		if(buyer==null||"".equals(buyer.getId())) {
							  
		}else{
			//User  buyer=UserUtils.get(buyerId);
		//	GroupBuyer groupBuyer=new GroupBuyer();
		//	groupBuyer.setUser(buyer);			
		    GroupBuyer groupBuyer=groupBuyerMapper.get(buyer);
			contractMain.setGroupBuyer(groupBuyer);
		}	
	}
				   
   /*//合同录入时自动生成状态
	String contrState=contractMain.getContrState();	
	if(contrState==null||"".equals(contrState)){
		contractMain.setContrState("M");
	}
	*/
	//设置成正在录入和修改
	String billStateFlag=contractMain.getBillStateFlag();	
	if(billStateFlag==null||"".equals(billStateFlag)){
		//contractMain.setContrState("M");
		contractMain.setBillStateFlag("A");
	}
	//
	List<ContractDetail> contractDetailList=contractMain.getContractDetailList();
	//合同信息子表填充计划单号和合同序列号
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
			if(conRelations!=null){
				//设置合同子表对应的计划单号和计划序号
				conDetail.setPlanBillNum(conRelations.getFrontNum());
				conDetail.setPlanSerialNum(conRelations.getFrontId());
				//设置合同签订量
				conDetail.setFrontItemQty(conDetail.getItemQty());
			}
		}
	}
	//添加采购员的计划信息
	 GroupBuyer groupBuyer=contractMain.getGroupBuyer();
	//合同信息子表填充计划单号和合同序列号
		if(groupBuyer!=null&&!("".equals(groupBuyer.getId()))){
		 PurPlanDetail purPlanDetail=new PurPlanDetail();
		 //将用户的id设置为采购员的id
//		 GroupBuyer buyer= contractMain.getGroupBuyer();
//		 buyer.setId(buyer.getUser().getId());
//		 purPlanDetail.setBuyerId(buyer);
		 GroupBuyer buyer= new GroupBuyer();
		 buyer.setId(contractMain.getGroupBuyer().getUser().getId());
		 purPlanDetail.setBuyerId(buyer);
		 List<PurPlanDetail> purPlanDetails= purPlanDetailMapper.findList(purPlanDetail);
		 Iterator<PurPlanDetail> purPlanIt = purPlanDetails.iterator();
	    
		 List <PurPlanDetail> idList = new  ArrayList <PurPlanDetail>(); 
		 
		 while(purPlanIt.hasNext()){
			 //添加物料数据
	    	 PurPlanDetail purPlan=purPlanIt.next();
	    	 PurPlanMain purPlanMain=purPlanMainMapper.get(purPlan.getBillNum());
	     	 String accountId=contractMain.getAccount().getId();
	     	 //查找满足所填写的供应商和采购员信息的计划信息
	    	 if(purPlanMain!=null&&"C".equals(purPlanMain.getBillStateFlag())){
        		if(purPlanMain.getSupId()!=null&&accountId!=null&&accountId.equals(purPlanMain.getSupId().getId())){        		
	    		 Item  item=itemMapper.get(purPlan.getItemCode());
	    	    //	 ItemExtend item= itemExtendMapper.get(purPlan.getItemCode());
	    		 if(item!=null){
	    			 ItemExtend itemExtend=new ItemExtend();
		    		 itemExtend.setCode(item.getCode());
		    		 itemExtend.setId(item.getId());
		    		 //填写物料材质
		           	 purPlan.setItemTexture(item.getTexture());
		           	 //填写物料编号
		           	 purPlan.setItemCode(itemExtend);
		           	 //填入计划单号
		           	 purPlan.setBillNum(purPlanMain);
		           	 //
		             idList.add(purPlan);
	    		     }
	    		 }
	    	 }
	   	 }
		 System.out.println("idList+++++++++++++++++++++++++++:"+idList);
		 contractMain.setPurPlanDetailList(idList);
		 
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
public Page<PurPlanDetail> selectPlan(Page<PurPlanDetail> page, PurPlanDetail purPlanDetail,String accountId) {
	     List<PurPlanDetail> purPlanDetails= purPlanDetailMapper.findList(purPlanDetail);
		 Iterator<PurPlanDetail> purPlanIt = purPlanDetails.iterator();
        
		 List <PurPlanDetail> idList = new  ArrayList <PurPlanDetail>(); 
		 
		 while(purPlanIt.hasNext()){
			 //添加物料数据
        	 PurPlanDetail purPlan=purPlanIt.next();
        	 PurPlanMain purPlanMain=purPlanMainMapper.get(purPlan.getBillNum());
        	 if(purPlanMain!=null&&"C".equals(purPlanMain.getBillStateFlag())){
        		 if(purPlanMain.getSupId()!=null&&accountId!=null&&accountId.equals(purPlanMain.getSupId().getId())){
        			 Item  item=itemMapper.get(purPlan.getItemCode());
        	        	//	 ItemExtend item= itemExtendMapper.get(purPlan.getItemCode());
        	        		 if(item!=null){
        	        			 ItemExtend itemExtend=new ItemExtend();
        	            		 itemExtend.setCode(item.getCode());
        	            		 itemExtend.setId(item.getId());
        	            		 //填写物料材质
        	    	           	 purPlan.setItemTexture(item.getTexture());
        	    	           	 //填写物料编号
        	    	           	 purPlan.setItemCode(itemExtend);
        	    	           	 //填入计划单号
        	    	           	 purPlan.setBillNum(purPlanMain);
        	    	           	 //
        	    	             idList.add(purPlan);
        		   }
        		 }
        	 }
       	 }
		 page.setList(idList);
	  return page;
}


	@Transactional(readOnly = false)
//	public void save(ContractMain contractMain) {
	public void save(ContractMain contractMain) {
		String billNum=contractMain.getBillNum();
		if(contractMain.getPayMode()!=null&&!("".equals(contractMain.getPayMode().getId()))){
			PayMode payMode=payModeMapper.get(contractMain.getPayMode());	
			if(payMode!=null){
				contractMain.setPayMode(payMode);
				contractMain.setPaymodeName(payMode.getPaymodename());
			}			
		}
		if(contractMain.getTransType()!=null&&!("".equals(contractMain.getTransType().getId()))){
			TranStype transType=tranStypeMapper.get(contractMain.getTransType());		
            if(transType!=null){
            	contractMain.setTransType(transType);
    			contractMain.setTranmodeName(transType.getTranstypename());
			}
		}
		if(contractMain.getContractType()!=null&&!("".equals(contractMain.getContractType().getId()))){
			ContractType contractType=contractTypeMapper.get(contractMain.getContractType().getId());
            if(contractType!=null){
            	contractMain.setContypeName(contractType.getContypename());
    			contractMain.setContractType(contractType);
			}
		}
		
		//判断是否是同一采购员,不是同一采购员的需删除原采购员的子表数据
	//	if(StringUtils.isBlank(contractMain.getId())){
			
	//	}
	    /**
	     * 判断是否修改过采购员
	     */
		//获取保存前数据库存储的采购员
		if(contractMain.getId()!=null&&!("".equals(contractMain.getId()))){
			ContractMain con=super.get(contractMain.getId());
			String oldByerId=con.getGroupBuyer().getId();
			String newByerId=contractMain.getGroupBuyer().getId();
			if(!newByerId.equals(oldByerId)){
				//查找该合同的旧采购员所有合同子表信息
				ContractDetail conDetail=new ContractDetail();
				conDetail.setContractMain(contractMain);
				conDetail.setDelFlag("0");
				List<ContractDetail> contractDetailList=contractDetailMapper.findList(conDetail);
				Iterator<ContractDetail> contractDetailIt = contractDetailList.iterator();  
				   // Integer serialNum=0;
					  while(contractDetailIt.hasNext()){
					     ContractDetail conDtail=contractDetailIt.next();
					        //关系表中查找对应的计划子表
					        ConRelations conRes=new ConRelations();
							conRes.setAfterNum(contractMain.getBillNum());
							conRes.setAfterId(conDtail.getSerialNum());
							conRes.setIdType("PC");
							//conRes.setState("A");
							conRes=conRelationsMapper.getConRelations(conRes);
					    	//往计划子表中回写合同量
				   		 PurPlanMain purPlanMain=new PurPlanMain();
				   		 purPlanMain.setBillNum(conRes.getFrontNum());
				   		 purPlanMain=purPlanMainMapper.getPurPlanMain(purPlanMain);
				   		 //依据计划单号和计划子表序号查找计划子表
				   		 PurPlanDetail purPlanDetail=new PurPlanDetail();
				   		 purPlanDetail.setBillNum(purPlanMain);
				   		 purPlanDetail.setSerialNum(conRes.getFrontId());
				   		 purPlanDetail= purPlanDetailMapper.getPurPlanDetail(purPlanDetail);
				   		 //计划子表中所填的合同总量
				   		 Double conQty= purPlanDetail.getConQty();
				   		 if(conQty==null||conQty==0){
				   			 conQty=0.0;
				   		 }
				   		 //该合同子表修改后的合同量
				   		 Double itemQty=conDtail.getItemQty();
				   		 if(itemQty==null||"".equals(itemQty)){
				   			 itemQty=0.0;
				   		 }
				   		 purPlanDetail.setConQty(conQty-itemQty);
					     purPlanDetailMapper.update(purPlanDetail);
					     //把之前的关系数据作废
					    // conRes.setState("V");
			        	// conRelationsMapper.update(conRes);
					     conRelationsMapper.delete(conRes);
					     //删除该子表
						 contractDetailMapper.delete(conDtail);
					  }
			}

		}
		
			//修改之前把之前的关系数据作废
		    ConRelations conRes=new ConRelations();
			conRes.setAfterNum(contractMain.getBillNum());
			conRes.setIdType("PC");
			//conRes.setState("A");
			List<ConRelations> conRelationList=conRelationsMapper.findList(conRes);
			Iterator<ConRelations> conRelationIt = conRelationList.iterator();  
	        while(conRelationIt.hasNext()){
	        	 ConRelations conRelations=conRelationIt.next();
	        	// conRelations.setState("V");
	        	// conRelationsMapper.update(conRelations);
	        	 conRelationsMapper.delete(conRelations);
	        }
		
		
		
	    //往采购关系表中填写数据
	    List<ContractDetail> contractDetails=contractMain.getContractDetailList();
	    Iterator<ContractDetail> contractDetailIt = contractDetails.iterator();  
	    Integer serialNum=0;
	    while(contractDetailIt.hasNext()){
	      ContractDetail conDtail=contractDetailIt.next();
	    	//System.out.println("conDtail.getPlanBillNum()++++++++++++++++++:"+conDtail.getPlanBillNum());
	      if(conDtail.getPlanBillNum()!=null){
	    	
	    	if("0".equals(conDtail.getDelFlag())){//修改合同信息
	    		
	    		 ConRelations conRelations=new ConRelations();
	    		 serialNum=serialNum+1;
	    		 conRelations.setType("PC");
	    		// conRelations.setState("A");
	    		 //设置合同子表的序列编号
	    		 conDtail.setSerialNum(serialNum);
	    		 //设置关系表数据
	    		 conRelations.setAfterId(serialNum);
	    		 conRelations.setAfterNum(contractMain.getBillNum());
	    		 conRelations.setFrontNum(conDtail.getPlanBillNum());
	    		 conRelations.setFrontId(conDtail.getPlanSerialNum());
	    		 conRelations.preInsert();
	    		 //关系表中填写数据
	    		 conRelationsMapper.insert(conRelations);
	    	     //往计划子表中回写合同量
	    		 PurPlanMain purPlanMain=new PurPlanMain();
	    		 purPlanMain.setBillNum(conDtail.getPlanBillNum());
	    		 purPlanMain=purPlanMainMapper.getPurPlanMain(purPlanMain);
	    		 PurPlanDetail purPlanDetail=new PurPlanDetail();
	    		 purPlanDetail.setBillNum(purPlanMain);
	    		 purPlanDetail.setSerialNum(conDtail.getPlanSerialNum());
	    		 purPlanDetail= purPlanDetailMapper.getPurPlanDetail(purPlanDetail);
	    		 if(purPlanDetail!=null){
	    			//计划子表中所填的合同总量
		    		 Double conQty= purPlanDetail.getConQty();
		    		 if(conQty==null||conQty==0){
		    			 conQty=0.0;
		    		 }
		    		 //该合同子表修改前的合同量
		    		 Double frontItemQty=conDtail.getFrontItemQty();
		    		 if(frontItemQty==null||"".equals(frontItemQty)){
		    			 frontItemQty=0.0;
		    		 }
		    		 //该合同子表修改后的合同量
		    		 Double itemQty=conDtail.getItemQty();
		    		 if(itemQty==null||"".equals(itemQty)){
		    			 itemQty=0.0;
		    		 }
		    		 purPlanDetail.setConQty(conQty-frontItemQty+itemQty);
	    			 purPlanDetailMapper.update(purPlanDetail);
	    		 }	    		 
	    	}else{//删除合同信息
	    		 //往计划子表中回写合同量
	    		 PurPlanMain purPlanMain=new PurPlanMain();
	    		 purPlanMain.setBillNum(conDtail.getPlanBillNum());
	    		 purPlanMain=purPlanMainMapper.getPurPlanMain(purPlanMain);
	    		 PurPlanDetail purPlanDetail=new PurPlanDetail();
	    		 purPlanDetail.setBillNum(purPlanMain);
	    		 purPlanDetail.setSerialNum(conDtail.getPlanSerialNum());
	    		 purPlanDetail= purPlanDetailMapper.getPurPlanDetail(purPlanDetail);
	    		 if(purPlanDetail!=null){
	    			//计划子表中所填的合同总量
		    		 Double conQty= purPlanDetail.getConQty();
		    		 if(conQty==null||conQty==0){
		    			 conQty=0.0;
		    		 }
		    		 //该合同子表修改前的合同量
		    		 Double frontItemQty=conDtail.getFrontItemQty();
		    		 if(frontItemQty==null||"".equals(frontItemQty)){
		    			 frontItemQty=0.0;
		    		 }
		    		 //该合同子表修改后的合同量
		    		/* Double itemQty=conDtail.getItemQty();
		    		 if(itemQty==null||"".equals(itemQty)){
		    			 itemQty=0.0;
		    		 }
		    		 */
		    		 purPlanDetail.setConQty(conQty-frontItemQty);
	   			     purPlanDetailMapper.update(purPlanDetail);
	    		 }
	    	}
	      }
	    }
	    contractMain.setContractDetailList(contractDetails);//将合同信息序号修改后的合同重新设置
	   
		
		for (ContractDetail contractDetail : contractMain.getContractDetailList()){
			if (contractDetail.getId() == null){
				continue;
			}
			if (ContractDetail.DEL_FLAG_NORMAL.equals(contractDetail.getDelFlag())){
				if (StringUtils.isBlank(contractDetail.getId())){
					contractDetail.setContractMain(contractMain);
					contractDetail.preInsert();
					contractMain.setBillNum(billNum);
					contractDetail.setContractMain(contractMain);
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
		
		
		
		/**
		 * 启动流程
		 */
		// 用来设置启动流程的人员ID，引擎会自动把用户ID保存到activiti:initiator中
		Map<String, Object> variables = Maps.newHashMap();
		if (StringUtils.isBlank(contractMain.getId())){
				identityService.setAuthenticatedUserId(contractMain.getCurrentUser().getLoginName());
				contractMain.preInsert();
				contractMainMapper.insert(contractMain);
				// 启动流程
				String businessKey = contractMain.getId().toString();
				variables.put("type", "contractMain");
				variables.put("busId", businessKey);
			    variables.put("title", contractMain.getBillNum());
				String[] PD_PUR_CONTRACT = new String[]{"pur_contract_create", "pur_contractmain"};
				ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(PD_PUR_CONTRACT[0], businessKey, variables);
				contractMain.setProcessInstance(processInstance);				
				// 更新流程实例ID
				contractMain.setProcInsId(processInstance.getId());
				contractMainMapper.updateInsId(contractMain);
				
				super.save(contractMain);
		}else{
			contractMain.preUpdate();
			contractMainMapper.update(contractMain);
			// 完成流程任务
			//contractMain.getAct().setComment(("yes".equals(contractMain.getAct().getFlag())?"[提交] ":"[销毁] ")+contractMain.getAct().getComment());
			contractMain.getAct().setComment(("yes".equals(contractMain.getAct().getFlag())?"[提交] ":"[销毁]"));
			variables.put("pass", "yes".equals(contractMain.getAct().getFlag())? "1" : "0");
			actTaskService.complete(contractMain.getAct().getTaskId(), contractMain.getAct().getProcInsId(), contractMain.getAct().getComment(),variables);
		    if("yes".equals(contractMain.getAct().getFlag())){
		    	super.save(contractMain);
		    }else{
		    	//修改单据状态为作废状态
		    	contractMain.setBillStateFlag("V");
		    	super.save(contractMain);
		    	//还原计划中的采购量
		    	deletePlanQty(contractMain);
		    }
		}
		
		
	}
	
	
	/**
	 * 查询待办任务
	 * @param userId 用户ID
	 * @return
	 */
	public List<ContractMain> findTodoTasks(String userId) {
		
		List<ContractMain> results = new ArrayList<ContractMain>();
		List<Task> tasks = new ArrayList<Task>();
		// 根据当前人的ID查询
		List<Task> todoList = taskService.createTaskQuery().processDefinitionKey(ActUtils.PD_LEAVE[0]).taskAssignee(userId).active().orderByTaskPriority().desc().orderByTaskCreateTime().desc().list();
		// 根据当前人未签收的任务
		List<Task> unsignedTasks = taskService.createTaskQuery().processDefinitionKey(ActUtils.PD_LEAVE[0]).taskCandidateUser(userId).active().orderByTaskPriority().desc().orderByTaskCreateTime().desc().list();
		// 合并
		tasks.addAll(todoList);
		tasks.addAll(unsignedTasks);
		// 根据流程的业务ID查询实体并关联
		for (Task task : tasks) {
			String processInstanceId = task.getProcessInstanceId();
			ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).active().singleResult();
			String businessKey = processInstance.getBusinessKey();
			ContractMain contractMain = contractMainMapper.get(businessKey);
			contractMain.setTask(task);
			contractMain.setProcessInstance(processInstance);
			contractMain.setProcessDefinition(repositoryService.createProcessDefinitionQuery().processDefinitionId((processInstance.getProcessDefinitionId())).singleResult());
			results.add(contractMain);
		}
		return results;
	}
	
	
	
	
	/*
	@Transactional(readOnly = false)
	public void saveRelations(ContractMain contractMain) {
	}
	@Transactional(readOnly = false)
	public void saveUpdate(ContractMain contractMain) {
		String billNum=contractMain.getBillNum();
		PayMode payMode=payModeMapper.get(contractMain.getPayMode());
		TranStype transType=tranStypeMapper.get(contractMain.getTransType());
		contractMain.setPayMode(payMode);
		contractMain.setPaymodeName(payMode.getPaymodename());
		contractMain.setTransType(transType);
		contractMain.setTranmodeName(transType.getTranstypename());
		ContractType contractType=contractTypeMapper.get(contractMain.getContractType().getId());
		contractMain.setContypeName(contractType.getContypename());
		contractMain.setContractType(contractType);
		ContractMain contractF=new ContractMain();
		contractF.setBillNum(billNum);
		//判断记录是否已经存在
		List<ContractMain> conList=contractMainMapper.findList(contractF);
		if(conList.size()==0){
			contractMainMapper.insert(contractMain);
		}else{
			String id=conList.get(0).getId();
			contractMain.setId(id);
			contractMainMapper.update(contractMain);
		}
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
	*/
	
	
	@Transactional(readOnly = false)
	public void delete(ContractMain contractMain) {
		
		//计划子表中的合同量修改
		ContractMain contract=contractMainMapper.get(contractMain);
		//List<ContractDetail> contractDetailList=contract.getContractDetailList();
		//查找该合同的所有合同子表信息
		ContractDetail conDetail=new ContractDetail();
		conDetail.setContractMain(contract);
		conDetail.setDelFlag("0");
		List<ContractDetail> contractDetailList=contractDetailMapper.findList(conDetail);
		Iterator<ContractDetail> contractDetailIt = contractDetailList.iterator();  
	    // Integer serialNum=0;
		while(contractDetailIt.hasNext()){
		     ContractDetail conDtail=contractDetailIt.next();
		        //关系表中查找对应的计划子表
		        ConRelations conRes=new ConRelations();
				conRes.setAfterNum(contractMain.getBillNum());
				conRes.setAfterId(conDtail.getSerialNum());
				conRes.setIdType("PC");
				//conRes.setState("A");
				conRes=conRelationsMapper.getConRelations(conRes);
		    	//往计划子表中回写合同量
	   		 PurPlanMain purPlanMain=new PurPlanMain();
	   		 purPlanMain.setBillNum(conRes.getFrontNum());
	   		 purPlanMain=purPlanMainMapper.getPurPlanMain(purPlanMain);
	   		 //依据计划单号和计划子表序号查找计划子表
	   		 PurPlanDetail purPlanDetail=new PurPlanDetail();
	   		 purPlanDetail.setBillNum(purPlanMain);
	   		 purPlanDetail.setSerialNum(conRes.getFrontId());
	   		 purPlanDetail= purPlanDetailMapper.getPurPlanDetail(purPlanDetail);
	   		 //计划子表中所填的合同总量
	   		 Double conQty= purPlanDetail.getConQty();
	   		 if(conQty==null||conQty==0){
	   			 conQty=0.0;
	   		 }
	   		 //该合同子表修改后的合同量
	   		 Double itemQty=conDtail.getItemQty();
	   		 if(itemQty==null||"".equals(itemQty)){
	   			 itemQty=0.0;
	   		 }
	   		 purPlanDetail.setConQty(conQty-itemQty);
		     purPlanDetailMapper.update(purPlanDetail);
		     //把之前的关系数据作废
		    // conRes.setState("V");
        	// conRelationsMapper.update(conRes);
		     conRelationsMapper.delete(conRes);
		  }
		   //删除之前把之前的关系数据作废
		  /*  ConRelations conRes=new ConRelations();
			conRes.setAfterNum(contractMain.getBillNum());
			conRes.setIdType("PC");
			conRes.setState("A");
			List<ConRelations> conRelationList=conRelationsMapper.findList(conRes);
			Iterator<ConRelations> conRelationIt = conRelationList.iterator();  
	        while(conRelationIt.hasNext()){
	        	 ConRelations conRelations=conRelationIt.next();
	        	 conRelations.setState("V");
	        	 conRelationsMapper.update(conRelations);
	        }
	        */
		  super.delete(contractMain);
		contractDetailMapper.delete(new ContractDetail(contractMain));
	}
	
	
	@Transactional(readOnly = false)
	public void deletePlanQty(ContractMain contractMain) {
		
		//计划子表中的合同量修改
		ContractMain contract=contractMainMapper.get(contractMain);
		//List<ContractDetail> contractDetailList=contract.getContractDetailList();
		//查找该合同的所有合同子表信息
		ContractDetail conDetail=new ContractDetail();
		conDetail.setContractMain(contract);
		conDetail.setDelFlag("0");
		List<ContractDetail> contractDetailList=contractDetailMapper.findList(conDetail);
		Iterator<ContractDetail> contractDetailIt = contractDetailList.iterator();  
	    // Integer serialNum=0;
		while(contractDetailIt.hasNext()){
		     ContractDetail conDtail=contractDetailIt.next();
		        //关系表中查找对应的计划子表
		        ConRelations conRes=new ConRelations();
				conRes.setAfterNum(contractMain.getBillNum());
				conRes.setAfterId(conDtail.getSerialNum());
				conRes.setIdType("PC");
				//conRes.setState("A");
				conRes=conRelationsMapper.getConRelations(conRes);
		    	//往计划子表中回写合同量
	   		 PurPlanMain purPlanMain=new PurPlanMain();
	   		 purPlanMain.setBillNum(conRes.getFrontNum());
	   		 purPlanMain=purPlanMainMapper.getPurPlanMain(purPlanMain);
	   		 //依据计划单号和计划子表序号查找计划子表
	   		 PurPlanDetail purPlanDetail=new PurPlanDetail();
	   		 purPlanDetail.setBillNum(purPlanMain);
	   		 purPlanDetail.setSerialNum(conRes.getFrontId());
	   		 purPlanDetail= purPlanDetailMapper.getPurPlanDetail(purPlanDetail);
	   		 //计划子表中所填的合同总量
	   		 Double conQty= purPlanDetail.getConQty();
	   		 if(conQty==null||conQty==0){
	   			 conQty=0.0;
	   		 }
	   		 //该合同子表修改后的合同量
	   		 Double itemQty=conDtail.getItemQty();
	   		 if(itemQty==null||"".equals(itemQty)){
	   			 itemQty=0.0;
	   		 }
	   		 purPlanDetail.setConQty(conQty-itemQty);
		     purPlanDetailMapper.update(purPlanDetail);
		     //把之前的关系数据作废
		     conRes.setState("V");
        	 conRelationsMapper.update(conRes);
		     conRelationsMapper.delete(conRes);
		  }
		  //super.delete(contractMain);
		  //contractDetailMapper.delete(new ContractDetail(contractMain));
	}
	
	
	
	@Transactional(readOnly = false)
    public Page<ContractDetail> findDetailPageService(Page<ContractDetail> page, ContractMain contractMain){
		//Page<ContractMain> pag= super.findPage(page, contractMain);
		ContractMain con=new ContractMain();
		con.setBillNum(contractMain.getBillNum());
		ContractMain conMain= contractMainMapper.get(con);
		if(conMain!=null){
			List<ContractDetail>  contractDetails= conMain.getContractDetailList();
			page.setList(contractDetails);
			page.setCount(contractDetails.size());
		}
		return page;
	}	
}