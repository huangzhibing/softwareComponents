/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.qualitymanage.qualitynorm.web;

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
import com.hqu.modules.qualitymanage.qualitynorm.entity.QualityNorm;
import com.hqu.modules.qualitymanage.qualitynorm.service.QualityNormService;

/**
 * 检验执行标准Controller
 * @author syc
 * @version 2018-04-26
 */
@Controller
@RequestMapping(value = "${adminPath}/qualitynorm/qualityNorm")
public class QualityNormController extends BaseController {

	@Autowired
	private QualityNormService qualityNormService;
	
	@ModelAttribute
	public QualityNorm get(@RequestParam(required=false) String id) {
		QualityNorm entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = qualityNormService.get(id);
		}
		if (entity == null){
			entity = new QualityNorm();
		}
		return entity;
	}
	
	/**
	 * 检验执行标准列表页面
	 */
	@RequiresPermissions("qualitynorm:qualityNorm:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "qualitymanage/qualitynorm/qualityNormList";
	}
	
		/**
	 * 检验执行标准列表数据
	 */
	@ResponseBody
	@RequiresPermissions("qualitynorm:qualityNorm:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(QualityNorm qualityNorm, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<QualityNorm> page = qualityNormService.findPage(new Page<QualityNorm>(request, response), qualityNorm); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑检验执行标准表单页面
	 */
	@RequiresPermissions(value={"qualitynorm:qualityNorm:view","qualitynorm:qualityNorm:add","qualitynorm:qualityNorm:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(QualityNorm qualityNorm, Model model) {
		model.addAttribute("qualityNorm", qualityNorm);
		if(StringUtils.isBlank(qualityNorm.getId())){//如果ID是空为添加
			model.addAttribute("isAdd", true);
		}
		return "qualitymanage/qualitynorm/qualityNormForm";
	}

	/**
	 * 保存检验执行标准
	 */
	@RequiresPermissions(value={"qualitynorm:qualityNorm:add","qualitynorm:qualityNorm:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(QualityNorm qualityNorm, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, qualityNorm)){
			return form(qualityNorm, model);
		}
		//新增或编辑表单保存
		qualityNormService.save(qualityNorm);//保存
		addMessage(redirectAttributes, "保存检验执行标准成功");
		return "redirect:"+Global.getAdminPath()+"/qualitynorm/qualityNorm/?repage";
	}
	
	/**
	 * 删除检验执行标准
	 */
	@ResponseBody
	@RequiresPermissions("qualitynorm:qualityNorm:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(QualityNorm qualityNorm, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		qualityNormService.delete(qualityNorm);
		j.setMsg("删除检验执行标准成功");
		return j;
	}
	
	/**
	 * 批量删除检验执行标准
	 */
	@ResponseBody
	@RequiresPermissions("qualitynorm:qualityNorm:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			qualityNormService.delete(qualityNormService.get(id));
		}
		j.setMsg("删除检验执行标准成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("qualitynorm:qualityNorm:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(QualityNorm qualityNorm, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "检验执行标准"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<QualityNorm> page = qualityNormService.findPage(new Page<QualityNorm>(request, response, -1), qualityNorm);
    		new ExportExcel("检验执行标准", QualityNorm.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出检验执行标准记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("qualitynorm:qualityNorm:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<QualityNorm> list = ei.getDataList(QualityNorm.class);
			for (QualityNorm qualityNorm : list){
				try{
					qualityNormService.save(qualityNorm);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条检验执行标准记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条检验执行标准记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入检验执行标准失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/qualitynorm/qualityNorm/?repage";
    }
	
	/**
	 * 下载导入检验执行标准数据模板
	 */
	@RequiresPermissions("qualitynorm:qualityNorm:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "检验执行标准数据导入模板.xlsx";
    		List<QualityNorm> list = Lists.newArrayList(); 
    		new ExportExcel("检验执行标准数据", QualityNorm.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/qualitynorm/qualityNorm/?repage";
    }

}