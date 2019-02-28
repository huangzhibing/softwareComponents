/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.costmanage.billingrule.web;

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
import com.hqu.modules.costmanage.billingrule.entity.CosBillRule;
import com.hqu.modules.costmanage.billingrule.service.CosBillRuleService;

/**
 * 制单规则Controller
 * @author ccr
 * @version 2018-09-01
 */
@Controller
@RequestMapping(value = "${adminPath}/billingrule/cosBillRule")
public class CosBillRuleController extends BaseController {

	@Autowired
	private CosBillRuleService cosBillRuleService;
	
	@ModelAttribute
	public CosBillRule get(@RequestParam(required=false) String id) {
		CosBillRule entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = cosBillRuleService.get(id);
		}
		if (entity == null){
			entity = new CosBillRule();
		}
		return entity;
	}
	
	/**
	 * 制单规则列表页面
	 */
	@RequiresPermissions("billingrule:cosBillRule:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "costmanage/billingrule/cosBillRuleList";
	}
	
		/**
	 * 制单规则列表数据
	 */
	@ResponseBody
	@RequiresPermissions("billingrule:cosBillRule:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(CosBillRule cosBillRule, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<CosBillRule> page = cosBillRuleService.findPage(new Page<CosBillRule>(request, response), cosBillRule); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑制单规则表单页面
	 */
	@RequiresPermissions(value={"billingrule:cosBillRule:view","billingrule:cosBillRule:add","billingrule:cosBillRule:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(CosBillRule cosBillRule, Model model) {
		model.addAttribute("cosBillRule", cosBillRule);
		if(StringUtils.isBlank(cosBillRule.getId())){//如果ID是空为添加
			model.addAttribute("isAdd", true);
			String ruleId=cosBillRuleService.getMaxRuleId();
		if(ruleId==null)ruleId="01";
		else{
			int maxnum = Integer.parseInt(ruleId) + 1;
			ruleId = String.format("%02d",maxnum);
		}
		 cosBillRule.setRuleId(ruleId);
		}
		return "costmanage/billingrule/cosBillRuleForm";
	}

	/**
	 * 保存制单规则
	 */
	@RequiresPermissions(value={"billingrule:cosBillRule:add","billingrule:cosBillRule:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(CosBillRule cosBillRule, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, cosBillRule)){
			return form(cosBillRule, model);
		}
		//新增或编辑表单保存
		cosBillRuleService.save(cosBillRule);//保存
		addMessage(redirectAttributes, "保存制单规则成功");
		return "redirect:"+Global.getAdminPath()+"/billingrule/cosBillRule/?repage";
	}
	
	/**
	 * 重复原始单据类型提示
	 */
	@ResponseBody
	@RequestMapping(value = "selectOne")
	public String selectOne(@RequestParam(required=false) String corType, @RequestParam(required=false) String id) {
		CosBillRule temp = cosBillRuleService.get(corType);;
		//新建时
		if(id == null){
			if(temp != null){
				return "0";
			}else{
				return "1";
			}
		}
		//修改时
		else{
			if(temp != null){
				if(temp.getId() != id){
					return "0";
				}else{
					return "1";
				}
			}else{
				return "1";
			}
		}
	}
	
	/**
	 * 删除制单规则
	 */
	@ResponseBody
	@RequiresPermissions("billingrule:cosBillRule:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(CosBillRule cosBillRule, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		cosBillRuleService.delete(cosBillRule);
		j.setMsg("删除制单规则成功");
		return j;
	}
	
	/**
	 * 批量删除制单规则
	 */
	@ResponseBody
	@RequiresPermissions("billingrule:cosBillRule:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			cosBillRuleService.delete(cosBillRuleService.get(id));
		}
		j.setMsg("删除制单规则成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("billingrule:cosBillRule:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(CosBillRule cosBillRule, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "制单规则"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<CosBillRule> page = cosBillRuleService.findPage(new Page<CosBillRule>(request, response, -1), cosBillRule);
    		new ExportExcel("制单规则", CosBillRule.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出制单规则记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("billingrule:cosBillRule:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<CosBillRule> list = ei.getDataList(CosBillRule.class);
			for (CosBillRule cosBillRule : list){
				try{
					cosBillRuleService.save(cosBillRule);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条制单规则记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条制单规则记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入制单规则失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/billingrule/cosBillRule/?repage";
    }
	
	/**
	 * 下载导入制单规则数据模板
	 */
	@RequiresPermissions("billingrule:cosBillRule:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "制单规则数据导入模板.xlsx";
    		List<CosBillRule> list = Lists.newArrayList(); 
    		new ExportExcel("制单规则数据", CosBillRule.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/billingrule/cosBillRule/?repage";
    }

}