/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.qualitymanage.purinvcheckmain.mapper;

import com.hqu.modules.qualitymanage.purinvcheckmain.entity.InvCheckDetailCode;
import com.hqu.modules.qualitymanage.purinvcheckmain.entity.InvCheckMain;
import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 入库通知单MAPPER接口
 * @author 张铮
 * @version 2018-04-21
 */
@MyBatisMapper
public interface InvCheckMainMapper extends BaseMapper<InvCheckMain> {
	public void qmsFlagByBillNum(InvCheckMain invCheckMain);
	
	List<InvCheckMain> findListbyBillTypeAndState(InvCheckMain invCheckMain);
	
	List<InvCheckDetailCode> findListbyBillNum(InvCheckMain invCheckMain);

	List<InvCheckMain> findBackList();

    List<InvCheckMain> findListbyBillTypeAndStateAndItemBarCode(InvCheckMain invCheckMain);

    List<InvCheckMain> findListForInv(InvCheckMain invCheckMain);

    double getCurrentQtybyConIdandItemCode(@Param("conId") String conId, @Param("itemCode") String itemCode);

    double getItemQtybyConIdandItemCode(@Param("conId") String conId, @Param("itemCode") String itemCode);
}