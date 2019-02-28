/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.basedata.personwork.web;

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
import com.hqu.modules.basedata.personwork.entity.PersonWork;
import com.hqu.modules.basedata.personwork.service.PersonWorkService;

/**
 * 人员工种表Controller
 * @author liujiachen
 * @version 2018-06-04
 */
@Controller
@RequestMapping(value = "${adminPath}/personwork/personWork")
public class PersonWorkController extends BaseController {

	@Autowired
	private PersonWorkService personWorkService;
	
	@ModelAttribute
	public PersonWork get(@RequestParam(required=false) String id) {
		PersonWork entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = personWorkService.get(id);
		}
		if (entity == null){
			entity = new PersonWork();
		}
		return entity;
	}
	
	/**
	 * 人员工种列表页面
	 */
	@RequiresPermissions("personwork:personWork:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "basedata/personwork/personWorkList";
	}
	
		/**
	 * 人员工种列表数据
	 */
	@ResponseBody
	@RequiresPermissions("personwork:personWork:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(PersonWork personWork, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<PersonWork> page = personWorkService.findPage(new Page<PersonWork>(request, response), personWork); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑人员工种表单页面
	 */
	@RequiresPermissions(value={"personwork:personWork:view","personwork:personWork:add","personwork:personWork:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(PersonWork personWork, Model model) {
		model.addAttribute("personWork", personWork);
		if(StringUtils.isBlank(personWork.getId())){//如果ID是空为添加
			model.addAttribute("isAdd", true);
		}
		return "basedata/personwork/personWorkForm";
	}

	/**
	 * 保存人员工种
	 */
	@RequiresPermissions(value={"personwork:personWork:add","personwork:personWork:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(PersonWork personWork, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, personWork)){
			return form(personWork, model);
		}
		//新增或编辑表单保存
		personWorkService.save(personWork);//保存
		addMessage(redirectAttributes, "保存人员工种成功");
		return "redirect:"+Global.getAdminPath()+"/personwork/personWork/?repage";
	}
	
	/**
	 * 删除人员工种
	 */
	@ResponseBody
	@RequiresPermissions("personwork:personWork:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(PersonWork personWork, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		personWorkService.delete(personWork);
		j.setMsg("删除人员工种成功");
		return j;
	}
	
	/**
	 * 批量删除人员工种
	 */
	@ResponseBody
	@RequiresPermissions("personwork:personWork:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			personWorkService.delete(personWorkService.get(id));
		}
		j.setMsg("删除人员工种成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("personwork:personWork:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(PersonWork personWork, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "人员工种"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<PersonWork> page = personWorkService.findPage(new Page<PersonWork>(request, response, -1), personWork);
    		new ExportExcel("人员工种", PersonWork.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出人员工种记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("personwork:personWork:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<PersonWork> list = ei.getDataList(PersonWork.class);
			for (PersonWork personWork : list){
				try{
					personWorkService.save(personWork);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条人员工种记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条人员工种记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入人员工种失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/personwork/personWork/?repage";
    }
	
	/**
	 * 下载导入人员工种数据模板
	 */
	@RequiresPermissions("personwork:personWork:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "人员工种数据导入模板.xlsx";
    		List<PersonWork> list = Lists.newArrayList(); 
    		new ExportExcel("人员工种数据", PersonWork.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/personwork/personWork/?repage";
    }

}