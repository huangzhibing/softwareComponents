/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.purchasemanage.contractmain.web;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.ConstraintViolationException;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.task.Comment;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.core.mapper.JsonMapper;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.modules.act.entity.Act;
import com.jeeplus.modules.act.service.ActTaskService;
import com.jeeplus.modules.oa.entity.Leave;
import com.jeeplus.modules.oa.service.LeaveService;
import com.jeeplus.modules.sys.utils.UserUtils;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.excel.ExportExcel;
import com.jeeplus.common.utils.excel.ImportExcel;
import com.hqu.modules.purchasemanage.adtbilltype.entity.AdtBillType;
import com.hqu.modules.purchasemanage.adtbilltype.service.AdtBillTypeService;
import com.hqu.modules.purchasemanage.adtdetail.entity.AdtDetail;
import com.hqu.modules.purchasemanage.adtdetail.service.AdtDetailService;
import com.hqu.modules.purchasemanage.adtmodel.entity.AdtModel;
import com.hqu.modules.purchasemanage.adtmodel.service.AdtModelService;
import com.hqu.modules.purchasemanage.contractmain.entity.ConRelations;
import com.hqu.modules.purchasemanage.contractmain.entity.ContractDetail;
import com.hqu.modules.purchasemanage.contractmain.entity.ContractMain;
import com.hqu.modules.purchasemanage.contractmain.service.ConRelationsService;
import com.hqu.modules.purchasemanage.contractmain.service.ContractMainCloseService;
import com.hqu.modules.purchasemanage.contractmain.service.ContractMainCreateService;
import com.hqu.modules.purchasemanage.contracttype.entity.ContractType;
import com.hqu.modules.purchasemanage.contracttype.service.ContractTypeService;
import com.hqu.modules.purchasemanage.group.entity.GroupBuyer;
import com.hqu.modules.purchasemanage.purplan.entity.PurPlanDetail;
import com.hqu.modules.purchasemanage.supprice.entity.PurSupPriceDetail;
import com.hqu.modules.purchasemanage.transtype.entity.TranStype;

/**
 * 采购合同管理Controller
 * @author ltq
 * @version 2018-04-28
 */
@Controller
@RequestMapping(value = "${adminPath}/contractmain/contractMainCreate")
public class ContractMainCreateController extends BaseController {

	@Autowired
	private ContractMainCreateService contractMainService;
	@Autowired
	private AdtDetailService adtDetailService;
	@Autowired
	private ContractMainCloseService contractMainCloseService;
	@Autowired
	protected RuntimeService runtimeService;
	@Autowired
	protected TaskService taskService;
	@Autowired
	private HistoryService historyService;
	
	@ModelAttribute
	public ContractMain get(@RequestParam(required=false) String id) {
		ContractMain entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = contractMainService.get(id);
		}
		if (entity == null){
			entity = new ContractMain();
		}
		return entity;
	}
	
	/**
	 * 采购合同管理列表页面
	 */
	@RequiresPermissions("contractmaincreate:contractMain:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "purchasemanage/contractmain/contractMainCreateList";
	}
	
	/**
	 * 采购合同管理列表数据
	 */
	@ResponseBody
	@RequiresPermissions("contractmaincreate:contractMain:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(ContractMain contractMain, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<ContractMain> page = contractMainService.findPage(new Page<ContractMain>(request, response), contractMain); 
		 //补全制单人、供应商编号等信息
		   page=contractMainService.addInf(page);
		 //分页
	        String intPage= request.getParameter("pageNo");
	        int pageNo=Integer.parseInt(intPage);
	        int pageSize= page.getPageSize();
	        List<ContractMain> pageList=page.getList();
	       if(pageNo==1&&pageSize==-1){
	    	   page.setList(pageList);
	       }else{
	    	   List<ContractMain> reportL=  new ArrayList<ContractMain>();        
		        for(int i=(pageNo-1)*pageSize;i<pageList.size() && i<pageNo*pageSize;i++){
		        	reportL.add(pageList.get(i));
			    } 
		        page.setList(reportL);
	       }
	        
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑采购合同管理表单页面
	 */
	@RequiresPermissions(value={"contractmaincreate:contractMain:view","contractmaincreate:contractMain:add","contractmaincreate:contractMain:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(ContractMain contractMain, Model model) {
		// 工作流结束后查看工单
		if(contractMain.getAct().isFinishTask()&&!StringUtils.isBlank(contractMain.getId())){
			//补全contractMaind对象中部分属性的数据
			contractMain=contractMainCloseService.addFormContractMainInf(contractMain);
		    //获取支付方式下拉框的数据
			model.addAttribute("payList", contractMainService.getPayList());
			//获取运输方式下拉框的数据
			model.addAttribute("transList", contractMainService.getTransList());
			model.addAttribute("contractMain", contractMain);
			if(StringUtils.isBlank(contractMain.getId())){//如果ID是空为添加
				model.addAttribute("isAdd", true);
			}		
			return "purchasemanage/contractmain/contractMainViewForm";
		}else{
			
			//补全contractMaind对象中部分属性的数据
			contractMain=contractMainService.addFormContractMainInf(contractMain);
		    //获取支付方式下拉框的数据
			model.addAttribute("payList", contractMainService.getPayList());
			//获取运输方式下拉框的数据
			model.addAttribute("transList", contractMainService.getTransList());
			model.addAttribute("contractMain", contractMain);
			//审核未通过时显示审核意见
			if("B".equals(contractMain.getBillStateFlag())&&StringUtils.isNotBlank(contractMain.getAct().getTaskId())){					
				List<HistoricActivityInstance> list = historyService.createHistoricActivityInstanceQuery().processInstanceId(contractMain.getProcInsId())
						.orderByHistoricActivityInstanceStartTime().asc().orderByHistoricActivityInstanceEndTime().asc().list();
				for (int i=0; i<list.size(); i++){
					HistoricActivityInstance histIns = list.get(i);
					System.out.println("histIns+++++++++++++++++++start: "+histIns.getStartTime()+"  end:"+histIns.getEndTime());
					// 获取意见评论内容
					if (StringUtils.isNotBlank(histIns.getTaskId())){
						List<Comment> commentList = taskService.getTaskComments(histIns.getTaskId());
						if (commentList.size()>0){
							contractMain.getAct().setComment(commentList.get(0).getFullMessage());
						}
					}
				}
			}
			
			if(StringUtils.isBlank(contractMain.getId())){//如果ID是空为添加
				model.addAttribute("isAdd", true);
			}	
			return "purchasemanage/contractmain/contractMainCreateForm";
		}
	}

	/**
	 * 提交采购合同管理
	 */
	@RequiresPermissions(value={"contractmaincreate:contractMain:add","contractmaincreate:contractMain:edit"},logical=Logical.OR)
	@RequestMapping(value = "submit")
	public String submit(ContractMain contractMain, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, contractMain)){
			return form(contractMain, model);
		}
		try {
		  //单据状态改为录入完毕
		  contractMain.setBillStateFlag("W");
		  //录入的合同
		  contractMain.setContrState("C");
		  //往审核表中设置添加数据
		  AdtBillType adtBillType=new AdtBillType();
		  adtBillType.setBillTypeCode("CON001");
		  adtBillType.setBillTypeName("采购合同");
		  adtDetailService.nextStep(contractMain.getBillNum(), adtBillType, "CON", true, null);	
		  //新增或编辑表单保存
		  contractMainService.save(contractMain);
		  addMessage(redirectAttributes, "合同生成提交成功");
		}catch (Exception e) {
			addMessage(redirectAttributes, "系统内部错误！");
		}
		//return "redirect:"+Global.getAdminPath()+"/contractmain/contractMainCreate/?repage";
		return "redirect:" + adminPath + "/act/task/todo/";
	}
	
	/**
	 * 保存采购合同管理
	 */
	
	@RequiresPermissions(value={"contractmaincreate:contractMain:add","contractmaincreate:contractMain:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(ContractMain contractMain, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, contractMain)){
			return form(contractMain, model);
		}
			//生成的合同
			contractMain.setContrState("C");
			//新增或编辑表单保存
		//	contractMainService.save(contractMain);//提交
			//提交的同时启动流程
			contractMainService.save(contractMain);
			addMessage(redirectAttributes, "合同录入保存成功");
		return "redirect:"+Global.getAdminPath()+"/contractmain/contractMainCreate/?repage";
	}
         
	/**
	 * 任务列表
	 * @param leave	
	 */
  /*	@RequestMapping(value = {"list/task",""})
	public String taskList(HttpSession session, Model model) {
		String userId = UserUtils.getUser().getLoginName();//ObjectUtils.toString(UserUtils.getUser().getId());
		List<ContractMain> results = contractMainService.findTodoTasks(userId);
		model.addAttribute("contractMains", results);
		return "modules/oa/leave/leaveTask";
	}
  */
	/**
	 * 读取所有流程
	 * @return
	 */
/*	@RequestMapping(value = {"list"})
	public String list(ContractMain contractMain, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<ContractMain> page = contractMainService.findPage(new Page<ContractMain>(request, response), contractMain); 
        model.addAttribute("page", page);
		return "modules/oa/leave/leaveList";
	}
	*/
	/**
	 * 读取详细数据
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "detail/{id}")
	@ResponseBody
	public String getLeave(@PathVariable("id") String id) {
		ContractMain contractMain = contractMainService.get(id);
		return JsonMapper.getInstance().toJson(contractMain);
	}

	/**
	 * 读取详细数据
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "detail-with-vars/{id}/{taskId}")
	@ResponseBody
	public String getLeaveWithVars(@PathVariable("id") String id, @PathVariable("taskId") String taskId) {
		ContractMain contractMain = contractMainService.get(id);
		Map<String, Object> variables = taskService.getVariables(taskId);
		contractMain.setVariables(variables);
		return JsonMapper.getInstance().toJson(contractMain);
	}
	
	
	
	
	
	
	/**
	 * 删除采购合同管理
	 */
	@ResponseBody
	@RequiresPermissions("contractmaincreate:contractMain:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(ContractMain contractMain, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		contractMainService.delete(contractMain);
		j.setMsg("删除采购合同管理成功");
		return j;
	}
	
	/**
	 * 批量删除采购合同管理
	 */
	@ResponseBody
	@RequiresPermissions("contractmaincreate:contractMain:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			contractMainService.delete(contractMainService.get(id));
		}
		j.setMsg("删除采购合同管理成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("contractmaincreate:contractMain:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(ContractMain contractMain, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "采购合同管理"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<ContractMain> page = contractMainService.findPage(new Page<ContractMain>(request, response, -1), contractMain);
    		new ExportExcel("采购合同管理", ContractMain.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出采购合同管理记录失败！失败信息："+e.getMessage());
		}
			return j;
    }
    
    @ResponseBody
    @RequestMapping(value = "detail")
	public ContractMain detail(String id) {
		return contractMainService.get(id);
	}
	

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("contractmaincreate:contractMain:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<ContractMain> list = ei.getDataList(ContractMain.class);
			for (ContractMain contractMain : list){
				try{
					contractMainService.save(contractMain);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条采购合同管理记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条采购合同管理记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入采购合同管理失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/contractmain/contractMainCreate/?repage";

	}
	
	/**
	 * 下载导入采购合同管理数据模板
	 */
	@RequiresPermissions("contractmaincreate:contractMain:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "采购合同管理数据导入模板.xlsx";
    		List<ContractMain> list = Lists.newArrayList(); 
    		new ExportExcel("采购合同管理数据", ContractMain.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/contractmain/contractMainCreate/?repage";

	}
	
	/**
	 * 获取采购合同子表的物料信息
	 */
	@RequiresPermissions(value={"contractmaincreate:contractMain:view","contractmaincreate:contractMain:add","contractmaincreate:contractMain:edit"},logical=Logical.OR)
	@RequestMapping(value = "getDatailForm")	
	public Map<String, Object> getContractDetail(ContractMain contractMain, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<ContractDetail> page = contractMainService.findDetailPageService(new Page<ContractDetail>(request, response), contractMain); 
		return getBootstrapData(page);
	}
	/**
	 * 依据采购员id筛选下达给采购员的计划
	 * @param buyerId
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	
	@ResponseBody
	@RequestMapping(value = "selectPlan")
	public Map<String, Object> selectPlan(@RequestParam(required=false) String buyerId, @RequestParam(required=false) String accountId,HttpServletRequest request, HttpServletResponse response, Model model) {
		 PurPlanDetail purPlanDetail=new PurPlanDetail();
		 GroupBuyer groupBuyer=new GroupBuyer();
		 groupBuyer.setId(buyerId);
		 purPlanDetail.setBuyerId(groupBuyer);
		 Page<PurPlanDetail> page = contractMainService.selectPlan(new Page<PurPlanDetail>(request, response), purPlanDetail,accountId); 
		 
		 page.setCount(page.getList().size());
		 return getBootstrapData(page);
	}
	
	/**
	 * input:物料ID(编码)、物料数量、供应商Id
	 * output:物料的单价
	 */
	/** @ResponseBody
	 @RequestMapping(value = "getItemPrice")
	 public Double getItemPrice(@RequestParam(required=false) String itemId, @RequestParam(required=false) Double itemNum,@RequestParam(required=false) String accountId,HttpServletRequest request, HttpServletResponse response, Model model){
		    Double itemPrice = 0.0;
			List<PurSupPriceDetail> purSupPriceDetails=new ArrayList<PurSupPriceDetail>();
			purSupPriceDetails=contractMainService.getPurSupPriceList(itemId, itemNum,accountId);		
			if(purSupPriceDetails.size()>0){
				itemPrice=purSupPriceDetails.get(0).getSupPrice();
			}
		 return itemPrice;
	 }
	*/ 
	/** @ResponseBody
	 @RequestMapping(value = "getItemPrice")
	 public Double getItemPrice(@RequestParam(required=false) String itemId,  @RequestParam(required=false) String condate,@RequestParam(required=false) Double itemNum,@RequestParam(required=false) String accountId,HttpServletRequest request, HttpServletResponse response, Model model){
		   // Double itemPrice = 0.0;
		    if(itemId==null||"".equals(itemId)){
		    	return 0.0;
		    }
		    if(itemNum==null){
		    	return 0.0;
		    }
		    if(accountId==null||"".equals(accountId)){
		    	return 0.0;
		    }	
		    List<PurSupPriceDetail> purSupPriceDetails=new ArrayList<PurSupPriceDetail>();
			//依据数量、时间查询物料总额计算时所需要的阶梯价格(按最小值从小到大排序)
			purSupPriceDetails=contractMainService.getPurSupPriceList(itemId,condate,itemNum,accountId);
			Double restItemNum=itemNum;//剩余未进行总额计算的物料数量
			Double sumPrice=0.0;//物料的总额
			//上条记录的最大值（用于比较i区间的最大值与i+1区间的最小值是否相等判断价格区间是否是连续的）
			Double preMaxQty=0.0;
				for(int i=0;i<purSupPriceDetails.size();i++){//遍历阶梯价格，依据阶梯价格计算总额
					PurSupPriceDetail purSupPriceDetail=purSupPriceDetails.get(i);
					Double minQty=purSupPriceDetail.getMinQty();
					Double maxQty=purSupPriceDetail.getMaxQty();
					
					if(preMaxQty.equals(minQty)){//判断价格区间是否是连续的，不连续则导致计算总额错误
						Double supPrice=purSupPriceDetail.getSupPrice();
						if(i==purSupPriceDetails.size()-1){//最后一个价格区间的物料计算
							if(itemNum>minQty&&itemNum<=maxQty){//物料的数量是否介于最大与最小数量之间（左开右闭）
							    sumPrice=sumPrice+restItemNum*supPrice;  
							}else{
								return 0.0;
							}
						}else{//非最后一个价格区间的总额计算
							Double curQty=maxQty-minQty;
							restItemNum=restItemNum-curQty;
							sumPrice=sumPrice+curQty*supPrice;
						}
					}else{//价格区间不连续
						//返回总额0表示价格数据缺失，总额计算失败
						return 0.0;
					}
					//当前区间的最大数量赋值
					preMaxQty=maxQty;
				}
		 return sumPrice;
	 }*/
	//按阶梯价的计算方法
/*	 @ResponseBody
	 @RequestMapping(value = "getItemPrice")
	 public AjaxJson getItemPrice(@RequestParam(required=true) String itemId, @RequestParam(required=true) Double itemNum,@RequestParam(required=true) String accountId,@RequestParam(required=true) String condate,HttpServletRequest request, HttpServletResponse response, Model model){
		    AjaxJson j = new AjaxJson();  
		    if(accountId==null||"".equals(accountId)){
		    	j.put("info","请选择供应商！");
			    j.put("sumPrice", 0.0);
		    	return j;
		    }
		    if(condate==null||"".equals(condate)){
		    	j.put("info","请输入签订日期！");
			    j.put("sumPrice", 0.0);
		    	return j;
		    }
		    if(itemId==null||"".equals(itemId)){
			    j.put("info","请选择物料！");
			    j.put("sumPrice", 0.0);
		    	return j;
		    }
		    if(itemNum==null){
		    	j.put("info","请输入签订的物料数量！");
			    j.put("sumPrice", 0.0);
		    	return j;
		    }
		    
			List<PurSupPriceDetail> purSupPriceDetails=new ArrayList<PurSupPriceDetail>();
			//依据数量、时间查询物料总额计算时所需要的阶梯价格(按最小值从小到大排序)
			purSupPriceDetails=contractMainService.getPurSupPriceList(itemId,condate,itemNum,accountId);
			Double restItemNum=itemNum;//剩余未进行总额计算的物料数量
			Double sumPrice=0.0;//物料的总额
			//上条记录的最大值（用于比较i区间的最大值与i+1区间的最小值是否相等判断价格区间是否是连续的）
			Double preMaxQty=0.0;
				for(int i=0;i<purSupPriceDetails.size();i++){//遍历阶梯价格，依据阶梯价格计算总额
					PurSupPriceDetail purSupPriceDetail=purSupPriceDetails.get(i);
					Double minQty=purSupPriceDetail.getMinQty();
					Double maxQty=purSupPriceDetail.getMaxQty();
					
					if(preMaxQty.equals(minQty)){//判断价格区间是否是连续的，不连续则导致计算总额错误
						Double supPrice=purSupPriceDetail.getSupPrice();
						if(i==purSupPriceDetails.size()-1){//最后一个价格区间的物料计算
							if(itemNum>minQty&&itemNum<=maxQty){//物料的数量是否介于最大与最小数量之间（左开右闭）
							    sumPrice=sumPrice+restItemNum*supPrice;  
							}else{
								//j.setMsg("供应商价格信息缺失！");
								j.put("info","lack");
							    j.put("sumPrice", 0.0);
								return j;
							}
						}else{//非最后一个价格区间的总额计算
							Double curQty=maxQty-minQty;
							restItemNum=restItemNum-curQty;
							sumPrice=sumPrice+curQty*supPrice;
						}
					}else{//价格区间不连续
						//返回总额0表示价格数据缺失，总额计算失败
						//j.setMsg("供应商价格信息缺失！");
						j.put("info","lack");
					    j.put("sumPrice", 0.0);
						return j;
					}
					//当前区间的最大数量赋值
					preMaxQty=maxQty;
				}
				if(sumPrice.equals(0.0)){
					j.put("info","lack");
				}
			    j.put("sumPrice", sumPrice);
		 return j;
	 }
	*/
	 
	 
//	 @ResponseBody
//	 @RequestMapping(value = "getItemPrice")
//	 public AjaxJson getItemPrice(@RequestParam(required=true) String itemId, @RequestParam(required=true) Double itemNum,@RequestParam(required=true) String accountId,@RequestParam(required=true) String condate,HttpServletRequest request, HttpServletResponse response, Model model){
//		    AjaxJson j = new AjaxJson();  
//		    if(accountId==null||"".equals(accountId)){
//		    	j.put("info","请选择供应商！");
//			    j.put("sumPrice", 0.0);
//		    	return j;
//		    }
//		    if(condate==null||"".equals(condate)){
//		    	j.put("info","请输入签订日期！");
//			    j.put("sumPrice", 0.0);
//		    	return j;
//		    }
//		    if(itemId==null||"".equals(itemId)){
//			    j.put("info","请选择物料！");
//			    j.put("sumPrice", 0.0);
//		    	return j;
//		    }
//		    if(itemNum==null){
//		    	j.put("info","请输入签订的物料数量！");
//			    j.put("sumPrice", 0.0);
//		    	return j;
//		    }
//		    
//			List<PurSupPriceDetail> purSupPriceDetails=new ArrayList<PurSupPriceDetail>();
//			//依据数量、时间查询物料总额计算时所需要的阶梯价格(按最小值从小到大排序)
//			purSupPriceDetails=contractMainService.getPurSupPriceList(itemId,condate,itemNum,accountId);
////			Double restItemNum=itemNum;//剩余未进行总额计算的物料数量
//			Double sumPrice=0.0;//物料的总额
//			Double supPrice=0.0;//物料的总额
//			//上条记录的最大值（用于比较i区间的最大值与i+1区间的最小值是否相等判断价格区间是否是连续的）
//			Double preMaxQty=0.0;
//				for(int i=0;i<purSupPriceDetails.size();i++){//遍历阶梯价格，依据阶梯价格计算总额
//					PurSupPriceDetail purSupPriceDetail=purSupPriceDetails.get(i);
//					Double minQty=purSupPriceDetail.getMinQty();
//					Double maxQty=purSupPriceDetail.getMaxQty();
//					
//					if(preMaxQty.equals(minQty)){//判断价格区间是否是连续的，不连续则导致计算总额错误
//						
////						if(i==purSupPriceDetails.size()-1){//最后一个价格区间的物料计算
////							if(itemNum>minQty&&itemNum<=maxQty){//物料的数量是否介于最大与最小数量之间（左开右闭）
////							    sumPrice=sumPrice+restItemNum*supPrice;  
////							}else{
////								//j.setMsg("供应商价格信息缺失！");
////								j.put("info","lack");
////							    j.put("sumPrice", 0.0);
////								return j;
////							}
////						}else{//非最后一个价格区间的总额计算
////							Double curQty=maxQty-minQty;
////							restItemNum=restItemNum-curQty;
////							sumPrice=sumPrice+curQty*supPrice;
////						}
//						if(itemNum>minQty&&itemNum<=maxQty){//物料的数量是否介于最大与最小数量之间（左开右闭）
//							supPrice=purSupPriceDetail.getSupPrice();
//							sumPrice=supPrice*itemNum;
//							break;//结束循环
//						}else{
//							if(i==purSupPriceDetails.size()-1){//最后一个价格区间的物料计算
//								//j.setMsg("供应商价格信息缺失！");
//								j.put("info","lack");
//							    j.put("sumPrice", 0.0);
//								return j;
//							}
//						}
//					}else{//价格区间不连续
//						//返回总额0表示价格数据缺失，总额计算失败
//						//j.setMsg("供应商价格信息缺失！");
//						j.put("info","lack");
//					    j.put("sumPrice", 0.0);
//						return j;
//					}
//					//当前区间的最大数量赋值
//					preMaxQty=maxQty;
//				}
//				if(sumPrice.equals(0.0)){
//					j.put("info","lack");
//				}
//			    j.put("sumPrice", sumPrice);//物料含税总额
//			    j.put("supPrice",supPrice);//物料含税单价
//		 return j;
//	 }
	 
	 @ResponseBody
	 @RequestMapping(value = "getItemPrice")
	 public AjaxJson getItemPrice(@RequestParam(required=true) String itemId, @RequestParam(required=true) Double itemNum,@RequestParam(required=true) String accountId,@RequestParam(required=true) String condate,HttpServletRequest request, HttpServletResponse response, Model model){
		    AjaxJson j = new AjaxJson();  
		    if(accountId==null||"".equals(accountId)){
		    	j.put("info","请选择供应商！");
			    j.put("sumPrice", 0.0);
		    	return j;
		    }
		    if(condate==null||"".equals(condate)){
		    	j.put("info","请输入签订日期！");
			    j.put("sumPrice", 0.0);
		    	return j;
		    }
		    if(itemId==null||"".equals(itemId)){
			    j.put("info","请选择物料！");
			    j.put("sumPrice", 0.0);
		    	return j;
		    }
		    if(itemNum==null){
		    	j.put("info","请输入签订的物料数量！");
			    j.put("sumPrice", 0.0);
		    	return j;
		    }
		    
			List<PurSupPriceDetail> purSupPriceDetails=new ArrayList<PurSupPriceDetail>();
			//依据数量、时间查询物料总额计算时所需要的阶梯价格(按最小值从小到大排序)
			purSupPriceDetails=contractMainService.getPurSupPriceList(itemId,condate,itemNum,accountId);
//			Double restItemNum=itemNum;//剩余未进行总额计算的物料数量
			Double sumPrice=0.0;//物料的总额
			Double supPrice=0.0;//物料的总额
			//上条记录的最大值（用于比较i区间的最大值与i+1区间的最小值是否相等判断价格区间是否是连续的）
			Double preMaxQty=0.0;
				for(int i=0;i<purSupPriceDetails.size();i++){//遍历阶梯价格，依据阶梯价格计算总额
					PurSupPriceDetail purSupPriceDetail=purSupPriceDetails.get(i);
					Double minQty=purSupPriceDetail.getMinQty();
					Double maxQty=purSupPriceDetail.getMaxQty();
					
				/*	if(preMaxQty.equals(minQty)){//判断价格区间是否是连续的，不连续则导致计算总额错误
						if(itemNum>minQty&&itemNum<=maxQty){//物料的数量是否介于最大与最小数量之间（左开右闭）
							supPrice=purSupPriceDetail.getSupPrice();
							sumPrice=supPrice*itemNum;
							System.out.println("物料数量+++++++++++++++++++++++++++++++"+itemNum);
							System.out.println("物料含税单价+++++++++++++++++++++++++++++++"+supPrice);
							System.out.println("物料含税总额+++++++++++++++++++++++++++++++"+sumPrice);
							break;//结束循环
						}else{
							if(i==purSupPriceDetails.size()-1){//最后一个价格区间的物料计算
								//j.setMsg("供应商价格信息缺失！");
								j.put("info","lack");
							    j.put("sumPrice", 0.0);
								return j;
							}
						}
					}else{//价格区间不连续
						//返回总额0表示价格数据缺失，总额计算失败
						//j.setMsg("供应商价格信息缺失！");
						j.put("info","lack");
					    j.put("sumPrice", 0.0);
						return j;
					}
					//当前区间的最大数量赋值
					preMaxQty=maxQty;
				*/
					if(itemNum>minQty&&itemNum<=maxQty){//物料的数量是否介于最大与最小数量之间（左开右闭）
						supPrice=purSupPriceDetail.getSupPrice();
						sumPrice=supPrice*itemNum;
//						System.out.println("物料数量+++++++++++++++++++++++++++++++"+itemNum);
//						System.out.println("物料含税单价+++++++++++++++++++++++++++++++"+supPrice);
//						System.out.println("物料含税总额+++++++++++++++++++++++++++++++"+sumPrice);
						break;//结束循环
					}else{
						if(i==purSupPriceDetails.size()-1){//最后一个价格区间的物料计算
							//j.setMsg("供应商价格信息缺失！");
							j.put("info","lack");
						    j.put("sumPrice", 0.0);
							return j;
						}
					}
					
				}
				if(sumPrice.equals(0.0)){
					j.put("info","lack");
				}
			    j.put("sumPrice", sumPrice);//物料含税总额
			    j.put("supPrice",supPrice);//物料含税单价
		 return j;
	 }
	
	
}