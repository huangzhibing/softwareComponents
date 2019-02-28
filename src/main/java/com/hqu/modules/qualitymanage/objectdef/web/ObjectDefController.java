/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.qualitymanage.objectdef.web;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import com.hqu.modules.qualitymanage.qualitynorm.entity.QualityNorm;
import com.hqu.modules.qualitymanage.qualitynorm.service.QualityNormService;
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
import com.hqu.modules.qualitymanage.objectdef.entity.ObjectDef;
import com.hqu.modules.qualitymanage.objectdef.service.ObjectDefService;

/**
 * 检验对象定义Controller
 * @author M1ngz
 * @version 2018-04-07
 */
@Controller
@RequestMapping(value = "${adminPath}/objectdef/objectDef")
public class ObjectDefController extends BaseController {

	@Autowired
	private ObjectDefService objectDefService;
	@Autowired
	private QualityNormService qualityNormService;

	@ModelAttribute
	public ObjectDef get(@RequestParam(required=false) String id) {
		ObjectDef entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = objectDefService.get(id);
		}
		if (entity == null){
			entity = new ObjectDef();
		}
		return entity;
	}
	
	/**
	 * 检验对象列表页面
	 */
	@RequiresPermissions("objectdef:objectDef:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "qualitymanage/objectdef/objectDefList";
	}
	
		/**
	 * 检验对象列表数据
	 */
	@ResponseBody
	@RequiresPermissions("objectdef:objectDef:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(ObjectDef objectDef, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<ObjectDef> page = objectDefService.findPage(new Page<ObjectDef>(request, response), objectDef); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑检验对象表单页面
	 */
	@RequiresPermissions(value={"objectdef:objectDef:view","objectdef:objectDef:add","objectdef:objectDef:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(ObjectDef objectDef, Model model) {
		model.addAttribute("objectDef", objectDef);
		model.addAttribute("qualityNormList",qualityNormService.getAll());
		if(StringUtils.isBlank(objectDef.getId())){//如果ID是空为添加
			model.addAttribute("isAdd", true);
		}
		return "qualitymanage/objectdef/objectDefForm";
	}

	/**
	 * 保存检验对象
	 */
	@RequiresPermissions(value={"objectdef:objectDef:add","objectdef:objectDef:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(ObjectDef objectDef, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, objectDef)){
			return form(objectDef, model);
		}
		//新增或编辑表单保存
		objectDefService.save(objectDef);//保存
		addMessage(redirectAttributes, "保存检验对象成功");
		return "redirect:"+Global.getAdminPath()+"/objectdef/objectDef/?repage";
	}
	
	/**
	 * 删除检验对象
	 */
	@ResponseBody
	@RequiresPermissions("objectdef:objectDef:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(ObjectDef objectDef, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		objectDefService.delete(objectDef);
		j.setMsg("删除检验对象成功");
		return j;
	}



    @RequiresPermissions("objectdef:objectDef:list")
    @RequestMapping(value = "chkCode")
    @ResponseBody
    public String checkCode(String accCode){
        Integer accountSize = objectDefService.getCodeNum(accCode);
        if(accountSize > 0){
            logger.debug("已存在");
            return "false";
        } else {
            logger.debug("未存在");
            return "true";
        }
    }
	/**
	 * 批量删除检验对象
	 */
	@ResponseBody
	@RequiresPermissions("objectdef:objectDef:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			objectDefService.delete(objectDefService.get(id));
		}
		j.setMsg("删除检验对象成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("objectdef:objectDef:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(ObjectDef objectDef, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "检验对象"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<ObjectDef> page = objectDefService.findPage(new Page<ObjectDef>(request, response, -1), objectDef);
    		new ExportExcel("检验对象", ObjectDef.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出检验对象记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("objectdef:objectDef:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<ObjectDef> list = ei.getDataList(ObjectDef.class);
			for (ObjectDef objectDef : list){
				try{
					objectDefService.save(objectDef);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条检验对象记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条检验对象记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入检验对象失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/objectdef/objectDef/?repage";
    }
	
	/**
	 * 下载导入检验对象数据模板
	 */
	@RequiresPermissions("objectdef:objectDef:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "检验对象数据导入模板.xlsx";
    		List<ObjectDef> list = Lists.newArrayList(); 
    		new ExportExcel("检验对象数据", ObjectDef.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/objectdef/objectDef/?repage";
    }
	

}