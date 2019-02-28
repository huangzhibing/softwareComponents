/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.purchasemanage.contractmain.web;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import org.activiti.engine.HistoryService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.task.Comment;
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
import com.jeeplus.modules.oa.entity.TestAudit;
import com.jeeplus.modules.sys.entity.Role;
import com.jeeplus.modules.sys.service.SystemService;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.excel.ExportExcel;
import com.jeeplus.common.utils.excel.ImportExcel;
import com.hqu.modules.purchasemanage.adtbilltype.entity.AdtBillType;
import com.hqu.modules.purchasemanage.adtdetail.entity.AdtDetail;
import com.hqu.modules.purchasemanage.adtdetail.service.AdtDetailService;
import com.hqu.modules.purchasemanage.adtmodel.service.AdtModelService;
import com.hqu.modules.purchasemanage.contractmain.entity.ContractMain;
import com.hqu.modules.purchasemanage.contractmain.service.ContractMainCheckService;
import com.hqu.modules.purchasemanage.purplan.entity.NextUser;

/**
 * 采购合同管理Controller
 * @author ltq
 * @version 2018-04-28
 */
@Controller
@RequestMapping(value = "${adminPath}/contractmain/contractMainCheck")
public class ContractMainCheckController extends BaseController {

	@Autowired
	private ContractMainCheckService contractMainService;
	@Autowired
	private AdtDetailService  adtDetailService;
	@Autowired
	private SystemService systemService;
	@Autowired
	private AdtModelService adtModelService;
	
	@Autowired
	private HistoryService historyService;
	@Autowired
	protected TaskService taskService;
	
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
	@RequiresPermissions("contractmaincheck:contractMain:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "purchasemanage/contractmain/contractMainCheckList";
	}
	
	/**
	 * 采购合同管理列表数据
	 */
	@ResponseBody
	@RequiresPermissions("contractmaincheck:contractMain:list")
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
	@RequiresPermissions(value={"contractmaincheck:contractMain:view","contractmainclose:contractMain:add","contractmainclose:contractMain:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(ContractMain contractMain, Model model) {
		//审核未通过时显示审核意见
		if(StringUtils.isNotBlank(contractMain.getAct().getTaskId())){					
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
		if(contractMain.getAct().isFinishTask()&&!StringUtils.isBlank(contractMain.getId())){
			//补全contractMaind对象中部分属性的数据
			contractMain=contractMainService.addFormContractMainInf(contractMain);
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
			if(StringUtils.isBlank(contractMain.getId())){//如果ID是空为添加
				model.addAttribute("isAdd", true);
			}		
			return "purchasemanage/contractmain/contractMainCheckForm";
		}
	}

	/**
	 * 审核通过提交采购合同管理
	 */
	@RequiresPermissions(value={"contractmaincheck:contractMain:add","contractmaincheck:contractMain:edit"},logical=Logical.OR)
	@RequestMapping(value = "passSave")
	public String passSave(ContractMain contractMain, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, contractMain)){
			return form(contractMain, model);
		}
		AdtDetail adtDetail=new AdtDetail();
		adtDetail.setBillNum(contractMain.getBillNum());

		adtDetail.setBillTypeCode("CON001");
		adtDetail.setBillTypeName("采购合同");
		adtDetail.setBillNum(contractMain.getBillNum());

		AdtDetail adt=new AdtDetail();
		adt.setBillNum(contractMain.getBillNum());
		adt.setIsFinished("0");
		List<AdtDetail> adts= adtDetailService.findList(adt);
		for(int k=0;k<adts.size();k++){
			AdtDetail a=adts.get(k);
			a.setIsFinished("1");
			adtDetailService.save(a);
		
		}
		String roleId=contractMain.getNextUser();//前台利用makeEmpname字段保存下一个审核人角色的ID
		if("end".equals(roleId)){                        //审核通过，结束工作流程
	     	adtDetailService.auditPass(adtDetail,true);
	     	contractMain.setBillStateFlag("E");
		}else {
			AdtBillType billType=new AdtBillType();
			billType.setBillTypeName("采购合同");
			billType.setBillTypeCode("CON001");
			Role role=systemService.getRole(roleId);
			adtDetailService.auditPass(adtDetail,false);
			adtDetailService.nextStep(contractMain.getBillNum(),billType,"CON",false,role);
			contractMain.setBillStateFlag("W");
		}
		//新增或编辑表单保存
	    contractMainService.save(contractMain);	   
		addMessage(redirectAttributes, "合同审核成功");
		return "redirect:"+Global.getAdminPath()+"/contractmain/contractMainCheck/?repage";
	}
	//审核不通过采购合同管理
	@RequiresPermissions(value={"contractmaincheck:contractMain:add","contractmaincheck:contractMain:edit"},logical=Logical.OR)
	@RequestMapping(value = "unpassSave")
	public String unpassSave(ContractMain contractMain, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, contractMain)){
			return form(contractMain, model);
		}
		AdtDetail adtDetail=new AdtDetail();
		adtDetail.setBillNum(contractMain.getBillNum());
		adtDetail.setBillTypeCode("CON001");
		adtDetail.setBillTypeName("采购合同");
		adtDetail.setJustifyRemark(contractMain.getJustifyRemark());
		//把上一个审核人的状态设置为已完成
		AdtDetail adt=new AdtDetail();
		adt.setBillNum(contractMain.getBillNum());
		adt.setIsFinished("0");
		List<AdtDetail> adts= adtDetailService.findList(adt);
		for(int k=0;k<adts.size();k++){
			AdtDetail a=adts.get(k);
			a.setIsFinished("1");
			adtDetailService.save(a);
		}
		//这里处理打回原制单人逻辑
		adtDetailService.auditFail(adtDetail,true);	
		//设置审核不通过状态
		contractMain.setBillStateFlag("B");
		//新增或编辑表单保存
	    contractMainService.save(contractMain);	   
		addMessage(redirectAttributes, "合同审核成功");
		return "redirect:"+Global.getAdminPath()+"/contractmain/contractMainCheck/?repage";
	}
	
	
	
	
	/**
	 * 跳转到输入审核意见弹窗
	 */
	@RequiresPermissions("contractmaincheck:contractMain:list")
	@RequestMapping(value = {"list"})
	//审核不通过
	public String list(ContractMain contractMain,Model model,HttpServletRequest request) {
	//	String contractMain1=(String)request.getParameter("contractMain");
		// System.out.println("request+++++++++++++++++"+request);
		List<NextUser> users=new ArrayList<NextUser>();
	/*	Map<String,String> roles=adtModelService.findRoleListByModelName("CON");
		for( Map.Entry<String,String> role: roles.entrySet()){
			NextUser u=new NextUser();
			u.setDeptId(role.getKey());
			u.setName(role.getValue());
			users.add(u);
		}
	*/	NextUser end=new NextUser();
		end.setName("打回原制单人");
		end.setDeptId("origin");
		users.add(end);
     //   System.out.println("contractMain+++++++++++++++++"+contractMain);
		NextUser nextUser = new NextUser();
		nextUser.setBillId(contractMain.getId());
		//AdtDetail adtDetail=new AdtDetail();
		model.addAttribute("nextUser",nextUser);
		model.addAttribute("nextCheckers", users);

		return "purchasemanage/contractmain/contractMainCheckComments";
	}

	/**
	 * 跳转到输入审核通过弹窗（指定下一个审核人）
	 */
	@RequiresPermissions("contractmaincheck:contractMain:list")
	@RequestMapping(value = {"pass"})
	public String pass(ContractMain contractMain,Model model) {

		List<NextUser> users=new ArrayList<NextUser>();
		Map<String,String> roles=adtModelService.findRoleListByModelName("CON");
		for( Map.Entry<String,String> role: roles.entrySet()){
			NextUser u=new NextUser();
			u.setDeptId(role.getKey());
			u.setName(role.getValue());
			users.add(u);
		}
		NextUser end=new NextUser();
		end.setName("结束");
		end.setDeptId("end");
		users.add(end);
		NextUser nextUser = new NextUser();
		nextUser.setBillId(contractMain.getId());
		model.addAttribute("nextUser", nextUser);
		model.addAttribute("nextCheckers", users);

		return "purchasemanage/contractmain/contractMainCheckNoComments";
	}

	
	
	
	/**
	 * 
	 
	@ResponseBody
	@RequiresPermissions(value={"contractmainclose:contractMain:add","contractmainclose:contractMain:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public AjaxJson save(ContractMain contractMain, Model model, RedirectAttributes redirectAttributes) throws Exception{
		AjaxJson j = new AjaxJson();
		if (!beanValidator(model, contractMain)){		
			j.setMsg("合同录入保存失败");
			return j;
		}
		//单据状态改为正在录入/修改
		contractMain.setBillStateFlag("A");
		//录入的合同
		contractMain.setContrState("C");
		//新增或编辑表单保存
		contractMainService.save(contractMain);//提交
		j.setMsg("合同生成保存成功");		
		return j;
	}
	*/
	
	/**
	 * 删除采购合同管理
	 */
	@ResponseBody
	@RequiresPermissions("contractmaincheck:contractMain:del")
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
	@RequiresPermissions("contractmaincheck:contractMain:del")
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
	@RequiresPermissions("contractmaincheck:contractMain:export")
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
	@RequiresPermissions("contractmaincheck:contractMain:import")
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
		return "redirect:"+Global.getAdminPath()+"/contractmain/contractMainCheck/?repage";

	}
	
	/**
	 * 下载导入采购合同管理数据模板
	 */
	@RequiresPermissions("contractmaincheck:contractMain:import")
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
		return "redirect:"+Global.getAdminPath()+"/contractmain/contractMainCheck/?repage";

	}		
	
	
	/**
	 * 工单执行（完成任务）
	 * @param testAudit
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "saveAudit")
	public String saveAudit(ContractMain contractMain, Model model,RedirectAttributes redirectAttributes) {
		/*if (StringUtils.isBlank(contractMain.getAct().getFlag())
				|| StringUtils.isBlank(contractMain.getAct().getComment())){
			addMessage(model, "请填写审核意见。");
			return form(contractMain, model);
		}*/
		try {
			if("no".equals(contractMain.getAct().getFlag())){
				if(contractMain.getJustifyRemark()==null||"".equals(contractMain.getJustifyRemark())){
					addMessage(model, "请填写审核意见。");
					return form(contractMain, model);
				}
			}
			contractMainService.save(contractMain);
			addMessage(redirectAttributes, "合同审核成功");
		}catch (Exception e) {
			addMessage(redirectAttributes, "系统内部错误！");
		}
		
		return "redirect:" + adminPath + "/act/task/todo/";
	}
	
	
}