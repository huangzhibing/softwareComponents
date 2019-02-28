/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.purchasemanage.purplan.web;

import com.google.common.collect.Lists;
import com.hqu.modules.Common.service.CommonService;
import com.hqu.modules.purchasemanage.adtbilltype.entity.AdtBillType;
import com.hqu.modules.purchasemanage.adtdetail.entity.AdtDetail;
import com.hqu.modules.purchasemanage.adtdetail.service.AdtDetailService;
import com.hqu.modules.purchasemanage.purplan.entity.ItemExtend;
import com.hqu.modules.purchasemanage.purplan.entity.PurPlanDetail;
import com.hqu.modules.purchasemanage.purplan.entity.PurPlanMain;
import com.hqu.modules.purchasemanage.purplan.service.PurPlanMainService;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.excel.ExportExcel;
import com.jeeplus.common.utils.excel.ImportExcel;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.modules.act.service.ActTaskService;
import com.jeeplus.modules.sys.utils.UserUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 采购计划Controller
 * @author yangxianbang
 * @version 2018-05-09
 */
@Controller
@RequestMapping(value = "${adminPath}/purplan/purPlanMainDraft")
public class PurPlanMainDraftController extends BaseController {

	@Autowired
	private PurPlanMainService purPlanMainService;
	@Autowired
	private CommonService commonService;
	@Autowired
	private AdtDetailService adtDetailService;
	@Autowired
	private ActTaskService actTaskService;
	
	@ModelAttribute
	public PurPlanMain get(@RequestParam(required=false) String id) {
		PurPlanMain entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = purPlanMainService.get(id);
		}
		if (entity == null){
			entity = new PurPlanMain();
		}
		return entity;
	}
	
	/**
	 * 采购计划列表页面
	 */
	@RequiresPermissions("purplan:purPlanMainDraft:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "purchasemanage/purplan/purPlanMainDraftList";
	}
	
		/**
	 * 采购计划列表数据
	 */
	@ResponseBody
	@RequiresPermissions("purplan:purPlanMainDraft:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(PurPlanMain purPlanMain, HttpServletRequest request, HttpServletResponse response, Model model) {
		purPlanMain.setBillType("C");
		purPlanMain.setMakeEmpid(UserUtils.getUser());
		Page<PurPlanMain> page = purPlanMainService.findABPage(new Page<PurPlanMain>(request, response), purPlanMain);
		return getBootstrapData(page);
	}
	
	/**
	 * 采购计划列表数据
	 */
	@ResponseBody
	@RequiresPermissions(value= {"purplan:purPlanMainDraft:add","purplan:purPlanMainDraft:edit",})
	@RequestMapping(value = "itemsdata")
	public Map<String, Object> itemsdata(ItemExtend item, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<ItemExtend> page = purPlanMainService.findItemPage(new Page<ItemExtend>(request, response), item); 
		return getBootstrapData(page);
	}



	/**
	 * 查看，增加，编辑采购计划表单页面
	 */
	@RequiresPermissions(value={"purplan:purPlanMainDraft:view","purplan:purPlanMainDraft:add","purplan:purPlanMainDraft:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(PurPlanMain purPlanMain, Model model,@RequestParam(required=false) String detail) {
		model.addAttribute("purPlanMain", purPlanMain);
		if(StringUtils.isBlank(purPlanMain.getId())){//如果ID是空为添加
			model.addAttribute("isAdd", true);
			model.addAttribute("billNum",getBillNum());
			purPlanMain.setBillStateFlag("A");//设置单据状态为正在录入/修改
			purPlanMain.setMakeDate(new Date());
		}else if(detail==null) {
			model.addAttribute("isEdit", true);
		}
		if("B".equals(purPlanMain.getBillStateFlag())) {
			List<AdtDetail> adtDetails = adtDetailService.getHistoryDetails(purPlanMain.getBillNum(), "PLN001");
			if (adtDetails != null&&adtDetails.size()>1) {
				AdtDetail d=adtDetails.get(adtDetails.size() - 2);
				SimpleDateFormat dataFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				purPlanMain.setJustifyRemark(d.getJustifyPerson()+"("+dataFormat.format(d.getJustifyDate())+"):\n"+d.getJustifyRemark());  //设置审核人不通过的意见
			}
		}
		if("B".equals(purPlanMain.getBillStateFlag())){
			/*purPlanMain.getAct().setProcIns(actTaskService.getProcIns(purPlanMain.getAct().getProcInsId()));
			if(purPlanMain.getAct().getProcIns()!=null) {
				purPlanMain.getAct().setTaskDefKey(purPlanMain.getAct().getProcIns().getActivityId());
			}*/
			for(HashMap<String,String> map :actTaskService.todoList(purPlanMain.getAct())){
				if(map.get("task.processInstanceId").equals(purPlanMain.getAct().getProcInsId())){
					purPlanMain.getAct().setTaskDefKey(map.get("task.taskDefinitionKey"));
					purPlanMain.getAct().setTaskId(map.get("task.id"));
				}
			}
			return "purchasemanage/purplan/purPlanMainDraftReApplyForm";
		}

		return "purchasemanage/purplan/purPlanMainDraftForm";
	}

	/**
	 * 保存采购计划
	 */
	@RequiresPermissions(value={"purplan:purPlanMainDraft:add","purplan:purPlanMainDraft:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(PurPlanMain purPlanMain, Model model, RedirectAttributes redirectAttributes ) throws Exception{
		if (!beanValidator(model, purPlanMain)){
			return form(purPlanMain, model,null);
		}
		purPlanMain.setBillType("C");//设置单据类型 生成
		
		//计算采购计划总金额
		List<PurPlanDetail> details=purPlanMain.getPurPlanDetailList();
		double sum=0;
		for(int i = 0;i<details.size();i++) {
			if (details.get(i).getPlanSum() != null && details.get(i).getPlanSum() > 0&&"0".equals(details.get(i).getDelFlag())) {
				sum += details.get(i).getPlanSum();
			}
		}
		purPlanMain.setPlanPriceSum(sum);
		if("W".equals(purPlanMain.getBillStateFlag())){
			purPlanMainService.submitSave(purPlanMain);//提交时保存并启动工作流
		}else{
			//新增或编辑表单保存
			purPlanMainService.save(purPlanMain);//保存
		}
		/**
		 * 旧版菜单式审核，非框架工作流
		 */
		/*if("W".equals(purPlanMain.getBillStateFlag())){//状态为录入完毕W 时 ，即提交到第一个审核人
			AdtBillType billType =new AdtBillType();
			billType.setBillTypeCode("PLN001");//审核流转表中单据类型写死为 PLN001/采购计划 APP001/采购需求 CON001/采购合同
			billType.setBillTypeName("采购计划");
			adtDetailService.nextStep(purPlanMain.getBillNum(),billType,"PLN",true,null);//往审核流转表插入第一个审核数据
		}

		addMessage(redirectAttributes, "保存采购计划成功");
		return "redirect:"+Global.getAdminPath()+"/purplan/purPlanMainDraft/?repage";*/
		addMessage(redirectAttributes, "保存采购计划成功");
		return "redirect:"+Global.getAdminPath()+"/act/task/todo/?repage";
	}
	
	/**
	 * 删除采购计划
	 */
	@ResponseBody
	@RequiresPermissions("purplan:purPlanMainDraft:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(PurPlanMain purPlanMain, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		purPlanMainService.delete(purPlanMain);
		j.setMsg("删除采购计划成功");
		return j;
	}
	
	/**
	 * 批量删除采购计划
	 */
	@ResponseBody
	@RequiresPermissions("purplan:purPlanMainDraft:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			purPlanMainService.delete(purPlanMainService.get(id));
		}
		j.setMsg("删除采购计划成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("purplan:purPlanMainDraft:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(PurPlanMain purPlanMain, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "采购计划"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<PurPlanMain> page = purPlanMainService.findPage(new Page<PurPlanMain>(request, response, -1), purPlanMain);
    		new ExportExcel("采购计划", PurPlanMain.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出采购计划记录失败！失败信息："+e.getMessage());
		}
			return j;
    }
    
    @ResponseBody
    @RequestMapping(value = "detail")
	public PurPlanMain detail(String id) {
		return purPlanMainService.get(id);
	}
	

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("purplan:purPlanMainDraft:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<PurPlanMain> list = ei.getDataList(PurPlanMain.class);
			for (PurPlanMain purPlanMain : list){
				try{
					purPlanMainService.save(purPlanMain);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条采购计划记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条采购计划记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入采购计划失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/purplan/purPlanMainDraft/?repage";
    }
	
	/**
	 * 下载导入采购计划数据模板
	 */
	@RequiresPermissions("purplan:purPlanMainDraft:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "采购计划数据导入模板.xlsx";
    		List<PurPlanMain> list = Lists.newArrayList(); 
    		new ExportExcel("采购计划数据", PurPlanMain.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/purplan/purPlanMainDraft/?repage";
    }
	
	private String getBillNum() {
		Date date = new Date();
		Random random = new Random();
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");

		String randStr=("0000"+random.nextInt(10000));
		String billn="pln"+df.format(date.getTime())+randStr.substring(randStr.length()-4,randStr.length());
		while(commonService.getCodeNum("pur_planmain", "bill_num", billn)) {
			randStr=("0000"+random.nextInt(10000));
			billn="pln"+df.format(date.getTime())+randStr.substring(randStr.length()-4,randStr.length());
		}
		return billn;
	}

}