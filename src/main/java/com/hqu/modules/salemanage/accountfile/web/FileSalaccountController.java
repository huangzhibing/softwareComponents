/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.salemanage.accountfile.web;

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
import com.hqu.modules.salemanage.accountfile.entity.FileSalaccount;
import com.hqu.modules.salemanage.accountfile.service.FileSalaccountService;

/**
 * 客户档案维护Controller
 * @author hzb
 * @version 2018-05-16
 */
@Controller
@RequestMapping(value = "${adminPath}/accountfile/fileSalaccount")
public class FileSalaccountController extends BaseController {

	@Autowired
	private FileSalaccountService fileSalaccountService;
	
	@ModelAttribute
	public FileSalaccount get(@RequestParam(required=false) String id) {
		FileSalaccount entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = fileSalaccountService.get(id);
		}
		if (entity == null){
			entity = new FileSalaccount();
		}
		return entity;
	}
	
	/**
	 * 客户档案维护列表页面
	 */
	@RequiresPermissions("accountfile:fileSalaccount:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "salemanage/accountfile/fileSalaccountList";
	}
	
		/**
	 * 客户档案维护列表数据
	 */
	@ResponseBody
	@RequiresPermissions("accountfile:fileSalaccount:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(FileSalaccount fileSalaccount, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<FileSalaccount> page = fileSalaccountService.findPage(new Page<FileSalaccount>(request, response), fileSalaccount); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑客户档案维护表单页面
	 */
//	@RequiresPermissions(value={"accountfile:fileSalaccount:view","accountfile:fileSalaccount:add","accountfile:fileSalaccount:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(FileSalaccount fileSalaccount, Model model) {
		model.addAttribute("fileSalaccount", fileSalaccount);
		if(StringUtils.isBlank(fileSalaccount.getId())){//如果ID是空为添加
			model.addAttribute("isAdd", true);
		}
		return "salemanage/accountfile/fileSalaccountForm";
	}

	/**
	 * 保存客户档案维护
	 */
//	@RequiresPermissions(value={"accountfile:fileSalaccount:add","accountfile:fileSalaccount:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(FileSalaccount fileSalaccount, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, fileSalaccount)){
			return form(fileSalaccount, model);
		}
		//新增或编辑表单保存
		fileSalaccountService.save(fileSalaccount);//保存
		addMessage(redirectAttributes, "保存客户档案维护成功");
		return "redirect:"+Global.getAdminPath()+"/accountfile/fileSalaccount/?repage";
	}
	
	/**
	 * 删除客户档案维护
	 */
	@ResponseBody
//	@RequiresPermissions("accountfile:fileSalaccount:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(FileSalaccount fileSalaccount, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		fileSalaccountService.delete(fileSalaccount);
		j.setMsg("删除客户档案维护成功");
		return j;
	}
	
	/**
	 * 批量删除客户档案维护
	 */
	@ResponseBody
//	@RequiresPermissions("accountfile:fileSalaccount:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			fileSalaccountService.delete(fileSalaccountService.get(id));
		}
		j.setMsg("删除客户档案维护成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
//	@RequiresPermissions("accountfile:fileSalaccount:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(FileSalaccount fileSalaccount, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "客户档案维护"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<FileSalaccount> page = fileSalaccountService.findPage(new Page<FileSalaccount>(request, response, -1), fileSalaccount);
    		new ExportExcel("客户档案维护", FileSalaccount.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出客户档案维护记录失败！失败信息："+e.getMessage());
		}
			return j;
    }
    
    @ResponseBody
    @RequestMapping(value = "detail")
	public FileSalaccount detail(String id) {
		return fileSalaccountService.get(id);
	}
	

	/**
	 * 导入Excel数据

	 */
//	@RequiresPermissions("accountfile:fileSalaccount:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<FileSalaccount> list = ei.getDataList(FileSalaccount.class);
			for (FileSalaccount fileSalaccount : list){
				try{
					fileSalaccountService.save(fileSalaccount);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条客户档案维护记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条客户档案维护记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入客户档案维护失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/accountfile/fileSalaccount/?repage";
    }
	
	/**
	 * 下载导入客户档案维护数据模板
	 */
//	@RequiresPermissions("accountfile:fileSalaccount:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "客户档案维护数据导入模板.xlsx";
    		List<FileSalaccount> list = Lists.newArrayList(); 
    		new ExportExcel("客户档案维护数据", FileSalaccount.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/accountfile/fileSalaccount/?repage";
    }
	

}