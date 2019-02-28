/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.basedata.unit.web;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import com.alibaba.fastjson.JSON;
import com.hqu.modules.basedata.accounttype.entity.AccountType;
import com.hqu.modules.basedata.unittype.entity.UnitType;
import com.hqu.modules.basedata.unittype.service.UnitTypeService;
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
import com.hqu.modules.basedata.unit.entity.Unit;
import com.hqu.modules.basedata.unit.service.UnitService;

/**
 * 计量单位定义Controller
 * @author 黄志兵
 * @version 2018-04-05
 */
@Controller
@RequestMapping(value = "${adminPath}/unit/unit")
public class UnitController extends BaseController {

	@Autowired
	private UnitService unitService;
	@Autowired
	private UnitTypeService unitTypeService;
	
	@ModelAttribute
	public Unit get(@RequestParam(required=false) String id) {
		Unit entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = unitService.get(id);
		}
		if (entity == null){
			entity = new Unit();
		}
		return entity;
	}
	
	/**
	 * 计量单位定义列表页面
	 */
	@RequiresPermissions("unit:unit:list")
	@RequestMapping(value = {"list", ""})
	public String list(Model model) {
		List<Unit> unitList = unitService.getStandUnitCode();
		model.addAttribute("standUnitType",unitList);
		return "basedata/unit/unitList";
	}
	
		/**
	 * 计量单位定义列表数据
	 */
	@ResponseBody
	@RequiresPermissions("unit:unit:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(Unit unit, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Unit> page = unitService.findPage(new Page<Unit>(request, response), unit); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑计量单位定义表单页面
	 */
	@RequiresPermissions(value={"unit:unit:view","unit:unit:add","unit:unit:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(Unit unit, Model model,String unitTypeCode) {

		UnitType unitType = null;
		if(StringUtils.isNotEmpty(unitTypeCode)){
			unitType = unitTypeService.get(unitTypeCode);
		}
		if(unitType != null){
			unit.setUnittype(unitType);
			unit.getUnittype().setUnitTypeCode(unitTypeCode);
		}

		List<Unit> unitList = unitService.getStandUnitCode();
		model.addAttribute("standUnitType",unitList);
		model.addAttribute("unit", unit);
		if(StringUtils.isBlank(unit.getId())){//如果ID是空为添加
			model.addAttribute("isAdd", true);
			//设置unitcode从0开始自增长并为4位流水压入表单中
			String unitcode = unitService.getMaxUnitCode();
			if(unitcode == null){
				unitcode = "0000";
			}
			else {
				int max = Integer.parseInt(unitcode)+1;
				unitcode = String.format("%04d",max);
			}

			unit.setUnitCode(unitcode);
		}

		return "basedata/unit/unitForm";
	}

	/**
	 * 保存计量单位定义
	 */
	@RequiresPermissions(value={"unit:unit:add","unit:unit:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(Unit unit, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, unit)){
			return form(unit, model,null);
		}
		UnitType unitType = unitTypeService.get(unit.getUnittype().getId());
		logger.debug("unittype:"+ unitType.getUnitTypeCode());
		unit.getUnittype().setId(unitType.getUnitTypeCode());
		//新增或编辑表单保存
		unitService.save(unit);//保存
		addMessage(redirectAttributes, "保存计量单位定义成功");
		return "redirect:"+Global.getAdminPath()+"/unit/unit/?repage";
	}
	
	/**
	 * 删除计量单位定义
	 */
	@ResponseBody
	@RequiresPermissions("unit:unit:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(Unit unit, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		unitService.delete(unit);
		j.setMsg("删除计量单位定义成功");
		return j;
	}
	
	/**
	 * 批量删除计量单位定义
	 */
	@ResponseBody
	@RequiresPermissions("unit:unit:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			unitService.delete(unitService.get(id));
		}
		j.setMsg("删除计量单位定义成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("unit:unit:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(Unit unit, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "计量单位定义"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<Unit> page = unitService.findPage(new Page<Unit>(request, response, -1), unit);
    		new ExportExcel("计量单位定义", Unit.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出计量单位定义记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("unit:unit:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<Unit> list = ei.getDataList(Unit.class);
			for (Unit unit : list){
				try{
					unitService.saveTry(unit);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条计量单位定义记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条计量单位定义记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入计量单位定义失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/unit/unit/?repage";
    }
	
	/**
	 * 下载导入计量单位定义数据模板
	 */
	@RequiresPermissions("unit:unit:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "计量单位定义数据导入模板.xlsx";
    		List<Unit> list = Lists.newArrayList(); 
    		new ExportExcel("计量单位定义数据", Unit.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/unit/unit/?repage";
    }

}