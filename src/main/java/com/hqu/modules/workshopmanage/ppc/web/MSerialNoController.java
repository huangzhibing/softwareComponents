/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.workshopmanage.ppc.web;

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
import com.hqu.modules.workshopmanage.ppc.entity.MSerialNo;
import com.hqu.modules.workshopmanage.ppc.service.MSerialNoService;

/**
 * 编码关联表Controller
 * @author yxb
 * @version 2018-10-31
 */
@Controller
@RequestMapping(value = "${adminPath}/mpsplan/mSerialNo")
public class MSerialNoController extends BaseController {

	@Autowired
	private MSerialNoService mSerialNoService;
	
	@ModelAttribute
	public MSerialNo get(@RequestParam(required=false) String id) {
		MSerialNo entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = mSerialNoService.get(id);
		}
		if (entity == null){
			entity = new MSerialNo();
		}
		return entity;
	}
	
	/**
	 * 编码关联表列表页面
	 */
	@RequiresPermissions("mpsplan:mSerialNo:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "workshopmanage/mpsplan/mSerialNoList";
	}
	
		/**
	 * 编码关联表列表数据
	 */
	@ResponseBody
//	@RequiresPermissions("mpsplan:mSerialNo:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(MSerialNo mSerialNo, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<MSerialNo> page = mSerialNoService.findPage(new Page<MSerialNo>(request, response), mSerialNo); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑编码关联表表单页面
	 */
	@RequiresPermissions(value={"mpsplan:mSerialNo:view","mpsplan:mSerialNo:add","mpsplan:mSerialNo:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(MSerialNo mSerialNo, Model model) {
		model.addAttribute("mSerialNo", mSerialNo);
		if(StringUtils.isBlank(mSerialNo.getId())){//如果ID是空为添加
			model.addAttribute("isAdd", true);
		}
		return "workshopmanage/mpsplan/mSerialNoForm";
	}

	/**
	 * 保存编码关联表
	 */
	@RequiresPermissions(value={"mpsplan:mSerialNo:add","mpsplan:mSerialNo:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(MSerialNo mSerialNo, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, mSerialNo)){
			return form(mSerialNo, model);
		}
		//新增或编辑表单保存
		mSerialNoService.save(mSerialNo);//保存
		addMessage(redirectAttributes, "保存编码关联表成功");
		return "redirect:"+Global.getAdminPath()+"/mpsplan/mSerialNo/?repage";
	}
	
	/**
	 * 删除编码关联表
	 */
	@ResponseBody
	@RequiresPermissions("mpsplan:mSerialNo:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(MSerialNo mSerialNo, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		mSerialNoService.delete(mSerialNo);
		j.setMsg("删除编码关联表成功");
		return j;
	}
	
	/**
	 * 批量删除编码关联表
	 */
	@ResponseBody
	@RequiresPermissions("mpsplan:mSerialNo:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			mSerialNoService.delete(mSerialNoService.get(id));
		}
		j.setMsg("删除编码关联表成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("mpsplan:mSerialNo:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(MSerialNo mSerialNo, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "编码关联表"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<MSerialNo> page = mSerialNoService.findPage(new Page<MSerialNo>(request, response, -1), mSerialNo);
    		new ExportExcel("编码关联表", MSerialNo.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出编码关联表记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("mpsplan:mSerialNo:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<MSerialNo> list = ei.getDataList(MSerialNo.class);
			for (MSerialNo mSerialNo : list){
				try{
					mSerialNoService.save(mSerialNo);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条编码关联表记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条编码关联表记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入编码关联表失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/mpsplan/mSerialNo/?repage";
    }
	
	/**
	 * 下载导入编码关联表数据模板
	 */
	@RequiresPermissions("mpsplan:mSerialNo:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "编码关联表数据导入模板.xlsx";
    		List<MSerialNo> list = Lists.newArrayList(); 
    		new ExportExcel("编码关联表数据", MSerialNo.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/mpsplan/mSerialNo/?repage";
    }

}