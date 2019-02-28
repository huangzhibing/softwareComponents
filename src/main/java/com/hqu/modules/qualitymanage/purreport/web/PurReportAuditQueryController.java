/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.qualitymanage.purreport.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hqu.modules.qualitymanage.purreport.entity.PurReport;
import com.hqu.modules.qualitymanage.purreport.service.PurReportAuditService;
import com.jeeplus.core.persistence.Page;

/**
 * 检验审核审核Controller
 * @author 张铮
 * @version 2018-04-17
 */
@Controller
@RequestMapping(value = "${adminPath}/purreport/purReportAuditQuery")
public class PurReportAuditQueryController extends PurReportController {
	@Autowired
	private PurReportAuditService purReportService;
	/**
	 * 检验审核单查询列表数据
	 */
	@ResponseBody
	@RequiresPermissions("purreport:purReportAuditQuery:list")
	@RequestMapping(value = "dataAuditQuery")
	public Map<String, Object> dataAuditQuery(PurReport purReport, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<PurReport> page = purReportService.findQueryPage(new Page<PurReport>(request, response), purReport); 
		 //分页
        String intPage= request.getParameter("pageNo");
        int pageNo=Integer.parseInt(intPage);
        int pageSize= page.getPageSize();
        List<PurReport> pageList=page.getList();
        if(pageNo==1&&pageSize==-1){
    	   page.setList(pageList);
        }else{
    	   List<PurReport> reportL=  new ArrayList<PurReport>();        
	        for(int i=(pageNo-1)*pageSize;i<pageList.size() && i<pageNo*pageSize;i++){
	        	reportL.add(pageList.get(i));
		    }
	        page.setList(reportL);
        }
		return getBootstrapData(page);
	}
	
	
	@RequiresPermissions("purreport:purReportAuditQuery:list")
	@RequestMapping(value = "listAuditQuery")
	public String listAuditQuery() {
		return "qualitymanage/purreport/purReportAuditQueryList";
	}
	
	/**
	 * 查看检验单审核表单页面
	 */
	@RequiresPermissions(value={"purreport:purReportAuditQuery:list"})
	@RequestMapping(value = "AuditDetail")
	public String detail(PurReport purReport, Model model) {
		model.addAttribute("purReport", purReport);
		return "qualitymanage/purreport/purReportAuditQueryDetail";

	 }
}
