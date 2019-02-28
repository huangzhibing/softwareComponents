/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.purchasemanage.monthreport.web;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import com.hqu.modules.qualitymanage.purreportnew.entity.PurReportNew;
import com.hqu.modules.qualitymanage.purreportnew.service.PurReportNewService;
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
import com.hqu.modules.purchasemanage.monthreport.entity.MonthReport;
import com.hqu.modules.purchasemanage.monthreport.service.MonthReportService;

/**
 * 进料不合格统计表，关联IQC来料检验单Controller
 * @author ckw
 * @version 2018-08-25
 */
@Controller
@RequestMapping(value = "${adminPath}/monthreport/monthReport")
public class MonthReportController extends BaseController {

	@Autowired
	private MonthReportService monthReportService;

	@Autowired
	private PurReportNewService purReportNewService;
	
	@ModelAttribute
	public MonthReport get(@RequestParam(required=false) String id) {
		MonthReport entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = monthReportService.get(id);
		}
		if (entity == null){
			entity = new MonthReport();
		}
		return entity;
	}
	
	/**
	 * 进料不合格统计表列表页面
	 */
	@RequiresPermissions("monthreport:monthReport:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "purchasemanage/monthreport/monthReportList";
	}
	
		/**
	 * 进料不合格统计表列表数据
	 */
	@ResponseBody
	@RequiresPermissions("monthreport:monthReport:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(MonthReport monthReport, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<MonthReport> page = monthReportService.findPage(new Page<MonthReport>(request, response), monthReport); 
		return getBootstrapData(page);
	}




	/**
	 * 获取IQC检验单
	 */
	@ResponseBody
	@RequestMapping(value = "purReportNew")
	public Map<String, Object> getPurReportNew(PurReportNew purReportNew, HttpServletRequest request, HttpServletResponse
			response, Model model) {
//		Page<MonthReport> page = monthReportService.findPage(new Page<MonthReport>(request, response), monthReport);
		Page<PurReportNew> page = purReportNewService.findPage(new Page<PurReportNew>(request, response), purReportNew);
		return getBootstrapData(page);
	}




	/**
	 * 查看，增加，编辑进料不合格统计表表单页面
	 */
	@RequiresPermissions(value={"monthreport:monthReport:view","monthreport:monthReport:add","monthreport:monthReport:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(MonthReport monthReport, Model model) {
		model.addAttribute("monthReport", monthReport);
		if(StringUtils.isBlank(monthReport.getId())){//如果ID是空为添加
			model.addAttribute("isAdd", true);
		}
		return "purchasemanage/monthreport/monthReportForm";
	}


	/**
	 * 保存进料不合格统计表
	 */
	@RequiresPermissions(value={"monthreport:monthReport:add","monthreport:monthReport:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(MonthReport monthReport, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, monthReport)){
			return form(monthReport, model);
		}
		//新增或编辑表单保存
		monthReportService.save(monthReport);//保存
		addMessage(redirectAttributes, "保存进料不合格统计表成功");
		return "redirect:"+Global.getAdminPath()+"/monthreport/monthReport/?repage";
	}
	
	/**
	 * 删除进料不合格统计表
	 */
	@ResponseBody
	@RequiresPermissions("monthreport:monthReport:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(MonthReport monthReport, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		monthReportService.delete(monthReport);
		j.setMsg("删除进料不合格统计表成功");
		return j;
	}
	
	/**
	 * 批量删除进料不合格统计表
	 */
	@ResponseBody
	@RequiresPermissions("monthreport:monthReport:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			monthReportService.delete(monthReportService.get(id));
		}
		j.setMsg("删除进料不合格统计表成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("monthreport:monthReport:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(MonthReport monthReport, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "进料不合格统计表"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<MonthReport> page = monthReportService.findPage(new Page<MonthReport>(request, response, -1), monthReport);
    		new ExportExcel("进料不合格统计表", MonthReport.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出进料不合格统计表记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("monthreport:monthReport:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<MonthReport> list = ei.getDataList(MonthReport.class);
			for (MonthReport monthReport : list){
				try{
					monthReportService.save(monthReport);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条进料不合格统计表记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条进料不合格统计表记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入进料不合格统计表失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/monthreport/monthReport/?repage";
    }
	
	/**
	 * 下载导入进料不合格统计表数据模板
	 */
	@RequiresPermissions("monthreport:monthReport:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "进料不合格统计表数据导入模板.xlsx";
    		List<MonthReport> list = Lists.newArrayList(); 
    		new ExportExcel("进料不合格统计表数据", MonthReport.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/monthreport/monthReport/?repage";
    }

}