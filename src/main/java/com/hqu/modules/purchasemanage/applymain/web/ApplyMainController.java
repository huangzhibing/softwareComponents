/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.purchasemanage.applymain.web;


import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
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
import com.google.common.collect.Maps;
import com.hqu.modules.basedata.item.entity.Item;
import com.hqu.modules.inventorymanage.invaccount.entity.InvAccount;
import com.hqu.modules.inventorymanage.invaccount.service.InvAccountService;
import com.hqu.modules.purchasemanage.applymain.entity.ApplyMain;
import com.hqu.modules.purchasemanage.applymain.entity.ItemCustmer;
import com.hqu.modules.purchasemanage.applymain.service.ApplyMainService;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.excel.ExportExcel;
import com.jeeplus.common.utils.excel.ImportExcel;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.modules.act.entity.Act;
import com.jeeplus.modules.act.service.ActTaskService;
import com.jeeplus.modules.sys.entity.Office;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.service.OfficeService;
import com.jeeplus.modules.sys.utils.UserUtils;

/**
 * 采购需求Controller
 * @author syc
 * @version 2018-04-21
 */
@Controller
@RequestMapping(value = "${adminPath}/applymain/applyMain")
public class ApplyMainController extends BaseController {

	@Autowired
	private ApplyMainService applyMainService;
	
	@Autowired
	private InvAccountService invAccountService;
	
	@Autowired
	private OfficeService officeService;
	
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
	
	@ModelAttribute
	public ApplyMain get(@RequestParam(required=false) String id) {
		
		ApplyMain entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = applyMainService.get(id);
		}
		if (entity == null){
			entity = new ApplyMain();
		}
		return entity;
	}
	
	/**
	 * 采购需求列表页面
	 */
	@RequiresPermissions("applymain:applyMain:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "purchasemanage/applymain/applyMainList";
	}
	
	
	@RequestMapping(value = "listCheck")
	public String listCheck() {
		return "purchasemanage/applymain/applyMainListCheck";
	}
	
	
	@RequestMapping(value = "deleteA")
	public String deleteA(ApplyMain applyMain, Model model) {
		applyMain.setBillStateFlag("正在录入");
		model.addAttribute(applyMain);
		return "purchasemanage/applymain/applyMainForm_delete";
	}
	
	@RequestMapping(value = "deleteA2")
	public String deleteA2(ApplyMain applyMain, RedirectAttributes redirectAttributes,Model model) {
		AjaxJson j = new AjaxJson();
		applyMainService.delete(applyMain);
		return "redirect:"+Global.getAdminPath()+"/applymain/applyMain/?repage";
	}
	
	
	
	/**
	 * 详情页面
	 */
	@RequiresPermissions(value={"applymain:applyMain:view","applymain:applyMain:add","applymain:applyMain:edit"},logical=Logical.OR)
	@RequestMapping(value = "detail_1")
	public String detail_1(ApplyMain applyMain, Model model) {
		if(StringUtils.isBlank(applyMain.getId())){//如果ID是空为添加
			//设置billnum
			String billNum = applyMainService.getBillNum();
			applyMain.setBillNum(billNum);
			//设置状态
			applyMain.setBillStateFlag("正在录入");
			//设置制单日期
			applyMain.setMakeDate(new Date());
			//获取当前用户
			User user = UserUtils.getUser();
			//设置User对象
			applyMain.setUser(user);
			applyMain.setMakeEmpname(user.getName());
			model.addAttribute("isAdd", true);
			model.addAttribute("applyMain", applyMain);
			return "purchasemanage/applymain/applyMainForm";
		}
		applyMain.setBillStateFlag("正在录入");
		model.addAttribute("applyMain", applyMain);
		return "purchasemanage/applymain/applyMainForm_detail";
	}
	
	
	/**
	 * 采购需求列表数据
	 */
	@ResponseBody
	@RequiresPermissions("applymain:applyMain:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(ApplyMain applyMain, HttpServletRequest request, HttpServletResponse response, Model model) {
		//只能看标志为A的
		applyMain.setBillStateFlag("A");
		//只能看自己的
		//applyMain.setUser(UserUtils.getUser());
		
		Page<ApplyMain> page = applyMainService.findPage(new Page<ApplyMain>(request, response), applyMain); 
		return getBootstrapData(page);
	}
	
	
	@ResponseBody
	@RequiresPermissions("applymain:applyMain:list")
	@RequestMapping(value = "checkData")
	public Map<String, Object> checkData(ApplyMain applyMain, HttpServletRequest request, HttpServletResponse response, Model model) {
		//只能看标志为A的
		applyMain.setBillStateFlag("W");
		//只能看自己的
		//applyMain.setUser(UserUtils.getUser());
		
		Page<ApplyMain> page = applyMainService.findPage(new Page<ApplyMain>(request, response), applyMain); 
		return getBootstrapData(page);
	}
	
	/**
	 * office需求列表数据
	 */
	@ResponseBody
	@RequiresPermissions("applymain:applyMain:list")
	@RequestMapping(value = "officeData")
	public Map<String, Object> officeData(Office office, HttpServletRequest request, HttpServletResponse response, Model model) {
		
		Page<Office> page = applyMainService.findOfficePage(new Page<Office>(request, response), office); 
		return getBootstrapData(page);
	}

	/**
	 * User需求列表数据
	 */
	@ResponseBody
	@RequiresPermissions("applymain:applyMain:list")
	@RequestMapping(value = "userData")
	public Map<String, Object> userData(User user, HttpServletRequest request, HttpServletResponse response, Model model) {
		
		Page<User> page = applyMainService.findUserPage(new Page<User>(request, response), user); 
		return getBootstrapData(page);
	}
	
	/**
	 * Item需求列表数据
	 */
	@ResponseBody
	@RequiresPermissions("applymain:applyMain:list")
	@RequestMapping(value = "itemData")
	public Map<String, Object> itemData(Item item, HttpServletRequest request, HttpServletResponse response, Model model) {
		
		Page<Item> page = applyMainService.findItemPage(new Page<Item>(request, response), item); 
		return getBootstrapData(page);
	}
	
	
	
	
	
	/**
	 * 查看，增加，编辑采购需求表单页面
	 */
	@RequiresPermissions(value={"applymain:applyMain:view","applymain:applyMain:add","applymain:applyMain:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(ApplyMain applyMain, Model model) {
		String name = UserUtils.getUser().getName();
		Act act = applyMain.getAct();
		act.setAssignee(UserUtils.getUser().getNo());
		act.setAssigneeName(UserUtils.getUser().getLoginName());
		applyMain = applyMainService.getByProInId(act.getProcInsId());
		String processInstanceId = act.getProcInsId();
		
		Task task = taskService.createTaskQuery().processInstanceId(processInstanceId).singleResult();
		if(task!=null){
			act.setTaskId(task.getId());
		}
		
		
		if(processInstanceId==null||"".equals(processInstanceId)){
		//通过key启动流程实例
			//ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("purApp");
			identityService.setAuthenticatedUserId(UserUtils.getUser().getLoginName());
			Map<String,Object> vars = Maps.newHashMap();
			vars.put("applyUserId", UserUtils.getUser().getLoginName());
			ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("purApp", vars);
			
			processInstanceId = processInstance.getId();
			task = taskService.createTaskQuery().processInstanceId(processInstanceId).singleResult();
			act.setTaskId(task.getId());
			act.setProcInsId(processInstanceId);
			act.setTaskName(task.getName());
		}
		applyMain.setAct(act);
//		List<Task> taskList = taskService.createTaskQuery()
//			.processInstanceId(processInstanceId)
//			.taskAssignee(name)
//			.list();
//		
//		if(taskList==null||taskList.size()==0){
//			//
//		}
		
		applyMain.setProcessInstanceId(processInstanceId);
		applyMain.setMakeDate(new Date());
		if(StringUtils.isBlank(applyMain.getId())){//如果ID是空为添加
			//设置billnum
			String billNum = applyMainService.getBillNum();
			applyMain.setBillNum(billNum);
			//设置状态
			applyMain.setBillStateFlag("正在录入");
			//设置制单日期
			applyMain.setMakeDate(new Date());
			//获取当前用户
			User user = UserUtils.getUser();
			//设置User对象
			applyMain.setUser(user);
			applyMain.setMakeEmpname(user.getName());
			model.addAttribute("isAdd", true);
			model.addAttribute("applyMain", applyMain);
			return "purchasemanage/applymain/applyMainForm";
		}
		applyMain.setBillStateFlag("正在修改");
		model.addAttribute("applyMain", applyMain);
		//判断流程是否结束
		boolean processFinish = applyMainService.isProcessFinish(processInstanceId);
		if(processFinish){
			return "purchasemanage/applymain/applyMainFormPCom";
		}
		return "purchasemanage/applymain/applyMainForm";
	}

	/**
	 * 保存采购需求
	 */
	@RequiresPermissions(value={"applymain:applyMain:add","applymain:applyMain:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(ApplyMain applyMain, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, applyMain)){
			return form(applyMain, model);
		}
		
		//向ADT_DETAIL表中插入数据
		//通过appty中的applytypeid去获取工作流模型并找到发起人
		applyMainService.insertIntoADTDetail(applyMain);
		
		
		//修改状态
		applyMain.setBillStateFlag("W");
		//设置部门编码
		//applyMain.setApplyDeptId(applyMain.getOffice().getCode());
		//获取用户所在部门的CODE
		String userOfficeCode = UserUtils.getUserOfficeCode();
		applyMain.setUserDeptCode(userOfficeCode);
		//设置登陆人的编码和所在部门编码
		User user = UserUtils.getUser();
		applyMain.setOffice(user.getOffice());
		applyMain.setUser(user);
		//历史记录
		applyMain = applyMainService.setHist(applyMain);
		
		//新增或编辑表单保存
		applyMainService.mySave(applyMain);//保存
		Map<String,Object> vars = Maps.newHashMap();
		//vars.put("applyUserId", UserUtils.getUser().getLoginName());
		
		//工作流结束任务
		actTaskService.complete(applyMain.getAct().getTaskId(), applyMain.getAct().getProcInsId(), applyMain.getAct().getComment(),applyMain.getBillNum(),vars);
		
//		List<Task> taskList = taskService.createTaskQuery()
//			.processInstanceId(applyMain.getProcessInstanceId())
//			.list();
//		for(Task temp:taskList){
//			String tId = temp.getId();
//			//完成任务
//			taskService.complete(tId);
//		}
		addMessage(redirectAttributes, "保存采购需求成功");
		return "redirect:" + adminPath + "/act/task";
		//return "redirect:"+Global.getAdminPath()+"/applymain/applyMain/?repage";
	}
	
	/**
	 * 删除采购需求
	 */
	@ResponseBody
	@RequiresPermissions("applymain:applyMain:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(ApplyMain applyMain, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		applyMainService.delete(applyMain);
		j.setMsg("删除采购需求成功");
		return j;
	}
	
	

	
	
	
	/**
	 * 批量删除采购需求
	 */
	@ResponseBody
	@RequiresPermissions("applymain:applyMain:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			applyMainService.delete(applyMainService.get(id));
		}
		j.setMsg("删除采购需求成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("applymain:applyMain:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(ApplyMain applyMain, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "采购需求"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<ApplyMain> page = applyMainService.findPage(new Page<ApplyMain>(request, response, -1), applyMain);
    		new ExportExcel("采购需求", ApplyMain.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出采购需求记录失败！失败信息："+e.getMessage());
		}
			return j;
    }
    
    @ResponseBody
    @RequestMapping(value = "detail")
	public ApplyMain detail(String id) {
		return applyMainService.get(id);
	}
	

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("applymain:applyMain:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<ApplyMain> list = ei.getDataList(ApplyMain.class);
			for (ApplyMain applyMain : list){
				try{
					applyMainService.save(applyMain);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条采购需求记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条采购需求记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入采购需求失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/applymain/applyMain/?repage";
    }
	
	/**
	 * 下载导入采购需求数据模板
	 */
	@RequiresPermissions("applymain:applyMain:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "采购需求数据导入模板.xlsx";
    		List<ApplyMain> list = Lists.newArrayList(); 
    		new ExportExcel("采购需求数据", ApplyMain.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/applymain/applyMain/?repage";
    }
	
	//public InvAccount getInvAccountByItemCode(HttpServletRequest request){
	@ResponseBody
	@RequestMapping(value="getInvAccountByItemCode")
	public AjaxJson getInvAccountByItemCode(String id){
		//String itemCode=request.getParameter("id");
		//String itemCode=id;
		//InvAccountPur invAccount=new InvAccountPur();
		Item item=new Item(); 
		//item.setCode(itemCode);
		item.setId(id);
		InvAccount inv = new InvAccount();
		inv.setItem(item);
		//System.out.println("itemCode+++++++++++++++++++++++++"+itemCode);
		//InvAccountPur invAccountPur=new InvAccountPur();
		inv=invAccountService.get(inv);
		AjaxJson j = new AjaxJson();
		return j;
		
	}
	
	
	/**
	 * 物料定义列表数据
	 */
	
	
	/**
	 * 采购需求列表数据
	 */
	@ResponseBody
	@RequiresPermissions("applymain:applyMain:list")
	@RequestMapping(value = "myData")
	public Map<String, Object> myData(ItemCustmer itemCustmer, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page p = new Page<ItemCustmer>(request, response);
		Page<ItemCustmer> page = applyMainService.findMyPage(p, itemCustmer);
		page.setCount(page.getList().size());
		
		return getBootstrapData(page);
	}
	
	@ResponseBody
	@RequestMapping(value = "jax")
	public String jax( ApplyMain applyMain, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, applyMain)){
			return form(applyMain, model);
		}
		
		//先删再加
		String billNum = applyMain.getBillNum();
		ApplyMain applyMain2 = new ApplyMain();
		applyMain2.setBillNum(billNum);
		List<ApplyMain> findList = applyMainService.findList(applyMain2);
		Iterator<ApplyMain> it = findList.iterator();
		while(it.hasNext()){
			ApplyMain next = it.next();
			//applyMainService.delete(next);
		}
		
		//修改状态
		applyMain.setBillStateFlag("A");
		//设置部门编码
		//applyMain.setApplyDeptId(applyMain.getOffice().getCode());
		//设置登陆人的编码和所在部门编码
		User user = UserUtils.getUser();
		applyMain.setOffice(user.getOffice());
		applyMain.setUser(user);
		//历史记录
		applyMain = applyMainService.setHist(applyMain);
		//新增或编辑表单保存
		applyMainService.mySave(applyMain);//保存
		//addMessage(redirectAttributes, "保存采购需求成功");
		return "success";
	}
	
	
}