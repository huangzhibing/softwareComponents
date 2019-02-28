/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.qualitymanage.qmsobjecttype.web;

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
import com.hqu.modules.qualitymanage.qmsobjecttype.entity.ObjectType;
import com.hqu.modules.qualitymanage.qmsobjecttype.service.ObjectTypeService;

/**
 * 检验对象类型Controller
 * @author tyo
 * @version 2018-04-26
 */
@Controller
@RequestMapping(value = "${adminPath}/qmsobjecttype/objectType")
public class ObjectTypeController extends BaseController {

	@Autowired
	private ObjectTypeService objectTypeService;
	
	@ModelAttribute
	public ObjectType get(@RequestParam(required=false) String id) {
		ObjectType entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = objectTypeService.get(id);
		}
		if (entity == null){
			entity = new ObjectType();
		}
		return entity;
	}
	
	/**
	 * 检验对象类型列表页面
	 */
	@RequiresPermissions("qmsobjecttype:objectType:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "qualitymanage/qmsobjecttype/objectTypeList";
	}
	
		/**
	 * 检验对象类型列表数据
	 */
	@ResponseBody
	@RequiresPermissions("qmsobjecttype:objectType:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(ObjectType objectType, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<ObjectType> page = objectTypeService.findPage(new Page<ObjectType>(request, response), objectType); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑检验对象类型表单页面
	 */
	@RequiresPermissions(value={"qmsobjecttype:objectType:view","qmsobjecttype:objectType:add","qmsobjecttype:objectType:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(ObjectType objectType, Model model) {
		model.addAttribute("objectType", objectType);
		if(StringUtils.isBlank(objectType.getId())){//如果ID是空为添加
			model.addAttribute("isAdd", true);
		}
		return "qualitymanage/qmsobjecttype/objectTypeForm";
	}

	/**
	 * 保存检验对象类型
	 */
	@RequiresPermissions(value={"qmsobjecttype:objectType:add","qmsobjecttype:objectType:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(ObjectType objectType, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, objectType)){
			return form(objectType, model);
		}
		//新增或编辑表单保存
		objectTypeService.save(objectType);//保存
		addMessage(redirectAttributes, "保存检验对象类型成功");
		return "redirect:"+Global.getAdminPath()+"/qmsobjecttype/objectType/?repage";
	}
	
	/**
	 * 删除检验对象类型
	 */
	@ResponseBody
	@RequiresPermissions("qmsobjecttype:objectType:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(ObjectType objectType, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		objectTypeService.delete(objectType);
		j.setMsg("删除检验对象类型成功");
		return j;
	}
	
	/**
	 * 批量删除检验对象类型
	 */
	@ResponseBody
	@RequiresPermissions("qmsobjecttype:objectType:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			objectTypeService.delete(objectTypeService.get(id));
		}
		j.setMsg("删除检验对象类型成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("qmsobjecttype:objectType:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(ObjectType objectType, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "检验对象类型"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<ObjectType> page = objectTypeService.findPage(new Page<ObjectType>(request, response, -1), objectType);
    		new ExportExcel("检验对象类型", ObjectType.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出检验对象类型记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("qmsobjecttype:objectType:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<ObjectType> list = ei.getDataList(ObjectType.class);
			for (ObjectType objectType : list){
				try{
					objectTypeService.save(objectType);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条检验对象类型记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条检验对象类型记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入检验对象类型失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/qmsobjecttype/objectType/?repage";
    }
	
	/**
	 * 下载导入检验对象类型数据模板
	 */
	@RequiresPermissions("qmsobjecttype:objectType:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "检验对象类型数据导入模板.xlsx";
    		List<ObjectType> list = Lists.newArrayList(); 
    		new ExportExcel("检验对象类型数据", ObjectType.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/qmsobjecttype/objectType/?repage";
    }

}