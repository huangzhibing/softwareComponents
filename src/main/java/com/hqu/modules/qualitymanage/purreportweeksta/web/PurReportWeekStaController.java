/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.qualitymanage.purreportweeksta.web;

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
import com.hqu.modules.qualitymanage.purreportweeksta.entity.PurReportWeekSta;
import com.hqu.modules.qualitymanage.purreportweeksta.service.PurReportWeekStaService;

/**
 * 进料检验统计Controller
 * @author yxb
 * @version 2018-08-25
 */
@Controller
@RequestMapping(value = "${adminPath}/purreportweeksta/purReportWeekSta")
public class PurReportWeekStaController extends BaseController {

	@Autowired
	private PurReportWeekStaService purReportWeekStaService;
	
	@ModelAttribute
	public PurReportWeekSta get(@RequestParam(required=false) String id) {
		PurReportWeekSta entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = purReportWeekStaService.get(id);
		}
		if (entity == null){
			entity = new PurReportWeekSta();
		}
		return entity;
	}
	
	/**
	 * 进料检验统计列表页面
	 */
	@RequiresPermissions("purreportweeksta:purReportWeekSta:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "qualitymanage/purreportweeksta/purReportWeekStaList";
	}
	
		/**
	 * 进料检验统计列表数据
	 */
	@ResponseBody
	@RequiresPermissions("purreportweeksta:purReportWeekSta:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(PurReportWeekSta purReportWeekSta, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<PurReportWeekSta> page = purReportWeekStaService.findPage(new Page<PurReportWeekSta>(request, response), purReportWeekSta); 
		if(page.getCount()==0){page.setCount(5);}//缓存原因有时会造成页面条数显示为0
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑进料检验统计表单页面
	 */
	@RequiresPermissions(value={"purreportweeksta:purReportWeekSta:view","purreportweeksta:purReportWeekSta:add","purreportweeksta:purReportWeekSta:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(PurReportWeekSta purReportWeekSta, Model model) {
		model.addAttribute("purReportWeekSta", purReportWeekSta);
		if(StringUtils.isBlank(purReportWeekSta.getId())){//如果ID是空为添加
			model.addAttribute("isAdd", true);
		}
		return "qualitymanage/purreportweeksta/purReportWeekStaForm";
	}

	/**
	 * 保存进料检验统计
	 */
	@RequiresPermissions(value={"purreportweeksta:purReportWeekSta:add","purreportweeksta:purReportWeekSta:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(PurReportWeekSta purReportWeekSta, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, purReportWeekSta)){
			return form(purReportWeekSta, model);
		}
		//新增或编辑表单保存
		purReportWeekStaService.save(purReportWeekSta);//保存
		addMessage(redirectAttributes, "保存进料检验统计成功");
		return "redirect:"+Global.getAdminPath()+"/purreportweeksta/purReportWeekSta/?repage";
	}
	
	/**
	 * 删除进料检验统计
	 */
	@ResponseBody
	@RequiresPermissions("purreportweeksta:purReportWeekSta:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(PurReportWeekSta purReportWeekSta, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		purReportWeekStaService.delete(purReportWeekSta);
		j.setMsg("删除进料检验统计成功");
		return j;
	}
	
	/**
	 * 批量删除进料检验统计
	 */
	@ResponseBody
	@RequiresPermissions("purreportweeksta:purReportWeekSta:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			purReportWeekStaService.delete(purReportWeekStaService.get(id));
		}
		j.setMsg("删除进料检验统计成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("purreportweeksta:purReportWeekSta:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(PurReportWeekSta purReportWeekSta, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "进料检验统计"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<PurReportWeekSta> page = purReportWeekStaService.findPage(new Page<PurReportWeekSta>(request, response, -1), purReportWeekSta);
    		new ExportExcel("进料检验统计", PurReportWeekSta.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出进料检验统计记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("purreportweeksta:purReportWeekSta:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<PurReportWeekSta> list = ei.getDataList(PurReportWeekSta.class);
			for (PurReportWeekSta purReportWeekSta : list){
				try{
					purReportWeekStaService.save(purReportWeekSta);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条进料检验统计记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条进料检验统计记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入进料检验统计失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/purreportweeksta/purReportWeekSta/?repage";
    }
	
	/**
	 * 下载导入进料检验统计数据模板
	 */
	@RequiresPermissions("purreportweeksta:purReportWeekSta:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "进料检验统计数据导入模板.xlsx";
    		List<PurReportWeekSta> list = Lists.newArrayList(); 
    		new ExportExcel("进料检验统计数据", PurReportWeekSta.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/purreportweeksta/purReportWeekSta/?repage";
    }

}