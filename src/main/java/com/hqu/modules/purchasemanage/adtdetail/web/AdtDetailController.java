/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.purchasemanage.adtdetail.web;

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
import com.hqu.modules.purchasemanage.adtdetail.entity.AdtDetail;
import com.hqu.modules.purchasemanage.adtdetail.service.AdtDetailService;

/**
 * 审核表Controller
 * @author ckw
 * @version 2018-05-08
 */
@Controller
@RequestMapping(value = "${adminPath}/adtdetail/adtDetail")
public class AdtDetailController extends BaseController {

	@Autowired
	private AdtDetailService adtDetailService;
	
	@ModelAttribute
	public AdtDetail get(@RequestParam(required=false) String id) {
		AdtDetail entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = adtDetailService.get(id);
		}
		if (entity == null){
			entity = new AdtDetail();
		}
		return entity;
	}
	
	/**
	 * 审核表列表页面
	 */
	@RequiresPermissions("adtdetail:adtDetail:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "purchasemanage/adtdetail/adtDetailList";
	}
	
		/**
	 * 审核表列表数据
	 */
	@ResponseBody
	@RequiresPermissions("adtdetail:adtDetail:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(AdtDetail adtDetail, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<AdtDetail> page = adtDetailService.findPage(new Page<AdtDetail>(request, response), adtDetail); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑审核表表单页面
	 */
	@RequiresPermissions(value={"adtdetail:adtDetail:view","adtdetail:adtDetail:add","adtdetail:adtDetail:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(AdtDetail adtDetail, Model model) {
		model.addAttribute("adtDetail", adtDetail);
		if(StringUtils.isBlank(adtDetail.getId())){//如果ID是空为添加
			model.addAttribute("isAdd", true);
		}
		return "purchasemanage/adtdetail/adtDetailForm";
	}

	/**
	 * 保存审核表
	 */
	@RequiresPermissions(value={"adtdetail:adtDetail:add","adtdetail:adtDetail:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(AdtDetail adtDetail, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, adtDetail)){
			return form(adtDetail, model);
		}
		//新增或编辑表单保存
		adtDetailService.save(adtDetail);//保存
		addMessage(redirectAttributes, "保存审核表成功");
		return "redirect:"+Global.getAdminPath()+"/adtdetail/adtDetail/?repage";
	}
	
	/**
	 * 删除审核表
	 */
	@ResponseBody
	@RequiresPermissions("adtdetail:adtDetail:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(AdtDetail adtDetail, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		adtDetailService.delete(adtDetail);
		j.setMsg("删除审核表成功");
		return j;
	}
	
	/**
	 * 批量删除审核表
	 */
	@ResponseBody
	@RequiresPermissions("adtdetail:adtDetail:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			adtDetailService.delete(adtDetailService.get(id));
		}
		j.setMsg("删除审核表成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("adtdetail:adtDetail:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(AdtDetail adtDetail, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "审核表"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<AdtDetail> page = adtDetailService.findPage(new Page<AdtDetail>(request, response, -1), adtDetail);
    		new ExportExcel("审核表", AdtDetail.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出审核表记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("adtdetail:adtDetail:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<AdtDetail> list = ei.getDataList(AdtDetail.class);
			for (AdtDetail adtDetail : list){
				try{
					adtDetailService.save(adtDetail);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条审核表记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条审核表记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入审核表失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/adtdetail/adtDetail/?repage";
    }
	
	/**
	 * 下载导入审核表数据模板
	 */
	@RequiresPermissions("adtdetail:adtDetail:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "审核表数据导入模板.xlsx";
    		List<AdtDetail> list = Lists.newArrayList(); 
    		new ExportExcel("审核表数据", AdtDetail.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/adtdetail/adtDetail/?repage";
    }

}