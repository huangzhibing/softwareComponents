/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.qualitymanage.qmreportquery.web;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
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
import com.hqu.modules.qualitymanage.qmreportquery.entity.QmReportQuery;
import com.hqu.modules.qualitymanage.qmreportquery.service.QmReportQueryService;

/**
 * 查询质量问题处理结果Controller
 * @author yangxianbang
 * @version 2018-04-17
 */
@Controller
@RequestMapping(value = "${adminPath}/qmreportquery/qmReportDeal")
public class QmReportDealController extends BaseController {

	@Autowired
	private QmReportQueryService qmReportQueryService;
	
	@ModelAttribute
	public QmReportQuery get(@RequestParam(required=false) String id) {
		QmReportQuery entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = qmReportQueryService.get(id);
		}
		if (entity == null){
			entity = new QmReportQuery();
		}
		return entity;
	}
	
	/**
	 * 质量问题报告列表页面
	 */
	@RequiresPermissions("qmreportquery:qmReportDeal:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "qualitymanage/qmreportquery/qmReportDealList";
	}
	
		/**
	 * 质量问题报告列表数据
	 */
	@ResponseBody
	@RequiresPermissions("qmreportquery:qmReportDeal:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(QmReportQuery qmReportQuery, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<QmReportQuery> page = qmReportQueryService.findPage(new Page<QmReportQuery>(request, response), qmReportQuery); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑质量问题报告表单页面
	 */
	@RequiresPermissions(value={"qmreportquery:qmReportDeal:view","qmreportquery:qmReportDeal:add","qmreportquery:qmReportDeal:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(QmReportQuery qmReportQuery, Model model) {
		model.addAttribute("qmReportQuery", qmReportQuery);
		if(StringUtils.isBlank(qmReportQuery.getId())){//如果ID是空为添加
			model.addAttribute("isAdd", true);
		}
		return "qualitymanage/qmreportquery/qmReportDealForm";
	}

	/**
	 * 保存质量问题报告
	 */
	@RequiresPermissions(value={"qmreportquery:qmReportDeal:add","qmreportquery:qmReportDeal:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(QmReportQuery qmReportQuery, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, qmReportQuery)){
			return form(qmReportQuery, model);
		}
		//新增或编辑表单保存
		qmReportQueryService.save(qmReportQuery);//保存
		addMessage(redirectAttributes, "保存质量问题报告成功");
		return "redirect:"+Global.getAdminPath()+"/qmreportquery/qmReportDeal/?repage";
	}
	
	/**
	 * 删除质量问题报告
	 */
	@ResponseBody
	@RequiresPermissions("qmreportquery:qmReportDeal:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(QmReportQuery qmReportQuery, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		qmReportQueryService.delete(qmReportQuery);
		j.setMsg("删除质量问题报告成功");
		return j;
	}
	
	/**
	 * 批量删除质量问题报告
	 */
	@ResponseBody
	@RequiresPermissions("qmreportquery:qmReportDeal:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			qmReportQueryService.delete(qmReportQueryService.get(id));
		}
		j.setMsg("删除质量问题报告成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("qmreportquery:qmReportDeal:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(QmReportQuery qmReportQuery, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "质量问题报告"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<QmReportQuery> page = qmReportQueryService.findPage(new Page<QmReportQuery>(request, response, -1), qmReportQuery);
    		new ExportExcel("质量问题报告", QmReportQuery.class).setDataList(page.getList()).write(response, fileName).dispose();
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
	public QmReportQuery detail(String id) {
		return qmReportQueryService.get(id);
	}
	

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("qmreportquery:qmReportDeal:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<QmReportQuery> list = ei.getDataList(QmReportQuery.class);
			for (QmReportQuery qmReportQuery : list){
				try{
					qmReportQueryService.save(qmReportQuery);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条质量问题报告记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条质量问题报告记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入质量问题报告失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/qmreportquery/qmReportDeal/?repage";
    }
	
	/**
	 * 下载导入质量问题报告数据模板
	 */
	@RequiresPermissions("qmreportquery:qmReportDeal:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "质量问题报告数据导入模板.xlsx";
    		List<QmReportQuery> list = Lists.newArrayList(); 
    		new ExportExcel("质量问题报告数据", QmReportQuery.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/qmreportquery/qmReportDeal/?repage";
    }
	

}