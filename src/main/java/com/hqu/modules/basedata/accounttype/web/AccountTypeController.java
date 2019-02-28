/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.basedata.accounttype.web;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import com.google.common.collect.Maps;
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
import com.hqu.modules.basedata.accounttype.entity.AccountType;
import com.hqu.modules.basedata.accounttype.service.AccountTypeService;

/**
 * 企业类型表单Controller
 * @author 黄志兵
 * @version 2018-04-05
 */
@Controller
@RequestMapping(value = "${adminPath}/accounttype/accountType")
public class AccountTypeController extends BaseController {

	@Autowired
	private AccountTypeService accountTypeService;
	
	@ModelAttribute
	public AccountType get(@RequestParam(required=false) String id) {
		AccountType entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = accountTypeService.get(id);
		}
		if (entity == null){
			entity = new AccountType();
		}
		return entity;
	}
	
	/**
	 * 企业类型表单列表页面
	 */
	@RequiresPermissions("accounttype:accountType:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "basedata/accounttype/accountTypeList";
	}
	
		/**
	 * 企业类型表单列表数据
	 */
	@ResponseBody
	@RequiresPermissions("accounttype:accountType:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(AccountType accountType, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<AccountType> page = accountTypeService.findPage(new Page<AccountType>(request, response), accountType); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑企业类型表单表单页面
	 */
	@RequiresPermissions(value={"accounttype:accountType:view","accounttype:accountType:add","accounttype:accountType:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(AccountType accountType, Model model) {
		model.addAttribute("accountType", accountType);
		if(StringUtils.isBlank(accountType.getId())){//如果ID是空为添加
			model.addAttribute("isAdd", true);
			//设置typecode从0开始自增长并为2位流水压入表单中
			String typecode = accountTypeService.getMaxAccTypeCode();
			if(typecode == null) {
				typecode = "00";
			}
			else {
				int maxnum = Integer.parseInt(typecode) + 1;
				typecode = String.format("%02d",maxnum);
			}

			accountType.setAccTypeCode(typecode);
		}
		return "basedata/accounttype/accountTypeForm";
	}

	/**
	 * 保存企业类型表单
	 */
	@RequiresPermissions(value={"accounttype:accountType:add","accounttype:accountType:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(AccountType accountType, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, accountType)){
			return form(accountType, model);
		}
		//新增或编辑表单保存
		accountTypeService.save(accountType);//保存
		addMessage(redirectAttributes, "保存企业类型表单成功");
		return "redirect:"+Global.getAdminPath()+"/accounttype/accountType/?repage";
	}
	
	/**
	 * 删除企业类型表单
	 */
	@ResponseBody
	@RequiresPermissions("accounttype:accountType:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(AccountType accountType, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		accountTypeService.delete(accountType);
		j.setMsg("删除企业类型表单成功");
		return j;
	}
	
	/**
	 * 批量删除企业类型表单
	 */
	@ResponseBody
	@RequiresPermissions("accounttype:accountType:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			accountTypeService.delete(accountTypeService.get(id));
		}
		j.setMsg("删除企业类型表单成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("accounttype:accountType:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(AccountType accountType, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "企业类型表单"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<AccountType> page = accountTypeService.findPage(new Page<AccountType>(request, response, -1), accountType);
    		new ExportExcel("企业类型表单", AccountType.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出企业类型表单记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("accounttype:accountType:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<AccountType> list = ei.getDataList(AccountType.class);
			for (AccountType accountType : list){
				try{
					accountTypeService.saveTry(accountType);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条企业类型表单记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条企业类型表单记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入企业类型表单失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/accounttype/accountType/?repage";
    }
	
	/**
	 * 下载导入企业类型表单数据模板
	 */
	@RequiresPermissions("accounttype:accountType:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "企业类型表单数据导入模板.xlsx";
    		List<AccountType> list = Lists.newArrayList(); 
    		new ExportExcel("企业类型表单数据", AccountType.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/accounttype/accountType/?repage";
    }

	@RequiresPermissions("user")
	@ResponseBody
	@RequestMapping(value = "treeData")
	public Map<String, Object> treeData(@RequestParam(required=false) String extId, HttpServletResponse response) {
		Map<String,Object> result = Maps.newHashMap();
		List<Map<String, Object>> mapList = Lists.newArrayList();
		List<AccountType> list = accountTypeService.findList(new AccountType());
		result.put("id",0);
		result.put("text","关系企业类型");
		Map<String,Object> item = null;
		for (int i=0; i<list.size(); i++){
			AccountType e = list.get(i);
			item = Maps.newHashMap();
			item.put("id",e.getId());
			item.put("text",e.getAccTypeCode()+e.getAccTypeName());
			mapList.add(item);
		}
		result.put("children",mapList);
		return result;
	}

}