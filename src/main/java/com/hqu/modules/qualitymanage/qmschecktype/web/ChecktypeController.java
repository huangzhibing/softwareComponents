/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.qualitymanage.qmschecktype.web;

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
import com.hqu.modules.qualitymanage.qmschecktype.entity.Checktype;
import com.hqu.modules.qualitymanage.qmschecktype.service.ChecktypeService;

/**
 * 检验类型Controller
 * @author tyo
 * @version 2018-04-14
 */
@Controller
@RequestMapping(value = "${adminPath}/qmschecktype/checktype")
public class ChecktypeController extends BaseController {

	@Autowired
	private ChecktypeService checktypeService;
	
	@ModelAttribute
	public Checktype get(@RequestParam(required=false) String id) {
		Checktype entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = checktypeService.get(id);
		}
		if (entity == null){
			entity = new Checktype();
		}
		return entity;
	}
	
	/**
	 * 检验类型列表页面
	 */
	@RequiresPermissions("qmschecktype:checktype:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "qualitymanage/qmschecktype/checktypeList";
	}
	
		/**
	 * 检验类型列表数据
	 */
	@ResponseBody
	@RequiresPermissions("qmschecktype:checktype:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(Checktype checktype, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Checktype> page = checktypeService.findPage(new Page<Checktype>(request, response), checktype); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑检验类型表单页面
	 */
	@RequiresPermissions(value={"qmschecktype:checktype:view","qmschecktype:checktype:add","qmschecktype:checktype:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(Checktype checktype, Model model) {
		model.addAttribute("checktype", checktype);
		if(StringUtils.isBlank(checktype.getId())){//如果ID是空为添加
			model.addAttribute("isAdd", true);
		}
		return "qualitymanage/qmschecktype/checktypeForm";
	}

	/**
	 * 保存检验类型
	 */
	@RequiresPermissions(value={"qmschecktype:checktype:add","qmschecktype:checktype:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(Checktype checktype, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, checktype)){
			return form(checktype, model);
		}
		//新增或编辑表单保存
		checktypeService.save(checktype);//保存
		addMessage(redirectAttributes, "保存检验类型成功");
		return "redirect:"+Global.getAdminPath()+"/qmschecktype/checktype/?repage";
	}
	
	/**
	 * 删除检验类型
	 */
	@ResponseBody
	@RequiresPermissions("qmschecktype:checktype:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(Checktype checktype, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		checktypeService.delete(checktype);
		j.setMsg("删除检验类型成功");
		return j;
	}
	
	/**
	 * 批量删除检验类型
	 */
	@ResponseBody
	@RequiresPermissions("qmschecktype:checktype:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			checktypeService.delete(checktypeService.get(id));
		}
		j.setMsg("删除检验类型成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("qmschecktype:checktype:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(Checktype checktype, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "检验类型"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<Checktype> page = checktypeService.findPage(new Page<Checktype>(request, response, -1), checktype);
    		new ExportExcel("检验类型", Checktype.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出检验类型记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("qmschecktype:checktype:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<Checktype> list = ei.getDataList(Checktype.class);
			for (Checktype checktype : list){
				try{
					checktypeService.save(checktype);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条检验类型记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条检验类型记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入检验类型失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/qmschecktype/checktype/?repage";
    }
	
	/**
	 * 下载导入检验类型数据模板
	 */
	@RequiresPermissions("qmschecktype:checktype:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "检验类型数据导入模板.xlsx";
    		List<Checktype> list = Lists.newArrayList(); 
    		new ExportExcel("检验类型数据", Checktype.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/qmschecktype/checktype/?repage";
    }

}