/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.costmanage.cositem.web;

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
import com.hqu.modules.costmanage.cositem.entity.CosItemLeft;
import com.hqu.modules.costmanage.cositem.mapper.CosItemLeftMapper;
import com.hqu.modules.costmanage.cositem.service.CosItemLeftService;

/**
 * 科目定义Controller
 * @author zz
 * @version 2018-09-05
 */
@Controller
@RequestMapping(value = "${adminPath}/cositem/cosItemLeft")
public class CosItemLeftController extends BaseController {

	@Autowired
	private CosItemLeftService cosItemLeftService;
	@Autowired
	private CosItemLeftMapper cosItemLeftMapper;
	
	@ModelAttribute
	public CosItemLeft get(@RequestParam(required=false) String id) {
		CosItemLeft entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = cosItemLeftService.get(id);
		}
		if (entity == null){
			entity = new CosItemLeft();
		}
		return entity;
	}
	
	/**
	 * 科目定义列表页面
	 */
	@RequestMapping(value = {"list", ""})
	public String list(CosItemLeft cosItemLeft,  HttpServletRequest request, HttpServletResponse response, Model model) {
		
		return "costmanage/cositem/cosItemLeftList";
	}

	/**
	 * 查看，增加，编辑科目定义表单页面
	 */
	@RequestMapping(value = "form")
	public String form(CosItemLeft cosItemLeft, Model model) {
		if (cosItemLeft.getParent()!=null && StringUtils.isNotBlank(cosItemLeft.getParent().getId())){
			cosItemLeft.setParent(cosItemLeftService.get(cosItemLeft.getParent().getId()));
			// 获取排序号，最末节点排序号+30
			if (StringUtils.isBlank(cosItemLeft.getId())){
				CosItemLeft cosItemLeftChild = new CosItemLeft();
				cosItemLeftChild.setParent(new CosItemLeft(cosItemLeft.getParent().getId()));
				List<CosItemLeft> list = cosItemLeftService.findList(cosItemLeft); 
				if (list.size() > 0){
					cosItemLeft.setSort(list.get(list.size()-1).getSort());
					if (cosItemLeft.getSort() != null){
						cosItemLeft.setSort(cosItemLeft.getSort() + 30);
					}
				}
			}
		}
		if (cosItemLeft.getSort() == null){
			cosItemLeft.setSort(30);
		}
		model.addAttribute("cosItemLeft", cosItemLeft);
		return "costmanage/cositem/cosItemLeftForm";
	}
	
	

	/**
	 * 保存科目定义
	 */
	@ResponseBody
	@RequestMapping(value = "save")
	public AjaxJson save(CosItemLeft cosItemLeft, Model model, RedirectAttributes redirectAttributes) throws Exception{
		AjaxJson j = new AjaxJson();
		if (!beanValidator(model, cosItemLeft)){
			j.setSuccess(false);
			j.setMsg("非法参数！");
			return j;
		}

		//新增或编辑表单保存
		cosItemLeftService.save(cosItemLeft);//保存
		j.setSuccess(true);
		j.put("cosItemLeft", cosItemLeft);
		j.setMsg("保存科目定义成功");
		return j;
	}
	
	@ResponseBody
	@RequestMapping(value = "getChildren")
	public List<CosItemLeft> getChildren(String parentId){
		if("-1".equals(parentId)){//如果是-1，没指定任何父节点，就从根节点开始查找
			parentId = "0";
		}
		return cosItemLeftService.getChildren(parentId);
	}
	
	/**
	 * 删除科目定义
	 */
	@ResponseBody
	@RequestMapping(value = "delete")
	public AjaxJson delete(CosItemLeft cosItemLeft, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		
		boolean child = cosItemLeftMapper.hasChildren(cosItemLeft.getId());
		if(child){
			j.setSuccess(false);
			j.setMsg("删除失败。该节点下有叶子节点");
		}else{
			boolean item = cosItemLeftMapper.hasItem(cosItemLeft.getId());
			if(item){
				j.setSuccess(false);
				j.setMsg("删除失败。该叶子节点下有科目定义");
			}else{
				cosItemLeftService.delete(cosItemLeft);
				j.setSuccess(true);
				j.setMsg("删除成功");
			}
		}
		
		return j;
	}
	
	@ResponseBody
	@RequestMapping(value = "hasItem")
	public AjaxJson hasItem(CosItemLeft cosItemLeft, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		
		boolean item = cosItemLeftMapper.hasItem(cosItemLeft.getId());
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
		List<CosItemLeft> list = cosItemLeftService.findList(new CosItemLeft());
		for (int i=0; i<list.size(); i++){
			CosItemLeft e = list.get(i);
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