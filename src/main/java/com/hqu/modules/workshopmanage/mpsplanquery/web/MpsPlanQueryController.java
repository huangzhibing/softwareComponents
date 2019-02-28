/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.workshopmanage.mpsplanquery.web;

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
import com.hqu.modules.workshopmanage.mpsplanquery.entity.MpsPlanQuery;
import com.hqu.modules.workshopmanage.mpsplanquery.service.MpsPlanQueryService;

/**
 * 主生产计划查询Controller
 * @author yxb
 * @version 2018-09-04
 */
@Controller
@RequestMapping(value = "${adminPath}/mpsplanquery/mpsPlanQuery")
public class MpsPlanQueryController extends BaseController {

	@Autowired
	private MpsPlanQueryService mpsPlanQueryService;
	
	@ModelAttribute
	public MpsPlanQuery get(@RequestParam(required=false) String id) {
		MpsPlanQuery entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = mpsPlanQueryService.get(id);
		}
		if (entity == null){
			entity = new MpsPlanQuery();
		}
		return entity;
	}
	
	/**
	 * 主生产计划列表页面
	 */
	@RequiresPermissions("mpsplanquery:mpsPlanQuery:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "workshopmanage/mpsplanquery/mpsPlanQueryList";
	}
	
		/**
	 * 主生产计划列表数据
	 */
	@ResponseBody
	@RequiresPermissions("mpsplanquery:mpsPlanQuery:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(MpsPlanQuery mpsPlanQuery, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<MpsPlanQuery> page = mpsPlanQueryService.findPage(new Page<MpsPlanQuery>(request, response), mpsPlanQuery); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑主生产计划表单页面
	 */
	@RequiresPermissions(value={"mpsplanquery:mpsPlanQuery:view","mpsplanquery:mpsPlanQuery:add","mpsplanquery:mpsPlanQuery:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(MpsPlanQuery mpsPlanQuery, Model model) {
		model.addAttribute("mpsPlanQuery", mpsPlanQuery);
		if(StringUtils.isBlank(mpsPlanQuery.getId())){//如果ID是空为添加
			model.addAttribute("isAdd", true);
		}
		return "workshopmanage/mpsplanquery/mpsPlanQueryForm";
	}

	/**
	 * 保存主生产计划
	 */
	@RequiresPermissions(value={"mpsplanquery:mpsPlanQuery:add","mpsplanquery:mpsPlanQuery:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(MpsPlanQuery mpsPlanQuery, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, mpsPlanQuery)){
			return form(mpsPlanQuery, model);
		}
		//新增或编辑表单保存
		mpsPlanQueryService.save(mpsPlanQuery);//保存
		addMessage(redirectAttributes, "保存主生产计划成功");
		return "redirect:"+Global.getAdminPath()+"/mpsplanquery/mpsPlanQuery/?repage";
	}
	
	/**
	 * 删除主生产计划
	 */
	@ResponseBody
	@RequiresPermissions("mpsplanquery:mpsPlanQuery:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(MpsPlanQuery mpsPlanQuery, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		mpsPlanQueryService.delete(mpsPlanQuery);
		j.setMsg("删除主生产计划成功");
		return j;
	}
	
	/**
	 * 批量删除主生产计划
	 */
	@ResponseBody
	@RequiresPermissions("mpsplanquery:mpsPlanQuery:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			mpsPlanQueryService.delete(mpsPlanQueryService.get(id));
		}
		j.setMsg("删除主生产计划成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("mpsplanquery:mpsPlanQuery:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(MpsPlanQuery mpsPlanQuery, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "主生产计划"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<MpsPlanQuery> page = mpsPlanQueryService.findPage(new Page<MpsPlanQuery>(request, response, -1), mpsPlanQuery);
    		new ExportExcel("主生产计划", MpsPlanQuery.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出主生产计划记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("mpsplanquery:mpsPlanQuery:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<MpsPlanQuery> list = ei.getDataList(MpsPlanQuery.class);
			for (MpsPlanQuery mpsPlanQuery : list){
				try{
					mpsPlanQueryService.save(mpsPlanQuery);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条主生产计划记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条主生产计划记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入主生产计划失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/mpsplanquery/mpsPlanQuery/?repage";
    }
	
	/**
	 * 下载导入主生产计划数据模板
	 */
	@RequiresPermissions("mpsplanquery:mpsPlanQuery:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "主生产计划数据导入模板.xlsx";
    		List<MpsPlanQuery> list = Lists.newArrayList(); 
    		new ExportExcel("主生产计划数据", MpsPlanQuery.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/mpsplanquery/mpsPlanQuery/?repage";
    }

}