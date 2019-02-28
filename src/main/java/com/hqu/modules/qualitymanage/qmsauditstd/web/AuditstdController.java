/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.qualitymanage.qmsauditstd.web;

import java.util.ArrayList;
import java.util.Iterator;
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
import com.hqu.modules.qualitymanage.qmsauditstd.entity.Auditstd;
import com.hqu.modules.qualitymanage.qmsauditstd.service.AuditstdService;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.excel.ExportExcel;
import com.jeeplus.common.utils.excel.ImportExcel;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.web.BaseController;

/**
 * 稽核标准Controller
 * @author syc
 * @version 2018-05-25
 */
@Controller
@RequestMapping(value = "${adminPath}/qmsauditstd/auditstd")
public class AuditstdController extends BaseController {

	@Autowired
	private AuditstdService auditstdService;
	
	@ModelAttribute
	public Auditstd get(@RequestParam(required=false) String id) {
		Auditstd entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = auditstdService.get(id);
		}
		if (entity == null){
			entity = new Auditstd();
		}
		return entity;
	}
	
	/**
	 * 稽核标准列表页面
	 */
	@RequiresPermissions("qmsauditstd:auditstd:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "qualitymanage/qmsauditstd/auditstdList";
	}
	
		/**
	 * 稽核标准列表数据
	 */
	@ResponseBody
	@RequiresPermissions("qmsauditstd:auditstd:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(Auditstd auditstd, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Auditstd> page = auditstdService.findPage(new Page<Auditstd>(request, response), auditstd); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑稽核标准表单页面
	 */
	@RequiresPermissions(value={"qmsauditstd:auditstd:view","qmsauditstd:auditstd:add","qmsauditstd:auditstd:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(Auditstd auditstd, Model model) {
		model.addAttribute("auditstd", auditstd);
		if(StringUtils.isBlank(auditstd.getId())){//如果ID是空为添加
			model.addAttribute("isAdd", true);
			return "qualitymanage/qmsauditstd/auditstdForm";
		}
		return "qualitymanage/qmsauditstd/auditstdFormEdit";
	}

	
	@ResponseBody
	@RequiresPermissions("qmsauditstd:auditstd:list")
	@RequestMapping(value = "myData")
	public Map<String, Object> myData(Auditstd auditstd, HttpServletRequest request, HttpServletResponse response, Model model) {
		//Page<Auditstd> page = auditstdService.findPage(new Page<Auditstd>(request, response), auditstd); 
		List<Auditstd> auditstdList=auditstdService.findList(auditstd);
		//稽核流程ID去重
		List<String> auditstdCode=new ArrayList<String>();
		Iterator<Auditstd> iteratorNew = auditstdList.iterator();
    	while(iteratorNew.hasNext()){
    		Auditstd report = iteratorNew.next();
    		if(auditstdCode.contains(report.getAuditpCode())) {
    			iteratorNew.remove();
    		}else{
    			auditstdCode.add(report.getAuditpCode());
    		}
    	}
		Page<Auditstd> page=new Page<Auditstd>(request, response);
		String intPage= request.getParameter("pageNo");
        int pageNo=Integer.parseInt(intPage);
        int pageSize= page.getPageSize();
        //不分页
        if(pageNo==1&&pageSize==-1){
	    	page.setList(auditstdList);
	    }else{
	        //分页显示
	    	List<Auditstd> reportL=  new ArrayList<Auditstd>();
	        for(int i=(pageNo-1)*pageSize;i<auditstdList.size() && i<pageNo*pageSize;i++){
	             reportL.add(auditstdList.get(i));	       
		      } 
	        page.setList(reportL);
	        page.setCount(reportL.size()); 
	    }      
		return getBootstrapData(page);
	}
	
	
	
	
	
	/**
	 * 保存稽核标准
	 */
	@RequiresPermissions(value={"qmsauditstd:auditstd:add","qmsauditstd:auditstd:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(Auditstd auditstd, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, auditstd)){
			return form(auditstd, model);
		}
		//新增或编辑表单保存
		auditstdService.save(auditstd);//保存
		addMessage(redirectAttributes, "保存稽核标准成功");
		return "redirect:"+Global.getAdminPath()+"/qmsauditstd/auditstd/?repage";
	}
	
	/**
	 * 删除稽核标准
	 */
	@ResponseBody
	@RequiresPermissions("qmsauditstd:auditstd:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(Auditstd auditstd, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		auditstdService.delete(auditstd);
		j.setMsg("删除稽核标准成功");
		return j;
	}
	
	/**
	 * 批量删除稽核标准
	 */
	@ResponseBody
	@RequiresPermissions("qmsauditstd:auditstd:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			auditstdService.delete(auditstdService.get(id));
		}
		j.setMsg("删除稽核标准成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("qmsauditstd:auditstd:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(Auditstd auditstd, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "稽核标准"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<Auditstd> page = auditstdService.findPage(new Page<Auditstd>(request, response, -1), auditstd);
    		new ExportExcel("稽核标准", Auditstd.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出稽核标准记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("qmsauditstd:auditstd:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<Auditstd> list = ei.getDataList(Auditstd.class);
			for (Auditstd auditstd : list){
				try{
					auditstdService.save(auditstd);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条稽核标准记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条稽核标准记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入稽核标准失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/qmsauditstd/auditstd/?repage";
    }
	
	/**
	 * 下载导入稽核标准数据模板
	 */
	@RequiresPermissions("qmsauditstd:auditstd:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "稽核标准数据导入模板.xlsx";
    		List<Auditstd> list = Lists.newArrayList(); 
    		new ExportExcel("稽核标准数据", Auditstd.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/qmsauditstd/auditstd/?repage";
    }
	
	
	
	@ResponseBody
	@RequestMapping(value = "ajax")
	public String ajax(Auditstd auditstd){
		String auditpCode = auditstd.getAuditpCode();
		String auditItemCode = auditstd.getAuditItemCode();
		
		Auditstd auditstd2 = new Auditstd();
		auditstd2.setAuditpCode(auditpCode);
		auditstd2.setAuditItemCode(auditItemCode);
		
		List<Auditstd> findList = auditstdService.findList(auditstd2);
		if(findList!=null&&findList.size()!=0){
			return "1";
		}
		return "0";
	}
	
	
	
	
	
	

}