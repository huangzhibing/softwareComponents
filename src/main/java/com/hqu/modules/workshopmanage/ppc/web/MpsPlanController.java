/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.workshopmanage.ppc.web;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import com.hqu.modules.workshopmanage.ppc.xhcUtil;
import com.hqu.modules.workshopmanage.processbatch.service.ProcessBatchService;
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
import com.hqu.modules.workshopmanage.ppc.entity.MpsPlan;
import com.hqu.modules.workshopmanage.ppc.service.MpsPlanService;
import com.hqu.modules.workshopmanage.process.service.ProcessService;
import com.hqu.modules.workshopmanage.processbatch.service.ProcessBatchService;
import com.hqu.modules.workshopmanage.processroutine.service.ProcessRoutineService;
import com.hqu.modules.workshopmanage.materialorder.service.SfcMaterialOrderService;

/**
 * MPSPlanController
 * @author XHC
 * @version 2018-06-01
 */
@Controller
@RequestMapping(value = "${adminPath}/ppc/mpsPlan")
public class MpsPlanController extends BaseController {

	@Autowired
	private MpsPlanService mpsPlanService;

	@Autowired
	private ProcessService processService;

	@Autowired
	private ProcessBatchService processBatchService;

	@Autowired
	private ProcessRoutineService processRoutineService;

	@Autowired
	private SfcMaterialOrderService sfcMaterialOrderService;


	@ModelAttribute
	public MpsPlan get(@RequestParam(required=false) String id) {
		MpsPlan entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = mpsPlanService.get(id);
		}
		if (entity == null){
			entity = new MpsPlan();
		}
		return entity;
	}
	
	/**
	 * MPSPlan列表页面
	 */
	@RequiresPermissions("ppc:mpsPlan:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		//测试用
		//mpsPlanService.generateWeeks();
		 //mpsPlanService.generateMSerialNo("MPS1807-3");
		//processService.fillProcessBillNo("MPS1807-3002");
		//processBatchService.fillProcessBillBatchNo("MPS1807-3002",2);
		//processRoutineService.fillProcessRoutineSeqNo("MPS1807-3002-2");
		//sfcMaterialOrderService.returnAndGetMaterial("SMO20180812-1",1,2);
		//测试
		return "modules/ppc/mpsPlanList";
	}

	/**
	 * MPSPlan审核列表页面
	 */
	@RequiresPermissions("ppc:mpsPlan:audit")
	@RequestMapping(value = {"auditList"})
	public String auditList() {
		return "modules/ppc/mpsPlanAuditList";
	}


    /**
     * 响应读取内部订单按钮事件
     */
    @ResponseBody
    @RequiresPermissions("ppc:mpsPlan:edit")
    @RequestMapping(value = "loadOrder")
    public AjaxJson loadOrder() {
        this.mpsPlanService.getInnerSalOrder();
        AjaxJson j = new AjaxJson();
        j.setMsg("已成功读入内部定单！");
        return j;
    }

		/**
	 * MPSPlan列表（状态为P或U的）数据
	 */
	@ResponseBody
	@RequiresPermissions("ppc:mpsPlan:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(MpsPlan mpsPlan, HttpServletRequest request, HttpServletResponse response, Model model) {
		//this.mpsPlanService.getInnerSalOrder();
		Page<MpsPlan> page = mpsPlanService.findPage(new Page<MpsPlan>(request, response), mpsPlan); 
		return getBootstrapData(page);
	}
	/**
	 * MPSPlan列表（状态为C的）数据
	 */
	@ResponseBody
	@RequiresPermissions("ppc:mpsPlan:list")
	@RequestMapping(value = "auditData")
	public Map<String, Object> auditData(MpsPlan mpsPlan, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<MpsPlan> page = mpsPlanService.findAuditPage(new Page<MpsPlan>(request, response), mpsPlan);
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑MPSPlan表单页面
	 */
	@RequiresPermissions(value={"ppc:mpsPlan:view","ppc:mpsPlan:add","ppc:mpsPlan:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(MpsPlan mpsPlan, Model model) {
		
		model.addAttribute("mpsPlan", mpsPlan);
		if(StringUtils.isBlank(mpsPlan.getId())){//如果ID是空为添加
			model.addAttribute("isAdd", true);
		}
		return "modules/ppc/mpsPlanForm";
	}

	/**
	 * MPSPlan审核表单页面
	 */
	@RequiresPermissions("ppc:mpsPlan:audit")
	@RequestMapping(value = "auditForm")
	public String auditForm(MpsPlan mpsPlan, Model model) {

		model.addAttribute("mpsPlan", mpsPlan);
		if(StringUtils.isBlank(mpsPlan.getId())){//如果ID是空为添加
			model.addAttribute("isAdd", true);
		}
		return "modules/ppc/mpsPlanAuditForm";
	}

	/**
	 * 保存MPSPlan
	 */
	@RequiresPermissions(value={"ppc:mpsPlan:add","ppc:mpsPlan:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(MpsPlan mpsPlan, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, mpsPlan)){
			return form(mpsPlan, model);

		}
		//新增或编辑表单保存
		mpsPlanService.save(mpsPlan);//保存
		addMessage(redirectAttributes, "执行成功");
		return "redirect:"+Global.getAdminPath()+"/ppc/mpsPlan/?repage";
	}

	/**
	 * 保存审核后的MPSPlan
	 */
	@RequiresPermissions("ppc:mpsPlan:audit")
	@RequestMapping(value = "auditSave")
	public String auditSave(MpsPlan mpsPlan, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, mpsPlan)){
			return form(mpsPlan, model);

		}
		//新增或编辑表单保存
		mpsPlanService.save(mpsPlan);//保存
		addMessage(redirectAttributes, "主生产计划审核成功");
		return "redirect:"+Global.getAdminPath()+"/ppc/mpsPlan/auditList/?repage";
	}

	
	/**
	 * 删除MPSPlan
	 */
	@ResponseBody
	@RequiresPermissions("ppc:mpsPlan:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(MpsPlan mpsPlan, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		mpsPlanService.delete(mpsPlan);
		j.setMsg("删除MPSPlan成功");
		return j;
	}
	
	/**
	 * 批量删除MPSPlan
	 */
	@ResponseBody
	@RequiresPermissions("ppc:mpsPlan:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			mpsPlanService.delete(mpsPlanService.get(id));
		}
		j.setMsg("删除MPSPlan成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("ppc:mpsPlan:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(MpsPlan mpsPlan, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "MPSPlan"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<MpsPlan> page = mpsPlanService.findPage(new Page<MpsPlan>(request, response, -1), mpsPlan);
    		new ExportExcel("MPSPlan", MpsPlan.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出MPSPlan记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("ppc:mpsPlan:import")
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
					mpsPlanService.save(mpsPlan);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条MPSPlan记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条MPSPlan记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入MPSPlan失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/ppc/mpsPlan/?repage";
    }
	
	/**
	 * 下载导入MPSPlan数据模板
	 */
	@RequiresPermissions("ppc:mpsPlan:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "MPSPlan数据导入模板.xlsx";
    		List<MpsPlan> list = Lists.newArrayList(); 
    		new ExportExcel("MPSPlan数据", MpsPlan.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/ppc/mpsPlan/?repage";
    }

}