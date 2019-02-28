package com.hqu.modules.inventorymanage.amountadjustment.web;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.hqu.modules.basedata.account.service.AccountService;
import com.hqu.modules.basedata.period.entity.Period;
import com.hqu.modules.basedata.period.service.PeriodService;
import com.hqu.modules.inventorymanage.amountadjustment.service.AmountAdjustmentService;
import com.hqu.modules.inventorymanage.amountadjustment.service.BillMainAmountService;
import com.hqu.modules.inventorymanage.billmain.entity.BillMain;
import com.hqu.modules.inventorymanage.billmain.service.BillMainService;
import com.hqu.modules.inventorymanage.billtype.entity.BillType;
import com.hqu.modules.inventorymanage.billtype.service.BillTypeService;
import com.hqu.modules.inventorymanage.employee.entity.Employee;
import com.hqu.modules.inventorymanage.invaccount.entity.InvAccount;
import com.hqu.modules.inventorymanage.invaccount.service.InvAccountService;
import com.hqu.modules.inventorymanage.outsourcingoutput.mapper.BillMainOutsourcingMapper;
import com.hqu.modules.inventorymanage.outsourcingoutput.service.BillMainOutsourcingService;
import com.hqu.modules.inventorymanage.outsourcingoutput.service.OutsourcingOutputService;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.excel.ExportExcel;
import com.jeeplus.common.utils.excel.ImportExcel;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.utils.UserUtils;

@Controller
@RequestMapping(value = "${adminPath}/amountadjustment/amountAdjustment")
public class AmountAdjustmentController extends BaseController{
	@Autowired
	private BillMainAmountService billMainOutsourcingService;
	@Autowired
	private AmountAdjustmentService outsourcingOutputService;
	@Autowired
	private BillTypeService billTypeService;
	@Autowired
	private InvAccountService invAccountService;
	@Autowired
	private PeriodService periodService;
	@ModelAttribute
	public BillMain get(@RequestParam(required=false) String id) {
		BillMain entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = billMainOutsourcingService.get(id);
		}
		if (entity == null){
			entity = new BillMain();
		}
		return entity;
	}
	
	/**
	 * 单据列表页面
	 */
	@RequiresPermissions("billmain:billMain:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		System.out.println("list---");
		return "inventorymanage/amountadjust/billMainList";
	}
	@RequiresPermissions("billmain:billMain:list")
	@RequestMapping(value = {"searchList"})
	public String searchList() {
		return "inventorymanage/amountadjust/billMainListSearch";
	}
	@RequiresPermissions("billmain:billMain:list")
	@RequestMapping(value = {"auditList"})
	public String auditList() {
		return "inventorymanage/amountadjust/billMainListAudit";
	}
		/**
	 * 单据列表数据
	 */
	@ResponseBody
	@RequiresPermissions("billmain:billMain:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(BillMain billMain, HttpServletRequest request, HttpServletResponse response, Model model) {
		System.out.println("data---");
		if("0".equals(request.getParameter("searchWay"))) {
			billMain.setUserId("");
		}else if("1".equals(request.getParameter("searchWay"))) {
			billMain.setUserId(UserUtils.getUser().getId());;
		}
		List<Period> plist=null;
		if(!StringUtils.isEmpty(billMain.getPeriod().getPeriodId())) {
			plist = periodService.findList(billMain.getPeriod());
		}
		billMain.setBillFlag(request.getParameter("showWay"));
		Page<BillMain> page = billMainOutsourcingService.findPage(new Page<BillMain>(request, response), billMain); 
		if(!StringUtils.isEmpty(billMain.getItemCode())) {
			List<BillMain> blist = outsourcingOutputService.findBillMainByItemCode(billMain.getItemCode());
			List<BillMain> btlist = new ArrayList<>();
			if(blist!=null) {
				for(int i=0;i<blist.size();i++) {
					if(page.getList().contains(blist.get(i))) {
						btlist.add(blist.get(i));
					}
				}
			}
			page.setList(btlist);
		}
		
		if(plist!=null) {
			int j;
			List<BillMain> btlist = new ArrayList<>();
			for(int i=0;i<page.getList().size();i++) {
				for(j=0;j<plist.size();j++) {
					if(page.getList().get(i).getPeriod().getId().equals(plist.get(j).getId()))
						break;
				}
				if(j<plist.size()) {
					btlist.add(page.getList().get(i));
				}
			}
			page.setList(btlist);
		}
		//logger.debug(page.getList().get(0).getWareEmp().getUser().getNo()+"-->wareEmp");
		return getBootstrapData(page);
	}
	
	/**
	 * 查看，增加，编辑单据表单页面
	 */
	@RequiresPermissions(value={"billmain:billMain:view","billmain:billMain:add","billmain:billMain:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(BillMain billMain, Model model) {
		System.out.println(billMain.getBillDetailList().size()+"+++++++");
		if(StringUtils.isBlank(billMain.getId())){//如果ID是空为添加
			Date date = new Date();
			SimpleDateFormat sdf =   new SimpleDateFormat( "yyyyMMdd" );
            String newBillNum = billMainOutsourcingService.getMaxIdByTypeAndDate("tzd"+sdf.format(date));
            if(StringUtils.isEmpty(newBillNum)){
                newBillNum = "0000";
            } else {
                newBillNum = String.format("%04d", Integer.parseInt(newBillNum.substring(11,15))+1);
            }
			logger.debug("billNum:"+newBillNum);
            billMain.setBillNum("tzd" + sdf.format(date) + newBillNum);
			billMain.setBillDate(date);
			Map<String,Object> map = new HashMap<>();
			SimpleDateFormat sdf1=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			map.put("today", sdf1.format(date));
			Period period=outsourcingOutputService.findPeriodByTime(map);
			billMain.setPeriod(period);
			User user=UserUtils.getUser();
			Employee emp=new Employee();
			emp.setUser(user);
			billMain.setWareEmp(emp);
			billMain.setWareEmpname(user.getName());
			billMain.setDept(user.getOffice());
			billMain.setDeptName(user.getOffice().getName());
			model.addAttribute("isAdd", true);
		}
		model.addAttribute("billMain", billMain);
		return "inventorymanage/amountadjust/billMainForm";
	}
	@RequiresPermissions(value={"billmain:billMain:view","billmain:billMain:add","billmain:billMain:edit"},logical=Logical.OR)
	@RequestMapping(value = "formSearch")
	public String formSearch(BillMain billMain, Model model) {
		model.addAttribute("billMain", billMain);
		return "inventorymanage/amountadjust/billMainFormSearch";
	}
	@RequiresPermissions(value={"billmain:billMain:view","billmain:billMain:add","billmain:billMain:edit"},logical=Logical.OR)
	@RequestMapping(value = "formAudit")
	public String formAudit(BillMain billMain, Model model) {
		System.out.println(billMain.getBillDetailList().size()+"+++++++");
		model.addAttribute("billMain", billMain);
		return "inventorymanage/amountadjust/billMainFormAudit";
	}
	@RequestMapping(value="formAuditdo")
	public String formAuditdo(BillMain billMain,Model model,RedirectAttributes redirectAttributes) throws Exception{
		
        Map<String,Object> resultMap = invAccountService.post(billMain);
        Boolean judge=(Boolean) resultMap.get("result");

        if(judge) {
			billMain.setBillFlag("T");
			billMainOutsourcingService.save(billMain);
			addMessage(redirectAttributes, "过账成功");
		}else {
			addMessage(redirectAttributes, "过账失败: " + resultMap.get("msg") );
		}
		return "redirect:"+Global.getAdminPath()+"/amountadjustment/amountAdjustment/auditList";
	}
//修改操作 数量的控制以及是否让选物料还是直接删除
	/**
	 * 保存单据
	 */
	@Transactional
	@RequiresPermissions(value={"billmain:billMain:add","billmain:billMain:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(BillMain billMain, Model model, RedirectAttributes redirectAttributes) throws Exception{
		billMain.setOrderCode(1);
//		BillType billtType = new BillType();
//		billtType.setIoType("WO01");
//		List<BillType> blist = billTypeService.findList(billtType);
//		billMain.setIo(blist.get(0));
		billMain.setBillEmp(UserUtils.getUser());
		Employee emp = new Employee();
		emp.setUser(UserUtils.getUser());
		emp.setId(UserUtils.getUser().getId());
		billMain.setWareEmp(emp);
		
		//billMain.setIoFlag("O");
		if (!beanValidator(model, billMain)){
			return form(billMain, model);
		}
		
		//新增或编辑表单保存
		billMainOutsourcingService.save(billMain);//保存
		addMessage(redirectAttributes, "保存单据成功");
		return "redirect:"+Global.getAdminPath()+"/amountadjustment/amountAdjustment";
	}
	
	/**
	 * 删除单据
	 */
	@ResponseBody
	@RequiresPermissions("billmain:billMain:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(BillMain billMain, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		billMainOutsourcingService.delete(billMain);
		j.setMsg("删除单据成功");
		return j;
	}
	
	/**
	 * 批量删除单据
	 */
	@ResponseBody
	@RequiresPermissions("billmain:billMain:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			billMainOutsourcingService.delete(billMainOutsourcingService.get(id));
		}
		j.setMsg("删除单据成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("billmain:billMain:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(BillMain billMain, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "单据"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<BillMain> page = billMainOutsourcingService.findPage(new Page<BillMain>(request, response, -1), billMain);
    		new ExportExcel("单据", BillMain.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出单据记录失败！失败信息："+e.getMessage());
		}
			return j;
    }
    
    @ResponseBody
    @RequestMapping(value = "detail")
	public BillMain detail(String id) {
		return billMainOutsourcingService.get(id);
	}
	

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("billmain:billMain:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<BillMain> list = ei.getDataList(BillMain.class);
			for (BillMain billMain : list){
				try{
					billMainOutsourcingService.save(billMain);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条单据记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条单据记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入单据失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/billmain/billMain/?repage";
    }
	
	/**
	 * 下载导入单据数据模板
	 */
	@RequiresPermissions("billmain:billMain:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "单据数据导入模板.xlsx";
    		List<BillMain> list = Lists.newArrayList(); 
    		new ExportExcel("单据数据", BillMain.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/billmain/billMain/?repage";
    }
}
