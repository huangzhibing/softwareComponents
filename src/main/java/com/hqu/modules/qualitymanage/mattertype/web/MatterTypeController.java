/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.qualitymanage.mattertype.web;

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
import com.hqu.modules.qualitymanage.mattertype.entity.MatterType;
import com.hqu.modules.qualitymanage.mattertype.service.MatterTypeService;

/**
 * MatterTypeController
 * @author 方翠虹
 * @version 2018-08-18
 */
@Controller
@RequestMapping(value = "${adminPath}/mattertype/matterType")
public class MatterTypeController extends BaseController {

	@Autowired
	private MatterTypeService matterTypeService;
	
	@ModelAttribute
	public MatterType get(@RequestParam(required=false) String id) {
		MatterType entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = matterTypeService.get(id);
		}
		if (entity == null){
			entity = new MatterType();
		}
		return entity;
	}
	
	/**
	 * 质量问题类型列表页面
	 */
	@RequiresPermissions("mattertype:matterType:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "qualitymanage/mattertype/matterTypeList";
	}
	
		/**
	 * 质量问题类型列表数据
	 */
	@ResponseBody
	@RequiresPermissions("mattertype:matterType:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(MatterType matterType, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<MatterType> page = matterTypeService.findPage(new Page<MatterType>(request, response), matterType); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑质量问题类型表单页面
	 */
	@RequiresPermissions(value={"mattertype:matterType:view","mattertype:matterType:add","mattertype:matterType:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(MatterType matterType, Model model) {
		model.addAttribute("matterType", matterType);
		if(StringUtils.isBlank(matterType.getId())){//如果ID是空为添加
			model.addAttribute("isAdd", true);
		}
		return "qualitymanage/mattertype/matterTypeForm";
	}

	/**
	 * 保存质量问题类型
	 */
	@RequiresPermissions(value={"mattertype:matterType:add","mattertype:matterType:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(MatterType matterType, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, matterType)){
			return form(matterType, model);
		}
		//新增或编辑表单保存
		matterTypeService.save(matterType);//保存
		addMessage(redirectAttributes, "保存质量问题类型成功");
		return "redirect:"+Global.getAdminPath()+"/mattertype/matterType/?repage";
	}
	
	/**
	 * 删除质量问题类型
	 */
	@ResponseBody
	@RequiresPermissions("mattertype:matterType:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(MatterType matterType, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		matterTypeService.delete(matterType);
		j.setMsg("删除质量问题类型成功");
		return j;
	}
	
	/**
	 * 批量删除质量问题类型
	 */
	@ResponseBody
	@RequiresPermissions("mattertype:matterType:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			matterTypeService.delete(matterTypeService.get(id));
		}
		j.setMsg("删除质量问题类型成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("mattertype:matterType:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(MatterType matterType, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "质量问题类型"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<MatterType> page = matterTypeService.findPage(new Page<MatterType>(request, response, -1), matterType);
    		new ExportExcel("质量问题类型", MatterType.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出质量问题类型记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("mattertype:matterType:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<MatterType> list = ei.getDataList(MatterType.class);
			for (MatterType matterType : list){
				try{
					matterTypeService.save(matterType);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条质量问题类型记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条质量问题类型记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入质量问题类型失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/mattertype/matterType/?repage";
    }
	
	/**
	 * 下载导入质量问题类型数据模板
	 */
	@RequiresPermissions("mattertype:matterType:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "质量问题类型数据导入模板.xlsx";
    		List<MatterType> list = Lists.newArrayList(); 
    		new ExportExcel("质量问题类型数据", MatterType.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/mattertype/matterType/?repage";
    }

}