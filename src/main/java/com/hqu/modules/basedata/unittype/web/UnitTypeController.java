/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.basedata.unittype.web;

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
import com.hqu.modules.basedata.unittype.entity.UnitType;
import com.hqu.modules.basedata.unittype.service.UnitTypeService;

/**
 * 计量单位类别维护Controller
 * @author yrg
 * @version 2018-04-05
 */
@Controller
@RequestMapping(value = "${adminPath}/unittype/unitType")
public class UnitTypeController extends BaseController {

	@Autowired
	private UnitTypeService unitTypeService;
	
	@ModelAttribute
	public UnitType get(@RequestParam(required=false) String id) {
		UnitType entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = unitTypeService.get(id);
		}
		if (entity == null){
			entity = new UnitType();
		}
		return entity;
	}
	
	/**
	 * 计量单位类别列表页面
	 */
	@RequiresPermissions("unittype:unitType:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "basedata/unittype/unitTypeList";
	}
	
	/**
	 * 计量单位类别列表数据
	 */
	@ResponseBody
	@RequiresPermissions("unittype:unitType:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(UnitType unitType, HttpServletRequest request, HttpServletResponse response, Model model) {
		//System.out.println(unitType.getUnitTypeName()+".+-");
		Page<UnitType> page = unitTypeService.findPage(new Page<UnitType>(request, response), unitType); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑计量单位类别表单页面
	 */
	@RequiresPermissions(value={"unittype:unitType:view","unittype:unitType:add","unittype:unitType:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(UnitType unitType, Model model) {
		model.addAttribute("unitType", unitType);
		String maxCode;
		if(StringUtils.isBlank(unitType.getId())){//如果ID是空为添加
			model.addAttribute("isAdd", true);
			if(unitTypeService.selectMax()!=null) {
				maxCode=unitTypeService.selectMax();
				int numCode=Integer.parseInt(maxCode);
				numCode++;
				maxCode=String.format("%02d", numCode);
			}else {
				maxCode="00";
			}
			unitType.setUnitTypeCode(maxCode);
		}
		return "basedata/unittype/unitTypeForm";
	}

	/**
	 * 保存计量单位类别
	 */
	@RequiresPermissions(value={"unittype:unitType:add","unittype:unitType:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(UnitType unitType, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, unitType)){
			return form(unitType, model);
		}
		//新增或编辑表单保存
		try{
			unitTypeService.save(unitType);//保存
			addMessage(redirectAttributes, "保存计量单位类别成功");
		}catch(Exception e){
			addMessage(redirectAttributes, e.getMessage());
		}finally{
			logger.info("保存计量单位类别");
		}
		
		return "redirect:"+Global.getAdminPath()+"/unittype/unitType/?repage";
	}
	
	/**
	 * 删除计量单位类别
	 */
	@ResponseBody
	@RequiresPermissions("unittype:unitType:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(UnitType unitType, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		unitTypeService.delete(unitType);
		j.setMsg("删除计量单位类别成功");
		return j;
	}
	
	/**
	 * 批量删除计量单位类别
	 */
	@ResponseBody
	@RequiresPermissions("unittype:unitType:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			unitTypeService.delete(unitTypeService.get(id));
		}
		j.setMsg("删除计量单位类别成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("unittype:unitType:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(UnitType unitType, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "计量单位类别"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<UnitType> page = unitTypeService.findPage(new Page<UnitType>(request, response, -1), unitType);
    		new ExportExcel("计量单位类别", UnitType.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出计量单位类别记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("unittype:unitType:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<UnitType> list = ei.getDataList(UnitType.class);
			for (UnitType unitType : list){
				try{
					unitTypeService.saveTry(unitType);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条计量单位类别记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条计量单位类别记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入计量单位类别失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/unittype/unitType/?repage";
    }
	
	/**
	 * 下载导入计量单位类别数据模板
	 */
	@RequiresPermissions("unittype:unitType:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "计量单位类别数据导入模板.xlsx";
    		List<UnitType> list = Lists.newArrayList(); 
    		new ExportExcel("计量单位类别数据", UnitType.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/unittype/unitType/?repage";
    }

}