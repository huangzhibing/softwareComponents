/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.purchasemanage.contractmain.service;

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
import com.hqu.modules.purchasemanage.contractmain.mapper.ContractMainMapper;
import com.hqu.modules.purchasemanage.contracttype.entity.ContractType;
import com.hqu.modules.purchasemanage.contracttype.mapper.ContractTypeMapper;
import com.hqu.modules.purchasemanage.group.entity.GroupBuyer;
import com.hqu.modules.purchasemanage.group.mapper.GroupBuyerMapper;
import com.hqu.modules.purchasemanage.linkman.entity.LinkMan;
import com.hqu.modules.purchasemanage.linkman.entity.Paccount;
import com.hqu.modules.purchasemanage.linkman.mapper.LinkManMapper;
import com.hqu.modules.purchasemanage.paymode.entity.PayMode;
import com.hqu.modules.purchasemanage.paymode.mapper.PayModeMapper;
import com.hqu.modules.purchasemanage.supprice.entity.PurSupPrice;
import com.hqu.modules.purchasemanage.supprice.entity.PurSupPriceDetail;
import com.hqu.modules.purchasemanage.supprice.mapper.PurSupPriceDetailMapper;
import com.hqu.modules.purchasemanage.transtype.entity.TranStype;
import com.hqu.modules.purchasemanage.transtype.mapper.TranStypeMapper;
import com.hqu.modules.qualitymanage.purinvcheckmain.entity.ItemforInv;
import com.hqu.modules.qualitymanage.purinvcheckmain.mapper.ItemforInvMapper;
import com.google.common.collect.Maps;
import com.hqu.modules.basedata.account.entity.Account;
import com.hqu.modules.basedata.item.entity.Item;
import com.hqu.modules.purchasemanage.contractmain.entity.ContractDetail;
import com.hqu.modules.purchasemanage.contractmain.mapper.ContractDetailMapper;

/**
 * 采购合同管理Service
 * @author ltq
 * @version 2018-04-24
 */
@Service
@Transactional(readOnly = true)
public class ContractMainService extends CrudService<ContractMainMapper, ContractMain> {
	@Autowired
	private ContractMainMapper contractMainMapper;
	@Autowired
	private ContractDetailMapper contractDetailMapper;
	@Autowired
	private PayModeMapper payModeMapper;
	@Autowired
	private TranStypeMapper tranStypeMapper;
	@Autowired
	private ContractTypeMapper  contractTypeMapper;
	@Autowired
	private GroupBuyerMapper groupBuyerMapper;
	@Autowired
	private ItemforInvMapper itemforInvMapper;
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
		//获取0至itemNum数量的所有价格区间
		Item item= new Item();
		item.setId(itemId);
		purSupPriceDetail.setItem(item);
		purSupPriceDetail.setMinQty(itemNum);
		//purSupPriceDetail.setMaxQty(itemNum);
		Date curDate=new Date();
		try {
			curDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(condate);
			//curDate = new SimpleDateFormat("yyyy-MM-dd").parse(condate);
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
	
	public List<ContractMain> findList(ContractMain contractMain) {
		return super.findList(contractMain);
	}	
	
	public Page<ContractMain> findPage(Page<ContractMain> page, ContractMain contractMain) {
		Item item=contractMain.getItem();
		if(item!=null&&!("".equals(item.getId()))){
			page=findPageService(page,contractMain);
		}else if(contractMain.getItemName()!=null&&!("".equals(contractMain.getItemName()))){
			page=findPageService(page,contractMain);
		}else if(contractMain.getItemModel()!=null&&!("".equals(contractMain.getItemModel()))){
			page=findPageService(page,contractMain);
		}else{
			if(contractMain.getBillStateFlag()!=null&&!("".equals(contractMain.getBillStateFlag()))){
				//只能查看是制单人本人的合同
		 		 User user=UserUtils.getUser();
		 	 	 contractMain.setUser(user);
				//生成的合同
		 		contractMain.setContrState("M");
		 		contractMain.setOrderBy(page.getOrderBy());
			    List<ContractMain> pageList=contractMainMapper.findPageNew(contractMain);		  
			    page.setList(pageList);
		 		page.setCount(pageList.size());	
			}else{
			//只能查看是制单人本人的合同
	 		 User user=UserUtils.getUser();
	 	 	 contractMain.setUser(user);
			 //生成的合同
	 		 contractMain.setContrState("M");
	 		 //正在录入/修改的合同
	 		 contractMain.setBillStateFlag("A");
	 		 contractMain.setOrderBy(page.getOrderBy());
		     List<ContractMain> pageList=contractMainMapper.findPageNew(contractMain);		  
		     contractMain.setBillStateFlag("B");
		     List<ContractMain> pageListDisPass=contractMainMapper.findPageNew(contractMain);
	 		 List <ContractMain> idList = new  ArrayList <ContractMain>(); 
	 		 idList.addAll(pageList);
	 		 idList.addAll(pageListDisPass);
	 		 page.setList(idList);
	 		 page.setCount(idList.size());
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
 	 	 //录入的合同
 		 contractMain.setContrState("M");
 		 //正在录入/修改的合同
 		 contractMain.setBillStateFlag("A");
 		 Page<ContractMain> page1=super.findPage(page, contractMain);
         //通过检索框中主表的输入信息模糊检索主表
         List<ContractMain> pageList=page1.getList();
 		
         //审批未通过的合同
         contractMain.setBillStateFlag("B");
         Page<ContractMain> page2=super.findPage(page, contractMain);
         List<ContractMain> pageListDisPass=page2.getList();
         //正在录入/修改的合同和审批未通过的合同
         pageList.addAll(pageListDisPass);   
         //添加满足物料和主表的查询结果
         for(ContractMain attr:pageList){	
		       if(newList.contains(attr.getBillNum())){
		    	   resultList.add(attr);
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
			    GroupBuyer  buyer=contMain.getGroupBuyer();
			    GroupBuyer groupBuyer=groupBuyerMapper.get(buyer);
				contMain.setGroupBuyer(groupBuyer);
				contrList.add(contMain);
				}
		}	
		page.setList(contrList);
		return page;		
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
		GroupBuyer buyer=contractMain.getGroupBuyer();
		if(buyer==null||"".equals(buyer.getId())) {
					  
		}else{	
		    GroupBuyer groupBuyer=groupBuyerMapper.get(buyer);
			contractMain.setGroupBuyer(groupBuyer);
			//补全采购员信息
		//	contractMain.setBuyerFaxNum(groupBuyer.getBuyerFaxNum());//设置采购员传真
		//	contractMain.setBuyerPhoneNum(groupBuyer.getBuyerPhoneNum());//设置采购员电话
		//	contractMain.setDeliveryAddress(groupBuyer.getDeliveryAddress());//设置送货地址
		}	
	}
				    
	//设置成正在录入和修改
	String billStateFlag=contractMain.getBillStateFlag();	
	if(billStateFlag==null||"".equals(billStateFlag)){
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
	
	//设置供应商传真
	//contractMain.setAccountFaxNum(contractMain.getAccount().getFaxNum());
	//设置供应商电话
	//contractMain.setAccountTelNum(contractMain.getAccount().getTelNum());
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
		
	//	super.save(contractMain);
		Integer serialNum=0;
		for (ContractDetail contractDetail : contractMain.getContractDetailList()){
			if (contractDetail.getId() == null){
				continue;
			}
			if (ContractDetail.DEL_FLAG_NORMAL.equals(contractDetail.getDelFlag())){
				if (StringUtils.isBlank(contractDetail.getId())){
					contractDetail.setContractMain(contractMain);
					contractDetail.preInsert();
					serialNum++;
					contractDetail.setSerialNum(serialNum);
					contractDetailMapper.insert(contractDetail);
				}else{
					contractDetail.preUpdate();
					contractMain.setBillNum(billNum);
					contractDetail.setContractMain(contractMain);
					serialNum++;
					contractDetail.setSerialNum(serialNum);
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
				String[] PD_PUR_CONTRACT = new String[]{"pur_contract_input", "pur_contractmain"};
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
		    }
		}
		
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
		//判断该记录是否已经存在
		List<ContractMain> conList=contractMainMapper.findList(contractF);
		if(conList.size()==0){
			contractMainMapper.insert(contractMain);
		}else{
			String id=conList.get(0).getId();
			contractMain.setId(id);
			//contractMain.i
			contractMainMapper.update(contractMain);
		}
	//	super.save(contractMain);
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
	
	public  List<ItemforInv> getItemList(Page<ItemforInv> page,ItemforInv item){
		item.setPage(page);
		List<ItemforInv> items=itemforInvMapper.findList(item);
		return items;		
	}
	
	@Transactional(readOnly = false)
	public void delete(ContractMain contractMain) {
		super.delete(contractMain);
		contractDetailMapper.delete(new ContractDetail(contractMain));
	}
	
}