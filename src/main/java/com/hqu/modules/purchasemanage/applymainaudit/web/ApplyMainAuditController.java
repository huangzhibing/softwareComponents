/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.purchasemanage.applymainaudit.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
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
import com.hqu.modules.purchasemanage.adtdetail.entity.AdtDetail;
import com.hqu.modules.purchasemanage.adtdetail.service.AdtDetailService;
import com.hqu.modules.purchasemanage.adtmodel.entity.AdtModel;
import com.hqu.modules.purchasemanage.adtmodel.service.AdtModelService;
import com.hqu.modules.purchasemanage.applymain.service.ApplyMainService;
import com.hqu.modules.purchasemanage.applymainaudit.entity.ApplyDetailAudit;
import com.hqu.modules.purchasemanage.applymainaudit.entity.ApplyMainAudit;
import com.hqu.modules.purchasemanage.applymainaudit.service.ApplyMainAuditService;
import com.hqu.modules.purchasemanage.applytype.entity.ApplyType;
import com.hqu.modules.purchasemanage.applytype.service.ApplyTypeService;
import com.hqu.modules.purchasemanage.rollplannewquery.entity.RollPlanNew;
import com.hqu.modules.purchasemanage.rollplannewquery.service.RollPlanNewService;
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
import com.jeeplus.modules.sys.entity.Role;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.mapper.RoleMapper;
import com.jeeplus.modules.sys.utils.UserUtils;

/**
 * 采购需求审批Controller
 * @author syc
 * @version 2018-05-08
 */
@Controller
@RequestMapping(value = "${adminPath}/applymainaudit/applyMainAudit")
public class ApplyMainAuditController extends BaseController {

	@Autowired
	private ApplyMainService applyMainService;
	
	@Autowired
	protected RuntimeService runtimeService;
	@Autowired
	private ActTaskService actTaskService;
	@Autowired
	protected TaskService taskService;
	@Autowired
	private ApplyMainAuditService applyMainAuditService;
	
	@Autowired
	private AdtModelService adtModelService;
	
	@Autowired
	private AdtDetailService adtDetailService;
	
	@Autowired
	private RoleMapper roleMapper;
	
	@Autowired
	private RollPlanNewService rollPlanNewService;
	
	@Autowired
	private ApplyTypeService applyTypeService;
	
	@ModelAttribute
	public ApplyMainAudit get(@RequestParam(required=false) String id) {
		ApplyMainAudit entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = applyMainAuditService.get(id);
		}
		if (entity == null){
			entity = new ApplyMainAudit();
		}
		return entity;
	}
	
	/**
	 * 采购需求审批列表页面
	 * @RequiresPermissions("applymain:applyMain:checkList")
	 */
	
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "purchasemanage/applymainaudit/applyMainAuditList";
	}
	
	@RequestMapping(value = "myList")
	public String myList(ApplyMainAudit applyMainAudit, Model model, RedirectAttributes redirectAttributes) {
		
		addMessage(redirectAttributes, "保存采购需求审批成功");
		return "redirect:"+Global.getAdminPath()+"/applymainaudit/applyMainAudit/?repage";
	}
	
		/**
	 * 采购需求审批列表数据
	 * @RequiresPermissions("applymain:applyMain:checkList")
	 */
	@ResponseBody
	
	@RequestMapping(value = "data")
	public Map<String, Object> data(ApplyMainAudit applyMainAudit, HttpServletRequest request, HttpServletResponse response, Model model) {

		Page<ApplyMainAudit> page = applyMainAuditService.findPage(new Page<ApplyMainAudit>(request, response), applyMainAudit); 
		//page.setCount(page.getList().size());
		//分页
        String intPage= request.getParameter("pageNo");
        int pageNo=Integer.parseInt(intPage);
        int pageSize= page.getPageSize();
        List<ApplyMainAudit> pageList=page.getList();

        if(pageNo==1&&pageSize==-1){
    	   page.setList(pageList);
         }else{
    	   List<ApplyMainAudit> reportL=  new ArrayList<ApplyMainAudit>();        
	        for(int i=(pageNo-1)*pageSize;i<pageList.size() && i<pageNo*pageSize;i++){
	        	reportL.add(pageList.get(i));
		    } 
	        page.setList(reportL);

       }

		
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑采购需求审批表单页面
	 * @RequiresPermissions("applymain:applyMain:checkList")
	 * @RequiresPermissions(value={"applymainaudit:applyMainAudit:view","applymainaudit:applyMainAudit:add","applymainaudit:applyMainAudit:edit"},logical=Logical.OR)
	 */
	
	
	@RequestMapping(value = "form")
	public String form(ApplyMainAudit applyMainAudit, Model model) {
		
		
		Act act = applyMainAudit.getAct();
		// 环节编号
		String taskDefKey = act.getTaskDefKey();
		String procInsId = act.getProcInsId();	
		applyMainAudit = applyMainAuditService.getByProInId(procInsId);
		applyMainAudit = applyMainAuditService.get(applyMainAudit.getId());
		applyMainAudit.setAct(act);
		//		if ("deptLeaderAudit".equals(taskDefKey)){
//			view = "leaveAudit";
//		}
		
		
		
		
		if(applyMainAudit!=null){
			String billStateFlag = applyMainAudit.getBillStateFlag();
			if("W".equals(billStateFlag)){
				applyMainAudit.setBillStateFlag("录入完毕");
			}
			if("E".equals(billStateFlag)){
				applyMainAudit.setBillStateFlag("审核通过");
			}
		}
		model.addAttribute("applyMainAudit", applyMainAudit);
		if(StringUtils.isBlank(applyMainAudit.getId())){//如果ID是空为添加
			model.addAttribute("isAdd", true);
		}
		
		boolean processFinish = applyMainService.isProcessFinish(procInsId);
		if(processFinish){
			return "purchasemanage/applymainaudit/applyMainAuditFormPCom";
		}
		return "purchasemanage/applymainaudit/applyMainAuditForm";
	}

	
	
	
	
	
	@RequestMapping(value = "reForm")
	public String reForm(ApplyMainAudit applyMainAudit, Model model) {
		
		
		Act act = applyMainAudit.getAct();
		// 环节编号
		String taskDefKey = act.getTaskDefKey();
		String procInsId = act.getProcInsId();	
		applyMainAudit = applyMainAuditService.getByProInId(procInsId);
		applyMainAudit = applyMainAuditService.get(applyMainAudit.getId());
		applyMainAudit.setAct(act);
		//		if ("deptLeaderAudit".equals(taskDefKey)){
//			view = "leaveAudit";
//		}
		
		
		
		
		if(applyMainAudit!=null){
			String billStateFlag = applyMainAudit.getBillStateFlag();
			if("W".equals(billStateFlag)){
				applyMainAudit.setBillStateFlag("录入完毕");
			}
			if("E".equals(billStateFlag)){
				applyMainAudit.setBillStateFlag("审核通过");
			}
		}
		model.addAttribute("applyMainAudit", applyMainAudit);
		if(StringUtils.isBlank(applyMainAudit.getId())){//如果ID是空为添加
			model.addAttribute("isAdd", true);
		}
		boolean processFinish = applyMainService.isProcessFinish(procInsId);
		if(processFinish){
			return "purchasemanage/applymainaudit/applyMainAuditFormPCom";
		}
		return "purchasemanage/applymainaudit/applyMainReAuditForm";
	}
	
	
	
	/**
	 * 保存采购需求审批
	 * @RequiresPermissions(value={"applymainaudit:applyMainAudit:add","applymainaudit:applyMainAudit:edit"},logical=Logical.OR)
	
	 */
	@RequestMapping(value = "save")
	public String save(ApplyMainAudit applyMainAudit, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, applyMainAudit)){
			return form(applyMainAudit, model);
		}
		//添加历史
		applyMainAudit = applyMainAuditService.setHist(applyMainAudit);
		//新增或编辑表单保存
		applyMainAuditService.save(applyMainAudit);//保存
		addMessage(redirectAttributes, "保存采购需求审批成功");
		
		String taskDefKey = applyMainAudit.getAct().getTaskDefKey();
		String exp = null;
		if ("purAppAud1".equals(taskDefKey)){
			exp = "pass1";
		}
		if ("purAppAud2".equals(taskDefKey)){
			exp = "pass2";
		}
		if ("purAppAud3".equals(taskDefKey)){
			exp = "pass3";
		}
		if ("reAud".equals(taskDefKey)){
			exp = "pass4";
		}
		Map<String, Object> vars = Maps.newHashMap();
		vars.put(exp,applyMainAudit.getAct().getFlag());
		//vars.put(exp, "yes".equals(applyMainAudit.getAct().getFlag())? true : false);
//		List<Task> taskList = taskService.createTaskQuery().taskAssignee(UserUtils.getUser().getName())
//				.processInstanceId(applyMainAudit.getProcessInstanceId())
//				.list();
//		for(Task temp:taskList){
//			actTaskService.complete(temp.getId(), applyMainAudit.getAct().getProcInsId(), applyMainAudit.getAct().getComment(),applyMainAudit.getBillNum(), vars);
//		}
		actTaskService.complete(applyMainAudit.getAct().getTaskId(), applyMainAudit.getAct().getProcInsId(), applyMainAudit.getAct().getComment(),applyMainAudit.getBillNum(), vars);
		// 提交流程任务
		
		String fg =  applyMainAudit.getAct().getFlag();
		if("yes".equals(fg)&&"pass3".equals(exp)){
			//插入滚动计划
			insertInToRollPlanNew(applyMainAudit);
		}
		if("end".equals(fg)){
			//插入滚动计划
			insertInToRollPlanNew(applyMainAudit);
		}
		return "redirect:" + adminPath + "/act/task";
		//return "redirect:"+Global.getAdminPath()+"/applymainaudit/applyMainAudit/?repage";
	}
	
	/**
	 * 删除采购需求审批
	 */
	@ResponseBody
	@RequiresPermissions("applymainaudit:applyMainAudit:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(ApplyMainAudit applyMainAudit, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		applyMainAuditService.delete(applyMainAudit);
		j.setMsg("删除采购需求审批成功");
		return j;
	}
	
	/**
	 * 批量删除采购需求审批
	 */
	@ResponseBody
	@RequiresPermissions("applymainaudit:applyMainAudit:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			applyMainAuditService.delete(applyMainAuditService.get(id));
		}
		j.setMsg("删除采购需求审批成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("applymainaudit:applyMainAudit:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(ApplyMainAudit applyMainAudit, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "采购需求审批"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<ApplyMainAudit> page = applyMainAuditService.findPage(new Page<ApplyMainAudit>(request, response, -1), applyMainAudit);
    		new ExportExcel("采购需求审批", ApplyMainAudit.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出采购需求审批记录失败！失败信息："+e.getMessage());
		}
			return j;
    }
    
    @ResponseBody
    @RequestMapping(value = "detail")
	public ApplyMainAudit detail(String id) {
		return applyMainAuditService.get(id);
	}
	

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("applymainaudit:applyMainAudit:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<ApplyMainAudit> list = ei.getDataList(ApplyMainAudit.class);
			for (ApplyMainAudit applyMainAudit : list){
				try{
					applyMainAuditService.save(applyMainAudit);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条采购需求审批记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条采购需求审批记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入采购需求审批失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/applymainaudit/applyMainAudit/?repage";
    }
	
	/**
	 * 下载导入采购需求审批数据模板
	 */
	@RequiresPermissions("applymainaudit:applyMainAudit:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "采购需求审批数据导入模板.xlsx";
    		List<ApplyMainAudit> list = Lists.newArrayList(); 
    		new ExportExcel("采购需求审批数据", ApplyMainAudit.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/applymainaudit/applyMainAudit/?repage";
    }
	
	
	
	
	/**
	 * 审批保存 ajaax
	 */
	@ResponseBody
	@RequestMapping(value = "aajax")
	public String aajax( String userId,ApplyMainAudit applyMainAudit, RedirectAttributes redirectAttributes) {
	//	String id = dbPurReport.getId();
//		//判断合格、不合格、部分合格
//		purReport = purReportService.subsave(purReport);
//		//设置为编辑中
//		purReport.setState("编辑中");
//		purReportService.updata(purReport);//保存
		//purReportService.executeUpdateSql("");
		applyMainAuditService.save(applyMainAudit);//保存
		addMessage(redirectAttributes, "保存检验单成功");
		return "success";
	}
	
	
	/**
	 * 跳转到输入审核意见弹窗
	 * @RequiresPermissions("purplan:purPlanMainCheck:list")
	 */
	
	@RequestMapping(value = "checkList")
	public String checkList(Model model,ApplyMainAudit applyMainAudit ,String yes) {

//		NextUser nextUser = new NextUser();
//		nextUser.setDeptId(1);
//		nextUser.setName("demo");
//		model.addAttribute("nextUser", nextUser);
		//先保存
//		applyMainAuditService.save(applyMainAudit);

		// 从表中读取数据
		AdtModel adtModel = new AdtModel();
		adtModel.setModelCode("APP001");
		List<Role> roleList = new ArrayList<Role>();
		List<AdtModel> findList = adtModelService.findList(adtModel);
		Iterator<AdtModel> it = findList.iterator();
		while(it.hasNext()){
			AdtModel adtModelTemp = it.next();
			Role role = adtModelTemp.getRole();
			
			roleList.add(role);
		}
		
		if(yes.equals("yes")){
			Role endRole = new Role();
			endRole.setId("end");
			endRole.setName("结束");
			roleList.add(endRole);
			model.addAttribute("adtDetail", new AdtDetail());
			model.addAttribute("roleList", roleList);
			model.addAttribute("applyMainAudit", applyMainAudit);
			return "purchasemanage/applymainaudit/checkComments";
		}

		
		model.addAttribute("adtDetail", new AdtDetail());
		model.addAttribute("roleList", roleList);
		model.addAttribute("applyMainAudit", applyMainAudit);
		return "purchasemanage/applymainaudit/checkCommentsNo";
	}
	
	@RequiresPermissions(value={"applymainaudit:applyMainAudit:add","applymainaudit:applyMainAudit:edit"},logical=Logical.OR)
	@RequestMapping(value = "subSave")
	public String subSave(ApplyMainAudit applyMainAudit, Model model, RedirectAttributes redirectAttributes) throws Exception{
		//adtDetailService.nextStep(billNum, billType, modelCode, isFirst, role);
		//新增或编辑表单保存
		applyMainAuditService.save(applyMainAudit);//保存
		addMessage(redirectAttributes, "保存采购需求审批成功");
		return "redirect:"+Global.getAdminPath()+"/applymainaudit/applyMainAudit/?repage";
	}
	
	//向采购计划中插入
	public void insertInToRollPlanNew(ApplyMainAudit applyMainAudit){
		if(applyMainAudit==null){
			return;
		}
		applyMainAudit = applyMainAuditService.get(applyMainAudit.getId());
		applyMainAudit.setBillStateFlag("E");
		applyMainAuditService.save(applyMainAudit);
		List<ApplyDetailAudit> applyDetailAuditList = applyMainAudit.getApplyDetailAuditList();
		Iterator<ApplyDetailAudit> it = applyDetailAuditList.iterator();
		while(it.hasNext()){
			RollPlanNew rollPlanNew = new RollPlanNew(); 
			ApplyDetailAudit applyDetailAudit = it.next();
			String billNum = applyMainAudit.getBillNum();
			if(billNum!=null&&!billNum.equals("")){
				rollPlanNew.setBillNum(billNum);
			}
			
			//
			String applytypeid = applyMainAudit.getApplyType().getApplytypeid();
			ApplyType applyType = new ApplyType();
			applyType.setApplytypeid(applytypeid);
			List<ApplyType> findList = applyTypeService.findList(applyType);
			if(findList!=null&&findList.size()!=0){
				ApplyType applyType2 = findList.get(0);
				rollPlanNew.setApplyTypeId(applyType2);
				rollPlanNew.setApplyTypeName(applyType2.getApplytypename());
			}
			
			rollPlanNew.setBillType("采购需求");
			
			Integer serialNum = applyDetailAudit.getSerialNum();
			if(serialNum!=null){
				rollPlanNew.setSerialNum(serialNum);
			}
			
			
			Item item = applyDetailAudit.getItem();
			if(item!=null){
				rollPlanNew.setItemCode(item);
			}
			
			
			String itemName = applyDetailAudit.getItemName();
			if(itemName!=null&&!itemName.equals("")){
				rollPlanNew.setItemName(itemName);
			}
		
			String itemSpecmodel = applyDetailAudit.getItemSpecmodel();
			if(itemSpecmodel!=null&&!itemSpecmodel.equals("")){
				rollPlanNew.setItemSpecmodel(itemSpecmodel);
			}
			
			String unitName = applyDetailAudit.getUnitName();
			if(unitName!=null&&!unitName.equals("")){
				rollPlanNew.setUnitName(unitName);
			}
			
			Integer applyQty = applyDetailAudit.getApplyQty();
			if(applyQty!=null){
				rollPlanNew.setApplyQty(applyQty);
			}
			
			
			Date requestDate = applyDetailAudit.getRequestDate();
			if(requestDate!=null){
				rollPlanNew.setRequestDate(requestDate);
			}
			
			String notes = applyDetailAudit.getNotes();
			if(notes!=null&&!notes.equals("")){
				rollPlanNew.setNotes(notes);
			}
			
			Date planArrivedate = applyDetailAudit.getPlanArrivedate();
			if(planArrivedate!=null){
				rollPlanNew.setPurArriveDate(planArrivedate);
			}
			
			if(applyQty!=null){
				rollPlanNew.setPurQty(applyQty*1.0);
			}
			
			//rollPlanNew.setPurQty(applyDetailAudit.getApplyQty());
			//
			if(!applyDetailAudit.getNowSum().equals("")){
				rollPlanNew.setInvQty(Double.parseDouble(applyDetailAudit.getNowSum()));
			}
			
			Date makeDate = applyMainAudit.getMakeDate();
			if(makeDate!=null){
				rollPlanNew.setMakeDate(makeDate);
			}
			
			Office office = applyMainAudit.getOffice();
			if(office!=null){
				rollPlanNew.setApplyDeptId(office);
			}
			
			String applyDept = applyMainAudit.getApplyDept();
			if(applyDept!=null&&!applyDept.equals("")){
					rollPlanNew.setApplyDept(applyDept);
			}
		
			User user = applyMainAudit.getUser();
			if(user!=null){
				rollPlanNew.setMakeEmpid(user);
			}
			
			String makeEmpname = applyMainAudit.getMakeEmpname();
			if(makeEmpname!=null&&!makeEmpname.equals("")){
				rollPlanNew.setMakeEmpname(makeEmpname);
			}
			
			//
			String costPrice = applyDetailAudit.getCostPrice();
			if(!costPrice.equals("")&&!costPrice.equals("")){
				rollPlanNew.setCostPrice(costPrice);
				//
				try{
					rollPlanNew.setPlanPrice(Double.parseDouble(costPrice));
				}catch(Exception e){
					e.printStackTrace();
				}
			}
			
			Integer applySum = applyDetailAudit.getApplySum();
			if(applySum!=null){
				rollPlanNew.setPlanSum(applySum*1.0);
				//rollPlanNew.setPlanPrice(costPrice*1.0);
				
			}
			
			
			String itemTexture = applyDetailAudit.getItemTexture();
			if(itemTexture!=null&&!itemTexture.equals("")){
				rollPlanNew.setItemTexture(itemTexture);
			}
			//来源标志写1
			rollPlanNew.setSourseFlag("1");
			
			
			
			rollPlanNewService.save(rollPlanNew);
		}
		
	}
	
	/**
	 * 审核通过写入原行，插入新行
	 * @RequiresPermissions(value={"applymainaudit:applyMainAudit:add","applymainaudit:applyMainAudit:edit"},logical=Logical.OR)
	
	 */
	@RequestMapping(value = "subSaveAdt")
	public String subSaveAdt(AdtDetail adtDetail1, Model model, RedirectAttributes redirectAttributes) throws Exception{
		//先根据billNum，updata审核人信息，并修改isfinished字段，如果下一个审核人是结束，在修改finishFlag,并修改业务主表。
		String billNum = adtDetail1.getBillNum();
		AdtDetail adtDetail = new AdtDetail();
		adtDetail.setIsFinished("0");
		adtDetail.setBillNum(billNum);
		Integer step = 0;
		List<AdtDetail> findList = adtDetailService.findList(adtDetail);
		if(findList!=null&&findList.size()!=0){
			AdtDetail adtDetail2 = findList.get(0);
			//获取当前对象的信息
			User user = UserUtils.getUser();
			Office office = user.getOffice();
			adtDetail2.setJpersonCode(user.getNo());
			adtDetail2.setJustifyPerson(user.getName());
			adtDetail2.setJdeptCode(office.getCode());
			adtDetail2.setJdeptName(office.getName());
			adtDetail2.setJustifyResult("通过");
			adtDetail2.setJustifyDate(new Date());
			adtDetail2.setIsFinished("1");
			step = adtDetail2.getStep();
			
			if(adtDetail1.getJpositionCode().equals("end")){
				adtDetail2.setFinishFlag("1");
				
				ApplyMainAudit applyMainAudit = new ApplyMainAudit();
				applyMainAudit.setBillNum(billNum);
				List<ApplyMainAudit> findList2 = applyMainAuditService.findList(applyMainAudit);
				if(findList2!=null&&findList2.size()!=0){
					ApplyMainAudit applyMainAudit2 = findList2.get(0);
					applyMainAudit2.setBillStateFlag("E");
					applyMainAuditService.save(applyMainAudit2);
					//插入滚动计划
					insertInToRollPlanNew(applyMainAudit2);
				}
			}
			//保存
			adtDetailService.save(adtDetail2);
		}
		//如果下一个审核人不是end，向adtdetail中插入根据下一个审核人插入新的一行
		if(!adtDetail1.getJpositionCode().equals("end")){
			AdtDetail adtDetail2 = new AdtDetail();
			adtDetail2.setBillNum(billNum);
			adtDetail2.setBillTypeCode("APP001");
			adtDetail2.setBillTypeName("采购需求");
			String code = adtDetail1.getJpositionCode();
			adtDetail2.setJpositionCode(code);
			Role role = roleMapper.get(code);
			if(role!=null){
				adtDetail2.setJpositionName(role.getName());
			}
			adtDetail2.setModelCode("APP001");
			adtDetail2.setStep(++step);
			adtDetail2.setIsFinished("0");
			adtDetail2.setFinishFlag("0");
			adtDetailService.save(adtDetail2);
		}
		
		//新增或编辑表单保存
//		applyMainAuditService.save(applyMainAudit);//保存
//		addMessage(redirectAttributes, "保存采购需求审批成功");
		return "purchasemanage/applymainaudit/applyMainAuditList";
//		return "redirect:"+Global.getAdminPath()+"purchasemanage/applymainaudit/applyMainAuditList";
	}
	
	
	
	/**
	 * 审核不通过写入原行，插入新行
	 * @RequiresPermissions(value={"applymainaudit:applyMainAudit:add","applymainaudit:applyMainAudit:edit"},logical=Logical.OR)
	
	 */
	@RequestMapping(value = "subSaveAdtNo")
	public String subSaveAdtNo(AdtDetail adtDetail1, Model model, RedirectAttributes redirectAttributes) throws Exception{
		//先根据billNum，updata审核人信息，并修改isfinished字段，如果下一个审核人是结束，在修改finishFlag,并修改业务主表。
		String billNum = adtDetail1.getBillNum();
		AdtDetail adtDetail = new AdtDetail();
		adtDetail.setIsFinished("0");
		adtDetail.setBillNum(billNum);
		List<AdtDetail> findList = adtDetailService.findList(adtDetail);
		if(findList!=null&&findList.size()!=0){
			AdtDetail adtDetail2 = findList.get(0);
			//获取当前对象的信息
			User user = UserUtils.getUser();
			Office office = user.getOffice();
			adtDetail2.setJpersonCode(user.getNo());
			adtDetail2.setJustifyPerson(user.getName());
			adtDetail2.setJdeptCode(office.getCode());
			adtDetail2.setJdeptName(office.getName());
			adtDetail2.setJustifyResult("不通过");
			adtDetail2.setJustifyDate(new Date());
			adtDetail2.setIsFinished("1");
			adtDetail2.setJustifyRemark(adtDetail1.getJustifyRemark());
			/*
			if(adtDetail1.getJpositionCode().equals("end")){
				adtDetail2.setFinishFlag("1");
				
				ApplyMainAudit applyMainAudit = new ApplyMainAudit();
				applyMainAudit.setBillNum(billNum);
				List<ApplyMainAudit> findList2 = applyMainAuditService.findList(applyMainAudit);
				if(findList2!=null&&findList2.size()!=0){
					ApplyMainAudit applyMainAudit2 = findList2.get(0);
					applyMainAudit2.setBillStateFlag("E");
					applyMainAuditService.save(applyMainAudit2);
					//插入滚动计划
					insertInToRollPlanNew(applyMainAudit2);
				}
			}*/
			//保存
			adtDetailService.save(adtDetail2);
			//向业务表中修改状态为B
			ApplyMainAudit applyMainAudit = new ApplyMainAudit();
			applyMainAudit.setBillNum(billNum);
			List<ApplyMainAudit> findList2 = applyMainAuditService.findList(applyMainAudit);
			Iterator<ApplyMainAudit> it = findList2.iterator();
			while(it.hasNext()){
				ApplyMainAudit next = it.next();
				next.setBillStateFlag("B");
				
				next.setAuditOpinion(next.getAuditOpinion()+"*"+adtDetail1.getJustifyRemark()+";   \r\n");
				applyMainAuditService.save(next);
				applyMainAuditService.save(next);
				applyMainAuditService.save(next);
				applyMainAuditService.save(next);
				applyMainAuditService.save(next);
			}
		}
		//如果下一个审核人不是end，向adtdetail中插入根据下一个审核人插入新的一行
		/*
		if(!adtDetail1.getJpositionCode().equals("end")){
			AdtDetail adtDetail2 = new AdtDetail();
			adtDetail2.setBillNum(billNum);
			adtDetail2.setBillTypeCode("APP001");
			adtDetail2.setBillTypeName("采购需求");
			String code = adtDetail1.getJpositionCode();
			adtDetail2.setJpositionCode(code);
			Role role = roleMapper.get(code);
			if(role!=null){
				adtDetail2.setJpositionName(role.getName());
			}
			adtDetail2.setModelCode("APP001");
			adtDetail2.setIsFinished("0");
			adtDetail2.setFinishFlag("0");
			adtDetailService.save(adtDetail2);
		}
		*/
		//新增或编辑表单保存
//		applyMainAuditService.save(applyMainAudit);//保存
//		addMessage(redirectAttributes, "保存采购需求审批成功");
		return "purchasemanage/applymainaudit/applyMainAuditList";
//		return "redirect:"+Global.getAdminPath()+"purchasemanage/applymainaudit/applyMainAuditList";
	}
	

}