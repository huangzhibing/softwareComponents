/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.purchasemanage.contractmain.web;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

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
import com.hqu.modules.purchasemanage.contractmain.entity.ContractDetailSummary;
import com.hqu.modules.purchasemanage.contractmain.entity.ContractDetailWarning;
import com.hqu.modules.purchasemanage.contractmain.entity.ContractMain;
import com.hqu.modules.purchasemanage.contractmain.service.ContractMainSummaryService;
import com.hqu.modules.purchasemanage.contractmain.service.ContractMainWarningService;
import com.hqu.modules.qualitymanage.purreport.entity.PurReport;

/**
 * 采购订单汇总Controller
 * @author ltq
 * @version 2018-04-28
 */
@Controller
@RequestMapping(value = "${adminPath}/contractmain/contractMainWarning")
public class ContractMainWarningController extends BaseController {

	@Autowired
	private ContractMainWarningService contractMainWarningService;
	
	@ModelAttribute
	public ContractDetailWarning get(@RequestParam(required=false) String id) {
		ContractDetailWarning entity = null;
		if (StringUtils.isNotBlank(id)){
			//entity = contractMainSummaryService.get(id);
		}
		if (entity == null){
			entity = new ContractDetailWarning();
		}
		return entity;
	}
	
	/**
	 * 采购合同管理列表页面
	 */
	@RequiresPermissions("contractmainwarning:contractMain:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "purchasemanage/contractmain/contractMainWarningList";
	}	
	
	/**
	 * 采购合同管理列表数据
	 */
	@ResponseBody
	@RequiresPermissions("contractmainwarning:contractMain:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(ContractDetailWarning contractDetailWarning, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<ContractDetailWarning> page = contractMainWarningService.findPage(new Page<ContractDetailWarning>(request, response,-1), contractDetailWarning); 		 		
		return getBootstrapData(page);
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("contractmainsummary:contractMain:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(ContractDetailWarning contractDetailWarning, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "采购订单交期预警表"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
    		Page<ContractDetailWarning> page = contractMainWarningService.findPage(new Page<ContractDetailWarning>(request, response,-1), contractDetailWarning); 		 
    		new ExportExcel("采购订单交期预警表", ContractDetailWarning.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出采购订单交期预警表失败！失败信息："+e.getMessage());
		}
			return j;
    }
    
 
	
	/**
	 * 下载导入采购合同管理数据模板
	 */
	/*@RequiresPermissions("contractmainwarning:contractMain:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "采购合同管理数据导入模板.xlsx";
    		List<ContractDetailWarning> list = Lists.newArrayList(); 
    		new ExportExcel("采购合同管理数据", ContractMain.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/contractmain/contractMainWarning/?repage";
	}
	*/
	
}