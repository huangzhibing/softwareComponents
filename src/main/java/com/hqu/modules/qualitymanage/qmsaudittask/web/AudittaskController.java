/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.qualitymanage.qmsaudittask.web;

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
import com.hqu.modules.purchasemanage.group.entity.GroupBuyer;
import com.hqu.modules.purchasemanage.purplan.entity.PurPlanDetail;
import com.hqu.modules.qualitymanage.qmsauditstd.entity.Auditstd;
import com.hqu.modules.qualitymanage.qmsauditstd.service.AuditstdService;
import com.hqu.modules.qualitymanage.qmsaudittask.entity.Audittask;
import com.hqu.modules.qualitymanage.qmsaudittask.service.AudittaskService;

/**
 * 稽核任务Controller
 * @author syc
 * @version 2018-05-25
 */
@Controller
@RequestMapping(value = "${adminPath}/qmsaudittask/audittask")
public class AudittaskController extends BaseController {

	@Autowired
	private AudittaskService audittaskService;
	@Autowired
	private AuditstdService auditstdService;
	@ModelAttribute
	public Audittask get(@RequestParam(required=false) String id) {
		Audittask entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = audittaskService.get(id);
		}
		if (entity == null){
			entity = new Audittask();
		}
		return entity;
	}
	
	/**
	 * 稽核任务列表页面
	 */
	@RequiresPermissions("qmsaudittask:audittask:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "qualitymanage/qmsaudittask/audittaskList";
	}
	
		/**
	 * 稽核任务列表数据
	 */
	@ResponseBody
	@RequiresPermissions("qmsaudittask:audittask:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(Audittask audittask, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Audittask> page = audittaskService.findPage(new Page<Audittask>(request, response), audittask); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑稽核任务表单页面
	 */
	@RequiresPermissions(value={"qmsaudittask:audittask:view","qmsaudittask:audittask:add","qmsaudittask:audittask:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(Audittask audittask, Model model) {
		model.addAttribute("audittask", audittask);
		if(StringUtils.isBlank(audittask.getId())){//如果ID是空为添加
			model.addAttribute("isAdd", true);
		}
		return "qualitymanage/qmsaudittask/audittaskForm";
	}

	
	@ResponseBody
	@RequiresPermissions("qmsaudittask:audittask:list")
	@RequestMapping(value = "getAuditStd")
	public Map<String, Object> getAuditStd(@RequestParam(required=false) String code, HttpServletRequest request, HttpServletResponse response, Model model) {
		 Page<Auditstd>  page=new Page<Auditstd>();
		 Auditstd auditstd=new Auditstd();
		 auditstd.setAuditpCode(code);
		 List<Auditstd> auditstdList=auditstdService.findList(auditstd);
		 page.setList(auditstdList);
		 page.setCount(auditstdList.size());
		 return getBootstrapData(page);
	}
	
	
	/**
	 * 保存稽核任务
	 */
	@RequiresPermissions(value={"qmsaudittask:audittask:add","qmsaudittask:audittask:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(Audittask audittask, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, audittask)){
			return form(audittask, model);
		}
		//新增或编辑表单保存
		audittaskService.save(audittask);//保存
		addMessage(redirectAttributes, "保存稽核任务成功");
		return "redirect:"+Global.getAdminPath()+"/qmsaudittask/audittask/?repage";
	}
	
	/**
	 * 删除稽核任务
	 */
	@ResponseBody
	@RequiresPermissions("qmsaudittask:audittask:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(Audittask audittask, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		audittaskService.delete(audittask);
		j.setMsg("删除稽核任务成功");
		return j;
	}
	
	/**
	 * 批量删除稽核任务
	 */
	@ResponseBody
	@RequiresPermissions("qmsaudittask:audittask:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			audittaskService.delete(audittaskService.get(id));
		}
		j.setMsg("删除稽核任务成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("qmsaudittask:audittask:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(Audittask audittask, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "稽核任务"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<Audittask> page = audittaskService.findPage(new Page<Audittask>(request, response, -1), audittask);
    		new ExportExcel("稽核任务", Audittask.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出稽核任务记录失败！失败信息："+e.getMessage());
		}
			return j;
    }
    
    @ResponseBody
    @RequestMapping(value = "detail")
	public Audittask detail(String id) {
		return audittaskService.get(id);
	}
	

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("qmsaudittask:audittask:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<Audittask> list = ei.getDataList(Audittask.class);
			for (Audittask audittask : list){
				try{
					audittaskService.save(audittask);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条稽核任务记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条稽核任务记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入稽核任务失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/qmsaudittask/audittask/?repage";
    }
	
	/**
	 * 下载导入稽核任务数据模板
	 */
	@RequiresPermissions("qmsaudittask:audittask:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "稽核任务数据导入模板.xlsx";
    		List<Audittask> list = Lists.newArrayList(); 
    		new ExportExcel("稽核任务数据", Audittask.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/qmsaudittask/audittask/?repage";
    }
	

}