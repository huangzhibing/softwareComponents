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
import com.hqu.modules.purchasemanage.contractmain.entity.ContractMain;
import com.hqu.modules.purchasemanage.contractmain.service.ContractMainSummaryService;

/**
 * 采购订单汇总Controller
 * @author ltq
 * @version 2018-04-28
 */
@Controller
@RequestMapping(value = "${adminPath}/contractmain/contractMainSummary")
public class ContractMainSummaryController extends BaseController {

	@Autowired
	private ContractMainSummaryService contractMainSummaryService;
	
	@ModelAttribute
	public ContractDetailSummary get(@RequestParam(required=false) String id) {
		ContractDetailSummary entity = null;
		if (StringUtils.isNotBlank(id)){
			//entity = contractMainSummaryService.get(id);
		}
		if (entity == null){
			entity = new ContractDetailSummary();
		}
		return entity;
	}
	
	/**
	 * 采购合同管理列表页面
	 */
	@RequiresPermissions("contractmainsummary:contractMain:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "purchasemanage/contractmain/contractMainSummaryList";
	}	
	
	/**
	 * 采购合同管理列表数据
	 */
	@ResponseBody
	@RequiresPermissions("contractmainsummary:contractMain:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(ContractDetailSummary contractDetailSummary, HttpServletRequest request, HttpServletResponse response, Model model) {
	
		Page<ContractDetailSummary> page = contractMainSummaryService.findPage(new Page<ContractDetailSummary>(request, response), contractDetailSummary); 		 		   
		return getBootstrapData(page);
	}
	
	/**
	 * 导出excel文件
	 */
		@ResponseBody
	@RequiresPermissions("contractmainsummary:contractMain:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(ContractDetailSummary contractDetailSummary, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "采购订单汇总表"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
    		Page<ContractDetailSummary> page = contractMainSummaryService.findPage(new Page<ContractDetailSummary>(request, response,-1), contractDetailSummary); 		 
    		new ExportExcel("采购订单汇总表", ContractDetailSummary.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出采购订单汇总表失败！失败信息："+e.getMessage());
		}
			return j;
    }
    
  
	
	/**
	 * 下载导入采购合同管理数据模板
	 */
	/*@RequiresPermissions("contractmainunfreeze:contractMain:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "采购合同管理数据导入模板.xlsx";
    		List<ContractMain> list = Lists.newArrayList(); 
    		new ExportExcel("采购合同管理数据", ContractMain.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/contractmain/contractMainSummary/?repage";
	}
	*/
	
}