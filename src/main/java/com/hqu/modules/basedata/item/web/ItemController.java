/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.basedata.item.web;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import com.hqu.modules.inventorymanage.billmain.entity.BillDetailCode;
import com.hqu.modules.inventorymanage.billmain.entity.BillMain;
import com.hqu.modules.inventorymanage.outsourcingoutput.service.OutsourcingOutputService;
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
import com.hqu.modules.basedata.item.entity.Item;
import com.hqu.modules.basedata.item.entity.ItemClassNew;
import com.hqu.modules.basedata.item.service.ItemClassNewService;
import com.hqu.modules.basedata.item.service.ItemService;
import com.hqu.modules.basedata.unit.entity.Unit;
import com.hqu.modules.basedata.unit.service.UnitService;

/**
 * 物料定义Controller
 * @author xn
 * @version 2018-04-06
 */
@Controller
@RequestMapping(value = "${adminPath}/item/item")
public class ItemController extends BaseController {

	@Autowired
	private ItemService itemService;
	@Autowired
	private UnitService unitService;
	@Autowired
	private ItemClassNewService itemClassNewService;
	@Autowired
	private OutsourcingOutputService outsourcingOutputService;
	@ModelAttribute
	public Item get(@RequestParam(required=false) String id) {
		Item entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = itemService.get(id);
		}
		if (entity == null){
			entity = new Item();
		}
		return entity;
	}
	
	/**
	 * 物料定义列表页面
	 */
	@RequiresPermissions("item:item:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "basedata/item/itemList";
	}
	
		/**
	 * 物料定义列表数据
	 */
	@ResponseBody
	@RequiresPermissions("item:item:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(Item item, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Item> page = itemService.findPage(new Page<Item>(request, response), item);
		for (int i = 0; i < page.getList().size(); i++) {
			if (page.getList().get(i).getClassCode().getClassId().length() == 32) {
				ItemClassNew itemClassNew = itemClassNewService.get(page.getList().get(i).getClassCode().getClassId());
				page.getList().get(i).getClassCode().setClassId(itemClassNew.getClassId());
				page.getList().get(i).setClassName(itemClassNew.getName());
			}
			if (page.getList().get(i).getUnitCode().getUnitCode().length() == 32) {
				Unit unit = unitService.get(page.getList().get(i).getUnitCode().getUnitCode());
				page.getList().get(i).getUnitCode().setUnitCode(unit.getUnitCode());
				page.getList().get(i).setUnit(unit.getUnitName());
			}
			
		}
		return getBootstrapData(page);
	}
	
	@ResponseBody
	@RequiresPermissions("item:item:list")
	@RequestMapping(value = "data2")
	public Map<String, Object> data2(Item item, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Item> page = itemService.findPage2(new Page<Item>(request, response), item); 
		return getBootstrapData(page);
	}

	@ResponseBody
	@RequiresPermissions("item:item:list")
	@RequestMapping(value = "data3")
	public Map<String, Object> data3(Item item, HttpServletRequest request, HttpServletResponse response, Model model) {

		Page<Item> itemPage = new Page<>();

		item.setPage(itemPage);
		itemPage.setList(itemService.findList1(item));

		return getBootstrapData(itemPage);
	}
	
	/**
	 * @author yrg
	 * 质检与采购需要调用
	 * @param item
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@ResponseBody
	@RequiresPermissions("item:item:list")
	@RequestMapping(value = "dataRequest")
	public Map<String, Object> dataCon(Item item, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Item> page = itemService.findPage(new Page<Item>(request, response), item); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑物料定义表单页面
	 */
	@RequiresPermissions(value={"item:item:view","item:item:add","item:item:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(Item item, Model model) {
		model.addAttribute("item", item);
		if(StringUtils.isBlank(item.getId())){//如果ID是空为添加
			model.addAttribute("isAdd", true);
		}
		return "basedata/item/itemForm";
	}

	/**
	 * 保存物料定义
	 */
	@RequiresPermissions(value={"item:item:add","item:item:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(Item item, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (item.getClassCode() != null && StringUtils.isNotEmpty(item.getClassCode().getClassId())) {
			ItemClassNew itemClassNew = itemClassNewService.getByClassId(item.getClassCode().getClassId());
			item.getClassCode().setClassId(itemClassNew.getClassId());
			item.setClassName(itemClassNew.getName());
		}
		Unit unit = unitService.get(item.getUnitCode().getUnitCode());
		item.setUnit(unit.getUnitName());
		item.getUnitCode().setUnitCode(unit.getUnitCode());
		if (!beanValidator(model, item)){
			return form(item, model);
		}
		//新增或编辑表单保存
		itemService.save(item);//保存
		addMessage(redirectAttributes, "保存物料定义成功");
		return "redirect:"+Global.getAdminPath()+"/item/item/?repage";
	}
	
	/**
	 * 删除物料定义
	 */
	@ResponseBody
	@RequiresPermissions("item:item:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(Item item, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		itemService.delete(item);
		j.setMsg("删除物料定义成功");
		return j;
	}
	
	/**
	 * 批量删除物料定义
	 */
	@ResponseBody
	@RequiresPermissions("item:item:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			itemService.delete(itemService.get(id));
		}
		j.setMsg("删除物料定义成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("item:item:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(Item item, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "物料定义"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<Item> page = itemService.findPage(new Page<Item>(request, response, -1), item);
    		new ExportExcel("物料定义", Item.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出物料定义记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("item:item:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<Item> list = ei.getDataList(Item.class);
			for (Item item : list){
				try{
					itemService.save(item);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条物料定义记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条物料定义记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入物料定义失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/item/item/?repage";
    }
	
	/**
	 * 下载导入物料定义数据模板
	 */
	@RequiresPermissions("item:item:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "物料定义数据导入模板.xlsx";
    		List<Item> list = Lists.newArrayList(); 
    		new ExportExcel("物料定义数据", Item.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/item/item/?repage";
    }

	/**
	 * 获取二维码信息
	 * @param billMain
	 * @return
	 */
	@RequestMapping(value = "getItemInfo")
	@RequiresPermissions("item:item:list")
	@ResponseBody
	public List<BillDetailCode> getItemInfo(BillMain billMain){
		List<BillDetailCode> codeList = outsourcingOutputService.findCodeByBillNum(billMain.getBillNum());

		return codeList;
	}
}