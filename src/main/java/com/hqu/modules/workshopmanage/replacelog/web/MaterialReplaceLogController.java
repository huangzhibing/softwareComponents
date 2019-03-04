/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.workshopmanage.replacelog.web;

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
import com.hqu.modules.workshopmanage.replacelog.entity.MaterialReplaceLog;
import com.hqu.modules.workshopmanage.replacelog.service.MaterialReplaceLogService;

/**
 * 换件日志Controller
 * @author xhc
 * @version 2019-02-23
 */
@Controller
@RequestMapping(value = "${adminPath}/replacelog/materialReplaceLog")
public class MaterialReplaceLogController extends BaseController {

	@Autowired
	private MaterialReplaceLogService materialReplaceLogService;
	
	@ModelAttribute
	public MaterialReplaceLog get(@RequestParam(required=false) String id) {
		MaterialReplaceLog entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = materialReplaceLogService.get(id);
		}
		if (entity == null){
			entity = new MaterialReplaceLog();
		}
		return entity;
	}


	/**
	 * 换件记录列表页面
	 */
	@RequiresPermissions("replacelog:materialReplaceLog:list")
	@RequestMapping(value = "listformachinequery")
	public String listformachinequery(@RequestParam(required=false) String sn,Model model) {
		model.addAttribute("sn",sn);
		return "workshopmanage/replacelog/materialReplaceLogListForMachineQuery";
	}



	/**
	 * 换件记录列表页面
	 */
	@RequiresPermissions("replacelog:materialReplaceLog:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "workshopmanage/replacelog/materialReplaceLogList";
	}
	
		/**
	 * 换件记录列表数据
	 */
	@ResponseBody
	@RequiresPermissions("replacelog:materialReplaceLog:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(MaterialReplaceLog materialReplaceLog, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<MaterialReplaceLog> page = materialReplaceLogService.findPage(new Page<MaterialReplaceLog>(request, response), materialReplaceLog); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑换件记录表单页面
	 */
	@RequiresPermissions(value={"replacelog:materialReplaceLog:view","replacelog:materialReplaceLog:add","replacelog:materialReplaceLog:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(MaterialReplaceLog materialReplaceLog, Model model) {
		model.addAttribute("materialReplaceLog", materialReplaceLog);
		if(StringUtils.isBlank(materialReplaceLog.getId())){//如果ID是空为添加
			model.addAttribute("isAdd", true);
		}
		return "workshopmanage/replacelog/materialReplaceLogForm";
	}

	/**
	 * 保存换件记录
	 */
	@RequiresPermissions(value={"replacelog:materialReplaceLog:add","replacelog:materialReplaceLog:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(MaterialReplaceLog materialReplaceLog, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, materialReplaceLog)){
			return form(materialReplaceLog, model);
		}
		//新增或编辑表单保存
		materialReplaceLogService.save(materialReplaceLog);//保存
		addMessage(redirectAttributes, "保存换件记录成功");
		return "redirect:"+Global.getAdminPath()+"/replacelog/materialReplaceLog/?repage";
	}
	
	/**
	 * 删除换件记录
	 */
	@ResponseBody
	@RequiresPermissions("replacelog:materialReplaceLog:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(MaterialReplaceLog materialReplaceLog, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		materialReplaceLogService.delete(materialReplaceLog);
		j.setMsg("删除换件记录成功");
		return j;
	}
	
	/**
	 * 批量删除换件记录
	 */
	@ResponseBody
	@RequiresPermissions("replacelog:materialReplaceLog:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			materialReplaceLogService.delete(materialReplaceLogService.get(id));
		}
		j.setMsg("删除换件记录成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("replacelog:materialReplaceLog:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(MaterialReplaceLog materialReplaceLog, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "换件记录"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<MaterialReplaceLog> page = materialReplaceLogService.findPage(new Page<MaterialReplaceLog>(request, response, -1), materialReplaceLog);
    		new ExportExcel("换件记录", MaterialReplaceLog.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出换件记录记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("replacelog:materialReplaceLog:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<MaterialReplaceLog> list = ei.getDataList(MaterialReplaceLog.class);
			for (MaterialReplaceLog materialReplaceLog : list){
				try{
					materialReplaceLogService.save(materialReplaceLog);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条换件记录记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条换件记录记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入换件记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/replacelog/materialReplaceLog/?repage";
    }
	
	/**
	 * 下载导入换件记录数据模板
	 */
	@RequiresPermissions("replacelog:materialReplaceLog:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "换件记录数据导入模板.xlsx";
    		List<MaterialReplaceLog> list = Lists.newArrayList(); 
    		new ExportExcel("换件记录数据", MaterialReplaceLog.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/replacelog/materialReplaceLog/?repage";
    }

}