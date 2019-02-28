package com.hqu.modules.costmanage.reportmanage.web;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hqu.modules.costmanage.reportmanage.entity.ResourceImport;
import com.hqu.modules.costmanage.reportmanage.resourceInportOutputservice.PayDetailService;
import com.hqu.modules.costmanage.reportmanage.resourceInportOutputservice.ResourceImportService;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.web.BaseController;

@Controller
@RequestMapping(value="${adminPath}/reportmanage/payDetail")
public class PayDetailController extends BaseController{
	@Autowired
	PayDetailService payDetailService;
	
	
	@ModelAttribute
	public ResourceImport get() {
		ResourceImport entity = new ResourceImport();
		
		return entity;
	}
	
	@RequiresPermissions("paydetail:payDetail:list")
	@RequestMapping(value = { "list", "" })
	public String list() {
		return "costmanage/reportmanage/paydetail/payDetailList";
	}
	
	@RequestMapping(value="data")
	@ResponseBody
	@RequiresPermissions("paydetail:payDetail:list")
	public Map<String, Object> data(ResourceImport resourceImport, HttpServletRequest request, HttpServletResponse response,
			Model model) {
		Page<ResourceImport> page = payDetailService.findPage(new Page<ResourceImport>(request,response), resourceImport);
		return getBootstrapData(page);
	}
}
