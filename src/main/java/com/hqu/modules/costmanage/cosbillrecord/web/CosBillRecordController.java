/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.costmanage.cosbillrecord.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

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
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.utils.UserUtils;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.excel.ExportExcel;
import com.jeeplus.common.utils.excel.ImportExcel;
import com.hqu.modules.costmanage.billingrule.entity.CosBillRule;
import com.hqu.modules.costmanage.billingrule.service.CosBillRuleService;
import com.hqu.modules.costmanage.cosbillrecord.entity.CosBillRecord;
import com.hqu.modules.costmanage.cosbillrecord.service.CosBillRecordService;
import com.hqu.modules.costmanage.cositem.entity.CosItem;
import com.hqu.modules.costmanage.cositem.service.CosItemService;
import com.hqu.modules.costmanage.cosprodobject.entity.CosProdObject;
import com.hqu.modules.costmanage.cosprodobject.service.CosProdObjectService;
import com.hqu.modules.costmanage.realaccount.entity.RealAccount;
import com.hqu.modules.costmanage.realaccount.entity.RealAccountDetail;
import com.hqu.modules.costmanage.realaccount.service.RealAccountService;
import com.hqu.modules.inventorymanage.billmain.entity.BillMain;
import com.hqu.modules.inventorymanage.billmain.mapper.BillDetailMapper;
import com.hqu.modules.inventorymanage.billmain.service.BillMainService;

/**
 * 材料单据稽核Controller
 * @author zz
 * @version 2018-08-29
 */
@Controller
@RequestMapping(value = "${adminPath}/cosbillrecord/cosBillRecord")
public class CosBillRecordController extends BaseController {

	@Autowired
	private CosBillRecordService cosBillRecordService;
	@Autowired
	private BillMainService billMainService;
	@Autowired
	private CosItemService cosItemService;
	@Autowired
	private RealAccountService realAccountService;
	@Autowired
	private CosProdObjectService cosProdObjectService;
	@Autowired
	private CosBillRuleService cosBillRuleService;
	@Autowired
	private BillDetailMapper billDetailMapper;
	
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
	 * 材料单据稽核列表页面
	 */
	//@RequiresPermissions("cosbillrecord:cosBillRecord:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "costmanage/cosbillrecord/cosBillRecordList";
		
	}
	
	/**
	 * 材料单据稽核列表页面
	 */
	//@RequiresPermissions("cosbillrecord:cosBillRecord:list")
	@RequestMapping(value = {"list2"})
	public String list2(HttpServletRequest request, HttpServletResponse response, Model model) {
		return "costmanage/cosbillrecord/cosBillRecordList";
	}
	
	/**
	 * 材料单据撤销列表页面
	 */
	//@RequiresPermissions("cosbillrecord:cosBillRecord:list")
	@RequestMapping(value = {"listCancel"})
	public String listCancel(HttpServletRequest request, HttpServletResponse response, Model model) {
		return "costmanage/cosbillrecord/cosBillRecordCancelList";
	}
	
	/**
	 * 材料单据查询列表页面
	 */
	//@RequiresPermissions("cosbillrecord:cosBillRecord:list")
	@RequestMapping(value = {"listQuery"})
	public String listQuery(HttpServletRequest request, HttpServletResponse response, Model model) {
		return "costmanage/cosbillrecord/cosBillRecordQueryList";
	}
	
	/**
	 * 材料单据制单列表页面
	 */
	//@RequiresPermissions("cosbillrecord:cosBillRecord:list")
	@RequestMapping(value = {"listMaking"})
	public String listMaking(HttpServletRequest request, HttpServletResponse response, Model model) {
		return "costmanage/cosbillrecord/cosBillRecordMakingList";
	}
	
//		/**
//	 * 材料单据稽核列表数据
//	 */
//	@ResponseBody
//	@RequiresPermissions("cosbillrecord:cosBillRecord:list")
//	@RequestMapping(value = "data")
//	public Map<String, Object> data(CosBillRecord cosBillRecord, HttpServletRequest request, HttpServletResponse response, Model model) {
//		Page<CosBillRecord> page = cosBillRecordService.findPage(new Page<CosBillRecord>(request, response), cosBillRecord); 
//		return getBootstrapData(page);
//	}
	
	/**
	 * 稽核单据列表数据
	 */
	@ResponseBody
	@RequestMapping(value = "dataforCheck")
	public Map<String, Object> dataforCheck(BillMain billMain, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<BillMain> page = billMainService.findPageforCheck(new Page<BillMain>(request, response), billMain); 
		return getBootstrapData(page);
	}
	
	/**
	 * 撤销稽核单据列表数据
	 */
	@ResponseBody
	@RequestMapping(value = "dataforCancelCheck")
	public Map<String, Object> dataforCancelCheck(BillMain billMain, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<BillMain> page = billMainService.findPageforCancelCheck(new Page<BillMain>(request, response), billMain); 
		return getBootstrapData(page);
	}
	
	/**
	 * 稽核单据列表数据
	 */
	@ResponseBody
	@RequestMapping(value = "dataforQuery")
	public Map<String, Object> dataforQuery(BillMain billMain, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<BillMain> page = billMainService.findPageforQuery(new Page<BillMain>(request, response), billMain); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑材料单据稽核表单页面
	 */
	@RequestMapping(value = "form")
	public String form(BillMain billMain, Model model) {
		model.addAttribute("billMain", billMain);
		if(StringUtils.isBlank(billMain.getId())){//如果ID是空为添加
			model.addAttribute("isAdd", true);
		}
		return "costmanage/cosbillrecord/cosBillRecordForm";
	}
	
	/**
	 * 查看，增加，编辑材料单据撤销表单页面
	 */
	@RequestMapping(value = "Cancelform")
	public String Cancelform(BillMain billMain, Model model) {
		model.addAttribute("billMain", billMain);
		if(StringUtils.isBlank(billMain.getId())){//如果ID是空为添加
			model.addAttribute("isAdd", true);
		}
		return "costmanage/cosbillrecord/cosBillRecordCancelForm";
	}
	
	/**
	 * 查看，增加，编辑材料单据撤销表单页面
	 */
	@RequestMapping(value = "Queryform")
	public String Queryform(BillMain billMain, Model model) {
		model.addAttribute("billMain", billMain);
		if(StringUtils.isBlank(billMain.getId())){//如果ID是空为添加
			model.addAttribute("isAdd", true);
		}
		return "costmanage/cosbillrecord/cosBillRecordQueryForm";
	}
	
	/**
	 * 查看，增加，编辑材料单据撤销表单页面
	 */
	@RequestMapping(value = "Makingform")
	public String Makingform(BillMain billMain, Model model) {
		model.addAttribute("billMain", billMain);
		if(StringUtils.isBlank(billMain.getId())){//如果ID是空为添加
			model.addAttribute("isAdd", true);
		}
		return "costmanage/cosbillrecord/cosBillRecordMakingForm";
	}
	
	/**
	 * 保存材料单据
	 */
	@RequestMapping(value = "saveCosBillRecord")
	public String saveCosBillRecord(BillMain billMain, Model model, RedirectAttributes redirectAttributes) throws Exception{
		CosBillRecord cosBillRecord = new CosBillRecord();
		cosBillRecord.setCorBillNum(billMain.getBillNum());
		cosBillRecord.setCorBillCatalog(billMain.getIo().getIoType());
		cosBillRecord.setCosBillNumStatus("A");
		cosBillRecord.setCheckId(UserUtils.getUser().getCurrentUser());
		cosBillRecord.setCheckName(cosBillRecord.getCheckId().getLoginName());
		cosBillRecord.setCheckDate(DateUtils.getDate());
		
		cosBillRecordService.save(cosBillRecord);//保存
		addMessage(redirectAttributes, "保存材料单据稽核成功");
		return "redirect:"+Global.getAdminPath()+"/cosbillrecord/cosBillRecord/list2?repage";
	}
	
	/**
	 * 保存材料单据
	 */
	@ResponseBody
	@RequestMapping(value = "saveCosBillRecordAll")
	public AjaxJson saveCosBillRecordAll(String ids, RedirectAttributes redirectAttributes) throws Exception{
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			BillMain billMain = billMainService.get(id);
			CosBillRecord cosBillRecord = new CosBillRecord();
			cosBillRecord.setCorBillNum(billMain.getBillNum());
			cosBillRecord.setCorBillCatalog(billMain.getIo().getIoType());
			cosBillRecord.setCosBillNumStatus("A");
			cosBillRecord.setCheckId(UserUtils.getUser().getCurrentUser());
			cosBillRecord.setCheckName(cosBillRecord.getCheckId().getLoginName());
			cosBillRecord.setCheckDate(DateUtils.getDate());
			
			cosBillRecordService.save(cosBillRecord);//保存
		}
		j.setMsg("保存材料单据稽核成功");
		return j;
	}

	/**
	 * 撤销材料单据稽核
	 */
	@RequestMapping(value = "delCosBillRecord")
	public String delCosBillRecord(BillMain billMain, Model model, RedirectAttributes redirectAttributes) throws Exception{
		CosBillRecord cosBillRecord = new CosBillRecord();
		cosBillRecord = cosBillRecordService.get(billMain.getBillNum());
		cosBillRecordService.delete(cosBillRecord);//删除
		addMessage(redirectAttributes, "删除材料单据稽核成功");
		return "redirect:"+Global.getAdminPath()+"/cosbillrecord/cosBillRecord/listCancel?repage";
	}
	
	/**
	 * 撤销材料单据稽核
	 */
	@ResponseBody
	@RequestMapping(value = "delCosBillRecordAll")
	public AjaxJson delCosBillRecordAll(String ids, RedirectAttributes redirectAttributes) throws Exception{
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			BillMain billMain = billMainService.get(id);
			CosBillRecord cosBillRecord = new CosBillRecord();
			cosBillRecord = cosBillRecordService.get(billMain.getBillNum());
			cosBillRecordService.delete(cosBillRecord);//删除
		}
		j.setMsg("删除材料单据稽核成功");
		return j;
	}
	
	/**
	 * 材料凭证制单
	 */
	@RequestMapping(value = "saveBillMaking")
	public String saveBillMaking(BillMain billMain, Model model, RedirectAttributes redirectAttributes) throws Exception{
		//先根据corBillCatalog找科目定义
		CosBillRecord cosBillRecord = new CosBillRecord();
		cosBillRecord = cosBillRecordService.get(billMain.getBillNum());//获取材料单据(包含制单规则)
		if(cosBillRecord!=null){
			RealAccount realAccount = new RealAccount();
			List<RealAccountDetail> realAccountDetailList = new ArrayList<RealAccountDetail>();
			
			//生成凭证号
			String billNum = realAccountService.getBillNum();
			if(billNum == null){
				billNum = "PZ" + DateUtils.getDate().replace("-", "") + "00001";
			}
			realAccount.setBillNum(billNum);
			//获得原始单据信息
			realAccount.setOriBillId(billMain.getBillNum());
			realAccount.setOriBillDate(billMain.getBillDate());
			//获得经办人
			User usernew = UserUtils.getUser();
			realAccount.setMakeId(usernew.getNo());
			realAccount.setMakeName(usernew.getName());
			//获得制单时间
			Date date = new Date();
			realAccount.setMakeDate(date);
			//获得会计时间
			try {
				realAccount.setPeriodId(billMainService.findPeriodByTime(new Date()).getPeriodId());
			}catch (Exception e){
				e.printStackTrace();
			}
			//制单状态
			realAccount.setCosBillNumStatus("S");
			realAccount.setBillMode("B");
			realAccount.setBillType("材料");
			
			//会计期间
			if(billMain.getPeriod()!=null){
				realAccount.setPeriodId(billMain.getPeriod().getPeriodId());
			}
			
			realAccount.setMpsPlanId(billMain.getMpsPlanId());
			realAccount.setOrderId(billMain.getOrderId());
			
			double itemSum = 0;
			double prodQty = 0;
			double bSum = 0;
			double lSum = 0;
			int bNum = 1;
			int lNum = 1;
			
			if(billDetailMapper.get(billMain.getId())!=null){
				prodQty = billDetailMapper.getQty(billMain.getId());
				itemSum = billDetailMapper.getSum(billMain.getId());
			}

			
			CosBillRule cosBillRule = new CosBillRule();
			cosBillRule.setCorType(billMain.getIo().getIoType());
			cosBillRule.setRuleType("Y");
			List<CosBillRule> list = cosBillRuleService.findList(cosBillRule);
			cosBillRule.setBlFlag("B");
			List<CosBillRule> listb = cosBillRuleService.findList(cosBillRule);
			cosBillRule.setBlFlag("L");
			List<CosBillRule> listl = cosBillRuleService.findList(cosBillRule);
			
			if(listb.size()>0){
				bNum = listb.size();
				bSum = itemSum/bNum;
			}else{
				addMessage(redirectAttributes, "未找到对应的借方制单规则,制单失败！");
				//redirectAttributes.addFlashAttribute("alertMsg", "未找到对应的科目或核算对象！");
				return "redirect:"+Global.getAdminPath()+"/cosbillrecord/cosBillRecord/listMaking?repage";
			}
			
			if(listl.size()>0){
				lNum = listl.size();
				lSum = itemSum/lNum;
			}else{
				addMessage(redirectAttributes, "未找到对应的贷方制单规则,制单失败！");
				//redirectAttributes.addFlashAttribute("alertMsg", "未找到对应的科目或核算对象！");
				return "redirect:"+Global.getAdminPath()+"/cosbillrecord/cosBillRecord/listMaking?repage";
			}
			
			if(list.size()>0){
				int recNo = 1;
				for (CosBillRule coBillr : list){
					RealAccountDetail realAccountDetail = new RealAccountDetail();
					if(coBillr.getItemNotes()!=null){
						//规则为固定科目值时获取科目
						if("I".equals(coBillr.getItemRule())){
							CosItem cosItem = cosItemService.get(coBillr.getItemNotes());
							if(cosItem!=null){
								realAccountDetail.setBlflag(coBillr.getBlFlag());
								realAccountDetail.setItemId(cosItem);
								realAccountDetail.setItemName(cosItem.getItemName());
								if(coBillr.getBlFlag().equals("B")){
									realAccountDetail.setItemSum(bSum);
								}
								if(coBillr.getBlFlag().equals("L")){
									realAccountDetail.setItemSum(lSum);
								}
							}else{
								addMessage(redirectAttributes, "规则中未找到对应科目,制单失败！");
								//redirectAttributes.addFlashAttribute("alertMsg", "规则中未找到对应科目！");
								return "redirect:"+Global.getAdminPath()+"/cosbillrecord/cosBillRecord/listMaking?repage";
							}
						}else{
							addMessage(redirectAttributes, "科目计算规则设置错误,制单失败！");
							//redirectAttributes.addFlashAttribute("alertMsg", "科目计算规则设置错误！");
							return "redirect:"+Global.getAdminPath()+"/cosbillrecord/cosBillRecord/listMaking?repage";
						}
					}else{
						addMessage(redirectAttributes, "科目计算规则内容无效,制单失败！");
						//redirectAttributes.addFlashAttribute("alertMsg", "规则中未找到对应科目！");
						return "redirect:"+Global.getAdminPath()+"/cosbillrecord/cosBillRecord/listMaking?repage";
					}
					if(coBillr.getResNotes()!=null){
						if("I".equals(coBillr.getProdRule())){
							CosProdObject cosProdObject = cosProdObjectService.get(coBillr.getResNotes());
							if(cosProdObject!=null){
								realAccountDetail.setCosProdObject(cosProdObject);
								realAccountDetail.setProdName(cosProdObject.getProdName());
								realAccountDetail.setProdSpec(cosProdObject.getSpecModel());
								realAccountDetail.setProdUnit(cosProdObject.getUnit());
							}else{
								addMessage(redirectAttributes, "规则中未找到对应核算对象,制单失败！");
								//redirectAttributes.addFlashAttribute("alertMsg", "核算对象计算规则设置错误！");
								return "redirect:"+Global.getAdminPath()+"/cosbillrecord/cosBillRecord/listMaking?repage";
							}
						}else{
							addMessage(redirectAttributes, "核算对象计算规则设置错误,制单失败！");
							//redirectAttributes.addFlashAttribute("alertMsg", "核算对象计算规则设置错误！");
							return "redirect:"+Global.getAdminPath()+"/cosbillrecord/cosBillRecord/listMaking?repage";
						}
					}else{
						addMessage(redirectAttributes, "核算对象计算内容无效,制单失败！");
						//redirectAttributes.addFlashAttribute("alertMsg", "核算对象计算规则设置错误！");
						return "redirect:"+Global.getAdminPath()+"/cosbillrecord/cosBillRecord/listMaking?repage";
					}
					
					realAccountDetail.setProdQty(prodQty);
					realAccountDetail.setRecNo(recNo);
					realAccountDetail.setId("");
					recNo++;
					
					realAccountDetailList.add(realAccountDetail);
				}
			}else{
				addMessage(redirectAttributes, "未找到对应的制单规则,制单失败！");
				//redirectAttributes.addFlashAttribute("alertMsg", "未找到对应的科目或核算对象！");
				return "redirect:"+Global.getAdminPath()+"/cosbillrecord/cosBillRecord/listMaking?repage";
			}
			
			
			realAccount.setRealAccountDetailList(realAccountDetailList);
			realAccountService.save(realAccount);
			
			//更新材料单据稽核表
			cosBillRecord.setCosBillNumStatus("B");
			cosBillRecord.setCosBillNum(billNum);
			cosBillRecordService.save(cosBillRecord);
			
			addMessage(redirectAttributes, "材料单据制单成功");
			return "redirect:"+Global.getAdminPath()+"/cosbillrecord/cosBillRecord/listMaking?repage";
		}
		else{
			addMessage(redirectAttributes, "未找到对应的科目或核算对象,制单失败！");
			//redirectAttributes.addFlashAttribute("alertMsg", "未找到对应的科目或核算对象！");
			return "redirect:"+Global.getAdminPath()+"/cosbillrecord/cosBillRecord/listMaking?repage";
		}
	}

	
	/**
	 * 删除材料单据稽核
	 */
	@ResponseBody
	@RequiresPermissions("cosbillrecord:cosBillRecord:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(CosBillRecord cosBillRecord, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		cosBillRecordService.delete(cosBillRecord);
		j.setMsg("删除材料单据稽核成功");
		return j;
	}
	
	/**
	 * 批量删除材料单据稽核
	 */
	@ResponseBody
	@RequiresPermissions("cosbillrecord:cosBillRecord:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			cosBillRecordService.delete(cosBillRecordService.get(id));
		}
		j.setMsg("删除材料单据稽核成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("cosbillrecord:cosBillRecord:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(CosBillRecord cosBillRecord, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "材料单据稽核"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<CosBillRecord> page = cosBillRecordService.findPage(new Page<CosBillRecord>(request, response, -1), cosBillRecord);
    		new ExportExcel("材料单据稽核", CosBillRecord.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出材料单据稽核记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("cosbillrecord:cosBillRecord:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<CosBillRecord> list = ei.getDataList(CosBillRecord.class);
			for (CosBillRecord cosBillRecord : list){
				try{
					cosBillRecordService.save(cosBillRecord);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条材料单据稽核记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条材料单据稽核记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入材料单据稽核失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/cosbillrecord/cosBillRecord/?repage";
    }
	
	/**
	 * 下载导入材料单据稽核数据模板
	 */
	@RequiresPermissions("cosbillrecord:cosBillRecord:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "材料单据稽核数据导入模板.xlsx";
    		List<CosBillRecord> list = Lists.newArrayList(); 
    		new ExportExcel("材料单据稽核数据", CosBillRecord.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/cosbillrecord/cosBillRecord/?repage";
    }

}