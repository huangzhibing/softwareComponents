/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.workshopmanage.shiftdef.web;

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
import com.hqu.modules.workshopmanage.shiftdef.entity.ShiftDef;
import com.hqu.modules.workshopmanage.shiftdef.service.ShiftDefService;

/**
 * 班次定义Controller
 * @author zhangxin
 * @version 2018-05-25
 */
@Controller
@RequestMapping(value = "${adminPath}/shiftdef/shiftDef")
public class ShiftDefController extends BaseController {

	@Autowired
	private ShiftDefService shiftDefService;
	
	@ModelAttribute
	public ShiftDef get(@RequestParam(required=false) String id) {
		ShiftDef entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = shiftDefService.get(id);
		}
		if (entity == null){
			entity = new ShiftDef();
		}
		return entity;
	}
	
	/**
	 * 班次列表页面
	 */
	@RequiresPermissions("shiftdef:shiftDef:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "workshopmanage/shiftdef/shiftDefList";
	}
	
		/**
	 * 班次列表数据
	 */
	@ResponseBody
	@RequiresPermissions("shiftdef:shiftDef:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(ShiftDef shiftDef, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<ShiftDef> page = shiftDefService.findPage(new Page<ShiftDef>(request, response), shiftDef); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑班次表单页面
	 */
	@RequiresPermissions(value={"shiftdef:shiftDef:view","shiftdef:shiftDef:add","shiftdef:shiftDef:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(ShiftDef shiftDef, Model model) {
		model.addAttribute("shiftDef", shiftDef);
		if(StringUtils.isBlank(shiftDef.getId())){//如果ID是空为添加
			model.addAttribute("isAdd", true);
		}
		return "workshopmanage/shiftdef/shiftDefForm";
	}

	/**
	 * 保存班次
	 */
	@RequiresPermissions(value={"shiftdef:shiftDef:add","shiftdef:shiftDef:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(ShiftDef shiftDef, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, shiftDef)){
			return form(shiftDef, model);
		}
		//新增或编辑表单保存
		shiftDefService.save(shiftDef);//保存
		addMessage(redirectAttributes, "保存班次成功");
		return "redirect:"+Global.getAdminPath()+"/shiftdef/shiftDef/?repage";
	}
	
	/**
	 * 删除班次
	 */
	@ResponseBody
	@RequiresPermissions("shiftdef:shiftDef:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(ShiftDef shiftDef, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		shiftDefService.delete(shiftDef);
		j.setMsg("删除班次成功");
		return j;
	}
	
	/**
	 * 批量删除班次
	 */
	@ResponseBody
	@RequiresPermissions("shiftdef:shiftDef:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			shiftDefService.delete(shiftDefService.get(id));
		}
		j.setMsg("删除班次成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("shiftdef:shiftDef:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(ShiftDef shiftDef, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "班次"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<ShiftDef> page = shiftDefService.findPage(new Page<ShiftDef>(request, response, -1), shiftDef);
    		new ExportExcel("班次", ShiftDef.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出班次记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("shiftdef:shiftDef:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<ShiftDef> list = ei.getDataList(ShiftDef.class);
			for (ShiftDef shiftDef : list){
				try{
					shiftDefService.save(shiftDef);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条班次记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条班次记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入班次失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/shiftdef/shiftDef/?repage";
    }
	
	/**
	 * 下载导入班次数据模板
	 */
	@RequiresPermissions("shiftdef:shiftDef:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "班次数据导入模板.xlsx";
    		List<ShiftDef> list = Lists.newArrayList(); 
    		new ExportExcel("班次数据", ShiftDef.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/shiftdef/shiftDef/?repage";
    }

}