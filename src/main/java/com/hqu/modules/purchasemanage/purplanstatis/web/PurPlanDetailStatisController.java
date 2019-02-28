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
import com.hqu.modules.purchasemanage.purplanstatis.entity.PurPlanDetailStatis;
import com.hqu.modules.purchasemanage.purplanstatis.service.PurPlanDetailStatisService;

/**
 * 采购计划明细表Controller
 * @author yxb
 * @version 2018-08-15
 */
@Controller
@RequestMapping(value = "${adminPath}/purplanstatis/purPlanDetailStatis")
public class PurPlanDetailStatisController extends BaseController {

	@Autowired
	private PurPlanDetailStatisService purPlanDetailStatisService;
	
	@ModelAttribute
	public PurPlanDetailStatis get(@RequestParam(required=false) String id) {
		PurPlanDetailStatis entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = purPlanDetailStatisService.get(id);
		}
		if (entity == null){
			entity = new PurPlanDetailStatis();
		}
		return entity;
	}
	
	/**
	 * 采购计划明细表列表页面
	 */
	@RequiresPermissions("purplanstatis:purPlanDetailStatis:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "purchasemanage/purplanstatis/purPlanDetailStatisList";
	}
	
		/**
	 * 采购计划明细表列表数据
	 */
	@ResponseBody
	@RequiresPermissions("purplanstatis:purPlanDetailStatis:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(PurPlanDetailStatis purPlanDetailStatis, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<PurPlanDetailStatis> page = purPlanDetailStatisService.findPage(new Page<PurPlanDetailStatis>(request, response), purPlanDetailStatis); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑采购计划明细表表单页面
	 */
	@RequiresPermissions(value={"purplanstatis:purPlanDetailStatis:view","purplanstatis:purPlanDetailStatis:add","purplanstatis:purPlanDetailStatis:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(PurPlanDetailStatis purPlanDetailStatis, Model model) {
		model.addAttribute("purPlanDetailStatis", purPlanDetailStatis);
		if(StringUtils.isBlank(purPlanDetailStatis.getId())){//如果ID是空为添加
			model.addAttribute("isAdd", true);
		}
		return "purchasemanage/purplanstatis/purPlanDetailStatisForm";
	}

	/**
	 * 保存采购计划明细表
	 */
	@RequiresPermissions(value={"purplanstatis:purPlanDetailStatis:add","purplanstatis:purPlanDetailStatis:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(PurPlanDetailStatis purPlanDetailStatis, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, purPlanDetailStatis)){
			return form(purPlanDetailStatis, model);
		}
		//新增或编辑表单保存
		purPlanDetailStatisService.save(purPlanDetailStatis);//保存
		addMessage(redirectAttributes, "保存采购计划明细表成功");
		return "redirect:"+Global.getAdminPath()+"/purplanstatis/purPlanDetailStatis/?repage";
	}
	
	/**
	 * 删除采购计划明细表
	 */
	@ResponseBody
	@RequiresPermissions("purplanstatis:purPlanDetailStatis:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(PurPlanDetailStatis purPlanDetailStatis, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		purPlanDetailStatisService.delete(purPlanDetailStatis);
		j.setMsg("删除采购计划明细表成功");
		return j;
	}
	
	/**
	 * 批量删除采购计划明细表
	 */
	@ResponseBody
	@RequiresPermissions("purplanstatis:purPlanDetailStatis:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			purPlanDetailStatisService.delete(purPlanDetailStatisService.get(id));
		}
		j.setMsg("删除采购计划明细表成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("purplanstatis:purPlanDetailStatis:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(PurPlanDetailStatis purPlanDetailStatis, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "采购计划明细表"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<PurPlanDetailStatis> page = purPlanDetailStatisService.findPage(new Page<PurPlanDetailStatis>(request, response, -1), purPlanDetailStatis);
    		new ExportExcel("采购计划明细表", PurPlanDetailStatis.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出采购计划明细表记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("purplanstatis:purPlanDetailStatis:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<PurPlanDetailStatis> list = ei.getDataList(PurPlanDetailStatis.class);
			for (PurPlanDetailStatis purPlanDetailStatis : list){
				try{
					purPlanDetailStatisService.save(purPlanDetailStatis);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条采购计划明细表记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条采购计划明细表记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入采购计划明细表失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/purplanstatis/purPlanDetailStatis/?repage";
    }
	
	/**
	 * 下载导入采购计划明细表数据模板
	 */
	@RequiresPermissions("purplanstatis:purPlanDetailStatis:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "采购计划明细表数据导入模板.xlsx";
    		List<PurPlanDetailStatis> list = Lists.newArrayList(); 
    		new ExportExcel("采购计划明细表数据", PurPlanDetailStatis.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/purplanstatis/purPlanDetailStatis/?repage";
    }

}