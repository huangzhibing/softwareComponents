/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.basedata.mdmcalendar.web;

import java.util.Calendar;
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
import com.hqu.modules.basedata.mdmcalendar.entity.MdmCalendar;
import com.hqu.modules.basedata.mdmcalendar.service.MdmCalendarService;
import java.text.SimpleDateFormat;

/**
 * 企业日历定义Controller
 * @author 何志铭
 * @version 2018-04-02
 */
@Controller
@RequestMapping(value = "${adminPath}/basedata/mdmcalendar/mdmCalendar")
public class MdmCalendarController extends BaseController {

	@Autowired
	private MdmCalendarService mdmCalendarService;
	
	@ModelAttribute
	public MdmCalendar get(@RequestParam(required=false) String id) {
		MdmCalendar entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = mdmCalendarService.get(id);
		}
		if (entity == null){
			entity = new MdmCalendar();
		}
		return entity;
	}
	
	/**
	 * 企业日历定义列表页面
	 */
	@RequiresPermissions("basedata:mdmcalendar:mdmCalendar:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "modules/basedata/mdmcalendar/mdmCalendarList";
	}
	
		/**
	 * 企业日历定义列表数据
	 */
	@ResponseBody
	@RequiresPermissions("basedata:mdmcalendar:mdmCalendar:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(MdmCalendar mdmCalendar, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<MdmCalendar> page = mdmCalendarService.findPage(new Page<MdmCalendar>(request, response), mdmCalendar); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑企业日历定义表单页面
	 */
	@RequiresPermissions(value={"basedata:mdmcalendar:mdmCalendar:view","basedata:mdmcalendar:mdmCalendar:add","basedata:mdmcalendar:mdmCalendar:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(MdmCalendar mdmCalendar, Model model) {
		model.addAttribute("mdmCalendar", mdmCalendar);
		if(StringUtils.isBlank(mdmCalendar.getId())){//如果ID是空为添加
			model.addAttribute("isAdd", true);
			return "modules/basedata/mdmcalendar/mdmCalendarForm";
		}else {
			
			return "modules/basedata/mdmcalendar/mdmCalendarUpdata";
		}
	}

	/**
	 * 保存企业日历定义
	 */
	@RequiresPermissions(value={"basedata:mdmcalendar:mdmCalendar:add","basedata:mdmcalendar:mdmCalendar:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(MdmCalendar mdmCalendar, Model model, RedirectAttributes redirectAttributes,HttpServletRequest request) throws Exception{
		if (!beanValidator(model, mdmCalendar)){
			return form(mdmCalendar, model);
		}
		//新增或编辑表单保存
		String workTime=request.getParameter("workTime");
		String restTime=request.getParameter("restTime");
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		Date startDate=sdf.parse(request.getParameter("startDate"));
		Date endDate=sdf.parse(request.getParameter("endDate"));
		sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		cal.setTime(startDate);
		String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
		while(startDate.getTime()<=endDate.getTime()) {
			//保存日期
			mdmCalendar.setCurDate(sdf.format(startDate));
			//保存日期对应星期
			int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
			mdmCalendar.setWeekDay(weekDays[w]);
			//保存日期对应工作日标识与工作时间
			if(cal.get(Calendar.DAY_OF_WEEK)==Calendar.SATURDAY||cal.get(Calendar.DAY_OF_WEEK)==Calendar.SUNDAY){
				mdmCalendar.setWorkFlag("N");
				mdmCalendar.setWorkTime(workTime);
			}else {
				mdmCalendar.setWorkFlag("Y");
				mdmCalendar.setWorkTime(restTime);
			}
			mdmCalendarService.save(mdmCalendar);//保存
			cal.add(Calendar.DATE,1);
			startDate =cal.getTime();
			mdmCalendar=new MdmCalendar();
		}
		addMessage(redirectAttributes, "保存企业日历定义成功");
		return "redirect:"+Global.getAdminPath()+"/basedata/mdmcalendar/mdmCalendar/?repage";
	}
	/**
	 * 更新企业日历定义
	 */
	@RequiresPermissions(value={"basedata:mdmcalendar:mdmCalendar:add","basedata:mdmcalendar:mdmCalendar:edit"},logical=Logical.OR)
	@RequestMapping(value = "updata")
	public String updata(MdmCalendar mdmCalendar, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, mdmCalendar)){
			return form(mdmCalendar, model);
		}
		//新增或编辑表单保存
		mdmCalendarService.save(mdmCalendar);//保存
		addMessage(redirectAttributes, "更新企业日历定义成功");
		return "redirect:"+Global.getAdminPath()+"/basedata/mdmcalendar/mdmCalendar/?repage";
	}
	/**
	 * 删除企业日历定义
	 */
	@ResponseBody
	@RequiresPermissions("basedata:mdmcalendar:mdmCalendar:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(MdmCalendar mdmCalendar, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		mdmCalendarService.delete(mdmCalendar);
		j.setMsg("删除企业日历定义成功");
		return j;
	}
	
	/**
	 * 批量删除企业日历定义
	 */
	@ResponseBody
	@RequiresPermissions("basedata:mdmcalendar:mdmCalendar:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			mdmCalendarService.delete(mdmCalendarService.get(id));
		}
		j.setMsg("删除企业日历定义成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("basedata:mdmcalendar:mdmCalendar:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(MdmCalendar mdmCalendar, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "企业日历定义"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<MdmCalendar> page = mdmCalendarService.findPage(new Page<MdmCalendar>(request, response, -1), mdmCalendar);
    		new ExportExcel("企业日历定义", MdmCalendar.class).setDataList(page.getList()).write(response, fileName).dispose();
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
	@RequiresPermissions("basedata:mdmcalendar:mdmCalendar:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<MdmCalendar> list = ei.getDataList(MdmCalendar.class);
			for (MdmCalendar mdmCalendar : list){
				try{
					mdmCalendarService.save(mdmCalendar);
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
		return "redirect:"+Global.getAdminPath()+"/basedata/mdmcalendar/mdmCalendar/?repage";
    }
	
	/**
	 * 下载导入企业日历定义数据模板
	 */
	@RequiresPermissions("basedata:mdmcalendar:mdmCalendar:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "企业日历定义数据导入模板.xlsx";
    		List<MdmCalendar> list = Lists.newArrayList(); 
    		new ExportExcel("企业日历定义数据", MdmCalendar.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/basedata/mdmcalendar/mdmCalendar/?repage";
    }

}