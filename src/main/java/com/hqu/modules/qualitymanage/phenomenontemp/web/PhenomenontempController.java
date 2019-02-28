/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.qualitymanage.phenomenontemp.web;

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
import com.hqu.modules.qualitymanage.phenomenontemp.entity.Phenomenontemp;
import com.hqu.modules.qualitymanage.phenomenontemp.service.PhenomenontempService;

/**
 * 来料不良主要现象Controller
 * @author syc
 * @version 2018-08-24
 */
@Controller
@RequestMapping(value = "${adminPath}/phenomenontemp/phenomenontemp")
public class PhenomenontempController extends BaseController {

	@Autowired
	private PhenomenontempService phenomenontempService;
	
	@ModelAttribute
	public Phenomenontemp get(@RequestParam(required=false) String id) {
		Phenomenontemp entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = phenomenontempService.get(id);
		}
		if (entity == null){
			entity = new Phenomenontemp();
		}
		return entity;
	}
	
	/**
	 * 来料不良主要现象列表页面
	 */
	@RequiresPermissions("phenomenontemp:phenomenontemp:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "qualitymanage/phenomenontemp/phenomenontempList";
	}
	
		/**
	 * 来料不良主要现象列表数据
	 */
	@ResponseBody
	@RequiresPermissions("phenomenontemp:phenomenontemp:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(Phenomenontemp phenomenontemp, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Phenomenontemp> page = phenomenontempService.findPage(new Page<Phenomenontemp>(request, response), phenomenontemp); 
		return getBootstrapData(page);
	}
	
	/**
	 * 来料不良主要现象列表数据打印
	 */

	@RequiresPermissions("phenomenontemp:phenomenontemp:list")
	@RequestMapping(value = "print")
	public String print(Phenomenontemp phenomenontemp, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Phenomenontemp> page = phenomenontempService.findPage(new Page<Phenomenontemp>(request, response), phenomenontemp); 
		model.addAttribute("contractDetailWarningList",page.getList());
		return "qualitymanage/phenomenontemp/phenomenontempPrintForm";
	}
	
	

	/**
	 * 查看，增加，编辑来料不良主要现象表单页面
	 */
	@RequiresPermissions(value={"phenomenontemp:phenomenontemp:view","phenomenontemp:phenomenontemp:add","phenomenontemp:phenomenontemp:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(Phenomenontemp phenomenontemp, Model model) {
		model.addAttribute("phenomenontemp", phenomenontemp);
		if(StringUtils.isBlank(phenomenontemp.getId())){//如果ID是空为添加
			model.addAttribute("isAdd", true);
		}
		return "qualitymanage/phenomenontemp/phenomenontempForm";
	}

	/**
	 * 保存来料不良主要现象
	 */
	@RequiresPermissions(value={"phenomenontemp:phenomenontemp:add","phenomenontemp:phenomenontemp:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(Phenomenontemp phenomenontemp, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, phenomenontemp)){
			return form(phenomenontemp, model);
		}
		//新增或编辑表单保存
		phenomenontempService.save(phenomenontemp);//保存
		addMessage(redirectAttributes, "保存来料不良主要现象成功");
		return "redirect:"+Global.getAdminPath()+"/phenomenontemp/phenomenontemp/?repage";
	}
	
	/**
	 * 删除来料不良主要现象
	 */
	@ResponseBody
	@RequiresPermissions("phenomenontemp:phenomenontemp:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(Phenomenontemp phenomenontemp, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		phenomenontempService.delete(phenomenontemp);
		j.setMsg("删除来料不良主要现象成功");
		return j;
	}
	
	/**
	 * 批量删除来料不良主要现象
	 */
	@ResponseBody
	@RequiresPermissions("phenomenontemp:phenomenontemp:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			phenomenontempService.delete(phenomenontempService.get(id));
		}
		j.setMsg("删除来料不良主要现象成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("phenomenontemp:phenomenontemp:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(Phenomenontemp phenomenontemp, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "来料不良主要现象"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<Phenomenontemp> page = phenomenontempService.findPage(new Page<Phenomenontemp>(request, response, -1), phenomenontemp);
    		new ExportExcel("来料不良主要现象", Phenomenontemp.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出来料不良主要现象记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("phenomenontemp:phenomenontemp:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<Phenomenontemp> list = ei.getDataList(Phenomenontemp.class);
			for (Phenomenontemp phenomenontemp : list){
				try{
					phenomenontempService.save(phenomenontemp);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条来料不良主要现象记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条来料不良主要现象记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入来料不良主要现象失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/phenomenontemp/phenomenontemp/?repage";
    }
	
	/**
	 * 下载导入来料不良主要现象数据模板
	 */
	@RequiresPermissions("phenomenontemp:phenomenontemp:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "来料不良主要现象数据导入模板.xlsx";
    		List<Phenomenontemp> list = Lists.newArrayList(); 
    		new ExportExcel("来料不良主要现象数据", Phenomenontemp.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/phenomenontemp/phenomenontemp/?repage";
    }

}