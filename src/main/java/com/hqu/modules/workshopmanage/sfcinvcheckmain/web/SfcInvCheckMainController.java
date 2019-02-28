/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.workshopmanage.sfcinvcheckmain.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import com.hqu.modules.workshopmanage.ppc.entity.MSerialNo;
import com.hqu.modules.workshopmanage.ppc.entity.MpsPlan;
import com.hqu.modules.workshopmanage.ppc.service.MSerialNoService;
import com.hqu.modules.workshopmanage.ppc.service.MpsPlanService;
import com.hqu.modules.workshopmanage.process.web.ProcessController;
import com.hqu.modules.workshopmanage.processroutine.entity.ProcessRoutine;
import com.hqu.modules.workshopmanage.processroutine.service.ProcessRoutineService;
import com.hqu.modules.workshopmanage.processroutinedetail.entity.ProcessRoutineDetail;
import com.hqu.modules.workshopmanage.processroutinedetail.mapper.ProcessRoutineDetailMapper;
import com.hqu.modules.workshopmanage.processroutinedetail.service.ProcessRoutineDetailService;
import com.hqu.modules.workshopmanage.sfcinvcheckmain.entity.SfcInvCheckDetail;
import com.sun.tracing.dtrace.Attributes;
import org.apache.shiro.authz.annotation.Logical;
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
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.excel.ExportExcel;
import com.jeeplus.common.utils.excel.ImportExcel;
import com.hqu.modules.workshopmanage.sfcinvcheckmain.entity.SfcInvCheckMain;
import com.hqu.modules.workshopmanage.sfcinvcheckmain.service.SfcInvCheckMainService;

import static com.jeeplus.core.service.BaseService.dataRuleFilter;

/**
 * 完工入库通知单Controller
 * @author zhangxin
 * @version 2018-06-01
 */
@Controller
@RequestMapping(value = "${adminPath}/sfcinvcheckmain/sfcInvCheckMain")
public class SfcInvCheckMainController extends BaseController {

	@Autowired
	private SfcInvCheckMainService sfcInvCheckMainService;
	@Autowired
	private ProcessRoutineService processRoutineService;
	@Autowired
	private ProcessRoutineDetailService processRoutineDetailService;

	@Autowired
	private ProcessRoutineDetailMapper processRoutineDetailMapper;
	@Autowired
	private MSerialNoService mSerialNoService;
	@Autowired
	private MpsPlanService mpsPlanService;
	
	@ModelAttribute
	public SfcInvCheckMain get(@RequestParam(required=false) String id) {
		SfcInvCheckMain entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = sfcInvCheckMainService.get(id);
		}
		if (entity == null){
			entity = new SfcInvCheckMain();
		}
		return entity;
	}

	/*********************************完工入库通知单begin**************************************************/

	/**
	 * 完工入库通知单生成列表页面
	 */
	@RequiresPermissions("sfcinvcheckmain:sfcInvCheckMain:generate")
	@RequestMapping(value = "generateList")
	public String generateList(Model model) {
		model.addAttribute("processRoutineDetail",new ProcessRoutineDetail());
		return "workshopmanage/sfcinvcheckmain/sfcInvCheckMainGenerateList";
	}


	/**
	 * 完工入库通知单生成列表数据
	 */
	@ResponseBody
	@RequiresPermissions("sfcinvcheckmain:sfcInvCheckMain:generate")
	@RequestMapping(value = "generateData")
	public Map<String, Object> generateData(ProcessRoutineDetail processRoutineDetail, HttpServletRequest request, HttpServletResponse response, Model model) {
		processRoutineDetail.setBillstate("O");//已扫码完工提交的工艺
		Page<ProcessRoutineDetail> page = processRoutineDetailService.findInvCheckPage(new Page<ProcessRoutineDetail>(request, response), processRoutineDetail);
		return getBootstrapData(page);
	}

	/**
	 * 完工入库通知单表单生成页面
	 */
	@RequiresPermissions("sfcinvcheckmain:sfcInvCheckMain:generate")
	@RequestMapping(value = "generateForm")
	public String generateForm(@RequestParam String id, Model model) {
		ProcessRoutineDetail processRoutineDetail=processRoutineDetailMapper.get(id);
		if(processRoutineDetail==null){
			processRoutineDetail=new ProcessRoutineDetail();
		}
		model.addAttribute("processRoutineDetail", processRoutineDetail);

		return "workshopmanage/sfcinvcheckmain/sfcInvCheckMainGenerateForm";
	}

	/**
	 * 生成完工入库通知单
	 */
	@RequiresPermissions("sfcinvcheckmain:sfcInvCheckMain:generate")
	@RequestMapping(value = "generate")
	public String generate(ProcessRoutineDetail processRoutineDetail, Model model, RedirectAttributes redirectAttributes) throws Exception{
		//防止重复生成
		ProcessRoutineDetail processRoutineDetailResult = processRoutineDetailMapper.get(processRoutineDetail.getId());
		if(processRoutineDetail!=null){
			if("C".equals(processRoutineDetailResult.getInvcheckstate())){
				addMessage(redirectAttributes, "该完工入库通知单已生成！");
				return "redirect:"+Global.getAdminPath()+"/sfcinvcheckmain/sfcInvCheckMain/generateList/?repage";
			}
		}

		//保存车间路线单的生成状态
		processRoutineDetailMapper.updateInvCheckState(processRoutineDetail);//保存
		try {
			//生成完工入库单
			processRoutineService.generateInvCheckByRoutineDetail(processRoutineDetail.getRoutinebillno(), processRoutineDetail.getRoutingcode(), processRoutineDetail.getSeqno());
		}catch (Exception e){

			logger.debug("sfcinvcheckmaincontroller.generater.ProcessRoutineService.generateRoutineDetail_________________error:"+e.getMessage());
		}
		addMessage(redirectAttributes, "该完工入库通知单生成成功！");
		return "redirect:"+Global.getAdminPath()+"/sfcinvcheckmain/sfcInvCheckMain/generateList/?repage";
	}


	/*********************************完工入库通知单end***************************************************/

	/**
	 * 完工入库通知单提交列表页面
	 */
	@RequiresPermissions("sfcinvcheckmain:sfcInvCheckMain:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "workshopmanage/sfcinvcheckmain/sfcInvCheckMainList";
	}
	
	/**
	 * 完工入库通知单列表数据
	 */
	@ResponseBody
	@RequiresPermissions("sfcinvcheckmain:sfcInvCheckMain:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(SfcInvCheckMain sfcInvCheckMain, HttpServletRequest request, HttpServletResponse response, Model model) {

		Page<SfcInvCheckMain> page = sfcInvCheckMainService.findPage(new Page<SfcInvCheckMain>(request, response), sfcInvCheckMain);
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑完工入库通知单表单页面
	 */
	@RequiresPermissions(value={"sfcinvcheckmain:sfcInvCheckMain:view","sfcinvcheckmain:sfcInvCheckMain:add","sfcinvcheckmain:sfcInvCheckMain:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(SfcInvCheckMain sfcInvCheckMain, Model model) {
		model.addAttribute("sfcInvCheckMain", sfcInvCheckMain);
		if("Q".equals(sfcInvCheckMain.getBillStateFlag())){//如果状态为待提交，则显示提交按钮
			model.addAttribute("isAdd", true);
		}
		return "workshopmanage/sfcinvcheckmain/sfcInvCheckMainForm";
	}

	/**
	 * 提交完工入库通知单
	 */
	@RequiresPermissions(value={"sfcinvcheckmain:sfcInvCheckMain:add","sfcinvcheckmain:sfcInvCheckMain:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(SfcInvCheckMain sfcInvCheckMain, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, sfcInvCheckMain)){
			return form(sfcInvCheckMain, model);
		}
		//防止重复提交
		List<SfcInvCheckMain> sfcInvCheckMainList = sfcInvCheckMainService.findList(sfcInvCheckMain);
		if(sfcInvCheckMainList!=null&&sfcInvCheckMainList.size()!=0){
			if(sfcInvCheckMainList.get(0)!=null){
				if("C".equals(sfcInvCheckMainList.get(0).getBillStateFlag())){
					addMessage(redirectAttributes, "该完工入库通知单已经提交");
					return "redirect:"+Global.getAdminPath()+"/sfcinvcheckmain/sfcInvCheckMain/?repage";
				}
			}
		}

		sfcInvCheckMain.setBillStateFlag("C");//将单据状态置为已提交
		//保存状态
		sfcInvCheckMainService.save(sfcInvCheckMain);//保存

		//整机入库单的生成时填写每个整机来自哪个主生产计划以及内部订单。（目前一个完工单只有一个整机,但以后可能有多个整机）
		/**
		 * 思路：1,根据完工单主表实体查到所有子表整机；2.按每个整机的mserialno（机台码）在表mserialno中找到mpsplanid；
		 * 		3，根据mpsplanid在表mpsplan中找到reqid（内部订单号，由##分割）
		 */
		String mpsPlanId="";
		String orderNo="";
		try{
			for(SfcInvCheckDetail sfcInvCheckDetail:sfcInvCheckMain.getSfcInvCheckDetailList()){
				if(sfcInvCheckDetail.DEL_FLAG_NORMAL.equals(sfcInvCheckDetail.getDelFlag())){
					MSerialNo serialNo=new MSerialNo();
					serialNo.setMserialno(sfcInvCheckDetail.getmSerialNo());
					serialNo=mSerialNoService.findList(serialNo).get(0);
					mpsPlanId=serialNo.getMpsplanid();
					MpsPlan mpsPlan=new MpsPlan();
					mpsPlan.setMpsPlanid(mpsPlanId);
					mpsPlan=mpsPlanService.findMyList(mpsPlan).get(0);
					orderNo=mpsPlan.getReqID().split("##")[0];
				}
			}
		}catch (Exception e){
			logger.debug("sfcinvcheckmaincontroller.save.ProcessRoutine.generateRoutineDetail_________________error(往整机入库单填写mpsplanid和orderno时出错！):"+e.getMessage());
		}

		try {
			//根据完工入库单生成库存的入库单
			processRoutineService.generateInvImportBill(sfcInvCheckMain.getBillNo(),mpsPlanId,orderNo);
			/**
			 * MSerialNo.mpsplanid  orderid in invbillmain
			 */
		}catch (Exception e){
			logger.debug("sfcinvcheckmaincontroller.save.ProcessRoutine.generateRoutineDetail_________________error:"+e.getMessage());
		}

		addMessage(redirectAttributes, "提交完工入库通知单成功");
		return "redirect:"+Global.getAdminPath()+"/sfcinvcheckmain/sfcInvCheckMain/?repage";
	}
	
	/**
	 * 提交完工入库通知单
	 *//*
	@RequiresPermissions("sfcinvcheckmain:sfcInvCheckMain:edit")
	@RequestMapping(value = "submit")
	public String submit(SfcInvCheckMain sfcInvCheckMain, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, sfcInvCheckMain)){
			return form(sfcInvCheckMain, model);
		}
		//提交表单
		sfcInvCheckMainService.submit(sfcInvCheckMain);//保存
		addMessage(redirectAttributes, "提交完工入库通知单成功");
		return "redirect:"+Global.getAdminPath()+"/sfcinvcheckmain/sfcInvCheckMain/?repage";
	}*/
	
	/**
	 * 删除完工入库通知单
	 */
	@ResponseBody
	@RequiresPermissions("sfcinvcheckmain:sfcInvCheckMain:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(SfcInvCheckMain sfcInvCheckMain, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		sfcInvCheckMainService.delete(sfcInvCheckMain);
		j.setMsg("删除完工入库通知单成功");
		return j;
	}
	
	/**
	 * 批量删除完工入库通知单
	 */
	@ResponseBody
	@RequiresPermissions("sfcinvcheckmain:sfcInvCheckMain:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			sfcInvCheckMainService.delete(sfcInvCheckMainService.get(id));
		}
		j.setMsg("删除完工入库通知单成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("sfcinvcheckmain:sfcInvCheckMain:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(SfcInvCheckMain sfcInvCheckMain, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "完工入库通知单"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<SfcInvCheckMain> page = sfcInvCheckMainService.findPage(new Page<SfcInvCheckMain>(request, response, -1), sfcInvCheckMain);
    		new ExportExcel("完工入库通知单", SfcInvCheckMain.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出完工入库通知单记录失败！失败信息："+e.getMessage());
		}
			return j;
    }
    
    @ResponseBody
    @RequestMapping(value = "detail")
	public SfcInvCheckMain detail(String id) {
		return sfcInvCheckMainService.get(id);
	}
	

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("sfcinvcheckmain:sfcInvCheckMain:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<SfcInvCheckMain> list = ei.getDataList(SfcInvCheckMain.class);
			for (SfcInvCheckMain sfcInvCheckMain : list){
				try{
					sfcInvCheckMainService.save(sfcInvCheckMain);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条完工入库通知单记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条完工入库通知单记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入完工入库通知单失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/sfcinvcheckmain/sfcInvCheckMain/?repage";
    }
	
	/**
	 * 下载导入完工入库通知单数据模板
	 */
	@RequiresPermissions("sfcinvcheckmain:sfcInvCheckMain:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "完工入库通知单数据导入模板.xlsx";
    		List<SfcInvCheckMain> list = Lists.newArrayList(); 
    		new ExportExcel("完工入库通知单数据", SfcInvCheckMain.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/sfcinvcheckmain/sfcInvCheckMain/?repage";
    }
	

}