/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.costmanage.personwage.web;

import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import com.hqu.modules.basedata.period.entity.Period;
import com.hqu.modules.basedata.period.mapper.PeriodMapper;
import com.hqu.modules.basedata.product.entity.Product;
import com.hqu.modules.basedata.product.service.ProductService;
import com.hqu.modules.basedata.routing.entity.Routing;
import com.hqu.modules.basedata.routing.service.RoutingService;
import com.hqu.modules.basedata.team.entity.Team;
import com.hqu.modules.basedata.team.service.TeamService;
import com.hqu.modules.costmanage.billingrule.entity.CosBillRule;
import com.hqu.modules.costmanage.billingrule.service.CosBillRuleService;
import com.hqu.modules.costmanage.cosbillrecord.entity.CosBillRecord;
import com.hqu.modules.costmanage.cosbillrecord.service.CosBillRecordService;
import com.hqu.modules.costmanage.cositem.entity.CosItem;
import com.hqu.modules.costmanage.cositem.service.CosItemService;

import com.hqu.modules.costmanage.cosprodobject.entity.CosProdObject;
import com.hqu.modules.costmanage.cosprodobject.service.CosProdObjectService;
import com.hqu.modules.costmanage.personwage.mapper.PersonWageMapper;
import com.hqu.modules.costmanage.realaccount.entity.RealAccount;
import com.hqu.modules.costmanage.realaccount.entity.RealAccountDetail;
import com.hqu.modules.costmanage.realaccount.mapper.RealAccountDetailMapper;
import com.hqu.modules.costmanage.realaccount.mapper.RealAccountMapper;
import com.hqu.modules.costmanage.realaccount.service.RealAccountService;
import com.hqu.modules.costmanage.settleaccount.entity.SettleAccount;
import com.hqu.modules.inventorymanage.billmain.service.BillMainService;
import com.hqu.modules.workshopmanage.processroutine.entity.ProcessRoutine;
import com.hqu.modules.workshopmanage.processroutine.mapper.ProcessRoutineMapper;
import com.hqu.modules.workshopmanage.processroutine.service.ProcessRoutineService;
import com.hqu.modules.workshopmanage.processroutinedetail.entity.ProcessRoutineDetail;
import com.hqu.modules.workshopmanage.processroutinedetail.mapper.ProcessRoutineDetailMapper;
import com.hqu.modules.workshopmanage.processroutinedetail.service.ProcessRoutineDetailService;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.utils.UserUtils;
import com.sun.prism.impl.BaseMesh;
import org.apache.ibatis.jdbc.Null;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jca.cci.RecordTypeNotSupportedException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.excel.ExportExcel;
import com.jeeplus.common.utils.excel.ImportExcel;
import com.hqu.modules.costmanage.personwage.entity.PersonWage;
import com.hqu.modules.costmanage.personwage.service.PersonWageService;

/**
 * 人工工资Controller
 * @author 黄志兵
 * @version 2018-09-01
 */
@Controller
@RequestMapping(value = "${adminPath}/personwage/personWage")
public class PersonWageController extends BaseController {
	@Autowired
	private PeriodMapper periodMapper;
	@Autowired
	private PersonWageMapper personWageMapper;
	@Autowired
	private RoutingService routingService;
	@Autowired
	private ProductService productService;
	@Autowired
	PersonWageService personWageService;
	@Autowired
	TeamService teamService;
	@Autowired
	ProcessRoutineMapper processRoutineMapper;
	@Autowired
	private CosBillRuleService cosBillRuleService;
	@Autowired
	private PersonWageTwoController personWageTwoController;
	@Autowired
	private CosItemService cosItemService;
	@Autowired
	private CosProdObjectService cosProdObjectService;
	@Autowired
	RealAccountService realAccountService;
	@Autowired
	RealAccountDetailMapper realAccountDetailMapper;


	/**
	 * 人工工资生成界面
	 */
	@RequiresPermissions("personwage:personWage:list")
	@RequestMapping(value = "")
	public String form(SettleAccount settleAccount, Model model) {
		Period per = new Period();

		String periodId = periodMapper.getPeriodByClose();
		if(periodId == null || periodId == ""){
			Calendar cal = Calendar.getInstance();
			Date date = cal.getTime();
			per.setCurrentDate(date);
			per = periodMapper.getCurrentPeriod(per);
			settleAccount.setPeriodId(per);
		}else{
			String nextPeriodId = getNextPeriod(periodId);
			per.setPeriodId(nextPeriodId);
			settleAccount.setPeriodId(per);
		}

		model.addAttribute("settleAccount", settleAccount);
		return "costmanage/personwage/buildPersonWage";
	}

	/**
	 * 自动生成人工工资成本
	 */
	//@RequiresPermissions(value={"billingrule:cosBillRule:add","billingrule:cosBillRule:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(SettleAccount settleAccount, Model model, RedirectAttributes redirectAttributes) throws Exception{
		String period = settleAccount.getPeriodId().getPeriodId();
		Map<Object,Date> timeMap= periodMapper.getBeginEndPeriod(period);
		logger.debug("timeMap:"+timeMap);
		ProcessRoutine processRoutine = new ProcessRoutine();
		processRoutine.setBillState("C");
		processRoutine.setBeginPlanEDate(timeMap.get("period_begin"));
		processRoutine.setEndPlanEDate(timeMap.get("period_end"));
		List<ProcessRoutine> processRoutines = processRoutineMapper.findList(processRoutine);
		int flag = processRoutines.size();
		for(int i=0;i<processRoutines.size();i++){
			processRoutines.get(i).setPeriodId(period);
			if(personWageMapper.checkExist(processRoutines.get(i)) == null){
				//插入一条新数据
				flag--;
				PersonWage personWage = new PersonWage();
				personWage.setPeriodId(period);
				Routing routing = routingService.get(processRoutines.get(i).getRoutingCode());
				personWage.setRouting(routing);
				if(StringUtils.isNotBlank(processRoutines.get(i).getTeamCode())) {
					Team team = teamService.get(processRoutines.get(i).getTeamCode());
					personWage.setTeam(team);
				}
				Product product = productService.get(processRoutines.get(i).getProdCode());
				personWage.setItemCode(product);
				personWage.setItemQty(processRoutines.get(i).getRealQty());
				personWage.setItemUnit(routing.getUnitWage());
				personWage.setBillStatus("A");
				personWage.setBillMode("A");
				personWage.setMakeId(UserUtils.getUser().getNo());
				Date date = new Date();
				personWage.setMakeDate(date);
				personWage.setMakeName(UserUtils.getUser().getName());
				//工资单据号
				String billNum = personWageService.getBillNum();
				personWage.setBillNum(billNum);

				/**
				 * update by @zhangzheng
				 */
				//获取作业计划编号
				String processBillNo = processRoutines.get(i).getProcessBillNo();
				personWage.setProcessBillNo(processBillNo);
				//获取内部订单号
				personWage.setOrderId(personWageMapper.getOrderId(processBillNo));

				personWageService.save(personWage);
				insertRealAccount(personWage,redirectAttributes);
				//更新加工路线单中的cosBillNum
				processRoutines.get(i).setCosBillNum(billNum);
				processRoutineMapper.update(processRoutines.get(i));
			}else{
				//已经存在就累加工作量
				PersonWage existPersonWage = personWageMapper.checkExist(processRoutines.get(i));
				if((processRoutines.get(i).getCosBillNum() == ""||processRoutines.get(i).getCosBillNum() == null) && existPersonWage.getItemUnit() != null) {
					flag--;
					//修改第二子表的工作量
					Double existQty = existPersonWage.getItemQty();
					existPersonWage.setItemQty(existQty + processRoutines.get(i).getRealQty());
					personWageService.save(existPersonWage);
					//修改第一个子表的金额
					Double itemSum = (existQty + processRoutines.get(i).getRealQty())*(existPersonWage.getItemUnit());
					RealAccount realAccount = new RealAccount();
					realAccount.setOriBillId(existPersonWage.getBillNum());
					if(realAccountService.findList(realAccount).size() != 0) {
						List<RealAccount> realAccountList = realAccountService.findList(realAccount);
						RealAccount realAccount1 = realAccountService.get(realAccountList.get(0).getId());
						for (int n = 0; n < realAccount1.getRealAccountDetailList().size(); n++) {
							realAccount1.getRealAccountDetailList().get(n).setItemSum(itemSum);
							realAccountDetailMapper.update(realAccount1.getRealAccountDetailList().get(n));
						}
					}
					//更新cosbillnum
					processRoutines.get(i).setCosBillNum(existPersonWage.getBillNum());
					processRoutineMapper.update(processRoutines.get(i));
				}
			}
		}
		if(flag == processRoutines.size()) {
			addMessage(redirectAttributes, "没有新的加工路线单，制单失败");
			return "redirect:"+Global.getAdminPath()+"/personwage/personWage/?repage";
		}else {
			addMessage(redirectAttributes,"人工成本制单成功");
			return "redirect:" + Global.getAdminPath() + "/personwage/personWage/?repage";
		}
	}

	/**
	 * 向realaccount插入数据
	 * @return
	 */
	public String insertRealAccount(PersonWage personWage,RedirectAttributes redirectAttributes){

//		if(personWage.getWageUnit() == null || personWage.getWageUnit() == ""){
//			addMessage(redirectAttributes, "该单据还未进行分配计算,制单失败！");
//			//redirectAttributes.addFlashAttribute("alertMsg", "未找到对应的科目或核算对象！");
//			return "redirect:"+Global.getAdminPath()+"/personwage/personWage/?repage";
//		}

		RealAccount realAccount = personWageTwoController.getRealAccount(personWage);
		List<RealAccountDetail> realAccountDetailList = new ArrayList<RealAccountDetail>();

		double itemSum = 0;
		double prodQty = 0;
		double bSum = 0;
		double lSum = 0;
		int bNum = 1;
		int lNum = 1;

		double wageUnit = 0;
		double itemUnit = 0;

		if(personWage.getItemQty() != null )
			prodQty =personWage.getItemQty();
		if(personWage.getWageUnit() != null && personWage.getWageUnit() != "")
			wageUnit = Double.parseDouble(personWage.getWageUnit());
		if(personWage.getItemUnit() != null)
			itemUnit =personWage.getItemUnit();
//
		itemSum = (wageUnit+itemUnit)*prodQty;

		CosBillRule cosBillRule = new CosBillRule();
		cosBillRule.setCorType("RG01");
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
			RealAccountDetail realAccountDetail = new RealAccountDetail();
			realAccountDetail.setBlflag("B");
			realAccountDetail.setItemSum(itemSum);
			realAccountDetail.setRecNo(1);
			realAccountDetail.setId("");
			realAccountDetailList.add(realAccountDetail);
		}

		if(listl.size()>0){
			lNum = listl.size();
			lSum = itemSum/lNum;
		}else{
			RealAccountDetail realAccountDetail = new RealAccountDetail();
			realAccountDetail.setBlflag("L");
			realAccountDetail.setItemSum(itemSum);
			realAccountDetail.setRecNo(2);
			realAccountDetail.setId("");
			realAccountDetailList.add(realAccountDetail);
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
							return "redirect:"+Global.getAdminPath()+"/personwage/personWage/?repage";
						}
					}else{
						addMessage(redirectAttributes, "科目计算规则设置错误,制单失败！");
						//redirectAttributes.addFlashAttribute("alertMsg", "科目计算规则设置错误！");
						return "redirect:"+Global.getAdminPath()+"/personwage/personWage/?repage";
					}
				}else{
					addMessage(redirectAttributes, "科目计算规则内容无效,制单失败！");
					//redirectAttributes.addFlashAttribute("alertMsg", "规则中未找到对应科目！");
					return "redirect:"+Global.getAdminPath()+"/personwage/personWage/?repage";
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
							return "redirect:"+Global.getAdminPath()+"/personwage/personWage/?repage";
						}
					}else{
						addMessage(redirectAttributes, "核算对象计算规则设置错误,制单失败！");
						//redirectAttributes.addFlashAttribute("alertMsg", "核算对象计算规则设置错误！");
						return "redirect:"+Global.getAdminPath()+"/personwage/personWage/?repage";
					}
				}else{
					addMessage(redirectAttributes, "核算对象计算内容无效,制单失败！");
					//redirectAttributes.addFlashAttribute("alertMsg", "核算对象计算规则设置错误！");
					return "redirect:"+Global.getAdminPath()+"/personwage/personWage/?repage";
				}

				realAccountDetail.setProdQty(prodQty);
				realAccountDetail.setRecNo(recNo);
				realAccountDetail.setId("");
				recNo++;

				realAccountDetailList.add(realAccountDetail);
			}
		}else{

		}

		//保存凭证
		realAccount.setRealAccountDetailList(realAccountDetailList);
		realAccount.setOriBillId(personWage.getBillNum());
		realAccountService.save(realAccount);

		//已制凭证
		personWage.setBillStatus("C");
		personWage.setCosBillNum(realAccount.getBillNum());
		personWageService.save(personWage);

		addMessage(redirectAttributes,"人工成本制单成功");
		return "redirect:"+Global.getAdminPath()+"/personwage/personWage/?repage";
	}

	public String getNextPeriod(String periodId)
	{
		String resualt = null;
		int year, month;
		if (periodId.length() != 6) {
			return null;
		}
		year = Integer.parseInt(periodId.substring(0, 4));
		month = Integer.parseInt(periodId.substring(4, 6));
		if (month == 12) {
			month = 1;
			year++;
		} else {
			month++;
		}
		resualt =
				String.valueOf(year)
						+ String.valueOf(100 + month).substring(1, 3);
		return (resualt == null ? "" : resualt).trim();

	}

}