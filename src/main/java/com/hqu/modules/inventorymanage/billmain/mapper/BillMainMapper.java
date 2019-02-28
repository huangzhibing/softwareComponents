/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.inventorymanage.billmain.mapper;

import com.hqu.modules.basedata.period.entity.Period;
import com.hqu.modules.inventorymanage.billmain.entity.BillDetailCode;
import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.hqu.modules.inventorymanage.billmain.entity.BillMain;

import java.util.List;
import java.util.Map;

/**
 * 出入库单据MAPPER接口
 * @author M1ngz
 * @version 2018-04-16
 */
@MyBatisMapper
public interface BillMainMapper extends BaseMapper<BillMain> {

    void saveBillCode(BillDetailCode billDetailCode);

    void updateBillCode(BillDetailCode billDetailCode);

    void deleteBillCode(BillDetailCode billDetailCode);

	String getMaxIdByTypeAndDate(String para);

    Period findPeriodByTime(Map<String, Object> map);

    BillMain selectMaxBillMainInfo();
    
    List<BillMain> findListforCheck(BillMain billMain);
    
    List<BillMain> findListforCancelCheck(BillMain billMain);
    
    List<BillMain> findListforQuery(BillMain billMain);

    List<BillDetailCode> findCodeList(String billNum);

    String getNumByChk(String code);
}