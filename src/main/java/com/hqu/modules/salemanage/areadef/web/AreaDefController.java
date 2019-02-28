/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.salemanage.areadef.web;

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
import com.hqu.modules.salemanage.areadef.entity.AreaDef;
import com.hqu.modules.salemanage.areadef.service.AreaDefService;

/**
 * 销售地区定义Controller
 * @author 黄志兵
 * @version 2018-05-05
 */
@Controller
@RequestMapping(value = "${adminPath}/areadef/areaDef")
public class AreaDefController extends BaseController {

	@Autowired
	private AreaDefService areaDefService;
	
	@ModelAttribute
	public AreaDef get(@RequestParam(required=false) String id) {
		AreaDef entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = areaDefService.get(id);
		}
		if (entity == null){
			entity = new AreaDef();
		}
		return entity;
	}
	
	/**
	 * 销售地区定义列表页面
	 */
	@RequiresPermissions("areadef:areaDef:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "salemanage/areadef/areaDefList";
	}
	
		/**
	 * 销售地区定义列表数据
	 */
	@ResponseBody
	@RequiresPermissions("areadef:areaDef:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(AreaDef areaDef, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<AreaDef> page = areaDefService.findPage(new Page<AreaDef>(request, response), areaDef); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑销售地区定义表单页面
	 */
	@RequiresPermissions(value={"areadef:areaDef:view","areadef:areaDef:add","areadef:areaDef:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(AreaDef areaDef, Model model) {
		model.addAttribute("areaDef", areaDef);
		String maxCode;
		if(StringUtils.isBlank(areaDef.getId())){//如果ID是空为添加
			model.addAttribute("isAdd", true);
			if(areaDefService.selectMax()!=null) {
				maxCode=areaDefService.selectMax();
				int numCode=Integer.parseInt(maxCode);
				numCode++;
				maxCode=String.format("%02d", numCode);
			}else {
				maxCode="00";
			}
			areaDef.setAreaCode(maxCode);
		}
		return "salemanage/areadef/areaDefForm";
	}

	/**
	 * 保存销售地区定义
	 */
	@RequiresPermissions(value={"areadef:areaDef:add","areadef:areaDef:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(AreaDef areaDef, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, areaDef)){
			return form(areaDef, model);
		}
		//新增或编辑表单保存
		areaDefService.save(areaDef);//保存
		addMessage(redirectAttributes, "保存销售地区定义成功");
		return "redirect:"+Global.getAdminPath()+"/areadef/areaDef/?repage";
	}
	
	/**
	 * 删除销售地区定义
	 */
	@ResponseBody
	@RequiresPermissions("areadef:areaDef:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(AreaDef areaDef, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		areaDefService.delete(areaDef);
		j.setMsg("删除销售地区定义成功");
		return j;
	}
	
	/**
	 * 批量删除销售地区定义
	 */
	@ResponseBody
	@RequiresPermissions("areadef:areaDef:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			areaDefService.delete(areaDefService.get(id));
		}
		j.setMsg("删除销售地区定义成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("areadef:areaDef:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(AreaDef areaDef, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "销售地区定义"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<AreaDef> page = areaDefService.findPage(new Page<AreaDef>(request, response, -1), areaDef);
    		new ExportExcel("销售地区定义", AreaDef.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出销售地区定义记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("areadef:areaDef:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<AreaDef> list = ei.getDataList(AreaDef.class);
			for (AreaDef areaDef : list){
				try{
					areaDefService.saveExc(areaDef);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条销售地区定义记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条销售地区定义记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入销售地区定义失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/areadef/areaDef/?repage";
    }
	
	/**
	 * 下载导入销售地区定义数据模板
	 */
	@RequiresPermissions("areadef:areaDef:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "销售地区定义数据导入模板.xlsx";
    		List<AreaDef> list = Lists.newArrayList(); 
    		new ExportExcel("销售地区定义数据", AreaDef.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/areadef/areaDef/?repage";
    }

}