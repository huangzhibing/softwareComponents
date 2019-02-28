/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.workshopmanage.mrpplanquery.web;

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
import com.hqu.modules.workshopmanage.mrpplanquery.entity.MrpPlanQuery;
import com.hqu.modules.workshopmanage.mrpplanquery.service.MrpPlanQueryService;

/**
 * 物料需求计划查询Controller
 * @author yxb
 * @version 2018-09-04
 */
@Controller
@RequestMapping(value = "${adminPath}/mrpplanquery/mrpPlanQuery")
public class MrpPlanQueryController extends BaseController {

	@Autowired
	private MrpPlanQueryService mrpPlanQueryService;
	
	@ModelAttribute
	public MrpPlanQuery get(@RequestParam(required=false) String id) {
		MrpPlanQuery entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = mrpPlanQueryService.get(id);
		}
		if (entity == null){
			entity = new MrpPlanQuery();
		}
		return entity;
	}
	
	/**
	 * 物料需求计划列表页面
	 */
	@RequiresPermissions("mrpplanquery:mrpPlanQuery:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "workshopmanage/mrpplanquery/mrpPlanQueryList";
	}
	
		/**
	 * 物料需求计划列表数据
	 */
	@ResponseBody
	@RequiresPermissions("mrpplanquery:mrpPlanQuery:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(MrpPlanQuery mrpPlanQuery, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<MrpPlanQuery> page = mrpPlanQueryService.findPage(new Page<MrpPlanQuery>(request, response), mrpPlanQuery); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑物料需求计划表单页面
	 */
	@RequiresPermissions(value={"mrpplanquery:mrpPlanQuery:view","mrpplanquery:mrpPlanQuery:add","mrpplanquery:mrpPlanQuery:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(MrpPlanQuery mrpPlanQuery, Model model) {
		model.addAttribute("mrpPlanQuery", mrpPlanQuery);
		if(StringUtils.isBlank(mrpPlanQuery.getId())){//如果ID是空为添加
			model.addAttribute("isAdd", true);
		}
		return "workshopmanage/mrpplanquery/mrpPlanQueryForm";
	}

	/**
	 * 保存物料需求计划
	 */
	@RequiresPermissions(value={"mrpplanquery:mrpPlanQuery:add","mrpplanquery:mrpPlanQuery:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(MrpPlanQuery mrpPlanQuery, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, mrpPlanQuery)){
			return form(mrpPlanQuery, model);
		}
		//新增或编辑表单保存
		mrpPlanQueryService.save(mrpPlanQuery);//保存
		addMessage(redirectAttributes, "保存物料需求计划成功");
		return "redirect:"+Global.getAdminPath()+"/mrpplanquery/mrpPlanQuery/?repage";
	}
	
	/**
	 * 删除物料需求计划
	 */
	@ResponseBody
	@RequiresPermissions("mrpplanquery:mrpPlanQuery:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(MrpPlanQuery mrpPlanQuery, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		mrpPlanQueryService.delete(mrpPlanQuery);
		j.setMsg("删除物料需求计划成功");
		return j;
	}
	
	/**
	 * 批量删除物料需求计划
	 */
	@ResponseBody
	@RequiresPermissions("mrpplanquery:mrpPlanQuery:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			mrpPlanQueryService.delete(mrpPlanQueryService.get(id));
		}
		j.setMsg("删除物料需求计划成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("mrpplanquery:mrpPlanQuery:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(MrpPlanQuery mrpPlanQuery, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "物料需求计划"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<MrpPlanQuery> page = mrpPlanQueryService.findPage(new Page<MrpPlanQuery>(request, response, -1), mrpPlanQuery);
    		new ExportExcel("物料需求计划", MrpPlanQuery.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出物料需求计划记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("mrpplanquery:mrpPlanQuery:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<MrpPlanQuery> list = ei.getDataList(MrpPlanQuery.class);
			for (MrpPlanQuery mrpPlanQuery : list){
				try{
					mrpPlanQueryService.save(mrpPlanQuery);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条物料需求计划记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条物料需求计划记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入物料需求计划失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/mrpplanquery/mrpPlanQuery/?repage";
    }
	
	/**
	 * 下载导入物料需求计划数据模板
	 */
	@RequiresPermissions("mrpplanquery:mrpPlanQuery:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "物料需求计划数据导入模板.xlsx";
    		List<MrpPlanQuery> list = Lists.newArrayList(); 
    		new ExportExcel("物料需求计划数据", MrpPlanQuery.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/mrpplanquery/mrpPlanQuery/?repage";
    }

}