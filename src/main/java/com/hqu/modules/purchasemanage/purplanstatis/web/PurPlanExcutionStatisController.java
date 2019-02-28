/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.purchasemanage.purplanstatis.web;

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
import com.hqu.modules.purchasemanage.purplanstatis.entity.PurPlanExcutionStatis;
import com.hqu.modules.purchasemanage.purplanstatis.service.PurPlanExcutionStatisService;

/**
 * 采购计划执行情况表Controller
 * @author yxb
 * @version 2018-08-19
 */
@Controller
@RequestMapping(value = "${adminPath}/purplanstatis/purPlanExcutionStatis")
public class PurPlanExcutionStatisController extends BaseController {

	@Autowired
	private PurPlanExcutionStatisService purPlanExcutionStatisService;
	
	@ModelAttribute
	public PurPlanExcutionStatis get(@RequestParam(required=false) String id) {
		PurPlanExcutionStatis entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = purPlanExcutionStatisService.get(id);
		}
		if (entity == null){
			entity = new PurPlanExcutionStatis();
		}
		return entity;
	}
	
	/**
	 * 采购计划执行情况表列表页面
	 */
	@RequiresPermissions("purplanstatis:purPlanExcutionStatis:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "purchasemanage/purplanstatis/purPlanExcutionStatisList";
	}
	
		/**
	 * 采购计划执行情况表列表数据
	 */
	@ResponseBody
	@RequiresPermissions("purplanstatis:purPlanExcutionStatis:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(PurPlanExcutionStatis purPlanExcutionStatis, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<PurPlanExcutionStatis> page = purPlanExcutionStatisService.findPage(new Page<PurPlanExcutionStatis>(request, response), purPlanExcutionStatis); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑采购计划执行情况表表单页面
	 */
	@RequiresPermissions(value={"purplanstatis:purPlanExcutionStatis:view","purplanstatis:purPlanExcutionStatis:add","purplanstatis:purPlanExcutionStatis:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(PurPlanExcutionStatis purPlanExcutionStatis, Model model) {
		model.addAttribute("purPlanExcutionStatis", purPlanExcutionStatis);
		if(StringUtils.isBlank(purPlanExcutionStatis.getId())){//如果ID是空为添加
			model.addAttribute("isAdd", true);
		}
		return "purchasemanage/purplanstatis/purPlanExcutionStatisForm";
	}

	/**
	 * 保存采购计划执行情况表
	 */
	@RequiresPermissions(value={"purplanstatis:purPlanExcutionStatis:add","purplanstatis:purPlanExcutionStatis:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(PurPlanExcutionStatis purPlanExcutionStatis, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, purPlanExcutionStatis)){
			return form(purPlanExcutionStatis, model);
		}
		//新增或编辑表单保存
		purPlanExcutionStatisService.save(purPlanExcutionStatis);//保存
		addMessage(redirectAttributes, "保存采购计划执行情况表成功");
		return "redirect:"+Global.getAdminPath()+"/purplanstatis/purPlanExcutionStatis/?repage";
	}
	
	/**
	 * 删除采购计划执行情况表
	 */
	@ResponseBody
	@RequiresPermissions("purplanstatis:purPlanExcutionStatis:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(PurPlanExcutionStatis purPlanExcutionStatis, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		purPlanExcutionStatisService.delete(purPlanExcutionStatis);
		j.setMsg("删除采购计划执行情况表成功");
		return j;
	}
	
	/**
	 * 批量删除采购计划执行情况表
	 */
	@ResponseBody
	@RequiresPermissions("purplanstatis:purPlanExcutionStatis:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			purPlanExcutionStatisService.delete(purPlanExcutionStatisService.get(id));
		}
		j.setMsg("删除采购计划执行情况表成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("purplanstatis:purPlanExcutionStatis:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(PurPlanExcutionStatis purPlanExcutionStatis, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "采购计划执行情况表"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<PurPlanExcutionStatis> page = purPlanExcutionStatisService.findPage(new Page<PurPlanExcutionStatis>(request, response, -1), purPlanExcutionStatis);
    		new ExportExcel("采购计划执行情况表", PurPlanExcutionStatis.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出采购计划执行情况表记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("purplanstatis:purPlanExcutionStatis:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<PurPlanExcutionStatis> list = ei.getDataList(PurPlanExcutionStatis.class);
			for (PurPlanExcutionStatis purPlanExcutionStatis : list){
				try{
					purPlanExcutionStatisService.save(purPlanExcutionStatis);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条采购计划执行情况表记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条采购计划执行情况表记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入采购计划执行情况表失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/purplanstatis/purPlanExcutionStatis/?repage";
    }
	
	/**
	 * 下载导入采购计划执行情况表数据模板
	 */
	@RequiresPermissions("purplanstatis:purPlanExcutionStatis:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "采购计划执行情况表数据导入模板.xlsx";
    		List<PurPlanExcutionStatis> list = Lists.newArrayList(); 
    		new ExportExcel("采购计划执行情况表数据", PurPlanExcutionStatis.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/purplanstatis/purPlanExcutionStatis/?repage";
    }

}