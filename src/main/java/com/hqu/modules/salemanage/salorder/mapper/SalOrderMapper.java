/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.salemanage.salorder.mapper;

import com.hqu.modules.salemanage.salorder.entity.SalOrderItem;
import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;

import java.util.List;

import com.hqu.modules.basedata.product.entity.Product;
import com.hqu.modules.salemanage.salorder.entity.SalOrder;
import org.apache.ibatis.annotations.Param;

/**
 * 内部订单MAPPER接口
 * @author dongqida
 * @version 2018-05-27
 */
@MyBatisMapper
public interface SalOrderMapper extends BaseMapper<SalOrder> {
	String getMaxIdByTypeAndDate(String para);
	public List<String> findProductList(Product product);

	List<SalOrderItem> selectPartCode(List<String> proCode);
}