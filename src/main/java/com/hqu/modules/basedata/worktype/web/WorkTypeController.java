/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.basedata.worktype.web;

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
import com.hqu.modules.basedata.worktype.entity.WorkType;
import com.hqu.modules.basedata.worktype.service.WorkTypeService;

/**
 * 工种定义Controller
 * @author zb
 * @version 2018-04-07
 */
@Controller
@RequestMapping(value = "${adminPath}/worktype/workType")
public class WorkTypeController extends BaseController {

	@Autowired
	private WorkTypeService workTypeService;

	@ModelAttribute
	public WorkType get(@RequestParam(required=false) String id) {
		WorkType entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = workTypeService.get(id);
		}
		if (entity == null){
			entity = new WorkType();
		}
		return entity;
	}

	/**
	 * 工种定义列表页面
	 */
	@RequiresPermissions("worktype:workType:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "basedata/worktype/workTypeList";
	}

		/**
	 * 工种定义列表数据
	 */
	@ResponseBody
	@RequiresPermissions("worktype:workType:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(WorkType workType, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<WorkType> page = workTypeService.findPage(new Page<WorkType>(request, response), workType);
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑工种定义表单页面
	 */
	@RequiresPermissions(value={"worktype:workType:view","worktype:workType:add","worktype:workType:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(WorkType workType, Model model) {
		model.addAttribute("workType", workType);
		if(StringUtils.isBlank(workType.getId())){//如果ID是空为添加
			model.addAttribute("isAdd", true);
		}
		return "basedata/worktype/workTypeForm";
	}

	/**
	 * 保存工种定义
	 */
	@RequiresPermissions(value={"worktype:workType:add","worktype:workType:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(WorkType workType, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, workType)){
			return form(workType, model);
		}
		//新增或编辑表单保存
		workTypeService.save(workType);//保存
		addMessage(redirectAttributes, "保存工种定义成功");
		return "redirect:"+Global.getAdminPath()+"/worktype/workType/?repage";
	}

	/**
	 * 删除工种定义
	 */
	@ResponseBody
	@RequiresPermissions("worktype:workType:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(WorkType workType, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		workTypeService.delete(workType);
		j.setMsg("删除工种定义成功");
		return j;
	}

	/**
	 * 批量删除工种定义
	 */
	@ResponseBody
	@RequiresPermissions("worktype:workType:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			workTypeService.delete(workTypeService.get(id));
		}
		j.setMsg("删除工种定义成功");
		return j;
	}

	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("worktype:workType:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(WorkType workType, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "工种定义"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<WorkType> page = workTypeService.findPage(new Page<WorkType>(request, response, -1), workType);
    		new ExportExcel("工种定义", WorkType.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出工种定义记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("worktype:workType:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<WorkType> list = ei.getDataList(WorkType.class);
			for (WorkType workType : list){
				try{
					workTypeService.save(workType);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条工种定义记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条工种定义记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入工种定义失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/worktype/workType/?repage";
    }

	/**
	 * 下载导入工种定义数据模板
	 */
	@RequiresPermissions("worktype:workType:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "工种定义数据导入模板.xlsx";
    		List<WorkType> list = Lists.newArrayList();
    		new ExportExcel("工种定义数据", WorkType.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/worktype/workType/?repage";
    }

}