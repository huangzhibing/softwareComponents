/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.salemanage.salcontract.web;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transaction;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
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
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.utils.UserUtils;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.excel.ExportExcel;
import com.jeeplus.common.utils.excel.ImportExcel;
import com.hqu.modules.basedata.period.entity.Period;
import com.hqu.modules.salemanage.salcontract.entity.Contract;
import com.hqu.modules.salemanage.salcontract.service.ContractService;

/**
 * 销售合同Controller
 * @author dongqida
 * @version 2018-05-12
 */
@Controller
@RequestMapping(value = "${adminPath}/salcontract/contract")
public class ContractController extends BaseController {

	@Autowired
	private ContractService contractService;
	
	@ModelAttribute
	public Contract get(@RequestParam(required=false) String id) {
		Contract entity = null;
		if (StringUtils.isNotBlank(id)){
			if(id.length() == 15){
				entity = contractService.getByCode(id);
			} else {
				entity = contractService.get(id);
			}
		}
		if (entity == null){
			entity = new Contract();
		}
		return entity;
	}
	
	/**
	 * 销售合同列表页面
	 */
	@RequiresPermissions("salcontract:contract:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "salemanage/salcontract/contractList";
	}
	@RequiresPermissions("salcontract:contract:list")
	@RequestMapping(value = "contractEnd")
	public String listEnd() {
		return "salemanage/salcontract/contractEndList";
	}
	@RequiresPermissions("salcontract:contract:list")
	@RequestMapping(value = "contractSearch")
	public String listSearch() {
		return "salemanage/salcontract/contractSearchList";
	}
	@RequiresPermissions("salcontract:contract:list")
	@RequestMapping(value = "contractFinish")
	public String listFinish() {
		return "salemanage/salcontract/contractFinishList";
	}
	@RequiresPermissions("salcontract:contract:list")
	@RequestMapping(value = "contractAudit")
	public String listAudit() {
		return "salemanage/salcontract/contractAuditList";
	}
	@RequestMapping(value = "contractSuggestions")
	public String contractSuggestions(HttpServletRequest request,ModelMap map) {
		map.put("id", request.getParameter("id"));
		return "salemanage/salcontract/suggestions";
	}
	/**
	 * 审核通过
	 * @param id
	 * @return
	 */
	@RequestMapping(value="contractPass")
	@ResponseBody
	public String contractPass(String id){
		Map<String, Object>map=new HashMap<>();
		map.put("id",id);
		map.put("contractStat", "E");
		Boolean judge=contractService.updateStat(map);
		if(judge)
			return "yes";
		else
			return "no";
	}
	/**
	 * 审核不通过
	 * @param id
	 * @param suggestions 意见
	 * @return
	 */
	@Transactional
	@RequestMapping(value="contractDeny")
	@ResponseBody
	public String contractDeny(String id,String suggestions){
		Map<String, Object>map=new HashMap<>();
		map.put("id", id);
		map.put("contractStat", "B");//修改状态，添加意见
		Map<String, Object> mapSuggestion=new HashMap<>();
		mapSuggestion.put("id", id);
		mapSuggestion.put("suggestions", suggestions);
		try {
			contractService.updateStat(map);
			contractService.updateSuggestions(mapSuggestion);
			return "yes";
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return "no";
		}
	}
		/**
	 * 销售合同列表数据
	 */
	@ResponseBody
	@RequiresPermissions("salcontract:contract:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(Contract contract, HttpServletRequest request, HttpServletResponse response, Model model) {
		contract.setContractStat(request.getParameter("status"));
		System.out.println("status"+request.getParameter("status"));
		Page<Contract> page = contractService.findPage(new Page<Contract>(request, response), contract); 
		if(contract.getProdCode()!=null&&!StringUtils.isEmpty(contract.getProdCode().getId())) {
			Set<String> set = contractService.findByProdCode(contract.getProdCode().getId());
			List<Contract> clist=new ArrayList<>();
			for(int i=0;i<page.getList().size();i++) {
				if(set.contains(page.getList().get(i).getId())) {
					clist.add(page.getList().get(i));
				}
			}
			page.setList(clist);
		}
		
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑销售合同表单页面
	 */
	@RequiresPermissions(value={"salcontract:contract:view","salcontract:contract:add","salcontract:contract:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(Contract contract, Model model) {
		
		if(StringUtils.isBlank(contract.getId())){//如果ID是空为添加
			//生成业务主键
			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			String newBillNum = contractService.getMaxIdByTypeAndDate("con" + sdf.format(date));
			if (StringUtils.isEmpty(newBillNum)) {
				newBillNum = "0000";
			} else {
				newBillNum = String.format("%04d", Integer.parseInt(newBillNum.substring(11, 15)) + 1);
			}
			logger.debug("billNum:" + newBillNum);
			contract.setContractCode("con" + sdf.format(date) + newBillNum);
			contract.setInputDate(date);//当前时间
			User user=UserUtils.getUser();//当前用户当录入员
			contract.setInputPerson(user);
			contract.setPerson(user);
			model.addAttribute("isAdd", true);
		}
		model.addAttribute("contract", contract);
		return "salemanage/salcontract/contractForm";
	}
	@RequestMapping(value = "formSearch")
	public String formSearch(Contract contract, Model model) {
		model.addAttribute("contract", contract);
		return "salemanage/salcontract/contractSearch";
	}
	@RequestMapping(value = "formAudit")
	public String formAudit(Contract contract, Model model) {
		model.addAttribute("contract", contract);
		return "salemanage/salcontract/contractAudit";
	}
	@RequestMapping(value = "formEnd")
	public String formEnd(Contract contract, Model model) {
		contract.setEndDate(new Date());
		User user = UserUtils.getUser();
		contract.setEndPerson(user);
		contract.setEndPsnName(user.getName());
		model.addAttribute("contract", contract);
		return "salemanage/salcontract/contractEnd";
	}
	@RequestMapping(value = "formFinish")
	public String formFinish(Contract contract, Model model) {
		model.addAttribute("contract", contract);
		return "salemanage/salcontract/contractFinish";
	}
	/**
	 * 保存销售合同
	 */
	@RequiresPermissions(value={"salcontract:contract:add","salcontract:contract:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(Contract contract, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, contract)){
			return form(contract, model);
		}
		//新增或编辑表单保存
		contractService.save(contract);//保存
		addMessage(redirectAttributes, "保存销售合同成功");
		return "redirect:"+Global.getAdminPath()+"/salcontract/contract/?repage";
	}
	@RequestMapping(value = "end")
	public String end(Contract contract, Model model, RedirectAttributes redirectAttributes) throws Exception{
//		if (!beanValidator(model, contract)){
//			return form(contract, model);
//		}
		Map<String, Object>map=new HashMap<>();
		map.put("id", contract.getId());
		contract.setContractStat("O");
		map.put("contractStat", contract.getContractStat());
		contractService.updateStat(map);
		addMessage(redirectAttributes, "保存销售结案成功");
		return "redirect:"+Global.getAdminPath()+"/salcontract/contract/contractEnd";
	}
	/**
	 * 删除销售合同
	 */
	@ResponseBody
	@RequiresPermissions("salcontract:contract:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(Contract contract, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		contractService.delete(contract);
		j.setMsg("删除销售合同成功");
		return j;
	}
	
	/**
	 * 批量删除销售合同
	 */
	@ResponseBody
	@RequiresPermissions("salcontract:contract:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			contractService.delete(contractService.get(id));
		}
		j.setMsg("删除销售合同成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("salcontract:contract:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(Contract contract, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "销售合同"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<Contract> page = contractService.findPage(new Page<Contract>(request, response, -1), contract);
    		new ExportExcel("销售合同", Contract.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出销售合同记录失败！失败信息："+e.getMessage());
		}
			return j;
    }
    
    @ResponseBody
    @RequestMapping(value = "detail")
	public Contract detail(String id) {
		return contractService.get(id);
	}

	@ResponseBody
    @RequestMapping(value = "getDetail")
	public Contract getDetail(String id) {
		return contractService.getByCode(id);
	}


	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("salcontract:contract:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<Contract> list = ei.getDataList(Contract.class);
			for (Contract contract : list){
				try{
					contractService.save(contract);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条销售合同记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条销售合同记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入销售合同失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/salcontract/contract/?repage";
    }
	
	/**
	 * 下载导入销售合同数据模板
	 */
	@RequiresPermissions("salcontract:contract:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "销售合同数据导入模板.xlsx";
    		List<Contract> list = Lists.newArrayList(); 
    		new ExportExcel("销售合同数据", Contract.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/salcontract/contract/?repage";
    }
	

}