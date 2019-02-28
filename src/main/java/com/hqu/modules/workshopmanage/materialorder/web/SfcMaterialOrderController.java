/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.workshopmanage.materialorder.web;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import com.hqu.modules.basedata.period.entity.Period;
import com.hqu.modules.basedata.period.mapper.PeriodMapper;
import com.hqu.modules.basedata.period.service.PeriodService;
import com.hqu.modules.inventorymanage.outsourcingoutput.service.OutsourcingOutputService;
import com.hqu.modules.workshopmanage.materialorder.entity.SfcMaterialOrderDetail;
import com.hqu.modules.workshopmanage.processroutine.service.ProcessRoutineService;
import com.jeeplus.common.utils.time.DateFormatUtil;
import com.jeeplus.modules.sys.utils.UserUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
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
import com.hqu.modules.workshopmanage.materialorder.entity.SfcMaterialOrder;
import com.hqu.modules.workshopmanage.materialorder.service.SfcMaterialOrderService;

/**
 * 领料单Controller
 * @author zhangxin
 * @version 2018-05-22
 */
@Controller
@RequestMapping(value = "${adminPath}/materialorder/sfcMaterialOrder")
public class SfcMaterialOrderController extends BaseController {

	@Autowired
	private SfcMaterialOrderService sfcMaterialOrderService;
	@Autowired
	private OutsourcingOutputService outsourcingOutputService;
	@Autowired
    private ProcessRoutineService processRoutineService;

	
	@ModelAttribute
	public SfcMaterialOrder get(@RequestParam(required=false) String id) {
		SfcMaterialOrder entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = sfcMaterialOrderService.get(id);
		}
		if (entity == null){
			entity = new SfcMaterialOrder();
		}
		return entity;
	}

	/**
	 *	领料单打印界面
	 */
	@RequiresPermissions("materialorder:sfcMaterialOrder:edit")
	@RequestMapping(value = "print")
	public String print(SfcMaterialOrder sfcMaterialOrder,Model model) {
		model.addAttribute("contractMain", sfcMaterialOrder);
		if("不确定".equals(sfcMaterialOrder.getWareEmpname())){
			sfcMaterialOrder.setWareEmpname("");
			sfcMaterialOrder.setWareName("");
		}
		return "workshopmanage/materialorder/sfcMaterialOrderPrintForm";
	}

	/**
	 * 领料单编辑列表页面
	 */
	@RequiresPermissions("materialorder:sfcMaterialOrder:edit")
	@RequestMapping(value = "editList")
	public String editList() {
		return "workshopmanage/materialorder/sfcMaterialOrderEditList";
	}


	/**
	 * 领料单审核列表页面
	 */
	@RequiresPermissions("materialorder:sfcMaterialOrder:audit")
	@RequestMapping(value = "auditList")
	public String auditList() {
		return "workshopmanage/materialorder/sfcMaterialOrderAuditList";
	}

	/**
	 * 领料单退料处理列表页面
	 */
	@RequiresPermissions("materialorder:sfcMaterialOrder:return")
	@RequestMapping(value = "returnList")
	public String returnList() {
		return "workshopmanage/materialorder/sfcMaterialOrderReturnList";
	}

	/**
	 * 领料单退补料处理列表页面
	 */
	@RequiresPermissions("materialorder:sfcMaterialOrder:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "workshopmanage/materialorder/sfcMaterialOrderList";
	}

	/**
	 * 领料单查询列表页面
	 */
	@RequiresPermissions("materialorder:sfcMaterialOrder:query")
	@RequestMapping(value = "queryList")
	public String queryList() {
		return "workshopmanage/materialorder/sfcMaterialOrderQueryList";
	}

	/**
	 * 领料单退料处理列表数据（审核通过的领料单 单据类型为领料G 单据状态为S）
	 */
	@ResponseBody
	@RequiresPermissions("materialorder:sfcMaterialOrder:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(SfcMaterialOrder sfcMaterialOrder, HttpServletRequest request, HttpServletResponse response, Model model) {
		sfcMaterialOrder.setBillStateFlag("S");
		sfcMaterialOrder.setBillType("G");
		Page<SfcMaterialOrder> page = sfcMaterialOrderService.findPage(new Page<SfcMaterialOrder>(request, response), sfcMaterialOrder);
		return getBootstrapData(page);
	}

	/**
	 * 领料单查询列表数据
	 */
	@ResponseBody
	@RequiresPermissions("materialorder:sfcMaterialOrder:query")
	@RequestMapping(value = "queryData")
	public Map<String, Object> queryData(SfcMaterialOrder sfcMaterialOrder, HttpServletRequest request, HttpServletResponse response, Model model) {
		//sfcMaterialOrderService.judgeMaterialLack();//查询库存数量，判断领料单缺料情况
		Page<SfcMaterialOrder> page = sfcMaterialOrderService.findPage(new Page<SfcMaterialOrder>(request, response), sfcMaterialOrder);
		return getBootstrapData(page);
	}

	/**
	 * 领料单审核列表数据 已提交的领料单 单据状态为C
	 */
	@ResponseBody
	@RequiresPermissions("materialorder:sfcMaterialOrder:audit")
	@RequestMapping(value = "auditData")
	public Map<String, Object> auditData(SfcMaterialOrder sfcMaterialOrder, HttpServletRequest request, HttpServletResponse response, Model model) {
		sfcMaterialOrder.setBillStateFlag("C");
		Page<SfcMaterialOrder> page = sfcMaterialOrderService.findPage(new Page<SfcMaterialOrder>(request, response), sfcMaterialOrder);
		return getBootstrapData(page);
	}

	/**
	 * 领料单编辑列表数据 编辑中的领料单 单据状态为P
	 */
	@ResponseBody
	@RequiresPermissions("materialorder:sfcMaterialOrder:edit")
	@RequestMapping(value = "editData")
	public Map<String, Object> editData(SfcMaterialOrder sfcMaterialOrder, HttpServletRequest request, HttpServletResponse response, Model model) {
		sfcMaterialOrder.setBillStateFlag("P");
		Page<SfcMaterialOrder> page = sfcMaterialOrderService.findPage(new Page<SfcMaterialOrder>(request, response), sfcMaterialOrder);
		return getBootstrapData(page);
	}



	/**
	 * 查看领料单退补料处理表单页面
	 */
	@RequiresPermissions(value={"materialorder:sfcMaterialOrder:view","materialorder:sfcMaterialOrder:add","materialorder:sfcMaterialOrder:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(SfcMaterialOrder sfcMaterialOrder, Model model) {
		model.addAttribute("sfcMaterialOrder", sfcMaterialOrder);
		if(StringUtils.isBlank(sfcMaterialOrder.getId())){//如果ID是空为添加
			model.addAttribute("isAdd", true);
		}
		return "workshopmanage/materialorder/sfcMaterialOrderForm";
	}


	/**
	 * 查看领料单退料处理表单页面
	 */
	@RequiresPermissions("materialorder:sfcMaterialOrder:return")
	@RequestMapping(value = "returnForm")
	public String returnForm(SfcMaterialOrder sfcMaterialOrder, Model model) {
		model.addAttribute("sfcMaterialOrder", sfcMaterialOrder);
		if(StringUtils.isBlank(sfcMaterialOrder.getId())){//如果ID是空为添加
			model.addAttribute("isAdd", true);
		}
		return "workshopmanage/materialorder/sfcMaterialOrderReturnForm";
	}



	/**
	 * 查看领料单查询表单页面
	 */
	@RequiresPermissions("materialorder:sfcMaterialOrder:query")
	@RequestMapping(value = "queryForm")
	public String queryForm(SfcMaterialOrder sfcMaterialOrder, Model model) {
		model.addAttribute("sfcMaterialOrder", sfcMaterialOrder);
		if(StringUtils.isBlank(sfcMaterialOrder.getId())){//如果ID是空为添加
			model.addAttribute("isAdd", true);
		}
		return "workshopmanage/materialorder/sfcMaterialOrderQueryForm";
	}

	/**
	 * 编辑领料单表单页面
	 */
	@RequiresPermissions("materialorder:sfcMaterialOrder:edit")
	@RequestMapping(value = "editForm")
	public String editForm(SfcMaterialOrder sfcMaterialOrder, Model model) {
		model.addAttribute("sfcMaterialOrder", sfcMaterialOrder);
		if(StringUtils.isBlank(sfcMaterialOrder.getId())){//如果ID是空为添加
			model.addAttribute("isAdd", true);
		}
		return "workshopmanage/materialorder/sfcMaterialOrderEditForm";
	}

    /**
     * 手工录入领料单表单页面
     */
    @RequiresPermissions("materialorder:sfcMaterialOrder:edit")
    @RequestMapping(value = "newForm")
    public String newForm(SfcMaterialOrder sfcMaterialOrder, Model model) {
    	//设置领料单主表的默认必填项的值
		sfcMaterialOrderService.setDefalutNewMaterialOrder(sfcMaterialOrder);

        model.addAttribute("sfcMaterialOrder", sfcMaterialOrder);


        return "workshopmanage/materialorder/sfcMaterialOrderNewForm";
    }

    /**
     * 生成集中领料单表单页面（输入要生成的领料日期）
     */
    @RequiresPermissions("materialorder:sfcMaterialOrder:edit")
    @RequestMapping(value = "genForm")
    public String genForm(SfcMaterialOrder sfcMaterialOrder, Model model) {
    	sfcMaterialOrder.setOperDate(new Date());
        model.addAttribute("sfcMaterialOrder", sfcMaterialOrder);
        return "workshopmanage/materialorder/sfcMaterialOrderGenDialogForm";
    }


	/**
	 * 查看领料单审核表单页面
	 */
	@RequiresPermissions("materialorder:sfcMaterialOrder:audit")
	@RequestMapping(value = "auditForm")
	public String auditForm(SfcMaterialOrder sfcMaterialOrder, Model model) {
		model.addAttribute("sfcMaterialOrder", sfcMaterialOrder);
		if(StringUtils.isBlank(sfcMaterialOrder.getId())){//如果ID是空为添加
			model.addAttribute("isAdd", true);
		}
		return "workshopmanage/materialorder/sfcMaterialOrderAuditForm";
	}

	/**
	 * 处理领料单退料
	 */
	@RequiresPermissions("materialorder:sfcMaterialOrder:return")
	@RequestMapping(value = "returnSave")
	public String rerurnSave(SfcMaterialOrder sfcMaterialOrder, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, sfcMaterialOrder)){
			return form(sfcMaterialOrder, model);
		}
		//新增或编辑表单保存
		//sfcMaterialOrderService.save(sfcMaterialOrder);//保存  原单据不能修改

		//针对子表物料做退补料处理
		for(SfcMaterialOrderDetail sfcMaterialOrderDetail:sfcMaterialOrder.getSfcMaterialOrderDetailList()){
			if(sfcMaterialOrderDetail.getReturnNum()>0){
				sfcMaterialOrderService.returnOldMaterial(sfcMaterialOrder.getMaterialOrder(),sfcMaterialOrderDetail.getSequenceId(),sfcMaterialOrderDetail.getReturnNum().intValue(),sfcMaterialOrder.getQualityFlag());
				//sfcMaterialOrderService.returnAndGetMaterial(sfcMaterialOrder.getMaterialOrder(),sfcMaterialOrderDetail.getSequenceId(),(sfcMaterialOrderDetail.getReturnNum()).intValue());

			}
		}
		addMessage(redirectAttributes, "处理成功");
		return "redirect:"+Global.getAdminPath()+"/materialorder/sfcMaterialOrder/returnList/?repage";
	}



	/**
	 * 处理领料单退补料
	 */
	@RequiresPermissions(value={"materialorder:sfcMaterialOrder:add","materialorder:sfcMaterialOrder:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(SfcMaterialOrder sfcMaterialOrder, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, sfcMaterialOrder)){
			return form(sfcMaterialOrder, model);
		}
		//新增或编辑表单保存
		//sfcMaterialOrderService.save(sfcMaterialOrder);//保存  原单据不能修改

		//针对子表物料做退补料处理
		for(SfcMaterialOrderDetail sfcMaterialOrderDetail:sfcMaterialOrder.getSfcMaterialOrderDetailList()){
			if(sfcMaterialOrderDetail.getReturnNum()>0){

				sfcMaterialOrderService.returnAndGetMaterial(sfcMaterialOrder.getMaterialOrder(),sfcMaterialOrderDetail.getSequenceId(),(sfcMaterialOrderDetail.getReturnNum()).intValue(),sfcMaterialOrder.getQualityFlag());

			}
		}
		addMessage(redirectAttributes, "处理成功");
		return "redirect:"+Global.getAdminPath()+"/materialorder/sfcMaterialOrder/?repage";
	}


	/**
	 * 处理领料单编辑和新增
	 */
	@RequiresPermissions("materialorder:sfcMaterialOrder:edit")
	@RequestMapping(value = "editSave")
	public String editSave(SfcMaterialOrder sfcMaterialOrder, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, sfcMaterialOrder)){
			return form(sfcMaterialOrder, model);
		}
		//新增或编辑表单保存
		sfcMaterialOrderService.save(sfcMaterialOrder);//保存
		addMessage(redirectAttributes, "处理成功");
		return "redirect:"+Global.getAdminPath()+"/materialorder/sfcMaterialOrder/editList/?repage";
	}
	/**
	 * 处理领料单审核
	 */
	@RequiresPermissions("materialorder:sfcMaterialOrder:audit")
	@RequestMapping(value = "auditSave")
	public String auditSave(SfcMaterialOrder sfcMaterialOrder, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, sfcMaterialOrder)){
			return form(sfcMaterialOrder, model);
		}
		//审核通过后生成外购件出库单
		if("S".equals(sfcMaterialOrder.getBillStateFlag())){
			// 调用库存提供的接口，完成出库操作
			// 根据接口给出的返回值，回填出库单号，已完成追溯操作。
			String billNum = outsourcingOutputService.transferMaterialOrder(sfcMaterialOrder);
			//logger.debug("   billNum   " + billNum);
			sfcMaterialOrder.setInvBillNo(billNum);

		}
		//新增或编辑表单保存
		sfcMaterialOrderService.save(sfcMaterialOrder);//保存
		addMessage(redirectAttributes, "处理成功");
		return "redirect:"+Global.getAdminPath()+"/materialorder/sfcMaterialOrder/auditList/?repage";
	}


    /**
     * 处理生成集中领料单
     * @param sfcMaterialOrder
     * @param model
     * @param redirectAttributes
     * @return
     * @throws Exception
     */
	@ResponseBody
    @RequiresPermissions("materialorder:sfcMaterialOrder:edit")
    @RequestMapping(value = "genMaterialOrder")
    public AjaxJson genMaterialOrder(SfcMaterialOrder sfcMaterialOrder, Model model, RedirectAttributes redirectAttributes) throws Exception{
        AjaxJson j = new AjaxJson();


        /**
         * 最新的处理方案已不需要删除存在的领料单，已经生成的领料单就不再集中生成了。
         */
        sfcMaterialOrderService.deleteNoPassMaterialOrderWithDate(sfcMaterialOrder);
        try {
			//根据日期生成集中领料单
			processRoutineService.generateBatchMaterialOrder(DateFormatUtil.formatDate(DateFormatUtil.PATTERN_ISO_ON_DATE,sfcMaterialOrder.getOperDate()));
		}catch (Exception ex){
			logger.error("genMaterialOrder__________error________________:"+ex.toString());
        	logger.debug("genMaterialOrder__________error________________:"+ex.toString());
        	j.setSuccess(true);
        	j.setMsg("领料单集中生成出错，请重试！");
        	return j;
		}

        j.setSuccess(true);
        j.setMsg("执行成功");
        return j;
    }

    @ResponseBody
    @RequiresPermissions("materialorder:sfcMaterialOrder:edit")
    @RequestMapping(value = "isExist")
    public AjaxJson isExist(SfcMaterialOrder sfcMaterialOrder,Model model,RedirectAttributes redirectAttributes) throws Exception{
	    AjaxJson ajaxJson = new AjaxJson();

	    ajaxJson.setSuccess(true);
	    /*
	    此处已不存在领料单被重复覆盖或者重复领料的问题，因此不再检测是否存在指定日期的领料单 --20190122，yang
	     */
		/*int result = sfcMaterialOrderService.isExistNoPassMaterialOrder(sfcMaterialOrder);
        if(result == 0){
            ajaxJson.setErrorCode("0");//errorcode设置为0表示提示已存在当前领料日期的领料单
        }else if(result == -1){
            ajaxJson.setErrorCode("-1");
        }else if(result == 1){
        	ajaxJson.setErrorCode("1");
		}else{
        	ajaxJson.setSuccess(false);
		}*/
		ajaxJson.setErrorCode("1");//当天不存在重复领料单
        return ajaxJson;
    }


	
	/**
	 * 提交领料单
	 */
	@RequiresPermissions("materialorder:sfcMaterialOrder:edit")
	@RequestMapping(value = "submit")
	public String submit(SfcMaterialOrder sfcMaterialOrder, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, sfcMaterialOrder)){
			return form(sfcMaterialOrder, model);
		}
		//提交表单
		sfcMaterialOrderService.submit(sfcMaterialOrder);//保存
		addMessage(redirectAttributes, "处理成功");
		return "redirect:"+Global.getAdminPath()+"/materialorder/sfcMaterialOrder/?repage";
	}



	
	/**
	 * 删除领料单
	 */
	@ResponseBody
	@RequiresPermissions("materialorder:sfcMaterialOrder:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(SfcMaterialOrder sfcMaterialOrder, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		sfcMaterialOrderService.delete(sfcMaterialOrder);
		j.setMsg("删除领料单成功");
		return j;
	}
	
	/**
	 * 批量删除领料单
	 */
	@ResponseBody
	@RequiresPermissions("materialorder:sfcMaterialOrder:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			sfcMaterialOrderService.delete(sfcMaterialOrderService.get(id));
		}
		j.setMsg("删除领料单成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("materialorder:sfcMaterialOrder:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(SfcMaterialOrder sfcMaterialOrder, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "领料单"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<SfcMaterialOrder> page = sfcMaterialOrderService.findPage(new Page<SfcMaterialOrder>(request, response, -1), sfcMaterialOrder);
    		new ExportExcel("领料单", SfcMaterialOrder.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出领料单记录失败！失败信息："+e.getMessage());
		}
			return j;
    }
    
    @ResponseBody
    @RequestMapping(value = "detail")
	public SfcMaterialOrder detail(String id) {
		return sfcMaterialOrderService.get(id);
	}
	

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("materialorder:sfcMaterialOrder:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<SfcMaterialOrder> list = ei.getDataList(SfcMaterialOrder.class);
			for (SfcMaterialOrder sfcMaterialOrder : list){
				try{
					sfcMaterialOrderService.save(sfcMaterialOrder);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条领料单记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条领料单记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入领料单失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/materialorder/sfcMaterialOrder/?repage";
    }
	
	/**
	 * 下载导入领料单数据模板
	 */
	@RequiresPermissions("materialorder:sfcMaterialOrder:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "领料单数据导入模板.xlsx";
    		List<SfcMaterialOrder> list = Lists.newArrayList(); 
    		new ExportExcel("领料单数据", SfcMaterialOrder.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/materialorder/sfcMaterialOrder/?repage";
    }
	

}