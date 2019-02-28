/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.costmanage.cositem.web;

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
import com.hqu.modules.costmanage.cositem.entity.CosItem;
import com.hqu.modules.costmanage.cositem.entity.CosItemLeft;
import com.hqu.modules.costmanage.cositem.service.CosItemLeftService;
import com.hqu.modules.costmanage.cositem.service.CosItemService;

/**
 * 科目定义Controller
 * @author zz
 * @version 2018-09-05
 */
@Controller
@RequestMapping(value = "${adminPath}/cositem/cosItem")
public class CosItemController extends BaseController {

	@Autowired
	private CosItemService cosItemService;
	@Autowired
	private CosItemLeftService cosItemLeftService;
	
	@ModelAttribute
	public CosItem get(@RequestParam(required=false) String id) {
		CosItem entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = cosItemService.get(id);
		}
		if (entity == null){
			entity = new CosItem();
		}
		return entity;
	}
	
	/**
	 * 科目定义列表页面
	 */
	@RequiresPermissions("cositem:cosItem:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "costmanage/cositem/cosItemList";
	}
	
		/**
	 * 科目定义列表数据
	 */
	@ResponseBody
	@RequiresPermissions("cositem:cosItem:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(CosItem cosItem, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<CosItem> page = cosItemService.findPage(new Page<CosItem>(request, response), cosItem); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑科目定义表单页面
	 */
	//@RequiresPermissions(value={"cositem:cosItem:view","cositem:cosItem:add","cositem:cosItem:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(CosItem cosItem, Model model) {
		model.addAttribute("cosItem", cosItem);
		if(StringUtils.isBlank(cosItem.getId())){//如果ID是空为添加
			model.addAttribute("isAdd", true);
		}
		return "costmanage/cositem/cosItemForm";
	}
	
	/**
	 * 新节点自动编号
	 */
	@ResponseBody
	@RequestMapping(value = "autoNumbering")
	public String autoNumbering(@RequestParam(required=false, value="id") String id, HttpServletRequest request, HttpServletResponse response, Model model){
		CosItemLeft cosItemLeft = new CosItemLeft();
		List<CosItemLeft> listcos = cosItemLeftService.findList(cosItemLeft);
		int max = -1;
		int count = 0;
		if(!listcos.isEmpty()){
			for(CosItemLeft cosileft : listcos){
				int temp = Integer.parseInt(cosileft.getItemCode());
				if(id.equals(cosileft.getParentId())){
					max = temp > max ? temp : max;
					count ++;
				}
			}
			if(!id.isEmpty()){
				//当有子节点时，取子节点最大的编号加一
				if(count != 0){
					return "0" + String.valueOf(max + 1);
				}
				cosItemLeft.setId(id);
				cosItemLeft = cosItemLeftService.get(id);
				//当为叶子节点时，创建新的子编号
				return cosItemLeft.getItemCode() + "01";
			}
			return "-1";
		}
		else{
			return "01";
		}
		//cosItemLeft.setId(id);
		//cosItemLeft = cosItemLeftService.findList(cosItemLeft).get(0);
		//String max = cosItemLeftService.getMaximumCode(cosItemLeft);
		
	}
	
	/**
	 * 新节点自动编号
	 */
	@ResponseBody
	@RequestMapping(value = "autoNumberingRight")
	public String autoNumberingRight(@RequestParam(required=false, value="id") String id, HttpServletRequest request, HttpServletResponse response, Model model){
		CosItem cosItem = new CosItem();
		List<CosItem> listcos = cosItemService.findList(cosItem);
		int max = -1;
		int count = 0;
		if(!listcos.isEmpty()){
			for(CosItem cosi : listcos){
				int temp = Integer.parseInt(cosi.getItemCode());
				if(id.equals(cosi.getFatherId().getId())){
					max = temp > max ? temp : max;
					count ++;
				}
			}
			if(!id.isEmpty()){
				//当有子节点时，取子节点最大的编号加一
				if(count != 0){
					return "0" + String.valueOf(max + 1);
				}
				CosItemLeft cosItemLeft = cosItemLeftService.get(id);
				//当为叶子节点时，创建新的子编号
				return cosItemLeft.getItemCode() + "01";
			}
			return "-1";
		}
		else{
			return "01";
		}
		//cosItemLeft.setId(id);
		//cosItemLeft = cosItemLeftService.findList(cosItemLeft).get(0);
		//String max = cosItemLeftService.getMaximumCode(cosItemLeft);
		
	}
	
	/**
	 * 保存科目定义
	 */
	@RequiresPermissions(value={"cositem:cosItem:add","cositem:cosItem:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(CosItem cosItem, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, cosItem)){
			return form(cosItem, model);
		}
		int level = 0;
		if(cosItem.getItemCode() != null && cosItem.getItemCode() != ""){
			level = cosItem.getItemCode().length()/2;
		}
		
		cosItem.setItemClass(String.valueOf(level));
		cosItem.setItemFinish("Y");
		//新增或编辑表单保存
		cosItemService.save(cosItem);//保存
		addMessage(redirectAttributes, "保存科目定义成功");
		return "redirect:"+Global.getAdminPath()+"/cositem/cosItem/?repage";
	}
	
	/**
	 * 删除科目定义
	 */
	@ResponseBody
	//@RequiresPermissions("cositem:cosItem:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(CosItem cosItem, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		cosItemService.delete(cosItem);
		j.setMsg("删除科目定义成功");
		return j;
	}
	
	/**
	 * 批量删除科目定义
	 */
	@ResponseBody
	//@RequiresPermissions("cositem:cosItem:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			cosItemService.delete(cosItemService.get(id));
		}
		j.setMsg("删除科目定义成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("cositem:cosItem:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(CosItem cosItem, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "科目定义"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<CosItem> page = cosItemService.findPage(new Page<CosItem>(request, response, -1), cosItem);
    		new ExportExcel("科目定义", CosItem.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出科目定义记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("cositem:cosItem:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<CosItem> list = ei.getDataList(CosItem.class);
			for (CosItem cosItem : list){
				try{
					cosItemService.save(cosItem);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条科目定义记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条科目定义记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入科目定义失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/cositem/cosItem/?repage";
    }
	
	/**
	 * 下载导入科目定义数据模板
	 */
	@RequiresPermissions("cositem:cosItem:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "科目定义数据导入模板.xlsx";
    		List<CosItem> list = Lists.newArrayList(); 
    		new ExportExcel("科目定义数据", CosItem.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/cositem/cosItem/?repage";
    }

}