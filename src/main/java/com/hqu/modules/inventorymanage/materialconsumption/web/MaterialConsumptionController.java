package com.hqu.modules.inventorymanage.materialconsumption.web;

import java.util.ArrayList;
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
import com.hqu.modules.inventorymanage.billmain.entity.BillDetail;
import com.hqu.modules.inventorymanage.billmain.entity.BillMain;
import com.hqu.modules.inventorymanage.invaccount.entity.InvAccount;
import com.hqu.modules.inventorymanage.outsourcingoutput.service.BillMainOutsourcingService;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.modules.sys.utils.UserUtils;
@Controller
@RequestMapping(value = "${adminPath}/materialconsumption/MaterialConsumption")
public class MaterialConsumptionController extends BaseController{
	@Autowired
	private MaterialConsumptionService materialConsumptionService;
	
	@RequestMapping(value = {"list", ""})
	public String list(String flag, Model model) {
		model.addAttribute("flag",flag);
		return "inventorymanage/materialconsumption/materialConsumption";
	}
	
	/*
	 * 获取材料消耗汇总数据
	 */
	@ResponseBody
	@RequestMapping(value = "data")
	public Map<String, Object> data(BillMain billMain,BillDetail billDetail, HttpServletRequest request, HttpServletResponse response,Model model) {
		Page<BillMain> page = materialConsumptionService.findPage1(new Page<BillMain>(request, response), billMain,billDetail); 
		return getBootstrapData(page);
	}
	
	/*
	 * 获取材料消耗明细数据
	 */
	@ResponseBody
	@RequestMapping(value = "data2")
	public Map<String, Object> data2(BillMain billMain,BillDetail billDetail, HttpServletRequest request, HttpServletResponse response,Model model) {
		Page<MaterialConsumption> page = materialConsumptionService.findPage2(new Page<BillMain>(request, response), billMain,billDetail); 
		return getBootstrapData(page);
	}
}
