/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.qualitymanage.purinvcheckmain.web;

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
import com.hqu.modules.qualitymanage.purinvcheckmain.entity.InvCheckRelations;
import com.hqu.modules.qualitymanage.purinvcheckmain.service.InvCheckRelationsService;

/**
 * 关系描述Controller
 * @author ltq
 * @version 2018-05-23
 */
@Controller
@RequestMapping(value = "${adminPath}/purinvcheckmain/invCheckRelations")
public class InvCheckRelationsController extends BaseController {

	@Autowired
	private InvCheckRelationsService invCheckRelationsService;
	
	@ModelAttribute
	public InvCheckRelations get(@RequestParam(required=false) String id) {
		InvCheckRelations entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = invCheckRelationsService.get(id);
		}
		if (entity == null){
			entity = new InvCheckRelations();
		}
		return entity;
	}
	
	/**
	 * 关系描述列表页面
	 */
	@RequiresPermissions("purinvcheckmain:invCheckRelations:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "qualitymanage/purinvcheckmain/invCheckRelationsList";
	}
	
		/**
	 * 关系描述列表数据
	 */
	@ResponseBody
	@RequiresPermissions("purinvcheckmain:invCheckRelations:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(InvCheckRelations invCheckRelations, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<InvCheckRelations> page = invCheckRelationsService.findPage(new Page<InvCheckRelations>(request, response), invCheckRelations); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑关系描述表单页面
	 */
	@RequiresPermissions(value={"purinvcheckmain:invCheckRelations:view","purinvcheckmain:invCheckRelations:add","purinvcheckmain:invCheckRelations:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(InvCheckRelations invCheckRelations, Model model) {
		model.addAttribute("invCheckRelations", invCheckRelations);
		if(StringUtils.isBlank(invCheckRelations.getId())){//如果ID是空为添加
			model.addAttribute("isAdd", true);
		}
		return "qualitymanage/purinvcheckmain/invCheckRelationsForm";
	}

	/**
	 * 保存关系描述
	 */
	@RequiresPermissions(value={"purinvcheckmain:invCheckRelations:add","purinvcheckmain:invCheckRelations:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(InvCheckRelations invCheckRelations, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, invCheckRelations)){
			return form(invCheckRelations, model);
		}
		//新增或编辑表单保存
		invCheckRelationsService.save(invCheckRelations);//保存
		addMessage(redirectAttributes, "保存关系描述成功");
		return "redirect:"+Global.getAdminPath()+"/purinvcheckmain/invCheckRelations/?repage";
	}
	
	/**
	 * 删除关系描述
	 */
	@ResponseBody
	@RequiresPermissions("purinvcheckmain:invCheckRelations:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(InvCheckRelations invCheckRelations, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		invCheckRelationsService.delete(invCheckRelations);
		j.setMsg("删除关系描述成功");
		return j;
	}
	
	/**
	 * 批量删除关系描述
	 */
	@ResponseBody
	@RequiresPermissions("purinvcheckmain:invCheckRelations:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			invCheckRelationsService.delete(invCheckRelationsService.get(id));
		}
		j.setMsg("删除关系描述成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("purinvcheckmain:invCheckRelations:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(InvCheckRelations invCheckRelations, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "关系描述"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<InvCheckRelations> page = invCheckRelationsService.findPage(new Page<InvCheckRelations>(request, response, -1), invCheckRelations);
    		new ExportExcel("关系描述", InvCheckRelations.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出关系描述记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("purinvcheckmain:invCheckRelations:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<InvCheckRelations> list = ei.getDataList(InvCheckRelations.class);
			for (InvCheckRelations invCheckRelations : list){
				try{
					invCheckRelationsService.save(invCheckRelations);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条关系描述记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条关系描述记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入关系描述失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/purinvcheckmain/invCheckRelations/?repage";
    }
	
	/**
	 * 下载导入关系描述数据模板
	 */
	@RequiresPermissions("purinvcheckmain:invCheckRelations:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "关系描述数据导入模板.xlsx";
    		List<InvCheckRelations> list = Lists.newArrayList(); 
    		new ExportExcel("关系描述数据", InvCheckRelations.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/purinvcheckmain/invCheckRelations/?repage";
    }

}