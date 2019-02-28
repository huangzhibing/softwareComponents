/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.inventorymanage.invaccountcode.web;

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
import com.hqu.modules.inventorymanage.invaccountcode.entity.InvAccountCode;
import com.hqu.modules.inventorymanage.invaccountcode.service.InvAccountCodeService;

/**
 * 库存帐扫码Controller
 * @author M1ngz
 * @version 2018-06-03
 */
@Controller
@RequestMapping(value = "${adminPath}/invaccountcode/invAccountCode")
public class InvAccountCodeController extends BaseController {

	@Autowired
	private InvAccountCodeService invAccountCodeService;
	
	@ModelAttribute
	public InvAccountCode get(@RequestParam(required=false) String id) {
		InvAccountCode entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = invAccountCodeService.get(id);
		}
		if (entity == null){
			entity = new InvAccountCode();
		}
		return entity;
	}
	
	/**
	 * 库存帐扫码列表页面
	 */
	@RequiresPermissions("invaccountcode:invAccountCode:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "inventorymanage/invaccountcode/invAccountCodeList";
	}
	
		/**
	 * 库存帐扫码列表数据
	 */
	@ResponseBody
	@RequiresPermissions("invaccountcode:invAccountCode:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(InvAccountCode invAccountCode, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<InvAccountCode> page = invAccountCodeService.findPage(new Page<InvAccountCode>(request, response), invAccountCode); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑库存帐扫码表单页面
	 */
	@RequiresPermissions(value={"invaccountcode:invAccountCode:view","invaccountcode:invAccountCode:add","invaccountcode:invAccountCode:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(InvAccountCode invAccountCode, Model model) {
		model.addAttribute("invAccountCode", invAccountCode);
		if(StringUtils.isBlank(invAccountCode.getId())){//如果ID是空为添加
			model.addAttribute("isAdd", true);
		}
		return "inventorymanage/invaccountcode/invAccountCodeForm";
	}

	/**
	 * 保存库存帐扫码
	 */
	@RequiresPermissions(value={"invaccountcode:invAccountCode:add","invaccountcode:invAccountCode:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(InvAccountCode invAccountCode, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, invAccountCode)){
			return form(invAccountCode, model);
		}
		//新增或编辑表单保存
		invAccountCodeService.save(invAccountCode);//保存
		addMessage(redirectAttributes, "保存库存帐扫码成功");
		return "redirect:"+Global.getAdminPath()+"/invaccountcode/invAccountCode/?repage";
	}
	
	/**
	 * 删除库存帐扫码
	 */
	@ResponseBody
	@RequiresPermissions("invaccountcode:invAccountCode:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(InvAccountCode invAccountCode, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		invAccountCodeService.delete(invAccountCode);
		j.setMsg("删除库存帐扫码成功");
		return j;
	}
	
	/**
	 * 批量删除库存帐扫码
	 */
	@ResponseBody
	@RequiresPermissions("invaccountcode:invAccountCode:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			invAccountCodeService.delete(invAccountCodeService.get(id));
		}
		j.setMsg("删除库存帐扫码成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("invaccountcode:invAccountCode:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(InvAccountCode invAccountCode, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "库存帐扫码"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<InvAccountCode> page = invAccountCodeService.findPage(new Page<InvAccountCode>(request, response, -1), invAccountCode);
    		new ExportExcel("库存帐扫码", InvAccountCode.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出库存帐扫码记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("invaccountcode:invAccountCode:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<InvAccountCode> list = ei.getDataList(InvAccountCode.class);
			for (InvAccountCode invAccountCode : list){
				try{
					invAccountCodeService.save(invAccountCode);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条库存帐扫码记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条库存帐扫码记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入库存帐扫码失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/invaccountcode/invAccountCode/?repage";
    }
	
	/**
	 * 下载导入库存帐扫码数据模板
	 */
	@RequiresPermissions("invaccountcode:invAccountCode:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "库存帐扫码数据导入模板.xlsx";
    		List<InvAccountCode> list = Lists.newArrayList(); 
    		new ExportExcel("库存帐扫码数据", InvAccountCode.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/invaccountcode/invAccountCode/?repage";
    }

}