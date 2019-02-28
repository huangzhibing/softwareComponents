/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.qualitymanage.qmreport.web;

import com.google.common.collect.Lists;
import com.hqu.modules.Common.service.CommonService;
import com.hqu.modules.qualitymanage.objectdef.entity.ObjectDef;
import com.hqu.modules.qualitymanage.objectdef.service.ObjectDefService;
import com.hqu.modules.qualitymanage.purreport.entity.PurReport;
import com.hqu.modules.qualitymanage.qmreport.entity.QmReport;
import com.hqu.modules.qualitymanage.qmreport.service.QmReportService;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.common.utils.CacheUtils;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.excel.ExportExcel;
import com.jeeplus.common.utils.excel.ImportExcel;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.modules.sys.utils.UserUtils;
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
import java.util.*;

/**
 * 质量问题报告Controller
 * @author yangxianbang
 * @version 2018-04-24
 */
@Controller
@RequestMapping(value = "${adminPath}/qmreport/qmReportMachine")
public class QmReportMachineController extends BaseController {

	@Autowired
	private QmReportService qmReportService;
	@Autowired
	private CommonService commonService;
	@Autowired
	private ObjectDefService objectDefService;
	
	@ModelAttribute
	public QmReport get(@RequestParam(required=false) String id) {
		QmReport entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = qmReportService.get(id);
		}
		if (entity == null){
			entity = new QmReport();
		}
		return entity;
	}
	
	
	/**
	 * 质量问题列表页面
	 */
	@RequiresPermissions("qmreport:qmReportMachine:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "qualitymanage/qmreport/qmReportMachineList";
	}
	
	/**
	 * 检验单数据选择列表
	 */
	@RequiresPermissions("purreport:purReport:list")
	@RequestMapping(value = {"purlist"})
	public String purlist(@ModelAttribute PurReport pur,@RequestParam(required=false) String type) {
		//CacheUtils.remove("dictMap");//清除dictMap（字典类型）缓存。调BUG专用（框架存在的BUG）。
/*		if("F".equals(type)) {
			return "qualitymanage/qmreport/purReportSelectList";
		}else if("R".equals(type)){
			return "qualitymanage/qmreport/purReportRSelectList";
		}else {
			return "";
		}*/
		return "qualitymanage/qmreport/purReportMachineSelectList";
	}
	
	
		/**
	 * 采购质量问题列表数据
	 */
	@ResponseBody
	@RequiresPermissions("qmreport:qmReportMachine:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(QmReport qmReport, HttpServletRequest request, HttpServletResponse response, Model model) {
		//qmReport.setCreateBy(UserUtils.getUser());
		qmReport.setQmType("整机");
		qmReport.setState("编辑中");
		Page<QmReport> page = qmReportService.findPage(new Page<QmReport>(request, response), qmReport); 
		return getBootstrapData(page);
	}



	/**
	 * 查看，增加，编辑采购质量问题表单页面
	 */
	@RequiresPermissions(value={"qmreport:qmReportMachine:view","qmreport:qmReportMachine:add","qmreport:qmReportMachine:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(QmReport qmReport, Model model,@RequestParam(required=false) String justSee) {
		model.addAttribute("qmReport", qmReport);
		if(StringUtils.isBlank(qmReport.getId())){//如果ID是空为添加
			model.addAttribute("isAdd", true);
			qmReport.setQmreportId(getQMRid());
			qmReport.setQmPerson(UserUtils.getUser());
			qmReport.setQmDate(new Date());
			//model.addAttribute("qmreportId", getQMRid());//传递生成的业务主键QMReportID
		}else if(justSee==null) {
			model.addAttribute("isEdit",true);
		}
		
		return "qualitymanage/qmreport/qmReportMachineForm";
	}

	/**
	 * 保存采购质量问题
	 */
	@RequiresPermissions(value={"qmreport:qmReportMachine:add","qmreport:qmReportMachine:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(QmReport qmReport, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, qmReport)){
			return form(qmReport, model,null);
		}
		//新增或编辑表单保存
		qmReport.setQmType("整机");
		qmReportService.save(qmReport);//保存
		addMessage(redirectAttributes, "保存质量问题报告成功");
		return "redirect:"+Global.getAdminPath()+"/qmreport/qmReportMachine/?repage";
	}


	/**
	 *
	 */
	@ResponseBody
	@RequestMapping(value = "getObjByObjCode")
	public AjaxJson getObjByObjCode(@RequestParam(required=true)String objCode){
		AjaxJson json=new AjaxJson();
		ObjectDef objectDef=new ObjectDef();
		objectDef.setObjCode(objCode);
		List<ObjectDef> objectDefobjectList =objectDefService.findList(objectDef);
		if(objectDefobjectList.size()>0){
			json.setSuccess(true);
			json.put("objName",objectDefobjectList.get(0).getObjName());
		}else {
			json.setSuccess(false);
		}
		return json;
	}




	
	/**
	 * 删除质量问题
	 */
	@ResponseBody
	@RequiresPermissions("qmreport:qmReportMachine:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(QmReport qmReport, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		qmReportService.delete(qmReport);
		j.setMsg("删除质量问题报告成功");
		return j;
	}
	
	/**
	 * 批量删除质量问题
	 */
	@ResponseBody
	@RequiresPermissions("qmreport:qmReportMachine:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			qmReportService.delete(qmReportService.get(id));
		}
		j.setMsg("删除质量问题报告成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("qmreport:qmReportMachine:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(QmReport qmReport, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "质量问题报告"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<QmReport> page = qmReportService.findPage(new Page<QmReport>(request, response, -1), qmReport);
    		new ExportExcel("质量问题报告", QmReport.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出质量问题报告记录失败！失败信息："+e.getMessage());
		}
			return j;
    }
    
    @ResponseBody
    @RequestMapping(value = "detail")
	public QmReport detail(String id) {
		return qmReportService.get(id);
	}
	

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("qmreport:qmReportMachine:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<QmReport> list = ei.getDataList(QmReport.class);
			for (QmReport qmReport : list){
				try{
					qmReportService.save(qmReport);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条质量问题记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条质量问题记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入质量问题报告失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/qmreport/qmReportMachine/?repage";
    }
	
	/**
	 * 下载导入质量问题数据模板
	 */
	@RequiresPermissions("qmreport:qmReportMachine:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "质量问题数据导入模板.xlsx";
    		List<QmReport> list = Lists.newArrayList(); 
    		new ExportExcel("质量问题数据", QmReport.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/qmreport/qmReportMachine/?repage";
    }
	
	
	
	
	/**
	 * 生成QMReportID，其为QMReport的业务主键
	 * @return ID由QRM+年(4位)+月日(各两位)+4位随机数组成
	 */
	
	private String getQMRid() {
		String rand=formatID((new Random(System.currentTimeMillis()).nextInt(10000)),true);
		Calendar cal=Calendar.getInstance();
		String y=formatID(cal.get(Calendar.YEAR),true);     
		String m=formatID(cal.get(Calendar.MONTH)+1,false); 
		String d=formatID(cal.get(Calendar.DATE),false);
/*		String h=formatID(cal.get(Calendar.HOUR_OF_DAY),false);
		String mi=formatID(cal.get(Calendar.MINUTE),false);
		String s=formatID(cal.get(Calendar.SECOND),false);*/
		
		
		String result="QMR"+(y+m+d)+rand;
		while(commonService.getCodeNum("qms_qmreport", "qmreport_id", result)) {
			result="QMR"+(y+m+d)+formatID((new Random(System.currentTimeMillis()).nextInt(10000)),true);
		}
		return result;
	}
	/**
	 * 由于生成的时分秒可能是个位数也可能是十位数，为统一随机生成的报告编号位数一致，通过该函数标准化时分秒格式。
	 * @param raw  未处理格式的整形数
	 * @param isRand 判断要变成四位格式还是两位格式
	 * @return
	 */
	private String formatID(int raw,boolean isRand) {
		String rStr=String.valueOf(raw);
		if(isRand) {
			while(rStr.length()<4) {
				rStr="0"+rStr;
			}
		}else {
			while(rStr.length()<2) {
				rStr="0"+rStr;
			}
		}
		return rStr;
	}
	
	

}