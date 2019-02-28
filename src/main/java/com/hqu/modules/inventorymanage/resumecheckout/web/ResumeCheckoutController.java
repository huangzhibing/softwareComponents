package com.hqu.modules.inventorymanage.resumecheckout.web;

import java.util.Date;
import java.util.List;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hqu.modules.basedata.period.entity.Period;
import com.hqu.modules.basedata.period.service.PeriodService;
import com.hqu.modules.inventorymanage.billmain.entity.BillMain;
import com.hqu.modules.inventorymanage.billmain.service.BillMainService;
import com.hqu.modules.inventorymanage.invaccount.entity.InvAccount;
import com.hqu.modules.inventorymanage.invaccount.service.InvAccountService;
import com.hqu.modules.inventorymanage.resumecheckout.service.ResumeCheckoutService;
import com.hqu.modules.inventorymanage.warehouse.entity.WareHouse;
import com.hqu.modules.inventorymanage.warehouse.service.WareHouseService;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.core.web.BaseController;

/**
 * 恢复结账Controller
 * @author hzm
 * @version 2018-04-30
 */
@Controller			
@RequestMapping(value = "${adminPath}/resumecheckout/resumeCheckout")
public class ResumeCheckoutController extends BaseController{
	@Autowired
	private BillMainService billMainService;
	@Autowired
	private ResumeCheckoutService resumeCheckoutService;
	@Autowired
	private InvAccountService invAccountService;
	@Autowired
	private WareHouseService wareHouseService;
	
	@RequestMapping(value = {""})
	public String index(BillMain billMain) {
		Date date=new Date();
		 //添加日期
        billMain.setBillDate(date);
		return "inventorymanage/resumecheckout/resumeCheckout";
	}
	
	/*
	 * 根据时间获取核算期
	 */
	@ResponseBody
	@RequestMapping(value = {"resume"})
	public String resume(Date date) {
		logger.debug("date:"+date);
		return billMainService.findPeriodByTime(date).getPeriodId();
	}
	
	/*
	 * 恢复到上一个核算期状态
	 */
	@RequestMapping(value = {"resumeLast"})
	public String resumeLast(BillMain billMain,RedirectAttributes redirectAttributes) {
		try {
			List<String>  periods=resumeCheckoutService.findLastByDate(billMain.getBillDate());
			List<InvAccount> lacounts;
			InvAccount db=new InvAccount();
			InvAccount invAccount=new InvAccount();
			//遍历当前核算期的库存账
			invAccount.setWare(billMain.getWare());
			invAccount.setYear(billMain.getPeriod().getPeriodId().substring(0,4));
			invAccount.setPeriod(billMain.getPeriod().getPeriodId().substring(4,6));
			List<InvAccount> nacounts=invAccountService.findList(invAccount);
			logger.debug("库存帐数:"+nacounts.size());
			//删除当前核算期的库存帐记录记录
			for(InvAccount ainvAccount:nacounts) {
				invAccountService.delete(ainvAccount);
			}
			BillMain main=new BillMain();
			main.setWare(billMain.getWare());
			Period p=billMainService.findPeriodByTime(billMain.getBillDate());
			main.setPeriod(p);
			main.setBillFlag("T");
			List<BillMain> lm=billMainService.findList(main);
			for(BillMain b:billMainService.findList(main)) {
				b.setBillFlag("N");
				billMainService.save(b);
			}
			//回退仓库核算期
			WareHouse ware=wareHouseService.get(billMain.getWare());
			ware.setCurrPeriod(periods.get(1));
			wareHouseService.save(ware);
			addMessage(redirectAttributes, "恢复成功");
			return "redirect:"+Global.getAdminPath()+"/resumecheckout/resumeCheckout";
		}catch(Exception e) {
			e.printStackTrace();
			addMessage(redirectAttributes, "恢复失败");
			return "redirect:"+Global.getAdminPath()+"/resumecheckout/resumeCheckout";
		}
	}
}
