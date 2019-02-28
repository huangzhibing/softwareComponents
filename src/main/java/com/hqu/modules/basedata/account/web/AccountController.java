/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.basedata.account.web;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import com.alibaba.fastjson.JSON;
import com.hqu.modules.basedata.accounttype.entity.AccountType;
import com.hqu.modules.basedata.accounttype.service.AccountTypeService;
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
import com.hqu.modules.basedata.account.entity.Account;
import com.hqu.modules.basedata.account.service.AccountService;

/**
 * 关系企业维护Controller
 * @author M1ngz
 * @version 2018-04-05
 */
@Controller
@RequestMapping(value = "${adminPath}/account/account")
public class AccountController extends BaseController {

	@Autowired
	private AccountService accountService;
	@Autowired
	private AccountTypeService accountTypeService;
	
	@ModelAttribute
	public Account get(@RequestParam(required=false) String id) {
		Account entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = accountService.get(id);
		}
		if (entity == null){
			entity = new Account();
		}
		return entity;
	}
	
	/**
	 * 关系企业列表页面
	 */
	@RequiresPermissions("account:account:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "basedata/account/accountList";
	}
	
		/**
	 * 关系企业列表数据
	 */
	@ResponseBody
	@RequiresPermissions("account:account:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(Account account, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Account> page = accountService.findPage(new Page<Account>(request, response), account); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑关系企业表单页面
	 */
	@RequiresPermissions(value={"account:account:view","account:account:add","account:account:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(Account account, Model model,String accTypeCode,String accTypeName) {
		AccountType accountType = null;
		if(StringUtils.isNotEmpty(accTypeCode)){
			accountType = accountTypeService.get(accTypeCode);
		}
		if(accountType != null){
			account.setAccType(accountType);
			account.setAccTypeNameRu(accountType.getAccTypeName());
		}
		model.addAttribute("account", account);
		if(StringUtils.isBlank(account.getId())){//如果ID是空为添加
			model.addAttribute("isAdd", true);
		}
		return "basedata/account/accountForm";
	}

	@RequiresPermissions("account:account:list")
	@RequestMapping(value = "chkCode")
    @ResponseBody
    public String checkCode(String accCode){
        Integer accountSize = accountService.getCodeNum(accCode);
        if(accountSize > 0){
            logger.debug("已存在");
            return "false";
        } else {
            logger.debug("未存在");
            return "true";
        }
    }

	/**
	 * 保存关系企业
	 */
	@RequiresPermissions(value={"account:account:add","account:account:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(Account account, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, account)){
			return form(account, model,null,null);
		}
		//新增或编辑表单保存
        logger.debug("type:"+ JSON.toJSONString(account.getAccType()));
		if(account.getAccType().getAccTypeCode().length() == 32) {
			AccountType accountType = accountTypeService.get(account.getAccType().getAccTypeCode());
			account.setAccType(accountType);
		}
        accountService.save(account);//保存
		addMessage(redirectAttributes, "保存关系企业成功");
		return "redirect:"+Global.getAdminPath()+"/account/account/?repage";
	}
	
	/**
	 * 删除关系企业
	 */
	@ResponseBody
	@RequiresPermissions("account:account:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(Account account, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		accountService.delete(account);
		j.setMsg("删除关系企业成功");
		return j;
	}
	
	/**
	 * 批量删除关系企业
	 */
	@ResponseBody
	@RequiresPermissions("account:account:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			accountService.delete(accountService.get(id));
		}
		j.setMsg("删除关系企业成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("account:account:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(Account account, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "关系企业"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<Account> page = accountService.findPage(new Page<Account>(request, response, -1), account);
    		new ExportExcel("关系企业", Account.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出关系企业记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("account:account:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<Account> list = ei.getDataList(Account.class);
			for (Account account : list){
				try{
					accountService.saveTry(account);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条关系企业记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条关系企业记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入关系企业失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/account/account/?repage";
    }
	
	/**
	 * 下载导入关系企业数据模板
	 */
	@RequiresPermissions("account:account:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "关系企业数据导入模板.xlsx";
    		List<Account> list = Lists.newArrayList(); 
    		new ExportExcel("关系企业数据", Account.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/account/account/?repage";
    }

}