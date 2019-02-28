/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.qualitymanage.purinvcheckmain.web;

import com.google.common.collect.Lists;
import com.hqu.modules.basedata.item.service.ItemService;
import com.hqu.modules.inventorymanage.billtype.entity.BillType;
import com.hqu.modules.inventorymanage.billtype.entity.BillTypeWareHouse;
import com.hqu.modules.inventorymanage.billtype.mapper.BillTypeMapper;
import com.hqu.modules.inventorymanage.billtype.mapper.BillTypeWareHouseMapper;
import com.hqu.modules.inventorymanage.employee.entity.Employee;
import com.hqu.modules.inventorymanage.employee.mapper.EmployeeWareHouseMapper;
import com.hqu.modules.inventorymanage.employee.service.EmployeeService;
import com.hqu.modules.inventorymanage.warehouse.entity.WareHouse;
import com.hqu.modules.inventorymanage.warehouse.mapper.WareHouseMapper;
import com.hqu.modules.purchasemanage.applymain.entity.ApplyDetail;
import com.hqu.modules.purchasemanage.applymain.entity.ApplyMain;
import com.hqu.modules.purchasemanage.applymain.mapper.ApplyMainMapper;
import com.hqu.modules.purchasemanage.applymain.service.ApplyMainService;
import com.hqu.modules.purchasemanage.contractmain.entity.ContractDetail;
import com.hqu.modules.purchasemanage.contractmain.entity.ContractMain;
import com.hqu.modules.purchasemanage.contractmain.mapper.ContractMainMapper;
import com.hqu.modules.purchasemanage.contractmain.service.ContractMainService;
import com.hqu.modules.purchasemanage.group.entity.Group;
import com.hqu.modules.purchasemanage.group.entity.GroupBuyer;
import com.hqu.modules.purchasemanage.group.entity.GroupWare;
import com.hqu.modules.purchasemanage.group.mapper.GroupBuyerMapper;
import com.hqu.modules.purchasemanage.group.mapper.GroupMapper;
import com.hqu.modules.purchasemanage.group.mapper.GroupWareMapper;
import com.hqu.modules.purchasemanage.purplan.entity.PurPlanDetail;
import com.hqu.modules.purchasemanage.purplan.entity.PurPlanMain;
import com.hqu.modules.purchasemanage.purplan.mapper.PurPlanDetailMapper;
import com.hqu.modules.purchasemanage.purplan.service.PurPlanMainService;
import com.hqu.modules.qualitymanage.objectdef.entity.ObjectDef;
import com.hqu.modules.qualitymanage.objectdef.mapper.ObjectDefMapper;
import com.hqu.modules.qualitymanage.purinvcheckmain.entity.*;
import com.hqu.modules.qualitymanage.purinvcheckmain.mapper.*;
import com.hqu.modules.qualitymanage.purinvcheckmain.service.InvCheckMainService;
import com.hqu.modules.qualitymanage.purreportnew.entity.PurReportNew;
import com.hqu.modules.qualitymanage.purreportnew.service.PurReportNewService;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.excel.ExportExcel;
import com.jeeplus.common.utils.excel.ImportExcel;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.mapper.UserMapper;
import com.jeeplus.modules.sys.utils.UserUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.codehaus.groovy.runtime.InvokerHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * 入库通知单Controller
 * @author 张铮
 * @version 2018-04-21
 */
@Controller
@RequestMapping(value = "${adminPath}/purinvcheckmain/invCheckMain")
public class InvCheckMainController extends BaseController {
	@Autowired
	private PurPlanDetailMapper purPlanDetailMapper;
	@Autowired
	private PurPlanMainService purPlanMainService;
	@Autowired
	private InvCheckMainService invCheckMainService;
	@Autowired
	private EmployeeService employeeService;
	@Autowired
	private EmployeeWareHouseMapper employeeWareHouseMapper;
	@Autowired
	private InvCheckMainMapper invCheckMainMapper;
	@Autowired
	private InvCheckRelationsMapper invCheckRelationsMapper;
	@Autowired
	private ApplyMainMapper applyMainMapper;
	@Autowired
	private GroupBuyerMapper groupBuyerMapper;
	@Autowired
	private WareHouseMapper wareHouseMapper;
	@Autowired
	private GroupWareMapper groupWareMapper;
	@Autowired
	private GroupMapper groupMapper;
	@Autowired
	private BillTypeWareHouseMapper billTypeWareHouseMapper;
	@Autowired
	private BillTypeMapper billTypeMapper;
	@Autowired
	private ObjectDefMapper objectDefMapper;
	@Autowired
	private ItemService itemService;
	
	@Autowired
	private ApplyMainService applyMainService;
	
	@Autowired
	private UserMapper userMapper;
	
	@Autowired
	private ContractMainMapper contractMainMapper;
	
	@Autowired
	private ContractMainService contractMainService;
	
	@Autowired
	private ItemforInvMapper itemforInvMapper;

	@Autowired
	private InvCheckDetailCodeMapper invCheckDetailCodeMapper;

	@Autowired
	private PurReportNewService purReportNewService;

	@Autowired
	private InvCheckDetailMapper invCheckDetailMapper;
	
	@ModelAttribute
	public InvCheckMain get(@RequestParam(required=false) String id) {
		InvCheckMain entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = invCheckMainService.get(id);
		}
		if (entity == null){
			entity = new InvCheckMain();
		}
		return entity;
	}
	
	/**
	 * 采购到货录入列表页面
	 */
	@RequiresPermissions("purinvcheckmain:invCheckMain:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "qualitymanage/purinvcheckmain/invCheckMainList";
	}
	
	/**
	 * 采购到货查询列表页面
	 */
	@RequiresPermissions("purinvcheckmain:invCheckMain:list")
	@RequestMapping(value = "listQuery")
	public String listQuery() {
		return "qualitymanage/purinvcheckmain/invCheckMainQueryList";
	}
	
	/**
	 * 采购到货审核列表页面
	 */
	@RequiresPermissions("purinvcheckmain:invCheckMain:list")
	@RequestMapping(value = {"listAudit"})
	public String listAudit() {
		return "qualitymanage/purinvcheckmain/invCheckMainAuditList";
	}
	
	/**
	 * 采购到货生成列表页面
	 */
	@RequiresPermissions("purinvcheckmain:invCheckMain:list")
	@RequestMapping(value = "listCreat")
	public String listCreat() {
		return "qualitymanage/purinvcheckmain/invCheckMainCreatList";
	}
	
	/**
	 * 采购到货物料二维码列表页面
	 */
	@RequiresPermissions("purinvcheckmain:invCheckMain:list")
	@RequestMapping(value = "listWarehousing")
	public String listWarehousing() {
		return "qualitymanage/purinvcheckmain/invCheckMainWarehousingList";
	}

	/**
	 * 采购到货手动生成物料二维码列表页面
	 */
	@RequestMapping(value = "QRCodeGen")
	public String QRCodeGen() {
		return "qualitymanage/purinvcheckmain/invCheckMainQRCodeGeneration";
	}
	
	/**
	 * 采购到货退货列表页面
	 */
	@RequiresPermissions("purinvcheckmain:invCheckMain:list")
	@RequestMapping(value = "listBack")
	public String listBack() {
		return "qualitymanage/purinvcheckmain/invCheckMainWarehousingBackList";
	}
	
	/**
	 * 采购到货(录入)列表数据
	 */
	@ResponseBody
	@RequiresPermissions("purinvcheckmain:invCheckMain:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(InvCheckMain invCheckMain, HttpServletRequest request, HttpServletResponse response, Model model) {
		List<InvCheckMain> list = invCheckMainService.findTypingList(invCheckMain); 
		Page<InvCheckMain> page = new Page<InvCheckMain>(request, response);
		//分页
        String intPage= request.getParameter("pageNo");
        int pageNo=Integer.parseInt(intPage);
        int pageSize= page.getPageSize();
        page.setCount(list.size());
        if(pageNo==1&&pageSize==-1){
    	   page.setList(list);
    	   
        }else{
    	   List<InvCheckMain> reportL=  new ArrayList<InvCheckMain>();      
	        for(int i=(pageNo-1)*pageSize;i<list.size() && i<pageNo*pageSize;i++){
	        	reportL.add(list.get(i));
		    }
	        page.setList(reportL);
	        
        }
		return getBootstrapData(page);
	}

	/**
	 * 外购件入库查询已经审核通过的到货单
	 */
	@ResponseBody
	@RequiresPermissions("purinvcheckmain:invCheckMain:list")
	@RequestMapping(value = "databyoutsouring")
	public Map<String, Object> datadatabyoutsouring(InvCheckMain invCheckMain, HttpServletRequest request, HttpServletResponse response, Model model) {

		List<InvCheckMain> invCheckMainList = new ArrayList<>();
		if(StringUtils.isBlank(invCheckMain.getBillnum())) {
			//选择审核通过的到货单
			invCheckMain.setBillStateFlag("E");
			invCheckMain.setQmsFlag("已质检");
			List<InvCheckMain> list = invCheckMainMapper.findListForInv(invCheckMain);
			for (int i = 0; i < list.size(); i++) {
				PurReportNew purReportNew = new PurReportNew();
				purReportNew.setInv(list.get(i));
				purReportNew.setCheckResult("合格");
				purReportNew.setHandleResult("入库");
				List<PurReportNew> purReportNewList = purReportNewService.findList(purReportNew);
				if (purReportNewList.size() >= 1) {
					invCheckMainList.add(list.get(i));
				}
//			for(int j=0;j<purReportNewList.size();j++){
//				invCheckMainList.add(purReportNewList.get(j).getInv());
//			}
			}

			//找到审核通过的到货单
			InvCheckMain invCheckMainTmp = new InvCheckMain();
			invCheckMainTmp.setBillStateFlag("E");
			List<InvCheckMain> listTmp = invCheckMainMapper.findListForInv(invCheckMainTmp);

			//筛选不质检的到货单，前提：一个到货单只有一条子表数据
			for (InvCheckMain invCheckMain1 : listTmp) {
				InvCheckDetail invCheckDetail = new InvCheckDetail();
				invCheckDetail.setInvCheckMain(invCheckMain1);
				List<InvCheckDetail> invCheckDetailList = invCheckDetailMapper.findListbyBillNum(invCheckDetail);

				//子表不为空&&子表长度为1&&子表定义为不质检
			/*qmsFlag 0 不质检
					1 质检
					null 未定义*/
				if (invCheckDetailList != null && invCheckDetailList.size() == 1) {


					if (invCheckDetailList.get(0).getQmsFlag() != null && invCheckDetailList.get(0).getQmsFlag().equals("0")) {
						invCheckMainList.add(invCheckMain1);
					}
				}

			}
		}else {
			invCheckMainList = invCheckMainService.findList(invCheckMain);
		}

		Page<InvCheckMain> page = new Page<InvCheckMain>(request, response);
		//分页
		logger.debug("list:"+invCheckMainList);

		String intPage= request.getParameter("pageNo");
		int pageNo=Integer.parseInt(intPage);
		int pageSize= page.getPageSize();
		page.setCount(invCheckMainList.size());
		if(pageNo==1&&pageSize==10){
			page.setList(invCheckMainList);

		}else{
			List<InvCheckMain> reportL=  new ArrayList<InvCheckMain>();
			for(int i=(pageNo-1)*pageSize;i<invCheckMainList.size() && i<pageNo*pageSize;i++){
				reportL.add(invCheckMainList.get(i));
			}
			page.setList(reportL);

		}
		logger.debug("page:"+page);
		return getBootstrapData(page);
	}


	/**
	 * 采购到货(查询)列表数据
	 */
	@ResponseBody
	@RequiresPermissions("purinvcheckmain:invCheckMain:list")
	@RequestMapping(value = {"dataQuery"})
	public Map<String, Object> databack(InvCheckMain invCheckMain, HttpServletRequest request, HttpServletResponse response, Model model) {
		List<InvCheckMain> list=invCheckMainService.findList(invCheckMain);
		invCheckMainService.ListToChinese(list);
		Page<InvCheckMain> page=new Page<InvCheckMain>(request, response);
		//分页
        String intPage= request.getParameter("pageNo");
        int pageNo=Integer.parseInt(intPage);
        int pageSize= page.getPageSize();
        page.setCount(list.size());
        if(pageNo==1&&pageSize==-1){
    	   page.setList(list);
    	   
        }else{
    	    List<InvCheckMain> reportL=  new ArrayList<InvCheckMain>();      
	        for(int i=(pageNo-1)*pageSize;i<list.size()&& i<pageNo*pageSize;i++){
	        	reportL.add(list.get(i));
		    }
	        page.setList(reportL);

        }
		return getBootstrapData(page);
	}
	
	
	/**
	 * 采购到货(审核)列表数据
	 */
	@ResponseBody
	@RequiresPermissions("purinvcheckmain:invCheckMain:list")
	@RequestMapping(value = {"dataAudit"})
	public Map<String, Object> dataAudit(InvCheckMain invCheckMain, HttpServletRequest request, HttpServletResponse response, Model model) {
		invCheckMain.setBillStateFlag("W");
		List<InvCheckMain> list=invCheckMainService.findListbyBillStateFlag(invCheckMain);
		invCheckMainService.ListToChinese(list);
		Page<InvCheckMain> page=new Page<InvCheckMain>(request, response);
		//分页
        String intPage= request.getParameter("pageNo");
        int pageNo=Integer.parseInt(intPage);
        int pageSize= page.getPageSize();
        page.setCount(list.size());
        if(pageNo==1&&pageSize==-1){
    	   page.setList(list);
    	   
        }else{
    	    List<InvCheckMain> reportL=  new ArrayList<InvCheckMain>();      
	        for(int i=(pageNo-1)*pageSize;i<list.size()&& i<pageNo*pageSize;i++){
	        	reportL.add(list.get(i));
		    }
	        page.setList(reportL);

        }
		return getBootstrapData(page);
	}
	
	/**
	 * 采购到货(生成)列表数据
	 */
	@ResponseBody
	@RequiresPermissions("purinvcheckmain:invCheckMain:list")
	@RequestMapping(value = {"dataCreat"})
	public Map<String, Object> dataCreat(InvCheckMain invCheckMain, HttpServletRequest request, HttpServletResponse response, Model model) {
		List<InvCheckMain> list = invCheckMainService.findCreatedList(invCheckMain); 
		Page<InvCheckMain> page = new Page<InvCheckMain>(request, response);
		//分页
        String intPage= request.getParameter("pageNo");
        int pageNo=Integer.parseInt(intPage);
        int pageSize= page.getPageSize();
        page.setCount(list.size());
        if(pageNo==1&&pageSize==-1){
    	   page.setList(list);
    	   
        }else{
    	   List<InvCheckMain> reportL=  new ArrayList<InvCheckMain>();      
	        for(int i=(pageNo-1)*pageSize;i<list.size() && i<pageNo*pageSize;i++){
	        	reportL.add(list.get(i));
		    }
	        page.setList(reportL);
	        
        }
		return getBootstrapData(page);
	}
	
	/**
	 * 采购到货(生成二维码)列表数据
	 */
	@ResponseBody
	@RequiresPermissions("purinvcheckmain:invCheckMain:list")
	@RequestMapping(value = {"dataWarehousing"})
	public Map<String, Object> dataWareHousing(InvCheckMain invCheckMain, HttpServletRequest request, HttpServletResponse response, Model model) {
		List<InvCheckMain> list = invCheckMainService.findWareHousingList(invCheckMain); 
		Page<InvCheckMain> page = new Page<InvCheckMain>(request, response);
		//分页
        String intPage= request.getParameter("pageNo");
        int pageNo=Integer.parseInt(intPage);
        int pageSize= page.getPageSize();
        page.setCount(list.size());
        if(pageNo==1&&pageSize==-1){
    	   page.setList(list);
    	   
        }else{
    	   List<InvCheckMain> reportL=  new ArrayList<InvCheckMain>();      
	        for(int i=(pageNo-1)*pageSize;i<list.size() && i<pageNo*pageSize;i++){
	        	reportL.add(list.get(i));
		    }
	        page.setList(reportL);
	        
        }
		return getBootstrapData(page);
	}
	
//	/**
//	 * 采购到货退货列表数据
//	 */
//	@ResponseBody
//	@RequiresPermissions("purinvcheckmain:invCheckMain:list")
//	@RequestMapping(value = "dataBack")
//	public Map<String, Object> dataBack(InvCheckMain invCheckMain, HttpServletRequest request, HttpServletResponse response, Model model) {
//		Page<InvCheckMain> page = invCheckMainService.findPage(new Page<InvCheckMain>(request, response), invCheckMain); 
//		return getBootstrapData(page);
//	}
	
	public String creatBillnum(){
		//生成单据编号
		Date date = new Date();
		Random random = new Random();
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
		int rand = random.nextInt(10000);
		String randnum = null;
		//满足四位流水
		if(rand<100){
			randnum = "00"+rand;
		}
		else if(rand<1000){
			randnum = "0"+rand;
		}
		else{
			randnum = String.valueOf(rand);
		}
		String billn = "chk"+df.format(date.getTime())+randnum;
		
		return billn;
	}
	
	/**
	 * 增加采购到货录入表单页面
	 */
	@RequiresPermissions(value="purinvcheckmain:invCheckMain:add")
	@RequestMapping(value = "form")
	public String form(InvCheckMain invCheckMain, Model model) {
		invCheckMainService.setDefultIO(invCheckMain);
		String billn = creatBillnum();
		invCheckMain.setBillnum(billn);
		List<InvCheckMain> list = invCheckMainMapper.findList(invCheckMain);
		if(list.isEmpty()){
		model.addAttribute("invCheckMain", invCheckMain);
		if(StringUtils.isBlank(invCheckMain.getId())){//如果ID是空为添加
			model.addAttribute("isAdd", true);
		}
			return "qualitymanage/purinvcheckmain/invCheckMainForm";
		}
		else
			return "redirect:"+Global.getAdminPath()+"/purinvcheckmain/invCheckMain/form?repage";
	}
	
	/**
	 * 采购到货录入表单详情页面
	 */
	@RequiresPermissions(value="purinvcheckmain:invCheckMain:add")
	@RequestMapping(value = "formDetail")
	public String formDetail(InvCheckMain invCheckMain, Model model) {
		invCheckMainService.toChinese(invCheckMain);
		model.addAttribute("invCheckMain", invCheckMain);
		if(StringUtils.isBlank(invCheckMain.getId())){//如果ID是空为添加
			model.addAttribute("isAdd", true);
		}
		return "qualitymanage/purinvcheckmain/invCheckMainDetailForm";
	}
	
	/**
	 * 采购到货审核表单详情页面
	 */
	@RequiresPermissions(value="purinvcheckmain:invCheckMain:add")
	@RequestMapping(value = "formAuditDetail")
	public String formAuditDetail(InvCheckMain invCheckMain, Model model) {
		invCheckMainService.toChinese(invCheckMain);
		model.addAttribute("invCheckMain", invCheckMain);
		if(StringUtils.isBlank(invCheckMain.getId())){//如果ID是空为添加
			model.addAttribute("isAdd", true);
		}
		return "qualitymanage/purinvcheckmain/invCheckMainAuditForm";
	}
	
	/**
	 * 采购到货生成表单详情页面
	 */
	@RequiresPermissions(value="purinvcheckmain:invCheckMain:add")
	@RequestMapping(value = "formCreatDetail")
	public String formCreatDetail(InvCheckMain invCheckMain, Model model) {
		
	   List<InvCheckDetail> invCheckDetailList=invCheckMain.getInvCheckDetailList();
		Iterator<InvCheckDetail> invCheckDetailIt = invCheckDetailList.iterator();  
         while(invCheckDetailIt.hasNext()){
        	 InvCheckDetail invCheck=invCheckDetailIt.next();
        	 
        	 InvCheckRelations conRes=new InvCheckRelations();
   		     conRes.setAfterNum(invCheckMain.getBillnum());
   		     conRes.setAfterId(invCheck.getSerialnum());
   			 List<InvCheckRelations> invCheckRelationsList = invCheckRelationsMapper.findList(conRes);
   			 if(invCheckRelationsList.size()>0) conRes=invCheckRelationsList.get(0);
         }
         invCheckMain.setInvCheckDetailList(invCheckDetailList);
		model.addAttribute("invCheckMain", invCheckMain);
		if(StringUtils.isBlank(invCheckMain.getId())){//如果ID是空为添加
			model.addAttribute("isAdd", true);
		}
		return "qualitymanage/purinvcheckmain/invCheckMainCreatDetailForm";
	}
	
	/**
	 * 增加采购到货生成表单页面
	 */
	@RequiresPermissions(value="purinvcheckmain:invCheckMain:add")
	@RequestMapping(value = "formCreat")
	public String formCreat(InvCheckMain invCheckMain, Model model) {
		invCheckMainService.setDefultIO(invCheckMain);
		String billn = creatBillnum();
		invCheckMain.setBillnum(billn);
		List<InvCheckMain> list = invCheckMainMapper.findList(invCheckMain);
		if(list.isEmpty()){
		model.addAttribute("invCheckMain", invCheckMain);
		if(StringUtils.isBlank(invCheckMain.getId())){//如果ID是空为添加
			model.addAttribute("isAdd", true);
		}
		return "qualitymanage/purinvcheckmain/invCheckMainCreatForm";
		}
		else 
			return "redirect:"+Global.getAdminPath()+"/purinvcheckmain/invCheckMain/formCreat?repage";
	}
	
	

	
	/**
	 * 查看采购到货表单页面
	 */
	@RequiresPermissions(value={"purinvcheckmain:invCheckMain:view","purinvcheckmain:invCheckMain:edit"})
	@RequestMapping(value = "viewform")
	public String viewform(InvCheckMain invCheckMain, Model model) {
		model.addAttribute("invCheckMain", invCheckMain);
		if(StringUtils.isBlank(invCheckMain.getId())){//如果ID是空为添加
			model.addAttribute("isAdd", true);
		}
		return "qualitymanage/purinvcheckmain/invCheckMainDetail";
	}
	
	/**
	 * 查看采购到货入库表单页面
	 */
	@RequiresPermissions(value={"purinvcheckmain:invCheckMain:view","purinvcheckmain:invCheckMain:edit"})
	@RequestMapping(value = "Warehousingform")
	public String Warehousingform(InvCheckMain invCheckMain, Model model) {
		invCheckMainService.toChinese(invCheckMain);
		model.addAttribute("invCheckMain", invCheckMain);
		if(StringUtils.isBlank(invCheckMain.getId())){//如果ID是空为添加
			model.addAttribute("isAdd", true);
		}
		return "qualitymanage/purinvcheckmain/invCheckMainWarehousingForm";
	}
	
	/**
	 * 查看采购到货退货表单页面
	 */
	@RequiresPermissions(value={"purinvcheckmain:invCheckMain:view","purinvcheckmain:invCheckMain:edit"})
	@RequestMapping(value = "Backform")
	public String Backform(InvCheckMain invCheckMain, Model model) {
		invCheckMainService.toChinese(invCheckMain);
		model.addAttribute("invCheckMain", invCheckMain);
		if(StringUtils.isBlank(invCheckMain.getId())){//如果ID是空为添加
			model.addAttribute("isAdd", true);
		}
		return "qualitymanage/purinvcheckmain/invCheckMainWarehousingBackForm";
	}
	/**
	 * 查看采购到货查询详情表单页面
	 */
	@RequiresPermissions(value={"purinvcheckmain:invCheckMain:view","purinvcheckmain:invCheckMain:edit"})
	@RequestMapping(value = "Queryform")
	public String Queryform(InvCheckMain invCheckMain, Model model) {
		invCheckMainService.toChinese(invCheckMain);
		model.addAttribute("invCheckMain", invCheckMain);
		if(StringUtils.isBlank(invCheckMain.getId())){//如果ID是空为添加
			model.addAttribute("isAdd", true);
		}
		return "qualitymanage/purinvcheckmain/invCheckMainQueryForm2";
	}
	/**
	 * 提交采购到货
	 */
	@RequiresPermissions(value={"purinvcheckmain:invCheckMain:add","purinvcheckmain:invCheckMain:edit"},logical=Logical.OR)
	@RequestMapping(value = "changeStateSave")
	public String changeStateSave(InvCheckMain invCheckMain, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, invCheckMain)){
			return form(invCheckMain, model);
		}
		//录入和生成提交之后直接转变为审核通过状态，生成二维码
		invCheckMain.setBillStateFlag("E");
		//保存登陆人所在部门编码
		invCheckMain.setUserDeptCode(UserUtils.getUserOfficeCode());
		invCheckMainService.toFlag(invCheckMain);//标志位转换
		invCheckMainService.save(invCheckMain);//保存
		addMessage(redirectAttributes, "提交采购到货成功");
		if("M".equals(invCheckMain.getBillType())){
			return "redirect:"+Global.getAdminPath()+"/purinvcheckmain/invCheckMain/?repage";
		}else{
			return "redirect:"+Global.getAdminPath()+"/purinvcheckmain/invCheckMain/listCreat?repage";
		}
		
	}
	
	/**
	 * 保存采购到货管理
	 */
	@RequiresPermissions(value={"purinvcheckmain:invCheckMain:add","purinvcheckmain:invCheckMain:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(InvCheckMain invCheckMain, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, invCheckMain)){
			return form(invCheckMain, model);
		}
		//保存登陆人所在部门编码
		invCheckMain.setUserDeptCode(UserUtils.getUserOfficeCode());
		invCheckMainService.toFlag(invCheckMain);//标志位转换
		invCheckMainService.save(invCheckMain);//保存
		addMessage(redirectAttributes, "保存采购到货管理成功");
		return "redirect:"+Global.getAdminPath()+"/purinvcheckmain/invCheckMain/?repage";
	}
	
	/**
	 * 生成物料二维码页面保存供应商二维码
	 */
	@RequiresPermissions(value={"purinvcheckmain:invCheckMain:add","purinvcheckmain:invCheckMain:edit"},logical=Logical.OR)
	@RequestMapping(value = "saveQRcode")
	public String saveQRcode(InvCheckMain invCheckMain, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, invCheckMain)){
			return form(invCheckMain, model);
		}
		//保存登陆人所在部门编码
		invCheckMain.setUserDeptCode(UserUtils.getUserOfficeCode());
		invCheckMainService.toFlag(invCheckMain);//标志位转换
		invCheckMainService.save(invCheckMain);//保存
		addMessage(redirectAttributes, "保存二维码成功");
		return "redirect:"+Global.getAdminPath()+"/purinvcheckmain/invCheckMain/listWarehousing?repage";
	}
	
	/**
	 * 提交采购到货管理
	 */
	@RequiresPermissions(value={"purinvcheckmain:invCheckMain:add","purinvcheckmain:invCheckMain:edit"},logical=Logical.OR)
	@RequestMapping(value = "submit")
	public String submit(InvCheckMain invCheckMain, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, invCheckMain)){
			return form(invCheckMain, model);
		}
		//保存登陆人所在部门编码
		invCheckMain.setUserDeptCode(UserUtils.getUserOfficeCode());
		invCheckMainService.toFlag(invCheckMain);//标志位转换
		invCheckMainService.save(invCheckMain);//保存
		addMessage(redirectAttributes, "提交采购到货管理成功");
		return "redirect:"+Global.getAdminPath()+"/purinvcheckmain/invCheckMain/?repage";
	}
	
	/**
	 * 保存采购到货审核
	 */
	@RequiresPermissions(value={"purinvcheckmain:invCheckMain:add","purinvcheckmain:invCheckMain:edit"},logical=Logical.OR)
	@RequestMapping(value = "AuditSubmit")
	public String saveAudit(InvCheckMain invCheckMain, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, invCheckMain)){
			return form(invCheckMain, model);
		}
		//保存登陆人所在部门编码
//		invCheckMain.setUserDeptCode(UserUtils.getUserOfficeCode());
		invCheckMainService.toFlag(invCheckMain);//标志位转换
		invCheckMainService.save(invCheckMain);//保存
		addMessage(redirectAttributes, "提交采购到货审核成功");
		return "redirect:"+Global.getAdminPath()+"/purinvcheckmain/invCheckMain/listAudit?repage";
	}
	
	/**
	 * 保存采购到货生成
	 */
	@RequiresPermissions(value={"purinvcheckmain:invCheckMain:add","purinvcheckmain:invCheckMain:edit"},logical=Logical.OR)
	@RequestMapping(value = "saveCreat")
	public String saveCreat(InvCheckMain invCheckMain, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, invCheckMain)){
			return form(invCheckMain, model);
		}
		//保存登陆人所在部门编码
		invCheckMain.setUserDeptCode(UserUtils.getUserOfficeCode());
		invCheckMainService.toFlag(invCheckMain);//标志位转换
		invCheckMainService.save(invCheckMain);//保存
		addMessage(redirectAttributes, "保存采购到货生成单成功");
		return "redirect:"+Global.getAdminPath()+"/purinvcheckmain/invCheckMain/listCreat?repage";
	}
	
/*	*//**
	 * 保存采购到货管理
	 *//*
	@RequiresPermissions(value={"purinvcheckmain:invCheckMain:add","purinvcheckmain:invCheckMain:edit"},logical=Logical.OR)
	@RequestMapping(value = "invCheck")
	public String invCheck(InvCheckMain invCheckMain, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, invCheckMain)){
			return form(invCheckMain, model);
		}
		invCheckMainService.toFlag(invCheckMain);//标志位转换
		invCheckMainService.save(invCheckMain);//保存
		addMessage(redirectAttributes, "提交入库单成功");
		return "redirect:"+Global.getAdminPath()+"/purinvcheckmain/invCheckMain/listWarehousing?repage";
	}*/
	
	/**
	 * 删除采购到货管理
	 */
	@ResponseBody
	@RequiresPermissions("purinvcheckmain:invCheckMain:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(InvCheckMain invCheckMain, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		invCheckMainService.delete(invCheckMain);
		j.setMsg("删除采购到货管理成功");
		return j;
	}
	
	/**
	 * 批量删除采购到货管理
	 */
	@ResponseBody
	@RequiresPermissions("purinvcheckmain:invCheckMain:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			invCheckMainService.delete(invCheckMainService.get(id));
		}
		j.setMsg("删除采购到货管理成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("purinvcheckmain:invCheckMain:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(InvCheckMain invCheckMain, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "采购到货管理"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<InvCheckMain> page = invCheckMainService.findPage(new Page<InvCheckMain>(request, response, -1), invCheckMain);
    		new ExportExcel("采购到货管理", InvCheckMain.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出采购到货管理记录失败！失败信息："+e.getMessage());
		}
			return j;
    }
    
    @ResponseBody
    @RequestMapping(value = "detail")
	public InvCheckMain detail(String id) {
		return invCheckMainService.get(id);
	}
	

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("purinvcheckmain:invCheckMain:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<InvCheckMain> list = ei.getDataList(InvCheckMain.class);
			for (InvCheckMain invCheckMain : list){
				try{
					invCheckMainService.save(invCheckMain);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条采购到货管理记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条采购到货管理记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入采购到货管理失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/purinvcheckmain/invCheckMain/?repage";
    }
	
	/**
	 * 下载导入采购到货管理数据模板
	 */
	@RequiresPermissions("purinvcheckmain:invCheckMain:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "采购到货管理数据导入模板.xlsx";
    		List<InvCheckMain> list = Lists.newArrayList(); 
    		new ExportExcel("采购到货管理数据", InvCheckMain.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/purinvcheckmain/invCheckMain/?repage";
    }
	
	
	/**
	 * 获得采购到货生成表单中的合同子表数据
	 */
	@ResponseBody
	@RequestMapping(value = "selectContract")
	public Map<String, Object> select1(@RequestParam(required=false) String contractId, HttpServletRequest request, HttpServletResponse response, Model model) {
	    ContractMain contractMain = new ContractMain();
		Page<ContractDetail> page = invCheckMainService.selectContract(new Page<ContractDetail>(request, response), contractMain, contractId); 
		return getBootstrapData(page);
	}
	
	
	/**
	 * 获得采购到货单中的退货数据
	 */
	@ResponseBody
	@RequestMapping(value = "selectBack")
	public Map<String, Object> selectBack(HttpServletRequest request, HttpServletResponse response, Model model) {
		InvCheckDetail invCheckDetail = new InvCheckDetail();
		List<InvCheckMain> list = invCheckMainService.selectBack(invCheckDetail); 
		Page<InvCheckMain> page = new Page<InvCheckMain>(request, response);
		//分页
        String intPage= request.getParameter("pageNo");
        int pageNo=Integer.parseInt(intPage);
        int pageSize= page.getPageSize();
        page.setCount(list.size());
        if(pageNo==1&&pageSize==-1){
    	   page.setList(list);
    	   
        }else{
    	   List<InvCheckMain> reportL=  new ArrayList<InvCheckMain>();      
	        for(int i=(pageNo-1)*pageSize;i<list.size() && i<pageNo*pageSize;i++){
	        	reportL.add(list.get(i));
		    }
	        page.setList(reportL);
	        
        }
		return getBootstrapData(page);
	}
	/**
	 * 获得人员仓库表中属于该制单人的仓库
	 */
	@ResponseBody
	@RequestMapping(value = "waredata")
	public Map<String, Object> data(Employee employee, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<WareHouse> page = new Page<WareHouse>();
		GroupBuyer groupbuyer = new GroupBuyer();
		User user = new User();
		user = UserUtils.getUser().getCurrentUser();
		List<GroupBuyer> groupbuyerList = groupBuyerMapper.findList(groupbuyer);
		List<WareHouse> wareHouseList = new ArrayList<WareHouse>();
		for(GroupBuyer groupBuyer : groupbuyerList){
			if(user.getId().equals(groupBuyer.getUser().getId())){
				Group group = groupBuyer.getGroup();
				GroupWare groupWare = new GroupWare();
				groupWare.setGroupid(group.getGroupid());
				List<GroupWare> groupwareList = new ArrayList<GroupWare>();
				groupwareList = groupWareMapper.getGroupWare(groupWare);
				if(groupwareList!=null){
					for(GroupWare groupware : groupwareList){
						WareHouse warehouse = groupware.getWarehouse();
						wareHouseList.add(warehouse);
					}
				}
			}
		}
		if(wareHouseList != null){
			page.setList(wareHouseList);
			page.setCount(wareHouseList.size());
		}
		return getBootstrapData(page);
	}
	
	/**
	 * 根据采购员Id获得计划
	 */
	@ResponseBody
	@RequestMapping(value = "plandatabyGroupbuyer")
	public Map<String, Object> databyGroupbuyer(PurPlanMain purPlanMain, HttpServletRequest request, HttpServletResponse response, Model model) {
		String buyerId = request.getParameter("buyerId");
		Page<PurPlanMain> page = purPlanMainService.findPage(new Page<PurPlanMain>(request, response), purPlanMain); 
		PurPlanDetail purPlanDetail = new PurPlanDetail();

		List<PurPlanDetail> detaillist = purPlanDetailMapper.findList(purPlanDetail); 
		List<String> idList = new ArrayList<String>();
	    List<PurPlanMain> plnlist = page.getList();
	    List<PurPlanMain> listcontainer = new ArrayList<PurPlanMain>();
	    for(PurPlanDetail purPlanDetailforLoop:detaillist){

	    	String idbuyer = purPlanDetailMapper.getUserNo(purPlanDetailforLoop);
	    	if(purPlanDetailforLoop.getBillnu()!=null&&buyerId.equals(idbuyer)){
	    		String detailid = purPlanDetailforLoop.getBillnu();
	    		for(PurPlanMain purPlanMainforLoop:plnlist){
	    			String mainid = purPlanMainforLoop.getId();
	    			if(mainid.equals(detailid)){
//	    				listcontainer.add(purPlanMainforLoop);
	    				idList.add(purPlanMainforLoop.getId());
	    			}
	    		}
	    	}
	    }
	    //id去重
		List<String> newList = new  ArrayList<String>(); 
		Set<String> set = new  HashSet<String>(); 
        set.addAll(idList);
        newList.addAll(set);
        for(PurPlanMain purPlanMainforLoop:plnlist){
        	if(newList.contains(purPlanMainforLoop.getId())){
        		if("C".equals(purPlanMainforLoop.getBillStateFlag())){
        			listcontainer.add(purPlanMainforLoop);
        			
        		}
        	}
        }
	    page.setList(listcontainer);
	    page.setCount(listcontainer.size());
		return getBootstrapData(page);
	}
	
	/**
	 * 根据采购员Id获得合同
	 */
	@ResponseBody
	@RequestMapping(value = "databyGroupbuyer")
	public Map<String, Object> condatabyGroupbuyer(PurPlanMain purPlanMain, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<ContractMain> page = new Page<ContractMain>();
		String buyerId = request.getParameter("buyerId");
		User user = new User();
		List<User> userList = userMapper.findList(user);
		List<ContractMain> contractMainlist = new ArrayList<ContractMain>();
		ContractMain contractMain = new ContractMain();
		for(User temp : userList){
			if(temp.getNo().equals(buyerId)){
				user = temp;
				break;
			}
		}
		GroupBuyer groupBuyer = new GroupBuyer();
		List<GroupBuyer> groupbuyerlist = new ArrayList<GroupBuyer>();
		groupbuyerlist = groupBuyerMapper.findList(groupBuyer);
		for(GroupBuyer temp : groupbuyerlist){
			if(user.getId().equals(temp.getUser().getId())){
				groupBuyer = temp;
				contractMain.setGroupBuyer(groupBuyer);
				contractMain.setBillStateFlag("E");
				contractMainlist.addAll(contractMainMapper.findList(contractMain));
			}
		}
		page.setCount(contractMainlist.size());
		page.setList(contractMainlist);
		return getBootstrapData(page);
		
	}
	
	
	@SuppressWarnings({"unused" })
	@ResponseBody
	@RequestMapping(value = "appDatabyRollPlan")
	public Map<String, Object> appDatabyRollPlan(@RequestParam(required=false) String billn ,HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<ApplyDetail> page = new Page<ApplyDetail>();
		InvCheckRelations relation = new InvCheckRelations();
		List<ApplyMain> appList = new ArrayList<ApplyMain>();
		List<ApplyDetail> appDetail = new ArrayList<ApplyDetail>();
		String type = billn.substring(0,3);
		
		relation.setAfterNum(billn);
		if("pln".equals(type)){
			relation.setType("AP");
			List<InvCheckRelations> listRe = invCheckRelationsMapper.findList(relation);
			List<String> plnBillList = new ArrayList<String>();
			if(!(listRe.isEmpty())){
				Iterator<InvCheckRelations> itRelist = listRe.iterator();
				while(itRelist.hasNext()){
					InvCheckRelations invRe = itRelist.next();
					plnBillList.add(invRe.getFrontNum());
				}
				if(plnBillList != null){
					//Id去重
					List<String> newList = new  ArrayList<String>(); 
					Set<String> set = new  HashSet<String>(); 
			        set.addAll(plnBillList);
			        newList.addAll(set);
			        for(String bill : newList){
			        	List<ApplyMain> appMain = applyMainMapper.getApplyMainByBillNum(bill);
			        	for(ApplyMain temp : appMain){
			        		//使得主表带上子表
			        		temp = applyMainService.get(temp.getId());
			        		List<ApplyDetail> detaillist = temp.getApplyDetailList();
			        		Iterator<ApplyDetail> itdetail = detaillist.iterator();
			        		while(itdetail.hasNext()){
			        			ApplyDetail applyDetail = itdetail.next();
			        			applyDetail.getApplyMain().setBillNum(bill);
			        			if("E".equals(temp.getBillStateFlag())){
			        				temp.setBillStateFlag("审批通过");
			        			}else if("W".equals(temp.getBillStateFlag())){
			        				temp.setBillStateFlag("录入完毕");
			        			}
			        			applyDetail.getApplyMain().setBillStateFlag(temp.getBillStateFlag());
			        		}
			        		appDetail.addAll(detaillist);
			        	}
			        }
			        page.setList(appDetail);
				}
			}
			else{
				page = null;
			}
		}
		return getBootstrapData(page);
	}
	/**
	 * 根据物料编号从检验对象定义中获取质检信息
	 */
	@ResponseBody
	@RequestMapping(value = "qmsByItemCode")
	public String qmsByItemCode(@RequestParam(required=false) String itemCode, HttpServletRequest request, HttpServletResponse response, Model model){
		ObjectDef objDef = new ObjectDef();
		objDef.setObjCode(itemCode);
		objDef = objectDefMapper.getByObjCode(objDef);
		if(objDef != null){
			return objDef.getQmsFlag();
		}else{
			return "";
		}
	}
	/**
	 * 根据采购员Id与计划Id获得计划子表
	 */
	@ResponseBody
	@RequestMapping(value = "plandatabyAjax")
	public Map<String, Object> plandatabyAjax(@RequestParam(required=false) String planId, @RequestParam(required=false) String buyerId, PurPlanMain purPlanMain, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<PurPlanMain> page = purPlanMainService.findPage(new Page<PurPlanMain>(request, response), purPlanMain); 
		PurPlanDetail purPlanDetail = new PurPlanDetail();

		List<PurPlanDetail> detaillist = purPlanDetailMapper.findList(purPlanDetail); 

	    List<PurPlanMain> plnlist = page.getList();

	    List<PurPlanDetail> detaillistcontainer = new ArrayList<PurPlanDetail>();
	    for(PurPlanDetail purPlanDetailforLoop:detaillist){
	    	if(purPlanDetailforLoop.getBillnu()!=null && buyerId.equals(purPlanDetailMapper.getUserNo(purPlanDetailforLoop))){
	    		String detailid = purPlanDetailforLoop.getBillnu();
	    		for(PurPlanMain purPlanMainforLoop:plnlist){
	    			String mainid = purPlanMainforLoop.getId();
	    			if(mainid.equals(detailid) && mainid.equals(planId)){//找到选择的计划的子表

	    				detaillistcontainer.add(purPlanDetailforLoop);
	    			}
	    		}
	    	}
	    }
	    Page<PurPlanDetail> page1=new Page<PurPlanDetail>();
	    page1.setList(detaillistcontainer);
		return getBootstrapData(page1);
	}
	
	
	
	/**
	 * 根据仓库编码获得入库类型
	 */
	@ResponseBody
	@RequestMapping(value = "databilltype")
	public Map<String, Object> databilltype(@RequestParam(required=false) String wareID,PurPlanMain purPlanMain, HttpServletRequest request, HttpServletResponse response, Model model) {
		//String wareID = request.getParameter("wareID");
		Page<BillType> page = new Page<BillType>();
		List<BillTypeWareHouse> billwarelist = new ArrayList<BillTypeWareHouse>();
		List<BillType> billlist = new ArrayList<BillType>();
		BillType billType = new BillType();
		BillTypeWareHouse billware = new BillTypeWareHouse();
		billwarelist = billTypeWareHouseMapper.findList(billware);
		for(BillTypeWareHouse billwareforloop : billwarelist){
			if(wareID.equals(billwareforloop.getWareHouse().getWareID())){
				billType.setId(billwareforloop.getBillType().getId());
				billlist.add(billTypeMapper.get(billType));
			}
		}
		if(billlist != null){
			page.setList(billlist);
			page.setCount(billlist.size());
		}
		return getBootstrapData(page);
	}
	
	
	
	/**
	 * 根据采购员Id与计划编号获得计划子表
	 */
	@ResponseBody
	@RequestMapping(value = "showplandatabyAjax")
	public Map<String, Object> showplandatabyAjax(@RequestParam(required=false) String planId, @RequestParam(required=false) String buyerId, PurPlanMain purPlanMain, HttpServletRequest request, HttpServletResponse response, Model model) {
		purPlanMain.setBillNum(planId);
		Page<PurPlanMain> page = purPlanMainService.findPage(new Page<PurPlanMain>(request, response), purPlanMain); 
		PurPlanDetail purPlanDetail = new PurPlanDetail();

		List<PurPlanDetail> detaillist = purPlanDetailMapper.findList(purPlanDetail); 

	    List<PurPlanMain> plnlist = page.getList();

	    List<PurPlanDetail> detaillistcontainer = new ArrayList<PurPlanDetail>();
	    
	    for(PurPlanDetail purPlanDetailforLoop:detaillist){
	    	if(purPlanDetailforLoop.getBillnu()!=null && buyerId.equals(purPlanDetailMapper.getUserNo(purPlanDetailforLoop))){
	    		String detailid = purPlanDetailforLoop.getBillnu();
	    		for(PurPlanMain purPlanMainforLoop:plnlist){
	    			String mainid = purPlanMainforLoop.getId();
	    			if(mainid.equals(detailid)){//找到选择的计划的子表
	    				detaillistcontainer.add(purPlanDetailforLoop);
	    			}
	    		}
	    	}
	    }
	    Page<PurPlanDetail> page1=new Page<PurPlanDetail>();
	    page1.setList(detaillistcontainer);
		return getBootstrapData(page1);
	}
	
	@ResponseBody
	@RequestMapping(value = "dataRequest")
	public Map<String, Object> dataCon(ItemforInv item, HttpServletRequest request, HttpServletResponse response, Model model) {
		List<ItemforInv> list = itemforInvMapper.findList(item);
		Page<ItemforInv> page = new Page<ItemforInv>(request, response);
		//分页
        String intPage= request.getParameter("pageNo");
        int pageNo=Integer.parseInt(intPage);
        int pageSize= page.getPageSize();
        page.setCount(list.size());
        if(pageNo==1&&pageSize==-1){
    	   page.setList(list);
    	   
        }else{
    	   List<ItemforInv> reportL=  new ArrayList<ItemforInv>();      
	        for(int i=(pageNo-1)*pageSize;i<list.size() && i<pageNo*pageSize;i++){
	        	reportL.add(list.get(i));
		    }
	        page.setList(reportL);
	        
        }
		return getBootstrapData(page);
	}

	/**
	 *	批量找回物料名称
	 */

	@RequestMapping(value="getItemNames")
	@ResponseBody
	public String getItemNames(@RequestParam(required=false) String obj, HttpServletRequest request, HttpServletResponse response, Model model){
		String itemNames = "";
		String[] itemCodes = obj.split(",");
		InvCheckDetailCode invdCode = new InvCheckDetailCode();
		for(int i = 0; i < itemCodes.length; i ++){
			invdCode.setItemBarcode(itemCodes[i]);
			InvCheckDetailCode invdCodetemp = new InvCheckDetailCode();
			List<InvCheckDetailCode> invdCodeList = invCheckDetailCodeMapper.findList(invdCode);
			if(invdCodeList.size() != 0){
				invdCodetemp = invdCodeList.get(0);
			}else{
				invdCodetemp = null;
			}
			if(invdCodetemp != null){
				itemNames += invdCodetemp.getItemName() + ",";
			}else{
				itemNames += "未找到对应物料" + ",";
			}
		}
		return itemNames;
	}
}