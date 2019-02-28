/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.qualitymanage.incomingstatistics.web;

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
import com.hqu.modules.qualitymanage.incomingstatistics.entity.Incomingstatistics;
import com.hqu.modules.qualitymanage.incomingstatistics.service.IncomingstatisticsService;

/**
 * 主要供应商来料不良批次统计Controller
 * @author syc
 * @version 2018-08-25
 */
@Controller
@RequestMapping(value = "${adminPath}/incomingstatistics/incomingstatistics")
public class IncomingstatisticsController extends BaseController {

	@Autowired
	private IncomingstatisticsService incomingstatisticsService;
	
	@ModelAttribute
	public Incomingstatistics get(@RequestParam(required=false) String id) {
		Incomingstatistics entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = incomingstatisticsService.get(id);
		}
		if (entity == null){
			entity = new Incomingstatistics();
		}
		return entity;
	}
	
	/**
	 * 主要供应商来料不良批次统计列表页面
	 */
	@RequiresPermissions("incomingstatistics:incomingstatistics:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "qualitymanage/incomingstatistics/incomingstatisticsList";
	}
	
		/**
	 * 主要供应商来料不良批次统计列表数据
	 */
	@ResponseBody
	@RequiresPermissions("incomingstatistics:incomingstatistics:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(Incomingstatistics incomingstatistics, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Incomingstatistics> page = incomingstatisticsService.findPage(new Page<Incomingstatistics>(request, response), incomingstatistics); 
		return getBootstrapData(page);
	}
	/**
	 * 打印
	 * @param incomingstatistics
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequiresPermissions("incomingstatistics:incomingstatistics:list")
	@RequestMapping(value = "print")
	public String print(Incomingstatistics incomingstatistics, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Incomingstatistics> page = incomingstatisticsService.findPage(new Page<Incomingstatistics>(request, response), incomingstatistics); 
		model.addAttribute("contractDetailWarningList",page.getList());
		return "qualitymanage/incomingstatistics/incomingstaticsPrintForm";
	}

	/**
	 * 查看，增加，编辑主要供应商来料不良批次统计表单页面
	 */
	@RequiresPermissions(value={"incomingstatistics:incomingstatistics:view","incomingstatistics:incomingstatistics:add","incomingstatistics:incomingstatistics:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(Incomingstatistics incomingstatistics, Model model) {
		model.addAttribute("incomingstatistics", incomingstatistics);
		if(StringUtils.isBlank(incomingstatistics.getId())){//如果ID是空为添加
			model.addAttribute("isAdd", true);
		}
		return "qualitymanage/incomingstatistics/incomingstatisticsForm";
	}

	/**
	 * 保存主要供应商来料不良批次统计
	 */
	@RequiresPermissions(value={"incomingstatistics:incomingstatistics:add","incomingstatistics:incomingstatistics:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(Incomingstatistics incomingstatistics, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, incomingstatistics)){
			return form(incomingstatistics, model);
		}
		//新增或编辑表单保存
		incomingstatisticsService.save(incomingstatistics);//保存
		addMessage(redirectAttributes, "保存主要供应商来料不良批次统计成功");
		return "redirect:"+Global.getAdminPath()+"/incomingstatistics/incomingstatistics/?repage";
	}
	
	/**
	 * 删除主要供应商来料不良批次统计
	 */
	@ResponseBody
	@RequiresPermissions("incomingstatistics:incomingstatistics:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(Incomingstatistics incomingstatistics, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		incomingstatisticsService.delete(incomingstatistics);
		j.setMsg("删除主要供应商来料不良批次统计成功");
		return j;
	}
	
	/**
	 * 批量删除主要供应商来料不良批次统计
	 */
	@ResponseBody
	@RequiresPermissions("incomingstatistics:incomingstatistics:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			incomingstatisticsService.delete(incomingstatisticsService.get(id));
		}
		j.setMsg("删除主要供应商来料不良批次统计成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("incomingstatistics:incomingstatistics:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(Incomingstatistics incomingstatistics, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "主要供应商来料不良批次统计"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<Incomingstatistics> page = incomingstatisticsService.findPage(new Page<Incomingstatistics>(request, response, -1), incomingstatistics);
    		new ExportExcel("主要供应商来料不良批次统计", Incomingstatistics.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出主要供应商来料不良批次统计记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("incomingstatistics:incomingstatistics:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<Incomingstatistics> list = ei.getDataList(Incomingstatistics.class);
			for (Incomingstatistics incomingstatistics : list){
				try{
					incomingstatisticsService.save(incomingstatistics);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条主要供应商来料不良批次统计记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条主要供应商来料不良批次统计记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入主要供应商来料不良批次统计失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/incomingstatistics/incomingstatistics/?repage";
    }
	
	/**
	 * 下载导入主要供应商来料不良批次统计数据模板
	 */
	@RequiresPermissions("incomingstatistics:incomingstatistics:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "主要供应商来料不良批次统计数据导入模板.xlsx";
    		List<Incomingstatistics> list = Lists.newArrayList(); 
    		new ExportExcel("主要供应商来料不良批次统计数据", Incomingstatistics.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/incomingstatistics/incomingstatistics/?repage";
    }

}