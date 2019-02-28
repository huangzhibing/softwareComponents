package com.hqu.modules.inventorymanage.shortagealarm.web;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hqu.modules.inventorymanage.materialconsumption.entity.MaterialConsumption;
import com.hqu.modules.inventorymanage.materialconsumption.service.MaterialConsumptionService;
import com.hqu.modules.basedata.item.service.ItemService;
import com.hqu.modules.inventorymanage.billmain.entity.BillDetail;
import com.hqu.modules.inventorymanage.billmain.entity.BillMain;
import com.hqu.modules.inventorymanage.billmain.service.BillMainService;
import com.hqu.modules.inventorymanage.invaccount.entity.InvAccount;
import com.hqu.modules.inventorymanage.invaccount.service.InvAccountService;
import com.hqu.modules.inventorymanage.outsourcingoutput.service.BillMainOutsourcingService;
import com.hqu.modules.inventorymanage.shortagealarm.service.ShortageAlarnService;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.modules.sys.utils.UserUtils;
@Controller
@RequestMapping(value = "${adminPath}/shortagealarm/shortageAlarm")
public class ShortageAlarmController extends BaseController{
	@Autowired
	ShortageAlarnService shortageAlarnService;
	@Autowired
	BillMainService billMainService;
	
	@RequestMapping(value = {"list", ""})
	public String list(String flag, Model model) {
		model.addAttribute("flag",flag);
		return "inventorymanage/shortagealarm/shortagealarm";
	}
	
	/*
	 * 获取最新核算期的仓库数据
	 */
	@ResponseBody
	@RequestMapping(value = "data")
	public Map<String, Object> data(InvAccount invAccount, HttpServletRequest request, HttpServletResponse response,Model model) {
		Date date = new Date();
	    String period = billMainService.findPeriodByTime(date).getPeriodId();
	    invAccount.setYear(period.substring(0,4));
	    invAccount.setPeriod(period.substring(4,6));
	    logger.debug("年:"+period.substring(0,4));
	    logger.debug("月:"+period.substring(4,6));
		Page<InvAccount> page = shortageAlarnService.findPage(new Page<InvAccount>(request, response), invAccount); 
		return getBootstrapData(page);
	}
	
}
