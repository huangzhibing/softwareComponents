/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.costmanage.cosprodobject.web;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.jeeplus.core.web.BaseController;
import com.jeeplus.common.utils.StringUtils;
import com.hqu.modules.costmanage.cosprodobject.entity.CosProdObjectLeft;
import com.hqu.modules.costmanage.cosprodobject.mapper.CosProdObjectLeftMapper;
import com.hqu.modules.costmanage.cosprodobject.service.CosProdObjectLeftService;

/**
 * 核算对象定义Controller
 * @author zz
 * @version 2018-09-07
 */
@Controller
@RequestMapping(value = "${adminPath}/cosprodobject/cosProdObjectLeft")
public class CosProdObjectLeftController extends BaseController {

	@Autowired
	private CosProdObjectLeftService cosProdObjectLeftService;
	@Autowired
	private CosProdObjectLeftMapper cosProdObjectLeftMapper;
	
	@ModelAttribute
	public CosProdObjectLeft get(@RequestParam(required=false) String id) {
		CosProdObjectLeft entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = cosProdObjectLeftService.get(id);
		}
		if (entity == null){
			entity = new CosProdObjectLeft();
		}
		return entity;
	}
	
	/**
	 * 核算对象定义列表页面
	 */
	@RequestMapping(value = {"list", ""})
	public String list(CosProdObjectLeft cosProdObjectLeft,  HttpServletRequest request, HttpServletResponse response, Model model) {
		
		return "costmanage/cosprodobject/cosProdObjectLeftList";
	}

	/**
	 * 查看，增加，编辑核算对象定义表单页面
	 */
	@RequestMapping(value = "form")
	public String form(CosProdObjectLeft cosProdObjectLeft, Model model) {
		if (cosProdObjectLeft.getParent()!=null && StringUtils.isNotBlank(cosProdObjectLeft.getParent().getId())){
			cosProdObjectLeft.setParent(cosProdObjectLeftService.get(cosProdObjectLeft.getParent().getId()));
			// 获取排序号，最末节点排序号+30
			if (StringUtils.isBlank(cosProdObjectLeft.getId())){
				CosProdObjectLeft cosProdObjectLeftChild = new CosProdObjectLeft();
				cosProdObjectLeftChild.setParent(new CosProdObjectLeft(cosProdObjectLeft.getParent().getId()));
				List<CosProdObjectLeft> list = cosProdObjectLeftService.findList(cosProdObjectLeft); 
				if (list.size() > 0){
					cosProdObjectLeft.setSort(list.get(list.size()-1).getSort());
					if (cosProdObjectLeft.getSort() != null){
						cosProdObjectLeft.setSort(cosProdObjectLeft.getSort() + 30);
					}
				}
			}
		}
		if (cosProdObjectLeft.getSort() == null){
			cosProdObjectLeft.setSort(30);
		}
		model.addAttribute("cosProdObjectLeft", cosProdObjectLeft);
		return "costmanage/cosprodobject/cosProdObjectLeftForm";
	}

	/**
	 * 保存核算对象定义
	 */
	@ResponseBody
	@RequestMapping(value = "save")
	public AjaxJson save(CosProdObjectLeft cosProdObjectLeft, Model model, RedirectAttributes redirectAttributes) throws Exception{
		AjaxJson j = new AjaxJson();
		if (!beanValidator(model, cosProdObjectLeft)){
			j.setSuccess(false);
			j.setMsg("非法参数！");
			return j;
		}

		//新增或编辑表单保存
		cosProdObjectLeftService.save(cosProdObjectLeft);//保存
		j.setSuccess(true);
		j.put("cosProdObjectLeft", cosProdObjectLeft);
		j.setMsg("保存核算对象定义成功");
		return j;
	}
	
	@ResponseBody
	@RequestMapping(value = "getChildren")
	public List<CosProdObjectLeft> getChildren(String parentId){
		if("-1".equals(parentId)){//如果是-1，没指定任何父节点，就从根节点开始查找
			parentId = "0";
		}
		return cosProdObjectLeftService.getChildren(parentId);
	}
	
	/**
	 * 删除核算对象定义
	 */
	@ResponseBody
	@RequestMapping(value = "delete")
	public AjaxJson delete(CosProdObjectLeft cosProdObjectLeft, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		
		boolean child = cosProdObjectLeftMapper.hasChildren(cosProdObjectLeft.getId());
		if(child){
			j.setSuccess(false);
			j.setMsg("删除失败。该节点下有叶子节点");
		}else{
			boolean item = cosProdObjectLeftMapper.hasItem(cosProdObjectLeft.getId());
			if(item){
				j.setSuccess(false);
				j.setMsg("删除失败。该叶子节点下有科目定义");
			}else{
				cosProdObjectLeftService.delete(cosProdObjectLeft);
				j.setSuccess(true);
				j.setMsg("删除成功");
			}
		}
		
		return j;
	}
	
	@ResponseBody
	@RequestMapping(value = "hasItem")
	public AjaxJson hasItem(CosProdObjectLeft cosProdObjectLeft, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		
		boolean item = cosProdObjectLeftMapper.hasItem(cosProdObjectLeft.getId());
		if(item){
			j.setSuccess(false);
			j.setMsg("无法添加下级科目。该叶子节点下有科目定义");
		}else{
			j.setSuccess(true);
		}
		
		return j;
	}

	@RequiresPermissions("user")
	@ResponseBody
	@RequestMapping(value = "treeData")
	public List<Map<String, Object>> treeData(@RequestParam(required=false) String extId, HttpServletResponse response) {
		List<Map<String, Object>> mapList = Lists.newArrayList();
		List<CosProdObjectLeft> list = cosProdObjectLeftService.findList(new CosProdObjectLeft());
		for (int i=0; i<list.size(); i++){
			CosProdObjectLeft e = list.get(i);
			if (StringUtils.isBlank(extId) || (extId!=null && !extId.equals(e.getId()) && e.getParentIds().indexOf(","+extId+",")==-1)){
				Map<String, Object> map = Maps.newHashMap();
				map.put("id", e.getId());
				map.put("text", e.getName());
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