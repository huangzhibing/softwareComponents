/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.basedata.itemclass.web;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.hqu.modules.basedata.item.service.ItemService;
import com.hqu.modules.basedata.itemclass.entity.ItemClass;
import com.hqu.modules.basedata.itemclass.entity.ItemClassF;
import com.hqu.modules.basedata.itemclass.service.ItemClassFService;
import com.hqu.modules.basedata.itemclass.service.ItemClassService;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.excel.ExportExcel;
import com.jeeplus.common.utils.excel.ImportExcel;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.web.BaseController;


/**
 * 物料附表Controller
 * 
 * @author dongqida
 * @version 2018-04-02
 */
@Controller
@RequestMapping(value = "${adminPath}/itemclass/itemClassF")
public class ItemClassFController extends BaseController {

	@Autowired
	private ItemClassFService itemClassFService;

	@Autowired
	private ItemClassService itemClassService;

	@ModelAttribute
	public ItemClassF get(@RequestParam(required = false) String id) {
		ItemClassF entity = null;
		if (StringUtils.isNotBlank(id)) {
			entity = itemClassFService.get(id);
		}
		if (entity == null) {
			entity = new ItemClassF();
		}
		return entity;
	}

	/**
	 * 物料附表列表页面
	 */
	@RequiresPermissions("itemclass:itemClassF:list")
	@RequestMapping(value = { "list", "" })
	public String list() {
		return "basedata/itemclass/itemClassFList";
	}

	/**
	 * 物料附表列表数据
	 */
	@ResponseBody
	@RequiresPermissions("itemclass:itemClassF:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(ItemClassF itemClassF, HttpServletRequest request, HttpServletResponse response,
			Model model) {
		Page<ItemClassF> page = itemClassFService.findPage(new Page<ItemClassF>(request, response), itemClassF);
		if(itemClassF.getFatherClassCode().getId()!=null&&!"".equals(itemClassF.getFatherClassCode().getId())) {
			ItemClass itemClass=itemClassService.findByClassId(itemClassF.getFatherClassCode().getId());
			ItemClassF itemF=itemClassFService.findByCode(itemClass.getClassId());
			//System.out.println(itemF.getFatherClassCode().getName());
			page.getList().add(itemF);
			Collections.sort(page.getList(), new Comparator<ItemClassF>() {
				@Override
				public int compare(ItemClassF o1, ItemClassF o2) {
					// TODO Auto-generated method stub
					return o1.getClassCode().compareTo(o2.getClassCode());
				}
			});
		}
		
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑物料附表表单页面
	 */
	@RequiresPermissions(value = { "itemclass:itemClassF:view", "itemclass:itemClassF:add",
			"itemclass:itemClassF:edit" }, logical = Logical.OR)
	@RequestMapping(value = "form") 
	public String form(ItemClassF itemClassF, Model model) {
		String maxCode = itemClassFService.findMaxCode();
		maxCode = String.format("%02d", Integer.parseInt(maxCode)+1);
		itemClassF.setClassCode(maxCode);
		model.addAttribute("itemClassF", itemClassF);
		if (StringUtils.isBlank(itemClassF.getId())) {// 如果ID是空为添加
			model.addAttribute("isAdd", true);
		}
		return "basedata/itemclass/itemClassFForm";
	}

	/**
	 * 保存物料附表
	 * 在插入附表的同时在维护树的表中插入节点记录
	 */
	@Transactional
	@RequiresPermissions(value = { "itemclass:itemClassF:add", "itemclass:itemClassF:edit" }, logical = Logical.OR)
	@RequestMapping(value = "save")
	public String save(ItemClassF itemClassF, Model model, RedirectAttributes redirectAttributes) throws Exception {
		if (!beanValidator(model, itemClassF)) {
			return form(itemClassF, model);
		}
		ItemClass item = new ItemClass();
		item.setSort(30);
		item.setName(itemClassF.getClassName());
		if (itemClassF.getId() != null && !"".equals(itemClassF.getId())) {
			String classCode = itemClassFService.findCodeById(itemClassF.getId());

			item = itemClassService.findByClassCode(classCode);

		}
		item.setName(itemClassF.getClassName());
		try {
			// 新增或编辑表单保存

			//String classcode = "";
			if (itemClassF.getFatherClassCode() == null || "".equals(itemClassF.getFatherClassCode().getId())
					|| itemClassF.getFatherClassCode().getId() == null) {
//				System.out.println("=========================");
				itemClassF.getFatherClassCode().setId(null);
//				;
//				item.setParent(null);
//				Map<String, Object> map = new HashMap<>();
//				map.put("length", 2);
//				List<ItemClassF> ilist = itemClassFService.findByCodeLength(map);
//				if (ilist.size() == 0)
//					classcode = "01";
//				else {
//					int max = 0;
//					for (int i = 0; i < ilist.size(); i++) {
//						if (max < Integer.parseInt(ilist.get(i).getClassCode())) {
//							max = Integer.parseInt(ilist.get(i).getClassCode());
//						}
//					}
//					classcode = String.format("%02d", max + 1);
//				}
//				System.out.println(classcode + "=====++++s");
//				item.setClassId(classcode);
			} else {//设置要插入到维护树的表中的数据
				ItemClass parent = new ItemClass();
				parent.setName(itemClassF.getFatherClassCode().getName());
				parent.setId(itemClassF.getFatherClassCode().getId());
				item.setParent(parent);
//				ItemClass itemclass = itemClassService.findByClassId(itemClassF.getFatherClassCode().getId());
//				String id = itemclass.getClassId();
//				Map<String, Object> map = new HashMap<>();
//				logger.debug(id + "-------");
//				map.put("length", id.length() + 2);
//				map.put("code", id);
				//List<ItemClassF> ilist = itemClassFService.findByCodeLength(map);System.out.println();
				//System.out.println(ilist.size()+"size");
				//logger.debug(ilist.size() + "======");
				//if (ilist.size() == 0)
					//classcode = "01";
//				else {
//					int max = 0;
//					for (int i = 0; i < ilist.size(); i++) {
//						String classCode = ilist.get(i).getClassCode();
//						if (max < Integer.parseInt(classCode.substring(classCode.length() - 2, classCode.length()))) {
//							max = Integer.parseInt(classCode.substring(classCode.length() - 2, classCode.length()));
//						}
//					}
//					classcode = String.format("%02d", max + 1);
//				}
				//System.out.println(id+"-+-"+classcode);
				//classcode = id + classcode;
				//item.setClassId(classcode);

			}
			itemClassService.save(item);
			item.setClassId(itemClassF.getClassCode());
			Map<String, Object> map = new HashMap<>();
			map.put("class_id", item.getClassId());
			map.put("id", item.getId());
			itemClassService.updateClassId(map);
			//itemClassF.setClassCode(classcode);
			itemClassFService.save(itemClassF);// 保存
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			logger.error(e.toString());
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			addMessage(redirectAttributes, "保存物料附表失败");
			return "redirect:" + Global.getAdminPath() + "/itemclass/itemClassF/?repage";
		}

		addMessage(redirectAttributes, "保存物料附表成功");
		return "redirect:" + Global.getAdminPath() + "/itemclass/itemClassF/?repage";
	}

	/**
	 * 删除物料附表
	 */
	@ResponseBody
	@RequiresPermissions("itemclass:itemClassF:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(ItemClassF itemClassF, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		return null;
		// ItemClass itemClass=new ItemClass();
		// itemClass.setId(id);
		// itemClassFService.delete(itemClassF);
		// itemClassService.delete(itemClass);
		// j.setMsg("删除物料附表成功");
		// return j;
	}

	/**
	 * 批量删除物料附表
	 */
	@Transactional
	@ResponseBody
	@RequiresPermissions("itemclass:itemClassF:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] = ids.split(",");
		try {
			for (String id : idArray) {
				String classCode = itemClassFService.findCodeById(id);
				ItemClass itemClass = itemClassService.findByClassCode(classCode);
				//System.out.println(itemClass.getId() + "===--");
				itemClassFService.delete(itemClassFService.get(id));
				itemClassService.delete(itemClass);
			}
			j.setMsg("删除物料附表成功");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			j.setMsg("删除失败");
		}
		return j;
	}

	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("itemclass:itemClassF:export")
	@RequestMapping(value = "export", method = RequestMethod.POST)
	public AjaxJson exportFile(ItemClassF itemClassF, HttpServletRequest request, HttpServletResponse response,
			RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
			String fileName = "物料附表" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
			Page<ItemClassF> page = itemClassFService.findPage(new Page<ItemClassF>(request, response, -1), itemClassF);
			new ExportExcel("物料附表", ItemClassF.class).setDataList(page.getList()).write(response, fileName).dispose();
			j.setSuccess(true);
			j.setMsg("导出成功！");
			return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出物料附表记录失败！失败信息：" + e.getMessage());
		}
		return j;
	}

	/**
	 * 导入Excel数据
	 * 
	 */
	@RequiresPermissions("itemclass:itemClassF:import")
	@RequestMapping(value = "import", method = RequestMethod.POST)
	public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<ItemClassF> list = ei.getDataList(ItemClassF.class);
			for (ItemClassF itemClassF : list) {
				try {
					ItemClass item = new ItemClass();
					item.setSort(30);
					item.setName(itemClassF.getClassName());
					// itemClassF.getFatherClassCode().setId(null);
					item.setParent(null);
					Map<String, Object> map = new HashMap<>();
					map.put("length", 2);
					String classcode = "";
					List<ItemClassF> ilist = itemClassFService.findByCodeLength(map);
					if (ilist.size() == 0)
						classcode = "01";
					else {
						int max = 0;
						for (int i = 0; i < ilist.size(); i++) {
							if (max < Integer.parseInt(ilist.get(i).getClassCode())) {
								max = Integer.parseInt(ilist.get(i).getClassCode());
							}
						}
						classcode = String.format("%02d", max + 1);
					}
					System.out.println(classcode + "=====++++s");
					item.setClassId(classcode);
					itemClassF.setIsLeaf(null);
					itemClassService.save(item);
					itemClassF.setClassCode(classcode);
					itemClassFService.save(itemClassF);
					successNum++;
				} catch (ConstraintViolationException ex) {
					ex.printStackTrace();
					failureNum++;
				} catch (Exception ex) {
					ex.printStackTrace();
					failureNum++;
				}
			}
			if (failureNum > 0) {
				failureMsg.insert(0, "，失败 " + failureNum + " 条物料附表记录。");
			}
			addMessage(redirectAttributes, "已成功导入 " + successNum + " 条物料附表记录" + failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入物料附表失败！失败信息：" + e.getMessage());
		}
		return "redirect:" + Global.getAdminPath() + "/itemclass/itemClassF/?repage";
	}

	/**
	 * 下载导入物料附表数据模板
	 */
	@RequiresPermissions("itemclass:itemClassF:import")
	@RequestMapping(value = "import/template")
	public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
			String fileName = "物料附表数据导入模板.xlsx";
			List<ItemClassF> list = Lists.newArrayList();
			new ExportExcel("物料附表数据", ItemClassF.class, 1).setDataList(list).write(response, fileName).dispose();
			return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息：" + e.getMessage());
		}
		return "redirect:" + Global.getAdminPath() + "/itemclass/itemClassF/?repage";
	}

	@RequestMapping(value="getClassCode")
	@ResponseBody
	public String getClassCode(String Name) {
		Map<String, Object> map = new HashMap<>();
		map.put("length", Name.length()+2);
		map.put("code", Name);
		List<ItemClassF> list = itemClassFService.findByCodeLength(map);
		if(list.size()==0) return "00";
		else {
			String smax = "00";
			for(int i=0;i<list.size();i++) {
				if("".equals(smax)||Integer.parseInt(smax)<Integer.parseInt(list.get(i).getClassCode().
						substring(list.get(i).getClassCode().length()-2, list.get(i).getClassCode().length()))) {
					smax=list.get(i).getClassCode().substring(list.get(i).getClassCode().length()-2, list.get(i).getClassCode().length());
				}
			}
			System.out.println(smax+"+++++");
			return String.format("%02d", Integer.parseInt(smax)+1);
		}
		
	}
}