/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.inventorymanage.finishedproduct.inboundinput.web;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.hqu.modules.basedata.item.entity.Item;
import com.hqu.modules.basedata.item.service.ItemService;
import com.hqu.modules.basedata.period.service.PeriodService;
import com.hqu.modules.inventorymanage.billdetailcodes.service.BillDetailCodesService;
import com.hqu.modules.inventorymanage.billmain.entity.BillDetail;
import com.hqu.modules.inventorymanage.billmain.entity.BillDetailCode;
import com.hqu.modules.inventorymanage.billmain.entity.BillMain;
import com.hqu.modules.inventorymanage.billmain.mapper.BillMainMapper;
import com.hqu.modules.inventorymanage.billmain.service.BillMainService;
import com.hqu.modules.inventorymanage.billtype.entity.BillType;
import com.hqu.modules.inventorymanage.billtype.entity.BillTypeWareHouse;
import com.hqu.modules.inventorymanage.billtype.service.BillTypeService;
import com.hqu.modules.inventorymanage.bin.service.BinService;
import com.hqu.modules.inventorymanage.employee.entity.Employee;
import com.hqu.modules.inventorymanage.employee.entity.EmployeeWareHouse;
import com.hqu.modules.inventorymanage.employee.service.EmployeeService;
import com.hqu.modules.inventorymanage.employee.service.EmployeeWareHouseService;
import com.hqu.modules.inventorymanage.finishedproduct.inboundinput.service.InBoundInputService;
import com.hqu.modules.inventorymanage.invaccount.entity.InvAccount;
import com.hqu.modules.inventorymanage.invaccount.service.InvAccountService;
import com.hqu.modules.inventorymanage.invaccountcode.service.InvAccountCodeService;
import com.hqu.modules.inventorymanage.location.service.LocationService;
import com.hqu.modules.inventorymanage.outsourcingoutput.service.OutsourcingOutputService;
import com.hqu.modules.inventorymanage.warehouse.entity.WareHouse;
import com.hqu.modules.inventorymanage.warehouse.service.WareHouseService;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.excel.ExportExcel;
import com.jeeplus.common.utils.excel.ImportExcel;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.modules.sys.entity.Office;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.service.OfficeService;
import com.jeeplus.modules.sys.utils.UserUtils;
import jdk.nashorn.internal.runtime.Debug;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.*;
import javax.validation.ConstraintViolationException;
import javax.xml.crypto.Data;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 成品入库单据录入Controller
 * @author hzb
 * @version 2018-04-16
 */
@Controller
@RequestMapping(value = "${adminPath}/inboundinput/inboundInput")
public class InBoundInputController extends BaseController {

	@Autowired
	private BillMainService billMainService;
	@Autowired
	private BillTypeService billTypeService;
	@Autowired
	private OfficeService officeService;
	@Autowired
	private InvAccountService invAccountService;
	@Autowired
	private EmployeeService employeeService;
	@Autowired
	private WareHouseService wareHouseService;
	@Autowired
	private OutsourcingOutputService outsourcingOutputService;
	@Autowired
	private PeriodService periodService;
	@Autowired
	private EmployeeWareHouseService employeeWareHouseService;
	@Autowired
	private InBoundInputService inBoundInputService;
	@Autowired
	private ItemService itemService;
	@Autowired
	private BinService binService;
	@Autowired
	private LocationService locationService;
	@Autowired
	private BillMainMapper billMainMapper;
	@Autowired
	private InvAccountCodeService invAccountCodeService;

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
	 * @param billMain
	 * @param model
	 * @param redirectAttributes
	 * @return
	 * @throws Exception
	 */
	@Transactional
	@RequestMapping(value = "formNotAuditdo")
	public String formNotAuditdo(String type,BillMain billMain, Model model, RedirectAttributes redirectAttributes) throws Exception {

		String curperiod = wareHouseService.get(billMain.getWare().getId()).getCurrPeriod();
		logger.debug("gid:" + billMain.getWare());
		//判断核算期是否一致
		if (!curperiod.equals(billMain.getPeriod().getPeriodId())) {
			addMessage(redirectAttributes, "反过账失败，当前核算期与仓库核算期不一致！");
			return "redirect:"+Global.getAdminPath()+"/inboundinput/inboundInput/?type="+type+"&repage";
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
                decreaseAcount(billMain);
				billMainService.save(billMain);
				addMessage(redirectAttributes, "反过账成功");
			} else {
				addMessage(redirectAttributes, "反过账失败: " + resultMap.get("msg"));
			}
			return "redirect:"+Global.getAdminPath()+"/inboundinput/inboundInput/?type="+type+"&repage";
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			addMessage(redirectAttributes, "反过账失败");
			return "redirect:"+Global.getAdminPath()+"/inboundinput/inboundInput/?type="+type+"&repage";
		}
	}
	/**
	 * 获取库管员
	 */
	@ResponseBody
	@RequestMapping(value = {"getEmp"})
	public Employee getEmp(String wareId){
		EmployeeWareHouse employeeWareHouse=new EmployeeWareHouse();
		WareHouse wareHouse=new WareHouse();
		wareHouse.setWareID(wareId);
		employeeWareHouse.setWareHouse(wareHouse);
		String id=employeeWareHouseService.findList(employeeWareHouse).get(0).getEmp().getId();
		Employee employee=employeeService.get(id);
		return employee;
	}

	/**
	 * 获取批次管理标识
	 */
	@ResponseBody
	@RequestMapping(value = {"getBatchFlag"})
	public String getBatchFlag(String wareId){
		String BatchFlag = wareHouseService.get(wareId).getBatchFlag();
		logger.debug("管理标识为："+BatchFlag);
		return BatchFlag;
	}

	/**
	 * 单据列表页面
	 */
	@RequiresPermissions("billmain:billMain:list")
	@RequestMapping(value = {"list", ""})
	public String list(String type,Model model) {
		model.addAttribute("type",type);
		return "inventorymanage/finishedproduct/inboundinput/inboundInputList";
	}

	/**
	 * 单据列表数据
	 */

	@ResponseBody
	@RequiresPermissions("billmain:billMain:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(BillMain billMain, HttpServletRequest request,String type,HttpServletResponse response, Model model) {
			//页面值只显示成品入库单据的数据
			BillType billType = new BillType();
			billType.setIoType("CI01");
			List<BillType> billTypes = billTypeService.findList(billType);
			billMain.setIo(billTypes.get(0));
			if("Input".equals(type)){
			    billMain.setBillFlag("AN");
            }
            if("Post".equals(type)){
				billMain.setBillFlag("N");
			}
            if("Fan".equals(type)){
				billMain.setBillFlag("T");
			}
			Page<BillMain> page = billMainService.findPage(new Page<BillMain>(request, response), billMain);
			//通过物料信息做查询时回调data接口
//			if(StringUtils.isNotEmpty(billMain.getItemCode())) {
//				List<BillMain> blist = outsourcingOutputService.findBillMainByItemCode(billMain.getItemCode());
//				logger.debug("item:"+blist);
//				List<BillMain> btlist = new ArrayList<>();
//				if (blist != null) {
//					for (int i = 0; i < blist.size(); i++) {
//						if (page.getList().contains(blist.get(i))) {
//							btlist.add(blist.get(i));
//						}
//					}
//				}
//				page.setList(btlist);
//			}
			return getBootstrapData(page);
	}

	/**
	 * 成品入库打印
	 */
//	@RequiresPermissions("billmain:billMain:list")
	@RequestMapping(value = "printForm")
	public String printForm(BillMain billMain,Model model){
		model.addAttribute("billMain",billMain);
		return "inventorymanage/finishedproduct/inboundinput/inboundInputPrintForm";
	}

	/**
	 * 查看，增加，编辑单据表单页面
	 */
	@RequiresPermissions(value={"billmain:billMain:view","billmain:billMain:add","billmain:billMain:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(BillMain billMain, Model model,String type) {
		//设置过账时的单据状态
		if("Post".equals(type)){
			billMain.setBillFlag("N");
			List<BillDetailCode> billDetailCodes=billMainService.findCodeList(billMain.getBillNum());
			billMain.setBillDetailCodeList(billDetailCodes);
			model.addAttribute("billMain", billMain);
			return "inventorymanage/finishedproduct/inboundinput/inboundPostForm";
		}
		if("Fan".equals(type)){
			return "inventorymanage/finishedproduct/inboundinput/inboundFanPostForm";
		}
		model.addAttribute("billMain", billMain);
		List<BillDetailCode> billDetailCodes=billMainService.findCodeList(billMain.getBillNum());
		billMain.setBillDetailCodeList(billDetailCodes);
		model.addAttribute("type",type);
		if(StringUtils.isBlank(billMain.getId())){//如果ID是空为添加
			User u = UserUtils.getUser();
			Office o = officeService.get(u.getOffice().getId());
			Date date =new Date();
			//设置单据日期
			billMain.setBillDate(date);
			//设置部门
			billMain.setDept(o);
			billMain.setDeptName(o.getName());
			//设置待过账的单据状态
			billMain.setBillFlag("A");
			billMain.setBillPerson(u);
			//设置当前核算期
			billMain.setPeriod(billMainService.findPeriodByTime(date));
			//设置单据号
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			String newBillNum = billMainService.getMaxIdByTypeAndDate("CPI"+sdf.format(date));
			if(StringUtils.isEmpty(newBillNum)){
				newBillNum = "0000";
			}
			else {
				newBillNum = String.format("%04d", Integer.parseInt(newBillNum.substring(11,15))+1);
			}

			billMain.setBillNum("CPI" + sdf.format(date) + newBillNum);
			model.addAttribute("isAdd", true);
		}
		return "inventorymanage/finishedproduct/inboundinput/inboundInputForm";
	}

	/**
	 * 保存单据
	 */
	@RequiresPermissions(value={"billmain:billMain:add","billmain:billMain:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(BillMain billMain, Model model, RedirectAttributes redirectAttributes,String type) throws Exception{
		if("Post".equals(type)){
			if(!inBoundInputService.judgeAdminMode(billMain)){
				addMessage(redirectAttributes,"过账失败，货区货位不符合管理标识！");
				return "redirect:"+Global.getAdminPath()+"/inboundinput/inboundInput/?type="+type+"&repage";
			}
			String curperiod=wareHouseService.get(billMain.getWare().getId()).getCurrPeriod();
			if(!curperiod.equals(billMain.getPeriod().getPeriodId())) {
				addMessage(redirectAttributes, "过账失败，当前核算期与仓库核算期不一致！");
				return "redirect:"+Global.getAdminPath()+"/inboundinput/inboundInput/?type="+type+"&repage";
			}
            Map<String,Object> resultMap = invAccountService.post(billMain);
            if((boolean)resultMap.get("result")){
				billMain.setBillFlag("T");
				addRealQty(billMain);
				billMainService.save(billMain);
				invAccountCodeService.saveProduct(billMain);
				addMessage(redirectAttributes,"单据过账成功");
				return "redirect:"+Global.getAdminPath()+"/inboundinput/inboundInput/?type="+type+"&repage";
			}
			else {
				addMessage(redirectAttributes,"单据过账失败: "+ resultMap.get("msg"));
				return "redirect:"+Global.getAdminPath()+"/inboundinput/inboundInput/?type="+type+"&repage";
			}
		}
		User u = UserUtils.getUser();
		BillType billType = new BillType();
		//设置io类型
		billType.setIoType("CI01");
		List<BillType> billTypes = billTypeService.findList(billType);
		billMain.setIo(billTypes.get(0));
		billMain.setIoFlag("I");
		//设置制单人
		billMain.setBillEmp(u);
		//设置经办人
		billMain.setBillPerson(u);
		billMain.setOrderCode(233);
		if (!beanValidator(model, billMain)){	//如果参数校验不符合就返回
			return form(billMain, model,"Input");
		}
		if("Input".equals(type)) {
			billMain.setBillFlag("N");
			//手动把子表的物料货区货位id转成code
			for(int i=0;i<billMain.getBillDetailList().size();i++){
				String itemcode = itemService.get(billMain.getBillDetailList().get(i).getItem().getId()).getCode();
				billMain.getBillDetailList().get(i).getItem().setId(itemcode);
				if(StringUtils.isNotEmpty(billMain.getBillDetailList().get(i).getBin().getId())&&
						StringUtils.isNotEmpty(billMain.getBillDetailList().get(i).getLoc().getId())) {
					String bincode = binService.get(billMain.getBillDetailList().get(i).getBin().getId()).getBinId();
					String loccode = locationService.get(billMain.getBillDetailList().get(i).getLoc().getId()).getLocId();
					billMain.getBillDetailList().get(i).getBin().setId(bincode);
					billMain.getBillDetailList().get(i).getLoc().setId(loccode);
				}
			}
			if(inBoundInputService.judgeAdminMode(billMain)){
				billMainService.save(billMain);//保存
				for (BillDetailCode billDetailCode : billMain.getBillDetailCodeList()){

					billMainService.saveBillCode(billMain,billDetailCode);
			}
				addMessage(redirectAttributes, "保存单据成功");
			}else
				{
				addMessage(redirectAttributes,"货区货位不符合管理标识，录入失败");
			}
		}

		return "redirect:"+Global.getAdminPath()+"/inboundinput/inboundInput/?type="+type+"&repage";
	}
	
	/**
	 * 删除单据
	 */
	@ResponseBody
	@RequiresPermissions("billmain:billMain:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(BillMain billMain, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
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
		String idArray[] =ids.split(",");
		for(String id : idArray){
			billMainService.delete(billMainService.get(id));
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
	 * 增加库存帐的可用量
	 */
	public boolean addRealQty(BillMain billMain){
		InvAccount invAccount = new InvAccount();
		InvAccount invAccount1;
		double realQty;
		for(BillDetail billDetail:billMain.getBillDetailList()){
			invAccount.setWare(billMain.getWare());
			invAccount.setItem(billDetail.getItem());
			invAccount.setYear(billMain.getPeriod().getPeriodId().substring(0,4));
			invAccount.setPeriod(billMain.getPeriod().getPeriodId().substring(4,6));
			invAccount1 = invAccountService.getByInvAccount(invAccount);
			//增加现有量
			if(invAccount1.getRealQty() == null||invAccount1.getRealQty() == 0) {
				realQty = billDetail.getItemQty();
			}
			else {
				realQty = invAccount1.getRealQty() + billDetail.getItemQty();
			}
			invAccount1.setRealQty(realQty);
			invAccountService.save(invAccount1);
		}
		return true;
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
}