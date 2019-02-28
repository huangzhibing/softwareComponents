/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.basedata.machinetype.web;

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
import com.hqu.modules.basedata.machinetype.entity.MachineType;
import com.hqu.modules.basedata.machinetype.service.MachineTypeService;

/**
 * 设备类别定义Controller
 * @author 何志铭
 * @version 2018-04-05
 */
@Controller
@RequestMapping(value = "${adminPath}/machinetype/machineType")
public class MachineTypeController extends BaseController {

	@Autowired
	private MachineTypeService machineTypeService;
	
	@ModelAttribute
	public MachineType get(@RequestParam(required=false) String id) {
		MachineType entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = machineTypeService.get(id);
		}
		if (entity == null){
			entity = new MachineType();
		}
		return entity;
	}
	
	/**
	 * 设备类别定义列表页面
	 */
	@RequiresPermissions("machinetype:machineType:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "basedata/machinetype/machineTypeList";
	}
	
		/**
	 * 设备类别定义列表数据
	 */
	@ResponseBody
	@RequiresPermissions("machinetype:machineType:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(MachineType machineType, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<MachineType> page = machineTypeService.findPage(new Page<MachineType>(request, response), machineType); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑设备类别定义表单页面
	 */
	@RequiresPermissions(value={"machinetype:machineType:view","machinetype:machineType:add","machinetype:machineType:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(MachineType machineType, Model model) {
		model.addAttribute("machineType", machineType);
		String maxCode;
		if(StringUtils.isBlank(machineType.getId())){//如果ID是空为添加
			model.addAttribute("isAdd", true);
			if(machineTypeService.selectMax()!=null) {
				maxCode=machineTypeService.selectMax();
				int numCode=Integer.parseInt(maxCode);
				numCode++;
				maxCode=String.format("%02d", numCode);
			}else {
				maxCode="00";
			}
			machineType.setMachineTypeCode(maxCode);
		}
		return "basedata/machinetype/machineTypeForm";
	}

	/**
	 * 保存设备类别定义
	 */
	@RequiresPermissions(value={"machinetype:machineType:add","machinetype:machineType:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(MachineType machineType, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, machineType)){
			return form(machineType, model);
		}
		//新增或编辑表单保存
		machineTypeService.save(machineType);//保存
		addMessage(redirectAttributes, "保存设备类别定义成功");
		return "redirect:"+Global.getAdminPath()+"/machinetype/machineType/?repage";
	}
	
	/**
	 * 删除设备类别定义
	 */
	@ResponseBody
	@RequiresPermissions("machinetype:machineType:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(MachineType machineType, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		machineTypeService.delete(machineType);
		j.setMsg("删除设备类别定义成功");
		return j;
	}
	
	/**
	 * 批量删除设备类别定义
	 */
	@ResponseBody
	@RequiresPermissions("machinetype:machineType:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			machineTypeService.delete(machineTypeService.get(id));
		}
		j.setMsg("删除设备类别定义成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("machinetype:machineType:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(MachineType machineType, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "设备类别定义"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<MachineType> page = machineTypeService.findPage(new Page<MachineType>(request, response, -1), machineType);
    		new ExportExcel("设备类别定义", MachineType.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出设备类别定义记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("machinetype:machineType:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<MachineType> list = ei.getDataList(MachineType.class);
			for (MachineType machineType : list){
				try{
					machineTypeService.saveExc(machineType);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条设备类别定义记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条设备类别定义记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入设备类别定义失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/machinetype/machineType/?repage";
    }
	
	/**
	 * 下载导入设备类别定义数据模板
	 */
	@RequiresPermissions("machinetype:machineType:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "设备类别定义数据导入模板.xlsx";
    		List<MachineType> list = Lists.newArrayList(); 
    		new ExportExcel("设备类别定义数据", MachineType.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/machinetype/machineType/?repage";
    }

}