/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.purchasemanage.contracttype.web;

import com.google.common.collect.Lists;
import com.hqu.modules.purchasemanage.contracttype.entity.ContractType;
import com.hqu.modules.purchasemanage.contracttype.service.ContractTypeService;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.excel.ExportExcel;
import com.jeeplus.common.utils.excel.ImportExcel;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.web.BaseController;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Map;

/**
 * 合同类型定义Controller
 * @author tyo
 * @version 2018-05-26
 */
@Controller
@RequestMapping(value = "${adminPath}/contracttype/contractType")
public class ContractTypeController extends BaseController {

	@Autowired
	private ContractTypeService contractTypeService;
	
	@ModelAttribute
	public ContractType get(@RequestParam(required=false) String id) {
		ContractType entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = contractTypeService.get(id);
		}
		if (entity == null){
			entity = new ContractType();
		}
		return entity;
	}
	
	/**
	 * 合同类型定义列表页面
	 */
	@RequiresPermissions("contracttype:contractType:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "purchasemanage/contracttype/contractTypeList";
	}
	
		/**
	 * 合同类型定义列表数据
	 */
	@ResponseBody
	@RequiresPermissions("contracttype:contractType:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(ContractType contractType, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<ContractType> page = contractTypeService.findPage(new Page<ContractType>(request, response), contractType); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑合同类型定义表单页面
	 */
	@RequiresPermissions(value={"contracttype:contractType:view","contracttype:contractType:add","contracttype:contractType:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(ContractType contractType, Model model) {
		model.addAttribute("contractType", contractType);
		if(StringUtils.isBlank(contractType.getId())){//如果ID是空为添加
			model.addAttribute("isAdd", true);


			String contractTypeID=contractTypeService.getMaxContractTypeID();
			if (contractTypeID==null) {
				contractTypeID = "00";

			}else {

				try {
					int maxnum = Integer.parseInt((contractTypeID)) + 1;
					contractTypeID=String.format("%03d",maxnum);
				} catch (NumberFormatException e) {
					System.out.println("number parse error");
				}

			}
			contractType.setContypeid(contractTypeID);

		}
		return "purchasemanage/contracttype/contractTypeForm";
	}

	/**
	 * 保存合同类型定义
	 */
	@RequiresPermissions(value={"contracttype:contractType:add","contracttype:contractType:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(ContractType contractType, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, contractType)){
			return form(contractType, model);
		}
		//新增或编辑表单保存
		contractTypeService.save(contractType);//保存
		addMessage(redirectAttributes, "保存合同类型定义成功");
		return "redirect:"+Global.getAdminPath()+"/contracttype/contractType/?repage";
	}
	
	/**
	 * 删除合同类型定义
	 */
	@ResponseBody
	@RequiresPermissions("contracttype:contractType:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(ContractType contractType, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		contractTypeService.delete(contractType);
		j.setMsg("删除合同类型定义成功");
		return j;
	}
	
	/**
	 * 批量删除合同类型定义
	 */
	@ResponseBody
	@RequiresPermissions("contracttype:contractType:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			contractTypeService.delete(contractTypeService.get(id));
		}
		j.setMsg("删除合同类型定义成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("contracttype:contractType:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(ContractType contractType, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "合同类型定义"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<ContractType> page = contractTypeService.findPage(new Page<ContractType>(request, response, -1), contractType);
    		new ExportExcel("合同类型定义", ContractType.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出合同类型定义记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("contracttype:contractType:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<ContractType> list = ei.getDataList(ContractType.class);
			for (ContractType contractType : list){
				try{
					contractTypeService.save(contractType);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条合同类型定义记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条合同类型定义记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入合同类型定义失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/contracttype/contractType/?repage";
    }
	
	/**
	 * 下载导入合同类型定义数据模板
	 */
	@RequiresPermissions("contracttype:contractType:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "合同类型定义数据导入模板.xlsx";
    		List<ContractType> list = Lists.newArrayList(); 
    		new ExportExcel("合同类型定义数据", ContractType.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/contracttype/contractType/?repage";
    }

}