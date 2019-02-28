/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.purchasemanage.purplan.web;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import com.jeeplus.modules.sys.entity.Role;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.utils.UserUtils;
import com.hqu.modules.purchasemanage.adtbilltype.entity.AdtBillType;
import com.hqu.modules.purchasemanage.adtdetail.entity.AdtDetail;
import com.hqu.modules.purchasemanage.adtdetail.service.AdtDetailService;
import com.hqu.modules.purchasemanage.adtmodel.entity.AdtModel;
import com.hqu.modules.purchasemanage.adtmodel.mapper.AdtModelMapper;
import com.hqu.modules.purchasemanage.adtmodel.service.AdtModelService;
import com.hqu.modules.purchasemanage.purplan.entity.PurPlanMain;
import com.jeeplus.modules.sys.entity.Role;
import com.jeeplus.modules.sys.service.SystemService;
import com.jeeplus.modules.sys.utils.UserUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
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
import com.hqu.modules.purchasemanage.purplan.entity.NextUser;
import com.hqu.modules.purchasemanage.purplan.entity.PurPlanMainCheck;
import com.hqu.modules.purchasemanage.purplan.service.PurPlanMainCheckService;

import com.hqu.modules.purchasemanage.purplan.entity.Checker;

/**
 * 采购计划审核意见控制
 * 
 * @author ckw
 * @version 2018-05-05
 */
@Controller
@RequestMapping(value = "${adminPath}/purplan/purPlanMainCheckComment")
public class PurPlanMainCheckCommentController extends BaseController {

	@Autowired
	AdtModelService adtModelService;
	@Autowired
	PurPlanMainCheckService purPlanMainCheckService;
	@Autowired
	AdtDetailService adtDetailService;
	@Autowired
	SystemService systemService;


	/**
	 * 点击审核不通过时弹出审核不通过窗口,传数据到窗口
	 */
	@RequiresPermissions("purplan:purPlanMainCheck:list")
	@RequestMapping(value = { "unPass", "" })
	public String unPass(Model model, @RequestParam String purPlanMainCheckId) {

//	    设置审核不通过界面的【下一个审核人】控件数据select
		List<NextUser> users=new ArrayList<NextUser>();
        List<Checker> checkers=adtModelService.findRoleList("PLN");


        //在select中添加【打回原制单人】选项
		NextUser end=new NextUser();
		end.setName("打回原制单人");
		end.setDeptId("origin");
		users.add(end);

		NextUser nextUser=new NextUser();
		nextUser.setBillId(purPlanMainCheckId);

		model.addAttribute("nextUser", nextUser);
		model.addAttribute("nextCheckers", users);

		return "purchasemanage/purplan/purPlanMainCheckComments";
	}


	/**
	 * 在审核不通过窗口点击“确定”按钮后，响应
     * @param nextUser 封装了表单提交的数据，以及保存的此审核记录的billnum，通过此可以找到单据对应的制单人
	 */
	@RequiresPermissions(value={"purplan:purPlanMainCheck:edit"},logical=Logical.OR)
	@RequestMapping(value = "unPassSave")
	public String unPassSave(NextUser nextUser, Model model, RedirectAttributes redirectAttributes) throws Exception{
		PurPlanMainCheck purPlanMainCheck =purPlanMainCheckService.get(nextUser.getBillId());
		AdtDetail adtDetail=new AdtDetail();
		adtDetail.setBillTypeCode("PLN001");
		adtDetail.setBillTypeName("采购计划");
		adtDetail.setBillNum(purPlanMainCheck.getBillNum());


		//保存审核不通过意见
		adtDetail.setJustifyRemark(nextUser.getNote());

        //前台自动将选择的角色部门id，审核不通过信息自动封装到nextUser对象
		String roleId=nextUser.getDeptId();
		if("origin".equals(roleId)){            //审核不通过，打回原制单人
            //获取此单据对应的制单人
            User createBy=purPlanMainCheck.getCreateBy();
			AdtBillType billType=new AdtBillType();
			billType.setBillTypeName("采购计划");
			billType.setBillTypeCode("PLN001");
			//更新当前审核人在detail表中的记录
			adtDetailService.auditFail(adtDetail,false);
			//发回原制单人时，写入detail表
            adtDetailService.nextStepForUnpass(purPlanMainCheck.getBillNum(),billType,"PLN",createBy);

            //在审核不通过，当打回原制单人时，才对待审核单据的bill_state_flag进行更新
            //A正在录入/修改   W录入完毕    E审批通过       B审批未通过      V作废单据     O结案   C下达
		}else {
			AdtBillType billType=new AdtBillType();
			billType.setBillTypeName("采购计划");
			billType.setBillTypeCode("PLN001");
			Role role=systemService.getRole(nextUser.getDeptId());
			adtDetailService.auditFail(adtDetail,false);
			//如果下一个审核人不是原制单人，直接调用nextStep方法更新detail表中的记录
			adtDetailService.nextStep(purPlanMainCheck.getBillNum(),billType,"PLN",false,role);
		}
		return "purchasemanage/purplan/purPlanMainCheckNoComments";
	}
	/**
	 * 跳转到输入审核通过弹窗（指定下一个审核人）
	 */
	@RequiresPermissions("purplan:purPlanMainCheck:list")
	@RequestMapping(value = { "pass", "" })
	public String pass(Model model, @RequestParam String purPlanMainCheckId) {

		List<NextUser> users=new ArrayList<NextUser>();
		Map<String,String> roles=adtModelService.findRoleListByModelName("PLN");
		for( Map.Entry<String,String> role: roles.entrySet()){
			NextUser u=new NextUser();
			u.setDeptId(role.getKey());
			u.setName(role.getValue());
			users.add(u);
		}
		NextUser end=new NextUser();
		end.setName("结束");
		end.setDeptId("0");
		users.add(end);


		NextUser nextUser=new NextUser();
		nextUser.setBillId(purPlanMainCheckId);
		model.addAttribute("nextUser", nextUser);
		model.addAttribute("nextCheckers", users);

		return "purchasemanage/purplan/purPlanMainCheckNoComments";
	}


	/**
	 * 审核通过处理
	 */
	@RequiresPermissions(value={"purplan:purPlanMainCheck:edit"},logical=Logical.OR)
	@RequestMapping(value = "passSave")
	public String passSave(NextUser nextUser, Model model, RedirectAttributes redirectAttributes) throws Exception{
		PurPlanMainCheck purPlanMainCheck =purPlanMainCheckService.get(nextUser.getBillId());
		AdtDetail adtDetail=new AdtDetail();
		adtDetail.setBillTypeCode("PLN001");
		adtDetail.setBillTypeName("采购计划");
		adtDetail.setBillNum(purPlanMainCheck.getBillNum());

		String roleId=nextUser.getDeptId();//前台利用makeEmpname字段保存下一个审核人角色的ID
		if("0".equals(roleId)){                        //审核通过，结束工作流程
			adtDetailService.auditPass(adtDetail,true);



		}else {
			AdtBillType billType=new AdtBillType();
			billType.setBillTypeName("采购计划");
			billType.setBillTypeCode("PLN001");
			Role role=systemService.getRole(nextUser.getDeptId());
			adtDetailService.auditPass(adtDetail,false);
			adtDetailService.nextStep(purPlanMainCheck.getBillNum(),billType,"PLN",false,role);
		}
		addMessage(redirectAttributes, "处理成功");
		return "redirect:"+Global.getAdminPath()+"/purplan/purPlanMainCheck/?repage";
	}
}