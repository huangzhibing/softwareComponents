/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.inventorymanage.billtype.web;

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
import com.hqu.modules.inventorymanage.billtype.entity.BillType;
import com.hqu.modules.inventorymanage.billtype.service.BillTypeService;

/**
 * 单据类型定义Controller
 * @author dongqida
 * @version 2018-04-14
 */
@Controller
@RequestMapping(value = "${adminPath}/billtype/billType")
public class BillTypeController extends BaseController {

	@Autowired
	private BillTypeService billTypeService;
	
	@ModelAttribute
	public BillType get(@RequestParam(required=false) String id) {
		BillType entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = billTypeService.get(id);
		}
		if (entity == null){
			entity = new BillType();
		}
		return entity;
	}
	
	/**
	 * 单据类型定义列表页面
	 */
	@RequiresPermissions("billtype:billType:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "inventorymanage/billtype/billTypeList";
	}
	
		/**
	 * 单据类型定义列表数据
	 */
	@ResponseBody
	@RequiresPermissions("billtype:billType:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(BillType billType, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<BillType> page = billTypeService.findPage(new Page<BillType>(request, response), billType); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑单据类型定义表单页面
	 */
	@RequiresPermissions(value={"billtype:billType:view","billtype:billType:add","billtype:billType:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(BillType billType, Model model) {
		model.addAttribute("billType", billType);
		if(StringUtils.isBlank(billType.getId())){//如果ID是空为添加
			model.addAttribute("isAdd", true);
		}
		return "inventorymanage/billtype/billTypeForm";
	}

	/**
	 * 保存单据类型定义
	 */
	@RequiresPermissions(value={"billtype:billType:add","billtype:billType:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(BillType billType, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, billType)){
			return form(billType, model);
		}
		//System.out.println(billType.getBillTypeWareHouseList().get(0).getWareHouse().getWareID()+"-----++++");
		//新增或编辑表单保存
		billTypeService.save(billType);//保存
		addMessage(redirectAttributes, "保存单据类型定义成功");
		return "redirect:"+Global.getAdminPath()+"/billtype/billType/?repage";
	}
	
	/**
	 * 删除单据类型定义
	 */
	@ResponseBody
	@RequiresPermissions("billtype:billType:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(BillType billType, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		billTypeService.delete(billType);
		j.setMsg("删除单据类型定义成功");
		return j;
	}
	
	/**
	 * 批量删除单据类型定义
	 */
	@ResponseBody
	@RequiresPermissions("billtype:billType:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			billTypeService.delete(billTypeService.get(id));
		}
		j.setMsg("删除单据类型定义成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("billtype:billType:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(BillType billType, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "单据类型定义"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<BillType> page = billTypeService.findPage(new Page<BillType>(request, response, -1), billType);
    		new ExportExcel("单据类型定义", BillType.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出单据类型定义记录失败！失败信息："+e.getMessage());
		}
			return j;
    }
    
    @ResponseBody
    @RequestMapping(value = "detail")
	public BillType detail(String id) {
		return billTypeService.get(id);
	}
	

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("billtype:billType:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<BillType> list = ei.getDataList(BillType.class);
			for (BillType billType : list){
				try{
					billTypeService.save(billType);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条单据类型定义记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条单据类型定义记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入单据类型定义失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/billtype/billType/?repage";
    }
	
	/**
	 * 下载导入单据类型定义数据模板
	 */
	@RequiresPermissions("billtype:billType:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "单据类型定义数据导入模板.xlsx";
    		List<BillType> list = Lists.newArrayList(); 
    		new ExportExcel("单据类型定义数据", BillType.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/billtype/billType/?repage";
    }
	

}