/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.purchasemanage.adtbilltype.web;

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
import com.hqu.modules.purchasemanage.adtbilltype.entity.AdtBillType;
import com.hqu.modules.purchasemanage.adtbilltype.service.AdtBillTypeService;

/**
 * 单据类型表Controller
 * @author ckw
 * @version 2018-05-08
 */
@Controller
@RequestMapping(value = "${adminPath}/adtbilltype/adtBillType")
public class AdtBillTypeController extends BaseController {

	@Autowired
	private AdtBillTypeService adtBillTypeService;
	
	@ModelAttribute
	public AdtBillType get(@RequestParam(required=false) String id) {
		AdtBillType entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = adtBillTypeService.get(id);
		}
		if (entity == null){
			entity = new AdtBillType();
		}
		return entity;
	}
	
	/**
	 * 保存成功列表页面
	 */
	@RequiresPermissions("adtbilltype:adtBillType:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "purchasemanage/adtbilltype/adtBillTypeList";
	}
	
		/**
	 * 保存成功列表数据
	 */
	@ResponseBody
	@RequiresPermissions("adtbilltype:adtBillType:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(AdtBillType adtBillType, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<AdtBillType> page = adtBillTypeService.findPage(new Page<AdtBillType>(request, response), adtBillType); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑保存成功表单页面
	 */
	@RequiresPermissions(value={"adtbilltype:adtBillType:view","adtbilltype:adtBillType:add","adtbilltype:adtBillType:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(AdtBillType adtBillType, Model model) {
		model.addAttribute("adtBillType", adtBillType);
		if(StringUtils.isBlank(adtBillType.getId())){//如果ID是空为添加
			model.addAttribute("isAdd", true);
		}
		return "purchasemanage/adtbilltype/adtBillTypeForm";
	}

	/**
	 * 保存保存成功
	 */
	@RequiresPermissions(value={"adtbilltype:adtBillType:add","adtbilltype:adtBillType:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(AdtBillType adtBillType, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, adtBillType)){
			return form(adtBillType, model);
		}
		//新增或编辑表单保存
		adtBillTypeService.save(adtBillType);//保存
		addMessage(redirectAttributes, "保存保存成功成功");
		return "redirect:"+Global.getAdminPath()+"/adtbilltype/adtBillType/?repage";
	}
	
	/**
	 * 删除保存成功
	 */
	@ResponseBody
	@RequiresPermissions("adtbilltype:adtBillType:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(AdtBillType adtBillType, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		adtBillTypeService.delete(adtBillType);
		j.setMsg("删除保存成功成功");
		return j;
	}
	
	/**
	 * 批量删除保存成功
	 */
	@ResponseBody
	@RequiresPermissions("adtbilltype:adtBillType:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			adtBillTypeService.delete(adtBillTypeService.get(id));
		}
		j.setMsg("删除保存成功成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("adtbilltype:adtBillType:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(AdtBillType adtBillType, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "保存成功"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<AdtBillType> page = adtBillTypeService.findPage(new Page<AdtBillType>(request, response, -1), adtBillType);
    		new ExportExcel("保存成功", AdtBillType.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出保存成功记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("adtbilltype:adtBillType:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<AdtBillType> list = ei.getDataList(AdtBillType.class);
			for (AdtBillType adtBillType : list){
				try{
					adtBillTypeService.save(adtBillType);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条保存成功记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条保存成功记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入保存成功失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/adtbilltype/adtBillType/?repage";
    }
	
	/**
	 * 下载导入保存成功数据模板
	 */
	@RequiresPermissions("adtbilltype:adtBillType:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "保存成功数据导入模板.xlsx";
    		List<AdtBillType> list = Lists.newArrayList(); 
    		new ExportExcel("保存成功数据", AdtBillType.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/adtbilltype/adtBillType/?repage";
    }

}