/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.inventorymanage.invaccount.web;

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
import com.hqu.modules.inventorymanage.invaccount.entity.InvAccount;
import com.hqu.modules.inventorymanage.invaccount.service.InvAccountService;

/**
 * 库存账Controller
 * @author M1ngz
 * @version 2018-04-22
 */
@Controller
@RequestMapping(value = "${adminPath}/invaccount/invAccount")
public class InvAccountController extends BaseController {

	@Autowired
	private InvAccountService invAccountService;
	
	@ModelAttribute
	public InvAccount get(@RequestParam(required=false) String id) {
		InvAccount entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = invAccountService.get(id);
		}
		if (entity == null){
			entity = new InvAccount();
		}
		return entity;
	}
	
	/**
	 * 库存帐列表页面
	 */
	@RequiresPermissions("invaccount:invAccount:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "inventorymanage/invaccount/invAccountList";
	}
	
		/**
	 * 库存帐列表数据
	 */
	@ResponseBody
//	@RequiresPermissions("invaccount:invAccount:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(InvAccount invAccount, HttpServletRequest request, HttpServletResponse response, Model model) {
		String year=request.getParameter("amp;year");
		String period=request.getParameter("amp;period");
		if(!StringUtils.isEmpty(year)&&!StringUtils.isEmpty(period)) {
			invAccount.setYear(year);
			invAccount.setPeriod(period);
		}
		Page<InvAccount> page = invAccountService.findPage(new Page<InvAccount>(request, response), invAccount); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑库存帐表单页面
	 */
	@RequiresPermissions(value={"invaccount:invAccount:view","invaccount:invAccount:add","invaccount:invAccount:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(InvAccount invAccount, Model model) {
		model.addAttribute("invAccount", invAccount);
		if(StringUtils.isBlank(invAccount.getId())){//如果ID是空为添加
			model.addAttribute("isAdd", true);
		}
		return "inventorymanage/invaccount/invAccountForm";
	}

	/**
	 * 保存库存帐
	 */
	@RequiresPermissions(value={"invaccount:invAccount:add","invaccount:invAccount:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(InvAccount invAccount, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, invAccount)){
			return form(invAccount, model);
		}
		//新增或编辑表单保存
		invAccountService.save(invAccount);//保存
		addMessage(redirectAttributes, "保存库存帐成功");
		return "redirect:"+Global.getAdminPath()+"/invaccount/invAccount/?repage";
	}
	
	/**
	 * 删除库存帐
	 */
	@ResponseBody
	@RequiresPermissions("invaccount:invAccount:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(InvAccount invAccount, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		invAccountService.delete(invAccount);
		j.setMsg("删除库存帐成功");
		return j;
	}
	
	/**
	 * 批量删除库存帐
	 */
	@ResponseBody
	@RequiresPermissions("invaccount:invAccount:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			invAccountService.delete(invAccountService.get(id));
		}
		j.setMsg("删除库存帐成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("invaccount:invAccount:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(InvAccount invAccount, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "库存帐"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<InvAccount> page = invAccountService.findPage(new Page<InvAccount>(request, response, -1), invAccount);
    		new ExportExcel("库存帐", InvAccount.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出库存帐记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("invaccount:invAccount:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<InvAccount> list = ei.getDataList(InvAccount.class);
			for (InvAccount invAccount : list){
				try{
					invAccountService.save(invAccount);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条库存帐记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条库存帐记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入库存帐失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/invaccount/invAccount/?repage";
    }
	
	/**
	 * 下载导入库存帐数据模板
	 */
	@RequiresPermissions("invaccount:invAccount:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "库存帐数据导入模板.xlsx";
    		List<InvAccount> list = Lists.newArrayList(); 
    		new ExportExcel("库存帐数据", InvAccount.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/invaccount/invAccount/?repage";
    }

}