/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.workshopmanage.processbatch.web;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import com.hqu.modules.workshopmanage.process.entity.SfcProcessDetail;
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
import com.hqu.modules.workshopmanage.processbatch.service.ProcessBatchService;

/**
 * 车间作业计划分批Controller
 * @author xhc
 * @version 2018-08-04
 */
@Controller
@RequestMapping(value = "${adminPath}/processbatch/sfcProcessDetail")
public class ProcessBatchController extends BaseController {

	@Autowired
	private ProcessBatchService processBatchService;
	
	@ModelAttribute
	public SfcProcessDetail get(@RequestParam(required=false) String id) {
		SfcProcessDetail entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = processBatchService.get(id);
		}
		if (entity == null){
			entity = new SfcProcessDetail();
		}
		return entity;
	}
	
	/**
	 * 车间作业计划分批列表页面
	 */
	@RequiresPermissions("processbatch:sfcProcessDetail:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		//测试用，测试后注销
		//processBatchService.generateRoutineInfo("MPS1806-4001",1);
		//processBatchService.generateRoutineInfo("MPS1806-4001",5);
		return "workshopmanage/processbatch/sfcProcessDetailList";
	}
	
	/**
	 * 车间作业计划分批列表数据
	 */
	@ResponseBody
	@RequiresPermissions("processbatch:sfcProcessDetail:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(SfcProcessDetail sfcProcessDetail, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<SfcProcessDetail> page = processBatchService.findPage(new Page<SfcProcessDetail>(request, response), sfcProcessDetail);
		return getBootstrapData(page);
	}


	/**
	 * 车间作业计划分批查询列表页面
	 */
	@RequiresPermissions("processbatch:sfcProcessDetail:query")
	@RequestMapping(value = "queryList")
	public String queryList() {
		return "workshopmanage/processbatch/sfcProcessDetailQueryList";
	}

	/**
	 * 车间作业计划分批查询列表数据
	 */
	@ResponseBody
	@RequiresPermissions("processbatch:sfcProcessDetail:query")
	@RequestMapping(value = "queryData")
	public Map<String, Object> queryData(SfcProcessDetail sfcProcessDetail, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<SfcProcessDetail> page = processBatchService.findQueryPage(new Page<SfcProcessDetail>(request, response), sfcProcessDetail);
		return getBootstrapData(page);
	}

	/**
	 * 查看车间作业计划分批查询表单页面
	 */
	@RequiresPermissions(value="processbatch:sfcProcessDetail:query")
	@RequestMapping(value = "queryForm")
	public String queryForm(SfcProcessDetail sfcProcessDetail, Model model) {
		model.addAttribute("sfcProcessDetail", sfcProcessDetail);
		return "workshopmanage/processbatch/sfcProcessDetailQueryForm";
	}


	/**
	 * 查看，增加，编辑车间作业计划分批表单页面
	 */
	@RequiresPermissions(value={"processbatch:sfcProcessDetail:view","processbatch:sfcProcessDetail:add","processbatch:sfcProcessDetail:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(SfcProcessDetail sfcProcessDetail, Model model) {
		model.addAttribute("sfcProcessDetail", sfcProcessDetail);
		if(StringUtils.isBlank(sfcProcessDetail.getId())){//如果ID是空为添加
			model.addAttribute("isAdd", true);
		}
		return "workshopmanage/processbatch/sfcProcessDetailForm";
	}

	/**
	 * 保存车间作业计划分批
	 */
	@RequiresPermissions(value={"processbatch:sfcProcessDetail:add","processbatch:sfcProcessDetail:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(SfcProcessDetail sfcProcessDetail, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, sfcProcessDetail)){
			return form(sfcProcessDetail, model);
		}
		if(sfcProcessDetail.getProcessBatchList().size()!=0) {
			sfcProcessDetail.getProcessBatchList().remove(0);//把batchNo为0的去掉，框架bug
		}
		//新增或编辑表单保存
		processBatchService.save(sfcProcessDetail);//保存
		addMessage(redirectAttributes, "保存车间作业计划分批成功");
		return "redirect:"+Global.getAdminPath()+"/processbatch/sfcProcessDetail/?repage";
	}
	
	/**
	 * 删除车间作业计划分批
	 */
	@ResponseBody
	@RequiresPermissions("processbatch:sfcProcessDetail:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(SfcProcessDetail sfcProcessDetail, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		processBatchService.delete(sfcProcessDetail);
		j.setMsg("删除车间作业计划分批成功");
		return j;
	}
	
	/**
	 * 批量删除车间作业计划分批
	 */
	@ResponseBody
	@RequiresPermissions("processbatch:sfcProcessDetail:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			processBatchService.delete(processBatchService.get(id));
		}
		j.setMsg("删除车间作业计划分批成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("processbatch:sfcProcessDetail:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(SfcProcessDetail sfcProcessDetail, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "车间作业计划分批"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<SfcProcessDetail> page = processBatchService.findPage(new Page<SfcProcessDetail>(request, response, -1), sfcProcessDetail);
    		new ExportExcel("车间作业计划分批", SfcProcessDetail.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出车间作业计划分批记录失败！失败信息："+e.getMessage());
		}
			return j;
    }
    
    @ResponseBody
    @RequestMapping(value = "detail")
	public SfcProcessDetail detail(String id) {
		return processBatchService.get(id);
	}
	

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("processbatch:sfcProcessDetail:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<SfcProcessDetail> list = ei.getDataList(SfcProcessDetail.class);
			for (SfcProcessDetail sfcProcessDetail : list){
				try{
					processBatchService.save(sfcProcessDetail);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条车间作业计划分批记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条车间作业计划分批记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入车间作业计划分批失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/processbatch/sfcProcessDetail/?repage";
    }
	
	/**
	 * 下载导入车间作业计划分批数据模板
	 */
	@RequiresPermissions("processbatch:sfcProcessDetail:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "车间作业计划分批数据导入模板.xlsx";
    		List<SfcProcessDetail> list = Lists.newArrayList(); 
    		new ExportExcel("车间作业计划分批数据", SfcProcessDetail.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/processbatch/sfcProcessDetail/?repage";
    }
	

}