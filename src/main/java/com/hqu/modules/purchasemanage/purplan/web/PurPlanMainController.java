/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.purchasemanage.purplan.web;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import com.hqu.modules.purchasemanage.adtbilltype.entity.AdtBillType;
import com.hqu.modules.purchasemanage.adtdetail.entity.AdtDetail;
import com.hqu.modules.purchasemanage.adtdetail.service.AdtDetailService;
import com.hqu.modules.purchasemanage.supprice.entity.PurSupPriceDetail;
import com.jeeplus.modules.act.service.ActTaskService;
import com.jeeplus.modules.sys.utils.UserUtils;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.Task;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.excel.ExportExcel;
import com.jeeplus.common.utils.excel.ImportExcel;
import com.hqu.modules.Common.service.CommonService;
import com.hqu.modules.purchasemanage.purplan.entity.ItemExtend;
import com.hqu.modules.purchasemanage.purplan.entity.PurPlanDetail;
import com.hqu.modules.purchasemanage.purplan.entity.PurPlanMain;
import com.hqu.modules.purchasemanage.purplan.service.PurPlanMainService;

/**
 * 采购计划Controller
 * @author yangxianbang
 * @version 2018-05-09
 */
@Controller
@RequestMapping(value = "${adminPath}/purplan/purPlanMain")
public class PurPlanMainController extends BaseController {

	@Autowired
	private PurPlanMainService purPlanMainService;
	@Autowired
	private CommonService commonService;
	@Autowired
	private AdtDetailService adtDetailService;

	@Autowired
	private RepositoryService repositoryService;
	@Autowired
	private ActTaskService actTaskService;
	@Autowired
	private TaskService taskService;
	
	@ModelAttribute
	public PurPlanMain get(@RequestParam(required=false) String id) {
		PurPlanMain entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = purPlanMainService.get(id);
		}
		if (entity == null){
			entity = new PurPlanMain();
		}
		return entity;
	}
	
	/**
	 * 采购计划列表页面
	 */
	@RequiresPermissions("purplan:purPlanMain:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "purchasemanage/purplan/purPlanMainList";
	}
	
		/**
	 * 采购计划列表数据
	 */
	@ResponseBody
	@RequiresPermissions("purplan:purPlanMain:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(PurPlanMain purPlanMain, HttpServletRequest request, HttpServletResponse response, Model model) {
		purPlanMain.setBillType("M");
		purPlanMain.setMakeEmpid(UserUtils.getUser());
		//查找状态为A,B（录入中和不通过）的计划
		Page<PurPlanMain> page = purPlanMainService.findABPage(new Page<PurPlanMain>(request, response), purPlanMain);
		return getBootstrapData(page);
	}
	
	/**
	 * 采购计划物料数据
	 */
	@ResponseBody
	@RequiresPermissions(value= {"purplan:purPlanMain:add","purplan:purPlanMain:edit",})
	@RequestMapping(value = "itemsdata")
	public Map<String, Object> itemsdata(ItemExtend item, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<ItemExtend> page = purPlanMainService.findItemPage(new Page<ItemExtend>(request, response), item); 
		return getBootstrapData(page);
	}

	@RequestMapping(value = "startForm")
	public String startForm(PurPlanMain purPlanMain, Model model){
		model.addAttribute("purPlanMain", purPlanMain);
		return "purchasemanage/purplan/purPlanMainStartForm";
	}

	/**
	 * 查看，增加，编辑采购计划表单页面
	 */
	@RequiresPermissions(value={"purplan:purPlanMain:view","purplan:purPlanMain:add","purplan:purPlanMain:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(PurPlanMain purPlanMain, Model model,@RequestParam(required=false) String detail) {

		//设置工作流模型ID
		/*List<ProcessDefinition> processDefinitions= repositoryService.createProcessDefinitionQuery().latestVersion().processDefinitionKey("pur_plan").list();
		if(processDefinitions.size()>0){
			purPlanMain.getAct().setProcDefId(processDefinitions.get(0).getId());
		}*/
		model.addAttribute("purPlanMain", purPlanMain);
		if(StringUtils.isBlank(purPlanMain.getId())){//如果ID是空为添加
			model.addAttribute("isAdd", true);
			model.addAttribute("billNum",getBillNum());
			purPlanMain.setBillStateFlag("A");//设置单据状态为正在录入/修改
			purPlanMain.setMakeDate(new Date());
		}else if(detail==null) {
			model.addAttribute("isEdit", true);
		}
		if("B".equals(purPlanMain.getBillStateFlag())) {
			List<AdtDetail> adtDetails = adtDetailService.getHistoryDetails(purPlanMain.getBillNum(), "PLN001");
			if (adtDetails != null&&adtDetails.size()>1) {
				AdtDetail d=adtDetails.get(adtDetails.size() - 2);
				SimpleDateFormat dataFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date date=new Date();
				if(d.getJustifyDate()!=null){
					date=d.getJustifyDate();
				}
				purPlanMain.setJustifyRemark(d.getJustifyPerson()+"("+dataFormat.format(date)+"):\n"+d.getJustifyRemark());  //设置审核人不通过的意见
			}
		}
		if(StringUtils.isNotBlank(purPlanMain.getId())&&null!=purPlanMain.getAct().getTaskDefKey()) {
			String view = "";
			String taskKey = purPlanMain.getAct().getTaskDefKey();
			// 查看工单
			if (purPlanMain.getAct().isFinishTask()) {
				view = "purPlanMainQueryForm";
			}// 修改环节
			else if ("reInput".equals(taskKey)) {
				if("C".equals(purPlanMain.getBillType())) {  //要调用生成还是录入界面
					view="purPlanMainDraftReApplyForm";
				}else{
					view = "purPlanMainReApplyForm";
				}

			}
			// 审核环节
			else if ("planAudit1".equals(taskKey)) {
				view = "purPlanMainAuditForm";
			}
			else if ("planAudit2".equals(taskKey)) {
				view = "purPlanMainAuditForm";
			}
			return "purchasemanage/purplan/"+view;
		}
		if("B".equals(purPlanMain.getBillStateFlag())){
			/*purPlanMain.getAct().setProcIns(actTaskService.getProcIns(purPlanMain.getAct().getProcInsId()));
			if(purPlanMain.getAct().getProcIns()!=null) {
				purPlanMain.getAct().setTaskDefKey(purPlanMain.getAct().getProcIns().getActivityId());
			}*/
			/*if(purPlanMain.getAct().getProcInsId()!=null) {
				List<Task> taskList = taskService.createTaskQuery().active().processInstanceId(purPlanMain.getAct().getProcInsId()).list();
			}*/
			for(HashMap<String,String> map :actTaskService.todoList(purPlanMain.getAct())){
				if(map.get("task.processInstanceId").equals(purPlanMain.getAct().getProcInsId())){
					purPlanMain.getAct().setTaskDefKey(map.get("task.taskDefinitionKey"));
					purPlanMain.getAct().setTaskId(map.get("task.id"));
				}
			}
			return "purchasemanage/purplan/purPlanMainReApplyForm";
		}

		return "purchasemanage/purplan/purPlanMainForm";
	}

	/**
	 * 获取物料价格
	 * 输入:物料ID(编码)、物料数量、供应商Id
	 * 输出:物料的单价
	 */
	@ResponseBody
	@RequestMapping(value = "getItemPrice")
	public AjaxJson getItemPrice(@RequestParam(required=false) String itemId, @RequestParam(required=false) Double itemNum,@RequestParam(required=false) String accountId,HttpServletRequest request, HttpServletResponse response, Model model){
		AjaxJson j=new AjaxJson();
		List<PurSupPriceDetail> purSupPriceDetails=new ArrayList<PurSupPriceDetail>();
		//依据数量、时间查询物料总额计算时所需要的阶梯价格(按最小值从小到大排序)
		purSupPriceDetails=purPlanMainService.getPurSupPriceList(itemId,itemNum,accountId);
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
						sumPrice=itemNum*supPrice;//应最新的用户要求，不要阶梯价格。注释此行即变为阶梯价格
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
					sumPrice=itemNum*supPrice;//应最新的用户要求，不要阶梯价格。注释此行即变为阶梯价格
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


	/**
	 * 保存采购计划
	 */
	@RequiresPermissions(value={"purplan:purPlanMain:add","purplan:purPlanMain:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(PurPlanMain purPlanMain, Model model, RedirectAttributes redirectAttributes ) throws Exception{
		if (!beanValidator(model, purPlanMain)){
			return form(purPlanMain, model,null);
		}
		purPlanMain.setBillType("M");//设置单据类型 录入
		
		//计算采购计划总金额
		List<PurPlanDetail> details=purPlanMain.getPurPlanDetailList();
		double sum=0;
		for(int i = 0;i<details.size();i++) {
			if(details.get(i).getPlanSum() != null && details.get(i).getPlanSum() > 0&&"0".equals(details.get(i).getDelFlag())) {
				sum+=details.get(i).getPlanSum();
			}
		}
		purPlanMain.setPlanPriceSum(sum);
		if("W".equals(purPlanMain.getBillStateFlag())){
		    purPlanMainService.submitSave(purPlanMain);//提交时保存并启动工作流
        }else{
            //新增或编辑表单保存
            purPlanMainService.save(purPlanMain);//保存
        }

		/**
		 * 旧版菜单式审核，非框架工作流
		 */
/*		if("W".equals(purPlanMain.getBillStateFlag())){//状态为录入完毕W 时 ，即提交到第一个审核人
			AdtBillType billType =new AdtBillType();
			billType.setBillTypeCode("PLN001");//审核流转表中单据类型写死为 PLN001/采购计划 APP001/采购需求 CON001/采购合同
			billType.setBillTypeName("采购计划");
			adtDetailService.nextStep(purPlanMain.getBillNum(),billType,"PLN",true,null);//往审核流转表插入第一个审核数据
		}

		return "redirect:"+Global.getAdminPath()+"/purplan/purPlanMain/?repage";*/
		addMessage(redirectAttributes, "保存采购计划成功");
		return "redirect:"+Global.getAdminPath()+"/act/task/todo/?repage";
	}


	/**
	 * 调整采购计划
	 */
	@RequiresPermissions(value={"purplan:purPlanMain:edit"},logical=Logical.OR)
	@RequestMapping(value = "reApplySave")
	public String reApplySave(PurPlanMain purPlanMain, Model model, RedirectAttributes redirectAttributes ) throws Exception{
		if (!beanValidator(model, purPlanMain)){
			return form(purPlanMain, model,null);
		}

		//计算采购计划总金额
		List<PurPlanDetail> details=purPlanMain.getPurPlanDetailList();
		double sum=0;
		for(int i = 0;i<details.size();i++) {
			if(details.get(i).getPlanSum() != null && details.get(i).getPlanSum() > 0&&"0".equals(details.get(i).getDelFlag())) {
				sum+=details.get(i).getPlanSum();
			}
		}
		purPlanMain.setPlanPriceSum(sum);

		purPlanMainService.auditSave(purPlanMain);//提交时保存并提交工作流


		addMessage(redirectAttributes, "保存采购计划成功");
		return "redirect:"+Global.getAdminPath()+"/act/task/todo/?repage";
	}


	/**
	 * 工单执行（完成任务）
	 * @param purPlanMain
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "saveAudit")
	public String saveAudit(PurPlanMain purPlanMain, Model model) {
		if (StringUtils.isBlank(purPlanMain.getAct().getFlag())
				|| StringUtils.isBlank(purPlanMain.getAct().getComment())){
			addMessage(model, "请填写审核意见。");
			return form(purPlanMain, model,null);
		}
		//计算采购计划总金额
		List<PurPlanDetail> details=purPlanMain.getPurPlanDetailList();
		double sum=0;
		for(int i = 0;i<details.size();i++) {
			if(details.get(i).getPlanSum() != null && details.get(i).getPlanSum() > 0&&"0".equals(details.get(i).getDelFlag())) {
				sum+=details.get(i).getPlanSum();
			}
		}
		purPlanMain.setPlanPriceSum(sum);
		purPlanMainService.auditSave(purPlanMain);
		return "redirect:" + adminPath + "/act/task";
	}


	
	/**
	 * 删除采购计划
	 */
	@ResponseBody
	@RequiresPermissions("purplan:purPlanMain:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(PurPlanMain purPlanMain, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		purPlanMainService.delete(purPlanMain);
		j.setMsg("删除采购计划成功");
		return j;
	}
	
	/**
	 * 批量删除采购计划
	 */
	@ResponseBody
	@RequiresPermissions("purplan:purPlanMain:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			purPlanMainService.delete(purPlanMainService.get(id));
		}
		j.setMsg("删除采购计划成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("purplan:purPlanMain:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(PurPlanMain purPlanMain, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "采购计划"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<PurPlanMain> page = purPlanMainService.findPage(new Page<PurPlanMain>(request, response, -1), purPlanMain);
    		new ExportExcel("采购计划", PurPlanMain.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出采购计划记录失败！失败信息："+e.getMessage());
		}
			return j;
    }
    
    @ResponseBody
    @RequestMapping(value = "detail")
	public PurPlanMain detail(String id) {
		return purPlanMainService.get(id);
	}
	

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("purplan:purPlanMain:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<PurPlanMain> list = ei.getDataList(PurPlanMain.class);
			for (PurPlanMain purPlanMain : list){
				try{
					purPlanMainService.save(purPlanMain);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条采购计划记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条采购计划记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入采购计划失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/purplan/purPlanMain/?repage";
    }
	
	/**
	 * 下载导入采购计划数据模板
	 */
	@RequiresPermissions("purplan:purPlanMain:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "采购计划数据导入模板.xlsx";
    		List<PurPlanMain> list = Lists.newArrayList(); 
    		new ExportExcel("采购计划数据", PurPlanMain.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/purplan/purPlanMain/?repage";
    }
	
	private String getBillNum() {
		Date date = new Date();
		Random random = new Random();
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
		String randStr=("0000"+random.nextInt(10000));
		String billn="pln"+df.format(date.getTime())+randStr.substring(randStr.length()-4,randStr.length());
		while(commonService.getCodeNum("pur_planmain", "bill_num", billn)) {
			randStr=("0000"+random.nextInt(10000));
			billn="pln"+df.format(date.getTime())+randStr.substring(randStr.length()-4,randStr.length());
		}
		return billn;
	}


}