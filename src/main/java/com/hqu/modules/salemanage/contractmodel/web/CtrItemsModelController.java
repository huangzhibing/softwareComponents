/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.salemanage.contractmodel.web;

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
import com.hqu.modules.salemanage.contractmodel.entity.CtrItemsModel;
import com.hqu.modules.salemanage.contractmodel.service.CtrItemsModelService;

/**
 * 合同模板Controller
 * @author dongqida
 * @version 2018-05-05
 */
@Controller
@RequestMapping(value = "${adminPath}/contractmodel/ctrItemsModel")
public class CtrItemsModelController extends BaseController {

	@Autowired
	private CtrItemsModelService ctrItemsModelService;
	
	@ModelAttribute
	public CtrItemsModel get(@RequestParam(required=false) String id) {
		CtrItemsModel entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = ctrItemsModelService.get(id);
		}
		if (entity == null){
			entity = new CtrItemsModel();
		}
		return entity;
	}
	
	/**
	 * contractmodel列表页面
	 */
	@RequiresPermissions("contractmodel:ctrItemsModel:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "salemanage/contractmodel/ctrItemsModelList";
	}
	
		/**
	 * contractmodel列表数据
	 */
	@ResponseBody
	@RequiresPermissions("contractmodel:ctrItemsModel:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(CtrItemsModel ctrItemsModel, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<CtrItemsModel> page = ctrItemsModelService.findPage(new Page<CtrItemsModel>(request, response), ctrItemsModel); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑contractmodel表单页面
	 */
	@RequiresPermissions(value={"contractmodel:ctrItemsModel:view","contractmodel:ctrItemsModel:add","contractmodel:ctrItemsModel:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(CtrItemsModel ctrItemsModel, Model model) {
		model.addAttribute("ctrItemsModel", ctrItemsModel);
		if(StringUtils.isBlank(ctrItemsModel.getId())){//如果ID是空为添加
			model.addAttribute("isAdd", true);
		}
		return "salemanage/contractmodel/ctrItemsModelForm";
	}

	/**
	 * 保存contractmodel
	 */
	@RequiresPermissions(value={"contractmodel:ctrItemsModel:add","contractmodel:ctrItemsModel:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(CtrItemsModel ctrItemsModel, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, ctrItemsModel)){
			return form(ctrItemsModel, model);
		}
		//新增或编辑表单保存
		ctrItemsModelService.save(ctrItemsModel);//保存
		addMessage(redirectAttributes, "保存contractmodel成功");
		return "redirect:"+Global.getAdminPath()+"/contractmodel/ctrItemsModel/?repage";
	}
	
	/**
	 * 删除contractmodel
	 */
	@ResponseBody
	@RequiresPermissions("contractmodel:ctrItemsModel:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(CtrItemsModel ctrItemsModel, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		ctrItemsModelService.delete(ctrItemsModel);
		j.setMsg("删除contractmodel成功");
		return j;
	}
	
	/**
	 * 批量删除contractmodel
	 */
	@ResponseBody
	@RequiresPermissions("contractmodel:ctrItemsModel:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			ctrItemsModelService.delete(ctrItemsModelService.get(id));
		}
		j.setMsg("删除contractmodel成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("contractmodel:ctrItemsModel:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(CtrItemsModel ctrItemsModel, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "contractmodel"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<CtrItemsModel> page = ctrItemsModelService.findPage(new Page<CtrItemsModel>(request, response, -1), ctrItemsModel);
    		new ExportExcel("contractmodel", CtrItemsModel.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出contractmodel记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("contractmodel:ctrItemsModel:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<CtrItemsModel> list = ei.getDataList(CtrItemsModel.class);
			for (CtrItemsModel ctrItemsModel : list){
				try{
					ctrItemsModelService.save(ctrItemsModel);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条contractmodel记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条contractmodel记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入contractmodel失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/contractmodel/ctrItemsModel/?repage";
    }
	
	/**
	 * 下载导入contractmodel数据模板
	 */
	@RequiresPermissions("contractmodel:ctrItemsModel:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "contractmodel数据导入模板.xlsx";
    		List<CtrItemsModel> list = Lists.newArrayList(); 
    		new ExportExcel("contractmodel数据", CtrItemsModel.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/contractmodel/ctrItemsModel/?repage";
    }

}