/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.basedata.itemclass.web;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.common.config.Global;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.common.utils.StringUtils;
import com.hqu.modules.basedata.itemclass.entity.ItemClass;
import com.hqu.modules.basedata.itemclass.service.ItemClassService;

/**
 * 物料类定义Controller
 * 
 * @author dongqida
 * @version 2018-04-02
 */
@Controller
@RequestMapping(value = "${adminPath}/itemclass/itemClass")
public class ItemClassController extends BaseController {

	@Autowired
	private ItemClassService itemClassService;

	@ModelAttribute
	public ItemClass get(@RequestParam(required = false) String id) {
		ItemClass entity = null;
		if (StringUtils.isNotBlank(id)) {
			entity = itemClassService.get(id);
		}
		if (entity == null) {
			entity = new ItemClass();
		}
		return entity;
	}

	/**
	 * 物料类列表页面
	 */
	@RequestMapping(value = { "list", "" })
	public String list(ItemClass itemClass, HttpServletRequest request, HttpServletResponse response, Model model) {

		return "basedata/itemclass/itemClassList";
	}

	/**
	 * 查看，增加，编辑物料类表单页面
	 */
	@RequestMapping(value = "form")
	public String form(ItemClass itemClass, Model model) {
		if (itemClass.getParent() != null && StringUtils.isNotBlank(itemClass.getParent().getId())) {
			itemClass.setParent(itemClassService.get(itemClass.getParent().getId()));
			// 获取排序号，最末节点排序号+30
			if (StringUtils.isBlank(itemClass.getId())) {
				ItemClass itemClassChild = new ItemClass();
				itemClassChild.setParent(new ItemClass(itemClass.getParent().getId()));
				List<ItemClass> list = itemClassService.findList(itemClass);
				if (list.size() > 0) {
					itemClass.setSort(list.get(list.size() - 1).getSort());
					if (itemClass.getSort() != null) {
						itemClass.setSort(itemClass.getSort() + 30);
					}
				}
			}
		}
		if (itemClass.getSort() == null) {
			itemClass.setSort(30);
		}
		model.addAttribute("itemClass", itemClass);
		return "basedata/itemclass/itemClassForm";
	}

	/**
	 * 保存物料类
	 */
	@ResponseBody
	@RequestMapping(value = "save")
	public AjaxJson save(ItemClass itemClass, Model model, RedirectAttributes redirectAttributes) throws Exception {
		AjaxJson j = new AjaxJson();
		if (!beanValidator(model, itemClass)) {
			j.setSuccess(false);
			j.setMsg("非法参数！");
			return j;
		}

		// 新增或编辑表单保存
		itemClassService.save(itemClass);// 保存
		j.setSuccess(true);
		j.put("itemClass", itemClass);
		j.setMsg("保存物料类成功");
		return j;
	}

	@ResponseBody
	@RequestMapping(value = "getChildren")
	public List<ItemClass> getChildren(String parentId) {
		if ("-1".equals(parentId)) {// 如果是-1，没指定任何父节点，就从根节点开始查找
			parentId = "0";
		}
		return itemClassService.getChildren(parentId);
	}

	/**
	 * 删除物料类
	 */
	@ResponseBody
	@RequestMapping(value = "delete")
	public AjaxJson delete(ItemClass itemClass, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		itemClassService.delete(itemClass);
		j.setSuccess(true);
		j.setMsg("删除物料类成功");
		return j;
	}

	@RequiresPermissions("user")
	@ResponseBody
	@RequestMapping(value = "treeData")
	public List<Map<String, Object>> treeData(@RequestParam(required = false) String extId,
			HttpServletResponse response) {
		List<Map<String, Object>> mapList = Lists.newArrayList();
		List<ItemClass> list = itemClassService.findList(new ItemClass());
		for (int i = 0; i < list.size(); i++) {
			ItemClass e = list.get(i);
			if (StringUtils.isBlank(extId) || (extId != null && !extId.equals(e.getId())
					&& e.getParentIds().indexOf("," + extId + ",") == -1)) {
				Map<String, Object> map = Maps.newHashMap();
				map.put("id", e.getId());
				map.put("text", e.getClassId()+" "+e.getName());
				map.put("classId", e.getClassId());
				if (StringUtils.isBlank(e.getParentId()) || "0".equals(e.getParentId())) {
					map.put("parent", "#");
					Map<String, Object> state = Maps.newHashMap();
					state.put("opened", true);
					map.put("state", state);
				} else {
					map.put("parent", e.getParentId());
				}
				mapList.add(map);
			}
		}
		return mapList;
	}

}