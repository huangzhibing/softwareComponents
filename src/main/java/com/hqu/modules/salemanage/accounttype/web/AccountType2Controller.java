/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.salemanage.accounttype.web;

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
import com.hqu.modules.salemanage.accounttype.entity.AccountType2;
import com.hqu.modules.salemanage.accounttype.service.AccountType2Service;

/**
 * 客户类型定义Controller
 * @author hzm
 * @version 2018-05-05
 */
@Controller
@RequestMapping(value = "${adminPath}/accounttype/accountType2")
public class AccountType2Controller extends BaseController {

	@Autowired
	private AccountType2Service accountType2Service;
	
	@ModelAttribute
	public AccountType2 get(@RequestParam(required=false) String id) {
		AccountType2 entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = accountType2Service.get(id);
		}
		if (entity == null){
			entity = new AccountType2();
		}
		return entity;
	}
	
	/**
	 * 客户类型定义列表页面
	 */
	@RequiresPermissions("accounttype:accountType2:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "salemanage/accounttype/accountType2List";
	}
	
		/**
	 * 客户类型定义列表数据
	 */
	@ResponseBody
	@RequiresPermissions("accounttype:accountType2:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(AccountType2 accountType2, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<AccountType2> page = accountType2Service.findPage(new Page<AccountType2>(request, response), accountType2); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑客户类型定义表单页面
	 */
	@RequiresPermissions(value={"accounttype:accountType2:view","accounttype:accountType2:add","accounttype:accountType2:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(AccountType2 accountType2, Model model) {
		model.addAttribute("accountType2", accountType2);
		String maxCode;
		if(StringUtils.isBlank(accountType2.getId())){//如果ID是空为添加
			model.addAttribute("isAdd", true);
			if(accountType2Service.selectMax()!=null) {
				maxCode=accountType2Service.selectMax();
				int numCode=Integer.parseInt(maxCode);
				numCode++;
				maxCode=String.format("%02d", numCode);
			}else {
				maxCode="00";
			}
			accountType2.setAccTypeCode(maxCode);
		}
		return "salemanage/accounttype/accountType2Form";
	}

	/**
	 * 保存客户类型定义
	 */
	@RequiresPermissions(value={"accounttype:accountType2:add","accounttype:accountType2:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(AccountType2 accountType2, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, accountType2)){
			return form(accountType2, model);
		}
		//新增或编辑表单保存
		accountType2Service.save(accountType2);//保存
		addMessage(redirectAttributes, "保存客户类型定义成功");
		return "redirect:"+Global.getAdminPath()+"/accounttype/accountType2/?repage";
	}
	
	/**
	 * 删除客户类型定义
	 */
	@ResponseBody
	@RequiresPermissions("accounttype:accountType2:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(AccountType2 accountType2, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		accountType2Service.delete(accountType2);
		j.setMsg("删除客户类型定义成功");
		return j;
	}
	
	/**
	 * 批量删除客户类型定义
	 */
	@ResponseBody
	@RequiresPermissions("accounttype:accountType2:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			accountType2Service.delete(accountType2Service.get(id));
		}
		j.setMsg("删除客户类型定义成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("accounttype:accountType2:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(AccountType2 accountType2, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "客户类型定义"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<AccountType2> page = accountType2Service.findPage(new Page<AccountType2>(request, response, -1), accountType2);
    		new ExportExcel("客户类型定义", AccountType2.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出客户类型定义记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("accounttype:accountType2:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<AccountType2> list = ei.getDataList(AccountType2.class);
			for (AccountType2 accountType2 : list){
				try{
					accountType2Service.saveExc(accountType2);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条客户类型定义记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条客户类型定义记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入客户类型定义失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/accounttype/accountType2/?repage";
    }
	
	/**
	 * 下载导入客户类型定义数据模板
	 */
	@RequiresPermissions("accounttype:accountType2:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "客户类型定义数据导入模板.xlsx";
    		List<AccountType2> list = Lists.newArrayList(); 
    		new ExportExcel("客户类型定义数据", AccountType2.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/accounttype/accountType2/?repage";
    }

}