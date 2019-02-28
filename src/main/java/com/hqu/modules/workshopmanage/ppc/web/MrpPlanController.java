/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.workshopmanage.ppc.web;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import com.hqu.modules.workshopmanage.ppc.entity.MpsPlan;
import com.hqu.modules.workshopmanage.ppc.service.MpsPlanService;
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
import com.hqu.modules.workshopmanage.ppc.entity.MrpPlan;
import com.hqu.modules.workshopmanage.ppc.service.MrpPlanService;

/**
 * MRP计划Controller
 * @author XHC
 * @version 2018-06-28
 */
@Controller
@RequestMapping(value = "${adminPath}/ppc/mrpPlan")
public class MrpPlanController extends BaseController {

	@Autowired
	private MrpPlanService mrpPlanService;
	@Autowired
	private MpsPlanService mpsPlanService;
	
	@ModelAttribute
	public MpsPlan get(@RequestParam(required=false) String id) {
		MpsPlan entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = mpsPlanService.getMRPbyMPS(id);
		}
		if (entity == null){
			entity = new MpsPlan();
		}
		return entity;
	}
	
	/**
	 * MRP计划列表页面
	 */
	@RequiresPermissions("ppc:mrpPlan:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		//测试MRP同采购之间的接口
		/*mrpPlanService.generatePurRollPlan("MRP201809-25");
		mrpPlanService.generatePurRollPlan("MRP201809-26");
		mrpPlanService.generatePurRollPlan("MRP201809-27");
		mrpPlanService.generatePurRollPlan("MRP201809-28");
		*/
		return "modules/ppc/mrpPlanList";
	}

	/**
	 * MRP计划审核列表页面
	 */
	@RequiresPermissions("ppc:mrpPlan:audit")
	@RequestMapping(value = "auditList")
	public String auditList() {
		return "modules/ppc/mrpPlanAuditList";
	}


	/**
	 * MRP计划列表数据
	 */
	@ResponseBody
	@RequiresPermissions("ppc:mrpPlan:list")
	@RequestMapping(value = "mrpData")
	public Map<String, Object> mrpData(MrpPlan mrpPlan, HttpServletRequest request, HttpServletResponse response, Model model) {
		this.mrpPlanService.makeMRP();
		Page<MrpPlan> page = mrpPlanService.findPage(new Page<MrpPlan>(request, response), mrpPlan);
		return getBootstrapData(page);
	}
	/**
	 * MPS计划列表数据
	 */
	@ResponseBody
	@RequiresPermissions("ppc:mrpPlan:list")
	@RequestMapping(value = "mpsData")
	public Map<String, Object> mpsData(MpsPlan mpsPlan, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<MpsPlan> page = mpsPlanService.findMRPDealPage(new Page<MpsPlan>(request, response), mpsPlan);
		return getBootstrapData(page);
	}


	/**
	 * MPS计划审核列表数据
	 */
	@ResponseBody
	@RequiresPermissions("ppc:mrpPlan:audit")
	@RequestMapping(value = "mpsAuditData")
	public Map<String, Object> mpsAuditData(MpsPlan mpsPlan, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<MpsPlan> page = mpsPlanService.findMRPAuditPage(new Page<MpsPlan>(request, response), mpsPlan);
		return getBootstrapData(page);
	}


	/**
	 * 查看，增加，编辑MRP计划表单页面
	 */
	@RequiresPermissions(value={"ppc:mrpPlan:view","ppc:mrpPlan:add","ppc:mrpPlan:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(MrpPlan mrpPlan, Model model) {
		model.addAttribute("mrpPlan", mrpPlan);
		if(StringUtils.isBlank(mrpPlan.getId())){//如果ID是空为添加
			model.addAttribute("isAdd", true);
		}
		return "modules/ppc/mrpPlanForm";
	}

	/**
	 * MRP计划审核表单页面
	 */
	@RequiresPermissions("ppc:mrpPlan:audit")
	@RequestMapping(value = "auditForm")
	public String auditForm(MrpPlan mrpPlan, Model model) {
		model.addAttribute("mrpPlan", mrpPlan);
		if(StringUtils.isBlank(mrpPlan.getId())){//如果ID是空为添加
			model.addAttribute("isAdd", true);
		}
		return "modules/ppc/mrpPlanAuditForm";
	}

	/**
	 * 保存MRP计划
	 */
	@RequiresPermissions(value={"ppc:mrpPlan:add","ppc:mrpPlan:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(MpsPlan mpsPlan, Model model, RedirectAttributes redirectAttributes) throws Exception{
		/*if (!beanValidator(model, mrpPlan)){
			return form(mrpPlan, model);
		}*/
		//编辑MRP表单保存
		mrpPlanService.saveMpsAndMrp(mpsPlan);//保存MPS及对应的MRP列表
		addMessage(redirectAttributes, "保存MRP计划列表成功");
		return "redirect:"+Global.getAdminPath()+"/ppc/mrpPlan/?repage";
	}

	/**
	 * 保存审核后的MRP计划
	 */
	@RequiresPermissions(value={"ppc:mrpPlan:add","ppc:mrpPlan:edit"},logical=Logical.OR)
	@RequestMapping(value = "auditSave")
	public String auditSave(MpsPlan mpsPlan, Model model, RedirectAttributes redirectAttributes) throws Exception{
		//编辑MRP表单保存
		mrpPlanService.saveMpsAndMrp(mpsPlan);//保存MPS及对应的MRP列表
		addMessage(redirectAttributes, "MRP计划审批成功");
		return "redirect:"+Global.getAdminPath()+"/ppc/mrpPlan/auditList/?repage";
	}

    /**
     * 响应自动生成MRP计划按钮事件
     */
    @ResponseBody
    @RequiresPermissions("ppc:mrpPlan:edit")
    @RequestMapping(value = "makeMRP")
    public AjaxJson makeMRP() {
        mrpPlanService.makeMRP();
        AjaxJson j = new AjaxJson();
        j.setMsg("执行成功！");
        return j;
    }

	/**
	 * 删除MRP计划
	 */
	@ResponseBody
	@RequiresPermissions("ppc:mrpPlan:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(MrpPlan mrpPlan, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		mrpPlanService.delete(mrpPlan);
		j.setMsg("删除MRP计划成功");
		return j;
	}
	
	/**
	 * 批量删除MRP计划
	 */
	@ResponseBody
	@RequiresPermissions("ppc:mrpPlan:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			mrpPlanService.delete(mrpPlanService.get(id));
		}
		j.setMsg("删除MRP计划成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("ppc:mrpPlan:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(MrpPlan mrpPlan, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "MRP计划"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<MrpPlan> page = mrpPlanService.findPage(new Page<MrpPlan>(request, response, -1), mrpPlan);
    		new ExportExcel("MRP计划", MrpPlan.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出MRP计划记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("ppc:mrpPlan:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<MrpPlan> list = ei.getDataList(MrpPlan.class);
			for (MrpPlan mrpPlan : list){
				try{
					mrpPlanService.save(mrpPlan);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条MRP计划记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条MRP计划记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入MRP计划失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/ppc/mrpPlan/?repage";
    }
	
	/**
	 * 下载导入MRP计划数据模板
	 */
	@RequiresPermissions("ppc:mrpPlan:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "MRP计划数据导入模板.xlsx";
    		List<MrpPlan> list = Lists.newArrayList(); 
    		new ExportExcel("MRP计划数据", MrpPlan.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/ppc/mrpPlan/?repage";
    }

}