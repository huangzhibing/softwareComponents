/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.purchasemanage.contractmain.web;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

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
import com.hqu.modules.purchasemanage.contractmain.entity.ContractMain;
import com.hqu.modules.purchasemanage.contractmain.service.ContractMainFreezeService;
import com.hqu.modules.purchasemanage.contractmain.service.ContractMainService;

/**
 * 采购合同管理Controller
 * @author ltq
 * @version 2018-04-28
 */
@Controller
@RequestMapping(value = "${adminPath}/contractmain/contractMainFreeze")
public class ContractMainFreezeController extends BaseController {

	@Autowired
	private ContractMainFreezeService contractMainService;
	@ModelAttribute
	public ContractMain get(@RequestParam(required=false) String id) {
		ContractMain entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = contractMainService.get(id);
		}
		if (entity == null){
			entity = new ContractMain();
		}
		return entity;
	}
	
	/**
	 * 采购合同管理列表页面
	 */
	@RequiresPermissions("contractmainfreeze:contractMain:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "purchasemanage/contractmain/contractMainFreezeList";
	}
	
	/**
	 * 采购合同管理列表数据
	 */
	@ResponseBody
	@RequiresPermissions("contractmainfreeze:contractMain:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(ContractMain contractMain, HttpServletRequest request, HttpServletResponse response, Model model) {
		//ContractMain
		Page<ContractMain> page = contractMainService.findPage(new Page<ContractMain>(request, response), contractMain); 
		 //补全制单人、供应商编号等信息
		   page=contractMainService.addInf(page);
		 /* //分页
	        String intPage= request.getParameter("pageNo");
	        int pageNo=Integer.parseInt(intPage);
	        int pageSize= page.getPageSize();
	        List<ContractMain> pageList=page.getList();
	       if(pageNo==1&&pageSize==-1){
	    	   page.setList(pageList);
	       }else{
	    	   List<ContractMain> reportL=  new ArrayList<ContractMain>();        
		        for(int i=(pageNo-1)*pageSize;i<pageList.size() && i<pageNo*pageSize;i++){
		        	reportL.add(pageList.get(i));
			    } 
		        page.setList(reportL);
	       }
	     */
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑采购合同管理表单页面
	 */
	@RequiresPermissions(value={"contractmainfreeze:contractMain:view","contractmainfreeze:contractMain:add","contractmainfreeze:contractMain:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(ContractMain contractMain, Model model) {
		//补全contractMaind对象中部分属性的数据
		contractMain=contractMainService.addFormContractMainInf(contractMain);
	    //获取支付方式下拉框的数据
		model.addAttribute("payList", contractMainService.getPayList());
		//获取运输方式下拉框的数据
		model.addAttribute("transList", contractMainService.getTransList());
		model.addAttribute("contractMain", contractMain);
		if(StringUtils.isBlank(contractMain.getId())){//如果ID是空为添加
			model.addAttribute("isAdd", true);
		}	
		
		// 工作流结束后查看工单
		if(contractMain.getAct().isFinishTask()){
			return "purchasemanage/contractmain/contractMainViewForm";
		}
		return "purchasemanage/contractmain/contractMainFreezeForm";
	}

	/**
	 * 提交采购合同管理
	 */
	@RequiresPermissions(value={"contractmainfreeze:contractMain:add","contractmainfreeze:contractMain:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String submit(ContractMain contractMain, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, contractMain)){
			return form(contractMain, model);
		}
		try {
			 //单据状态改为录入完毕
			contractMain.setBillStateFlag("D");
			//录入的合同
			//contractMain.setContrState("C");
			//新增或编辑表单保存
			contractMainService.save(contractMain);//提交
			addMessage(redirectAttributes, "合同冻结成功");
		}catch (Exception e) {
			//logger.error("启动请假流程失败：", e);
			addMessage(redirectAttributes, "系统内部错误！");
		}
		//return "redirect:" + adminPath + "/act/task/process/";
		return "redirect:" + adminPath + "/act/task/todo/";
		//return "redirect:"+Global.getAdminPath()+"/contractmain/contractMainFreeze/?repage";
	}
	
	/**
	 * 保存采购合同管理
	 
	@ResponseBody
	@RequiresPermissions(value={"contractmainfreeze:contractMain:add","contractmainfreeze:contractMain:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public AjaxJson save(ContractMain contractMain, Model model, RedirectAttributes redirectAttributes) throws Exception{
		AjaxJson j = new AjaxJson();
		if (!beanValidator(model, contractMain)){		
			j.setMsg("合同冻结失败");
			return j;
		}
		//单据状态改为正在录入/修改
		contractMain.setBillStateFlag("D");
		//新增或编辑表单保存
		contractMainService.save(contractMain);//提交
		j.setMsg("合同冻结成功");		
		return j;
	}
	*/
	
	/**
	 * 删除采购合同管理
	 */
	@ResponseBody
	@RequiresPermissions("contractmainfreeze:contractMain:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(ContractMain contractMain, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		contractMainService.delete(contractMain);
		j.setMsg("删除采购合同管理成功");
		return j;
	}
	
	/**
	 * 批量删除采购合同管理
	 */
	@ResponseBody
	@RequiresPermissions("contractmainfreeze:contractMain:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			contractMainService.delete(contractMainService.get(id));
		}
		j.setMsg("删除采购合同管理成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("contractmainfreeze:contractMain:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(ContractMain contractMain, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "采购合同管理"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<ContractMain> page = contractMainService.findPage(new Page<ContractMain>(request, response, -1), contractMain);
    		new ExportExcel("采购合同管理", ContractMain.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出采购合同管理记录失败！失败信息："+e.getMessage());
		}
			return j;
    }
    
    @ResponseBody
    @RequestMapping(value = "detail")
	public ContractMain detail(String id) {
		return contractMainService.get(id);
	}
	

	/**
	 * 导入Excel数据
	 */
	@RequiresPermissions("contractmainfreeze:contractMain:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<ContractMain> list = ei.getDataList(ContractMain.class);
			for (ContractMain contractMain : list){
				try{
					contractMainService.save(contractMain);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条采购合同管理记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条采购合同管理记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入采购合同管理失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/contractmain/contractMainFreeze/?repage";

	}
	
	/**
	 * 下载导入采购合同管理数据模板
	 */
	@RequiresPermissions("contractmainfreeze:contractMain:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "采购合同管理数据导入模板.xlsx";
    		List<ContractMain> list = Lists.newArrayList(); 
    		new ExportExcel("采购合同管理数据", ContractMain.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/contractmain/contractMainFreeze/?repage";

	}
	
	
	
	
	
}