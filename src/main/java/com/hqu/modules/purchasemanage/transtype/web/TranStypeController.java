/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.purchasemanage.transtype.web;

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
import com.hqu.modules.purchasemanage.transtype.entity.TranStype;
import com.hqu.modules.purchasemanage.transtype.service.TranStypeService;

/**
 * 运输方式定义Controller
 * @author 石豪迈
 * @version 2018-04-14
 */
@Controller
@RequestMapping(value = "${adminPath}/transtype/tranStype")
public class TranStypeController extends BaseController {

	@Autowired
	private TranStypeService tranStypeService;
	
	@ModelAttribute
	public TranStype get(@RequestParam(required=false) String id) {
		TranStype entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = tranStypeService.get(id);
		}
		if (entity == null){
			entity = new TranStype();
		}
		return entity;
	}
	
	/**
	 * 保存成功列表页面
	 */
	@RequiresPermissions("transtype:tranStype:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "purchasemanage/transtype/tranStypeList";
	}
	
		/**
	 * 保存成功列表数据
	 */
	@ResponseBody
	@RequiresPermissions("transtype:tranStype:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(TranStype tranStype, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<TranStype> page = tranStypeService.findPage(new Page<TranStype>(request, response), tranStype); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑保存成功表单页面
	 */
	@RequiresPermissions(value={"transtype:tranStype:view","transtype:tranStype:add","transtype:tranStype:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(TranStype tranStype, Model model) {
		model.addAttribute("tranStype", tranStype);
		if(StringUtils.isBlank(tranStype.getId())){//如果ID是空为添加
			model.addAttribute("isAdd", true);
			//设置transtypeid从0开始自增长并为2位流水压入表单中
			String transtypeid = tranStypeService.getMaxTranstypeid();
			if(transtypeid == null) {
				transtypeid = "00";
			}
			else {
				int maxnum = Integer.parseInt(transtypeid) + 1;
				transtypeid = String.format("%02d",maxnum);
			}

			tranStype.setTranstypeid(transtypeid);
		}
		return "purchasemanage/transtype/tranStypeForm";
	}

	/**
	 * 保存保存成功
	 */
	@RequiresPermissions(value={"transtype:tranStype:add","transtype:tranStype:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(TranStype tranStype, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, tranStype)){
			return form(tranStype, model);
		}
		//新增或编辑表单保存
		tranStypeService.save(tranStype);//保存
		addMessage(redirectAttributes, "保存保存成功成功");
		return "redirect:"+Global.getAdminPath()+"/transtype/tranStype/?repage";
	}
	
	/**
	 * 删除保存成功
	 */
	@ResponseBody
	@RequiresPermissions("transtype:tranStype:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(TranStype tranStype, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		tranStypeService.delete(tranStype);
		j.setMsg("删除保存成功成功");
		return j;
	}
	
	/**
	 * 批量删除保存成功
	 */
	@ResponseBody
	@RequiresPermissions("transtype:tranStype:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			tranStypeService.delete(tranStypeService.get(id));
		}
		j.setMsg("删除保存成功成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("transtype:tranStype:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(TranStype tranStype, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "保存成功"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<TranStype> page = tranStypeService.findPage(new Page<TranStype>(request, response, -1), tranStype);
    		new ExportExcel("保存成功", TranStype.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出保存成功记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("transtype:tranStype:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<TranStype> list = ei.getDataList(TranStype.class);
			for (TranStype tranStype : list){
				try{
					tranStypeService.save(tranStype);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条保存成功记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条保存成功记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入保存成功失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/transtype/tranStype/?repage";
    }
	
	/**
	 * 下载导入保存成功数据模板
	 */
	@RequiresPermissions("transtype:tranStype:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "保存成功数据导入模板.xlsx";
    		List<TranStype> list = Lists.newArrayList(); 
    		new ExportExcel("保存成功数据", TranStype.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/transtype/tranStype/?repage";
    }

}