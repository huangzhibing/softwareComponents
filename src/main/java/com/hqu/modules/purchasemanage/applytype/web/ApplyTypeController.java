/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.purchasemanage.applytype.web;

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
import com.hqu.modules.purchasemanage.applytype.entity.ApplyType;
import com.hqu.modules.purchasemanage.applytype.service.ApplyTypeService;

/**
 * ApplyTypeController
 * @author 方翠虹
 * @version 2018-04-25
 */
@Controller
@RequestMapping(value = "${adminPath}/applytype/applyType")
public class ApplyTypeController extends BaseController {

	@Autowired
	private ApplyTypeService applyTypeService;
	
	@ModelAttribute
	public ApplyType get(@RequestParam(required=false) String id) {
		ApplyType entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = applyTypeService.get(id);
		}
		if (entity == null){
			entity = new ApplyType();
		}
		return entity;
	}
	
	/**
	 * 需求类别定义列表页面
	 */
	@RequiresPermissions("applytype:applyType:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "purchasemanage/applytype/applyTypeList";
	}
	
		/**
	 * 需求类别定义列表数据
	 */
	@ResponseBody
	@RequiresPermissions("applytype:applyType:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(ApplyType applyType, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<ApplyType> page = applyTypeService.findPage(new Page<ApplyType>(request, response), applyType); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑需求类别定义表单页面
	 */
	@RequiresPermissions(value={"applytype:applyType:view","applytype:applyType:add","applytype:applyType:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(ApplyType applyType, Model model) {
		model.addAttribute("applyType", applyType);
		if(StringUtils.isBlank(applyType.getId())){//如果ID是空为添加
			model.addAttribute("isAdd", true);
			//设置plantypeid从0开始自增长并为2位流水压入表单中
			String applytypeid = applyTypeService.getMaxApplytypeid();
			if(applytypeid== null) {
				applytypeid = "00";
			}
			else {
				int maxnum = Integer.parseInt(applytypeid) + 1;
				applytypeid = String.format("%02d",maxnum);
			}

			applyType.setApplytypeid(applytypeid);
		}
		return "purchasemanage/applytype/applyTypeForm";
	}

	/**
	 * 保存需求类别定义
	 */
	@RequiresPermissions(value={"applytype:applyType:add","applytype:applyType:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(ApplyType applyType, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, applyType)){
			return form(applyType, model);
		}
		//新增或编辑表单保存
		applyTypeService.save(applyType);//保存
		addMessage(redirectAttributes, "保存需求类别定义成功");
		return "redirect:"+Global.getAdminPath()+"/applytype/applyType/?repage";
	}
	
	/**
	 * 删除需求类别定义
	 */
	@ResponseBody
	@RequiresPermissions("applytype:applyType:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(ApplyType applyType, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		applyTypeService.delete(applyType);
		j.setMsg("删除需求类别定义成功");
		return j;
	}
	
	/**
	 * 批量删除需求类别定义
	 */
	@ResponseBody
	@RequiresPermissions("applytype:applyType:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			applyTypeService.delete(applyTypeService.get(id));
		}
		j.setMsg("删除需求类别定义成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("applytype:applyType:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(ApplyType applyType, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "需求类别定义"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<ApplyType> page = applyTypeService.findPage(new Page<ApplyType>(request, response, -1), applyType);
    		new ExportExcel("需求类别定义", ApplyType.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出需求类别定义记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("applytype:applyType:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<ApplyType> list = ei.getDataList(ApplyType.class);
			for (ApplyType applyType : list){
				try{
					applyTypeService.save(applyType);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条需求类别定义记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条需求类别定义记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入需求类别定义失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/applytype/applyType/?repage";
    }
	
	/**
	 * 下载导入需求类别定义数据模板
	 */
	@RequiresPermissions("applytype:applyType:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "需求类别定义数据导入模板.xlsx";
    		List<ApplyType> list = Lists.newArrayList(); 
    		new ExportExcel("需求类别定义数据", ApplyType.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/applytype/applyType/?repage";
    }

}