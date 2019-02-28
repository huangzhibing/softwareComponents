/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.basedata.period.web;

import com.google.common.collect.Lists;
import com.hqu.modules.basedata.period.entity.Period;
import com.hqu.modules.basedata.period.service.PeriodService;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.excel.ExportExcel;
import com.jeeplus.common.utils.excel.ImportExcel;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.web.BaseController;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

/**
 * 企业核算期定义Controller
 * @author Neil
 * @version 2018-06-02
 */
@Controller
@RequestMapping(value = "${adminPath}/period/period")
public class PeriodController extends BaseController {

	@Autowired
	private PeriodService periodService;
	
	@ModelAttribute
	public Period get(@RequestParam(required=false) String id) {
		Period entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = periodService.get(id);
		}
		if (entity == null){
			entity = new Period();
		}
		return entity;
	}
	
	/**
	 * 企业核算期定义列表页面
	 */
	@RequiresPermissions("period:period:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "basedata/period/periodList";
	}
	
		/**
	 * 企业核算期定义列表数据
	 */
	@ResponseBody
	@RequiresPermissions("period:period:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(Period period, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Period> page = periodService.findPage(new Page<Period>(request, response), period); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑企业核算期定义表单页面
	 */
	@RequiresPermissions(value={"period:period:view","period:period:add","period:period:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(Period period, Model model) {
		model.addAttribute("period", period);
		return "basedata/period/periodForm";
	}
	
	@RequiresPermissions(value={"period:period:view","period:period:add","period:period:edit"},logical=Logical.OR)
	@RequestMapping(value = "create")
	public String create(Period period, Model model) {
		model.addAttribute("period", period);
		return "basedata/period/periodCreate";
	}

	@RequiresPermissions(value={"period:period:add","period:period:edit"},logical=Logical.OR)
	@RequestMapping(value = "created")
	public String created(Period periodTemp, Model model, RedirectAttributes redirectAttributes,HttpServletRequest request) throws Exception{
		String year=(String) request.getParameter("periodYear");
		String endmonth=(String) request.getParameter("endMonth");
		String endate=(String) request.getParameter("endDate");
		String endtime=(String) request.getParameter("endtime");
		if(endtime.equals(""))
			endtime="00";
		Integer periodYear=Integer.parseInt(year);
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		if(endmonth.equals("1")) {
			Period periodOne=new Period();
			periodOne.setPeriodId(periodYear+"01");
			periodOne.setPeriodBegin(sdf.parse((periodYear-1)+"-12-"+endate));
			periodOne.setPeriodEnd(sdf.parse(periodYear+"-01-"+endate));
			periodOne.setBeginHour(endtime+":00:00");
			periodOne.setEndHour(endtime+":00:00");
			periodOne.setYear(year);
			periodService.save(periodOne);
			for(int i=0;i<11;i++) {
				Period period=new Period();
				if(i<8)
					period.setPeriodId(year+"0"+(i+2));
				else
					period.setPeriodId(year+(i+2));
				period.setPeriodBegin(sdf.parse(periodYear+"-"+(i+1)+"-"+endate));
				period.setPeriodEnd(sdf.parse(periodYear+"-"+(i+2)+"-"+endate));
				period.setBeginHour(endtime+":00:00");
				period.setEndHour(endtime+":00:00");
				period.setYear(year);
				periodService.save(period);
			}
		}else{
			for(int i=0;i<11;i++) {
				Period period=new Period();
				if(i<9)
					period.setPeriodId(year+"0"+(i+1));
				else
					period.setPeriodId(year+(i+1));
				period.setPeriodBegin(sdf.parse(periodYear+"-"+(i+1)+"-"+endate));
				period.setPeriodEnd(sdf.parse(periodYear+"-"+(i+2)+"-"+endate));
				period.setBeginHour(endtime+":00:00");
				period.setEndHour(endtime+":00:00");
				period.setYear(year);
				periodService.save(period);
			}
			Period periodEnd=new Period();
			periodEnd.setPeriodId(year+"12");
			periodEnd.setPeriodBegin(sdf.parse(periodYear+"-12-"+endate));
			periodEnd.setPeriodEnd(sdf.parse((periodYear+1)+"-01-"+endate));
			periodEnd.setBeginHour(endtime+":00:00");
			periodEnd.setEndHour(endtime+":00:00");
			periodEnd.setYear(year);
			periodService.save(periodEnd);
		}
		return "basedata/period/periodList";
	}
	
	
	/**
	 * 保存企业核算期定义
	 */
	@RequiresPermissions(value={"period:period:add","period:period:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(Period period, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, period)){
			addMessage(redirectAttributes,"非法参数！");
			return "redirect:"+Global.getAdminPath()+"/period/period?repage";
		}
		periodService.save(period);//新建或者编辑保存
		addMessage(redirectAttributes,"保存企业核算期定义成功");
		return "redirect:"+Global.getAdminPath()+"/period/period?repage";
	}

	/**
	 * 删除企业核算期定义
	 */
	@ResponseBody
	@RequiresPermissions("period:period:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(Period period, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		periodService.delete(period);
		j.setMsg("删除企业核算期定义成功");
		return j;
	}
	
	/**
	 * 批量删除企业核算期定义
	 */
	@ResponseBody
	@RequiresPermissions("period:period:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			periodService.delete(periodService.get(id));
		}
		j.setMsg("删除企业核算期定义成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("period:period:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(Period period, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "企业核算期定义"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<Period> page = periodService.findPage(new Page<Period>(request, response, -1), period);
    		new ExportExcel("企业核算期定义", Period.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出企业核算期定义记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("period:period:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<Period> list = ei.getDataList(Period.class);
			for (Period period : list){
				try{
					periodService.save(period);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条企业核算期定义记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条企业核算期定义记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入企业核算期定义失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/period/period/?repage";
    }
	
	/**
	 * 下载导入企业核算期定义数据模板
	 */
	@RequiresPermissions("period:period:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "企业核算期定义数据导入模板.xlsx";
    		List<Period> list = Lists.newArrayList(); 
    		new ExportExcel("企业核算期定义数据", Period.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/period/period/?repage";
    }

}