/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.qualitymanage.machinequalitysta.web;

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
import com.hqu.modules.qualitymanage.machinequalitysta.entity.MachineRate;
import com.hqu.modules.qualitymanage.machinequalitysta.service.MachineRateService;

/**
 * 整机良率统计（电子看板）Controller
 * @author yxb
 * @version 2018-11-17
 */
@Controller
@RequestMapping(value = "${adminPath}/machinequalitysta/machineRate")
public class MachineRateController extends BaseController {

	@Autowired
	private MachineRateService machineRateService;
	
	@ModelAttribute
	public MachineRate get(@RequestParam(required=false) String id) {
		MachineRate entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = machineRateService.get(id);
		}
		if (entity == null){
			entity = new MachineRate();
		}
		return entity;
	}
	
	/**
	 * 整机良率统计列表页面
	 */
	@RequiresPermissions("machinequalitysta:machineRate:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "qualitymanage/machinequalitysta/machineRateList";
	}
	
		/**
	 * 整机良率统计列表数据
	 */
	@ResponseBody
	@RequiresPermissions("machinequalitysta:machineRate:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(MachineRate machineRate, HttpServletRequest request, HttpServletResponse response, Model model) {
		//按当天每个小时实时统计
		if("D".equals(machineRate.getTimeType())){
			machineRateService.machineRateStaByDay(machineRate);
		}

		Page<MachineRate> page = machineRateService.findPage(new Page<MachineRate>(request, response), machineRate);
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑整机良率统计表单页面
	 */
	@RequiresPermissions(value={"machinequalitysta:machineRate:view","machinequalitysta:machineRate:add","machinequalitysta:machineRate:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(MachineRate machineRate, Model model) {
		model.addAttribute("machineRate", machineRate);
		if(StringUtils.isBlank(machineRate.getId())){//如果ID是空为添加
			model.addAttribute("isAdd", true);
		}
		return "qualitymanage/machinequalitysta/machineRateForm";
	}

	/**
	 * 保存整机良率统计
	 */
	@RequiresPermissions(value={"machinequalitysta:machineRate:add","machinequalitysta:machineRate:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(MachineRate machineRate, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, machineRate)){
			return form(machineRate, model);
		}
		//新增或编辑表单保存
		machineRateService.save(machineRate);//保存
		addMessage(redirectAttributes, "保存整机良率统计成功");
		return "redirect:"+Global.getAdminPath()+"/machinequalitysta/machineRate/?repage";
	}
	
	/**
	 * 删除整机良率统计
	 */
	@ResponseBody
	@RequiresPermissions("machinequalitysta:machineRate:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(MachineRate machineRate, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		machineRateService.delete(machineRate);
		j.setMsg("删除整机良率统计成功");
		return j;
	}
	
	/**
	 * 批量删除整机良率统计
	 */
	@ResponseBody
	@RequiresPermissions("machinequalitysta:machineRate:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			machineRateService.delete(machineRateService.get(id));
		}
		j.setMsg("删除整机良率统计成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("machinequalitysta:machineRate:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(MachineRate machineRate, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "整机良率统计"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<MachineRate> page = machineRateService.findPage(new Page<MachineRate>(request, response, -1), machineRate);
    		new ExportExcel("整机良率统计", MachineRate.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出整机良率统计记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("machinequalitysta:machineRate:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<MachineRate> list = ei.getDataList(MachineRate.class);
			for (MachineRate machineRate : list){
				try{
					machineRateService.save(machineRate);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条整机良率统计记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条整机良率统计记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入整机良率统计失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/machinequalitysta/machineRate/?repage";
    }
	
	/**
	 * 下载导入整机良率统计数据模板
	 */
	@RequiresPermissions("machinequalitysta:machineRate:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "整机良率统计数据导入模板.xlsx";
    		List<MachineRate> list = Lists.newArrayList(); 
    		new ExportExcel("整机良率统计数据", MachineRate.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/machinequalitysta/machineRate/?repage";
    }

}