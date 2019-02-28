/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.inventorymanage.use.web;

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
import com.hqu.modules.inventorymanage.use.entity.Use;
import com.hqu.modules.inventorymanage.use.service.UseService;

/**
 * 用途定义Controller
 * @author lmy
 * @version 2018-04-14
 */
@Controller
@RequestMapping(value = "${adminPath}/use/use")
public class UseController extends BaseController {

	@Autowired
	private UseService useService;
	
	@ModelAttribute
	public Use get(@RequestParam(required=false) String id) {
		Use entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = useService.get(id);
		}
		if (entity == null){
			entity = new Use();
		}
		return entity;
	}
	
	/**
	 * 用途定义列表页面
	 */
	@RequiresPermissions("use:use:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "inventorymanage/use/useList";
	}
	
		/**
	 * 用途定义列表数据
	 */
	@ResponseBody
	@RequiresPermissions("use:use:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(Use use, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Use> page = useService.findPage(new Page<Use>(request, response), use); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑用途定义表单页面
	 */
	@RequiresPermissions(value={"use:use:view","use:use:add","use:use:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(Use use, Model model) {
		model.addAttribute("use", use);
		if(StringUtils.isBlank(use.getId())){//如果ID是空为添加
			model.addAttribute("isAdd", true);
		}
		return "inventorymanage/use/useForm";
	}

	/**
	 * 保存用途定义
	 */
	@RequiresPermissions(value={"use:use:add","use:use:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(Use use, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, use)){
			return form(use, model);
		}
		//新增或编辑表单保存
		useService.save(use);//保存
		addMessage(redirectAttributes, "保存用途定义成功");
		return "redirect:"+Global.getAdminPath()+"/use/use/?repage";
	}
	
	/**
	 * 删除用途定义
	 */
	@ResponseBody
	@RequiresPermissions("use:use:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(Use use, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		useService.delete(use);
		j.setMsg("删除用途定义成功");
		return j;
	}
	
	/**
	 * 批量删除用途定义
	 */
	@ResponseBody
	@RequiresPermissions("use:use:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			useService.delete(useService.get(id));
		}
		j.setMsg("删除用途定义成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("use:use:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(Use use, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "用途定义"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<Use> page = useService.findPage(new Page<Use>(request, response, -1), use);
    		new ExportExcel("用途定义", Use.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出用途定义记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("use:use:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<Use> list = ei.getDataList(Use.class);
			for (Use use : list){
				try{
					useService.save(use);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条用途定义记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条用途定义记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入用途定义失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/use/use/?repage";
    }
	
	/**
	 * 下载导入用途定义数据模板
	 */
	@RequiresPermissions("use:use:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "用途定义数据导入模板.xlsx";
    		List<Use> list = Lists.newArrayList(); 
    		new ExportExcel("用途定义数据", Use.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/use/use/?repage";
    }

}