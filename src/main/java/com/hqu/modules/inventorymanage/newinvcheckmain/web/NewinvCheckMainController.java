/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.inventorymanage.newinvcheckmain.web;

import com.google.common.collect.Lists;
import com.hqu.modules.basedata.item.service.ItemService;
import com.hqu.modules.inventorymanage.billdetailcodes.entity.BillDetailCodes;
import com.hqu.modules.inventorymanage.billdetailcodes.service.BillDetailCodesService;
import com.hqu.modules.inventorymanage.billmain.entity.BillDetail;
import com.hqu.modules.inventorymanage.billmain.entity.BillMain;
import com.hqu.modules.inventorymanage.billmain.mapper.BillDetailMapper;
import com.hqu.modules.inventorymanage.billmain.service.BillMainService;
import com.hqu.modules.inventorymanage.billtype.entity.BillType;
import com.hqu.modules.inventorymanage.employee.entity.Employee;
import com.hqu.modules.inventorymanage.employee.service.EmployeeService;
import com.hqu.modules.inventorymanage.invaccount.entity.InvAccount;
import com.hqu.modules.inventorymanage.invaccount.service.InvAccountService;
import com.hqu.modules.inventorymanage.invaccountcode.entity.InvAccountCode;
import com.hqu.modules.inventorymanage.invaccountcode.service.InvAccountCodeService;
import com.hqu.modules.inventorymanage.newinvcheckmain.entity.NewinvCheckDetail;
import com.hqu.modules.inventorymanage.newinvcheckmain.entity.NewinvCheckMain;
import com.hqu.modules.inventorymanage.newinvcheckmain.service.NewinvCheckMainService;
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
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.utils.UserUtils;
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
import javax.validation.ConstraintViolationException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 超期复验Controller
 * @author Neil
 * @version 2018-06-15
 */
@Controller
@RequestMapping(value = "${adminPath}/newinvcheckmain/newinvCheckMain")
public class NewinvCheckMainController extends BaseController {
	@Autowired
	BillDetailMapper billDetailMapper;

	@Autowired
	private ItemService itemService;

	@Autowired
	private BillDetailCodesService billDetailCodesService;

	@Autowired
	private InvAccountCodeService invAccountCodeService;

    @Autowired
    private InvAccountService invAccountService;

	@Autowired
	private NewinvCheckMainService newinvCheckMainService;


	@Autowired
	private WareHouseService wareHouseService;

	@Autowired
	private EmployeeService employeeService;

	@Autowired
	private BillMainService billMainService;

	@ModelAttribute
	public NewinvCheckMain get(@RequestParam(required=false) String id) {
		NewinvCheckMain entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = newinvCheckMainService.get(id);
		}
		if (entity == null){
			entity = new NewinvCheckMain();
		}
		return entity;
	}


	@RequiresPermissions("newinvcheckmain:newinvCheckMain:list")
	@RequestMapping(value = {"HGSearch", ""})
	public String HGSearch() {
		return "inventorymanage/newinvcheckmain/HGBillList";
	}

	@RequiresPermissions("newinvcheckmain:newinvCheckMain:list")
	@RequestMapping(value = {"HGGSearch", ""})
	public String HGGSearch() {
		return "inventorymanage/newinvcheckmain/HGGBillList";
	}

	@ResponseBody
	@RequiresPermissions("newinvcheckmain:newinvCheckMain:list")
	@RequestMapping(value = "HGdata")
	public Map<String, Object> HGdata(HttpServletRequest request, HttpServletResponse response, Model model) {
		BillMain billMain=new BillMain();
		String ioType="HG01";
		BillType billType=new BillType();
		billType.setIoType(ioType);
		billMain.setIo(billType);
		List<BillMain> list=billMainService.findList(billMain);
		Page<BillMain> page=new Page<BillMain>();
		page.setList(list);
		return getBootstrapData(page);
	}

	@ResponseBody
	@RequiresPermissions("newinvcheckmain:newinvCheckMain:list")
	@RequestMapping(value = "HGGdata")
	public Map<String, Object> HGGdata(HttpServletRequest request, HttpServletResponse response, Model model) {
		BillMain billMain=new BillMain();
		String ioType="HG01";
		BillType billType=new BillType();
		billType.setIoType(ioType);
		billMain.setIo(billType);
		List<BillMain> list=billMainService.findList(billMain);
		Page<BillMain> page=new Page<BillMain>();
		page.setList(list);
		return getBootstrapData(page);
	}

	@RequiresPermissions("newinvcheckmain:newinvCheckMain:list")
	@RequestMapping(value = {"BFSearch", ""})
	public String BFSearch() {
		return "inventorymanage/newinvcheckmain/BFBillList";
	}

	@RequiresPermissions("newinvcheckmain:newinvCheckMain:list")
	@RequestMapping(value = {"BFCSearch", ""})
	public String BFCSearch() {
		return "inventorymanage/newinvcheckmain/BFCBillList";
	}
	@ResponseBody
	@RequiresPermissions("newinvcheckmain:newinvCheckMain:list")
	@RequestMapping(value = "BFdata")
	public Map<String, Object> BFdata(HttpServletRequest request, HttpServletResponse response, Model model) {
		BillMain billMain=new BillMain();
		String ioType="BF01";
		BillType billType=new BillType();
		billType.setIoType(ioType);
		billMain.setIo(billType);
		List<BillMain> list=billMainService.findList(billMain);
		Page<BillMain> page=new Page<BillMain>();
		page.setList(list);
		return getBootstrapData(page);
	}
	@ResponseBody
	@RequiresPermissions("newinvcheckmain:newinvCheckMain:list")
	@RequestMapping(value = "BFCdata")
	public Map<String, Object> BFCdata(HttpServletRequest request, HttpServletResponse response, Model model) {
		BillMain billMain=new BillMain();
		String ioType="BF01";
		BillType billType=new BillType();
		billType.setIoType(ioType);
		billMain.setIo(billType);
		List<BillMain> list=billMainService.findList(billMain);
		Page<BillMain> page=new Page<BillMain>();
		page.setList(list);
		return getBootstrapData(page);
	}
	/**
	 * 超期复验单列表页面
	 */
	@RequiresPermissions("newinvcheckmain:newinvCheckMain:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "inventorymanage/newinvcheckmain/newinvCheckMainList";
	}

	@RequiresPermissions("newinvcheckmain:newinvCheckMain:list")
	@RequestMapping(value = {"Search"})
	public String Search() {
		return "inventorymanage/newinvcheckmain/newinvCheckMainSearchList";
	}

	@RequiresPermissions(value={"newinvcheckmain:newinvCheckMain:view","newinvcheckmain:newinvCheckMain:add","newinvcheckmain:newinvCheckMain:edit"},logical=Logical.OR)
	@RequestMapping(value = "search")
	public String search(NewinvCheckMain newinvCheckMain, Model model) {
		model.addAttribute("newinvCheckMain", newinvCheckMain);
		return "inventorymanage/newinvcheckmain/newinvCheckMainSearchForm";
	}

	@RequiresPermissions("newinvcheckmain:newinvCheckMain:list")
	@RequestMapping(value = {"Audit"})
	public String Audit() {
		return "inventorymanage/newinvcheckmain/newinvCheckMainAuditList";
	}

	@RequiresPermissions(value={"newinvcheckmain:newinvCheckMain:view","newinvcheckmain:newinvCheckMain:add","newinvcheckmain:newinvCheckMain:edit"},logical=Logical.OR)
	@RequestMapping(value = "audit")
	public String audit(NewinvCheckMain newinvCheckMain, Model model) {
		model.addAttribute("newinvCheckMain", newinvCheckMain);
		return "inventorymanage/newinvcheckmain/newinvCheckMainAuditForm";
	}

	@RequiresPermissions("newinvcheckmain:newinvCheckMain:list")
	@RequestMapping(value = {"Dispose"})
	public String Dispose() {
		return "inventorymanage/newinvcheckmain/newinvCheckMainDisposeList";
	}

	@RequiresPermissions(value={"newinvcheckmain:newinvCheckMain:view","newinvcheckmain:newinvCheckMain:add","newinvcheckmain:newinvCheckMain:edit"},logical=Logical.OR)
	@RequestMapping(value = "dispose")
	public String dispose(NewinvCheckMain newinvCheckMain, Model model) {
		model.addAttribute("newinvCheckMain", newinvCheckMain);
		return "inventorymanage/newinvcheckmain/newinvCheckMainDisposeForm";
	}



		/**
	 * 超期复验单列表数据
	 */
	@ResponseBody
	@RequiresPermissions("newinvcheckmain:newinvCheckMain:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(NewinvCheckMain newinvCheckMain, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<NewinvCheckMain> page = newinvCheckMainService.findPage(new Page<NewinvCheckMain>(request, response), newinvCheckMain);
		return getBootstrapData(page);
	}

	@ResponseBody
	@RequiresPermissions("newinvcheckmain:newinvCheckMain:list")
	@RequestMapping(value = "getdisposedata")
	public Map<String, Object> getcheckdata(NewinvCheckMain newinvCheckMain, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<NewinvCheckMain> page = newinvCheckMainService.findPage(new Page<NewinvCheckMain>(request, response), newinvCheckMain);
		List<NewinvCheckMain> list=page.getList();
		for(int i=0;i<list.size();){
			if(list.get(i).getQmsFlag().equals("未质检"))
				i++;
			else
				list.remove(i);
		}
		page.setList(list);
		//手动设置分页信息
		page.setCount(list.size());
		return getBootstrapData(page);
	}

	@ResponseBody
	@RequiresPermissions("newinvcheckmain:newinvCheckMain:list")
	@RequestMapping(value = "disposedata")
	public Map<String, Object> checkdata(NewinvCheckMain newinvCheckMain, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<NewinvCheckMain> page = newinvCheckMainService.findPage(new Page<NewinvCheckMain>(request, response), newinvCheckMain);
		List<NewinvCheckMain> list=page.getList();
		for(int i=0;i<list.size();){
			if(list.get(i).getQmsFlag().equals("已质检"))
				i++;
			else
				list.remove(i);
		}
		page.setList(list);
		//手动设置分页信息
		page.setCount(list.size());
		return getBootstrapData(page);
	}
	@ResponseBody
	@RequiresPermissions("newinvcheckmain:newinvCheckMain:list")
	@RequestMapping(value = "checkdata")
	public Map<String, Object> disposedata(NewinvCheckMain newinvCheckMain, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<NewinvCheckMain> page = newinvCheckMainService.findPage(new Page<NewinvCheckMain>(request, response), newinvCheckMain);
		List<NewinvCheckMain> list=page.getList();
		for(int i=0;i<list.size();){
			if(list.get(i).getQmsFlag().equals("将要质检"))
				i++;
			else
				list.remove(i);
		}
		page.setList(list);
		page.setCount(list.size());
		return getBootstrapData(page);
	}
	/**
	 * 查询子表二维码
	 */
	@ResponseBody
	@RequiresPermissions("newinvcheckmain:newinvCheckMain:list")
	@RequestMapping(value="findcodelist")
	public Map<String,Object> findcodelist(String wareId,InvAccount invAccount, HttpServletRequest request, HttpServletResponse response, Model model){
		List<InvAccountCode> listCode=newinvCheckMainService.getOutDateList(wareId);
		Page<InvAccountCode> page=new Page<>();
		page.setList(listCode);
		return getBootstrapData(page);
	}

	/**
	 * 查询子表数据
	 */
	@ResponseBody
	@RequiresPermissions("newinvcheckmain:newinvCheckMain:list")
	@RequestMapping(value="findlist")
	public Map<String,Object> findlist(String wareId,InvAccount invAccount, HttpServletRequest request, HttpServletResponse response, Model model) {
		List<InvAccountCode> listCode=newinvCheckMainService.getOutDateList(wareId);
		List<NewinvCheckDetail> list=new ArrayList<>();
		while (!listCode.isEmpty()){
			NewinvCheckDetail newinvCheckDetail=new NewinvCheckDetail();
			newinvCheckDetail.setItemCode(listCode.get(0).getItem());
			newinvCheckDetail.setItemQty(1);
			BillDetailCodes billDetailCode=new BillDetailCodes();
			billDetailCode.setItemBarcode(listCode.get(0).getItemBarcode());
			List<BillDetailCodes> billList=billDetailCodesService.findList(billDetailCode);
			if(billList.isEmpty()){

			}else {
				BillMain billMain=new BillMain();
				billMain.setBillNum(billList.get(0).getBillNum());
				BillDetail billDetail=new BillDetail();
				billDetail.setBillNum(billMain);
				billDetail.setItem(listCode.get(0).getItem());
				List<BillDetail> billDetailList=billDetailMapper.findList(billDetail);
				if(billDetailList.isEmpty()){

				}else {
					billDetail=billDetailList.get(0);
					if(billDetail.getLoc()!=null&&billDetail.getBin()!=null){
						newinvCheckDetail.setLocId(billDetail.getLoc().getLocId());
						newinvCheckDetail.setBinId(billDetail.getBin().getBinId());
					}
					newinvCheckDetail.setLocName(billDetail.getLocName());
					newinvCheckDetail.setBinName(billDetail.getBinName());
					newinvCheckDetail.setMeasUnit(billDetail.getMeasUnit());
					newinvCheckDetail.setItemName(billDetail.getItemName());
					newinvCheckDetail.setItemBatch(billDetail.getItemBatch());
				}
			}
			listCode.remove(0);
			for(int i=0;i<listCode.size();){
				if (listCode.get(i).getItem().getCode().equals(newinvCheckDetail.getItemCode().getCode())){
					newinvCheckDetail.setItemQty(newinvCheckDetail.getItemQty()+1);
					listCode.remove(i);
				}else {
					i++;
				}
			}
			list.add(newinvCheckDetail);
		}
		Page<NewinvCheckDetail> page=new Page<NewinvCheckDetail>();
		page.setList(list);
		page.setCount(list.size());
	    return  getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑超期复验单表单页面
	 */
	@RequiresPermissions(value={"newinvcheckmain:newinvCheckMain:view","newinvcheckmain:newinvCheckMain:add","newinvcheckmain:newinvCheckMain:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	
	public String form(NewinvCheckMain newinvCheckMain, Model model) {
		model.addAttribute("newinvCheckMain", newinvCheckMain);
		if(StringUtils.isBlank(newinvCheckMain.getId())){//如果ID是空为添加
			model.addAttribute("isAdd", true);
			User u = UserUtils.getUser();
			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			String billnum = newinvCheckMainService.getMaxIdByBillnum("FYD"+sdf.format(date));
			if(StringUtils.isEmpty(billnum)) {
				billnum="0000";
			}else {
				billnum=String.format("%04d", Integer.parseInt(billnum.substring(11,15))+1);
			}
			newinvCheckMain.setBillNum("FYD"+sdf.format(date)+billnum);
			newinvCheckMain.setPeriodId(billMainService.findPeriodByTime(date).getPeriodId().toString());
			newinvCheckMain.setMakeDate(date);
			newinvCheckMain.setMakeEmpid(u);
			newinvCheckMain.setMakeEmpName(u.getName());
			newinvCheckMain.setBillFlag("A");
			newinvCheckMain.setQmsFlag("未质检");
			model.addAttribute("newinvCheckMain", newinvCheckMain);
		}
		return "inventorymanage/newinvcheckmain/newinvCheckMainForm";
	}
	
	/**
	 * 保存超期复验单
	 */
	@RequiresPermissions(value={"newinvcheckmain:newinvCheckMain:add","newinvcheckmain:newinvCheckMain:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(NewinvCheckMain newinvCheckMain, HttpServletRequest request, HttpServletResponse response, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, newinvCheckMain)){
			return form(newinvCheckMain, model);
		}
		//新增或编辑表单保存
		String wareID=request.getParameter("wareId.wareIDs");
		String wareEmpId=request.getParameter("wareEmpId.empID");
		String checkEmpIdnos=request.getParameter("checkEmpId.nos");
		WareHouse wareHouse=new WareHouse();
		wareHouse.setWareID(wareID);
		User user=new User();
		user.setNo(wareEmpId);
		Employee employee=new Employee();
		employee.setUser(user);
		newinvCheckMain.setWareId(wareHouse);
		newinvCheckMain.setWareEmpId(employee);
		User user1=new User();
		user1.setNo(checkEmpIdnos);
		newinvCheckMain.setCheckEmpId(user1);
		newinvCheckMain.setBillFlag("F");
		newinvCheckMainService.save(newinvCheckMain);//保存
		//newinvCheckMainService.lockItem();
		addMessage(redirectAttributes, "保存超期复验单成功");
		return "redirect:"+Global.getAdminPath()+"/newinvcheckmain/newinvCheckMain/list/?repage";
	}

	/**
	 * 审核超期复验单
	 */
	@RequiresPermissions(value={"newinvcheckmain:newinvCheckMain:add","newinvcheckmain:newinvCheckMain:edit"},logical=Logical.OR)
	@RequestMapping(value = "auditcheck")
	public String auditcheck(NewinvCheckMain newinvCheckMain, HttpServletRequest request, HttpServletResponse response, Model model, RedirectAttributes redirectAttributes) throws Exception{
		newinvCheckMainService.audit(newinvCheckMain);
		addMessage(redirectAttributes, "审核超期复验单成功");
		return "redirect:"+Global.getAdminPath()+"/newinvcheckmain/newinvCheckMain/Audit/?repage";
	}

	/**
	 * 处理超期复验单
	 */
	@RequiresPermissions(value={"newinvcheckmain:newinvCheckMain:add","newinvcheckmain:newinvCheckMain:edit"},logical=Logical.OR)
	@RequestMapping(value = "disposeF")
	public String dispose(NewinvCheckMain newinvCheckMain, HttpServletRequest request, HttpServletResponse response, Model model, RedirectAttributes redirectAttributes) throws Exception{
		newinvCheckMain.setQmsFlag("已处理");
		newinvCheckMainService.save(newinvCheckMain);
		newinvCheckMainService.dispose(newinvCheckMain);
		addMessage(redirectAttributes, "处理超期复验单成功");
		return "redirect:"+Global.getAdminPath()+"/newinvcheckmain/newinvCheckMain/Dispose/?repage";
	}

	/**
	 * 删除超期复验单
	 */
	@ResponseBody
	@RequiresPermissions("newinvcheckmain:newinvCheckMain:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(NewinvCheckMain newinvCheckMain, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		newinvCheckMainService.delete(newinvCheckMain);
		j.setMsg("删除超期复验单成功");
		return j;
	}
	
	/**
	 * 批量删除超期复验单
	 */
	@ResponseBody
	@RequiresPermissions("newinvcheckmain:newinvCheckMain:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			newinvCheckMainService.delete(newinvCheckMainService.get(id));
		}
		j.setMsg("删除超期复验单成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("newinvcheckmain:newinvCheckMain:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(NewinvCheckMain newinvCheckMain, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "超期复验单"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<NewinvCheckMain> page = newinvCheckMainService.findPage(new Page<NewinvCheckMain>(request, response, -1), newinvCheckMain);
    		new ExportExcel("超期复验单", NewinvCheckMain.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出超期复验单记录失败！失败信息："+e.getMessage());
		}
			return j;
    }
    
    @ResponseBody
    @RequestMapping(value = "detail")
	public NewinvCheckMain detail(String id) {
		return newinvCheckMainService.get(id);
	}
	

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("newinvcheckmain:newinvCheckMain:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<NewinvCheckMain> list = ei.getDataList(NewinvCheckMain.class);
			for (NewinvCheckMain newinvCheckMain : list){
				try{
					newinvCheckMainService.save(newinvCheckMain);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条超期复验单记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条超期复验单记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入超期复验单失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/newinvcheckmain/newinvCheckMain/?repage";
    }
	
	/**
	 * 下载导入超期复验单数据模板
	 */
	@RequiresPermissions("newinvcheckmain:newinvCheckMain:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "超期复验单数据导入模板.xlsx";
    		List<NewinvCheckMain> list = Lists.newArrayList(); 
    		new ExportExcel("超期复验单数据", NewinvCheckMain.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/newinvcheckmain/newinvCheckMain/?repage";
    }
	

}