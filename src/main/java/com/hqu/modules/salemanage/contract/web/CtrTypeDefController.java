/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.salemanage.contract.web;

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
import com.hqu.modules.salemanage.contract.entity.CtrTypeDef;
import com.hqu.modules.salemanage.contract.service.CtrTypeDefService;

/**
 * 合同类型Controller
 * @author dongqida
 * @version 2018-05-05
 */
@Controller
@RequestMapping(value = "${adminPath}/contract/ctrTypeDef")
public class CtrTypeDefController extends BaseController {

	@Autowired
	private CtrTypeDefService ctrTypeDefService;
	
	@ModelAttribute
	public CtrTypeDef get(@RequestParam(required=false) String id) {
		CtrTypeDef entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = ctrTypeDefService.get(id);
		}
		if (entity == null){
			entity = new CtrTypeDef();
		}
		return entity;
	}
	
	/**
	 * 合同类型列表页面
	 */
	@RequiresPermissions("contract:ctrTypeDef:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "salemanage/contract/ctrTypeDefList";
	}
	
		/**
	 * 合同类型列表数据
	 */
	@ResponseBody
	@RequiresPermissions("contract:ctrTypeDef:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(CtrTypeDef ctrTypeDef, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<CtrTypeDef> page = ctrTypeDefService.findPage(new Page<CtrTypeDef>(request, response), ctrTypeDef); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑合同类型表单页面
	 */
	@RequiresPermissions(value={"contract:ctrTypeDef:view","contract:ctrTypeDef:add","contract:ctrTypeDef:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(CtrTypeDef ctrTypeDef, Model model) {
		model.addAttribute("ctrTypeDef", ctrTypeDef);
		if(StringUtils.isBlank(ctrTypeDef.getId())){//如果ID是空为添加
			model.addAttribute("isAdd", true);
		}
		return "salemanage/contract/ctrTypeDefForm";
	}

	/**
	 * 保存合同类型
	 */
	@RequiresPermissions(value={"contract:ctrTypeDef:add","contract:ctrTypeDef:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(CtrTypeDef ctrTypeDef, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, ctrTypeDef)){
			return form(ctrTypeDef, model);
		}
		//新增或编辑表单保存
		ctrTypeDefService.save(ctrTypeDef);//保存
		addMessage(redirectAttributes, "保存合同类型成功");
		return "redirect:"+Global.getAdminPath()+"/contract/ctrTypeDef/?repage";
	}
	
	/**
	 * 删除合同类型
	 */
	@ResponseBody
	@RequiresPermissions("contract:ctrTypeDef:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(CtrTypeDef ctrTypeDef, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		ctrTypeDefService.delete(ctrTypeDef);
		j.setMsg("删除合同类型成功");
		return j;
	}
	
	/**
	 * 批量删除合同类型
	 */
	@ResponseBody
	@RequiresPermissions("contract:ctrTypeDef:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			ctrTypeDefService.delete(ctrTypeDefService.get(id));
		}
		j.setMsg("删除合同类型成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("contract:ctrTypeDef:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(CtrTypeDef ctrTypeDef, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "合同类型"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<CtrTypeDef> page = ctrTypeDefService.findPage(new Page<CtrTypeDef>(request, response, -1), ctrTypeDef);
    		new ExportExcel("合同类型", CtrTypeDef.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出合同类型记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("contract:ctrTypeDef:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<CtrTypeDef> list = ei.getDataList(CtrTypeDef.class);
			for (CtrTypeDef ctrTypeDef : list){
				try{
					ctrTypeDefService.save(ctrTypeDef);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条合同类型记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条合同类型记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入合同类型失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/contract/ctrTypeDef/?repage";
    }
	
	/**
	 * 下载导入合同类型数据模板
	 */
	@RequiresPermissions("contract:ctrTypeDef:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "合同类型数据导入模板.xlsx";
    		List<CtrTypeDef> list = Lists.newArrayList(); 
    		new ExportExcel("合同类型数据", CtrTypeDef.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/contract/ctrTypeDef/?repage";
    }

}