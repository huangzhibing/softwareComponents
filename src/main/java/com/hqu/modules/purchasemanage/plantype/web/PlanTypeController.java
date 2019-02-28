/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.purchasemanage.plantype.web;

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
import com.hqu.modules.purchasemanage.plantype.entity.PlanType;
import com.hqu.modules.purchasemanage.plantype.service.PlanTypeService;

/**
 * PlanTypeController
 * @author 方翠虹
 * @version 2018-04-14
 */
@Controller
@RequestMapping(value = "${adminPath}/plantype/planType")
public class PlanTypeController extends BaseController {

	@Autowired
	private PlanTypeService planTypeService;
	
	@ModelAttribute
	public PlanType get(@RequestParam(required=false) String id) {
		PlanType entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = planTypeService.get(id);
		}
		if (entity == null){
			entity = new PlanType();
		}
		return entity;
	}
	
	/**
	 * 计划类别定义列表页面
	 */
	@RequiresPermissions("plantype:planType:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "purchasemanage/plantype/planTypeList";
	}
	
		/**
	 * 计划类别定义列表数据
	 */
	@ResponseBody
	@RequiresPermissions("plantype:planType:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(PlanType planType, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<PlanType> page = planTypeService.findPage(new Page<PlanType>(request, response), planType); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑计划类别定义表单页面
	 */
	@RequiresPermissions(value={"plantype:planType:view","plantype:planType:add","plantype:planType:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(PlanType planType, Model model) {
		model.addAttribute("planType", planType);
		if(StringUtils.isBlank(planType.getId())){//如果ID是空为添加
			model.addAttribute("isAdd", true);
			//设置plantypeid从0开始自增长并为2位流水压入表单中
			String plantypeid = planTypeService.getMaxPlantypeId();
			if(plantypeid == null) {
				plantypeid = "00";
			}
			else {
				int maxnum = Integer.parseInt(plantypeid) + 1;
				plantypeid = String.format("%02d",maxnum);
			}

			planType.setPlantypeId(plantypeid);
		}
		return "purchasemanage/plantype/planTypeForm";
	}

	/**
	 * 保存计划类别定义
	 */
	@RequiresPermissions(value={"plantype:planType:add","plantype:planType:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(PlanType planType, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, planType)){
			return form(planType, model);
		}
		//新增或编辑表单保存
		planTypeService.save(planType);//保存
		addMessage(redirectAttributes, "保存计划类别定义成功");
		return "redirect:"+Global.getAdminPath()+"/plantype/planType/?repage";
	}
	
	/**
	 * 删除计划类别定义
	 */
	@ResponseBody
	@RequiresPermissions("plantype:planType:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(PlanType planType, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		planTypeService.delete(planType);
		j.setMsg("删除计划类别定义成功");
		return j;
	}
	
	/**
	 * 批量删除计划类别定义
	 */
	@ResponseBody
	@RequiresPermissions("plantype:planType:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			planTypeService.delete(planTypeService.get(id));
		}
		j.setMsg("删除计划类别定义成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("plantype:planType:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(PlanType planType, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "计划类别定义"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<PlanType> page = planTypeService.findPage(new Page<PlanType>(request, response, -1), planType);
    		new ExportExcel("计划类别定义", PlanType.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出计划类别定义记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("plantype:planType:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<PlanType> list = ei.getDataList(PlanType.class);
			for (PlanType planType : list){
				try{
					planTypeService.save(planType);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条计划类别定义记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条计划类别定义记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入计划类别定义失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/plantype/planType/?repage";
    }
	
	/**
	 * 下载导入计划类别定义数据模板
	 */
	@RequiresPermissions("plantype:planType:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "计划类别定义数据导入模板.xlsx";
    		List<PlanType> list = Lists.newArrayList(); 
    		new ExportExcel("计划类别定义数据", PlanType.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/plantype/planType/?repage";
    }

}