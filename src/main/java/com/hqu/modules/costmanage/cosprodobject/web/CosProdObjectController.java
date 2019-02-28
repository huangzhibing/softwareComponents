/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.costmanage.cosprodobject.web;

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
import com.hqu.modules.costmanage.cosprodobject.entity.CosProdObject;
import com.hqu.modules.costmanage.cosprodobject.service.CosProdObjectService;

/**
 * 核算对象定义(右表）Controller
 * @author zz
 * @version 2018-09-07
 */
@Controller
@RequestMapping(value = "${adminPath}/cosprodobject/cosProdObject")
public class CosProdObjectController extends BaseController {

	@Autowired
	private CosProdObjectService cosProdObjectService;
	
	@ModelAttribute
	public CosProdObject get(@RequestParam(required=false) String id) {
		CosProdObject entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = cosProdObjectService.get(id);
		}
		if (entity == null){
			entity = new CosProdObject();
		}
		return entity;
	}
	
	/**
	 * 核算对象定义(右表）列表页面
	 */
	//@RequiresPermissions("cosprodobject:cosProdObject:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "costmanage/cosprodobject/cosProdObjectList";
	}
	
		/**
	 * 核算对象定义(右表）列表数据
	 */
	@ResponseBody
	//@RequiresPermissions("cosprodobject:cosProdObject:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(CosProdObject cosProdObject, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<CosProdObject> page = cosProdObjectService.findPage(new Page<CosProdObject>(request, response), cosProdObject); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑核算对象定义(右表）表单页面
	 */
	//@RequiresPermissions(value={"cosprodobject:cosProdObject:view","cosprodobject:cosProdObject:add","cosprodobject:cosProdObject:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(CosProdObject cosProdObject, Model model) {
		model.addAttribute("cosProdObject", cosProdObject);
		if(StringUtils.isBlank(cosProdObject.getId())){//如果ID是空为添加
			model.addAttribute("isAdd", true);
		}
		return "costmanage/cosprodobject/cosProdObjectForm";
	}

	/**
	 * 保存核算对象定义(右表）
	 */
	//@RequiresPermissions(value={"cosprodobject:cosProdObject:add","cosprodobject:cosProdObject:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(CosProdObject cosProdObject, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, cosProdObject)){
			return form(cosProdObject, model);
		}
		cosProdObject.setProdFinish("Y");
		//新增或编辑表单保存
		cosProdObjectService.save(cosProdObject);//保存
		addMessage(redirectAttributes, "保存核算对象定义(右表）成功");
		return "redirect:"+Global.getAdminPath()+"/cosprodobject/cosProdObject/?repage";
	}
	
	/**
	 * 删除核算对象定义(右表）
	 */
	@ResponseBody
	//@RequiresPermissions("cosprodobject:cosProdObject:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(CosProdObject cosProdObject, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		cosProdObjectService.delete(cosProdObject);
		j.setMsg("删除核算对象定义(右表）成功");
		return j;
	}
	
	/**
	 * 批量删除核算对象定义(右表）
	 */
	@ResponseBody
	//@RequiresPermissions("cosprodobject:cosProdObject:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			cosProdObjectService.delete(cosProdObjectService.get(id));
		}
		j.setMsg("删除核算对象定义(右表）成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	//@RequiresPermissions("cosprodobject:cosProdObject:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(CosProdObject cosProdObject, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "核算对象定义(右表）"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<CosProdObject> page = cosProdObjectService.findPage(new Page<CosProdObject>(request, response, -1), cosProdObject);
    		new ExportExcel("核算对象定义(右表）", CosProdObject.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出核算对象定义(右表）记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	//@RequiresPermissions("cosprodobject:cosProdObject:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<CosProdObject> list = ei.getDataList(CosProdObject.class);
			for (CosProdObject cosProdObject : list){
				try{
					cosProdObjectService.save(cosProdObject);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条核算对象定义(右表）记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条核算对象定义(右表）记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入核算对象定义(右表）失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/cosprodobject/cosProdObject/?repage";
    }
	
	/**
	 * 下载导入核算对象定义(右表）数据模板
	 */
	//@RequiresPermissions("cosprodobject:cosProdObject:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "核算对象定义(右表）数据导入模板.xlsx";
    		List<CosProdObject> list = Lists.newArrayList(); 
    		new ExportExcel("核算对象定义(右表）数据", CosProdObject.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/cosprodobject/cosProdObject/?repage";
    }

}