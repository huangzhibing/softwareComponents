/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.salemanage.pickbill.web;

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
import javax.validation.Validator;

import com.google.common.collect.Maps;
import com.jeeplus.modules.act.entity.Act;
import com.jeeplus.modules.act.service.ActTaskService;
import com.jeeplus.modules.oa.entity.Leave;
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
import com.hqu.modules.salemanage.pickbill.entity.PickBill;
import com.hqu.modules.salemanage.pickbill.service.PickBillService;

/**
 * 销售发货单据Controller
 * @author M1ngz
 * @version 2018-05-15
 */
@Controller
@RequestMapping(value = "${adminPath}/pickbill/pickBill")
public class PickBillController extends BaseController {

	@Autowired
	private PickBillService pickBillService;
	@Autowired
	private ActTaskService actTaskService;
	
	@ModelAttribute
	public PickBill get(@RequestParam(required=false) String id) {
		PickBill entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = pickBillService.get(id);
		}
		if (entity == null){
			entity = new PickBill();
		}
		return entity;
	}
	
	/**
	 * 单据列表页面
	 */
	@RequiresPermissions("pickbill:pickBill:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "salemanage/pickbill/pickBillList";
	}
	/**
	 * 单据列表页面
	 */
	@RequiresPermissions("pickbill:pickBill:list")
	@RequestMapping(value = "query")
	public String query(Model model) {
		return "salemanage/pickbill/pickBillQueryList";
	}
	
		/**
	 * 单据列表数据
	 */
	@ResponseBody
	@RequiresPermissions("pickbill:pickBill:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(PickBill pickBill, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<PickBill> page = pickBillService.findPage(new Page<PickBill>(request, response), pickBill); 
		return getBootstrapData(page);
	}

	@RequestMapping(value = "todo")
	public String todoList(Act act, HttpServletResponse response, Model model) throws Exception {
		return "salemanage/pickbill/pickBillTodoList";
	}

	@ResponseBody
	@RequestMapping(value = "todo/data")
	public  Map<String, Object> todoListData(Act act, HttpServletResponse response, Model model,PickBill pickBill) throws Exception {
		act.setProcDefKey("pickBill");
		List<HashMap<String,String>> list = actTaskService.todoList(act);
		List<PickBill> result = Lists.newArrayList();
		PickBill tempBill = null;
		for(HashMap map : list){
			tempBill = pickBillService.getByProInsId((String)map.get("task.processInstanceId"));
			if(tempBill != null && StringUtils.isNotEmpty(pickBill.getPbillCode())) {
				if(!tempBill.getPbillCode().contains(pickBill.getPbillCode())) {
					continue;
				}
			}
			if(org.springframework.util.StringUtils.isEmpty(tempBill)){
				continue;
			} else {
				tempBill.setTemp(map);
				result.add(tempBill);
			}
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("rows",result);
		map.put("total",result.size());
		return map;
	}

	/**
	 * 查看，增加，编辑单据表单页面
	 */
	@RequiresPermissions(value={"pickbill:pickBill:view","pickbill:pickBill:add","pickbill:pickBill:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(PickBill pickBill, Model model, String type) {

		String view = "pickBillForm";

		if(StringUtils.isNotEmpty(type)) {
			model.addAttribute("type",type);
		}

		// 查看审批申请单
		if (org.apache.commons.lang3.StringUtils.isNotBlank(pickBill.getId())){//.getAct().getProcInsId())){
			if(org.springframework.util.StringUtils.isEmpty(pickBill.getProcessInstanceId())){
				model.addAttribute("pickBill", pickBill);
				return "salemanage/pickbill/" + view;

			}
			// 环节编号
			String taskDefKey = pickBill.getAct().getTaskDefKey();

			// 查看工单
			if(pickBill.getAct().isFinishTask()){
				view = "pickBillView";
			}
			// 修改环节
			else if ("pickBillAudit".equals(taskDefKey)){
				view = "pickBillAudit";
			}
			// 审核环节3
			else if ("modifyPickBill".equals(taskDefKey)){
				pickBill.setPbillStat("A");
				view = "pickBillForm";
			}
		} else {
			pickBill.setPbillStat("A");

			Date date = new Date();
			pickBill.setPbillDate(date);
			pickBill.setPickDate(date);
			SimpleDateFormat sdf =   new SimpleDateFormat( "yyyyMMdd" );
			String newBillNum = pickBillService.getMaxId();
			if(StringUtils.isEmpty(newBillNum)){
				newBillNum = "0000";
			} else {
				newBillNum = String.format("%04d", Integer.parseInt(newBillNum.substring(11,15))+1);
			}
			pickBill.setPbillCode("PIK" + sdf.format(date) + newBillNum);

			User u = UserUtils.getUser();
			pickBill.setPbillPerson(u);
			pickBill.setPbillPsnName(u.getName());
			model.addAttribute("isAdd", true);
		}
		model.addAttribute("pickBill", pickBill);
		return "salemanage/pickbill/" + view;
	}

	/**
	 * 保存单据
	 */
	@RequiresPermissions(value={"pickbill:pickBill:add","pickbill:pickBill:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(PickBill pickBill, Model model, RedirectAttributes redirectAttributes,String start) throws Exception{
		if (!beanValidator(model, pickBill)){
			return form(pickBill, model,"");
		}
		//新增或编辑表单保存
		Map<String, Object> variables = Maps.newHashMap();
		boolean startProc = false;
		if("1".equals(start)){
			startProc = true;
		}
		User u = UserUtils.getUser();
		pickBill.setPbillPerson(u);
		pickBill.setPbillPsnName(u.getName());
		pickBillService.save(pickBill,variables,startProc);//保存
		addMessage(redirectAttributes, "保存单据成功");

		if(!org.springframework.util.StringUtils.isEmpty(pickBill.getProcessInstanceId())){
			model.addAttribute("pickBill", pickBill);
			return "redirect:"+Global.getAdminPath()+"/pickbill/pickBill/todo?repage";

		}
		return "redirect:"+Global.getAdminPath()+"/pickbill/pickBill/?repage";
	}

	@RequestMapping(value = "saveAudit")
	public String saveAudit(PickBill pickBill, RedirectAttributes redirectAttributes, Map<String, Object> vars, Model model) {
		if (org.apache.commons.lang3.StringUtils.isBlank(pickBill.getAct().getComment())){
			addMessage(model, "请填写审核意见。");
			return form(pickBill, model, "");
		}
		pickBillService.auditSave(pickBill);
		addMessage(redirectAttributes, "审核成功");
		return "redirect:" + adminPath + "/pickbill/pickBill/todo";
	}
	
	/**
	 * 删除单据
	 */
	@ResponseBody
	@RequiresPermissions("pickbill:pickBill:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(PickBill pickBill, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		pickBillService.delete(pickBill);
		j.setMsg("删除单据成功");
		return j;
	}
	
	/**
	 * 批量删除单据
	 */
	@ResponseBody
	@RequiresPermissions("pickbill:pickBill:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			pickBillService.delete(pickBillService.get(id));
		}
		j.setMsg("删除单据成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("pickbill:pickBill:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(PickBill pickBill, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "单据"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<PickBill> page = pickBillService.findPage(new Page<PickBill>(request, response, -1), pickBill);
    		new ExportExcel("单据", PickBill.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出单据记录失败！失败信息："+e.getMessage());
		}
			return j;
    }
    
    @ResponseBody
    @RequestMapping(value = "detail")
	public PickBill detail(String id) {
		return pickBillService.get(id);
	}
	

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("pickbill:pickBill:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<PickBill> list = ei.getDataList(PickBill.class);
			for (PickBill pickBill : list){
				try{
					pickBillService.save(pickBill);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条单据记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条单据记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入单据失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/pickbill/pickBill/?repage";
    }
	
	/**
	 * 下载导入单据数据模板
	 */
	@RequiresPermissions("pickbill:pickBill:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "单据数据导入模板.xlsx";
    		List<PickBill> list = Lists.newArrayList(); 
    		new ExportExcel("单据数据", PickBill.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/pickbill/pickBill/?repage";
    }

	@RequestMapping(value = "print")
	public String print(){
		return "salemanage/pickbill/print";
	}

}