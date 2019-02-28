package com.hqu.modules.inventorymanage.plananduse.web;
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
import com.hqu.modules.inventorymanage.plananduse.entity.PlanAndUse;
import com.hqu.modules.inventorymanage.plananduse.service.PlanAndUseService;
import com.hqu.modules.inventorymanage.shortagealarm.service.ShortageAlarnService;
import com.hqu.modules.inventorymanage.warehouse.service.WareHouseService;
import com.hqu.modules.purchasemanage.purplan.entity.PurPlanDetail;
import com.hqu.modules.purchasemanage.purplan.entity.PurPlanMain;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.modules.sys.utils.UserUtils;
@Controller
@RequestMapping(value = "${adminPath}/plananduse/plananduse")
public class PlanAndUseController extends BaseController{
	@Autowired
	InvAccountService  invAccountService;
	@Autowired
	WareHouseService wareHouseService;
	@Autowired
	BillMainService billMainService;
	@Autowired
	MaterialConsumptionService materialConsumptionService;
	@Autowired
	PlanAndUseService planAndUseService;
	@RequestMapping(value = {"list", ""})
	public String list(String flag, Model model) {
		model.addAttribute("flag",flag);
		return "inventorymanage/plananduse/planAndUse";
	}
	
	@ResponseBody
	@RequestMapping(value = "data")
	public Map<String, Object> data(BillMain billMain,BillDetail billDetail,PurPlanMain purPlanMain,PurPlanDetail purPlanDetail,HttpServletRequest request, HttpServletResponse response,Model model) {
		Page<MaterialConsumption> page1 = materialConsumptionService.findPage2(new Page<BillMain>(request, response), billMain,billDetail); 
		Page<PlanAndUse> page2 =planAndUseService.findPage(new Page<PurPlanMain>(request, response),purPlanMain,purPlanDetail);
		return getBootstrapData(sun(page1,page2));
	}
	
	/*
	 * 按部门和物料合并采购单和外购件出库单的数据
	 */
	private Page<PlanAndUse> sun(Page<MaterialConsumption> page1,Page<PlanAndUse> page2){
		List<MaterialConsumption> list1=page1.getList();
		List<PlanAndUse> list2=page2.getList();
		Page<PlanAndUse> myPage=new Page<PlanAndUse>();
		List<PlanAndUse> myList=myPage.getList();
		logger.debug("第一轮");
		//第一轮：合并同部门同物料的数据项到mylist
		for(int i=0;i<list1.size();i++) {
			for(int j=0;j<list2.size();j++) {
				if(list1.get(i).getDeptCode().equals(list2.get(j).getDeptCode())) {
					if(StringUtils.equals(list1.get(i).getItemName(),list2.get(j).getItemName())) {
						PlanAndUse planAndUse=new PlanAndUse();
						planAndUse.setDeptCode(list1.get(i).getDeptCode());
						planAndUse.setDeptName(list1.get(i).getDeptName());
						planAndUse.setItemCode(list1.get(j).getItemCode());
						planAndUse.setItemName(list1.get(i).getItemName());
						planAndUse.setItemSpec(list1.get(i).getItemSpec());
						planAndUse.setItemQty(list1.get(i).getItemQty());
						planAndUse.setItemSum(list1.get(i).getItemSum());
						planAndUse.setPlanQty(list2.get(j).getPlanQty());
						planAndUse.setPlanSum(list2.get(j).getPlanSum());
						myList.add(planAndUse);
						list1.remove(i--);
						list2.remove(j--);
						break;
					}
				}
			}
		}
		logger.debug("第二轮");
		//第二轮：将list1,list2加到mylist后面去中
		for(int i=0;i<list1.size();i++) {
			PlanAndUse planAndUse=new PlanAndUse();
			planAndUse.setDeptCode(list1.get(i).getDeptCode());
			planAndUse.setDeptName(list1.get(i).getDeptName());
			planAndUse.setItemCode(list1.get(i).getItemCode());
			planAndUse.setItemName(list1.get(i).getItemName());
			planAndUse.setItemSpec(list1.get(i).getItemSpec());
			planAndUse.setItemQty(list1.get(i).getItemQty());
			planAndUse.setItemSum(list1.get(i).getItemSum());
			myList.add(planAndUse);
		}
		logger.debug("测试"+list2.toString());
		myList.addAll(list2);
		return myPage;
	}
}
