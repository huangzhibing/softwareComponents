/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.purchasemanage.linkman.mapper;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.hqu.modules.purchasemanage.linkman.entity.LinkMan;

/**
 * 联系人档案MAPPER接口
 * @author luyumiao
 * @version 2018-04-15
 */
@MyBatisMapper
public interface LinkManMapper extends BaseMapper<LinkMan> {

    /**
     * 获得联系人编码的最大值流水号
     * @return MaxAccTypeCode
     */
	 public String getMaxLinkCode();
}