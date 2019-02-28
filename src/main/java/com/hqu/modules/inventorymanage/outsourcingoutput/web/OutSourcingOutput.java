package com.hqu.modules.inventorymanage.outsourcingoutput.web;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import com.hqu.modules.inventorymanage.billmain.service.BillMainService;
import com.hqu.modules.inventorymanage.warehouse.entity.WareHouse;
import com.jeeplus.modules.sys.entity.Office;
import com.jeeplus.modules.sys.entity.User;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.hqu.modules.basedata.item.entity.Item;
import com.hqu.modules.basedata.period.entity.Period;
import com.hqu.modules.inventorymanage.billmain.entity.BillDetail;
import com.hqu.modules.inventorymanage.billmain.entity.BillDetailCode;
import com.hqu.modules.inventorymanage.billmain.entity.BillMain;
import com.hqu.modules.inventorymanage.billtype.entity.BillType;
import com.hqu.modules.inventorymanage.billtype.service.BillTypeService;
import com.hqu.modules.inventorymanage.invaccount.entity.InvAccount;
import com.hqu.modules.inventorymanage.invaccount.service.InvAccountService;
import com.hqu.modules.inventorymanage.outsourcingoutput.service.BillMainOutsourcingService;
import com.hqu.modules.inventorymanage.outsourcingoutput.service.OutsourcingOutputService;
import com.hqu.modules.inventorymanage.warehouse.service.WareHouseService;
import com.hqu.modules.workshopmanage.materialorder.entity.SfcMaterialOrder;
import com.hqu.modules.workshopmanage.materialorder.entity.SfcMaterialOrderDetail;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.UDP.UdpUtil;
import com.jeeplus.common.utils.excel.ExportExcel;
import com.jeeplus.common.utils.excel.ImportExcel;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.modules.sys.utils.UserUtils;

@Controller
@RequestMapping(value = "${adminPath}/outsourcingoutput/outsourcingOutput")
public class OutSourcingOutput extends BaseController {
	@Autowired
	private BillMainOutsourcingService billMainOutsourcingService;
	@Autowired
	private OutsourcingOutputService outsourcingOutputService;
	@Autowired
	private BillTypeService billTypeService;
	@Autowired
	private InvAccountService invAccountService;
	@Autowired
	private BillMainService billMainService;

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
		return "inventorymanage/outsourcingoutput/billMainList";
	}

	@RequiresPermissions("billmain:billMain:list")
	@RequestMapping(value = { "searchList" })
	public String searchList() {
		return "inventorymanage/outsourcingoutput/billMainListSearch";
	}

	@RequiresPermissions("billmain:billMain:list")
	@RequestMapping(value = { "auditList" })
	public String auditList() {
		return "inventorymanage/outsourcingoutput/billMainListAudit";
	}
	@RequiresPermissions("billmain:billMain:list")
	@RequestMapping(value = { "notAuditList" })
	public String notAuditList() {
		return "inventorymanage/outsourcingoutput/billMainListNotAudit";
	}
	@RequestMapping(value="submitList")
	@RequiresPermissions("billmain:billMain:list")
	public String submitList() {
		return "inventorymanage/outsourcingoutput/billMainListSubmit";
	}
	/**
	 * 单据列表数据
	 */
	@ResponseBody
	@RequiresPermissions("billmain:billMain:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(BillMain billMain, HttpServletRequest request, HttpServletResponse response,
			Model model) {
//		SfcMaterialOrder scfm=new SfcMaterialOrder();
//		scfm.setShopId("4594cb2fb7a64960b6a883d99891f6fb");
//		scfm.setShopName(UserUtils.getUserOfficeName());
//		scfm.setPeriodId("201808");
//		SfcMaterialOrderDetail detail = new SfcMaterialOrderDetail();
//		detail.setMaterialId("010100000001");
//		detail.setMaterialName("VTM60NO.1");
//		detail.setMaterialSpec("1");
//		detail.setNumUnit("件");
//		detail.setRequireNum(14.0);
//		detail.setSequenceId(1);
//		List<SfcMaterialOrderDetail> list = new ArrayList<>();
//		list.add(detail);
//		scfm.setSfcMaterialOrderDetailList(list);
//		outsourcingOutputService.transferMaterialOrder(scfm);
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
		System.out.println("--111---");
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
		}System.out.println("--222---");
		//设置现有量
		
		for (int i = 0; i < page.getList().size(); i++) {
			BillMain billMainTemp = billMainOutsourcingService.get(page.getList().get(i).getId());
			page.getList().get(i).setBillDetailList(billMainTemp.getBillDetailList());
			for (int j = 0; j < page.getList().get(i).getBillDetailList().size(); j++) {
				InvAccount invAccount = new InvAccount();
				page.getList().get(i).getBillDetailList().get(j).getItem().setCode(page.getList().get(i).getBillDetailList().get(j).getItem().getId());
				System.out.println(page.getList().get(i).getBillDetailList().get(j).getItem()+"-+->"+page.getList().get(i).getBillDetailList().get(j).getItem().getCode());
				invAccount.setItem(page.getList().get(i).getBillDetailList().get(j).getItem());
				invAccount.setPeriod(page.getList().get(i).getPeriod().getPeriodId().substring(4, 6));
				invAccount.setYear(page.getList().get(i).getPeriod().getPeriodId().substring(0, 4));
				invAccount.setWare(page.getList().get(i).getWare());
				//System.out.println("ss++---"+invAccount.getWare().getWareID());
				invAccount = invAccountService.getByInvAccount(invAccount);
				System.out.println("ss++ss"+invAccount);
				if(invAccount!=null) {
					page.getList().get(i).getBillDetailList().get(j).setItemQtyTemp(invAccount.getRealQty());
					page.getList().get(i).getBillDetailList().get(j).setAccountId(invAccount.getId());;
				}
				
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
		
		if (StringUtils.isBlank(billMain.getId())) {// 如果ID是空为添加
			//生成业务主键
			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			String newBillNum = billMainOutsourcingService.getMaxIdByTypeAndDate("wgo" + sdf.format(date));
			if (StringUtils.isEmpty(newBillNum)) {
				newBillNum = "0000";
			} else {
				newBillNum = String.format("%04d", Integer.parseInt(newBillNum.substring(11, 15)) + 1);
			}
			logger.debug("billNum:" + newBillNum);
			billMain.setBillNum("wgo" + sdf.format(date) + newBillNum);
			billMain.setBillDate(date);
			Map<String, Object> map = new HashMap<>();
			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			map.put("today", sdf1.format(date));//查询核算期
			Period period = outsourcingOutputService.findPeriodByTime(map);
			billMain.setPeriod(period);
			//获取当前用户信息
			User user = UserUtils.getUser();
			billMain.setBillPerson(user);
			model.addAttribute("isAdd", true);
		}
		if(billMain.getBillDetailList().size()>0) {
			for (int j = 0; j < billMain.getBillDetailList().size(); j++) {
				InvAccount invAccount = new InvAccount();
				billMain.getBillDetailList().get(j).getItem().setCode(billMain.getBillDetailList().get(j).getItem().getId());
				System.out.println(billMain.getBillDetailList().get(j).getItem()+"-+->"+billMain.getBillDetailList().get(j).getItem().getCode());
				invAccount.setItem(billMain.getBillDetailList().get(j).getItem());
				invAccount.setPeriod(billMain.getPeriod().getPeriodId().substring(4, 6));
				invAccount.setYear(billMain.getPeriod().getPeriodId().substring(0, 4));
				invAccount.setWare(billMain.getWare());
				System.out.println("ss++---ss"+invAccount);
				invAccount = invAccountService.getByInvAccount(invAccount);
				System.out.println("ss++ss"+invAccount);
				if(invAccount!=null) {
					billMain.getBillDetailList().get(j).setItemQtyTemp(invAccount.getRealQty());
					billMain.getBillDetailList().get(j).setAccountId(invAccount.getId());;
				}
				
			}
		}
		List<BillDetailCode> codeList = outsourcingOutputService.findCodeByBillNum(billMain.getBillNum());
		billMain.setBillDetailCodeList(codeList);
		model.addAttribute("billMain", billMain);
		return "inventorymanage/outsourcingoutput/billMainForm";
	}

	@RequiresPermissions(value = { "billmain:billMain:view", "billmain:billMain:add",
			"billmain:billMain:edit" }, logical = Logical.OR)
	@RequestMapping(value = "formSearch")
	public String formSearch(BillMain billMain, Model model) {
		List<BillDetailCode> codeList = outsourcingOutputService.findCodeByBillNum(billMain.getBillNum());
		billMain.setBillDetailCodeList(codeList);
		model.addAttribute("billMain", billMain);
		return "inventorymanage/outsourcingoutput/billMainFormSearch";
	}

	@RequiresPermissions(value = { "billmain:billMain:view", "billmain:billMain:add",
			"billmain:billMain:edit" }, logical = Logical.OR)
	@RequestMapping(value = "formAudit")
	public String formAudit(BillMain billMain, Model model) {
		System.out.println(billMain.getBillNum() + "+++++++");
		List<BillDetailCode> codeList = outsourcingOutputService.findCodeByBillNum(billMain.getBillNum());
		billMain.setBillDetailCodeList(codeList);
		if(billMain.getBillDetailList().size()>0) {
			for (int j = 0; j < billMain.getBillDetailList().size(); j++) {
				InvAccount invAccount = new InvAccount();
				billMain.getBillDetailList().get(j).getItem().setCode(billMain.getBillDetailList().get(j).getItem().getId());
				System.out.println(billMain.getBillDetailList().get(j).getItem()+"-+->"+billMain.getBillDetailList().get(j).getItem().getCode());
				invAccount.setItem(billMain.getBillDetailList().get(j).getItem());
				invAccount.setPeriod(billMain.getPeriod().getPeriodId().substring(4, 6));
				invAccount.setYear(billMain.getPeriod().getPeriodId().substring(0, 4));
				invAccount.setWare(billMain.getWare());
				System.out.println("ss++---ss"+invAccount);
				invAccount = invAccountService.getByInvAccount(invAccount);
				System.out.println("ss++ss"+invAccount);
				if(invAccount!=null) {
					billMain.getBillDetailList().get(j).setItemQtyTemp(invAccount.getRealQty());
					billMain.getBillDetailList().get(j).setAccountId(invAccount.getId());;
				}

			}
		}
		model.addAttribute("billMain", billMain);
		return "inventorymanage/outsourcingoutput/billMainFormAudit";
	}

	@Transactional
	@RequestMapping(value = "formAuditdo")
	public String formAuditdo(HttpServletRequest request,BillMain billMain, Model model, RedirectAttributes redirectAttributes) throws Exception {
		System.out.println(request.getParameter("audit_btn")+"---===---"+request.getParameter("submit_btn"));
		if("1".equals(request.getParameter("audit_btn"))) {
			List<String> itemList = new ArrayList<>();
			int itemSum = 0;
			for (int i = 0; i < billMain.getBillDetailList().size(); i++) {
				itemSum += Math.abs(billMain.getBillDetailList().get(i).getItemQty());
				itemList.add(billMain.getBillDetailList().get(i).getItem().getCode());
			}
			if (itemSum != billMain.getBillDetailCodeList().size()) {
				addMessage(redirectAttributes, "过账失败，请领数量与实际数量不一致！");
				return "redirect:" + Global.getAdminPath() + "/outsourcingoutput/outsourcingOutput/auditList";
			} else {
				for (int i = 0; i < billMain.getBillDetailCodeList().size(); i++) {
					String itemCode = billMain.getBillDetailCodeList().get(i).getItemBarcode().substring(15, 27);
					if (!itemList.contains(itemCode)) {
						addMessage(redirectAttributes, "过账失败，物料信息不一致");
						return "redirect:" + Global.getAdminPath() + "/outsourcingoutput/outsourcingOutput/auditList";
					}
				}
			}
			String curperiod = wareHouseService.get(billMain.getWare().getId()).getCurrPeriod();
			logger.debug("gid:" + billMain.getWare());
			//判断核算期是否一致
			if (!curperiod.equals(billMain.getPeriod().getPeriodId())) {
				addMessage(redirectAttributes, "过账失败，当前核算期与仓库核算期不一致！");
				return "redirect:" + Global.getAdminPath() + "/outsourcingoutput/outsourcingOutput/auditList";
			}
			try {
				if (billMain.getBillDetailList().size() > 0 && billMain.getBillDetailList().get(0).getItemQty() < 0) {
					for (int i = 0; i < billMain.getBillDetailList().size(); i++) {

						System.out.println(billMain.getBillDetailList().get(i).getAccountId() + "----=====");
						Map<String, Object> map = new HashMap<>();
						double num = billMain.getBillDetailList().get(i).getItemQty();
						System.out.println(num + "---num");
						map.put("id", billMain.getBillDetailList().get(i).getAccountId());
						map.put("num", num);
						invAccountService.updateRealQty(map);//修改可用量
					}
				}
				Map<String, Object> resultMap = invAccountService.post(billMain);
				Map<String, Object> flagMap = new HashMap<>();
				for (int i = 0; i < billMain.getBillDetailCodeList().size(); i++) {
					flagMap.put("Oflag", "1");
					flagMap.put("itemBarcode", billMain.getBillDetailCodeList().get(i).getItemBarcode());
					outsourcingOutputService.updateOflagByBarcode(flagMap);

				}
				Boolean judge = (Boolean) resultMap.get("result");

				if (judge) {
					billMain.setBillFlag("T");
					//billMainService.save(billMain);
					billMainOutsourcingService.save(billMain);
					addMessage(redirectAttributes, "过账成功");
				} else {
					addMessage(redirectAttributes, "过账失败: " + resultMap.get("msg"));
				}
				return "redirect:" + Global.getAdminPath() + "/outsourcingoutput/outsourcingOutput/auditList";
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				addMessage(redirectAttributes, "过账失败");
				return "redirect:" + Global.getAdminPath() + "/outsourcingoutput/outsourcingOutput/auditList";
			}
		}else{
//			Map<String, Object> map = new HashMap<>();
//			map.put("billNum", billMain.getBillNum());
//			map.put("billFlag", billMain.getBillFlag());
			WareHouse wareHouse = wareHouseService.findList(billMain.getWare()).get(0);
			try {
				//outsourcingOutputService.changeBillFlagById(map);
				if("Y".equals(wareHouse.getAutoFlag()) && billMain.getBillDetailList().size()> 0 && billMain.getBillDetailList().get(0).getItemQty()> 0) {
					for (int i = 0; i < billMain.getBillDetailList().size(); i++) {
						Thread.sleep(Long.valueOf(Global.getConfig("udpSendSleepTime")));
						UdpUtil.outTreasury(billMain.getBillNum(), billMain.getBillDetailList().get(i).getItem().getCode(),
								Integer.parseInt(billMain.getBillDetailList().get(i).getItemQty().toString().substring(0,
										billMain.getBillDetailList().get(i).getItemQty().toString().indexOf("."))));
					}
					System.out.println("---Y---");
				}
				if("Y".equals(wareHouse.getAutoFlag()) && billMain.getBillDetailList().size()> 0 && billMain.getBillDetailList().get(0).getItemQty()< 0) {
					for (int i = 0; i < billMain.getBillDetailList().size(); i++) {
						Thread.sleep(Long.valueOf(Global.getConfig("udpSendSleepTime")));
						UdpUtil.backTreasury(billMain.getBillNum(), billMain.getBillDetailList().get(i).getItem().getCode(),
								-1*Integer.parseInt(billMain.getBillDetailList().get(i).getItemQty().toString().substring(0,
										billMain.getBillDetailList().get(i).getItemQty().toString().indexOf("."))));
					}
					System.out.println("---Y---");
				}
				addMessage(redirectAttributes, "提交成功");
			}catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				addMessage(redirectAttributes, "提交失败");
			}
			return "redirect:" + Global.getAdminPath() + "/outsourcingoutput/outsourcingOutput/auditList";
		}
		
		
	}
	@RequiresPermissions(value = { "billmain:billMain:view", "billmain:billMain:add",
	"billmain:billMain:edit" }, logical = Logical.OR)
	@RequestMapping(value = "formNotAudit")
	public String formNotAudit(BillMain billMain, Model model) {
		System.out.println(billMain.getBillNum() + "+++++++");
		List<BillDetailCode> codeList = outsourcingOutputService.findCodeByBillNum(billMain.getBillNum());
		billMain.setBillDetailCodeList(codeList);
		if(billMain.getBillDetailList().size()>0) {
			for (int j = 0; j < billMain.getBillDetailList().size(); j++) {
				InvAccount invAccount = new InvAccount();
				billMain.getBillDetailList().get(j).getItem().setCode(billMain.getBillDetailList().get(j).getItem().getId());
				System.out.println(billMain.getBillDetailList().get(j).getItem()+"-+->"+billMain.getBillDetailList().get(j).getItem().getCode());
				invAccount.setItem(billMain.getBillDetailList().get(j).getItem());
				invAccount.setPeriod(billMain.getPeriod().getPeriodId().substring(4, 6));
				invAccount.setYear(billMain.getPeriod().getPeriodId().substring(0, 4));
				invAccount.setWare(billMain.getWare());
				System.out.println("ss++---ss"+invAccount);
				invAccount = invAccountService.getByInvAccount(invAccount);
				System.out.println("ss++ss"+invAccount);
				if(invAccount!=null) {
					billMain.getBillDetailList().get(j).setItemQtyTemp(invAccount.getRealQty());
					billMain.getBillDetailList().get(j).setAccountId(invAccount.getId());;
				}

			}
		}
		model.addAttribute("billMain", billMain);
		return "inventorymanage/outsourcingoutput/billMainFormNotAudit";
	}
	@Transactional
	@RequestMapping(value = "formNotAuditdo")
	public String formNotAuditdo(BillMain billMain, Model model, RedirectAttributes redirectAttributes) throws Exception {
		
		String curperiod=wareHouseService.get(billMain.getWare().getId()).getCurrPeriod();
		logger.debug("gid:"+billMain.getWare());
		//判断核算期是否一致
		if(!curperiod.equals(billMain.getPeriod().getPeriodId())) {
			addMessage(redirectAttributes, "过账失败，当前核算期与仓库核算期不一致！");
			return "redirect:"+Global.getAdminPath()+"/outsourcingoutput/outsourcingOutput/notAuditList";
		}
		try {
			/*for (int i = 0; i < billMain.getBillDetailList().size(); i++) {

				System.out.println(billMain.getBillDetailList().get(i).getAccountId() + "----=====");
				Map<String, Object> map = new HashMap<>();
				double num = billMain.getBillDetailList().get(i).getItemQty() * (-1);
				System.out.println(num + "---num");
				map.put("id", billMain.getBillDetailList().get(i).getAccountId());
				map.put("num", num);
				invAccountService.updateRealQty(map);//修改可用量
			}*/
			List<BillDetail> detaillist = billMain.getBillDetailList();
			for(int i=0;i<detaillist.size();i++) {
				detaillist.get(i).setItemQty(detaillist.get(i).getItemQty()*(-1));
			}
			billMain.setBillDetailList(detaillist);
			Map<String, Object> resultMap = invAccountService.post(billMain);
			Map<String, Object> flagMap = new HashMap<>();
			for(int i=0;i<billMain.getBillDetailCodeList().size();i++) {
				flagMap.put("Oflag", "0");
				flagMap.put("itemBarcode", billMain.getBillDetailCodeList().get(i).getItemBarcode());
				outsourcingOutputService.updateOflagByBarcode(flagMap);
				
			}
			Boolean judge = (Boolean) resultMap.get("result");

			if (judge) {
				billMain.setBillFlag("N");
				//billMainService.save(billMain);
				List<BillDetail> dlist = billMain.getBillDetailList();
				for(int i=0;i<dlist.size();i++) {
					dlist.get(i).setItemQty(detaillist.get(i).getItemQty()*(-1));
				}
				billMain.setBillDetailList(dlist);
				billMainOutsourcingService.save(billMain);
				addMessage(redirectAttributes, "反过账成功");
			} else {
				addMessage(redirectAttributes, "反过账失败: " + resultMap.get("msg"));
			}
			return "redirect:" + Global.getAdminPath() + "/outsourcingoutput/outsourcingOutput/notAuditList";
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			addMessage(redirectAttributes, "过账失败");
			return "redirect:" + Global.getAdminPath() + "/outsourcingoutput/outsourcingOutput/notAuditList";
		}
		
		
		
	}
	// 修改操作 数量的控制以及是否让选物料还是直接删除
	/**
	 * 保存单据
	 */
	@Transactional
	@RequiresPermissions(value = { "billmain:billMain:add", "billmain:billMain:edit" }, logical = Logical.OR)
	@RequestMapping(value = "save")
	public String save(BillMain billMain, Model model, RedirectAttributes redirectAttributes) throws Exception {
		System.out.println("AAAAAAA");
		billMain.setOrderCode(1);
		BillType billtType = new BillType();
		billtType.setIoType("WO01");
		List<BillType> blist = billTypeService.findList(billtType);
		billMain.setIo(blist.get(0));
		billMain.setBillEmp(UserUtils.getUser());
		billMain.setIoFlag("O");
		billMain.setWorkshopFlag("0");
		System.out.println("code+++");

		if (!beanValidator(model, billMain)) {
			return form(billMain, model);
		}
		System.out.println("code++---+");
		// 新增或编辑表单保存
		try {
			for (BillDetailCode billDetailCode : billMain.getBillDetailCodeList()){

				billMainService.saveBillCode(billMain,billDetailCode);
			}
			billMainOutsourcingService.save(billMain);// 保存
			if(billMain.getBillDetailList().size()>0 && billMain.getBillDetailList().get(0).getItemQty()>0) {
				for (int i = 0; i < billMain.getBillDetailList().size(); i++) {

					System.out.println(billMain.getBillDetailList().get(i).getAccountId() + "----=====");
					Map<String, Object> map = new HashMap<>();
					double num = billMain.getBillDetailList().get(i).getItemQty() - billMain.getBillDetailList().get(i).getItemQty2();
					System.out.println(num + "---num");
					map.put("id", billMain.getBillDetailList().get(i).getAccountId());
					map.put("num", num);
					invAccountService.updateRealQty(map);//修改可用量
				}
			}
			addMessage(redirectAttributes, "保存单据成功");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("-+++++--------");
			e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			addMessage(redirectAttributes, "保存单据失败");
		}

		
		return "redirect:" + Global.getAdminPath() + "/outsourcingoutput/outsourcingOutput";
	}
	/**
	 * 提交出库单
	 * @param
	 * @return
	 */
	@Transactional
	@RequiresPermissions(value = { "billmain:billMain:add", "billmain:billMain:edit" }, logical = Logical.OR)
	@RequestMapping(value = "submit")
	public String submit(BillMain billMain, Model model, RedirectAttributes redirectAttributes) {
		Map<String, Object> map = new HashMap<>();
		map.put("billNum", billMain.getBillNum());
		map.put("billFlag", billMain.getBillFlag());
		WareHouse wareHouse = wareHouseService.findList(billMain.getWare()).get(0);
		try {
			outsourcingOutputService.changeBillFlagById(map);
			if("Y".equals(wareHouse.getAutoFlag()) && billMain.getBillDetailList().size()> 0 && billMain.getBillDetailList().get(0).getItemQty()> 0) {
				for (int i = 0; i < billMain.getBillDetailList().size(); i++) {
					Thread.sleep(Long.valueOf(Global.getConfig("udpSendSleepTime")));
					UdpUtil.outTreasury(billMain.getBillNum(), billMain.getBillDetailList().get(i).getItem().getCode(),
							Integer.parseInt(billMain.getBillDetailList().get(i).getItemQty().toString().substring(0,
									billMain.getBillDetailList().get(i).getItemQty().toString().indexOf("."))));
				}
				System.out.println("---Y---");
			}
			if("Y".equals(wareHouse.getAutoFlag()) && billMain.getBillDetailList().size()> 0 && billMain.getBillDetailList().get(0).getItemQty()< 0) {
				for (int i = 0; i < billMain.getBillDetailList().size(); i++) {
					Thread.sleep(Long.valueOf(Global.getConfig("udpSendSleepTime")));
					UdpUtil.backTreasury(billMain.getBillNum(), billMain.getBillDetailList().get(i).getItem().getCode(),
							-1*Integer.parseInt(billMain.getBillDetailList().get(i).getItemQty().toString().substring(0,
									billMain.getBillDetailList().get(i).getItemQty().toString().indexOf("."))));
				}
				System.out.println("---Y---");
			}
			addMessage(redirectAttributes, "提交成功");
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			addMessage(redirectAttributes, "提交失败");
		}
		return "redirect:" + Global.getAdminPath() + "/outsourcingoutput/outsourcingOutput/submitList";
	}
	@RequiresPermissions(value = { "billmain:billMain:view", "billmain:billMain:add",
	"billmain:billMain:edit" }, logical = Logical.OR)
	@RequestMapping(value = "formSubmit")
	public String formSubmit(BillMain billMain, Model model) {
		List<BillDetailCode> codeList = outsourcingOutputService.findCodeByBillNum(billMain.getBillNum());
		billMain.setBillDetailCodeList(codeList);
		model.addAttribute("billMain", billMain);
		return "inventorymanage/outsourcingoutput/billMainFormSubmit";
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
				invAccount.setPeriod(billMain.getPeriod().getPeriodId().substring(4, 6));
				invAccount.setYear(billMain.getPeriod().getPeriodId().substring(0, 4));
				//修改现有量
				for (int i = 0; i < billMain.getBillDetailList().size(); i++) {
					billMain.getBillDetailList().get(i).getItem().setCode(billMain.getBillDetailList().get(i).getItem().getId());
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
		BillMain billMain = billMainOutsourcingService.get(id);
		
		return billMain;
	}

	/**
	 * 导入Excel数据
	 * 
	 */
	@RequiresPermissions("billmain:billMain:import")
	@RequestMapping(value = "import", method = RequestMethod.POST)
	public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<BillMain> list = ei.getDataList(BillMain.class);
			for (BillMain billMain : list) {
				try {
					billMainOutsourcingService.save(billMain);
					successNum++;
				} catch (ConstraintViolationException ex) {
					failureNum++;
				} catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum > 0) {
				failureMsg.insert(0, "，失败 " + failureNum + " 条单据记录。");
			}
			addMessage(redirectAttributes, "已成功导入 " + successNum + " 条单据记录" + failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入单据失败！失败信息：" + e.getMessage());
		}
		return "redirect:" + Global.getAdminPath() + "/billmain/billMain/?repage";
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
			addMessage(redirectAttributes, "导入模板下载失败！失败信息：" + e.getMessage());
		}
		return "redirect:" + Global.getAdminPath() + "/billmain/billMain/?repage";
	}
	/*@RequestMapping(value="test")
	@ResponseBody
	public String transferMaterialOrder(SfcMaterialOrder sfcMaterialOrder) {
		BillMain billMain = new BillMain();
		Office office = new Office();
		office.setCode(sfcMaterialOrder.getShopId());
		office.setName(sfcMaterialOrder.getShopName());
		billMain.setDept(office);
		billMain.setDeptName(sfcMaterialOrder.getShopName());
		Period period = new Period();
		period.setPeriodId(sfcMaterialOrder.getPeriodId());
		billMain.setPeriod(period);
		billMain.setBillFlag("A");
		billMain.setWorkshopFlag("1");
		billMain.setBillDate(new Date());
		//billMain.setBillPerson(UserUtils.getUser());
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String newBillNum = billMainOutsourcingService.getMaxIdByTypeAndDate("wgo" + sdf.format(date));
		if (StringUtils.isEmpty(newBillNum)) {
			newBillNum = "0000";
		} else {
			newBillNum = String.format("%04d", Integer.parseInt(newBillNum.substring(11, 15)) + 1);
		}
		billMain.setBillNum("wgo" + sdf.format(date) + newBillNum);
		List<BillDetail> blist = new ArrayList<>();
		List<SfcMaterialOrderDetail> mlist = sfcMaterialOrder.getSfcMaterialOrderDetailList();
		for(int i=0;i<mlist.size();i++) {
			BillDetail billDetail = new BillDetail();
			billDetail.setItemName(mlist.get(i).getMaterialName());
			billDetail.setItemSpec(mlist.get(i).getMaterialSpec());
			Item item = new Item();
			item.setCode(mlist.get(i).getMaterialId());
			billDetail.setItem(item);
			billDetail.setSerialNum(mlist.get(i).getSequenceId());
			billDetail.setMeasUnit(mlist.get(i).getNumUnit());
			billDetail.setItemQty(mlist.get(i).getRequireNum());
			blist.add(billDetail);
		}
		billMain.setBillDetailList(blist);
		billMainOutsourcingService.save(billMain);
		return billMain.getBillNum();
	}*/
}
