/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.inventorymanage.billdetailcodes.web;

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
import com.hqu.modules.inventorymanage.billdetailcodes.entity.BillDetailCodes;
import com.hqu.modules.inventorymanage.billdetailcodes.service.BillDetailCodesService;

/**
 * 二维码扫描表Controller
 * @author huang.youcai
 * @version 2018-08-06
 */
@Controller
@RequestMapping(value = "${adminPath}/billdetailcodes/billDetailCodes")
public class BillDetailCodesController extends BaseController {

	@Autowired
	private BillDetailCodesService billDetailCodesService;
	
	@ModelAttribute
	public BillDetailCodes get(@RequestParam(required=false) String id) {
		BillDetailCodes entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = billDetailCodesService.get(id);
		}
		if (entity == null){
			entity = new BillDetailCodes();
		}
		return entity;
	}
	
	/**
	 * 二维码列表页面
	 */
	@RequiresPermissions("billdetailcodes:billDetailCodes:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "inventorymanage/billdetailcodes/billDetailCodesList";
	}
	
		/**
	 * 二维码列表数据
	 */
	@ResponseBody
	@RequiresPermissions("billdetailcodes:billDetailCodes:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(BillDetailCodes billDetailCodes, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<BillDetailCodes> page = billDetailCodesService.findPage(new Page<BillDetailCodes>(request, response), billDetailCodes); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑二维码表单页面
	 */
	@RequiresPermissions(value={"billdetailcodes:billDetailCodes:view","billdetailcodes:billDetailCodes:add","billdetailcodes:billDetailCodes:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(BillDetailCodes billDetailCodes, Model model) {
		model.addAttribute("billDetailCodes", billDetailCodes);
		if(StringUtils.isBlank(billDetailCodes.getId())){//如果ID是空为添加
			model.addAttribute("isAdd", true);
		}
		return "inventorymanage/billdetailcodes/billDetailCodesForm";
	}

	/**
	 * 保存二维码
	 */
	@RequiresPermissions(value={"billdetailcodes:billDetailCodes:add","billdetailcodes:billDetailCodes:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(BillDetailCodes billDetailCodes, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, billDetailCodes)){
			return form(billDetailCodes, model);
		}
		//新增或编辑表单保存
		billDetailCodesService.save(billDetailCodes);//保存
		addMessage(redirectAttributes, "保存二维码成功");
		return "redirect:"+Global.getAdminPath()+"/billdetailcodes/billDetailCodes/?repage";
	}
	
	/**
	 * 删除二维码
	 */
	@ResponseBody
	@RequiresPermissions("billdetailcodes:billDetailCodes:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(BillDetailCodes billDetailCodes, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		billDetailCodesService.delete(billDetailCodes);
		j.setMsg("删除二维码成功");
		return j;
	}
	
	/**
	 * 批量删除二维码
	 */
	@ResponseBody
	@RequiresPermissions("billdetailcodes:billDetailCodes:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			billDetailCodesService.delete(billDetailCodesService.get(id));
		}
		j.setMsg("删除二维码成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("billdetailcodes:billDetailCodes:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(BillDetailCodes billDetailCodes, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "二维码"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<BillDetailCodes> page = billDetailCodesService.findPage(new Page<BillDetailCodes>(request, response, -1), billDetailCodes);
    		new ExportExcel("二维码", BillDetailCodes.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出二维码记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("billdetailcodes:billDetailCodes:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<BillDetailCodes> list = ei.getDataList(BillDetailCodes.class);
			for (BillDetailCodes billDetailCodes : list){
				try{
					billDetailCodesService.save(billDetailCodes);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条二维码记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条二维码记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入二维码失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/billdetailcodes/billDetailCodes/?repage";
    }
	
	/**
	 * 下载导入二维码数据模板
	 */
	@RequiresPermissions("billdetailcodes:billDetailCodes:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "二维码数据导入模板.xlsx";
    		List<BillDetailCodes> list = Lists.newArrayList(); 
    		new ExportExcel("二维码数据", BillDetailCodes.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/billdetailcodes/billDetailCodes/?repage";
    }

}