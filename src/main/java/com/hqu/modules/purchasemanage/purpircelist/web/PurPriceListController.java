/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.purchasemanage.purpircelist.web;

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
import com.hqu.modules.purchasemanage.purpircelist.entity.PurPriceList;
import com.hqu.modules.purchasemanage.purpircelist.service.PurPriceListService;

/**
 * 价格清单表Controller
 * @author zz
 * @version 2018-08-20
 */
@Controller
@RequestMapping(value = "${adminPath}/purpircelist/purPriceList")
public class PurPriceListController extends BaseController {

	@Autowired
	private PurPriceListService purPriceListService;
	
	@ModelAttribute
	public PurPriceList get(@RequestParam(required=false) String id) {
		PurPriceList entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = purPriceListService.get(id);
		}
		if (entity == null){
			entity = new PurPriceList();
		}
		return entity;
	}
	
	/**
	 * 价格清单列表页面
	 */
	@RequiresPermissions("purpircelist:purPriceList:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "purchasemanage/purpircelist/purPriceListList";
	}
	
		/**
	 * 价格清单列表数据
	 */
	@ResponseBody
	@RequiresPermissions("purpircelist:purPriceList:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(PurPriceList purPriceList, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<PurPriceList> page = purPriceListService.findPage(new Page<PurPriceList>(request, response), purPriceList); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑价格清单表单页面
	 */
	@RequiresPermissions(value={"purpircelist:purPriceList:view","purpircelist:purPriceList:add","purpircelist:purPriceList:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(PurPriceList purPriceList, Model model) {
		model.addAttribute("purPriceList", purPriceList);
		if(StringUtils.isBlank(purPriceList.getId())){//如果ID是空为添加
			model.addAttribute("isAdd", true);
		}
		return "purchasemanage/purpircelist/purPriceListForm";
	}

	/**
	 * 保存价格清单
	 */
	@RequiresPermissions(value={"purpircelist:purPriceList:add","purpircelist:purPriceList:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(PurPriceList purPriceList, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, purPriceList)){
			return form(purPriceList, model);
		}
		//新增或编辑表单保存
		purPriceListService.save(purPriceList);//保存
		addMessage(redirectAttributes, "保存价格清单成功");
		return "redirect:"+Global.getAdminPath()+"/purpircelist/purPriceList/?repage";
	}
	
	/**
	 * 删除价格清单
	 */
	@ResponseBody
	@RequiresPermissions("purpircelist:purPriceList:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(PurPriceList purPriceList, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		purPriceListService.delete(purPriceList);
		j.setMsg("删除价格清单成功");
		return j;
	}
	
	/**
	 * 批量删除价格清单
	 */
	@ResponseBody
	@RequiresPermissions("purpircelist:purPriceList:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			purPriceListService.delete(purPriceListService.get(id));
		}
		j.setMsg("删除价格清单成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("purpircelist:purPriceList:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(PurPriceList purPriceList, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "价格清单"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<PurPriceList> page = purPriceListService.findPage(new Page<PurPriceList>(request, response, -1), purPriceList);
    		new ExportExcel("价格清单", PurPriceList.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出价格清单记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("purpircelist:purPriceList:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<PurPriceList> list = ei.getDataList(PurPriceList.class);
			for (PurPriceList purPriceList : list){
				try{
					purPriceListService.save(purPriceList);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条价格清单记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条价格清单记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入价格清单失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/purpircelist/purPriceList/?repage";
    }
	
	/**
	 * 下载导入价格清单数据模板
	 */
	@RequiresPermissions("purpircelist:purPriceList:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "价格清单数据导入模板.xlsx";
    		List<PurPriceList> list = Lists.newArrayList(); 
    		new ExportExcel("价格清单数据", PurPriceList.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/purpircelist/purPriceList/?repage";
    }

}