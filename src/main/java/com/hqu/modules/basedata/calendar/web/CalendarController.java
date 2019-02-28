/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.basedata.calendar.web;

import java.text.SimpleDateFormat;
import java.util.Date;
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
import com.hqu.modules.basedata.calendar.entity.Calendar;
import com.hqu.modules.basedata.calendar.service.CalendarService;

/**
 * 企业日历定义Controller
 * @author 何志铭
 * @version 2018-04-04
 */
@Controller
@RequestMapping(value = "${adminPath}/calendar/calendar")
public class CalendarController extends BaseController {

	@Autowired
	private CalendarService calendarService;
	
	@ModelAttribute
	public Calendar get(@RequestParam(required=false) String id) {
		Calendar entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = calendarService.get(id);
		}
		if (entity == null){
			entity = new Calendar();
		}
		return entity;
	}
	/*
	 * 检查唯一性
	 */
	 @RequestMapping(value = "chkCode")
	  @ResponseBody
	    public String checkCode(String tableName,String fieldName,String sfieldValue,String efieldValue){
	        Boolean exist = calendarService.getCodeNum(tableName,fieldName,sfieldValue,efieldValue);
	        if(exist){
	            logger.debug("已存在");
	            return "false";
	        } else {
	            logger.debug("未存在");
	            return "true";
	        }
	    }
	/**
	 * 企业日历定义列表页面
	 */
	@RequiresPermissions("calendar:calendar:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "basedata/calendar/calendarList";
	}
	
		/**
	 * 企业日历定义列表数据
	 */
	@ResponseBody
	@RequiresPermissions("calendar:calendar:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(Calendar calendar, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Calendar> page = calendarService.findPage(new Page<Calendar>(request, response), calendar); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑企业日历定义表单页面
	 */
	@RequiresPermissions(value={"calendar:calendar:view","calendar:calendar:add","calendar:calendar:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(Calendar calendar, Model model) {
		model.addAttribute("calendar", calendar);
		if(StringUtils.isBlank(calendar.getId())){//如果ID是空为添加
			model.addAttribute("isAdd", true);
			return "basedata/calendar/calendarSave";
		}
		return "basedata/calendar/calendarForm";
	}

	/**
	 * 保存企业日历定义
	 */
	@RequiresPermissions(value={"calendar:calendar:add","calendar:calendar:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(Calendar calendar, Model model, RedirectAttributes redirectAttributes,HttpServletRequest request) throws Exception{
		if (!beanValidator(model, calendar)){
			return form(calendar, model);
		}
		//保存所有起始日期与结束日期间的数据
		calendarService.saveAll(calendar,request);
		addMessage(redirectAttributes, "保存企业日历定义成功");
		return "redirect:"+Global.getAdminPath()+"/calendar/calendar/?repage";
	}
	
	/**
	 * 更新企业日历定义
	 */
	@RequiresPermissions(value={"calendar:calendar:add","calendar:calendar:edit"},logical=Logical.OR)
	@RequestMapping(value = "updata")
	public String updata(Calendar calendar, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, calendar)){
			return form(calendar, model);
		}
		//新增或编辑表单保存
		calendarService.save(calendar);//保存
		addMessage(redirectAttributes, "更新企业日历定义成功");
		return "redirect:"+Global.getAdminPath()+"/calendar/calendar/?repage";
	}
	
	/**
	 * 删除企业日历定义
	 */
	@ResponseBody
	@RequiresPermissions("calendar:calendar:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(Calendar calendar, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		calendarService.delete(calendar);
		j.setMsg("删除企业日历定义成功");
		return j;
	}
	
	/**
	 * 批量删除企业日历定义
	 */
	@ResponseBody
	@RequiresPermissions("calendar:calendar:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			calendarService.delete(calendarService.get(id));
		}
		j.setMsg("删除企业日历定义成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("calendar:calendar:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(Calendar calendar, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "企业日历定义"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<Calendar> page = calendarService.findPage(new Page<Calendar>(request, response, -1), calendar);
    		new ExportExcel("企业日历定义", Calendar.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出企业日历定义记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("calendar:calendar:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<Calendar> list = ei.getDataList(Calendar.class);
			for (Calendar calendar : list){
				try{
					calendarService.saveExc(calendar);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条企业日历定义记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条企业日历定义记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入企业日历定义失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/calendar/calendar/?repage";
    }
	
	/**
	 * 下载导入企业日历定义数据模板
	 */
	@RequiresPermissions("calendar:calendar:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "企业日历定义数据导入模板.xlsx";
    		List<Calendar> list = Lists.newArrayList(); 
    		new ExportExcel("企业日历定义数据", Calendar.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/calendar/calendar/?repage";
    }

}