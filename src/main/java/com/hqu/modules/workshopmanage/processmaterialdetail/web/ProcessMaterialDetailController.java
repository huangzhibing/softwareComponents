/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.workshopmanage.processmaterialdetail.web;

import java.util.List;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import com.hqu.modules.workshopmanage.processmaterialdetail.service.ProcessMaterialDetailService;
import com.hqu.modules.workshopmanage.processroutine.service.ProcessRoutineService;
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
import com.hqu.modules.workshopmanage.processroutinedetail.entity.ProcessRoutineDetail;
import com.hqu.modules.workshopmanage.processroutinedetail.service.ProcessRoutineDetailService;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.excel.ExportExcel;
import com.jeeplus.common.utils.excel.ImportExcel;
//import com.hqu.modules.workshopmanage.processmaterialdetail.entity.ProcessRoutineDetail;
//import com.hqu.modules.workshopmanage.processmaterialdetail.service.ProcessRoutineDetailService;

/**
 * 车间物料安装明细Controller
 * @author chen
 * @version 2018-08-10
 */
@Controller
@RequestMapping(value = "${adminPath}/processmaterialdetail/processRoutineDetail")
public class ProcessMaterialDetailController extends BaseController {

	//@Autowired
	//private ProcessRoutineDetailService processRoutineDetailService;
	@Autowired
	private ProcessMaterialDetailService processMaterialDetailService;

	@Autowired
	private ProcessRoutineService processRoutineService;
	
	@ModelAttribute
	public ProcessRoutineDetail get(@RequestParam(required=false) String id) {
		ProcessRoutineDetail entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = processMaterialDetailService.get(id);
		}
		if (entity == null){
			entity = new ProcessRoutineDetail();
		}
		return entity;
	}
	
	/**
	 * 车间物料安装明细列表页面
	 */
	@RequiresPermissions("processmaterialdetail:processRoutineDetail:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "workshopmanage/processmaterialdetail/processRoutineDetailList";
	}

	/**
	 * 车间物料安装明细查询列表页面
	 */
	@RequiresPermissions("processmaterialdetail:processRoutineDetail:query")
	@RequestMapping(value = "queryList")
	public String queryList() {
		return "workshopmanage/processmaterialdetail/processRoutineDetailQueryList";
	}

	/**
	 * 车间物料换件列表页面
	 */
	@RequiresPermissions("processmaterialdetail:processRoutineDetail:changeDeal")
	@RequestMapping(value = "changeList")
	public String changeList() {
		return "workshopmanage/processmaterialdetail/processRoutineDetailChangeList";
	}
	
	/**
	 * 车间物料安装明细录入列表数据(state为C的数据)
	 */
	@ResponseBody
	@RequiresPermissions("processmaterialdetail:processRoutineDetail:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(ProcessRoutineDetail processRoutineDetail, HttpServletRequest request, HttpServletResponse response, Model model) {
		processRoutineDetail.setBillstate("C");
		Page<ProcessRoutineDetail> page = processMaterialDetailService.findAssemblePage(new Page<ProcessRoutineDetail>(request, response), processRoutineDetail);
		return getBootstrapData(page);
	}

	/**
	 * 车间物料安装明细查询列表数据（包含billstate为所有状态的数据C：已确认未录入二维码；O：已完成该工艺步骤 录入二维码）
	 */
	@ResponseBody
	@RequiresPermissions("processmaterialdetail:processRoutineDetail:query")
	@RequestMapping(value = "queryData")
	public Map<String, Object> queryData(ProcessRoutineDetail processRoutineDetail, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<ProcessRoutineDetail> page = processMaterialDetailService.findPage(new Page<ProcessRoutineDetail>(request, response), processRoutineDetail);
		return getBootstrapData(page);
	}


	/**
	 * 车间物料换件列表数据;包含billstate为状态为O(已完成该工艺步骤 录入二维码)的数据
	 */
	@ResponseBody
	@RequiresPermissions("processmaterialdetail:processRoutineDetail:changedeal")
	@RequestMapping(value = "changeDealData")
	public Map<String, Object> changeDealData(ProcessRoutineDetail processRoutineDetail, HttpServletRequest request, HttpServletResponse response, Model model) {
		processRoutineDetail.setBillstate("O");
		Page<ProcessRoutineDetail> page = processMaterialDetailService.findPage(new Page<ProcessRoutineDetail>(request, response), processRoutineDetail);
		return getBootstrapData(page);
	}


	/**
	 * 查看，增加，编辑车间物料安装明细表单页面
	 */
	@RequiresPermissions(value={"processmaterialdetail:processRoutineDetail:view","processmaterialdetail:processRoutineDetail:add","processmaterialdetail:processRoutineDetail:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(ProcessRoutineDetail processRoutineDetail, Model model) {
		model.addAttribute("processRoutineDetail", processRoutineDetail);
		if(StringUtils.isBlank(processRoutineDetail.getId())){//如果ID是空为添加
			model.addAttribute("isAdd", true);
		}
		return "workshopmanage/processmaterialdetail/processRoutineDetailForm";
	}

	/**
	 * 查询车间物料安装明细表单页面
	 */
	@RequiresPermissions("processmaterialdetail:processRoutineDetail:query")
	@RequestMapping(value = "queryForm")
	public String queryForm(ProcessRoutineDetail processRoutineDetail, Model model) {
		model.addAttribute("processRoutineDetail", processRoutineDetail);
		if(StringUtils.isBlank(processRoutineDetail.getId())){//如果ID是空为添加
			model.addAttribute("isAdd", true);
		}
		return "workshopmanage/processmaterialdetail/processRoutineDetailQueryForm";
	}


	/**
	 * 车间物料换件处理表单页面
	 */
	@RequiresPermissions("processmaterialdetail:processRoutineDetail:changedeal")
	@RequestMapping(value = "changeDealForm")
	public String changeDealForm(ProcessRoutineDetail processRoutineDetail, Model model) {
		model.addAttribute("processRoutineDetail", processRoutineDetail);
		if(StringUtils.isBlank(processRoutineDetail.getId())){//如果ID是空为添加
			model.addAttribute("isAdd", true);
		}
		return "workshopmanage/processmaterialdetail/processRoutineDetailChangeForm";
	}


	/**
	 * 保存车间物料安装明细
	 */
	@RequiresPermissions(value={"processmaterialdetail:processRoutineDetail:add","processmaterialdetail:processRoutineDetail:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(ProcessRoutineDetail processRoutineDetail, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, processRoutineDetail)){
			return form(processRoutineDetail, model);
		}
		//新增或编辑表单保存
		processMaterialDetailService.save(processRoutineDetail);//保存
		addMessage(redirectAttributes, "执行成功");
		return "redirect:"+Global.getAdminPath()+"/processmaterialdetail/processRoutineDetail/?repage&sn="+processRoutineDetail.getMserialno();
	}


	/**
	 * 物料安装换件处理
	 */
	@ResponseBody
	@RequiresPermissions("processmaterialdetail:processRoutineDetail:changedeal")
	@RequestMapping(value = "changeDeal")
	public AjaxJson changeDeal(String changeId, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		if(changeId==null||changeId.equals("")){
			j.setSuccess(false);
			//j.setMsg("执行失败，请刷新页面后重试！");
			return j;
		}
		try {
			processRoutineService.replaceAndReassemble(changeId);
		}catch (Exception e){
			j.setSuccess(false);
			logger.error("processRoutineService.replaceAndReassemble______________error________________:"+e.toString());
			logger.debug("processRoutineService.replaceAndReassemble______________error________________:"+e.toString());
			return j;
		}
		//processMaterialDetailService.delete(processRoutineDetail);
		//j.setMsg("执行成功");
		j.setSuccess(true);
		return j;
	}

	
	/**
	 * 删除车间物料安装明细
	 */
	@ResponseBody
	@RequiresPermissions("processmaterialdetail:processRoutineDetail:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(ProcessRoutineDetail processRoutineDetail, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		processMaterialDetailService.delete(processRoutineDetail);
		j.setMsg("删除车间物料安装明细成功");
		return j;
	}
	
	/**
	 * 批量删除车间物料安装明细
	 */
	@ResponseBody
	@RequiresPermissions("processmaterialdetail:processRoutineDetail:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			processMaterialDetailService.delete(processMaterialDetailService.get(id));
		}
		j.setMsg("删除车间物料安装明细成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("processmaterialdetail:processRoutineDetail:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(ProcessRoutineDetail processRoutineDetail, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "车间物料安装明细"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<ProcessRoutineDetail> page = processMaterialDetailService.findPage(new Page<ProcessRoutineDetail>(request, response, -1), processRoutineDetail);
    		new ExportExcel("车间物料安装明细", ProcessRoutineDetail.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出车间物料安装明细记录失败！失败信息："+e.getMessage());
		}
			return j;
    }
    
    @ResponseBody
    @RequestMapping(value = "detail")
	public ProcessRoutineDetail detail(String id) {
		return processMaterialDetailService.get(id);
	}
	

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("processmaterialdetail:processRoutineDetail:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<ProcessRoutineDetail> list = ei.getDataList(ProcessRoutineDetail.class);
			for (ProcessRoutineDetail processRoutineDetail : list){
				try{
					processMaterialDetailService.save(processRoutineDetail);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条车间物料安装明细记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条车间物料安装明细记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入车间物料安装明细失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/processmaterialdetail/processRoutineDetail/?repage";
    }
	
	/**
	 * 下载导入车间物料安装明细数据模板
	 */
	@RequiresPermissions("processmaterialdetail:processRoutineDetail:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "车间物料安装明细数据导入模板.xlsx";
    		List<ProcessRoutineDetail> list = Lists.newArrayList(); 
    		new ExportExcel("车间物料安装明细数据", ProcessRoutineDetail.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/processmaterialdetail/processRoutineDetail/?repage";
    }
	

}