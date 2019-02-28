/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.basedata.mdmproductbom.service;

import java.util.List;
import java.util.Map;

import com.hqu.modules.basedata.item.entity.Item;
import com.hqu.modules.basedata.item.service.ItemService;
import com.hqu.modules.basedata.product.entity.Product;
import com.hqu.modules.basedata.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.hqu.modules.basedata.mdmproductbom.entity.ProductBom;
import com.hqu.modules.basedata.mdmproductbom.mapper.ProductBomMapper;

/**
 * 产品结构维护Service
 * @author liujiachen
 * @version 2018-06-04
 */
@Service
@Transactional(readOnly = true)
public class ProductBomService extends CrudService<ProductBomMapper, ProductBom> {
	@Autowired
	ProductService productService;
	@Autowired
	ItemService itemService;
	public ProductBom get(String id) {
		return super.get(id);
	}
	
	public List<ProductBom> findList(ProductBom productBom) {
		return super.findList(productBom);
	}
	
	public Page<ProductBom> findPage(Page<ProductBom> page, ProductBom productBom) {
		return super.findPage(page, productBom);
	}
	
	@Transactional(readOnly = false)
	public void save(ProductBom productBom) {
		if(productBom.getProduct().getItem().getCode().length() == 32){
			Product product = productService.getById(productBom.getProduct().getItem().getCode());
			productBom.setProduct(product);
		}
		if(productBom.getItem().getCode().length() == 32){
			Item item = itemService.get(productBom.getItem().getCode());
			productBom.setItem(item);
		}
		if(productBom.getFatherItem().getCode().length() == 32){
			Item item = itemService.get(productBom.getFatherItem().getCode());
			productBom.setFatherItem(item);
		}
		super.save(productBom);
	}
	
	@Transactional(readOnly = false)
	public void delete(ProductBom productBom) {
		super.delete(productBom);
	}

	public List<ProductBom> getByFatherId(String id){
		return mapper.getByFatherId(id);
	}

	public List<ProductBom> findNodeWithoutFatherItem(String productItemCode){
		return mapper.findNodeWithoutFatherItem(productItemCode);
	}
	public List<ProductBom> findNodeWithFatherItem(String itemCode){
		return mapper.findNodeWithFatherItem(itemCode);
	}

	public List<ProductBom> findProductNode(){
		return mapper.findProductNode();
	}

	public List<ProductBom> findItemNode(Map<String,Object> param){
		return mapper.findItemNode(param);
	}

}