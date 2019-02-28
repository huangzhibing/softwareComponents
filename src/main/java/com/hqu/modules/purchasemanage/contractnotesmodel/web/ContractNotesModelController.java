/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.purchasemanage.contractnotesmodel.web;

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
import com.hqu.modules.purchasemanage.contractnotesmodel.entity.ContractNotesModel;
import com.hqu.modules.purchasemanage.contractnotesmodel.service.ContractNotesModelService;

/**
 * 合同模板定义Controller
 * @author luyumiao
 * @version 2018-04-25
 */
@Controller
@RequestMapping(value = "${adminPath}/contractnotesmodel/contractNotesModel")
public class ContractNotesModelController extends BaseController {

	@Autowired
	private ContractNotesModelService contractNotesModelService;
	
	@ModelAttribute
	public ContractNotesModel get(@RequestParam(required=false) String id) {
		ContractNotesModel entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = contractNotesModelService.get(id);
		}
		if (entity == null){
			entity = new ContractNotesModel();
		}
		return entity;
	}
	
	/**
	 * 合同模板定义列表页面
	 */
	@RequiresPermissions("contractnotesmodel:contractNotesModel:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "purchasemanage/contractnotesmodel/contractNotesModelList";
	}
	
		/**
	 * 合同模板定义列表数据
	 */
	@ResponseBody
	@RequiresPermissions("contractnotesmodel:contractNotesModel:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(ContractNotesModel contractNotesModel, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<ContractNotesModel> page = contractNotesModelService.findPage(new Page<ContractNotesModel>(request, response), contractNotesModel); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑合同模板定义表单页面
	 */
	@RequiresPermissions(value={"contractnotesmodel:contractNotesModel:view","contractnotesmodel:contractNotesModel:add","contractnotesmodel:contractNotesModel:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(ContractNotesModel contractNotesModel, Model model) {
		model.addAttribute("contractNotesModel", contractNotesModel);
		if(StringUtils.isBlank(contractNotesModel.getId())){//如果ID是空为添加
			model.addAttribute("isAdd", true);
			//设置typecode从0开始自增长并为6位流水压入表单中
			String typecode = contractNotesModelService.getMaxContractId();
			if(typecode == null) {
				typecode = "000000";
			}
			else {
				int maxnum = Integer.parseInt(typecode) + 1;
				typecode = String.format("%06d",maxnum);
			}

			contractNotesModel.setContractId(typecode);
		}
		return "purchasemanage/contractnotesmodel/contractNotesModelForm";
	}

	/**
	 * 保存合同模板定义
	 */
	@RequiresPermissions(value={"contractnotesmodel:contractNotesModel:add","contractnotesmodel:contractNotesModel:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(ContractNotesModel contractNotesModel, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, contractNotesModel)){
			return form(contractNotesModel, model);
		}
		//新增或编辑表单保存
		contractNotesModelService.save(contractNotesModel);//保存
		addMessage(redirectAttributes, "保存合同模板定义成功");
		return "redirect:"+Global.getAdminPath()+"/contractnotesmodel/contractNotesModel/?repage";
	}
	
	/**
	 * 删除合同模板定义
	 */
	@ResponseBody
	@RequiresPermissions("contractnotesmodel:contractNotesModel:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(ContractNotesModel contractNotesModel, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		contractNotesModelService.delete(contractNotesModel);
		j.setMsg("删除合同模板定义成功");
		return j;
	}
	
	/**
	 * 批量删除合同模板定义
	 */
	@ResponseBody
	@RequiresPermissions("contractnotesmodel:contractNotesModel:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			contractNotesModelService.delete(contractNotesModelService.get(id));
		}
		j.setMsg("删除合同模板定义成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("contractnotesmodel:contractNotesModel:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(ContractNotesModel contractNotesModel, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "合同模板定义"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<ContractNotesModel> page = contractNotesModelService.findPage(new Page<ContractNotesModel>(request, response, -1), contractNotesModel);
    		new ExportExcel("合同模板定义", ContractNotesModel.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出合同模板定义记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("contractnotesmodel:contractNotesModel:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<ContractNotesModel> list = ei.getDataList(ContractNotesModel.class);
			for (ContractNotesModel contractNotesModel : list){
				try{
					contractNotesModelService.save(contractNotesModel);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条合同模板定义记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条合同模板定义记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入合同模板定义失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/contractnotesmodel/contractNotesModel/?repage";
    }
	
	/**
	 * 下载导入合同模板定义数据模板
	 */
	@RequiresPermissions("contractnotesmodel:contractNotesModel:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "合同模板定义数据导入模板.xlsx";
    		List<ContractNotesModel> list = Lists.newArrayList(); 
    		new ExportExcel("合同模板定义数据", ContractNotesModel.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/contractnotesmodel/contractNotesModel/?repage";
    }

}