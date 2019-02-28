/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.purchasemanage.contractmain.web;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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
import com.jeeplus.modules.sys.entity.DictValue;
import com.jeeplus.modules.sys.entity.Role;
import com.jeeplus.modules.sys.service.SystemService;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.excel.ExportExcel;
import com.jeeplus.common.utils.excel.ImportExcel;
import com.hqu.modules.purchasemanage.adtbilltype.entity.AdtBillType;
import com.hqu.modules.purchasemanage.adtdetail.entity.AdtDetail;
import com.hqu.modules.purchasemanage.adtdetail.service.AdtDetailService;
import com.hqu.modules.purchasemanage.adtmodel.entity.AdtModel;
import com.hqu.modules.purchasemanage.adtmodel.service.AdtModelService;
import com.hqu.modules.purchasemanage.contractmain.entity.ContractMain;
import com.hqu.modules.purchasemanage.contractmain.service.ContractMainCheckService;
import com.hqu.modules.purchasemanage.paymode.entity.PayMode;
import com.hqu.modules.purchasemanage.purplan.entity.NextUser;
import com.hqu.modules.purchasemanage.purplan.entity.PurPlanMainCheck;
import com.hqu.modules.purchasemanage.purplan.service.PurPlanMainCheckService;

/**
 * 采购计划审核意见控制
 * 
 * @author ckw
 * @version 2018-05-05
 */
@Controller
@RequestMapping(value = "${adminPath}/contractmain/contractMainCheckComment")
public class ContractMainCheckCommentController extends BaseController {

	
	@Autowired
	AdtModelService adtModelService;
	@Autowired
	ContractMainCheckService contractMainCheckService;
	@Autowired
	AdtDetailService adtDetailService;
	@Autowired
	SystemService systemService;
	/**
	 * 跳转到输入审核意见弹窗
	 */
	@RequiresPermissions("contractmaincheck:contractMain:list")
	@RequestMapping(value = {"list"})
	//审核不通过
	public String list(ContractMain contractMain,Model model,HttpServletRequest request) {
	//	String contractMain1=(String)request.getParameter("contractMain");
		// System.out.println("request+++++++++++++++++"+request);
		List<NextUser> users=new ArrayList<NextUser>();
	/*	Map<String,String> roles=adtModelService.findRoleListByModelName("CON");
		for( Map.Entry<String,String> role: roles.entrySet()){
			NextUser u=new NextUser();
			u.setDeptId(role.getKey());
			u.setName(role.getValue());
			users.add(u);
		}
	*/	NextUser end=new NextUser();
		end.setName("打回原制单人");
		end.setDeptId("origin");
		users.add(end);
		NextUser nextUser = new NextUser();
		nextUser.setBillId(contractMain.getId());
		//AdtDetail adtDetail=new AdtDetail();
		model.addAttribute("nextUser",nextUser);
		model.addAttribute("nextCheckers", users);

		return "purchasemanage/contractmain/contractMainCheckComments";
	}

	/**
	 * 跳转到输入审核通过弹窗（指定下一个审核人）
	 */
	@RequiresPermissions("contractmaincheck:contractMain:list")
	@RequestMapping(value = {"pass"})
	public String pass(ContractMain contractMain,Model model) {

		List<NextUser> users=new ArrayList<NextUser>();
		Map<String,String> roles=adtModelService.findRoleListByModelName("CON");
		for( Map.Entry<String,String> role: roles.entrySet()){
			NextUser u=new NextUser();
			u.setDeptId(role.getKey());
			u.setName(role.getValue());
			users.add(u);
		}
		NextUser end=new NextUser();
		end.setName("结束");
		end.setDeptId("end");
		users.add(end);
		NextUser nextUser = new NextUser();
		nextUser.setBillId(contractMain.getId());
		model.addAttribute("nextUser", nextUser);
		model.addAttribute("nextCheckers", users);

		return "purchasemanage/contractmain/contractMainCheckNoComments";
	}

}