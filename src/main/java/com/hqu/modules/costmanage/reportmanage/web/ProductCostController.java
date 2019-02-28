package com.hqu.modules.costmanage.reportmanage.web;

import com.fasterxml.jackson.databind.annotation.JsonAppend;
import com.hqu.modules.costmanage.reportmanage.entity.ReportRealAccount;
import com.hqu.modules.costmanage.reportmanage.entity.ResourceImport;
import com.hqu.modules.costmanage.reportmanage.resourceInportOutputservice.ResourceImportService;
import com.hqu.modules.costmanage.reportmanage.service.ProductCostService;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.web.BaseController;
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
@RequestMapping(value = "${adminPath}/reportmanage/productcost")
public class ProductCostController extends BaseController{
	@Autowired
	ProductCostService productCostService;
	
	
	@ModelAttribute
	public ResourceImport get() {
		ResourceImport entity = new ResourceImport();
		
		return entity;
	}
	
	@RequiresPermissions("reportmanage:productcost:list")
	@RequestMapping(value = { "list", "" })
	public String list() {
		return "costmanage/reportmanage/productcost/productcostList";
	}
	
	@RequestMapping(value="data")
	@ResponseBody
	@RequiresPermissions("reportmanage:productcost:list")
	public Map<String, Object> data(ReportRealAccount realAccount, HttpServletRequest request, HttpServletResponse response,
									Model model) {
		Page<ReportRealAccount> page = productCostService.findPage(new Page<ReportRealAccount>(request,response), realAccount);
		return getBootstrapData(page);
	}
}
