/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.purchasemanage.purplan.web;

import com.google.common.collect.Lists;
import com.hqu.modules.Common.service.CommonService;
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
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * 采购计划Controller
 * @author yangxianbang
 * @version 2018-05-09
 */
@Controller
@RequestMapping(value = "${adminPath}/purplan/purPlanMainOrder")
public class PurPlanMainOrderController extends BaseController {

	@Autowired
	private PurPlanMainService purPlanMainService;
	@Autowired
	private CommonService commonService;
	
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
	@RequiresPermissions("purplan:purPlanMainOrder:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "purchasemanage/purplan/purPlanMainOrderList";
	}
	
		/**
	 * 采购计划列表数据
	 */
	@ResponseBody
	@RequiresPermissions("purplan:purPlanMainOrder:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(PurPlanMain purPlanMain, HttpServletRequest request, HttpServletResponse response, Model model) {
		purPlanMain.setBillStateFlag("E");
		Page<PurPlanMain> page = purPlanMainService.findPage(new Page<PurPlanMain>(request, response), purPlanMain); 
		return getBootstrapData(page);
	}
	
	/**
	 * 采购计划列表数据
	 */
	@ResponseBody
	@RequiresPermissions(value= {"purplan:pulanMainOrder:add","purplan:purPlanMainOrder:edit",})
	@RequestMapping(value = "itemsdata")
	public Map<String, Object> itemsdata(ItemExtend item, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<ItemExtend> page = purPlanMainService.findItemPage(new Page<ItemExtend>(request, response), item); 
		return getBootstrapData(page);
	}


	/**
	 * 查看，增加，编辑采购计划表单页面
	 */
	@RequiresPermissions(value={"purplan:purPlanMainOrder:view","purplan:purPlanMainOrder:add","purplan:purPlanMainOrder:edit"},logical=Logical.OR)
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
        if(StringUtils.isNotBlank(purPlanMain.getId())&&null!=purPlanMain.getAct().getTaskDefKey()){
		    if(purPlanMain.getAct().isFinishTask()) {
                return "purchasemanage/purplan/purPlanMainQueryForm";
            }
        }
		return "purchasemanage/purplan/purPlanMainOrderForm";
	}

	/**
	 * 保存采购计划
	 */
	@RequiresPermissions(value={"purplan:purPlanMainOrder:add","purplan:purPlanMainOrder:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(PurPlanMain purPlanMain, Model model, RedirectAttributes redirectAttributes ) throws Exception{
		if (!beanValidator(model, purPlanMain)){
			return form(purPlanMain, model,null);
		}
		//purPlanMain.setBillType("M");//设置单据类型 录入
		
		//计算采购计划总金额
		List<PurPlanDetail> details=purPlanMain.getPurPlanDetailList();
		double sum=0;
		for(int i = 0;i<details.size();i++) {
			if(details.get(i).getPlanSum() != null && details.get(i).getPlanSum() > 0&&"0".equals(details.get(i).getDelFlag())) {
				sum+=details.get(i).getPlanSum();
			}
		}
		purPlanMain.setPlanPriceSum(sum);

		addMessage(redirectAttributes, "保存采购计划成功");
		if(purPlanMain.getAct().getTaskDefKey()==null||purPlanMain.getAct().getTaskDefKey().equals("")) {
			//新增或编辑表单保存
			purPlanMainService.save(purPlanMain);//保存
		}else {
			purPlanMainService.auditSave(purPlanMain);//带工作流的保存
			return "redirect:"+Global.getAdminPath()+"/act/task/todo/?repage";
		}
		return "redirect:"+Global.getAdminPath()+"/purplan/purPlanMainOrder/?repage";
	}
	
	/**
	 * 删除采购计划
	 */
	@ResponseBody
	@RequiresPermissions("purplan:purPlanMainOrder:del")
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
	@RequiresPermissions("purplan:purPlanMainOrder:del")
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
	@RequiresPermissions("purplan:purPlanMainOrder:export")
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
	@RequiresPermissions("purplan:purPlanMainOrder:import")
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
		return "redirect:"+Global.getAdminPath()+"/purplan/purPlanMainOrder/?repage";
    }
	
	/**
	 * 下载导入采购计划数据模板
	 */
	@RequiresPermissions("purplan:purPlanMainOrder:import")
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
		return "redirect:"+Global.getAdminPath()+"/purplan/purPlanMainOrder/?repage";
    }
	
	private String getBillNum() {
		Date date = new Date();
		Random random = new Random();
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
		
		String billn="pln"+df.format(date.getTime())+random.nextInt(10000);
		while(commonService.getCodeNum("pur_planmain", "bill_num", billn)) {
			billn="pln"+df.format(date.getTime())+random.nextInt(10000);
		}
		return billn;
	}

}