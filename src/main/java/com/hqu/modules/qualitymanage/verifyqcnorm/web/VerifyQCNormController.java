/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.qualitymanage.verifyqcnorm.web;

import com.google.common.collect.Lists;
import com.hqu.modules.qualitymanage.verifyqcnorm.entity.VerifyQCNorm;
import com.hqu.modules.qualitymanage.verifyqcnorm.service.VerifyQCNormService;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.excel.ExportExcel;
import com.jeeplus.common.utils.excel.ImportExcel;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.web.BaseController;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Map;

/**
 * 检验数据验证Controller
 * @author 石豪迈
 * @version 2018-05-18
 */
@Controller
@RequestMapping(value = "${adminPath}/verifyqcnorm/verifyQCNorm")
public class VerifyQCNormController extends BaseController {

	@Autowired
	private VerifyQCNormService verifyQCNormService;
	
	@ModelAttribute
	public VerifyQCNorm get(@RequestParam(required=false) String id) {
		VerifyQCNorm entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = verifyQCNormService.get(id);
		}
		if (entity == null){
			entity = new VerifyQCNorm();
		}
		return entity;
	}
	
	/**
	 * 检验数据验证列表页面
	 */
	@RequiresPermissions("verifyqcnorm:verifyQCNorm:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "qualitymanage/verifyqcnorm/verifyQCNormList";
	}
	
		/**
	 * 检验数据验证列表数据
	 */
	@ResponseBody
	@RequiresPermissions("verifyqcnorm:verifyQCNorm:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(VerifyQCNorm verifyQCNorm, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<VerifyQCNorm> page = verifyQCNormService.findPage(new Page<VerifyQCNorm>(request, response), verifyQCNorm); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑检验数据验证表单页面
	 */
	@RequiresPermissions(value={"verifyqcnorm:verifyQCNorm:view","verifyqcnorm:verifyQCNorm:add","verifyqcnorm:verifyQCNorm:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(VerifyQCNorm verifyQCNorm, Model model) {
		model.addAttribute("verifyQCNorm", verifyQCNorm);
		if(StringUtils.isBlank(verifyQCNorm.getId())){//如果ID是空为添加
			model.addAttribute("isAdd", true);
		}
		return "qualitymanage/verifyqcnorm/verifyQCNormForm";
	}

	/**
	 * 保存检验数据验证
	 */
	@RequiresPermissions(value={"verifyqcnorm:verifyQCNorm:add","verifyqcnorm:verifyQCNorm:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(VerifyQCNorm verifyQCNorm, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, verifyQCNorm)){
			return form(verifyQCNorm, model);
		}
		//新增或编辑表单保存
		verifyQCNormService.save(verifyQCNorm);//保存
		addMessage(redirectAttributes, "保存检验数据验证成功");
		return "redirect:"+Global.getAdminPath()+"/verifyqcnorm/verifyQCNorm/?repage";
	}
	
	/**
	 * 删除检验数据验证
	 */
	@ResponseBody
	@RequiresPermissions("verifyqcnorm:verifyQCNorm:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(VerifyQCNorm verifyQCNorm, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		verifyQCNormService.delete(verifyQCNorm);
		j.setMsg("删除检验数据验证成功");
		return j;
	}
	
	/**
	 * 批量删除检验数据验证
	 */
	@ResponseBody
	@RequiresPermissions("verifyqcnorm:verifyQCNorm:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			verifyQCNormService.delete(verifyQCNormService.get(id));
		}
		j.setMsg("删除检验数据验证成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("verifyqcnorm:verifyQCNorm:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(VerifyQCNorm verifyQCNorm, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "检验数据验证"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<VerifyQCNorm> page = verifyQCNormService.findPage(new Page<VerifyQCNorm>(request, response, -1), verifyQCNorm);
    		new ExportExcel("检验数据验证", VerifyQCNorm.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出检验数据验证记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("verifyqcnorm:verifyQCNorm:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<VerifyQCNorm> list = ei.getDataList(VerifyQCNorm.class);
			for (VerifyQCNorm verifyQCNorm : list){
				try{
					verifyQCNormService.save(verifyQCNorm);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条检验数据验证记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条检验数据验证记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入检验数据验证失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/verifyqcnorm/verifyQCNorm/?repage";
    }
	
	/**
	 * 下载导入检验数据验证数据模板
	 */
	@RequiresPermissions("verifyqcnorm:verifyQCNorm:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "检验数据验证数据导入模板.xlsx";
    		List<VerifyQCNorm> list = Lists.newArrayList(); 
    		new ExportExcel("检验数据验证数据", VerifyQCNorm.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/verifyqcnorm/verifyQCNorm/?repage";
    }

}