/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.inventorymanage.billmain.web;

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
import com.hqu.modules.inventorymanage.billmain.entity.BillMain;
import com.hqu.modules.inventorymanage.billmain.service.BillMainService;

/**
 * 出入库单据Controller
 * @author M1ngz
 * @version 2018-04-16
 */
@Controller
@RequestMapping(value = "${adminPath}/billmain/billMain")
public class BillMainController extends BaseController {

	@Autowired
	private BillMainService billMainService;
	
	@ModelAttribute
	public BillMain get(@RequestParam(required=false) String id) {
		BillMain entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = billMainService.get(id);
		}
		if (entity == null){
			entity = new BillMain();
		}
		return entity;
	}
	
	/**
	 * 单据列表页面
	 */
	@RequiresPermissions("billmain:billMain:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "inventorymanage/billmain/billMainList";
	}
	
		/**
	 * 单据列表数据
	 */
	@ResponseBody
	@RequiresPermissions("billmain:billMain:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(BillMain billMain, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<BillMain> page = billMainService.findPage(new Page<BillMain>(request, response), billMain); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑单据表单页面
	 */
	@RequiresPermissions(value={"billmain:billMain:view","billmain:billMain:add","billmain:billMain:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(BillMain billMain, Model model) {
		model.addAttribute("billMain", billMain);
		if(StringUtils.isBlank(billMain.getId())){//如果ID是空为添加
			model.addAttribute("isAdd", true);
		}
		return "inventorymanage/billmain/billMainForm";
	}

	/**
	 * 保存单据
	 */
	@RequiresPermissions(value={"billmain:billMain:add","billmain:billMain:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(BillMain billMain, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, billMain)){
			return form(billMain, model);
		}
		//新增或编辑表单保存
		billMainService.save(billMain);//保存
		addMessage(redirectAttributes, "保存单据成功");
		return "redirect:"+Global.getAdminPath()+"/billmain/billMain/?repage";
	}
	
	/**
	 * 删除单据
	 */
	@ResponseBody
	@RequiresPermissions("billmain:billMain:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(BillMain billMain, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		billMainService.delete(billMain);
		j.setMsg("删除单据成功");
		return j;
	}
	
	/**
	 * 批量删除单据
	 */
	@ResponseBody
	@RequiresPermissions("billmain:billMain:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			billMainService.delete(billMainService.get(id));
		}
		j.setMsg("删除单据成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("billmain:billMain:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(BillMain billMain, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "单据"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<BillMain> page = billMainService.findPage(new Page<BillMain>(request, response, -1), billMain);
    		new ExportExcel("单据", BillMain.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出单据记录失败！失败信息："+e.getMessage());
		}
			return j;
    }
    
    @ResponseBody
    @RequestMapping(value = "detail")
	public BillMain detail(String id) {
		return billMainService.get(id);
	}
	

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("billmain:billMain:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<BillMain> list = ei.getDataList(BillMain.class);
			for (BillMain billMain : list){
				try{
					billMainService.save(billMain);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条单据记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条单据记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入单据失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/billmain/billMain/?repage";
    }
	
	/**
	 * 下载导入单据数据模板
	 */
	@RequiresPermissions("billmain:billMain:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "单据数据导入模板.xlsx";
    		List<BillMain> list = Lists.newArrayList(); 
    		new ExportExcel("单据数据", BillMain.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/billmain/billMain/?repage";
    }
	

}