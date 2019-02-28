package com.hqu.modules.costmanage.reportmanage.web;


import com.hqu.modules.costmanage.reportmanage.entity.ReportPersonWage;
import com.hqu.modules.costmanage.reportmanage.entity.ResourceImport;
import com.hqu.modules.costmanage.reportmanage.resourceInportOutputservice.ResourceImportService;
import com.hqu.modules.costmanage.reportmanage.service.ReportAccountService;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.modules.sys.entity.Office;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.service.OfficeService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Controller
@RequestMapping(value = "${adminPath}/reportmanage/account")
public class ReportAccountController extends BaseController{
	@Autowired
	ReportAccountService reportAccountService;
	@Autowired
	OfficeService officeService;
	
	
	@ModelAttribute
	public ResourceImport get() {
		ResourceImport entity = new ResourceImport();
		
		return entity;
	}
	
	@RequiresPermissions("reportmanage:account:list")
	@RequestMapping(value = { "Zlist", "" })
	public String list() {
		return "costmanage/reportmanage/account/accountList";
	}
	
	@RequestMapping(value="data")
	@ResponseBody
	@RequiresPermissions("reportmanage:account:list")
	public Map<String, Object> data(ReportPersonWage personWage, HttpServletRequest request, HttpServletResponse response,
			Model model) {
		Page<ReportPersonWage> page = reportAccountService.findPage(new Page<ReportPersonWage>(request,response), personWage);
//		if(StringUtils.isBlank(personWage.getOfficeName())){
//			for(int i=0;i<page.getList().size();i++){
//				User user = personWage.getPersonCode();
//				Office office = officeService.get(user.getOffice().getId());
//				page.getList().get(i).setOfficeName(office.getName());
//			}
//		}
		return getBootstrapData(page);
	}
}
