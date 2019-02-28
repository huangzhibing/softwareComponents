/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.basedata.product.web;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import com.google.common.collect.Maps;
import com.hqu.modules.basedata.item.entity.Item;
import com.hqu.modules.basedata.item.service.ItemService;
import org.apache.commons.beanutils.BeanUtils;
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
import com.hqu.modules.basedata.product.entity.Product;
import com.hqu.modules.basedata.product.service.ProductService;

/**
 * 产品维护Controller
 * @author M1ngz
 * @version 2018-04-06
 */
@Controller
@RequestMapping(value = "${adminPath}/product/product")
public class ProductController extends BaseController {

	@Autowired
	private ProductService productService;
	@Autowired
	private ItemService itemService;
	
	@ModelAttribute
	public Product get(@RequestParam(required=false) String id) {
		Product entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = productService.getById(id);
		}
		if (entity == null){
			entity = new Product();
		}
		return entity;
	}
	
	/**
	 * 产品维护列表页面
	 */
	@RequiresPermissions("product:product:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "basedata/product/productList";
	}
	
		/**
	 * 产品维护列表数据
	 */
	@ResponseBody
	@RequiresPermissions("product:product:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(Product product, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Product> page = productService.findPage(new Page<Product>(request, response), product);
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑产品维护表单页面
	 */
	@RequiresPermissions(value={"product:product:view","product:product:add","product:product:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(Product product, Model model) {
		model.addAttribute("product", product);
		if(StringUtils.isBlank(product.getId())){//如果ID是空为添加
			model.addAttribute("isAdd", true);
		}
		return "basedata/product/productForm";
	}

	/**
	 * 保存产品维护
	 */
	@RequiresPermissions(value={"product:product:add","product:product:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(Product product, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, product)){
			return form(product, model);
		}
		//新增或编辑表单保存
		if(product.getItem().getCode().length() == 32){

			Item item = itemService.get(product.getItem().getCode());
			product.setItem(item);

		}
		Integer count = productService.getCountByCode(product.getItem().getCode());
		if( product.getId().equals("") && count >= 1 ){
			addMessage(model, "该物料已存在对应产品");
			return form(product, model);
		}
		productService.save(product);//保存
		addMessage(redirectAttributes, "保存产品成功");
		return "redirect:"+Global.getAdminPath()+"/product/product/?repage";
	}
	
	/**
	 * 删除产品维护
	 */
	@ResponseBody
	@RequiresPermissions("product:product:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(Product product, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		productService.delete(product);
		j.setMsg("删除产品维护成功");
		return j;
	}
	
	/**
	 * 批量删除产品维护
	 */
	@ResponseBody
	@RequiresPermissions("product:product:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			productService.delete(productService.getById(id));
		}
		j.setMsg("删除产品维护成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("product:product:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(Product product, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "产品维护"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<Product> page = productService.findPage(new Page<Product>(request, response, -1), product);
    		new ExportExcel("产品维护", Product.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出产品维护记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("product:product:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<Product> list = ei.getDataList(Product.class);
			for (Product product : list){
				try{
					productService.save(product);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条产品维护记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条产品维护记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入产品维护失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/product/product/?repage";
    }
	
	/**
	 * 下载导入产品维护数据模板
	 */
	@RequiresPermissions("product:product:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "产品维护数据导入模板.xlsx";
    		List<Product> list = Lists.newArrayList(); 
    		new ExportExcel("产品维护数据", Product.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/product/product/?repage";
    }
	@RequiresPermissions("user")
	@ResponseBody
	@RequestMapping(value = "treeData")
	public Map<String, Object> treeData(@RequestParam(required=false) String extId, HttpServletResponse response) {
		Map<String,Object> result = Maps.newHashMap();
		List<Map<String, Object>> mapList = Lists.newArrayList();
		List<Product> list = productService.findList(new Product());
		result.put("id",0);
		result.put("text","产品");
		Map<String,Object> item = null;
		for (int i=0; i<list.size(); i++){
			Product e = list.get(i);
			item = Maps.newHashMap();
			item.put("id",e.getId());
			item.put("data",e.getItem().getCode());
			item.put("text",e.getItemNameRu());
			mapList.add(item);
		}
		result.put("children",mapList);
		return result;
	}
}