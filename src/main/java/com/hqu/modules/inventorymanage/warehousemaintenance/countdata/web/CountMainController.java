/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.inventorymanage.warehousemaintenance.countdata.web;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import com.jeeplus.modules.sys.utils.UserUtils;
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
import com.hqu.modules.inventorymanage.warehousemaintenance.countdata.entity.CountMain;
import com.hqu.modules.inventorymanage.warehousemaintenance.countdata.service.CountMainService;

/**
 * 盘点数据录入Controller
 * @author zb
 * @version 2018-04-23
 */
@Controller
@RequestMapping(value = "${adminPath}/countdata/countMain")
public class CountMainController extends BaseController {

	@Autowired
	private CountMainService countMainService;
	
	@ModelAttribute
	public CountMain get(@RequestParam(required=false) String id) {
		CountMain entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = countMainService.get(id);
		}
		if (entity == null){
			entity = new CountMain();
		}
		return entity;
	}
	
	/**
	 * 盘点数据列表页面
	 */
	@RequiresPermissions("countdata:countMain:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "inventorymanage/warehousemaintenance/countdata/countMainList";
	}
	
		/**
	 * 盘点数据列表数据
	 */
	@ResponseBody
	@RequiresPermissions("countdata:countMain:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(CountMain countMain, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<CountMain> page = countMainService.findPage(new Page<CountMain>(request, response), countMain); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑盘点数据表单页面
	 */
	@RequiresPermissions(value={"countdata:countMain:view","countdata:countMain:add","countdata:countMain:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(CountMain countMain, Model model) {
		model.addAttribute("countMain", countMain);
		if(StringUtils.isBlank(countMain.getId())){//如果ID是空为添加
			model.addAttribute("isAdd", true);
		}
		countMain.setAdjEmpId(UserUtils.getUser());
		return "inventorymanage/warehousemaintenance/countdata/countMainForm";
	}

	/**
	 * 保存盘点数据
	 */
	@RequiresPermissions(value={"countdata:countMain:add","countdata:countMain:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(CountMain countMain, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, countMain)){
			return form(countMain, model);
		}
		//新增或编辑表单保存
        countMain.setProcFlag("Y");
		countMainService.save(countMain);//保存
		addMessage(redirectAttributes, "保存盘点数据成功");
		return "redirect:"+Global.getAdminPath()+"/countdata/countMain/?repage";
	}
	
	/**
	 * 删除盘点数据
	 */
	@ResponseBody
	@RequiresPermissions("countdata:countMain:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(CountMain countMain, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		countMainService.delete(countMain);
		j.setMsg("删除盘点数据成功");
		return j;
	}
	
	/**
	 * 批量删除盘点数据
	 */
	@ResponseBody
	@RequiresPermissions("countdata:countMain:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			countMainService.delete(countMainService.get(id));
		}
		j.setMsg("删除盘点数据成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("countdata:countMain:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(CountMain countMain, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "盘点数据"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<CountMain> page = countMainService.findPage(new Page<CountMain>(request, response, -1), countMain);
    		new ExportExcel("盘点数据", CountMain.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出盘点数据记录失败！失败信息："+e.getMessage());
		}
			return j;
    }
    
    @ResponseBody
    @RequestMapping(value = "detail")
	public CountMain detail(String id) {
		return countMainService.get(id);
	}
	

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("countdata:countMain:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<CountMain> list = ei.getDataList(CountMain.class);
			for (CountMain countMain : list){
				try{
					countMainService.save(countMain);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条盘点数据记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条盘点数据记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入盘点数据失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/countdata/countMain/?repage";
    }
	
	/**
	 * 下载导入盘点数据数据模板
	 */
	@RequiresPermissions("countdata:countMain:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "盘点数据数据导入模板.xlsx";
    		List<CountMain> list = Lists.newArrayList(); 
    		new ExportExcel("盘点数据数据", CountMain.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/countdata/countMain/?repage";
    }
	

}