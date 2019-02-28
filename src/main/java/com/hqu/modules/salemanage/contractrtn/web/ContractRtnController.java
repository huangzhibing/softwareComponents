/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.salemanage.contractrtn.web;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import com.jeeplus.modules.sys.entity.Office;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.utils.UserUtils;
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
import com.hqu.modules.salemanage.contractrtn.entity.ContractRtn;
import com.hqu.modules.salemanage.contractrtn.service.ContractRtnService;

/**
 * 销售回款管理Controller
 * @author liujiachen
 * @version 2018-07-09
 */
@Controller
@RequestMapping(value = "${adminPath}/contractrtn/contractRtn")
public class ContractRtnController extends BaseController {

	@Autowired
	private ContractRtnService contractRtnService;
	
	@ModelAttribute
	public ContractRtn get(@RequestParam(required=false) String id) {
		ContractRtn entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = contractRtnService.get(id);
		}
		if (entity == null){
			entity = new ContractRtn();
		}
		return entity;
	}
	
	/**
	 * 销售回款单据列表页面
	 */
	@RequiresPermissions("contractrtn:contractRtn:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "salemanage/contractrtn/contractRtnList";
	}
	
		/**
	 * 销售回款单据列表数据
	 */
	@ResponseBody
	@RequiresPermissions("contractrtn:contractRtn:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(ContractRtn contractRtn, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<ContractRtn> page = contractRtnService.findPage(new Page<ContractRtn>(request, response), contractRtn); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑销售回款单据表单页面
	 */
	@RequiresPermissions(value={"contractrtn:contractRtn:view","contractrtn:contractRtn:add","contractrtn:contractRtn:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(ContractRtn contractRtn, Model model) {
		model.addAttribute("contractRtn", contractRtn);
		if(StringUtils.isBlank(contractRtn.getId())){//如果ID是空为添加
			User u = UserUtils.getUser();
			//添加经办人
//			contractRtn.setInputPerson(u);
			//添加日期
			Date date =new Date();
			contractRtn.setInputDate(date);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			String newBillNum = contractRtnService.getMaxIdByTypeAndDate("hhk"+sdf.format(date));
			if(StringUtils.isEmpty(newBillNum)){
				newBillNum = "0000";
			}
			else {
				newBillNum = String.format("%04d", Integer.parseInt(newBillNum.substring(11,15))+1);
			}
			//设置单据编号
			contractRtn.setRtnBillCode("hhk" + sdf.format(date) + newBillNum);
			model.addAttribute("isAdd", true);
		}
		return "salemanage/contractrtn/contractRtnForm";
	}

	/**
	 * 保存销售回款单据
	 */
	@RequiresPermissions(value={"contractrtn:contractRtn:add","contractrtn:contractRtn:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(ContractRtn contractRtn, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, contractRtn)){
			return form(contractRtn, model);
		}
		//新增或编辑表单保存
		contractRtnService.save(contractRtn);//保存
		addMessage(redirectAttributes, "保存销售回款单据成功");
		return "redirect:"+Global.getAdminPath()+"/contractrtn/contractRtn/?repage";
	}
	
	/**
	 * 删除销售回款单据
	 */
	@ResponseBody
	@RequiresPermissions("contractrtn:contractRtn:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(ContractRtn contractRtn, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		contractRtnService.delete(contractRtn);
		j.setMsg("删除销售回款单据成功");
		return j;
	}
	
	/**
	 * 批量删除销售回款单据
	 */
	@ResponseBody
	@RequiresPermissions("contractrtn:contractRtn:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			contractRtnService.delete(contractRtnService.get(id));
		}
		j.setMsg("删除销售回款单据成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("contractrtn:contractRtn:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(ContractRtn contractRtn, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "销售回款单据"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<ContractRtn> page = contractRtnService.findPage(new Page<ContractRtn>(request, response, -1), contractRtn);
    		new ExportExcel("销售回款单据", ContractRtn.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出销售回款单据记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("contractrtn:contractRtn:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<ContractRtn> list = ei.getDataList(ContractRtn.class);
			for (ContractRtn contractRtn : list){
				try{
					contractRtnService.save(contractRtn);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条销售回款单据记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条销售回款单据记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入销售回款单据失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/contractrtn/contractRtn/?repage";
    }
	
	/**
	 * 下载导入销售回款单据数据模板
	 */
	@RequiresPermissions("contractrtn:contractRtn:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "销售回款单据数据导入模板.xlsx";
    		List<ContractRtn> list = Lists.newArrayList(); 
    		new ExportExcel("销售回款单据数据", ContractRtn.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/contractrtn/contractRtn/?repage";
    }

}