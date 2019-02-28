/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.purchasemanage.taxratio.web;

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
import com.hqu.modules.purchasemanage.taxratio.entity.TaxRatio;
import com.hqu.modules.purchasemanage.taxratio.service.TaxRatioService;

/**
 * 采购税率定义Controller
 * @author luyumiao
 * @version 2018-04-25
 */
@Controller
@RequestMapping(value = "${adminPath}/taxratio/taxRatio")
public class TaxRatioController extends BaseController {

	@Autowired
	private TaxRatioService taxRatioService;
	
	@ModelAttribute
	public TaxRatio get(@RequestParam(required=false) String id) {
		TaxRatio entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = taxRatioService.get(id);
		}
		if (entity == null){
			entity = new TaxRatio();
		}
		return entity;
	}
	
	/**
	 * 采购税率定义列表页面
	 */
	@RequiresPermissions("taxratio:taxRatio:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "purchasemanage/taxratio/taxRatioList";
	}
	
		/**
	 * 采购税率定义列表数据
	 */
	@ResponseBody
	@RequiresPermissions("taxratio:taxRatio:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(TaxRatio taxRatio, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<TaxRatio> page = taxRatioService.findPage(new Page<TaxRatio>(request, response), taxRatio); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑采购税率定义表单页面
	 */
	@RequiresPermissions(value={"taxratio:taxRatio:view","taxratio:taxRatio:add","taxratio:taxRatio:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(TaxRatio taxRatio, Model model) {
		model.addAttribute("taxRatio", taxRatio);
		if(StringUtils.isBlank(taxRatio.getId())){//如果ID是空为添加
			model.addAttribute("isAdd", true);
			//设置typecode从0开始自增长并为6位流水压入表单中
			String typecode = taxRatioService.getMaxTaxratioId();
			if(typecode == null) {
				typecode = "000000";
			}
			else {
				int maxnum = Integer.parseInt(typecode) + 1;
				typecode = String.format("%06d",maxnum);
			}

			taxRatio.setTaxratioId(typecode);
		}
		return "purchasemanage/taxratio/taxRatioForm";
	}

	/**
	 * 保存采购税率定义
	 */
	@RequiresPermissions(value={"taxratio:taxRatio:add","taxratio:taxRatio:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(TaxRatio taxRatio, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, taxRatio)){
			return form(taxRatio, model);
		}
		//新增或编辑表单保存
		taxRatioService.save(taxRatio);//保存
		addMessage(redirectAttributes, "保存采购税率定义成功");
		return "redirect:"+Global.getAdminPath()+"/taxratio/taxRatio/?repage";
	}
	
	/**
	 * 删除采购税率定义
	 */
	@ResponseBody
	@RequiresPermissions("taxratio:taxRatio:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(TaxRatio taxRatio, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		taxRatioService.delete(taxRatio);
		j.setMsg("删除采购税率定义成功");
		return j;
	}
	
	/**
	 * 批量删除采购税率定义
	 */
	@ResponseBody
	@RequiresPermissions("taxratio:taxRatio:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			taxRatioService.delete(taxRatioService.get(id));
		}
		j.setMsg("删除采购税率定义成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("taxratio:taxRatio:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(TaxRatio taxRatio, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "采购税率定义"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<TaxRatio> page = taxRatioService.findPage(new Page<TaxRatio>(request, response, -1), taxRatio);
    		new ExportExcel("采购税率定义", TaxRatio.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出采购税率定义记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("taxratio:taxRatio:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<TaxRatio> list = ei.getDataList(TaxRatio.class);
			for (TaxRatio taxRatio : list){
				try{
					taxRatioService.save(taxRatio);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条采购税率定义记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条采购税率定义记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入采购税率定义失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/taxratio/taxRatio/?repage";
    }
	
	/**
	 * 下载导入采购税率定义数据模板
	 */
	@RequiresPermissions("taxratio:taxRatio:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "采购税率定义数据导入模板.xlsx";
    		List<TaxRatio> list = Lists.newArrayList(); 
    		new ExportExcel("采购税率定义数据", TaxRatio.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/taxratio/taxRatio/?repage";
    }

}