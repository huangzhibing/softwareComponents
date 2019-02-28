/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.qualitymanage.purreport.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hqu.modules.inventorymanage.invcheckmain.entity.ReinvCheckMain;
import com.hqu.modules.inventorymanage.invcheckmain.mapper.ReinvCheckMainMapper;
import com.hqu.modules.inventorymanage.invcheckmain.service.ReinvCheckMainService;
import com.hqu.modules.qualitymanage.purinvcheckmain.entity.InvCheckMain;
import com.hqu.modules.qualitymanage.purinvcheckmain.service.InvCheckMainService;
import com.hqu.modules.qualitymanage.purreport.entity.PurReport;
import com.hqu.modules.qualitymanage.purreport.entity.PurReportRSn;
import com.hqu.modules.qualitymanage.purreport.mapper.PurReportAuditMapper;
import com.hqu.modules.qualitymanage.purreport.service.PurReportAuditService;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.modules.sys.entity.Role;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.mapper.RoleMapper;
import com.jeeplus.modules.sys.service.SystemService;
import com.jeeplus.modules.sys.utils.UserUtils;

/**
 * 检验审核单(采购/装配/整机检测)Controller
 * @author 张铮
 * @version 2018-04-03
 */
@Controller
@RequestMapping(value = {"${adminPath}/purreport/purReportAudit","${adminPath}/purreport/purReportAudit1"})
public class PurReportAuditController extends PurReportController {
	@Autowired
	private PurReportAuditService purReportAuditService;
	@Autowired
	private InvCheckMainService invCheckMainService;
	@Autowired
	private ReinvCheckMainService reinvCheckMainService;
	@Autowired
	private ReinvCheckMainMapper reinvCheckMainMapper;
	@Autowired
	private PurReportAuditMapper purReportAuditMapper;
	
	@Autowired
	private SystemService systemService;
	@Autowired
	private String office;
	@Autowired
	private RoleMapper roleMapper;

	
	@ModelAttribute
	public PurReport get(@RequestParam(required=false) String id) {
		PurReport entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = purReportAuditService.get(id);
		}
		if (entity == null){
			entity = new PurReport();
		}
		return entity;
	}
	
	/**
	 * 检验审核单列表页面
	 */
	@RequiresPermissions("purreport:purReportAudit:list")
	@RequestMapping(value = {"listAudit", ""})
	public String list() {
		return "qualitymanage/purreport/purReportAuditList";
	}
	
	/**
	 * 检验审核单列表数据
	 */
	@ResponseBody
	@RequiresPermissions("purreport:purReportAudit:list")
	@RequestMapping(value = "dataAudit")
	public Map<String, Object> data(PurReport purReport, HttpServletRequest request, HttpServletResponse response, Model model) {
		//获取当前编号
		purReport.setJpersonCode(UserUtils.getUser().getNo());
		purReport.setChecktName("采购");
		Page<PurReport> page = new Page<PurReport>();
		List<PurReport> list = new ArrayList<PurReport>();
		list = purReportAuditMapper.findListByJustifyPerson(purReport);
		page.setList(list);
		page.setCount(list.size());
		return getBootstrapData(page);
	}
	
	
	/**
	 * 查看检验单审核表单页面
	 */
	@RequiresPermissions(value={"purreport:purReport:view"})
	@RequestMapping(value = "AuditDetail")
	public String detail(PurReport purReport, Model model) {
		model.addAttribute("purReport", purReport);
		if(StringUtils.isBlank(purReport.getId())){//如果ID是空为添加
			model.addAttribute("isAdd", true);
		}
		if("未审核".equals(purReport.getState())){
			//获取当前时间
			Date date = new Date();
			purReport.setJustifyDate(date);
			//获取当前用户信息
			User user = UserUtils.getUser();
			if(user!= null){
				 office = user.getOffice().toString();
			}
			purReport.setJdeptName(office);
			purReport.setJustifyPerson(UserUtils.getUser().getLoginName());
			model.addAttribute("purReport", purReport);
			return "qualitymanage/purreport/purReportAuditDetail";
		}
		else{
			return "qualitymanage/purreport/purReportAuditDetail1";
		}

	 }
	/**
	 * 提交审核
	 * */
	@RequiresPermissions(value={"purreport:purReport:edit"})
	@RequestMapping(value = {"AuditSubmit"})
	public String AuditSubmit(PurReport purReport, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, purReport)){
			return form(purReport, model);
		}
		
		if(purReport.getJustifyResult() == null){
			addMessage(redirectAttributes, "请输入审核结果！");
		}
		//设置单据状态为审核
		if(purReport.getJustifyResult() != null){
			if(purReport.getJustifyResult().equals("未通过")){
				purReport.setJpersonCode(UserUtils.getUser().getNo());			
				purReport.setState("编辑中");
				purReport.setIsAudit("审核未通过");
			}else{
				//发送
				if("超期复验".equals(purReport.getChecktName())){
					//boolean i = Send(purReport,model,redirectAttributes);
					//if(i==true){
					purReport.setJpersonCode(UserUtils.getUser().getNo());
					purReport.setState("已审核");
					purReport.setIsAudit("审核通过");
					//}
				}else{
					purReport.setJpersonCode(UserUtils.getUser().getNo());
					purReport.setState("已审核");
					purReport.setIsAudit("审核通过");
				}
			}
			purReport.setJdeptCode(UserUtils.getUserOfficeCode());
		}
		InvCheckMain invCheckMain = new InvCheckMain();
		invCheckMain.setId(purReport.getBillnum());
		purReport.setInvCheckMain(invCheckMain);
		purReportAuditService.save(purReport);//添加审核
		if("装配".equals(purReport.getChecktName())){
			addMessage(redirectAttributes, "提交装配检验单审核成功");
			return "redirect:"+Global.getAdminPath()+"/purreport/purReportAudit/listAssembleAudit?repage";
		}else if("采购".equals(purReport.getChecktName())){
			addMessage(redirectAttributes, "提交检验单审核成功");
			return "redirect:"+Global.getAdminPath()+"/purreport/purReportAudit/?repage";
		}else{
			addMessage(redirectAttributes, "提交超期复验单审核成功");
			return "redirect:"+Global.getAdminPath()+"/purreport/purReportAudit/listReInvAudit?repage";
		}
	}
	
	/**
	 * 装配检验审核单列表页面
	 */
	@RequiresPermissions("purreport:purReportAudit:list")
	@RequestMapping(value = "listAssembleAudit")
	public String listAssembleAudit() {
		return "qualitymanage/purreport/purReportAssembleAuditList";
	}
	
	/**
	 * 超期复验审核单列表页面
	 */
	@RequiresPermissions("purreport:purReportAudit:list")
	@RequestMapping(value = "listReInvAudit")
	public String listReInvAudit() {
		return "qualitymanage/purreport/purReportReInvAuditList";
	}
	
	/**
	 * 超期复验审核单列表数据
	 */
	@ResponseBody
	@RequiresPermissions("purreport:purReportAudit:list")
	@RequestMapping(value = "dataReInvAudit")
	public Map<String, Object> dataReInvAudit(PurReport purReport, HttpServletRequest request, HttpServletResponse response, Model model) {
		//获取当前编号
		purReport.setJpersonCode(UserUtils.getUser().getNo());
		purReport.setChecktName("超期复验");
		Page<PurReport> page = new Page<PurReport>();
		List<PurReport> list = new ArrayList<PurReport>();
		list = purReportAuditMapper.findListByJustifyPerson(purReport);
		page.setList(list);
		page.setCount(list.size());
		return getBootstrapData(page);
	}
	
	/**
	 * 装配检验审核单列表数据
	 */
	@SuppressWarnings("null")
	@ResponseBody
	@RequiresPermissions("purreport:purReportAudit:list")
	@RequestMapping(value = "dataAssembleAudit")
	public Map<String, Object> dataAssembleAudit(PurReport purReport, HttpServletRequest request, HttpServletResponse response, Model model) {
		//获取当前编号
		purReport.setJpersonCode(UserUtils.getUser().getNo());
		purReport.setChecktName("装配");
		Page<PurReport> page = new Page<PurReport>();
		List<PurReport> list = new ArrayList<PurReport>();
		list = purReportAuditMapper.findListByJustifyPerson(purReport);
		page.setList(list);
		page.setCount(list.size());
		return getBootstrapData(page);
	}
	
	/**
	 * 查看装配检验单审核表单页面
	 */
	@RequiresPermissions(value={"purreport:purReport:view"})
	@RequestMapping(value = "AssembleAuditDetail")
	public String assembleDetail(PurReport purReport, Model model) {
		model.addAttribute("purReport", purReport);
		if(StringUtils.isBlank(purReport.getId())){//如果ID是空为添加
			model.addAttribute("isAdd", true);
		}
		if("未审核".equals(purReport.getState())){
			//获取当前时间
			Date date = new Date();
			purReport.setJustifyDate(date);
			//获取当前用户信息
			User user = UserUtils.getUser();
			if(user!= null){
				 office = user.getOffice().toString();
			}
			purReport.setJdeptName(office);
			purReport.setJustifyPerson(UserUtils.getUser().getLoginName());
			model.addAttribute("purReport", purReport);
			return "qualitymanage/purreport/purReportAssembleAuditDetail";
		}
		else{
			return "qualitymanage/purreport/purReportAssembleAuditDetail1";
		}

	 }
	
	/**
	 * 查看超期复验审核表单页面
	 */
	@RequiresPermissions(value={"purreport:purReport:view"})
	@RequestMapping(value = "ReInvAuditDetail")
	public String reInvAuditDetail(PurReport purReport, Model model) {
		model.addAttribute("purReport", purReport);
		if(StringUtils.isBlank(purReport.getId())){//如果ID是空为添加
			model.addAttribute("isAdd", true);
		}
		if("未审核".equals(purReport.getState())){
			//获取当前时间
			Date date = new Date();
			purReport.setJustifyDate(date);
			//获取当前用户信息
			User user = UserUtils.getUser();
			if(user!= null){
				 office = user.getOffice().toString();
			}
			purReport.setJdeptName(office);
			purReport.setJustifyPerson(UserUtils.getUser().getLoginName());
			model.addAttribute("purReport", purReport);
			return "qualitymanage/purreport/purReportReInvAuditDetail";
		}
		else{
			return "qualitymanage/purreport/purReportReInvAuditDetail1";
		}

	 }
	
	@ResponseBody
	@RequestMapping(value = "getSysRole")
	public Map<String, Object> data(Role role, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Role> page = new Page<Role>();
		User user = UserUtils.getUser().getCurrentUser();
		role.setUser(user);
		List<Role> list = roleMapper.findList(role);
		page.setList(list);
		page.setCount(list.size());
		return getBootstrapData(page);
	}
	
	
	 @ResponseBody
     @RequestMapping(value = "detail")
     public PurReport detail(String id) {
        return purReportAuditService.get(id);
     }
	 
	 
	 /*
	  * 发送通知
	  * */
    public boolean Send(PurReport purReport, Model model, RedirectAttributes redirectAttributes) throws Exception{
        String invCheckMainID= purReport.getReinvCheckMain().getId();
        ReinvCheckMain reinvCheckMain=new ReinvCheckMain();
        reinvCheckMain.setId(invCheckMainID);
        reinvCheckMain=reinvCheckMainService.get(reinvCheckMain);
        if(reinvCheckMain==null){
            addMessage(redirectAttributes, "超期复验单尚未创建");
            return false;
        }else{
        	reinvCheckMain.setQmsFlag("1");
            reinvCheckMainService.save(reinvCheckMain);
            addMessage(redirectAttributes, "超期复验单发送成功");
            return true;
        }
    }
}

