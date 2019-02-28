/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.inventorymanage.employee.web;

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
import com.hqu.modules.inventorymanage.employee.entity.EmployeeWareHouse;
import com.hqu.modules.inventorymanage.employee.service.EmployeeWareHouseService;

/**
 * 仓库人员定义Controller
 * @author liujiachen
 * @version 2018-04-18
 */
@Controller
@RequestMapping(value = "${adminPath}/employee/employeeWareHouse")
public class EmployeeWareHouseController extends BaseController {

	@Autowired
	private EmployeeWareHouseService employeeWareHouseService;
	
	@ModelAttribute
	public EmployeeWareHouse get(@RequestParam(required=false) String id) {
		EmployeeWareHouse entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = employeeWareHouseService.get(id);
		}
		if (entity == null){
			entity = new EmployeeWareHouse();
		}
		return entity;
	}
	
	/**
	 * 仓库人员列表页面
	 */
	@RequiresPermissions("employee:employeeWareHouse:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "inventorymanage/employee/employeeWareHouseList";
	}
	
		/**
	 * 仓库人员列表数据
	 */
	@ResponseBody
	@RequiresPermissions("employee:employeeWareHouse:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(EmployeeWareHouse employeeWareHouse, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<EmployeeWareHouse> page = employeeWareHouseService.findPage(new Page<EmployeeWareHouse>(request, response), employeeWareHouse); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑仓库人员表单页面
	 */
	@RequiresPermissions(value={"employee:employeeWareHouse:view","employee:employeeWareHouse:add","employee:employeeWareHouse:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(EmployeeWareHouse employeeWareHouse, Model model) {
		model.addAttribute("employeeWareHouse", employeeWareHouse);
		if(StringUtils.isBlank(employeeWareHouse.getId())){//如果ID是空为添加
			model.addAttribute("isAdd", true);
		}
		return "inventorymanage/employee/employeeWareHouseForm";
	}

	/**
	 * 保存仓库人员
	 */
	@RequiresPermissions(value={"employee:employeeWareHouse:add","employee:employeeWareHouse:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(EmployeeWareHouse employeeWareHouse, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, employeeWareHouse)){
			return form(employeeWareHouse, model);
		}
		//新增或编辑表单保存
		employeeWareHouseService.save(employeeWareHouse);//保存
		addMessage(redirectAttributes, "保存仓库人员成功");
		return "redirect:"+Global.getAdminPath()+"/employee/employeeWareHouse/?repage";
	}
	
	/**
	 * 删除仓库人员
	 */
	@ResponseBody
	@RequiresPermissions("employee:employeeWareHouse:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(EmployeeWareHouse employeeWareHouse, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		employeeWareHouseService.delete(employeeWareHouse);
		j.setMsg("删除仓库人员成功");
		return j;
	}
	
	/**
	 * 批量删除仓库人员
	 */
	@ResponseBody
	@RequiresPermissions("employee:employeeWareHouse:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			employeeWareHouseService.delete(employeeWareHouseService.get(id));
		}
		j.setMsg("删除仓库人员成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("employee:employeeWareHouse:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(EmployeeWareHouse employeeWareHouse, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "仓库人员"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<EmployeeWareHouse> page = employeeWareHouseService.findPage(new Page<EmployeeWareHouse>(request, response, -1), employeeWareHouse);
    		new ExportExcel("仓库人员", EmployeeWareHouse.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出仓库人员记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("employee:employeeWareHouse:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<EmployeeWareHouse> list = ei.getDataList(EmployeeWareHouse.class);
			for (EmployeeWareHouse employeeWareHouse : list){
				try{
					employeeWareHouseService.save(employeeWareHouse);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条仓库人员记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条仓库人员记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入仓库人员失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/employee/employeeWareHouse/?repage";
    }
	
	/**
	 * 下载导入仓库人员数据模板
	 */
	@RequiresPermissions("employee:employeeWareHouse:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "仓库人员数据导入模板.xlsx";
    		List<EmployeeWareHouse> list = Lists.newArrayList(); 
    		new ExportExcel("仓库人员数据", EmployeeWareHouse.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/employee/employeeWareHouse/?repage";
    }

}