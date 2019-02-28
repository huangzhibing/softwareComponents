/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.purchasemanage.group.web;

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
import com.jeeplus.modules.sys.utils.UserUtils;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.excel.ExportExcel;
import com.jeeplus.common.utils.excel.ImportExcel;
import com.hqu.modules.purchasemanage.contractmain.entity.ContractMain;
import com.hqu.modules.purchasemanage.group.entity.Group;
import com.hqu.modules.purchasemanage.group.entity.GroupBuyer;
import com.hqu.modules.purchasemanage.group.service.GroupQueryService;
import com.hqu.modules.purchasemanage.group.service.GroupService;

/**
 * GroupQueryController
 * @author 杨贤邦
 * @version 2018-04-23
 */
@Controller
@RequestMapping(value = "${adminPath}/group/groupQuery")
public class GroupQueryController extends BaseController {

	@Autowired
	private GroupQueryService groupService;
	

	
	/**
	 * 采购组列表页面
	 */
	@RequiresPermissions("group:group:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "purchasemanage/group/groupList";
	}
	
		/**
	 * 采购组列表数据
	 */
	@ResponseBody
	@RequiresPermissions("group:group:list")
	@RequestMapping(value = "buyersdata")
	public Map<String, Object> buyersdata(GroupBuyer group, HttpServletRequest request, HttpServletResponse response, Model model) {
		
		Page<GroupBuyer> page = groupService.findBuyersPage(UserUtils.getUser(),new Page<GroupBuyer>(request, response), group); 
		//分页
        String intPage= request.getParameter("pageNo");
        int pageNo=Integer.parseInt(intPage);
        int pageSize= page.getPageSize();
        List<GroupBuyer> pageList=page.getList();
       if(pageNo==1&&pageSize==-1){
    	   page.setList(pageList);
       }else{
    	   List<GroupBuyer> reportL=  new ArrayList<GroupBuyer>();        
	        for(int i=(pageNo-1)*pageSize;i<pageList.size() && i<pageNo*pageSize;i++){
	        	reportL.add(pageList.get(i));
		    } 
	        page.setList(reportL);
       }
       
		System.out.println("page++++++++++++++++++++++++++++"+page.getList());
		
		return getBootstrapData(page);
	}

	

}