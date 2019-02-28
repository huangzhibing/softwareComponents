/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.qualitymanage.qmsqcnorm.web;

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
import com.hqu.modules.qualitymanage.qmsqcnorm.entity.QCNorm;
import com.hqu.modules.qualitymanage.qmsqcnorm.service.QCNormService;

/**
 * 检验标准定义Controller
 * @author luyumiao
 * @version 2018-05-05
 */
@Controller
@RequestMapping(value = "${adminPath}/qmsqcnorm/qCNorm")
public class QCNormController extends BaseController {

	@Autowired
	private QCNormService qCNormService;
	
	@ModelAttribute
	public QCNorm get(@RequestParam(required=false) String id) {
		QCNorm entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = qCNormService.get(id);
		}
		if (entity == null){
			entity = new QCNorm();
		}
		return entity;
	}
	
	/**
	 * 检验标准定义列表页面
	 */
	@RequiresPermissions("qmsqcnorm:qCNorm:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "qualitymanage/qmsqcnorm/qCNormList";
	}
	
	/**
	 * 检验标准定义列表数据
	 */
	@ResponseBody
	@RequiresPermissions("qmsqcnorm:qCNorm:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(QCNorm qCNorm, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<QCNorm> page = qCNormService.findPage(new Page<QCNorm>(request, response), qCNorm); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑检验标准定义表单页面
	 */
	@RequiresPermissions(value={"qmsqcnorm:qCNorm:view","qmsqcnorm:qCNorm:add","qmsqcnorm:qCNorm:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(QCNorm qCNorm, Model model) {
		model.addAttribute("qCNorm", qCNorm);
		if(StringUtils.isBlank(qCNorm.getId())){//如果ID是空为添加
			model.addAttribute("isAdd", true);
		}
		return "qualitymanage/qmsqcnorm/qCNormForm";
	}

	/**
	 * 保存检验标准定义
	 */
	@RequiresPermissions(value={"qmsqcnorm:qCNorm:add","qmsqcnorm:qCNorm:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(QCNorm qCNorm, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, qCNorm)){
			return form(qCNorm, model);
		}
		//新增或编辑表单保存
		qCNormService.save(qCNorm);//保存
		addMessage(redirectAttributes, "保存检验标准定义成功");
		return "redirect:"+Global.getAdminPath()+"/qmsqcnorm/qCNorm/?repage";
	}
	
	/**
	 * 删除检验标准定义
	 */
	@ResponseBody
	@RequiresPermissions("qmsqcnorm:qCNorm:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(QCNorm qCNorm, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		qCNormService.delete(qCNorm);
		j.setMsg("删除检验标准定义成功");
		return j;
	}
	
	/**
	 * 批量删除检验标准定义
	 */
	@ResponseBody
	@RequiresPermissions("qmsqcnorm:qCNorm:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			qCNormService.delete(qCNormService.get(id));
		}
		j.setMsg("删除检验标准定义成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("qmsqcnorm:qCNorm:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(QCNorm qCNorm, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "检验标准定义"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<QCNorm> page = qCNormService.findPage(new Page<QCNorm>(request, response, -1), qCNorm);
    		new ExportExcel("检验标准定义", QCNorm.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出检验标准定义记录失败！失败信息："+e.getMessage());
		}
			return j;
    }
    
    @ResponseBody
    @RequestMapping(value = "detail")
	public QCNorm detail(String id) {
		return qCNormService.get(id);
	}
	

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("qmsqcnorm:qCNorm:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<QCNorm> list = ei.getDataList(QCNorm.class);
			for (QCNorm qCNorm : list){
				try{
					qCNormService.save(qCNorm);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条检验标准定义记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条检验标准定义记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入检验标准定义失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/qmsqcnorm/qCNorm/?repage";
    }
	
	/**
	 * 下载导入检验标准定义数据模板
	 */
	@RequiresPermissions("qmsqcnorm:qCNorm:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "检验标准定义数据导入模板.xlsx";
    		List<QCNorm> list = Lists.newArrayList(); 
    		new ExportExcel("检验标准定义数据", QCNorm.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/qmsqcnorm/qCNorm/?repage";
    }
	

}