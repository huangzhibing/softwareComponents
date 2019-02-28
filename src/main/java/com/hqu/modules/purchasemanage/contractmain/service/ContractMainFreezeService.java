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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.act.service.ActTaskService;
import com.jeeplus.modules.act.utils.ActUtils;
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
import com.hqu.modules.purchasemanage.transtype.entity.TranStype;
import com.hqu.modules.purchasemanage.transtype.mapper.TranStypeMapper;
import com.google.common.collect.Maps;
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
public class ContractMainFreezeService extends CrudService<ContractMainCreateMapper, ContractMain> {
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
	private ConRelationsMapper conRelationsMapper;
	@Autowired
	private LinkManMapper linkManMapper;
	
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
	
	
	public List<ContractMain> findList(ContractMain contractMain) {
		return super.findList(contractMain);
	}
	public List<LinkMan> getLinkManList(String id) {
		Paccount paccount =new Paccount();
		paccount.setId(id);
		paccount.setLinkManList(linkManMapper.findList(new LinkMan(paccount)));
		return paccount.getLinkManList();
	}
	public List<PurPlanDetail> findPurPlanDetailList(PurPlanDetail purPlanDetail) {
		return purPlanDetailMapper.findList(purPlanDetail);
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
			 contractMain.setOrderBy(page.getOrderBy());
			 contractMain.setBillStateFlag("E");
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
		}else{
			//生成的合同
			 //只能查看是制单人本人的合同
	 	//	 User user=UserUtils.getUser();
	 	// 	 contractMain.setUser(user);	 	 
	 		 //正在录入/修改的合同
	 		 contractMain.setBillStateFlag("E");
	 	   	 contractMain.setOrderBy(page.getOrderBy());
			 List<ContractMain> resultList=contractMainMapper.findPageNew(contractMain);
		    // page=super.findPage(page, contractMain);	
			 page.setList(resultList);
	         page.setCount(resultList.size());
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
 	//	 User user=UserUtils.getUser();
 	// 	 contractMain.setUser(user);
 	
 		 //正在录入/修改的合同
 		 contractMain.setBillStateFlag("E");
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
	//        System.out.println(flag.toString());  
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
//		String billNum=contractMain.getBillNum();
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
		
		// 设置意见
	    contractMain.getAct().setComment("冻结");
		contractMain.preUpdate();
		Map<String, Object> vars = Maps.newHashMap();
		//vars.put("pass", "yes".equals(contractMain.getAct().getFlag())? "1" : "0");
		vars.put("pass","1");
		actTaskService.complete(contractMain.getAct().getTaskId(), contractMain.getAct().getProcInsId(), contractMain.getAct().getComment(), vars);
	
		
		
		super.save(contractMain);
		
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