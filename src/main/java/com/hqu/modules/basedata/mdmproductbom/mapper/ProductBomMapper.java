/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.basedata.mdmproductbom.mapper;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.hqu.modules.basedata.mdmproductbom.entity.ProductBom;

import java.util.List;
import java.util.Map;

/**
 * 产品结构维护MAPPER接口
 * @author liujiachen
 * @version 2018-06-04
 */
@MyBatisMapper
public interface ProductBomMapper extends BaseMapper<ProductBom> {
    List<ProductBom> getByFatherId(String id);
    List<ProductBom> findNodeWithoutFatherItem(String productItemCode);
    List<ProductBom> findNodeWithFatherItem(String itemCode);
    List<ProductBom> findProductNode();
    List<ProductBom> findItemNode(Map<String,Object> param);
}