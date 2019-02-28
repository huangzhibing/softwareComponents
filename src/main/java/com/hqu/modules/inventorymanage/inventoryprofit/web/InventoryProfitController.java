/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.inventorymanage.inventoryprofit.web;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.hqu.modules.basedata.period.service.PeriodService;
import com.hqu.modules.inventorymanage.billmain.entity.BillMain;
import com.hqu.modules.inventorymanage.billmain.service.BillMainService;
import com.hqu.modules.inventorymanage.billtype.entity.BillType;
import com.hqu.modules.inventorymanage.billtype.service.BillTypeService;
import com.hqu.modules.inventorymanage.employee.entity.Employee;
import com.hqu.modules.inventorymanage.employee.service.EmployeeService;
import com.hqu.modules.inventorymanage.invaccount.service.InvAccountService;
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
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.BeanUtils;
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
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 出入库单据Controller
 * @author M1ngz
 * @version 2018-04-16
 */
@Controller
@RequestMapping(value = "${adminPath}/inventoryprofit/inventoryProfit")
public class InventoryProfitController extends BaseController {

	@Autowired
	private BillMainService billMainService;
	@Autowired
	private OfficeService officeService;
	@Autowired
	private BillTypeService billTypeService;
	@Autowired
	private EmployeeService employeeService;
	@Autowired
	private PeriodService periodService;
	@Autowired
	private InvAccountService invAccountService;


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
	 * 单据列表页面
	 */
	@RequiresPermissions("billmain:billMain:list")
	@RequestMapping(value = {"list", ""})
	public String list(String type,Model model) {
		model.addAttribute("type",type);
		return "inventorymanage/inventoryprofit/inventoryProfitList";
	}


		/**
	 * 单据列表数据
	 */
	@ResponseBody
	@RequiresPermissions("billmain:billMain:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(BillMain billMain, HttpServletRequest request, HttpServletResponse response, Model model ,String type) {
        billMain.setIo(billTypeService.get("5a1048f566f74d20a5582f908fbb6785"));
        if("post".equals(type)){
        	billMain.setBillFlag("N");
		}
	    Page<BillMain> page = billMainService.findPage(new Page<BillMain>(request, response), billMain);
	    logger.debug("page:"+JSON.toJSONString(page));
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑单据表单页面
	 */
	@RequiresPermissions(value={"billmain:billMain:view","billmain:billMain:add","billmain:billMain:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(BillMain billMain, Model model,String type) {

		if("post".equals(type)){
			billMain.setBillFlag("T");
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
            SimpleDateFormat sdf =   new SimpleDateFormat( "yyyyMMdd" );
            String newBillNum = billMainService.getMaxIdByTypeAndDate("PYI"+sdf.format(date));
            if(StringUtils.isEmpty(newBillNum)){
                newBillNum = "0000";
            } else {
                newBillNum = String.format("%04d", Integer.parseInt(newBillNum.substring(11,15))+1);
            }
			logger.debug("billNum:"+newBillNum);
            billMain.setBillNum("PYI" + sdf.format(date) + newBillNum);
			model.addAttribute("isAdd", true);
		} else {
//		    billMain.setWareEmp(employeeService.get(billMain.getWareEmp().getId()));
        }
		logger.debug(JSON.toJSONString(billMain));
        model.addAttribute("billMain", billMain);
        model.addAttribute("type",type);
		return "inventorymanage/inventoryprofit/inventoryProfitForm";
	}

	/**
	 * 保存单据
	 */
	@RequiresPermissions(value={"billmain:billMain:add","billmain:billMain:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(BillMain billMain, Model model, RedirectAttributes redirectAttributes,String type) throws Exception{
        if("post".equals(type)){
            Map<String,Object> resultMap = invAccountService.post(billMain);
            if((boolean)resultMap.get("result")){
                billMain.setBillFlag("T");
                billMainService.save(billMain);//保存
                addMessage(redirectAttributes, "单据过账成功");
                return "redirect:"+Global.getAdminPath()+"/inventoryprofit/inventoryProfit/?type="+type+"&repage";
            } else {

                addMessage(redirectAttributes, "单据过账失败:"+resultMap.get("msg"));
                return "redirect:"+Global.getAdminPath()+"/inventoryprofit/inventoryProfit/?type="+type+"&repage";
            }
        }
        User u = UserUtils.getUser();
        BillType billType = new BillType();
        billType.setIoType("PI01");
        billMain.setIo(billTypeService.get("5a1048f566f74d20a5582f908fbb6785"));
        billMain.setOrderCode(290);
        if("input".equals(type)) {
            billMain.setBillFlag("N");
        }
        billMain.setBillEmp(u);
        billMain.setBillPerson(u);
        logger.debug(JSON.toJSONString(billMain));

        if (!beanValidator(model, billMain)){
			return form(billMain, model,"input");
		}
		//新增或编辑表单保存
        billMainService.save(billMain);//保存
		addMessage(redirectAttributes, "保存单据成功");
		return "redirect:"+Global.getAdminPath()+"/inventoryprofit/inventoryProfit/?type="+type+"&repage";
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
		return "redirect:"+Global.getAdminPath()+"/inventoryprofit/inventoryProfit/?repage";
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
		return "redirect:"+Global.getAdminPath()+"/inventoryprofit/inventoryProfit/?repage";
    }
	

}