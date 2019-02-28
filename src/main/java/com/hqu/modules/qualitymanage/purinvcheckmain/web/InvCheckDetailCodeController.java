/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.qualitymanage.purinvcheckmain.web;

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
import com.hqu.modules.qualitymanage.purinvcheckmain.entity.InvCheckDetailCode;
import com.hqu.modules.qualitymanage.purinvcheckmain.service.InvCheckDetailCodeService;

/**
 * 二维码Controller
 * @author 张铮
 * @version 2018-06-22
 */
@Controller
@RequestMapping(value = "${adminPath}/purinvcheckmain/invCheckDetailCode")
public class InvCheckDetailCodeController extends BaseController {

	@Autowired
	private InvCheckDetailCodeService invCheckDetailCodeService;
	
	@ModelAttribute
	public InvCheckDetailCode get(@RequestParam(required=false) String id) {
		InvCheckDetailCode entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = invCheckDetailCodeService.get(id);
		}
		if (entity == null){
			entity = new InvCheckDetailCode();
		}
		return entity;
	}
	
	/**
	 * 到货扫码表列表页面
	 */
	@RequiresPermissions("purinvcheckmain:invCheckDetailCode:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "modules/purinvcheckmain/invCheckDetailCodeList";
	}
	
		/**
	 * 到货扫码表列表数据
	 */
	@ResponseBody
	@RequiresPermissions("purinvcheckmain:invCheckDetailCode:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(InvCheckDetailCode invCheckDetailCode, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<InvCheckDetailCode> page = invCheckDetailCodeService.findPage(new Page<InvCheckDetailCode>(request, response), invCheckDetailCode); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑到货扫码表表单页面
	 */
	@RequiresPermissions(value={"purinvcheckmain:invCheckDetailCode:view","purinvcheckmain:invCheckDetailCode:add","purinvcheckmain:invCheckDetailCode:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(InvCheckDetailCode invCheckDetailCode, Model model) {
		model.addAttribute("invCheckDetailCode", invCheckDetailCode);
		if(StringUtils.isBlank(invCheckDetailCode.getId())){//如果ID是空为添加
			model.addAttribute("isAdd", true);
		}
		return "modules/purinvcheckmain/invCheckDetailCodeForm";
	}

	/**
	 * 保存到货扫码表
	 */
	@RequiresPermissions(value={"purinvcheckmain:invCheckDetailCode:add","purinvcheckmain:invCheckDetailCode:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(InvCheckDetailCode invCheckDetailCode, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, invCheckDetailCode)){
			return form(invCheckDetailCode, model);
		}
		//新增或编辑表单保存
		invCheckDetailCodeService.save(invCheckDetailCode);//保存
		addMessage(redirectAttributes, "保存到货扫码表成功");
		return "redirect:"+Global.getAdminPath()+"/purinvcheckmain/invCheckDetailCode/?repage";
	}
	
	/**
	 * 删除到货扫码表
	 */
	@ResponseBody
	@RequiresPermissions("purinvcheckmain:invCheckDetailCode:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(InvCheckDetailCode invCheckDetailCode, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		invCheckDetailCodeService.delete(invCheckDetailCode);
		j.setMsg("删除到货扫码表成功");
		return j;
	}
	
	/**
	 * 批量删除到货扫码表
	 */
	@ResponseBody
	@RequiresPermissions("purinvcheckmain:invCheckDetailCode:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			invCheckDetailCodeService.delete(invCheckDetailCodeService.get(id));
		}
		j.setMsg("删除到货扫码表成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("purinvcheckmain:invCheckDetailCode:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(InvCheckDetailCode invCheckDetailCode, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "到货扫码表"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<InvCheckDetailCode> page = invCheckDetailCodeService.findPage(new Page<InvCheckDetailCode>(request, response, -1), invCheckDetailCode);
    		new ExportExcel("到货扫码表", InvCheckDetailCode.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出到货扫码表记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("purinvcheckmain:invCheckDetailCode:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<InvCheckDetailCode> list = ei.getDataList(InvCheckDetailCode.class);
			for (InvCheckDetailCode invCheckDetailCode : list){
				try{
					invCheckDetailCodeService.save(invCheckDetailCode);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条到货扫码表记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条到货扫码表记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入到货扫码表失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/purinvcheckmain/invCheckDetailCode/?repage";
    }
	
	/**
	 * 下载导入到货扫码表数据模板
	 */
	@RequiresPermissions("purinvcheckmain:invCheckDetailCode:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "到货扫码表数据导入模板.xlsx";
    		List<InvCheckDetailCode> list = Lists.newArrayList(); 
    		new ExportExcel("到货扫码表数据", InvCheckDetailCode.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/purinvcheckmain/invCheckDetailCode/?repage";
    }

}