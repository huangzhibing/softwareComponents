/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.purchasemanage.linkman.web;

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
import com.hqu.modules.purchasemanage.linkman.entity.Paccount;
import com.hqu.modules.purchasemanage.linkman.service.PaccountService;

/**
 * 供应商联系人Controller
 * @author luyumiao
 * @version 2018-04-15
 */
@Controller
@RequestMapping(value = "${adminPath}/linkman/paccount")
public class PaccountController extends BaseController {

	@Autowired
	private PaccountService paccountService;
	
	@ModelAttribute
	public Paccount get(@RequestParam(required=false) String id) {
		Paccount entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = paccountService.get(id);
		}
		if (entity == null){
			entity = new Paccount();
		}
		return entity;
	}
	
	/**
	 * 供应商联系人列表页面
	 */
	@RequiresPermissions("linkman:paccount:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "purchasemanage/linkman/paccountList";
	}
	
		/**
	 * 供应商联系人列表数据
	 */
	@ResponseBody
	@RequiresPermissions("linkman:paccount:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(Paccount paccount, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Paccount> page = paccountService.findPage(new Page<Paccount>(request, response), paccount); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑供应商联系人表单页面
	 */
	@RequiresPermissions(value={"linkman:paccount:view","linkman:paccount:add","linkman:paccount:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(Paccount paccount, Model model) {
		model.addAttribute("paccount", paccount);
		if(StringUtils.isBlank(paccount.getId())){//如果ID是空为添加
			model.addAttribute("isAdd", true);
	
		}
		return "purchasemanage/linkman/paccountForm";
	}
	
	/**
	 * 供应商联系人表单页面详情
	 */
	@RequestMapping(value = "details")
	public String details(Paccount paccount, Model model) {
		model.addAttribute("paccount", paccount);
		if(StringUtils.isBlank(paccount.getId())){//如果ID是空为添加
			model.addAttribute("isAdd", true);
	
		}
		return "purchasemanage/linkman/paccountFormReadOnly";
	}

	/**
	 * 保存供应商联系人
	 */
	@RequiresPermissions(value={"linkman:paccount:add","linkman:paccount:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(Paccount paccount, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, paccount)){
			return form(paccount, model);
		}
		//新增或编辑表单保存
		paccountService.save(paccount);//保存
		addMessage(redirectAttributes, "保存供应商联系人成功");
		return "redirect:"+Global.getAdminPath()+"/linkman/paccount/?repage";
	}
	
	/**
	 * 删除供应商联系人
	 */
	@ResponseBody
	@RequiresPermissions("linkman:paccount:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(Paccount paccount, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		paccountService.delete(paccount);
		j.setMsg("删除供应商联系人成功");
		return j;
	}
	
	/**
	 * 批量删除供应商联系人
	 */
	@ResponseBody
	@RequiresPermissions("linkman:paccount:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			paccountService.delete(paccountService.get(id));
		}
		j.setMsg("删除供应商联系人成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("linkman:paccount:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(Paccount paccount, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "供应商联系人"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<Paccount> page = paccountService.findPage(new Page<Paccount>(request, response, -1), paccount);
    		new ExportExcel("供应商联系人", Paccount.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出供应商联系人记录失败！失败信息："+e.getMessage());
		}
			return j;
    }
    
    @ResponseBody
    @RequestMapping(value = "detail")
	public Paccount detail(String id) {
		return paccountService.get(id);
	}
	

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("linkman:paccount:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<Paccount> list = ei.getDataList(Paccount.class);
			for (Paccount paccount : list){
				try{
					paccountService.save(paccount);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条供应商联系人记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条供应商联系人记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入供应商联系人失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/linkman/paccount/?repage";
    }
	
	/**
	 * 下载导入供应商联系人数据模板
	 */
	@RequiresPermissions("linkman:paccount:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "供应商联系人数据导入模板.xlsx";
    		List<Paccount> list = Lists.newArrayList(); 
    		new ExportExcel("供应商联系人数据", Paccount.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/linkman/paccount/?repage";
    }
	

}