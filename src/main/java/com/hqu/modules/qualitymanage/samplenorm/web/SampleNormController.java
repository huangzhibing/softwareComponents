/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.qualitymanage.samplenorm.web;

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
import com.hqu.modules.qualitymanage.samplenorm.entity.SampleNorm;
import com.hqu.modules.qualitymanage.samplenorm.service.SampleNormService;

/**
 * 抽样标准维护Controller
 * @author ckw
 * @version 2018-09-05
 */
@Controller
@RequestMapping(value = "${adminPath}/samplenorm/sampleNorm")
public class SampleNormController extends BaseController {

	@Autowired
	private SampleNormService sampleNormService;
	
	@ModelAttribute
	public SampleNorm get(@RequestParam(required=false) String id) {
		SampleNorm entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = sampleNormService.get(id);
		}
		if (entity == null){
			entity = new SampleNorm();
		}
		return entity;
	}
	
	/**
	 * 维护“抽样标准”列表页面
	 */
	@RequiresPermissions("samplenorm:sampleNorm:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "qualitymanage/samplenorm/sampleNormList";
	}
	
		/**
	 * 维护“抽样标准”列表数据
	 */
	@ResponseBody
	@RequiresPermissions("samplenorm:sampleNorm:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(SampleNorm sampleNorm, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<SampleNorm> page = sampleNormService.findPage(new Page<SampleNorm>(request, response), sampleNorm); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑维护“抽样标准”表单页面
	 */
	@RequiresPermissions(value={"samplenorm:sampleNorm:view","samplenorm:sampleNorm:add","samplenorm:sampleNorm:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(SampleNorm sampleNorm, Model model) {
		model.addAttribute("sampleNorm", sampleNorm);
		if(StringUtils.isBlank(sampleNorm.getId())){//如果ID是空为添加
			model.addAttribute("isAdd", true);
		}
		return "qualitymanage/samplenorm/sampleNormForm";
	}

	/**
	 * 保存维护“抽样标准”
	 */
	@RequiresPermissions(value={"samplenorm:sampleNorm:add","samplenorm:sampleNorm:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(SampleNorm sampleNorm, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, sampleNorm)){
			return form(sampleNorm, model);
		}
		//新增或编辑表单保存
		sampleNormService.save(sampleNorm);//保存
		addMessage(redirectAttributes, "保存维护“抽样标准”成功");
		return "redirect:"+Global.getAdminPath()+"/samplenorm/sampleNorm/?repage";
	}
	
	/**
	 * 删除维护“抽样标准”
	 */
	@ResponseBody
	@RequiresPermissions("samplenorm:sampleNorm:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(SampleNorm sampleNorm, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		sampleNormService.delete(sampleNorm);
		j.setMsg("删除维护“抽样标准”成功");
		return j;
	}
	
	/**
	 * 批量删除维护“抽样标准”
	 */
	@ResponseBody
	@RequiresPermissions("samplenorm:sampleNorm:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			sampleNormService.delete(sampleNormService.get(id));
		}
		j.setMsg("删除维护“抽样标准”成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("samplenorm:sampleNorm:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(SampleNorm sampleNorm, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "维护“抽样标准”"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<SampleNorm> page = sampleNormService.findPage(new Page<SampleNorm>(request, response, -1), sampleNorm);
    		new ExportExcel("维护“抽样标准”", SampleNorm.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出维护“抽样标准”记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("samplenorm:sampleNorm:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<SampleNorm> list = ei.getDataList(SampleNorm.class);
			for (SampleNorm sampleNorm : list){
				try{
					sampleNormService.save(sampleNorm);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条维护“抽样标准”记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条维护“抽样标准”记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入维护“抽样标准”失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/samplenorm/sampleNorm/?repage";
    }
	
	/**
	 * 下载导入维护“抽样标准”数据模板
	 */
	@RequiresPermissions("samplenorm:sampleNorm:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "维护“抽样标准”数据导入模板.xlsx";
    		List<SampleNorm> list = Lists.newArrayList(); 
    		new ExportExcel("维护“抽样标准”数据", SampleNorm.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/samplenorm/sampleNorm/?repage";
    }

}