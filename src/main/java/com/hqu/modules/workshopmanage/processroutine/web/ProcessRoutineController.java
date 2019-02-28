/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.workshopmanage.processroutine.web;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import com.hqu.modules.workshopmanage.processbatch.entity.ProcessBatch;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
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
import com.hqu.modules.workshopmanage.processroutine.service.ProcessRoutineService;

/**
 * 车间加工路线管理Controller
 * @author xhc
 * @version 2018-08-06
 */
@Controller
@RequestMapping(value = "${adminPath}/processroutine/processBatch")
public class ProcessRoutineController extends BaseController {

	@Autowired
	private ProcessRoutineService processRoutineService;
	
	@ModelAttribute
	public ProcessBatch get(@RequestParam(required=false) String id) {
		ProcessBatch entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = processRoutineService.get(id);
		}
		if (entity == null){
			entity = new ProcessBatch();
		}
		return entity;
	}

	/**
	 * 车间加工路线查询列表页面
	 */
	@RequiresPermissions("processroutine:processBatch:query")
	@RequestMapping(value="queryList")
	public String queryList(){
		return "workshopmanage/processroutine/processBatchQueryList";
	}


	/**
	 * 车间加工路线查询列表数据
	 */
	@ResponseBody
	@RequiresPermissions("processroutine:processBatch:query")
	@RequestMapping(value = "queryData")
	public Map<String, Object> queryData(ProcessBatch processBatch, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<ProcessBatch> page = processRoutineService.findQueryPage(new Page<ProcessBatch>(request, response), processBatch);
		return getBootstrapData(page);

	}

	/**
	 * 查看车间加工路线查询表单页面
	 */
	@RequiresPermissions("processroutine:processBatch:query")
	@RequestMapping(value = "queryForm")
	public String queryForm(ProcessBatch processBatch, Model model) {
		model.addAttribute("processBatch", processBatch);
		if(StringUtils.isBlank(processBatch.getId())){//如果ID是空为添加
			model.addAttribute("isAdd", true);
		}
		return "workshopmanage/processroutine/processBatchQueryForm";
	}



	/**
	 * 车间加工路线管理列表页面
	 */
	@RequiresPermissions("processroutine:processBatch:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		//测试用
		//processRoutineService.generateRoutineDetail("MPS1806-4001-1","A001");
		return "workshopmanage/processroutine/processBatchList";
	}



	
		/**
	 * 车间加工路线管理列表数据
	 */
	@ResponseBody
	@RequiresPermissions("processroutine:processBatch:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(ProcessBatch processBatch, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<ProcessBatch> page = processRoutineService.findPage(new Page<ProcessBatch>(request, response), processBatch);
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑车间加工路线管理表单页面
	 */
	@RequiresPermissions(value={"processroutine:processBatch:view","processroutine:processBatch:add","processroutine:processBatch:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(ProcessBatch processBatch, Model model) {
		model.addAttribute("processBatch", processBatch);
		if(StringUtils.isBlank(processBatch.getId())){//如果ID是空为添加
			model.addAttribute("isAdd", true);
		}
		return "workshopmanage/processroutine/processBatchForm";
	}

	/**
	 * 保存车间加工路线管理
	 */
	@RequiresPermissions(value={"processroutine:processBatch:add","processroutine:processBatch:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(ProcessBatch processBatch, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, processBatch)){
			return form(processBatch, model);
		}
		//新增或编辑表单保存
		processRoutineService.save(processBatch);//保存
		addMessage(redirectAttributes, "执行成功");
		return "redirect:"+Global.getAdminPath()+"/processroutine/processBatch/?repage";
	}
	
	/**
	 * 删除车间加工路线管理
	 */
	@ResponseBody
	@RequiresPermissions("processroutine:processBatch:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(ProcessBatch processBatch, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		processRoutineService.delete(processBatch);
		j.setMsg("删除车间加工路线管理成功");
		return j;
	}
	
	/**
	 * 批量删除车间加工路线管理
	 */
	@ResponseBody
	@RequiresPermissions("processroutine:processBatch:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			processRoutineService.delete(processRoutineService.get(id));
		}
		j.setMsg("删除车间加工路线管理成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("processroutine:processBatch:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(ProcessBatch processBatch, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "车间加工路线管理"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<ProcessBatch> page = processRoutineService.findPage(new Page<ProcessBatch>(request, response, -1), processBatch);
    		new ExportExcel("车间加工路线管理", ProcessBatch.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出车间加工路线管理记录失败！失败信息："+e.getMessage());
		}
			return j;
    }
    
    @ResponseBody
    @RequestMapping(value = "detail")
	public ProcessBatch detail(String id) {
		return processRoutineService.get(id);
	}
	

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("processroutine:processBatch:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<ProcessBatch> list = ei.getDataList(ProcessBatch.class);
			for (ProcessBatch processBatch : list){
				try{
					processRoutineService.save(processBatch);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条车间加工路线管理记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条车间加工路线管理记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入车间加工路线管理失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/processroutine/processBatch/?repage";
    }
	
	/**
	 * 下载导入车间加工路线管理数据模板
	 */
	@RequiresPermissions("processroutine:processBatch:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "车间加工路线管理数据导入模板.xlsx";
    		List<ProcessBatch> list = Lists.newArrayList(); 
    		new ExportExcel("车间加工路线管理数据", ProcessBatch.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/processroutine/processBatch/?repage";
    }
	

}