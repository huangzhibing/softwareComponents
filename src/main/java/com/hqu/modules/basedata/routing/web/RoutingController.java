/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.basedata.routing.web;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import com.hqu.modules.basedata.routing.entity.RoutingItem;
import com.hqu.modules.basedata.routing.entity.RoutingWork;
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
import com.hqu.modules.basedata.item.service.ItemService;
import com.hqu.modules.basedata.product.entity.Product;
import com.hqu.modules.basedata.routing.entity.Routing;
import com.hqu.modules.basedata.routing.service.RoutingService;

/**
 * 工艺路线Controller
 * @author huang.youcai
 * @version 2018-06-02
 */
@Controller
@RequestMapping(value = "${adminPath}/routing/routing")
public class RoutingController extends BaseController {

	@Autowired
	private RoutingService routingService;
	@Autowired
	private ItemService itemService;
	@ModelAttribute
	public Routing get(@RequestParam(required=false) String id) {
		Routing entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = routingService.get(id);
//			entity.getProduct().setItem(itemService.get(entity.getProduct().getItem()));
//			entity.setItem(itemService.get(entity.getItem()));
		}
		if (entity == null){
			entity = new Routing();
		}
		return entity;
	}
	
	/**
	 * 工艺路线列表页面
	 */
	@RequiresPermissions("routing:routing:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "basedata/routing/routingList";
	}
	
		/**
	 * 工艺路线列表数据
	 */
	@ResponseBody
	@RequiresPermissions("routing:routing:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(Routing routing, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Routing> page = routingService.findPage(new Page<Routing>(request, response), routing); 
//		for(Routing routing1 : page.getList()){
//			routing1.getProduct().setItem(itemService.get(routing1.getProduct().getItem()));
//			routing1.setItem(itemService.get(routing1.getItem()));
//		}
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑工艺路线表单页面
	 */
	@RequiresPermissions(value={"routing:routing:view","routing:routing:add","routing:routing:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(Routing routing, Model model) {
		model.addAttribute("routing", routing);
		if(StringUtils.isBlank(routing.getId())){//如果ID是空为添加
			model.addAttribute("isAdd", true);
			String routingcode = routingService.getMaxRoutingCode();
			if(routingcode == null){
				routingcode = "0000";
			}
			else {
				int maxcode = Integer.parseInt(routingcode) + 1;
				routingcode = String.format("%04d", maxcode);
			}
			routing.setRoutingCode(routingcode);
		}
		return "basedata/routing/routingForm";
	}

	/**
	 * 保存工艺路线
	 */
	@RequiresPermissions(value={"routing:routing:add","routing:routing:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(Routing routing, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, routing)){
			return form(routing, model);
		}
		//判断工序表是否为空
		if(routing.getRoutingWorkList().size()==0){
			addMessage(redirectAttributes,"提交失败,工序不能为空!");
			return "redirect:"+Global.getAdminPath()+"/routing/routing/?repage";
		}else {
			boolean flag=true;
			for (RoutingWork rw : routing.getRoutingWorkList()){
				if (RoutingWork.DEL_FLAG_NORMAL.equals(rw.getDelFlag())){
					flag=false;
				}
			}
			if (flag){
				addMessage(redirectAttributes,"提交失败,工序不能为空!");
				return "redirect:"+Global.getAdminPath()+"/routing/routing/?repage";
			}
		}
		//根据产品编码和工艺编码判断唯一性
		if(routing.getId()==null||"".equals(routing.getId())){
			if(routingService.findListByCode(routing).size()!=0){
				addMessage(redirectAttributes,"提交失败,工艺编码和产品编码不唯一!");
				return "redirect:"+Global.getAdminPath()+"/routing/routing/?repage";
			}
		}
		//判断两个子表的唯一性
		List<RoutingItem> routingItemList=routing.getRoutingItemList();
		for(int i=0;i<routingItemList.size();i++){
			if(RoutingItem.DEL_FLAG_DELETE.equals(routingItemList.get(i).getDelFlag())) break;
			for (int j=i+1;j<routingItemList.size();j++){
				if(RoutingItem.DEL_FLAG_DELETE.equals(routingItemList.get(j).getDelFlag())) break;
				if(routingItemList.get(i).getItem().getCode().equals(routingItemList.get(j).getItem().getCode())){
					addMessage(redirectAttributes,"提交失败,物料不唯一!");
					return "redirect:"+Global.getAdminPath()+"/routing/routing/?repage";
				}
			}
		}
		List<RoutingWork> routingWorkList=routing.getRoutingWorkList();
		for(int i=0;i<routingWorkList.size();i++){
			if(RoutingWork.DEL_FLAG_DELETE.equals(routingWorkList.get(i).getDelFlag())) break;
			for (int j=i+1;j<routingWorkList.size();j++){
				if(RoutingWork.DEL_FLAG_DELETE.equals(routingWorkList.get(j).getDelFlag())) break;
				if(routingWorkList.get(i).getWorkProcedure().getWorkProcedureId().equals(routingWorkList.get(j).getWorkProcedure().getWorkProcedureId())){
					addMessage(redirectAttributes,"提交失败,工序不唯一!");
					return "redirect:"+Global.getAdminPath()+"/routing/routing/?repage";
				}
			}
		}
		//新增或编辑表单保存
		routingService.save(routing);//保存
		addMessage(redirectAttributes, "保存工艺路线成功");
		return "redirect:"+Global.getAdminPath()+"/routing/routing/?repage";
	}
	
	/**
	 * 删除工艺路线
	 */
	@ResponseBody
	@RequiresPermissions("routing:routing:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(Routing routing, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		routingService.delete(routing);
		j.setMsg("删除工艺路线成功");
		return j;
	}
	
	/**
	 * 批量删除工艺路线
	 */
	@ResponseBody
	@RequiresPermissions("routing:routing:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			routingService.delete(routingService.get(id));
		}
		j.setMsg("删除工艺路线成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("routing:routing:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(Routing routing, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "工艺路线"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<Routing> page = routingService.findPage(new Page<Routing>(request, response, -1), routing);
    		new ExportExcel("工艺路线", Routing.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出工艺路线记录失败！失败信息："+e.getMessage());
		}
			return j;
    }
    
    @ResponseBody
    @RequestMapping(value = "detail")
	public Routing detail(String id) {
		return routingService.get(id);
	}
	

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("routing:routing:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<Routing> list = ei.getDataList(Routing.class);
			for (Routing routing : list){
				try{
					routingService.save(routing);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条工艺路线记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条工艺路线记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入工艺路线失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/routing/routing/?repage";
    }
	
	/**
	 * 下载导入工艺路线数据模板
	 */
	@RequiresPermissions("routing:routing:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "工艺路线数据导入模板.xlsx";
    		List<Routing> list = Lists.newArrayList(); 
    		new ExportExcel("工艺路线数据", Routing.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/routing/routing/?repage";
    }
	

}