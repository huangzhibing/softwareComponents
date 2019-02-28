package com.hqu.modules.inventorymanage.capitaloccupation.web;
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
import com.hqu.modules.inventorymanage.warehouse.service.WareHouseService;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.modules.sys.utils.UserUtils;
@Controller
@RequestMapping(value = "${adminPath}/capitaloccupation/capitaloccupation")
public class capitaloccupationController extends BaseController{
	@Autowired
	InvAccountService  invAccountService;
	@Autowired
	WareHouseService wareHouseService;
	@Autowired
	BillMainService billMainService;
	
	@RequestMapping(value = {"list", ""})
	public String list(String flag, Model model) {
		model.addAttribute("flag",flag);
		return "inventorymanage/capitaloccupation/capitaloccupation";
	}
	
	/*
	 * 获取最新核算期的库存数据
	 */
	@ResponseBody
	@RequestMapping(value = "data")
	public Map<String, Object> data(InvAccount invAccount, HttpServletRequest request, HttpServletResponse response,Model model) {
		Date date = new Date();
	    String period = billMainService.findPeriodByTime(date).getPeriodId();
	    invAccount.setYear(period.substring(0,4));
	    invAccount.setPeriod(period.substring(4,6));
		Page<InvAccount> page = invAccountService.findPage(new Page<InvAccount>(request, response), invAccount); 
		sum(page);
		return getBootstrapData(page);
	}
	
	/*
	 * 柱状图
	 */
	@RequestMapping(value = "bar")
	public String bar() {
		return "inventorymanage/capitaloccupation/barGraph";
	}
	
	/*
	 * 饼图
	 */
	@RequestMapping(value = "pie")
	public String pie() {
		return "inventorymanage/capitaloccupation/pieGraph";
	}
	
	/*
	 * 合并仓库相同的数据
	 */
	private void sum(Page<InvAccount> page){
		List<InvAccount> invs=page.getList();
		for(int i=0;i<invs.size();i++) {
			for(int j=i+1;j<invs.size();j++) {
				if(invs.get(i).getWare().getWareID().equals(invs.get(j).getWare().getWareID())) {
					invs.get(i).setNowSum(invs.get(i).getNowSum()+invs.get(j).getNowSum());
					invs.remove(j--);
				}
			}
		}
	}
}
