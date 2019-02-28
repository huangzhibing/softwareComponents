/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.basedata.mdmproductbom.web;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import com.google.common.collect.Maps;
import com.hqu.modules.basedata.item.entity.Item;
import com.hqu.modules.basedata.item.service.ItemService;
import com.hqu.modules.basedata.product.entity.Product;
import com.hqu.modules.basedata.product.service.ProductService;
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
import com.hqu.modules.basedata.mdmproductbom.entity.ProductBom;
import com.hqu.modules.basedata.mdmproductbom.service.ProductBomService;

/**
 * 产品结构维护Controller
 * @author liujiachen
 * @version 2018-06-04
 */
@Controller
@RequestMapping(value = "${adminPath}/mdmproductbom/productBom")
public class ProductBomController extends BaseController {

	@Autowired
	private ProductBomService productBomService;
	@Autowired
	private ItemService itemService;
	@Autowired
	private ProductService productService;
	
	@ModelAttribute
	public ProductBom get(@RequestParam(required=false) String id) {
		ProductBom entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = productBomService.get(id);
//			Product p = productService.get(entity.getProduct());
//			entity.getProduct().setItemCode(p.getItem().getCode());
		}
		if (entity == null){
			entity = new ProductBom();
		}
		return entity;
	}
	
	/**
	 * 产品结构维护列表页面
	 */
	@RequiresPermissions("mdmproductbom:productBom:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "basedata/mdmproductbom/productBomList";
	}
	/**
	 * 产品结构维护列表页面
	 */
	@RequiresPermissions("mdmproductbom:productBom:list")
	@RequestMapping(value = "listWithoutTree")
	public String listWithoutTree() {
		return "basedata/mdmproductbom/productBomListWithoutTree";
	}
	
		/**
	 * 产品结构维护列表数据
	 */
	@ResponseBody
	@RequiresPermissions("mdmproductbom:productBom:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(ProductBom productBom, HttpServletRequest request, HttpServletResponse response, Model model) {
		if(productBom.getId() != null && !productBom.getId().equals("")) {
			String[] ids = productBom.getId().split(",");
			List<ProductBom> list = new ArrayList<>();
			for(String id : ids) {
				list.add(productBomService.get(id));
			}

			Page<ProductBom> page = new Page<ProductBom>(request, response);
			page.setCount(list.size());
			page.setList(list);

			Product p = null;
			for(ProductBom productBom1 : page.getList()){
//			productBom.getProduct().setItemCode(itemService.get(productBom1.getProduct().getItem().getId()).getCode());
				p = productService.get(productBom1.getProduct());
				if(org.springframework.util.StringUtils.isEmpty(p)){
					continue;
				}
				productBom1.getProduct().setItemCode(p.getItem().getCode());
			}
			return getBootstrapData(page);
		}
		if(productBom.getItem() != null && productBom.getItem().getId() != null) {
			productBom.setItem(itemService.get(productBom.getItem().getId()));
		}
		if(productBom.getFatherItem() != null && productBom.getFatherItem().getId() != null) {
			productBom.setFatherItem(itemService.get(productBom.getFatherItem().getId()));
		}
		Page<ProductBom> page = productBomService.findPage(new Page<ProductBom>(request, response), productBom);
		Product p = null;
		for(ProductBom productBom1 : page.getList()){
//			productBom.getProduct().setItemCode(itemService.get(productBom1.getProduct().getItem().getId()).getCode());
			p = productService.get(productBom1.getProduct());
			if(org.springframework.util.StringUtils.isEmpty(p)){
				continue;
			}
			productBom1.getProduct().setItemCode(p.getItem().getCode());
		}
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑产品结构维护表单页面
	 */
	@RequiresPermissions(value={"mdmproductbom:productBom:view","mdmproductbom:productBom:add","mdmproductbom:productBom:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(ProductBom productBom, Model model,String pid,String nid) {
//		if(!org.springframework.util.StringUtils.isEmpty(id) && !org.springframework.util.StringUtils.isEmpty(name)) {
//			Product product = new Product();
//			Product dbProduct = productService.get(id);
//			if(!org.springframework.util.StringUtils.isEmpty(dbProduct)) {
//				dbProduct.setItemCode(dbProduct.getItem().getCode());
//				productBom.setProduct(dbProduct);
//				productBom.setProductItemName(name);
//			}
//		}

		if(StringUtils.isNotBlank(pid)){
			Product product = productBomService.get(pid).getProduct();
			productBom.setProduct(product);
			Product p = productService.get(product);
			productBom.getProduct().setItemCode(p.getItem().getCode());
			productBom.setProductItemName(product.getItemName());
			Item item = itemService.getByCode(p.getItem().getCode());
			productBom.setFatherItem(item);
			productBom.setFatherItemName(item.getName());
			productBom.setFatherItemPdn(item.getDrawNO());
			productBom.setFatherItemSpec(item.getSpecModel());

		}
		if(StringUtils.isNotBlank(nid)){
			ProductBom root = productBomService.get(pid);
			ProductBom temp = productBomService.get(nid);
			Item item = itemService.getByCode(temp.getItem().getCode());
			productBom.setFatherItem(item);
			productBom.setFatherItemName(item.getName());
			productBom.setFatherItemPdn(item.getDrawNO());
			productBom.setFatherItemSpec(item.getSpecModel());
			productBom.setProduct(root.getProduct());
			productBom.getProduct().setItemCode(root.getProduct().getItem().getCode());
			productBom.setProductItemName(root.getProduct().getItemName());
//			productBom.setFatherItemMeasure(item.getUnit());
		}
		if(StringUtils.isBlank(productBom.getId())){//如果ID是空为添加
			model.addAttribute("isAdd", true);
			productBom.setLeadTimeAssemble("0");
			productBom.setNumInFather("1");
		}

		model.addAttribute("productBom", productBom);
		return "basedata/mdmproductbom/productBomForm";
	}

	/**
	 * 保存产品结构维护
	 */
	@RequiresPermissions(value={"mdmproductbom:productBom:add","mdmproductbom:productBom:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(ProductBom productBom, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, productBom)){
			return form(productBom, model,null,null);
		}
//		ProductBom temp = new ProductBom();
//		temp.setProduct(productBom.getProduct());
//		List<ProductBom> productBoms = productBomService.findList(temp);
//		if(productBoms.size() > 0) {
//			addMessage(redirectAttributes, "该产品编码已存在");
//			return form(productBom, model,null,null);
//		}
		//新增或编辑表单保存
//		productBom.setId("");
		productBomService.save(productBom);//保存
		addMessage(redirectAttributes, "保存产品结构维护成功");
		return "redirect:"+Global.getAdminPath()+"/mdmproductbom/productBom/?repage";
	}
	
	/**
	 * 删除产品结构维护
	 */
	@ResponseBody
	@RequiresPermissions("mdmproductbom:productBom:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(ProductBom productBom, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		productBomService.delete(productBom);
		j.setMsg("删除产品结构维护成功");
		return j;
	}
	
	/**
	 * 批量删除产品结构维护
	 */
	@ResponseBody
	@RequiresPermissions("mdmproductbom:productBom:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			productBomService.delete(productBomService.get(id));
		}
		j.setMsg("删除产品结构维护成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("mdmproductbom:productBom:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(ProductBom productBom, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "产品结构维护"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<ProductBom> page = productBomService.findPage(new Page<ProductBom>(request, response, -1), productBom);
    		new ExportExcel("产品结构维护", ProductBom.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出产品结构维护记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("mdmproductbom:productBom:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<ProductBom> list = ei.getDataList(ProductBom.class);
			for (ProductBom productBom : list){
				try{
					productBomService.save(productBom);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条产品结构维护记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条产品结构维护记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入产品结构维护失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/mdmproductbom/productBom/?repage";
    }
	
	/**
	 * 下载导入产品结构维护数据模板
	 */
	@RequiresPermissions("mdmproductbom:productBom:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "产品结构维护数据导入模板.xlsx";
    		List<ProductBom> list = Lists.newArrayList(); 
    		new ExportExcel("产品结构维护数据", ProductBom.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/mdmproductbom/productBom/?repage";
    }
	@RequiresPermissions("user")
	@ResponseBody
	@RequestMapping(value = "treeData")
	public Map<String, Object> treeData(@RequestParam(required=false) String extId, HttpServletResponse response) {
		Map<String,Object> result = Maps.newHashMap();
		List<Map<String, Object>> mapList = Lists.newArrayList();
		List<ProductBom> list = productBomService.findProductNode();
		result.put("id",0);
		result.put("text","产品");

		Map<String,Object> item = null;
		for (int i=0; i<list.size(); i++){
			ProductBom e = list.get(i);
			if(StringUtils.isNotBlank(e.getItemName())){
				continue;
			}
			item = Maps.newHashMap();
			item.put("id",e.getId());
			item.put("data","p");
			item.put("text",e.getProduct().getItemName());
			item.put("children",nodeDataWithoutFatherItem(e.getProduct()));
			mapList.add(item);
		}
		result.put("children",mapList);
		return result;
	}

	private List<Map<String, Object>> nodeDataWithoutFatherItem(Product p){
		List<Map<String, Object>> mapList = Lists.newArrayList();
		Map<String,Object> param = Maps.newHashMap();
		param.put("productItemCode",p.getItem().getCode());
		param.put("fatherItemCode",p.getItem().getCode());
//		List<ProductBom> list = productBomService.findNodeWithoutFatherItem(p.getItem().getCode());
		List<ProductBom> list = productBomService.findItemNode(param);

		Map<String,Object> item = null;
		for (int i=0; i<list.size(); i++){
			ProductBom e = list.get(i);
			if(StringUtils.isBlank(e.getItemName())){
				continue;
			}
			item = Maps.newHashMap();
			item.put("id",e.getId());
			item.put("data","n");
			item.put("text",e.getItemName());
			item.put("children",nodeDataWithFatherItem(p,e.getItem()));
			mapList.add(item);
		}
		return mapList;
	}
	private List<Map<String,Object>> nodeDataWithFatherItem(Product p,Item it){
		List<Map<String, Object>> mapList = Lists.newArrayList();
//		List<ProductBom> list = productBomService.findNodeWithFatherItem(it.getCode());
		Map<String,Object> param = Maps.newHashMap();
		param.put("productItemCode",p.getItem().getCode());
		param.put("fatherItemCode",it.getCode());
//		List<ProductBom> list = productBomService.findNodeWithoutFatherItem(p.getItem().getCode());
		List<ProductBom> list = productBomService.findItemNode(param);

		Map<String,Object> item = null;
		for (int i=0; i<list.size(); i++){
			ProductBom e = list.get(i);
			if(StringUtils.isBlank(e.getFatherItemName())){
				continue;
			}
			item = Maps.newHashMap();
			item.put("id",e.getId());
			item.put("data","n");
			item.put("text",e.getItemName());
			item.put("children",nodeDataWithFatherItem(p,e.getItem()));
			mapList.add(item);
		}
		return mapList;
	}
}