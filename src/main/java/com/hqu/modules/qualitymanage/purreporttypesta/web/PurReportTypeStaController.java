/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.qualitymanage.purreporttypesta.web;

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
import com.hqu.modules.qualitymanage.purreporttypesta.entity.PurReportTypeSta;
import com.hqu.modules.qualitymanage.purreporttypesta.service.PurReportTypeStaService;

/**
 * 来料按物料类别统计分析Controller
 * @author yxb
 * @version 2018-08-25
 */
@Controller
@RequestMapping(value = "${adminPath}/purreporttypesta/purReportTypeSta")
public class PurReportTypeStaController extends BaseController {

	@Autowired
	private PurReportTypeStaService purReportTypeStaService;
	
	@ModelAttribute
	public PurReportTypeSta get(@RequestParam(required=false) String id) {
		PurReportTypeSta entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = purReportTypeStaService.get(id);
		}
		if (entity == null){
			entity = new PurReportTypeSta();
		}
		return entity;
	}
	
	/**
	 * 来料按物料类别统计分析列表页面
	 */
	@RequiresPermissions("purreporttypesta:purReportTypeSta:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "qualitymanage/purreporttypesta/purReportTypeStaList";
	}
	
		/**
	 * 来料按物料类别统计分析列表数据
	 */
	@ResponseBody
	@RequiresPermissions("purreporttypesta:purReportTypeSta:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(PurReportTypeSta purReportTypeSta, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<PurReportTypeSta> page = purReportTypeStaService.findPage(new Page<PurReportTypeSta>(request, response), purReportTypeSta); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑来料按物料类别统计分析表单页面
	 */
	@RequiresPermissions(value={"purreporttypesta:purReportTypeSta:view","purreporttypesta:purReportTypeSta:add","purreporttypesta:purReportTypeSta:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(PurReportTypeSta purReportTypeSta, Model model) {
		model.addAttribute("purReportTypeSta", purReportTypeSta);
		if(StringUtils.isBlank(purReportTypeSta.getId())){//如果ID是空为添加
			model.addAttribute("isAdd", true);
		}
		return "qualitymanage/purreporttypesta/purReportTypeStaForm";
	}

	/**
	 * 保存来料按物料类别统计分析
	 */
	@RequiresPermissions(value={"purreporttypesta:purReportTypeSta:add","purreporttypesta:purReportTypeSta:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(PurReportTypeSta purReportTypeSta, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, purReportTypeSta)){
			return form(purReportTypeSta, model);
		}
		//新增或编辑表单保存
		purReportTypeStaService.save(purReportTypeSta);//保存
		addMessage(redirectAttributes, "保存来料按物料类别统计分析成功");
		return "redirect:"+Global.getAdminPath()+"/purreporttypesta/purReportTypeSta/?repage";
	}
	
	/**
	 * 删除来料按物料类别统计分析
	 */
	@ResponseBody
	@RequiresPermissions("purreporttypesta:purReportTypeSta:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(PurReportTypeSta purReportTypeSta, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		purReportTypeStaService.delete(purReportTypeSta);
		j.setMsg("删除来料按物料类别统计分析成功");
		return j;
	}
	
	/**
	 * 批量删除来料按物料类别统计分析
	 */
	@ResponseBody
	@RequiresPermissions("purreporttypesta:purReportTypeSta:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			purReportTypeStaService.delete(purReportTypeStaService.get(id));
		}
		j.setMsg("删除来料按物料类别统计分析成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("purreporttypesta:purReportTypeSta:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(PurReportTypeSta purReportTypeSta, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "来料按物料类别统计分析"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<PurReportTypeSta> page = purReportTypeStaService.findPage(new Page<PurReportTypeSta>(request, response, -1), purReportTypeSta);
    		new ExportExcel("来料按物料类别统计分析", PurReportTypeSta.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出来料按物料类别统计分析记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("purreporttypesta:purReportTypeSta:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<PurReportTypeSta> list = ei.getDataList(PurReportTypeSta.class);
			for (PurReportTypeSta purReportTypeSta : list){
				try{
					purReportTypeStaService.save(purReportTypeSta);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条来料按物料类别统计分析记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条来料按物料类别统计分析记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入来料按物料类别统计分析失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/purreporttypesta/purReportTypeSta/?repage";
    }
	
	/**
	 * 下载导入来料按物料类别统计分析数据模板
	 */
	@RequiresPermissions("purreporttypesta:purReportTypeSta:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "来料按物料类别统计分析数据导入模板.xlsx";
    		List<PurReportTypeSta> list = Lists.newArrayList(); 
    		new ExportExcel("来料按物料类别统计分析数据", PurReportTypeSta.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/purreporttypesta/purReportTypeSta/?repage";
    }

}