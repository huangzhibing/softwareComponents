/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.salemanage.blnctypedef.web;

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
import com.hqu.modules.salemanage.blnctypedef.entity.BlncTypeDef;
import com.hqu.modules.salemanage.blnctypedef.service.BlncTypeDefService;

/**
 * 结算方式定义Controller
 * @author M1ngz
 * @version 2018-05-07
 */
@Controller
@RequestMapping(value = "${adminPath}/blnctypedef/blncTypeDef")
public class BlncTypeDefController extends BaseController {

	@Autowired
	private BlncTypeDefService blncTypeDefService;
	
	@ModelAttribute
	public BlncTypeDef get(@RequestParam(required=false) String id) {
		BlncTypeDef entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = blncTypeDefService.get(id);
		}
		if (entity == null){
			entity = new BlncTypeDef();
		}
		return entity;
	}
	
	/**
	 * 结算方式列表页面
	 */
	@RequiresPermissions("blnctypedef:blncTypeDef:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "salemanage/blnctypedef/blncTypeDefList";
	}
	
		/**
	 * 结算方式列表数据
	 */
	@ResponseBody
	@RequiresPermissions("blnctypedef:blncTypeDef:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(BlncTypeDef blncTypeDef, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<BlncTypeDef> page = blncTypeDefService.findPage(new Page<BlncTypeDef>(request, response), blncTypeDef); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑结算方式表单页面
	 */
	@RequiresPermissions(value={"blnctypedef:blncTypeDef:view","blnctypedef:blncTypeDef:add","blnctypedef:blncTypeDef:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(BlncTypeDef blncTypeDef, Model model) {
		model.addAttribute("blncTypeDef", blncTypeDef);
		if(StringUtils.isBlank(blncTypeDef.getId())){//如果ID是空为添加
			model.addAttribute("isAdd", true);
		}
		return "salemanage/blnctypedef/blncTypeDefForm";
	}

	/**
	 * 保存结算方式
	 */
	@RequiresPermissions(value={"blnctypedef:blncTypeDef:add","blnctypedef:blncTypeDef:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(BlncTypeDef blncTypeDef, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, blncTypeDef)){
			return form(blncTypeDef, model);
		}
		//新增或编辑表单保存
		blncTypeDefService.save(blncTypeDef);//保存
		addMessage(redirectAttributes, "保存结算方式成功");
		return "redirect:"+Global.getAdminPath()+"/blnctypedef/blncTypeDef/?repage";
	}
	
	/**
	 * 删除结算方式
	 */
	@ResponseBody
	@RequiresPermissions("blnctypedef:blncTypeDef:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(BlncTypeDef blncTypeDef, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		blncTypeDefService.delete(blncTypeDef);
		j.setMsg("删除结算方式成功");
		return j;
	}
	
	/**
	 * 批量删除结算方式
	 */
	@ResponseBody
	@RequiresPermissions("blnctypedef:blncTypeDef:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			blncTypeDefService.delete(blncTypeDefService.get(id));
		}
		j.setMsg("删除结算方式成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("blnctypedef:blncTypeDef:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(BlncTypeDef blncTypeDef, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "结算方式"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<BlncTypeDef> page = blncTypeDefService.findPage(new Page<BlncTypeDef>(request, response, -1), blncTypeDef);
    		new ExportExcel("结算方式", BlncTypeDef.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出结算方式记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("blnctypedef:blncTypeDef:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<BlncTypeDef> list = ei.getDataList(BlncTypeDef.class);
			for (BlncTypeDef blncTypeDef : list){
				try{
					blncTypeDefService.save(blncTypeDef);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条结算方式记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条结算方式记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入结算方式失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/blnctypedef/blncTypeDef/?repage";
    }
	
	/**
	 * 下载导入结算方式数据模板
	 */
	@RequiresPermissions("blnctypedef:blncTypeDef:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "结算方式数据导入模板.xlsx";
    		List<BlncTypeDef> list = Lists.newArrayList(); 
    		new ExportExcel("结算方式数据", BlncTypeDef.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/blnctypedef/blncTypeDef/?repage";
    }

}