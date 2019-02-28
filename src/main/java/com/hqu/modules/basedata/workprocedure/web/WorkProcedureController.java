/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.basedata.workprocedure.web;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

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
import com.hqu.modules.basedata.workprocedure.entity.WorkProcedure;
import com.hqu.modules.basedata.workprocedure.service.WorkProcedureService;

/**
 * 工序定义Controller
 * @author liujiachen
 * @version 2018-04-06
 */
@Controller
@RequestMapping(value = "${adminPath}/workprocedure/workProcedure")
public class WorkProcedureController extends BaseController {

	@Autowired
	private WorkProcedureService workProcedureService;
	
	@ModelAttribute
	public WorkProcedure get(@RequestParam(required=false) String id) {
		WorkProcedure entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = workProcedureService.get(id);
		}
		if (entity == null){
			entity = new WorkProcedure();
		}
		return entity;
	}
	
	/**
	 * 工序定义列表页面
	 */
	@RequiresPermissions("workprocedure:workProcedure:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "basedata/workprocedure/workProcedureList";
	}
	
		/**
	 * 工序定义列表数据
	 */
	@ResponseBody
	@RequiresPermissions("workprocedure:workProcedure:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(WorkProcedure workProcedure, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<WorkProcedure> page = workProcedureService.findPage(new Page<WorkProcedure>(request, response), workProcedure); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑工序定义表单页面
	 */
	@RequiresPermissions(value={"workprocedure:workProcedure:view","workprocedure:workProcedure:add","workprocedure:workProcedure:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(WorkProcedure workProcedure, Model model) {
		model.addAttribute("workProcedure", workProcedure);
		if(StringUtils.isBlank(workProcedure.getId())){//如果ID是空为添加
			model.addAttribute("isAdd", true);
		}
		//设置workid从0开始自增长并为2位流水压入表单中
		String workid = workProcedureService.getMaxWorkProcedureId();
		if(workid == null) {
			workid = "0001";
		}
		else {
			int maxnum = Integer.parseInt(workid) + 1;
			workid = String.format("%04d",maxnum);
		}

		workProcedure.setWorkProcedureId(workid);
		model.addAttribute("workProcedure", workProcedure);
		return "basedata/workprocedure/workProcedureForm";
	}

	/**
	 * 保存工序定义
	 */
	@RequiresPermissions(value={"workprocedure:workProcedure:add","workprocedure:workProcedure:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(WorkProcedure workProcedure, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, workProcedure)){
			return form(workProcedure, model);
		}
		//新增或编辑表单保存
		workProcedureService.save(workProcedure);//保存
		addMessage(redirectAttributes, "保存工序定义成功");
		return "redirect:"+Global.getAdminPath()+"/workprocedure/workProcedure/?repage";
	}
	
	/**
	 * 删除工序定义
	 */
	@ResponseBody
	@RequiresPermissions("workprocedure:workProcedure:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(WorkProcedure workProcedure, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		workProcedureService.delete(workProcedure);
		j.setMsg("删除工序定义成功");
		return j;
	}
	
	/**
	 * 批量删除工序定义
	 */
	@ResponseBody
	@RequiresPermissions("workprocedure:workProcedure:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			workProcedureService.delete(workProcedureService.get(id));
		}
		j.setMsg("删除工序定义成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("workprocedure:workProcedure:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(WorkProcedure workProcedure, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "工序定义"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<WorkProcedure> page = workProcedureService.findPage(new Page<WorkProcedure>(request, response, -1), workProcedure);
    		new ExportExcel("工序定义", WorkProcedure.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出工序定义记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("workprocedure:workProcedure:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<WorkProcedure> list = ei.getDataList(WorkProcedure.class);
			for (WorkProcedure workProcedure : list){
				try{
					workProcedureService.save(workProcedure);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条工序定义记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条工序定义记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入工序定义失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/workprocedure/workProcedure/?repage";
    }
	
	/**
	 * 下载导入工序定义数据模板
	 */
	@RequiresPermissions("workprocedure:workProcedure:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "工序定义数据导入模板.xlsx";
    		List<WorkProcedure> list = Lists.newArrayList(); 
    		new ExportExcel("工序定义数据", WorkProcedure.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/workprocedure/workProcedure/?repage";
    }

}