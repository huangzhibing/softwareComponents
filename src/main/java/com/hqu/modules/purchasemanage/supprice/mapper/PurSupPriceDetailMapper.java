/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.purchasemanage.supprice.mapper;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.hqu.modules.purchasemanage.supprice.entity.PurSupPriceDetail;
import java.util.List;
/**
 * 供应商价格维护明细MAPPER接口
 * @author syc
 * @version 2018-08-02
 */
@MyBatisMapper
public interface PurSupPriceDetailMapper extends BaseMapper<PurSupPriceDetail> {
	List<PurSupPriceDetail> findPriceList(PurSupPriceDetail purSupPriceDetail);
}