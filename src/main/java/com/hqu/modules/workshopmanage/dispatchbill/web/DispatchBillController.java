/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.workshopmanage.dispatchbill.web;

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
import com.hqu.modules.workshopmanage.dispatchbill.entity.DispatchBill;
import com.hqu.modules.workshopmanage.dispatchbill.service.DispatchBillService;

/**
 * 车间派工单Controller
 * @author zhangxin
 * @version 2018-05-31
 */
@Controller
@RequestMapping(value = "${adminPath}/dispatchbill/dispatchBill")
public class DispatchBillController extends BaseController {

	@Autowired
	private DispatchBillService dispatchBillService;
	
	@ModelAttribute
	public DispatchBill get(@RequestParam(required=false) String id) {
		DispatchBill entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = dispatchBillService.get(id);
		}
		if (entity == null){
			entity = new DispatchBill();
		}
		return entity;
	}
	
	/**
	 * 车间派工单列表页面
	 */
	@RequiresPermissions("dispatchbill:dispatchBill:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "workshopmanage/dispatchbill/dispatchBillList";
	}
	
		/**
	 * 车间派工单列表数据
	 */
	@ResponseBody
	@RequiresPermissions("dispatchbill:dispatchBill:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(DispatchBill dispatchBill, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<DispatchBill> page = dispatchBillService.findPage(new Page<DispatchBill>(request, response), dispatchBill); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑车间派工单表单页面
	 */
	@RequiresPermissions(value={"dispatchbill:dispatchBill:view","dispatchbill:dispatchBill:add","dispatchbill:dispatchBill:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(DispatchBill dispatchBill, Model model) {
		model.addAttribute("dispatchBill", dispatchBill);
		if(StringUtils.isBlank(dispatchBill.getId())){//如果ID是空为添加
			model.addAttribute("isAdd", true);
		}
		return "workshopmanage/dispatchbill/dispatchBillForm";
	}

	/**
	 * 保存车间派工单
	 */
	@RequiresPermissions(value={"dispatchbill:dispatchBill:add","dispatchbill:dispatchBill:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(DispatchBill dispatchBill, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, dispatchBill)){
			return form(dispatchBill, model);
		}
		//新增或编辑表单保存
		dispatchBillService.save(dispatchBill);//保存
		addMessage(redirectAttributes, "保存车间派工单成功");
		return "redirect:"+Global.getAdminPath()+"/dispatchbill/dispatchBill/?repage";
	}
	
	/**
	 * 提交车间派工单
	 */
	@RequiresPermissions("dispatchbill:dispatchBill:edit")
	@RequestMapping(value = "submit")
	public String submit(DispatchBill dispatchBill, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, dispatchBill)){
			return form(dispatchBill, model);
		}
		//提交表单
		dispatchBillService.submit(dispatchBill);//保存
		addMessage(redirectAttributes, "提交车间派工单成功");
		return "redirect:"+Global.getAdminPath()+"/dispatchbill/dispatchBill/?repage";
	}
	
	/**
	 * 删除车间派工单
	 */
	@ResponseBody
	@RequiresPermissions("dispatchbill:dispatchBill:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(DispatchBill dispatchBill, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		dispatchBillService.delete(dispatchBill);
		j.setMsg("删除车间派工单成功");
		return j;
	}
	
	/**
	 * 批量删除车间派工单
	 */
	@ResponseBody
	@RequiresPermissions("dispatchbill:dispatchBill:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			dispatchBillService.delete(dispatchBillService.get(id));
		}
		j.setMsg("删除车间派工单成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("dispatchbill:dispatchBill:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(DispatchBill dispatchBill, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "车间派工单"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<DispatchBill> page = dispatchBillService.findPage(new Page<DispatchBill>(request, response, -1), dispatchBill);
    		new ExportExcel("车间派工单", DispatchBill.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出车间派工单记录失败！失败信息："+e.getMessage());
		}
			return j;
    }
    
    @ResponseBody
    @RequestMapping(value = "detail")
	public DispatchBill detail(String id) {
		return dispatchBillService.get(id);
	}
	

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("dispatchbill:dispatchBill:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<DispatchBill> list = ei.getDataList(DispatchBill.class);
			for (DispatchBill dispatchBill : list){
				try{
					dispatchBillService.save(dispatchBill);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条车间派工单记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条车间派工单记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入车间派工单失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/dispatchbill/dispatchBill/?repage";
    }
	
	/**
	 * 下载导入车间派工单数据模板
	 */
	@RequiresPermissions("dispatchbill:dispatchBill:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "车间派工单数据导入模板.xlsx";
    		List<DispatchBill> list = Lists.newArrayList(); 
    		new ExportExcel("车间派工单数据", DispatchBill.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/dispatchbill/dispatchBill/?repage";
    }
	

}