/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.purchasemanage.supprice.web;

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
import com.hqu.modules.purchasemanage.supprice.entity.PurSupPrice;
import com.hqu.modules.purchasemanage.supprice.service.PurSupPriceService;

/**
 * 供应商价格维护Controller
 * @author syc
 * @version 2018-08-02
 */
@Controller
@RequestMapping(value = "${adminPath}/supprice/purSupPrice")
public class PurSupPriceController extends BaseController {

	@Autowired
	private PurSupPriceService purSupPriceService;
	
	@ModelAttribute
	public PurSupPrice get(@RequestParam(required=false) String id) {
		PurSupPrice entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = purSupPriceService.get(id);
		}
		if (entity == null){
			entity = new PurSupPrice();
		}
		return entity;
	}
	
	/**
	 * 供应商价格维护列表页面
	 */
	@RequiresPermissions("supprice:purSupPrice:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "purchasemanage/supprice/purSupPriceList";
	}
	
		/**
	 * 供应商价格维护列表数据
	 */
	@ResponseBody
	@RequiresPermissions("supprice:purSupPrice:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(PurSupPrice purSupPrice, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<PurSupPrice> page = purSupPriceService.findPage(new Page<PurSupPrice>(request, response), purSupPrice); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑供应商价格维护表单页面
	 */
	@RequiresPermissions(value={"supprice:purSupPrice:view","supprice:purSupPrice:add","supprice:purSupPrice:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(PurSupPrice purSupPrice, Model model) {
		model.addAttribute("purSupPrice", purSupPrice);
		if(StringUtils.isBlank(purSupPrice.getId())){//如果ID是空为添加
			model.addAttribute("isAdd", true);
		}
		return "purchasemanage/supprice/purSupPriceForm";
	}

	/**
	 * 保存供应商价格维护
	 */
	@RequiresPermissions(value={"supprice:purSupPrice:add","supprice:purSupPrice:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(PurSupPrice purSupPrice, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, purSupPrice)){
			return form(purSupPrice, model);
		}
		//新增或编辑表单保存
		purSupPriceService.save(purSupPrice);//保存
		addMessage(redirectAttributes, "保存供应商价格维护成功");
		return "redirect:"+Global.getAdminPath()+"/supprice/purSupPrice/?repage";
	}
	
	/**
	 * 删除供应商价格维护
	 */
	@ResponseBody
	@RequiresPermissions("supprice:purSupPrice:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(PurSupPrice purSupPrice, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		purSupPriceService.delete(purSupPrice);
		j.setMsg("删除供应商价格维护成功");
		return j;
	}
	
	/**
	 * 批量删除供应商价格维护
	 */
	@ResponseBody
	@RequiresPermissions("supprice:purSupPrice:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			purSupPriceService.delete(purSupPriceService.get(id));
		}
		j.setMsg("删除供应商价格维护成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("supprice:purSupPrice:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(PurSupPrice purSupPrice, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "供应商价格维护"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<PurSupPrice> page = purSupPriceService.findPage(new Page<PurSupPrice>(request, response, -1), purSupPrice);
    		new ExportExcel("供应商价格维护", PurSupPrice.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出供应商价格维护记录失败！失败信息："+e.getMessage());
		}
			return j;
    }
    
    @ResponseBody
    @RequestMapping(value = "detail")
	public PurSupPrice detail(String id) {
		return purSupPriceService.get(id);
	}
	

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("supprice:purSupPrice:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<PurSupPrice> list = ei.getDataList(PurSupPrice.class);
			for (PurSupPrice purSupPrice : list){
				try{
					purSupPriceService.save(purSupPrice);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条供应商价格维护记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条供应商价格维护记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入供应商价格维护失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/supprice/purSupPrice/?repage";
    }
	
	/**
	 * 下载导入供应商价格维护数据模板
	 */
	@RequiresPermissions("supprice:purSupPrice:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "供应商价格维护数据导入模板.xlsx";
    		List<PurSupPrice> list = Lists.newArrayList(); 
    		new ExportExcel("供应商价格维护数据", PurSupPrice.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/supprice/purSupPrice/?repage";
    }
	

}