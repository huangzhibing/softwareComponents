/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.workshopmanage.process.web;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import com.hqu.modules.workshopmanage.ppc.entity.MpsPlan;
import com.hqu.modules.workshopmanage.process.entity.SfcProcessDetail;
import com.hqu.modules.workshopmanage.process.service.ProcessService;
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

/**
 * 车间作业计划管理Controller
 * @author xhc
 * @version 2018-08-02
 */
@Controller
@RequestMapping(value = "${adminPath}/process/sfcProcessMain")
public class ProcessController extends BaseController {

	@Autowired
	private ProcessService processService;

	@ModelAttribute
	public MpsPlan get(@RequestParam(required=false) String id) {
		MpsPlan entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = processService.get(id);
		}
		if (entity == null){
			entity = new MpsPlan();
		}
		return entity;
	}
	
	/**
	 * 车间作业计划管理列表页面
	 */
	@RequiresPermissions("process:sfcProcessMain:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "workshopmanage/process/sfcProcessMainList";
	}

	/**
	 * 车间作业计划状态为已提交（C）的下达列表页面
	 */
	@RequiresPermissions("process:sfcProcessMain:order")
	@RequestMapping(value = {"orderList"})
	public String orderList() {
		return "workshopmanage/process/sfcProcessMainOrderList";
	}


	/**
	 * 车间作业计划管理列表数据
	 */
	@ResponseBody
	@RequiresPermissions("process:sfcProcessMain:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(MpsPlan mpsPlan, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<MpsPlan> page = processService.findPage(new Page<MpsPlan>(request, response), mpsPlan);
		return getBootstrapData(page);
	}


	/**
	 * 车间作业计划状态为所有的查询列表页面
	 */
	@RequiresPermissions("process:sfcProcessMain:query")
	@RequestMapping(value = {"queryList"})
	public String queryList() {
		return "workshopmanage/process/sfcProcessMainQueryList";
	}


	/**
	 * 车间作业计划状态为所有的列表数据（查询列表页面）
	 */
	@ResponseBody
	@RequiresPermissions("process:sfcProcessMain:query")
	@RequestMapping(value = "queryData")
	public Map<String, Object> queryData(MpsPlan mpsPlan, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<MpsPlan> page = processService.findAllPage(new Page<MpsPlan>(request, response), mpsPlan);
		return getBootstrapData(page);
	}

	/**
	 * 查看车间作业计划查询表单页面
	 */
	@RequiresPermissions("process:sfcProcessMain:query")
	@RequestMapping(value = "queryForm")
	public String queryForm(MpsPlan mpsPlan, Model model) {
		model.addAttribute("mpsPlan", mpsPlan);
		return "workshopmanage/process/sfcProcessMainQueryForm";
	}



	/**
	 * 车间作业计划状态为已提交（C）的列表数据（下达列表页面）
	 */
	@ResponseBody
	@RequiresPermissions("process:sfcProcessMain:order")
	@RequestMapping(value = "orderData")
	public Map<String, Object> orderData(MpsPlan mpsPlan, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<MpsPlan> page = processService.findOrderPage(new Page<MpsPlan>(request, response), mpsPlan);
		return getBootstrapData(page);
	}


	/**
	 * 查看，增加，编辑车间作业计划管理表单页面（包含下达页面）
	 */
	@RequiresPermissions(value={"process:sfcProcessMain:view","process:sfcProcessMain:add","process:sfcProcessMain:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(MpsPlan mpsPlan, Model model) {
		model.addAttribute("mpsPlan", mpsPlan);
		if(StringUtils.isBlank(mpsPlan.getId())){//如果ID是空为添加
			model.addAttribute("isAdd", true);
		}
		if("C".equals(mpsPlan.getSFCDealStatus())){//该mps计划对应的车间作业计划是已提交状态（C），则调用下达页面
			return "workshopmanage/process/sfcProcessMainOrderForm";
		}
		return "workshopmanage/process/sfcProcessMainForm";
	}


	/**
	 * 保存车间作业计划管理
	 */
	@RequiresPermissions(value={"process:sfcProcessMain:add","process:sfcProcessMain:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(MpsPlan mpsPlan, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, mpsPlan)){
			return form(mpsPlan, model);
		}
		//新增或编辑表单保存
		processService.save(mpsPlan);//保存
		addMessage(redirectAttributes, "执行成功");
		if("S".equals(mpsPlan.getSFCDealStatus())){//若是下达页面过来的，返回下达列表。
			return "redirect:"+Global.getAdminPath()+"/process/sfcProcessMain/orderList/?repage";
		}
		return "redirect:"+Global.getAdminPath()+"/process/sfcProcessMain/?repage";
	}
	
	/**
	 * 删除车间作业计划管理
	 */
	@ResponseBody
	@RequiresPermissions("process:sfcProcessMain:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(MpsPlan mpsPlan, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		processService.delete(mpsPlan);
		j.setMsg("删除车间作业计划管理成功");
		return j;
	}
	
	/**
	 * 批量删除车间作业计划管理
	 */
	@ResponseBody
	@RequiresPermissions("process:sfcProcessMain:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			processService.delete(processService.get(id));
		}
		j.setMsg("删除车间作业计划管理成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("process:sfcProcessMain:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(MpsPlan mpsPlan, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "车间作业计划管理"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<MpsPlan> page = processService.findPage(new Page<MpsPlan>(request, response, -1), mpsPlan);
    		new ExportExcel("车间作业计划管理", MpsPlan.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出车间作业计划管理记录失败！失败信息："+e.getMessage());
		}
			return j;
    }
    
    @ResponseBody
    @RequestMapping(value = "detail")
	public MpsPlan detail(String id) {
		return processService.get(id);
	}
	

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("process:sfcProcessMain:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<MpsPlan> list = ei.getDataList(MpsPlan.class);
			for (MpsPlan mpsPlan : list){
				try{
					processService.save(mpsPlan);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条车间作业计划管理记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条车间作业计划管理记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入车间作业计划管理失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/process/sfcProcessMain/?repage";
    }
	
	/**
	 * 下载导入车间作业计划管理数据模板
	 */
	@RequiresPermissions("process:sfcProcessMain:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "车间作业计划管理数据导入模板.xlsx";
    		List<MpsPlan> list = Lists.newArrayList(); 
    		new ExportExcel("车间作业计划管理数据", MpsPlan.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/process/sfcProcessMain/?repage";
    }
	

}