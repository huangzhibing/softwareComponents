/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.purchasemanage.purplan.web;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import com.hqu.modules.purchasemanage.purplan.entity.PurPlanDetail;
import com.hqu.modules.purchasemanage.purplan.entity.PurPlanDetailCheck;
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
import com.hqu.modules.purchasemanage.purplan.entity.PurPlanMainCheck;
import com.hqu.modules.purchasemanage.purplan.service.PurPlanMainCheckService;

/**
 * 采购计划审核Controller
 * @author ckw
 * @version 2018-05-03
 */
@Controller
@RequestMapping(value = "${adminPath}/purplan/purPlanMainCheck")
public class PurPlanMainCheckController extends BaseController {

	@Autowired
	private PurPlanMainCheckService purPlanMainCheckService;

//	编辑/新建时获取entity
	@ModelAttribute
	public PurPlanMainCheck get(@RequestParam(required=false) String id) {
		PurPlanMainCheck entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = purPlanMainCheckService.get(id);
		}
		if (entity == null){
			entity = new PurPlanMainCheck();
		}
		return entity;
	}
	
	/**
	 * 采购计划审核列表页面
	 */
	@RequiresPermissions("purplan:purPlanMainCheck:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "purchasemanage/purplan/purPlanMainCheckList";
	}
	
		/**
	 * 采购计划审核列表数据
	 */
	@ResponseBody
	@RequiresPermissions("purplan:purPlanMainCheck:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(PurPlanMainCheck purPlanMainCheck, HttpServletRequest request, HttpServletResponse response, Model model) {

    //在findMyPage中筛选符合条件的记录
		Page<PurPlanMainCheck> page = purPlanMainCheckService.findMyPage(new Page<PurPlanMainCheck>(request, response), purPlanMainCheck);
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑采购计划审核表单页面
	 */
	@RequiresPermissions(value={"purplan:purPlanMainCheck:view","purplan:purPlanMainCheck:add","purplan:purPlanMainCheck:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(PurPlanMainCheck purPlanMainCheck, Model model) {

		model.addAttribute("purPlanMainCheck", purPlanMainCheck);
		if(StringUtils.isBlank(purPlanMainCheck.getId())){//如果ID是空为添加
			model.addAttribute("isAdd", true);
		}
		return "purchasemanage/purplan/purPlanMainCheckForm";
	}

	/**
	 * 保存采购计划审核
	 */
	@RequiresPermissions(value={"purplan:purPlanMainCheck:add","purplan:purPlanMainCheck:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(PurPlanMainCheck purPlanMainCheck, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, purPlanMainCheck)){
			return form(purPlanMainCheck, model);
		}
		//计算采购计划总金额
		List<PurPlanDetailCheck> details=purPlanMainCheck.getPurPlanDetailCheckList();
		double sum=0;
		for(int i = 0;i<details.size();i++) {
			if(details.get(i).getPlanSum() != null && details.get(i).getPlanSum() > 0&&"0".equals(details.get(i).getDelFlag())) {
				sum+=details.get(i).getPlanSum();
			}
		}
		purPlanMainCheck.setPlanPriceSum(sum);
		//新增或编辑表单保存
		purPlanMainCheckService.save(purPlanMainCheck);//保存
		addMessage(redirectAttributes, "采购计划审核成功");
		return "redirect:"+Global.getAdminPath()+"/purplan/purPlanMainCheck/?repage";
	}
	
	/**
	 * 删除采购计划审核
	 */
	@ResponseBody
	@RequiresPermissions("purplan:purPlanMainCheck:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(PurPlanMainCheck purPlanMainCheck, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		purPlanMainCheckService.delete(purPlanMainCheck);
		j.setMsg("删除采购计划审核成功");
		return j;
	}
	
	/**
	 * 批量删除采购计划审核
	 */
	@ResponseBody
	@RequiresPermissions("purplan:purPlanMainCheck:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			purPlanMainCheckService.delete(purPlanMainCheckService.get(id));
		}
		j.setMsg("删除采购计划审核成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("purplan:purPlanMainCheck:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(PurPlanMainCheck purPlanMainCheck, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "采购计划审核"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<PurPlanMainCheck> page = purPlanMainCheckService.findPage(new Page<PurPlanMainCheck>(request, response, -1), purPlanMainCheck);
    		new ExportExcel("采购计划审核", PurPlanMainCheck.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出采购计划审核记录失败！失败信息："+e.getMessage());
		}
			return j;
    }
    
    @ResponseBody
    @RequestMapping(value = "detail")
	public PurPlanMainCheck detail(String id) {
		return purPlanMainCheckService.get(id);
	}
	

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("purplan:purPlanMainCheck:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<PurPlanMainCheck> list = ei.getDataList(PurPlanMainCheck.class);
			for (PurPlanMainCheck purPlanMainCheck : list){
				try{
					purPlanMainCheckService.save(purPlanMainCheck);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条采购计划审核记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条采购计划审核记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入采购计划审核失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/purplan/purPlanMainCheck/?repage";
    }
	
	/**
	 * 下载导入采购计划审核数据模板
	 */
	@RequiresPermissions("purplan:purPlanMainCheck:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "采购计划审核数据导入模板.xlsx";
    		List<PurPlanMainCheck> list = Lists.newArrayList(); 
    		new ExportExcel("采购计划审核数据", PurPlanMainCheck.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/purplan/purPlanMainCheck/?repage";
    }
}