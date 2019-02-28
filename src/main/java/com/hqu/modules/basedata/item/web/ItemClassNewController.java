/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.basedata.item.web;

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
import com.hqu.modules.basedata.item.entity.ItemClassNew;
import com.hqu.modules.basedata.item.service.ItemClassNewService;

/**
 * 物料定义Controller
 * @author xn
 * @version 2018-04-06
 */
@Controller
@RequestMapping(value = "${adminPath}/item/itemClassNew")
public class ItemClassNewController extends BaseController {

	@Autowired
	private ItemClassNewService itemClassNewService;
	
	@ModelAttribute
	public ItemClassNew get(@RequestParam(required=false) String id) {
		ItemClassNew entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = itemClassNewService.get(id);
		}
		if (entity == null){
			entity = new ItemClassNew();
		}
		return entity;
	}
	
	/**
	 * 物料列表页面
	 */
	@RequestMapping(value = {"list", ""})
	public String list(ItemClassNew itemClassNew,  HttpServletRequest request, HttpServletResponse response, Model model) {
		
		return "basedata/item/itemClassNewList";
	}

	/**
	 * 查看，增加，编辑物料表单页面
	 */
	@RequestMapping(value = "form")
	public String form(ItemClassNew itemClassNew, Model model) {
		if (itemClassNew.getParent()!=null && StringUtils.isNotBlank(itemClassNew.getParent().getId())){
			itemClassNew.setParent(itemClassNewService.get(itemClassNew.getParent().getId()));
			// 获取排序号，最末节点排序号+30
			if (StringUtils.isBlank(itemClassNew.getId())){
				ItemClassNew itemClassNewChild = new ItemClassNew();
				itemClassNewChild.setParent(new ItemClassNew(itemClassNew.getParent().getId()));
				List<ItemClassNew> list = itemClassNewService.findList(itemClassNew); 
				if (list.size() > 0){
					itemClassNew.setSort(list.get(list.size()-1).getSort());
					if (itemClassNew.getSort() != null){
						itemClassNew.setSort(itemClassNew.getSort() + 30);
					}
				}
			}
		}
		if (itemClassNew.getSort() == null){
			itemClassNew.setSort(30);
		}
		model.addAttribute("itemClassNew", itemClassNew);
		return "basedata/item/itemClassNewForm";
	}

	/**
	 * 保存物料
	 */
	@ResponseBody
	@RequestMapping(value = "save")
	public AjaxJson save(ItemClassNew itemClassNew, Model model, RedirectAttributes redirectAttributes) throws Exception{
		AjaxJson j = new AjaxJson();
		if (!beanValidator(model, itemClassNew)){
			j.setSuccess(false);
			j.setMsg("非法参数！");
			return j;
		}

		//新增或编辑表单保存
		itemClassNewService.save(itemClassNew);//保存
		j.setSuccess(true);
		j.put("itemClassNew", itemClassNew);
		j.setMsg("保存物料成功");
		return j;
	}
	
	@ResponseBody
	@RequestMapping(value = "getChildren")
	public List<ItemClassNew> getChildren(String parentId){
		if("-1".equals(parentId)){//如果是-1，没指定任何父节点，就从根节点开始查找
			parentId = "0";
		}
		return itemClassNewService.getChildren(parentId);
	}
	
	/**
	 * 删除物料
	 */
	@ResponseBody
	@RequestMapping(value = "delete")
	public AjaxJson delete(ItemClassNew itemClassNew, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		itemClassNewService.delete(itemClassNew);
		j.setSuccess(true);
		j.setMsg("删除物料成功");
		return j;
	}

	@RequiresPermissions("user")
	@ResponseBody
	@RequestMapping(value = "treeData")
	public List<Map<String, Object>> treeData(@RequestParam(required=false) String extId, 
			HttpServletResponse response) {
		List<Map<String, Object>> mapList = Lists.newArrayList();
		List<ItemClassNew> list = itemClassNewService.findList(new ItemClassNew());
		for (int i=0; i<list.size(); i++){
			ItemClassNew e = list.get(i);
			if (StringUtils.isBlank(extId) || (extId!=null && !extId.equals(e.getId()) 
					&& e.getParentIds().indexOf(","+extId+",")==-1)){
				Map<String, Object> map = Maps.newHashMap();
				map.put("id", e.getId());
				map.put("text",e.getName());
				map.put("classId", e.getClassId());
				if(StringUtils.isBlank(e.getParentId()) || "0".equals(e.getParentId())){
					map.put("parent", "#");
					Map<String, Object> state = Maps.newHashMap();
					state.put("opened", true);
					map.put("state", state);
				}else{
					map.put("parent", e.getParentId());
				}
				mapList.add(map);
			}
		}
		return mapList;
	}

	/**
	 * 该方法支持展示树形类型编码code -name
	 * @param extId
	 * @param response
	 * @return
	 */
	@RequiresPermissions("user")
	@ResponseBody
	@RequestMapping(value = "codeNameTreeData")
	public List<Map<String, Object>> codeNameTreeData(@RequestParam(required=false) String extId,
											  HttpServletResponse response) {
		List<Map<String, Object>> mapList = Lists.newArrayList();
		List<ItemClassNew> list = itemClassNewService.findList(new ItemClassNew());
		for (int i=0; i<list.size(); i++){
			ItemClassNew e = list.get(i);
			if (StringUtils.isBlank(extId) || (extId!=null && !extId.equals(e.getId())
					&& e.getParentIds().indexOf(","+extId+",")==-1)){
				Map<String, Object> map = Maps.newHashMap();
				map.put("id", e.getId());
				map.put("text",e.getClassId()+"-"+e.getName());
				map.put("classId", e.getClassId());
				if(StringUtils.isBlank(e.getParentId()) || "0".equals(e.getParentId())){
					map.put("parent", "#");
					Map<String, Object> state = Maps.newHashMap();
					state.put("opened", true);
					map.put("state", state);
				}else{
					map.put("parent", e.getParentId());
				}
				mapList.add(map);
			}
		}
		return mapList;
	}
	
}