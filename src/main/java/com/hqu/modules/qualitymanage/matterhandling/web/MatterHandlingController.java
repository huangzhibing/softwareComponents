/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.qualitymanage.matterhandling.web;

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
import com.hqu.modules.qualitymanage.matterhandling.entity.MatterHandling;
import com.hqu.modules.qualitymanage.matterhandling.service.MatterHandlingService;

/**
 * MatterHandlingController
 * @author 方翠虹
 * @version 2018-04-25
 */
@Controller
@RequestMapping(value = "${adminPath}/matterhandling/matterHandling")
public class MatterHandlingController extends BaseController {

	@Autowired
	private MatterHandlingService matterHandlingService;
	
	@ModelAttribute
	public MatterHandling get(@RequestParam(required=false) String id) {
		MatterHandling entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = matterHandlingService.get(id);
		}
		if (entity == null){
			entity = new MatterHandling();
		}
		return entity;
	}
	
	/**
	 * 常见质量问题处理意见列表页面
	 */
	@RequiresPermissions("matterhandling:matterHandling:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "qualitymanage/matterhandling/matterHandlingList";
	}
	
		/**
	 * 常见质量问题处理意见列表数据
	 */
	@ResponseBody
	@RequiresPermissions("matterhandling:matterHandling:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(MatterHandling matterHandling, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<MatterHandling> page = matterHandlingService.findPage(new Page<MatterHandling>(request, response), matterHandling); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑常见质量问题处理意见表单页面
	 */
	@RequiresPermissions(value={"matterhandling:matterHandling:view","matterhandling:matterHandling:add","matterhandling:matterHandling:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(MatterHandling matterHandling, Model model) {
		model.addAttribute("matterHandling", matterHandling);
		if(StringUtils.isBlank(matterHandling.getId())){//如果ID是空为添加
			model.addAttribute("isAdd", true);
		}
		return "qualitymanage/matterhandling/matterHandlingForm";
	}

	/**
	 * 保存常见质量问题处理意见
	 */
	@RequiresPermissions(value={"matterhandling:matterHandling:add","matterhandling:matterHandling:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(MatterHandling matterHandling, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, matterHandling)){
			return form(matterHandling, model);
		}
		//新增或编辑表单保存
		matterHandlingService.save(matterHandling);//保存
		addMessage(redirectAttributes, "保存常见质量问题处理意见成功");
		return "redirect:"+Global.getAdminPath()+"/matterhandling/matterHandling/?repage";
	}
	
	/**
	 * 删除常见质量问题处理意见
	 */
	@ResponseBody
	@RequiresPermissions("matterhandling:matterHandling:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(MatterHandling matterHandling, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		matterHandlingService.delete(matterHandling);
		j.setMsg("删除常见质量问题处理意见成功");
		return j;
	}
	
	/**
	 * 批量删除常见质量问题处理意见
	 */
	@ResponseBody
	@RequiresPermissions("matterhandling:matterHandling:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			matterHandlingService.delete(matterHandlingService.get(id));
		}
		j.setMsg("删除常见质量问题处理意见成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("matterhandling:matterHandling:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(MatterHandling matterHandling, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "常见质量问题处理意见"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<MatterHandling> page = matterHandlingService.findPage(new Page<MatterHandling>(request, response, -1), matterHandling);
    		new ExportExcel("常见质量问题处理意见", MatterHandling.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出常见质量问题处理意见记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("matterhandling:matterHandling:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<MatterHandling> list = ei.getDataList(MatterHandling.class);
			for (MatterHandling matterHandling : list){
				try{
					matterHandlingService.save(matterHandling);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条常见质量问题处理意见记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条常见质量问题处理意见记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入常见质量问题处理意见失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/matterhandling/matterHandling/?repage";
    }
	
	/**
	 * 下载导入常见质量问题处理意见数据模板
	 */
	@RequiresPermissions("matterhandling:matterHandling:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "常见质量问题处理意见数据导入模板.xlsx";
    		List<MatterHandling> list = Lists.newArrayList(); 
    		new ExportExcel("常见质量问题处理意见数据", MatterHandling.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/matterhandling/matterHandling/?repage";
    }

}