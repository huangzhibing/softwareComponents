/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.inventorymanage.finishedproduct.outboundinput.web;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import com.hqu.modules.inventorymanage.billdetailcodes.service.BillDetailCodesService;
import com.hqu.modules.inventorymanage.billmain.entity.BillDetailCode;
import com.hqu.modules.inventorymanage.billmain.mapper.BillMainMapper;
import com.hqu.modules.salemanage.salcontract.entity.Contract;
import com.hqu.modules.salemanage.salcontract.service.ContractService;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
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

import com.esotericsoftware.minlog.Log;
import com.github.abel533.echarts.Data;
import com.google.common.collect.Lists;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.modules.sys.entity.Office;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.service.OfficeService;
import com.jeeplus.modules.sys.utils.UserUtils;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.excel.ExportExcel;
import com.jeeplus.common.utils.excel.ImportExcel;
import com.hqu.modules.basedata.item.entity.Item;
import com.hqu.modules.basedata.item.service.ItemService;
import com.hqu.modules.basedata.period.entity.Period;
import com.hqu.modules.basedata.period.service.PeriodService;
import com.hqu.modules.basedata.product.service.ProductService;
import com.hqu.modules.inventorymanage.billmain.entity.BillDetail;
import com.hqu.modules.inventorymanage.billmain.entity.BillMain;
import com.hqu.modules.inventorymanage.billmain.service.BillMainService;
import com.hqu.modules.inventorymanage.billtype.entity.BillType;
import com.hqu.modules.inventorymanage.billtype.service.BillTypeService;
import com.hqu.modules.inventorymanage.employee.entity.Employee;
import com.hqu.modules.inventorymanage.employee.entity.EmployeeWareHouse;
import com.hqu.modules.inventorymanage.employee.service.EmployeeService;
import com.hqu.modules.inventorymanage.employee.service.EmployeeWareHouseService;
import com.hqu.modules.inventorymanage.invaccount.entity.InvAccount;
import com.hqu.modules.inventorymanage.invaccount.mapper.InvAccountMapper;
import com.hqu.modules.inventorymanage.invaccount.service.InvAccountService;
import com.hqu.modules.inventorymanage.outsourcingoutput.mapper.OutsourcingOutputMapper;
import com.hqu.modules.inventorymanage.outsourcingoutput.service.OutsourcingOutputService;
import com.hqu.modules.inventorymanage.warehouse.entity.WareHouse;
import com.hqu.modules.inventorymanage.warehouse.service.WareHouseService;
import com.hqu.modules.salemanage.pickbill.entity.PickBill;
import com.hqu.modules.salemanage.pickbill.service.PickBillService;

/**
 * 出入库单据Controller
 * @author M1ngz
 * @version 2018-04-16
 */
@Controller			
@RequestMapping(value = "${adminPath}/outboundinput/outboundInput")
public class OutboundinputController extends BaseController {

	@Autowired
	private BillMainService billMainService;
	@Autowired
	private OfficeService officeService;
	@Autowired
	private BillTypeService billTypeService;
	@Autowired
	private OutsourcingOutputMapper outsourcingOutputMapper;
	@Autowired
	private EmployeeService employeeService;
	@Autowired
	private InvAccountService invAccountService;
	@Autowired
	private WareHouseService wareHouseService;
	@Autowired
	private OutsourcingOutputService outsourcingOutputService;
	@Autowired
	private PeriodService periodService;
	@Autowired
	private EmployeeWareHouseService employeeWareHouseService;
	@Autowired
	private ProductService productService;
	@Autowired
	private ItemService itemService;
	@Autowired
	private PickBillService pickBillService;
	@Autowired
	private ContractService contractService;
	@Autowired
	private BillDetailCodesService billDetailCodesService;
	@Autowired
	private BillMainMapper billMainMapper;
	@ModelAttribute
	public BillMain get(@RequestParam(required=false) String id) {
		BillMain entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = billMainService.get(id);
		}
		if (entity == null){
			entity = new BillMain();
		}
		return entity;
	}
	/**
	 * 反过账
	 */
	@Transactional
	@RequestMapping(value = "formNotAuditdo")
	public String formNotAuditdo(BillMain billMain, Model model, RedirectAttributes redirectAttributes) throws Exception {

		String curperiod = wareHouseService.get(billMain.getWare().getId()).getCurrPeriod();
		logger.debug("gid:" + billMain.getWare());
		//判断核算期是否一致
		if (!curperiod.equals(billMain.getPeriod().getPeriodId())) {
			addMessage(redirectAttributes, "反过账失败，当前核算期与仓库核算期不一致！");
			return "redirect:" + Global.getAdminPath() + "/outboundinput/outboundInput?type=fan";
		}
		try {
			List<BillDetail> detaillist = billMain.getBillDetailList();
			for (int i = 0; i < detaillist.size(); i++) {
				detaillist.get(i).setItemQty(detaillist.get(i).getItemQty() * (-1));
			}
			billMain.setBillDetailList(detaillist);
			Map<String, Object> resultMap = invAccountService.post(billMain);
			Boolean judge = (Boolean) resultMap.get("result");

			if (judge) {
				billMain.setBillFlag("N");
				//billMainService.save(billMain);
				List<BillDetail> dlist = billMain.getBillDetailList();
				for (int i = 0; i < dlist.size(); i++) {
					dlist.get(i).setItemQty(detaillist.get(i).getItemQty() * (-1));
				}
				billMain.setBillDetailList(dlist);
				billMainService.save(billMain);
				addMessage(redirectAttributes, "反过账成功");
			} else {
				addMessage(redirectAttributes, "反过账失败: " + resultMap.get("msg"));
			}
			return "redirect:" + Global.getAdminPath() + "/outboundinput/outboundInput?type=fan";
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			addMessage(redirectAttributes, "反过账失败");
			return "redirect:" + Global.getAdminPath() + "/outboundinput/outboundInput?type=fan";
		}
	}
	/**
	 * 打印
	 */
	@RequiresPermissions(value={"billmain:billMain:view","billmain:billMain:add","billmain:billMain:edit"},logical=Logical.OR)
	@RequestMapping(value = "print")
	public String print(BillMain billMain, Model model) {
		model.addAttribute("billMain", billMain);
		try {
			PickBill pickBill = pickBillService.get(billMain.getCorBillNum());
			model.addAttribute("itemContents", contractService.getByCode(pickBill.getContract().getContractCode()).getPaperCtrCode());
			model.addAttribute("pickBill", pickBill);
		}catch (Exception e){
			e.printStackTrace();
		}
		return "inventorymanage/finishedproduct/outboundquery/print";
	}
	/*
	 *获取库存帐信息
	 */
	@ResponseBody
	@RequestMapping(value = {"getInv"})
	public InvAccount getInv(InvAccount inv) {
		List<InvAccount> invs=invAccountService.findList(inv);
		if(invs.size()==1) {
			return invAccountService.findList(inv).get(0);
		}else{
			return null;
		}
	}
	/**
	 * 获取仓库
	 */
	@ResponseBody
	@RequestMapping(value = {"getWare"})
	public WareHouse getWare(String wareName) {
		WareHouse wareHouse=new WareHouse();
		wareHouse.setWareName(wareName);
		if(wareHouseService.findList(wareHouse)!=null) {
			return wareHouseService.findList(wareHouse).get(0);
		}else return null;
	}

	/*
	 * 处理tag
	 */
	@RequiresPermissions("user")
	@RequestMapping(value = "gridselect")
	public String gridselect(String url, String fieldLabels, String fieldKeys, String searchLabels, String searchKeys, String isMultiSelected, HttpServletRequest request, HttpServletResponse response, Model model) {
		try {
//			fieldLabels = URLDecoder.decode(fieldLabels, "UTF-8");
//			fieldKeys = URLDecoder.decode(fieldKeys, "UTF-8");
//			searchLabels = URLDecoder.decode(searchLabels, "UTF-8");
//			searchKeys = URLDecoder.decode(searchKeys, "UTF-8");
			url=URLDecoder.decode(url,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		model.addAttribute("isMultiSelected", isMultiSelected);
		model.addAttribute("fieldLabels", fieldLabels.split("\\|"));
		model.addAttribute("fieldKeys", fieldKeys.split("\\|"));
		model.addAttribute("url", url);
		model.addAttribute("searchLabels", searchLabels.split("\\|"));
		model.addAttribute("searchKeys", searchKeys.split("\\|"));
		return "modules/common/gridselect";
	}
	/**
	 * 获取库管员
	 */
	@ResponseBody
	@RequestMapping(value = {"getEmp"})
	public Employee getEmp(String wareID) {
		EmployeeWareHouse employeeWareHouse=new EmployeeWareHouse();
		WareHouse wareHouse=new WareHouse();
		wareHouse.setWareID(wareID);
		employeeWareHouse.setWareHouse(wareHouse);
		String no=employeeWareHouseService.findList(employeeWareHouse).get(0).getEmp().getId();
		Employee employee=new Employee();
		employee.setUser(new User());
		employee.getUser().setNo(no);
		employee=employeeService.findList(employee).get(0);
		return employee;
	}
	/**
	 * 单据列表页面
	 */
	@RequiresPermissions("billmain:billMain:list")
	@RequestMapping(value = {"list", ""})
	public String list(String type,Model model) {
		return "inventorymanage/finishedproduct/outbound"+type.toLowerCase()+"/outbound"+type+"List";
	}
	
	/**
	 * 单据列表数据
	 *  
	 */
	@ResponseBody
	@RequiresPermissions("billmain:billMain:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(BillMain billMain,String type, HttpServletRequest request, HttpServletResponse response, Model model) {
		BillType billType = new BillType();
		billType.setIoType("CO01");
		List<BillType> billTypes = billTypeService.findList(billType);
		billMain.setIo(billTypes.get(0));
        if("Post".equals(type)||"Input".equals(type)){
        	billMain.setBillFlag("N");
		}
		Page<BillMain> page = billMainService.findPage(new Page<BillMain>(request, response), billMain);
		return getBootstrapData(page);
	}

	/**
	 * 放行单中加入外购件出库单据
	 * @hzb
	 */
	@ResponseBody
	@RequiresPermissions("billmain:billMain:list")
	@RequestMapping(value = "data2")
	public Map<String, Object> data2(BillMain billMain,String type, HttpServletRequest request, HttpServletResponse response, Model model) {
		BillType billType = new BillType();
		billType.setIoType("12");
		List<BillType> billTypes = billTypeService.findList(billType);
		List<BillMain> billMainList = new ArrayList<>();
		for(int i=0;i<billTypes.size();i++){
			billMain.setIo(billTypes.get(i));
			billMain.setBillFlag("T");
			List<BillMain> billMains = billMainService.findList(billMain);
			for(int j=0;j<billMains.size();j++){
				billMainList.add(billMains.get(j));
			}
		}
		//分页
		Page<BillMain> page = new Page<BillMain>(request, response);
		Integer pageNo = page.getPageNo();
		Integer pageSize = page.getPageSize();
		List<BillMain> pageList = new ArrayList<BillMain>();
		for(int i=(pageNo-1)*pageSize;i<billMainList.size()&&i<pageNo*pageSize;i++){
			pageList.add(billMainList.get(i));
		}
		page.setCount(billMainList.size());
		page.setList(pageList);
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑单据表单页面
	 */
	@RequiresPermissions(value={"billmain:billMain:view","billmain:billMain:add","billmain:billMain:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(@RequestParam(required=false) String type,BillMain billMain, Model model) {
		model.addAttribute("billMain", billMain);
		List<BillDetailCode> billDetailCodes=billMainService.findCodeList(billMain.getBillNum());
		billMain.setBillDetailCodeList(billDetailCodes);
		//如果ID是空为添加
		if(StringUtils.isBlank(billMain.getId())){
			User u = UserUtils.getUser();
            Office o = officeService.get(u.getOffice().getId());
            //添加经办人
            billMain.setBillPerson(u);
            //添加单据编号
            Date date = new Date();
            SimpleDateFormat sdf =   new SimpleDateFormat( "yyyyMMdd" );
            String newBillNum = billMainService.getMaxIdByTypeAndDate("CPO"+sdf.format(date));
            if(StringUtils.isEmpty(newBillNum)){
                newBillNum = "0000";
            } else {
                newBillNum = String.format("%04d", Integer.parseInt(newBillNum.substring(11,15))+1);
            }
			logger.debug("billNum:"+newBillNum);
            billMain.setBillNum("CPO" + sdf.format(date) + newBillNum);
            //添加出库日期
            billMain.setBillDate(date);
            //添加核算期
            billMain.setPeriod(billMainService.findPeriodByTime(date));
            //添加部门编号
            billMain.setDept(o);
            //添加部门名称
            billMain.setDeptName(o.getName());
            //设置单据状态
            billMain.setBillFlag("A");
			model.addAttribute("isAdd", true);
		}
		return "inventorymanage/finishedproduct/outbound"+type.toLowerCase()+"/outbound"+type+"Form";
	}
	/*
	 * 过账
	 */
	@RequiresPermissions(value={"billmain:billMain:add","billmain:billMain:edit"},logical=Logical.OR)
	@RequestMapping(value = "post")
	public String post(BillMain billMain, Model model,String type, RedirectAttributes redirectAttributes,String page) throws Exception{
		 //设置出入库类型
		BillType billType = new BillType();
		billType.setIoType("CO01");
		List<BillType> billTypes = billTypeService.findList(billType);
		billMain.setIo(billTypes.get(0));
		String curperiod=wareHouseService.findList(billMain.getWare()).get(0).getCurrPeriod();
		logger.debug("gid:"+billMain.getWare());
		if(!curperiod.equals(billMain.getPeriod().getPeriodId())) {
			addMessage(redirectAttributes, "过账失败，当前核算期与仓库核算期不一致！");
			return "redirect:"+Global.getAdminPath()+"/outboundinput/outboundInput/?repage=&type=Post";
		}
		Map<String,Object> resultMap = invAccountService.post(billMain);
		if((boolean)resultMap.get("result")) {
			billMain.setBillFlag("T");
			billMainService.save(billMain);
			for(final BillDetailCode billDetailCode:billMain.getBillDetailCodeList()){
				outsourcingOutputService.updateOflagByBarcode(new HashMap<String, Object>(){
					{
						put("Oflag","1");
						put("itemBarcode",billDetailCode.getItemBarcode());
					}
				});
			}
			addMessage(redirectAttributes, "过账成功");
		}else {
			addMessage(redirectAttributes, "过账失败: "+resultMap.get("msg"));
		}
		return "redirect:"+Global.getAdminPath()+"/outboundinput/outboundInput/?repage=&type=Post";
	}
	/**
	 * 保存单据
	 */
	@Transactional(rollbackFor = Exception.class)
	@RequiresPermissions(value={"billmain:billMain:add","billmain:billMain:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(BillMain billMain, Model model,String type, RedirectAttributes redirectAttributes,String page,String saleId) throws Exception{
		if(billMain.getBillDetailList().size()==0){
			addMessage(model,"录入失败,子表不能为空!");
			return form("Input",billMain,model);
		}
		for(BillDetail detail : billMain.getBillDetailList()){
			detail.getItem().setId(detail.getItem().getCode());
			detail.getLoc().setId(detail.getLoc().getLocId());
			detail.getBin().setId(detail.getBin().getBinId());
		}
		PickBill pickBill=pickBillService.get(saleId);
		try {
			//如果id不为空则是修改
			if(StringUtils.isNotBlank(billMain.getId())) {
				for(BillDetail billDetail:billMain.getBillDetailList()) {
					//删除子表
					if(billDetail.getDelFlag().equals("1")) {
						billDetail.setItemQty(0.0);
					}
				}
				BillMain dbbillMain=billMainService.get(billMain.getId());
				restoreAcount(dbbillMain);
			}
			//检查
			if(checkAcount(billMain)==0) {
				if(StringUtils.isNotBlank(billMain.getId())) {
					BillMain dbbillMain=billMainService.get(billMain.getId());
					decreaseAcount(dbbillMain);
				}
				addMessage(model, "录入失败，请选择库存账中的物料！");
				return form("Input",billMain,model);
			}else if(checkAcount(billMain)==1) {
				if(StringUtils.isNotBlank(billMain.getId())) {
					BillMain dbbillMain=billMainService.get(billMain.getId());
					decreaseAcount(dbbillMain);
				}
				addMessage(model, "录入失败，物料数量超过可用量！");
				return form("Input",billMain,model);
			}
		}catch(Exception e) {
			e.printStackTrace();
			if(StringUtils.isNotBlank(billMain.getId())) {
				BillMain dbbillMain=billMainService.get(billMain.getId());
				decreaseAcount(dbbillMain);
			}
			addMessage(model, "录入失败，请选择库存账中的物料！");
			return form("Input",billMain,model);
		}
		//减少可用量
		decreaseAcount(billMain);
		//设置过账标志
		billMain.setBillFlag("N");
		//新增或编辑表单保存
		User u = UserUtils.getUser();
        //设置出入库类型
		BillType billType = new BillType();
		billType.setIoType("CO01");
		List<BillType> billTypes = billTypeService.findList(billType);
		billMain.setIo(billTypes.get(0));
        //设置接口序号
        billMain.setOrderCode(341);
        //设置制单人
        billMain.setBillEmp(u);
        if (!beanValidator(model, billMain)){
			return form(type,billMain, model);
		}
        //保存
		billMain.setCorBillNum(pickBill.getPbillCode());
       	billMainService.save(billMain);
		for (BillDetailCode billDetailCode : billMain.getBillDetailCodeList()){
			if (billDetailCode.getId() == null){
				continue;
			}
			billMainService.saveBillCode(billMain,billDetailCode);
		}
		//修改发货单状态
		if(StringUtils.isNotBlank(saleId)) {
			pickBill.setPbillStat("O");
			pickBillService.save(pickBill);
		}
		addMessage(redirectAttributes, "保存单据成功");
		return "redirect:"+Global.getAdminPath()+"/outboundinput/outboundInput/?repage=&type="+type;
	}
	
	/**
	 * 删除单据
	 */
	@ResponseBody
	@RequiresPermissions("billmain:billMain:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(BillMain billMain, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		restoreAcount(billMain);
		billMainService.delete(billMain);
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
		BillMain billMain=null;
		String idArray[] =ids.split(",");
		try {
			for (String id : idArray) {
				billMain = billMainService.get(id);
				restoreAcount(billMain);
				billMainService.delete(billMainService.get(id));
			}
			j.setMsg("删除单据成功");
		}catch (Exception e){
			j.setMsg("删除单据失败");
		}
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
            Page<BillMain> page = billMainService.findPage(new Page<BillMain>(request, response, -1), billMain);
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
		return billMainService.get(id);
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
					billMainService.save(billMain);
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
	/**
	 * 检查库存帐剩余物料
	 * 0为物料不存在，1为物料超过可用量，2为通过检查
	 */
	public int checkAcount(BillMain billMain) {
		InvAccount invAccount=new InvAccount();
		InvAccount dbInvAccount=new InvAccount();
		for(BillDetail billDetail:billMain.getBillDetailList()) {
			invAccount.setWare(billMain.getWare());
			invAccount.setItem(billDetail.getItem());
			invAccount.setYear(billMain.getPeriod().getPeriodId().substring(0,4));
			invAccount.setPeriod(billMain.getPeriod().getPeriodId().substring(4,6));
			if(StringUtils.isNotBlank(billDetail.getBin().getBinId())){
				invAccount.setBin(billDetail.getBin());
				if(StringUtils.isNotBlank(billDetail.getLoc().getLocId())){
					invAccount.setLoc(billDetail.getLoc());
				}
			}
			dbInvAccount=invAccountService.getByInvAccount(invAccount);
			if(dbInvAccount!=null){
				if(wareHouseService.get(billMain.getWare().getId()).getSubFlag().equals("1")) {
					return 2;
				}			
				if(dbInvAccount.getRealQty()==null) return 1;
				if(billDetail.getItemQty()>dbInvAccount.getRealQty()) {
					return 1;
				}
			}else return 0;
		}
		return 2;
	}
	/**
	 * 减去库存账中的可用量
	 */
	public boolean decreaseAcount(BillMain billMain) {
		InvAccount invAccount=new InvAccount();
		InvAccount dbInvAccount=new InvAccount();
		double num;
		for(BillDetail billDetail:billMain.getBillDetailList()) {
			invAccount.setWare(billMain.getWare());
			invAccount.setItem(billDetail.getItem());
			invAccount.setYear(billMain.getPeriod().getPeriodId().substring(0,4));
			invAccount.setPeriod(billMain.getPeriod().getPeriodId().substring(4,6));
			if(StringUtils.isNotBlank(billDetail.getBin().getId())){
				invAccount.setBin(billDetail.getBin());
				if(StringUtils.isNotBlank(billDetail.getLoc().getId())){
					invAccount.setLoc(billDetail.getLoc());
				}
			}
			dbInvAccount=invAccountService.getByInvAccount(invAccount);
			num=dbInvAccount.getRealQty()-billDetail.getItemQty();
			dbInvAccount.setRealQty(num);
			invAccountService.save(dbInvAccount);
		}
		return true;
	}
	/**
	 * 还原库存帐的可用量
	 */
	public boolean restoreAcount(BillMain billMain) {
		InvAccount invAccount=new InvAccount();
		InvAccount dbInvAccount=new InvAccount();
		double num;
		for(BillDetail billDetail:billMain.getBillDetailList()) {
			if(billDetail.getItem()!=null) {
				billDetail.getItem().setCode(billDetail.getItem().getId());
			}
			invAccount.setWare(billMain.getWare());
			invAccount.setItem(billDetail.getItem());
			invAccount.setYear(billMain.getPeriod().getPeriodId().substring(0,4));
			invAccount.setPeriod(billMain.getPeriod().getPeriodId().substring(4,6));
			if(StringUtils.isNotBlank(billDetail.getBin().getId())){
				billDetail.getBin().setBinId(billDetail.getBin().getId());
				invAccount.setBin(billDetail.getBin());
				if(StringUtils.isNotBlank(billDetail.getLoc().getId())){
					billDetail.getLoc().setLocId(billDetail.getLoc().getId());
					invAccount.setLoc(billDetail.getLoc());
				}
			}
			dbInvAccount=invAccountService.getByInvAccount(invAccount);
			num=dbInvAccount.getRealQty()+billDetail.getItemQty();
			dbInvAccount.setRealQty(num);
			invAccountService.save(dbInvAccount);
		}
		return true;
	}
}