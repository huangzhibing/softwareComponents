package com.hqu.modules.costmanage.reportmanage.web;

import com.hqu.modules.costmanage.reportmanage.entity.ResourceImport;
import com.hqu.modules.costmanage.reportmanage.service.InProductService;
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
@RequestMapping(value = "${adminPath}/reportmanage/inproduct")
public class InProductController extends BaseController{
	@Autowired
	InProductService inProductService;
	
	
	@ModelAttribute
	public ResourceImport get() {
		ResourceImport entity = new ResourceImport();
		
		return entity;
	}
	
	@RequiresPermissions("reportmanage:inproduct:list")
	@RequestMapping(value = { "list", "" })
	public String list() {
		return "costmanage/reportmanage/inproduct/inproductList";
	}
	
	@RequestMapping(value="data")
	@ResponseBody
	@RequiresPermissions("reportmanage:inproduct:list")
	public Map<String, Object> data(ResourceImport resourceImport, HttpServletRequest request, HttpServletResponse response,
			Model model) {
		Page<ResourceImport> page = inProductService.findPage(new Page<ResourceImport>(request,response), resourceImport);
		return getBootstrapData(page);
	}
}
