/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.inventorymanage.invout.web;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import com.google.common.collect.Maps;
import com.jeeplus.modules.act.entity.Act;
import com.jeeplus.modules.act.service.ActTaskService;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.utils.UserUtils;
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
import com.hqu.modules.inventorymanage.invout.entity.InvOutMain;
import com.hqu.modules.inventorymanage.invout.service.InvOutMainService;

/**
 * 放行单Controller
 * @author M1ngz
 * @version 2018-06-02
 */
@Controller
@RequestMapping(value = "${adminPath}/invout/invOutMain")
public class InvOutMainController extends BaseController {

	@Autowired
	private InvOutMainService invOutMainService;
	@Autowired
	private ActTaskService actTaskService;
	
	@ModelAttribute
	public InvOutMain get(@RequestParam(required=false) String id) {
		InvOutMain entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = invOutMainService.get(id);
		}
		if (entity == null){
			entity = new InvOutMain();
		}
		return entity;
	}

	@RequiresPermissions("invout:invOutMain:list")
	@RequestMapping(value = {"print"})
	public String print() {
		return "inventorymanage/invout/print";
	}

	/**
	 * 放行单列表页面
	 */
	@RequiresPermissions("invout:invOutMain:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "inventorymanage/invout/invOutMainList";
	}
	
		/**
	 * 放行单列表数据
	 */
	@ResponseBody
	@RequiresPermissions("invout:invOutMain:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(InvOutMain invOutMain, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<InvOutMain> page = invOutMainService.findPage(new Page<InvOutMain>(request, response), invOutMain); 
		return getBootstrapData(page);
	}



	@RequestMapping(value = "todo")
	public String todoList(Act act, HttpServletResponse response, Model model) throws Exception {
		return "inventorymanage/invout/invOutMainTodoList";
	}

	@ResponseBody
	@RequestMapping(value = "todo/data")
	public  Map<String, Object> todoListData(Act act, HttpServletResponse response, Model model) throws Exception {
		act.setProcDefKey("outMain");
		List<HashMap<String,String>> list = actTaskService.todoList(act);
		List<InvOutMain> result = Lists.newArrayList();
		InvOutMain invOutMain = null;
		for(HashMap map : list){
			invOutMain = invOutMainService.getByProInsId((String)map.get("task.processInstanceId"));
			if(org.springframework.util.StringUtils.isEmpty(invOutMain)){
				continue;
			} else {
				invOutMain.setTemp(map);
				result.add(invOutMain);
			}
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("rows",result);
		map.put("total",result.size());
		return map;
	}
	/**
	 * 查看，增加，编辑放行单表单页面
	 */
	@RequiresPermissions(value={"invout:invOutMain:view","invout:invOutMain:add","invout:invOutMain:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(InvOutMain invOutMain, Model model) {
		String view = "invOutMainForm";

		// 查看审批申请单
		if (org.apache.commons.lang3.StringUtils.isNotBlank(invOutMain.getId())){//.getAct().getProcInsId())){
			if(org.springframework.util.StringUtils.isEmpty(invOutMain.getProcessInstanceId())){
				model.addAttribute("invOutMain", invOutMain);
				return "inventorymanage/invout/" + view;

			}
			// 环节编号
			String taskDefKey = invOutMain.getAct().getTaskDefKey();

			// 查看工单
			if(invOutMain.getAct().isFinishTask()){
				view = "invOutMainView";
			}
			// 修改环节
			else if ("outMainAudit".equals(taskDefKey)){
				view = "invOutMainAudit";
			}
			// 审核环节3
			else if ("modifyOutMain".equals(taskDefKey)){
				invOutMain.setBillFlag("A");
				view = "invOutMainForm";
			}
		} else {
			invOutMain.setBillFlag("A");

			Date date = new Date();
			invOutMain.setMakeDate(date);
			invOutMain.setAwayDate(date);
			SimpleDateFormat sdf =   new SimpleDateFormat( "yyyyMMdd" );
			String newBillNum = invOutMainService.getMaxId();
			if(StringUtils.isEmpty(newBillNum)){
				newBillNum = "0000";
			} else {
				newBillNum = String.format("%04d", Integer.parseInt(newBillNum.substring(11,15))+1);
			}
			invOutMain.setBillNum("IOM" + sdf.format(date) + newBillNum);

			User u = UserUtils.getUser();
			invOutMain.setMakeEmp(u);
			invOutMain.setMakeEmpName(u.getName());
			model.addAttribute("isAdd", true);
		}
		model.addAttribute("invOutMain", invOutMain);
		return "inventorymanage/invout/" + view;
	}

	/**
	 * 保存放行单
	 */
	@RequiresPermissions(value={"invout:invOutMain:add","invout:invOutMain:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(InvOutMain invOutMain, Model model, RedirectAttributes redirectAttributes,String start) throws Exception{
		if (!beanValidator(model, invOutMain)){
			return form(invOutMain, model);
		}
		//新增或编辑表单保存
		Map<String, Object> variables = Maps.newHashMap();
		boolean startProc = false;
		if("1".equals(start)){
			startProc = true;
		}
		User u = UserUtils.getUser();
		invOutMain.setMakeEmp(u);
		invOutMain.setMakeEmpName(u.getName());
		invOutMainService.save(invOutMain,variables,startProc);//保存
		addMessage(redirectAttributes, "保存放行单成功");
		if(!org.springframework.util.StringUtils.isEmpty(invOutMain.getProcessInstanceId())){
			model.addAttribute("invOutMain", invOutMain);
			return "redirect:"+Global.getAdminPath()+"/invout/invOutMain/todo?repage";

		}
		return "redirect:"+Global.getAdminPath()+"/invout/invOutMain/?repage";
	}
	@RequestMapping(value = "saveAudit")
	public String saveAudit(InvOutMain invOutMain, RedirectAttributes redirectAttributes, Map<String, Object> vars, Model model) {
		if (org.apache.commons.lang3.StringUtils.isBlank(invOutMain.getAct().getComment())){
			addMessage(model, "请填写审核意见。");
			return form(invOutMain, model);
		}
		invOutMainService.auditSave(invOutMain);
		addMessage(redirectAttributes, "审核成功");
		return "redirect:" + adminPath + "/invout/invOutMain/todo";
	}
	/**
	 * 删除放行单
	 */
	@ResponseBody
	@RequiresPermissions("invout:invOutMain:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(InvOutMain invOutMain, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		invOutMainService.delete(invOutMain);
		j.setMsg("删除放行单成功");
		return j;
	}
	
	/**
	 * 批量删除放行单
	 */
	@ResponseBody
	@RequiresPermissions("invout:invOutMain:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			invOutMainService.delete(invOutMainService.get(id));
		}
		j.setMsg("删除放行单成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("invout:invOutMain:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(InvOutMain invOutMain, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "放行单"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<InvOutMain> page = invOutMainService.findPage(new Page<InvOutMain>(request, response, -1), invOutMain);
    		new ExportExcel("放行单", InvOutMain.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出放行单记录失败！失败信息："+e.getMessage());
		}
			return j;
    }
    
    @ResponseBody
    @RequestMapping(value = "detail")
	public InvOutMain detail(String id) {
		return invOutMainService.get(id);
	}
	

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("invout:invOutMain:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<InvOutMain> list = ei.getDataList(InvOutMain.class);
			for (InvOutMain invOutMain : list){
				try{
					invOutMainService.save(invOutMain);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条放行单记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条放行单记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入放行单失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/invout/invOutMain/?repage";
    }
	
	/**
	 * 下载导入放行单数据模板
	 */
	@RequiresPermissions("invout:invOutMain:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "放行单数据导入模板.xlsx";
    		List<InvOutMain> list = Lists.newArrayList(); 
    		new ExportExcel("放行单数据", InvOutMain.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/invout/invOutMain/?repage";
    }
	

}