/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.purchasemanage.areacode.web;

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
import com.hqu.modules.purchasemanage.areacode.entity.AreaCode;
import com.hqu.modules.purchasemanage.areacode.service.AreaCodeService;

/**
 * 地区定义Controller
 * @author 石豪迈
 * @version 2018-04-25
 */
@Controller
@RequestMapping(value = "${adminPath}/areacode/areaCode")
public class AreaCodeController extends BaseController {

	@Autowired
	private AreaCodeService areaCodeService;
	
	@ModelAttribute
	public AreaCode get(@RequestParam(required=false) String id) {
		AreaCode entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = areaCodeService.get(id);
		}
		if (entity == null){
			entity = new AreaCode();
		}
		return entity;
	}
	
	/**
	 * 保存成功列表页面
	 */
	@RequiresPermissions("areacode:areaCode:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "purchasemanage/areacode/areaCodeList";
	}
	
		/**
	 * 保存成功列表数据
	 */
	@ResponseBody
	@RequiresPermissions("areacode:areaCode:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(AreaCode areaCode, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<AreaCode> page = areaCodeService.findPage(new Page<AreaCode>(request, response), areaCode); 
		return getBootstrapData(page);
	}

	/**
	 * 保存成功列表数据
	 */
	@ResponseBody
	@RequiresPermissions("areacode:areaCode:list")
	@RequestMapping(value = "myAreaCode")
	public Map<String, Object> myAreaCodedata(AreaCode areaCode, HttpServletRequest request, HttpServletResponse
			response, Model model) {
		Page<AreaCode> page = areaCodeService.findPage(new Page<AreaCode>(request, response), areaCode);
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑保存成功表单页面
	 */
	@RequiresPermissions(value={"areacode:areaCode:view","areacode:areaCode:add","areacode:areaCode:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(AreaCode areaCode, Model model) {
		model.addAttribute("areaCode", areaCode);
		if(StringUtils.isBlank(areaCode.getId())){//如果ID是空为添加
			model.addAttribute("isAdd", true);
			//设置areacode从0开始自增长并为2位流水压入表单中
			String areacode = areaCodeService.getMaxAreacode();
			if(areacode == null) {
				areacode = "00";
			}
			else {
				int maxnum = Integer.parseInt(areacode) + 1;
				areacode = String.format("%02d",maxnum);
			}

			areaCode.setAreacode(areacode);
		}
		return "purchasemanage/areacode/areaCodeForm";
	}

	/**
	 * 保存保存成功
	 */
	@RequiresPermissions(value={"areacode:areaCode:add","areacode:areaCode:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(AreaCode areaCode, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, areaCode)){
			return form(areaCode, model);
		}
		//新增或编辑表单保存
		areaCodeService.save(areaCode);//保存
		addMessage(redirectAttributes, "保存保存成功成功");
		return "redirect:"+Global.getAdminPath()+"/areacode/areaCode/?repage";
	}
	
	/**
	 * 删除保存成功
	 */
	@ResponseBody
	@RequiresPermissions("areacode:areaCode:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(AreaCode areaCode, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		areaCodeService.delete(areaCode);
		j.setMsg("删除保存成功成功");
		return j;
	}
	
	/**
	 * 批量删除保存成功
	 */
	@ResponseBody
	@RequiresPermissions("areacode:areaCode:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			areaCodeService.delete(areaCodeService.get(id));
		}
		j.setMsg("删除保存成功成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("areacode:areaCode:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(AreaCode areaCode, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "保存成功"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<AreaCode> page = areaCodeService.findPage(new Page<AreaCode>(request, response, -1), areaCode);
    		new ExportExcel("保存成功", AreaCode.class).setDataList(page.getList()).write(response, fileName).dispose();
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
	@RequiresPermissions("areacode:areaCode:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<AreaCode> list = ei.getDataList(AreaCode.class);
			for (AreaCode areaCode : list){
				try{
					areaCodeService.save(areaCode);
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
		return "redirect:"+Global.getAdminPath()+"/areacode/areaCode/?repage";
    }
	
	/**
	 * 下载导入保存成功数据模板
	 */
	@RequiresPermissions("areacode:areaCode:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "保存成功数据导入模板.xlsx";
    		List<AreaCode> list = Lists.newArrayList(); 
    		new ExportExcel("保存成功数据", AreaCode.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/areacode/areaCode/?repage";
    }

}