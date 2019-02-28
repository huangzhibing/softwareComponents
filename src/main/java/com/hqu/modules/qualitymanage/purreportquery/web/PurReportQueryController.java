/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.qualitymanage.purreportquery.web;

import com.google.common.collect.Lists;
import com.hqu.modules.qualitymanage.purreportquery.entity.PurReportQuery;
import com.hqu.modules.qualitymanage.purreportquery.mapper.PurReportQueryMapper;
import com.hqu.modules.qualitymanage.purreportquery.service.PurReportQueryService;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.excel.ExportExcel;
import com.jeeplus.common.utils.excel.ImportExcel;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.modules.sys.entity.Office;
import com.jeeplus.modules.sys.entity.User;
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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 检验单查询(采购/装配/整机检测)Controller
 * @author 孙映川
 * @version 2018-04-17
 */
@Controller
@RequestMapping(value = "${adminPath}/purreportquery/purReportQuery")
public class PurReportQueryController extends BaseController {

	@Autowired
	private PurReportQueryService purReportQueryService;
	
	@Autowired
	private PurReportQueryMapper purReportQueryMapper;
	
	@ModelAttribute
	public PurReportQuery get(@RequestParam(required=false) String id) {
		PurReportQuery entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = purReportQueryService.get(id);
		}
		if (entity == null){
			entity = new PurReportQuery();
		}
		return entity;
	}
	
	/**
	 * 检验单查询列表页面
	 */
	@RequiresPermissions("purreportquery:purReportQuery:list")
	@RequestMapping(value = {"list", ""})
	public String list(Model model) {
		User currentUser = UserUtils.getUser();
		List<User> userList = new ArrayList<User>();
		List<Office> officeList = purReportQueryMapper.getOfficeListByUserId(currentUser.getId());
		Iterator<Office> it = officeList.iterator();
		while(it.hasNext()){
			Office next = it.next();
			List<User> user = purReportQueryMapper.getUserByOfficeId(next.getId());
			userList.addAll(user);
//			purReportQuery.setOffice(next);
//			List<PurReportQuery> findListResult =  mapper.findList(purReportQuery);
//			listSum.addAll(findListResult);
		}
		model.addAttribute("officeList", officeList);
		model.addAttribute("userList", userList);
		return "qualitymanage/purreportquery/purReportQueryList";
	}
	
		/**
	 * 检验单查询列表数据
	 */
	@ResponseBody
	@RequiresPermissions("purreportquery:purReportQuery:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(PurReportQuery purReportQuery, HttpServletRequest request, HttpServletResponse response, Model model) {
		//Page<PurReportQuery> page = purReportQueryService.findPage(new Page<PurReportQuery>(request, response), purReportQuery); 
		Page<PurReportQuery> page = purReportQueryService.findyMyPage(new Page<PurReportQuery>(request, response), purReportQuery); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑检验单查询表单页面
	 */
	@RequiresPermissions(value={"purreportquery:purReportQuery:view","purreportquery:purReportQuery:add","purreportquery:purReportQuery:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(PurReportQuery purReportQuery, Model model) {
		purReportQuery = purReportQueryService.get(purReportQuery.getId());
		model.addAttribute("purReportQuery", purReportQuery);
		if(StringUtils.isBlank(purReportQuery.getId())){//如果ID是空为添加
			model.addAttribute("isAdd", true);
		}
		
		String checktName = purReportQuery.getChecktName();
		if("采购".equals(checktName)){
			return "qualitymanage/purreportquery/purReportQueryForm";
		}
		if("装配".equals(checktName)){
			return "qualitymanage/purreportquery/purReportQueryFormAsse";
			
		}
		if("整机".equals(checktName)){
			return "qualitymanage/purreportquery/purReportQueryFormCom";
		}
		if("超期复验".equals(checktName)){
			return "qualitymanage/purreportquery/ReInvPurReportFormDetail";
		}
		return "qualitymanage/purreportquery/purReportQueryForm";
	}

	/**
	 * 保存检验单查询
	 */
	@RequiresPermissions(value={"purreportquery:purReportQuery:add","purreportquery:purReportQuery:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(PurReportQuery purReportQuery, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, purReportQuery)){
			return form(purReportQuery, model);
		}
		//新增或编辑表单保存
		purReportQueryService.save(purReportQuery);//保存
		addMessage(redirectAttributes, "保存检验单查询成功");
		return "redirect:"+Global.getAdminPath()+"/purreportquery/purReportQuery/?repage";
	}
	
	/**
	 * 删除检验单查询
	 */
	@ResponseBody
	@RequiresPermissions("purreportquery:purReportQuery:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(PurReportQuery purReportQuery, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		purReportQueryService.delete(purReportQuery);
		j.setMsg("删除检验单查询成功");
		return j;
	}
	
	/**
	 * 批量删除检验单查询
	 */
	@ResponseBody
	@RequiresPermissions("purreportquery:purReportQuery:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			purReportQueryService.delete(purReportQueryService.get(id));
		}
		j.setMsg("删除检验单查询成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("purreportquery:purReportQuery:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(PurReportQuery purReportQuery, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "检验单查询"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<PurReportQuery> page = purReportQueryService.findPage(new Page<PurReportQuery>(request, response, -1), purReportQuery);
    		new ExportExcel("检验单查询", PurReportQuery.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出检验单查询记录失败！失败信息："+e.getMessage());
		}
			return j;
    }
    
    @ResponseBody
    @RequestMapping(value = "detail")
	public PurReportQuery detail(String id) {
		return purReportQueryService.get(id);
	}
	

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("purreportquery:purReportQuery:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<PurReportQuery> list = ei.getDataList(PurReportQuery.class);
			for (PurReportQuery purReportQuery : list){
				try{
					purReportQueryService.save(purReportQuery);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条检验单查询记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条检验单查询记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入检验单查询失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/purreportquery/purReportQuery/?repage";
    }
	
	/**
	 * 下载导入检验单查询数据模板
	 */
	@RequiresPermissions("purreportquery:purReportQuery:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "检验单查询数据导入模板.xlsx";
    		List<PurReportQuery> list = Lists.newArrayList(); 
    		new ExportExcel("检验单查询数据", PurReportQuery.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/purreportquery/purReportQuery/?repage";
    }
	

}