/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.costmanage.settleaccount.web;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hqu.modules.basedata.period.entity.Period;
import com.hqu.modules.basedata.period.mapper.PeriodMapper;
import com.hqu.modules.costmanage.cosbillrecord.entity.CosBillRecord;
import com.hqu.modules.costmanage.cosbillrecord.service.CosBillRecordService;
import com.hqu.modules.costmanage.cospersonother.entity.CosPersonOther;
import com.hqu.modules.costmanage.cospersonother.service.CosPersonOtherService;
import com.hqu.modules.costmanage.personwage.entity.PersonWage;
import com.hqu.modules.costmanage.personwage.service.PersonWageService;
import com.hqu.modules.costmanage.realaccount.entity.RealAccount;
import com.hqu.modules.costmanage.realaccount.service.RealAccountService;
import com.hqu.modules.costmanage.settleaccount.entity.SettleAccount;
import com.jeeplus.common.config.Global;
import com.jeeplus.core.web.BaseController;

/**
 * 月末核算Controller
 * @author zz
 * @version 2018-09-09
 */
@Controller
@RequestMapping(value = "${adminPath}/settleaccount/settleAccount")
public class SettleAccountController extends BaseController {

	@Autowired
	private PeriodMapper periodMapper;
	@Autowired
	private RealAccountService realAccountService;
	@Autowired
	private CosBillRecordService cosBillRecordService;
	@Autowired
	private PersonWageService personWageService;
	@Autowired
	private CosPersonOtherService cosPersonOtherService;
	
	/**
	 * 查看月末核算
	 */
	//@RequiresPermissions(value={"billingrule:cosBillRule:view","billingrule:cosBillRule:add","billingrule:cosBillRule:edit"},logical=Logical.OR)
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
		return "costmanage/settleaccount/settleAccount";
	}

	/**
	 * 保存结账
	 */
	//@RequiresPermissions(value={"billingrule:cosBillRule:add","billingrule:cosBillRule:edit"},logical=Logical.OR)
	@RequestMapping(value = "saveAccounting")
	public String save(SettleAccount settleAccount, Model model, RedirectAttributes redirectAttributes) throws Exception{
		RealAccount reali = new RealAccount();
		reali.setBillType("初始");
		reali.setCosBillNumStatus("S");
		reali.setPeriodId(settleAccount.getPeriodId().getPeriodId());
		List<RealAccount> liReali = realAccountService.findList(reali);
		if(liReali.size()>0){
			addMessage(redirectAttributes, "检查成本凭证初始化中存在未稽核单据，结账失败！");
			return "redirect:"+Global.getAdminPath()+"/settleaccount/settleAccount/?repage";
		}
		CosBillRecord cosBillR = new CosBillRecord();
		cosBillR.setCosBillNumStatus("A");
		List<CosBillRecord> liCosb = cosBillRecordService.findList(cosBillR);
		if(liCosb.size()>0){
			addMessage(redirectAttributes, "检查材料单据中存在未制单单据，结账失败！");
			return "redirect:"+Global.getAdminPath()+"/settleaccount/settleAccount/?repage";
		}
		RealAccount reala = new RealAccount();
		reala.setBillType("材料");
		reala.setCosBillNumStatus("S");
		reala.setPeriodId(settleAccount.getPeriodId().getPeriodId());
		List<RealAccount> liReala = realAccountService.findList(reala);
		if(liReala.size()>0){
			addMessage(redirectAttributes, "检查材料凭证中存在未提交单据，结账失败！");
			return "redirect:"+Global.getAdminPath()+"/settleaccount/settleAccount/?repage";
		}
		RealAccount realb = new RealAccount();
		realb.setBillType("材料");
		realb.setCosBillNumStatus("A");
		realb.setPeriodId(settleAccount.getPeriodId().getPeriodId());
		List<RealAccount> liRealb = realAccountService.findList(realb);
		if(liRealb.size()>0){
			addMessage(redirectAttributes, "检查材料凭证中存在未稽核单据，结账失败！");
			return "redirect:"+Global.getAdminPath()+"/settleaccount/settleAccount/?repage";
		}
		CosPersonOther personoa = new CosPersonOther();
		personoa.setBillStatus("A");
		List<CosPersonOther> liPersonoa = cosPersonOtherService.findList(personoa);
		if(liPersonoa.size()>0){
			addMessage(redirectAttributes, "检查其他工资单据中存在未稽核单据，结账失败！");
			return "redirect:"+Global.getAdminPath()+"/settleaccount/settleAccount/?repage";
		}
		CosPersonOther personob = new CosPersonOther();
		personob.setBillStatus("B");
		List<CosPersonOther> liPersonob = cosPersonOtherService.findList(personob);
		if(liPersonob.size()>0){
			addMessage(redirectAttributes, "检查其他工资单据中存在未分配单据，结账失败！");
			return "redirect:"+Global.getAdminPath()+"/settleaccount/settleAccount/?repage";
		}
		PersonWage personwa = new PersonWage();
		personwa.setBillStatus("A");
		personwa.setPeriodId(settleAccount.getPeriodId().getPeriodId());
		List<PersonWage> liPersonwa = personWageService.findList(personwa);
		if(liPersonwa.size()>0){
			addMessage(redirectAttributes, "检查人工工资单据中存在未稽核单据，结账失败！");
			return "redirect:"+Global.getAdminPath()+"/settleaccount/settleAccount/?repage";
		}
		PersonWage personwb = new PersonWage();
		personwb.setBillStatus("B");
		personwb.setPeriodId(settleAccount.getPeriodId().getPeriodId());
		List<PersonWage> liPersonwb = personWageService.findList(personwb);
		if(liPersonwb.size()>0){
			addMessage(redirectAttributes, "检查人工工资单据中存在未制单单据，结账失败！");
			return "redirect:"+Global.getAdminPath()+"/settleaccount/settleAccount/?repage";
		}
		RealAccount realpa = new RealAccount();
		realpa.setBillType("人工");
		realpa.setCosBillNumStatus("S");
		realpa.setPeriodId(settleAccount.getPeriodId().getPeriodId());
		List<RealAccount> liRealpa = realAccountService.findList(realpa);
		if(liRealpa.size()>0){
			addMessage(redirectAttributes, "检查人工成本凭证中存在未提交单据，结账失败！");
			return "redirect:"+Global.getAdminPath()+"/settleaccount/settleAccount/?repage";
		}
		RealAccount realpb = new RealAccount();
		realpb.setBillType("人工");
		realpb.setCosBillNumStatus("A");
		realpb.setPeriodId(settleAccount.getPeriodId().getPeriodId());
		List<RealAccount> liRealpb = realAccountService.findList(realpb);
		if(liRealpb.size()>0){
			addMessage(redirectAttributes, "检查人工成本凭证中存在未稽核单据，结账失败！");
			return "redirect:"+Global.getAdminPath()+"/settleaccount/settleAccount/?repage";
		}
		
		Period peri = new Period();
		if(settleAccount.getPeriodId()!=null){
			peri.setPeriodId(settleAccount.getPeriodId().getPeriodId());
			peri = periodMapper.findList(peri).get(0);
			peri.setCloseFlag("Y");
			periodMapper.update(peri);
			addMessage(redirectAttributes, "保存结账成功");
		}else{
			addMessage(redirectAttributes, "保存结账失败！");
		}
		
		return "redirect:"+Global.getAdminPath()+"/settleaccount/settleAccount/?repage";
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