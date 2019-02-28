/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.workshopmanage.processroutinedetail.web;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
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
import com.hqu.modules.workshopmanage.processroutinedetail.entity.ProcessRoutineDetail;
import com.hqu.modules.workshopmanage.processroutinedetail.service.ProcessRoutineDetailService;

/**
 * 加工路线单明细单Controller
 * @author xhc
 * @version 2018-08-22
 */
@Controller
@RequestMapping(value = "${adminPath}/processroutinedetail/processRoutineDetail")
public class ProcessRoutineDetailController extends BaseController {

	@Autowired
	private ProcessRoutineDetailService processRoutineDetailService;
	
	@ModelAttribute
	public ProcessRoutineDetail get(@RequestParam(required=false) String id) {
		ProcessRoutineDetail entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = processRoutineDetailService.get(id);
		}
		if (entity == null){
			entity = new ProcessRoutineDetail();
		}
		return entity;
	}
	
	/**
	 * 加工路线单明细列表页面
	 */
	@RequiresPermissions("processroutinedetail:processRoutineDetail:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "workshopmanage/processroutinedetail/processRoutineDetailList";
	}
	
		/**
	 * 车间加工路线明细列表数据（只显示状态为已提交的数据C）
	 */
	@ResponseBody
	@RequiresPermissions("processroutinedetail:processRoutineDetail:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(ProcessRoutineDetail processRoutineDetail, HttpServletRequest request, HttpServletResponse response, Model model) {
		processRoutineDetail.setBillstate("C");
		Page<ProcessRoutineDetail> page = processRoutineDetailService.findPage(new Page<ProcessRoutineDetail>(request, response), processRoutineDetail);
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑加工路线单明细表单页面
	 */
	@RequiresPermissions(value={"processroutinedetail:processRoutineDetail:view","processroutinedetail:processRoutineDetail:add","processroutinedetail:processRoutineDetail:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(ProcessRoutineDetail processRoutineDetail, Model model) {
		model.addAttribute("processRoutineDetail", processRoutineDetail);
		if(StringUtils.isBlank(processRoutineDetail.getId())){//如果ID是空为添加
			model.addAttribute("isAdd", true);
		}
		return "workshopmanage/processroutinedetail/processRoutineDetailForm";
	}

	/**
	 * 保存加工路线单明细
	 */
	@RequiresPermissions(value={"processroutinedetail:processRoutineDetail:add","processroutinedetail:processRoutineDetail:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(ProcessRoutineDetail processRoutineDetail, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, processRoutineDetail)){
			return form(processRoutineDetail, model);
		}
		//新增或编辑表单保存
		processRoutineDetailService.save(processRoutineDetail);//保存
		addMessage(redirectAttributes, "保存加工路线单明细成功");
		return "redirect:"+Global.getAdminPath()+"/processroutinedetail/processRoutineDetail/?repage";
	}
	
	/**
	 * 删除加工路线单明细
	 */
	@ResponseBody
	@RequiresPermissions("processroutinedetail:processRoutineDetail:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(ProcessRoutineDetail processRoutineDetail, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		processRoutineDetailService.delete(processRoutineDetail);
		j.setMsg("删除加工路线单明细成功");
		return j;
	}
	
	/**
	 * 批量删除加工路线单明细
	 */
	@ResponseBody
	@RequiresPermissions("processroutinedetail:processRoutineDetail:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			processRoutineDetailService.delete(processRoutineDetailService.get(id));
		}
		j.setMsg("删除加工路线单明细成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("processroutinedetail:processRoutineDetail:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(ProcessRoutineDetail processRoutineDetail, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "加工路线单明细"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<ProcessRoutineDetail> page = processRoutineDetailService.findPage(new Page<ProcessRoutineDetail>(request, response, -1), processRoutineDetail);
    		new ExportExcel("加工路线单明细", ProcessRoutineDetail.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出加工路线单明细记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("processroutinedetail:processRoutineDetail:import")
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
					processRoutineDetailService.save(processRoutineDetail);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条加工路线单明细记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条加工路线单明细记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入加工路线单明细失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/processroutinedetail/processRoutineDetail/?repage";
    }
	
	/**
	 * 下载导入加工路线单明细数据模板
	 */
	@RequiresPermissions("processroutinedetail:processRoutineDetail:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "加工路线单明细数据导入模板.xlsx";
    		List<ProcessRoutineDetail> list = Lists.newArrayList(); 
    		new ExportExcel("加工路线单明细数据", ProcessRoutineDetail.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/processroutinedetail/processRoutineDetail/?repage";
    }

}