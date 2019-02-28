/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.purchasemanage.archives.web;

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
import com.hqu.modules.purchasemanage.archives.entity.Puraccount;
import com.hqu.modules.purchasemanage.archives.service.PuraccountService;

/**
 * 供应商档案Controller
 * @author luyumiao
 * @version 2018-05-01
 */
@Controller
@RequestMapping(value = "${adminPath}/archives/puraccount")
public class PuraccountController extends BaseController {

	@Autowired
	private PuraccountService puraccountService;
	
	@ModelAttribute
	public Puraccount get(@RequestParam(required=false) String id) {
		Puraccount entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = puraccountService.get(id);
		}
		if (entity == null){
			entity = new Puraccount();
		}
		return entity;
	}
	
	/**
	 * 供应商档案列表页面
	 */
	@RequiresPermissions("archives:puraccount:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "purchasemanage/archives/puraccountList";
	}
	
		/**
	 * 供应商档案列表数据
	 */
	@ResponseBody
	@RequiresPermissions("archives:puraccount:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(Puraccount puraccount, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Puraccount> page = puraccountService.findPage(new Page<Puraccount>(request, response), puraccount); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑供应商档案表单页面
	 */
	@RequiresPermissions(value={"archives:puraccount:view","archives:puraccount:add","archives:puraccount:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(Puraccount puraccount, Model model) {
		model.addAttribute("puraccount", puraccount);
		if(StringUtils.isBlank(puraccount.getId())){//如果ID是空为添加
			model.addAttribute("isAdd", true);
		}
		return "purchasemanage/archives/puraccountForm";
	}

	/**
	 * 保存供应商档案
	 */
	@RequiresPermissions(value={"archives:puraccount:add","archives:puraccount:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(Puraccount puraccount, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, puraccount)){
			return form(puraccount, model);
		}
		//新增或编辑表单保存
		puraccountService.save(puraccount);//保存
		addMessage(redirectAttributes, "保存供应商档案成功");
		return "redirect:"+Global.getAdminPath()+"/archives/puraccount/?repage";
	}
	
	/**
	 * 删除供应商档案
	 */
	@ResponseBody
	@RequiresPermissions("archives:puraccount:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(Puraccount puraccount, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		puraccountService.delete(puraccount);
		j.setMsg("删除供应商档案成功");
		return j;
	}
	
	/**
	 * 批量删除供应商档案
	 */
	@ResponseBody
	@RequiresPermissions("archives:puraccount:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			puraccountService.delete(puraccountService.get(id));
		}
		j.setMsg("删除供应商档案成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("archives:puraccount:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(Puraccount puraccount, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "供应商档案"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<Puraccount> page = puraccountService.findPage(new Page<Puraccount>(request, response, -1), puraccount);
    		new ExportExcel("供应商档案", Puraccount.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出供应商档案记录失败！失败信息："+e.getMessage());
		}
			return j;
    }
    
    @ResponseBody
    @RequestMapping(value = "detail")
	public Puraccount detail(String id) {
		return puraccountService.get(id);
	}
	

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("archives:puraccount:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<Puraccount> list = ei.getDataList(Puraccount.class);
			for (Puraccount puraccount : list){
				try{
					puraccountService.save(puraccount);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条供应商档案记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条供应商档案记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入供应商档案失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/archives/puraccount/?repage";
    }
	
	/**
	 * 下载导入供应商档案数据模板
	 */
	@RequiresPermissions("archives:puraccount:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "供应商档案数据导入模板.xlsx";
    		List<Puraccount> list = Lists.newArrayList(); 
    		new ExportExcel("供应商档案数据", Puraccount.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/archives/puraccount/?repage";
    }
	

}