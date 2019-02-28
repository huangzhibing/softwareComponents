/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.inventorymanage.invcheckmain.web;

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
import com.hqu.modules.inventorymanage.invcheckmain.entity.ReinvCheckMain;
import com.hqu.modules.inventorymanage.invcheckmain.service.ReinvCheckMainService;

/**
 * 超期复检主表Controller
 * @author syc代建
 * @version 2018-05-28
 */
@Controller
@RequestMapping(value = "${adminPath}/invcheckmain/reinvCheckMain")
public class ReinvCheckMainController extends BaseController {

	@Autowired
	private ReinvCheckMainService reinvCheckMainService;
	
	@ModelAttribute
	public ReinvCheckMain get(@RequestParam(required=false) String id) {
		ReinvCheckMain entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = reinvCheckMainService.get(id);
		}
		if (entity == null){
			entity = new ReinvCheckMain();
		}
		return entity;
	}
	
	/**
	 * 超期复检主表列表页面
	 */
	@RequiresPermissions("invcheckmain:reinvCheckMain:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "inventorymanage/invcheckmain/reinvCheckMainList";
	}
	
		/**
	 * 超期复检主表列表数据
	 */
	@ResponseBody
	@RequiresPermissions("invcheckmain:reinvCheckMain:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(ReinvCheckMain reinvCheckMain, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<ReinvCheckMain> page = reinvCheckMainService.findPage(new Page<ReinvCheckMain>(request, response), reinvCheckMain); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑超期复检主表表单页面
	 */
	@RequiresPermissions(value={"invcheckmain:reinvCheckMain:view","invcheckmain:reinvCheckMain:add","invcheckmain:reinvCheckMain:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(ReinvCheckMain reinvCheckMain, Model model) {
		model.addAttribute("reinvCheckMain", reinvCheckMain);
		if(StringUtils.isBlank(reinvCheckMain.getId())){//如果ID是空为添加
			model.addAttribute("isAdd", true);
		}
		return "inventorymanage/invcheckmain/reinvCheckMainForm";
	}

	/**
	 * 保存超期复检主表
	 */
	@RequiresPermissions(value={"invcheckmain:reinvCheckMain:add","invcheckmain:reinvCheckMain:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(ReinvCheckMain reinvCheckMain, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, reinvCheckMain)){
			return form(reinvCheckMain, model);
		}
		//新增或编辑表单保存
		reinvCheckMainService.save(reinvCheckMain);//保存
		addMessage(redirectAttributes, "保存超期复检主表成功");
		return "redirect:"+Global.getAdminPath()+"/invcheckmain/reinvCheckMain/?repage";
	}
	
	/**
	 * 删除超期复检主表
	 */
	@ResponseBody
	@RequiresPermissions("invcheckmain:reinvCheckMain:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(ReinvCheckMain reinvCheckMain, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		reinvCheckMainService.delete(reinvCheckMain);
		j.setMsg("删除超期复检主表成功");
		return j;
	}
	
	/**
	 * 批量删除超期复检主表
	 */
	@ResponseBody
	@RequiresPermissions("invcheckmain:reinvCheckMain:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			reinvCheckMainService.delete(reinvCheckMainService.get(id));
		}
		j.setMsg("删除超期复检主表成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("invcheckmain:reinvCheckMain:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(ReinvCheckMain reinvCheckMain, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "超期复检主表"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<ReinvCheckMain> page = reinvCheckMainService.findPage(new Page<ReinvCheckMain>(request, response, -1), reinvCheckMain);
    		new ExportExcel("超期复检主表", ReinvCheckMain.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出超期复检主表记录失败！失败信息："+e.getMessage());
		}
			return j;
    }
    
    @ResponseBody
    @RequestMapping(value = "detail")
	public ReinvCheckMain detail(String id) {
		return reinvCheckMainService.get(id);
	}
	

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("invcheckmain:reinvCheckMain:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<ReinvCheckMain> list = ei.getDataList(ReinvCheckMain.class);
			for (ReinvCheckMain reinvCheckMain : list){
				try{
					reinvCheckMainService.save(reinvCheckMain);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条超期复检主表记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条超期复检主表记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入超期复检主表失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/invcheckmain/reinvCheckMain/?repage";
    }
	
	/**
	 * 下载导入超期复检主表数据模板
	 */
	@RequiresPermissions("invcheckmain:reinvCheckMain:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "超期复检主表数据导入模板.xlsx";
    		List<ReinvCheckMain> list = Lists.newArrayList(); 
    		new ExportExcel("超期复检主表数据", ReinvCheckMain.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/invcheckmain/reinvCheckMain/?repage";
    }
	

}