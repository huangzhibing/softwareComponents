/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.inventorymanage.impactestimationinput.web;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.hqu.modules.basedata.period.entity.Period;
import com.hqu.modules.basedata.period.service.PeriodService;
import com.hqu.modules.inventorymanage.billmain.entity.BillDetail;
import com.hqu.modules.inventorymanage.billmain.entity.BillMain;
import com.hqu.modules.inventorymanage.billmain.service.BillMainService;
import com.hqu.modules.inventorymanage.billtype.entity.BillType;
import com.hqu.modules.inventorymanage.billtype.entity.BillTypeWareHouse;
import com.hqu.modules.inventorymanage.billtype.service.BillTypeService;
import com.hqu.modules.inventorymanage.employee.entity.Employee;
import com.hqu.modules.inventorymanage.employee.entity.EmployeeWareHouse;
import com.hqu.modules.inventorymanage.employee.service.EmployeeService;
import com.hqu.modules.inventorymanage.employee.service.EmployeeWareHouseService;
import com.hqu.modules.inventorymanage.finishedproduct.inboundinput.service.InBoundInputService;
import com.hqu.modules.inventorymanage.invaccount.service.InvAccountService;
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
 * 冲估单据管理Controller
 * @author ljc
 * @version 2018-04-16
 */
@Controller
@RequestMapping(value = "${adminPath}/impactestimationinput/impactestimationInput")
public class ImpactEstimationInputController extends BaseController {

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
	 * 单据列表页面
	 */
	@RequiresPermissions("billmain:billMain:list")
	@RequestMapping(value = {"list", ""})
	public String list(String type,Model model) {
		model.addAttribute("type",type);
		return "inventorymanage/impactestimationinput/impactestimationInputList";
	}

	/**
	 * 单据列表数据
	 */

	@ResponseBody
	@RequiresPermissions("billmain:billMain:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(BillMain billMain, HttpServletRequest request,String type,HttpServletResponse response, Model model) {
		BillType billType = new BillType();
		billType.setIoType("CG01");
		List<BillType> billTypes = billTypeService.findList(billType);
		billMain.setIo(billTypes.get(0));
		if("Post".equals(type)){
			billMain.setBillFlag("N");
		}
		Page<BillMain> page = billMainService.findPage(new Page<BillMain>(request, response), billMain);
		BillMain mbillMain=null;
		for(int i=0;i<page.getList().size();i++) {
			mbillMain=page.getList().get(i);
			mbillMain.setWareEmp(employeeService.get(mbillMain.getWareEmp().getUser().getNo()));
		}
		logger.debug("page:"+JSON.toJSONString(page));
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑单据表单页面
	 */
	@RequiresPermissions(value={"billmain:billMain:view","billmain:billMain:add","billmain:billMain:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(BillMain billMain, Model model,String type) {
		if("Post".equals(type)){
			billMain.setBillFlag("N");
		}

		if(StringUtils.isBlank(billMain.getId())){//如果ID是空为添加
			User u = UserUtils.getUser();
			Office o = officeService.get(u.getOffice().getId());
			Date date = new Date();
			billMain.setBillDate(date);
			billMain.setDept(o);
			billMain.setDeptName(o.getName());
			billMain.setBillFlag("A");
			billMain.setBillPerson(u);
			billMain.setPeriod(billMainService.findPeriodByTime(date));
			billMain.setWareEmp(employeeService.get("9744f7c5715f413ab61e2be5181eff1d"));
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			String newBillNum = billMainService.getMaxIdByTypeAndDate("CGD"+sdf.format(date));
			if(StringUtils.isEmpty(newBillNum)){
				newBillNum = "0000";
			}
			else {
				newBillNum = String.format("%04d", Integer.parseInt(newBillNum.substring(11,15))+1);
			}

			billMain.setBillNum("CGD" + sdf.format(date) + newBillNum);
			model.addAttribute("isAdd", true);

		}
		else{
			model.addAttribute("displaynone", true);
		}
		model.addAttribute("billMain", billMain);
		model.addAttribute("type",type);
		return "inventorymanage/impactestimationinput/impactestimationInputForm";
	}

	/**
	 * 保存单据
	 */
	@RequiresPermissions(value={"billmain:billMain:add","billmain:billMain:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(BillMain billMain, Model model, RedirectAttributes redirectAttributes,String type) throws Exception{
		if("Post".equals(type)){
//			if(!inBoundInputService.judgeAdminMode(billMain)){
//				addMessage(redirectAttributes,"货区货位不符合管理标识");
//				return "redirect:"+Global.getAdminPath()+"/impactestimationinput/impactestimationInput/?type="+type+"&repage";
//			}
			try {
				Map<String, Object> resultMap = invAccountService.post(billMain);
				if((boolean)resultMap.get("result")){
					billMain.setBillFlag("T");
					billMainService.save(billMain);
					addMessage(redirectAttributes,"单据过账成功");
					return "redirect:"+Global.getAdminPath()+"/impactestimationinput/impactestimationInput/?type="+type+"&repage";
				}
				else {
					addMessage(redirectAttributes,"单据过账失败"+ resultMap.get("msg"));
					return "redirect:"+Global.getAdminPath()+"/impactestimationinput/impactestimationInput/?type="+type+"&repage";
				}
			}
			catch (Exception e){
				e.printStackTrace();
				addMessage(redirectAttributes,"单据过账失败");
				return "redirect:"+Global.getAdminPath()+"/impactestimationinput/impactestimationInput/?type="+type+"&repage";
			}
		}

		User u = UserUtils.getUser();
		BillType billType = new BillType();
		billType.setIoType("CG01");
		List<BillType> billTypes = billTypeService.findList(billType);
		billMain.setIo(billTypes.get(0));
		billMain.setBillEmp(u);
		billMain.setOrderCode(290);
		if("Input".equals(type)) {
			billMain.setBillFlag("N");
		}
		billMain.setBillEmp(u);
		logger.debug(JSON.toJSONString(billMain));

		if (!beanValidator(model, billMain)){
			return form(billMain, model,"input");
		}
		//新增或编辑表单保存
		billMainService.save(billMain);//保存
		addMessage(redirectAttributes, "保存单据成功");
		return "redirect:"+Global.getAdminPath()+"/impactestimationinput/impactestimationInput/?type="+type+"&repage";
//		if("Input".equals(type)) {
//			billMain.setBillFlag("N");
//			if(inBoundInputService.judgeAdminMode(billMain)){
//				billMainService.save(billMain);//保存
//				addMessage(redirectAttributes, "保存单据成功");
//			}else
//			{
//				addMessage(redirectAttributes,"货区货位不符合管理标识");
//			}
//		}

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


}