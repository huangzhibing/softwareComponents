/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.workshopmanage.sfcmaterialordersingle.web;

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
import com.hqu.modules.workshopmanage.sfcmaterialordersingle.entity.SfcMaterialordermainsingle;
import com.hqu.modules.workshopmanage.sfcmaterialordersingle.service.SfcMaterialordermainsingleService;

/**
 * 原单行领料单Controller
 * @author xhc
 * @version 2018-12-09
 */
@Controller
@RequestMapping(value = "${adminPath}/sfcmaterialordersingle/sfcMaterialordermainsingle")
public class SfcMaterialordermainsingleController extends BaseController {

	@Autowired
	private SfcMaterialordermainsingleService sfcMaterialordermainsingleService;
	
	@ModelAttribute
	public SfcMaterialordermainsingle get(@RequestParam(required=false) String id) {
		SfcMaterialordermainsingle entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = sfcMaterialordermainsingleService.get(id);
		}
		if (entity == null){
			entity = new SfcMaterialordermainsingle();
		}
		return entity;
	}
	
	/**
	 * 原单行领料单列表页面
	 */
	@RequiresPermissions("sfcmaterialordersingle:sfcMaterialordermainsingle:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "workshopmanage/sfcmaterialordersingle/sfcMaterialordermainsingleList";
	}
	
		/**
	 * 原单行领料单列表数据
	 */
	@ResponseBody
	@RequiresPermissions("sfcmaterialordersingle:sfcMaterialordermainsingle:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(SfcMaterialordermainsingle sfcMaterialordermainsingle, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<SfcMaterialordermainsingle> page = sfcMaterialordermainsingleService.findPage(new Page<SfcMaterialordermainsingle>(request, response), sfcMaterialordermainsingle); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑原单行领料单表单页面
	 */
	@RequiresPermissions(value={"sfcmaterialordersingle:sfcMaterialordermainsingle:view","sfcmaterialordersingle:sfcMaterialordermainsingle:add","sfcmaterialordersingle:sfcMaterialordermainsingle:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(SfcMaterialordermainsingle sfcMaterialordermainsingle, Model model) {
		model.addAttribute("sfcMaterialordermainsingle", sfcMaterialordermainsingle);
		if(StringUtils.isBlank(sfcMaterialordermainsingle.getId())){//如果ID是空为添加
			model.addAttribute("isAdd", true);
		}
		return "workshopmanage/sfcmaterialordersingle/sfcMaterialordermainsingleForm";
	}

	/**
	 * 保存原单行领料单
	 */
	@RequiresPermissions(value={"sfcmaterialordersingle:sfcMaterialordermainsingle:add","sfcmaterialordersingle:sfcMaterialordermainsingle:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(SfcMaterialordermainsingle sfcMaterialordermainsingle, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, sfcMaterialordermainsingle)){
			return form(sfcMaterialordermainsingle, model);
		}
		//新增或编辑表单保存
		sfcMaterialordermainsingleService.save(sfcMaterialordermainsingle);//保存
		addMessage(redirectAttributes, "保存原单行领料单成功");
		return "redirect:"+Global.getAdminPath()+"/sfcmaterialordersingle/sfcMaterialordermainsingle/?repage";
	}
	
	/**
	 * 删除原单行领料单
	 */
	@ResponseBody
	@RequiresPermissions("sfcmaterialordersingle:sfcMaterialordermainsingle:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(SfcMaterialordermainsingle sfcMaterialordermainsingle, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		sfcMaterialordermainsingleService.delete(sfcMaterialordermainsingle);
		j.setMsg("删除原单行领料单成功");
		return j;
	}
	
	/**
	 * 批量删除原单行领料单
	 */
	@ResponseBody
	@RequiresPermissions("sfcmaterialordersingle:sfcMaterialordermainsingle:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			sfcMaterialordermainsingleService.delete(sfcMaterialordermainsingleService.get(id));
		}
		j.setMsg("删除原单行领料单成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("sfcmaterialordersingle:sfcMaterialordermainsingle:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(SfcMaterialordermainsingle sfcMaterialordermainsingle, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "原单行领料单"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<SfcMaterialordermainsingle> page = sfcMaterialordermainsingleService.findPage(new Page<SfcMaterialordermainsingle>(request, response, -1), sfcMaterialordermainsingle);
    		new ExportExcel("原单行领料单", SfcMaterialordermainsingle.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出原单行领料单记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("sfcmaterialordersingle:sfcMaterialordermainsingle:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<SfcMaterialordermainsingle> list = ei.getDataList(SfcMaterialordermainsingle.class);
			for (SfcMaterialordermainsingle sfcMaterialordermainsingle : list){
				try{
					sfcMaterialordermainsingleService.save(sfcMaterialordermainsingle);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条原单行领料单记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条原单行领料单记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入原单行领料单失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/sfcmaterialordersingle/sfcMaterialordermainsingle/?repage";
    }
	
	/**
	 * 下载导入原单行领料单数据模板
	 */
	@RequiresPermissions("sfcmaterialordersingle:sfcMaterialordermainsingle:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "原单行领料单数据导入模板.xlsx";
    		List<SfcMaterialordermainsingle> list = Lists.newArrayList(); 
    		new ExportExcel("原单行领料单数据", SfcMaterialordermainsingle.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/sfcmaterialordersingle/sfcMaterialordermainsingle/?repage";
    }

}