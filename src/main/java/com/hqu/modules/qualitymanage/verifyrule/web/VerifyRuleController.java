/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.qualitymanage.verifyrule.web;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import com.jeeplus.modules.sys.entity.Role;
import com.jeeplus.modules.sys.service.SystemService;
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
import com.hqu.modules.qualitymanage.verifyrule.entity.VerifyRule;
import com.hqu.modules.qualitymanage.verifyrule.service.VerifyRuleService;

/**
 * 验证规则表Controller
 * @author 石豪迈
 * @version 2018-05-16
 */
@Controller
@RequestMapping(value = "${adminPath}/verifyrule/verifyRule")
public class VerifyRuleController extends BaseController {

	@Autowired
	private VerifyRuleService verifyRuleService;

	//获取岗位名称
	@Autowired
	private SystemService systemService;
	
	@ModelAttribute
	public VerifyRule get(@RequestParam(required=false) String id) {
		VerifyRule entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = verifyRuleService.get(id);
		}
		if (entity == null){
			entity = new VerifyRule();
		}
		return entity;
	}
	
	/**
	 * 保存成功列表页面
	 */
	@RequiresPermissions("verifyrule:verifyRule:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "qualitymanage/verifyrule/verifyRuleList";
	}
	
		/**
	 * 保存成功列表数据
	 */
	@ResponseBody
	@RequiresPermissions("verifyrule:verifyRule:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(VerifyRule verifyRule, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<VerifyRule> page = verifyRuleService.findPage(new Page<VerifyRule>(request, response), verifyRule); 
		return getBootstrapData(page);
	}


	/**
	 * 保存成功列表数据
	 */
	@ResponseBody
	@RequestMapping(value = "sysRole")
	public Map<String, Object> getSysRole(Role role) {
		//根据传入的参数查询岗位名称
		List<Role> list = systemService.findRole(role);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("rows", list);
		map.put("total", list.size());
		return map;
	}

	/**
	 * 查看，增加，编辑保存成功表单页面
	 */
	@RequiresPermissions(value={"verifyrule:verifyRule:view","verifyrule:verifyRule:add","verifyrule:verifyRule:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(VerifyRule verifyRule, Model model) {
		model.addAttribute("verifyRule", verifyRule);
		if(StringUtils.isBlank(verifyRule.getId())){//如果ID是空为添加
			model.addAttribute("isAdd", true);
		}
		return "qualitymanage/verifyrule/verifyRuleForm";
	}

	/**
	 * 保存保存成功
	 */
	@RequiresPermissions(value={"verifyrule:verifyRule:add","verifyrule:verifyRule:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(VerifyRule verifyRule, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, verifyRule)){
			return form(verifyRule, model);
		}
		//新增或编辑表单保存
		verifyRuleService.save(verifyRule);//保存
		addMessage(redirectAttributes, "保存保存成功成功");
		return "redirect:"+Global.getAdminPath()+"/verifyrule/verifyRule/?repage";
	}
	
	/**
	 * 删除保存成功
	 */
	@ResponseBody
	@RequiresPermissions("verifyrule:verifyRule:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(VerifyRule verifyRule, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		verifyRuleService.delete(verifyRule);
		j.setMsg("删除保存成功成功");
		return j;
	}
	
	/**
	 * 批量删除保存成功
	 */
	@ResponseBody
	@RequiresPermissions("verifyrule:verifyRule:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			verifyRuleService.delete(verifyRuleService.get(id));
		}
		j.setMsg("删除保存成功成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("verifyrule:verifyRule:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(VerifyRule verifyRule, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "保存成功"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<VerifyRule> page = verifyRuleService.findPage(new Page<VerifyRule>(request, response, -1), verifyRule);
    		new ExportExcel("保存成功", VerifyRule.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出保存成功记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("verifyrule:verifyRule:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<VerifyRule> list = ei.getDataList(VerifyRule.class);
			for (VerifyRule verifyRule : list){
				try{
					verifyRuleService.save(verifyRule);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条保存成功记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条保存成功记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入保存成功失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/verifyrule/verifyRule/?repage";
    }
	
	/**
	 * 下载导入保存成功数据模板
	 */
	@RequiresPermissions("verifyrule:verifyRule:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "保存成功数据导入模板.xlsx";
    		List<VerifyRule> list = Lists.newArrayList(); 
    		new ExportExcel("保存成功数据", VerifyRule.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/verifyrule/verifyRule/?repage";
    }

}