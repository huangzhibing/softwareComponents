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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hqu.modules.costmanage.reportmanage.entity.ResourceImport;
import com.hqu.modules.costmanage.reportmanage.resourceInportOutputservice.ResourceImportService;
import com.hqu.modules.costmanage.reportmanage.resourceInportOutputservice.ResourceOutputService;
import com.hqu.modules.inventorymanage.billmain.entity.BillMain;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.web.BaseController;

@Controller
@RequestMapping(value = "${adminPath}/reportmanage/resourceImport")
public class ResourceImportController extends BaseController{
	@Autowired
	ResourceImportService resourceImportService;
	
	
	@ModelAttribute
	public ResourceImport get() {
		ResourceImport entity = new ResourceImport();
		
		return entity;
	}
	
	@RequiresPermissions("resourceimport:resourceImport:list")
	@RequestMapping(value = { "list", "" })
	public String list() {
		return "costmanage/reportmanage/resourceimport/resourceImportList";
	}
	
	@RequestMapping(value="data")
	@ResponseBody
	@RequiresPermissions("resourceimport:resourceImport:list")
	public Map<String, Object> data(ResourceImport resourceImport, HttpServletRequest request, HttpServletResponse response,
			Model model) {
		Page<ResourceImport> page = resourceImportService.findPage(new Page<ResourceImport>(request,response), resourceImport);
		return getBootstrapData(page);
	}
}
