/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.basedata.workcenter.web;

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
import com.hqu.modules.basedata.workcenter.entity.WorkCenter;
import com.hqu.modules.basedata.workcenter.service.WorkCenterService;

/**
 * 工作中心Controller
 * @author dongqida
 * @version 2018-04-05
 */
@Controller
@RequestMapping(value = "${adminPath}/workcenter/workCenter")
public class WorkCenterController extends BaseController {

	@Autowired
	private WorkCenterService workCenterService;
	
	@ModelAttribute
	public WorkCenter get(@RequestParam(required=false) String id) {
		WorkCenter entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = workCenterService.get(id);
		}
		if (entity == null){
			entity = new WorkCenter();
		}
		return entity;
	}
	
	/**
	 * 工作中心定义列表页面
	 */
	@RequiresPermissions("workcenter:workCenter:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "basedata/workcenter/workCenterList";
	}
	
		/**
	 * 工作中心定义列表数据
	 */
	@ResponseBody
	@RequiresPermissions("workcenter:workCenter:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(WorkCenter workCenter, HttpServletRequest request, HttpServletResponse response, Model model) {
		//System.out.println(workCenter.getMachineTypeCode().getMachineTypeCode()+"machine---");
		Page<WorkCenter> page = workCenterService.findPage(new Page<WorkCenter>(request, response), workCenter); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑工作中心定义表单页面
	 */
	@RequiresPermissions(value={"workcenter:workCenter:view","workcenter:workCenter:add","workcenter:workCenter:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(WorkCenter workCenter, Model model) {
		
		if(StringUtils.isBlank(workCenter.getId())){//如果ID是空为添加
			String centercode=workCenterService.findMaxCode();
			if(centercode==null) {
				centercode="0000";
			}else {
				int max=Integer.parseInt(centercode)+1;
				centercode=String.format("%04d", max);
			}
			workCenter.setCenterCode(centercode);
			model.addAttribute("isAdd", true);
		}
		model.addAttribute("workCenter", workCenter);
		return "basedata/workcenter/workCenterForm";
	}

	/**
	 * 保存工作中心定义
	 */
	@RequiresPermissions(value={"workcenter:workCenter:add","workcenter:workCenter:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(WorkCenter workCenter, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, workCenter)){
			return form(workCenter, model);
		}
		/*if(workCenter.getMachineTypeCode().getMachineTypeCode()==null||"".equals(workCenter.getMachineTypeCode().getMachineTypeCode())) {
			String centercode=workCenterService.findMaxCode();
			if(centercode==null) {
				centercode="0000";
			}else {
				int max=Integer.parseInt(centercode)+1;
				centercode=String.format("%04d", max);
			}
			workCenter.setCenterCode(centercode);
		}*/
		
		//新增或编辑表单保存
		workCenterService.save(workCenter);//保存
		addMessage(redirectAttributes, "保存工作中心定义成功");
		return "redirect:"+Global.getAdminPath()+"/workcenter/workCenter/?repage";
	}
	
	/**
	 * 删除工作中心定义
	 */
	@ResponseBody
	@RequiresPermissions("workcenter:workCenter:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(WorkCenter workCenter, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		workCenterService.delete(workCenter);
		j.setMsg("删除工作中心定义成功");
		return j;
	}
	
	/**
	 * 批量删除工作中心定义
	 */
	@ResponseBody
	@RequiresPermissions("workcenter:workCenter:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			workCenterService.delete(workCenterService.get(id));
		}
		j.setMsg("删除工作中心定义成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("workcenter:workCenter:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(WorkCenter workCenter, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "工作中心定义"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<WorkCenter> page = workCenterService.findPage(new Page<WorkCenter>(request, response, -1), workCenter);
    		new ExportExcel("工作中心定义", WorkCenter.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出工作中心定义记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("workcenter:workCenter:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			String centercode=workCenterService.findMaxCode();
			if(centercode==null) {
				centercode="0000";
			}else {
				int max=Integer.parseInt(centercode)+1;
				centercode=String.format("%04d", max);
			}
			
			List<WorkCenter> list = ei.getDataList(WorkCenter.class);
			for (WorkCenter workCenter : list){
				try{
					String centerCode=String.format("%04d",Integer.parseInt(centercode)+successNum);
					workCenter.setCenterCode(centerCode);
					workCenterService.save(workCenter);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条工作中心定义记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条工作中心定义记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入工作中心定义失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/workcenter/workCenter/?repage";
    }
	
	/**
	 * 下载导入工作中心定义数据模板
	 */
	@RequiresPermissions("workcenter:workCenter:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "工作中心定义数据导入模板.xlsx";
    		List<WorkCenter> list = Lists.newArrayList(); 
    		new ExportExcel("工作中心定义数据", WorkCenter.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/workcenter/workCenter/?repage";
    }

}