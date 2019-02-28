/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.purchasemanage.adtmodel.web;

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
import com.hqu.modules.purchasemanage.adtmodel.entity.AdtModel;
import com.hqu.modules.purchasemanage.adtmodel.service.AdtModelService;

/**
 * 工作流模型Controller
 * @author ckw
 * @version 2018-05-08
 */
@Controller
@RequestMapping(value = "${adminPath}/adtmodel/adtModel")
public class AdtModelController extends BaseController {

	@Autowired
	private AdtModelService adtModelService;
	
	@ModelAttribute
	public AdtModel get(@RequestParam(required=false) String id) {
		AdtModel entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = adtModelService.get(id);
		}
		if (entity == null){
			entity = new AdtModel();
		}
		return entity;
	}
	
	/**
	 * 工作流模型列表页面
	 */
	@RequiresPermissions("adtmodel:adtModel:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "purchasemanage/adtmodel/adtModelList";
	}
	
		/**
	 * 工作流模型列表数据
	 */
	@ResponseBody
	@RequiresPermissions("adtmodel:adtModel:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(AdtModel adtModel, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<AdtModel> page = adtModelService.findPage(new Page<AdtModel>(request, response), adtModel); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑工作流模型表单页面
	 */
	@RequiresPermissions(value={"adtmodel:adtModel:view","adtmodel:adtModel:add","adtmodel:adtModel:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(AdtModel adtModel, Model model) {
		model.addAttribute("adtModel", adtModel);
		if(StringUtils.isBlank(adtModel.getId())){//如果ID是空为添加
			model.addAttribute("isAdd", true);
		}
		return "purchasemanage/adtmodel/adtModelForm";
	}

	/**
	 * 保存工作流模型
	 */
	@RequiresPermissions(value={"adtmodel:adtModel:add","adtmodel:adtModel:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(AdtModel adtModel, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, adtModel)){
			return form(adtModel, model);
		}
		//新增或编辑表单保存
		adtModelService.save(adtModel);//保存
		addMessage(redirectAttributes, "保存工作流模型成功");
		return "redirect:"+Global.getAdminPath()+"/adtmodel/adtModel/?repage";
	}
	
	/**
	 * 删除工作流模型
	 */
	@ResponseBody
	@RequiresPermissions("adtmodel:adtModel:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(AdtModel adtModel, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		adtModelService.delete(adtModel);
		j.setMsg("删除工作流模型成功");
		return j;
	}
	
	/**
	 * 批量删除工作流模型
	 */
	@ResponseBody
	@RequiresPermissions("adtmodel:adtModel:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			adtModelService.delete(adtModelService.get(id));
		}
		j.setMsg("删除工作流模型成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("adtmodel:adtModel:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(AdtModel adtModel, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "工作流模型"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<AdtModel> page = adtModelService.findPage(new Page<AdtModel>(request, response, -1), adtModel);
    		new ExportExcel("工作流模型", AdtModel.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出工作流模型记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("adtmodel:adtModel:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<AdtModel> list = ei.getDataList(AdtModel.class);
			for (AdtModel adtModel : list){
				try{
					adtModelService.save(adtModel);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条工作流模型记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条工作流模型记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入工作流模型失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/adtmodel/adtModel/?repage";
    }
	
	/**
	 * 下载导入工作流模型数据模板
	 */
	@RequiresPermissions("adtmodel:adtModel:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "工作流模型数据导入模板.xlsx";
    		List<AdtModel> list = Lists.newArrayList(); 
    		new ExportExcel("工作流模型数据", AdtModel.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/adtmodel/adtModel/?repage";
    }

}