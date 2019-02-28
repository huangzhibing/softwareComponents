/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.purchasemanage.paymode.web;

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
import com.hqu.modules.purchasemanage.paymode.entity.PayMode;
import com.hqu.modules.purchasemanage.paymode.service.PayModeService;

/**
 * 付款方式定义Controller
 * @author 石豪迈
 * @version 2018-04-14
 */
@Controller
@RequestMapping(value = "${adminPath}/paymode/payMode")
public class PayModeController extends BaseController {

	@Autowired
	private PayModeService payModeService;
	
	@ModelAttribute
	public PayMode get(@RequestParam(required=false) String id) {
		PayMode entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = payModeService.get(id);
		}
		if (entity == null){
			entity = new PayMode();
		}
		return entity;
	}
	
	/**
	 * 保存成功列表页面
	 */
	@RequiresPermissions("paymode:payMode:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "purchasemanage/paymode/payModeList";
	}
	
		/**
	 * 保存成功列表数据
	 */
	@ResponseBody
	@RequiresPermissions("paymode:payMode:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(PayMode payMode, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<PayMode> page = payModeService.findPage(new Page<PayMode>(request, response), payMode); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑保存成功表单页面
	 */
	@RequiresPermissions(value={"paymode:payMode:view","paymode:payMode:add","paymode:payMode:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(PayMode payMode, Model model) {
		model.addAttribute("payMode", payMode);
		if(StringUtils.isBlank(payMode.getId())){//如果ID是空为添加
			model.addAttribute("isAdd", true);
			//设置areacode从0开始自增长并为2位流水压入表单中
			String paymodeid = payModeService.getMaxPaymodeid();
			if(paymodeid == null) {
				paymodeid = "00";
			}
			else {
				int maxnum = Integer.parseInt(paymodeid) + 1;
				paymodeid = String.format("%02d",maxnum);
			}

			payMode.setPaymodeid(paymodeid);
		}
		return "purchasemanage/paymode/payModeForm";
	}

	/**
	 * 保存保存成功
	 */
	@RequiresPermissions(value={"paymode:payMode:add","paymode:payMode:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(PayMode payMode, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, payMode)){
			return form(payMode, model);
		}
		//新增或编辑表单保存
		payModeService.save(payMode);//保存
		addMessage(redirectAttributes, "保存保存成功成功");
		return "redirect:"+Global.getAdminPath()+"/paymode/payMode/?repage";
	}
	
	/**
	 * 删除保存成功
	 */
	@ResponseBody
	@RequiresPermissions("paymode:payMode:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(PayMode payMode, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		payModeService.delete(payMode);
		j.setMsg("删除保存成功成功");
		return j;
	}
	
	/**
	 * 批量删除保存成功
	 */
	@ResponseBody
	@RequiresPermissions("paymode:payMode:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			payModeService.delete(payModeService.get(id));
		}
		j.setMsg("删除保存成功成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("paymode:payMode:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(PayMode payMode, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "保存成功"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<PayMode> page = payModeService.findPage(new Page<PayMode>(request, response, -1), payMode);
    		new ExportExcel("保存成功", PayMode.class).setDataList(page.getList()).write(response, fileName).dispose();
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
	@RequiresPermissions("paymode:payMode:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<PayMode> list = ei.getDataList(PayMode.class);
			for (PayMode payMode : list){
				try{
					payModeService.save(payMode);
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
		return "redirect:"+Global.getAdminPath()+"/paymode/payMode/?repage";
    }
	
	/**
	 * 下载导入保存成功数据模板
	 */
	@RequiresPermissions("paymode:payMode:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "保存成功数据导入模板.xlsx";
    		List<PayMode> list = Lists.newArrayList(); 
    		new ExportExcel("保存成功数据", PayMode.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/paymode/payMode/?repage";
    }

}