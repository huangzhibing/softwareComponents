/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.purchasemanage.applymain.web;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
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
import com.hqu.modules.basedata.item.entity.Item;
import com.hqu.modules.inventorymanage.invaccount.entity.InvAccount;
import com.hqu.modules.inventorymanage.invaccount.service.InvAccountService;
import com.hqu.modules.purchasemanage.applymain.entity.InvAccountPur;
import com.hqu.modules.purchasemanage.applymain.service.InvAccountPurService;

/**
 * 库存账Controller
 * @author M1ngz
 * @version 2018-04-22
 */
@Controller
@RequestMapping(value = "${adminPath}/applymain/invAccount")
public class InvAccountPurController extends BaseController {

	@Autowired
	private InvAccountPurService invAccountPurService;
	
	@ModelAttribute
	public InvAccountPur get(@RequestParam(required=false) String id) {
		InvAccountPur entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = invAccountPurService.get(id);
		}
		if (entity == null){
			entity = new InvAccountPur();
		}
		return entity;
	}
	/*
	@RequestMapping(value="getInvAccountByItemCode1", method={RequestMethod.POST})
	@ResponseBody
	//public InvAccount getInvAccountByItemCode(HttpServletRequest request){
	public AjaxJson getInvAccountByItemCode(@RequestParam String id){
		//String itemCode=request.getParameter("id");
		String itemCode=id;
		InvAccountPur invAccount=new InvAccountPur();
		Item item=new Item(); 
		//item.setCode(itemCode);
		item.setId(id);
		invAccount.setItem(item);
		System.out.println("itemId+++++++++++++++++++++++++"+id);
		InvAccountPur invAccountPur=new InvAccountPur();
		invAccountPur=invAccountPurService.
		AjaxJson j = new AjaxJson();
		j.put("nowSum", invAccountPur.getNowSum());
		j.put("costPrice", invAccountPur.getCostPrice());
		return j;
		
	}
	*/
}