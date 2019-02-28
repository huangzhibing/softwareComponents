/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.salemanage.saltaxratio.web;

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
import com.hqu.modules.salemanage.saltaxratio.entity.SaleTaxRatio;
import com.hqu.modules.salemanage.saltaxratio.service.SaleTaxRatioService;

/**
 * 销售税率定义Controller
 * @author liujiachen
 * @version 2018-05-09
 */
@Controller
@RequestMapping(value = "${adminPath}/saltaxratio/saleTaxRatio")
public class SaleTaxRatioController extends BaseController {

	@Autowired
	private SaleTaxRatioService saleTaxRatioService;
	
	@ModelAttribute
	public SaleTaxRatio get(@RequestParam(required=false) String id) {
		SaleTaxRatio entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = saleTaxRatioService.get(id);
		}
		if (entity == null){
			entity = new SaleTaxRatio();
		}
		return entity;
	}
	
	/**
	 * 销售税率列表页面
	 */
	@RequiresPermissions("saltaxratio:saleTaxRatio:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "salemanage/saltaxratio/saleTaxRatioList";
	}
	
		/**
	 * 销售税率列表数据
	 */
	@ResponseBody
	@RequiresPermissions("saltaxratio:saleTaxRatio:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(SaleTaxRatio saleTaxRatio, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<SaleTaxRatio> page = saleTaxRatioService.findPage(new Page<SaleTaxRatio>(request, response), saleTaxRatio); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑销售税率表单页面
	 */
	@RequiresPermissions(value={"saltaxratio:saleTaxRatio:view","saltaxratio:saleTaxRatio:add","saltaxratio:saleTaxRatio:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(SaleTaxRatio saleTaxRatio, Model model) {
		model.addAttribute("saleTaxRatio", saleTaxRatio);
		if(StringUtils.isBlank(saleTaxRatio.getId())){//如果ID是空为添加
			String salTaxRatioID = saleTaxRatioService.getMaxSalTaxRatioID();
			if(salTaxRatioID == null) {
				salTaxRatioID = "01";
			}
			else {
				int maxnum = Integer.parseInt(salTaxRatioID) + 1;
				salTaxRatioID = String.format("%02d",maxnum);
			}

			saleTaxRatio.setSalTaxCode(salTaxRatioID);
			model.addAttribute("isAdd", true);
		}
		//设置salTaxRatioID从0开始自增长并为2位流水压入表单中
		model.addAttribute("saleTaxRatio", saleTaxRatio);
		return "salemanage/saltaxratio/saleTaxRatioForm";
	}

	/**
	 * 保存销售税率
	 */
	@RequiresPermissions(value={"saltaxratio:saleTaxRatio:add","saltaxratio:saleTaxRatio:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(SaleTaxRatio saleTaxRatio, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, saleTaxRatio)){
			return form(saleTaxRatio, model);
		}
		//新增或编辑表单保存
		saleTaxRatioService.save(saleTaxRatio);//保存
		addMessage(redirectAttributes, "保存销售税率成功");
		return "redirect:"+Global.getAdminPath()+"/saltaxratio/saleTaxRatio/?repage";
	}
	
	/**
	 * 删除销售税率
	 */
	@ResponseBody
	@RequiresPermissions("saltaxratio:saleTaxRatio:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(SaleTaxRatio saleTaxRatio, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		saleTaxRatioService.delete(saleTaxRatio);
		j.setMsg("删除销售税率成功");
		return j;
	}
	
	/**
	 * 批量删除销售税率
	 */
	@ResponseBody
	@RequiresPermissions("saltaxratio:saleTaxRatio:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			saleTaxRatioService.delete(saleTaxRatioService.get(id));
		}
		j.setMsg("删除销售税率成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("saltaxratio:saleTaxRatio:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(SaleTaxRatio saleTaxRatio, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "销售税率"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<SaleTaxRatio> page = saleTaxRatioService.findPage(new Page<SaleTaxRatio>(request, response, -1), saleTaxRatio);
    		new ExportExcel("销售税率", SaleTaxRatio.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出销售税率记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("saltaxratio:saleTaxRatio:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<SaleTaxRatio> list = ei.getDataList(SaleTaxRatio.class);
			for (SaleTaxRatio saleTaxRatio : list){
				try{
					saleTaxRatioService.save(saleTaxRatio);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条销售税率记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条销售税率记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入销售税率失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/saltaxratio/saleTaxRatio/?repage";
    }
	
	/**
	 * 下载导入销售税率数据模板
	 */
	@RequiresPermissions("saltaxratio:saleTaxRatio:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "销售税率数据导入模板.xlsx";
    		List<SaleTaxRatio> list = Lists.newArrayList(); 
    		new ExportExcel("销售税率数据", SaleTaxRatio.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/saltaxratio/saleTaxRatio/?repage";
    }

}