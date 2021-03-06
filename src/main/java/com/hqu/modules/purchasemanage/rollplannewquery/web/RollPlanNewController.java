/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.purchasemanage.rollplannewquery.web;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
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
import com.hqu.modules.purchasemanage.rollplannewquery.entity.RollPlanNew;
import com.hqu.modules.purchasemanage.rollplannewquery.service.RollPlanNewService;

/**
 * 滚动计划查询Controller
 * @author yangxianbang
 * @version 2018-05-08
 */
@Controller
@RequestMapping(value = "${adminPath}/rollplannewquery/rollPlanNew")
public class RollPlanNewController extends BaseController {

	@Autowired
	private RollPlanNewService rollPlanNewService;
	
	@ModelAttribute
	public RollPlanNew get(@RequestParam(required=false) String id) {
		RollPlanNew entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = rollPlanNewService.get(id);
		}
		if (entity == null){
			entity = new RollPlanNew();
		}
		return entity;
	}
	
	/**
	 * 滚动计划列表页面
	 */
	@RequiresPermissions("rollplannewquery:rollPlanNew:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "purchasemanage/rollplannewquery/rollPlanNewList";
	}
	
		/**
	 * 滚动计划列表数据
	 */
	@ResponseBody
	@RequiresPermissions("rollplannewquery:rollPlanNew:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(RollPlanNew rollPlanNew, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<RollPlanNew> page = rollPlanNewService.findPage(new Page<RollPlanNew>(request, response), rollPlanNew); 
		return getBootstrapData(page);
	}

	/**
	 * 滚动计划列表数据
	 */
	@ResponseBody
	@RequiresPermissions("rollplannewquery:rollPlanNew:list")
	@RequestMapping(value = "dataForPlan")
	public Map<String, Object> dataForPlan(RollPlanNew rollPlanNew, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<RollPlanNew> page = rollPlanNewService.findDataForPlanPage(new Page<RollPlanNew>(request, response), rollPlanNew);
		return getBootstrapData(page);
	}



	/**
	 * 查看，增加，编辑滚动计划表单页面
	 */
	@RequiresPermissions(value={"rollplannewquery:rollPlanNew:view","rollplannewquery:rollPlanNew:add","rollplannewquery:rollPlanNew:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(RollPlanNew rollPlanNew, Model model) {
		model.addAttribute("rollPlanNew", rollPlanNew);
		if(StringUtils.isBlank(rollPlanNew.getId())){//如果ID是空为添加
			model.addAttribute("isAdd", true);
		}
		return "purchasemanage/rollplannewquery/rollPlanNewForm";
	}

	/**
	 * 保存滚动计划
	 */
	@RequiresPermissions(value={"rollplannewquery:rollPlanNew:add","rollplannewquery:rollPlanNew:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(RollPlanNew rollPlanNew, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, rollPlanNew)){
			return form(rollPlanNew, model);
		}
		//新增或编辑表单保存
		rollPlanNewService.save(rollPlanNew);//保存
		addMessage(redirectAttributes, "保存滚动计划成功");
		return "redirect:"+Global.getAdminPath()+"/rollplannewquery/rollPlanNew/?repage";
	}
	
	/**
	 * 删除滚动计划
	 */
	@ResponseBody
	@RequiresPermissions("rollplannewquery:rollPlanNew:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(RollPlanNew rollPlanNew, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		rollPlanNewService.delete(rollPlanNew);
		j.setMsg("删除滚动计划成功");
		return j;
	}
	
	/**
	 * 批量删除滚动计划
	 */
	@ResponseBody
	@RequiresPermissions("rollplannewquery:rollPlanNew:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			rollPlanNewService.delete(rollPlanNewService.get(id));
		}
		j.setMsg("删除滚动计划成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("rollplannewquery:rollPlanNew:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(RollPlanNew rollPlanNew, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "滚动计划"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<RollPlanNew> page = rollPlanNewService.findPage(new Page<RollPlanNew>(request, response, -1), rollPlanNew);
    		new ExportExcel("滚动计划", RollPlanNew.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出滚动计划记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("rollplannewquery:rollPlanNew:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<RollPlanNew> list = ei.getDataList(RollPlanNew.class);
			for (RollPlanNew rollPlanNew : list){
				try{
					rollPlanNewService.save(rollPlanNew);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条滚动计划记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条滚动计划记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入滚动计划失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/rollplannewquery/rollPlanNew/?repage";
    }
	
	/**
	 * 下载导入滚动计划数据模板
	 */
	@RequiresPermissions("rollplannewquery:rollPlanNew:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "滚动计划数据导入模板.xlsx";
    		List<RollPlanNew> list = Lists.newArrayList(); 
    		new ExportExcel("滚动计划数据", RollPlanNew.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/rollplannewquery/rollPlanNew/?repage";
    }

}