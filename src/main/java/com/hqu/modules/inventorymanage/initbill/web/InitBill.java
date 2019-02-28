package com.hqu.modules.inventorymanage.initbill.web;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hqu.modules.basedata.period.entity.Period;
import com.hqu.modules.inventorymanage.billmain.entity.BillMain;
import com.hqu.modules.inventorymanage.billtype.entity.BillType;
import com.hqu.modules.inventorymanage.billtype.service.BillTypeService;
import com.hqu.modules.inventorymanage.initbill.service.BillMainInitBillService;
import com.hqu.modules.inventorymanage.initbill.service.InitBillService;
import com.hqu.modules.inventorymanage.invaccount.entity.InvAccount;
import com.hqu.modules.inventorymanage.invaccount.service.InvAccountService;
import com.hqu.modules.inventorymanage.warehouse.service.WareHouseService;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.excel.ExportExcel;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.utils.UserUtils;

@Controller
@RequestMapping(value = "${adminPath}/initbill/initBill")
public class InitBill extends BaseController {
	@Autowired
	private BillMainInitBillService billMainOutsourcingService;
	@Autowired
	private InitBillService outsourcingOutputService;
	@Autowired
	private BillTypeService billTypeService;
	@Autowired
	private InvAccountService invAccountService;

	@Autowired
	private WareHouseService wareHouseService;
	@ModelAttribute
	public BillMain get(@RequestParam(required = false) String id) {
		BillMain entity = null;
		if (StringUtils.isNotBlank(id)) {
			entity = billMainOutsourcingService.get(id);
		}
		if (entity == null) {
			entity = new BillMain();
		}
		return entity;
	}

	/**
	 * 单据列表页面
	 */
	@RequiresPermissions("billmain:billMain:list")
	@RequestMapping(value = { "list", "" })
	public String list() {
		return "inventorymanage/initbill/billMainList";
	}

	@RequiresPermissions("billmain:billMain:list")
	@RequestMapping(value = { "searchList" })
	public String searchList() {
		return "inventorymanage/initbill/billMainListSearch";
	}

	@RequiresPermissions("billmain:billMain:list")
	@RequestMapping(value = { "auditList" })
	public String auditList() {
		return "inventorymanage/initbill/billMainListAudit";
	}

	/**
	 * 单据列表数据
	 */
	@ResponseBody
	@RequiresPermissions("billmain:billMain:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(BillMain billMain, HttpServletRequest request, HttpServletResponse response,
			Model model) {
		//判断searchWay的数字来区分是否根据登录的人来显示数据
		if ("0".equals(request.getParameter("searchWay"))) {
			billMain.setUserId("");
		} else if ("1".equals(request.getParameter("searchWay"))) {
			billMain.setUserId(UserUtils.getUser().getId());
			;
		}
		//设置查询条件 单据状态
		billMain.setBillFlag(request.getParameter("showWay"));
		Page<BillMain> page = billMainOutsourcingService.findPage(new Page<BillMain>(request, response), billMain);
		//检索时根据物料查询
		if (!StringUtils.isEmpty(billMain.getItemCode())) {
			List<BillMain> blist = outsourcingOutputService.findBillMainByItemCode(billMain.getItemCode());
			List<BillMain> btlist = new ArrayList<>();
			if (blist != null) {
				for (int i = 0; i < blist.size(); i++) {
					if (page.getList().contains(blist.get(i))) {
						btlist.add(blist.get(i));
					}
				}
			}
			page.setList(btlist);
		}
		//设置现有量
		InvAccount invAccount = new InvAccount();
		for (int i = 0; i < page.getList().size(); i++) {
			for (int j = 0; j < page.getList().get(i).getBillDetailList().size(); j++) {
				invAccount.setWare(page.getList().get(i).getWare());
				invAccount.setItem(page.getList().get(i).getBillDetailList().get(j).getItem());
				invAccount.setPeriod(page.getList().get(i).getPeriod().getPeriodId());
				;
				invAccount = invAccountService.getByInvAccount(invAccount);
				if(invAccount != null)
				page.getList().get(i).getBillDetailList().get(j).setItemQtyTemp(invAccount.getRealQty());
			}

		}
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑单据表单页面
	 */
	@RequiresPermissions(value = { "billmain:billMain:view", "billmain:billMain:add",
			"billmain:billMain:edit" }, logical = Logical.OR)
	@RequestMapping(value = "form")
	public String form(BillMain billMain, Model model) {
		System.out.println(billMain.getBillDetailList().size() + "+++++++");
		if (StringUtils.isBlank(billMain.getId())) {// 如果ID是空为添加
			//生成业务主键
			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			String newBillNum = billMainOutsourcingService.getMaxIdByTypeAndDate("csh" + sdf.format(date));
			if (StringUtils.isEmpty(newBillNum)) {
				newBillNum = "0000";
			} else {
				newBillNum = String.format("%04d", Integer.parseInt(newBillNum.substring(11, 15)) + 1);
			}
			logger.debug("billNum:" + newBillNum);
			billMain.setBillNum("csh" + sdf.format(date) + newBillNum);
			billMain.setBillDate(date);
			Map<String, Object> map = new HashMap<>();
			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			map.put("today", sdf1.format(date));//查询核算期
			Period period = outsourcingOutputService.findPeriodByTime(map);
			billMain.setPeriod(period);
			User user=UserUtils.getUser();//获取当前用户信息
			billMain.setBillEmp(user);
			billMain.setBillEmpname(user.getName());
			billMain.setDept(user.getOffice());
			billMain.setDeptName(user.getOffice().getName());
			model.addAttribute("isAdd", true);
		}
		model.addAttribute("billMain", billMain);
		return "inventorymanage/initbill/billMainForm";
	}

	@RequiresPermissions(value = { "billmain:billMain:view", "billmain:billMain:add",
			"billmain:billMain:edit" }, logical = Logical.OR)
	@RequestMapping(value = "formSearch")
	public String formSearch(BillMain billMain, Model model) {
		model.addAttribute("billMain", billMain);
		return "inventorymanage/initbill/billMainFormSearch";
	}

	@RequiresPermissions(value = { "billmain:billMain:view", "billmain:billMain:add",
			"billmain:billMain:edit" }, logical = Logical.OR)
	@RequestMapping(value = "formAudit")
	public String formAudit(BillMain billMain, Model model) {
		System.out.println(billMain.getBillDetailList().size() + "+++++++");
		model.addAttribute("billMain", billMain);
		return "inventorymanage/initbill/billMainFormAudit";
	}

	@RequestMapping(value = "formAuditdo")
	@Transactional(rollbackFor = Exception.class)
	public String formAuditdo(BillMain billMain, Model model, RedirectAttributes redirectAttributes) throws Exception {
		//判断核算期是否一致
		String curperiod=wareHouseService.get(billMain.getWare().getId()).getCurrPeriod();
		logger.debug("gid:"+curperiod+"--"+billMain.getPeriod().getPeriodId());
		if(!curperiod.equals(billMain.getPeriod().getPeriodId())) {
			addMessage(redirectAttributes, "过账失败，当前核算期与仓库核算期不一致！");
			return "redirect:"+Global.getAdminPath()+"/initbill/initBill/auditList";
		}
		Map<String, Object> resultMap = invAccountService.post(billMain);
		Boolean judge = (Boolean) resultMap.get("result");

		if (judge) {
			billMain.setBillFlag("T");
			//billMainService.save(billMain);
			String wareId=billMain.getWare().getWareID();
			billMainOutsourcingService.save(billMain);
			for(int i=0;i<billMain.getBillDetailList().size();i++) {
				String binId=billMain.getBillDetailList().get(i).getBin().getBinId();
				String locId=billMain.getBillDetailList().get(i).getLoc().getLocId();
				String itemId=billMain.getBillDetailList().get(i).getItem().getCode();
				Map<String, Object> smap=new HashMap<>();
				smap.put("binId", binId);
				smap.put("locId", locId);
				smap.put("itemId", itemId);
				smap.put("wareId", wareId);//初始化时在库存账中新增数据
				smap.put("qty", billMain.getBillDetailList().get(i).getItemQty());
				outsourcingOutputService.updateRealQty(smap);
			}
			addMessage(redirectAttributes, "过账成功");
		} else {
			addMessage(redirectAttributes, "过账失败: " + resultMap.get("msg"));
		}
		return "redirect:" + Global.getAdminPath() + "/initbill/initBill/auditList";
	}

	/**
	 * 保存单据
	 */
	@Transactional
	@RequiresPermissions(value = { "billmain:billMain:add", "billmain:billMain:edit" }, logical = Logical.OR)
	@RequestMapping(value = "save")
	public String save(BillMain billMain, Model model, RedirectAttributes redirectAttributes) throws Exception {
		billMain.setOrderCode(1);
		BillType billtType = new BillType();
		billtType.setIoType("SI01");
		List<BillType> blist = billTypeService.findList(billtType);
		billMain.setIo(blist.get(0));
		billMain.setBillEmp(UserUtils.getUser());
		billMain.setIoFlag("O");
		if (!beanValidator(model, billMain)) {
			return form(billMain, model);
		}

		// 新增或编辑表单保存
		try {
			billMainOutsourcingService.save(billMain);// 保存
//			for (int i = 0; i < billMain.getBillDetailList().size(); i++) {
//				System.out.println(billMain.getBillDetailList().get(i).getAccountId() + "----=====");
//				Map<String, Object> map = new HashMap<>();
//				map.put("id", billMain.getBillDetailList().get(i).getAccountId());
//				map.put("num", billMain.getBillDetailList().get(i).getItemQty()*-1);
//				invAccountService.updateRealQty(map);
//			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}

		addMessage(redirectAttributes, "保存单据成功");
		return "redirect:" + Global.getAdminPath() + "/initbill/initBill";
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
	@Transactional
	@ResponseBody
	@RequiresPermissions("billmain:billMain:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] = ids.split(",");
		try {
			for (String id : idArray) {
				BillMain billMain = billMainOutsourcingService.get(id);
				InvAccount invAccount = new InvAccount();
				invAccount.setWare(billMain.getWare());
				invAccount.setPeriod(billMain.getPeriod().getPeriodId());
				for (int i = 0; i < billMain.getBillDetailList().size(); i++) {
					invAccount.setItem(billMain.getBillDetailList().get(i).getItem());
					invAccount = invAccountService.getByInvAccount(invAccount);
					Map<String, Object> map = new HashMap<>();
					map.put("id", invAccount.getId());
					map.put("num", billMain.getBillDetailList().get(i).getItemQty());
					outsourcingOutputService.changeRealQty(map);
				}
				billMainOutsourcingService.delete(billMain);
			}
			j.setMsg("删除单据成功");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			j.setMsg("删除单据失败");
		}
		
		return j;
	}

	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("billmain:billMain:export")
	@RequestMapping(value = "export", method = RequestMethod.POST)
	public AjaxJson exportFile(BillMain billMain, HttpServletRequest request, HttpServletResponse response,
			RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
			String fileName = "单据" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
			Page<BillMain> page = billMainOutsourcingService.findPage(new Page<BillMain>(request, response, -1),
					billMain);
			new ExportExcel("单据", BillMain.class).setDataList(page.getList()).write(response, fileName).dispose();
			j.setSuccess(true);
			j.setMsg("导出成功！");
			return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出单据记录失败！失败信息：" + e.getMessage());
		}
		return j;
	}

	@ResponseBody
	@RequestMapping(value = "detail")
	public BillMain detail(String id) {
		return billMainOutsourcingService.get(id);
	}

	

	
}
