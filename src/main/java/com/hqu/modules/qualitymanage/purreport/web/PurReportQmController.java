/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.qualitymanage.purreport.web;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
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
import com.hqu.modules.qualitymanage.purreport.entity.PurReport;
import com.hqu.modules.qualitymanage.purreport.service.PurReportQmService;
import com.hqu.modules.qualitymanage.qmreport.entity.QmReport;

/**
 * 检验单(采购/装配/)质量问题处理Controller
 * @author 杨贤邦
 * @version 2018-05-04
 */
@Controller
@RequestMapping(value = "${adminPath}/purreport/purQmReport")
public class PurReportQmController extends BaseController {

	@Autowired
	private PurReportQmService purReportService;
	
	@ModelAttribute
	public PurReport get(@RequestParam(required=false) String id) {
		PurReport entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = purReportService.get(id);
		}
		if (entity == null){
			entity = new PurReport();
		}
		return entity;
	}

	
		/**
	 * 检验单列表数据
	 */
	@ResponseBody
	@RequiresPermissions("purreport:purReport:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(PurReport purReport, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<PurReport> page = purReportService.findPage(new Page<PurReport>(request, response), purReport); 
		return getBootstrapData(page);
	}


	//--------------------------------------------采购检验单---------------------------------------------------------//


	/**
	 * 采购检验单列表数据(已审核的数据)
	 */
	@ResponseBody
	@RequiresPermissions("purreport:purReport:list")
	@RequestMapping(value = "dataAudited")
	public Map<String, Object> dataAudited(PurReport purReport, HttpServletRequest request, HttpServletResponse response, Model model) {
		purReport.setState("已审核");
		purReport.setChecktName("采购");
		Page<PurReport> page = purReportService.findPage(new Page<PurReport>(request, response), purReport);
		List<PurReport> purReList =new ArrayList<PurReport>();
		for(PurReport pur:page.getList()) {
			purReList.add(pur);
		}
		for(PurReport pur:page.getList()) {
			if(("合格").equals(pur.getCheckResult())||"2".equals(pur.getIsQmatter())) {
				purReList.remove(pur);
			}
		}
		page.setCount(purReList.size());
		page.setList(purReList);
		return getBootstrapData(page);
	}

	/**
	 * 未处理的采购检验单数量
	 */
	@ResponseBody
	@RequiresPermissions("purreport:purReport:list")
	@RequestMapping(value = "dataNotProcessNum")
	public AjaxJson dataNotProcessNum(PurReport purReport, HttpServletRequest request, HttpServletResponse response, Model model) {
		AjaxJson j = new AjaxJson();
		long total=0;
		long editNum=0;
		long submitNum=0;
		List<PurReport> list;
		purReport.setChecktName("采购");
		purReport.setState("已审核");
		purReport.setCheckResult("不合格");
		Page<PurReport> page = purReportService.findPage(new Page<PurReport>(request, response), purReport);
		total=page.getCount();
		purReport.setCheckResult("部分合格");
		page=purReportService.findPage(new Page<PurReport>(request, response), purReport);
		total+=page.getCount();

		purReport.setIsQmatter("2");
		purReport.setCheckResult("不合格");
		page = purReportService.findPage(new Page<PurReport>(request, response), purReport);
		submitNum=page.getCount();
		purReport.setCheckResult("部分合格");
		list=purReportService.findList(purReport);
		submitNum+=list.size();

		purReport.setIsQmatter("1");
		purReport.setCheckResult("不合格");
		page = purReportService.findPage(new Page<PurReport>(request, response), purReport);
		editNum=page.getCount();
		purReport.setCheckResult("部分合格");
		list=purReportService.findList(purReport);
		editNum+=list.size();

		j.put("total", total);
		j.put("editNum", editNum);
		j.put("submitNum", submitNum);

		return j;
	}



	//--------------------------------------------装配检验单---------------------------------------------------------//

	/**
	 * 装配检验单列表数据(已审核的数据)
	 */
	@ResponseBody
	@RequiresPermissions("purreport:purReport:list")
	@RequestMapping(value = "dataAssembleAudited")
	public Map<String, Object> dataAssembleAudited(PurReport purReport, HttpServletRequest request, HttpServletResponse response, Model model) {
		purReport.setState("已审核");
		purReport.setChecktName("装配");
		Page<PurReport> page = purReportService.findPage(new Page<PurReport>(request, response), purReport);
		List<PurReport> purReList =new ArrayList<PurReport>();
		for(PurReport pur:page.getList()) {
			purReList.add(pur);
		}
		for(PurReport pur:page.getList()) {
			if(("合格").equals(pur.getCheckResult())||"2".equals(pur.getIsQmatter())) {
				purReList.remove(pur);
			}
		}
		page.setCount(purReList.size());
		page.setList(purReList);
		return getBootstrapData(page);
	}

	/**
	 * 未处理的装配检验单数量
	 */
	@ResponseBody
	@RequiresPermissions("purreport:purReport:list")
	@RequestMapping(value = "dataAssembleNotProcessNum")
	public AjaxJson dataAssembleNotProcessNum(PurReport purReport, HttpServletRequest request, HttpServletResponse response, Model model) {
		AjaxJson j = new AjaxJson();
		long total=0;
		long editNum=0;
		long submitNum=0;
		purReport.setChecktName("装配");
		purReport.setState("已审核");
		purReport.setCheckResult("不合格");
		Page<PurReport> page = purReportService.findPage(new Page<PurReport>(request, response), purReport);
		total=page.getCount();
		purReport.setCheckResult("部分合格");
		page=purReportService.findPage(new Page<PurReport>(request, response), purReport);
		total+=page.getCount();

		purReport.setIsQmatter("2");
		purReport.setCheckResult("不合格");
		page = purReportService.findPage(new Page<PurReport>(request, response), purReport);
		submitNum=page.getCount();
		purReport.setCheckResult("部分合格");
		page=purReportService.findPage(new Page<PurReport>(request, response), purReport);
		submitNum+=page.getCount();

		purReport.setIsQmatter("1");
		purReport.setCheckResult("不合格");
		page = purReportService.findPage(new Page<PurReport>(request, response), purReport);
		editNum=page.getCount();
		purReport.setCheckResult("部分合格");
		page=purReportService.findPage(new Page<PurReport>(request, response), purReport);
		editNum+=page.getCount();

		j.put("total", total);
		j.put("editNum", editNum);
		j.put("submitNum", submitNum);

		return j;
	}

	//--------------------------------------------整机检验单---------------------------------------------------------//

	/**
	 * 整机检验单列表数据(已审核的数据)
	 */
	@ResponseBody
	@RequiresPermissions("purreport:purReport:list")
	@RequestMapping(value = "dataMachine")
	public Map<String, Object> dataMachine(PurReport purReport, HttpServletRequest request, HttpServletResponse response, Model model) {
		//purReport.setState("已审核");
		purReport.setChecktName("整机");
		purReport.setState("编辑中");
		Page<PurReport> page = purReportService.findPage(new Page<PurReport>(request, response), purReport);
		List<PurReport> purReList =new ArrayList<PurReport>();
		for(PurReport pur:page.getList()) {
			purReList.add(pur);
		}
		for(PurReport pur:page.getList()) {
			if("2".equals(pur.getIsQmatter())) {
				purReList.remove(pur);
			}
		}
		page.setCount(purReList.size());
		page.setList(purReList);
		return getBootstrapData(page);
	}

	/**整机质量报告处理时用于查找子对象专用
	 * @author xianbang
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "detailForMachineQm")
	public PurReport detailForMachineQm(String id) {
		PurReport purReport=purReportService.get(id);
		return purReport;
	}



	//--------------------------------------------超期复验检验单---------------------------------------------------------//

	/**
	 * 超期复验检验单列表数据(已审核的数据)
	 */
	@ResponseBody
	@RequiresPermissions("purreport:purReport:list")
	@RequestMapping(value = "dataRecheckAudited")
	public Map<String, Object> dataRecheckAudited(PurReport purReport, HttpServletRequest request, HttpServletResponse response, Model model) {
		purReport.setState("已审核");
		purReport.setChecktName("超期复验");
		Page<PurReport> page = purReportService.findPage(new Page<PurReport>(request, response), purReport);
		List<PurReport> purReList =new ArrayList<PurReport>();
		for(PurReport pur:page.getList()) {
			purReList.add(pur);
		}
		for(PurReport pur:page.getList()) {
			if(("合格").equals(pur.getCheckResult())||"2".equals(pur.getIsQmatter())) {
				purReList.remove(pur);
			}
		}
		page.setCount(purReList.size());
		page.setList(purReList);
		return getBootstrapData(page);
	}

	/**
	 * 未处理的超期复验检验单数量
	 */
	@ResponseBody
	@RequiresPermissions("purreport:purReport:list")
	@RequestMapping(value = "dataRecheckNotProcessNum")
	public AjaxJson dataRecheckNotProcessNum(PurReport purReport, HttpServletRequest request, HttpServletResponse response, Model model) {
		AjaxJson j = new AjaxJson();
		long total=0;
		long editNum=0;
		long submitNum=0;
		purReport.setChecktName("超期复验");
		purReport.setState("已审核");
		purReport.setCheckResult("不合格");
		Page<PurReport> page = purReportService.findPage(new Page<PurReport>(request, response), purReport);
		total=page.getCount();
		purReport.setCheckResult("部分合格");
		page=purReportService.findPage(new Page<PurReport>(request, response), purReport);
		total+=page.getCount();

		purReport.setIsQmatter("2");
		purReport.setCheckResult("不合格");
		page = purReportService.findPage(new Page<PurReport>(request, response), purReport);
		submitNum=page.getCount();
		purReport.setCheckResult("部分合格");
		page=purReportService.findPage(new Page<PurReport>(request, response), purReport);
		submitNum+=page.getCount();

		purReport.setIsQmatter("1");
		purReport.setCheckResult("不合格");
		page = purReportService.findPage(new Page<PurReport>(request, response), purReport);
		editNum=page.getCount();
		purReport.setCheckResult("部分合格");
		page=purReportService.findPage(new Page<PurReport>(request, response), purReport);
		editNum+=page.getCount();

		j.put("total", total);
		j.put("editNum", editNum);
		j.put("submitNum", submitNum);

		return j;
	}



//-----------------------------------------------公用部分-----------------------------------------------------------//


	/**质量报告处理时用于查找不合格对象用
	 * @author xianbang
	 * @param id
	 * @return
	 */
    @ResponseBody
    @RequestMapping(value = "detailForQm")
	public PurReport detailForQm(String id) {
    	PurReport purReport=purReportService.get(id);
    	int n = purReport.getPurReportRSnList().size();
        	for(int i=0;i<n;) {
        		if(purReport.getPurReportRSnList().get(i).getCheckResult().equals("合格"))
        		{
        			purReport.getPurReportRSnList().remove(i);
        			n--;
        		}else{
        			i++;
        		}
        	}
        return purReport;
	}
	








	

}