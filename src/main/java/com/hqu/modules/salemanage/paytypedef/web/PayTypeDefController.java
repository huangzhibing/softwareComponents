/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.salemanage.paytypedef.web;

import com.google.common.collect.Lists;
import com.hqu.modules.salemanage.paytypedef.entity.PayTypeDef;
import com.hqu.modules.salemanage.paytypedef.service.PayTypeDefService;
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
import java.util.List;
import java.util.Map;

/**
 * 付款方式定义Controller
 * @author M1ngz
 * @version 2018-05-07
 */
@Controller
@RequestMapping(value = "${adminPath}/paytypedef/payTypeDef")
public class PayTypeDefController extends BaseController {

	@Autowired
	private PayTypeDefService payTypeDefService;
	
	@ModelAttribute
	public PayTypeDef get(@RequestParam(required=false) String id) {
		PayTypeDef entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = payTypeDefService.get(id);
		}
		if (entity == null){
			entity = new PayTypeDef();
		}
		return entity;
	}
	
	/**
	 * 付款方式列表页面
	 */
	@RequiresPermissions("paytypedef:payTypeDef:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "salemanage/paytypedef/payTypeDefList";
	}
	
		/**
	 * 付款方式列表数据
	 */
	@ResponseBody
	@RequiresPermissions("paytypedef:payTypeDef:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(PayTypeDef payTypeDef, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<PayTypeDef> page = payTypeDefService.findPage(new Page<PayTypeDef>(request, response), payTypeDef); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑付款方式表单页面
	 */
	@RequiresPermissions(value={"paytypedef:payTypeDef:view","paytypedef:payTypeDef:add","paytypedef:payTypeDef:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(PayTypeDef payTypeDef, Model model) {
		model.addAttribute("payTypeDef", payTypeDef);
		if(StringUtils.isBlank(payTypeDef.getId())){//如果ID是空为添加
			model.addAttribute("isAdd", true);
			String maxCode;
			if(payTypeDefService.selectMax()!=null){
				maxCode=payTypeDefService.selectMax();
				int maxNum=Integer.parseInt(maxCode);
				maxNum++;
				maxCode=String.format("%02d",maxNum);
			}else {
				maxCode="00";
			}
			payTypeDef.setTypeCode(maxCode);
		}
		return "salemanage/paytypedef/payTypeDefForm";
	}

	/**
	 * 保存付款方式
	 */
	@RequiresPermissions(value={"paytypedef:payTypeDef:add","paytypedef:payTypeDef:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(PayTypeDef payTypeDef, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, payTypeDef)){
			return form(payTypeDef, model);
		}
		//新增或编辑表单保存
		payTypeDefService.save(payTypeDef);//保存
		addMessage(redirectAttributes, "保存付款方式成功");
		return "redirect:"+Global.getAdminPath()+"/paytypedef/payTypeDef/?repage";
	}
	
	/**
	 * 删除付款方式
	 */
	@ResponseBody
	@RequiresPermissions("paytypedef:payTypeDef:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(PayTypeDef payTypeDef, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		payTypeDefService.delete(payTypeDef);
		j.setMsg("删除付款方式成功");
		return j;
	}
	
	/**
	 * 批量删除付款方式
	 */
	@ResponseBody
	@RequiresPermissions("paytypedef:payTypeDef:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			payTypeDefService.delete(payTypeDefService.get(id));
		}
		j.setMsg("删除付款方式成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("paytypedef:payTypeDef:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(PayTypeDef payTypeDef, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "付款方式"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<PayTypeDef> page = payTypeDefService.findPage(new Page<PayTypeDef>(request, response, -1), payTypeDef);
    		new ExportExcel("付款方式", PayTypeDef.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出付款方式记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("paytypedef:payTypeDef:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<PayTypeDef> list = ei.getDataList(PayTypeDef.class);
			for (PayTypeDef payTypeDef : list){
				try{
					payTypeDefService.save(payTypeDef);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条付款方式记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条付款方式记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入付款方式失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/paytypedef/payTypeDef/?repage";
    }
	
	/**
	 * 下载导入付款方式数据模板
	 */
	@RequiresPermissions("paytypedef:payTypeDef:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "付款方式数据导入模板.xlsx";
    		List<PayTypeDef> list = Lists.newArrayList(); 
    		new ExportExcel("付款方式数据", PayTypeDef.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/paytypedef/payTypeDef/?repage";
    }

}