/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.qualitymanage.purreport.web;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import com.hqu.modules.inventorymanage.newinvcheckmain.entity.NewinvCheckDetailCode;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
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
import com.hqu.modules.basedata.item.entity.Item;
import com.hqu.modules.inventorymanage.invcheckmain.entity.ReInvCheckDetail;
import com.hqu.modules.inventorymanage.invcheckmain.entity.ReinvCheckMain;
import com.hqu.modules.inventorymanage.invcheckmain.service.ReinvCheckMainService;
import com.hqu.modules.inventorymanage.newinvcheckmain.entity.NewinvCheckDetail;
import com.hqu.modules.inventorymanage.newinvcheckmain.entity.NewinvCheckMain;
import com.hqu.modules.inventorymanage.newinvcheckmain.service.NewinvCheckMainService;
import com.hqu.modules.qualitymanage.objectdef.entity.ObjectDef;
import com.hqu.modules.qualitymanage.objectdef.service.ObjectDefService;
import com.hqu.modules.qualitymanage.purinvcheckmain.entity.InvCheckMain;
import com.hqu.modules.qualitymanage.purinvcheckmain.service.InvCheckMainService;
import com.hqu.modules.qualitymanage.purreport.entity.PurReport;
import com.hqu.modules.qualitymanage.purreport.entity.PurReportRSn;
import com.hqu.modules.qualitymanage.purreport.entity.VerifyQCNormCust;
import com.hqu.modules.qualitymanage.purreport.mapper.PurReportMapper;
import com.hqu.modules.qualitymanage.purreport.service.PurReportService;
import com.hqu.modules.qualitymanage.purreportnew.entity.PurReportNew;
import com.hqu.modules.qualitymanage.purreportnew.entity.PurReportNewCust;
import com.hqu.modules.qualitymanage.qualitynorm.entity.QualityNorm;
import com.hqu.modules.qualitymanage.qualitynorm.mapper.QualityNormMapper;
import com.hqu.modules.qualitymanage.verifyqcnorm.entity.VerifyQCNorm;
import com.hqu.modules.qualitymanage.verifyqcnorm.service.VerifyQCNormService;
import com.hqu.modules.workshopmanage.processroutinedetail.entity.ProcessRoutineDetail;
import com.hqu.modules.workshopmanage.processroutinedetail.service.ProcessRoutineDetailService;
import com.hqu.modules.workshopmanage.sfcinvcheckmain.entity.SfcInvCheckDetail;
import com.hqu.modules.workshopmanage.sfcinvcheckmain.entity.SfcInvCheckMain;
import com.hqu.modules.workshopmanage.sfcinvcheckmain.service.SfcInvCheckMainService;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.utils.SpringContextHolder;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.excel.ExportExcel;
import com.jeeplus.common.utils.excel.ImportExcel;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.modules.act.service.ActTaskService;
import com.jeeplus.modules.sys.entity.Office;
import com.jeeplus.modules.sys.entity.Role;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.mapper.OfficeMapper;
import com.jeeplus.modules.sys.service.OfficeService;
import com.jeeplus.modules.sys.service.SystemService;
import com.jeeplus.modules.sys.utils.UserUtils;

/**
 * 检验单(采购/装配/整机检测)Controller
 * @author 孙映川
 * @version 2018-04-05
 */
@Controller
@RequestMapping(value = "${adminPath}/purreport/purReport")
public class PurReportController extends BaseController {

	@Autowired
	private ObjectDefService objectDefService;

	@Autowired
	private ReinvCheckMainService reinvCheckMainService;

	@Autowired
	private NewinvCheckMainService newinvCheckMainService;
	
	@Autowired
	private ProcessRoutineDetailService processRoutineDetailService;
	@Autowired
	private PurReportService purReportService;
	
	@Autowired
	private OfficeService officeService;
	

	
	@Autowired
	private SystemService systemService;
//	@Autowired
//	private QualityNormService qualitynormService;
	@Autowired
	private QualityNormMapper qualityNormMapper;
	@Autowired
	private InvCheckMainService invCheckMainService;
	@Autowired
	private PurReportMapper purReportMapper;
	
	@Autowired
	private RuntimeService runtimeService;
	@Autowired
	protected TaskService taskService;
	@Autowired
	protected HistoryService historyService;
	@Autowired
	protected RepositoryService repositoryService;
	@Autowired
	private IdentityService identityService;
	@Autowired
	private ActTaskService actTaskService;
	
	@Autowired
	private SfcInvCheckMainService sfcInvCheckMainService;
	
	@Autowired
	private VerifyQCNormService verifyQCNormService;
	

	private static OfficeMapper officeMapper = SpringContextHolder.getBean(OfficeMapper.class);

	@RequestMapping(value = "mydeleteCom")
	public String mydeleteCom(PurReport purReport, Model model) {
		model.addAttribute("purReport", purReport);
		return "qualitymanage/purreport/purReportForm_deleteCom";
	}
	
	@RequestMapping(value = "mydeleteAsse")
	public String mydeleteAsse(PurReport purReport, Model model) {
		//检验执行标准名称
				List<QualityNorm> findList = qualityNormMapper.findList(new QualityNorm());
				List<String> list = new ArrayList<String>();
				Iterator<QualityNorm> it = findList.iterator();
				while(it.hasNext()){
					QualityNorm qualityNorm = it.next();
			//		String qNormName = qualityNorm.getQNormName();
			//		list.add(qNormName);
				}
				//purReport.setQualityNormList(findList);
				model.addAttribute("qualityNormList", findList);
				String[] qualityNormArr = list.toArray(new String[0]);
			//	purReport.setQualityNormArr(qualityNormArr);
				
				//获取所有角色名称数组
				List<Role> allRole = systemService.findAllRole();
				List<String> roleList = new ArrayList<String>();
				//Iterator<Role> it1 = allRole.iterator();
//				while(it1.hasNext()){
//					Role role = it1.next();
//					roleList.add(role.getName());
//				}
//				String[] roleNameArr = roleList.toArray(new String[0]);
				//purReport.setRoleName(roleNameArr);
				
				model.addAttribute("purReport", purReport);
				
				if(StringUtils.isBlank(purReport.getId())){//如果ID是空为添加
					//创建日期对象
					Date date = new Date();
					purReport.setReportId(purReportService.getReportID());
					//获取时分秒
					String checktime = date.getHours()+"时"+date.getMinutes()+"分"+date.getSeconds()+"秒"; 
					purReport.setCheckTime(checktime);
					//purReport.setCheckDate(date);
					
					
					
					
					model.addAttribute("purReport", purReport);
					model.addAttribute("isAdd", true);
				}
		return "qualitymanage/purreport/purReportForm_deleteAsse";
	}
	
	
	@RequestMapping(value = "mydelete")
	public String mydelete(PurReport purReport, Model model) {
		//检验执行标准名称
				List<QualityNorm> findList = qualityNormMapper.findList(new QualityNorm());
				List<String> list = new ArrayList<String>();
				Iterator<QualityNorm> it = findList.iterator();
				while(it.hasNext()){
					QualityNorm qualityNorm = it.next();
			//		String qNormName = qualityNorm.getQNormName();
			//		list.add(qNormName);
				}
				//purReport.setQualityNormList(findList);
				model.addAttribute("qualityNormList", findList);
				String[] qualityNormArr = list.toArray(new String[0]);
			//	purReport.setQualityNormArr(qualityNormArr);
				
				//获取所有角色名称数组
				List<Role> allRole = systemService.findAllRole();
				List<String> roleList = new ArrayList<String>();
				//Iterator<Role> it1 = allRole.iterator();
//				while(it1.hasNext()){
//					Role role = it1.next();
//					roleList.add(role.getName());
//				}
//				String[] roleNameArr = roleList.toArray(new String[0]);
				//purReport.setRoleName(roleNameArr);
				
				model.addAttribute("purReport", purReport);
				
				if(StringUtils.isBlank(purReport.getId())){//如果ID是空为添加
					//创建日期对象
					Date date = new Date();
					purReport.setReportId(purReportService.getReportID());
					//获取时分秒
					String checktime = date.getHours()+"时"+date.getMinutes()+"分"+date.getSeconds()+"秒"; 
					purReport.setCheckTime(checktime);
					//purReport.setCheckDate(date);
					
					
					
					
					model.addAttribute("purReport", purReport);
					model.addAttribute("isAdd", true);
				}
		return "qualitymanage/purreport/purReportForm_delete";
	}
	
	
	
	@RequestMapping(value = "mydeleteReInv")
	public String mydeleteReInv(PurReport purReport, Model model) {
		//检验执行标准名称
				List<QualityNorm> findList = qualityNormMapper.findList(new QualityNorm());
				List<String> list = new ArrayList<String>();
				Iterator<QualityNorm> it = findList.iterator();
				while(it.hasNext()){
					QualityNorm qualityNorm = it.next();
			//		String qNormName = qualityNorm.getQNormName();
			//		list.add(qNormName);
				}
				//purReport.setQualityNormList(findList);
				model.addAttribute("qualityNormList", findList);
				String[] qualityNormArr = list.toArray(new String[0]);
			//	purReport.setQualityNormArr(qualityNormArr);
				
				//获取所有角色名称数组
				List<Role> allRole = systemService.findAllRole();
				List<String> roleList = new ArrayList<String>();
				//Iterator<Role> it1 = allRole.iterator();
//				while(it1.hasNext()){
//					Role role = it1.next();
//					roleList.add(role.getName());
//				}
//				String[] roleNameArr = roleList.toArray(new String[0]);
				//purReport.setRoleName(roleNameArr);
				
				model.addAttribute("purReport", purReport);
				
				if(StringUtils.isBlank(purReport.getId())){//如果ID是空为添加
					//创建日期对象
					Date date = new Date();
					purReport.setReportId(purReportService.getReportID());
					//获取时分秒
					String checktime = date.getHours()+"时"+date.getMinutes()+"分"+date.getSeconds()+"秒"; 
					purReport.setCheckTime(checktime);
					//purReport.setCheckDate(date);
					
					
					
					
					model.addAttribute("purReport", purReport);
					model.addAttribute("isAdd", true);
				}
		return "qualitymanage/purreport/ReInvPurReportFormDelete";
	}
	
	
	
	
	@RequestMapping(value = "mydeleteEle")
	public String mydeleteEle(PurReport purReport, Model model) {
		PurReport purReport2 = purReportService.get(purReport.getId());
		//purReportMapper.delete(purReport2);
		purReportService.delete(purReport2);
		
		return "redirect:"+Global.getAdminPath()+"/purreport/purReport/?repage";
	}
	
	
	@RequestMapping(value = "mydeleteEleReInv")
	public String mydeleteEleReInv(PurReport purReport, Model model) {
		PurReport purReport2 = purReportService.get(purReport.getId());
		//purReportMapper.delete(purReport2);
		purReportService.delete(purReport2);
		
		return "redirect:"+Global.getAdminPath()+"/purreport/purReport/reInvlist";
	}
	
	
	@RequiresPermissions("purreport:purReport:delAsse")
	@RequestMapping(value = "mydeleteEleAsse")
	public String mydeleteEleAsse(PurReport purReport, Model model) {
		PurReport purReport2 = purReportService.get(purReport.getId());
		//purReportMapper.delete(purReport2);
		purReportService.delete(purReport2);
		
		return "redirect:"+Global.getAdminPath()+"/purreport/purReport/asseList?repage";
	}
	
	@RequiresPermissions("purreport:purReport:delComp")
	@RequestMapping(value = "mydeleteEleCom")
	public String mydeleteEleCom(PurReport purReport, Model model) {
		PurReport purReport2 = purReportService.get(purReport.getId());
		//purReportMapper.delete(purReport2);
		purReportService.delete(purReport2);
		
		return "redirect:"+Global.getAdminPath()+"/purreport/purReport/completelist?repage";
	}
	
	@ModelAttribute
	public PurReport get(@RequestParam(required=false) String id) {
		PurReport entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = purReportService.get(id);
		}
		if (entity == null){
			entity = new PurReport();
		}
		return entity;
	}
	
	/**
	 * 检验单列表页面
	 */
	@RequiresPermissions("purreport:purReport:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "qualitymanage/purreport/purReportList";
	}
	
	
	/**
	 * 检验单列表页面
	 */
	@RequiresPermissions("purreport:purReport:listComp")
	@RequestMapping(value = "completelist")
	public String completelist() {
		return "qualitymanage/purreport/purReportListComplete";
	}
	
	
	@RequestMapping(value = "completeCheck")
	public String completeCheck() {
		return "qualitymanage/purreport/purReportListCompleteCheck";
	}
	
	
	/**
	 * 超期复验表页面
	 */
	@RequiresPermissions("purreport:purReport:reInvlist")
	@RequestMapping(value = "reInvlist")
	public String reInvlist() {
		return "qualitymanage/purreport/ReInvpurReportList";
	}
	
	
	
	
	/**
	 * 检验单列表页面
	 */
	@RequiresPermissions("purreport:purReport:listAsse")
	@RequestMapping(value = "asseList")
	public String asseList() {
		return "qualitymanage/purreport/purReportListAsse";
	}
	
	
	
	@ResponseBody
	@RequestMapping(value = "aajax")
	public String aajax( String userId,PurReport purReport, RedirectAttributes redirectAttributes) {
	//	String id = dbPurReport.getId();
		//判断合格、不合格、部分合格
		purReport = purReportService.subsave(purReport);
		//设置为编辑中
		purReport.setState("编辑中");
		InvCheckMain inv = purReport.getInvCheckMain();

		purReportService.save1(purReport);//保存
		//purReportService.executeUpdateSql("");
		addMessage(redirectAttributes, "保存检验单成功");
		return "success";
	}
	
	@ResponseBody
	@RequestMapping(value = "aajaxAsse")
	public String aajaxAsse( String userId,PurReport purReport, RedirectAttributes redirectAttributes) {
	//	String id = dbPurReport.getId();
		//判断合格、不合格、部分合格
		purReport = purReportService.subsave(purReport);
		//设置为编辑中
		purReport.setState("编辑中");
		purReportService.save1(purReport);//保存
		//purReportService.executeUpdateSql("");
		addMessage(redirectAttributes, "保存检验单成功");
		return "success";
	}
	
	
	
	
	
	
	@ResponseBody
	@RequestMapping(value = "myAjax")
	public String myAjax( PurReport purReport, RedirectAttributes redirectAttributes) {
	//	String id = dbPurReport.getId();
		//判断合格、不合格、部分合格
		purReport = purReportService.subsave(purReport);
		//设置为编辑中
		purReport.setState("编辑中");
		purReportService.save1(purReport);//保存
		//purReportService.executeUpdateSql("");
		purReportService.insertVerifyQCNorm(purReport);
		addMessage(redirectAttributes, "保存检验单成功");
		return "success";
	}
	
	
	@ResponseBody
	@RequestMapping(value = "ajaxCom")
	public String ajaxCom( VerifyQCNormCust verifyQCNormCust, RedirectAttributes redirectAttributes) {
	//	String id = dbPurReport.getId();
		//判断合格、不合格、部分合格
//		purReport = purReportService.subsave(purReport);
//		//设置为编辑中
//		purReport.setState("编辑中");
//		purReportService.myUpdata(purReport);//保存
		//purReportService.executeUpdateSql("");
		List<VerifyQCNorm> verifyQCNormList = verifyQCNormCust.getVerifyQCNormList();
		Iterator<VerifyQCNorm> it = verifyQCNormList.iterator();
		while(it.hasNext()){
			VerifyQCNorm verifyQCNorm = it.next();
			String isQuality = verifyQCNorm.getIsQuality();
			if(isQuality.equals("t")){
				verifyQCNorm.setIsfisished("1,0, ");
			}else if(isQuality.equals("f")){
				verifyQCNorm.setIsfisished("1,1,0");
				//写入问题处理单
				//---
			}else{
				verifyQCNorm.setIsfisished("1, , ");
			}
			
			VerifyQCNorm temp = verifyQCNormService.get(verifyQCNorm.getId());
			verifyQCNorm.setRoleCode(temp.getRoleCode());
			
			verifyQCNormService.save(verifyQCNorm);
		}
		
		addMessage(redirectAttributes, "保存检验单成功");
		return "success";
	}
	
	@ResponseBody
	@RequestMapping(value = "getComFormAjax")
	public PurReport getComFormAjax(PurReport purReport, RedirectAttributes redirectAttributes){
		String billNo = purReport.getSfcInvCheckMain().getBillNo();
		
		//String billNo = justifyRemark.substring(0, 15);
		//String sn = justifyRemark.substring(15,27);
		List<PurReportRSn> purReportRSnList = purReport.getPurReportRSnList();
		PurReportRSn psn = purReportRSnList.get(0);
		String sn = psn.getObjSn();
		
		SfcInvCheckMain sfcInvCheckMain2 = new SfcInvCheckMain();
		sfcInvCheckMain2.setBillNo(billNo);
		List<SfcInvCheckMain> list = sfcInvCheckMainService.findList(sfcInvCheckMain2);
		Iterator<SfcInvCheckMain> it = list.iterator();
		while(it.hasNext()){
			SfcInvCheckMain next = it.next();
			next = sfcInvCheckMainService.get(next.getId());
			List<SfcInvCheckDetail> sfcInvCheckDetailList = next.getSfcInvCheckDetailList();
			Iterator<SfcInvCheckDetail> itDetail = sfcInvCheckDetailList.iterator();
			while(itDetail.hasNext()){
				SfcInvCheckDetail sfcInvCheckDetail =itDetail.next();
				String itemCode = sfcInvCheckDetail.getItemCode();
				if(itemCode==null){continue;}
				if(itemCode.equals(sn)){
					PurReportRSn purReportRSn = new PurReportRSn();
					//两表字段手工对应
					purReportRSn.setId(purReportService.getUUID());
					purReportRSn.setObjSn(""+sfcInvCheckDetail.getSerialNo());
					purReportRSn.setObjCode(sfcInvCheckDetail.getItemCode());
					purReportRSn.setObjName(sfcInvCheckDetail.getItemName());
					purReportRSn.setCheckResult("");
					purReportRSn.setCheckDate(new Date());
					List<PurReportRSn> arrayList = new ArrayList<PurReportRSn>();
					arrayList.add(purReportRSn);
					purReport.setPurReportRSnList(arrayList);
					return purReport;
				}
			}
		}
		PurReport purReport2 = new PurReport();
		ArrayList<PurReportRSn> arrayList = new ArrayList<PurReportRSn>();
		PurReportRSn p = new PurReportRSn();
		arrayList.add(p);
		purReport2.setPurReportRSnList(arrayList);
		return purReport2;
	}
	
	
	@RequestMapping(value = "saveCom")
	public String saveCom(VerifyQCNormCust verifyQCNormCust, Model model, RedirectAttributes redirectAttributes) throws Exception{
		
		List<VerifyQCNorm> verifyQCNormList = verifyQCNormCust.getVerifyQCNormList();
		if(verifyQCNormList==null||verifyQCNormList.size()==0){
			return "qualitymanage/purreport/purReportListComplete";
		}
		
		String reportId=null;
		String objCode = null;
		Iterator<VerifyQCNorm> it = verifyQCNormList.iterator();
		while(it.hasNext()){
			VerifyQCNorm next = it.next();
			VerifyQCNorm verifyQCNorm = next;
			
			VerifyQCNorm temp = verifyQCNormService.get(verifyQCNorm.getId());
			verifyQCNorm.setRoleCode(temp.getRoleCode());
			verifyQCNorm.setIsfisished("2, , ");
			
			//保存一项
			verifyQCNormService.save(verifyQCNorm);
			reportId = verifyQCNorm.getReportId();
			objCode = verifyQCNorm.getObjCode();
		}
		
		
		List<VerifyQCNorm> verifyQCNormList1 = null;
		VerifyQCNorm verifyQCNorm = new VerifyQCNorm();
		//按照reportID和objCode查找全部对象
		verifyQCNorm.setReportId(reportId);
		verifyQCNorm.setObjCode(objCode);
		verifyQCNorm.setIsfisished(" , , ");
		
		verifyQCNorm.setCheckprj("安规");
		verifyQCNormList1 = verifyQCNormService.findList(verifyQCNorm);
		if(verifyQCNormList1!=null&&verifyQCNormList1.size()!=0){
			it = verifyQCNormList1.iterator();
			while(it.hasNext()){
				VerifyQCNorm verifyQCNorm2 = it.next();
				verifyQCNorm2.setIsfisished("0, , ");
				verifyQCNormService.save(verifyQCNorm2);
			}
			return "qualitymanage/purreport/purReportListComplete";
		}
		
		
		verifyQCNorm.setCheckprj("功能老化");
		verifyQCNormList1 = verifyQCNormService.findList(verifyQCNorm);
		if(verifyQCNormList1!=null&&verifyQCNormList1.size()!=0){
			it = verifyQCNormList1.iterator();
			while(it.hasNext()){
				VerifyQCNorm verifyQCNorm2 = it.next();
				verifyQCNorm2.setIsfisished("0, , ");
				verifyQCNormService.save(verifyQCNorm2);
			}
			return "qualitymanage/purreport/purReportListComplete";
		}
		
		verifyQCNorm.setCheckprj("激活");
		verifyQCNormList1 = verifyQCNormService.findList(verifyQCNorm);
		if(verifyQCNormList1!=null&&verifyQCNormList1.size()!=0){
			it = verifyQCNormList1.iterator();
			while(it.hasNext()){
				VerifyQCNorm verifyQCNorm2 = it.next();
				verifyQCNorm2.setIsfisished("0, , ");
				verifyQCNormService.save(verifyQCNorm2);
			}
			return "qualitymanage/purreport/purReportListComplete";
		}
		
		verifyQCNorm.setCheckprj("包装");
		verifyQCNormList1 = verifyQCNormService.findList(verifyQCNorm);
		if(verifyQCNormList1!=null&&verifyQCNormList1.size()!=0){
			it = verifyQCNormList1.iterator();
			while(it.hasNext()){
				VerifyQCNorm verifyQCNorm2 = it.next();
				verifyQCNorm2.setIsfisished("0, , ");
				verifyQCNormService.save(verifyQCNorm2);
			}
			return "qualitymanage/purreport/purReportListComplete";
		}
		
		
		
		//查询条件
		PurReport purReport = new PurReport();
		purReport.setReportId(reportId);
		//获取集合
		List<PurReport> findList = purReportService.findList(purReport);
		//遍历集合
		Iterator<PurReport> it1 = findList.iterator();
		while(it1.hasNext()){
			PurReport next = it1.next();
			next.setState("已审核");
			purReportService.save(next);
		}
		
		return "qualitymanage/purreport/purReportListComplete";
	}
	
	
	
	/**
	 * 检验单列表数据
	 */
	@ResponseBody
	@RequiresPermissions("purreport:purReport:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(PurReport purReport,String to, HttpServletRequest request, HttpServletResponse response, Model model) {
//		Page<PurReport> page = purReportService.findPage(new Page<PurReport>(request, response), purReport); 
//		return getBootstrapData(page);
		//获取当前用户信息
		//无法筛选当前用户，mapper中无cpersoncode，数据权限自己配置
				User user = UserUtils.getUser();
				String no = user.getNo();
				purReport.setCpersonCode(no);
				//设置搜索条件
				purReport.setState("编辑中");
				
				Page<PurReport> page = purReportService.findNewPage(new Page<PurReport>(request, response), purReport); 
				Map<String, Object> map = getBootstrapData(page);
				return map;
	}
	
	@ResponseBody
	@RequiresPermissions("purreport:purReport:list")
	@RequestMapping(value = "myData")
	public Map<String, Object> myData(PurReport purReport, HttpServletRequest request, HttpServletResponse response, Model model) {
		User user = UserUtils.getUser();
		String no = user.getNo();
		purReport.setCpersonCode(no);
		//设置搜索条件
		purReport.setState("编辑中");
		purReport.setChecktName("采购");
		Page<PurReport> page = purReportService.findNewPage(new Page<PurReport>(request, response), purReport); 
		Map<String, Object> map = getBootstrapData(page);
		return map;
	}
	
	
	@ResponseBody
	@RequiresPermissions("purreport:purReport:list")
	@RequestMapping(value = "myDataAsse")
	public Map<String, Object> myDataAsse(PurReport purReport, HttpServletRequest request, HttpServletResponse response, Model model) {
		User user = UserUtils.getUser();
		String no = user.getNo();
		purReport.setCpersonCode(no);
		//设置搜索条件
		purReport.setState("编辑中");
		purReport.setChecktName("装配");
		Page<PurReport> page = purReportService.findNewPage(new Page<PurReport>(request, response), purReport); 
		Map<String, Object> map = getBootstrapData(page);
		return map;
	}
	
	
	
	
	@ResponseBody
	@RequiresPermissions("purreport:purReport:list")
	@RequestMapping(value = "myDataComplete")
	public Map<String, Object> myDataComplete(PurReport purReport, HttpServletRequest request, HttpServletResponse response, Model model) {
		User user = UserUtils.getUser();
		String no = user.getNo();
		purReport.setCpersonCode(no);
		//设置搜索条件
		purReport.setState("编辑中");
		purReport.setChecktName("整机");
		Page<PurReport> page = purReportService.findNewPageForCom(new Page<PurReport>(request, response), purReport); 
		Map<String, Object> map = getBootstrapData(page);
		return map;
	}
	
	
	
	@ResponseBody
	@RequiresPermissions("purreport:purReport:list")
	@RequestMapping(value = "myDataReInv")
	public Map<String, Object> myDataReInv(PurReport purReport, HttpServletRequest request, HttpServletResponse response, Model model) {
		User user = UserUtils.getUser();
		String no = user.getNo();
		purReport.setCpersonCode(no);
		//设置搜索条件
		purReport.setState("编辑中");
		purReport.setChecktName("超期复验");
		Page<PurReport> page = purReportService.findNewPage(new Page<PurReport>(request, response), purReport); 
		Map<String, Object> map = getBootstrapData(page);
		return map;
	}
	
	
	
	@ResponseBody
	@RequiresPermissions("purreport:purReport:list")
	@RequestMapping(value = "findDate")
	public List<SfcInvCheckDetail> findDate(@RequestParam(required=false) String str, HttpServletRequest request, HttpServletResponse response, Model model) {
	    
		SfcInvCheckMain sfcInvCheckMain = sfcInvCheckMainService.get(str);
//		SfcInvCheckMain sfcInvCheck=new SfcInvCheckMain();
//		sfcInvCheck.setBillNo(sfcInvCheckMain.getBillNo());
//		sfcInvCheck.setId(sfcInvCheckMain.getId());
		Page<SfcInvCheckMain> page = sfcInvCheckMainService.findPage(new Page<SfcInvCheckMain>(request, response), sfcInvCheckMain); 

		
		
		
		
		List<SfcInvCheckMain> list = page.getList();
		Iterator<SfcInvCheckMain> it = list.iterator();
		List<SfcInvCheckMain> newlist = new ArrayList<SfcInvCheckMain>();
		while(it.hasNext()){
			SfcInvCheckMain next = it.next();
			
			List<SfcInvCheckDetail> sfcdetailList = new ArrayList<SfcInvCheckDetail>();
			
			List<SfcInvCheckDetail> sfcInvCheckDetailList = next.getSfcInvCheckDetailList();
			Iterator<SfcInvCheckDetail> it1 = sfcInvCheckDetailList.iterator();
			while(it1.hasNext()){
				SfcInvCheckDetail next2 = it1.next();
				next2.setId(purReportService.getUUID());
				sfcdetailList.add(next2);
			}
			next.setSfcInvCheckDetailList(sfcdetailList);
			newlist.add(next);
		}
		page.setList(newlist);
		page.setCount(newlist.size());
		//page.setCount(count);
		//Map<String, Object> map = getBootstrapData(page);		
		return sfcInvCheckMain.getSfcInvCheckDetailList();
	}
	
	
	
	@ResponseBody
	@RequiresPermissions("purreport:purReport:list")
	@RequestMapping(value = "findReInvDate")
	public List<PurReportRSn> findReInvDate(@RequestParam(required=false) String str, HttpServletRequest request, HttpServletResponse response, Model model) {
	    
		
		List<PurReportRSn> resultList = new ArrayList<PurReportRSn>();
		NewinvCheckMain newinvCheckMain = newinvCheckMainService.get(str);
		
		List<NewinvCheckDetail> newinvCheckDetailList = newinvCheckMain.getNewinvCheckDetailList();
		List<NewinvCheckDetailCode> codeList = newinvCheckMain.getNewinvCheckDetailCodeList();
		Iterator<NewinvCheckDetailCode> it = codeList.iterator();
		while(it.hasNext()){
			NewinvCheckDetailCode codeBean = it.next();
			PurReportRSn purReportRSn = new PurReportRSn();
			//获得对象序号
			String serialNum = codeBean.getItemBarcode();
			//获取物料编码
			String itemCode = codeBean.getItemCode();

			//获取物料名称
			ObjectDef objdef = new ObjectDef();

			objdef.setObjCode(itemCode);
			List<ObjectDef> objList = objectDefService.findList(objdef);
			String itemName = "";
			if(objList!=null&&objList.size()!=0){
				itemName = objList.get(0).getObjName();
			}

			//String itemName = reInvCheckDetail.getItemName();
			purReportRSn.setObjSn(serialNum+"");
			//purReportRSn.setSeriaNo(serialNum+"");
			//设置物料编码名称
			purReportRSn.setObjCode(itemCode);
			purReportRSn.setObjName(itemName);
			
			resultList.add(purReportRSn);
			
		}
		
		
		return resultList;
	}
	
	
	

	/**
	 * 采购查看，增加，编辑检验单表单页面
	 */
	@RequiresPermissions(value={"purreport:purReport:view","purreport:purReport:add","purreport:purReport:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(PurReport purReport, Model model) {
		//执行标准
		List<QualityNorm> findList = qualityNormMapper.findList(new QualityNorm());
		model.addAttribute("qualityNormList", findList);
		model.addAttribute("purReport", purReport);
		
		//执行岗位list
		List<Role> allRole = systemService.findAllRole();
		model.addAttribute("roleList", allRole);
		
		
		if(StringUtils.isBlank(purReport.getId())){//如果ID是空为添加
			//默认Role和office为IQC
			Role role = new Role();
			role.setName("IQC");
			List<Role> findRole = systemService.findRole(role);
			if(findRole!=null&&findRole.size()!=0){
				Role role2 = findRole.get(0);
				String roleId = role2.getId();
				purReport.setCpositionCode(roleId);
				purReport.setCpositionName("IQC");
			}
			
			//默认检验部门
			Office office = new Office();
			office.setName("IQC");
			List<Office> findOffice = officeMapper.findList(office);
			List<Office> list = new ArrayList<>();
			for(Office temp:findOffice){
				String officeName = temp.getName();
				if("IQC".equals(officeName)){
					list.add(temp);
				}
			}
			if(list!=null&&list.size()!=0){
				Office office2 = list.get(0);
				String officeId = office2.getId();
				purReport.setCdeptCode(officeId);
				purReport.setOffice(office2);
			}
			
			//创建日期对象
			Date date = new Date();
			purReport.setReportId(purReportService.getReportID());
			//获取时分秒
			String checktime = date.getHours()+"时"+date.getMinutes()+"分"+date.getSeconds()+"秒"; 
			purReport.setCheckTime(checktime);
			purReport.setCheckDate(date);
			purReport.setReportId(purReportService.getReportID());
			purReport.setIsAudit("未审核");
			model.addAttribute("isAdd", true);
			model.addAttribute("purReport", purReport);
		}
		return "qualitymanage/purreport/purReportForm";
	}
	
	
	
	
	
	/**
	 * 查看，增加，编辑检验单表单页面
	 */
	@RequiresPermissions(value={"purreport:purReport:view","purreport:purReport:add","purreport:purReport:edit"},logical=Logical.OR)
	@RequestMapping(value = "ReInvForm")
	public String ReInvForm(PurReport purReport, Model model) {

		ReinvCheckMain reinv = purReport.getReinvCheckMain();
		if(reinv!=null&&newinvCheckMainService.get(reinv.getId())!=null){
            NewinvCheckMain reinv1= newinvCheckMainService.get(reinv.getId());
           // reinv.setId(reinv1.getid());
            reinv.setBillNum(( reinv1).getBillNum());

			purReport.setReinvCheckMain(reinv);
		}




		//执行标准
		List<QualityNorm> findList = qualityNormMapper.findList(new QualityNorm());
		model.addAttribute("qualityNormList", findList);
		model.addAttribute("purReport", purReport);
		
		//执行岗位list
		List<Role> allRole = systemService.findAllRole();
		model.addAttribute("roleList", allRole);
		
		
		if(StringUtils.isBlank(purReport.getId())){//如果ID是空为添加
			
			
			//默认检验部门
			Office office = new Office();
			office.setName("品质部");
			List<Office> findOffice = officeMapper.findList(office);
			List<Office> list = new ArrayList<>();
			for(Office temp:findOffice){
				String officeName = temp.getName();
				if("品质部".equals(officeName)){
					list.add(temp);
				}
			}
			if(list!=null&&list.size()!=0){
				Office office2 = list.get(0);
				String officeId = office2.getId();
				purReport.setCdeptCode(officeId);
				purReport.setOffice(office2);
			}
			
			
			//创建日期对象
			Date date = new Date();
			purReport.setReportId(purReportService.getReportID());
			//获取时分秒
			String checktime = date.getHours()+"时"+date.getMinutes()+"分"+date.getSeconds()+"秒"; 
			purReport.setCheckTime(checktime);
			purReport.setCheckDate(date);
			purReport.setReportId(purReportService.getReportID());
			purReport.setIsAudit("未审核");
			model.addAttribute("isAdd", true);
			model.addAttribute("purReport", purReport);
		}
		return "qualitymanage/purreport/ReInvpurReportForm";
	}
	
	
	
	/**
	 * 查看，增加，编辑检验单表单页面
	 */
	@RequiresPermissions(value={"purreport:purReport:view","purreport:purReport:add","purreport:purReport:edit"},logical=Logical.OR)
	@RequestMapping(value = "myDetailReInv")
	public String myDetailReInv(PurReport purReport, Model model) {
		//执行标准
		List<QualityNorm> findList = qualityNormMapper.findList(new QualityNorm());
		model.addAttribute("qualityNormList", findList);
		model.addAttribute("purReport", purReport);
		
		//执行岗位list
		List<Role> allRole = systemService.findAllRole();
		model.addAttribute("roleList", allRole);
		
		
		if(StringUtils.isBlank(purReport.getId())){//如果ID是空为添加
			//创建日期对象
			Date date = new Date();
			purReport.setReportId(purReportService.getReportID());
			//获取时分秒
			String checktime = date.getHours()+"时"+date.getMinutes()+"分"+date.getSeconds()+"秒"; 
			purReport.setCheckTime(checktime);
			purReport.setCheckDate(date);
			purReport.setReportId(purReportService.getReportID());
			purReport.setIsAudit("未审核");
			model.addAttribute("isAdd", true);
			model.addAttribute("purReport", purReport);
		}
		return "qualitymanage/purreport/ReInvPurReportFormDetail";
	}
	
	
	
	
	
	
	
	/**
	 * 整机查看，增加，编辑检验单表单页面
	 */
	@RequiresPermissions(value={"purreport:purReport:viewComp","purreport:purReport:addComp","purreport:purReport:editComp"},logical=Logical.OR)
	@RequestMapping(value = "myForm")
	public String myForm(PurReport purReport, Model model) {
		
		
		
		//做转换
		List<PurReportRSn> li = new ArrayList<PurReportRSn>();
		List<PurReportRSn> purReportRSnList = purReport.getPurReportRSnList();
		Iterator<PurReportRSn> it = purReportRSnList.iterator();
		while(it.hasNext()){
			PurReportRSn purReportRSn = it.next();
			purReportRSn.setSeriaNo(purReportRSn.getObjSn());
			if(purReportRSn.getObjectDef()!=null){
				purReportRSn.setItemCode(purReportRSn.getObjectDef().getId());
			}
			purReportRSn.setItemName(purReportRSn.getObjName());
			
			li.add(purReportRSn);
		}
		purReport.setPurReportRSnList(li);
		
		
		//执行标准
		List<QualityNorm> findList = qualityNormMapper.findList(new QualityNorm());
		model.addAttribute("qualityNormList", findList);
		model.addAttribute("purReport", purReport);
		
		//执行岗位list
		List<Role> allRole = systemService.findAllRole();
		model.addAttribute("roleList", allRole);
		
		
		if(StringUtils.isBlank(purReport.getId())){//如果ID是空为添加
			
			//默认检验部门
			Office office = new Office();
			office.setName("品质部");
			List<Office> findOffice = officeMapper.findList(office);
			List<Office> list = new ArrayList<>();
			for(Office temp:findOffice){
				String officeName = temp.getName();
				if("品质部".equals(officeName)){
					list.add(temp);
				}
			}
			if(list!=null&&list.size()!=0){
				Office office2 = list.get(0);
				String officeId = office2.getId();
				purReport.setCdeptCode(officeId);
				purReport.setOffice(office2);
			}
			
			
			//创建日期对象
			Date date = new Date();
			purReport.setReportId(purReportService.getReportID());
			//获取时分秒
			String checktime = date.getHours()+"时"+date.getMinutes()+"分"+date.getSeconds()+"秒"; 
			purReport.setCheckTime(checktime);
			purReport.setCheckDate(date);
			purReport.setReportId(purReportService.getReportID());
			model.addAttribute("isAdd", true);
			//修改部分：直接跳到拆单后
			//return "qualitymanage/purreport/purReportFormCompleteNew";
			//整机检验单子表
			List<PurReportRSn> l = new ArrayList<PurReportRSn>();
			//每个检验单一条子表记录，创建一个子表对象
			PurReportRSn purReportRSn = new PurReportRSn();
			
			//设置子表的检验日期
			Date d = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String format = sdf.format(d);
			purReportRSn.setCheckDate(d);
			
			//设置子表的检验时间
			SimpleDateFormat sdf1 = new SimpleDateFormat("HH-mm-ss");
			String format2 = sdf1.format(d);
			purReportRSn.setCheckTime(format2);
		
			//设置子表的检验人
			String name = UserUtils.getUser().getName();
			purReportRSn.setCheckPerson(name);
			
			//对象加入列表
			l.add(purReportRSn);
			//列表加入主表
			purReport.setPurReportRSnList(l);
		}
		
		return "qualitymanage/purreport/purReportFormComplete";
	}
	
	

	/**
	 * 装配查看，增加，编辑检验单表单页面
	 */
	@RequiresPermissions(value={"purreport:purReport:viewAsse","purreport:purReport:addAsse","purreport:purReport:editAsse"},logical=Logical.OR)
	@RequestMapping(value = "formAsse")
	public String formAsse(PurReport purReport, Model model) {
		//执行标准
		List<QualityNorm> findList = qualityNormMapper.findList(new QualityNorm());
		model.addAttribute("qualityNormList", findList);
		model.addAttribute("purReport", purReport);
		
		//执行岗位list
		List<Role> allRole = systemService.findAllRole();
		model.addAttribute("roleList", allRole);
		
		
		if(StringUtils.isBlank(purReport.getId())){//如果ID是空为添加
			
			//默认检验部门
			Office office = new Office();
			office.setName("品质部");
			List<Office> findOffice = officeMapper.findList(office);
			List<Office> list = new ArrayList<>();
			for(Office temp:findOffice){
				String officeName = temp.getName();
				if("品质部".equals(officeName)){
					list.add(temp);
				}
			}
			if(list!=null&&list.size()!=0){
				Office office2 = list.get(0);
				String officeId = office2.getId();
				purReport.setCdeptCode(officeId);
				purReport.setOffice(office2);
			}
			
			
			
			//创建日期对象
			Date date = new Date();
			purReport.setReportId(purReportService.getReportID());
			//获取时分秒
			String checktime = date.getHours()+"时"+date.getMinutes()+"分"+date.getSeconds()+"秒"; 
			purReport.setCheckTime(checktime);
			purReport.setCheckDate(date);
			purReport.setIsAudit("未审核");
			purReport.setReportId(purReportService.getReportID());
			model.addAttribute("isAdd", true);
			model.addAttribute("purReport", purReport);
		}
		return "qualitymanage/purreport/purReportFormAsse";
	}
	
	
	@RequiresPermissions("purreport:purReport:view")
	@RequestMapping(value = "Mydetail")
	public String Mydetail(PurReport purReport, Model model ) {
		//检验执行标准名称
		List<QualityNorm> findList = qualityNormMapper.findList(new QualityNorm());
		List<String> list = new ArrayList<String>();
		Iterator<QualityNorm> it = findList.iterator();
		while(it.hasNext()){
			QualityNorm qualityNorm = it.next();
	//		String qNormName = qualityNorm.getQNormName();
	//		list.add(qNormName);
		}
		//purReport.setQualityNormList(findList);
		model.addAttribute("qualityNormList", findList);
		String[] qualityNormArr = list.toArray(new String[0]);
	//	purReport.setQualityNormArr(qualityNormArr);
		
		//获取所有角色名称数组
		List<Role> allRole = systemService.findAllRole();
		List<String> roleList = new ArrayList<String>();
		//Iterator<Role> it1 = allRole.iterator();
//		while(it1.hasNext()){
//			Role role = it1.next();
//			roleList.add(role.getName());
//		}
//		String[] roleNameArr = roleList.toArray(new String[0]);
		//purReport.setRoleName(roleNameArr);
		
		model.addAttribute("purReport", purReport);
		
		if(StringUtils.isBlank(purReport.getId())){//如果ID是空为添加
			//创建日期对象
			Date date = new Date();
			purReport.setReportId(purReportService.getReportID());
			//获取时分秒
			String checktime = date.getHours()+"时"+date.getMinutes()+"分"+date.getSeconds()+"秒"; 
			purReport.setCheckTime(checktime);
			//purReport.setCheckDate(date);
			
			
			
			
			model.addAttribute("purReport", purReport);
			model.addAttribute("isAdd", true);
		}
		return "qualitymanage/purreport/purReportForm_detail";
	}
	
	
	/**
	 * 装配的详情MydetailCom
	 */
	@RequiresPermissions("purreport:purReport:viewAsse")
	@RequestMapping(value = "MydetailAsse")
	public String MydetailAsse(PurReport purReport, Model model ) {
		//检验执行标准名称
		List<QualityNorm> findList = qualityNormMapper.findList(new QualityNorm());
		List<String> list = new ArrayList<String>();
		Iterator<QualityNorm> it = findList.iterator();
		while(it.hasNext()){
			QualityNorm qualityNorm = it.next();
	//		String qNormName = qualityNorm.getQNormName();
	//		list.add(qNormName);
		}
		//purReport.setQualityNormList(findList);
		model.addAttribute("qualityNormList", findList);
		String[] qualityNormArr = list.toArray(new String[0]);
	//	purReport.setQualityNormArr(qualityNormArr);
		
		//获取所有角色名称数组
		List<Role> allRole = systemService.findAllRole();
		List<String> roleList = new ArrayList<String>();
		//Iterator<Role> it1 = allRole.iterator();
//		while(it1.hasNext()){
//			Role role = it1.next();
//			roleList.add(role.getName());
//		}
//		String[] roleNameArr = roleList.toArray(new String[0]);
		//purReport.setRoleName(roleNameArr);
		
		model.addAttribute("purReport", purReport);
		
		if(StringUtils.isBlank(purReport.getId())){//如果ID是空为添加
			//创建日期对象
			Date date = new Date();
			purReport.setReportId(purReportService.getReportID());
			//获取时分秒
			String checktime = date.getHours()+"时"+date.getMinutes()+"分"+date.getSeconds()+"秒"; 
			purReport.setCheckTime(checktime);
			//purReport.setCheckDate(date);
			
			
			
			
			model.addAttribute("purReport", purReport);
			model.addAttribute("isAdd", true);
		}
		return "qualitymanage/purreport/purReportForm_detailAsse";
	}
	
	
	
	/**
	 * 整机的详情MydetailCom
	 */
	@RequiresPermissions("purreport:purReport:viewComp")
	@RequestMapping(value = "MydetailCom")
	public String MydetailCom(PurReport purReport, Model model ) {
		//检验执行标准名称
		List<QualityNorm> findList = qualityNormMapper.findList(new QualityNorm());
		List<String> list = new ArrayList<String>();
		Iterator<QualityNorm> it = findList.iterator();
		while(it.hasNext()){
			QualityNorm qualityNorm = it.next();
	//		String qNormName = qualityNorm.getQNormName();
	//		list.add(qNormName);
		}
		//purReport.setQualityNormList(findList);
		model.addAttribute("qualityNormList", findList);
		String[] qualityNormArr = list.toArray(new String[0]);
	//	purReport.setQualityNormArr(qualityNormArr);
		
		//获取所有角色名称数组
		List<Role> allRole = systemService.findAllRole();
		List<String> roleList = new ArrayList<String>();
		//Iterator<Role> it1 = allRole.iterator();
//		while(it1.hasNext()){
//			Role role = it1.next();
//			roleList.add(role.getName());
//		}
//		String[] roleNameArr = roleList.toArray(new String[0]);
		//purReport.setRoleName(roleNameArr);
		
		model.addAttribute("purReport", purReport);
		
		if(StringUtils.isBlank(purReport.getId())){//如果ID是空为添加
			//创建日期对象
			Date date = new Date();
			purReport.setReportId(purReportService.getReportID());
			//获取时分秒
			String checktime = date.getHours()+"时"+date.getMinutes()+"分"+date.getSeconds()+"秒"; 
			purReport.setCheckTime(checktime);
			//purReport.setCheckDate(date);
			
			
			
			
			model.addAttribute("purReport", purReport);
			model.addAttribute("isAdd", true);
		}
		return "qualitymanage/purreport/purReportForm_detailCom";
	}

	/**
	 * 整机追溯
	 * @param purReport
	 * @param model
	 * @return
	 */

	@RequestMapping(value = "MydetailComCheck")
	public String MydetailComCheck(PurReport purReport, Model model ) {
		//检验执行标准名称
		List<QualityNorm> findList = qualityNormMapper.findList(new QualityNorm());
		List<String> list = new ArrayList<String>();
		Iterator<QualityNorm> it = findList.iterator();
		while(it.hasNext()){
			QualityNorm qualityNorm = it.next();
			//		String qNormName = qualityNorm.getQNormName();
			//		list.add(qNormName);
		}
		//purReport.setQualityNormList(findList);
		model.addAttribute("qualityNormList", findList);
		String[] qualityNormArr = list.toArray(new String[0]);
		//	purReport.setQualityNormArr(qualityNormArr);

		//获取所有角色名称数组
		List<Role> allRole = systemService.findAllRole();
		List<String> roleList = new ArrayList<String>();
		//Iterator<Role> it1 = allRole.iterator();
//		while(it1.hasNext()){
//			Role role = it1.next();
//			roleList.add(role.getName());
//		}
//		String[] roleNameArr = roleList.toArray(new String[0]);
		//purReport.setRoleName(roleNameArr);

		model.addAttribute("purReport", purReport);

		if(StringUtils.isBlank(purReport.getId())){//如果ID是空为添加
			//创建日期对象
			Date date = new Date();
			purReport.setReportId(purReportService.getReportID());
			//获取时分秒
			String checktime = date.getHours()+"时"+date.getMinutes()+"分"+date.getSeconds()+"秒";
			purReport.setCheckTime(checktime);
			//purReport.setCheckDate(date);

		}

			////////////////////////////////////////////////////////////////////////////////////////////////////

			//整机追溯时，插入purReport子表
			//根据purReport_id 查询
			String reportId = purReport.getReportId();
			
			purReport = purReportService.get(purReport.getId());
			List<PurReportRSn> purReportRSnList = purReport.getPurReportRSnList();
			String mserialno = purReportRSnList.get(0).getMserialno();
//verifyQCNormService
			VerifyQCNorm verifyQCNorm = new VerifyQCNorm();
			verifyQCNorm.setReportId(reportId);
			List<VerifyQCNorm> verifyQCNormList = verifyQCNormService.findList(verifyQCNorm);

			//追溯时安规检验列表
			List<VerifyQCNorm> aList = new ArrayList<>();
			//追溯时老化检验列表
			List<VerifyQCNorm> lList = new ArrayList<>();
			//追溯时激活检验列表
			List<VerifyQCNorm> jList = new ArrayList<>();
			//追溯时包装检验列表
			List<VerifyQCNorm> bList = new ArrayList<>();

			//分组插入
			for(VerifyQCNorm verifyqcnorm : verifyQCNormList){
				
				verifyqcnorm.setMserialno(mserialno);
				String prjName = verifyqcnorm.getCheckprj();
				if("安规".equals(prjName)){
					aList.add(verifyqcnorm);
				}
				if("激活".equals(prjName)){
					jList.add(verifyqcnorm);
				}
				if("包装".equals(prjName)){
					bList.add(verifyqcnorm);
				}
				if("功能老化".equals(prjName)){
					lList.add(verifyqcnorm);
				}
				
				String isfisished = verifyqcnorm.getIsfisished();
				if(" , , ".equals(isfisished)){
					verifyqcnorm.setIsfisished("未检验");
				}
				if("0, , ".equals(isfisished)){
					verifyqcnorm.setIsfisished("正在检验");
				}
				if("1,0, ".equals(isfisished)){
					verifyqcnorm.setIsfisished("检验合格");
				}
				if("2, , ".equals(isfisished)){
					verifyqcnorm.setIsfisished("检验完成");
				}
				if("1,1,0".equals(isfisished)){
					verifyqcnorm.setIsfisished("检验不合格");
				}
			}

			purReport.setaList(aList);
			purReport.setbList(bList);
			purReport.setlList(lList);
			purReport.setjList(jList);


			model.addAttribute("purReport", purReport);
			model.addAttribute("isAdd", true);
		
		return "qualitymanage/purreport/purReportForm_detailComCheck";
	}




	/**
	 * 保存检验单
	 */
	@RequiresPermissions(value={"purreport:purReport:add","purreport:purReport:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(PurReport purReport, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, purReport)){
			return form(purReport, model);
		}
		purReport = purReportService.subsave(purReport);

		//向到货单主表回写标志1，标识已质检
        InvCheckMain invcheckMain = purReport.getInvCheckMain();
        String id = invcheckMain.getId();
        invcheckMain = invCheckMainService.get(id);
        invcheckMain.setQmsFlag("已质检");
        invCheckMainService.updateQmsFlag(invcheckMain);
		//新增或编辑表单保存
		purReport.setState("未审核");
		purReportService.updata(purReport);//保存
//		addMessage(redirectAttributes, "保存检验单成功");
		
//		
//		ProcessInstance pi = runtimeService.startProcessInstanceById(purReport.getAct().getProcDefId());;
//		List<Task> list = taskService.createTaskQuery().processInstanceId(pi.getId()).list();
////		TaskQuery processInstanceId = taskService.createTaskQuery().processInstanceId(pi.getId());
//		for(Task temp:list){
//			taskService.complete(temp.getId());
//		}
		return "redirect:"+Global.getAdminPath()+"/purreport/purReport/?repage";
		//return "redirect:" + adminPath + "/act/task";
	}
	
	
	
	@RequestMapping(value = "subsave")
	public String subsave(PurReport purReport, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, purReport)){
			return form(purReport, model);
		}
		purReport = purReportService.addJustifyLog(purReport);
		
		//新增或编辑表单保存
		//purReport.setCdeptCode(purReport.getOffice().getId());
		purReport = purReportService.subsave(purReport);
		purReport.setState("未审核");
		purReportService.save1(purReport);//保存
		addMessage(redirectAttributes, "保存检验单成功");
		return "redirect:"+Global.getAdminPath()+"/purreport/purReport/?repage";
	}
	
	
	
	
	@RequestMapping(value = "subSaveReInv")
	public String subSaveReInv(PurReport purReport, Model model, RedirectAttributes redirectAttributes) throws Exception{
		
		purReport = purReportService.addJustifyLog(purReport);
		
		//新增或编辑表单保存
		//purReport.setCdeptCode(purReport.getOffice().getId());
		purReport = purReportService.subsave(purReport);
		purReport.setState("未审核");
		purReportService.save1(purReport);//保存
		addMessage(redirectAttributes, "保存检验单成功");
		return "redirect:"+Global.getAdminPath()+"/purreport/purReport/reInvlist";
	}
	
	
	
	
	
	
		/**
	 * 采购到货管理列表数据
	 */
	@ResponseBody
	@RequiresPermissions("purinvcheckmain:invCheckMain:list")
	@RequestMapping(value = "invCheckMainData")
	public Map<String, Object> invCheckMainData(InvCheckMain invCheckMain, HttpServletRequest request, HttpServletResponse response, Model model) {
		//根据未质检状态查
		//invCheckMain.setQmsFlag("0");
		Page<InvCheckMain> page = purReportService.findInvCheckMainPage(new Page<InvCheckMain>(request, response), invCheckMain); 
		return getBootstrapData(page);
	}
	
	@ResponseBody
	@RequiresPermissions("purinvcheckmain:invCheckMain:list")
	@RequestMapping(value = "invCheckMainData2")
	public Map<String, Object> invCheckMainData2(PurReportNewCust purReportNewCust, HttpServletRequest request, HttpServletResponse response, Model model) {
		//根据未质检状态查
		//invCheckMain.setQmsFlag("0");

		Page<PurReportNew> page = purReportService.findInvCheckMainPage2(new Page<PurReportNew>(request, response), purReportNewCust); 
		return getBootstrapData(page);
	}
	
	

	
	@ResponseBody
	@RequiresPermissions("purreport:purReport:list")
	@RequestMapping(value = "SFCData")
	public Map<String, Object> SFCData(SfcInvCheckMain sfcInvCheckMain, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<SfcInvCheckMain> page = purReportService.findSFCPage(new Page<SfcInvCheckMain>(request, response), sfcInvCheckMain); 
		return getBootstrapData(page);
	}
	
	
	@ResponseBody
	@RequiresPermissions("purreport:purReport:list")
	@RequestMapping(value = "reInvData")
	public Map<String, Object> reInvData(ReinvCheckMain reinvCheckMain, HttpServletRequest request, HttpServletResponse response, Model model) {
		//reinvCheckMain.setQmsFlag("0");
		Page<ReinvCheckMain> page = purReportService.findReInvPage(new Page<ReinvCheckMain>(request, response), reinvCheckMain); 
		return getBootstrapData(page);
	}
	
	
	@RequestMapping(value = "subsaveAsse")
	public String subsaveAsse(PurReport purReport, Model model, RedirectAttributes redirectAttributes) throws Exception{
		
		
		//新增或编辑表单保存
		//purReport.setCdeptCode(purReport.getOffice().getId());
		purReport = purReportService.addJustifyLog(purReport);
		purReport = purReportService.subsave(purReport);
		purReport.setState("未审核");
		purReportService.save1(purReport);//保存
		addMessage(redirectAttributes, "保存检验单成功");
		return "redirect:"+Global.getAdminPath()+"/purreport/purReport/asseList?repage";
	}
	
	
	@RequestMapping(value = "subsaveCom")
	public String subsaveCom(PurReport purReport, Model model, RedirectAttributes redirectAttributes) throws Exception{
		
		
		//新增或编辑表单保存
		//purReport.setCdeptCode(purReport.getOffice().getId());
		purReport = purReportService.addJustifyLog(purReport);
		purReport = purReportService.subsave(purReport);
		purReport.setState("编辑中");
		//purReport = purReportService.get(purReport.getId());
		purReportService.save1(purReport);//保存
	//	purReportService.save(purReport);//保存
	//	purReportService.save(purReport);//保存
		//向表单中插入数据（若已有则不插入）
		purReportService.insertVerifyQCNorm(purReport);
		//获取数据(按照reportID查询)
		VerifyQCNorm verifyQCNorm = new VerifyQCNorm();
		purReport = purReportService.get(purReport.getId());
		List<PurReportRSn> purReportRSnList = purReport.getPurReportRSnList();
		if(purReportRSnList!=null&&purReportRSnList.size()!=0){
			
			verifyQCNorm.setObjCode(purReportRSnList.get(0).getObjCode());
		}
		verifyQCNorm.setReportId(purReport.getReportId());
		
		List<VerifyQCNorm> verifyQCNormList = verifyQCNormService.findList(verifyQCNorm);
		//verifyQCNorm.setIsfisished("0");ll
		/*
		 * 控制显示"0,,"或"1,,"的展示
		 * */
		List<VerifyQCNorm> verifyQCNormList1 = new ArrayList<VerifyQCNorm>();
		for(VerifyQCNorm temp:verifyQCNormList){
			String isfisished = temp.getIsfisished();
			String[] split = isfisished.split(",");
			if(split[0].equals("0")||split[0].equals("1")){
				verifyQCNormList1.add(temp);
			}
		}
		
		//顺序  写在提交中
		//List<VerifyQCNorm> verifyQCNormList2 = new ArrayList<VerifyQCNorm>();
		
		
		
		//权限
		List<VerifyQCNorm> verifyQCNormList2 = new ArrayList<VerifyQCNorm>();
		List<String> roleIdList = UserUtils.getUser().getRoleIdList();
		Iterator<String> it = roleIdList.iterator();
		while(it.hasNext()){
			String roleId = it.next();
			for(VerifyQCNorm temp:verifyQCNormList1){
				if(temp!=null&&temp.getRoleCode()!=null){
					if(temp.getRoleCode().equals(roleId)){
						verifyQCNormList2.add(temp);
					}
				}
				
			}
		}
		
		//存入展示
		model.addAttribute("verifyQCNormList", verifyQCNormList2);
		
		return "qualitymanage/purreport/purReportFormCompleteVerify";
	}
	
	
	
	@RequestMapping(value = "subsaveComNew")
	public String subsaveComNew(PurReport purReport) throws Exception{
		
		//按照子表拆单，有几条子表拆成几个
		 String billNo = purReport.getSfcInvCheckMain().getBillNo();
		 //按照billNo查询
		 SfcInvCheckMain sfcInvCheckMainTemp = new SfcInvCheckMain();
		 sfcInvCheckMainTemp.setBillNo(billNo);
		 List<SfcInvCheckMain> sfcInvCheckMainList = sfcInvCheckMainService.findList(sfcInvCheckMainTemp);
		 //遍历
		Iterator<SfcInvCheckMain> it = sfcInvCheckMainList.iterator();
		while(it.hasNext()){
			SfcInvCheckMain sfcInvCheckMain = it.next();
			
			sfcInvCheckMain = sfcInvCheckMainService.get(sfcInvCheckMain.getId());
			List<SfcInvCheckDetail> sfcInvCheckDetailList = sfcInvCheckMain.getSfcInvCheckDetailList();
			Iterator<SfcInvCheckDetail> it1 = sfcInvCheckDetailList.iterator();
			while(it1.hasNext()){
				List<PurReportRSn> list = new ArrayList<PurReportRSn>();
				SfcInvCheckDetail SfcInvCheckDetail = it1.next();
				PurReportRSn purReportRSn = new PurReportRSn();
				//两表字段手工对应getSeriaNo
				purReportRSn.setId(purReportService.getUUID());
				purReportRSn.setObjSn(""+SfcInvCheckDetail.getSerialNo());
				purReportRSn.setObjCode(SfcInvCheckDetail.getItemCode());
				purReportRSn.setObjName(SfcInvCheckDetail.getItemName());
				purReportRSn.setCheckResult("");
				purReportRSn.setCheckDate(new Date());
				list.add(purReportRSn);
				//拆表保存
				purReport.setPurReportRSnList(list);
				//保存
				purReport = purReportService.subsave(purReport);
				//设置为编辑中
				purReport.setState("编辑中");
				purReport.setId("");
				purReport.setReportId(purReportService.getReportID());
				purReportService.save1(purReport);//保存
				purReportService.insertVerifyQCNorm(purReport);
			}
		}
		return "redirect:"+Global.getAdminPath()+"/purreport/purReport/completelist";
	}
	
	
	
	
	/**
	 * 删除检验单
	 */
	@ResponseBody
	@RequiresPermissions("purreport:purReport:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(PurReport purReport, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		purReportService.delete(purReport);
		j.setMsg("删除检验单成功");
		return j;
	}
	
	/**
	 * 批量删除检验单
	 */
	@ResponseBody
	@RequiresPermissions("purreport:purReport:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			purReportService.delete(purReportService.get(id));
		}
		j.setMsg("删除检验单成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("purreport:purReport:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(PurReport purReport, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "检验单"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<PurReport> page = purReportService.findPage(new Page<PurReport>(request, response, -1), purReport);
    		new ExportExcel("检验单", PurReport.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出检验单记录失败！失败信息："+e.getMessage());
		}
			return j;
    }
    
    @ResponseBody
    @RequestMapping(value = "detail")
	public PurReport detail(String id) {
		return purReportService.get(id);
	}
	

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("purreport:purReport:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<PurReport> list = ei.getDataList(PurReport.class);
			for (PurReport purReport : list){
				try{
					purReportService.save(purReport);
					purReport.setState("未审核");
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条检验单记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条检验单记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入检验单失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/purreport/purReport/?repage";
    }
	
	/**
	 * 下载导入检验单数据模板
	 */
	@RequiresPermissions("purreport:purReport:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "检验单数据导入模板.xlsx";
    		List<PurReport> list = Lists.newArrayList(); 
    		new ExportExcel("检验单数据", PurReport.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/purreport/purReport/?repage";
    }
	
	
	/**
	 * 返回物料名称
	 */
	@ResponseBody
	@RequestMapping(value = "ajaxForObjName")
	public String ajaxForObjName(String objCode,String objSn){
		
		//objsn ->机台序列码20位
		ProcessRoutineDetail processRoutineDetail = new ProcessRoutineDetail();
		processRoutineDetail.setObjSn(objSn);
		List<ProcessRoutineDetail> findList2 = processRoutineDetailService.findList(processRoutineDetail);
		String mserialno ="";
		if(findList2!=null&&findList2.size()!=0){
			mserialno = findList2.get(0).getMserialno();
		}
		
		
		
		
		ObjectDef objdef = new ObjectDef();
		objdef.setObjCode(objCode);
		List<ObjectDef> findList = objectDefService.findList(objdef);
		if(findList!=null&&findList.size()!=0){
			ObjectDef objectDef = findList.get(0);
			if(objectDef!=null){
				return objectDef.getObjName()+"|"+mserialno;
			}
		}
		return "";
	}

	/**
	 * 返回物料名称
	 */
	@ResponseBody
	@RequestMapping(value = "ajaxForObjNameCheck")
	public String ajaxForObjNameCheck(String objSn){

		//返回1表示已存在，返回2表示不存在
		String result = "2";
		PurReport purReport = new PurReport();
		purReport.setChecktName("装配");
		List<PurReport> purReportList = purReportService.findList(purReport);
		for(PurReport temp : purReportList){
			temp = purReportService.get(temp.getId());
			List<PurReportRSn> purReportRSnList = temp.getPurReportRSnList();
			for(PurReportRSn purReportRSn:purReportRSnList){
				String objSn1 = purReportRSn.getObjSn();
				if(objSn1.equals(objSn)){
					result = "1";
					return result;
				}
			}
		}
		return result;
	}

	@ResponseBody
	@RequestMapping(value = "ajaxForObjNameCom")
	public String ajaxForObjNameCom(String objSn){

		//返回1表示已存在，返回2表示不存在
		String result = "2";
		PurReport purReport = new PurReport();
		purReport.setChecktName("整机");
		List<PurReport> purReportList = purReportService.findList(purReport);
		for(PurReport temp : purReportList){
			temp = purReportService.get(temp.getId());
			List<PurReportRSn> purReportRSnList = temp.getPurReportRSnList();
			for(PurReportRSn purReportRSn:purReportRSnList){
				String objSn1 = purReportRSn.getObjSn();
				if(objSn1.equals(objSn)){
					result = "1";
					return result;
				}
			}
		}
		return result;
	}


	

}