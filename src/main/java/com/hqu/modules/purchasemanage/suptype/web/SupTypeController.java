/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.purchasemanage.suptype.web;

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
import com.hqu.modules.purchasemanage.suptype.entity.SupType;
import com.hqu.modules.purchasemanage.suptype.service.SupTypeService;

/**
 * 供应商类别定义Controller
 * @author tyo
 * @version 2018-04-18
 */
@Controller
@RequestMapping(value = "${adminPath}/suptype/supType")
public class SupTypeController extends BaseController {

	@Autowired
	private SupTypeService supTypeService;
	
	@ModelAttribute
	public SupType get(@RequestParam(required=false) String id) {
		SupType entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = supTypeService.get(id);
		}
		if (entity == null){
			entity = new SupType();
		}
		return entity;
	}
	
	/**
	 * 供应商类别定义列表页面
	 */
	@RequiresPermissions("suptype:supType:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "purchasemanage/suptype/supTypeList";
	}
	
		/**
	 * 供应商类别定义列表数据
	 */
	@ResponseBody
	@RequiresPermissions("suptype:supType:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(SupType supType, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<SupType> page = supTypeService.findPage(new Page<SupType>(request, response), supType); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑供应商类别定义表单页面
	 */
	@RequiresPermissions(value={"suptype:supType:view","suptype:supType:add","suptype:supType:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(SupType supType, Model model) {
		model.addAttribute("supType", supType);
		if(StringUtils.isBlank(supType.getId())){//如果ID是空为添加
			model.addAttribute("isAdd", true);
		}
		return "purchasemanage/suptype/supTypeForm";
	}

	/**
	 * 保存供应商类别定义
	 */
	@RequiresPermissions(value={"suptype:supType:add","suptype:supType:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(SupType supType, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, supType)){
			return form(supType, model);
		}
		//新增或编辑表单保存
		supTypeService.save(supType);//保存
		addMessage(redirectAttributes, "保存供应商类别定义成功");
		return "redirect:"+Global.getAdminPath()+"/suptype/supType/?repage";
	}
	
	/**
	 * 删除供应商类别定义
	 */
	@ResponseBody
	@RequiresPermissions("suptype:supType:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(SupType supType, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		supTypeService.delete(supType);
		j.setMsg("删除供应商类别定义成功");
		return j;
	}
	
	/**
	 * 批量删除供应商类别定义
	 */
	@ResponseBody
	@RequiresPermissions("suptype:supType:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			supTypeService.delete(supTypeService.get(id));
		}
		j.setMsg("删除供应商类别定义成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("suptype:supType:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(SupType supType, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "供应商类别定义"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<SupType> page = supTypeService.findPage(new Page<SupType>(request, response, -1), supType);
    		new ExportExcel("供应商类别定义", SupType.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出供应商类别定义记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("suptype:supType:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<SupType> list = ei.getDataList(SupType.class);
			for (SupType supType : list){
				try{
					supTypeService.save(supType);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条供应商类别定义记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条供应商类别定义记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入供应商类别定义失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/suptype/supType/?repage";
    }
	
	/**
	 * 下载导入供应商类别定义数据模板
	 */
	@RequiresPermissions("suptype:supType:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "供应商类别定义数据导入模板.xlsx";
    		List<SupType> list = Lists.newArrayList(); 
    		new ExportExcel("供应商类别定义数据", SupType.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/suptype/supType/?repage";
    }

}