/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.purchasemanage.applymainquery.web;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
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
import com.hqu.modules.purchasemanage.applymainquery.entity.ApplyDetailQuery;
import com.hqu.modules.purchasemanage.applymainquery.entity.ApplyMainQuery;
import com.hqu.modules.purchasemanage.applymainquery.service.ApplyMainQueryService;

/**
 * 采购需求查询Controller
 * @author syc
 * @version 2018-04-26
 */
@Controller
@RequestMapping(value = "${adminPath}/applymainquery/applyMainQuery")
public class ApplyMainQueryController extends BaseController {

	@Autowired
	private ApplyMainQueryService applyMainQueryService;
	
	@ModelAttribute
	public ApplyMainQuery get(@RequestParam(required=false) String id) {
		ApplyMainQuery entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = applyMainQueryService.get(id);
		}
		if (entity == null){
			entity = new ApplyMainQuery();
		}
		return entity;
	}
	
	/**
	 * 采购需求查询列表页面
	 */
	@RequiresPermissions("applymainquery:applyMainQuery:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "purchasemanage/applymainquery/applyMainQueryList";
	}
	
		/**
	 * 采购需求查询列表数据
	 */
	@ResponseBody
	@RequiresPermissions("applymainquery:applyMainQuery:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(ApplyMainQuery applyMainQuery, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<ApplyMainQuery> page = applyMainQueryService.findPage(new Page<ApplyMainQuery>(request, response), applyMainQuery); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑采购需求查询表单页面
	 */
	@RequiresPermissions(value={"applymainquery:applyMainQuery:view","applymainquery:applyMainQuery:add","applymainquery:applyMainQuery:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(ApplyMainQuery applyMainQuery, Model model) {
		//添加信息
		applyMainQuery = applyMainQueryService.addInfo2(applyMainQuery);
		List<ApplyDetailQuery> resultList = new ArrayList<ApplyDetailQuery>();
		
		
		String billStateFlag = applyMainQuery.getBillStateFlag();
		if("A".equals(billStateFlag)){
			applyMainQuery.setBillStateFlag("正在录入");
		}
		if("W".equals(billStateFlag)){
			applyMainQuery.setBillStateFlag("录入完毕");
		}
		if("E".equals(billStateFlag)){
			applyMainQuery.setBillStateFlag("审批通过");
		}
		if("B".equals(billStateFlag)){
			applyMainQuery.setBillStateFlag("审批未通过");
		}
		if("V".equals(billStateFlag)){
			applyMainQuery.setBillStateFlag("作废单据");
		}
		
		List<ApplyDetailQuery> applyDetailQueryList = applyMainQuery.getApplyDetailQueryList();
		int i=0;
		for(ApplyDetailQuery temp : applyDetailQueryList){
			temp.setSerialNum(++i);
			resultList.add(temp);
		}
		applyMainQuery.setApplyDetailQueryList(resultList);
		
		if(StringUtils.isBlank(applyMainQuery.getId())){//如果ID是空为添加
			model.addAttribute("isAdd", true);
		}
		model.addAttribute("applyMainQuery", applyMainQuery);
		return "purchasemanage/applymainquery/applyMainQueryForm";
	}

	/**
	 * 保存采购需求查询
	 */
	@RequiresPermissions(value={"applymainquery:applyMainQuery:add","applymainquery:applyMainQuery:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(ApplyMainQuery applyMainQuery, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, applyMainQuery)){
			return form(applyMainQuery, model);
		}
		//新增或编辑表单保存
		applyMainQueryService.save(applyMainQuery);//保存
		addMessage(redirectAttributes, "保存采购需求查询成功");
		return "redirect:"+Global.getAdminPath()+"/applymainquery/applyMainQuery/?repage";
	}
	
	/**
	 * 删除采购需求查询
	 */
	@ResponseBody
	@RequiresPermissions("applymainquery:applyMainQuery:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(ApplyMainQuery applyMainQuery, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		applyMainQueryService.delete(applyMainQuery);
		j.setMsg("删除采购需求查询成功");
		return j;
	}
	
	/**
	 * 批量删除采购需求查询
	 */
	@ResponseBody
	@RequiresPermissions("applymainquery:applyMainQuery:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			applyMainQueryService.delete(applyMainQueryService.get(id));
		}
		j.setMsg("删除采购需求查询成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("applymainquery:applyMainQuery:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(ApplyMainQuery applyMainQuery, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "采购需求查询"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<ApplyMainQuery> page = applyMainQueryService.findPage(new Page<ApplyMainQuery>(request, response, -1), applyMainQuery);
    		new ExportExcel("采购需求查询", ApplyMainQuery.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出采购需求查询记录失败！失败信息："+e.getMessage());
		}
			return j;
    }
    
    @ResponseBody
    @RequestMapping(value = "detail")
	public ApplyMainQuery detail(String id) {
		return applyMainQueryService.get(id);
	}
	

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("applymainquery:applyMainQuery:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<ApplyMainQuery> list = ei.getDataList(ApplyMainQuery.class);
			for (ApplyMainQuery applyMainQuery : list){
				try{
					applyMainQueryService.save(applyMainQuery);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条采购需求查询记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条采购需求查询记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入采购需求查询失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/applymainquery/applyMainQuery/?repage";
    }
	
	/**
	 * 下载导入采购需求查询数据模板
	 */
	@RequiresPermissions("applymainquery:applyMainQuery:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "采购需求查询数据导入模板.xlsx";
    		List<ApplyMainQuery> list = Lists.newArrayList(); 
    		new ExportExcel("采购需求查询数据", ApplyMainQuery.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/applymainquery/applyMainQuery/?repage";
    }
	

}