/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.inventorymanage.udpinfo.web;

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
import com.hqu.modules.inventorymanage.udpinfo.entity.UdpRecord;
import com.hqu.modules.inventorymanage.udpinfo.service.UdpRecordService;

/**
 * 记录udp信息Controller
 * @author huang.youcai
 * @version 2018-08-06
 */
@Controller
@RequestMapping(value = "${adminPath}/udpinfo/udpRecord")
public class UdpRecordController extends BaseController {

	@Autowired
	private UdpRecordService udpRecordService;
	
	@ModelAttribute
	public UdpRecord get(@RequestParam(required=false) String id) {
		UdpRecord entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = udpRecordService.get(id);
		}
		if (entity == null){
			entity = new UdpRecord();
		}
		return entity;
	}
	
	/**
	 * 保存udp信息成功列表页面
	 */
	@RequiresPermissions("udpinfo:udpRecord:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "inventorymanage/udpinfo/udpRecordList";
	}
	
		/**
	 * 保存udp信息成功列表数据
	 */
	@ResponseBody
	@RequiresPermissions("udpinfo:udpRecord:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(UdpRecord udpRecord, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<UdpRecord> page = udpRecordService.findPage(new Page<UdpRecord>(request, response), udpRecord); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑保存udp信息成功表单页面
	 */
	@RequiresPermissions(value={"udpinfo:udpRecord:view","udpinfo:udpRecord:add","udpinfo:udpRecord:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(UdpRecord udpRecord, Model model) {
		model.addAttribute("udpRecord", udpRecord);
		if(StringUtils.isBlank(udpRecord.getId())){//如果ID是空为添加
			model.addAttribute("isAdd", true);
		}
		return "inventorymanage/udpinfo/udpRecordForm";
	}

	/**
	 * 保存保存udp信息成功
	 */
	@RequiresPermissions(value={"udpinfo:udpRecord:add","udpinfo:udpRecord:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(UdpRecord udpRecord, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, udpRecord)){
			return form(udpRecord, model);
		}
		//新增或编辑表单保存
		udpRecordService.save(udpRecord);//保存
		addMessage(redirectAttributes, "保存保存udp信息成功成功");
		return "redirect:"+Global.getAdminPath()+"/udpinfo/udpRecord/?repage";
	}
	
	/**
	 * 删除保存udp信息成功
	 */
	@ResponseBody
	@RequiresPermissions("udpinfo:udpRecord:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(UdpRecord udpRecord, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		udpRecordService.delete(udpRecord);
		j.setMsg("删除保存udp信息成功成功");
		return j;
	}
	
	/**
	 * 批量删除保存udp信息成功
	 */
	@ResponseBody
	@RequiresPermissions("udpinfo:udpRecord:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			udpRecordService.delete(udpRecordService.get(id));
		}
		j.setMsg("删除保存udp信息成功成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("udpinfo:udpRecord:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(UdpRecord udpRecord, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "保存udp信息成功"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<UdpRecord> page = udpRecordService.findPage(new Page<UdpRecord>(request, response, -1), udpRecord);
    		new ExportExcel("保存udp信息成功", UdpRecord.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出保存udp信息成功记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("udpinfo:udpRecord:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<UdpRecord> list = ei.getDataList(UdpRecord.class);
			for (UdpRecord udpRecord : list){
				try{
					udpRecordService.save(udpRecord);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条保存udp信息成功记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条保存udp信息成功记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入保存udp信息成功失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/udpinfo/udpRecord/?repage";
    }
	
	/**
	 * 下载导入保存udp信息成功数据模板
	 */
	@RequiresPermissions("udpinfo:udpRecord:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "保存udp信息成功数据导入模板.xlsx";
    		List<UdpRecord> list = Lists.newArrayList(); 
    		new ExportExcel("保存udp信息成功数据", UdpRecord.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/udpinfo/udpRecord/?repage";
    }

}